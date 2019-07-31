package com.gliesereum.coupler.ui;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.gliesereum.coupler.R;
import com.gliesereum.coupler.data.network.APIClient;
import com.gliesereum.coupler.data.network.APIInterface;
import com.gliesereum.coupler.data.network.CustomCallback;
import com.gliesereum.coupler.data.network.json.avatar.UploadResponse;
import com.gliesereum.coupler.data.network.json.user.User;
import com.gliesereum.coupler.util.CircleTransform;
import com.gliesereum.coupler.util.FastSave;
import com.gliesereum.coupler.util.Util;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

import static com.gliesereum.coupler.util.Constants.ACCESS_TOKEN;
import static com.gliesereum.coupler.util.Constants.USER_AVATAR;
import static com.gliesereum.coupler.util.Constants.USER_INFO;
import static com.gliesereum.coupler.util.Constants.USER_NAME;
import static com.gliesereum.coupler.util.Constants.USER_SECOND_NAME;


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
    private String TAG = "upload";
    private ImageView avatarImg;
    private Button uploadBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initData();
        initView();
        getUser();
    }

    private void initData() {
        FastSave.init(getApplicationContext());
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

        avatarImg = findViewById(R.id.avatarImg);
        uploadBtn = findViewById(R.id.uploadBtn);
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.Companion.with(ProfileActivity.this)
//                        .galleryOnly()
                        .compress(1024)
                        .maxResultSize(640, 640)
                        .cropSquare()
                        .start();
            }
        });

    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            File imgFileOrig = new File(getPath(ProfileActivity.this, data.getData()));
            RequestBody reqFile = RequestBody.create(MediaType.parse("file"), imgFileOrig);
            MultipartBody.Part body = MultipartBody.Part.createFormData("file", imgFileOrig.getName(), reqFile);
            RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "true");
            API.uploadAvatar(FastSave.getInstance().getString(ACCESS_TOKEN, ""), body, name)
                    .enqueue(customCallback.getResponseWithProgress(new CustomCallback.ResponseCallback<UploadResponse>() {
                        @Override
                        public void onSuccessful(Call<UploadResponse> call, Response<UploadResponse> response) {
                            Picasso.get().load(response.body().getUrl()).transform(new CircleTransform()).into(avatarImg);
                            User user = FastSave.getInstance().getObject(USER_INFO, User.class);
                            user.setAvatarUrl(response.body().getUrl());
                            API.updateUser(FastSave.getInstance().getString(ACCESS_TOKEN, ""), user)
                                    .enqueue(customCallback.getResponse(new CustomCallback.ResponseCallback<User>() {
                                        @Override
                                        public void onSuccessful(Call<User> call, Response<User> response) {
                                            FastSave.getInstance().saveString(USER_AVATAR, response.body().getAvatarUrl());
                                            new Util(ProfileActivity.this, toolbar, 4).addNavigation();
                                            Toast.makeText(ProfileActivity.this, "Сохраненно", Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onEmpty(Call<User> call, Response<User> response) {

                                        }
                                    }));
                        }

                        @Override
                        public void onEmpty(Call<UploadResponse> call, Response<UploadResponse> response) {
                            Log.e(TAG, "onClick: ");
                        }
                    }));


        } else {
//            Toast.makeText(ProfileActivity.this, "You haven't picked Image", Toast.LENGTH_LONG).show();
        }
    }

    public String getPath(final Context context, final Uri uri) {

        // DocumentProvider
        if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    private void getUser() {
        API.getUser(FastSave.getInstance().getString(ACCESS_TOKEN, ""))
                .enqueue(customCallback.getResponseWithProgress(new CustomCallback.ResponseCallback<User>() {
                    @Override
                    public void onSuccessful(Call<User> call, Response<User> response) {
                        FastSave.getInstance().saveObject(USER_INFO, response.body());
                        if (response.body().getAvatarUrl() != null && !response.body().getAvatarUrl().equals("")) {
                            Picasso.get().load(response.body().getAvatarUrl()).transform(new CircleTransform()).into(avatarImg);
                        }
                        nameTextView.setText(response.body().getFirstName());
                        secondNameTextView.setText(response.body().getLastName());
                        thirdNameTextView.setText(response.body().getMiddleName());
                    }

                    @Override
                    public void onEmpty(Call<User> call, Response<User> response) {

                    }
                }));
    }

    @Override
    public void onClick(View v) {
        if (editFlag) {
            User user = FastSave.getInstance().getObject(USER_INFO, User.class);
            user.setFirstName(nameTextView.getText().toString());
            user.setLastName(secondNameTextView.getText().toString());
            user.setMiddleName(thirdNameTextView.getText().toString());

            API.updateUser(FastSave.getInstance().getString(ACCESS_TOKEN, ""), user)
                    .enqueue(customCallback.getResponse(new CustomCallback.ResponseCallback<User>() {
                        @Override
                        public void onSuccessful(Call<User> call, Response<User> response) {
                            FastSave.getInstance().saveObject(USER_INFO, response.body());
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
