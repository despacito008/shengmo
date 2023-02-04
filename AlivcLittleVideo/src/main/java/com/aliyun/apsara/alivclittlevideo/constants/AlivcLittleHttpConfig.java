package com.aliyun.apsara.alivclittlevideo.constants;

/**
 * http config
 * 主要是表单请求的key值
 *
 * @author xlx
 */
public class AlivcLittleHttpConfig {

    public interface RequestKey {
        String FORM_KEY_USER_ID = "userId";
        String FORM_KEY_TOKEN = "token";
        String FORM_KEY_NEW_NICK_NAME = "nickname";
        String FORM_KEY_PAGE_INDEX = "pageIndex";
        String FORM_KEY_PAGE_SIZE = "pageSize";
        String FORM_KEY_ID = "id";

        String FORM_KEY_VIDEO_ID = "videoId";
        String FORM_KEY_VIDEO_TITLE = "title";
        String FORM_KEY_VIDEO_DESCRIBE = "description";
        String FORM_KEY_VIDEO_COVER = "coverUrl";
        String FORM_KEY_VIDEO_DURATION = "duration";
        String FORM_KEY_VIDEO_SIZE = "size";
        String FORM_KEY_VIDEO_TAGS = "tags";
        String FORM_KEY_VIDEO_CATE_ID = "cateId";
        String FORM_KEY_VIDEO_CATE_NAME = "cateName";

        String FORM_KEY_VIDEO_FILE_NAME = "fileName";

        String FORM_KEY_IMAGE_TYPE = "imageType";
        String FORM_KEY_PACKAGE_NAME = "PACKAGE_NAME";
    }
    /**
     * 视频列表每页数据
     */
    public static final int DEFAULT_PAGE_SIZE = 10;
}
