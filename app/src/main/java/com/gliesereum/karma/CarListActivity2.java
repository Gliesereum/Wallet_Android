package com.gliesereum.karma;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.appizona.yehiahd.fastsave.FastSave;
import com.github.vivchar.viewpagerindicator.ViewPagerIndicator;
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
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.gliesereum.karma.util.Constants.ACCESS_TOKEN;
import static com.gliesereum.karma.util.Constants.CAR_BRAND;
import static com.gliesereum.karma.util.Constants.CAR_FILTER_LIST;
import static com.gliesereum.karma.util.Constants.CAR_ID;
import static com.gliesereum.karma.util.Constants.CAR_MODEL;
import static com.gliesereum.karma.util.Constants.CAR_SERVICE_CLASS;

public class CarListActivity2 extends AppCompatActivity {
    private ViewPagerIndicator viewPagerIndicator;
    private int item = 0;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private CarListAdapter carListAdapter;
    private List<AllCarResponse> carsList;
    private MaterialButton addCarBtn;
    private APIInterface apiInterface;
    private ErrorHandler errorHandler;
    private TextView splashTextView;
    private WrapContentHeightViewPager viewPager;
    private MaterialButton chooseCarBtn;
    private MaterialButton addFirstCarBtn;
    private int selectPosition = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_list2);
        FastSave.init(getApplicationContext());
        initView();
        viewPagerIndicator = findViewById(R.id.view_pager_indicator);

        errorHandler = new ErrorHandler(this, this);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Список авто");
        setSupportActionBar(toolbar);
        new Util(this, toolbar, 2).addNavigation();

        getAllCars();

        viewPager = findViewById(R.id.viewPager);
        viewPager.setPageMargin(20);
//        viewPager.setAdapter(new ViewPagerAdapter(this, listDate));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                selectPosition = position;
                if (FastSave.getInstance().getString(CAR_ID, "").equals(carsList.get(selectPosition).getId())) {
                    chooseCarBtn.setTextColor(getResources().getColor(R.color.black));
                    chooseCarBtn.setBackgroundTintMode(PorterDuff.Mode.SRC_OVER);
                    chooseCarBtn.setText("Еду на " + carsList.get(selectPosition).getBrand().getName());
                } else {
                    chooseCarBtn.setTextColor(getResources().getColor(R.color.black));
                    chooseCarBtn.setBackgroundTintMode(PorterDuff.Mode.ADD);
                    chooseCarBtn.setText("Пересесть на " + carsList.get(selectPosition).getBrand().getName());
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });


    }

    private void getAllCars() {
        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<List<AllCarResponse>> call = apiInterface.getAllCars(FastSave.getInstance().getString(ACCESS_TOKEN, ""));
        call.enqueue(new Callback<List<AllCarResponse>>() {
            @Override
            public void onResponse(Call<List<AllCarResponse>> call, Response<List<AllCarResponse>> response) {
                if (response.code() == 200) {
                    carsList = response.body();
                    viewPager.setAdapter(new ViewPagerAdapter(CarListActivity2.this, carsList));
                    viewPagerIndicator.setupWithViewPager(viewPager);
                    if (carsList != null && carsList.size() > 0) {
                        if (carsList.size() == 1) {
                            FastSave.getInstance().saveString(CAR_ID, carsList.get(0).getId());
                            FastSave.getInstance().saveString(CAR_BRAND, carsList.get(0).getBrand().getName());
                            FastSave.getInstance().saveString(CAR_MODEL, carsList.get(0).getModel().getName());
                            FastSave.getInstance().saveObject(CAR_SERVICE_CLASS, carsList.get(0).getServices());
                            FastSave.getInstance().saveObjectsList(CAR_FILTER_LIST, carsList.get(0).getAttributes());

                            chooseCarBtn.setTextColor(getResources().getColor(R.color.black));
                            chooseCarBtn.setBackgroundTintMode(PorterDuff.Mode.SRC_OVER);
                            chooseCarBtn.setText("Еду на " + carsList.get(selectPosition).getBrand().getName());
                        }
                        if (FastSave.getInstance().getString(CAR_ID, "").equals(carsList.get(0).getId())) {
                            chooseCarBtn.setTextColor(getResources().getColor(R.color.black));
                            chooseCarBtn.setBackgroundTintMode(PorterDuff.Mode.SRC_OVER);
                            chooseCarBtn.setText("Еду на " + carsList.get(selectPosition).getBrand().getName());
                        } else {
                            chooseCarBtn.setTextColor(getResources().getColor(R.color.black));
                            chooseCarBtn.setBackgroundTintMode(PorterDuff.Mode.ADD);
                            chooseCarBtn.setText("Пересесть на " + carsList.get(0).getBrand().getName());
                        }
                    }
                } else {
                    if (response.code() == 204) {
                        splashTextView.setVisibility(View.VISIBLE);
                        addFirstCarBtn.setVisibility(View.VISIBLE);
                        chooseCarBtn.setVisibility(View.GONE);
                        viewPagerIndicator.setVisibility(View.GONE);
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
        splashTextView = findViewById(R.id.splashTextView);
        chooseCarBtn = findViewById(R.id.orderButton);
        chooseCarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MaterialButton) v).setTextColor(getResources().getColor(R.color.black));
                FastSave.getInstance().saveString(CAR_ID, carsList.get(selectPosition).getId());
                FastSave.getInstance().saveString(CAR_BRAND, carsList.get(selectPosition).getBrand().getName());
                FastSave.getInstance().saveString(CAR_MODEL, carsList.get(selectPosition).getModel().getName());
                FastSave.getInstance().saveObject(CAR_SERVICE_CLASS, carsList.get(selectPosition).getServices());
                FastSave.getInstance().saveObjectsList(CAR_FILTER_LIST, carsList.get(selectPosition).getAttributes());

                if (FastSave.getInstance().getString(CAR_ID, "").equals(carsList.get(selectPosition).getId())) {
                    chooseCarBtn.setTextColor(getResources().getColor(R.color.black));
                    chooseCarBtn.setBackgroundTintMode(PorterDuff.Mode.SRC_OVER);
                    chooseCarBtn.setText("Еду на " + carsList.get(selectPosition).getBrand().getName());
                } else {
                    chooseCarBtn.setTextColor(getResources().getColor(R.color.black));
                    chooseCarBtn.setBackgroundTintMode(PorterDuff.Mode.ADD);
                    chooseCarBtn.setText("Пересесть на " + carsList.get(selectPosition).getBrand().getName());
                }
                Toast.makeText(CarListActivity2.this, "Вы выбрали " + carsList.get(selectPosition).getBrand().getName(), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(CarListActivity2.this, MapsActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }
        });
        addFirstCarBtn = findViewById(R.id.addFirstCarBtn);
        addFirstCarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CarListActivity2.this, AddCarActivity.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_car_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.add_car_menu:
                startActivity(new Intent(CarListActivity2.this, AddCarActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
