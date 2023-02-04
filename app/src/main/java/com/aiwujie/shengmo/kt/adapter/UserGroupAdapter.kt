package com.aiwujie.shengmo.kt.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.bean.NormalGroupBean
import com.aiwujie.shengmo.utils.ImageLoader
import com.aiwujie.shengmo.utils.OnSimpleItemListener
import org.w3c.dom.Text


/**
 * @ProjectName: workspace2
 * @Package: com.aiwujie.shengmo.kt.adapter
 * @ClassName: UserGroupAdapter
 * @Author: xmf
 * @CreateDate: 2022/6/7 14:07
 * @Description:
 */
class UserGroupAdapter(val context: Context,val groupList:List<NormalGroupBean>): RecyclerView.Adapter<UserGroupAdapter.UserGroupHolder>() {
    inner class UserGroupHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val ivItemIcon:ImageView by lazy { itemView.findViewById<ImageView>(R.id.iv_item_icon) }
        private val ivItemLevel:ImageView by lazy { itemView.findViewById<ImageView>(R.id.iv_item_level) }
        private val tvItemName:TextView by lazy { itemView.findViewById<TextView>(R.id.tv_item_name) }
        private val tvItemInfo:TextView by lazy { itemView.findViewById<TextView>(R.id.tv_item_info) }
        private val tvItemNum:TextView by lazy { itemView.findViewById<TextView>(R.id.tv_item_num) }
        private val tvItemDistance:TextView by lazy { itemView.findViewById<TextView>(R.id.tv_item_distance) }
        private val tvItemClaim:TextView by lazy { itemView.findViewById<TextView>(R.id.tv_item_claim) }
        fun display(index:Int) {
            groupList[index].run {
                ImageLoader.loadImage(context,group_pic,ivItemIcon)
                tvItemName.text = groupname
                tvItemInfo.text = introduce
                tvItemNum.text = "$member 人"
                tvItemDistance.text = "$distance km"
                tvItemClaim.visibility = if (is_claim == "1") View.VISIBLE else View.GONE
            }
            itemView.setOnClickListener {
                simpleItemListener?.onItemListener(index)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): UserGroupHolder {
        return UserGroupHolder(LayoutInflater.from(context).inflate(R.layout.app_item_group,parent,false))
    }

    override fun onBindViewHolder(holder: UserGroupHolder?, position: Int) {
        holder?.display(position)
    }

    override fun getItemCount(): Int {
        return groupList.size
    }

    var simpleItemListener:OnSimpleItemListener? = null
}
