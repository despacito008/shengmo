package com.aliyun.apsara.alivclittlevideo.net;

import android.content.Context;

import com.aliyun.apsara.alivclittlevideo.constants.AlivcLittleHttpConfig;
import com.aliyun.apsara.alivclittlevideo.constants.AlivcLittleServerApiConstants;
import com.aliyun.apsara.alivclittlevideo.net.data.LittleHttpResponse;
import com.aliyun.apsara.alivclittlevideo.net.data.LittleUserInfo;
import com.aliyun.apsara.alivclittlevideo.view.mine.AlivcLittleUserManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * http manager
 */

public class LittleHttpManager {
    private static LittleHttpManager mInstance;
    private HttpEngine mHttpEngine = new HttpEngine();
    private String mPackageName;
    public static LittleHttpManager getInstance() {
        if (mInstance == null) {
            synchronized (LittleHttpManager.class) {
                if (mInstance == null) {
                    mInstance = new LittleHttpManager();
                }
            }
        }
        return mInstance;
    }
    public void init(Context context){
        this.mPackageName = context.getPackageName();
    }
    /**
     * 生成新用户
     *
     * @param callback OnResponseCallback
     */
    public void newGuest(HttpEngine.HttpResponseResultCallback<LittleHttpResponse.LittleUserInfoResponse> callback) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(AlivcLittleHttpConfig.RequestKey.FORM_KEY_PACKAGE_NAME, mPackageName);
        Request request = new Request.Builder().url(urlAppend(AlivcLittleServerApiConstants.URL_NEW_GUEST, hashMap))
            .get()
            .build();
        mHttpEngine.request(request, LittleHttpResponse.LittleUserInfoResponse.class, callback);
    }

    /**
     * 修改用户昵称
     *
     * @param userId      userId
     * @param token       token
     * @param newNickName new nickname
     * @param callback    callback
     */
    public void requestChangeNickName(String userId, String token, String newNickName,
                                      HttpEngine.HttpResponseResultCallback<LittleHttpResponse
                                          .LittleUserInfoResponse> callback) {

        RequestBody requestBody = new FormBody.Builder()
            .add(AlivcLittleHttpConfig.RequestKey.FORM_KEY_PACKAGE_NAME, mPackageName)
            .add(AlivcLittleHttpConfig.RequestKey.FORM_KEY_USER_ID, userId)
            .add(AlivcLittleHttpConfig.RequestKey.FORM_KEY_TOKEN, token)
            .add(AlivcLittleHttpConfig.RequestKey.FORM_KEY_NEW_NICK_NAME, newNickName)
            .build();

        Request request = new Request.Builder().url(AlivcLittleServerApiConstants.URL_MODIFY_NICK_NAME)
            .post(requestBody)
            .build();
        mHttpEngine.request(request, LittleHttpResponse.LittleUserInfoResponse.class, callback);
    }

    /**
     * 获取sts信息
     */
    public void getStsInfo(HttpEngine.HttpResponseResultCallback<LittleHttpResponse.LittleStsInfoResponse> callback) {
        LittleUserInfo userInfo = AlivcLittleUserManager.getInstance().getUserInfo();

        String token;
        if (userInfo != null) {
            token = userInfo.getToken();
        } else {
            token = "";
        }
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(AlivcLittleHttpConfig.RequestKey.FORM_KEY_PACKAGE_NAME, mPackageName);
        hashMap.put(AlivcLittleHttpConfig.RequestKey.FORM_KEY_TOKEN, token);
        Request request = new Request.Builder().url(urlAppend(AlivcLittleServerApiConstants.URL_GET_STS_INFO, hashMap))
            .get()
            .build();

        mHttpEngine.request( request, LittleHttpResponse.LittleStsInfoResponse.class, callback);

    }

    /**
     * @param token     token
     * @param pageIndex pageIndex
     * @param pageSize  pageSize
     */
    public void requestMineVideoList( String token, int pageIndex, int pageSize, int id,
                                     HttpEngine.HttpResponseResultCallback<LittleHttpResponse
                                         .LittleMyVideoListResponse> callback) {

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(AlivcLittleHttpConfig.RequestKey.FORM_KEY_PAGE_INDEX, pageIndex + "");
        hashMap.put(AlivcLittleHttpConfig.RequestKey.FORM_KEY_PAGE_SIZE, pageSize + "");
        hashMap.put(AlivcLittleHttpConfig.RequestKey.FORM_KEY_PACKAGE_NAME, mPackageName);
        hashMap.put(AlivcLittleHttpConfig.RequestKey.FORM_KEY_TOKEN, token);
        if (id > 0) {
            hashMap.put(AlivcLittleHttpConfig.RequestKey.FORM_KEY_ID, id + "");
        }
        Request request = new Request.Builder().url(
            urlAppend(AlivcLittleServerApiConstants.URL_GET_PERSIONAL_VIDEO_LIST, hashMap))
            .get()
            .build();
        mHttpEngine.request(request, LittleHttpResponse.LittleMyVideoListResponse.class, callback);

    }

    /**
     * 获取推荐的视频列表
     *
     * @param token
     * @param pageIndex
     * @param pageSize
     * @param callback
     */
    public void requestRecommonVideoList( String token, int pageIndex, int pageSize, int id,
                                         HttpEngine.HttpResponseResultCallback<LittleHttpResponse
                                             .LittleMyVideoListResponse> callback) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(AlivcLittleHttpConfig.RequestKey.FORM_KEY_PACKAGE_NAME, mPackageName);
        hashMap.put(AlivcLittleHttpConfig.RequestKey.FORM_KEY_PAGE_INDEX, pageIndex + "");
        hashMap.put(AlivcLittleHttpConfig.RequestKey.FORM_KEY_PAGE_SIZE, pageSize + "");
        hashMap.put(AlivcLittleHttpConfig.RequestKey.FORM_KEY_TOKEN, token);
        if (id > 0) {
            hashMap.put(AlivcLittleHttpConfig.RequestKey.FORM_KEY_ID, id + "");
        }
        Request request = new Request.Builder().url(
            urlAppend(AlivcLittleServerApiConstants.URL_GET_RECOMMEND_VIDEO_LIST, hashMap))
            .get()
            .build();
        mHttpEngine.request(request, LittleHttpResponse.LittleMyVideoListResponse.class, callback);
    }

    /**
     * 获取直播的视频列表
     *
     * @param token
     * @param pageIndex
     * @param pageSize
     * @param callback
     */
    public void requestLiveVideoList(String token, int pageIndex, int pageSize,
                                     HttpEngine.HttpResponseResultCallback<LittleHttpResponse.LittleLiveListResponse>
                                         callback) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(AlivcLittleHttpConfig.RequestKey.FORM_KEY_PACKAGE_NAME, mPackageName);
        hashMap.put(AlivcLittleHttpConfig.RequestKey.FORM_KEY_PAGE_INDEX, pageIndex + "");
        hashMap.put(AlivcLittleHttpConfig.RequestKey.FORM_KEY_PAGE_SIZE, pageSize + "");
        hashMap.put(AlivcLittleHttpConfig.RequestKey.FORM_KEY_TOKEN, token);

        Request request = new Request.Builder().url(
            urlAppend(AlivcLittleServerApiConstants.URL_GET_LIVE_VIDEO_LIST, hashMap))
            .get()
            .build();
        mHttpEngine.request(request, LittleHttpResponse.LittleLiveListResponse.class, callback);
    }

    /**
     * 获取视频上传凭证
     *
     * @param title
     * @param fileName
     */

    public void requestVideoUploadAuth(String title, String fileName,
                                       HttpEngine.HttpResponseResultCallback<LittleHttpResponse
                                           .LittleVideoUploadAuthResponse> callback) {
        LittleUserInfo userInfo = AlivcLittleUserManager.getInstance().getUserInfo();
        String token = "";
        if (userInfo != null) {
            token = userInfo.getToken();
        }
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(AlivcLittleHttpConfig.RequestKey.FORM_KEY_PACKAGE_NAME, mPackageName);
        hashMap.put(AlivcLittleHttpConfig.RequestKey.FORM_KEY_TOKEN, token);
        hashMap.put(AlivcLittleHttpConfig.RequestKey.FORM_KEY_VIDEO_TITLE, title);
        hashMap.put(AlivcLittleHttpConfig.RequestKey.FORM_KEY_VIDEO_FILE_NAME, fileName);
        Request request = new Request.Builder().url(
            urlAppend(AlivcLittleServerApiConstants.URL_GET_VIDEO_UPLOAD_AUTH, hashMap))
            .get()
            .build();
        mHttpEngine.request(request, LittleHttpResponse.LittleVideoUploadAuthResponse.class, callback);

    }

    /**
     * 刷新视频上传凭证
     *
     * @param videoId
     * @param callback
     */
    public void refreshVideoUploadAuth(String videoId,
                                       HttpEngine.HttpResponseResultCallback<LittleHttpResponse
                                           .LittleVideoUploadAuthResponse> callback) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(AlivcLittleHttpConfig.RequestKey.FORM_KEY_VIDEO_ID, videoId);
        hashMap.put(AlivcLittleHttpConfig.RequestKey.FORM_KEY_PACKAGE_NAME, mPackageName);
        Request request = new Request.Builder().url(
            urlAppend(AlivcLittleServerApiConstants.URL_REFRESH_VIDEO_UPLOAD_AUTH, hashMap))
            .get()
            .build();
        mHttpEngine.request(request, LittleHttpResponse.LittleVideoUploadAuthResponse.class, callback);
    }

    /**
     * 获取图片上传凭证
     *
     * @param callback
     */
    public void requestImageUploadAuth(HttpEngine.HttpResponseResultCallback<LittleHttpResponse
                                           .LittleImageUploadAuthResponse> callback) {
        LittleUserInfo userInfo = AlivcLittleUserManager.getInstance().getUserInfo();

        String token = "";
        if (userInfo != null) {
            token = userInfo.getToken();
        }
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(AlivcLittleHttpConfig.RequestKey.FORM_KEY_PACKAGE_NAME, mPackageName);
        hashMap.put(AlivcLittleHttpConfig.RequestKey.FORM_KEY_TOKEN, token);
        hashMap.put(AlivcLittleHttpConfig.RequestKey.FORM_KEY_IMAGE_TYPE, "cover");
        Request request = new Request.Builder().url(
            urlAppend(AlivcLittleServerApiConstants.URL_GET_IMAGE_UPLOAD_AUTH, hashMap))
            .get()
            .build();
        mHttpEngine.request(request, LittleHttpResponse.LittleImageUploadAuthResponse.class, callback);
    }

    /**
     * 发布视频完成后通知app server
     *
     * @param token
     * @param videoId
     * @param coverUrl
     * @param title
     * @param description
     * @param tags
     * @param duration
     * @param size
     * @param cateId
     * @param cateName
     * @param callback
     */
    public void videoPublish(String token,
                             String videoId,
                             String coverUrl,
                             String title,
                             String description,
                             String tags,
                             float duration,
                             long size,
                             int cateId,
                             String cateName,
                             HttpEngine.HttpResponseResultCallback<LittleHttpResponse.LittlePublishResponse> callback) {

        RequestBody requestBody = new FormBody.Builder()
            .add(AlivcLittleHttpConfig.RequestKey.FORM_KEY_PACKAGE_NAME, mPackageName)
            .add(AlivcLittleHttpConfig.RequestKey.FORM_KEY_TOKEN, token)
            .add(AlivcLittleHttpConfig.RequestKey.FORM_KEY_VIDEO_COVER, coverUrl)
            .add(AlivcLittleHttpConfig.RequestKey.FORM_KEY_VIDEO_ID, videoId)
            .add(AlivcLittleHttpConfig.RequestKey.FORM_KEY_VIDEO_TITLE, title)
            .add(AlivcLittleHttpConfig.RequestKey.FORM_KEY_VIDEO_DESCRIBE, description)
            .add(AlivcLittleHttpConfig.RequestKey.FORM_KEY_VIDEO_TAGS, tags)
            .add(AlivcLittleHttpConfig.RequestKey.FORM_KEY_VIDEO_DURATION, duration + "")
            .add(AlivcLittleHttpConfig.RequestKey.FORM_KEY_VIDEO_SIZE, size + "")
            .add(AlivcLittleHttpConfig.RequestKey.FORM_KEY_VIDEO_CATE_ID, cateId + "")
            .add(AlivcLittleHttpConfig.RequestKey.FORM_KEY_VIDEO_CATE_NAME, cateName)
            .build();
        Request request = new Request.Builder()
            .url(AlivcLittleServerApiConstants.URL_VIDEO_PUBLISH)
            .post(requestBody)
            .build();
        mHttpEngine.request(request, LittleHttpResponse.LittlePublishResponse.class, callback);
    }

    /**
     * 删除视频
     *
     * @param token    token
     * @param userId   userId
     * @param videoId  videoId
     * @param callback callback
     */
    public void requestDeleteVideo(String token, String userId, String videoId,
                                   HttpEngine.HttpResponseResultCallback<LittleHttpResponse
                                       .LittleMyVideoListResponse> callback) {

        RequestBody requestBody = new FormBody.Builder()
            .add(AlivcLittleHttpConfig.RequestKey.FORM_KEY_PACKAGE_NAME, mPackageName)
            .add(AlivcLittleHttpConfig.RequestKey.FORM_KEY_USER_ID, userId)
            .add(AlivcLittleHttpConfig.RequestKey.FORM_KEY_TOKEN, token)
            .add(AlivcLittleHttpConfig.RequestKey.FORM_KEY_VIDEO_ID, videoId)
            .build();

        Request request = new Request.Builder().url(AlivcLittleServerApiConstants.URL_DELETE_VIDEO)
            .post(requestBody)
            .build();
        mHttpEngine.request(request, LittleHttpResponse.LittleMyVideoListResponse.class, callback);
    }

    /**
     * 拼接get 请求的url
     *
     * @param baseUrl baseUrl
     * @param params  要拼接的参数
     * @return
     */
    private String urlAppend(String baseUrl, HashMap<String, String> params) {
        StringBuffer absUrl = new StringBuffer();
        int size = 0;
        if (params != null && params.size() > 0) {
            Set<Map.Entry<String, String>> entries = params.entrySet();
            for (Map.Entry<String, String> item : entries) {
                if (size == 0) {
                    absUrl.append(baseUrl).append("?" + item.getKey() + "=" + item.getValue());
                    size++;
                } else {
                    absUrl.append("&" + item.getKey() + "=" + item.getValue());
                }
            }
        }
        return absUrl.toString();
    }

    public void cancelRequest(String cancelUrl) {
        mHttpEngine.cancel(cancelUrl);
    }

}
