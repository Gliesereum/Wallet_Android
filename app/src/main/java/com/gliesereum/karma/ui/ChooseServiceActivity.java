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
                                    carWashImage.setTag(response.body().get(i).getId());
                                    break;
                                case "TIRE_FITTING":
                                    tireFittingImage.setTag(response.body().get(i).getId());
                                    break;
                                case "CAR_SERVICE":
                                    carServiceImage.setTag(response.body().get(i).getId());
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
        FastSave.getInstance().saveString(BUSINESS_CATEGORY_ID, (String) v.getTag());
        startActivity(new Intent(ChooseServiceActivity.this, MapsActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
        finish();
    }
}
