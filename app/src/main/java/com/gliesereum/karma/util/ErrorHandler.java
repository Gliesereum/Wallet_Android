package com.gliesereum.karma.util;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.gliesereum.karma.R;
import com.labters.lottiealertdialoglibrary.DialogTypes;
import com.labters.lottiealertdialoglibrary.LottieAlertDialog;

public class ErrorHandler {

    private Context context;
    private Activity activity;

    public ErrorHandler(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    public void showError(Integer errorCode) {
        try {
            new LottieAlertDialog.Builder(context, DialogTypes.TYPE_WARNING)
                    .setTitle("Предупреждение")
                    .setDescription(ErrorList.init(context).getErrorMessage(errorCode))
                    .build()
                    .show();
        } catch (Exception e) {
            new LottieAlertDialog.Builder(context, DialogTypes.TYPE_ERROR)
                    .setTitle(context.getResources().getString(R.string.error))
                    .setDescription("[" + errorCode + "] " + context.getResources().getString(R.string.unknown_error))
                    .build()
                    .show();
        }


    }

    public void showCustomError(String message) {
        try {
            new LottieAlertDialog.Builder(context, DialogTypes.TYPE_WARNING)
                    .setTitle("Fatal Error!")
                    .setDescription(message)
                    .build()
                    .show();
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }


    }
}
