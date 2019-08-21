package com.gliesereum.coupler.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gliesereum.coupler.R;
import com.gliesereum.coupler.data.network.APIClient;
import com.gliesereum.coupler.data.network.APIInterface;
import com.gliesereum.coupler.data.network.CustomCallback;
import com.gliesereum.coupler.data.network.json.user.User;
import com.gliesereum.coupler.util.FastSave;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Response;

import static com.gliesereum.coupler.util.Constants.ACCESS_TOKEN;
import static com.gliesereum.coupler.util.Constants.ANDROID_APP;
import static com.gliesereum.coupler.util.Constants.BUSINESS_TYPE;
import static com.gliesereum.coupler.util.Constants.USER_ID;
import static com.gliesereum.coupler.util.Constants.USER_INFO;
import static com.gliesereum.coupler.util.Constants.USER_NAME;
import static com.gliesereum.coupler.util.Constants.USER_SECOND_NAME;

//import com.appizona.yehiahd.fastsave.FastSave;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText secondNameTextView;
    private TextInputEditText nameTextView;
    private TextInputEditText thirdNameTextView;
    private MaterialButton registrationBtn;
    private User user;
    private APIInterface API;
    private CustomCallback customCallback;
    private boolean doubleBackToExitPressedOnce;
    private TextInputLayout secondNameTextInputLayout;
    private TextInputLayout nameTextInputLayout;
    private TextInputLayout thirdNameTextInputLayout;
    private boolean firstNameFlag;
    private boolean secondNameFlag;
    private boolean thirdNameFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initData();
        initView();
    }

    private void initData() {
        FastSave.init(getApplicationContext());
        API = APIClient.getClient().create(APIInterface.class);
        customCallback = new CustomCallback(this, this);
        user = FastSave.getInstance().getObject(USER_INFO, User.class);
        doubleBackToExitPressedOnce = false;
    }

    private void initView() {
        secondNameTextView = findViewById(R.id.secondNameTextView);
        nameTextView = findViewById(R.id.nameTextView);
        thirdNameTextView = findViewById(R.id.thirdNameTextView);
        secondNameTextInputLayout = findViewById(R.id.secondNameTextInputLayout);
        nameTextInputLayout = findViewById(R.id.nameTextInputLayout);
        thirdNameTextInputLayout = findViewById(R.id.thirdNameTextInputLayout);
        registrationBtn = findViewById(R.id.registrationBtn);
        registrationBtn.setOnClickListener(this);

        if (user != null) {
            if (user.getFirstName() != null) {
                nameTextView.setText(user.getFirstName().toString());
            }
            if (user.getLastName() != null) {
                secondNameTextView.setText(user.getLastName().toString());
            }
            if (user.getMiddleName() != null) {
                thirdNameTextView.setText(user.getMiddleName().toString());
            }
        }
        nameTextView.addTextChangedListener(firstNameListener);
        secondNameTextView.addTextChangedListener(secondNameListener);
        thirdNameTextView.addTextChangedListener(thirdNameListener);

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

        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.registrationBtn:
                registerUser();
                break;
        }

    }

    private void registerUser() {
        if (!nameTextView.getText().toString().equals("") && !secondNameTextView.getText().toString().equals("") && !thirdNameTextView.getText().toString().equals("")) {
            user.setFirstName(nameTextView.getText().toString());
            user.setLastName(secondNameTextView.getText().toString());
            user.setMiddleName(thirdNameTextView.getText().toString());
            user.setCountry(ANDROID_APP);
            user.setAddress(ANDROID_APP);
            user.setCity(ANDROID_APP);
            user.setAddAddress(ANDROID_APP);
            user.setPosition(ANDROID_APP);

            API.updateUser(FastSave.getInstance().getString(ACCESS_TOKEN, ""), user)
                    .enqueue(customCallback.getResponse(new CustomCallback.ResponseCallback<User>() {
                        @Override
                        public void onSuccessful(Call<User> call, Response<User> response) {
                            FastSave.getInstance().saveObject(USER_INFO, user);
                            FastSave.getInstance().saveString(USER_ID, response.body().getId());
                            FastSave.getInstance().saveString(USER_NAME, response.body().getFirstName());
                            FastSave.getInstance().saveString(USER_SECOND_NAME, response.body().getLastName());
                            if (FastSave.getInstance().getString(BUSINESS_TYPE, "").equals("CAR")) {
                                startActivity(new Intent(RegisterActivity.this, CarListActivity.class));
                                finish();
                            } else {
                                if (FastSave.getInstance().getString(BUSINESS_TYPE, "").equals("")) {
                                    startActivity(new Intent(RegisterActivity.this, ChooseServiceNewActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                                    finish();
                                } else {
//                                    startActivity(new Intent(RegisterActivity.this, ChooseServiceNewActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                                    finish();
                                }
//                                if (FastSave.getInstance().getBoolean(OPEN_SERVICE_FLAG, false)) {
//                                    startActivity(new Intent(RegisterActivity.this, CarWashActivity.class));
//                                    finish();
//                                } else {
//                                    startActivity(new Intent(RegisterActivity.this, MapsActivity.class));
//                                    finish();
//                                }
                            }
                        }

                        @Override
                        public void onEmpty(Call<User> call, Response<User> response) {

                        }
                    }));
        } else {
            Toast.makeText(RegisterActivity.this, "Заполните все поля", Toast.LENGTH_SHORT).show();
        }
    }

    TextWatcher firstNameListener = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() < 2) {
                nameTextInputLayout.setError("Обязательное поле");
                firstNameFlag = false;
                checkButton();
            } else {
                nameTextInputLayout.setError(null);
                firstNameFlag = true;
                checkButton();

            }

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }
    };

    TextWatcher secondNameListener = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() < 2) {
                secondNameTextInputLayout.setError("Обязательное поле");
                secondNameFlag = false;
                checkButton();
            } else {
                secondNameTextInputLayout.setError(null);
                secondNameFlag = true;
                checkButton();

            }

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }
    };

    TextWatcher thirdNameListener = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() < 3) {
                thirdNameTextInputLayout.setError("Обязательное поле");
                thirdNameFlag = false;
                checkButton();
            } else {
                thirdNameTextInputLayout.setError(null);
                thirdNameFlag = true;
                checkButton();
            }

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }
    };

    private void checkButton() {
        if (firstNameFlag && secondNameFlag && thirdNameFlag) {
            registrationBtn.setEnabled(true);
        } else {
            registrationBtn.setEnabled(false);
        }
    }
}
