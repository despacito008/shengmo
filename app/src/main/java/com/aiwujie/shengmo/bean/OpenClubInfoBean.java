package com.aiwujie.shengmo.bean;

/**
 * @ProjectName: workspace2
 * @Package: com.aiwujie.shengmo.bean
 * @ClassName: OpenClubInfoBean
 * @Author: xmf
 * @CreateDate: 2022/6/1 16:44
 * @Description:
 */
public class OpenClubInfoBean {

    /**
     * retcode : 2000
     * msg : 操作成功
     * data : {"intro":"Q：什么是粉丝团？\n粉丝团是由主播粉丝组成的群体组织，是主播在圣魔平台人气象征之一。粉丝勋章是主播提供给粉丝团成员的可佩戴勋章，\n是主播与成员日积月累亲密度的体现。粉丝加入粉丝团，获取和主播的亲密度，提升自己的粉丝勋章。\n\n\nQ:主播如何开通粉丝团？\n主播在我的 - 粉丝团 - 粉丝团列表页面可以申请开通粉丝团，并设置团名。成功开通之后会自动获得一个专属的粉丝群，粉丝入团之后\n会自动加入粉丝群。粉丝群是主播和粉丝的交流中心。"}
     */

    private int retcode;
    private String msg;
    private DataBean data;

    public int getRetcode() {
        return retcode;
    }

    public void setRetcode(int retcode) {
        this.retcode = retcode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * intro : Q：什么是粉丝团？
         粉丝团是由主播粉丝组成的群体组织，是主播在圣魔平台人气象征之一。粉丝勋章是主播提供给粉丝团成员的可佩戴勋章，
         是主播与成员日积月累亲密度的体现。粉丝加入粉丝团，获取和主播的亲密度，提升自己的粉丝勋章。


         Q:主播如何开通粉丝团？
         主播在我的 - 粉丝团 - 粉丝团列表页面可以申请开通粉丝团，并设置团名。成功开通之后会自动获得一个专属的粉丝群，粉丝入团之后
         会自动加入粉丝群。粉丝群是主播和粉丝的交流中心。
         */

        private String intro;

        public String getIntro() {
            return intro;
        }

        public void setIntro(String intro) {
            this.intro = intro;
        }
    }
}
