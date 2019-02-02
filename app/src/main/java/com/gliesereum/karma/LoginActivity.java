package com.gliesereum.karma;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import com.appizona.yehiahd.fastsave.FastSave;
import com.chaos.view.PinView;
import com.gliesereum.karma.data.network.APIClient;
import com.gliesereum.karma.data.network.APIInterface;
import com.gliesereum.karma.data.network.json.code.CodeResponse;
import com.gliesereum.karma.data.network.json.code.SigninBody;
import com.gliesereum.karma.data.network.json.user.UserResponse;
import com.gliesereum.karma.util.ErrorHandler;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.rilixtech.CountryCodePicker;

import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.gliesereum.karma.util.Constants.ACCESS_EXPIRATION_DATE;
import static com.gliesereum.karma.util.Constants.ACCESS_TOKEN;
import static com.gliesereum.karma.util.Constants.ACCESS_TOKEN_WITHOUT_BEARER;
import static com.gliesereum.karma.util.Constants.IS_LOGIN;
import static com.gliesereum.karma.util.Constants.REFRESH_EXPIRATION_DATE;
import static com.gliesereum.karma.util.Constants.REFRESH_TOKEN;
import static com.gliesereum.karma.util.Constants.USER_ID;
import static com.gliesereum.karma.util.Constants.USER_NAME;
import static com.gliesereum.karma.util.Constants.USER_SECOND_NAME;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private CountryCodePicker ccp;
    private TextInputLayout phoneTextInputLayout;
    private MaterialButton getCodeBtn;
    private MaterialButton loginBtn;
    private TextInputEditText phoneTextView;
    private String TAG = "TAG";
    private ConstraintLayout valueBlock;
    private PinView codeView;
    private TextView codeLabel1;
    private TextView codeLabel2;
    private TextView timerLabel;
    private String code;
    private CountDownTimer countDownTimer;
    private APIInterface apiInterface;
    private ErrorHandler errorHandler;
    private MaterialButton registerBtn;
    private CardView cardView;
    private TextView textView2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FastSave.init(getApplicationContext());
        initView();
    }

    private void initView() {
        errorHandler = new ErrorHandler(this, this);
        getCodeBtn = findViewById(R.id.registerBtn);
        getCodeBtn.setOnClickListener(this);
        loginBtn = findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(this);
        ccp = findViewById(R.id.ccp);
        phoneTextInputLayout = findViewById(R.id.phoneTextInputLayout);
        phoneTextView = findViewById(R.id.phoneTextView);
        phoneTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 5) {
                    getCodeBtn.setEnabled(true);
                } else {
                    getCodeBtn.setEnabled(false);
                }
            }
        });
        valueBlock = findViewById(R.id.valueBlock);
        codeView = findViewById(R.id.codeView);
        codeView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                code = String.valueOf(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() < 6) {
                    loginBtn.setEnabled(false);
                } else {
                    loginBtn.setEnabled(true);
                }

            }
        });
        codeLabel1 = findViewById(R.id.codeLabel1);
        codeLabel2 = findViewById(R.id.codeLabel2);
        timerLabel = findViewById(R.id.timerLabel);
        registerBtn = (MaterialButton) findViewById(R.id.registerBtn);
        cardView = (CardView) findViewById(R.id.cardView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.registerBtn:
                getPhoneCode(ccp.getFullNumber() + phoneTextView.getText().toString());
                break;
            case R.id.loginBtn:
                    signIn(new SigninBody(ccp.getFullNumber() + phoneTextView.getText().toString(), code, "PHONE"));
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

    private void saveUserInfo(Response<UserResponse> user) {
        FastSave.getInstance().saveBoolean(IS_LOGIN, true);
        FastSave.getInstance().saveString(USER_ID, user.body().getUser().getId());
        FastSave.getInstance().saveString(ACCESS_TOKEN, "Bearer " + user.body().getTokenInfo().getAccessToken());
        FastSave.getInstance().saveString(ACCESS_TOKEN_WITHOUT_BEARER, user.body().getTokenInfo().getAccessToken());
        FastSave.getInstance().saveString(REFRESH_TOKEN, user.body().getTokenInfo().getRefreshToken());
        FastSave.getInstance().saveLong(ACCESS_EXPIRATION_DATE, user.body().getTokenInfo().getAccessExpirationDate());
        FastSave.getInstance().saveLong(REFRESH_EXPIRATION_DATE, user.body().getTokenInfo().getRefreshExpirationDate());
        FastSave.getInstance().saveObject("userInfo", user);
    }

    public void signIn(SigninBody signinBody) {
        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<UserResponse> call = apiInterface.signIn(signinBody);
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.code() == 200) {
                    countDownTimer.cancel();
                    saveUserInfo(response);
                    if (response.body().getUser().getFirstName() == null ||
                            response.body().getUser().getLastName() == null ||
                            response.body().getUser().getMiddleName() == null) {
                        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                    } else {
                        FastSave.getInstance().saveString(USER_NAME, response.body().getUser().getFirstName());
                        FastSave.getInstance().saveString(USER_SECOND_NAME, response.body().getUser().getLastName());
                        startActivity(new Intent(LoginActivity.this, MapsActivity.class));
                        saveUserInfo(response);
                    }
                    finish();
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
            public void onFailure(Call<UserResponse> call, Throwable t) {
                errorHandler.showCustomError(t.getMessage());
            }
        });
    }

//    public void signUp(SignupBody signupBody) {
//        apiInterface = APIClient.getClient().create(APIInterface.class);
//        Call<UserResponse> call = apiInterface.signUp(signupBody);
//        call.enqueue(new Callback<UserResponse>() {
//            @Override
//            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
//                if (response.code() == 200) {
//                    FastSave.getInstance().saveObject("userInfo", response.body().getUser());
//                    countDownTimer.cancel();
//                    saveUserInfo(response);
//                    if (response.body().getUser().getFirstName() == null ||
//                            response.body().getUser().getLastName() == null ||
//                            response.body().getUser().getMiddleName() == null) {
//                        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
//                    } else {
//                        startActivity(new Intent(LoginActivity.this, MapsActivity.class));
//                    }
//                    finish();
//                } else {
//                    try {
//                        JSONObject jObjError = new JSONObject(response.errorBody().string());
//                        errorHandler.showError(jObjError.getInt("code"));
//                    } catch (Exception e) {
//                        errorHandler.showCustomError(e.getMessage());
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<UserResponse> call, Throwable t) {
//                errorHandler.showCustomError(t.getMessage());
//            }
//        });
//    }

    public void getPhoneCode(String phone) {
        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<CodeResponse> call = apiInterface.getPhoneCode(phone);
        call.enqueue(new Callback<CodeResponse>() {
            @Override
            public void onResponse(Call<CodeResponse> call, Response<CodeResponse> response) {
                if (response.code() == 200) {
                    showCodeBlock();
                    setPhoneCodeLabel(phone);
                    startTimer();
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
            public void onFailure(Call<CodeResponse> call, Throwable t) {
                errorHandler.showCustomError(t.getMessage());
            }
        });
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
}
