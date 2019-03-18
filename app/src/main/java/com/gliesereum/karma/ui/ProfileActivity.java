package com.gliesereum.karma.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.appizona.yehiahd.fastsave.FastSave;
import com.gliesereum.karma.R;
import com.gliesereum.karma.data.network.APIClient;
import com.gliesereum.karma.data.network.APIInterface;
import com.gliesereum.karma.data.network.json.user.User;
import com.gliesereum.karma.util.ErrorHandler;
import com.gliesereum.karma.util.Util;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.gliesereum.karma.util.Constants.ACCESS_TOKEN;
import static com.gliesereum.karma.util.Constants.ANDROID_APP;
import static com.gliesereum.karma.util.Constants.USER_NAME;
import static com.gliesereum.karma.util.Constants.USER_SECOND_NAME;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private TextInputEditText secondNameTextView;
    private TextInputEditText nameTextView;
    private TextInputEditText thirdNameTextView;
    private MaterialButton registerBtn;
    private APIInterface apiInterface;
    private ErrorHandler errorHandler;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initData();
        initView();
        getUser();
    }

    private void initData() {
        apiInterface = APIClient.getClient().create(APIInterface.class);
        errorHandler = new ErrorHandler(this, this);
    }


    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        new Util(this, toolbar, 4).addNavigation();
        secondNameTextView = findViewById(R.id.secondNameTextView);
        nameTextView = findViewById(R.id.nameTextView);
        thirdNameTextView = findViewById(R.id.thirdNameTextView);
        registerBtn = findViewById(R.id.registerBtn);
        registerBtn.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_menu:
                Toast.makeText(this, "Edit", Toast.LENGTH_SHORT).show();
                secondNameTextView.setEnabled(true);
                nameTextView.setEnabled(true);
                thirdNameTextView.setEnabled(true);
                registerBtn.setEnabled(true);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getUser() {
        showProgressDialog();
        Call<User> call = apiInterface.getUser(FastSave.getInstance().getString(ACCESS_TOKEN, ""));
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.code() == 200) {
                    nameTextView.setText(response.body().getFirstName());
                    secondNameTextView.setText(response.body().getLastName());
                    thirdNameTextView.setText(response.body().getMiddleName());
                    FastSave.getInstance().saveObject("userInfo", response.body());
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        errorHandler.showError(jObjError.getInt("code"));
                    } catch (Exception e) {
                        errorHandler.showCustomError(e.getMessage());
                        closeProgressDialog();
                    }
                }
                closeProgressDialog();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                errorHandler.showCustomError(t.getMessage());
                closeProgressDialog();
            }
        });
    }

    public void showProgressDialog() {
        progressDialog = ProgressDialog.show(this, "Ща сек...", "Ща все сделаю...");
    }

    public void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        User user = FastSave.getInstance().getObject("userInfo", User.class);
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
                    secondNameTextView.setEnabled(false);
                    nameTextView.setEnabled(false);
                    thirdNameTextView.setEnabled(false);
                    registerBtn.setEnabled(false);
                    Toast.makeText(ProfileActivity.this, "Сохраненно", Toast.LENGTH_SHORT).show();
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
    }
}
