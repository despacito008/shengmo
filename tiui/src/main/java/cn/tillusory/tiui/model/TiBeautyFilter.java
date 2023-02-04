package cn.tillusory.tiui.model;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;

import cn.tillusory.tiui.R;
import cn.tillusory.tiui.adapter.TiFilterAdapter;

/**
 * Created by Anko on 2018/11/26.
 * Copyright (c) 2018-2020 拓幻科技 - tillusory.cn. All rights reserved.
 */
public enum TiBeautyFilter {
    NO_FILTER("", R.string.filter_no, R.drawable.ic_ti_filter_nature_1, TiFilterAdapter.ITEM_COMMON, 0),

    SUYAN_FILTER("suyan", R.string.filter_suyan, R.drawable.ic_ti_filter_nature_1, TiFilterAdapter.ITEM_CATEGORY_TITLE, R.string.category_nature),
    FEIYING_FILTER("feiying", R.string.filter_feiying, R.drawable.ic_ti_filter_nature_2, TiFilterAdapter.ITEM_COMMON, 0),
    QIANXING_FILTER("qianxing", R.string.filter_qianxing, R.drawable.ic_ti_filter_nature_3, TiFilterAdapter.ITEM_COMMON, 0),
    QIANGWEI_FILTER("qiangwei", R.string.filter_qiangwei, R.drawable.ic_ti_filter_nature_4, TiFilterAdapter.ITEM_COMMON, 0),
    QINGNING_FILTER("qingning", R.string.filter_qingning, R.drawable.ic_ti_filter_nature_5, TiFilterAdapter.ITEM_COMMON, 0),
    ZHENZHU_FILTER("zhenzhu", R.string.filter_zhenzhu, R.drawable.ic_ti_filter_nature_6, TiFilterAdapter.ITEM_COMMON, 0),
    NUANCHUN_FILTER("nuanchun", R.string.filter_nuanchun, R.drawable.ic_ti_filter_nature_7, TiFilterAdapter.ITEM_COMMON, 0),

    QINGXI_FILTER("qingxi", R.string.filter_qingxi, R.drawable.ic_ti_filter_clear_1, TiFilterAdapter.ITEM_CATEGORY_TITLE, R.string.category_clear),
    NIUNAI_FILTER("niunai", R.string.filter_niunai, R.drawable.ic_ti_filter_clear_2, TiFilterAdapter.ITEM_COMMON, 0),
    SHUIWU_FILTER("shuiwu", R.string.filter_shuiwu, R.drawable.ic_ti_filter_clear_3, TiFilterAdapter.ITEM_COMMON, 0),
    YANXI_FILTER("yanxi", R.string.filter_yanxi, R.drawable.ic_ti_filter_clear_4, TiFilterAdapter.ITEM_COMMON, 0),
    SHUIGUANG_FILTER("shuiguang", R.string.filter_shuiguang, R.drawable.ic_ti_filter_clear_5, TiFilterAdapter.ITEM_COMMON, 0),
    NAIXING_FILTER("naixing", R.string.filter_naixing, R.drawable.ic_ti_filter_clear_6, TiFilterAdapter.ITEM_COMMON, 0),

    SHAONV_FILTER("shaonv", R.string.filter_shaonv, R.drawable.ic_ti_filter_vigour_1, TiFilterAdapter.ITEM_CATEGORY_TITLE, R.string.category_vigour),
    BAITAO_FILTER("baitao", R.string.filter_baitao, R.drawable.ic_ti_filter_vigour_2, TiFilterAdapter.ITEM_COMMON, 0),
    RIXI_FILTER("rixi", R.string.filter_rixi, R.drawable.ic_ti_filter_vigour_3, TiFilterAdapter.ITEM_COMMON, 0),
    FENXIA_FILTER("fenxia", R.string.filter_fenxia, R.drawable.ic_ti_filter_vigour_4, TiFilterAdapter.ITEM_COMMON, 0),
    TIANMEI_FILTER("tianmei", R.string.filter_tianmei, R.drawable.ic_ti_filter_vigour_5, TiFilterAdapter.ITEM_COMMON, 0),
    NAIYOU_FILTER("naiyou", R.string.filter_naiyou, R.drawable.ic_ti_filter_vigour_6, TiFilterAdapter.ITEM_COMMON, 0),
    RIZA_FILTER("riza", R.string.filter_riza, R.drawable.ic_ti_filter_vigour_7, TiFilterAdapter.ITEM_COMMON, 0),
    NAIYOUMITAO_FILTER("naiyoumitao", R.string.filter_naiyoumitao, R.drawable.ic_ti_filter_vigour_8, TiFilterAdapter.ITEM_COMMON, 0),
    JUZIQISHUI_FILTER("juziqishui", R.string.filter_juziqishui, R.drawable.ic_ti_filter_vigour_9, TiFilterAdapter.ITEM_COMMON, 0),

    HUIDIAO_FILTER("huidiao", R.string.filter_huidiao, R.drawable.ic_ti_filter_senior_1, TiFilterAdapter.ITEM_CATEGORY_TITLE, R.string.category_senior),
    LENGDAN_FILTER("lengdan", R.string.filter_lengdan, R.drawable.ic_ti_filter_senior_2, TiFilterAdapter.ITEM_COMMON, 0),
    HUAYAN_FILTER("huayan", R.string.filter_huayan, R.drawable.ic_ti_filter_senior_3, TiFilterAdapter.ITEM_COMMON, 0),
    ZHIGAN_FILTER("zhigan", R.string.filter_zhigan, R.drawable.ic_ti_filter_senior_4, TiFilterAdapter.ITEM_COMMON, 0),
    JIZHOU_FILTER("jizhou", R.string.filter_jizhou, R.drawable.ic_ti_filter_senior_5, TiFilterAdapter.ITEM_COMMON, 0),

    YOUHUA1_FILTER("youhua1", R.string.filter_youhua1, R.drawable.ic_ti_filter_atmosphere_1, TiFilterAdapter.ITEM_CATEGORY_TITLE, R.string.category_atmosphere),
    YOUHUA2_FILTER("youhua2", R.string.filter_youhua2, R.drawable.ic_ti_filter_atmosphere_2, TiFilterAdapter.ITEM_COMMON, 0),
    SENXI_FILTER("senxi", R.string.filter_senxi, R.drawable.ic_ti_filter_atmosphere_3, TiFilterAdapter.ITEM_COMMON, 0),
    ZHONGXIAMENG_FILTER("zhongxiameng", R.string.filter_zhongxiameng, R.drawable.ic_ti_filter_atmosphere_4, TiFilterAdapter.ITEM_COMMON, 0),

    MEIWEI_FILTER("meiwei", R.string.filter_meiwei, R.drawable.ic_ti_filter_food_1, TiFilterAdapter.ITEM_CATEGORY_TITLE, R.string.category_food),
    XINXIAN_FILTER("xinxian", R.string.filter_xinxian, R.drawable.ic_ti_filter_food_2, TiFilterAdapter.ITEM_COMMON, 0),
    MITAOWULONG_FILTER("mitaowulong", R.string.filter_mitaowulong, R.drawable.ic_ti_filter_food_3, TiFilterAdapter.ITEM_COMMON, 0),
    NUANSHI_FILTER("nuanshi", R.string.filter_nuanshi, R.drawable.ic_ti_filter_food_4, TiFilterAdapter.ITEM_COMMON, 0),
    SHENYESHITANG_FILTER("shenyeshitang", R.string.filter_shenyeshitang, R.drawable.ic_ti_filter_food_5, TiFilterAdapter.ITEM_COMMON, 0),

    XIARI_FILTER("xiari", R.string.filter_xiari, R.drawable.ic_ti_filter_holiday_1, TiFilterAdapter.ITEM_CATEGORY_TITLE, R.string.category_holiday),
    NUANYANG_FILTER("nuanyang", R.string.filter_nuanyang, R.drawable.ic_ti_filter_holiday_2, TiFilterAdapter.ITEM_COMMON, 0),
    ZHAOHE_FILTER("zhaohe", R.string.filter_zhaohe, R.drawable.ic_ti_filter_holiday_3, TiFilterAdapter.ITEM_COMMON, 0),
    BOSHIDUN_FILTER("boshidun", R.string.filter_boshidun, R.drawable.ic_ti_filter_holiday_4, TiFilterAdapter.ITEM_COMMON, 0),

    PAILIDE_FILTER("pailide", R.string.filter_pailide, R.drawable.ic_ti_filter_film_1, TiFilterAdapter.ITEM_CATEGORY_TITLE, R.string.category_film),
    HUIYI_FILTER("huiyi", R.string.filter_huiyi, R.drawable.ic_ti_filter_film_2, TiFilterAdapter.ITEM_COMMON, 0),
    FANCHASE_FILTER("fanchase", R.string.filter_fanchase, R.drawable.ic_ti_filter_film_3, TiFilterAdapter.ITEM_COMMON, 0),
    FUGU_FILTER("fugu", R.string.filter_fugu, R.drawable.ic_ti_filter_film_4, TiFilterAdapter.ITEM_COMMON, 0),
    HUAIJIU_FILTER("huaijiu", R.string.filter_huaijiu, R.drawable.ic_ti_filter_film_5, TiFilterAdapter.ITEM_COMMON, 0),
    HEIBAI_FILTER("heibai", R.string.filter_heibai, R.drawable.ic_ti_filter_film_6, TiFilterAdapter.ITEM_COMMON, 0);

    private String filterName;
    private int stringId;
    private int imageId;
    private int type;
    private int titleId;

    TiBeautyFilter(String filterName, int stringId, @DrawableRes int imageId, int type, int titleId) {
        this.filterName = filterName;
        this.stringId = stringId;
        this.imageId = imageId;
        this.type = type;
        this.titleId = titleId;
    }

    public String getFilterName() {
        return filterName;
    }

    public String getString(@NonNull Context context) {
        return context.getResources().getString(stringId);
    }

    public Drawable getImageDrawable(@NonNull Context context) {
        return context.getResources().getDrawable(imageId);
    }

    public int getType() {
        return type;
    }

    public String getTitle(@NonNull Context context) {
        return context.getResources().getString(titleId);
    }
}
