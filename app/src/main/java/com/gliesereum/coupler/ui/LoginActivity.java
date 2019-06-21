package com.gliesereum.coupler.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.RemoteException;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.installreferrer.api.InstallReferrerClient;
import com.android.installreferrer.api.InstallReferrerStateListener;
import com.android.installreferrer.api.ReferrerDetails;
import com.chaos.view.PinView;
import com.gliesereum.coupler.R;
import com.gliesereum.coupler.data.network.APIClient;
import com.gliesereum.coupler.data.network.APIInterface;
import com.gliesereum.coupler.data.network.CustomCallback;
import com.gliesereum.coupler.data.network.json.code.CodeResponse;
import com.gliesereum.coupler.data.network.json.code.SigninBody;
import com.gliesereum.coupler.data.network.json.notificatoin.NotificatoinBody;
import com.gliesereum.coupler.data.network.json.notificatoin.UserSubscribe;
import com.gliesereum.coupler.data.network.json.user.UserResponse;
import com.gliesereum.coupler.util.FastSave;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.iid.FirebaseInstanceId;
import com.rilixtech.CountryCodePicker;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Response;

import static com.gliesereum.coupler.util.Constants.ACCESS_EXPIRATION_DATE;
import static com.gliesereum.coupler.util.Constants.ACCESS_TOKEN;
import static com.gliesereum.coupler.util.Constants.ACCESS_TOKEN_WITHOUT_BEARER;
import static com.gliesereum.coupler.util.Constants.FIREBASE_TOKEN;
import static com.gliesereum.coupler.util.Constants.IS_LOGIN;
import static com.gliesereum.coupler.util.Constants.KARMA_USER_RECORD;
import static com.gliesereum.coupler.util.Constants.KARMA_USER_REMIND_RECORD;
import static com.gliesereum.coupler.util.Constants.OPEN_SERVICE_FLAG;
import static com.gliesereum.coupler.util.Constants.REFRESH_EXPIRATION_DATE;
import static com.gliesereum.coupler.util.Constants.REFRESH_TOKEN;
import static com.gliesereum.coupler.util.Constants.REF_CODE;
import static com.gliesereum.coupler.util.Constants.USER_AVATAR;
import static com.gliesereum.coupler.util.Constants.USER_ID;
import static com.gliesereum.coupler.util.Constants.USER_INFO;
import static com.gliesereum.coupler.util.Constants.USER_NAME;
import static com.gliesereum.coupler.util.Constants.USER_SECOND_NAME;

//import com.appizona.yehiahd.fastsave.FastSave;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private CountryCodePicker ccp;
    private MaterialButton getCodeBtn;
    private MaterialButton loginBtn;
    private TextInputEditText phoneTextView;
    private ConstraintLayout valueBlock;
    private PinView codeView;
    private TextView codeLabel1;
    private TextView codeLabel2;
    private TextView timerLabel;
    private String code;
    private CountDownTimer countDownTimer;
    private APIInterface API;
    private CustomCallback customCallback;
    private ImageView mapImageBtn;
    boolean doubleBackToExitPressedOnce;
    private InstallReferrerClient referrerClient;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initData();
        initView();
        getRefCode();
    }

    private void initData() {
        FastSave.init(getApplicationContext());
        API = APIClient.getClient().create(APIInterface.class);
        customCallback = new CustomCallback(this, this);
        doubleBackToExitPressedOnce = false;
    }

    private void initView() {
        mapImageBtn = findViewById(R.id.mapImageBtn);
        mapImageBtn.setOnClickListener(this);
        getCodeBtn = findViewById(R.id.registerBtn);
        getCodeBtn.setOnClickListener(this);
        loginBtn = findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(this);
        ccp = findViewById(R.id.ccp);
        phoneTextView = findViewById(R.id.phoneTextView);
        valueBlock = findViewById(R.id.valueBlock);
        codeView = findViewById(R.id.codeView);
        codeLabel1 = findViewById(R.id.codeLabel1);
        codeLabel2 = findViewById(R.id.codeLabel2);
        timerLabel = findViewById(R.id.timerLabel);
        phoneTextView.addTextChangedListener(phoneTextViewChangedListener);
        codeView.addTextChangedListener(codeViewChangedListener);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.registerBtn:
                v.setEnabled(false);
                getPhoneCode(ccp.getFullNumber() + phoneTextView.getText().toString());
                break;
            case R.id.loginBtn:
                v.setEnabled(false);
                signIn(new SigninBody(ccp.getFullNumber() + phoneTextView.getText().toString(), code, "PHONE", FastSave.getInstance().getString(REF_CODE, "")));
                break;
            case R.id.mapImageBtn:
                startActivity(new Intent(LoginActivity.this, MapsActivity.class));
                break;
        }

    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(180000, 1000) {
            public void onTick(long millisUntilFinished) {
                setTimerTime(millisUntilFinished);
            }

            public void onFinish() {
                showValueBlock();
            }
        }.start();
    }

    private void saveUserInfo(UserResponse user) {
        FastSave.getInstance().saveBoolean(IS_LOGIN, true);
        FastSave.getInstance().saveString(USER_AVATAR, user.getUser().getAvatarUrl());
        FastSave.getInstance().saveString(USER_ID, user.getUser().getId());
        FastSave.getInstance().saveString(ACCESS_TOKEN, "Bearer " + user.getTokenInfo().getAccessToken());
        FastSave.getInstance().saveString(ACCESS_TOKEN_WITHOUT_BEARER, user.getTokenInfo().getAccessToken());
        FastSave.getInstance().saveString(REFRESH_TOKEN, user.getTokenInfo().getRefreshToken());
        FastSave.getInstance().saveLong(ACCESS_EXPIRATION_DATE, user.getTokenInfo().getAccessExpirationDate());
        FastSave.getInstance().saveLong(REFRESH_EXPIRATION_DATE, user.getTokenInfo().getRefreshExpirationDate());
        FastSave.getInstance().saveObject(USER_INFO, user);
        getRegToken(user);
    }

    private void getRegToken(UserResponse user) {
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, instanceIdResult -> {
            String newToken = instanceIdResult.getToken();
            Log.e("TAG_NOTIF", newToken);
            FastSave.getInstance().saveString(FIREBASE_TOKEN, newToken);
            UserSubscribe userSubscribe = new UserSubscribe(true, KARMA_USER_RECORD);
            UserSubscribe userSubscribe2 = new UserSubscribe(true, KARMA_USER_REMIND_RECORD);
            NotificatoinBody notificatoinBody = new NotificatoinBody(newToken, true, Arrays.asList(userSubscribe, userSubscribe2));
            API.sendRegistrationToken(FastSave.getInstance().getString(ACCESS_TOKEN, ""), notificatoinBody)
                    .enqueue(customCallback.getResponse(new CustomCallback.ResponseCallback<NotificatoinBody>() {
                        @Override
                        public void onSuccessful(Call<NotificatoinBody> call, Response<NotificatoinBody> response) {
                            Toast.makeText(LoginActivity.this, "Уведомления включены", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onEmpty(Call<NotificatoinBody> call, Response<NotificatoinBody> response) {

                        }
                    }));
        });


    }

    public void signIn(SigninBody signinBody) {
        API.signIn(signinBody)
                .enqueue(customCallback.getResponseWithProgress(new CustomCallback.ResponseCallback<UserResponse>() {
                    @Override
                    public void onSuccessful(Call<UserResponse> call, Response<UserResponse> response) {
                        countDownTimer.cancel();
                        if (response.body().getUser().getFirstName() == null ||
                                response.body().getUser().getLastName() == null ||
                                response.body().getUser().getMiddleName() == null) {
                            saveUserInfo(response.body());
                            startActivity(new Intent(LoginActivity.this, RegisterActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        } else {
                            FastSave.getInstance().saveString(USER_NAME, response.body().getUser().getFirstName());
                            FastSave.getInstance().saveString(USER_SECOND_NAME, response.body().getUser().getLastName());
                            saveUserInfo(response.body());
                            if (FastSave.getInstance().getBoolean(OPEN_SERVICE_FLAG, false)) {
                                startActivity(new Intent(LoginActivity.this, CarWashActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                            } else {
                                startActivity(new Intent(LoginActivity.this, MapsActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                            }
                        }
                        finish();
                    }

                    @Override
                    public void onEmpty(Call<UserResponse> call, Response<UserResponse> response) {

                    }
                }));
    }

    private void getRefCode() {
        referrerClient = InstallReferrerClient.newBuilder(this).build();
        referrerClient.startConnection(installReferrerStateListener);
    }

    private InstallReferrerStateListener installReferrerStateListener =
            new InstallReferrerStateListener() {
                @Override
                public void onInstallReferrerSetupFinished(int responseCode) {
                    switch (responseCode) {
                        case InstallReferrerClient.InstallReferrerResponse.OK:
                            try {
                                ReferrerDetails response = referrerClient.getInstallReferrer();
                                response.getInstallReferrer();
                                response.getReferrerClickTimestampSeconds();
                                referrerClient.endConnection();
                                if (response.getReferrerClickTimestampSeconds() != 0) {
                                    FastSave.getInstance().saveString(REF_CODE, response.getInstallReferrer());
                                }
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                            break;
                        case InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED:
                            Toast.makeText(LoginActivity.this, "API не поддерживается текущей версией Google Play", Toast.LENGTH_SHORT).show();
                            break;
                        case InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE:
                            Toast.makeText(LoginActivity.this, "Соединение не может быть установлено", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }

                @Override
                public void onInstallReferrerServiceDisconnected() {
                    referrerClient.startConnection(installReferrerStateListener);
                }
            };

    public void getPhoneCode(String phone) {
        API.getPhoneCode(phone)
                .enqueue(customCallback.getResponseWithProgress(new CustomCallback.ResponseCallback<CodeResponse>() {
                    @Override
                    public void onSuccessful(Call<CodeResponse> call, Response<CodeResponse> response) {
                        showCodeBlock();
                        setPhoneCodeLabel(phone);
                        startTimer();
                    }

                    @Override
                    public void onEmpty(Call<CodeResponse> call, Response<CodeResponse> response) {

                    }
                }));
    }

    public void showValueBlock() {
        codeLabel1.setVisibility(View.GONE);
        codeLabel2.setVisibility(View.GONE);
        timerLabel.setVisibility(View.GONE);
        codeView.setVisibility(View.GONE);
        loginBtn.setVisibility(View.GONE);
        getCodeBtn.setVisibility(View.VISIBLE);
        valueBlock.setVisibility(View.VISIBLE);
        getCodeBtn.setEnabled(false);
    }

    public void showCodeBlock() {
        valueBlock.setVisibility(View.GONE);
        getCodeBtn.setVisibility(View.GONE);
        loginBtn.setVisibility(View.VISIBLE);
        codeLabel1.setVisibility(View.VISIBLE);
        codeLabel2.setVisibility(View.VISIBLE);
        timerLabel.setVisibility(View.VISIBLE);
        codeView.setVisibility(View.VISIBLE);
        loginBtn.setEnabled(false);
        codeView.requestFocus();
    }

    public void setTimerTime(long millisUntilFinished) {
        timerLabel.setText("" + String.format("%d:%d",
                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
    }

    public void setPhoneCodeLabel(String phone) {
        codeLabel2.setText("+" + phone);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Пожалуйста, нажмите НАЗАД еще раз, чтобы выйти", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    TextWatcher phoneTextViewChangedListener = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable s) {
            if (s.toString().length() > 5) {
                getCodeBtn.setEnabled(true);
            } else {
                getCodeBtn.setEnabled(false);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }
    };

    TextWatcher codeViewChangedListener = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() < 6) {
                loginBtn.setEnabled(false);
            } else {
                loginBtn.setEnabled(true);
            }

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            code = String.valueOf(s);
        }
    };
}
