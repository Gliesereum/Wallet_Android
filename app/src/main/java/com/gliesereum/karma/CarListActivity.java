package com.gliesereum.karma;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.appizona.yehiahd.fastsave.FastSave;
import com.gliesereum.karma.data.network.APIClient;
import com.gliesereum.karma.data.network.APIInterface;
import com.gliesereum.karma.data.network.adapter.CarListAdapter;
import com.gliesereum.karma.data.network.json.car.AllCarResponse;
import com.gliesereum.karma.util.ErrorHandler;
import com.gliesereum.karma.util.Util;
import com.google.android.material.button.MaterialButton;

import org.json.JSONObject;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.gliesereum.karma.util.Constants.ACCESS_TOKEN;
import static com.gliesereum.karma.util.Constants.CAR_BRAND;
import static com.gliesereum.karma.util.Constants.CAR_FILTER_LIST;
import static com.gliesereum.karma.util.Constants.CAR_ID;
import static com.gliesereum.karma.util.Constants.CAR_MODEL;
import static com.gliesereum.karma.util.Constants.CAR_SERVICE_CLASS;

public class CarListActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private CarListAdapter carListAdapter;
    private List<AllCarResponse> carsList;
    private MaterialButton addCarBtn;
    private APIInterface apiInterface;
    private ErrorHandler errorHandler;
    private TextView splashTextView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_list);
        FastSave.init(getApplicationContext());
        initView();
        getAllCars();
    }

    private void getAllCars() {
        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<List<AllCarResponse>> call = apiInterface.getAllCars(FastSave.getInstance().getString(ACCESS_TOKEN, ""));
        call.enqueue(new Callback<List<AllCarResponse>>() {
            @Override
            public void onResponse(Call<List<AllCarResponse>> call, Response<List<AllCarResponse>> response) {
                if (response.code() == 200) {
                    carsList = response.body();
                    if (carsList != null && carsList.size() > 0) {
                        if (carsList.size() == 1) {
                            FastSave.getInstance().saveString(CAR_ID, carsList.get(0).getId());
                            FastSave.getInstance().saveString(CAR_BRAND, carsList.get(0).getBrand().getName());
                            FastSave.getInstance().saveString(CAR_MODEL, carsList.get(0).getModel().getName());
                            FastSave.getInstance().saveObject(CAR_SERVICE_CLASS, carsList.get(0).getServices());
                            FastSave.getInstance().saveObjectsList(CAR_FILTER_LIST, carsList.get(0).getAttributes());
                        }
                        carListAdapter.setItems(carsList);
                    }
                } else {
                    if (response.code() == 204) {
                        splashTextView.setVisibility(View.VISIBLE);
//                        Toast.makeText(CarListActivity.this, "Добавте свой первый автомобиль", Toast.LENGTH_LONG).show();
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
            public void onFailure(Call<List<AllCarResponse>> call, Throwable t) {
                errorHandler.showCustomError(t.getMessage());
            }
        });
    }

    private void initView() {
        errorHandler = new ErrorHandler(this, this);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Список авто");
        setSupportActionBar(toolbar);
        splashTextView = findViewById(R.id.splashTextView);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        carListAdapter = new CarListAdapter();
        recyclerView.setAdapter(carListAdapter);
        recyclerView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });

        new Util(this, toolbar, 2).addNavigation();
        addCarBtn = (MaterialButton) findViewById(R.id.addCarBtn);
        addCarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CarListActivity.this, AddCarActivity.class));
            }
        });
    }
}
