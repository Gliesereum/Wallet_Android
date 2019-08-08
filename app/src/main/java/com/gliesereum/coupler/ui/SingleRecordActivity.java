package com.gliesereum.coupler.ui;

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
import com.gliesereum.coupler.R;
import com.gliesereum.coupler.data.network.APIClient;
import com.gliesereum.coupler.data.network.APIInterface;
import com.gliesereum.coupler.data.network.CustomCallback;
import com.gliesereum.coupler.data.network.json.car.AllCarResponse;
import com.gliesereum.coupler.data.network.json.record.AllRecordResponse;
import com.gliesereum.coupler.data.network.json.record_new.ContentItem;
import com.gliesereum.coupler.data.network.json.record_new.WorkersItem;
import com.gliesereum.coupler.util.FastSave;
import com.gliesereum.coupler.util.Util;
import com.gohn.nativedialog.ButtonType;
import com.gohn.nativedialog.NDialog;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.labters.lottiealertdialoglibrary.LottieAlertDialog;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static com.gliesereum.coupler.util.Constants.ACCESS_TOKEN;
import static com.gliesereum.coupler.util.Constants.CANCELED;
import static com.gliesereum.coupler.util.Constants.COMPLETED;
import static com.gliesereum.coupler.util.Constants.IN_PROCESS;
import static com.gliesereum.coupler.util.Constants.OBJECT_ID;
import static com.gliesereum.coupler.util.Constants.RECORD;

//import com.appizona.yehiahd.fastsave.FastSave;

public class SingleRecordActivity extends AppCompatActivity implements View.OnClickListener {

    private APIInterface API;
    private CustomCallback customCallback;
    private Button goRoad;
    private String TAG = "TAG";
    private ContentItem record;
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
    private TextView textView26;
    private TextView commentTextView;
    private TextView master;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_record);
        initData();
        initView();
//        getDeviceLocation();
        if (getIntent().getStringExtra(OBJECT_ID) != null) {
            getSingleRecord(getIntent().getStringExtra(OBJECT_ID));
        } else {
            getDeviceLocation();
            if (record.getBusiness().getBusinessCategory().getBusinessType().equals("CAR")) {
                if (record.getTargetId() != null) {
                    getCar(record.getTargetId());
                } else {
                    car.setVisibility(View.GONE);
                    textView26.setVisibility(View.GONE);
                }
            } else {
                car.setVisibility(View.GONE);
                textView26.setVisibility(View.GONE);
            }

            fillActivity(record);
        }
    }

    private void getSingleRecord(String objectId) {
        API.getSingleRecord(FastSave.getInstance().getString(ACCESS_TOKEN, ""), objectId)
                .enqueue(customCallback.getResponseWithProgress(new CustomCallback.ResponseCallback<ContentItem>() {
                    @Override
                    public void onSuccessful(Call<ContentItem> call, Response<ContentItem> response) {
                        record = response.body();
                        getDeviceLocation();
                        getCar(record.getTargetId());
                        fillActivity(record);
                    }

                    @Override
                    public void onEmpty(Call<ContentItem> call, Response<ContentItem> response) {

                    }
                }));
    }

    private void initData() {
        FastSave.init(getApplicationContext());
        API = APIClient.getClient().create(APIInterface.class);
        customCallback = new CustomCallback(this, this);
        record = FastSave.getInstance().getObject(RECORD, ContentItem.class);
    }

    private void fillActivity(ContentItem record) {
        getWorker();
        date.setText(Util.getStringDate(record.getBegin()));
        time.setText(Util.getStringTime(record.getBegin()));
        duration.setText(String.valueOf((record.getFinish() - record.getBegin()) / 60000) + " мин");
        price.setText(String.valueOf(record.getPrice()) + " грн");
        carWashName.setText(record.getBusiness().getName());
        if (record.getStatusProcess().equals(COMPLETED) || record.getStatusProcess().equals(IN_PROCESS)) {
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

    private void getWorker() {
        API.getWorkerById(FastSave.getInstance().getString(ACCESS_TOKEN, ""), record.getWorkerId())
                .enqueue(customCallback.getResponse(new CustomCallback.ResponseCallback<WorkersItem>() {
                    @Override
                    public void onSuccessful(Call<WorkersItem> call, Response<WorkersItem> response) {
                        master.setText(response.body().getUser().getFirstName());
                    }

                    @Override
                    public void onEmpty(Call<WorkersItem> call, Response<WorkersItem> response) {

                    }
                }));

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
        textView26 = findViewById(R.id.textView26);
        master = findViewById(R.id.master);
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
        if (!record.getStatusProcess().equals(CANCELED)) {
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
                NDialog cancelDialog = new NDialog(SingleRecordActivity.this, ButtonType.NO_BUTTON);
                cancelDialog.setCustomView(R.layout.cancele_dialog);
                List<View> childViews = cancelDialog.getCustomViewChildren();
                for (View childView : childViews) {
                    switch (childView.getId()) {
                        case R.id.commentTextView:
                            commentTextView = childView.findViewById(R.id.commentTextView);
                            break;
                        case R.id.deleteRecord:
                            Button deleteRecord = childView.findViewById(R.id.deleteRecord);
                            deleteRecord.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (!commentTextView.getText().toString().equals("")) {
                                        cancelRecord();
                                        cancelDialog.dismiss();
                                    } else {
                                        Toast.makeText(SingleRecordActivity.this, "Введите причину отмены заказа", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            break;
                        case R.id.cancelBtn:
                            Button cancelBtn = childView.findViewById(R.id.cancelBtn);
                            cancelBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    cancelDialog.dismiss();
                                }
                            });
                            break;

                    }
                }
                cancelDialog.show();
                break;
        }
    }

    private void cancelRecord() {
        API.canceleRecord(FastSave.getInstance().getString(ACCESS_TOKEN, ""), record.getId(), commentTextView.getText().toString())
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
