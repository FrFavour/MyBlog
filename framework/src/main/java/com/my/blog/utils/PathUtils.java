package com.my.blog.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class PathUtils {
    public static String generatorPath(String filename) {
        //根据日期生成路径
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/");
        String date = sdf.format(new Date());
        // 生成UUID文件名
        String file = UUID.randomUUID().toString().replaceAll("-", "");
        // 取后缀
        int index = filename.lastIndexOf(".");
        String sub = filename.substring(index);
        return new StringBuilder().append(date).append(file).append(sub).toString();

    }
}
