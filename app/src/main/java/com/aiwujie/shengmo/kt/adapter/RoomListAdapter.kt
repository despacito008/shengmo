package com.aiwujie.shengmo.kt.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.support.constraint.Group
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.bean.ScenesRoomInfoBean
import com.aiwujie.shengmo.kt.adapter.`interface`.OnRoomItemClickListener
import com.aiwujie.shengmo.timlive.view.RoundAngleImageView
import com.aiwujie.shengmo.utils.ClickUtils
import com.aiwujie.shengmo.utils.OnSimpleItemListener
import com.aiwujie.shengmo.utils.UserIdentityUtils
import com.bumptech.glide.Glide
import org.feezu.liuli.timeselector.Utils.TextUtil

/**
 * 用于展示房间列表的item
 */
class RoomListAdapter : RecyclerView.Adapter<RoomListAdapter.ViewHolder> {
    private var mContext: Context
    private var mList: List<ScenesRoomInfoBean>?
    var type = ""

    constructor(context: Context, list: List<ScenesRoomInfoBean>?, type: String) {
        mContext = context
        mList = list
        this.type = type
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.live_room_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mContext, position)
    }

    override fun getItemCount(): Int {
        return if (mList != null && mList!!.isNotEmpty()) {
            mList!!.size
        } else 0
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var mImagePic: RoundAngleImageView? = null
        private var mTextRoomName: TextView? = null
        private var mTextAnchorVLevel: ImageView? = null
        private var mTextRoomType: TextView? = null

        //private RelativeLayout mLiveRoomItem;
        private var mTextMemberCount: TextView? = null
        private var mTvLiveEnd: TextView? = null
        private var mIvInteract: ImageView? = null
        private var mIvAudio: ImageView? = null
        private var mIvContract: ImageView? = null
        private var mViewMask: View? = null
        private var mTvTitle: TextView? = null
        private var groupTicket: Group? = null
        private var tvTicket: TextView? = null
        private var viewPkSign: View? = null
        private var mIvPassword: ImageView? = null
        private var mRvSign: RecyclerView? = null
        private var mTvPlayBack: TextView? = null
        private fun initView(itemView: View) {
            mTextRoomName = itemView.findViewById(R.id.live_tv_live_room_name)
            mTextAnchorVLevel = itemView.findViewById(R.id.live_tv_live_anchor_v_level)
            mTextMemberCount = itemView.findViewById(R.id.live_tv_room_member_count)
            mTextRoomType = itemView.findViewById(R.id.live_tv_live_type)
            mImagePic = itemView.findViewById(R.id.live_iv_live_room_pic)
            mViewMask = itemView.findViewById(R.id.view_item_live_user_mask)
            mTvLiveEnd = itemView.findViewById(R.id.tv_item_live_user_end)
            mIvInteract = itemView.findViewById(R.id.iv_item_live_interact)
            mIvAudio = itemView.findViewById(R.id.iv_item_live_audio)
            mIvContract = itemView.findViewById(R.id.iv_item_live_contract)
            mTvTitle = itemView.findViewById(R.id.live_tv_live_room_live_title)
            groupTicket = itemView.findViewById(R.id.group_item_live_ticket)
            tvTicket = itemView.findViewById(R.id.tv_item_live_ticket_count)
            viewPkSign = itemView.findViewById(R.id.view_item_live_pk)
            mIvPassword = itemView.findViewById(R.id.iv_item_live_password)
            mRvSign = itemView.findViewById(R.id.rv_item_room_sign)
            mTvPlayBack = itemView.findViewById(R.id.tv_item_room_play_back)
        }

        @SuppressLint("SetTextI18n")
        fun bind(context: Context, position: Int) {
            val roomInfo = mList!![position] ?: return
            mTextRoomName!!.text = roomInfo.nickname
            when (roomInfo.sex) {
                "1" -> mTextRoomName!!.setTextColor(context.resources.getColor(R.color.liveBoyColor))
                "2" -> mTextRoomName!!.setTextColor(context.resources.getColor(R.color.liveGirlColor))
                else -> mTextRoomName!!.setTextColor(context.resources.getColor(R.color.liveCdtColor))
            }
            if (roomInfo.nickname == roomInfo.live_title) {
                mTvTitle!!.visibility = View.GONE
            } else {
                mTvTitle!!.visibility = View.VISIBLE
                mTvTitle!!.text = roomInfo.live_title
            }

            //  判断显示标识，志愿者>官方管理>年svip>svip>年vip>vip
            UserIdentityUtils.showIdentity(context, roomInfo.head_pic, roomInfo.uid, roomInfo.is_admin, roomInfo.svipannual, roomInfo.svip, roomInfo.vipannual, roomInfo.vip, mTextAnchorVLevel)

            Glide.with(context).load(roomInfo.live_poster).error(R.drawable.live_room_default_cover).into(mImagePic!!)
            itemView.setOnClickListener { v ->
                if (!ClickUtils.isFastClick(v.id)) {
                    onRoomItemClickListener?.onRoomClickItem(layoutPosition, roomInfo)
                }
            }
            if (type == "red") {
                tvTicket!!.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 7f)
            } else {
                tvTicket!!.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12f)
            }
            if ("1" == roomInfo.is_ticket) {
                groupTicket!!.visibility = View.VISIBLE
                tvTicket!!.text = roomInfo.ticket_beans
            } else {
                groupTicket!!.visibility = View.GONE
            }
            if ("1" == roomInfo.is_pk) {
                viewPkSign!!.visibility = View.VISIBLE
            } else {
                viewPkSign!!.visibility = View.GONE
            }
            if (TextUtils.isEmpty(roomInfo.watchsum)) {
                mTextMemberCount!!.visibility = View.GONE
            } else {
                mTextMemberCount!!.visibility = View.VISIBLE
                mTextMemberCount!!.text = roomInfo.watchsum + ""
            }

            //回放 还是 直播
            if (TextUtil.isEmpty(roomInfo.live_log_id) || "0" == roomInfo.live_log_id) {
                mTvPlayBack!!.visibility = View.GONE
                mRvSign!!.visibility = View.VISIBLE
                if (roomInfo.icon_list != null && roomInfo.icon_list.size > 0) {
                    mRvSign!!.visibility = View.VISIBLE
                    val signAdapter = LiveRoomSignAdapter(context, roomInfo.icon_list)
                    val layoutManager = LinearLayoutManager(context)
                    layoutManager.orientation = LinearLayoutManager.HORIZONTAL
                    mRvSign!!.layoutManager = layoutManager
                    mRvSign!!.adapter = signAdapter
                    mRvSign!!.layoutDirection = View.LAYOUT_DIRECTION_RTL
                } else {
                    mRvSign!!.visibility = View.GONE
                }
            } else {
                mTvPlayBack!!.visibility = View.VISIBLE
                mRvSign!!.visibility = View.GONE
            }
        }

        init {
            initView(itemView)
        }
    }

    var onRoomItemClickListener:OnRoomItemClickListener? = null
}