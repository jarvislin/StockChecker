package com.jarvis.stockchecker.app;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Jarvis on 2014/5/9.
 */
public class DataParser {
    public DataParser(){

    }
    public static String matchData(String rule, String htmlContent){
        Pattern p = Pattern.compile(rule);
        Matcher m = p.matcher(htmlContent);
        if (m.find())
            return m.group(1).trim();
        else
            return null;
    }
}
