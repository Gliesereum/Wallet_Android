package com.gliesereum.karma.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.appizona.yehiahd.fastsave.FastSave;
import com.gliesereum.karma.R;
import com.gliesereum.karma.data.network.APIClient;
import com.gliesereum.karma.data.network.APIInterface;
import com.gliesereum.karma.data.network.CustomCallback;
import com.gliesereum.karma.data.network.json.user.User;
import com.gliesereum.karma.util.Util;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import retrofit2.Call;
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
    private APIInterface API;
    private CustomCallback customCallback;


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

        API.updateUser(FastSave.getInstance().getString(ACCESS_TOKEN, ""), user)
                .enqueue(customCallback.getResponse(new CustomCallback.ResponseCallback<User>() {
                    @Override
                    public void onSuccessful(Call<User> call, Response<User> response) {
                        FastSave.getInstance().saveString(USER_NAME, response.body().getFirstName());
                        FastSave.getInstance().saveString(USER_SECOND_NAME, response.body().getLastName());
                        secondNameTextView.setEnabled(false);
                        nameTextView.setEnabled(false);
                        thirdNameTextView.setEnabled(false);
                        registerBtn.setEnabled(false);
                        Toast.makeText(ProfileActivity.this, "Сохраненно", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onEmpty(Call<User> call, Response<User> response) {

                    }
                }));
    }
}
