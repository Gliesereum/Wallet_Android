package com.gliesereum.karma;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.appizona.yehiahd.fastsave.FastSave;
import com.gliesereum.karma.data.network.APIClient;
import com.gliesereum.karma.data.network.APIInterface;
import com.gliesereum.karma.data.network.json.user.TokenInfo;
import com.gliesereum.karma.util.ErrorHandler;
import com.gliesereum.karma.util.Util;

import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.gliesereum.karma.util.Constants.ACCESS_EXPIRATION_DATE;
import static com.gliesereum.karma.util.Constants.ACCESS_TOKEN;
import static com.gliesereum.karma.util.Constants.ACCESS_TOKEN_WITHOUT_BEARER;
import static com.gliesereum.karma.util.Constants.IS_LOGIN;
import static com.gliesereum.karma.util.Constants.REFRESH_EXPIRATION_DATE;
import static com.gliesereum.karma.util.Constants.REFRESH_TOKEN;

public class SplashActivity extends AppCompatActivity {

    private APIInterface apiInterface;
    private ErrorHandler errorHandler;
    private static final int REQUEST_LOCATION_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getLocationPermission();
        FastSave.init(getApplicationContext());
        errorHandler = new ErrorHandler(this, this);

        if (FastSave.getInstance().getBoolean(IS_LOGIN, false)) {
            checkToken();
        } else {
            startActivity(new Intent(SplashActivity.this, MapsActivity.class));
            finish();
        }


    }

    public void checkToken() {
        Long accessExpirationDate = FastSave.getInstance().getLong(ACCESS_EXPIRATION_DATE, 0);
        Long refreshExpirationDate = FastSave.getInstance().getLong(REFRESH_EXPIRATION_DATE, 0);

        if (!accessExpirationDate.equals("")) {
            if (Util.checkExpirationToken(accessExpirationDate)) {
                FastSave.getInstance().saveBoolean(IS_LOGIN, true);
                startActivity(new Intent(SplashActivity.this, MapsActivity.class));
                finish();
            } else {
                if (Util.checkExpirationToken(refreshExpirationDate)) {
                    refreshToken(FastSave.getInstance().getString(ACCESS_TOKEN_WITHOUT_BEARER, ""), FastSave.getInstance().getString(REFRESH_TOKEN, ""));
                } else {
                    FastSave.getInstance().saveBoolean(IS_LOGIN, false);
                    startActivity(new Intent(SplashActivity.this, MapsActivity.class));
                    finish();
                }
            }
        } else {
            FastSave.getInstance().saveBoolean(IS_LOGIN, false);
            startActivity(new Intent(SplashActivity.this, MapsActivity.class));
            finish();
        }
    }

    public void refreshToken(String accessToken, String refreshToken) {
        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<TokenInfo> call = apiInterface.refreshAccessToken(accessToken, refreshToken);
        call.enqueue(new Callback<TokenInfo>() {
            @Override
            public void onResponse(Call<TokenInfo> call, Response<TokenInfo> response) {
                if (response.code() == 200) {
                    FastSave.getInstance().saveBoolean(IS_LOGIN, true);
                    Toast.makeText(SplashActivity.this, "Refresh!", Toast.LENGTH_SHORT).show();
                    setTokenInfo(response);
                    startActivity(new Intent(SplashActivity.this, MapsActivity.class));
                    finish();
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
            }

            @Override
            public void onFailure(Call<TokenInfo> call, Throwable t) {
                errorHandler.showCustomError(t.getMessage());
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();
            }
        });

    }

    private void setTokenInfo(Response<TokenInfo> response) {
        FastSave.getInstance().saveBoolean(IS_LOGIN, true);
        FastSave.getInstance().saveString(ACCESS_TOKEN, "Bearer " + response.body().getAccessToken());
        Log.e("TAG", "ACCESS_TOKEN: " + FastSave.getInstance().getString(ACCESS_TOKEN, ""));
        FastSave.getInstance().saveString(ACCESS_TOKEN_WITHOUT_BEARER, response.body().getAccessToken());
        FastSave.getInstance().saveString(REFRESH_TOKEN, response.body().getRefreshToken());
        FastSave.getInstance().saveLong(ACCESS_EXPIRATION_DATE, response.body().getAccessExpirationDate());
        FastSave.getInstance().saveLong(REFRESH_EXPIRATION_DATE, response.body().getRefreshExpirationDate());
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


}
