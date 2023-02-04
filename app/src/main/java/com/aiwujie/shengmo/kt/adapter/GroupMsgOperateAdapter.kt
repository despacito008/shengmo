package com.aiwujie.shengmo.kt.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.activity.PesonInfoActivity
import com.aiwujie.shengmo.bean.GroupOperationData
import com.aiwujie.shengmo.net.HttpCodeListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.GlideImgManager
import com.aiwujie.shengmo.utils.OnSimpleItemListener
import com.aiwujie.shengmo.view.RefuseJoinGroupPop
import org.feezu.liuli.timeselector.Utils.TextUtil

class GroupMsgOperateAdapter(private val context: Context, private val operateList: List<GroupOperationData.DataBean>) : RecyclerView.Adapter<GroupMsgOperateAdapter.GroupMsgOperateHolder>() {

    inner class GroupMsgOperateHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvName: TextView = itemView.findViewById(R.id.tv_item_group_msg_operate_name)
        var tvInfo: TextView = itemView.findViewById(R.id.tv_item_group_msg_operate_info)
        var tvTime: TextView = itemView.findViewById(R.id.tv_item_group_msg_operate_time)
        var tvAgree: TextView = itemView.findViewById(R.id.tv_item_group_msg_operate_agree)
        var tvRefuse: TextView = itemView.findViewById(R.id.tv_item_group_msg_operate_refuse)
        var tvDone: TextView = itemView.findViewById(R.id.tv_item_group_msg_operate_done)
        var tvPerson: TextView = itemView.findViewById(R.id.tv_item_group_msg_operate_person)
        var llOperate: LinearLayout = itemView.findViewById(R.id.ll_item_group_msg_operate_ing)
        var llDone: LinearLayout = itemView.findViewById(R.id.ll_item_group_msg_operate_done)
        var ivIcon: ImageView = itemView.findViewById(R.id.iv_item_group_msg_operate_icon)

        fun setData(position: Int) {
            tvInfo.visibility = View.VISIBLE
            tvTime.visibility = View.VISIBLE
            tvName.text = operateList[position].content
            tvInfo.text = "验证消息: ${if (TextUtil.isEmpty(operateList[position].msg)) "未填写" else operateList[position].msg}"
            tvTime.text = operateList[position].operatortime
            GlideImgManager.glideLoader(context, operateList[position].head_pic, R.mipmap.morentouxiang, R.mipmap.morentouxiang, ivIcon, 0)
            when (operateList[position].state) {
                "0" -> {
                    llOperate.visibility = View.GONE
                    llDone.visibility = View.GONE
                    tvInfo.visibility = View.GONE
                    tvTime.visibility = View.GONE
                }
                "1" -> { //未处理
                    llOperate.visibility = View.VISIBLE
                    llDone.visibility = View.GONE
                }

                "2" -> { //已同意
                    llOperate.visibility = View.GONE
                    llDone.visibility = View.VISIBLE
                    tvDone.text = "已同意"
                    tvDone.background = context.resources.getDrawable(R.drawable.bg_group_request_done)
                    tvDone.setTextColor(context.resources.getColor(R.color.normalGray))
                    tvPerson.text = operateList[position].nickname
                }

                "3" -> { //已拒绝
                    llOperate.visibility = View.GONE
                    llDone.visibility = View.VISIBLE
                    tvDone.text = "已拒绝"
                    tvDone.background = context.resources.getDrawable(R.drawable.bg_group_request_refuse)
                    tvDone.setTextColor(context.resources.getColor(R.color.redOrange))
                    tvPerson.text = operateList[position].nickname
                }

            }
            ivIcon.setOnClickListener {
                if (operateList[position].other_uid != "0") {
                    val intent = Intent(context, PesonInfoActivity::class.java)
                    intent.putExtra("uid", operateList[position].other_uid)
                    context.startActivity(intent)
                }
            }

            tvAgree.setOnClickListener {
                agreeJoin(position)
            }

            tvRefuse.setOnClickListener {
                val refuseJoinGroupPop = RefuseJoinGroupPop(context, operateList[position].content)
                refuseJoinGroupPop.showPopupWindow()
                refuseJoinGroupPop.setOnPopRefuseListener { reason -> refuseJoin(position, reason) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): GroupMsgOperateHolder {
        return GroupMsgOperateHolder(LayoutInflater.from(context).inflate(R.layout.app_item_group_msg_operation, parent, false))
    }

    override fun getItemCount(): Int {
        return operateList.size
    }

    override fun onBindViewHolder(holder: GroupMsgOperateHolder?, position: Int) {
        holder!!.setData(position)
    }

    private var onSimpleItemListener: OnSimpleItemListener? = null

    fun setOnSimpleItemListener(onSimpleItemListener: OnSimpleItemListener) {
        this.onSimpleItemListener = onSimpleItemListener
    }

    fun refuseJoin(index:Int,reason:String) {
        HttpHelper.getInstance().refuseJoinGroup(operateList[index].ugid,operateList[index].mid,reason,object : HttpCodeListener {
            override fun onSuccess(data: String?) {
                operateList[index].state = "3"
                operateList[index].nickname = "我"
                notifyItemChanged(index)
            }

            override fun onFail(code: Int, msg: String?) {

            }
        })
    }

    fun agreeJoin(index:Int) {
        HttpHelper.getInstance().agreeJoinGroup(operateList[index].ugid,operateList[index].mid,object : HttpCodeListener {
            override fun onSuccess(data: String?) {
                operateList[index].state = "2"
                operateList[index].nickname = "我"
                notifyItemChanged(index)
            }

            override fun onFail(code: Int, msg: String?) {

            }
        })
    }

}