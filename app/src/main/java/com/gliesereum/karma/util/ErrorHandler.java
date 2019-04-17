package com.gliesereum.karma.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.gliesereum.karma.R;
import com.gliesereum.karma.ui.SplashActivity;
import com.labters.lottiealertdialoglibrary.ClickListener;
import com.labters.lottiealertdialoglibrary.DialogTypes;
import com.labters.lottiealertdialoglibrary.LottieAlertDialog;

import org.jetbrains.annotations.NotNull;

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
                    .setPositiveText("Обновить")
                    .setPositiveListener(new ClickListener() {
                        @Override
                        public void onClick(@NotNull LottieAlertDialog lottieAlertDialog) {
                            activity.startActivity(new Intent(context, SplashActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                            activity.finish();
                        }
                    })
                    .build()
                    .show();
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }


    }
}
