package com.gliesereum.karma.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import com.gliesereum.karma.R;
import com.gliesereum.karma.data.network.APIClient;
import com.gliesereum.karma.data.network.APIInterface;
import com.gliesereum.karma.data.network.json.status.StatusResponse;
import com.gliesereum.karma.data.network.json.user.TokenInfo;
import com.gliesereum.karma.data.network.json.user.UserResponse;
import com.gliesereum.karma.util.ErrorHandler;
import com.gliesereum.karma.util.FastSave;
import com.gliesereum.karma.util.Util;
import com.labters.lottiealertdialoglibrary.DialogTypes;
import com.labters.lottiealertdialoglibrary.LottieAlertDialog;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.gliesereum.karma.util.Constants.ACCESS_EXPIRATION_DATE;
import static com.gliesereum.karma.util.Constants.ACCESS_TOKEN;
import static com.gliesereum.karma.util.Constants.ACCESS_TOKEN_WITHOUT_BEARER;
import static com.gliesereum.karma.util.Constants.BUSINESS_CATEGORY_ID;
import static com.gliesereum.karma.util.Constants.IS_LOGIN;
import static com.gliesereum.karma.util.Constants.REFRESH_EXPIRATION_DATE;
import static com.gliesereum.karma.util.Constants.REFRESH_TOKEN;
import static com.gliesereum.karma.util.Constants.STATUS_UP;
import static com.gliesereum.karma.util.Constants.USER_ID;
import static com.gliesereum.karma.util.Constants.USER_NAME;
import static com.gliesereum.karma.util.Constants.USER_SECOND_NAME;

//import com.appizona.yehiahd.fastsave.FastSave;

public class SplashActivity extends AppCompatActivity {

    private APIInterface API;
    private ErrorHandler errorHandler;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private ConstraintLayout errorBlock;
    private Button refreshBtn;
    private String TAG = "test_log";
    private LottieAlertDialog alertDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
//        getLocationPermission();
        initData();
        initView();
        if (FastSave.getInstance().getBoolean("CLEAN_STORAGE_1", true)) {
            FastSave.getInstance().clearSession();
            FastSave.getInstance().saveBoolean("CLEAN_STORAGE_1", false);
        }
        checkStatus();
    }

    private void initData() {
        FastSave.init(getApplicationContext());
        errorHandler = new ErrorHandler(this, this);
        API = APIClient.getClient().create(APIInterface.class);
    }

    private void initView() {
        errorBlock = findViewById(R.id.errorBlock);
        refreshBtn = findViewById(R.id.refreshBtn);
        refreshBtn.setOnClickListener(v -> checkStatus());
    }

    public void checkStatus() {
        errorBlock.setVisibility(View.GONE);
        showProgressDialog();
        Call<StatusResponse> call = API.checkStatus();
        call.enqueue(new Callback<StatusResponse>() {
            @Override
            public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                if (response.code() == 200) {
                    if (response.body().getAccountService() != null && response.body().getAccountService().equals(STATUS_UP)
                            && response.body().getMailService() != null && response.body().getMailService().equals(STATUS_UP)
                            && response.body().getKarmaService() != null && response.body().getKarmaService().equals(STATUS_UP)) {
                        checkAccessToken();
                    } else {
                        errorBlock.setVisibility(View.VISIBLE);
                    }
                } else {
                    errorBlock.setVisibility(View.VISIBLE);
                }
                closeProgressDialog();
            }

            @Override
            public void onFailure(Call<StatusResponse> call, Throwable t) {
                errorBlock.setVisibility(View.VISIBLE);
                closeProgressDialog();
            }
        });
    }

    public void checkAccessToken() {
        Log.d(TAG, "checkAccessToken: ");
        Call<UserResponse> call = API.checkAccessToken(FastSave.getInstance().getString(ACCESS_TOKEN_WITHOUT_BEARER, ""));
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.code() == 200) {
                    if (FastSave.getInstance().getBoolean(IS_LOGIN, false)) {
                        checkToken();
                    } else {
                        if (FastSave.getInstance().getString(BUSINESS_CATEGORY_ID, "").equals("")) {
                            startActivity(new Intent(SplashActivity.this, ChooseServiceActivity.class));
                        } else {
                            startActivity(new Intent(SplashActivity.this, MapsActivity.class));
                        }
                        finish();
                    }
                } else {
                    checkToken();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                errorBlock.setVisibility(View.VISIBLE);
                closeProgressDialog();
            }
        });
    }

    public void checkToken() {
        Log.d(TAG, "checkToken: ");
        Long accessExpirationDate = FastSave.getInstance().getLong(ACCESS_EXPIRATION_DATE, 0);
        Long refreshExpirationDate = FastSave.getInstance().getLong(REFRESH_EXPIRATION_DATE, 0);

        if (accessExpirationDate != 0) {
            if (Util.checkExpirationToken(accessExpirationDate)) {
                FastSave.getInstance().saveBoolean(IS_LOGIN, true);
                if (FastSave.getInstance().getString(USER_NAME, "").equals("") || FastSave.getInstance().getString(USER_SECOND_NAME, "").equals("")) {
                    startActivity(new Intent(SplashActivity.this, RegisterActivity.class));
                    finish();
                } else {
                    if (FastSave.getInstance().getString(BUSINESS_CATEGORY_ID, "").equals("")) {
                        startActivity(new Intent(SplashActivity.this, ChooseServiceActivity.class));
                    } else {
                        startActivity(new Intent(SplashActivity.this, MapsActivity.class));
                    }
                    finish();
                }
            } else {
                if (Util.checkExpirationToken(refreshExpirationDate)) {
                    refreshToken(FastSave.getInstance().getString(REFRESH_TOKEN, ""));
                } else {
                    FastSave.getInstance().saveBoolean(IS_LOGIN, false);
                    if (FastSave.getInstance().getString(BUSINESS_CATEGORY_ID, "").equals("")) {
                        startActivity(new Intent(SplashActivity.this, ChooseServiceActivity.class));
                    } else {
                        startActivity(new Intent(SplashActivity.this, MapsActivity.class));
                    }
                    finish();
                }
            }
        } else {
            FastSave.getInstance().saveBoolean(IS_LOGIN, false);
            if (FastSave.getInstance().getString(BUSINESS_CATEGORY_ID, "").equals("")) {
                startActivity(new Intent(SplashActivity.this, ChooseServiceActivity.class));
            } else {
                startActivity(new Intent(SplashActivity.this, MapsActivity.class));
            }
            finish();
        }
    }

    public void refreshToken(String refreshToken) {
        Log.d(TAG, "refreshToken: ");
        API = APIClient.getClient().create(APIInterface.class);
        Call<UserResponse> call = API.refreshAccessToken(refreshToken);
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.code() == 200) {
                    FastSave.getInstance().saveBoolean(IS_LOGIN, true);
                    Toast.makeText(SplashActivity.this, "Refresh!", Toast.LENGTH_SHORT).show();
                    setTokenInfo(response.body().getTokenInfo());
                    if (FastSave.getInstance().getString(USER_NAME, "").equals("") || FastSave.getInstance().getString(USER_SECOND_NAME, "").equals("")) {
                        startActivity(new Intent(SplashActivity.this, RegisterActivity.class));
                        finish();
                    } else {
                        if (FastSave.getInstance().getString(BUSINESS_CATEGORY_ID, "").equals("")) {
                            startActivity(new Intent(SplashActivity.this, ChooseServiceActivity.class));
                        } else {
                            startActivity(new Intent(SplashActivity.this, MapsActivity.class));
                        }
                        finish();
                    }
                } else {
                    try {
                        FastSave.getInstance().saveBoolean(IS_LOGIN, false);
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        errorHandler.showError(jObjError.getInt("code"));
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                        finish();
                    } catch (Exception e) {
                        FastSave.getInstance().saveBoolean(IS_LOGIN, false);
                        errorHandler.showCustomError(e.getMessage());
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                        finish();
                    }
                }
//                closeProgressDialog();
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
//                closeProgressDialog();
                errorHandler.showCustomError(t.getMessage());
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();
            }
        });

    }

    private void setTokenInfo(TokenInfo response) {
        Log.d(TAG, "setTokenInfo: ");
        FastSave.getInstance().saveBoolean(IS_LOGIN, true);
        FastSave.getInstance().saveString(ACCESS_TOKEN, "Bearer " + response.getAccessToken());
        FastSave.getInstance().saveString(ACCESS_TOKEN_WITHOUT_BEARER, response.getAccessToken());
        FastSave.getInstance().saveString(REFRESH_TOKEN, response.getRefreshToken());
        FastSave.getInstance().saveString(USER_ID, response.getUserId());
        FastSave.getInstance().saveLong(ACCESS_EXPIRATION_DATE, response.getAccessExpirationDate());
        FastSave.getInstance().saveLong(REFRESH_EXPIRATION_DATE, response.getRefreshExpirationDate());
    }

    private void getLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        }
    }

    public void showProgressDialog() {
//        progressDialog = ProgressDialog.show(this, "Ща сек...", "Ща все сделаю...");
//        ProgressLoadingJIGB.startLoadingJIGB(this, R.raw.loading,"Загрузка",1000,200,200);

        alertDialog = new LottieAlertDialog.Builder(this, DialogTypes.TYPE_LOADING)
                .setTitle("Загрузка")
                .setDescription("Загружается контент, подождите")
                .build();
        alertDialog.setCancelable(false);
        alertDialog.show();

    }

    public void closeProgressDialog() {
//        ProgressLoadingJIGB.finishLoadingJIGB(this);

        alertDialog.dismiss();

//        if (progressDialog != null) {
//            progressDialog.dismiss();
//        }
    }
}
