package com.gliesereum.karma;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ScrollView;
import android.widget.Toast;

import com.appizona.yehiahd.fastsave.FastSave;
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
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import fr.ganfra.materialspinner.MaterialSpinner;
import gr.escsoft.michaelprimez.searchablespinner.SearchableSpinner;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.gliesereum.karma.util.Constants.ACCESS_TOKEN;
import static com.gliesereum.karma.util.Constants.SERVICE_TYPE;

public class AddCarActivity extends AppCompatActivity {

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
    private final ArrayList<String> mStrings = new ArrayList<>();
    private ScrollView scrollView;
    private gr.escsoft.michaelprimez.searchablespinner.SearchableSpinner SearchableSpinner;
    private TextInputLayout registrationNumberTextInputLayout;
    private TextInputEditText registrationNumberTextView;
    private TextInputLayout descriptionNumberTextInputLayout;
    private TextInputEditText descriptionNumberTextView;
    private MaterialButton addCarBtn;
    private List<ClassServiceResponse> classServiceList;
    private ArrayAdapter<String> modelAdapter;
    private ProgressDialog progressDialog;
    private APIInterface apiInterface;
    private ErrorHandler errorHandler;
    ArrayList<String> modelITEMS = new ArrayList<>();
    HashMap<String, String> brandHashMap = new HashMap<>();
    HashMap<String, String> modelHashMap = new HashMap<>();
    HashMap<String, String> yearHashMap = new HashMap<>();
    HashMap<String, String> interiorHashMap = new HashMap<>();
    HashMap<String, String> carBodyHashMap = new HashMap<>();
    HashMap<String, String> colorHashMap = new HashMap<>();
    ArrayAdapter<String> interiorAdapter;
    private ArrayAdapter<String> carBodyAdapter;
    private ArrayAdapter<String> colourAdapter;
    private ConstraintLayout constraintLayout;
    private ChipGroup chipGroup;
    Map<String, String> mapClassServise = new HashMap<>();
    List<Chip> selectedChip = new ArrayList<>();
    private List<FilterResponse> allFilterList = new ArrayList<>();
    private Map<String, FilterResponse> filterMap = new HashMap<>();
    private String TAG = "TAG";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);
        FastSave.init(getApplicationContext());

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Добавление авто");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        initView();
        enableSpinner(brandSpinner);
        getAllClassService();
        getAllFilter();

        brandSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        });
        modelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        });
        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        });
        interiorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        });
        carBodySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        });
        colourSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        });

        String[] modelITEMS = {""};
        modelAdapter = new ArrayAdapter<String>(this, R.layout.car_hint_item_layout, modelITEMS);
        modelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        modelSpinner.setAdapter(modelAdapter);

        String[] yearITEMS = {""};
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(this, R.layout.car_hint_item_layout, yearITEMS);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(yearAdapter);

        String[] interiorITEMS = {""};
        ArrayAdapter<String> interiorAdapter = new ArrayAdapter<String>(this, R.layout.car_hint_item_layout, interiorITEMS);
        interiorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        interiorSpinner.setAdapter(interiorAdapter);

        String[] carBodyITEMS = {""};
        ArrayAdapter<String> carBodyAdapter = new ArrayAdapter<String>(this, R.layout.car_hint_item_layout, carBodyITEMS);
        carBodyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        carBodySpinner.setAdapter(carBodyAdapter);

        String[] colourITEMS = {""};
        ArrayAdapter<String> colourAdapter = new ArrayAdapter<String>(this, R.layout.car_hint_item_layout, colourITEMS);
        colourAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        colourSpinner.setAdapter(colourAdapter);
    }

    private void getAllClassService() {
        apiInterface = APIClient.getClient().create(APIInterface.class);
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
//                        Toast.makeText(CarListActivity.this, "", Toast.LENGTH_SHORT).show();
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

//    private OnItemSelectedListener mOnItemSelectedListener = new OnItemSelectedListener() {
//        @Override
//        public void onItemSelected(View view, int position, long id) {
//            Toast.makeText(AddCarActivity.this, "Item on position " + position + " : " + " Selected", Toast.LENGTH_SHORT).show();
//        }
//
//        @Override
//        public void onNothingSelected() {
//            Toast.makeText(AddCarActivity.this, "Nothing Selected", Toast.LENGTH_SHORT).show();
//        }
//    };


    private void initView() {
        errorHandler = new ErrorHandler(this, this);
        brandSpinner = findViewById(R.id.brandSpinner);
        modelSpinner = (MaterialSpinner) findViewById(R.id.modelSpinner);
        yearSpinner = (MaterialSpinner) findViewById(R.id.yearSpinner);
        interiorSpinner = (MaterialSpinner) findViewById(R.id.interiorSpinner);
        carBodySpinner = (MaterialSpinner) findViewById(R.id.carBodySpinner);
        colourSpinner = (MaterialSpinner) findViewById(R.id.colourSpinner);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        SearchableSpinner = (SearchableSpinner) findViewById(R.id.SearchableSpinner);
        registrationNumberTextInputLayout = (TextInputLayout) findViewById(R.id.registrationNumberTextInputLayout);
        registrationNumberTextView = (TextInputEditText) findViewById(R.id.registrationNumberTextView);
        descriptionNumberTextInputLayout = (TextInputLayout) findViewById(R.id.descriptionNumberTextInputLayout);
        descriptionNumberTextView = (TextInputEditText) findViewById(R.id.descriptionNumberTextView);
        addCarBtn = (MaterialButton) findViewById(R.id.addCarBtn);
        addCarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCar();
            }
        });


        constraintLayout = (ConstraintLayout) findViewById(R.id.constraintLayout);
        chipGroup = (ChipGroup) findViewById(R.id.chipGroup);
    }

    private String setInterior(String string) {
        String interior = "";
        if (string.equals(getString(R.string.textile))) {
            interior = "TEXTILE";
        } else if (string.equals(getString(R.string.suede))) {
            interior = "SUEDE";
        } else if (string.equals(getString(R.string.leather))) {
            interior = "LEATHER";
        } else if (string.equals(getString(R.string.artificial_leather))) {
            interior = "ARTIFICIAL_LEATHER";
        } else if (string.equals(getString(R.string.alcantara))) {
            interior = "ALCANTARA";
        } else if (string.equals(getString(R.string.taskana))) {
            interior = "TASKANA";
        } else if (string.equals(getString(R.string.velours))) {
            interior = "VELOURS";
        }
        return interior;
    }

    private String setCarBody(String string) {
        String carBody = "";
        if (string.equals(getString(R.string.sedan))) {
            carBody = "SEDAN";
        } else if (string.equals(getString(R.string.wagon))) {
            carBody = "WAGON";
        } else if (string.equals(getString(R.string.hatchback))) {
            carBody = "HATCHBACK";
        } else if (string.equals(getString(R.string.liftback))) {
            carBody = "LIFTBACK";
        } else if (string.equals(getString(R.string.limousine))) {
            carBody = "LIMOUSINE";
        } else if (string.equals(getString(R.string.minivan))) {
            carBody = "MINIVAN";
        } else if (string.equals(getString(R.string.coupe))) {
            carBody = "COUPE";
        } else if (string.equals(getString(R.string.cabriolet))) {
            carBody = "CABRIOLET";
        } else if (string.equals(getString(R.string.crossover))) {
            carBody = "CROSSOVER";
        } else if (string.equals(getString(R.string.suv))) {
            carBody = "SUV";
        }
        return carBody;
    }

    private String setColor(String string) {
        String color = "";
        if (string.equals(getString(R.string.white))) {
            color = "WHITE";
        } else if (string.equals(getString(R.string.black))) {
            color = "BLACK";
        } else if (string.equals(getString(R.string.gray))) {
            color = "GRAY";
        } else if (string.equals(getString(R.string.silver))) {
            color = "SILVER";
        } else if (string.equals(getString(R.string.golden))) {
            color = "GOLDEN";
        } else if (string.equals(getString(R.string.red))) {
            color = "RED";
        } else if (string.equals(getString(R.string.blue))) {
            color = "BLUE";
        } else if (string.equals(getString(R.string.brown))) {
            color = "BROWN";
        } else if (string.equals(getString(R.string.beige))) {
            color = "BEIGE";
        } else if (string.equals(getString(R.string.yellow))) {
            color = "YELLOW";
        } else if (string.equals(getString(R.string.green))) {
            color = "GREEN";
        } else if (string.equals(getString(R.string.other))) {
            color = "OTHER";
        }
        return color;
    }

    private void addCar() {
        apiInterface = APIClient.getClient().create(APIInterface.class);
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
                    startActivity(new Intent(AddCarActivity.this, CarListActivity2.class));
                    finish();
                    Toast.makeText(AddCarActivity.this, "Машина успешно добавлена", Toast.LENGTH_SHORT).show();
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
            public void onFailure(Call<AllCarResponse> call, Throwable t) {
                errorHandler.showCustomError(t.getMessage());
            }
        });
    }

    private void addClassService(String idCar, String idService) {
        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<CarDeleteResponse> call = apiInterface.addClassService(idCar, idService, FastSave.getInstance().getString(ACCESS_TOKEN, ""));
        call.enqueue(new Callback<CarDeleteResponse>() {
            @Override
            public void onResponse(Call<CarDeleteResponse> call, Response<CarDeleteResponse> response) {
                if (response.code() == 200) {


                } else {
                    if (response.code() == 204) {
//                        Toast.makeText(CarListActivity.this, "", Toast.LENGTH_SHORT).show();
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
        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<CarDeleteResponse> call = apiInterface.addCarFilter(idCar, idAttribute, FastSave.getInstance().getString(ACCESS_TOKEN, ""));
        call.enqueue(new Callback<CarDeleteResponse>() {
            @Override
            public void onResponse(Call<CarDeleteResponse> call, Response<CarDeleteResponse> response) {
                if (response.code() == 200) {


                } else {
                    if (response.code() == 204) {
//                        Toast.makeText(CarListActivity.this, "", Toast.LENGTH_SHORT).show();
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
        showProgressDialog();
        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<List<BrandResponse>> call = apiInterface.getBrands();
        call.enqueue(new Callback<List<BrandResponse>>() {
            @Override
            public void onResponse(Call<List<BrandResponse>> call, Response<List<BrandResponse>> response) {
                if (response.code() == 200) {
                    ArrayList<String> modelITEMS = new ArrayList<>();
                    for (int i = 0; i < response.body().size(); i++) {
                        modelITEMS.add(response.body().get(i).getName());
                        brandHashMap.put(response.body().get(i).getName(), response.body().get(i).getId());
                    }
                    modelAdapter = new ArrayAdapter<String>(AddCarActivity.this, R.layout.car_hint_item_layout, modelITEMS);
                    modelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    brandSpinner.setAdapter(modelAdapter);
                    closeProgressDialog();
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        errorHandler.showError(jObjError.getInt("code"));
                        closeProgressDialog();
                    } catch (Exception e) {
                        errorHandler.showCustomError(e.getMessage());
                        closeProgressDialog();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<BrandResponse>> call, Throwable t) {
                errorHandler.showCustomError(t.getMessage());
                closeProgressDialog();
            }
        });
    }

    private void getModel() {
        showProgressDialog();
        apiInterface = APIClient.getClient().create(APIInterface.class);
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
                    modelAdapter = new ArrayAdapter<String>(AddCarActivity.this, R.layout.car_hint_item_layout, modelITEMS);
                    modelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    modelSpinner.setAdapter(modelAdapter);
                    closeProgressDialog();
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        errorHandler.showError(jObjError.getInt("code"));
                        closeProgressDialog();
                    } catch (Exception e) {
                        errorHandler.showCustomError(e.getMessage());
                        closeProgressDialog();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<BrandResponse>> call, Throwable t) {
                errorHandler.showCustomError(t.getMessage());
                closeProgressDialog();
            }
        });
    }

    private void getYears() {
        showProgressDialog();
        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<List<BrandResponse>> call = apiInterface.getYears();
        call.enqueue(new Callback<List<BrandResponse>>() {
            @Override
            public void onResponse(Call<List<BrandResponse>> call, Response<List<BrandResponse>> response) {
                if (response.code() == 200) {
                    ArrayList<String> modelITEMS = new ArrayList<>();
                    for (int i = 0; i < response.body().size(); i++) {
                        modelITEMS.add(response.body().get(i).getName());
                        yearHashMap.put(response.body().get(i).getName(), response.body().get(i).getId());

                    }
                    modelAdapter = new ArrayAdapter<String>(AddCarActivity.this, R.layout.car_hint_item_layout, modelITEMS);
                    modelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    yearSpinner.setAdapter(modelAdapter);
                    closeProgressDialog();
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        errorHandler.showError(jObjError.getInt("code"));
                        closeProgressDialog();
                    } catch (Exception e) {
                        errorHandler.showCustomError(e.getMessage());
                        closeProgressDialog();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<BrandResponse>> call, Throwable t) {
                errorHandler.showCustomError(t.getMessage());
                closeProgressDialog();
            }
        });
    }

    public void getInterior() {
        ArrayList<String> interiorITEMS = new ArrayList<>();
        for (int i = 0; i < filterMap.get("CAR_INTERIOR").getAttributes().size(); i++) {
            interiorITEMS.add(filterMap.get("CAR_INTERIOR").getAttributes().get(i).getTitle());
            interiorHashMap.put(filterMap.get("CAR_INTERIOR").getAttributes().get(i).getTitle(), filterMap.get("CAR_INTERIOR").getAttributes().get(i).getId());
        }

        interiorAdapter = new ArrayAdapter<String>(AddCarActivity.this, R.layout.car_hint_item_layout, interiorITEMS);
        interiorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        interiorSpinner.setAdapter(interiorAdapter);
    }

    public void getCarBody() {
//        carBodySpinner.setAdapter(carBodyAdapter);
        ArrayList<String> carBodyITEMS = new ArrayList<>();
        for (int i = 0; i < filterMap.get("CAR_BODY").getAttributes().size(); i++) {
            carBodyITEMS.add(filterMap.get("CAR_BODY").getAttributes().get(i).getTitle());
            carBodyHashMap.put(filterMap.get("CAR_BODY").getAttributes().get(i).getTitle(), filterMap.get("CAR_BODY").getAttributes().get(i).getId());
        }

        carBodyAdapter = new ArrayAdapter<String>(AddCarActivity.this, R.layout.car_hint_item_layout, carBodyITEMS);
        carBodyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        carBodySpinner.setAdapter(carBodyAdapter);
    }

    public void getAllFilter() {
//        showProgressDialog();
        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<List<FilterResponse>> call = apiInterface.getFilters(SERVICE_TYPE);
        call.enqueue(new Callback<List<FilterResponse>>() {
            @Override
            public void onResponse(Call<List<FilterResponse>> call, Response<List<FilterResponse>> response) {
                if (response.code() == 200) {
                    for (int i = 0; i < response.body().size(); i++) {
                        filterMap.put(response.body().get(i).getValue(), response.body().get(i));
                    }
//                    closeProgressDialog();
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        errorHandler.showError(jObjError.getInt("code"));
//                        closeProgressDialog();
                    } catch (Exception e) {
                        errorHandler.showCustomError(e.getMessage());
//                        closeProgressDialog();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<FilterResponse>> call, Throwable t) {
                errorHandler.showCustomError(t.getMessage());
//                closeProgressDialog();
            }
        });
    }

    public void getColor() {
//        colourSpinner.setAdapter(colourAdapter);
        ArrayList<String> colorITEMS = new ArrayList<>();
        for (int i = 0; i < filterMap.get("CAR_COLOR").getAttributes().size(); i++) {
            colorITEMS.add(filterMap.get("CAR_COLOR").getAttributes().get(i).getTitle());
            colorHashMap.put(filterMap.get("CAR_COLOR").getAttributes().get(i).getTitle(), filterMap.get("CAR_COLOR").getAttributes().get(i).getId());
        }

        carBodyAdapter = new ArrayAdapter<String>(AddCarActivity.this, R.layout.car_hint_item_layout, colorITEMS);
        carBodyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        colourSpinner.setAdapter(carBodyAdapter);
    }

    public void showProgressDialog() {
//        Log.d(TAG, "showProgressDialog: ");
        progressDialog = ProgressDialog.show(this, "Ща сек...", "Ща все сделаю...");

    }

    public void closeProgressDialog() {
//        Log.d(TAG, "closeProgressDialog: ");
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

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        startActivity(new Intent(AddCarActivity.this, CarListActivity.class));
//        finish();
//        Toast.makeText(this, "11111111", Toast.LENGTH_SHORT).show();
//    }
}
