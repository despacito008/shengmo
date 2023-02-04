package cn.tillusory.tiui.custom;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.hwangjr.rxbus.RxBus;

import java.lang.ref.WeakReference;

import cn.tillusory.tiui.R;
import cn.tillusory.tiui.model.RxBusAction;
import cn.tillusory.tiui.model.TiMode;

public class TiResetDialogFragment extends DialogFragment {
    public static final String BEAUTY_MODE = "BEAUTY_MODE";

    private View root;
    private Context context;
    private int tiMode = TiMode.MODE_BEAUTY;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        context = new WeakReference<>(getActivity()).get();
        root = LayoutInflater.from(context).inflate(R.layout.dialog_reset, null);
        Dialog dialog = new Dialog(context, R.style.TiDialog);
        dialog.setContentView(root);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        Window window = dialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            params.gravity = Gravity.CENTER;
            window.setAttributes(params);
        }

        return dialog;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            tiMode = bundle.getInt(BEAUTY_MODE);
        }

        root.findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tiMode == TiMode.MODE_MAKEUP) {
                    TiSharePreferences.getInstance().resetMakeup();
                } else {
                    TiSharePreferences.getInstance().resetBeauty();
                }
                RxBus.get().post(RxBusAction.ACTION_ENABLED_BTN_RESET, false);
                dismiss();
            }
        });

        root.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }



}


