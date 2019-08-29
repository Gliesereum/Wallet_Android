package com.gliesereum.coupler.util;

import android.content.Context;

import com.gliesereum.coupler.R;

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
        errorMap.put(1160, context.getResources().getString(R.string.error_1160));
        errorMap.put(1161, context.getResources().getString(R.string.error_1161));
        errorMap.put(1162, context.getResources().getString(R.string.error_1162));
        errorMap.put(1163, context.getResources().getString(R.string.error_1163));
        errorMap.put(1164, context.getResources().getString(R.string.error_1164));
        errorMap.put(1000, context.getResources().getString(R.string.error_1000));
        errorMap.put(1001, context.getResources().getString(R.string.error_1001));
        errorMap.put(1002, context.getResources().getString(R.string.error_1002));
        errorMap.put(1003, context.getResources().getString(R.string.error_1003));
        errorMap.put(1004, context.getResources().getString(R.string.error_1004));
        errorMap.put(1040, context.getResources().getString(R.string.error_1040));
        errorMap.put(1041, context.getResources().getString(R.string.error_1041));
        errorMap.put(1042, context.getResources().getString(R.string.error_1042));
        errorMap.put(1043, context.getResources().getString(R.string.error_1043));
        errorMap.put(1044, context.getResources().getString(R.string.error_1044));
        errorMap.put(1045, context.getResources().getString(R.string.error_1045));
        errorMap.put(1046, context.getResources().getString(R.string.error_1046));
        errorMap.put(9000, context.getResources().getString(R.string.error_9000));
        errorMap.put(9001, context.getResources().getString(R.string.error_9001));
        errorMap.put(1140, context.getResources().getString(R.string.error_1140));
        errorMap.put(1141, context.getResources().getString(R.string.error_1141));
        errorMap.put(1142, context.getResources().getString(R.string.error_1142));
        errorMap.put(1143, context.getResources().getString(R.string.error_1143));
        errorMap.put(1144, context.getResources().getString(R.string.error_1144));
        errorMap.put(1145, context.getResources().getString(R.string.error_1145));
        errorMap.put(1146, context.getResources().getString(R.string.error_1146));
        errorMap.put(1147, context.getResources().getString(R.string.error_1147));
        errorMap.put(1300, context.getResources().getString(R.string.error_1300));
        errorMap.put(1301, context.getResources().getString(R.string.error_1301));
        errorMap.put(1302, context.getResources().getString(R.string.error_1302));
        errorMap.put(1303, context.getResources().getString(R.string.error_1303));
        errorMap.put(1400, context.getResources().getString(R.string.error_1400));
        errorMap.put(1410, context.getResources().getString(R.string.error_1410));
        errorMap.put(1411, context.getResources().getString(R.string.error_1411));
        errorMap.put(1420, context.getResources().getString(R.string.error_1420));
        errorMap.put(1421, context.getResources().getString(R.string.error_1421));
        errorMap.put(1422, context.getResources().getString(R.string.error_1422));
        errorMap.put(1423, context.getResources().getString(R.string.error_1423));
        errorMap.put(1424, context.getResources().getString(R.string.error_1424));
        errorMap.put(1425, context.getResources().getString(R.string.error_1425));
        errorMap.put(1426, context.getResources().getString(R.string.error_1426));
        errorMap.put(1427, context.getResources().getString(R.string.error_1427));
        errorMap.put(1428, context.getResources().getString(R.string.error_1428));
        errorMap.put(1429, context.getResources().getString(R.string.error_1429));
        errorMap.put(1430, context.getResources().getString(R.string.error_1430));
        errorMap.put(1431, context.getResources().getString(R.string.error_1431));
        errorMap.put(1432, context.getResources().getString(R.string.error_1432));
        errorMap.put(1433, context.getResources().getString(R.string.error_1433));
        errorMap.put(1434, context.getResources().getString(R.string.error_1434));
        errorMap.put(1435, context.getResources().getString(R.string.error_1435));
        errorMap.put(1436, context.getResources().getString(R.string.error_1436));
        errorMap.put(1437, context.getResources().getString(R.string.error_1437));
        errorMap.put(1438, context.getResources().getString(R.string.error_1438));
        errorMap.put(1439, context.getResources().getString(R.string.error_1439));
        errorMap.put(1440, context.getResources().getString(R.string.error_1440));
        errorMap.put(1441, context.getResources().getString(R.string.error_1441));
        errorMap.put(1442, context.getResources().getString(R.string.error_1442));
        errorMap.put(1443, context.getResources().getString(R.string.error_1443));
        errorMap.put(1450, context.getResources().getString(R.string.error_1450));
        errorMap.put(1120, context.getResources().getString(R.string.error_1120));
        errorMap.put(1121, context.getResources().getString(R.string.error_1121));
        errorMap.put(1122, context.getResources().getString(R.string.error_1122));
        errorMap.put(1123, context.getResources().getString(R.string.error_1123));
        errorMap.put(1124, context.getResources().getString(R.string.error_1124));
        errorMap.put(1125, context.getResources().getString(R.string.error_1125));
        errorMap.put(1126, context.getResources().getString(R.string.error_1126));
        errorMap.put(1127, context.getResources().getString(R.string.error_1127));
        errorMap.put(1100, context.getResources().getString(R.string.error_1100));
        errorMap.put(1101, context.getResources().getString(R.string.error_1101));
        errorMap.put(1102, context.getResources().getString(R.string.error_1102));
        errorMap.put(1103, context.getResources().getString(R.string.error_1103));
        errorMap.put(1104, context.getResources().getString(R.string.error_1104));
        errorMap.put(1105, context.getResources().getString(R.string.error_1105));
        errorMap.put(1010, context.getResources().getString(R.string.error_1010));
        errorMap.put(1011, context.getResources().getString(R.string.error_1011));
        errorMap.put(1012, context.getResources().getString(R.string.error_1012));
        errorMap.put(1013, context.getResources().getString(R.string.error_1013));
        errorMap.put(1014, context.getResources().getString(R.string.error_1014));
        errorMap.put(1015, context.getResources().getString(R.string.error_1015));
        errorMap.put(1016, context.getResources().getString(R.string.error_1016));
        errorMap.put(1017, context.getResources().getString(R.string.error_1017));
        errorMap.put(1018, context.getResources().getString(R.string.error_1018));

        errorMap.put(1451, context.getResources().getString(R.string.error_1451));
        errorMap.put(1510, context.getResources().getString(R.string.error_1510));
        errorMap.put(1511, context.getResources().getString(R.string.error_1511));
        errorMap.put(1512, context.getResources().getString(R.string.error_1512));
        errorMap.put(1513, context.getResources().getString(R.string.error_1513));
        errorMap.put(1514, context.getResources().getString(R.string.error_1514));

        errorMap.put(1492, context.getResources().getString(R.string.error_1492));
        errorMap.put(1497, context.getResources().getString(R.string.error_1497));

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
