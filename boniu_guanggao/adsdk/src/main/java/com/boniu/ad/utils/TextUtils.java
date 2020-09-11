package com.boniu.ad.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

public class TextUtils {
    public static boolean isEmpty(String text){
        if (text == null || text.length() == 0 || "null".equals(text)){
            return true;
        }else{
            return false;
        }
    }

    public static boolean isPhone(String phone){
        if (phone.length() == 11 && phone.substring(0,1).equals("1")){
            return true;
        }
        return false;
    }

    /**
     * 复制内容到剪切板
     *
     * @param copyStr
     * @return
     */
    public static boolean copy(Context context,String copyStr) {
        try {
            //获取剪贴板管理器
            ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            // 创建普通字符型ClipData
            ClipData mClipData = ClipData.newPlainText("Label", copyStr);
            // 将ClipData内容放到系统剪贴板里。
            cm.setPrimaryClip(mClipData);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
