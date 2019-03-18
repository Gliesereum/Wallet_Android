package com.gliesereum.karma.ui;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appizona.yehiahd.fastsave.FastSave;
import com.gliesereum.karma.GlideApp;
import com.gliesereum.karma.R;
import com.gliesereum.karma.adapter.CommentListAdapter;
import com.gliesereum.karma.data.network.APIClient;
import com.gliesereum.karma.data.network.APIInterface;
import com.gliesereum.karma.data.network.json.carwash.AllCarWashResponse;
import com.gliesereum.karma.data.network.json.carwash.CommentsItem;
import com.gliesereum.karma.data.network.json.carwash.MediaItem;
import com.gliesereum.karma.data.network.json.carwash.PackagesItem;
import com.gliesereum.karma.data.network.json.carwash.ServicePricesItem;
import com.gliesereum.karma.data.network.json.carwash.WorkTimesItem;
import com.gliesereum.karma.util.ErrorHandler;
import com.gliesereum.karma.util.SmartRatingBar;
import com.gliesereum.karma.util.Util;
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

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import hakobastvatsatryan.DropdownTextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import smartdevelop.ir.eram.showcaseviewlib.GuideView;
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType;
import smartdevelop.ir.eram.showcaseviewlib.listener.GuideListener;

import static com.gliesereum.karma.util.Constants.ACCESS_TOKEN;
import static com.gliesereum.karma.util.Constants.CARWASHA_CTIVITY;
import static com.gliesereum.karma.util.Constants.CARWASH_ID;

public class CarWashActivity extends AppCompatActivity implements View.OnClickListener {

    private String carWashId;
    private AllCarWashResponse carWash;
    private APIInterface apiInterface;
    private ProgressDialog progressDialog;
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
    private ErrorHandler errorHandler;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_wash);
        initData();
        initView();
        getCarWash();
    }

    private void initData() {
//        FastSave.init(getApplicationContext());
        context = this;
        carWashId = FastSave.getInstance().getString(CARWASH_ID, "");
        apiInterface = APIClient.getClient().create(APIInterface.class);
        errorHandler = new ErrorHandler(this, this);
        customServiceMap = new HashMap<>();
        workTimeMap = new HashMap<>();
        packageMap = new HashMap<>();
        servicePriceMap = new HashMap<>();
        mediaURLList = new ArrayList<>();
        commentsItemList = new ArrayList<>();
        mediumList = new ArrayList<>();
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
        sendCommentBtn = findViewById(R.id.sendCommentBtn);
        commentList = findViewById(R.id.commentList);
        orderButton = findViewById(R.id.orderButton);
        orderButton.setOnClickListener(this);
        workTimeImage.setOnClickListener(this);
        sendCommentBtn.setOnClickListener(this);
        GlideApp.with(this).load(R.mipmap.ic_launcher_round).circleCrop().into(logoImageView);
        carWashRating = findViewById(R.id.carWashRating);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.orderButton:
                carWash.setId(carWashId);
                FastSave.getInstance().saveObject("carWash", carWash);
                startActivity(new Intent(CarWashActivity.this, OrderActivity.class));
                break;
            case R.id.workTimeImage:
                workTimePowerMenu.showAsDropDown(workTimeImage);
                break;
            case R.id.sendCommentBtn:
                openCommentDialog();
                break;
        }
    }

    private void getCarWash() {
        showProgressDialog();
        Call<AllCarWashResponse> call = apiInterface.getCarWashFull(carWashId);
        call.enqueue(new Callback<AllCarWashResponse>() {
            @Override
            public void onResponse(Call<AllCarWashResponse> call, Response<AllCarWashResponse> response) {
                if (response.code() == 200) {
                    carWash = response.body();
                    for (int i = 0; i < carWash.getPackages().size(); i++) {
                        packageMap.put(carWash.getPackages().get(i).getId(), carWash.getPackages().get(i));
                    }
                    for (int i = 0; i < carWash.getServicePrices().size(); i++) {
                        servicePriceMap.put(carWash.getServicePrices().get(i).getId(), carWash.getServicePrices().get(i));
                    }
                    workTimeMap.clear();
                    for (int i = 0; i < response.body().getWorkTimes().size(); i++) {
                        workTimeMap.put(response.body().getWorkTimes().get(i).getDayOfWeek(), response.body().getWorkTimes().get(i));
                    }
                    name.setText(carWash.getName());
                    address.setText(carWash.getAddress());
                    descriptionDropdown.setContentText(carWash.getDescription());
                    carWashRating.setRatingNum(carWash.getRating().getRating());
                    setWorkTime();
                    setPackages();
                    setPhotoSlider();
                    setCommentList();

                    for (int i = 0; i < carWash.getServicePrices().size(); i++) {
                        if (carWash.getServicePrices().get(i).getName() != null) {
                            customServiceMap.put(carWash.getServicePrices().get(i).getName(), carWash.getServicePrices().get(i).getServiceId());
                        }
                    }
                    closeProgressDialog();
                    showTutorial();
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
            public void onFailure(Call<AllCarWashResponse> call, Throwable t) {
                errorHandler.showCustomError(t.getMessage());
            }
        });
    }

    public void setCommentList() {
        commentsItemList.clear();
        Call<CommentsItem> call = apiInterface.getMyComment(FastSave.getInstance().getString(ACCESS_TOKEN, ""), carWashId);
        call.enqueue(new Callback<CommentsItem>() {
            @Override
            public void onResponse(Call<CommentsItem> call, Response<CommentsItem> response) {
                if (response.code() == 200) {
                    commentListAdapter = new CommentListAdapter(true, CarWashActivity.this);
                    commentsItemList.add(response.body());
                    carWash.getComments().remove(response.body());
                    commentsItemList.addAll(carWash.getComments());
                    commentListAdapter.setItems(commentsItemList);
                    commentList.setAdapter(commentListAdapter);
                    commentList.setLayoutManager(new LinearLayoutManager(CarWashActivity.this));
                    sendCommentBtn.setVisibility(View.GONE);
                } else {
                    if (response.code() == 204) {
                        commentListAdapter = new CommentListAdapter(false, CarWashActivity.this);
                        commentsItemList.addAll(carWash.getComments());
                        commentListAdapter.setItems(commentsItemList);
                        commentList.setAdapter(commentListAdapter);
                        commentList.setLayoutManager(new LinearLayoutManager(CarWashActivity.this));
                        sendCommentBtn.setVisibility(View.VISIBLE);
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
            public void onFailure(Call<CommentsItem> call, Throwable t) {
                errorHandler.showCustomError(t.getMessage());
            }
        });
    }

    private void setPhotoSlider() {
        mediumList = carWash.getMedia();
        for (int i = 0; i < mediumList.size(); i++) {
            mediaURLList.add(mediumList.get(i).getUrl());
            View view = LayoutInflater.from(this).inflate(R.layout.image_view_for_slider, photoScrollView, false);
            ImageView imageView = view.findViewById(R.id.imageView6);
            Picasso.get().load(mediumList.get(i).getUrl()).fit().into(imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialog dialog = new PopopDialogBuilder(CarWashActivity.this)
                            .setList(mediaURLList)
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
        String monday, tuesday, wednesday, thursday, friday, saturday, sunday;
        if (workTimeMap.get("MONDAY").isIsWork()) {
            monday = "Пн: " + Util.getStringTime(workTimeMap.get("MONDAY").getFrom()) + "-" + Util.getStringTime(workTimeMap.get("MONDAY").getTo());
        } else {
            monday = "Пн: Не работает";
        }
        if (workTimeMap.get("TUESDAY").isIsWork()) {
            tuesday = "Вт: " + Util.getStringTime(workTimeMap.get("TUESDAY").getFrom()) + "-" + Util.getStringTime(workTimeMap.get("TUESDAY").getTo());
        } else {
            tuesday = "Вт: Не работает";
        }
        if (workTimeMap.get("WEDNESDAY").isIsWork()) {
            wednesday = "Ср: " + Util.getStringTime(workTimeMap.get("WEDNESDAY").getFrom()) + "-" + Util.getStringTime(workTimeMap.get("WEDNESDAY").getTo());
        } else {
            wednesday = "Ср: Не работает";
        }
        if (workTimeMap.get("THURSDAY").isIsWork()) {
            thursday = "Чт: " + Util.getStringTime(workTimeMap.get("THURSDAY").getFrom()) + "-" + Util.getStringTime(workTimeMap.get("THURSDAY").getTo());
        } else {
            thursday = "Чт: Не работает";
        }
        if (workTimeMap.get("FRIDAY").isIsWork()) {
            friday = "Пт: " + Util.getStringTime(workTimeMap.get("FRIDAY").getFrom()) + "-" + Util.getStringTime(workTimeMap.get("FRIDAY").getTo());
        } else {
            friday = "Пт: Не работает";
        }
        if (workTimeMap.get("SATURDAY").isIsWork()) {
            saturday = "Сб: " + Util.getStringTime(workTimeMap.get("SATURDAY").getFrom()) + "-" + Util.getStringTime(workTimeMap.get("SATURDAY").getTo());
        } else {
            saturday = "Сб: Не работает";
        }
        if (workTimeMap.get("SUNDAY").isIsWork()) {
            sunday = "Вс: " + Util.getStringTime(workTimeMap.get("SUNDAY").getFrom()) + "-" + Util.getStringTime(workTimeMap.get("SUNDAY").getTo());
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
                    sendCommentBtn.setOnClickListener(this);
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
        showProgressDialog();
        Call<CommentsItem> call = apiInterface.sendComment(FastSave.getInstance().getString(ACCESS_TOKEN, ""), carWashId, new CommentsItem((int) scaleRatingBar.getRating(), commentTextView.getText().toString()));
        call.enqueue(new Callback<CommentsItem>() {
            @Override
            public void onResponse(Call<CommentsItem> call, Response<CommentsItem> response) {
                if (response.code() == 200) {
                    Call<AllCarWashResponse> call1 = apiInterface.getCarWashFull(carWashId);
                    call1.enqueue(new Callback<AllCarWashResponse>() {
                        @Override
                        public void onResponse(Call<AllCarWashResponse> call1, Response<AllCarWashResponse> response) {
                            if (response.code() == 200) {
                                carWash = response.body();
                                setCommentList();
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
                        public void onFailure(Call<AllCarWashResponse> call, Throwable t) {
                            errorHandler.showCustomError(t.getMessage());
                            closeProgressDialog();
                        }
                    });

                    commentDialog.dismiss();
                    Toast.makeText(CarWashActivity.this, "Комментарий добавлен", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        errorHandler.showError(jObjError.getInt("code"));
                        closeProgressDialog();
                        commentDialog.dismiss();
                    } catch (Exception e) {
                        errorHandler.showCustomError(e.getMessage());
                        closeProgressDialog();
                        commentDialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<CommentsItem> call, Throwable t) {
                errorHandler.showCustomError(t.getMessage());
                closeProgressDialog();
                commentDialog.dismiss();
            }
        });
    }

    private void setPackages() {
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

    }

    public void showProgressDialog() {
        progressDialog = ProgressDialog.show(this, "Ща сек...", "Ща все сделаю...");
    }

    public void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    private void showTutorial() {
        if (FastSave.getInstance().getBoolean(CARWASHA_CTIVITY, true)) {
            new GuideView.Builder(CarWashActivity.this)
                    .setTitle("Расписание")
                    .setContentText("Тут Вы можете посмотреть расписание мойки")
                    .setTargetView(workTimeImage)
                    .setDismissType(DismissType.anywhere)
                    .setGuideListener(view -> new GuideView.Builder(CarWashActivity.this)
                            .setTitle("Пакеты услуг")
                            .setContentText("Ознакомтесь с пакетами услуг данной мойки тут")
                            .setTargetView(horizontalScrollView)
                            .setDismissType(DismissType.anywhere)
                            .setGuideListener(new GuideListener() {
                                @Override
                                public void onDismiss(View view) {
                                    new GuideView.Builder(CarWashActivity.this)
                                            .setTitle("Фото мойки")
                                            .setContentText("Тут можно посмотреть фотографии мойки")
                                            .setTargetView(photoScrollView)
                                            .setDismissType(DismissType.anywhere)
                                            .setGuideListener(new GuideListener() {
                                                @Override
                                                public void onDismiss(View view) {
                                                    new GuideView.Builder(CarWashActivity.this)
                                                            .setTitle("Заказать мойку")
                                                            .setContentText("Перейти к заказу мойки")
                                                            .setTargetView(orderButton)
                                                            .setDismissType(DismissType.anywhere)
                                                            .setGuideListener(new GuideListener() {
                                                                @Override
                                                                public void onDismiss(View view) {
                                                                    FastSave.getInstance().saveBoolean(CARWASHA_CTIVITY, false);
                                                                }
                                                            })
                                                            .build()
                                                            .show();
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

}
