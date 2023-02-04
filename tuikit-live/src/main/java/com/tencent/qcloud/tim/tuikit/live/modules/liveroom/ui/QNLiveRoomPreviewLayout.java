package com.tencent.qcloud.tim.tuikit.live.modules.liveroom.ui;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Group;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.donkingliang.labels.LabelsView;
import com.tencent.qcloud.tim.tuikit.live.R;
import com.tencent.qcloud.tim.tuikit.live.TUIKitLive;
import com.tencent.qcloud.tim.tuikit.live.bean.AnchorLiveCardBean;
import com.tencent.qcloud.tim.tuikit.live.component.other.RoundAngleImageView;
import com.tencent.qcloud.tim.tuikit.live.utils.ClickUtils;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;
import com.tencent.trtc.TRTCCloudDef;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * 预览页的Layout - 七牛
 */
public class QNLiveRoomPreviewLayout extends ConstraintLayout {
    private EditText mEditLiveRoomName;
    private RadioButton mRbLiveRoomQualityNormal;
    private RadioButton mRbLiveRoomQualityMusic;
    private PreviewCallback mPreviewCallback;
    private PreviewOnClickCover mPreviewOnClickCover;
    private PreviewOnLoadCover mPreviewOnLoadCover;
    private ImageButton mButtonBeauty;
    private Button mButtonStartRoom;
    private RoundAngleImageView mLiveRoomCover;
    private LabelsView mLabelsViewPreview;
    private TextView mLabelsTip;
    private TextView mLabelsTipNum;
    private CheckBox mCbInteract;
    private TextView mInteractTip;
    private ImageButton mButtonCameraType;
    private SeekBar seekBarTicket;
    private TextView tvTicketPrice;
    private Group groupTicket;
    private CheckBox mCbTicket;
    private EditText mEtLiveRoomTicket;
    private ConstraintLayout mConstraintPassword;

    private int is_ticket;
    private int ticket_beans = 0;

    private int is_record;
    private TextView mTvRecord;
    private CheckBox mCbRecord;
    private Group mGroupRecord;

    public QNLiveRoomPreviewLayout(Context context) {
        this(context, null);
        initView(context);
    }

    public QNLiveRoomPreviewLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public QNLiveRoomPreviewLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        inflate(context, R.layout.live_layout_live_room_preview, this);
        mEditLiveRoomName = findViewById(R.id.et_live_room_name);
        mRbLiveRoomQualityNormal = findViewById(R.id.rb_live_room_quality_normal);
        mRbLiveRoomQualityMusic = findViewById(R.id.rb_live_room_quality_music);
        mButtonBeauty = findViewById(R.id.btn_beauty);
        mLiveRoomCover = findViewById(R.id.img_live_room_cover);
        mLiveRoomCover.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(mPreviewOnClickCover != null){
//                    mPreviewOnClickCover.onClickCover(mLiveRoomCover);
//                }
                if (mPreviewCallback != null) {
                    mPreviewCallback.onClickCover();
                }
            }
        });
        mButtonBeauty.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPreviewCallback != null) {
                    mPreviewCallback.onBeautyPanel();
                }
            }
        });
        mButtonStartRoom = findViewById(R.id.btn_start_room);
        mButtonStartRoom.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ClickUtils.isFastClick(v.getId())) return;
                String roomName = mEditLiveRoomName.getText().toString().trim();
//                if(mPreviewOnLoadCover != null){
//                    mPreviewOnLoadCover.onLoadLiveTitle(roomName,mCbInteract.isChecked()?"1":"0");
//                }
                if (mPreviewCallback != null) {
                    mPreviewCallback.onStartLive(roomName,livePoster,0);
                }
            }
        });

        mButtonCameraType = findViewById(R.id.btn_switch_cam);
        mButtonCameraType.setBackgroundResource(R.drawable.ic_camera_preview_front);
        mButtonCameraType.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPreviewCallback != null) {
                    mPreviewCallback.onSwitchCamera();
                }
            }
        });
        findViewById(R.id.btn_close_before_live).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPreviewCallback != null) {
                    mPreviewCallback.onClose();
                }
            }
        });
        mLabelsViewPreview = findViewById(R.id.label_start_live);
        mLabelsTip = findViewById(R.id.tv_start_live_choose_label);
        mCbInteract = findViewById(R.id.cb_start_live_interact);
        mInteractTip = findViewById(R.id.tv_start_live_interact_tip);
        mLabelsTipNum = findViewById(R.id.tv_start_live_choose_label_num);
        seekBarTicket = findViewById(R.id.seek_bar_ticket);
        seekBarTicket.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvTicketPrice.setText("（" + progress + "魔豆）");
                ticket_beans = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        tvTicketPrice = findViewById(R.id.tv_start_live_ticket_price);
        groupTicket = findViewById(R.id.group_start_live_ticket);
        mCbTicket = findViewById(R.id.cb_start_live_ticket);
        mCbTicket.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isPressed()) {
                    if (!ticketPermission) {
                        if (TextUtils.isEmpty(ticketTip)) {
                            ToastUtil.toastShortMessage("您暂时不能使用此功能");
                        } else {
                            ToastUtil.toastShortMessage(ticketTip);
                        }
                        mCbTicket.setChecked(false);
                    } else {
                        if (isChecked) {
                            groupTicket.setVisibility(View.VISIBLE);
                            mCbInteract.setChecked(false);
                            tvTicketPrice.setText("（" + ticket_beans + "魔豆）");
                        } else {
                            groupTicket.setVisibility(View.GONE);
                            tvTicketPrice.setText("（" + "非必选" + "）");
                        }
                    }
                }
            }
        });
        mCbInteract.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mCbTicket.setChecked(false);
                }
            }
        });
        mEtLiveRoomTicket = findViewById(R.id.et_live_room_ticket);
        mEtLiveRoomTicket.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())) {
                    ticket_beans = 0;
                    //mEtLiveRoomTicket.setText(String.valueOf(ticket_beans));
                    //mEtLiveRoomTicket.setSelection(mEtLiveRoomTicket.getText().toString().length());
                } else {
                    ticket_beans = Integer.parseInt(s.toString());
                    if (ticket_beans > 1000) {
                        ToastUtil.toastShortMessage("门票房最高价格为1000魔豆");
                        ticket_beans = 1000;
                        mEtLiveRoomTicket.setText(String.valueOf(ticket_beans));
                        mEtLiveRoomTicket.setSelection(mEtLiveRoomTicket.getText().toString().length());
                    }
                }
                tvTicketPrice.setText("（" + ticket_beans + "魔豆）");
            }
        });

        mTvRecord = findViewById(R.id.tv_start_live_record);
        mCbRecord = findViewById(R.id.cb_start_live_record);
        mGroupRecord = findViewById(R.id.group_start_live_record);

        initPasswordLayout();
    }
    //开播
    public void startLive(String roomName,String coverUrl) {
        if (TextUtils.isEmpty(roomName)) {
            Toast.makeText(TUIKitLive.getAppContext(), R.string.live_room_name_empty, Toast.LENGTH_SHORT).show();
            return;
        }
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEditLiveRoomName.getWindowToken(), 0);
        int audioQuality = TRTCCloudDef.TRTC_AUDIO_QUALITY_MUSIC;
        if (mRbLiveRoomQualityNormal.isChecked()) {
            audioQuality = TRTCCloudDef.TRTC_AUDIO_QUALITY_DEFAULT;
        } else if (mRbLiveRoomQualityMusic.isChecked()) {
            audioQuality = TRTCCloudDef.TRTC_AUDIO_QUALITY_MUSIC;
        }
        if (mPreviewCallback != null) {
            mPreviewCallback.onStartLive(roomName,coverUrl, audioQuality);
        }
    }

    public void setPreviewCallback(PreviewCallback previewCallback) {
        mPreviewCallback = previewCallback;
    }

    public void setBottomViewVisibility(int visibility) {
        mButtonBeauty.setVisibility(visibility);
        mButtonStartRoom.setVisibility(visibility);
    }

    public interface PreviewCallback {
        void onClose();

        void onBeautyPanel();

        void onSwitchCamera();

        void onClickCover();

        void onStartLive(String roomName, String coverUrl, int audioQualityType);
    }



    public void setPreviewOnClickCover(PreviewOnClickCover previewOnClickCover){
        mPreviewOnClickCover = previewOnClickCover;
    }

    public void setmPreviewOnLoadCover(PreviewOnLoadCover mPreviewOnLoadCover) {
        this.mPreviewOnLoadCover = mPreviewOnLoadCover;
        if(this.mPreviewOnLoadCover != null){
            mPreviewOnLoadCover.onLoadCover(mLiveRoomCover);
        }
    }



    public interface PreviewOnClickCover{
        void onClickCover(ImageView view);
    }

    public interface PreviewOnLoadCover{
        void onLoadCover(ImageView view);
        void onLoadLiveTitle(String live_title, String is_interact);
    }

    public void showTitle(String title) {
        mEditLiveRoomName.setText(title);
    }

    String livePoster;
    public void showCover(Context context,String cover) {
        livePoster = cover;
        Glide.with(context).load(cover).into(mLiveRoomCover);
    }


    public void showHistoryInteraction(String isInteract) {
        mCbInteract.setChecked("1".equals(isInteract));
    }

    public void showHistoryTicket(String isTicket,String ticketBean) {
//        mCbTicket.setChecked("1".equals(isTicket));
//        if (mCbTicket.isChecked()) {
//            groupTicket.setVisibility(View.VISIBLE);
//            seekBarTicket.setProgress(Integer.parseInt(ticketBean));
//            tvTicketPrice.setText("（" + ticketBean + "魔豆）");
//        } else {
//            groupTicket.setVisibility(View.GONE);
//        }
    }

    public void showInteractTip(String tip) {
        if (!TextUtils.isEmpty(tip)) {
            mInteractTip.setText(tip);
        }
    }

    boolean ticketPermission = false;
    String ticketTip = "";
    public void showTicketPermission(String anchorStatus,String ticketTip) {
        if ("2".equals(anchorStatus) || "3".equals(anchorStatus)) {
            ticketPermission = true;
        } else {
            ticketPermission = false;
        }
        this.ticketTip = ticketTip;
    }

    AnchorLiveCardBean.DataBean labelDataBean;
    public void showLiveLabel(final AnchorLiveCardBean.DataBean labelBean) {
        labelDataBean = labelBean;
        mLabelsViewPreview.setMaxSelect(labelBean.getMax_label_num());
        mLabelsTipNum.setText("（必选,最多选择" + labelBean.getMax_label_num()+"个）");
        final List<Integer> selectIndexList = new ArrayList<>();
        mLabelsViewPreview.setLabels(labelBean.getLabel(), new LabelsView.LabelTextProvider<AnchorLiveCardBean.DataBean.LabelBean>() {
            @Override
            public CharSequence getLabelText(TextView label, int position, AnchorLiveCardBean.DataBean.LabelBean data) {
                if ("1".equals(data.getIs_default())) {
                   selectIndexList.add(position);
                }
                //Drawable drawable = getResources().getDrawable(R.drawable.ic_camera);
                //drawable.setBounds(0,0,15,50);
                //label.setCompoundDrawables(null,null,drawable,null);
                return data.getName();
            }
        });
        mLabelsViewPreview.setSelects(selectIndexList);
        mLabelsViewPreview.setOnSelectChangeIntercept(new LabelsView.OnSelectChangeIntercept() {
            @Override
            public boolean onIntercept(TextView label, Object data, boolean oldSelect, boolean newSelect, int position) {
                if ("1".equals(labelBean.getLabel().get(position).getIs_lock())) {
                    return true;
                } else {
                    return false;
                }
            }
        });
        mLabelsViewPreview.setOnLabelSelectChangeListener(new LabelsView.OnLabelSelectChangeListener() {
            @Override
            public void onLabelSelectChange(TextView label, Object data, boolean isSelect, int position) {
                AnchorLiveCardBean.DataBean.LabelBean bean = labelDataBean.getLabel().get(position);
                if (isSelect) {
                    bean.setIs_default("1");
                } else {
                    bean.setIs_default("0");
                }
                labelDataBean.getLabel().set(position,bean);
            }
        });

        mLabelsViewPreview.setOnLabelClickListener(new LabelsView.OnLabelClickListener() {
            @Override
            public void onLabelClick(TextView label, Object data, int position) {
                if ("1".equals(labelDataBean.getLabel().get(position).getIs_lock())) {
                    ToastUtil.toastShortMessage("该分区已锁定,无法操作");
                }
            }
        });
    }

    public void showRecordBtn() {
        mGroupRecord.setVisibility(View.VISIBLE);
    }

    public int getRecord() {
        if (mCbRecord.getVisibility() == View.GONE) {
            return 0;
        } else {
            return mCbRecord.isChecked() ? 1 : 0;
        }
    }


    public void showPasswordEdit() {
        mConstraintPassword.setVisibility(View.VISIBLE);

    }

    CheckBox mCbPassword;
    EditText mEtPassword;
    void initPasswordLayout() {
        mConstraintPassword = findViewById(R.id.layout_preview_password);
        mCbPassword = mConstraintPassword.findViewById(R.id.cb_start_live_password);
        mEtPassword = mConstraintPassword.findViewById(R.id.et_live_room_password);
        mCbPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mCbPassword.isPressed()) {
                    if (isChecked) {
                        mEtPassword.setVisibility(View.VISIBLE);
                        removeTicketRoom();
                    } else {
                        mEtPassword.setVisibility(View.GONE);
                        mEtPassword.setText("");
                    }
                }
            }
        });
    }

    public String getPassword() {
        if (mConstraintPassword.getVisibility() != View.VISIBLE) {
            return "";
        } else {
            return mEtPassword.getText().toString();
        }
    }

    void removeTicketRoom() {
        mCbTicket.setChecked(false);
        groupTicket.setVisibility(View.GONE);
        tvTicketPrice.setText("（" + "非必选" + "）");
    }

    void removePasswordRoom() {
        mCbPassword.setChecked(false);
        mEtPassword.setVisibility(View.GONE);
        mEtPassword.setText("");
    }


    public String getChooseLabel() {
        StringBuilder tid = new StringBuilder("");
        for (int i = 0; i < labelDataBean.getLabel().size(); i++) {
            if ("1".equals(labelDataBean.getLabel().get(i).getIs_default())) {
                tid.append(labelDataBean.getLabel().get(i).getTid());
                tid.append(",");
            }
        }
        return tid.toString().endsWith(",")?tid.substring(0,tid.length()-1):tid.toString();
    }

    public int getIsInteract() {
        return mCbInteract.isChecked() ? 1 : 0;
    }

    public int getIsTicket() {
        return mCbTicket.isChecked() ? 1 : 0;
    }

    public int getTicketBeans() {
        return ticket_beans;
    }

    public void updateCameraType(int type) {
        switch (type) {
            case 1 :
                mButtonCameraType.setBackgroundResource(R.drawable.ic_camera_preview_front);
                break;
            case 2 :
                mButtonCameraType.setBackgroundResource(R.drawable.ic_camera_preview_back);
                break;
            case 3 :
                mButtonCameraType.setBackgroundResource(R.drawable.ic_camera_preview_close);
                break;
        }
    }
}
