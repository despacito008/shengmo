package com.aiwujie.shengmo.recycleradapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.AccountActivity;
import com.aiwujie.shengmo.activity.HistoryAddressActivity;
import com.aiwujie.shengmo.activity.user.UserInfoActivity;
import com.aiwujie.shengmo.bean.AccountDeviceData;
import com.aiwujie.shengmo.kt.ui.activity.UserInviteListActivity;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.view.headerviewadapter.adapter.HeaderViewAdapter;


import org.feezu.liuli.timeselector.Utils.TextUtil;

import static android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;

/**
 * @author：zq 2021/4/12 15:36
 * 邮箱：80776234@qq.com
 */
public class AccountDeviceAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
   private AccountDeviceData.Data accountDeviceData;
    private Context context;
    private  String uid;
    private final int TYPE_HEADER=1001;
    private final LayoutInflater mLayoutInflater;
    public AccountDeviceAdapter( Context context,AccountDeviceData.Data accountDeviceData,String uid) {
        this.accountDeviceData = accountDeviceData;
        this.context = context;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.uid=uid;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType==TYPE_HEADER){
         View headView =  mLayoutInflater.inflate(R.layout.header_account_device,parent,false);
        return new HeaderViewHolder(headView);
        }
        View view =   mLayoutInflater.inflate(R.layout.item_account_device,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder){
            if (accountDeviceData.getDevice_info().size()>0){
               if (position-1==0){
                   ((ViewHolder) holder).tvDeviceNumberStr.setText("当前设备号：");
               }else {
                   ((ViewHolder) holder).tvDeviceNumberStr.setText("历史设备号：");
               }
               ((ViewHolder) holder).tvDeviceNumber.setText(accountDeviceData.getDevice_info().get(position-1).getDevice());
               UserInfoAdapter userInfoAdapter =new UserInfoAdapter(context,accountDeviceData.getDevice_info().get(position-1).getDevice_user());
               ((ViewHolder) holder).rlvUserInfo.setLayoutManager(new LinearLayoutManager(context));
                ((ViewHolder) holder).rlvUserInfo.setAdapter(userInfoAdapter);
            }

        }else if (holder instanceof HeaderViewHolder){
            ((HeaderViewHolder)holder).tvAccount.setText(accountDeviceData.getAccout_number()  + "---" + accountDeviceData.getUid());
            ((HeaderViewHolder)holder).tvPhone.setText(accountDeviceData.getMobile());
            ((HeaderViewHolder)holder).tvEmail.setText(accountDeviceData.getEmail());
            ((HeaderViewHolder)holder).tvType.setText(accountDeviceData.getNew_device_brand());
            ((HeaderViewHolder)holder).tvPhoneModel.setText(accountDeviceData.getNew_device_version());
            ((HeaderViewHolder)holder).tvPhoneVersion.setText(accountDeviceData.getNew_device_appversion());
            ((HeaderViewHolder)holder).tvTime.setText(accountDeviceData.getNew_login_time());
            if (TextUtil.isEmpty(accountDeviceData.getInvite_nickname())) {
                ((HeaderViewHolder)holder).llAccountInvite.setVisibility(View.GONE);
            } else {
                ((HeaderViewHolder)holder).llAccountInvite.setVisibility(View.VISIBLE);
                ((HeaderViewHolder)holder).tvAccountInvite.setText(accountDeviceData.getInvite_nickname());
                ((HeaderViewHolder)holder).tvAccountInvite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, UserInfoActivity.class);
                        intent.putExtra("uid",accountDeviceData.getInvite_uid());
                        context.startActivity(intent);
                    }
                });
            }
            if (accountDeviceData.getLocation_info()!=null){
                ((HeaderViewHolder)holder).tvEnterTime.setText(accountDeviceData.getLocation_info().getAddtime());
                ((HeaderViewHolder)holder).tvOutTime.setText(accountDeviceData.getLocation_info().getEndtime());

            }else {
                ((HeaderViewHolder)holder).tvEnterTime.setText("");
                ((HeaderViewHolder)holder).tvOutTime.setText("");
            }

            SpannableStringBuilder spannableStringBuilder=   new SpannableStringBuilder();
            spannableStringBuilder.append("地址："+(TextUtils.isEmpty(accountDeviceData.getLocation_info().getAddr())?"暂无":accountDeviceData.getLocation_info().getAddr()));
            ClickableSpan clickableSpan =new ClickableSpan() {
                @Override
                public void onClick(@NonNull View widget) {
                  Intent  intent  = new Intent(context, HistoryAddressActivity.class);
                    intent.putExtra("uid", uid);
                    context. startActivity(intent);
                }
            };
            spannableStringBuilder.setSpan(clickableSpan,3,spannableStringBuilder.length(),SPAN_EXCLUSIVE_EXCLUSIVE);
            ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#b73acb"));
            spannableStringBuilder.setSpan(foregroundColorSpan,3,spannableStringBuilder.length(),SPAN_EXCLUSIVE_EXCLUSIVE);
            ((HeaderViewHolder)holder).tvAddress.setMovementMethod(LinkMovementMethod.getInstance());
            ((HeaderViewHolder)holder).tvAddress.setText(spannableStringBuilder);
            ((HeaderViewHolder)holder).tvPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    Uri data = Uri.parse("tel:" + accountDeviceData.getMobile());
                    intent.setData(data);
                    context.startActivity(intent);
                }
            });
            if (TextUtil.isEmpty(accountDeviceData.getInvite_count()) || "0".equals(accountDeviceData.getInvite_count())) {
                ((HeaderViewHolder)holder).llAccountInviteUser.setVisibility(View.GONE);
            } else {
                ((HeaderViewHolder)holder).llAccountInviteUser.setVisibility(View.VISIBLE);
                ((HeaderViewHolder)holder).tvAccountInviteNum.setText(accountDeviceData.getInvite_count() + "人");
                ((HeaderViewHolder)holder).llAccountInviteUser.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent  intent  = new Intent(context, UserInviteListActivity.class);
                        intent.putExtra("uid", uid);
                        context.startActivity(intent);
                    }
                });
            }
        }
    }


    @Override
    public int getItemCount() {
        if (accountDeviceData.getDevice_info()!=null){
        return accountDeviceData.getDevice_info().size()+1;

        }else {
            return 1;

        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position==0)
        {
        return TYPE_HEADER;
        }
        return super.getItemViewType(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private  TextView tvDeviceNumberStr;
        private  TextView tvDeviceNumber;
        private  RecyclerView rlvUserInfo;

        public ViewHolder(View itemView) {
            super(itemView);
            tvDeviceNumberStr = itemView.findViewById(R.id.tv_device_number_str);
            tvDeviceNumber = itemView.findViewById(R.id.tv_device_number);
            rlvUserInfo = itemView.findViewById(R.id.rlv_user_info);

        }
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder{

        private  TextView tvAccount,tvPhone,tvEmail,tvPhoneModel,tvPhoneVersion,tvAddress,tvEnterTime,tvOutTime,tvType,tvTime;
        private LinearLayout llAccountInvite,llAccountInviteUser;
        private TextView tvAccountInvite,tvAccountInviteNum;
        public HeaderViewHolder(View itemView) {
            super(itemView);
            tvAccount = itemView.findViewById(R.id.tv_account);
            tvPhone = itemView.findViewById(R.id.tv_phone);
            tvEmail = itemView.findViewById(R.id.tv_email);
            tvType = itemView.findViewById(R.id.tv_type);
            tvPhoneModel = itemView.findViewById(R.id.tv_phone_model);
            tvPhoneVersion = itemView.findViewById(R.id.tv_phone_version);
            tvAddress = itemView.findViewById(R.id.tv_address);
            tvEnterTime = itemView.findViewById(R.id.tv_enter_time);
            tvOutTime = itemView.findViewById(R.id.tv_out_time);
            tvTime = itemView.findViewById(R.id.tv_time);
            llAccountInvite = itemView.findViewById(R.id.ll_account_invite);
            tvAccountInvite = itemView.findViewById(R.id.tv_account_invite);
            llAccountInviteUser = itemView.findViewById(R.id.ll_account_invite_user);
            tvAccountInviteNum = itemView.findViewById(R.id.tv_account_invite_num);
        }
    }}
