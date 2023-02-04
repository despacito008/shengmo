package com.aiwujie.shengmo.popuwindow;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RadioGroup;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.SecretActivity;

/**
 * @author
 * @version
 * 类说明  所在地筛选 弹出框
 */
public class SuozaidiPopupWindow extends PopupWindow {
	private View mMenuView;
RadioGroup rg;
	
	public SuozaidiPopupWindow(SecretActivity context) {

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.pop_xinxizhongduan, null);
		rg = (RadioGroup) mMenuView.findViewById(R.id.rg_suozaidi);
		rg.setOnCheckedChangeListener(context);
		show();
	}
	public void show() {
		// 设置SelectPicPopupWindow的View
		this.setContentView(mMenuView);
		// 设置SelectPicPopupWindow弹出窗体的宽
//		getwh();
		this.setWidth(LayoutParams.MATCH_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		
		// 设置SelectPicPopupWindow弹出窗体动画效果
		// this.setAnimationStyle(R.style.AnimBottom);
		// 实例化一个ColorDrawable颜色为半透明
		/*this.setOutsideTouchable(true);
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		// 设置SelectPicPopupWindow弹出窗体的背景
		 this.setBackgroundDrawable(dw);*/
		// mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
		this.setOutsideTouchable(true);
		this.setBackgroundDrawable(new BitmapDrawable());  

	}

}
