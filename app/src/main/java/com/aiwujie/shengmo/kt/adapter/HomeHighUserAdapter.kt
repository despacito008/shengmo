package com.aiwujie.shengmo.kt.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.bean.HighUserBean
import com.aiwujie.shengmo.kt.ui.activity.HighEndUserActivity
import com.aiwujie.shengmo.kt.util.EasySpannableString
import com.aiwujie.shengmo.kt.util.IntentKey
import com.aiwujie.shengmo.utils.GlideImgManager
import com.aiwujie.shengmo.utils.OnSimpleItemListener
import com.tencent.qcloud.tim.tuikit.live.component.common.CircleImageView
import kotlinx.android.synthetic.main.activity_group_liwu_list.view.*
import org.feezu.liuli.timeselector.Utils.TextUtil

class HomeHighUserAdapter(var context: Context, var list: List<HighUserBean>) : RecyclerView.Adapter<HomeHighUserAdapter.HomeHighUserViewHolder>() {



    inner class HomeHighUserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var ivIcon: CircleImageView = itemView.findViewById(R.id.iv_item_high_end_auth_icon)
        var tvName: TextView = itemView.findViewById(R.id.tv_name)
        var tvSexAge: TextView = itemView.findViewById(R.id.tv_layout_user_normal_info_age)
        var tvCity: TextView = itemView.findViewById(R.id.tv_city)
        var ivCity: ImageView = itemView.findViewById(R.id.iv_city)
        var tvDesc: TextView = itemView.findViewById(R.id.tv_item_high_end_auth_desc)
        var tvRecommd: TextView = itemView.findViewById(R.id.tv_item_high_end_auth_recommend)
        var ivSexAge: ImageView = itemView.findViewById(R.id.iv_layout_user_normal_info_sex)
        var llSexAge: LinearLayout = itemView.findViewById(R.id.ll_layout_user_normal_info_sex_age)
        var tvUserRole: TextView = itemView.findViewById(R.id.tv_layout_user_normal_info_role)


        var ivCaichan: ImageView = itemView.findViewById(R.id.iv_caichan)
        var ivJiankang: ImageView = itemView.findViewById(R.id.iv_jiankang)
        var ivXueli: ImageView = itemView.findViewById(R.id.iv_xueli)
        var ivJineng: ImageView = itemView.findViewById(R.id.iv_jineng)
        var ivQita: ImageView = itemView.findViewById(R.id.iv_qita)

        var ivRealId: ImageView = itemView.findViewById(R.id.iv_realId)
        var ivRealName: ImageView = itemView.findViewById(R.id.iv_realName)
        var ivVidoeAuth: ImageView = itemView.findViewById(R.id.iv_vidoeAuth)


        fun display(position: Int) {
            list[position].run {
                if (is_gaussian == "1") {
                    GlideImgManager.glideBlurLoader(context, head_pic, R.mipmap.morentouxiang, R.mipmap.morentouxiang, ivIcon)
                } else {
                    GlideImgManager.glideLoader(context, head_pic, R.mipmap.morentouxiang, R.mipmap.morentouxiang, ivIcon)
                }

                when (sex) {
                    "1" -> {
                        ivSexAge.setImageResource(R.drawable.nan)
                        llSexAge.setBackgroundResource(R.drawable.bg_user_info_sex_boy)
                    }
                    "2" -> {
                        ivSexAge.setImageResource(R.drawable.nv)
                        llSexAge.setBackgroundResource(R.drawable.bg_user_info_sex_girl)
                    }
                    else -> {
                        ivSexAge.setImageResource(R.drawable.san)
                        llSexAge.setBackgroundResource(R.drawable.bg_user_info_sex_cdts)
                    }
                }

                if ("S" == role_string) {
                    tvUserRole.setBackgroundResource(R.drawable.bg_user_info_sex_boy)
                    tvUserRole.text = "斯"
                } else if ("M" == role_string) {
                    tvUserRole.setBackgroundResource(R.drawable.bg_user_info_sex_girl)
                    tvUserRole.text = "慕"
                } else if ("SM" ==role_string) {
                    tvUserRole.setBackgroundResource(R.drawable.bg_user_info_sex_cdts)
                    tvUserRole.text = "双"
                } else {
                    tvUserRole.setBackgroundResource(R.drawable.bg_user_info_sex_other)
                    tvUserRole.text=role_string
                }



                tvName.text = serial_id
                tvSexAge.text = if (age.isEmpty()) "0" else age
                if (location_city_switch == "1"){
                    tvCity.visibility =View.GONE
                    ivCity.visibility =View.VISIBLE
                }else {
                    if (TextUtil.isEmpty(province)  && TextUtil.isEmpty(city)){
                        tvCity.visibility =View.GONE
                        ivCity.visibility =View.VISIBLE
                    }else {
                        tvCity.visibility =View.VISIBLE
                        ivCity.visibility =View.GONE
                        tvCity.text = if (province == "" || province == city) {
                            city
                        } else {
                            "$province  $city"
                        }
                    }

                }

                if (!TextUtil.isEmpty(top_desc)) {
                    tvDesc.visibility = View.VISIBLE
                    val content = EasySpannableString(context, "内心独白：$top_desc".replace(" ","").replace("\\/n",""))
                            .first("内心独白：").textColor( R.color.color_high_title)
                    tvDesc.text = content
                } else {
                    tvDesc.visibility=View.GONE
                }

                if (!TextUtil.isEmpty(top_red_desc)  ) {
                    tvRecommd.visibility=View.VISIBLE
                    val content = EasySpannableString(context, "红娘荐语：$top_red_desc")
                            .first("红娘荐语：").textColor( R.color.color_high_title)
                    tvRecommd.text = content
                } else {
                    tvRecommd.visibility=View.GONE
                }




                if (realname == "1") {
                    ivRealId.visibility = View.VISIBLE
                } else {
                    ivRealId.visibility = View.GONE
                }
                if (video_auth_status == "1") {
                    ivVidoeAuth.visibility = View.VISIBLE
                } else {
                    ivVidoeAuth.visibility = View.GONE
                }
                if (realids == "1") {
                    ivRealName.visibility = View.VISIBLE
                } else {
                    ivRealName.visibility = View.GONE
                }
                if (top_cc_status == "1") {
                    ivCaichan.visibility = View.VISIBLE
                } else {
                    ivCaichan.visibility = View.GONE
                }
                if (top_jk_status == "1") {
                    ivJiankang.visibility = View.VISIBLE
                } else {
                    ivJiankang.visibility = View.GONE
                }
                if (top_xl_status == "1") {
                    ivXueli.visibility = View.VISIBLE
                } else {
                    ivXueli.visibility = View.GONE
                }
                if (top_jn_status == "1") {
                    ivJineng.visibility = View.VISIBLE
                } else {
                    ivJineng.visibility = View.GONE
                }
                if (top_qt_status == "1") {
                    ivQita.visibility = View.VISIBLE
                } else {
                    ivQita.visibility = View.GONE
                }


                itemView.setOnClickListener {
                    onSimpleItemListener?.onItemListener(position)
                }

            }
        }
    }


    private var onSimpleItemListener: OnSimpleItemListener? = null

    fun setOnSimpleItemListener(onSimpleItemListener: OnSimpleItemListener?) {
        this.onSimpleItemListener = onSimpleItemListener
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): HomeHighUserViewHolder {
        return HomeHighUserViewHolder(LayoutInflater.from(context).inflate(R.layout.app_item_high_end_auth_user, parent, false))
    }

    override fun getItemCount(): Int {

        return list.size

    }

    override fun onBindViewHolder(holder: HomeHighUserViewHolder?, position: Int) {
        holder?.display(position)
    }
}