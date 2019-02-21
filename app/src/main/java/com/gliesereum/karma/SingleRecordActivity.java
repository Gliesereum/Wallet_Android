package com.gliesereum.karma;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appizona.yehiahd.fastsave.FastSave;
import com.github.okdroid.checkablechipview.CheckableChipView;
import com.gliesereum.karma.data.network.APIClient;
import com.gliesereum.karma.data.network.APIInterface;
import com.gliesereum.karma.data.network.json.car.AllCarResponse;
import com.gliesereum.karma.data.network.json.record.AllRecordResponse;
import com.gliesereum.karma.util.ErrorHandler;
import com.gliesereum.karma.util.Util;

import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.gliesereum.karma.util.Constants.ACCESS_TOKEN;

public class SingleRecordActivity extends AppCompatActivity implements LocationListener {

    private APIInterface apiInterface;
    private ErrorHandler errorHandler;
    private Button goRoad;
    private LocationManager locationManager;
    private double myLatitude = 0.0;
    private double myLongitude = 0.0;
    private ProgressDialog progressDialog;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_record);
        FastSave.init(getApplicationContext());
        record = FastSave.getInstance().getObject("RECORD", AllRecordResponse.class);
        Log.d(TAG, "onCreate: ");
        initView();
        fillActivity(record);
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }

    }

    private void fillActivity(AllRecordResponse record) {
        getCar(record.getTargetId());
        date.setText(Util.getStringDate(record.getBegin()));
        time.setText(Util.getStringTime(record.getBegin()));
        duration.setText(String.valueOf((record.getFinish() - record.getBegin()) / 60000) + " мин");
        price.setText(String.valueOf(record.getPrice()) + " грн");
        carWashName.setText(record.getBusiness().getName());

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
        }

        if (record.getServices() != null) {
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
        }
    }


    private void initView() {
        errorHandler = new ErrorHandler(this, this);
        goRoad = findViewById(R.id.goRoad);
        goRoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?saddr=" +
                                myLatitude
                                + "," +
                                myLongitude
                                + "&daddr=" + record.getBusiness().getLatitude() + "," + record.getBusiness().getLongitude()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                startActivity(intent);
            }
        });

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
    }

    private void getCar(String targetId) {
        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<AllCarResponse> call = apiInterface.getCarById(FastSave.getInstance().getString(ACCESS_TOKEN, ""), targetId);
        call.enqueue(new Callback<AllCarResponse>() {
            @Override
            public void onResponse(Call<AllCarResponse> call, Response<AllCarResponse> response) {
                AllCarResponse carById = response.body();
                if (response.code() == 200) {
                    car.setText(response.body().getBrand().getName() + "  " + response.body().getModel().getName());
                } else {
                    if (response.code() == 204) {
//                        Toast.makeText(CarListActivity.this, "", Toast.LENGTH_SHORT).show();
                    } else {
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            errorHandler.showError(jObjError.getInt("code"));
                        } catch (Exception e) {
                            errorHandler.showCustomError(e.getMessage());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<AllCarResponse> call, Throwable t) {
                errorHandler.showCustomError(t.getMessage());
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged: ");
        myLatitude = location.getLatitude();
        myLongitude = location.getLongitude();
        goRoad.setText("проложить маршрут");
        goRoad.setEnabled(true);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d(TAG, "onStatusChanged: ");
        Toast.makeText(this, "onStatusChanged", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d(TAG, "onProviderEnabled: ");
        Toast.makeText(this, "onProviderEnabled", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d(TAG, "onProviderDisabled: ");
        Toast.makeText(this, "onProviderDisabled", Toast.LENGTH_SHORT).show();
    }
}
