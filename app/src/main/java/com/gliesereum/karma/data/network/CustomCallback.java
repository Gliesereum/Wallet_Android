package com.gliesereum.karma.data.network;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;

import com.gliesereum.karma.util.ErrorHandler;
import com.labters.lottiealertdialoglibrary.DialogTypes;
import com.labters.lottiealertdialoglibrary.LottieAlertDialog;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomCallback {

    private Context context;
    private ErrorHandler errorHandler;
    private ProgressDialog progressDialog;
    private LottieAlertDialog alertDialog;


    public CustomCallback(Context context, Activity activity) {
        this.context = context;
        this.errorHandler = new ErrorHandler(context, activity);
    }

    public <T> Callback<T> getResponse(ResponseCallback<T> responseCallback) {
        return new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                if (response.code() == 200) {
                    responseCallback.onSuccessful(call, response);
                } else if (response.code() == 204) {
                    responseCallback.onEmpty(call, response);
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        errorHandler.showError(jObjError.getInt("code"));
                    } catch (Exception e) {
                        errorHandler.showCustomError(e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                errorHandler.showCustomError(t.getMessage());
            }
        };
    }

    public <T> Callback<T> getResponseWithProgress(ResponseCallback<T> responseCallback) {
        showProgressDialog();
        return new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                if (response.code() == 200) {
                    responseCallback.onSuccessful(call, response);
                } else if (response.code() == 204) {
                    responseCallback.onEmpty(call, response);
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        errorHandler.showError(jObjError.getInt("code"));
                    } catch (Exception e) {
                        errorHandler.showCustomError(e.getMessage());
                    }
                }
                closeProgressDialog();
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                errorHandler.showCustomError(t.getMessage());
                closeProgressDialog();
            }
        };
    }

    public interface ResponseCallback<T> {
        void onSuccessful(Call<T> call, Response<T> response);

        void onEmpty(Call<T> call, Response<T> response);
    }

    private void showProgressDialog() {
        if (alertDialog == null || !alertDialog.isShowing()) {
            alertDialog = new LottieAlertDialog.Builder(context, DialogTypes.TYPE_LOADING)
                    .setTitle("Загрузка")
                    .setDescription("Загружается контент, подождите")
                    .build();
            alertDialog.setCancelable(false);
            alertDialog.show();
        }
//        if (progressDialog ==null || !progressDialog.isShowing()){
//            progressDialog = ProgressDialog.show(context, "Ща сек...", "Ща все сделаю...");
//        }
    }

    private void closeProgressDialog() {
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
//        if (progressDialog != null && progressDialog.isShowing()) {
//            progressDialog.dismiss();
//        }
    }
}
