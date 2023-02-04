package cn.tillusory.tiui.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.tillusory.tiui.R;
import cn.tillusory.tiui.adapter.TiGreenScreenEditAdapter;
import cn.tillusory.tiui.custom.TiSharePreferences;
import cn.tillusory.tiui.model.RxBusAction;
import cn.tillusory.tiui.model.TiGreenScreenEdit;

public class TiGreenScreenView extends LinearLayout {

    private TextView tiGreenScreenTV;
    private TextView tiBtnRestore;
    private RecyclerView tiGreenScreenRV;
    private ImageView tiBackIV;
    private TiGreenScreenEditAdapter greenScreenEditAdapter;
    private List<TiGreenScreenEdit> greenScreenEditList = new ArrayList<>();

    public TiGreenScreenView(Context context) {
        super(context);
    }

    public TiGreenScreenView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TiGreenScreenView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TiGreenScreenView init() {
        RxBus.get().register(this);

        initView();

        initData();

        return this;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        if(RxBus.get().hasRegistered(this)) { RxBus.get().unregister(this); }
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_ti_green_screen, this);

        tiGreenScreenTV = findViewById(R.id.tiGreenScreenTV);
        tiBtnRestore = findViewById(R.id.tiBtnRestore);
        tiGreenScreenRV = findViewById(R.id.tiGreenScreenRV);
        tiBackIV = findViewById(R.id.tiGreenScreenBackIV);
    }

    private void initData() {
        tiGreenScreenTV.setText(R.string.green_screen);

        tiBtnRestore.setEnabled(TiSharePreferences.getInstance().isGreenScreenRestoreEnable());
        tiBtnRestore.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                tiBtnRestore.setEnabled(false);
                TiSharePreferences.getInstance().restoreGreenScreenEdit();
                RxBus.get().post(RxBusAction.ACTION_RESTORE_GREEN_SCREEN, true);
            }
        });

        tiBackIV.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                RxBus.get().post(RxBusAction.ACTION_SHOW_GREEN_SCREEN, false);
            }
        });

        greenScreenEditList.clear();
        greenScreenEditList.addAll(Arrays.asList(TiGreenScreenEdit.values()));
        greenScreenEditAdapter = new TiGreenScreenEditAdapter(greenScreenEditList);
        tiGreenScreenRV.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        tiGreenScreenRV.setAdapter(greenScreenEditAdapter);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = @Tag(RxBusAction.ACTION_ENABLE_GREEN_SCREEN_RESTORE))
    public void setBtnRestoreEnabled(Boolean enabled) {
        if (!tiBtnRestore.isEnabled()) {
            tiBtnRestore.setEnabled(true);
            TiSharePreferences.getInstance().putGreenScreenRestoreEnable(true);
        }
    }



}


