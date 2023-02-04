package com.aiwujie.shengmo.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 290243232 on 2017/5/27.
 */

public class JudgmentFormatUtils {

    public static boolean isMobile(String mobiles){

       // Pattern p = Pattern.compile("^13[0-9]{1}[0-9]{8}$|^15[0-9]{9}$|^18[0-9]{9}$|^14[57]{1}[0-9]{8}$|^17[0-9]{9}$");
        Pattern p = Pattern.compile("^[0-9]{11}$");

        Matcher m = p.matcher(mobiles);

        return m.matches();

    }
    public static boolean isEmail(String email){
        Pattern p =  Pattern.compile("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");//复杂匹配
        Matcher m = p.matcher(email);
        return m.matches();
    }
}
