package com.aiwujie.shengmo.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.VipCenterActivity;
import com.aiwujie.shengmo.activity.VipWebActivity;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.http.HttpUrl;
import com.tencent.qcloud.tim.tuikit.live.utils.UIUtil;

/**
 * Created by 290243232 on 2017/6/9.
 */

public class UserIdentityUtils {

    public static void showIdentity(final Context context,final String headurl, final String uid ,String volunteer, String admin , String svipannual, String svip, String vipannual, String Vip ,String bkvip,String blvip, ImageView vip) {
        //  判断显示标识，志愿者>官方管理>年svip>svip>年vip>vip
        vip.setScaleType(ImageView.ScaleType.FIT_XY);

            if ("1".equals(admin)) {
                vip.setVisibility(View.VISIBLE);
                vip.setImageResource(R.drawable.user_manager);
                vip.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Intent intent = new Intent(context, VipWebActivity.class);
//                        intent.putExtra("title", "全职招聘");
//                        intent.putExtra("path", HttpUrl.NetPic()+ HttpUrl.Recruit);
//                        context.startActivity(intent);
                    }
                });
            } else if("1".equals(bkvip)){
                vip.setVisibility(View.VISIBLE);
                vip.setImageResource(R.drawable.user_manager);
                vip.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Intent intent = new Intent(context, VipWebActivity.class);
//                        intent.putExtra("title", "全职招聘");
//                        intent.putExtra("path",HttpUrl.NetPic()+  HttpUrl.Recruit);
//                        context.startActivity(intent);
                    }
                });
            }else if("1".equals(blvip)){
                vip.setVisibility(View.VISIBLE);
                vip.setImageResource(R.drawable.user_manager);
                vip.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Intent intent = new Intent(context, VipWebActivity.class);
//                        intent.putExtra("title", "全职招聘");
//                        intent.putExtra("path", HttpUrl.NetPic()+ HttpUrl.Recruit);
//                        context.startActivity(intent);
                    }
                });
            }else if("1".equals(volunteer)){

                vip.setVisibility(View.VISIBLE);
               // vip.setImageResource(R.mipmap.zhiyuanzhevip);
                vip.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Intent intent = new Intent(context, VipWebActivity.class);
//                        intent.putExtra("title", "志愿者招聘");
//                        intent.putExtra("path", HttpUrl.NetPic()+ HttpUrl.Volunteer);
//                        context.startActivity(intent);
                    }
                });

            }else {
                if (("1").equals(svipannual)) {
                    vip.setVisibility(View.VISIBLE);
                    vip.setImageResource(R.drawable.user_svip_year);
                } else {
                    if (("1").equals(svip)) {
                        vip.setVisibility(View.VISIBLE);
                        vip.setImageResource(R.drawable.user_svip);
                    } else {
                        if ("1".equals(vipannual)) {
                            vip.setVisibility(View.VISIBLE);
                            vip.setImageResource(R.drawable.user_vip_year);
                        } else {
                            if (("1").equals(Vip)) {
                                vip.setVisibility(View.VISIBLE);
                                vip.setImageResource(R.drawable.user_vip);
                            } else {
                                vip.setVisibility(View.INVISIBLE);
                            }
                        }
                    }
                }
                vip.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!MyApp.uid.equals(uid)) {
                            Intent intent = new Intent(context, VipCenterActivity.class);
                            intent.putExtra("headpic", headurl);
                            intent.putExtra("uid", uid);
                            context.startActivity(intent);
                        }
                    }
                });
            }
        }
    //直播-热门列表
    public static void showIdentity(final Context context,final String headurl, final String uid ,String admin , String svipannual, String svip, String vipannual, String Vip , ImageView vip) {
        //  判断显示标识，志愿者>官方管理>年svip>svip>年vip>vip
        vip.setScaleType(ImageView.ScaleType.FIT_XY);

        if ("1".equals(admin)) {
            vip.setVisibility(View.VISIBLE);
            vip.setImageResource(R.drawable.user_manager);
            vip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent intent = new Intent(context, VipWebActivity.class);
//                    intent.putExtra("title", "全职招聘");
//                    intent.putExtra("path", HttpUrl.NetPic()+ HttpUrl.Recruit);
//                    context.startActivity(intent);
                }
            });
        } else {
            if (("1").equals(svipannual)) {
                vip.setVisibility(View.VISIBLE);
                vip.setImageResource(R.drawable.user_svip_year);
            } else {
                if (("1").equals(svip)) {
                    vip.setVisibility(View.VISIBLE);
                    vip.setImageResource(R.drawable.user_svip);
                } else {
                    if ("1".equals(vipannual)) {
                        vip.setVisibility(View.VISIBLE);
                        vip.setImageResource(R.drawable.user_vip_year);
                    } else {
                        if (("1").equals(Vip)) {
                            vip.setVisibility(View.VISIBLE);
                            vip.setImageResource(R.drawable.user_vip);
                        } else {
                            vip.setVisibility(View.GONE);
                        }
                    }
                }
            }
            vip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!MyApp.uid.equals(uid)) {
                        Intent intent = new Intent(context, VipCenterActivity.class);
                        intent.putExtra("headpic", headurl);
                        intent.putExtra("uid", uid);
                        context.startActivity(intent);
                    }
                }
            });
        }
    }

    public static void showIdentity(final Context context, final String uid ,String volunteer, String admin , String svipannual, String svip, String vipannual, String Vip ,String bkvip,String blvip, ImageView vip) {
        //  判断显示标识，志愿者>官方管理>年svip>svip>年vip>vip
        vip.setScaleType(ImageView.ScaleType.FIT_XY);

        if (admin.equals("1")) {
            vip.setVisibility(View.VISIBLE);
            vip.setImageResource(R.drawable.user_manager);
            vip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent intent = new Intent(context, VipWebActivity.class);
//                    intent.putExtra("title", "全职招聘");
//                    intent.putExtra("path", HttpUrl.NetPic()+ HttpUrl.Recruit);
//                    context.startActivity(intent);
                }
            });
        } else if("1".equals(bkvip)){
            vip.setVisibility(View.VISIBLE);
            vip.setImageResource(R.mipmap.guanfangbiaozhigui);
            vip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent intent = new Intent(context, VipWebActivity.class);
//                    intent.putExtra("title", "全职招聘");
//                    intent.putExtra("path",HttpUrl.NetPic()+  HttpUrl.Recruit);
//                    context.startActivity(intent);
                }
            });
        }else if("1".equals(blvip)){
            vip.setVisibility(View.VISIBLE);
            vip.setImageResource(R.drawable.user_manager);
            vip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent intent = new Intent(context, VipWebActivity.class);
//                    intent.putExtra("title", "全职招聘");
//                    intent.putExtra("path", HttpUrl.NetPic()+ HttpUrl.Recruit);
//                    context.startActivity(intent);
                }
            });
        }else if("1".equals(volunteer)){

            vip.setVisibility(View.VISIBLE);
            vip.setImageResource(R.mipmap.zhiyuanzhevip);
            vip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent intent = new Intent(context, VipWebActivity.class);
//                    intent.putExtra("title", "志愿者招聘");
//                    intent.putExtra("path", HttpUrl.NetPic()+ HttpUrl.Volunteer);
//                    context.startActivity(intent);
                }
            });

        }else {
            if (("1").equals(svipannual)) {
                vip.setVisibility(View.VISIBLE);
                vip.setImageResource(R.drawable.user_svip_year);
            } else {
                if (("1").equals(svip)) {
                    vip.setVisibility(View.VISIBLE);
                    vip.setImageResource(R.drawable.user_svip);
                } else {
                    if ("1".equals(vipannual)) {
                        vip.setVisibility(View.VISIBLE);
                        vip.setImageResource(R.drawable.user_vip_year);
                    } else {
                        if (("1").equals(Vip)) {
                            vip.setVisibility(View.VISIBLE);
                            vip.setImageResource(R.drawable.user_vip);
                        } else {
                            vip.setVisibility(View.INVISIBLE);
                        }
                    }
                }
            }
        }
    }


    public static void showIdentity(final Context context, ImageView iv) {
        String vip = (String) SharedPreferencesUtils.getParam(context, "vip", "0");
        String admin = (String) SharedPreferencesUtils.getParam(context, "admin", "0");

    }

    /**
     * 获取vip等级值
     * @param role
     * @param view
     */
    public static void showUserIdentity(Context mContext, int role, ImageView view){
        view.setVisibility(View.VISIBLE);
        switch (role) {
            case 2:
            case 3:
            case 4:
                view.setImageResource(R.drawable.user_manager);
                break;
            case 5:
                break;
            case 6:
                view.setImageResource(R.drawable.user_svip_year);
                break;
            case 7:
                view.setImageResource(R.drawable.user_svip);
                break;
            case 8:
                view.setImageResource(R.drawable.user_vip_year);
                break;
            case 9:
                view.setImageResource(R.drawable.user_vip);
                break;
            default:

                view.setVisibility(View.GONE);
                break;
        }
    }
}
