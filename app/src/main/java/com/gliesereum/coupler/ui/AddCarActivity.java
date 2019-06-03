package com.gliesereum.coupler.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.gliesereum.coupler.R;
import com.gliesereum.coupler.data.network.APIClient;
import com.gliesereum.coupler.data.network.APIInterface;
import com.gliesereum.coupler.data.network.CustomCallback;
import com.gliesereum.coupler.data.network.json.car.AllCarResponse;
import com.gliesereum.coupler.data.network.json.car.BrandResponse;
import com.gliesereum.coupler.data.network.json.car.CarDeleteResponse;
import com.gliesereum.coupler.data.network.json.classservices.ClassServiceResponse;
import com.gliesereum.coupler.data.network.json.filter.FilterResponse;
import com.gliesereum.coupler.util.FastSave;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.ganfra.materialspinner.MaterialSpinner;
import retrofit2.Call;
import retrofit2.Response;

import static com.gliesereum.coupler.util.Constants.ACCESS_TOKEN;
import static com.gliesereum.coupler.util.Constants.BUSINESS_TYPE;

//import com.appizona.yehiahd.fastsave.FastSave;

public class AddCarActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private MaterialSpinner brandSpinner;
    private MaterialSpinner modelSpinner;
    private MaterialSpinner yearSpinner;
    private MaterialSpinner radiusSpinner;
    private MaterialSpinner interiorSpinner;
    private MaterialSpinner carBodySpinner;
    private MaterialSpinner colourSpinner;
    private boolean brandFlag;
    private boolean modelFlag;
    private boolean yearFlag;
    private boolean interiorFlag;
    private boolean carBodyFlag;
    private boolean colourFlag;
    private boolean radiusFlag;
    private TextInputEditText registrationNumberTextView;
    private TextInputEditText descriptionNumberTextView;
    private MaterialButton addCarBtn;
    private List<ClassServiceResponse> classServiceList;
    private APIInterface API;
    private CustomCallback customCallback;
    private HashMap<String, String> brandHashMap;
    private HashMap<String, String> modelHashMap;
    private HashMap<String, String> yearHashMap;
    private HashMap<String, String> interiorHashMap;
    private HashMap<String, String> radiusHashMap;
    private HashMap<String, String> carBodyHashMap;
    private HashMap<String, String> colorHashMap;
    private ArrayAdapter<String> spinnerAdapter;
    private ChipGroup chipGroup;
    private Map<String, String> mapClassServise;
    private List<Chip> selectedChip;
    private Map<String, FilterResponse> filterMap;
    private String TAG = "spinner";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);
        initData();
        initView();
        enableSpinner(brandSpinner);
        getAllClassService();
        getAllFilter();

    }

    private void initData() {
        API = APIClient.getClient().create(APIInterface.class);
        customCallback = new CustomCallback(this, this);
        spinnerAdapter = new ArrayAdapter<>(this, R.layout.car_hint_item_layout, new String[]{""});
        filterMap = new HashMap<>();
        mapClassServise = new HashMap<>();
        brandHashMap = new HashMap<>();
        modelHashMap = new HashMap<>();
        yearHashMap = new HashMap<>();
        interiorHashMap = new HashMap<>();
        radiusHashMap = new HashMap<>();
        carBodyHashMap = new HashMap<>();
        colorHashMap = new HashMap<>();
        selectedChip = new ArrayList<>();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Добавление авто");
        setSupportActionBar(toolbar);
        brandSpinner = findViewById(R.id.brandSpinner);
        modelSpinner = findViewById(R.id.modelSpinner);
        yearSpinner = findViewById(R.id.yearSpinner);
        interiorSpinner = findViewById(R.id.interiorSpinner);
        radiusSpinner = findViewById(R.id.radiusSpinner);
        carBodySpinner = findViewById(R.id.carBodySpinner);
        colourSpinner = findViewById(R.id.colourSpinner);
        brandSpinner.setOnItemSelectedListener(brandSpinnerItemSelectedListener);
        modelSpinner.setOnItemSelectedListener(modelSpinnerItemSelectedListener);
        yearSpinner.setOnItemSelectedListener(yearSpinnerItemSelectedListener);
        interiorSpinner.setOnItemSelectedListener(interiorSpinnerItemSelectedListener);
        carBodySpinner.setOnItemSelectedListener(carBodySpinnerItemSelectedListener);
        colourSpinner.setOnItemSelectedListener(colourSpinnerItemSelectedListener);
        radiusSpinner.setOnItemSelectedListener(radiusSpinnerItemSelectedListener);
        brandSpinner.setAdapter(spinnerAdapter);
        modelSpinner.setAdapter(spinnerAdapter);
        yearSpinner.setAdapter(spinnerAdapter);
        interiorSpinner.setAdapter(spinnerAdapter);
        carBodySpinner.setAdapter(spinnerAdapter);
        colourSpinner.setAdapter(spinnerAdapter);
        radiusSpinner.setAdapter(spinnerAdapter);
        registrationNumberTextView = findViewById(R.id.registrationNumberTextView);
        descriptionNumberTextView = findViewById(R.id.descriptionNumberTextView);
        addCarBtn = findViewById(R.id.addCarBtn);
        addCarBtn.setOnClickListener(this);
        chipGroup = findViewById(R.id.chipGroup);
    }

    private void getAllClassService() {
        API.getAllClassService(FastSave.getInstance().getString(ACCESS_TOKEN, ""))
                .enqueue(customCallback.getResponse(new CustomCallback.ResponseCallback<List<ClassServiceResponse>>() {
                    @Override
                    public void onSuccessful(Call<List<ClassServiceResponse>> call, Response<List<ClassServiceResponse>> response) {
                        classServiceList = response.body();
                        for (int i = 0; i < classServiceList.size(); i++) {
                            mapClassServise.put(classServiceList.get(i).getName(), classServiceList.get(i).getId());
                            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            View rowView = inflater.inflate(R.layout.chip_class_service, null);
                            ((Chip) rowView).setText(classServiceList.get(i).getName());
                            rowView.setOnClickListener(v -> checkFillFields());
                            chipGroup.addView(rowView);
                        }
                    }

                    @Override
                    public void onEmpty(Call<List<ClassServiceResponse>> call, Response<List<ClassServiceResponse>> response) {

                    }
                }));
    }

    private void addCar() {
        AllCarResponse carInfo = new AllCarResponse(
                brandHashMap.get(brandSpinner.getSelectedItem().toString()),
                modelHashMap.get(modelSpinner.getSelectedItem().toString()),
                yearHashMap.get(yearSpinner.getSelectedItem().toString()),
                registrationNumberTextView.getText().toString(),
                descriptionNumberTextView.getText().toString()
        );
        API.addCar(FastSave.getInstance().getString(ACCESS_TOKEN, ""), carInfo)
                .enqueue(customCallback.getResponseWithProgress(new CustomCallback.ResponseCallback<AllCarResponse>() {
                    @Override
                    public void onSuccessful(Call<AllCarResponse> call, Response<AllCarResponse> response) {
                        for (int j = 0; j < chipGroup.getChildCount(); j++) {
                            Chip chip = (Chip) chipGroup.getChildAt(j);
                            if (chip.isChecked()) {
                                selectedChip.add(chip);
                            }
                        }

                        for (int i = 0; i < selectedChip.size(); i++) {
                            addClassService(response.body().getId(), mapClassServise.get(selectedChip.get(i).getText().toString()));
                        }

                        addCarFilter(response.body().getId(), interiorHashMap.get(interiorSpinner.getSelectedItem().toString()));
                        addCarFilter(response.body().getId(), carBodyHashMap.get(carBodySpinner.getSelectedItem().toString()));
                        addCarFilter(response.body().getId(), colorHashMap.get(colourSpinner.getSelectedItem().toString()));
                        addCarFilter(response.body().getId(), radiusHashMap.get(radiusSpinner.getSelectedItem().toString()));
                        startActivity(new Intent(AddCarActivity.this, CarListActivity.class));
                        finish();
                        Toast.makeText(AddCarActivity.this, "Машина успешно добавлена", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onEmpty(Call<AllCarResponse> call, Response<AllCarResponse> response) {

                    }
                }));
    }

    private void addClassService(String idCar, String idService) {
        API.addClassService(idCar, idService, FastSave.getInstance().getString(ACCESS_TOKEN, ""))
                .enqueue(customCallback.getResponse(new CustomCallback.ResponseCallback<CarDeleteResponse>() {
                    @Override
                    public void onSuccessful(Call<CarDeleteResponse> call, Response<CarDeleteResponse> response) {

                    }

                    @Override
                    public void onEmpty(Call<CarDeleteResponse> call, Response<CarDeleteResponse> response) {

                    }
                }));
    }

    private void addCarFilter(String idCar, String idAttribute) {
        API.addCarFilter(idCar, idAttribute, FastSave.getInstance().getString(ACCESS_TOKEN, ""))
                .enqueue(customCallback.getResponse(new CustomCallback.ResponseCallback<CarDeleteResponse>() {
                    @Override
                    public void onSuccessful(Call<CarDeleteResponse> call, Response<CarDeleteResponse> response) {

                    }

                    @Override
                    public void onEmpty(Call<CarDeleteResponse> call, Response<CarDeleteResponse> response) {

                    }
                }));
    }

    private void enableSpinner(MaterialSpinner materialSpinner) {
        switch (materialSpinner.getId()) {
            case R.id.brandSpinner:
                getBrands();
                brandSpinner.setEnabled(true);
                modelSpinner.setEnabled(false);
                yearSpinner.setEnabled(false);
                radiusSpinner.setEnabled(false);
                carBodySpinner.setEnabled(false);
                interiorSpinner.setEnabled(false);
                colourSpinner.setEnabled(false);
                break;
            case R.id.modelSpinner:
                getModel();
                brandSpinner.setEnabled(true);
                modelSpinner.setEnabled(true);
                yearSpinner.setEnabled(false);
                radiusSpinner.setEnabled(false);
                carBodySpinner.setEnabled(false);
                interiorSpinner.setEnabled(false);
                colourSpinner.setEnabled(false);
                break;
            case R.id.yearSpinner:
                getYears();
                brandSpinner.setEnabled(true);
                modelSpinner.setEnabled(true);
                yearSpinner.setEnabled(true);
                radiusSpinner.setEnabled(false);
                carBodySpinner.setEnabled(false);
                interiorSpinner.setEnabled(false);
                colourSpinner.setEnabled(false);
                break;
            case R.id.radiusSpinner:
                getRadius();
                brandSpinner.setEnabled(true);
                modelSpinner.setEnabled(true);
                yearSpinner.setEnabled(true);
                radiusSpinner.setEnabled(true);
                carBodySpinner.setEnabled(false);
                interiorSpinner.setEnabled(false);
                colourSpinner.setEnabled(false);
                break;
            case R.id.carBodySpinner:
                getCarBody();
                brandSpinner.setEnabled(true);
                modelSpinner.setEnabled(true);
                yearSpinner.setEnabled(true);
                radiusSpinner.setEnabled(true);
                carBodySpinner.setEnabled(true);
                interiorSpinner.setEnabled(false);
                colourSpinner.setEnabled(false);
                break;
            case R.id.interiorSpinner:
                getInterior();
                brandSpinner.setEnabled(true);
                modelSpinner.setEnabled(true);
                yearSpinner.setEnabled(true);
                radiusSpinner.setEnabled(true);
                carBodySpinner.setEnabled(true);
                interiorSpinner.setEnabled(true);
                colourSpinner.setEnabled(false);
                break;

            case R.id.colourSpinner:
                getColor();
                brandSpinner.setEnabled(true);
                modelSpinner.setEnabled(true);
                yearSpinner.setEnabled(true);
                radiusSpinner.setEnabled(true);
                carBodySpinner.setEnabled(true);
                interiorSpinner.setEnabled(true);
                colourSpinner.setEnabled(true);
                break;
        }
    }

//    private void disableSpinner(MaterialSpinner materialSpinner) {
//        switch (materialSpinner.getId()) {
//            case R.id.brandSpinner:
//                brandSpinner.setEnabled(true);
//                spinnerAdapter = new ArrayAdapter<>(this, R.layout.car_hint_item_layout, new String[]{""});
//                modelSpinner.setEnabled(false);
//                modelSpinner.setAdapter(spinnerAdapter);
//                yearSpinner.setEnabled(false);
//                yearSpinner.setAdapter(spinnerAdapter);
//                interiorSpinner.setEnabled(false);
//                interiorSpinner.setAdapter(spinnerAdapter);
//                carBodySpinner.setEnabled(false);
//                carBodySpinner.setAdapter(spinnerAdapter);
//                colourSpinner.setEnabled(false);
//                colourSpinner.setAdapter(spinnerAdapter);
//                break;
//            case R.id.modelSpinner:
//                brandSpinner.setEnabled(true);
//                modelSpinner.setEnabled(true);
//                spinnerAdapter = new ArrayAdapter<>(this, R.layout.car_hint_item_layout, new String[]{""});
//                yearSpinner.setEnabled(false);
//                yearSpinner.setAdapter(spinnerAdapter);
//                interiorSpinner.setEnabled(false);
//                interiorSpinner.setAdapter(spinnerAdapter);
//                carBodySpinner.setEnabled(false);
//                carBodySpinner.setAdapter(spinnerAdapter);
//                colourSpinner.setEnabled(false);
//                colourSpinner.setAdapter(spinnerAdapter);
//                break;
//            case R.id.yearSpinner:
//                brandSpinner.setEnabled(true);
//                modelSpinner.setEnabled(true);
//                yearSpinner.setEnabled(true);
//                spinnerAdapter = new ArrayAdapter<>(this, R.layout.car_hint_item_layout, new String[]{""});
//                interiorSpinner.setEnabled(false);
//                interiorSpinner.setAdapter(spinnerAdapter);
//                carBodySpinner.setEnabled(false);
//                carBodySpinner.setAdapter(spinnerAdapter);
//                colourSpinner.setEnabled(false);
//                colourSpinner.setAdapter(spinnerAdapter);
//                break;
//            case R.id.interiorSpinner:
//                brandSpinner.setEnabled(true);
//                modelSpinner.setEnabled(true);
//                yearSpinner.setEnabled(true);
//                interiorSpinner.setEnabled(true);
//                spinnerAdapter = new ArrayAdapter<>(this, R.layout.car_hint_item_layout, new String[]{""});
//                carBodySpinner.setEnabled(false);
//                carBodySpinner.setAdapter(spinnerAdapter);
//                colourSpinner.setEnabled(false);
//                colourSpinner.setAdapter(spinnerAdapter);
//                break;
//            case R.id.carBodySpinner:
//                brandSpinner.setEnabled(true);
//                modelSpinner.setEnabled(true);
//                yearSpinner.setEnabled(true);
//                interiorSpinner.setEnabled(true);
//                carBodySpinner.setEnabled(true);
//                spinnerAdapter = new ArrayAdapter<>(this, R.layout.car_hint_item_layout, new String[]{""});
//                colourSpinner.setEnabled(false);
//                colourSpinner.setAdapter(spinnerAdapter);
//                break;
//            case R.id.colourSpinner:
//                brandSpinner.setEnabled(true);
//                modelSpinner.setEnabled(true);
//                yearSpinner.setEnabled(true);
//                interiorSpinner.setEnabled(true);
//                carBodySpinner.setEnabled(true);
//                colourSpinner.setEnabled(true);
//                break;
//        }
//    }

    private void getBrands() {
        API.getBrands(FastSave.getInstance().getString(ACCESS_TOKEN, ""))
                .enqueue(customCallback.getResponse(new CustomCallback.ResponseCallback<List<BrandResponse>>() {
                    @Override
                    public void onSuccessful(Call<List<BrandResponse>> call, Response<List<BrandResponse>> response) {
                        ArrayList<String> brandITEMS = new ArrayList<>();
                        for (int i = 0; i < response.body().size(); i++) {
                            brandITEMS.add(response.body().get(i).getName());
                            brandHashMap.put(response.body().get(i).getName(), response.body().get(i).getId());
                        }
                        spinnerAdapter = new ArrayAdapter<>(AddCarActivity.this, R.layout.car_hint_item_layout, brandITEMS);
                        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        brandSpinner.setAdapter(spinnerAdapter);
                    }

                    @Override
                    public void onEmpty(Call<List<BrandResponse>> call, Response<List<BrandResponse>> response) {

                    }
                }));
    }

    private void getModel() {
        API.getModels(FastSave.getInstance().getString(ACCESS_TOKEN, ""), brandHashMap.get(brandSpinner.getSelectedItem()))
                .enqueue(customCallback.getResponse(new CustomCallback.ResponseCallback<List<BrandResponse>>() {
                    @Override
                    public void onSuccessful(Call<List<BrandResponse>> call, Response<List<BrandResponse>> response) {
                        ArrayList<String> modelITEMS = new ArrayList<>();
                        for (int i = 0; i < response.body().size(); i++) {
                            modelITEMS.add(response.body().get(i).getName());
                            modelHashMap.put(response.body().get(i).getName(), response.body().get(i).getId());

                        }
                        spinnerAdapter = new ArrayAdapter<String>(AddCarActivity.this, R.layout.car_hint_item_layout, modelITEMS);
                        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        modelSpinner.setAdapter(spinnerAdapter);
                    }

                    @Override
                    public void onEmpty(Call<List<BrandResponse>> call, Response<List<BrandResponse>> response) {

                    }
                }));
    }

    private void getYears() {
        API.getYears(FastSave.getInstance().getString(ACCESS_TOKEN, ""))
                .enqueue(customCallback.getResponse(new CustomCallback.ResponseCallback<List<BrandResponse>>() {
                    @Override
                    public void onSuccessful(Call<List<BrandResponse>> call, Response<List<BrandResponse>> response) {
                        ArrayList<String> yearITEMS = new ArrayList<>();
                        for (int i = 0; i < response.body().size(); i++) {
                            yearITEMS.add(response.body().get(i).getName());
                            yearHashMap.put(response.body().get(i).getName(), response.body().get(i).getId());

                        }
                        spinnerAdapter = new ArrayAdapter<>(AddCarActivity.this, R.layout.car_hint_item_layout, yearITEMS);
                        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        yearSpinner.setAdapter(spinnerAdapter);
                    }

                    @Override
                    public void onEmpty(Call<List<BrandResponse>> call, Response<List<BrandResponse>> response) {

                    }
                }));
    }

    public void getInterior() {
        ArrayList<String> interiorITEMS = new ArrayList<>();
        for (int i = 0; i < filterMap.get("CAR_INTERIOR").getAttributes().size(); i++) {
            interiorITEMS.add(filterMap.get("CAR_INTERIOR").getAttributes().get(i).getTitle());
            interiorHashMap.put(filterMap.get("CAR_INTERIOR").getAttributes().get(i).getTitle(), filterMap.get("CAR_INTERIOR").getAttributes().get(i).getId());
        }
        spinnerAdapter = new ArrayAdapter<String>(AddCarActivity.this, R.layout.car_hint_item_layout, interiorITEMS);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        interiorSpinner.setAdapter(spinnerAdapter);
    }

    public void getRadius() {
        ArrayList<String> radiusITEMS = new ArrayList<>();
        for (int i = 0; i < filterMap.get("CAR_WHEEL_RADIUS").getAttributes().size(); i++) {
            radiusITEMS.add(filterMap.get("CAR_WHEEL_RADIUS").getAttributes().get(i).getTitle());
            radiusHashMap.put(filterMap.get("CAR_WHEEL_RADIUS").getAttributes().get(i).getTitle(), filterMap.get("CAR_WHEEL_RADIUS").getAttributes().get(i).getId());
        }
        spinnerAdapter = new ArrayAdapter<String>(AddCarActivity.this, R.layout.car_hint_item_layout, radiusITEMS);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        radiusSpinner.setAdapter(spinnerAdapter);
    }

    public void getCarBody() {
        ArrayList<String> carBodyITEMS = new ArrayList<>();
        for (int i = 0; i < filterMap.get("CAR_BODY").getAttributes().size(); i++) {
            carBodyITEMS.add(filterMap.get("CAR_BODY").getAttributes().get(i).getTitle());
            carBodyHashMap.put(filterMap.get("CAR_BODY").getAttributes().get(i).getTitle(), filterMap.get("CAR_BODY").getAttributes().get(i).getId());
        }
        spinnerAdapter = new ArrayAdapter<String>(AddCarActivity.this, R.layout.car_hint_item_layout, carBodyITEMS);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        carBodySpinner.setAdapter(spinnerAdapter);
    }

//    public void getAllFilter() {
//        API.getFilters(FastSave.getInstance().getString(BUSINESS_CATEGORY_ID, ""))
//                .enqueue(customCallback.getResponse(new CustomCallback.ResponseCallback<List<FilterResponse>>() {
//                    @Override
//                    public void onSuccessful(Call<List<FilterResponse>> call, Response<List<FilterResponse>> response) {
//                        for (int i = 0; i < response.body().size(); i++) {
//                            filterMap.put(response.body().get(i).getValue(), response.body().get(i));
//                        }
//                    }
//
//                    @Override
//                    public void onEmpty(Call<List<FilterResponse>> call, Response<List<FilterResponse>> response) {
//
//                    }
//                }));
//    }

    public void getAllFilter() {
        API.getFiltersBusinessType(FastSave.getInstance().getString(BUSINESS_TYPE, ""))
                .enqueue(customCallback.getResponse(new CustomCallback.ResponseCallback<List<FilterResponse>>() {
                    @Override
                    public void onSuccessful(Call<List<FilterResponse>> call, Response<List<FilterResponse>> response) {
                        for (int i = 0; i < response.body().size(); i++) {
                            filterMap.put(response.body().get(i).getValue(), response.body().get(i));
                        }
                    }

                    @Override
                    public void onEmpty(Call<List<FilterResponse>> call, Response<List<FilterResponse>> response) {

                    }
                }));
    }

    public void getColor() {
        ArrayList<String> colorITEMS = new ArrayList<>();
        for (int i = 0; i < filterMap.get("CAR_COLOR").getAttributes().size(); i++) {
            colorITEMS.add(filterMap.get("CAR_COLOR").getAttributes().get(i).getTitle());
            colorHashMap.put(filterMap.get("CAR_COLOR").getAttributes().get(i).getTitle(), filterMap.get("CAR_COLOR").getAttributes().get(i).getId());
        }
        spinnerAdapter = new ArrayAdapter<String>(AddCarActivity.this, R.layout.car_hint_item_layout, colorITEMS);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        colourSpinner.setAdapter(spinnerAdapter);
    }

    private void checkFillFields() {
        boolean result = false;
        if (brandFlag && modelFlag && yearFlag && interiorFlag && carBodyFlag && colourFlag && radiusFlag) {
            for (int i = 0; i < chipGroup.getChildCount(); i++) {
                if (((Chip) chipGroup.getChildAt(i)).isChecked()) {
                    result = true;
                }
            }
        }

        if (result) {
            addCarBtn.setEnabled(true);
        } else {
            addCarBtn.setEnabled(false);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addCarBtn:
                addCar();
                break;
        }

    }

    AdapterView.OnItemSelectedListener brandSpinnerItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (id != 0) {
                Log.d(TAG, "onItemSelected: id != 0");
                enableSpinner(modelSpinner);
                brandFlag = true;
            } else {
                Log.d(TAG, "onItemSelected: id = 0");
                brandFlag = false;
            }
            checkFillFields();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            Log.d(TAG, "onNothingSelected: ");
            brandFlag = false;
            checkFillFields();
        }
    };
    AdapterView.OnItemSelectedListener modelSpinnerItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (id != 0) {
                enableSpinner(yearSpinner);
                modelFlag = true;
            } else {
                modelFlag = false;
            }
            checkFillFields();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            modelFlag = false;
            checkFillFields();
        }
    };
    AdapterView.OnItemSelectedListener yearSpinnerItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (id != 0) {
                enableSpinner(carBodySpinner);
                yearFlag = true;
            } else {
                yearFlag = false;
            }
            checkFillFields();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            yearFlag = false;
            checkFillFields();
        }
    };
    AdapterView.OnItemSelectedListener interiorSpinnerItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (id != 0) {
                enableSpinner(colourSpinner);
                interiorFlag = true;
            } else {
                interiorFlag = false;
            }
            checkFillFields();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            interiorFlag = false;
            checkFillFields();
        }
    };
    AdapterView.OnItemSelectedListener carBodySpinnerItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (id != 0) {
                enableSpinner(radiusSpinner);
                carBodyFlag = true;
            } else {
                carBodyFlag = false;
            }
            checkFillFields();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            carBodyFlag = false;
            checkFillFields();
        }
    };
    AdapterView.OnItemSelectedListener colourSpinnerItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (id != 0) {
                colourFlag = true;
            } else {
                colourFlag = false;
            }
            checkFillFields();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            colourFlag = false;
            checkFillFields();
        }
    };
    AdapterView.OnItemSelectedListener radiusSpinnerItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (id != 0) {
                enableSpinner(interiorSpinner);
                radiusFlag = true;
            } else {
                radiusFlag = false;
            }
            checkFillFields();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            radiusFlag = false;
            checkFillFields();
        }
    };
}
