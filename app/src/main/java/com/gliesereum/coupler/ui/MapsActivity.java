package com.gliesereum.coupler.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.transition.Slide;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ferfalk.simplesearchview.SimpleSearchView;
import com.gliesereum.coupler.R;
import com.gliesereum.coupler.SampleClusterItem;
import com.gliesereum.coupler.adapter.CustomInfoWindowAdapter;
import com.gliesereum.coupler.adapter.PointListAdapter;
import com.gliesereum.coupler.adapter.customadapterrecycleview.listener.OnClickItemListener;
import com.gliesereum.coupler.data.network.APIClient;
import com.gliesereum.coupler.data.network.APIInterface;
import com.gliesereum.coupler.data.network.CustomCallback;
import com.gliesereum.coupler.data.network.json.bonus.BonusScoreResponse;
import com.gliesereum.coupler.data.network.json.car.AllCarResponse;
import com.gliesereum.coupler.data.network.json.carwash.FilterCarWashBody;
import com.gliesereum.coupler.data.network.json.carwashnew.CarWashResponse;
import com.gliesereum.coupler.data.network.json.service.ServiceResponse;
import com.gliesereum.coupler.service.MyFirebaseMessagingService;
import com.gliesereum.coupler.util.FastSave;
import com.gliesereum.coupler.util.IconGenerator;
import com.gliesereum.coupler.util.Util;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.labters.lottiealertdialoglibrary.DialogTypes;
import com.labters.lottiealertdialoglibrary.LottieAlertDialog;
import com.savvyapps.togglebuttonlayout.Toggle;
import com.savvyapps.togglebuttonlayout.ToggleButtonLayout;

import net.sharewire.googlemapsclustering.Cluster;
import net.sharewire.googlemapsclustering.ClusterManager;
import net.sharewire.googlemapsclustering.IconStyle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import kotlin.Unit;
import kotlin.jvm.functions.Function3;
import retrofit2.Call;
import retrofit2.Response;
import za.co.riggaroo.materialhelptutorial.TutorialItem;

import static com.gliesereum.coupler.util.Constants.ACCESS_TOKEN;
import static com.gliesereum.coupler.util.Constants.BUSINESS_CATEGORY_ID;
import static com.gliesereum.coupler.util.Constants.BUSINESS_CODE;
import static com.gliesereum.coupler.util.Constants.BUSINESS_TYPE;
import static com.gliesereum.coupler.util.Constants.CARWASH_ID;
import static com.gliesereum.coupler.util.Constants.CAR_BRAND;
import static com.gliesereum.coupler.util.Constants.CAR_FILTER_LIST;
import static com.gliesereum.coupler.util.Constants.CAR_ID;
import static com.gliesereum.coupler.util.Constants.CAR_MODEL;
import static com.gliesereum.coupler.util.Constants.CAR_SERVICE_CLASS;
import static com.gliesereum.coupler.util.Constants.FILTER_CARWASH_BODY;
import static com.gliesereum.coupler.util.Constants.FIRST_START;
import static com.gliesereum.coupler.util.Constants.IS_LOGIN;
import static com.gliesereum.coupler.util.Constants.MAP_LIST;
import static com.gliesereum.coupler.util.Constants.MARKER_LIST;
import static com.gliesereum.coupler.util.Constants.MARKER_LOGO;
import static com.gliesereum.coupler.util.Constants.NEED_LOGIN_USER;
import static com.gliesereum.coupler.util.Constants.REF_SCORE;
import static com.gliesereum.coupler.util.Constants.SERVICE_LIST;
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
    private List<CarWashResponse> carWashListNew;
    private List<CarWashResponse> carWashListCache;
    private APIInterface API;
    private CustomCallback customCallback;
    private List<ServiceResponse> serviceList;
    private Map<String, String> mapServise;
    private Set<String> serviceIdList;
    private boolean doubleBack = false;
    private LottieAlertDialog alertDialog;
    private ImageView couplerLogoImg;
    private ToggleButtonLayout toggleButtonLayout;
    private RecyclerView recyclerView;
    PointListAdapter pointListAdapter;
    private SimpleSearchView searchView;
    private ImageView searchImg;
    private FilterCarWashBody searchBody;
    private Timer timer;
    private TextView emptyLabelSearch;
    ClusterManager<SampleClusterItem> clusterManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        setupWindowAnimations();
        initData();
        initView();

        if (FastSave.getInstance().getString(BUSINESS_CODE, "").equals("")) {
            startActivity(new Intent(MapsActivity.this, ChooseServiceNewActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
            finish();
        }

//        firstStartNotify();
        initMap(savedInstanceState);
        getLocationPermission();
        getAllService();
        if (FastSave.getInstance().getBoolean(IS_LOGIN, false)) {
            getBonusScore();
            if (FastSave.getInstance().getString(BUSINESS_TYPE, "").equals("CAR")) {
                getAllCars();
            }
        }
    }

    private ArrayList<TutorialItem> getTutorialItems(Context context) {
        TutorialItem tutorialItem1 = new TutorialItem("slide_1_african_story_books", "slide_1_african_story_books_subtitle",
                R.color.black, R.drawable.ic_icon_step_01, R.drawable.ic_icon_step_02);
        // You can also add gifs, [IN BETA YET] (because Glide is awesome!)
//        TutorialItem tutorialItem1 = new TutorialItem(context.getString(R.string.slide_1_african_story_books), context.getString(R.string.slide_1_african_story_books_subtitle),
//                R.color.slide_1, R.drawable.gif_drawable, true);
        ArrayList<TutorialItem> tutorialItems = new ArrayList<>();
        tutorialItems.add(tutorialItem1);
        return tutorialItems;
    }

    private void getBonusScore() {
        API.getBonusScore(FastSave.getInstance().getString(ACCESS_TOKEN, ""))
                .enqueue(customCallback.getResponse(new CustomCallback.ResponseCallback<BonusScoreResponse>() {
                    @Override
                    public void onSuccessful(Call<BonusScoreResponse> call, Response<BonusScoreResponse> response) {
                        Log.d(TAG, "onSuccessful: " + response.body().getScore());
                        FastSave.getInstance().saveInt(REF_SCORE, response.body().getScore());
                    }

                    @Override
                    public void onEmpty(Call<BonusScoreResponse> call, Response<BonusScoreResponse> response) {

                    }
                }));

    }

    private void initData() {
        FastSave.init(getApplicationContext());
        FastSave.getInstance().saveBoolean(NEED_LOGIN_USER, false);
        FastSave.getInstance().saveBoolean(MAP_LIST, false);
        API = APIClient.getClient().create(APIInterface.class);
        customCallback = new CustomCallback(this, this);
        serviceIdList = new HashSet<>();
        mapServise = new HashMap<>();
        serviceList = new ArrayList<>();
        mDefaultLocation = new LatLng(50, 30);
        if (FastSave.getInstance().getBoolean(IS_LOGIN, false)) {
            startService(new Intent(this, MyFirebaseMessagingService.class));
        }
        if (FastSave.getInstance().getObjectsList(MARKER_LIST, CarWashResponse.class) != null) {
            carWashListCache = FastSave.getInstance().getObjectsList(MARKER_LIST, CarWashResponse.class);
        } else {
            carWashListCache = new ArrayList<>();
        }
    }

    private void initView() {
        couplerLogoImg = findViewById(R.id.couplerLogoImg);
        mapView = findViewById(R.id.mapView);
        toolbar = findViewById(R.id.toolbar);
        couplerLogoImg.setVisibility(View.VISIBLE);
        toolbar.setTitle("");
        toolbar.setSubtitle("");
        setSupportActionBar(toolbar);
        new Util(this, toolbar, 1).addNavigation();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        pointListAdapter = new PointListAdapter(this);
        pointListAdapter.endLessScrolled(recyclerView);
        recyclerView.setAdapter(pointListAdapter);

        pointListAdapter.setOnClickItemListener(recyclerView, new OnClickItemListener<CarWashResponse>() {
            @Override
            public void onClickItem(int position, CarWashResponse element) {
                if (element != null) {
                    FastSave.getInstance().saveString(CARWASH_ID, element.getId());
                    startActivity(new Intent(MapsActivity.this, CarWashActivity.class));
                }
            }

            @Override
            public void onLongClickItem(int position, CarWashResponse element) {

            }
        });

        searchView = findViewById(R.id.searchView);
        searchImg = findViewById(R.id.searchImg);
        searchImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchView.showSearch();
            }
        });

        searchView.setOnQueryTextListener(new SimpleSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("SimpleSearchView", "Submit:" + query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (timer != null) {
                    timer.cancel();
                }
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (FastSave.getInstance().getObject(FILTER_CARWASH_BODY, FilterCarWashBody.class) != null) {
                            searchBody = FastSave.getInstance().getObject(FILTER_CARWASH_BODY, FilterCarWashBody.class);
                        } else {
                            searchBody = new FilterCarWashBody(FastSave.getInstance().getString(BUSINESS_CATEGORY_ID, ""));
                        }
                        if (newText != null) {
                            searchBody.setFullTextQuery(newText);
                        }
                        Log.d("SimpleSearchView", "Text changed:" + newText);
                        getAllCarWash(searchBody, true);
                    }
                }, 250);
                return false;
            }

            @Override
            public boolean onQueryTextCleared() {
                Log.d("SimpleSearchView", "Text cleared");
                return false;
            }
        });

        searchView.setOnSearchViewListener(new SimpleSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                Log.d("SimpleSearchView", "onSearchViewShown");
                toggleButtonLayout.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onSearchViewClosed() {
                Log.d("SimpleSearchView", "onSearchViewClosed");
                searchBody.setFullTextQuery(null);
                toggleButtonLayout.setVisibility(View.VISIBLE);
                searchView.setVisibility(View.INVISIBLE);
                hideKeyboard(MapsActivity.this);
            }

            @Override
            public void onSearchViewShownAnimation() {
                Log.d("SimpleSearchView", "onSearchViewShownAnimation");
            }

            @Override
            public void onSearchViewClosedAnimation() {
                Log.d("SimpleSearchView", "onSearchViewClosedAnimation");
            }
        });

        toggleButtonLayout = findViewById(R.id.toggle_button_layout);
        toggleButtonLayout.setToggled(R.id.toggle_left, true);
        toggleButtonLayout.setOnToggledListener(new Function3<ToggleButtonLayout, Toggle, Boolean, Unit>() {
            @Override
            public Unit invoke(ToggleButtonLayout toggleButtonLayout, Toggle toggle, Boolean aBoolean) {
                if (toggle.getId() == R.id.toggle_left) {
                    FastSave.getInstance().saveBoolean(MAP_LIST, false);
                    toggleButtonLayout.setToggled(R.id.toggle_left, true);
                    updateLocationUI();
                    mapView.onResume();
                    mapView.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    searchImg.setVisibility(View.GONE);
                    emptyLabelSearch.setVisibility(View.GONE);
                }
                if (toggle.getId() == R.id.toggle_right) {
                    FastSave.getInstance().saveBoolean(MAP_LIST, true);
                    toggleButtonLayout.setToggled(R.id.toggle_right, true);
                    recyclerView.setVisibility(View.VISIBLE);
                    searchImg.setVisibility(View.VISIBLE);
                    mapView.setVisibility(View.GONE);
                    if (carWashListNew == null || carWashListCache.isEmpty()) {
                        emptyLabelSearch.setVisibility(View.VISIBLE);
                    }
                }
                return null;
            }
        });
//        if (FastSave.getInstance().getBoolean(MAP_LIST, false)) {
//            toggleButtonLayout.setToggled(R.id.toggle_right, true);
//        } else {
//            toggleButtonLayout.setToggled(R.id.toggle_left, true);
//        }

        emptyLabelSearch = findViewById(R.id.emptyLabelSearch);
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void firstStartNotify() {
        if (FastSave.getInstance().getBoolean(FIRST_START, true)) {
            alertDialog = new LottieAlertDialog.Builder(this, DialogTypes.TYPE_SUCCESS)
                    .setTitle("Тестовый режим")
                    .setDescription("В данный момент интерактивная карта запущена в тестовом режиме. Услуги указанных компаний недоступны. Мы работаем над тем, чтобы как можно скорее наполнить карту нужными вам сервисами.")
                    .setPositiveText("Спасибо, мне понятно ")
                    .setPositiveListener(lottieAlertDialog -> {
                        FastSave.getInstance().saveBoolean(FIRST_START, false);
                        alertDialog.dismiss();
                    })
                    .build();
            alertDialog.setCancelable(false);
            alertDialog.show();
        }
    }

    private void setupWindowAnimations() {
        Slide slide = new Slide();
        slide.setDuration(1000);
        getWindow().setExitTransition(slide);
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
            boolean success = googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.coupler_map));
            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }
        mMap.clear();
        List<SampleClusterItem> clusterItems = new ArrayList<>();
        clusterManager = new ClusterManager<>(MapsActivity.this, mMap);
//        ClusterManager<SampleClusterItem> clusterManager = new ClusterManager<>(MapsActivity.this, mMap);
        IconStyle.Builder stilo = new IconStyle.Builder(MapsActivity.this);
        stilo.setClusterBackgroundColor(getResources().getColor(R.color.primary));
        stilo.setClusterTextColor(getResources().getColor(R.color.white));
        IconGenerator iconGenerator = new IconGenerator(MapsActivity.this);
        iconGenerator.setIconStyle(stilo.build());
        clusterManager.setIconGenerator(iconGenerator);
        clusterManager.setMinClusterSize(4);
        mMap.setOnCameraIdleListener(clusterManager);
        clusterManager.setCallbacks(new ClusterManager.Callbacks<SampleClusterItem>() {
            @Override
            public boolean onClusterClick(@NonNull Cluster<SampleClusterItem> cluster) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(cluster.getLatitude(), cluster.getLongitude()), (float) Math.floor(mMap.getCameraPosition().zoom + 1)), null);
                return true;
            }

            @Override
            public boolean onClusterItemClick(@NonNull SampleClusterItem clusterItem) {
                return false;
            }
        });
        for (CarWashResponse coordinate : carWashListCache) {
            clusterItems.add(new SampleClusterItem(new LatLng(coordinate.getLatitude(), coordinate.getLongitude()), coordinate.getName(), coordinate.getId()));
        }
        clusterManager.setItems(clusterItems);
        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(MapsActivity.this));
        mMap.getUiSettings().setMapToolbarEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);
        updateLocationUI();
        getDeviceLocation();
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                FastSave.getInstance().saveString(CARWASH_ID, marker.getSnippet());
                startActivity(new Intent(MapsActivity.this, CarWashActivity.class));
            }
        });
        if (FastSave.getInstance().getObject(FILTER_CARWASH_BODY, FilterCarWashBody.class) != null) {
            getAllCarWash(FastSave.getInstance().getObject(FILTER_CARWASH_BODY, FilterCarWashBody.class), false);
        } else {
            getAllCarWash(new FilterCarWashBody(FastSave.getInstance().getString(BUSINESS_CATEGORY_ID, "")), false);
        }
    }

    private void getAllCars() {
        if (!FastSave.getInstance().getString(ACCESS_TOKEN, "").equals("")) {
            API.getAllCars(FastSave.getInstance().getString(ACCESS_TOKEN, ""))
                    .enqueue(customCallback.getResponse(new CustomCallback.ResponseCallback<List<AllCarResponse>>() {
                                @Override
                                public void onSuccessful(Call<List<AllCarResponse>> call, Response<List<AllCarResponse>> response) {
                                    FastSave.getInstance().deleteValue(CAR_ID);
                                    List<AllCarResponse> body = response.body();
                                    for (int i = 0; i < response.body().size(); i++) {
                                        if (response.body().get(i).isFavorite()) {
                                            FastSave.getInstance().saveString(CAR_ID, response.body().get(i).getId());
                                            FastSave.getInstance().saveString(CAR_BRAND, response.body().get(i).getBrand().getName());
                                            FastSave.getInstance().saveString(CAR_MODEL, response.body().get(i).getModel().getName());
                                            FastSave.getInstance().saveObject(CAR_SERVICE_CLASS, response.body().get(i).getServices());
                                            FastSave.getInstance().saveObjectsList(CAR_FILTER_LIST, response.body().get(i).getAttributes());
                                        }
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

    public void getAllCarWash(FilterCarWashBody filterCarWashBody, boolean searchFlag) {
        FilterCarWashBody verifyFilterCarWashBody = new FilterCarWashBody(filterCarWashBody, true);
        FilterCarWashBody notVerifyFilterCarWashBody = new FilterCarWashBody(filterCarWashBody, false);
        verifyFilterCarWashBody.setBusinessVerify(true);
        notVerifyFilterCarWashBody.setBusinessVerify(false);
        API.getAllCarWashNew(verifyFilterCarWashBody)
                .enqueue(customCallback.getResponse(new CustomCallback.ResponseCallback<List<CarWashResponse>>() {
                    @Override
                    public void onSuccessful(Call<List<CarWashResponse>> call, Response<List<CarWashResponse>> response) {
                        pointListAdapter.removeAll();
                        pointListAdapter.addItems(response.body());
                        if (FastSave.getInstance().getBoolean(MAP_LIST, false)) {
                            recyclerView.setVisibility(View.VISIBLE);
                        }
                        emptyLabelSearch.setVisibility(View.GONE);
                        if (!searchFlag) {
                            carWashListNew = response.body();
                            Log.d(TAG, "onSuccessful: " + carWashListCache.equals(response.body()));
                            if (!carWashListCache.equals(carWashListNew)) {
                                Map<String, String> markersLogo = new HashMap<>();
                                for (int i = 0; i < carWashListNew.size(); i++) {
                                    if (carWashListNew.get(i).getId() != null && carWashListNew.get(i).getLogoUrl() != null) {
                                        markersLogo.put(carWashListNew.get(i).getId(), carWashListNew.get(i).getLogoUrl());
                                    }
                                }
                                FastSave.getInstance().saveObject(MARKER_LOGO, markersLogo);
                                FastSave.getInstance().saveObjectsList(MARKER_LIST, carWashListNew);
                                carWashListCache = carWashListNew;
                                mMap.clear();
                                List<SampleClusterItem> clusterItems = new ArrayList<>();
                                clusterManager = new ClusterManager<>(MapsActivity.this, mMap);
//                                ClusterManager<SampleClusterItem> clusterManager = new ClusterManager<>(MapsActivity.this, mMap);
                                IconStyle.Builder stilo = new IconStyle.Builder(MapsActivity.this);
                                stilo.setClusterBackgroundColor(getResources().getColor(R.color.primary));
                                stilo.setClusterTextColor(getResources().getColor(R.color.white));
                                IconGenerator iconGenerator = new IconGenerator(MapsActivity.this);
                                iconGenerator.setIconStyle(stilo.build());
                                clusterManager.setIconGenerator(iconGenerator);
                                clusterManager.setMinClusterSize(4);
                                mMap.setOnCameraIdleListener(clusterManager);
                                clusterManager.setCallbacks(new ClusterManager.Callbacks<SampleClusterItem>() {
                                    @Override
                                    public boolean onClusterClick(@NonNull Cluster<SampleClusterItem> cluster) {
                                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(cluster.getLatitude(), cluster.getLongitude()), (float) Math.floor(mMap.getCameraPosition().zoom + 1)), null);
                                        return true;
                                    }

                                    @Override
                                    public boolean onClusterItemClick(@NonNull SampleClusterItem clusterItem) {
                                        return false;
                                    }
                                });
                                for (CarWashResponse coordinate : carWashListNew) {
                                    clusterItems.add(new SampleClusterItem(new LatLng(coordinate.getLatitude(), coordinate.getLongitude()), coordinate.getName(), coordinate.getId()));
                                }
                                clusterManager.setItems(clusterItems);
                                mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(MapsActivity.this));
                                mMap.getUiSettings().setMapToolbarEnabled(true);
                                mMap.getUiSettings().setMyLocationButtonEnabled(true);
                                mMap.getUiSettings().setAllGesturesEnabled(true);
                                updateLocationUI();
                                getDeviceLocation();
                            }
                        } else {
                            emptyLabelSearch.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                        }


                        API.getAllCarWashNew(notVerifyFilterCarWashBody)
                                .enqueue(customCallback.getResponse(new CustomCallback.ResponseCallback<List<CarWashResponse>>() {
                                    @Override
                                    public void onSuccessful(Call<List<CarWashResponse>> call, Response<List<CarWashResponse>> response) {
//                                        mMap.clear();
                                        Drawable background;
                                        background = ContextCompat.getDrawable(MapsActivity.this, R.drawable.ic_new_pin_others);
                                        background.setBounds(0, 0, background.getIntrinsicWidth(), background.getIntrinsicHeight());
                                        Bitmap bitmap = Bitmap.createBitmap(background.getIntrinsicWidth(), background.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                                        Canvas canvas = new Canvas(bitmap);
                                        background.draw(canvas);

                                        for (int i = 0; i < response.body().size(); i++) {
                                            MarkerOptions markerOptions = new MarkerOptions();
                                            markerOptions.position(new LatLng(response.body().get(i).getLatitude(), response.body().get(i).getLongitude()));
                                            markerOptions.title(response.body().get(i).getName());
                                            markerOptions.snippet(response.body().get(i).getId());
                                            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
                                            mMap.addMarker(markerOptions);
                                        }

                                    }

                                    @Override
                                    public void onEmpty(Call<List<CarWashResponse>> call, Response<List<CarWashResponse>> response) {

                                    }
                                }));


                    }

                    @Override
                    public void onEmpty(Call<List<CarWashResponse>> call, Response<List<CarWashResponse>> response) {
                        carWashListCache.clear();
                        if (!searchFlag) {
                            Log.d(TAG, "onEmpty: map clear");
                            mMap.clear();
//                            ClusterManager<SampleClusterItem> clusterManager = new ClusterManager<>(MapsActivity.this, mMap);
                            List<SampleClusterItem> clusterItems = new ArrayList<>();
                            clusterManager.setItems(clusterItems);
                            mMap.setBuildingsEnabled(true);
                            mMap.getUiSettings().setMapToolbarEnabled(true);
                            mMap.getUiSettings().setMyLocationButtonEnabled(true);
                            mMap.getUiSettings().setAllGesturesEnabled(true);
                            updateLocationUI();
                            getDeviceLocation();
                        } else {
                            recyclerView.setVisibility(View.GONE);
                            emptyLabelSearch.setVisibility(View.VISIBLE);
                        }
                        if (FastSave.getInstance().getBoolean(MAP_LIST, false)) {
                            pointListAdapter.removeAll();
                            recyclerView.setVisibility(View.GONE);
                            emptyLabelSearch.setVisibility(View.VISIBLE);
                        }
                    }
                }));
    }

    private Collection<CarWashResponse> compareCarWashList(Response<List<CarWashResponse>> response) {
        carWashListNew = response.body();
        Collection<CarWashResponse> similar = new HashSet<CarWashResponse>(carWashListCache);
        Collection<CarWashResponse> different = new HashSet<CarWashResponse>(carWashListCache);
        different.addAll(carWashListNew);
        similar.retainAll(carWashListNew);
        different.removeAll(similar);
        return different;
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
        hideKeyboard(MapsActivity.this);
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
        getMenuInflater().inflate(R.menu.filter_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.filter_menu:
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
        if (searchView.onBackPressed()) {
            return;
        }
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

//    @Override
//    protected void onPostResume() {
//        super.onPostResume();
//        Log.d("serviceIdList", "onPostResume: ");
//        if (FastSave.getInstance().getBoolean(UPDATE_MAP, false)) {
//            if (FastSave.getInstance().getObject(FILTER_CARWASH_BODY, FilterCarWashBody.class) != null) {
//                getAllCarWash(FastSave.getInstance().getObject(FILTER_CARWASH_BODY, FilterCarWashBody.class), false);
//            } else {
//                getAllCarWash(new FilterCarWashBody(FastSave.getInstance().getString(BUSINESS_CATEGORY_ID, "")), false);
//            }
//            FastSave.getInstance().deleteValue(UPDATE_MAP);
//        }
//    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
        new Util(this, toolbar, 1).addNavigation();
        if (FastSave.getInstance().getBoolean(UPDATE_MAP, false)) {
            if (FastSave.getInstance().getObject(FILTER_CARWASH_BODY, FilterCarWashBody.class) != null) {
                getAllCarWash(FastSave.getInstance().getObject(FILTER_CARWASH_BODY, FilterCarWashBody.class), false);
            } else {
                getAllCarWash(new FilterCarWashBody(FastSave.getInstance().getString(BUSINESS_CATEGORY_ID, "")), false);
            }
            FastSave.getInstance().deleteValue(UPDATE_MAP);
        }
    }


}
