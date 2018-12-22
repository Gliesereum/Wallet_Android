package com.gliesereum.karma;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import com.gliesereum.karma.data.network.json.classservices.ClassServiceResponse;
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
import gr.escsoft.michaelprimez.searchablespinner.interfaces.IStatusListener;
import gr.escsoft.michaelprimez.searchablespinner.interfaces.OnItemSelectedListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.gliesereum.karma.util.Constants.ACCESS_TOKEN;

public class AddCarActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private MaterialSpinner brandSpinner;
    private MaterialSpinner modelSpinner;
    private MaterialSpinner yearSpinner;
    private MaterialSpinner interiorSpinner;
    private MaterialSpinner carBodySpinner;
    private MaterialSpinner colourSpinner;
    private SearchableSpinner mSearchableSpinner;
    private SimpleArrayListAdapter mSimpleArrayListAdapter;
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
    ArrayAdapter<String> interiorAdapter;
    private ArrayAdapter<String> carBodyAdapter;
    private ArrayAdapter<String> colourAdapter;
    private ConstraintLayout constraintLayout;
    private ChipGroup chipGroup;
    Map<String, String> mapClassServise = new HashMap<>();
    List<Chip> selectedChip = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);
        FastSave.init(getApplicationContext());

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        initView();
        enableSpinner(brandSpinner);
        getAllClassService();

        brandSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (id != 0) {
                    enableSpinner(modelSpinner);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        modelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (id != 0) {
                    enableSpinner(yearSpinner);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (id != 0) {
                    enableSpinner(interiorSpinner);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        interiorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (id != 0) {
                    enableSpinner(carBodySpinner);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        carBodySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (id != 0) {
                    enableSpinner(colourSpinner);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        String[] modelITEMS = {"A100"};
        modelAdapter = new ArrayAdapter<String>(this, R.layout.car_hint_item_layout, modelITEMS);
        modelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        modelSpinner.setAdapter(modelAdapter);

        String[] yearITEMS = {"1990"};
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(this, R.layout.car_hint_item_layout, yearITEMS);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(yearAdapter);

        String[] interiorITEMS = {"SUEDE", "LEATHER", "ARTIFICIAL_LEATHER", "ALCANTARA", "TASKANA", "VELOURS"};
        interiorAdapter = new ArrayAdapter<String>(this, R.layout.car_hint_item_layout, interiorITEMS);
        interiorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        interiorSpinner.setAdapter(interiorAdapter);

        String[] carBodyITEMS = {"SEDAN", "WAGON", "HATCHBACK", "LIFTBACK", "LIMOUSINE", "MINIVAN", "COUPE", "CABRIOLET", "CROSSOVER", "SUV"};
        carBodyAdapter = new ArrayAdapter<String>(this, R.layout.car_hint_item_layout, carBodyITEMS);
        carBodyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        carBodySpinner.setAdapter(carBodyAdapter);

        String[] colourITEMS = {"WHITE", "BLACK", "GRAY", "SILVER", "GOLDEN", "RED", "BLUE", "BROWN", "BEIGE", "YELLOW", "GREEN", "OTHER"};
        colourAdapter = new ArrayAdapter<String>(this, R.layout.car_hint_item_layout, colourITEMS);
        colourAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        colourSpinner.setAdapter(colourAdapter);


        mSimpleArrayListAdapter = new SimpleArrayListAdapter(this, mStrings);

        mSearchableSpinner.setAdapter(mSimpleArrayListAdapter);
        mSearchableSpinner.setOnItemSelectedListener(mOnItemSelectedListener);
        mSearchableSpinner.setStatusListener(new IStatusListener() {
            @Override
            public void spinnerIsOpening() {
//                mSearchableSpinner1.hideEdit();
//                mSearchableSpinner2.hideEdit();
            }

            @Override
            public void spinnerIsClosing() {

            }
        });

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
                        chipGroup.addView(rowView);
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
            public void onFailure(Call<List<ClassServiceResponse>> call, Throwable t) {
                errorHandler.showCustomError(t.getMessage());
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!mSearchableSpinner.isInsideSearchEditText(event)) {
            mSearchableSpinner.hideEdit();
        }
//        if (!mSearchableSpinner1.isInsideSearchEditText(event)) {
//            mSearchableSpinner1.hideEdit();
//        }
//        if (!mSearchableSpinner2.isInsideSearchEditText(event)) {
//            mSearchableSpinner2.hideEdit();
//        }
        return super.onTouchEvent(event);
    }

    private OnItemSelectedListener mOnItemSelectedListener = new OnItemSelectedListener() {
        @Override
        public void onItemSelected(View view, int position, long id) {
            Toast.makeText(AddCarActivity.this, "Item on position " + position + " : " + " Selected", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNothingSelected() {
            Toast.makeText(AddCarActivity.this, "Nothing Selected", Toast.LENGTH_SHORT).show();
        }
    };


    private void initView() {
        errorHandler = new ErrorHandler(this, this);
        brandSpinner = (MaterialSpinner) findViewById(R.id.brandSpinner);
        modelSpinner = (MaterialSpinner) findViewById(R.id.modelSpinner);
        yearSpinner = (MaterialSpinner) findViewById(R.id.yearSpinner);
        interiorSpinner = (MaterialSpinner) findViewById(R.id.interiorSpinner);
        carBodySpinner = (MaterialSpinner) findViewById(R.id.carBodySpinner);
        colourSpinner = (MaterialSpinner) findViewById(R.id.colourSpinner);
        mSearchableSpinner = (SearchableSpinner) findViewById(R.id.SearchableSpinner);
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

    private void addCar() {
        apiInterface = APIClient.getClient().create(APIInterface.class);
        AllCarResponse carInfo = new AllCarResponse(
                brandHashMap.get(brandSpinner.getSelectedItem().toString()),
                modelHashMap.get(modelSpinner.getSelectedItem().toString()),
                yearHashMap.get(yearSpinner.getSelectedItem().toString()),
                registrationNumberTextView.getText().toString(),
                descriptionNumberTextView.getText().toString(),
                interiorSpinner.getSelectedItem().toString(),
                carBodySpinner.getSelectedItem().toString(),
                colourSpinner.getSelectedItem().toString()
        );

        Call<AllCarResponse> call = apiInterface.addCar("Bearer " + FastSave.getInstance().getString(ACCESS_TOKEN, ""), carInfo);
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


                    startActivity(new Intent(AddCarActivity.this, CarListActivity.class));
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
        Call<AllCarResponse> call = apiInterface.addClassService(idCar, idService, "Bearer " + FastSave.getInstance().getString(ACCESS_TOKEN, ""));
        call.enqueue(new Callback<AllCarResponse>() {
            @Override
            public void onResponse(Call<AllCarResponse> call, Response<AllCarResponse> response) {
                if (response.code() == 200) {


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
        interiorSpinner.setAdapter(interiorAdapter);
    }

    public void getCarBody() {
        carBodySpinner.setAdapter(carBodyAdapter);
    }

    public void getColor() {
        colourSpinner.setAdapter(colourAdapter);
    }

    public void showProgressDialog() {
        progressDialog = ProgressDialog.show(this, "title", "message");

    }

    public void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

}
