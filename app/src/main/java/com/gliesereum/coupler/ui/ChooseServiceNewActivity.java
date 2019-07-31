package com.gliesereum.coupler.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gliesereum.coupler.R;
import com.gliesereum.coupler.adapter.ChooseServiceAdapter;
import com.gliesereum.coupler.data.network.APIClient;
import com.gliesereum.coupler.data.network.APIInterface;
import com.gliesereum.coupler.data.network.CustomCallback;
import com.gliesereum.coupler.data.network.json.businesscategory.BusinesCategoryResponse;
import com.gliesereum.coupler.util.FastSave;
import com.gliesereum.coupler.util.Util;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static com.gliesereum.coupler.util.Constants.BEAUTY_SALONS;
import static com.gliesereum.coupler.util.Constants.CAR_SERVICE;
import static com.gliesereum.coupler.util.Constants.CAR_WASH;
import static com.gliesereum.coupler.util.Constants.SERVICE_BEAUTY_SALONS_ID;
import static com.gliesereum.coupler.util.Constants.SERVICE_CAR_SERVICE_ID;
import static com.gliesereum.coupler.util.Constants.SERVICE_CAR_WASH_ID;
import static com.gliesereum.coupler.util.Constants.SERVICE_TIRE_FITTING_ID;
import static com.gliesereum.coupler.util.Constants.TIRE_FITTING;

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
                        for (int i = 0; i < response.body().size(); i++) {
                            switch (response.body().get(i).getCode()) {
                                case BEAUTY_SALONS:
                                    FastSave.getInstance().saveString(SERVICE_BEAUTY_SALONS_ID, response.body().get(i).getId());
                                    break;
                                case CAR_WASH:
                                    FastSave.getInstance().saveString(SERVICE_CAR_WASH_ID, response.body().get(i).getId());
                                    break;
                                case TIRE_FITTING:
                                    FastSave.getInstance().saveString(SERVICE_TIRE_FITTING_ID, response.body().get(i).getId());
                                    break;
                                case CAR_SERVICE:
                                    FastSave.getInstance().saveString(SERVICE_CAR_SERVICE_ID, response.body().get(i).getId());
                                    break;
                            }
                        }
                    }

                    @Override
                    public void onEmpty(Call<List<BusinesCategoryResponse>> call, Response<List<BusinesCategoryResponse>> response) {

                    }
                }));
    }


}
