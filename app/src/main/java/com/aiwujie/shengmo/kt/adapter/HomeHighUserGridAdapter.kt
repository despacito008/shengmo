package com.aiwujie.shengmo.kt.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.adapter.GetOutOfLineAdapter
import com.aiwujie.shengmo.bean.HighUserBean
import com.aiwujie.shengmo.utils.DensityUtil
import com.aiwujie.shengmo.utils.GlideImgManager
import com.aiwujie.shengmo.utils.OnSimpleItemListener
import com.tencent.liteav.demo.beauty.utils.ResourceUtils
import org.feezu.liuli.timeselector.Utils.TextUtil

class HomeHighUserGridAdapter(var  context: Context, var  list:List<HighUserBean>) :RecyclerView.Adapter<HomeHighUserGridAdapter.HomeHighUserGridViewHolder>() {

    inner class HomeHighUserGridViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView){

        var ivCorver :ImageView= itemView.findViewById(R.id.iv_corver)
        var tvName :TextView= itemView.findViewById(R.id.tv_name)
        var tvSexAge: TextView = itemView.findViewById(R.id.tv_user_info_sex_and_age)
        var tvUserRole: TextView = itemView.findViewById(R.id.tv_layout_user_normal_info_role)
        var tvCity: TextView = itemView.findViewById(R.id.tv_city)
        var ivCity: ImageView = itemView.findViewById(R.id.iv_city)
        var tvDesc: TextView = itemView.findViewById(R.id.tv_desc)

        var ivCaichan: ImageView = itemView.findViewById(R.id.iv_caichan)
        var ivJiankang: ImageView = itemView.findViewById(R.id.iv_jiankang)
        var ivXueli: ImageView = itemView.findViewById(R.id.iv_xueli)
        var ivJineng: ImageView = itemView.findViewById(R.id.iv_jineng)
        var ivQita: ImageView = itemView.findViewById(R.id.iv_qita)

        var ivRealId: ImageView = itemView.findViewById(R.id.iv_realId)
        var ivRealName: ImageView = itemView.findViewById(R.id.iv_realName)
        var ivVidoeAuth: ImageView = itemView.findViewById(R.id.iv_vidoeAuth)



        fun  display(position: Int){
            list[position].run {
//                GlideImgManager.glideLoader(context, head_pic, R.mipmap.morentouxiang, R.mipmap.morentouxiang, ivCorver)
                if (is_gaussian == "1"){
                    GlideImgManager.glideBlurLoader(context, head_pic, R.mipmap.default_error, R.mipmap.default_error, ivCorver)
                }else {
                    GlideImgManager.glideLoader(context, head_pic, R.mipmap.default_error, R.mipmap.default_error, ivCorver)
                }
                when (sex) {
                    "1" -> {
                        val drawable = ContextCompat.getDrawable(context, R.mipmap.nan)
                        drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
                        tvSexAge.setCompoundDrawables(drawable, null, null, null)
                        tvSexAge.setBackgroundResource(R.drawable.bg_user_info_sex_boy)
                    }
                    "2" -> {
                        val drawable = ContextCompat.getDrawable(context, R.mipmap.nv)
                        drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
                        tvSexAge.setCompoundDrawables(drawable, null, null, null)
                        tvSexAge.setBackgroundResource(R.drawable.bg_user_info_sex_girl)
                    }
                    else -> {
                        val drawable = ContextCompat.getDrawable(context, R.mipmap.san)
                        drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
                        tvSexAge.setCompoundDrawables(drawable, null, null, null)
                        tvSexAge.setBackgroundResource(R.drawable.bg_user_info_sex_cdts)
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
                tvSexAge.text = age
                tvDesc.text = top_desc
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
                if (realname == "1"){
                    ivRealId .visibility =View.VISIBLE
                }else {
                    ivRealId .visibility =View.GONE
                }
                if (video_auth_status == "1"){
                    ivVidoeAuth .visibility =View.VISIBLE
                }else {
                    ivVidoeAuth .visibility =View.GONE
                }
                if (realids == "1"){
                    ivRealName .visibility =View.VISIBLE
                }else {
                    ivRealName .visibility =View.GONE
                }
                if (top_cc_status == "1"){
                    ivCaichan .visibility =View.VISIBLE
                }else {
                    ivCaichan .visibility =View.GONE
                }
                if (top_jk_status == "1"){
                    ivJiankang .visibility =View.VISIBLE
                }else {
                    ivJiankang .visibility =View.GONE
                }
                if (top_xl_status == "1"){
                    ivXueli .visibility =View.VISIBLE
                }else {
                    ivXueli .visibility =View.GONE
                }
                if (top_jn_status == "1"){
                    ivJineng .visibility =View.VISIBLE
                }else {
                    ivJineng .visibility =View.GONE
                }
                if (top_qt_status == "1"){
                    ivQita .visibility =View.VISIBLE
                }else {
                    ivQita .visibility =View.GONE
                }


                itemView.setOnClickListener {
                    onSimpleItemListener?.onItemListener(position)
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): HomeHighUserGridViewHolder {
        return  HomeHighUserGridViewHolder(LayoutInflater.from(context).inflate(R.layout.app_item_hight_grid_user,parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: HomeHighUserGridViewHolder?, position: Int) {
            holder?.display(position)
    }

    private var onSimpleItemListener: OnSimpleItemListener? = null

    fun setOnSimpleItemListener(onSimpleItemListener: OnSimpleItemListener?) {

        this.onSimpleItemListener = onSimpleItemListener
    }

}