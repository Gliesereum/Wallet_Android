package com.gliesereum.coupler.ui;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.gliesereum.coupler.R;
import com.gliesereum.coupler.data.network.APIClient;
import com.gliesereum.coupler.data.network.APIInterface;
import com.gliesereum.coupler.data.network.CustomCallback;
import com.gliesereum.coupler.data.network.json.user.User;
import com.gliesereum.coupler.util.FastSave;
import com.gliesereum.coupler.util.Util;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Response;

import static com.gliesereum.coupler.util.Constants.ACCESS_TOKEN;
import static com.gliesereum.coupler.util.Constants.ANDROID_APP;
import static com.gliesereum.coupler.util.Constants.USER_NAME;
import static com.gliesereum.coupler.util.Constants.USER_SECOND_NAME;

//import com.appizona.yehiahd.fastsave.FastSave;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private TextInputEditText secondNameTextView;
    private TextInputEditText nameTextView;
    private TextInputEditText thirdNameTextView;
    private MaterialButton registerBtn;
    private APIInterface API;
    private CustomCallback customCallback;
    private TextInputLayout secondNameTextInputLayout;
    private TextInputLayout nameTextInputLayout;
    private TextInputLayout thirdNameTextInputLayout;
    private boolean firstNameFlag;
    private boolean secondNameFlag;
    private boolean thirdNameFlag;
    private boolean editFlag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initData();
        initView();
        getUser();
    }

    private void initData() {
        API = APIClient.getClient().create(APIInterface.class);
        customCallback = new CustomCallback(this, this);
    }


    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Мой профиль");
        setSupportActionBar(toolbar);
        new Util(this, toolbar, 4).addNavigation();
        secondNameTextView = findViewById(R.id.secondNameTextView);
        nameTextView = findViewById(R.id.nameTextView);
        thirdNameTextView = findViewById(R.id.thirdNameTextView);
        registerBtn = findViewById(R.id.registerBtn);
        registerBtn.setOnClickListener(this);
        secondNameTextInputLayout = findViewById(R.id.secondNameTextInputLayout);
        nameTextInputLayout = findViewById(R.id.nameTextInputLayout);
        thirdNameTextInputLayout = findViewById(R.id.thirdNameTextInputLayout);
        nameTextView.addTextChangedListener(firstNameListener);
        secondNameTextView.addTextChangedListener(secondNameListener);
        thirdNameTextView.addTextChangedListener(thirdNameListener);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main_menu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.edit_menu:
//                    secondNameTextView.setEnabled(true);
//                    nameTextView.setEnabled(true);
//                    thirdNameTextView.setEnabled(true);
//                    registerBtn.setEnabled(true);
//                    return true;
//
//
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

    private void getUser() {
        API.getUser(FastSave.getInstance().getString(ACCESS_TOKEN, ""))
                .enqueue(customCallback.getResponseWithProgress(new CustomCallback.ResponseCallback<User>() {
                    @Override
                    public void onSuccessful(Call<User> call, Response<User> response) {
                        nameTextView.setText(response.body().getFirstName());
                        secondNameTextView.setText(response.body().getLastName());
                        thirdNameTextView.setText(response.body().getMiddleName());
                        FastSave.getInstance().saveObject("userInfo", response.body());
                    }

                    @Override
                    public void onEmpty(Call<User> call, Response<User> response) {

                    }
                }));
    }

    @Override
    public void onClick(View v) {
        if (editFlag) {
            User user = FastSave.getInstance().getObject("userInfo", User.class);
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
                            FastSave.getInstance().saveString(USER_NAME, response.body().getFirstName());
                            FastSave.getInstance().saveString(USER_SECOND_NAME, response.body().getLastName());
                            secondNameTextView.setEnabled(false);
                            nameTextView.setEnabled(false);
                            thirdNameTextView.setEnabled(false);
                            registerBtn.setText("Изменить");
                            editFlag = false;
                            Toast.makeText(ProfileActivity.this, "Сохраненно", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onEmpty(Call<User> call, Response<User> response) {

                        }
                    }));

        } else {
            secondNameTextView.setEnabled(true);
            nameTextView.setEnabled(true);
            thirdNameTextView.setEnabled(true);
            secondNameTextView.requestFocus();
            secondNameTextView.setSelection(secondNameTextView.length());
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(secondNameTextView, InputMethodManager.SHOW_IMPLICIT);
            registerBtn.setText("Сохранить");
            editFlag = true;


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
            Log.d("checkButton", "checkButton: true");
            registerBtn.setEnabled(true);
        } else {
            registerBtn.setEnabled(false);
            Log.d("checkButton", "checkButton: false");

        }
    }
}
