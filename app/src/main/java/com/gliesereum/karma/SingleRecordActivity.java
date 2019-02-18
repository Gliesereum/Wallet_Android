package com.gliesereum.karma;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.appizona.yehiahd.fastsave.FastSave;
import com.gliesereum.karma.data.network.APIClient;
import com.gliesereum.karma.data.network.APIInterface;
import com.gliesereum.karma.data.network.json.car.AllCarResponse;
import com.gliesereum.karma.data.network.json.carwash.AllCarWashResponse;
import com.gliesereum.karma.data.network.json.carwash.RecordsItem;
import com.gliesereum.karma.util.ErrorHandler;

import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.gliesereum.karma.util.Constants.ACCESS_TOKEN;

public class SingleRecordActivity extends AppCompatActivity {

    private String recordId;
    private String targetId;
    private String packageId;
    private String businessId;
    private APIInterface apiInterface;
    private ErrorHandler errorHandler;
    private TextView priceLabel;
    private TextView priceTextView;
    private TextView durationLabel;
    private TextView durationTextView;
    private TextView addresLabel;
    private TextView addresTextView;
    private TextView carLabel;
    private TextView carTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_record);
        FastSave.init(getApplicationContext());
        recordId = getIntent().getStringExtra("recordId");
        initView();
        getSingleRecord(recordId);

    }

    private void getSingleRecord(String recordId) {
        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<RecordsItem> call = apiInterface.getSingleRecord(FastSave.getInstance().getString(ACCESS_TOKEN, ""), recordId);
        call.enqueue(new Callback<RecordsItem>() {
            @Override
            public void onResponse(Call<RecordsItem> call, Response<RecordsItem> response) {
                if (response.code() == 200) {
                    targetId = response.body().getTargetId();
                    packageId = response.body().getPackageId();
                    businessId = response.body().getBusinessId();
                    priceTextView.setText(String.valueOf(response.body().getPrice()));
                    durationTextView.setText(String.valueOf(response.body().getFinish() - response.body().getBegin()));
                    Log.d("TAG", "onResponse: " + targetId);
                    Log.d("TAG", "onResponse: " + packageId);
                    Log.d("TAG", "onResponse: " + businessId);

                    if (targetId != null) getCar(targetId);
                    if (businessId != null) getCarwash(businessId);
                    if (packageId != null) getPackage(packageId);

                } else {
                    if (response.code() == 204) {

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
            public void onFailure(Call<RecordsItem> call, Throwable t) {
                errorHandler.showCustomError(t.getMessage());
            }
        });
    }

    private void getCarwash(String businessId) {
        Call<AllCarWashResponse> call = apiInterface.getCarWash(businessId);
        call.enqueue(new Callback<AllCarWashResponse>() {
            @Override
            public void onResponse(Call<AllCarWashResponse> call, Response<AllCarWashResponse> response) {
                if (response.code() == 200) {
                    addresTextView.setText(response.body().getAddress());
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
            public void onFailure(Call<AllCarWashResponse> call, Throwable t) {
                errorHandler.showCustomError(t.getMessage());
            }
        });
    }

    private void getCar(String targetId) {
        Call<AllCarResponse> call = apiInterface.getCarById(FastSave.getInstance().getString(ACCESS_TOKEN, ""), targetId);
        call.enqueue(new Callback<AllCarResponse>() {
            @Override
            public void onResponse(Call<AllCarResponse> call, Response<AllCarResponse> response) {
                AllCarResponse carById = response.body();
                if (response.code() == 200) {
                    carTextView.setText(response.body().getBrand().getName() + "  " + response.body().getModel().getName());
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

    private void getPackage(String packageId) {
    }

    private void initView() {
        errorHandler = new ErrorHandler(this, this);
        priceLabel = findViewById(R.id.priceLabel);
        priceTextView = findViewById(R.id.priceTextView);
        durationLabel = findViewById(R.id.durationLabel);
        durationTextView = findViewById(R.id.durationTextView);
        addresLabel = findViewById(R.id.addresLabel);
        addresTextView = findViewById(R.id.addresTextView);
        carLabel = findViewById(R.id.carLabel);
        carTextView = findViewById(R.id.carTextView);
    }
}
