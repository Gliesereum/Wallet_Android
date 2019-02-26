package com.gliesereum.karma;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.appizona.yehiahd.fastsave.FastSave;
import com.gliesereum.karma.data.network.APIClient;
import com.gliesereum.karma.data.network.APIInterface;
import com.gliesereum.karma.data.network.json.carwash.AllCarWashResponse;
import com.gliesereum.karma.data.network.json.carwash.FilterCarWashBody;
import com.gliesereum.karma.data.network.json.filter.AttributesItem;
import com.gliesereum.karma.data.network.json.service.ServiceResponse;
import com.gliesereum.karma.util.ErrorHandler;
import com.gliesereum.karma.util.Util;
import com.gohn.nativedialog.ButtonType;
import com.gohn.nativedialog.NDialog;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;

import net.sharewire.googlemapsclustering.Cluster;
import net.sharewire.googlemapsclustering.ClusterManager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.gliesereum.karma.util.Constants.CARWASH_ID;
import static com.gliesereum.karma.util.Constants.CAR_BRAND;
import static com.gliesereum.karma.util.Constants.CAR_FILTER_LIST;
import static com.gliesereum.karma.util.Constants.CAR_ID;
import static com.gliesereum.karma.util.Constants.CAR_MODEL;
import static com.gliesereum.karma.util.Constants.IS_LOGIN;
import static com.gliesereum.karma.util.Constants.SERVICE_TYPE;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, CompoundButton.OnCheckedChangeListener {

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1101;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private GoogleMap mMap;
    private boolean mLocationPermissionGranted;
    private String TAG = "TAG";
    private Location mLastKnownLocation;
    private LatLng mDefaultLocation = new LatLng(50, 30);
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private FusedLocationProviderClient mFusedLocationClient;
    Bitmap bitmapSource;
    private Toolbar toolbar;
    private MapView mapView;
    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";
    private List<AllCarWashResponse> carWashList;
    private MaterialButton addCarBtn;
    private APIInterface apiInterface;
    private ErrorHandler errorHandler;
    private List<ServiceResponse> serviceList = new ArrayList<>();
    private Map<String, String> mapServise = new HashMap<>();
    private Set<String> serviceIdList = new HashSet<>();
    private Menu menu;
    boolean doubleBackToExitPressedOnce = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        FastSave.init(getApplicationContext());
        errorHandler = new ErrorHandler(this, this);
        toolbar = findViewById(R.id.toolbar);
        if (FastSave.getInstance().getString(CAR_BRAND, "").equals("") && FastSave.getInstance().getString(CAR_MODEL, "").equals("")) {
            toolbar.setTitle("KARMA");
        } else {
            toolbar.setTitle(FastSave.getInstance().getString(CAR_BRAND, "") + " " + FastSave.getInstance().getString(CAR_MODEL, ""));
            toolbar.setSubtitle("Выбранный автомобиль");
        }
        setSupportActionBar(toolbar);

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }

        mapView = findViewById(R.id.map);
        mapView.getMapAsync(this);
        mapView.onCreate(mapViewBundle);

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLocationPermission();
        new Util(this, toolbar, 1).addNavigation();

        getAllService();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onMapReady: ");
        mMap = googleMap;
        getAllCarWash(new FilterCarWashBody());
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                if (FastSave.getInstance().getBoolean(IS_LOGIN, false)) {
                    if (FastSave.getInstance().getObjectsList(CAR_FILTER_LIST, AttributesItem.class) != null) {
                        FastSave.getInstance().saveString(CARWASH_ID, marker.getSnippet());
                        startActivity(new Intent(MapsActivity.this, CarWashActivity.class));
                    } else {
                        startActivity(new Intent(MapsActivity.this, CarListActivity2.class));
                    }
                } else {
                    startActivity(new Intent(MapsActivity.this, LoginActivity.class));
                    finish();
                }

            }
        });

    }

    private void getAllCarWash(FilterCarWashBody filterCarWashBody) {
        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<List<AllCarWashResponse>> call = apiInterface.getAllCarWash(filterCarWashBody);
        call.enqueue(new Callback<List<AllCarWashResponse>>() {
            @Override
            public void onResponse(Call<List<AllCarWashResponse>> call, Response<List<AllCarWashResponse>> response) {
                if (response.code() == 200) {
                    carWashList = response.body();
                    mMap.clear();
                    List<LatLng> coordinateList = new ArrayList<>();
                    List<SampleClusterItem> clusterItems = new ArrayList<>();

                    ClusterManager<SampleClusterItem> clusterManager = new ClusterManager<>(MapsActivity.this, mMap);
                    mMap.setOnCameraIdleListener(clusterManager);
                    clusterManager.setCallbacks(new ClusterManager.Callbacks<SampleClusterItem>() {
                        @Override
                        public boolean onClusterClick(@NonNull Cluster<SampleClusterItem> cluster) {
                            Log.d(TAG, "onClusterClick");
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(cluster.getLatitude(), cluster.getLongitude()), (float) Math.floor(mMap.getCameraPosition().zoom + 1)), null);
                            return true;
                        }

                        @Override
                        public boolean onClusterItemClick(@NonNull SampleClusterItem clusterItem) {
                            Log.d(TAG, "onClusterItemClick");
                            return false;
                        }
                    });


                    for (AllCarWashResponse coordinate : carWashList) {
                        clusterItems.add(new SampleClusterItem(new LatLng(coordinate.getLatitude(), coordinate.getLongitude()), coordinate.getName(), coordinate.getId()));
                    }
                    clusterManager.setItems(clusterItems);
                    mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(MapsActivity.this));
                    mMap.setBuildingsEnabled(true);
                    mMap.getUiSettings().setMapToolbarEnabled(true);
                    mMap.getUiSettings().setMyLocationButtonEnabled(true);
                    mMap.getUiSettings().setAllGesturesEnabled(true);
                    updateLocationUI();
                    getDeviceLocation();
                } else {
                    if (response.code() == 204) {
                        mMap.clear();
                        ClusterManager<SampleClusterItem> clusterManager = new ClusterManager<>(MapsActivity.this, mMap);
                        List<SampleClusterItem> clusterItems = new ArrayList<>();
                        clusterManager.setItems(clusterItems);
                        mMap.setBuildingsEnabled(true);
                        mMap.getUiSettings().setMapToolbarEnabled(true);
                        mMap.getUiSettings().setMyLocationButtonEnabled(true);
                        mMap.getUiSettings().setAllGesturesEnabled(true);
                        updateLocationUI();
                        getDeviceLocation();
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
            public void onFailure(Call<List<AllCarWashResponse>> call, Throwable t) {
                errorHandler.showCustomError(t.getMessage());
            }
        });
    }


    private void getLocationPermission() {
        Log.d(TAG, "getLocationPermission: ");
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        } else {
            Log.d(TAG, "getLocation: permissions granted");
            mLocationPermissionGranted = true;
//            updateLocationUI();
            updateLocationUI();
            getDeviceLocation();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    updateLocationUI();
                    getDeviceLocation();
                } else {
//                    Toast.makeText(this, "FAIL", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void updateLocationUI() {
        Log.d(TAG, "updateLocationUI: ");
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
        Log.d(TAG, "getDeviceLocation: ");
        try {
            if (mLocationPermissionGranted) {
                Task locationResult = mFusedLocationProviderClient.getLastLocation();
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
    protected void onResume() {
        super.onResume();
        mapView.onResume();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.filter_menu, menu);
//        BubbleShowCaseBuilder first =  new BubbleShowCaseBuilder(this) //Activity instance
//                .title("Заказать мойку")//Any title for the bubble view
//                .targetView(menu.getItem(0).); //View to point out
//
//        new BubbleShowCaseSequence()
//                .addShowCase(first)
//                .show();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.filter_menu:
                NDialog nDialog = new NDialog(MapsActivity.this, ButtonType.NO_BUTTON);
                nDialog.setTitle("Фильтр");
                nDialog.setMessage("Накликай фильтров");
//                ButtonClickListener buttonClickListener = new ButtonClickListener() {
//                    @Override
//                    public void onClick(int button) {
//                        switch (button) {
//                            case NDialog.BUTTON_POSITIVE:
//                                FilterCarWashBody filterCarWashBody = new FilterCarWashBody();
//                                filterCarWashBody.setTargetId(FastSave.getInstance().getString(CAR_ID, null));
//                                filterCarWashBody.setServiceType(SERVICE_TYPE);
//                                filterCarWashBody.setServiceIds(new ArrayList<>(serviceIdList));
//                                getAllCarWash(filterCarWashBody);
//                                break;
//                        }
//                    }
//                };
//                nDialog.setPositiveButtonText("Применить фильтр");
//                nDialog.setPositiveButtonTextColor(Color.BLUE);
//                nDialog.setPositiveButtonOnClickDismiss(true); // default : true
//                nDialog.setPositiveButtonClickListener(buttonClickListener);

                nDialog.isCancelable(true);

                nDialog.setCustomView(R.layout.service_chip_view);

                List<View> childViews = nDialog.getCustomViewChildren();
                for (View childView : childViews) {
                    switch (childView.getId()) {
                        case R.id.timeOrderBtn:
                            MaterialButton okBtn = childView.findViewById(R.id.timeOrderBtn);
                            okBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    FilterCarWashBody filterCarWashBody = new FilterCarWashBody();
                                    filterCarWashBody.setTargetId(FastSave.getInstance().getString(CAR_ID, null));
                                    filterCarWashBody.setServiceType(SERVICE_TYPE);
                                    filterCarWashBody.setServiceIds(new ArrayList<>(serviceIdList));
                                    getAllCarWash(filterCarWashBody);
                                    nDialog.dismiss();
                                }
                            });
                            break;
                        case R.id.serviceGroup:
                            LinearLayout checkGroup = childView.findViewById(R.id.serviceGroup);
                            for (int i = 0; i < mapServise.size(); i++) {
                                CheckBox checkBox = new CheckBox(MapsActivity.this);
                                checkBox.setText(serviceList.get(i).getName());
                                checkBox.setOnCheckedChangeListener(this);
                                if (serviceIdList.contains(mapServise.get(serviceList.get(i).getName()))) {
                                    checkBox.setChecked(true);
                                }
                                checkGroup.addView(checkBox);
                            }
                            break;
                    }
                }

// SHOW DIALOG
                nDialog.show();


                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getAllService() {
        Log.d(TAG, "getAllService: ");
        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<List<ServiceResponse>> call = apiInterface.getAllService();
        call.enqueue(new Callback<List<ServiceResponse>>() {
            @Override
            public void onResponse(Call<List<ServiceResponse>> call, Response<List<ServiceResponse>> response) {
                if (response.code() == 200) {
                    serviceList = response.body();
                    for (int i = 0; i < serviceList.size(); i++) {
                        mapServise.put(serviceList.get(i).getName(), serviceList.get(i).getId());
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
            public void onFailure(Call<List<ServiceResponse>> call, Throwable t) {
                errorHandler.showCustomError(t.getMessage());
            }
        });
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            Log.d(TAG, "onCheckedChanged: TRUE");
            serviceIdList.add(mapServise.get(buttonView.getText().toString()));
            Log.d(TAG, "onCheckedChanged: " + serviceIdList.size());
        } else {
            Log.d(TAG, "onCheckedChanged: FALSE");
            serviceIdList.remove(mapServise.get(buttonView.getText().toString()));
            Log.d(TAG, "onCheckedChanged: " + serviceIdList.size());
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Пожалуйста, нажмите НАЗАД еще раз, чтобы выйти", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}
