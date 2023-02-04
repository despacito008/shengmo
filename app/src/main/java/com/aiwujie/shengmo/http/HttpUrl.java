package com.aiwujie.shengmo.http;

import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.timlive.bean.ChatRoom;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;

import org.feezu.liuli.timeselector.Utils.TextUtil;

import java.util.List;

/**
 * Created by 290243232 on 2016/12/17.
 */
public class HttpUrl {

    public static String baseUrl = "";

    public static String NetPic() {
        if (TextUtil.isEmpty(baseUrl)) {
            baseUrl = TextUtil.isEmpty(SharedPreferencesUtils.getParam(MyApp.getInstance(), "api_host", "").toString())
                    ? "http://cs.aiwujie.net/" :
                    SharedPreferencesUtils.getParam(MyApp.getInstance(), "api_host", "").toString();
        }
        return baseUrl;
    }

    public static String getImagePath(String url) {
        String imgPreUrl = TextUtil.isEmpty(SharedPreferencesUtils.getParam(MyApp.getInstance(), "image_host", "").toString())
                    ? "http://cs.aiwujie.net/" :
                    SharedPreferencesUtils.getParam(MyApp.getInstance(), "image_host", "").toString();

        return imgPreUrl + url;
    }


    //测试服域名
    public static String TestAdrr = "http://tx.aiwujie.com.cn/";
    //服务器IP
//    public static String NetPic()Ip = "http://59.110.28.150:888/";
    //图片前缀     所有接口前缀  正式的
    //public static String NetPic() = "http://hao.shengmo.org/";
    //正式的
//    public static String NetPic() = "http://39.105.50.200:888/";
//    public static String NetPic = ((String) SharedPreferencesUtils.getParam(MyApp.getInstance(), "base_url", "http://cs.aiwujie.net/")).length() == 0 ? "http://cs.aiwujie.net/" : ((String) SharedPreferencesUtils.getParam(MyApp.getInstance(), "base_url", "http://cs.aiwujie.net/"));
    //    public static String NetPic() = ((String) SharedPreferencesUtils.getParam(MyApp.getInstance(),"base_url",""));

    //测试
//    public static String NetPic() ="http://cs.aiwujie.net/";
    //注册（最新）
    public static String Registernewrd = "Api/Users/registernewrd";
    //注册获取验证码
    public static String SendVercode = "api/api/sendVerCode";
    //登录
    public static String Login = "Api/Users/login_pw";
    //忘记密码发送验证短信
    public static String ForPswVercode = "Api/Api/sendVerCode_forget";
    //忘记密码
    public static String ForPsw = "Api/Users/forgotPassword";
    //判断用户是否被封号
    public static String JudgeUserStatus = "Api/Other/judgeUserStatus";
    //第三方登陆开关
    public static String thirdParty = "api/power/thirdParty";
    //设置自由或指定
    public static String setChatMicType = "api/power/setChatMicType";
    //聊天室排麦列表
    public static String getChatListMic = "api/power/getChatListMic";
    //聊天室排麦
    public static String setChatListMic = "api/power/setChatListMic";
    //修改聊天室连麦人数
    public static String editChatMicNum = "api/power/editChatMicNum";
    //gid uid 返回 cardname
    public static String getGroupCardName = "Api/friend/getGroupCardName";
    //修改群名片
    public static String editGroupCardName = "Api/friend/editGroupCardName";
    //修改聊天室介绍
    public static String editChatinfo = "api/power/editChatinfo";
    //修改聊天室昵称
    public static String editChatName = "api/power/editChatName";
    //修改聊天室头像
    public static String editChatPic = "api/power/editChatPic";
    //获取聊天室信息
    public static String getChatInfo = "api/power/getChatInfo";
    //聊天室上麦按钮
    public static String chatmikeOpenOrClose = "api/power/chatmikeOpenOrClose";
    //头条
    public static String topnew = "api/index/topnew";
    //将好友移除分组
    public static String delfgusers = "Api/friendgroup/delfgusers";
    //获取分组内好友列表
    public static String getgfuserslist = "Api/friendgroup/getgfuserslist";
    //添加一个好友入多个分组
    public static String setfgsusers = "Api/friendgroup/setfgsusers";
    //添加多个好友入一个分组
    public static String setfgusers = "Api/friendgroup/setfgusers";
    //修改分组名称
    public static String updfriendgroup = "Api/friendgroup/updfriendgroup";
    //分组删除
    public static String delfriendgroup = "Api/friendgroup/delfriendgroup";
    //分组排序
    public static String friendgroupsort = "Api/friendgroup/friendgroupsort";
    //获取分组
    public static String friendgrouplist = "Api/friendgroup/friendgrouplist";
    //添加分组
    public static String addfriendgroup = "Api/friendgroup/addfriendgroup";
    //聊天室获取人
    public static String chatroomUserlist = "Api/Power/chatroomUserlist";
    //聊天室上麦
    public static String chatroomUpMicrophone = "Api/power/chatroomUpMicrophone";
    //聊天室下麦
    public static String chatroomDownMicrophone = "Api/power/chatroomDownMicrophone";
    //聊天室开关
    public static String chatroomOpenOrClose = "Api/power/chatroomOpenOrClose";
    //聊天室权限
    public static String chatroomUserinfo = "Api/Power/chatroomUserinfo";
    //设置备注
    public static String markName = "Api/users/markName";
    //聊天室-禁言相关-操作
    public static String chatroomUserGagAction = "Api/power/chatroomUserGagAction";
    //聊天室-踢出相关-操作
    public static String chatroomUserBlockAction = "Api/power/chatroomUserBlockAction";
    //uid 返回token
    public static String login_third = "Api/Users/login_third";
    //黑v查看违规次数
    public static String detailMobile = "Api/users/detailMobile";
    //全隐身
    public static String hideStateSwitch = "/Api/users/hideStateSwitch";
    //悄悄关注列表
    public static String getFollowListQueit = "/api/friend/getFollowListQueit";
    //悄悄取消关注
    public static String overfollowquiet = "/Api/friend/overfollowquiet";
    //悄悄关注
    public static String followOneBoxQuiet = "/Api/friend/followOneBoxQuiet";
    //悄悄关注人数
    public static String getFollowCountQueit = "/Api/friend/getFollowCountQueit  ";
    //管理员标记
    public static String setDynamicAuditMark = "/Api/dynamic/setDynamicAuditMark";
    //新用户判断可疑
    public static String getGirlState = "/Api/users/getGirlState";
    //发红包
    public static String giveRedbag = "/Api/Ping/giveRedbag";
    //取红包
    public static String takeRedbag = "/Api/Ping/takeRedbag";
    //群领红包
    public static String qunGetRedbag = "/Api/Ping/qunGetRedbag";
    //群发红包
    public static String qunGiveRedbag = "/Api/Ping/qunGiveRedbag";
    //群红包判断是否领取
    public static String yetRedbag = "/Api/ping/yetRedbag";

    //获取两个人所有红包的领取状态
    public static String getAllRedBagStatus = "api/ping/getAllRedBagstatus";

    //领取群聊红包列表
    public static String getQunRedbagList = "/Api/Ping/getQunRedbagList";
    //身份证认证-操作
    public static String setrealidcard = "/Api/Other/setrealidcard";
    //身份证认证-显示
    public static String getrealidstate = "/Api/Other/getrealidstate";
    //群领礼物列表
    public static String getQunGiftList = "Api/friend/getQunGiftList";
    //打开关闭查询地理位置
    public static String placeIntercalate = "/Api/users/placeIntercalate";
    //详细描述
    public static String lmarkName = "Api/users/lmarkName";
    //昵称备注-显示
    public static String markNameshow = "/Api/users/markNameshow";
    //首页数据（新）
    public static String UserListNewrd = "Api/index/userListNewrd";
    //首页数据（最新）
    public static String UserListNewth = "Api/index/userListNewth340";
    //登录时间
    public static String LogintimeAndLocation = "Api/Other/setLogintimeAndLocation";
    //广告
    public static String GetBanner = "Api/Other/getSlideNew";
    //轮播广告
    public static String GetSlideMore = "Api/Other/getSlideMore";
    //搜索用户（新）
    public static String SearchUserNewrd = "Api/index/searchUserNewrd";
    //管理员备注-操作
    public static String editAdminmrak = "Api/Power/editAdminmrak";
    //管理员备注-显示
    public static String adminmark = "/Api/Power/adminmark";
    //使用推顶卡-推顶动态
    public static String useTopcard = "/Api/power/useTopcard";
    //魔豆兑换-操作
    public static String exBeans = "/Api/Ping/exBeans";
    //Vip购买记录
    public static String vipbuylist = "/Api/users/vipbuylist";
    //Vip获赠记录
    public static String vipgiftlist = "/Api/users/vipgiftlist";
    //魔豆推顶卡
    public static String topcard_baans = "Api/Ping/topcard_baans_android";
    //推顶卡余额
    public static String getTopcardPageInfo = "/Api/users/getTopcardPageInfo";
    //推顶卡购买记录
    public static String getTopcardPaymentRs = "/Api/users/getTopcardPaymentRs";
    //推顶记录查询
    public static String getTopcardUsedRs = "/Api/users/getTopcardUsedRs";
    //搜索用户（最新）
    public static String SearchUserNewth = "Api/index/searchUserNewth340";
    //地图找人
    public static String SearchMap = "Api/index/searchByMap";
    //地图找人（新）
    public static String SearchByMapNew = "Api/index/searchByMapNew";
    //用户个人信息
    public static String GetUserInfoOnly = "Api/friend/getUserInfoOnly";
    //用户个人信息（最新，仅获取个人资料）
    public static String GetUserInfo = "Api/friend/getUserInfo340";
    public static String GetNewUserInfo = "/Api/UserInfo/newGetUserInfo";
    //获取个人中心页基本资料
    public static String OwnerInfo = "Api/users/getmineinfo";
    //关注(新)
    public static String FollowNew = "Api/friend/followOneNew";
    //关注(最新)
    public static String FollowOneBox = "Api/friend/followOneBox";
    //取消关注
    public static String OverFollow = "Api/friend/overfollow";
    //获取谁看过我看过谁
    public static String ReadList = "Api/friend/getReadList";
    //修改资料时获取原有的资料
    public static String GetMineinfodetail = "Api/users/getmineinfodetail";
    //修改资料时获取原有的资料(新)
    public static String GetMineinfodetailNew = "Api/users/getmineinfodetailnew";
    //(新)提交修改资料
    public static String EditInfoNewrd = "Api/Users/editInfoNewrd";
    //(新)提交修改资料
    public static String EditInfo = "Api/Users/editInfo";
    //删除文件
    public static String DeletePic = "Api/Api/delPicture";
    //获取当前隐私设置的状态
    public static String GetSecretSit = "Api/Users/getSecretSit";
    //设置隐私状态（退出隐私设置页面是调用）
    public static String SetSecretSit = "Api/Users/setSecretSit";
    //获取VIP骚扰情况
    public static String GetVipSecretSit = "Api/users/getVipSecretSit";
    //设置VIP骚扰情况
    public static String SetVipSecretSit = "Api/users/setVipSecretSit";
    //查看用户是否设置相册密码
    public static String JudgePhotoPwd = "Api/users/judgePhotoPwd";
    //添加相册密码
    public static String AddPhotoPwd = "Api/users/addPhotoPwd";
    //修改相册密码
    public static String EditPhotoPwd = "Api/users/editPhotoPwd";
    //验证相册密码
    public static String ChargePhotoPwd = "Api/users/chargePhotoPwd";
    //添加用户到黑名单
    public static String SetOneToBlacklist = "Api/friend/setOneToBlacklist";
    //获取黑名单列表
    public static String GetBlackList = "Api/friend/getBlackList";
    //取消拉黑
    public static String CancelBlackState = "Api/friend/cancelBlackState";
    //获取关注、粉丝、好友列表
    public static String GetFollewingList = "Api/friend/getFollewingList350";
    //搜索、粉丝、好友列表
    public static String getAtList = "Api/friend/getAtList";
    //获取关注、粉丝、好友列表(新)
    public static String GetFollewingListFilter = "Api/friend/getFollewingListFilter";
    //修改密码
    public static String EditPwd = "Api/Users/editPwd";
    //我的钱包
    public static String Getmywallet = "Api/Users/getmywallet";
    //用户银行卡信息获取
    public static String Getbankcard = "Api/Users/getbankcard";
    //添加修改银行卡
    public static String Addbankcard = "Api/Users/addbankcard";
    //删除银行卡
    public static String Deletebankcard = "Api/Users/deletebankcard";
    //根据银行卡号获取银行名称
    public static String GetBanknameByBanknum = "Api/Users/getBanknameByBanknum";
    //提现（计算魔豆兑换的现金，入参计算后取整的魔豆和现金兑换量，判空判70整）
    public static String Tixian = "Api/Users/tixian";
    //提现（新）（计算魔豆兑换的现金，入参计算后取整的魔豆和现金兑换量，判空判1000整）
    public static String TixianNew = "Api/Users/tixiannew";
    //充值创建订单获取订单号
    public static String Czorder = "Api/Users/czorder";
    //意见反馈
    public static String Suggest = "Api/Other/suggest";
    //获取身份认证状态
    public static String Getidstate = "Api/Other/getidstate";
    //获取身份证认证状态
    public static String getrealidcard = "Api/Other/getrealidcard";
    //请求身份认证
    public static String Setidcard = "Api/Other/setidcard";
    //认证用户更换认证照片
    // 入参：用户-uid 需要更换的照片路径-card_replace
    public static String Replaceidcard = "Api/Power/replaceidcard";
    //帮助文档H5
    public static String Help = "index.php/Home/info/help";
    //会员协议H5
    public static String Vipregagreement = "index.php/Home/info/vipregagreement";
    //获取会员到期时间
    public static String GetVipOverTime = "Api/Vip/getVipOverTime";
    //获取会员到期时间（新）
    public static String GetVipOverTimeNew = "Api/Vip/getVipOverTimeNew";
    //获取vip会员商品
    public static String GetVipGoods = "Api/Vip/getVipPayType";
    //获取vip会员商品
    public static String GetPushGoods = "Api/Vip/getTopCardType";
    //获取vip会员商品
    public static String GetStampGoods = "Api/Vip/getStampType";
    //获取vip会员商品
    public static String GetBeansGoods = "Api/Vip/getBeansType";
    //获取充值会员订单号
    public static String GetVipOrder = "Api/Vip/getVipId";
    //充值会员获取PING++charge
    public static String Vipcharge = "Api/Ping/vipcharge";
    //充值超级会员获取PING++charge
    public static String Svipcharge = "Api/Ping/svipcharge";
    //获取余额记录
    public static String GetWalletRecord = "Api/Users/getWalletRecord";
    //获取用户头像昵称
    public static String GetHeadAndNickname = "Api/Other/getHeadAndNickname";
    //获取用户头像昵称（新）
    public static String GetHeadAndNicknameNew = "Api/Other/getHeadAndNicknameNew";
    //联系我们
    public static String GetCall = "Api/Other/getCallAct";
    //圣魔客服ID
    public static String SMKF = "KEFU148492045558421";
    //创建群
    public static String MakeGroup = "Api/friend/addGroup";
    //(新)获取筛选群列表
    public static String GetGroupListFilter = "Api/friend/getGroupListFilter";
    //获取群资料（返回字段userpower小于1时，用户不能修改群资料，不等于2时，解散群组按钮禁用，-1时显示已申请加入群，等于2时设置管理员）
    public static String GetGroupinfo = "Api/friend/getGroupinfo";
    //获取群成员
    //入参：用户-uid 群id-gid 纬度-lat 经度-lng 分页数-page 获取类型-pagetype（获取所有时不传，获取群资料页传1仅返回6个成员）
    public static String GetGroupMember = "Api/friend/getGroupMember";
    //超过七天未登录人员，踢除出群接口
    public static String shotOffmore = "/Api/friend/shotOffmore";
    //修改群资料
    public static String EditGroupInfo = "Api/friend/editGroupInfo";
    //申请加入某群（判断是否满员）
    public static String JoinGroup = "Api/friend/getIntoGroupOne";
    //搜索群
    //入参：搜索内容-search 用户-uid 获取类型-type（0：最近 1：最火） 纬度-lat 经度-lng 分页数-page
    public static String SearchGroup = "Api/friend/searchGroup";
    //获取群消息
    public static String GetGroupMsg = "Api/friend/getGroupMsg350";
    //同意某人加入群
    public static String AgreeOneInto = "Api/friend/agreeOneInto";
    //拒绝某人加入群
    public static String RefuseOneInto = "Api/friend/refuseOneInto";
    //清空群消息
    public static String DelGroupMsg = "Api/friend/delGroupMsg";
    //退出群
    public static String ExitGroup = "Api/friend/exitGroup";
    //将某人踢出群
    public static String TiGroup = "Api/friend/shotOffone";
    //设置管理员
    public static String SetManager = "Api/friend/setManager";
    //取消管理员
    public static String CancelManager = "Api/friend/cancelManager";
    //解散群
    public static String DelGroup = "Api/friend/delGroup";
    //获取群信息
    public static String GroupUserInfo = "Api/Other/getGroupinfo";
    //获取群成员id
    public static String GetGroupMemberId = "Api/Other/getGroupMember";
    //发布动态
    public static String SendDynamic = "Api/Dynamic/sendDynamic";
    //获取动态详情（新）
    public static String GetDynamicdetailNewth = "Api/Dynamic/getDynamicdetailNewth";
    //获取编辑动态详情
    public static String getDynamicdetailNewFiveEdit350 = "/Api/Dynamic/getDynamicdetailNewFiveEdit350";
    //获取动态详情（最新）
    public static String GetDynamicdetailFive = "/Api/Dynamic/getDynamicdetailNewFive350";
    //获取动态列表(最新)
    public static String GetDynamicListNewFive = "/Api/dynamic/getDynamicListLatestFive350";
    //获取动态列表(热帖)
    public static String GetHotDynamicListed = "Api/Dynamicf/getHotDynamicListed";
    //为动态点赞（最新）
    public static String LaudDynamicNewrd = "Api/Dynamic/laudDynamicNewrd";
    //取消点赞
    public static String CancelLaud = "Api/Dynamic/cancelLaud";
    //为动态打赏
    public static String RewardDynamic = "Api/Dynamic/RewardDynamic";
    //为动态打赏（新）
    public static String RewardDynamicNew = "/Api/Dynamic/RewardDynamicnew";
    //获取点赞的人列表
    public static String GetLaudList = "Api/Dynamic/getLaudList";
    //获取点赞的人列表（新）
    public static String GetLaudListNew = "Api/Dynamic/getLaudListNew";
    //获取动态评论列表
    public static String GetCommentList = "Api/Dynamic/getCommentList";
    //获取动态评论列表（新）
    public static String GetCommentListNew = "Api/Dynamic/getNewCommentList";
    //评论动态（新）
    public static String SendCommentNewrd = "Api/Dynamic/sendCommentNewrd";
    //评论动态（最新）
    public static String SendCommentNewred = "Api/NewDynamic/newSendComment";
    //获取评论详情
    public static String GetCommentInfo = "Api/NewDynamic/getDynamicCommentInfo";
    //评论点赞
    public static String ThumnUpComment = "Api/NewDynamic/likeComment";
    //@我的
    public static String AtUsData = "Api/NewDynamic/getAtuserList";

    //获取打赏的人列表
    public static String GetRewardList = "Api/Dynamic/getRewardList";
    //获取打赏的人列表（新）
    public static String GetRewardListNew = "Api/Dynamic/getRewardListNew";
    //删除动态
    public static String DelDynamic = "Api/Dynamic/delDynamic";
    //黑V删除动态
    public static String AdminDelDynamic = "Api/Power/delDynamic";
    //获取消费记录
    public static String GetPayRecord = "Api/Users/getPayRecord";
    //举报
    public static String Report = "Api/Other/report";
    //获取动态消息未读数
    //入参：当前登录用户-uid 获取类型-type（0：分别 1：所有）
    public static String GetUnreadNum = "Api/Dynamic/getUnreadNum";

    //清除动态消息未读数
    // 入参：当前登录用户-uid 获取类型-type（1：点赞 2：打赏 3：评论）
    public static String ClearUnreadNum = "Api/Dynamic/clearUnreadNum";
    // 清除所有动态消息未读数  入参：当前登录用户-uid
    public static String ClearUnreadNumAll = "api/dynamic/clearUnreadNumAll";
    //获取赞过我的人列表
    //  入参：分页数-page 用户-uid
    public static String GetLaudedList = "Api/Dynamic/getLaudedList";
    // 获取打赏过我的人列表
    public static String GetRewardedList = "Api/Dynamic/getRewardedList";
    //获取我的所有评论列表
    public static String GetCommentedList = "Api/Dynamic/getCommentedList";
    //发现
    public static String FindDetail = "index.php/Home/info/finddetail/state";
    //添加修改音频介绍
    public static String MediaEdit = "Api/users/mediaEdit";
    //删除音频介绍（调用同时调用删除文件接口删除源文件）
    public static String MediaDel = "Api/users/mediaDel";
    //验证第三方OPEN_ID是否注册
    // 入参：open_id
    //2000, '登录成功！',$userinfo
    //4000, '未注册！'
    public static String ChargeOpenid = "api/users/charge_openid";
    //删除评论
    public static String DelComment = "Api/Dynamic/delComment";
    //获取发现H5URL
    public static String GetFindUrl = "Api/Other/getFindUrl";
    //获取用户加入的所有群groupId
    public static String GetUserGroupId = "Api/Other/getUserGroup";
    //是否更新(新)
    public static String JudgeVersionNew = "Api/Other/judgeVersionNew";
    //获取某人距上次使用APP的各种红点和数
    public static String GetRedDutNum = "Api/Other/getRedDutNewNum350";
    //注册相关验证
    //1.判断手机号、邮箱、密码是否存在
    public static String ChargeFrist = "api/users/chargeFrist";
    //1.判断手机号、邮箱、密码是否存在(新)
    public static String ChargeFristNew = "api/users/chargeFristNew";
    //2.判断昵称是否存在
    public static String ChargeSecond = "api/users/chargeSecond";
    //图文规范
// public static String PicTextHtml="http://hao.shengmo.org:888/index.php/Home/Info/specification.html";
    public static String PicTextHtml = "Home/Info/Shengmosimu/id/11";
    //隐私协议
//    public static String SecretHtml = "http://hao.shengmo.org:888/Home/info/secretAgreement";
    public static String SecretHtml = "Home/info/secretAgreement";
    //@群成员列表
    public static String AtGroupMember = "api/friend/getAtGroupMember";
    //禁言某人
    public static String BanSomeOne = "Api/friend/gagSomeOne";
    //解除禁言
    public static String RmoveGagSomeOne = "Api/friend/removeGagSomeOne";
    //判断是否可以发布动态（新）
    public static String JudgeDynamicNew = "Api/Dynamic/judgeDynamicNew";
    //判断是否可以发布动态（最新）
    public static String JudgeDynamicNewrd = "Api/Dynamic/judgeDynamicNewrd";
    //被打赏排行榜(财富榜)
    //入参：分页数-page 类型-type（ 0：日榜 1：周榜 2：月榜）
    public static String GetBeRewardedRankingList = "api/dynamic/getBeRewardedRankingList";
    //打赏排行榜
    //入参：分页数-page 类型-type（ 0：日榜 1：周榜 2：月榜）
    public static String GetRewardRankingList = "api/dynamic/getRewardRankingList";
    //被评论排行榜
    //入参：分页数-page 类型-type（ 0：日榜 1：周榜 2：月榜）
    public static String GetBeCommentedRankingList = "api/dynamic/getBeCommentedRankingList";
    //被点赞排行榜
    //入参：分页数-page 类型-type（ 0：日榜 1：周榜 2：月榜）
    public static String GetBeLaudedRankingList = "api/dynamic/getBeLaudedRankingList";
    //动态排行榜，推荐排行榜
    // 入参：分页数-page 类型-type（ 0：日榜 1：周榜 2：月榜）推荐动态-recommend（0，动态，1，推荐）
    public static String GetSendDynamicRandkingList = "api/dynamic/getSendDynamicRandkingList";
    //评论排行榜
    //入参：分页数-page 类型-type（ 0：日榜 1：周榜 2：月榜）
    public static String GetCommentRankingList = "api/dynamic/getCommentRankingList";
    //点赞排行榜
    //入参：分页数-page 类型-type（ 0：日榜 1：周榜 2：月榜）
    public static String GetLaudRankingList = "api/dynamic/getLaudRankingList";
    //获取账号绑定状态
    public static String GetBindingState = "Api/Users/getBindingState";
    //绑定手机  //更换绑定时传change参数，参数为1.不是更换不传
    public static String BindingMobile = "Api/Users/bindingMobile";
    //换绑手机号获取验证码
    public static String SendVerCode_change = "Api/Api/sendVerCode_change";
    //换绑邮箱获取验证码
    public static String SendEmailCode_change = "Api/Api/sendEmailCode_change";
    //绑定邮箱  //更换绑定时传change参数，参数为1.不是更换不传
    public static String BindingEmail = "Api/Users/bindingEmail";
    //更换绑定手机号和邮箱验证验证码
    public static String ChargeMCodeByBinding = "Api/Users/chargeMCodeByBinding";
    //绑定第三方账号
    public static String BindingOther = "Api/Users/bindingOther";
    //注册获取验证码（邮箱）
    public static String SendMailCode_reg = "api/api/sendMailCode_reg";
    //解绑第三方账号
    public static String RemoveOther = "Api/Users/removeOther";
    //忘记密码发送验证邮件
    public static String SendMailCode_forget = "Api/Api/sendMailCode_forget";
    //获取防骚扰选项状态
    public static String GetRestrictState = "Api/Users/getRestrictState";
    //设置防骚扰选项
    public static String SetRestrictState = "Api/Users/setRestrictState";
    // 判断是否可以创建会话
    //入参：当前登录用户-uid 想要创建会话用户-otheruid
    public static String GetOpenChatRestrictSvip = "Api/Restrict/getOpenChatRestrictSvip";
    // 判断是否可以创建会话（新）
    //入参：当前登录用户-uid 想要创建会话用户-otheruid
    public static String GetOpenChatRestrictAndInfo = "Api/Restrict/getOpenChatRestrictAndInfo";
    //使用指定类型的邮票打开会话
    public static String UseStampToChatNew = "Api/Restrict/useStampToChatNew";
    //发送群邀请
    public static String InviteOneIntoGroup = "Api/Restrict/inviteOneIntoGroup";
    //发送群邀请
    public static String InviteOneIntoGroupNew = "Api/Restrict/inviteOneIntoGroupNew";
    //群主或管理员邀请同意直接加入群
    public static String AgreeIntoGroupOne = "Api/friend/agreeIntoGroupOne";
    //绿色斯慕
    public static String GreenSM = "Home/info/greensm";
    //全职招聘
    public static String Recruit = "Home/Info/Shengmosimu/id/14";
    //志愿者
    public static String Volunteer = "Home/Info/Shengmosimu/id/13";
    //圣魔站
    //public static String SMaddress = "http://www.aiwujie.net";
    public static String SMaddress = "http://www.shengmo.cn";
    //圣魔动态详情分享
    public static String ShareDynamicDetail = "Home/Share/dynamic/did/";
    //圣魔群组详情分享
    public static String ShareGroupDetail = "Home/Share/group/gid/";
    //圣魔个人详情分享
    public static String ShareUserDetail = "Home/Share/user/uid/";
    //魔豆购买会员
    public static String VipBeans = "Api/Ping/vip_beans";
    //魔豆充值超级会员
    public static String SvipBeans = "Api/Ping/svip_beans";
    //打赏某人
    public static String RewardOne = "Api/friend/RewardOne";
    //群领礼物
    public static String getQunGift = "Api/friend/getQunGift";
    //动态页大喇叭
    //入参：分页数-page 类型-type（0：礼物 1：会员）
    public static String GetPresentMsg = "api/dynamic/getPresentMsg";
    //推顶大喇叭
    public static String getTopcardUsedLb = "api/Users/getTopcardUsedLb";
    //获取喇叭红点状态
    public static String GetBigPresentNum = "api/dynamic/getBigPresentNum350";
    //获取我的礼物
    public static String GetMyPresent = "Api/Users/getMyPresent";
    //获取充值明细-消费记录-赠送记录
    //入参：用户-uid 分页数-page 获取类型-type（0：充值明细 1：礼物明细）
    public static String GetGivePsRerond = "Api/Users/getGivePsRerond";
    //获取充值明细-消费记录-兑换记录
    //入参：用户-uid 分页数-page 获取类型-type（0：充值明细 1：礼物明细）
    public static String GetExchangeRecord = "Api/Users/getExchangeRecord";
    //获取收到的礼物明细
    public static String GetReceivePresent = "Api/Users/getReceivePresent";
    //礼物明细-系统赠送
    public static String GetBasicGiveRecord = "Api/Users/getBasicGiveRecord";
    //礼物明细-兑换记录-提现记录
    public static String GetWithdrawedRecord = "Api/Users/getWithdrawedRecord";
    //土豪/魅力总榜
    public static String GetGiftAllList = "api/dynamic/getGiftAllList";
    //土豪日榜/周榜接口
    //类型-type（0：日榜 1：周榜）
    public static String GetWealthRankingList = "api/dynamic/getWealthRankingList";
    //魅力日榜/周榜
    public static String GetCharmRankingList = "api/dynamic/getCharmRankingList";
    //获取购买邮票支付凭证
    public static String StampCharge = "Api/Ping/stampcharge";
    //充值/礼物魔豆购买邮票
    public static String StampBaans = "Api/Ping/stamp_baans";
    //获取邮票页数据
    public static String GetStampPageInfo = "Api/Restrict/getStampPageInfo";
    //应用商店好评获取邮票
    public static String GetCMAppStamp = "Api/Users/getCMAppStamp";
    //分享APP获取奖励邮票
    public static String GetShareStamp = "Api/Users/getShareStamp";
    //获取我的系统礼物
    public static String GetMyBasicGift = "Api/Users/getMyBasicGift";
    //为动态打赏系统礼物
    public static String RewardBasicGDynamic = "api/dynamic/RewardBasicGDynamic";
    //打赏某人系统礼物
    public static String RewardOneBasicG = "Api/friend/RewardOneBasicG";
    //获取邮票购买记录
    public static String GetStampPaymentRs = "Api/Users/getStampPaymentRs";
    //获取系统邮票赠送记录
    public static String GetBasicStampGiveRs = "Api/Users/getBasicStampGiveRs";
    //获取邮票使用记录
    public static String GetStampUsedRs = "Api/Users/getStampUsedRs";
    //获取签到状态和记录
    public static String GetSignTimesInWeeks = "Api/Draw/getSignTimesInWeeks";
    //第一、二、三天签到获取礼物
    public static String SignOnDay1stTo3rd = "Api/Draw/signOnDay1stTo3rd";
    //第四天抽取小礼包
    public static String SignOnDay4th = "Api/Draw/signOnDay4th";
    //第五天签到获取通用邮票
    public static String SignOnDay5th = "Api/Draw/signOnDay5th";
    //第六天签到送系统邮票
    public static String SignOnDay6th = "Api/Draw/signOnDay6th";
    //第七天抽取大礼包
    public static String SignOnDay7th = "Api/Draw/signOnDay7th";
    //VIP置顶动态
    public static String SetStickDynamic = "Api/Power/setStickDynamic";
    //举报动态
    public static String ReportDynamic = "Api/Dynamic/reportDynamic";
    //志愿者推荐动态
    public static String RecommendDynamic = "Api/Power/recommendDynamic";
    //获取历史昵称
    public static String ls_name = "/Api/Users/getEditnicknameList";
    //获取用户身份信息
    public static String GetUserPowerInfo = "Api/Other/getUserPowerInfo";
    //创建话题接口
    public static String CreateTopic = "api/dynamic/createTopic";
    //关注话题
    public static String FollowTopic = "Api/Dynamic/followTopic";
    //取消关注话题
    public static String CancleFollowTopic = "Api/Dynamic/cancelFollowTopic";
    //获取关注话题列表
    public static String GetFollowTopicList = "api/dynamic/getFollowTopicList";
    //获取直播bunner和分类列表
    public static String GetLiveBannerAndLabel = "Api/Live/getLiveBannerAndLabel";
    //获取分类主播列表
    public static String GetTypeAnchor = "Api/Live/getTypeAnchor";
    //开播
    public static String beginToShow = "/Api/Cloudv/beginToShow";
    //获取关注主播列表
    public static String GetFollowAnchor = "Api/Live/getFollowAnchor";
    //获取开播权限
    public static String GetLiveAuth = "Api/Live/getLiveAuth";
    //获取话题列表
    public static String GetTopicList = "api/dynamic/getTopicList";
    //发布动态时候底部话题和公益页获取话题（入参pid为7）
    //入参：父分类-pid
    public static String GetTopicEight = "api/dynamic/getTopicEight";
    //获取动态页顶部热门话题7条
    public static String GetTopicDyHead = "api/dynamic/getTopicDyHead";
    //获取话题详情
    public static String GetTopicDetail = "api/dynamic/getTopicDetail";
    //取消置顶动态（自己查看的自己动态，是VIP，并且动态状态为置顶的显示此按钮）
    public static String SetUnStickDynamic = "Api/Power/setUnStickDynamic";
    //收藏动态
    public static String CollectDynamic = "api/dynamic/collectDynamic";
    //取消收藏动态
    public static String CancelCollectDynamic = "api/dynamic/cancelCollectDynamic";
    //获取收藏动态列表
    public static String GetCollectDynamicList = "api/dynamic/getCollectDynamicList";
    //获取动态热帖列表
    public static String GetHotDynamicList = "api/dynamic/getHotDynamicList";
    //超级管理员修改动态
    //（只能修改内容和图片，未修改传原内容，如删除图片需调用删除文件接口删除pic和sypic）
    public static String EditDynamic = "Api/Power/editDynamic";
    //获取群热搜词
    public static String GetGroupSearchText = "Api/friend/getGroupSearchText";
    //超级管理员封号
    //入参：被封用户-uid 当前登录用户-login_uid
    public static String ChangeStatus = "Api/Power/changeStatus";
    //红娘html5
    public static String HongniangHtml = "Home/Info/hongniang";
    //获取值班红娘数据
    public static String GetMatchmakerInfo = "Api/Matchmaker/getMatchmakerInfo";
    //获取才俊佳丽列表
    public static String GetMatchUsersList = "Api/Matchmaker/getMatchUsersList";
    //获取才俊佳丽会员主页
    public static String GetMatchUserInfo = "Api/Matchmaker/getMatchUserInfo";
    //获取是不是好友的状态  2001为好友 2002不是好友
    public static String GetAchievePower = "Api/Power/getAchievePower";
    //获取客服数据
    public static String GetServiceInfo = "Api/Index/getServiceInfo";
    //普通用户每日获取邮票
    public static String NarmalUserGetBasicStamp = "Api/Restrict/narmalUserGetBasicStamp";
    //VIP用户每日获取邮票
    public static String ViplUserGetBasicStamp = "Api/Restrict/vipUserGetBasicStamp";
    //获取关注消息
    public static String GetFollowMessage = "Api/friend/getFollowMessage";
    //清空关注消息
    public static String DelFollowMessage = "Api/friend/delFollowMessage";
    //动态和话题统计的接口
    public static String GetDynamicAndTopicCount = "api/friend/getDynamicAndTopicCount";
    //个人信息写入浏览记录
    public static String WriteVisitRecord = "api/friend/writeVisitRecord";
    //隐藏动态
    public static String DynamicForbid = "Api/Power/editHiddenState/method/forbid";
    //取消隐藏动态
    public static String DynamicResume = "Api/Power/editHiddenState/method/resume";
    //获取某人参与的评论列表（好友和会员有权限调用）
    public static String GetCommentedListOnUserInfo = "api/dynamic/getCommentedListOnUserInfo";
    //获取公益页官方公告
    public static String GetBasicNews = "Api/Index/getBasicNews";
    //获取更多公益页官方公告
    public static String getBasicNewsList = "Api/Index/getBasicNewsList";
    //获取公益页常见问题
    public static String GetQuestions = "Api/Index/getSamerNews";
    //获取更多公益页常见问题
    public static String getQuestionsList = "Api/Index/getSamerNewsList";
    //获取启动页广告
    //入参：适配设备：state（5：安卓，6：5，7：6，8：6p，9：x）
    public static String GetSlideStartup = "Api/Other/getSlideStartup";


    public static String GetBaseUrl = "Api/Index/newGetQequestIp";
    //黑V在客户端封禁动态三天
    public static String ChangeDynamicStatus = "Api/Power/changeDynamicStatus";
    //黑V操作// 入参：被删除的用户-uid 当前操作用户-login_uid 操作类型-type 1.违规头像 2.违规昵称 3.违规签名 4.违规相册
    public static String delIllegallyUserInfo = "Api/Power/delIllegallyUserInfo";
    //黑v设为可疑用户
    public static String SetOneLiker = "Api/Power/setLikeLiarState/method/setone";
    //黑V取消可疑用户
    public static String RemoveOneLiker = "Api/Power/setLikeLiarState/method/removeone";
    //黑V取消可疑用户
    public static String OperateOneLiker = "Api/Power/setLikeLiarState";
    //认证用户设置查看认证照片权限  入参status-vip,nobody。vip-vip可查看，nobody-所有人不可见
    public static String SetRealnameState = "Api/Power/setRealnameState";
    //获取用户所有权限封禁状态
    public static String GetAllUserStatus = "Api/Power/getAllUserStatus";
    //黑V封禁接口   入参method 1-账号 2-动态 3-资料 4-聊天  5 禁止加群 被封禁用户-uid 当前操作用户-login_uid 封禁天数-blockingalong（永久封禁传0）
    public static String ChangeAllUserStatus = "Api/Power/changeAllUserStatus";
    //黑V解除封禁   入参method 1-账号 2-动态 3-资料 4-聊天 forbid-封禁设备 resume-解封设备 被封禁用户-uid 当前操作用户-login_uid
    public static String RecoverAllUserStatus = "Api/Power/recoverAllUserStatus";
    //警示公布列表
    public static String GetPunishmentList = "Api/Index/publishViolationUser";
    //编辑警示公布
    public static String EditPunishment = "Api/Index/editPublishViolation";
    //黑V封禁、解封设备  入参-method   forbid-封禁  resume-解封
    public static String ChangeDeviceStatus = "Api/Power/changeDeviceStatus";
    //获取警示榜用户列表 入参-type 1.违规用户 2.可疑用户 3.封号用户
    public static String GetOutOfLineList = "Api/index/getOutOfLineList";
    //黑v权限   入参method 0-推荐动态 1-置顶动态
    public static String SetDynamicRerommend = "Api/Power/setDynamicRerommend";
    //黑v权限   入参method 0-取消推荐动态 1-取消置顶动态
    public static String CancelDynamicRerommend = "Api/Power/cancelDynamicRerommend";
    //获取用户红娘牵线会员状态
    public static String GetMatchState = "Api/Matchmaker/getMatchState";
    //获取红娘个人中心用户基本资料
    public static String GetUserCenterHeader = "Api/Matchmaker/getUserCenterHeader";
    //获取红娘个人中心step2数据
    public static String GetStep2Info = "Api/Matchmaker/getStep2Info";
    //红娘相册仅对牵线会员可见 入参 method 0-关 1-开
    public static String SetPhotoLock = "Api/Matchmaker/setPhotoLock";
    //红娘会员添加/修改资料(首次)
    //入参：用户-uid 读白-match_introduce  相册-match_photo(不加域名以逗号分隔)
    public static String EditStep2Info = "Api/Matchmaker/editStep2Info";
    //修改红娘资料(更新)
    public static String UpdateStep2Info = "Api/Matchmaker/updateStep2Info";
    //黑V编辑红娘会员资料
    public static String AdminEditStep2Info = "Api/Power/editStep2Info";
    //获取红娘荐语
    public static String GetMatchMakerIntroduce = "Api/Matchmaker/getMatchMakerIntroduce";
    //获取配对用户列表
    public static String GetMatchObList = "Api/Matchmaker/getMatchObList";
    //黑V编辑红娘寄语
    public static String EditMatchMarkerIntroduce = "Api/Power/editMatchMarkerIntroduce";
    //黑V编辑红娘配对备注
    public static String EditRemarks = "Api/Power/editRemarks";
    //支付宝充值魔豆
    public static String ALIPAYCzcharge = "Api/Alipay/czcharge";
    //微信充值魔豆
    public static String WXCzcharge = "Api/Wxpay/czcharge";
    //支付宝充值邮票
    public static String ALIPAYstampcharge = "Api/Alipay/stampcharge";
    //支付宝充值推顶卡
    public static String ALIPAYtopcardcharge = "Api/Alipay/topcardcharge";
    //微信充值推顶卡
    public static String WXCtopcardcharge = "Api/Wxpay/topcardcharge";
    //微信充值邮票
    public static String WXCstampcharge = "Api/Wxpay/stampcharge";
    //支付宝充值svip
    public static String ALIPAYsvipcharge = "Api/Alipay/svipcharge";
    //微信充值svip
    public static String WXsvipcharge = "Api/Wxpay/svipcharge";
    //支付宝充值vip
    public static String ALIPAYvipcharge = "Api/Alipay/vipcharge";
    //微信充值vip
    public static String WXvipcharge = "Api/Wxpay/vipcharge";
    //获取图形验证码
    public static String GetPicSession = "Api/Other/verify";
    //我的页面获取获取客服
    public static String NewGetServiceInfo = "/Api/Index/newGetServiceInfo";

    //获取红娘客服
    public static String NewGetMatchmakerInfo = "/Api/Matchmaker/newGetMatchmakerInfo";

    //获取短视频列表
    public static String DynamicShortVideoList = "Api/Dynamic/getDynamicShortVideoList";



  //获取动态评论列表（二级评论新）
  public static String GetNewCommentList =  "Api/Dynamic/getNewCommentList";

  //黑V查看用户违规记录（1 封号 2 禁动 3 禁言 4 禁资）
  public static String BlockingList =  "Api/Index/blockingList";

  //投诉/被投诉接口  (1投诉 2被投诉)
  public  static  String RecordList ="Api/Index/recordList";

    //账户页面
    public  static  String Userdetail ="Api/Admin/userdetail";

    //历史访问地址
    public  static  String Userposition ="Api/Admin/userposition";

    //悄悄 关注/取关
    public static String followQuiet = "Api/UserInfo/followQuietly";

    //拉黑/取消拉黑
    public static String blockUser = "Api/UserInfo/blockUser";



    public static String callingState = "Api/Power/audioAndVideoStatus";

    public static String callingStateNew = "Api/Power/audioAndVideoStatusNew";



    public static String getUserSign = "Api/UserInfo/getUserTsign";

    public static String applyQuitGroup = "Api/friend/applyQuitGroup";

    public static String appUpdate = "Api/Other/getUserVersion";

    public static String addViewCount = "Api/NewDynamic/addDynamicViewCount";

    public static String revokeMessage = "Api/Friend/groupMsgRecall";

    public static String editGroupAutoCheck = "Api/Friend/editGroupAutoCheck";

    public static String getMessageState = "Api/Ping/forwardGiftRedbag";

    public static String banGroup = "Api/Friend/banGroup";

    public static String getClaimState = "Api/Friend/getGroupClaimStatus";

    public static String addClaimGroup = "Api/Friend/addClaimGroup";

    public static String pushTopTopic = "Api/NewDynamic/topic_topping";

    public static String getTopicTopList = "Api/NewDynamic/topic_topping_list";

    public static String getInviteVIPState = "Api/UserInfo/inviteGetVip";

    public static String checkInviteCode = "/Api/Vip/checkInviteCode";

    public static String getNewUserInviteState = "Api/UserInfo/bindingMobileInfo";

    public static String getUserInviteRewordInfo = "/Api/UserInfo/shareGetSvipList";
    //修改群定位
    public static String changeGroupLocation = "/Api/Friend/editGroupLocation";
    //转让群主
    public static String changeGroupAdmin = "/Api/Friend/changeGroupAdmin";


    public static String getLiveAuth = "/Api/Live/getLiveAuth";

    public static String applyAnchor = "/Api/Live/applyAnchor";

    public static String applyAnchorNew = "/Api/Live/newApplyAnchor";

    public static String getliveBanner = "/Api/Live/getLiveBannerAndLabel";

    public static String getPullAddress = "/Api/Cloudv/getPullAddress";

    public static String getFollowAnchor = "/Api/Live/getFollowAnchor";

    public static String uploadLivePoster = "/Api/Live/uploadLivePoster";

    public static String closeToShow = "/Api/Cloudv/closeToShow";

    public static String celebrityList = "/Api/Live/celebrityList";

    public static String hotAnchorList = "/Api/Live/hotAnchorList";

    public static String RedHotList = "Api/Live/onairlist";

    public static String newAnchorList = "/Api/Live/newAnchorList";

    public static String joinLiveRoom = "/Api/Live/joinLiveRoom";

    public static String quitAvChatRoom = "/Api/Live/quitLiveRoom";

    public static String getAnchorInfo = "/Api/Live/getAnchorInfo";

    public static String liveGiftList = "/Api/Live/liveGoods";

    public static String searchAnchor = "/Api/Live/searchAnchor";

    public static String sendGift = "/Api/Gift/reward";

    public static String setChatRoomAdmin = "Api/ChatRoom/setChatRoomAdmin";

    public static String groupAdminList = "Api/ChatRoom/groupAdminList";

    public static String noTalking = "Api/ChatRoom/setNoTalking";

    public static String card = "Api/ChatRoom/card";

    public static String giftRank = "/Api/Gift/giftRanking";

    public static String hourRank = "/Api/Gift/hourRanking";

    public static String likeAnchor = "/Api/ChatRoom/likeanchor";

    public static String sendBarrage = "/Api/ChatRoom/sendbarrage";

    public static String balanceInfo = "/Api/Withdrawal/balanceInfo";

    public static String withdrawMoney = "/Api/Withdrawal/exchangeMoney";

    public static String recommendLive = "/Api/Live/recommendLiveUser";

    public static String withdrawLog = "/Api/Withdrawal/withdrawalLog";

    public static String liveOnlineUser = "/Api/ChatRoom/watchUserList";

    public static String getMuteForeverUser = "/Api/ChatRoom/groupNoTalkingList";

    public static String reportHeartBeat = "/Api/ChatRoom/heartbeatReport";

    public static String reportAnchorHeartBeat = "/Api/Live/anchorHeartbeatReport";

    public static String followAnchor = "/Api/UserInfo/followAnchor";

    public static String liveAdminOperate = "/Api/Live/methods";

    public static String liveAuthNew = "/Api/Live/getNewLiveAuth";

    public static String withdrawAuth = "/Api/Live/getWithdrawalAuth";

    public static String anchorLiveCard = "/Api/Live/getAnchorCardInfo";

    public static String changeAnchorLiveCard = "/api/Live/live_anchor";

    public static String homePageLiveLabel = "/Api/Live/getLiveLabel";

    public static String labelAnchorList = "/Api/Live/labelAnchorList";

    public static String updateLinkMicList = "/Api/Live/saveLinkMicList";

    public static String updateLinkMangerList = "/Api/Live/saveAdminLinkMicList";

    public static String linkMicList = "/Api/Live/getLinkMicList";

    public static String liveRankingList = "api/dynamic/getLiveRankingList";

    public static String liveCharmRankingList = "api/dynamic/getLiveCharmRankingList";

    public static String livePlayBackRankingList = "api/dynamic/getLivePlaybackRankingList";

    public static String liveAnchorState = "/Api/Live/recommend";

    public static String recommendRedAnchor = "/Api/Live/redman";

    public static String liveHistory = "Api/Live/history_anchor";

    public static String reportChatMessage = "/Api/ChatRoom/reportChatContent";

    public static String consumptionState = "/Api/Live/consumptionStatus";

    public static String labelIntroduce = "/Api/Live/getLabelIntroduce";

    public static String updateCameraStatus = "/Api/Live/updateCameraStatus";

    public static String liveTaskList = "/Api/chatRoom/getTaskList";

    public static String closeLive = "APi/Live/closeLive";

    public static String systemGiftRecord = "/Api/Users/newGetBasicGiveRecord";

    public static String mutePeopleList = "/Api/chatRoom/getNoTalkingList";

    public static String joinLive = "/Api/Live/joinLive";

    public static String buyTicket = "/Api/Live/buyLiveTicket";

    public static String liveTicketInfo = "/Api/Live/getTicketInfo";

    public static String chatRanking = "/Api/ChatRoom/chatRanking";

    public static String liveSeal = "Api/Live/seallist";

    public static String onlinePkAnchorList = "/Api/Pk/onlineAnchorList";

    public static String pkAnchorInfo = "/Api/Pk/pkAnchorInfo";

    public static String LivePkStart  = "/Api/Pk/reportPkStart";

    public static String LivePkComplete  = "/Api/Pk/reportPkComplete";

    public static String LivePkEnd  = "/Api/Pk/reportPkEnd";

    public static String PkTopData = "/Api/pk/getTop";

    public static String myLiveLevel = "/Api/userInfo/getLevelProgress";

    public static String setCardDefault = "/Api/Users/bank_status";

    //回放详情
    public static String livePlayBackDetail = "/Api/live/live_log_detail";

    //购买回放
    public static String buyPlayBack = "/Api/Live/buy_playback";

    //设置回放价格
    public static String setPlayBackPrice = "/Api/Live/history_anchor_free";

    //获取回放信息
    public static String playBackTicketInfo = "/Api/Live/getPlayBackTicketInfo";



    //关闭用户连麦
    public static String kickOutUser = "/Api/QiNiu/roomKickUser";

    //分配接口域名
    public static String basePreUrl = "https://shengmo.oss-cn-shanghai.aliyuncs.com/config/url.json";

    //删除回放
    public static String delPlayBack = "/Api/Live/history_anchor_del";

    //隐藏回放
    public static String hidePlayBack = "/Api/Live/history_anchor_hide";

    //新身份认证
    public static String idCardAuth = "api/Other/idcard";

    public static String KickUser = "/Api/Live/kickMember";

    public static String kickUserList = "/Api/Live/getKickMemberList";

    public static String invitePkAgain = "Api/Pk/invitePk";

    public static String pkAgreeOrRefuse = "/Api/Pk/pkAgreeOrRefuse";

    public static String MuteUser = "/Api/Live/newNoTalking";

    public static String MuteUserList = "/Api/Live/getNewNoTalking";

    public static String changePlayBackInfo = "Api/Live/history_live_up";

    public static String verifyLivePwd = "/Api/Live/verifyPwd";

    public static String lockLiveLog = "/Api/Live/adminLockLiveLog";

    public static String setRewardType = "/Api/UserInfo/inviteRewardType";

    public static String inviteList = "/Api/Admin/inviteList";

    public static String liveSendRedBag = "/Api/Ping/liveGiveRedBag";

    public static String getLiveRedBagList = "/Api/Ping/getLiveRedBagList";

    public static String liveReceiveRedBag = "/Api/Ping/liveGetRedBag";

    public static String getLiveRedBagHistoryList  = "/Api/Ping/getLiveRedBagHistoryList";

    public static String getLiveRedBagHistoryListNew  = "/Api/Ping/getLiveRedBagHistoryListNew";

    public static String giveSvip = "/api/Users/givesvip";

    public static String reportManagerLink = "/Api/Live/adminLinkMicReport";

    public static String retryPk = "/Api/Pk/invitePk";

    public static String responsePk = "/Api/Pk/pkAgreeOrRefuse";

    public static String liveRedBagRank = "/api/dynamic/getLiveRedBagRankingList";

    public static String redBagMsg = "/api/dynamic/getRedBagPresentMsg";

    public static String getLiveLogList = "/api/live/getLiveLogList";

    public static String getLiveChatSetting = "/api/Live/getAnchorChat";

    public static String setLiveChat = "/api/Live/setAnchorChat";

    public static String userChatState = "/api/live/checkAnchorChatAuth";

    public static String startLinkUser = "/api/live/reportStartAnchorChat";

    public static String linkChatList = "/api/live/getAnchorChatList";

    public static String buyLiveLogList = "/api/live/getBuyLiveLogList";

    public static String delete_live_info = "/api/live/live_anchor_delete";

    public static String lotteryDrawGiftList = "/api/gift/getGiftPrize";

    public static String startLotteryDraw = "/api/gift/setDrawGiftPrize";

    public static String lotteryDrawRecord = "/api/gift/getGiftPrizeLog";

    public static String lotteryDrawMarquee = "/api/gift/getGiftPrizeMsg";

    public static String topicPageList = "/api/dynamic/getPidList";

    public static String pidTopicList = "/api/dynamic/getTopicListByPid";

    public static String synthesisGiftList = "/api/gift/getMergeGiftList";

    public static String mergeGiftInfo = "/api/gift/getCanMergeGiftInfo";

    public static String doSynthesisGift = "/api/gift/setMergeGift";

    public static String synthesisRecord = "/api/gift/getMergeGiftLog";

    public static String largeGift = "/Api/index/getLargeGift";

    public static String updateFreeLink = "/api/Power/updateChatDuration";

    public static String dailySign = "/Api/Draw/loginSign";

    public static String signState = "Api/Draw/signTimesInWeeks";

    public static String noticeMessage = "api/message/notice";

    public static String selfieState = "Api/Other/idstate";

    public static String selfieAuth = "Api/UserAttestation/selfieAudit";

    public static String getHighList = "Api/Top/getTopList";

    public static String HighUserDetail = "Api/Top/getTopDetails";

    public static String updateHighPhoto = "Api/Top/changePhotoAuth";

    public static String getHighRule = "Api/Top/getTopBag";

    public static String submitHighInfo = "Api/Top/topInfoSave";

    public static String submitHighAuthInfo = "Api/Top/submitAudit";

    public static String checkSelfTop = "Api/Top/getTopStatus";

    public static String highAuthInfo = "Api/Top/getAuditInfo";

    public static String deleteHighAuth = "Api/Top/deleteAuditInfo";

    public static String openHighWechat = "Api/Wxpay/openUserTop";

    public static String openHighAliPay = "Api/Alipay/openUserTop";

    public static String screenCapture = "/Api/Live/updateScreenStatus";

    public static String onAirListNew = "api/Live/onairlistNewV2";

    public static String hotAnchorLiveLog = "api/Live/getHotAnchorLiveLog";

    public static String userLiveLogList = "api/Live/getUserLiveLogList";

    public static String getTopConfig = "Api/Top/getTopConfig";

    //获取配对用户
    public static String getHighMatchList = "Api/Top/getPairData";

    public static String getHighIsChat = "Api/Top/startChat";
    //统一礼物赠送接口
    public static String normalSendGift = "api/giftSend/sendGiftReward";
    //普通配置信息
    public static String systemConfig = "/api/index/getSystemConfig";
    //用户连线历史
    public static String linkHistory = "/Api/Live/getLiveAnchorChatLogList";
    //加点主播相关信息
    public static String signedAnchorInfo = "api/live/signedAnchorInfo";

    public static String getHighDetail = "Api/Top/getTopHistory";

    public static String  adminMatchPair = "Api/Top/makePair";

    public static String  adminOpenHigh = "Api/Top/openTop";

    public static String  adminCloseHigh = "Api/Top/closeTop";

    public static String  adminOpenRedService = "Api/Top/openMatch";

    public static String  adminCloseRedService = "Api/Top/closeMatch";

    public static String  adminMangerTopDesc = "Api/Top/changeHiddenByField";

    public static String  adminCancleChatAndInfo = "Api/Top/cancelDisableAdmin";

    public static String  adminBannedChatAndInfo = "Api/Top/setDisableAdmin";

    public static String  adminCancleAudit = "Api/Top/cancelAuditTopUser";

    public static String  adminHideUser = "Api/Top/changeShowStatus";

    public static String anchorRecordList = "Api/Live/history_anchor_record";

    public static String callItem = "/Api/Call/getCallBag";

    public static String callStatus = "/Api/Call/getCallStatus";

    public static String buyCallByBeans = "/Api/Call/beansExchange";

    public static String callList = "/Api/Call/getCallList";
    //开启呼唤
    public static String openCall = "/Api/Call/openCall";
    //用户是否可以编辑资料
    public static String isEditPermission = "/Api/Call/isCanEditUserInfo";
    //呼唤配置信息
    public static String callConfig = "/Api/Call/getCallConfig";
    //呼唤使用记录
    public static String callUseHistory = "/Api/Call/getUseHistory";
    //呼唤购买记录
    public static String callBuyHistory = "Api/Call/getBuyRecord";

    public static String  getCarlist = "Api/LiveAnimation/animationList";

    public static String  getCarInfo= "Api/LiveAnimation/animationDetails";

    public static String  exchangeCar= "Api/LiveAnimation/exchange";

    public static String  wechatBuyCar= "Api/Wxpay/animationOrder";

    public static String  aliBuyCar= "Api/Alipay/animationOrder";

    public static String  myCarList= "Api/LiveAnimation/userAnimationList";

    public static String  switchCar= "Api/LiveAnimation/setDefaultAnimation";

    public static String  carRecord= "Api/LiveAnimation/userPurchaseRecord";

    //呼唤 - 微信支付
    public static String callWeChatOrder = "/Api/Wxpay/openUserCall";
    //呼唤 - 支付宝支付
    public static String callAliPayOrder = "/Api/Alipay/openUserCall";
    //粉丝群 入团券礼物
    public static String fanClubGiftInfo = "/Api/LiveFanclub/getFanclubGift";
    //粉丝群 购买券入群
    public static String buyFanClubGift = "/api/LiveFanclub/sendFanclubGift";
    //开通粉丝团
    public static String openFanClub = "/Api/LiveFanclub/setAnchorFanclub";
    //我的粉丝团
    public static String fanClubInfo = "/Api/LiveFanclub/fanclubInfo";
    //我加入的粉丝团
    public static String fanClubList = "/Api/LiveFanclub/fanclubList";
    //粉丝团成员列表
    public static String fanClubMemberList = "Api/LiveFanclub/fanclubMemberList";
    //用户设置粉丝牌 显示和隐藏
    public static String hideOrShowFanClub = "/Api/LiveFanclub/setUserFanclubCard";
    //粉丝团排名
    public static String fanClubRank = "/Api/LiveFanclub/fanclubRankList";
    //主播修改勋章名称
    public static String setAnchorClubCard = "/Api/LiveFanclub/setAnchorFanclubCard";
    //开通粉丝团信息
    public static String openClubInfo = "Api/LiveFanclub/getBeforeFanclub";
    //分组是否在聊天显示
    public static String groupingConfig = "Api/friendgroup/userFriendGroupSet";
    //设置分组是否在聊天显示
    public static String setGroupingConfig = "Api/friendgroup/setUserFriendGroupStatus";
    //推广详情
    public static String callDetails = "/Api/Call/getCallDetails";
    //我创建的群组
    public static String createGroupList = "Api/friend/getGroupListFilterV3";
    //获取高端id
    public static String getHighIdByUid = "Api/Top/getTopIdByUid";
    //  高端聊天
    public static String startHighGroup = "/Api/Top/startChatV2";
    // 删除聊天记录
    public static String deleteMsgRecord = "Api/Top/deleteTopChat";
    // 高端列表  聊天
    public static String  startHighGroupChat = "Api/Top/startChatByList";
    //  高端 关注 取关
    public static String  setHighUserFollow = "Api/Top/setFollow";
    //  高端 关注列表
    public static String  highFollowList = "Api/Top/getFollowList";
    //  高端  粉丝列表
    public static String  highFansList = "Api/Top/getFansList";
    //回放评论列表
    public static String livePlayBackCommentList = "Api/LivePlaybackComment/commentList";
    //回放增加评论
    public static String livePlayBackAddComment = "Api/LivePlaybackComment/addComment";
    //回放删除评论
    public static String livePlayBackDeleteComment = "Api/LivePlaybackComment/delComment";
    //举报 相关配置
    public static String livePlayBackReportConfig = "Api/RejectContent/errorOptionList";
    //回放评论举报
    public static String livePlayBackReport = "Api/LivePlaybackComment/reportComment";
    //直播关闭连麦用户
    public static String liveKickMicUser = "/Api/Live/kickMicUser";

    //查看身份证
    public static String getIDCardPhoto = "Api/Users/getIdcardDataByUid";
    //  连线状态
    public static String getLinkSettingStatus = "Api/Live/getChatStatus";

}
