package com.original.abroadeasy.util;

import android.widget.EditText;
import android.widget.TextView;

/**
 * Tks:    ZhuWenWu
 */
public class StringHelper {
    public static String getStr(String str) {
        return isEmpty(str) ? "" : str;
    }

    public static String getEditTextContent(EditText edt) {
        return edt.getText().toString().trim();
    }

    public static boolean isEmpty(String str) {
        return str == null || str.equals("null") || str.equals("");
    }

    public static boolean isEditTextEmpty(EditText edt) {
        return isEmpty(getEditTextContent(edt));
    }

    public static boolean isPhoneEditTextEmpty(EditText edt) {
        return isEmpty(getEditTextContent(edt)) || getEditTextContent(edt).length() != 11;
    }

    public static boolean notEmpty(String str) {
        return !isEmpty(str);
    }

    public static String getTextViewContent(TextView tv) {
        return tv.getText().toString().trim();
    }
}
