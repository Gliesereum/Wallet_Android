package com.gliesereum.karma;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.appizona.yehiahd.fastsave.FastSave;
import com.gliesereum.karma.data.network.APIClient;
import com.gliesereum.karma.data.network.APIInterface;
import com.gliesereum.karma.data.network.json.user.User;
import com.gliesereum.karma.util.ErrorHandler;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.gliesereum.karma.util.Constants.ACCESS_TOKEN;
import static com.gliesereum.karma.util.Constants.ANDROID_APP;

public class RegisterActivity extends AppCompatActivity {


    private TextInputLayout secondNameTextInputLayout;
    private TextInputEditText secondNameTextView;
    private TextInputLayout nameTextInputLayout;
    private TextInputEditText nameTextView;
    private TextInputLayout thirdNameTextInputLayout;
    private TextInputEditText thirdNameTextView;
    private MaterialButton registerBtn;
    private User user;
    private APIInterface apiInterface;
    private ErrorHandler errorHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        FastSave.init(getApplicationContext());
        user = FastSave.getInstance().getObject("userInfo", User.class);
        initView();
    }

    private void initView() {
        errorHandler = new ErrorHandler(this, this);
        secondNameTextInputLayout = (TextInputLayout) findViewById(R.id.secondNameTextInputLayout);
        secondNameTextView = (TextInputEditText) findViewById(R.id.secondNameTextView);
        nameTextInputLayout = (TextInputLayout) findViewById(R.id.nameTextInputLayout);
        nameTextView = (TextInputEditText) findViewById(R.id.nameTextView);
        thirdNameTextInputLayout = (TextInputLayout) findViewById(R.id.thirdNameTextInputLayout);
        thirdNameTextView = (TextInputEditText) findViewById(R.id.thirdNameTextView);
        registerBtn = (MaterialButton) findViewById(R.id.registerBtn);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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


                    apiInterface = APIClient.getClient().create(APIInterface.class);
                    Call<User> call = apiInterface.updateUser("Bearer " + FastSave.getInstance().getString(ACCESS_TOKEN, ""), user);
                    call.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if (response.code() == 200) {
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
        });

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
}
