package com.aiwujie.shengmo.kt.ui.adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.adapter.PunishmentImgAdapter
import com.aiwujie.shengmo.bean.PunishmentBean
import com.aiwujie.shengmo.http.HttpUrl
import com.aiwujie.shengmo.utils.DateUtils
import com.aiwujie.shengmo.utils.GlideImgManager
import com.aiwujie.shengmo.utils.UserIdentityUtils
import com.lzy.ninegrid.ImageInfo
import com.lzy.ninegrid.preview.ImagePreviewActivity
import org.feezu.liuli.timeselector.Utils.TextUtil
import java.io.Serializable

class PunishmentAdapter(var context: Context, var mList: ArrayList<PunishmentBean.DataBean>, var isShowFlag: Boolean) : RecyclerView.Adapter<PunishmentAdapter.PunishmentViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): PunishmentViewHolder {
        return PunishmentViewHolder(LayoutInflater.from(context).inflate(R.layout.item_punishment_publicity, parent,false))
    }

    override fun onBindViewHolder(holder: PunishmentViewHolder?, position: Int) {
        val data: PunishmentBean.DataBean = mList[position]
//          判断显示标识，志愿者>官方管理>年svip>svip>年vip>vip
        //          判断显示标识，志愿者>官方管理>年svip>svip>年vip>vip
        UserIdentityUtils.showIdentity(context, data.head_pic, data.uid, data.is_volunteer, "-1", data.svipannual, data.svip, data.vipannual, data.vip, data.bkvip, data.blvip, holder!!.itemListviewWarningVip)
        if (data.realname == "0") {
            holder!!.itemListviewWarningShiming.visibility = View.GONE
        } else {
            holder!!.itemListviewWarningShiming.visibility = View.VISIBLE
        }

        if (data.sex == "1") {
            holder!!.llLayoutUserNormalInfoSexAge.setBackgroundResource(R.drawable.item_sex_nan_bg)
            holder!!.ivLayoutUserNormalInfoSex.setImageResource(R.mipmap.nan)
        } else if (data.sex == "2") {
            holder!!.llLayoutUserNormalInfoSexAge.setBackgroundResource(R.drawable.item_sex_nv_bg)
            holder!!.ivLayoutUserNormalInfoSex.setImageResource(R.mipmap.nv)
        } else if (data.sex == "3") {
            holder!!.llLayoutUserNormalInfoSexAge.setBackgroundResource(R.drawable.item_sex_san_bg)
            holder!!.ivLayoutUserNormalInfoSex.setImageResource(R.mipmap.san)
        }
        if (data.role == "S") {
            holder!!.tvLayoutUserNormalInfoRole.setBackgroundResource(R.drawable.item_sex_nan_bg)
            holder!!.tvLayoutUserNormalInfoRole.text = "斯"
        } else if (data.role == "M") {
            holder!!.tvLayoutUserNormalInfoRole.setBackgroundResource(R.drawable.item_sex_nv_bg)
            holder!!.tvLayoutUserNormalInfoRole.text = "慕"
        } else if (data.role == "SM") {
            holder!!.tvLayoutUserNormalInfoRole.setBackgroundResource(R.drawable.item_sex_san_bg)
            holder!!.tvLayoutUserNormalInfoRole.text = "双"
        } else if (data.role == "~") {
            holder!!.tvLayoutUserNormalInfoRole.setBackgroundResource(R.drawable.item_sex_lang_bg)
            holder!!.tvLayoutUserNormalInfoRole.text = "~"
        }
        holder!!.tvLayoutUserNormalInfoAge.text = data.age
        if (TextUtil.isEmpty(data.comnum) || "0" == data.comnum) {
            holder!!.tvPunishmentComment.text = "去评论"
        } else {
            holder!!.tvPunishmentComment.text = data.comnum
        }
        if (data.head_pic != HttpUrl.NetPic() || data.head_pic != "") { //"http://59.110.28.150:888/"
            GlideImgManager.glideLoader(context, data.head_pic, R.mipmap.morentouxiang, R.mipmap.morentouxiang, holder!!.itemListviewWarningIcon, 0)
        } else {
            holder!!.itemListviewWarningIcon.setImageResource(R.mipmap.morentouxiang)
        }
        holder!!.itemListviewWarningName.text = data.nickname
        if (data.charm_val_new != "0") {
            holder!!.llLayoutUserNormalInfoCharm.visibility = View.VISIBLE
            holder!!.tvLayoutUserNormalInfoCharm.text = data.charm_val_new
        } else {
            holder!!.llLayoutUserNormalInfoCharm.visibility = View.GONE
        }

        if (data.wealth_val_new != "0") {
            holder!!.llLayoutUserNormalInfoWealth.visibility = View.VISIBLE
            holder!!.tvLayoutUserNormalInfoWealth.text = data.wealth_val_new
        } else {
            holder!!.llLayoutUserNormalInfoWealth.visibility = View.GONE
        }

        if (data.onlinestate == 1) {
            holder!!.ivItemOnline.visibility = View.VISIBLE
        } else {
            holder!!.ivItemOnline.visibility = View.INVISIBLE
        }

        when (data.type) {
            "1" -> holder!!.tvPunishmentType.text = "已禁账号"
            "2" -> holder!!.tvPunishmentType.text = "已禁动态"
            "3" -> holder!!.tvPunishmentType.text = "已禁聊天"
            "4" -> holder!!.tvPunishmentType.text = "已禁资料"
            "66" -> holder!!.tvPunishmentType.text = "禁看直播"
            else -> holder!!.tvPunishmentType.text = "已禁设备"
        }

        when (data.blockingalong) {
            "1" -> holder!!.tvPunishmentTime.text = "1天"
            "3" -> holder!!.tvPunishmentTime.text = "3天"
            "7" -> holder!!.tvPunishmentTime.text = "1周"
            "14" -> holder!!.tvPunishmentTime.text = "2周"
            "30", "31" -> holder!!.tvPunishmentTime.text = "1月"
            else -> holder!!.tvPunishmentTime.text = "永久"
        }
        try {
            holder!!.tvPunishmentDate.text = DateUtils.getParseTime(data.addtime.toLong())
        } catch (e: Exception) {
        }
        holder!!.tvPunishmentReason.text = data.blockreason
        val images = data.image

        if (images == null || images.size == 0) {
            holder!!.rvPunishmentImg.visibility = View.GONE
        } else {
            holder!!.rvPunishmentImg.visibility = View.VISIBLE
            val punishmentImgAdapter = PunishmentImgAdapter(images, context)
            val gridLayoutManager = GridLayoutManager(context, 5)
            holder!!.rvPunishmentImg.layoutManager = gridLayoutManager
            holder!!.rvPunishmentImg.adapter = punishmentImgAdapter
            punishmentImgAdapter.setOnSimpleItemListener { view ->
                val index = holder!!.rvPunishmentImg.getChildAdapterPosition(view)
                val imageInfo: MutableList<ImageInfo> = java.util.ArrayList()
                for (i in images.indices) {
                    val info = ImageInfo()
                    info.setThumbnailUrl(images[i])
                    info.setBigImageUrl(images[i])
                    imageInfo.add(info)
                }
                val intent = Intent(context, ImagePreviewActivity::class.java)
                val bundle = Bundle()
                bundle.putSerializable(ImagePreviewActivity.IMAGE_INFO, imageInfo as Serializable)
                bundle.putInt(ImagePreviewActivity.CURRENT_ITEM, index)
                intent.putExtras(bundle)
                context.startActivity(intent)
            }
        }
        if (isShowFlag) {
            holder.tvPunishmentEdit.visibility = View.VISIBLE
        } else {
            holder.tvPunishmentEdit.visibility = View.GONE
        }

        holder.tvPunishmentEdit.setOnClickListener {
                onPunishmentListener!!.onItemEdit(position)
        }

        holder.llItemPunishment.setOnClickListener {
                onPunishmentListener!!.onItemClick(position)
        }


    }

    interface OnPunishmentListener {
        fun onItemEdit(position: Int)
        fun onItemClick(position: Int)
    }

    var onPunishmentListener: OnPunishmentListener? =null

    override fun getItemCount(): Int {
        return mList.size
    }


    inner class PunishmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var itemListviewWarningIcon: ImageView = itemView.findViewById(R.id.item_listview_warning_icon)

        var itemListviewWarningVip: ImageView = itemView.findViewById(R.id.item_listview_warning_vip)

        var itemListviewWarningName: TextView = itemView.findViewById(R.id.item_listview_warning_name)

        var itemListviewWarningShiming: ImageView = itemView.findViewById(R.id.item_listview_warning_shiming)

        var tvPunishmentType: TextView = itemView.findViewById(R.id.tv_punishment_type)


        var tvPunishmentTime: TextView = itemView.findViewById(R.id.tv_punishment_time)

        var itemListviewWarningBanDynamic: LinearLayout = itemView.findViewById(R.id.item_listview_warning_ban_dynamic)

        var ivLayoutUserNormalInfoSex: ImageView = itemView.findViewById(R.id.iv_layout_user_normal_info_sex)

        var tvLayoutUserNormalInfoAge: TextView = itemView.findViewById(R.id.tv_layout_user_normal_info_age)

        var llLayoutUserNormalInfoSexAge: LinearLayout = itemView.findViewById(R.id.ll_layout_user_normal_info_sex_age)


        var tvLayoutUserNormalInfoRole: TextView = itemView.findViewById(R.id.tv_layout_user_normal_info_role)

        var ivLayoutUserNormalInfoWealth: ImageView = itemView.findViewById(R.id.iv_layout_user_normal_info_wealth)


        var tvLayoutUserNormalInfoWealth: TextView = itemView.findViewById(R.id.tv_layout_user_normal_info_wealth)

        var llLayoutUserNormalInfoWealth: LinearLayout = itemView.findViewById(R.id.ll_layout_user_normal_info_wealth)

        var ivLayoutUserNormalInfoCharm: ImageView = itemView.findViewById(R.id.iv_layout_user_normal_info_charm)


        var tvLayoutUserNormalInfoCharm: TextView = itemView.findViewById(R.id.tv_layout_user_normal_info_charm)

        var llLayoutUserNormalInfoCharm: LinearLayout = itemView.findViewById(R.id.ll_layout_user_normal_info_charm)

        var tvPunishmentDate: TextView = itemView.findViewById(R.id.tv_punishment_date)


        var tvPunishmentEdit: TextView = itemView.findViewById(R.id.tv_punishment_edit)

        var tvPunishmentReason: TextView = itemView.findViewById(R.id.tv_punishment_reason)

        var rvPunishmentImg: RecyclerView = itemView.findViewById(R.id.rv_punishment_img)

        var llItemPunishment: LinearLayout = itemView.findViewById(R.id.ll_item_punishment)

        var tvPunishmentComment: TextView = itemView.findViewById(R.id.tv_punishment_common)

        var ivItemOnline: ImageView = itemView.findViewById(R.id.item_listview_warning_online)
    }
}