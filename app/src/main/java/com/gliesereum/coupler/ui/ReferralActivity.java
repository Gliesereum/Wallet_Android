package com.gliesereum.coupler.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.installreferrer.api.InstallReferrerClient;
import com.android.installreferrer.api.InstallReferrerStateListener;
import com.android.installreferrer.api.ReferrerDetails;
import com.gliesereum.coupler.R;
import com.gliesereum.coupler.data.network.APIClient;
import com.gliesereum.coupler.data.network.APIInterface;
import com.gliesereum.coupler.data.network.CustomCallback;
import com.gliesereum.coupler.util.FastSave;
import com.gliesereum.coupler.util.Util;
import com.google.zxing.EncodeHintType;

import net.glxn.qrgen.android.QRCode;

public class ReferralActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private APIInterface API;
    private CustomCallback customCallback;
    private TextView textView10;
    private ImageView qrCode;
    private Uri mInvitationUrl;
    private String TAG = "referral";
    private InstallReferrerClient mReferrerClient;
    private Button button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referal);
        initData();
        initView();


        mReferrerClient = InstallReferrerClient.newBuilder(this).build();
        mReferrerClient.startConnection(installReferrerStateListener);

        Bitmap myBitmap = QRCode.from("https://coupler.app/r/suPeRCOdE").withHint(EncodeHintType.MARGIN, "1").withSize(700, 700).bitmap();
        qrCode.setImageBitmap(myBitmap);
    }

    private InstallReferrerStateListener installReferrerStateListener =
            new InstallReferrerStateListener() {
                @Override
                public void onInstallReferrerSetupFinished(int responseCode) {
                    switch (responseCode) {
                        case InstallReferrerClient.InstallReferrerResponse.OK:
                            // Соединение установлено
                            try {
                                Log.d("referral", "OK");
                                ReferrerDetails response = mReferrerClient.getInstallReferrer();
                                response.getInstallReferrer();
                                response.getReferrerClickTimestampSeconds();
                                response.getInstallBeginTimestampSeconds();
                                Log.d(TAG, "onInstallReferrerSetupFinished: " + response.getInstallReferrer());
                                Log.d(TAG, "onInstallReferrerSetupFinished: " + response.getReferrerClickTimestampSeconds());
                                Log.d(TAG, "onInstallReferrerSetupFinished: " + response.getInstallBeginTimestampSeconds());
                                mReferrerClient.endConnection();
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                            break;
                        case InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED:
                            Log.d("referral", "FEATURE_NOT_SUPPORTED");
                            // API не поддерживается текущей версией Google Play
                            break;
                        case InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE:
                            Log.d("referral", "SERVICE_UNAVAILABLE");
                            // Соединение не может быть установлено
                            break;
                    }
                }

                @Override
                public void onInstallReferrerServiceDisconnected() {
                    // Попробуйте перезапустите соединение, заново вызвав метод startConnection()
                    mReferrerClient.startConnection(installReferrerStateListener);
                }
            };

    private void initData() {
        FastSave.init(getApplicationContext());
        API = APIClient.getClient().create(APIInterface.class);
        customCallback = new CustomCallback(this, this);
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        new Util(this, toolbar, 9).addNavigation();
        qrCode = findViewById(R.id.qrCode);
        button2 = findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "А ты видел Coupler? Нет? Так вот тебе ссылочка на скачивание!" + "\n" + "https://coupler.app/r/suPeRCOdE");
                sendIntent.setType("text/plain");
                ReferralActivity.this.startActivity(Intent.createChooser(sendIntent, "Пригласи друга"));
            }
        });
    }
}
