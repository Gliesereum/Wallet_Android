package com.gliesereum.coupler.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.transition.Slide;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.gliesereum.coupler.MyFirebaseMessagingService;
import com.gliesereum.coupler.R;
import com.gliesereum.coupler.SampleClusterItem;
import com.gliesereum.coupler.adapter.CustomInfoWindowAdapter;
import com.gliesereum.coupler.data.network.APIClient;
import com.gliesereum.coupler.data.network.APIInterface;
import com.gliesereum.coupler.data.network.CustomCallback;
import com.gliesereum.coupler.data.network.json.car.AllCarResponse;
import com.gliesereum.coupler.data.network.json.carwash.AllCarWashResponse;
import com.gliesereum.coupler.data.network.json.carwash.FilterCarWashBody;
import com.gliesereum.coupler.data.network.json.carwashnew.CarWashResponse;
import com.gliesereum.coupler.data.network.json.service.ServiceResponse;
import com.gliesereum.coupler.util.FastSave;
import com.gliesereum.coupler.util.IconGenerator;
import com.gliesereum.coupler.util.Util;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.labters.lottiealertdialoglibrary.DialogTypes;
import com.labters.lottiealertdialoglibrary.LottieAlertDialog;

import net.sharewire.googlemapsclustering.Cluster;
import net.sharewire.googlemapsclustering.ClusterManager;
import net.sharewire.googlemapsclustering.IconStyle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Response;

import static com.gliesereum.coupler.util.Constants.ACCESS_TOKEN;
import static com.gliesereum.coupler.util.Constants.BUSINESS_CATEGORY_ID;
import static com.gliesereum.coupler.util.Constants.BUSINESS_CODE;
import static com.gliesereum.coupler.util.Constants.CARWASH_ID;
import static com.gliesereum.coupler.util.Constants.CAR_BRAND;
import static com.gliesereum.coupler.util.Constants.CAR_FILTER_LIST;
import static com.gliesereum.coupler.util.Constants.CAR_ID;
import static com.gliesereum.coupler.util.Constants.CAR_MODEL;
import static com.gliesereum.coupler.util.Constants.CAR_SERVICE_CLASS;
import static com.gliesereum.coupler.util.Constants.FILTER_CARWASH_BODY;
import static com.gliesereum.coupler.util.Constants.FIRST_START;
import static com.gliesereum.coupler.util.Constants.IS_LOGIN;
import static com.gliesereum.coupler.util.Constants.OPEN_SERVICE_FLAG;
import static com.gliesereum.coupler.util.Constants.SERVICE_LIST;
import static com.gliesereum.coupler.util.Constants.TEST_LOG;
import static com.gliesereum.coupler.util.Constants.UPDATE_MAP;

//import com.appizona.yehiahd.fastsave.FastSave;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, CompoundButton.OnCheckedChangeListener {

    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";
    private GoogleMap mMap;
    private boolean mLocationPermissionGranted;
    private String TAG = "TAG";
    private Location mLastKnownLocation;
    private LatLng mDefaultLocation;
    private FusedLocationProviderClient mFusedLocationClient;
    private Toolbar toolbar;
    private MapView mapView;
    private List<AllCarWashResponse> carWashList;
    private List<CarWashResponse> carWashListNew;
    private APIInterface API;
    private CustomCallback customCallback;
    private List<ServiceResponse> serviceList;
    private Map<String, String> mapServise;
    private Set<String> serviceIdList;
    private boolean doubleBack = false;
    private LottieAlertDialog alertDialog;
    private ImageView couplerLogoImg;

    private void initData() {
        FastSave.init(getApplicationContext());
        API = APIClient.getClient().create(APIInterface.class);
        customCallback = new CustomCallback(this, this);
        serviceIdList = new HashSet<>();
        mapServise = new HashMap<>();
        serviceList = new ArrayList<>();
        mDefaultLocation = new LatLng(50, 30);
        if (FastSave.getInstance().getBoolean(IS_LOGIN, false)) {
            startService(new Intent(this, MyFirebaseMessagingService.class));
            Log.d(TEST_LOG, "sendBroadcast: ");
//            sendBroadcast(new Intent(this, RestartServiceReceiver.class));
        }
    }

    private void firstStartNotify() {
        if (FastSave.getInstance().getBoolean(FIRST_START, true)) {
            alertDialog = new LottieAlertDialog.Builder(this, DialogTypes.TYPE_SUCCESS)
                    .setTitle("Тестовый режим")
                    .setDescription("Приложение работает в тестовом режиме." +
                            "Сейчас созданна одна ТЕСТОВАЯ мойка в городе Киев." +
                            "Вы можете уже сейчас попробовать записаться на мойку выбрав все интересущие вас услуги." +
                            "Также вы можете попробовать все остальные функции даного приложения." +
                            "А вообще тут надо придумать нормальный текст, что б люди поняли, что если они сделали заказ мойки, то не надо не нее ехать мыться)))")
                    .setPositiveText("Да! Я понял!")
                    .setPositiveListener(lottieAlertDialog -> {
                        FastSave.getInstance().saveBoolean(FIRST_START, false);
                        alertDialog.dismiss();
                    })
                    .build();
            alertDialog.setCancelable(false);
            alertDialog.show();
        }
    }

    private void initView() {
        couplerLogoImg = findViewById(R.id.couplerLogoImg);
        mapView = findViewById(R.id.mapView);
        toolbar = findViewById(R.id.toolbar);
        if (!FastSave.getInstance().getString(CAR_ID, "").equals("")) {
            if (FastSave.getInstance().getString(CAR_BRAND, "").equals("") || FastSave.getInstance().getString(CAR_MODEL, "").equals("")) {
                FastSave.getInstance().deleteValue(CAR_ID);
                couplerLogoImg.setVisibility(View.VISIBLE);
                toolbar.setTitle("");
                toolbar.setSubtitle("");
            } else {
                couplerLogoImg.setVisibility(View.GONE);
                toolbar.setTitle(FastSave.getInstance().getString(CAR_BRAND, "") + " " + FastSave.getInstance().getString(CAR_MODEL, ""));
                toolbar.setSubtitle("Выбранный автомобиль");
            }
        } else {
            FastSave.getInstance().deleteValue(CAR_ID);
            couplerLogoImg.setVisibility(View.VISIBLE);
            toolbar.setTitle("");
            toolbar.setSubtitle("");
        }
        setSupportActionBar(toolbar);
        new Util(this, toolbar, 1).addNavigation();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setupWindowAnimations();
        initData();
        initView();
//        firstStartNotify();
        initMap(savedInstanceState);
        getLocationPermission();
        getAllService();
        if (FastSave.getInstance().getBoolean(IS_LOGIN, false)) {
            getAllCars();
        }
    }

    private void setupWindowAnimations() {
        Slide slide = new Slide();
        slide.setDuration(1000);
        getWindow().setExitTransition(slide);
    }

    private void getAllCars() {
        if (!FastSave.getInstance().getString(ACCESS_TOKEN, "").equals("")) {
            API.getAllCars(FastSave.getInstance().getString(ACCESS_TOKEN, ""))
                    .enqueue(customCallback.getResponse(new CustomCallback.ResponseCallback<List<AllCarResponse>>() {
                                @Override
                                public void onSuccessful(Call<List<AllCarResponse>> call, Response<List<AllCarResponse>> response) {
                                    FastSave.getInstance().deleteValue(CAR_ID);
                                    for (int i = 0; i < response.body().size(); i++) {
                                        if (response.body().get(i).isFavorite()) {
                                            FastSave.getInstance().saveString(CAR_ID, response.body().get(i).getId());
                                            FastSave.getInstance().saveString(CAR_BRAND, response.body().get(i).getBrand().getName());
                                            FastSave.getInstance().saveString(CAR_MODEL, response.body().get(i).getModel().getName());
                                            FastSave.getInstance().saveObject(CAR_SERVICE_CLASS, response.body().get(i).getServices());
                                            FastSave.getInstance().saveObjectsList(CAR_FILTER_LIST, response.body().get(i).getAttributes());
                                        }
                                    }
                                    if (!FastSave.getInstance().getString(CAR_ID, "").equals("")) {
                                        if (FastSave.getInstance().getString(CAR_BRAND, "").equals("") || FastSave.getInstance().getString(CAR_MODEL, "").equals("")) {
                                            FastSave.getInstance().deleteValue(CAR_ID);
                                            couplerLogoImg.setVisibility(View.VISIBLE);
                                            toolbar.setTitle("");
                                            toolbar.setSubtitle("");
                                        } else {
                                            couplerLogoImg.setVisibility(View.GONE);
                                            toolbar.setTitle(FastSave.getInstance().getString(CAR_BRAND, "") + " " + FastSave.getInstance().getString(CAR_MODEL, ""));
                                            toolbar.setSubtitle("Выбранный автомобиль");
                                        }
                                    } else {
                                        FastSave.getInstance().deleteValue(CAR_ID);
                                        couplerLogoImg.setVisibility(View.VISIBLE);
                                        toolbar.setTitle("");
                                        toolbar.setSubtitle("");
                                    }

                                }

                                @Override
                                public void onEmpty(Call<List<AllCarResponse>> call, Response<List<AllCarResponse>> response) {
                                    FastSave.getInstance().deleteValue(CAR_ID);
                                }
                            })
                    );
        }
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.coupler_map));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }
        getAllCarWash(new FilterCarWashBody(FastSave.getInstance().getString(BUSINESS_CATEGORY_ID, "")));
//        getAllCarWash(new FilterCarWashBody());
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                FastSave.getInstance().saveString(CARWASH_ID, marker.getSnippet());
                if (FastSave.getInstance().getBoolean(IS_LOGIN, false)) {
                    if (!FastSave.getInstance().getString(CAR_ID, "").equals("")) {
                        FastSave.getInstance().saveBoolean(OPEN_SERVICE_FLAG, false);
                        FastSave.getInstance().saveString(CARWASH_ID, marker.getSnippet());
                        startActivity(new Intent(MapsActivity.this, CarWashActivity.class));
                    } else {
                        FastSave.getInstance().saveBoolean(OPEN_SERVICE_FLAG, true);
                        startActivity(new Intent(MapsActivity.this, CarListActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
                    }
                } else {
                    FastSave.getInstance().saveBoolean(OPEN_SERVICE_FLAG, true);
                    startActivity(new Intent(MapsActivity.this, LoginActivity.class));
                    finish();
                }

            }
        });
    }

//    private void getAllCarWash(FilterCarWashBody filterCarWashBody) {
//        API.getAllCarWash(filterCarWashBody)
//                .enqueue(customCallback.getResponse(new CustomCallback.ResponseCallback<List<AllCarWashResponse>>() {
//                    @Override
//                    public void onSuccessful(Call<List<AllCarWashResponse>> call, Response<List<AllCarWashResponse>> response) {
//                        carWashList = response.body();
//                        mMap.clear();
//                        List<SampleClusterItem> clusterItems = new ArrayList<>();
//                        ClusterManager<SampleClusterItem> clusterManager = new ClusterManager<>(MapsActivity.this, mMap);
//                        IconStyle.Builder stilo = new IconStyle.Builder(MapsActivity.this);
//                        stilo.setClusterBackgroundColor(getResources().getColor(R.color.primary));
//                        stilo.setClusterTextColor(getResources().getColor(R.color.white));
//                        IconGenerator iconGenerator = new IconGenerator(MapsActivity.this);
//                        iconGenerator.setIconStyle(stilo.build());
//                        clusterManager.setIconGenerator(iconGenerator);
//                        clusterManager.setMinClusterSize(3);
//                        mMap.setOnCameraIdleListener(clusterManager);
//                        clusterManager.setCallbacks(new ClusterManager.Callbacks<SampleClusterItem>() {
//                            @Override
//                            public boolean onClusterClick(@NonNull Cluster<SampleClusterItem> cluster) {
//                                Log.d(TAG, "onClusterClick");
//                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(cluster.getLatitude(), cluster.getLongitude()), (float) Math.floor(mMap.getCameraPosition().zoom + 1)), null);
//                                return true;
//                            }
//
//                            @Override
//                            public boolean onClusterItemClick(@NonNull SampleClusterItem clusterItem) {
//                                Log.d(TAG, "onClusterItemClick");
//                                return false;
//                            }
//                        });
//                        for (AllCarWashResponse coordinate : carWashList) {
//                            clusterItems.add(new SampleClusterItem(new LatLng(coordinate.getLatitude(), coordinate.getLongitude()), coordinate.getName(), coordinate.getId()));
//                        }
//                        clusterManager.setItems(clusterItems);
//                        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(MapsActivity.this));
//                        mMap.setBuildingsEnabled(false);
//                        mMap.getUiSettings().setMapToolbarEnabled(true);
//                        mMap.getUiSettings().setMyLocationButtonEnabled(true);
//                        mMap.getUiSettings().setAllGesturesEnabled(true);
//                        updateLocationUI();
//                        getDeviceLocation();
//                    }
//
//                    @Override
//                    public void onEmpty(Call<List<AllCarWashResponse>> call, Response<List<AllCarWashResponse>> response) {
//                        mMap.clear();
//                        ClusterManager<SampleClusterItem> clusterManager = new ClusterManager<>(MapsActivity.this, mMap);
//                        List<SampleClusterItem> clusterItems = new ArrayList<>();
//                        clusterManager.setItems(clusterItems);
//                        mMap.setBuildingsEnabled(true);
//                        mMap.getUiSettings().setMapToolbarEnabled(true);
//                        mMap.getUiSettings().setMyLocationButtonEnabled(true);
//                        mMap.getUiSettings().setAllGesturesEnabled(true);
//                        updateLocationUI();
//                        getDeviceLocation();
//                    }
//                }));
//    }

    public void getAllCarWash(FilterCarWashBody filterCarWashBody) {
        API.getAllCarWashNew(filterCarWashBody)
                .enqueue(customCallback.getResponse(new CustomCallback.ResponseCallback<List<CarWashResponse>>() {
                    @Override
                    public void onSuccessful(Call<List<CarWashResponse>> call, Response<List<CarWashResponse>> response) {
                        carWashListNew = response.body();
                        mMap.clear();
                        List<SampleClusterItem> clusterItems = new ArrayList<>();
                        ClusterManager<SampleClusterItem> clusterManager = new ClusterManager<>(MapsActivity.this, mMap);
                        IconStyle.Builder stilo = new IconStyle.Builder(MapsActivity.this);
                        stilo.setClusterBackgroundColor(getResources().getColor(R.color.primary));
                        stilo.setClusterTextColor(getResources().getColor(R.color.white));
                        IconGenerator iconGenerator = new IconGenerator(MapsActivity.this, FastSave.getInstance().getString(BUSINESS_CODE, ""));
                        iconGenerator.setIconStyle(stilo.build());
                        clusterManager.setIconGenerator(iconGenerator);
                        clusterManager.setMinClusterSize(3);
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
                        for (CarWashResponse coordinate : carWashListNew) {
                            clusterItems.add(new SampleClusterItem(new LatLng(coordinate.getLatitude(), coordinate.getLongitude()), coordinate.getName(), coordinate.getId()));
                        }
                        clusterManager.setItems(clusterItems);
                        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(MapsActivity.this));
//                        mMap.setBuildingsEnabled(false);
                        mMap.getUiSettings().setMapToolbarEnabled(true);
                        mMap.getUiSettings().setMyLocationButtonEnabled(true);
                        mMap.getUiSettings().setAllGesturesEnabled(true);
                        updateLocationUI();
                        getDeviceLocation();
                    }

                    @Override
                    public void onEmpty(Call<List<CarWashResponse>> call, Response<List<CarWashResponse>> response) {
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
                    }
                }));
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
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
//                NDialog nDialog = new NDialog(MapsActivity.this, ButtonType.NO_BUTTON);
//                nDialog.setTitle("Фильтр");
//                nDialog.setMessage("Накликай фильтров");
//                nDialog.isCancelable(true);
//                nDialog.setCustomView(R.layout.service_chip_view);
//                List<View> childViews = nDialog.getCustomViewChildren();
//                for (View childView : childViews) {
//                    switch (childView.getId()) {
//                        case R.id.timeOrderBtn:
//                            MaterialButton okBtn = childView.findViewById(R.id.timeOrderBtn);
//                            okBtn.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    FilterCarWashBody filterCarWashBody = new FilterCarWashBody();
//                                    filterCarWashBody.setTargetId(FastSave.getInstance().getString(CAR_ID, null));
//                                    filterCarWashBody.setBusinessCategoryId(FastSave.getInstance().getString(BUSINESS_CATEGORY_ID, ""));
//                                    filterCarWashBody.setServiceIds(new ArrayList<>(serviceIdList));
//                                    getAllCarWash(filterCarWashBody);
//                                    nDialog.dismiss();
//                                }
//                            });
//                            break;
//                        case R.id.serviceGroup:
//                            LinearLayout checkGroup = childView.findViewById(R.id.serviceGroup);
//                            for (int i = 0; i < mapServise.size(); i++) {
//                                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                                layoutParams.setMargins(0, 4, 0, 4);
//                                CheckBox checkBox = new CheckBox(MapsActivity.this);
//                                checkBox.setText(serviceList.get(i).getName());
//                                checkBox.setOnCheckedChangeListener(this);
//                                if (serviceIdList.contains(mapServise.get(serviceList.get(i).getName()))) {
//                                    checkBox.setChecked(true);
//                                }
//                                checkGroup.addView(checkBox);
//                            }
//                            break;
//                    }
//                }
//                nDialog.show();


                startActivity(new Intent(MapsActivity.this, TestActivity.class));
                overridePendingTransition(R.anim.enter, R.anim.no_animation);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getAllService() {
        API.getAllService(FastSave.getInstance().getString(BUSINESS_CATEGORY_ID, ""))
                .enqueue(customCallback.getResponse(new CustomCallback.ResponseCallback<List<ServiceResponse>>() {
                    @Override
                    public void onSuccessful(Call<List<ServiceResponse>> call, Response<List<ServiceResponse>> response) {
                        serviceList = response.body();
                        for (int i = 0; i < serviceList.size(); i++) {
                            mapServise.put(serviceList.get(i).getName(), serviceList.get(i).getId());
                        }
                        FastSave.getInstance().saveObjectsList(SERVICE_LIST, serviceList);
                    }

                    @Override
                    public void onEmpty(Call<List<ServiceResponse>> call, Response<List<ServiceResponse>> response) {

                    }
                }));
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            serviceIdList.add(mapServise.get(buttonView.getText().toString()));
        } else {
            serviceIdList.remove(mapServise.get(buttonView.getText().toString()));
        }
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
    protected void onPostResume() {
        super.onPostResume();
        Log.d("serviceIdList", "onPostResume: ");
        if (FastSave.getInstance().getBoolean(UPDATE_MAP, false)) {
            getAllCarWash(FastSave.getInstance().getObject(FILTER_CARWASH_BODY, FilterCarWashBody.class));
            FastSave.getInstance().deleteValue(UPDATE_MAP);
        }
    }
}
