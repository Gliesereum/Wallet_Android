package com.gliesereum.karma.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.appizona.yehiahd.fastsave.FastSave;
import com.gliesereum.karma.R;
import com.gliesereum.karma.data.network.APIClient;
import com.gliesereum.karma.data.network.APIInterface;
import com.gliesereum.karma.data.network.json.user.User;
import com.gliesereum.karma.util.ErrorHandler;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.gliesereum.karma.util.Constants.ACCESS_TOKEN;
import static com.gliesereum.karma.util.Constants.ANDROID_APP;
import static com.gliesereum.karma.util.Constants.USER_NAME;
import static com.gliesereum.karma.util.Constants.USER_SECOND_NAME;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText secondNameTextView;
    private TextInputEditText nameTextView;
    private TextInputEditText thirdNameTextView;
    private MaterialButton registrationBtn;
    private User user;
    private APIInterface apiInterface;
    private ErrorHandler errorHandler;
    private boolean doubleBackToExitPressedOnce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initData();
        initView();
    }

    private void initData() {
        errorHandler = new ErrorHandler(this, this);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        user = FastSave.getInstance().getObject("userInfo", User.class);
        doubleBackToExitPressedOnce = false;
    }

    private void initView() {
        secondNameTextView = findViewById(R.id.secondNameTextView);
        nameTextView = findViewById(R.id.nameTextView);
        thirdNameTextView = findViewById(R.id.thirdNameTextView);
        registrationBtn = findViewById(R.id.registrationBtn);
        registrationBtn.setOnClickListener(this);
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
            user.setCoverUrl(ANDROID_APP);
            user.setCountry(ANDROID_APP);
            user.setAddress(ANDROID_APP);
            user.setAvatarUrl(ANDROID_APP);
            user.setCity(ANDROID_APP);
            user.setAddAddress(ANDROID_APP);
            user.setPosition(ANDROID_APP);

            Call<User> call = apiInterface.updateUser(FastSave.getInstance().getString(ACCESS_TOKEN, ""), user);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.code() == 200) {
                        FastSave.getInstance().saveString(USER_NAME, response.body().getFirstName());
                        FastSave.getInstance().saveString(USER_SECOND_NAME, response.body().getLastName());
                        startActivity(new Intent(RegisterActivity.this, CarListActivity.class));
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
                public void onFailure(Call<User> call, Throwable t) {
                    errorHandler.showCustomError(t.getMessage());
                }
            });
        } else {
            Toast.makeText(RegisterActivity.this, "Enter value", Toast.LENGTH_SHORT).show();
        }
    }
}
