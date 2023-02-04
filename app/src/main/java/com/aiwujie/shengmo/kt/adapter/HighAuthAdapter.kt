package com.aiwujie.shengmo.kt.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import cn.bingoogolapple.photopicker.widget.BGASortableNinePhotoLayout
import com.aiwujie.shengmo.bean.HighAuthDataBean
import com.aiwujie.shengmo.bean.HighAuthInfoBean
import com.aiwujie.shengmo.kt.adapter.`interface`.BaseHolderContentInterface
import com.aiwujie.shengmo.kt.adapter.`interface`.BaseHolderInterfce
import com.aiwujie.shengmo.kt.adapter.holder.HighAuthBaseHolder
import com.aiwujie.shengmo.kt.util.showToast
import java.util.ArrayList


class HighAuthAdapter(var context: Context, var list: List<HighAuthInfoBean.DataInfoBean>, private var itemViewType: String) : RecyclerView.Adapter<HighAuthBaseHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): HighAuthBaseHolder? {
        return HighAuthBaseHolder.BaseHolder.getInstance(context, parent, viewType)
    }


    override fun getItemCount(): Int {
        return if (list.size > 3) 3 else list.size
    }

    override fun onBindViewHolder(holder: HighAuthBaseHolder?, position: Int) {
        list[position].run {
            holder?.layoutDatas(this)
        }



        holder?.baseHolderContentInterface = object : BaseHolderContentInterface {
            override fun onAddItem(layoutPosition :Int) {
                baseHolderInterfce?.onPictureAdd(layoutPosition)
            }

            override fun onDeleteItem(layoutPosition: Int) {
                baseHolderInterfce?.onDeleteAdapterItem(layoutPosition)
            }

            override fun onAdapterNotify() {
                baseHolderInterfce?.onAdapterNotify()

            }

            override fun onAdapterNotify(layoutPosition: Int) {

            }

            override fun onNineClickItem(layoutPosition :Int,position: Int, models: ArrayList<String>,photoLayout: BGASortableNinePhotoLayout) {
                baseHolderInterfce?.onNinePictureItemClick(layoutPosition,position,models,photoLayout)
            }
        }

    }

    override fun getItemViewType(position: Int): Int {
        return itemViewType.toInt()
    }

    var baseHolderInterfce: BaseHolderInterfce? = null


}