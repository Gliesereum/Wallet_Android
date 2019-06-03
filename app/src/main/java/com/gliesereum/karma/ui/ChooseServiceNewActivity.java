package com.gliesereum.karma.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gliesereum.karma.R;
import com.gliesereum.karma.adapter.ChooseServiceAdapter;
import com.gliesereum.karma.data.network.APIClient;
import com.gliesereum.karma.data.network.APIInterface;
import com.gliesereum.karma.data.network.CustomCallback;
import com.gliesereum.karma.data.network.json.businesscategory.BusinesCategoryResponse;
import com.gliesereum.karma.util.FastSave;
import com.gliesereum.karma.util.Util;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class ChooseServiceNewActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private APIInterface API;
    private CustomCallback customCallback;
    private ChooseServiceAdapter chooseServiceAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_service_new);
        initView();
        getBusinessCategory();
    }

    private void initView() {
        FastSave.init(getApplicationContext());
        API = APIClient.getClient().create(APIInterface.class);
        customCallback = new CustomCallback(this, this);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        chooseServiceAdapter = new ChooseServiceAdapter(ChooseServiceNewActivity.this);
        recyclerView.setAdapter(chooseServiceAdapter);
        new Util(this, toolbar, 8).addNavigation();
    }

    private void getBusinessCategory() {
        API.getBusinessCategory()
                .enqueue(customCallback.getResponseWithProgress(new CustomCallback.ResponseCallback<List<BusinesCategoryResponse>>() {
                    @Override
                    public void onSuccessful(Call<List<BusinesCategoryResponse>> call, Response<List<BusinesCategoryResponse>> response) {
                        chooseServiceAdapter.setItems(response.body());
//                        for (int i = 0; i < response.body().size(); i++) {
//                            switch (response.body().get(i).getCode()) {
//                                case "CAR_WASH":
//                                    carWashImage.setTag(R.string.tagBusinessCategoryId, response.body().get(i).getId());
//                                    carWashImage.setTag(R.string.tagBusinessCategoryName, response.body().get(i).getName());
//                                    carWashImage.setTag(R.string.tagBusinessCode, response.body().get(i).getCode());
//                                    carWashImage.setTag(R.string.tagBusinessType, response.body().get(i).getBusinessType());
//                                    break;
//                                case "TIRE_FITTING":
//                                    tireFittingImage.setTag(R.string.tagBusinessCategoryId, response.body().get(i).getId());
//                                    tireFittingImage.setTag(R.string.tagBusinessCategoryName, response.body().get(i).getName());
//                                    tireFittingImage.setTag(R.string.tagBusinessCode, response.body().get(i).getCode());
//                                    tireFittingImage.setTag(R.string.tagBusinessType, response.body().get(i).getBusinessType());
//                                    break;
//                                case "CAR_SERVICE":
//                                    carServiceImage.setTag(R.string.tagBusinessCategoryId, response.body().get(i).getId());
//                                    carServiceImage.setTag(R.string.tagBusinessCategoryName, response.body().get(i).getName());
//                                    carServiceImage.setTag(R.string.tagBusinessCode, response.body().get(i).getCode());
//                                    carServiceImage.setTag(R.string.tagBusinessType, response.body().get(i).getBusinessType());
//                                    break;
//                            }
//                        }
                    }

                    @Override
                    public void onEmpty(Call<List<BusinesCategoryResponse>> call, Response<List<BusinesCategoryResponse>> response) {

                    }
                }));
    }


}
