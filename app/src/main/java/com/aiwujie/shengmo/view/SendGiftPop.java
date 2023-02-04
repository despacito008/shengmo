package com.aiwujie.shengmo.view;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.adapter.PresentGridViewAdapter;
import com.aiwujie.shengmo.bean.PresentData;
import com.aiwujie.shengmo.customview.DashangDialogNew;
import com.aiwujie.shengmo.view.indicator.CircleIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import razerdp.basepopup.BasePopupWindow;

public class SendGiftPop extends BasePopupWindow {

    @BindView(R.id.tv_pop_send_gift_normal)
    TextView tvPopSendGiftNormal;
    @BindView(R.id.tv_pop_send_gift_free)
    TextView tvPopSendGiftFree;
    @BindView(R.id.view_pager_pop_send_gift)
    ViewPager viewPagerPopSendGift;
    @BindView(R.id.indicator_pop_send_gift)
    CircleIndicator indicatorPopSendGift;
    @BindView(R.id.tv_pop_send_gift_beans)
    TextView tvPopSendGiftBeans;
    @BindView(R.id.tv_pop_send_gift_buy)
    TextView tvPopSendGiftBuy;

    private String[] titles = {"棒棒糖", "狗粮", "雪糕", "黄瓜", "心心相印", "香蕉", "口红", "亲一个", "玫瑰花", "眼罩",
            "心灵束缚", "黄金", "拍之印", "鞭之痕", "一飞冲天", "一生一世", "水晶高跟", "恒之光", "666", "红酒", "蛋糕", "钻戒", "皇冠",
            "跑车", "直升机", "游轮", "城堡"};
    private String[] titletos = {"幸运草", "糖果", "玩具狗", "内内", "TT"};
    private String[] moneys = {"2魔豆", "6魔豆", "10魔豆", "38魔豆", "99魔豆", "88魔豆", "123魔豆", "166魔豆", "199魔豆", "520魔豆", "666魔豆", "250魔豆", "777魔豆", "888魔豆", "999魔豆", "1314魔豆", "1666魔豆", "1999魔豆", "666魔豆", "999魔豆", "1888魔豆", "2899魔豆", "3899魔豆", "6888魔豆", "9888魔豆", "52000魔豆", "99999魔豆"};
    private String[] moneytos = {"1魔豆", "3魔豆", "5魔豆", "10魔豆", "8魔豆"};
    private int[] presents = {R.mipmap.presentnew01, R.mipmap.presentnew02, R.mipmap.presentnew03, R.mipmap.presentnew04, R.mipmap.presentnew05,
            R.mipmap.presentnew06, R.mipmap.presentnew07, R.mipmap.presentnew08, R.mipmap.presentnew09,
            R.mipmap.presentnew10, R.mipmap.presentnew11, R.mipmap.presentnew12, R.mipmap.presentnew13,
            R.mipmap.presentnew14, R.mipmap.presentnew15, R.mipmap.presentnew16, R.mipmap.presentnew17,
            R.mipmap.presentnew18, R.mipmap.presentnew19, R.mipmap.presentnew20, R.mipmap.presentnew21,
            R.mipmap.presentnew22, R.mipmap.presentnew23, R.mipmap.presentnew24, R.mipmap.presentnew25,
            R.mipmap.presentnew26, R.mipmap.presentnew27};

    private int[] presenttos = {R.mipmap.presentnew28, R.mipmap.presentnew29, R.mipmap.presentnew30, R.mipmap.presentnew31, R.mipmap.presentnew32};
    private List<PresentData> presentDataList;
    private int pageSize = 9;
    private int pageCount;
    private ArrayList<View> mPagerList;
    private Context context;
    public SendGiftPop(Context context) {
        super(context);
        this.context = context;
        initData();
        initListener();
    }

    @Override
    public View onCreateContentView() {
        View view = createPopupById(R.layout.app_pop_send_gift);
        ButterKnife.bind(this,view);
        return view;
    }


    void initData() {
        presentDataList = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            presentDataList.add(new PresentData(titles[i], moneys[i], presents[i], i + 10));
        }
        //总的页数=总数/每页数量，并取整
        pageCount = (int) Math.ceil(presentDataList.size() * 1.0 / pageSize);
        viewPagerPopSendGift.setOffscreenPageLimit(pageCount);
        mPagerList = new ArrayList<View>();
        for (int i = 0; i < pageCount; i++) {
            // 每个页面都是inflate出一个新实例
            GridView gridView = (GridView) LayoutInflater.from(context).inflate(R.layout.gridview, viewPagerPopSendGift, false);
            //MyGridView gridView = new MyGridView(context);
            PresentGridViewAdapter presentGridViewAdapter = new PresentGridViewAdapter(context, presentDataList, i, pageSize);
            gridView.setAdapter(presentGridViewAdapter);
            mPagerList.add(gridView);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    Toast.makeText(context.getApplicationContext(), mDatas.get((int) id).getName()+","+id, Toast.LENGTH_SHORT).show();
                    //打赏礼物的id   从10开始
                    //psid = (int) (id + 10);
                    //选择礼物
                   // selectPresentCount(id);
                }
            });
        }
        viewPagerPopSendGift.setAdapter(new ViewPagerAdapter(mPagerList));
        indicatorPopSendGift.setViewPager(viewPagerPopSendGift);
    }

    void initListener() {

    }

    class ViewPagerAdapter extends PagerAdapter {
        private List<View> mViewList;

        public ViewPagerAdapter(List<View> mViewList) {
            this.mViewList = mViewList;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mViewList.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mViewList.get(position));
            return (mViewList.get(position));
        }

        @Override
        public int getCount() {
            if (mViewList == null)
                return 0;
            return mViewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }
}
