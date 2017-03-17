package com.dicot.jitprint.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by Hunter
 * Describe IO流工具类
 * on 2017/3/11.
 */
public class IOUtils {
    /**
     * 关闭流
     */
    public static boolean close(Closeable io) {
        if (io != null) {
            try {
                io.close();
            } catch (IOException e) {
                LogUtils.e(e);
            }
        }
        return true;
    }
}
