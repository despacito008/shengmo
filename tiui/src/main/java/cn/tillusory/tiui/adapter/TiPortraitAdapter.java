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
import cn.tillusory.sdk.bean.TiPortrait;
import cn.tillusory.sdk.bean.TiSticker;
import cn.tillusory.sdk.common.TiUtils;
import cn.tillusory.tiui.R;
import cn.tillusory.tiui.model.TiSelectedPosition;

/**
 * Created by Anko on 2018/5/24.
 * Copyright (c) 2018-2020 拓幻科技 - tillusory.cn. All rights reserved.
 */
public class TiPortraitAdapter extends RecyclerView.Adapter<TiStickerViewHolder> {

    private final int ITEM_TYPE_ONE = 1;
    private final int ITEM_TYPE_TWO = 2;

    private int selectedPosition = TiSelectedPosition.POSITION_PORTRAIT;

    private List<TiPortrait> portraitList;
    private TiSDKManager tiSDKManager;

    private Handler handler = new Handler();

    private Map<String, String> downloadingStickers = new ConcurrentHashMap<>();

    public TiPortraitAdapter(List<TiPortrait> portraitList, TiSDKManager tiSDKManager) {
        this.portraitList = portraitList;
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
        final TiPortrait tiPortrait = portraitList.get(holder.getAdapterPosition());

        if (selectedPosition == position) {
            holder.itemView.setSelected(true);
        } else {
            holder.itemView.setSelected(false);
        }

        //显示封面
        if (tiPortrait == TiPortrait.NO_Portrait) {
            holder.thumbIV.setImageResource(R.drawable.ic_ti_none_sticker);
        } else {
            Glide.with(holder.itemView.getContext())
                    .load(portraitList.get(position).getThumb())
                    .into(holder.thumbIV);
        }

        //判断是否已经下载
        if (tiPortrait.isDownloaded()) {
            holder.downloadIV.setVisibility(View.GONE);
            holder.loadingIV.setVisibility(View.GONE);
            holder.stopLoadingAnimation();
        } else {
            //判断是否正在下载，如果正在下载，则显示加载动画
            if (downloadingStickers.containsKey(tiPortrait.getName())) {
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
                if (!tiPortrait.isDownloaded()) {

                    //如果已经在下载了，则不操作
                    if (downloadingStickers.containsKey(tiPortrait.getName())) {
                        return;
                    }

                    new DownloadTask.Builder(tiPortrait.getUrl(), new File(TiSDK.getPortraitPath(holder.itemView.getContext())))
                            .setMinIntervalMillisCallbackProcess(30)
                            .setConnectionCount(1)
                            .build()
                            .enqueue(new DownloadListener2() {
                                @Override
                                public void taskStart(@NonNull DownloadTask task) {
                                    downloadingStickers.put(tiPortrait.getName(), tiPortrait.getUrl());
                                    notifyDataSetChanged();
                                }

                                @Override
                                public void taskEnd(@NonNull final DownloadTask task, @NonNull EndCause cause, @Nullable final Exception realCause) {
                                    downloadingStickers.remove(tiPortrait.getName());

                                    if (cause == EndCause.COMPLETED) {
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                File targetDir = new File(TiSDK.getPortraitPath(holder.itemView.getContext()));
                                                File file = task.getFile();
                                                try {
                                                    //解压到贴纸目录
                                                    TiUtils.unzip(file, targetDir);
                                                    if (file != null) {
                                                        file.delete();
                                                    }

                                                    //修改内存与文件
                                                    tiPortrait.setDownloaded(true);
                                                    tiPortrait.portraitDownload(holder.itemView.getContext());

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
                                        notifyDataSetChanged();
                                        if (realCause != null) {
                                            Toast.makeText(holder.itemView.getContext(), realCause.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            });

                } else {
                    if (holder.getAdapterPosition() == RecyclerView.NO_POSITION) {
                        return;
                    }

                    //如果已经下载了，则让其生效
                    tiSDKManager.setPortrait(tiPortrait.getName());

                    //切换选中背景
                    int lastPosition = selectedPosition;
                    selectedPosition = holder.getAdapterPosition();
                    TiSelectedPosition.POSITION_PORTRAIT = selectedPosition;
                    notifyItemChanged(selectedPosition);
                    notifyItemChanged(lastPosition);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return portraitList == null ? 0 : portraitList.size();
    }
}
