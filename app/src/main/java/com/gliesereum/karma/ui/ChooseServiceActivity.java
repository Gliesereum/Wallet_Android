package com.gliesereum.karma.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.gliesereum.karma.R;
import com.gliesereum.karma.data.network.APIClient;
import com.gliesereum.karma.data.network.APIInterface;
import com.gliesereum.karma.data.network.CustomCallback;
import com.gliesereum.karma.data.network.json.businesscategory.BusinesCategoryResponse;
import com.gliesereum.karma.util.FastSave;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static com.gliesereum.karma.util.Constants.BUSINESS_CATEGORY_ID;
import static com.gliesereum.karma.util.Constants.BUSINESS_CATEGORY_NAME;
import static com.gliesereum.karma.util.Constants.BUSINESS_TYPE;

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
                                    FastSave.getInstance().saveString(BUSINESS_TYPE, response.body().get(i).getBusinessType());
                                    carWashImage.setTag(R.string.tagBusinessCategoryId, response.body().get(i).getId());
                                    carWashImage.setTag(R.string.tagBusinessCategoryName, response.body().get(i).getName());
                                    break;
                                case "TIRE_FITTING":
                                    FastSave.getInstance().saveString(BUSINESS_TYPE, response.body().get(i).getBusinessType());
                                    tireFittingImage.setTag(R.string.tagBusinessCategoryId, response.body().get(i).getId());
                                    tireFittingImage.setTag(R.string.tagBusinessCategoryName, response.body().get(i).getName());
                                    break;
                                case "CAR_SERVICE":
                                    FastSave.getInstance().saveString(BUSINESS_TYPE, response.body().get(i).getBusinessType());
                                    carServiceImage.setTag(R.string.tagBusinessCategoryId, response.body().get(i).getId());
                                    carServiceImage.setTag(R.string.tagBusinessCategoryName, response.body().get(i).getName());
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
        FastSave.getInstance().saveString(BUSINESS_CATEGORY_ID, (String) v.getTag(R.string.tagBusinessCategoryId));
        FastSave.getInstance().saveString(BUSINESS_CATEGORY_NAME, " (" + (String) v.getTag(R.string.tagBusinessCategoryName) + ")");
        startActivity(new Intent(ChooseServiceActivity.this, MapsActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
        finish();
    }
}
