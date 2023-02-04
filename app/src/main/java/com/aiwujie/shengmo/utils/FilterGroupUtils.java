package com.aiwujie.shengmo.utils;

/**
 * Created by 290243232 on 2017/5/27.
 */

public class FilterGroupUtils {
    /**
     *
     * @return  1.普通人 2.同志 3.拉拉 4.cdts
     */
    public static String isWhatSexual(String groupSex,String groupSexual){
        String sexualFlag="1";
        if(groupSex.equals("1")&&(groupSexual.equals("1") || groupSexual.equals("1,3"))){
            sexualFlag="2";
        }
        if(groupSex.equals("2")&&groupSexual.equals("2")){
            sexualFlag="3";
        }
        if(groupSex.equals("3")){
            sexualFlag="4";
        }
        return sexualFlag;
    }

    /**
     *
     * @return  0.普通人 1.同志 2.拉拉 3.cdts
     */
    public static String isWhatSexual2(String sex,String sexual){
        String sexualFlag="";
        if(sex.equals("1")&&(sexual.equals("1") || sexual.equals("1,3"))){
            sexualFlag="1";
        }
        if(sex.equals("2")&&(sexual.equals("2") || sexual.equals("2,3"))){
            sexualFlag="2";
        }
        if(sex.equals("3") &&sexual.equals("3")){
            sexualFlag="3";
        }
        return sexualFlag;
    }

}
