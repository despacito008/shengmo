package com.aliyun.apsara.alivclittlevideo.net.data;

/**
 * @author zsy_18 data:2019/3/7
 */
public class LittleImageUploadAuthInfo {

    /**
     * uploadAddress :
     * eyJFbmRwb2ludCI6Imh0dHBzOi8vb3NzLWNuLXNoYW5naGFpLmFsaXl1bmNzLmNvbSIsIkJ1Y2tldCI6Im91dGluLTEyZWJlMDFmMDI5ZDExZTliNjMzMDAxNjNlMWM4ZGJhIiwiRmlsZU5hbWUiOiJpbWFnZS9jb3Zlci80REY5NTE2Nzk2NUQ0OUM4OUFBNUIzRUUwRUYzN0U0Qi02LTIucG5nIn0=
     * uploadAuth :
     * eyJTZWN1cml0eVRva2VuIjoiQ0FJUzB3UjFxNkZ0NUIyeWZTaklyNGorQ096SG02MFg0S2l2VjIzWnNYRTJYTUZibnIvQXNqejJJSGxQZTNGaEFPb2V2L2svbVc5VTdmb2NsclVxRXNjZUhCQ1lNSkFyc3M0SnFsUC9KcGZadjh1ODRZQURpNUNqUVlKUXhMWUdtWjI4V2Y3d2FmK0FVQkxHQ1RtZDVNQVlvOWJUY1RHbFFDWnVXLy90b0pWN2I5TVJjeENsWkQ1ZGZybC9MUmRqcjhsbzF4R3pVUEcyS1V6U24zYjNCa2hsc1JZZTcyUms4dmFIeGRhQXpSRGNnVmJtcUpjU3ZKK2pDNEM4WXM5Z0c1MTlYdHlwdm9weGJiR1Q4Q05aNXo5QTlxcDlrTTQ5L2l6YzdQNlFIMzViNFJpTkw4L1o3dFFOWHdoaWZmb2JIYTlZcmZIZ21OaGx2dkRTajQzdDF5dFZPZVpjWDBha1E1dTdrdTdaSFArb0x0OGphWXZqUDNQRTNyTHBNWUx1NFQ0OFpYVVNPRHREWWNaRFVIaHJFazRSVWpYZEk2T2Y4VXJXU1FDN1dzcjIxN290ZzdGeXlrM3M4TWFIQWtXTFg3U0IyRHdFQjRjNGFFb2tWVzRSeG5lelc2VUJhUkJwYmxkN0JxNmNWNWxPZEJSWm9LK0t6UXJKVFg5RXoycExtdUQ2ZS9MT3M3b0RWSjM3V1p0S3l1aDRZNDlkNFU4clZFalBRcWl5a1Qwa0ZncGZUSzFSemJQbU5MS205YmFCMjUvelcrUGREZTBkc1Znb0pWS0RwaUdXRzNSTE5uK3p0Sjl4YmtlRStzS1VsZmJCK1o0NFNRVjJ2SUZUVkZpSUlOd3o5QWMrdS9Mc3RCbksrNy92V0hudC8yOHg5ZFNmdmFzM3NCVTBMNmI4M3JYTjVHV0c1Q0xPT3BOVXdwbUhCRGRkSmoyc1lHRjh6ZnlvZ1hZS21nc01pV25jT1d4RXNnL09qVGZwSnBWS2o2eldtUzhmWHZsSjVjM2NTaWE5K0Z0bkJlbUE2cTB3UmZoWWUrUkRRbWtjWTdMYU1CV01Hb0FCSGJXQXQ1a3RaVlhVQlRjRVlMQ0RyazhJbGhLYkFjbS9jR215TEthTE5tV2s2dkVZdzA0UVlIMXREMzlPMkQvbVJVNzdoQWdHeFVMTTJTeldGYXNNYmJLQXpDOTlwQ21nZjV4OEUwM1lON1pVTVg0azFLMWxzWCtxamhLREdROEdySlcrMDV0UENWd0w2ODlaQ3hFOEpjd21VQkpkQ1FjczRjbktlbzFXYXVjPSIsIkFjY2Vzc0tleUlkIjoiU1RTLk5LS0NWc3ZyNldqRFVLaFVxY1BOd3F6a1AiLCJFeHBpcmVVVENUaW1lIjoiMjAxOS0wMy0wN1QxMjowMzo1M1oiLCJBY2Nlc3NLZXlTZWNyZXQiOiJGMTlDbThiRDY5U1lzdkE3OGpEUkZiRnlDU0hIa0Q1VmpEdktER1lkZUtlbiIsIkV4cGlyYXRpb24iOiIzNjAwIiwiUmVnaW9uIjoiY24tc2hhbmdoYWkifQ==
     * imageId : 0bf9abc16c5d444084b3012cfb160202
     * imageURL : http://alivc-demo-vod.aliyuncs.com/image/cover/4DF95167965D49C89AA5B3EE0EF37E4B-6-2.png
     */

    private String uploadAddress;
    private String uploadAuth;
    private String imageId;
    private String imageURL;

    public String getUploadAddress() {
        return uploadAddress;
    }

    public void setUploadAddress(String uploadAddress) {
        this.uploadAddress = uploadAddress;
    }

    public String getUploadAuth() {
        return uploadAuth;
    }

    public void setUploadAuth(String uploadAuth) {
        this.uploadAuth = uploadAuth;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
