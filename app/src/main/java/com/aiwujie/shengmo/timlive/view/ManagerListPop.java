package com.aiwujie.shengmo.timlive.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.net.HttpListener;
import com.aiwujie.shengmo.net.LiveHttpHelper;
import com.aiwujie.shengmo.timlive.bean.ChatRoom;
import com.aiwujie.shengmo.timlive.bean.ManagerList;
import com.aiwujie.shengmo.utils.GlideImgManager;
import com.aiwujie.shengmo.utils.GsonUtil;
import com.aiwujie.shengmo.utils.LogUtil;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.utils.UserIdentityUtils;

import org.feezu.liuli.timeselector.Utils.TextUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ManagerListPop extends PopupWindow {
    private final ManagerList managerList;
    private String TAG = ManagerListPop.this.getClass().getSimpleName();
    private Context context;
    private View mMenuView;
    private TextView tvTitle;
    private RadioGroup rg;
    public View getmMenuView() { return mMenuView; }

    public ManagerListPop(@NonNull Context context, ManagerList managerList) {
        super(context);
        this.context = context;
        this.managerList = managerList;
        // 设置SelectPicPopupWindow的View
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.pop_live_manager_list, null);
        setContentView(mMenuView);
        setOutsideTouchable(true);
        tvTitle = mMenuView.findViewById(R.id.tv_manager_title);
        RecyclerView mRecyclerView = mMenuView.findViewById(R.id.recycler_label);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));    //线性布局

        mRecyclerView.setAdapter(new MyAdapter(context,managerList, tvTitle,new MyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {

            }
        }));
    }

    public static class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        private static ManagerList managerList;
        private final LayoutInflater inflater;
        private final Context context;
        private final TextView tvTitle;

        public MyAdapter(Context context, ManagerList managerList,TextView tvTitle,OnItemClickListener listener) {
            this.context = context;
            this.listener = listener;
            this.tvTitle = tvTitle;
            this.managerList = managerList;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.live_manager_list_item_layout, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int position) {
            initData(viewHolder,position,managerList.getData());
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onItemClick(viewHolder.itemView, viewHolder.getAdapterPosition());
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return managerList.getData().size();
        }

        private OnItemClickListener listener;

        public void setOnItemClickListener(OnItemClickListener listener) {
            this.listener = listener;
        }

        public interface OnItemClickListener {
            void onItemClick(View itemView, int position);
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.iv_item_live_user_icon)
            ImageView iv_icon;
            @BindView(R.id.iv_item_live_user_identity)
            ImageView itemIdentityIcon;
            @BindView(R.id.tv_item_live_user_name)
            TextView tv_name;
            @BindView(R.id.tv_item_live_user_address)
            TextView tv_address;
            @BindView(R.id.tv_item_live_user_info)
            TextView tv_info;
            @BindView(R.id.ll_item_live_control_status)
            LinearLayout ll_control_status;

            ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }
        }
        private void initData(final ViewHolder viewHolder, int position, final List<ManagerList.DataBean> managerList) {
            if(managerList == null) return;
            final ManagerList.DataBean control = managerList.get(position);
            if(control == null) return;
            GlideImgManager.glideLoader(context, control.getHead_pic(), R.mipmap.morentouxiang, R.mipmap.morentouxiang, viewHolder.iv_icon, 0);

            //  判断显示标识，志愿者>官方管理>年svip>svip>年vip>vip

            UserIdentityUtils.showIdentity(context, control.getHead_pic(), control.getUid(), control.getIs_admin(), control.getSvipannual(), control.getSvip(), control.getVipannual(), control.getVip(), viewHolder.itemIdentityIcon);

            viewHolder.tv_name.setText(control.getNickname());
            viewHolder.tv_address.setText(MyApp.address);
            StringBuffer info = new StringBuffer();
            if(!TextUtil.isEmpty(control.getAge()) ){
                info.append(control.getAge()).append("\t").append("|").append("\t");
            }
            if(!TextUtil.isEmpty(managerList.get(position).getSex()) ){
                info.append(control.getSex()).append("\t").append("|").append("\t");
            }
            if(!TextUtil.isEmpty(managerList.get(position).getRole()) ){
                info.append(control.getRole()).append("\t").append("|").append("\t");
            }
            if(!TextUtil.isEmpty(info.toString())){
                viewHolder.tv_info.setText(info.toString());
            } else {
                viewHolder.tv_info.setVisibility(View.GONE);
            }
            viewHolder.ll_control_status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LiveHttpHelper.getInstance().setChatRoomAdmin("2", control.getUid(),"", new HttpListener() {
                        @Override
                        public void onSuccess(String data) {
                            LogUtil.d(data);
                            ChatRoom chatRoom = GsonUtil.GsonToBean(data, ChatRoom.class);
                            if(chatRoom.getRetcode() == 2000){
                                ToastUtil.show(context,chatRoom.getMsg());
                                MyAdapter.managerList.getData().remove(control);
                                notifyDataSetChanged();
                                if(managerList.size() == 0){
                                    tvTitle.setVisibility(View.GONE);
                                }else {
                                    tvTitle.setVisibility(View.VISIBLE);
                                }
                                if(onControlListener!=null){
                                    onControlListener.onControlSuccess(control);
                                }
                            }
                        }

                        @Override
                        public void onFail(String msg) {
                            LogUtil.d(msg);
                            if(onControlListener!=null){
                                onControlListener.onControlFailed(control);
                            }
                        }
                    });
                }
            });
        }
    }

    public void setOnControlListener(ManagerListPop.onControlListener onControlListener) {
        this.onControlListener = onControlListener;
    }

    private static onControlListener onControlListener;

    public interface onControlListener{
        void onControlSuccess(ManagerList.DataBean control);
        void onControlFailed(ManagerList.DataBean control);
    }
}
