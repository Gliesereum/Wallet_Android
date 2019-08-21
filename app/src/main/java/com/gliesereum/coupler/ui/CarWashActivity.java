package com.gliesereum.coupler.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gliesereum.coupler.R;
import com.gliesereum.coupler.adapter.CommentListAdapter;
import com.gliesereum.coupler.data.network.APIClient;
import com.gliesereum.coupler.data.network.APIInterface;
import com.gliesereum.coupler.data.network.CustomCallback;
import com.gliesereum.coupler.data.network.json.car.AllCarResponse;
import com.gliesereum.coupler.data.network.json.carwash.AllCarWashResponse;
import com.gliesereum.coupler.data.network.json.carwash.CommentsItem;
import com.gliesereum.coupler.data.network.json.carwash.MediaItem;
import com.gliesereum.coupler.data.network.json.carwash.PackagesItem;
import com.gliesereum.coupler.data.network.json.carwash.Rating;
import com.gliesereum.coupler.data.network.json.carwash.ServicePricesItem;
import com.gliesereum.coupler.data.network.json.carwash.WorkTimesItem;
import com.gliesereum.coupler.util.CircleTransform;
import com.gliesereum.coupler.util.FastSave;
import com.gliesereum.coupler.util.SmartRatingBar;
import com.gliesereum.coupler.util.Util;
import com.gohn.nativedialog.ButtonType;
import com.gohn.nativedialog.NDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.kodmap.app.library.PopopDialogBuilder;
import com.skydoves.powermenu.MenuAnimation;
import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;
import com.squareup.picasso.Picasso;
import com.willy.ratingbar.ScaleRatingBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hakobastvatsatryan.DropdownTextView;
import retrofit2.Call;
import retrofit2.Response;
import smartdevelop.ir.eram.showcaseviewlib.GuideView;
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType;
import smartdevelop.ir.eram.showcaseviewlib.listener.GuideListener;

import static com.gliesereum.coupler.util.Constants.ACCESS_TOKEN;
import static com.gliesereum.coupler.util.Constants.BUSINESS_TYPE;
import static com.gliesereum.coupler.util.Constants.CARWASH;
import static com.gliesereum.coupler.util.Constants.CARWASHA_CTIVITY;
import static com.gliesereum.coupler.util.Constants.CARWASH_ID;
import static com.gliesereum.coupler.util.Constants.CAR_BRAND;
import static com.gliesereum.coupler.util.Constants.CAR_FILTER_LIST;
import static com.gliesereum.coupler.util.Constants.CAR_ID;
import static com.gliesereum.coupler.util.Constants.CAR_MODEL;
import static com.gliesereum.coupler.util.Constants.CAR_SERVICE_CLASS;
import static com.gliesereum.coupler.util.Constants.FRIDAY;
import static com.gliesereum.coupler.util.Constants.IS_LOGIN;
import static com.gliesereum.coupler.util.Constants.MONDAY;
import static com.gliesereum.coupler.util.Constants.OPEN_SERVICE_FLAG;
import static com.gliesereum.coupler.util.Constants.SATURDAY;
import static com.gliesereum.coupler.util.Constants.SUNDAY;
import static com.gliesereum.coupler.util.Constants.THURSDAY;
import static com.gliesereum.coupler.util.Constants.TUESDAY;
import static com.gliesereum.coupler.util.Constants.WEDNESDAY;

//import com.gliesereum.coupler.GlideApp;

//import com.appizona.yehiahd.fastsave.FastSave;

public class CarWashActivity extends AppCompatActivity implements View.OnClickListener {

    private String carWashId;
    private AllCarWashResponse carWash;
    private APIInterface API;
    private CustomCallback customCallback;
    private PowerMenu packagePowerMenu;
    private PowerMenu workTimePowerMenu;
    private Map<String, String> customServiceMap;
    private Map<String, WorkTimesItem> workTimeMap;
    private Map<String, PackagesItem> packageMap;
    private Map<String, ServicePricesItem> servicePriceMap;
    private List<MediaItem> mediumList;
    private List<CommentsItem> commentsItemList;
    private ArrayList<String> mediaURLList;
    private TextInputEditText commentTextView;
    private CommentListAdapter commentListAdapter;
    private ScaleRatingBar scaleRatingBar;
    private Context context;
    private TextView name;
    private TextView address;
    private ImageView logoImageView;
    private ImageView workTimeImage;
    private HorizontalScrollView horizontalScrollView;
    private LinearLayout packageScroll;
    private DropdownTextView descriptionDropdown;
    private HorizontalScrollView photoScrollView;
    private LinearLayout photosViewSlider;
    private Button sendCommentBtn;
    private RecyclerView commentList;
    private MaterialButton orderButton;
    private SmartRatingBar carWashRating;
    private NDialog commentDialog;
    private TextView nowStatus;
    private Button connectBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_wash);
        initData();
        initView();
        getCarWash();
        if (FastSave.getInstance().getBoolean(IS_LOGIN, false) && FastSave.getInstance().getString(BUSINESS_TYPE, "").equals("CAR")) {
            getAllCars();
        }
    }

    private void initData() {
        FastSave.init(getApplicationContext());
        context = this;
        carWashId = FastSave.getInstance().getString(CARWASH_ID, "");
        API = APIClient.getClient().create(APIInterface.class);
        customCallback = new CustomCallback(this, this);
        customServiceMap = new HashMap<>();
        workTimeMap = new HashMap<>();
        packageMap = new HashMap<>();
        servicePriceMap = new HashMap<>();
        mediaURLList = new ArrayList<>();
        commentsItemList = new ArrayList<>();
        mediumList = new ArrayList<>();
        FastSave.getInstance().saveBoolean(OPEN_SERVICE_FLAG, false);
    }

    private void initView() {
        name = findViewById(R.id.name);
        address = findViewById(R.id.address);
        logoImageView = findViewById(R.id.logoImageView);
        workTimeImage = findViewById(R.id.workTimeImage);
        horizontalScrollView = findViewById(R.id.horizontalScrollView);
        packageScroll = findViewById(R.id.packageScroll);
        descriptionDropdown = findViewById(R.id.descriptionDropdown);
        photoScrollView = findViewById(R.id.photoScrollView);
        photosViewSlider = findViewById(R.id.photosViewSlider);
        sendCommentBtn = findViewById(R.id.writeCommentBtn);
        commentList = findViewById(R.id.commentList);
        orderButton = findViewById(R.id.orderButton);
        orderButton.setOnClickListener(this);
        workTimeImage.setOnClickListener(this);
        sendCommentBtn.setOnClickListener(this);
        carWashRating = findViewById(R.id.carWashRating);
        nowStatus = findViewById(R.id.nowStatus);
        if (!FastSave.getInstance().getBoolean(IS_LOGIN, false)) {
            sendCommentBtn.setVisibility(View.GONE);
        }
        connectBtn = findViewById(R.id.connectBtn);
        connectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:+" + carWash.getPhone()));//change the number
                startActivity(callIntent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.orderButton:
                carWash.setId(carWashId);
                FastSave.getInstance().saveObject(CARWASH, carWash);
                startActivity(new Intent(CarWashActivity.this, OrderActivity.class));
                break;
            case R.id.workTimeImage:
                workTimePowerMenu.showAsDropDown(workTimeImage);
                break;
            case R.id.writeCommentBtn:
                openCommentDialog();
                break;
        }
    }

    private void getCarWash() {
        API.getCarWashFull(FastSave.getInstance().getString(ACCESS_TOKEN, ""), carWashId)
                .enqueue(customCallback.getResponseWithProgress(new CustomCallback.ResponseCallback<AllCarWashResponse>() {
                            @Override
                            public void onSuccessful(Call<AllCarWashResponse> call, Response<AllCarWashResponse> response) {
                                carWash = response.body();
                                if (carWash != null) {
                                    if (carWash.getLogoUrl() != null) {
                                        Picasso.get().load(carWash.getLogoUrl()).transform(new CircleTransform()).into(logoImageView);
                                    }
                                    for (int i = 0; i < carWash.getPackages().size(); i++) {
                                        packageMap.put(carWash.getPackages().get(i).getId(), carWash.getPackages().get(i));
                                    }
                                }
                                if (carWash != null) {
                                    for (int i = 0; i < carWash.getServicePrices().size(); i++) {
                                        servicePriceMap.put(carWash.getServicePrices().get(i).getId(), carWash.getServicePrices().get(i));
                                    }
                                }
                                workTimeMap.clear();
                                if (response.body() != null) {
                                    for (int i = 0; i < response.body().getWorkTimes().size(); i++) {
                                        workTimeMap.put(response.body().getWorkTimes().get(i).getDayOfWeek(), response.body().getWorkTimes().get(i));
                                    }
                                }
                                name.setText(carWash.getName());
                                address.setText(carWash.getAddress());
                                if (carWash.getDescription() != null && !carWash.getDescription().equals("")) {
                                    descriptionDropdown.setContentText(carWash.getDescription());
                                } else {
                                    descriptionDropdown.setVisibility(View.GONE);
                                }
//                                if (carWash.getRating()!=null){
                                carWashRating.setRatingNum(carWash.getRating().getRating());
//                                }
                                if (Util.checkCarWashWorkTime(carWash)) {
                                    nowStatus.setText("работает");
                                    nowStatus.setTextColor(getResources().getColor(R.color.md_green_300));
                                } else {
                                    nowStatus.setText("закрыто");
                                    nowStatus.setTextColor(getResources().getColor(R.color.md_red_A200));
                                }
                                setWorkTime();
                                setPackages();
                                setPhotoSlider();

                                commentListAdapter = new CommentListAdapter(false, CarWashActivity.this);
                                commentsItemList.addAll(carWash.getComments());
                                commentListAdapter.setItems(commentsItemList);
                                commentList.setAdapter(commentListAdapter);
                                commentList.setLayoutManager(new LinearLayoutManager(CarWashActivity.this));

                                setCommentList();

                                for (int i = 0; i < carWash.getServicePrices().size(); i++) {
                                    if (carWash.getServicePrices().get(i).getName() != null) {
                                        customServiceMap.put(carWash.getServicePrices().get(i).getName(), carWash.getServicePrices().get(i).getServiceId());
                                    }
                                }
                                showTutorial();
                            }

                            @Override
                            public void onEmpty(Call<AllCarWashResponse> call, Response<AllCarWashResponse> response) {

                            }
                        })
                );
    }

    public void setCommentList() {
        if (FastSave.getInstance().getBoolean(IS_LOGIN, false)) {
            commentsItemList.clear();
            API.getMyComment(FastSave.getInstance().getString(ACCESS_TOKEN, ""), carWashId)
                    .enqueue(customCallback.getResponse(new CustomCallback.ResponseCallback<CommentsItem>() {
                        @Override
                        public void onSuccessful(Call<CommentsItem> call, Response<CommentsItem> response) {
                            commentListAdapter = new CommentListAdapter(true, CarWashActivity.this);
                            commentsItemList.add(response.body());
                            carWash.getComments().remove(response.body());
                            commentsItemList.addAll(carWash.getComments());
                            commentListAdapter.setItems(commentsItemList);
                            commentList.setAdapter(commentListAdapter);
                            commentList.setLayoutManager(new LinearLayoutManager(CarWashActivity.this));
                            sendCommentBtn.setVisibility(View.GONE);
                        }

                        @Override
                        public void onEmpty(Call<CommentsItem> call, Response<CommentsItem> response) {
                            commentListAdapter = new CommentListAdapter(false, CarWashActivity.this);
                            commentsItemList.addAll(carWash.getComments());
                            commentListAdapter.setItems(commentsItemList);
                            commentList.setAdapter(commentListAdapter);
                            commentList.setLayoutManager(new LinearLayoutManager(CarWashActivity.this));
                            sendCommentBtn.setVisibility(View.VISIBLE);
                        }
                    }));
        }

    }

    private void setPhotoSlider() {
        mediumList = carWash.getMedia();
        for (int i = 0; i < mediumList.size(); i++) {
            mediaURLList.add(mediumList.get(i).getUrl());
            View view = LayoutInflater.from(this).inflate(R.layout.image_view_for_slider, photoScrollView, false);
            ImageView imageView = view.findViewById(R.id.imageView6);
            imageView.setTag(i);
            Picasso.get().load(mediumList.get(i).getUrl()).fit().into(imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialog dialog = new PopopDialogBuilder(CarWashActivity.this)
                            .setList(mediaURLList, (Integer) v.getTag())
                            .setHeaderBackgroundColor(R.color.accent)
                            .setCloseDrawable(R.drawable.ic_close_white_24dp)
                            .showThumbSlider(true)
                            .setIsZoomable(true)
                            .build();
                    dialog.show();
                }
            });
            photosViewSlider.addView(view);
        }
        if (mediaURLList.size() != 0) {
            photoScrollView.setVisibility(View.VISIBLE);
        } else {
            photoScrollView.setVisibility(View.GONE);
        }
    }

    private void setWorkTime() {
        if (workTimeMap.size() > 0) {
            String monday, tuesday, wednesday, thursday, friday, saturday, sunday = "";
            if (workTimeMap.get(MONDAY) != null && workTimeMap.get(MONDAY).isIsWork()) {
                monday = "Пн: " + Util.getStringTime(workTimeMap.get(MONDAY).getFrom()) + "-" + Util.getStringTime(workTimeMap.get(MONDAY).getTo());
            } else {
                monday = "Пн: Не работает";
            }
            if (workTimeMap.get(TUESDAY) != null && workTimeMap.get(TUESDAY).isIsWork()) {
                tuesday = "Вт: " + Util.getStringTime(workTimeMap.get(TUESDAY).getFrom()) + "-" + Util.getStringTime(workTimeMap.get(TUESDAY).getTo());
            } else {
                tuesday = "Вт: Не работает";
            }
            if (workTimeMap.get(WEDNESDAY) != null && workTimeMap.get(WEDNESDAY).isIsWork()) {
                wednesday = "Ср: " + Util.getStringTime(workTimeMap.get(WEDNESDAY).getFrom()) + "-" + Util.getStringTime(workTimeMap.get(WEDNESDAY).getTo());
            } else {
                wednesday = "Ср: Не работает";
            }
            if (workTimeMap.get(THURSDAY) != null && workTimeMap.get(THURSDAY).isIsWork()) {
                thursday = "Чт: " + Util.getStringTime(workTimeMap.get(THURSDAY).getFrom()) + "-" + Util.getStringTime(workTimeMap.get(THURSDAY).getTo());
            } else {
                thursday = "Чт: Не работает";
            }
            if (workTimeMap.get(FRIDAY) != null && workTimeMap.get(FRIDAY).isIsWork()) {
                friday = "Пт: " + Util.getStringTime(workTimeMap.get(FRIDAY).getFrom()) + "-" + Util.getStringTime(workTimeMap.get(FRIDAY).getTo());
            } else {
                friday = "Пт: Не работает";
            }
            if (workTimeMap.get(SATURDAY) != null && workTimeMap.get(SATURDAY).isIsWork()) {
                saturday = "Сб: " + Util.getStringTime(workTimeMap.get(SATURDAY).getFrom()) + "-" + Util.getStringTime(workTimeMap.get(SATURDAY).getTo());
            } else {
                saturday = "Сб: Не работает";
            }
            if (workTimeMap.get(SUNDAY) != null && workTimeMap.get(SUNDAY).isIsWork()) {
                sunday = "Вс: " + Util.getStringTime(workTimeMap.get(SUNDAY).getFrom()) + "-" + Util.getStringTime(workTimeMap.get(SUNDAY).getTo());
            } else {
                sunday = "Вс: Не работает";
            }

            workTimePowerMenu = new PowerMenu.Builder(CarWashActivity.this)
                    .addItem(new PowerMenuItem(monday, false))
                    .addItem(new PowerMenuItem(tuesday, false))
                    .addItem(new PowerMenuItem(wednesday, false))
                    .addItem(new PowerMenuItem(thursday, false))
                    .addItem(new PowerMenuItem(friday, false))
                    .addItem(new PowerMenuItem(saturday, false))
                    .addItem(new PowerMenuItem(sunday, false))
                    .setAnimation(MenuAnimation.SHOWUP_TOP_LEFT)
                    .setMenuRadius(10f)
                    .setMenuShadow(10f)
                    .setWidth(600)
                    .setSelectedTextColor(Color.WHITE)
                    .setMenuColor(Color.WHITE)
                    .build();
        }
    }

    private void openCommentDialog() {
        commentDialog = new NDialog(CarWashActivity.this, ButtonType.NO_BUTTON);
        commentDialog.isCancelable(false);
        commentDialog.setCustomView(R.layout.comment_dialog);
        List<View> childViews = commentDialog.getCustomViewChildren();
        for (View childView : childViews) {
            switch (childView.getId()) {
                case R.id.commentTextView:
                    commentTextView = childView.findViewById(R.id.commentTextView);
                    break;
                case R.id.simpleRatingBar:
                    scaleRatingBar = childView.findViewById(R.id.simpleRatingBar);
                    scaleRatingBar.setRating(5);
                    break;
                case R.id.sendCommentBtn:
                    Button sendCommentBtn = childView.findViewById(R.id.sendCommentBtn);
                    sendCommentBtn.setOnClickListener(v -> sendComment());
                    break;
                case R.id.cancelBtn:
                    Button cancelBtn = childView.findViewById(R.id.cancelBtn);
                    cancelBtn.setOnClickListener(v -> commentDialog.dismiss());
                    break;
            }
        }
        commentDialog.show();
    }

    private void sendComment() {
        API.sendComment(FastSave.getInstance().getString(ACCESS_TOKEN, ""), carWashId, new CommentsItem((int) scaleRatingBar.getRating(), commentTextView.getText().toString()))
                .enqueue(customCallback.getResponse(new CustomCallback.ResponseCallback<CommentsItem>() {
                            @Override
                            public void onSuccessful(Call<CommentsItem> call, Response<CommentsItem> response) {
                                API.getCarWashFull(FastSave.getInstance().getString(ACCESS_TOKEN, ""), carWashId)
                                        .enqueue(customCallback.getResponse(new CustomCallback.ResponseCallback<AllCarWashResponse>() {
                                            @Override
                                            public void onSuccessful(Call<AllCarWashResponse> call, Response<AllCarWashResponse> response) {
                                                carWash = response.body();
                                                setCommentList();
                                            }

                                            @Override
                                            public void onEmpty(Call<AllCarWashResponse> call, Response<AllCarWashResponse> response) {

                                            }
                                        }));
                                commentDialog.dismiss();
                                Toast.makeText(CarWashActivity.this, "Комментарий добавлен", Toast.LENGTH_SHORT).show();
                                updateRating();
                            }

                            @Override
                            public void onEmpty(Call<CommentsItem> call, Response<CommentsItem> response) {

                            }
                        })
                );
    }

    public void updateRating() {
        API.getRating(FastSave.getInstance().getString(ACCESS_TOKEN, ""), carWashId)
                .enqueue(customCallback.getResponse(new CustomCallback.ResponseCallback<Rating>() {
                    @Override
                    public void onSuccessful(Call<Rating> call, Response<Rating> response) {
                        carWashRating.setRatingNum(response.body().getRating());
                    }

                    @Override
                    public void onEmpty(Call<Rating> call, Response<Rating> response) {

                    }
                }));
    }

    private void setPackages() {
        if (carWash.getPackages().size() != 0) {
            for (int i = 0; i < carWash.getPackages().size(); i++) {
                View layout2 = LayoutInflater.from(this).inflate(R.layout.package_btn2, packageScroll, false);
                MaterialButton packageBtn = layout2.findViewById(R.id.packageBtn);
                packageBtn.setText(carWash.getPackages().get(i).getName());
                packageBtn.setTag(carWash.getPackages().get(i).getId());
                packageBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        List<PowerMenuItem> powerMenuItemList = new ArrayList<>();
                        for (int j = 0; j < packageMap.get(v.getTag()).getServices().size(); j++) {
                            powerMenuItemList.add(new PowerMenuItem(packageMap.get(v.getTag()).getServices().get(j).getName()));
                        }

                        packagePowerMenu = new PowerMenu.Builder(CarWashActivity.this)
                                .addItem(new PowerMenuItem("Скидка = " + packageMap.get(v.getTag()).getDiscount() + "%", false))
                                .addItemList(powerMenuItemList)
                                .setAnimation(MenuAnimation.SHOWUP_TOP_LEFT)
                                .setMenuRadius(10f)
                                .setMenuShadow(10f)
                                .setWidth(600)
                                .setSelectedTextColor(Color.WHITE)
                                .setMenuColor(Color.WHITE)
                                .build();
                        packagePowerMenu.showAsDropDown(v);
                    }
                });
                packageScroll.addView(layout2);
            }
        } else {
            packageScroll.setVisibility(View.GONE);
        }


    }

    private void showTutorial() {
        if (FastSave.getInstance().getBoolean(CARWASHA_CTIVITY, true)) {
            new GuideView.Builder(CarWashActivity.this)
                    .setTitle("Режим работы")
                    .setContentText("Здесь вы можете узнать режим работы компании")
                    .setTargetView(workTimeImage)
                    .setDismissType(DismissType.anywhere)
                    .setGuideListener(view1 -> new GuideView.Builder(CarWashActivity.this)
                            .setTitle("Связь")
                            .setContentText("Нажав на эту кнопку, вы сможете связаться с администратором компании по телефону \n")
                            .setTargetView(connectBtn)
                            .setDismissType(DismissType.anywhere)
                            .setGuideListener(new GuideListener() {
                                @Override
                                public void onDismiss(View view1) {
                                    new GuideView.Builder(CarWashActivity.this)
                                            .setTitle("Услуги")
                                            .setContentText("Здесь вы можете посмотреть и выбрать услуги")
                                            .setTargetView(orderButton)
                                            .setDismissType(DismissType.anywhere)
                                            .setGuideListener(new GuideListener() {
                                                @Override
                                                public void onDismiss(View view1) {
                                                    FastSave.getInstance().saveBoolean(CARWASHA_CTIVITY, false);
                                                }
                                            })
                                            .build()
                                            .show();
                                }
                            })
                            .build()
                            .show())
                    .build()
                    .show();
        }
    }

    public Context getContext() {
        return context;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(CarWashActivity.this, MapsActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
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

                                }

                                @Override
                                public void onEmpty(Call<List<AllCarResponse>> call, Response<List<AllCarResponse>> response) {
                                    FastSave.getInstance().deleteValue(CAR_ID);
                                    FastSave.getInstance().saveBoolean(OPEN_SERVICE_FLAG, true);
                                    startActivity(new Intent(CarWashActivity.this, CarListActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
                                }
                            })
                    );
        }
    }
}
