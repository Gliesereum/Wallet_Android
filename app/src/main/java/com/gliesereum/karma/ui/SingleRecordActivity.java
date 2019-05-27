package com.gliesereum.karma.ui;

import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.github.okdroid.checkablechipview.CheckableChipView;
import com.gliesereum.karma.R;
import com.gliesereum.karma.data.network.APIClient;
import com.gliesereum.karma.data.network.APIInterface;
import com.gliesereum.karma.data.network.CustomCallback;
import com.gliesereum.karma.data.network.json.car.AllCarResponse;
import com.gliesereum.karma.data.network.json.record.AllRecordResponse;
import com.gliesereum.karma.util.FastSave;
import com.gliesereum.karma.util.Util;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.labters.lottiealertdialoglibrary.ClickListener;
import com.labters.lottiealertdialoglibrary.DialogTypes;
import com.labters.lottiealertdialoglibrary.LottieAlertDialog;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Response;

import static com.gliesereum.karma.util.Constants.ACCESS_TOKEN;

//import com.appizona.yehiahd.fastsave.FastSave;

public class SingleRecordActivity extends AppCompatActivity implements View.OnClickListener {

    private APIInterface API;
    private CustomCallback customCallback;
    private Button goRoad;
    private String TAG = "TAG";
    private AllRecordResponse record;
    private ConstraintLayout packageBlock;
    private LinearLayout packageItems;
    private ConstraintLayout servicePriceBlock;
    private LinearLayout servicePriceItem;
    private TextView carWashName;
    private TextView date;
    private TextView time;
    private TextView duration;
    private TextView price;
    private TextView car;
    private FusedLocationProviderClient mFusedLocationClient;
    private Location mLastKnownLocation;
    private TextView servicePriceLabel;
    private Button cancelRecordBtn;
    private LottieAlertDialog alertDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_record);
        initData();
        initView();
//        getDeviceLocation();
        if (getIntent().getStringExtra("objectId") != null) {
            getSingleRecord(getIntent().getStringExtra("objectId"));
        } else {
            getDeviceLocation();
            getCar(record.getTargetId());
            fillActivity(record);
        }
    }

    private void getSingleRecord(String objectId) {
        API.getSingleRecord(FastSave.getInstance().getString(ACCESS_TOKEN, ""), objectId)
                .enqueue(customCallback.getResponseWithProgress(new CustomCallback.ResponseCallback<AllRecordResponse>() {
                    @Override
                    public void onSuccessful(Call<AllRecordResponse> call, Response<AllRecordResponse> response) {
                        record = response.body();
                        getDeviceLocation();
                        getCar(record.getTargetId());
                        fillActivity(record);
                    }

                    @Override
                    public void onEmpty(Call<AllRecordResponse> call, Response<AllRecordResponse> response) {

                    }
                }));
    }

    private void initData() {
        API = APIClient.getClient().create(APIInterface.class);
        customCallback = new CustomCallback(this, this);
        record = FastSave.getInstance().getObject("RECORD", AllRecordResponse.class);
    }

    private void fillActivity(AllRecordResponse record) {
        date.setText(Util.getStringDate(record.getBegin()));
        time.setText(Util.getStringTime(record.getBegin()));
        duration.setText(String.valueOf((record.getFinish() - record.getBegin()) / 60000) + " мин");
        price.setText(String.valueOf(record.getPrice()) + " грн");
        carWashName.setText(record.getBusiness().getName());
        if (record.getStatusProcess().equals("COMPLETED") || record.getStatusProcess().equals("IN_PROCESS")) {
            cancelRecordBtn.setVisibility(View.GONE);
        }
        if (record.getPackageDto() != null) {
            packageBlock.setVisibility(View.VISIBLE);
            for (int i = 0; i < record.getPackageDto().getServices().size(); i++) {
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0, 4, 0, 4);
                CheckableChipView checkableChipView = new CheckableChipView(SingleRecordActivity.this);
                checkableChipView.setText(record.getPackageDto().getServices().get(i).getName());
                checkableChipView.setOutlineCornerRadius(10f);
                checkableChipView.setBackgroundColor(getResources().getColor(R.color.white));
                checkableChipView.setOutlineColor(getResources().getColor(R.color.black));
                checkableChipView.setCheckedColor(getResources().getColor(R.color.accent));
                checkableChipView.setEnabled(false);
                packageItems.addView(checkableChipView, 0, layoutParams);
            }
        } else {
            servicePriceLabel.setText("Выбранные услуги");
        }

        if (record.getServices() != null) {
            if (record.getServices().size() > 0) {
                servicePriceBlock.setVisibility(View.VISIBLE);
                for (int i = 0; i < record.getServices().size(); i++) {
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams.setMargins(0, 4, 0, 4);
                    CheckableChipView checkableChipView = new CheckableChipView(SingleRecordActivity.this);
                    checkableChipView.setText(record.getServices().get(i).getName());
                    checkableChipView.setOutlineCornerRadius(10f);
                    checkableChipView.setBackgroundColor(getResources().getColor(R.color.white));
                    checkableChipView.setOutlineColor(getResources().getColor(R.color.black));
                    checkableChipView.setCheckedColor(getResources().getColor(R.color.accent));
                    checkableChipView.setEnabled(false);
                    servicePriceItem.addView(checkableChipView, 0, layoutParams);
                }
            } else {
                servicePriceLabel.setText("");
            }

        }
    }


    private void initView() {
        goRoad = findViewById(R.id.goRoad);
        cancelRecordBtn = findViewById(R.id.cancelRecord);
        goRoad.setOnClickListener(this);
        cancelRecordBtn.setOnClickListener(this);
        packageBlock = findViewById(R.id.packageBlock);
        packageItems = findViewById(R.id.packageItems);
        servicePriceBlock = findViewById(R.id.servicePriceBlock);
        servicePriceItem = findViewById(R.id.servicePriceItem);
        carWashName = findViewById(R.id.carWashName);
        date = findViewById(R.id.date);
        time = findViewById(R.id.time);
        duration = findViewById(R.id.duration);
        price = findViewById(R.id.price);
        car = findViewById(R.id.car);
        servicePriceLabel = findViewById(R.id.servicePriceLabel);
    }

    private void getCar(String targetId) {
        API.getCarById(FastSave.getInstance().getString(ACCESS_TOKEN, ""), targetId)
                .enqueue(customCallback.getResponse(new CustomCallback.ResponseCallback<AllCarResponse>() {
                    @Override
                    public void onSuccessful(Call<AllCarResponse> call, Response<AllCarResponse> response) {
                        car.setText(response.body().getBrand().getName() + "  " + response.body().getModel().getName());
                    }

                    @Override
                    public void onEmpty(Call<AllCarResponse> call, Response<AllCarResponse> response) {

                    }
                }));
    }

    private void getDeviceLocation() {
        if (!record.getStatusProcess().equals("CANCELED")) {
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            try {
                Task locationResult = mFusedLocationClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            mLastKnownLocation = (Location) task.getResult();
                            if (mLastKnownLocation != null) {
                                goRoad.setText("Проложить маршрут");
                                goRoad.setEnabled(true);
                            }
                        }
                    }
                });

            } catch (SecurityException e) {
                Log.e("Exception: %s", e.getMessage());
            }
        } else {
            goRoad.setText("Заказ отменен");
            goRoad.setEnabled(false);
            cancelRecordBtn.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.goRoad:
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?saddr=" +
                                mLastKnownLocation.getLatitude()
                                + "," +
                                mLastKnownLocation.getLongitude()
                                + "&daddr=" + record.getBusiness().getLatitude() + "," + record.getBusiness().getLongitude()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                startActivity(intent);
                break;
            case R.id.cancelRecord:
                alertDialog = new LottieAlertDialog.Builder(this, DialogTypes.TYPE_QUESTION)
                        .setTitle("Отменить заказ")
                        .setDescription("Вы действительно хотите отменить заказ?\n(Эту операцию нельзя отменить)")
                        .setPositiveText("Да")
                        .setNegativeText("Нет")
                        .setPositiveButtonColor(getResources().getColor(R.color.md_red_A200))
                        .setPositiveListener(new ClickListener() {
                            @Override
                            public void onClick(@NotNull LottieAlertDialog lottieAlertDialog) {
                                cancelRecord();
                            }
                        })
                        .setNegativeListener(new ClickListener() {
                            @Override
                            public void onClick(@NotNull LottieAlertDialog lottieAlertDialog) {
                                alertDialog.dismiss();
                            }
                        })
                        .build();
                alertDialog.setCancelable(false);
                alertDialog.show();
                break;
        }
    }

    private void cancelRecord() {
        API.canceleRecord(FastSave.getInstance().getString(ACCESS_TOKEN, ""), record.getId())
                .enqueue(customCallback.getResponse(new CustomCallback.ResponseCallback<AllRecordResponse>() {
                    @Override
                    public void onSuccessful(Call<AllRecordResponse> call, Response<AllRecordResponse> response) {
                        startActivity(new Intent(SingleRecordActivity.this, RecordListActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
                        Toast.makeText(SingleRecordActivity.this, "Заказ отменен", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onEmpty(Call<AllRecordResponse> call, Response<AllRecordResponse> response) {

                    }
                }));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(SingleRecordActivity.this, RecordListActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
    }
}
