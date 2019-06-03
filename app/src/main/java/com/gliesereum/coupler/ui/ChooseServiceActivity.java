package com.gliesereum.coupler.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.gliesereum.coupler.R;
import com.gliesereum.coupler.data.network.APIClient;
import com.gliesereum.coupler.data.network.APIInterface;
import com.gliesereum.coupler.data.network.CustomCallback;
import com.gliesereum.coupler.data.network.json.businesscategory.BusinesCategoryResponse;
import com.gliesereum.coupler.util.FastSave;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static com.gliesereum.coupler.util.Constants.BUSINESS_CATEGORY_ID;
import static com.gliesereum.coupler.util.Constants.BUSINESS_CATEGORY_NAME;
import static com.gliesereum.coupler.util.Constants.BUSINESS_CODE;
import static com.gliesereum.coupler.util.Constants.BUSINESS_TYPE;

public class ChooseServiceActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView carWashImage;
    private ImageView tireFittingImage;
    private ImageView carServiceImage;
    private APIInterface API;
    private CustomCallback customCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_service);
        initData();
        initView();
        getBusinessCategory();
    }

    private void getBusinessCategory() {
        API.getBusinessCategory()
                .enqueue(customCallback.getResponseWithProgress(new CustomCallback.ResponseCallback<List<BusinesCategoryResponse>>() {
                    @Override
                    public void onSuccessful(Call<List<BusinesCategoryResponse>> call, Response<List<BusinesCategoryResponse>> response) {
                        for (int i = 0; i < response.body().size(); i++) {
                            switch (response.body().get(i).getCode()) {
                                case "CAR_WASH":
                                    carWashImage.setTag(R.string.tagBusinessCategoryId, response.body().get(i).getId());
                                    carWashImage.setTag(R.string.tagBusinessCategoryName, response.body().get(i).getName());
                                    carWashImage.setTag(R.string.tagBusinessCode, response.body().get(i).getCode());
                                    carWashImage.setTag(R.string.tagBusinessType, response.body().get(i).getBusinessType());
                                    break;
                                case "TIRE_FITTING":
                                    tireFittingImage.setTag(R.string.tagBusinessCategoryId, response.body().get(i).getId());
                                    tireFittingImage.setTag(R.string.tagBusinessCategoryName, response.body().get(i).getName());
                                    tireFittingImage.setTag(R.string.tagBusinessCode, response.body().get(i).getCode());
                                    tireFittingImage.setTag(R.string.tagBusinessType, response.body().get(i).getBusinessType());
                                    break;
                                case "CAR_SERVICE":
                                    carServiceImage.setTag(R.string.tagBusinessCategoryId, response.body().get(i).getId());
                                    carServiceImage.setTag(R.string.tagBusinessCategoryName, response.body().get(i).getName());
                                    carServiceImage.setTag(R.string.tagBusinessCode, response.body().get(i).getCode());
                                    carServiceImage.setTag(R.string.tagBusinessType, response.body().get(i).getBusinessType());
                                    break;
                            }
                        }
                    }

                    @Override
                    public void onEmpty(Call<List<BusinesCategoryResponse>> call, Response<List<BusinesCategoryResponse>> response) {

                    }
                }));
    }

    private void initView() {
        carWashImage = findViewById(R.id.carWashImage);
        tireFittingImage = findViewById(R.id.tireFittingImage);
        carServiceImage = findViewById(R.id.carServiceImage);
        carWashImage.setOnClickListener(this);
        tireFittingImage.setOnClickListener(this);
        carServiceImage.setOnClickListener(this);
    }

    private void initData() {
        FastSave.init(getApplicationContext());
        API = APIClient.getClient().create(APIInterface.class);
        customCallback = new CustomCallback(this, this);
    }

    @Override
    public void onClick(View v) {
        FastSave.getInstance().saveString(BUSINESS_CODE, (String) v.getTag(R.string.tagBusinessCode));
        FastSave.getInstance().saveString(BUSINESS_TYPE, (String) v.getTag(R.string.tagBusinessType));
        FastSave.getInstance().saveString(BUSINESS_CATEGORY_ID, (String) v.getTag(R.string.tagBusinessCategoryId));
        FastSave.getInstance().saveString(BUSINESS_CATEGORY_NAME, " (" + (String) v.getTag(R.string.tagBusinessCategoryName) + ")");
        startActivity(new Intent(ChooseServiceActivity.this, MapsActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ChooseServiceActivity.this, MapsActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
        finish();
    }
}
