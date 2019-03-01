package com.gliesereum.karma.util;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.andrognito.flashbar.Flashbar;
import com.andrognito.flashbar.anim.FlashAnim;
import com.gliesereum.karma.R;

public class ErrorHandler {

    private Context context;
    private Activity activity;

    public ErrorHandler(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    public void showError(Integer errorCode) {
        try {
            new Flashbar.Builder(activity).gravity(Flashbar.Gravity.TOP)
                    .title(context.getResources().getString(R.string.error))
                    .message(ErrorList.init(context).getErrorMessage(errorCode))
                    .backgroundColorRes(R.color.error_color)
                    .duration(3000)
                    .enterAnimation(FlashAnim.with(context)
                            .animateBar()
                            .duration(750)
                            .alpha()
                            .overshoot())
                    .exitAnimation(FlashAnim.with(context)
                            .animateBar()
                            .duration(400)
                            .accelerateDecelerate())
                    .build()
                    .show();


        } catch (Exception e) {
            new Flashbar.Builder(activity).gravity(Flashbar.Gravity.TOP)
                    .title(context.getResources().getString(R.string.error))
                    .message("[" + errorCode + "] " + context.getResources().getString(R.string.unknown_error))
                    .backgroundColorRes(R.color.error_color)
                    .dismissOnTapOutside()
                    .enterAnimation(FlashAnim.with(context)
                            .animateBar()
                            .duration(750)
                            .alpha()
                            .overshoot())
                    .exitAnimation(FlashAnim.with(context)
                            .animateBar()
                            .duration(400)
                            .accelerateDecelerate())
                    .build()
                    .show();
        }


    }

    public void showCustomError(String message) {
        try {
            new Flashbar.Builder(activity).gravity(Flashbar.Gravity.TOP)
                    .title("Fatal Error!")
                    .message(message)
                    .backgroundColorRes(R.color.error_color)
                    .dismissOnTapOutside()
                    .enterAnimation(FlashAnim.with(context)
                            .animateBar()
                            .duration(750)
                            .alpha()
                            .overshoot())
                    .exitAnimation(FlashAnim.with(context)
                            .animateBar()
                            .duration(400)
                            .accelerateDecelerate())
                    .build()
                    .show();
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }


    }
}
