package com.aiwujie.shengmo.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.adapter.RedwomenNewListAdapter.WomenViewHolder
import com.aiwujie.shengmo.eventbus.RedWomenIntroData
import com.aiwujie.shengmo.http.HttpUrl
import com.aiwujie.shengmo.kt.util.addTextChangedListenerDsl
import com.aiwujie.shengmo.utils.GlideImgManager
import com.aiwujie.shengmo.utils.OnSimpleItemListener
import com.aiwujie.shengmo.utils.SharedPreferencesUtils

/**
 * Created by 290243232 on 2017/1/23.
 */
class RedwomenNewListAdapter : RecyclerView.Adapter<WomenViewHolder>{
    private var context: Context
    private var list: List<RedWomenIntroData.DataBean>
    private val inflater: LayoutInflater? = null
    private var admin: String
    var handler = Handler()

    //声明接口对象
    var mClickListener: OnRedwomenClickListener? = null
    private var mType = ""

    constructor(context: Context, list: List<RedWomenIntroData.DataBean>) {
        this.context = context
        this.list = list
        admin = SharedPreferencesUtils.getParam(context.applicationContext, "admin", "0") as String
    }

    constructor(context: Context, list: List<RedWomenIntroData.DataBean>, type: String) {
        this.context = context
        this.list = list
        admin = SharedPreferencesUtils.getParam(context.applicationContext, "admin", "0") as String
        mType = type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WomenViewHolder {
        return WomenViewHolder(LayoutInflater.from(context).inflate(R.layout.app_item_listview_redwomen_person_center, parent, false))
    }

    override fun onBindViewHolder(holder: WomenViewHolder, position: Int, payloads: List<Any>) {
        super.onBindViewHolder(holder, position, payloads)
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
        } else {
            holder.itemListviewRedwomenPersonCenterTv!!.setText(list[position].remarks)
        }
    }

    override fun onBindViewHolder(holder: WomenViewHolder, @SuppressLint("RecyclerView") position: Int) {
            holder.display(position)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    var  onSimpleItemListener :OnSimpleItemListener? =null


    //创建接口
    interface OnRedwomenClickListener {
        fun onRedwomenClick(position: Int)
        fun onRedwomenClick(position: Int, remarks: String)
    }

    fun setRedwomenClick(mClickListener: OnRedwomenClickListener?) {
        this.mClickListener = mClickListener
    }



    inner class WomenViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener  {

        var itemListviewRedwomenPersonCenterIcon: ImageView =  itemView.findViewById(R.id.item_listview_redwomen_person_center_icon)

        var itemListviewRedwomenPersonCenterName: TextView  =  itemView.findViewById(R.id.item_listview_redwomen_person_center_name)

        var itemListviewRedwomenPersonCenterRealname: ImageView  = itemView.findViewById(R.id.item_listview_redwomen_person_center_realname)

        var itemListviewRedwomenPersonCenterSex: TextView  = itemView.findViewById(R.id.item_listview_redwomen_person_center_sex)

        var itemListviewRedwomenPersonCenterRole: TextView  = itemView .findViewById(R.id.item_listview_redwomen_person_center_role)

        var itemListviewRedwomenPersonCenterCity: TextView  = itemView.findViewById(R.id.item_listview_redwomen_person_center_city)

        var itemListviewRedwomenPersonCenterTime: TextView =itemView.findViewById(R.id.item_listview_redwomen_person_center_time)

        var itemListviewRedwomenPersonCenterTv: EditText = itemView.findViewById(R.id.item_listview_redwomen_person_center_tv)

        var itemListviewRedwomenPersonCenterHnjy: ImageView =itemView.findViewById(R.id.item_listview_redwomen_person_center_hnjy)

        var llRemarks: LinearLayout = itemView.findViewById(R.id.ll_remarks)

        var tvSave: TextView =itemView.findViewById(R.id.tv_save)

        fun  display(position: Int){
            list[position].run {
                val data = list[position]
                //        if (admin.equals("1")) {
//            holder.itemListviewRedwomenPersonCenterHnjy.setVisibility(View.VISIBLE);
//        } else {
//            holder.itemListviewRedwomenPersonCenterHnjy.setVisibility(View.GONE);
//        }
                if ("1" == mType) {
                    llRemarks.visibility = View.GONE
                    tvSave.visibility = View.GONE
                } else {
                    if (admin == "1") {
                        llRemarks.visibility = View.VISIBLE
                        tvSave.visibility = View.GONE
                    } else {
                        llRemarks.visibility = View.GONE
                        tvSave.visibility = View.GONE
                    }
                }
                itemListviewRedwomenPersonCenterTv.addTextChangedListenerDsl { afterTextChanged {
                    if (it.toString() != data.remarks) {
                        tvSave.visibility = View.VISIBLE
                    } else {
                        tvSave.visibility = View.GONE
                    }
                } }
                itemListviewRedwomenPersonCenterName.text = data.nickname
                if (data.head_pic == "" || data.head_pic == HttpUrl.NetPic()) { //"http://59.110.28.150:888/"
                    itemListviewRedwomenPersonCenterIcon.setImageResource(R.mipmap.morentouxiang)
                } else {
                    GlideImgManager.glideLoader(context, data.head_pic, R.mipmap.morentouxiang, R.mipmap.morentouxiang, itemListviewRedwomenPersonCenterIcon, 0)
                }
                itemListviewRedwomenPersonCenterSex.text = data.age
                if (data.city == "" && data.province == "") {
                    itemListviewRedwomenPersonCenterCity.text = "未知"
                } else {
                    if (data.city != "") {
                        itemListviewRedwomenPersonCenterCity.text = data.city
                    } else {
                        itemListviewRedwomenPersonCenterCity.text = data.province
                    }
                }
                if (data.realname == "0") {
                    itemListviewRedwomenPersonCenterRealname.visibility = View.GONE
                } else {
                    itemListviewRedwomenPersonCenterRealname.visibility = View.VISIBLE
                }
                if (data.sex == "1") {
                    itemListviewRedwomenPersonCenterSex.setBackgroundResource(R.drawable.item_sex_nan_bg)
                    val drawable = context.resources.getDrawable(R.mipmap.nan)
                    drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
                    itemListviewRedwomenPersonCenterSex.setCompoundDrawables(drawable, null, null, null)
                } else if (data.sex == "2") {
                    itemListviewRedwomenPersonCenterSex.setBackgroundResource(R.drawable.item_sex_nv_bg)
                    val drawable = context.resources.getDrawable(R.mipmap.nv)
                    drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
                    itemListviewRedwomenPersonCenterSex.setCompoundDrawables(drawable, null, null, null)
                } else if (data.sex == "3") {
                    itemListviewRedwomenPersonCenterSex.setBackgroundResource(R.drawable.item_sex_san_bg)
                    val drawable = context.resources.getDrawable(R.mipmap.san)
                    drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
                    itemListviewRedwomenPersonCenterSex.setCompoundDrawables(drawable, null, null, null)
                }
                if (data.role == "S") {
                    itemListviewRedwomenPersonCenterRole.setBackgroundResource(R.drawable.item_sex_nan_bg)
                    itemListviewRedwomenPersonCenterRole.text = "斯"
                } else if (data.role == "M") {
                    itemListviewRedwomenPersonCenterRole.setBackgroundResource(R.drawable.item_sex_nv_bg)
                    itemListviewRedwomenPersonCenterRole.text = "慕"
                } else if (data.role == "SM") {
                    itemListviewRedwomenPersonCenterRole.setBackgroundResource(R.drawable.item_sex_san_bg)
                    itemListviewRedwomenPersonCenterRole.text = "双"
                } else if (data.role == "~") {
                    itemListviewRedwomenPersonCenterRole.setBackgroundResource(R.drawable.item_sex_lang_bg)
                    itemListviewRedwomenPersonCenterRole.text = "~"
                }
                itemListviewRedwomenPersonCenterTime.text = "牵线时间：" + data.addtime
                itemListviewRedwomenPersonCenterTv.setText(data.remarks)
                itemListviewRedwomenPersonCenterHnjy.tag = position
//                itemListviewRedwomenPersonCenterHnjy.setOnClickListener(this)
                tvSave.tag = position
                tvSave.setOnClickListener {
                    if (mClickListener != null) {
                        mClickListener!!.onRedwomenClick(position, itemListviewRedwomenPersonCenterTv.text.toString().trim { it <= ' ' })
                    }
                }


                itemView.setOnClickListener {
                    if (onSimpleItemListener != null){
                        onSimpleItemListener?.onItemListener(position)
                    }
                }
            }
        }

        override fun onClick(v: View?) {
            val pos = v?.tag as Int
            mClickListener!!.onRedwomenClick(pos)
        }
    }
}