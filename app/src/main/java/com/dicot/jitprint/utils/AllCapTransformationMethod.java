package com.dicot.jitprint.utils;

import android.text.method.ReplacementTransformationMethod;

/**
 * @Author Hunter
 * Describe 设置把输入字母自动转换成大写字母
 * @Date 2017/1/12
 */

public class AllCapTransformationMethod extends ReplacementTransformationMethod {

    @Override
    protected char[] getOriginal() {
        char[] str = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
        return str;
    }

    @Override
    protected char[] getReplacement() {
        char[] str = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
        return str;
    }

}
