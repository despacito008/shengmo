package cn.tillusory.tiui.adapter;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hwangjr.rxbus.RxBus;
import com.liulishuo.okdownload.DownloadTask;
import com.liulishuo.okdownload.core.cause.EndCause;
import com.liulishuo.okdownload.core.dispatcher.DownloadDispatcher;
import com.liulishuo.okdownload.core.listener.DownloadListener2;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import cn.tillusory.sdk.TiSDK;
import cn.tillusory.sdk.TiSDKManager;
import cn.tillusory.sdk.bean.TiGesture;
import cn.tillusory.sdk.common.TiUtils;
import cn.tillusory.tiui.R;
import cn.tillusory.tiui.model.RxBusAction;
import cn.tillusory.tiui.model.TiSelectedPosition;

/**
 * Created by Anko.
 * Copyright (c) 2020 拓幻科技 - tillusory.cn. All rights reserved.
 */
public class TiGestureAdapter extends RecyclerView.Adapter<TiStickerViewHolder> {

    private final int ITEM_TYPE_ONE = 1;
    private final int ITEM_TYPE_TWO = 2;

    private int selectedPosition = TiSelectedPosition.POSITION_GESTURE;

    private List<TiGesture> gestureList;
    private TiSDKManager tiSDKManager;

    private Handler handler = new Handler();

    private Map<String, String> downloadingInteractions = new ConcurrentHashMap<>();

    public TiGestureAdapter(List<TiGesture> gestureList, TiSDKManager tiSDKManager) {
        this.gestureList = gestureList;
        this.tiSDKManager = tiSDKManager;

        DownloadDispatcher.setMaxParallelRunningCount(5);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ITEM_TYPE_ONE;
        } else {
            return ITEM_TYPE_TWO;
        }
    }

    @NonNull
    @Override
    public TiStickerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == ITEM_TYPE_ONE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ti_sticker_one, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ti_sticker, parent, false);
        }
        return new TiStickerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TiStickerViewHolder holder, int position) {
        final TiGesture tiGesture = gestureList.get(holder.getAdapterPosition());

        if (selectedPosition == position) {
            holder.itemView.setSelected(true);
        } else {
            holder.itemView.setSelected(false);
        }

        //显示封面
        if (tiGesture == TiGesture.NO_Gesture) {
            holder.thumbIV.setImageResource(R.drawable.ic_ti_none_sticker);
        } else {
            Glide.with(holder.itemView.getContext())
                    .load(gestureList.get(position).getThumb())
                    .into(holder.thumbIV);
        }

        //判断是否已经下载
        if (tiGesture.isDownloaded()) {
            holder.downloadIV.setVisibility(View.GONE);
            holder.loadingIV.setVisibility(View.GONE);
            holder.stopLoadingAnimation();
        } else {
            //判断是否正在下载，如果正在下载，则显示加载动画
            if (downloadingInteractions.containsKey(tiGesture.getName())) {
                holder.downloadIV.setVisibility(View.GONE);
                holder.loadingIV.setVisibility(View.VISIBLE);
                holder.startLoadingAnimation();
            } else {
                holder.downloadIV.setVisibility(View.VISIBLE);
                holder.loadingIV.setVisibility(View.GONE);
                holder.stopLoadingAnimation();
            }
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //如果没有下载，则开始下载到本地
                if (!tiGesture.isDownloaded()) {

                    //如果已经在下载了，则不操作
                    if (downloadingInteractions.containsKey(tiGesture.getName())) {
                        return;
                    }

                    new DownloadTask.Builder(tiGesture.getUrl(), new File(TiSDK.getGesturePath(holder.itemView.getContext())))
                            .setMinIntervalMillisCallbackProcess(30)
                            .setConnectionCount(1)
                            .build()
                            .enqueue(new DownloadListener2() {
                                @Override
                                public void taskStart(@NonNull DownloadTask task) {
                                    downloadingInteractions.put(tiGesture.getName(), tiGesture.getUrl());
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            notifyDataSetChanged();
                                        }
                                    });
                                }

                                @Override
                                public void taskEnd(@NonNull final DownloadTask task, @NonNull EndCause cause, @Nullable final Exception realCause) {
                                    downloadingInteractions.remove(tiGesture.getName());

                                    if (cause == EndCause.COMPLETED) {
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                File targetDir = new File(TiSDK.getGesturePath(holder.itemView.getContext()));
                                                File file = task.getFile();
                                                try {
                                                    //解压到互动贴纸目录
                                                    TiUtils.unzip(file, targetDir);
                                                    if (file != null) {
                                                        file.delete();
                                                    }

                                                    //修改内存与文件
                                                    tiGesture.setDownloaded(true);
                                                    tiGesture.gestureDownload(holder.itemView.getContext());

                                                    handler.post(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            notifyDataSetChanged();
                                                        }
                                                    });

                                                } catch (Exception e) {
                                                    if (file != null) {
                                                        file.delete();
                                                    }
                                                }
                                            }
                                        }).start();

                                    } else {
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                notifyDataSetChanged();
                                                if (realCause != null) {
                                                    Toast.makeText(holder.itemView.getContext(), realCause.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                }
                            });

                } else {
                    if (holder.getAdapterPosition() == RecyclerView.NO_POSITION) {
                        return;
                    }

                    //如果已经下载了，则生效
                    tiSDKManager.setGesture(tiGesture.getName());

                    //切换选中背景
                    int lastPosition = selectedPosition;
                    selectedPosition = holder.getAdapterPosition();
                    TiSelectedPosition.POSITION_GESTURE = selectedPosition;
                    notifyItemChanged(selectedPosition);
                    notifyItemChanged(lastPosition);

                    RxBus.get().post(RxBusAction.ACTION_SHOW_GESTURE, gestureList.get(selectedPosition).getHint());
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return gestureList == null ? 0 : gestureList.size();
    }
}
