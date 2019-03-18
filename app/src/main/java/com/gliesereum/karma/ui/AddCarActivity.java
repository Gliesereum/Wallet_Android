package com.gliesereum.karma.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.appizona.yehiahd.fastsave.FastSave;
import com.gliesereum.karma.R;
import com.gliesereum.karma.data.network.APIClient;
import com.gliesereum.karma.data.network.APIInterface;
import com.gliesereum.karma.data.network.json.car.AllCarResponse;
import com.gliesereum.karma.data.network.json.car.BrandResponse;
import com.gliesereum.karma.data.network.json.car.CarDeleteResponse;
import com.gliesereum.karma.data.network.json.classservices.ClassServiceResponse;
import com.gliesereum.karma.data.network.json.filter.FilterResponse;
import com.gliesereum.karma.util.ErrorHandler;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import fr.ganfra.materialspinner.MaterialSpinner;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.gliesereum.karma.util.Constants.ACCESS_TOKEN;
import static com.gliesereum.karma.util.Constants.SERVICE_TYPE;

public class AddCarActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private MaterialSpinner brandSpinner;
    private MaterialSpinner modelSpinner;
    private MaterialSpinner yearSpinner;
    private MaterialSpinner interiorSpinner;
    private MaterialSpinner carBodySpinner;
    private MaterialSpinner colourSpinner;
    private boolean brandFlag;
    private boolean modelFlag;
    private boolean yearFlag;
    private boolean interiorFlag;
    private boolean carBodyFlag;
    private boolean colourFlag;
    private TextInputEditText registrationNumberTextView;
    private TextInputEditText descriptionNumberTextView;
    private MaterialButton addCarBtn;
    private List<ClassServiceResponse> classServiceList;
    private ProgressDialog progressDialog;
    private APIInterface apiInterface;
    private ErrorHandler errorHandler;
    private HashMap<String, String> brandHashMap;
    private HashMap<String, String> modelHashMap;
    private HashMap<String, String> yearHashMap;
    private HashMap<String, String> interiorHashMap;
    private HashMap<String, String> carBodyHashMap;
    private HashMap<String, String> colorHashMap;
    private ArrayAdapter<String> spinnerAdapter;
    private ChipGroup chipGroup;
    private Map<String, String> mapClassServise;
    private List<Chip> selectedChip;
    private Map<String, FilterResponse> filterMap;

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
        FastSave.init(getApplicationContext());
        apiInterface = APIClient.getClient().create(APIInterface.class);
        spinnerAdapter = new ArrayAdapter<>(this, R.layout.car_hint_item_layout, new String[]{""});
        filterMap = new HashMap<>();
        mapClassServise = new HashMap<>();
        brandHashMap = new HashMap<>();
        modelHashMap = new HashMap<>();
        yearHashMap = new HashMap<>();
        interiorHashMap = new HashMap<>();
        carBodyHashMap = new HashMap<>();
        colorHashMap = new HashMap<>();
        selectedChip = new ArrayList<>();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Добавление авто");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        errorHandler = new ErrorHandler(this, this);
        brandSpinner = findViewById(R.id.brandSpinner);
        modelSpinner = findViewById(R.id.modelSpinner);
        yearSpinner = findViewById(R.id.yearSpinner);
        interiorSpinner = findViewById(R.id.interiorSpinner);
        carBodySpinner = findViewById(R.id.carBodySpinner);
        colourSpinner = findViewById(R.id.colourSpinner);
        brandSpinner.setOnItemSelectedListener(brandSpinnerItemSelectedListener);
        modelSpinner.setOnItemSelectedListener(modelSpinnerItemSelectedListener);
        yearSpinner.setOnItemSelectedListener(yearSpinnerItemSelectedListener);
        interiorSpinner.setOnItemSelectedListener(interiorSpinnerItemSelectedListener);
        carBodySpinner.setOnItemSelectedListener(carBodySpinnerItemSelectedListener);
        colourSpinner.setOnItemSelectedListener(colourSpinnerItemSelectedListener);
        brandSpinner.setAdapter(spinnerAdapter);
        modelSpinner.setAdapter(spinnerAdapter);
        yearSpinner.setAdapter(spinnerAdapter);
        interiorSpinner.setAdapter(spinnerAdapter);
        carBodySpinner.setAdapter(spinnerAdapter);
        colourSpinner.setAdapter(spinnerAdapter);
        registrationNumberTextView = findViewById(R.id.registrationNumberTextView);
        descriptionNumberTextView = findViewById(R.id.descriptionNumberTextView);
        addCarBtn = findViewById(R.id.addCarBtn);
        addCarBtn.setOnClickListener(this);
        chipGroup = findViewById(R.id.chipGroup);
    }

    private void getAllClassService() {
        Call<List<ClassServiceResponse>> call = apiInterface.getAllClassService();
        call.enqueue(new Callback<List<ClassServiceResponse>>() {
            @Override
            public void onResponse(Call<List<ClassServiceResponse>> call, Response<List<ClassServiceResponse>> response) {
                if (response.code() == 200) {
                    classServiceList = response.body();
                    for (int i = 0; i < classServiceList.size(); i++) {
                        mapClassServise.put(classServiceList.get(i).getName(), classServiceList.get(i).getId());
                        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View rowView = inflater.inflate(R.layout.chip_class_service, null);
                        ((Chip) rowView).setText(classServiceList.get(i).getName());
                        rowView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                checkFillFields();
                            }
                        });
                        chipGroup.addView(rowView);
                    }
                } else {
                    if (response.code() == 204) {
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
            public void onFailure(Call<List<ClassServiceResponse>> call, Throwable t) {
                errorHandler.showCustomError(t.getMessage());
            }
        });
    }

    private void addCar() {
        showProgressDialog();
        AllCarResponse carInfo = new AllCarResponse(
                brandHashMap.get(brandSpinner.getSelectedItem().toString()),
                modelHashMap.get(modelSpinner.getSelectedItem().toString()),
                yearHashMap.get(yearSpinner.getSelectedItem().toString()),
                registrationNumberTextView.getText().toString(),
                descriptionNumberTextView.getText().toString()
        );
        Call<AllCarResponse> call = apiInterface.addCar(FastSave.getInstance().getString(ACCESS_TOKEN, ""), carInfo);
        call.enqueue(new Callback<AllCarResponse>() {
            @Override
            public void onResponse(Call<AllCarResponse> call, Response<AllCarResponse> response) {
                if (response.code() == 200) {
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
                    startActivity(new Intent(AddCarActivity.this, CarListActivity.class));
                    finish();
                    Toast.makeText(AddCarActivity.this, "Машина успешно добавлена", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        errorHandler.showError(jObjError.getInt("code"));
                    } catch (Exception e) {
                        errorHandler.showCustomError(e.getMessage());
                        closeProgressDialog();
                    }
                }
                closeProgressDialog();
            }

            @Override
            public void onFailure(Call<AllCarResponse> call, Throwable t) {
                errorHandler.showCustomError(t.getMessage());
                closeProgressDialog();
            }
        });
    }

    private void addClassService(String idCar, String idService) {
        Call<CarDeleteResponse> call = apiInterface.addClassService(idCar, idService, FastSave.getInstance().getString(ACCESS_TOKEN, ""));
        call.enqueue(new Callback<CarDeleteResponse>() {
            @Override
            public void onResponse(Call<CarDeleteResponse> call, Response<CarDeleteResponse> response) {
                if (response.code() == 200) {


                } else {
                    if (response.code() == 204) {
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
            public void onFailure(Call<CarDeleteResponse> call, Throwable t) {
                errorHandler.showCustomError(t.getMessage());
            }
        });
    }

    private void addCarFilter(String idCar, String idAttribute) {
        Call<CarDeleteResponse> call = apiInterface.addCarFilter(idCar, idAttribute, FastSave.getInstance().getString(ACCESS_TOKEN, ""));
        call.enqueue(new Callback<CarDeleteResponse>() {
            @Override
            public void onResponse(Call<CarDeleteResponse> call, Response<CarDeleteResponse> response) {
                if (response.code() == 200) {


                } else {
                    if (response.code() == 204) {
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
            public void onFailure(Call<CarDeleteResponse> call, Throwable t) {
                errorHandler.showCustomError(t.getMessage());
            }
        });
    }

    private void enableSpinner(MaterialSpinner materialSpinner) {
        switch (materialSpinner.getId()) {
            case R.id.brandSpinner:
                getBrands();
                brandSpinner.setEnabled(true);
                modelSpinner.setEnabled(false);
                yearSpinner.setEnabled(false);
                interiorSpinner.setEnabled(false);
                carBodySpinner.setEnabled(false);
                colourSpinner.setEnabled(false);
                break;
            case R.id.modelSpinner:
                getModel();
                brandSpinner.setEnabled(true);
                modelSpinner.setEnabled(true);
                yearSpinner.setEnabled(false);
                interiorSpinner.setEnabled(false);
                carBodySpinner.setEnabled(false);
                colourSpinner.setEnabled(false);
                break;
            case R.id.yearSpinner:
                getYears();
                brandSpinner.setEnabled(true);
                modelSpinner.setEnabled(true);
                yearSpinner.setEnabled(true);
                interiorSpinner.setEnabled(false);
                carBodySpinner.setEnabled(false);
                colourSpinner.setEnabled(false);
                break;
            case R.id.interiorSpinner:
                getInterior();
                brandSpinner.setEnabled(true);
                modelSpinner.setEnabled(true);
                yearSpinner.setEnabled(true);
                interiorSpinner.setEnabled(true);
                carBodySpinner.setEnabled(false);
                colourSpinner.setEnabled(false);
                break;
            case R.id.carBodySpinner:
                getCarBody();
                brandSpinner.setEnabled(true);
                modelSpinner.setEnabled(true);
                yearSpinner.setEnabled(true);
                interiorSpinner.setEnabled(true);
                carBodySpinner.setEnabled(true);
                colourSpinner.setEnabled(false);
                break;
            case R.id.colourSpinner:
                getColor();
                brandSpinner.setEnabled(true);
                modelSpinner.setEnabled(true);
                yearSpinner.setEnabled(true);
                interiorSpinner.setEnabled(true);
                carBodySpinner.setEnabled(true);
                colourSpinner.setEnabled(true);
                break;
        }
    }

    private void getBrands() {
        Call<List<BrandResponse>> call = apiInterface.getBrands();
        call.enqueue(new Callback<List<BrandResponse>>() {
            @Override
            public void onResponse(Call<List<BrandResponse>> call, Response<List<BrandResponse>> response) {
                if (response.code() == 200) {
                    ArrayList<String> brandITEMS = new ArrayList<>();
                    for (int i = 0; i < response.body().size(); i++) {
                        brandITEMS.add(response.body().get(i).getName());
                        brandHashMap.put(response.body().get(i).getName(), response.body().get(i).getId());
                    }
                    spinnerAdapter = new ArrayAdapter<>(AddCarActivity.this, R.layout.car_hint_item_layout, brandITEMS);
                    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    brandSpinner.setAdapter(spinnerAdapter);
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
            public void onFailure(Call<List<BrandResponse>> call, Throwable t) {
                errorHandler.showCustomError(t.getMessage());
            }
        });
    }

    private void getModel() {
        Call<List<BrandResponse>> call = apiInterface.getModels(brandHashMap.get(brandSpinner.getSelectedItem()));
        call.enqueue(new Callback<List<BrandResponse>>() {
            @Override
            public void onResponse(Call<List<BrandResponse>> call, Response<List<BrandResponse>> response) {
                if (response.code() == 200) {
                    ArrayList<String> modelITEMS = new ArrayList<>();
                    for (int i = 0; i < response.body().size(); i++) {
                        modelITEMS.add(response.body().get(i).getName());
                        modelHashMap.put(response.body().get(i).getName(), response.body().get(i).getId());

                    }
                    spinnerAdapter = new ArrayAdapter<String>(AddCarActivity.this, R.layout.car_hint_item_layout, modelITEMS);
                    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    modelSpinner.setAdapter(spinnerAdapter);
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
            public void onFailure(Call<List<BrandResponse>> call, Throwable t) {
                errorHandler.showCustomError(t.getMessage());
            }
        });
    }

    private void getYears() {
        Call<List<BrandResponse>> call = apiInterface.getYears();
        call.enqueue(new Callback<List<BrandResponse>>() {
            @Override
            public void onResponse(Call<List<BrandResponse>> call, Response<List<BrandResponse>> response) {
                if (response.code() == 200) {
                    ArrayList<String> yearITEMS = new ArrayList<>();
                    for (int i = 0; i < response.body().size(); i++) {
                        yearITEMS.add(response.body().get(i).getName());
                        yearHashMap.put(response.body().get(i).getName(), response.body().get(i).getId());

                    }
                    spinnerAdapter = new ArrayAdapter<>(AddCarActivity.this, R.layout.car_hint_item_layout, yearITEMS);
                    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    yearSpinner.setAdapter(spinnerAdapter);
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
            public void onFailure(Call<List<BrandResponse>> call, Throwable t) {
                errorHandler.showCustomError(t.getMessage());
            }
        });
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

    public void getAllFilter() {
        Call<List<FilterResponse>> call = apiInterface.getFilters(SERVICE_TYPE);
        call.enqueue(new Callback<List<FilterResponse>>() {
            @Override
            public void onResponse(Call<List<FilterResponse>> call, Response<List<FilterResponse>> response) {
                if (response.code() == 200) {
                    for (int i = 0; i < response.body().size(); i++) {
                        filterMap.put(response.body().get(i).getValue(), response.body().get(i));
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
            public void onFailure(Call<List<FilterResponse>> call, Throwable t) {
                errorHandler.showCustomError(t.getMessage());
            }
        });
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

    public void showProgressDialog() {
        progressDialog = ProgressDialog.show(this, "Ща сек...", "Ща все сделаю...");
    }

    public void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    private void checkFillFields() {
        boolean result = false;
        if (brandFlag && modelFlag && yearFlag && interiorFlag && carBodyFlag && colourFlag) {
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
                enableSpinner(modelSpinner);
                brandFlag = true;
            } else {
                brandFlag = false;
            }
            checkFillFields();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
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
                enableSpinner(interiorSpinner);
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
                enableSpinner(carBodySpinner);
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
                enableSpinner(colourSpinner);
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
}
