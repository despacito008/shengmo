package com.tencent.qcloud.tim.uikit.modules.chat.layout.message.holder;

import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tencent.imsdk.v2.V2TIMDownloadCallback;
import com.tencent.imsdk.v2.V2TIMElem;
import com.tencent.imsdk.v2.V2TIMFileElem;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.qcloud.tim.uikit.R;
import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.modules.message.MessageInfo;
import com.tencent.qcloud.tim.uikit.utils.FileUtil;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import java.io.File;

public class MessageFileHolder extends MessageContentHolder {

    private TextView fileNameText;
    private TextView fileSizeText;
    private TextView fileStatusText;
    private ImageView fileIconImage;

    public MessageFileHolder(View itemView) {
        super(itemView);
    }

    @Override
    public int getVariableLayout() {
        return R.layout.message_adapter_content_file;
    }

    @Override
    public void initVariableViews() {
        fileNameText = rootView.findViewById(R.id.file_name_tv);
        fileSizeText = rootView.findViewById(R.id.file_size_tv);
        fileStatusText = rootView.findViewById(R.id.file_status_tv);
        fileIconImage = rootView.findViewById(R.id.file_icon_iv);
    }

    @Override
    public void layoutVariableViews(final MessageInfo msg, final int position) {
        V2TIMMessage message = msg.getTimMessage();
        if (message.getElemType() != V2TIMMessage.V2TIM_ELEM_TYPE_FILE) {
            return;
        }
        final V2TIMFileElem fileElem = message.getFileElem();
       // final String path = msg.getDataPath();
        fileNameText.setText(fileElem.getFileName());
        String size = FileUtil.FormetFileSize(fileElem.getFileSize());
        fileSizeText.setText(size);
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "_shengmo");
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        final String path = storageDir.getPath() + "/" + fileElem.getFileName();
        msgContentFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.toastLongMessage(TUIKit.getAppContext().getString(R.string.file_path) + path);
            }
        });
        if (msg.getStatus() == MessageInfo.MSG_STATUS_SEND_SUCCESS || msg.getStatus() == MessageInfo.MSG_STATUS_NORMAL) {
            fileStatusText.setText(R.string.sended);
        } else if (msg.getStatus() == MessageInfo.MSG_STATUS_DOWNLOADING) {
            fileStatusText.setText(R.string.downloading);
        } else if (msg.getStatus() == MessageInfo.MSG_STATUS_DOWNLOADED) {
            fileStatusText.setText(R.string.downloaded);
        } else if (msg.getStatus() == MessageInfo.MSG_STATUS_UN_DOWNLOAD) {
            fileStatusText.setText(R.string.un_download);
            msgContentFrame.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    msg.setStatus(MessageInfo.MSG_STATUS_DOWNLOADING);
                    sendingProgress.setVisibility(View.VISIBLE);
                    fileStatusText.setText(R.string.downloading);
//                    File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "_shengmo");
//                    boolean success = true;
//                    if (!storageDir.exists()) {
//                        success = storageDir.mkdirs();
//                    }
//                    if (success) {
//                        final File imageFile = new File(storageDir, imageFileName);
//                        if (imageFile.exists()) {
//                            ToastUtil.toastShortMessage("图片已保存");
//                        } else {
//
//                        }
//                    }
                    //下载到外部存储 用户可以查看
                    fileElem.downloadFile(path, new V2TIMDownloadCallback() {
                        @Override
                        public void onProgress(V2TIMElem.V2ProgressInfo progressInfo) {

                        }

                        @Override
                        public void onError(int code, String desc) {
                            ToastUtil.toastLongMessage("getToFile fail:" + code + "=" + desc);
                            fileStatusText.setText(R.string.un_download);
                            sendingProgress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onSuccess() {
                            msg.setDataPath(path);
                            fileStatusText.setText(R.string.downloaded);
                            msg.setStatus(MessageInfo.MSG_STATUS_DOWNLOADED);
                            sendingProgress.setVisibility(View.GONE);
                            msgContentFrame.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ToastUtil.toastLongMessage(TUIKit.getAppContext().getString(R.string.file_path) + path);
                                }
                            });
                        }
                    });

                    //另下一份到私有存储  再次进入显示已下载
                    fileElem.downloadFile(msg.getDataPath(), new V2TIMDownloadCallback() {
                        @Override
                        public void onProgress(V2TIMElem.V2ProgressInfo v2ProgressInfo) {

                        }

                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(int i, String s) {

                        }
                    });
                }
            });
        }
    }

}
