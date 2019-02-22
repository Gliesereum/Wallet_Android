package com.gliesereum.karma;

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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.appizona.yehiahd.fastsave.FastSave;
import com.elconfidencial.bubbleshowcase.BubbleShowCaseBuilder;
import com.elconfidencial.bubbleshowcase.BubbleShowCaseSequence;
import com.gliesereum.karma.data.network.APIClient;
import com.gliesereum.karma.data.network.APIInterface;
import com.gliesereum.karma.data.network.adapter.CommentListAdapter;
import com.gliesereum.karma.data.network.json.carwash.AllCarWashResponse;
import com.gliesereum.karma.data.network.json.carwash.CommentsItem;
import com.gliesereum.karma.data.network.json.carwash.MediaItem;
import com.gliesereum.karma.data.network.json.carwash.PackagesItem;
import com.gliesereum.karma.data.network.json.carwash.RecordsItem;
import com.gliesereum.karma.data.network.json.carwash.ServicePricesItem;
import com.gliesereum.karma.data.network.json.carwash.WorkTimesItem;
import com.gliesereum.karma.util.ErrorHandler;
import com.gliesereum.karma.util.Util;
import com.gliesereum.karma.util.photo.PhotosViewSlider;
import com.gohn.nativedialog.ButtonType;
import com.gohn.nativedialog.NDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.skydoves.powermenu.MenuAnimation;
import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;
import com.willy.ratingbar.ScaleRatingBar;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.ehsun.coloredtimebar.TimelinePickerView;
import de.ehsun.coloredtimebar.TimelineView;
import hakobastvatsatryan.DropdownTextView;
import iammert.com.expandablelib.ExpandableLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.gliesereum.karma.util.Constants.ACCESS_TOKEN;

public class CarWashActivity extends AppCompatActivity implements View.OnClickListener {

    String carWashId;
    AllCarWashResponse carWash;
    private MaterialButton addCarBtn;
    private APIInterface apiInterface;
    private ErrorHandler errorHandler;
    private ProgressDialog progressDialog;
    private TimelinePickerView timelinePicker;
    private String TAG = "TAG";
    private TimelineView timelineBox01;
    private TimelineView timelineBox02;
    private TimelineView timelineBox03;
    private TextView name;
    private TextView adres;
    private TextView description;
    private TextView workTimes;
    private LinearLayout boxBlock;
    private DropdownTextView descriptionDropdown;
    private MaterialButton materialButton;
    private MaterialButton orderButton;
    //    private CollapsibleCalendar collapsibleCalendar;
    private Set<String> idSet = new HashSet<>();
    private Map<String, String> customServiceMap = new HashMap<>();
    //    private Toolbar toolbar;
    private TextView address;
    private LinearLayout packagesBlock;
    private ImageView workTimeImage;
    private ImageView imageView3;
    private ScrollView scrollView2;
    private HorizontalScrollView horizontalScrollView;
    private LinearLayout linearLayout;
    private LinearLayout linearLayout2;
    private LinearLayout servicePricesBlock;
    //    private ExpandableLayout expandablePackage;
    private ExpandableLayout expandableWorkTime;
    private ExpandableLayout expandableBoxBlock;
    private Map<String, WorkTimesItem> workTimeMap = new HashMap<>();
    private PowerMenu packagePowerMenu;
    private PowerMenu powerMenu1;
    private PowerMenu editCommentMenu;
    //    private LinearLayout boxLinearLayout;
    private LinearLayout packageScroll;
    private Map<String, PackagesItem> packageMap = new HashMap<>();
    private Map<String, ServicePricesItem> servicePriceMap = new HashMap<>();
    private PhotosViewSlider photosViewSlider;
    private TextInputLayout commentTextInputLayout;
    private TextInputEditText commentTextView;
    private CardView cardView;
    private TextView textView15;
    private HorizontalScrollView photoScrollView;
    private Button sendCommentBtn;
    private RecyclerView commentList;
    private CommentListAdapter commentListAdapter;
    private ScaleRatingBar scaleRatingBar;
    private String commentString = "";
    private Context context;
    private LinearLayout photoLayout;
    private boolean isImageFitToScreen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_wash_test);
        FastSave.init(getApplicationContext());
        context = this;
        carWashId = getIntent().getStringExtra("carWashId");
        initView();
        getCarWash();
        GlideApp.with(this).load(R.mipmap.ic_launcher_round).circleCrop().into(imageView3);
    }

    private void showTutorial() {
        BubbleShowCaseBuilder workTimeTutorial = new BubbleShowCaseBuilder(this) //Activity instance
                .title("Тут будет титул этого сообщения")
                .description("Тут можно посмотреть график работы мойки")
                .backgroundColorResourceId(R.color.colorAccent)
                .textColorResourceId(R.color.black)
                .showOnce("CarWashActivity")
                .targetView(workTimeImage);

        BubbleShowCaseBuilder descriptionTutorial = new BubbleShowCaseBuilder(this)
                .title("Вы всегда можете прочитать что то интересное про мойку")
                .backgroundColorResourceId(R.color.colorAccent)
                .textColorResourceId(R.color.black)
                .showOnce("CarWashActivity")
                .targetView(descriptionDropdown);

        BubbleShowCaseBuilder packageTutorial = new BubbleShowCaseBuilder(this)
                .title("Пакеты услуг")
                .description("Пакет дешевле... Можете ознакомиться тут")
                .backgroundColorResourceId(R.color.colorAccent)
                .textColorResourceId(R.color.black)
                .showOnce("CarWashActivity")
                .targetView(packageScroll);

        BubbleShowCaseBuilder commentTutorial = new BubbleShowCaseBuilder(this)
                .description("Оставте комментарий как Вам все понравилось")
                .backgroundColorResourceId(R.color.colorAccent)
                .textColorResourceId(R.color.black)
                .showOnce("CarWashActivity")
                .targetView(sendCommentBtn);

        BubbleShowCaseBuilder orderTutorial = new BubbleShowCaseBuilder(this)
                .title("Заказать мойку")
                .description("Жмите сюда и выбирайте когда, как и чем мыть вашу Ласточку")
                .backgroundColorResourceId(R.color.colorAccent)
                .textColorResourceId(R.color.black)
                .showOnce("CarWashActivity")
                .targetView(orderButton);

        new BubbleShowCaseSequence()
                .addShowCase(workTimeTutorial)
                .addShowCase(descriptionTutorial)
                .addShowCase(packageTutorial)
                .addShowCase(commentTutorial)
                .addShowCase(orderTutorial)
                .show();
    }

    private void getCarWash() {
        showProgressDialog();
        apiInterface = APIClient.getClient().create(APIInterface.class);
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
                    adres.setText(carWash.getAddress());
                    descriptionDropdown.setContentText(carWash.getDescription());
                    setWorkTime(carWash);
                    setPackages(carWash);
                    setPhotoSlider(carWash);
                    setCommentList(carWash);

                    for (int i = 0; i < carWash.getServicePrices().size(); i++) {
                        if (carWash.getServicePrices().get(i).getName() != null) {
                            customServiceMap.put(carWash.getServicePrices().get(i).getName(), carWash.getServicePrices().get(i).getServiceId());
                        } else {
//                            customServiceMap.put(carWash.getServicePrices().get(i).getOriginalName(), carWash.getServicePrices().get(i).getServiceId());
                        }

                        if (idSet.contains(carWash.getServicePrices().get(i).getCorporationServiceId())) {
                            //set checked
                        } else {

                        }
                    }
                    closeProgressDialog();
//                    showTutorial();
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

    public void setCommentList(AllCarWashResponse carWash) {
        List<CommentsItem> commentsItemList = new ArrayList<>();
        apiInterface = APIClient.getClient().create(APIInterface.class);
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

    private void setPhotoSlider(AllCarWashResponse carWash) {
        List<MediaItem> mediumList = carWash.getMedia();
        ArrayList<String> mediaURLList = new ArrayList<>();
        for (int i = 0; i < mediumList.size(); i++) {
            mediaURLList.add(mediumList.get(i).getUrl());
        }

        if (mediaURLList.size() != 0) {
            photoScrollView.setVisibility(View.VISIBLE);
            photosViewSlider.setGridColumns(mediaURLList.size());
            photosViewSlider.initializePhotosUrls(mediaURLList);
        } else {
            photoScrollView.setVisibility(View.GONE);
        }
    }

    private void setWorkTime(AllCarWashResponse carWash) {
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

        powerMenu1 = new PowerMenu.Builder(CarWashActivity.this)
                .addItem(new PowerMenuItem(monday, false))
                .addItem(new PowerMenuItem(tuesday, false))
                .addItem(new PowerMenuItem(wednesday, false))
                .addItem(new PowerMenuItem(thursday, false))
                .addItem(new PowerMenuItem(friday, false))
                .addItem(new PowerMenuItem(saturday, false))
                .addItem(new PowerMenuItem(sunday, false))
                .setAnimation(MenuAnimation.SHOWUP_TOP_LEFT) // Animation start point (TOP | LEFT)
                .setMenuRadius(10f)
                .setMenuShadow(10f)
                .setWidth(600) // set popup width size
//                .setTextColor(context.getResources().getColor(R.color.md_grey_800))
                .setSelectedTextColor(Color.WHITE)
                .setMenuColor(Color.WHITE)
                .build();
    }

    private void initView() {
        photosViewSlider = findViewById(R.id.photosViewSlider);
        name = (TextView) findViewById(R.id.name);
        adres = (TextView) findViewById(R.id.address);
        description = (TextView) findViewById(R.id.description);
        boxBlock = (LinearLayout) findViewById(R.id.boxBlock);
        orderButton = (MaterialButton) findViewById(R.id.orderButton);
        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carWash.setId(carWashId);
                FastSave.getInstance().saveObject("carWash", carWash);
                startActivity(new Intent(CarWashActivity.this, OrderActivity.class));
            }
        });
//        boxLinearLayout = (LinearLayout) findViewById(R.id.boxLinearLayout);
        address = (TextView) findViewById(R.id.address);
        packagesBlock = (LinearLayout) findViewById(R.id.packagesBlock);
        workTimeImage = (ImageView) findViewById(R.id.imageView2);
        imageView3 = (ImageView) findViewById(R.id.imageView3);
        workTimeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                powerMenu1.showAsDropDown(workTimeImage); // view is an anchor
            }
        });
        packageScroll = (LinearLayout) findViewById(R.id.packageScroll);
        descriptionDropdown = findViewById(R.id.descriptionDropdown);
        commentTextInputLayout = findViewById(R.id.commentTextInputLayout);
        commentTextView = findViewById(R.id.commentTextView);
        cardView = findViewById(R.id.cardView);
        textView15 = findViewById(R.id.textView15);
        photoScrollView = findViewById(R.id.photoScrollView);
        sendCommentBtn = findViewById(R.id.sendCommentBtn);
        sendCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCommentDialog();
            }
        });
        commentList = findViewById(R.id.commentList);
    }

    private void openCommentDialog() {
        NDialog commentDialog = new NDialog(CarWashActivity.this, ButtonType.NO_BUTTON);
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
                case R.id.timeOrderBtn:
                    Button okBtn = childView.findViewById(R.id.timeOrderBtn);
                    okBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showProgressDialog();
                            apiInterface = APIClient.getClient().create(APIInterface.class);
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
                                                    setCommentList(response.body());
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
                                            public void onFailure(Call<AllCarWashResponse> call, Throwable t) {
                                                errorHandler.showCustomError(t.getMessage());
                                            }
                                        });
                                        closeProgressDialog();
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
                    });
                    break;
                case R.id.nowOrderBtn:
                    Button backBtn = childView.findViewById(R.id.nowOrderBtn);
                    backBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            commentDialog.dismiss();
                        }
                    });
                    break;
            }
        }
        commentDialog.show();
    }

    private String getCurrentDayOfWeek(int intDay) {
        String dayOfWeek = "";
        switch (intDay) {
            case 1:
                dayOfWeek = "SUNDAY";
                break;
            case 2:
                dayOfWeek = "MONDAY";
                break;
            case 3:
                dayOfWeek = "TUESDAY";
                break;
            case 4:
                dayOfWeek = "WEDNESDAY";
                break;
            case 5:
                dayOfWeek = "THURSDAY";
                break;
            case 6:
                dayOfWeek = "FRIDAY";
                break;
            case 7:
                dayOfWeek = "SATURDAY";
                break;
        }

        return dayOfWeek;
    }

    private List<String> getTimeInBox(List<RecordsItem> records, String id) {
        List<String> timeRange = new ArrayList<>();
        for (int i = 0; i < records.size(); i++) {
            if (records.get(i).getWorkingSpaceId().equals(id)) {
                timeRange.add(Util.getStringTime(records.get(i).getBegin()) + "-" + Util.getStringTime(records.get(i).getFinish()));
            }
        }
        return timeRange;
    }

    public void showProgressDialog() {
        progressDialog = ProgressDialog.show(this, "Ща сек...", "Ща все сделаю...");

    }

    public void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case 0:
                Toast.makeText(this, "0", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();
                break;
            case 2:
                Toast.makeText(this, "2", Toast.LENGTH_SHORT).show();
                break;
            case 3:
                Toast.makeText(this, "3", Toast.LENGTH_SHORT).show();
                break;
            case 4:
                Toast.makeText(this, "4", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void setPackages(AllCarWashResponse carWash) {
        for (int i = 0; i < carWash.getPackages().size(); i++) {
            View layout2 = LayoutInflater.from(this).inflate(R.layout.package_btn, packageScroll, false);
            MaterialButton packageBtn = layout2.findViewById(R.id.packageBtn);
            packageBtn.setText(carWash.getPackages().get(i).getName());
            packageBtn.setTag(carWash.getPackages().get(i).getId());
            packageBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    List<String> serviceNameList = new ArrayList<>();
                    List<PowerMenuItem> powerMenuItemList = new ArrayList<>();
                    for (int j = 0; j < packageMap.get(v.getTag()).getServices().size(); j++) {
//                        serviceNameList.add(packageMap.get(v.getTag()).getServices().get(j).getName());
                        powerMenuItemList.add(new PowerMenuItem(packageMap.get(v.getTag()).getServices().get(j).getName()));
                    }

                    packagePowerMenu = new PowerMenu.Builder(CarWashActivity.this)
                            .addItem(new PowerMenuItem("Скидка = " + packageMap.get(v.getTag()).getDiscount() + "%", false))
                            .addItemList(powerMenuItemList)
                            .setAnimation(MenuAnimation.SHOWUP_TOP_LEFT) // Animation start point (TOP | LEFT)
                            .setMenuRadius(10f)
                            .setMenuShadow(10f)
                            .setWidth(600) // set popup width size
//                .setTextColor(context.getResources().getColor(R.color.md_grey_800))
                            .setSelectedTextColor(Color.WHITE)
                            .setMenuColor(Color.WHITE)
//                .setSelectedMenuColor(context.getResources().getColor(R.color.colorPrimary))
//                .setOnMenuItemClickListener(onMenuItemClickListener)
                            .build();

                    packagePowerMenu.showAsDropDown(v); // view is an anchor


                }
            });
            packageScroll.addView(layout2);
        }

    }

    public AllCarWashResponse getCarWashObjact() {
        return carWash;
    }

    public Context getContext() {
        return context;
    }
}
