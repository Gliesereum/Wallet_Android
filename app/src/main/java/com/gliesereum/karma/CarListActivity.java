package com.gliesereum.karma;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.appizona.yehiahd.fastsave.FastSave;
import com.gliesereum.karma.data.network.APIClient;
import com.gliesereum.karma.data.network.APIInterface;
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

public class CarListActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private TweetAdapter tweetAdapter;
    private List<AllCarResponse> carsList;
    private MaterialButton addCarBtn;
    private APIInterface apiInterface;
    private ErrorHandler errorHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_list);
        FastSave.init(getApplicationContext());
        Log.d("TOKEN", FastSave.getInstance().getString(ACCESS_TOKEN, ""));
        initView();
        getAllCars();
    }

    private void getAllCars() {
        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<List<AllCarResponse>> call = apiInterface.getAllCars("Bearer " + FastSave.getInstance().getString(ACCESS_TOKEN, ""));
        call.enqueue(new Callback<List<AllCarResponse>>() {
            @Override
            public void onResponse(Call<List<AllCarResponse>> call, Response<List<AllCarResponse>> response) {
                if (response.code() == 200) {
                    carsList = response.body();
                    if (carsList != null && carsList.size() > 0) {
                        tweetAdapter.setItems(carsList);
                    } else {
                        Toast.makeText(CarListActivity.this, "Please add car", Toast.LENGTH_SHORT).show();
                    }
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
            public void onFailure(Call<List<AllCarResponse>> call, Throwable t) {
                errorHandler.showCustomError(t.getMessage());
            }
        });
    }

    private void initView() {
        errorHandler = new ErrorHandler(this, this);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        tweetAdapter = new TweetAdapter();
        recyclerView.setAdapter(tweetAdapter);

        new Util(this, toolbar).addNavigation();
//modify an item of the drawer
//notify the drawer about the updated element. it will take care about everything else
        //to update only the name, badge, icon you can also use one of the quick methods
        addCarBtn = (MaterialButton) findViewById(R.id.addCarBtn);
        addCarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CarListActivity.this, AddCarActivity.class));
            }
        });
    }
}
