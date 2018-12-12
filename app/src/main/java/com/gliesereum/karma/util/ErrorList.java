package com.gliesereum.karma.util;

import android.content.Context;

import com.gliesereum.karma.R;

import java.util.HashMap;
import java.util.Map;

public class ErrorList {

    private static ErrorList errorInstance;
    public Map<Integer, String> errorMap = new HashMap<>();

    private ErrorList(Context context) {
        if (errorInstance != null) {
            throw new RuntimeException("Use init() method to get the single instance of this class.");
        }

        fillErrorList(context);

    }

    private void fillErrorList(Context context) {
        errorMap.put(1122, context.getResources().getString(R.string.error_1122));
        errorMap.put(1124, context.getResources().getString(R.string.error_1124));
        errorMap.put(1142, context.getResources().getString(R.string.error_1142));
        errorMap.put(1163, context.getResources().getString(R.string.error_1163));
        errorMap.put(1143, context.getResources().getString(R.string.error_1143));
    }

    public static ErrorList init(Context context) {
        if (errorInstance == null) {
            errorInstance = new ErrorList(context);
        }

        return errorInstance;
    }

    public String getErrorMessage(int code) {
        return errorMap.get(code);
    }

}
