package com.tencent.qcloud.tim.tuikit.live.component.gift.imp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.tencent.qcloud.tim.tuikit.live.R;
import com.tencent.qcloud.tim.tuikit.live.utils.UIUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 下拉列表
 */
public class GiftPanelPopImp extends PopupWindow {
    private String TAG = GiftPanelPopImp.this.getClass().getSimpleName();
    private Map<Integer,GiftBeanPlatform.Auspicious> mapAuspicious = new HashMap<>();
    private Context mContext;
    private View mMenuView;
    private RadioGroup mRadioGroup;
    private List<GiftBeanPlatform.Auspicious> mAuspicious;
    private SpinnerType spinnerType = SpinnerType.NOR;
    private enum SpinnerType{//朝上/朝下
        NOR, PRE;
    }


    public GiftPanelPopImp(@NonNull Context context) {
        super(context);
        this.mContext = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView =  LayoutInflater.from(context).inflate(R.layout.pop_gift_send,null);
        //mMenuView = inflater.inflate(R.layout.pop_gift_send, null);
        mRadioGroup = (RadioGroup) mMenuView.findViewById(R.id.rg_gift_send);
    }

    /**
     * 加载右侧下拉列表的数据
     * @param mAuspicious
     */
    private void initMenuData(List<GiftBeanPlatform.Auspicious> mAuspicious) {
        if(mAuspicious == null) return;
        RadioButton rb4 = mMenuView.findViewById(R.id.rb_item_4);
        RadioButton rb3 = mMenuView.findViewById(R.id.rb_item_3);
        RadioButton rb2 = mMenuView.findViewById(R.id.rb_item_2);
        RadioButton rb1 = mMenuView.findViewById(R.id.rb_item_1);
        for (int i = 0; i < mAuspicious.size(); i++) {
            mapAuspicious.put(i,mAuspicious.get(i));
        }
        if (mapAuspicious.size() > 0) {
            rb1.setText(mapAuspicious.get(0).toString());
        }
        if (mapAuspicious.size() > 1) {
            rb2.setText(mapAuspicious.get(1).toString());
        }
        if (mapAuspicious.size() > 2) {
            rb3.setText(mapAuspicious.get(2).toString());
        }
        if (mapAuspicious.size() > 3) {
            rb4.setText(mapAuspicious.get(3).toString());
        }
    }

    /**
     * 显示控件
     * @param tv
     * @param mSpinnerShowGift
     * @param ivShowGiftArray
     */
    public void show(View tv, TextView mSpinnerShowGift, ImageView ivShowGiftArray) {
        if(isShowing()) dismiss();
        int offY = 245; //距离底部的高度
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(mMenuView);
        if(!UIUtil.hasNavBar(mContext)){
            offY = offY- UIUtil.getNavigationBarHeight(mContext);
        }
        //voidadapterApiV24ForShowAtLocation(this,tv,Gravity.RIGHT|Gravity.BOTTOM,25,offY);
        setOutsideTouchable(true);
        setOnCheckedChangeListener(mSpinnerShowGift);

        showAtLocation(tv, Gravity.RIGHT|Gravity.BOTTOM,25,offY);
        if(spinnerType == SpinnerType.NOR){
            ivShowGiftArray.setBackgroundResource(R.drawable.send_number_pre);
            spinnerType = SpinnerType.PRE;
        }else if(spinnerType == SpinnerType.PRE){
            ivShowGiftArray.setBackgroundResource(R.drawable.send_number_nor);
            spinnerType = SpinnerType.NOR;
        }
        initMenuData(mAuspicious);
    }

    /**
     * 获取文本中的数字
     * @param text
     * @return
     */
    public static List<Long> getDigit(String text) {
        List<Long> digitList =new ArrayList<Long>();
        Pattern p= Pattern.compile("(\\d+)");
        Matcher m= p.matcher(text);
        while (m.find()) {
            String find= m.group(1).toString();
            digitList.add(Long.valueOf(find));
        }
        return digitList;
    }

    public void setmAuspicious(List<GiftBeanPlatform.Auspicious> mAuspicious) {
        this.mAuspicious = mAuspicious;
    }

    public void setOnCheckedChangeListener(final TextView mSpinnerShowGift) {
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton)mMenuView.findViewById(mRadioGroup.getCheckedRadioButtonId());
                if(rb!=null && !TextUtils.isEmpty(rb.getText())){
                    List<Long> mData = getDigit(rb.getText().toString());
                    StringBuffer str = new StringBuffer();
                    for (Long l: mData) {
                        str.append(l);
                    }
                    mSpinnerShowGift.setText(str);
                    dismiss();
                }
            }
        });
    }

    public void  voidadapterApiV24ForShowAsDropDown(PopupWindow window, View parent, int offsetY) {

        if (null == window) {
            return;
        }

        if (null == parent) {

            return;

        }

        if(Build.VERSION.SDK_INT >= 24){

            Rect visibleFrame = new Rect();

            parent.getGlobalVisibleRect(visibleFrame);

            int height =parent.getResources().getDisplayMetrics().heightPixels - visibleFrame.bottom
                    +Math.abs(offsetY);

            window.setHeight(height);

        }

        window.showAsDropDown(parent, 0, offsetY);

    }

    public void  voidadapterApiV24ForShowAtLocation(PopupWindow window, View parent, int gravity, int x, int y) {

        if (null == window) {
            return;
        }

        if (null == parent) {

            return;

        }

        if(Build.VERSION.SDK_INT >= 24){

            Rect visibleFrame = new Rect();

            parent.getGlobalVisibleRect(visibleFrame);

            int height =parent.getResources().getDisplayMetrics().heightPixels - visibleFrame.bottom
                    +Math.abs(y);

            window.setHeight(height);

        }

        window.showAtLocation(parent, gravity,x, y);

    }
}
