package com.gliesereum.coupler.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.installreferrer.api.InstallReferrerClient;
import com.gliesereum.coupler.R;
import com.gliesereum.coupler.data.network.APIClient;
import com.gliesereum.coupler.data.network.APIInterface;
import com.gliesereum.coupler.data.network.CustomCallback;
import com.gliesereum.coupler.data.network.json.code.ReferralCodeResponse;
import com.gliesereum.coupler.util.FastSave;
import com.gliesereum.coupler.util.Util;
import com.google.zxing.EncodeHintType;

import net.glxn.qrgen.android.QRCode;

import retrofit2.Call;
import retrofit2.Response;

import static com.gliesereum.coupler.util.Constants.ACCESS_TOKEN;

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
    private String referralCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referal);
        initData();
        initView();
        getMyReferralCode();
    }

    private void getMyReferralCode() {
        API.getReferralCode(FastSave.getInstance().getString(ACCESS_TOKEN, ""))
                .enqueue(customCallback.getResponse(new CustomCallback.ResponseCallback<ReferralCodeResponse>() {
                    @Override
                    public void onSuccessful(Call<ReferralCodeResponse> call, Response<ReferralCodeResponse> response) {
                        referralCode = response.body().getCode();
                        Bitmap myBitmap = QRCode.from("https://coupler.app/r/" + referralCode).withHint(EncodeHintType.MARGIN, "1").withSize(700, 700).bitmap();
                        qrCode.setImageBitmap(myBitmap);
                    }

                    @Override
                    public void onEmpty(Call<ReferralCodeResponse> call, Response<ReferralCodeResponse> response) {

                    }
                }));
    }

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
                sendIntent.putExtra(Intent.EXTRA_TEXT, "А ты видел Coupler? Нет? Так вот тебе ссылочка на скачивание!" + "\n" + "https://coupler.app/r/" + referralCode);
                sendIntent.setType("text/plain");
                ReferralActivity.this.startActivity(Intent.createChooser(sendIntent, "Пригласи друга"));
            }
        });
    }
}
