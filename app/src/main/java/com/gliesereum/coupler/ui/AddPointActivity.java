package com.gliesereum.coupler.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.gliesereum.coupler.R;
import com.gliesereum.coupler.data.network.APIClient;
import com.gliesereum.coupler.data.network.APIInterface;
import com.gliesereum.coupler.data.network.CustomCallback;
import com.gliesereum.coupler.data.network.json.businesscategory.BusinesCategoryResponse;
import com.gliesereum.coupler.data.network.json.carwash.AddPointBody;
import com.gliesereum.coupler.data.network.json.carwashnew.CarWashResponse;
import com.gliesereum.coupler.util.FastSave;
import com.gohn.nativedialog.ButtonType;
import com.gohn.nativedialog.NDialog;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import fr.ganfra.materialspinner.MaterialSpinner;
import retrofit2.Call;
import retrofit2.Response;

import static com.gliesereum.coupler.util.Constants.ACCESS_TOKEN;

public class AddPointActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";
    private GoogleMap mMap;
    private boolean mLocationPermissionGranted;
    private String TAG = "TAG";
    private Location mLastKnownLocation;
    private LatLng mDefaultLocation;
    private FusedLocationProviderClient mFusedLocationClient;
    private MapView mapView;
    private Button openDialogBtn;
    private boolean doubleBack = false;

    private MaterialSpinner categorySpinner;
    private APIInterface API;
    private CustomCallback customCallback;
    private HashMap<String, String> categoryHashMap;
    private ArrayAdapter<String> spinnerAdapter;

    private TextInputEditText pointNameTextView;
    private TextInputEditText pointPhoneTextView;
    private Button addPointToBaseBtn;
    private AddPointBody addPointBody;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_point);
        initData();
        initView();
        initMap(savedInstanceState);
        getLocationPermission();
        getBusinessCategory();
    }

    private void initData() {
        FastSave.init(getApplicationContext());
        API = APIClient.getClient().create(APIInterface.class);
        customCallback = new CustomCallback(this, this);
        spinnerAdapter = new ArrayAdapter<>(this, R.layout.car_hint_item_layout, new String[]{""});
        categoryHashMap = new HashMap<>();
        addPointBody = new AddPointBody();
    }

    private void initView() {
        mapView = findViewById(R.id.mapView);
        openDialogBtn = findViewById(R.id.openDialogBtn);
        openDialogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddPointDialog();
            }
        });
        mDefaultLocation = new LatLng(50, 30);
    }

    private void openAddPointDialog() {
        NDialog addPointDialog = new NDialog(AddPointActivity.this, ButtonType.NO_BUTTON);
        addPointDialog.isCancelable(true);
        addPointDialog.setCustomView(R.layout.add_point_dialod);
        List<View> childViews = addPointDialog.getCustomViewChildren();
        for (View childView : childViews) {
            switch (childView.getId()) {
                case R.id.spinner:
                    categorySpinner = childView.findViewById(R.id.spinner);
                    categorySpinner.setAdapter(spinnerAdapter);
                    break;
                case R.id.pointNameTextView:
                    pointNameTextView = childView.findViewById(R.id.pointNameTextView);
                    break;
                case R.id.pointPhoneTextView:
                    pointPhoneTextView = childView.findViewById(R.id.pointPhoneTextView);
                    break;
                case R.id.addPointToBaseBtn:
                    addPointToBaseBtn = childView.findViewById(R.id.addPointToBaseBtn);
                    addPointToBaseBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            addPointBody.setBusinessCategoryId(categoryHashMap.get(categorySpinner.getSelectedItem().toString()));
                            addPointBody.setName(pointNameTextView.getText().toString());
                            addPointBody.setPhone(pointPhoneTextView.getText().toString());
                            API.addPointToMap(FastSave.getInstance().getString(ACCESS_TOKEN, ""), addPointBody)
                                    .enqueue(customCallback.getResponseWithProgress(new CustomCallback.ResponseCallback<CarWashResponse>() {
                                        @Override
                                        public void onSuccessful(Call<CarWashResponse> call, Response<CarWashResponse> response) {
                                            addPointDialog.dismiss();
                                        }

                                        @Override
                                        public void onEmpty(Call<CarWashResponse> call, Response<CarWashResponse> response) {

                                        }
                                    }));
                        }
                    });
                    break;
            }
        }
        addPointDialog.show();
    }


    private void getBusinessCategory() {
        API.getBusinessCategory()
                .enqueue(customCallback.getResponse(new CustomCallback.ResponseCallback<List<BusinesCategoryResponse>>() {
                    @Override
                    public void onSuccessful(Call<List<BusinesCategoryResponse>> call, Response<List<BusinesCategoryResponse>> response) {
                        ArrayList<String> categoryItems = new ArrayList<>();
                        for (int i = 0; i < response.body().size(); i++) {
                            categoryItems.add(response.body().get(i).getName());
                            categoryHashMap.put(response.body().get(i).getName(), response.body().get(i).getId());
                        }
                        spinnerAdapter = new ArrayAdapter<>(AddPointActivity.this, R.layout.car_hint_item_layout, categoryItems);
                        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    }

                    @Override
                    public void onEmpty(Call<List<BusinesCategoryResponse>> call, Response<List<BusinesCategoryResponse>> response) {

                    }
                }));
    }

    private void initMap(Bundle savedInstanceState) {
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }
        mapView.getMapAsync(this);
        mapView.onCreate(mapViewBundle);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    private void getLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        } else {
            mLocationPermissionGranted = true;
//            getAllCarWash(new FilterCarWashBody());
            updateLocationUI();
            getDeviceLocation();
        }
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e) {
        }
    }

    private void getDeviceLocation() {
        try {
            if (mLocationPermissionGranted) {
                Task locationResult = mFusedLocationClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            mLastKnownLocation = (Location) task.getResult();
                            if (mLastKnownLocation != null) {
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), 12));
                            }
                        } else {
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, 12));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        try {
            boolean success = googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.coupler_map));
            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }
        mMap.clear();
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                addPointBody.setLatitude(latLng.latitude);
                addPointBody.setLongitude(latLng.longitude);
                markerOptions.title(latLng.latitude + " : " + latLng.longitude);
                mMap.clear();
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.addMarker(markerOptions);
                openDialogBtn.setEnabled(true);
            }
        });
        mMap.getUiSettings().setMapToolbarEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);
        updateLocationUI();
        getDeviceLocation();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    updateLocationUI();
                    getDeviceLocation();
                } else {
                }
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onBackPressed() {
        if (doubleBack) {
            super.onBackPressed();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return;
        }
        this.doubleBack = true;
        Toast.makeText(this, "Пожалуйста, нажмите НАЗАД еще раз, чтобы выйти", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBack = false;
            }
        }, 2000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }
}
