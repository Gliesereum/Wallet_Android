package com.gliesereum.karma.ui;

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
import com.gliesereum.karma.R;
import com.gliesereum.karma.WrapContentHeightViewPager;
import com.gliesereum.karma.adapter.ViewPagerAdapter;
import com.gliesereum.karma.data.network.APIClient;
import com.gliesereum.karma.data.network.APIInterface;
import com.gliesereum.karma.data.network.CustomCallback;
import com.gliesereum.karma.data.network.json.car.AllCarResponse;
import com.gliesereum.karma.util.Util;
import com.google.android.material.button.MaterialButton;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import retrofit2.Call;
import retrofit2.Response;

import static com.gliesereum.karma.util.Constants.ACCESS_TOKEN;
import static com.gliesereum.karma.util.Constants.CAR_BRAND;
import static com.gliesereum.karma.util.Constants.CAR_FILTER_LIST;
import static com.gliesereum.karma.util.Constants.CAR_ID;
import static com.gliesereum.karma.util.Constants.CAR_MODEL;
import static com.gliesereum.karma.util.Constants.CAR_SERVICE_CLASS;

public class CarListActivity extends AppCompatActivity implements View.OnClickListener {
    private ViewPagerIndicator viewPagerIndicator;
    private Toolbar toolbar;
    private List<AllCarResponse> carsList;
    private APIInterface API;
    private CustomCallback customCallback;
    private TextView splashTextView;
    private WrapContentHeightViewPager viewPager;
    private MaterialButton chooseCarBtn;
    private MaterialButton addFirstCarBtn;
    private int selectPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_list);

        initData();
        initView();
        getAllCars();
    }

    private void initData() {
        API = APIClient.getClient().create(APIInterface.class);
        customCallback = new CustomCallback(this, this);

    }

    private void initView() {
        viewPagerIndicator = findViewById(R.id.view_pager_indicator);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Список авто");
        setSupportActionBar(toolbar);
        new Util(this, toolbar, 2).addNavigation();
        splashTextView = findViewById(R.id.splashTextView);
        chooseCarBtn = findViewById(R.id.chooseCarBtn);
        addFirstCarBtn = findViewById(R.id.addFirstCarBtn);
        chooseCarBtn.setOnClickListener(this);
        addFirstCarBtn.setOnClickListener(this);
        viewPager = findViewById(R.id.viewPager);
        viewPager.setPageMargin(20);
        viewPager.addOnPageChangeListener(addOnPageChangeListener);
    }

    private void chooseCar(View v) {
        ((MaterialButton) v).setTextColor(getResources().getColor(R.color.black));
        setFavoriteCar(carsList.get(selectPosition).getId());
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
        Toast.makeText(CarListActivity.this, "Вы выбрали " + carsList.get(selectPosition).getBrand().getName(), Toast.LENGTH_SHORT).show();
        startActivity(new Intent(CarListActivity.this, MapsActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
    }

    private void getAllCars() {
        API.getAllCars(FastSave.getInstance().getString(ACCESS_TOKEN, ""))
                .enqueue(customCallback.getResponse(new CustomCallback.ResponseCallback<List<AllCarResponse>>() {
                    @Override
                    public void onSuccessful(Call<List<AllCarResponse>> call, Response<List<AllCarResponse>> response) {
                        carsList = response.body();
                        viewPager.setAdapter(new ViewPagerAdapter(CarListActivity.this, carsList, CarListActivity.this));
                        viewPagerIndicator.setupWithViewPager(viewPager);
                        if (carsList != null && carsList.size() > 0) {
                            if (carsList.size() == 1) {
                                FastSave.getInstance().saveString(CAR_ID, carsList.get(0).getId());
                                FastSave.getInstance().saveString(CAR_BRAND, carsList.get(0).getBrand().getName());
                                FastSave.getInstance().saveString(CAR_MODEL, carsList.get(0).getModel().getName());
                                FastSave.getInstance().saveObject(CAR_SERVICE_CLASS, carsList.get(0).getServices());
                                FastSave.getInstance().saveObjectsList(CAR_FILTER_LIST, carsList.get(0).getAttributes());
                                viewPagerIndicator.setVisibility(View.GONE);
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
                    }

                    @Override
                    public void onEmpty(Call<List<AllCarResponse>> call, Response<List<AllCarResponse>> response) {
                        splashTextView.setVisibility(View.VISIBLE);
                        addFirstCarBtn.setVisibility(View.VISIBLE);
                        chooseCarBtn.setVisibility(View.GONE);
                        viewPagerIndicator.setVisibility(View.GONE);
                        ActionMenuItemView menuItem = findViewById(R.id.add_car_menu);
                        menuItem.setVisibility(View.GONE);
                    }
                })
        );
    }

    private void setFavoriteCar(String carId) {
        API.setFavoriteCar(FastSave.getInstance().getString(ACCESS_TOKEN, ""), carId)
                .enqueue(customCallback.getResponse(new CustomCallback.ResponseCallback<AllCarResponse>() {
                    @Override
                    public void onSuccessful(Call<AllCarResponse> call, Response<AllCarResponse> response) {

                    }

                    @Override
                    public void onEmpty(Call<AllCarResponse> call, Response<AllCarResponse> response) {

                    }
                }));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_car_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_car_menu:
                startActivity(new Intent(CarListActivity.this, AddCarActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.chooseCarBtn:
                chooseCar(v);
                break;
            case R.id.addFirstCarBtn:
                startActivity(new Intent(CarListActivity.this, AddCarActivity.class));
                break;
        }

    }

    ViewPager.OnPageChangeListener addOnPageChangeListener = new ViewPager.OnPageChangeListener() {
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
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };
}
