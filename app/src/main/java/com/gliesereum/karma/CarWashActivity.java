package com.gliesereum.karma;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.appizona.yehiahd.fastsave.FastSave;
import com.gliesereum.karma.data.network.APIClient;
import com.gliesereum.karma.data.network.APIInterface;
import com.gliesereum.karma.data.network.json.carwash.AllCarWashResponse;
import com.gliesereum.karma.data.network.json.carwash.PackagesItem;
import com.gliesereum.karma.data.network.json.carwash.RecordsItem;
import com.gliesereum.karma.data.network.json.carwash.ServicePricesItem;
import com.gliesereum.karma.data.network.json.carwash.WorkTimesItem;
import com.gliesereum.karma.util.ErrorHandler;
import com.gliesereum.karma.util.Util;
import com.google.android.material.button.MaterialButton;
import com.shrikanthravi.collapsiblecalendarview.widget.CollapsibleCalendar;
import com.skydoves.powermenu.MenuAnimation;
import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import androidx.appcompat.app.AppCompatActivity;
import de.ehsun.coloredtimebar.TimelinePickerView;
import de.ehsun.coloredtimebar.TimelineView;
import iammert.com.expandablelib.ExpandableLayout;
import iammert.com.expandablelib.Section;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CarWashActivity extends AppCompatActivity implements View.OnClickListener {

    String carWashId;
    private AllCarWashResponse carWash;
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
    //    private DropdownTextView workTimesDropdown;
    private MaterialButton materialButton;
    private MaterialButton orderButton;
    //    private CollapsibleCalendar collapsibleCalendar;
    private Set<String> idSet = new HashSet<>();
    private Map<String, String> customServiceMap = new HashMap<>();
    //    private Toolbar toolbar;
    private TextView address;
    private CollapsibleCalendar calendarView;
    private LinearLayout packagesBlock;
    private ImageView imageView2;
    private ScrollView scrollView2;
    private HorizontalScrollView horizontalScrollView;
    private LinearLayout linearLayout;
    private LinearLayout linearLayout2;
    private LinearLayout servicePricesBlock;
    private ExpandableLayout expandablePackage;
    private ExpandableLayout expandableWorkTime;
    private ExpandableLayout expandableBoxBlock;
    private Map<String, WorkTimesItem> workTimeMap = new HashMap<>();
    private PowerMenu packagePowerMenu;
    private PowerMenu powerMenu1;
    private LinearLayout boxLinearLayout;
    private LinearLayout packageScroll;
    private Map<String, PackagesItem> packageMap = new HashMap<>();
    private Map<String, ServicePricesItem> servicePriceMap = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_wash_test);

        carWashId = getIntent().getStringExtra("carWashId");
        initView();
        getCarWash();

    }

    private void getCarWash() {
        showProgressDialog();
        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<AllCarWashResponse> call = apiInterface.getCarWash(carWashId);
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
                    description.setText(carWash.getDescription());
                    setWorkTime(carWash);
                    setBoxTime(carWash);
                    setPackageBlock(carWash);
                    setPackages(carWash);
//                    setServicePricesBlock(carWash);

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

//                    int countBox = carWash.getSpaces().size();
//                    int countBox = 5;
//                    for (int i = 0; i < countBox; i++) {
//                        TextView textView = new TextView(CarWashActivity.this);
//                        TimelineView timelineBox = new TimelineView(CarWashActivity.this);
//                        textView.setText("Бокс №" + (i + 1));
//                        timelineBox.setId(i);
//                        timelineBox.setTimeRange(carWash.getWorkTimes().get(0).getFrom() + "-" + carWash.getWorkTimes().get(0).getTo());
//                        timelineBox.setTimeTextInterval(2);
//                        timelineBox.setFractionTextSize(30);
//                        timelineBox.setBarWidth(75);
//                        timelineBox.setFractionLineLength(20);
//                        timelineBox.setBarColorAvailable(Color.parseColor("#FF0000"));
//                        timelineBox.setBarColorNotAvailable(Color.parseColor("#00FF00"));
////                        timelineBox.setFractionLineColor(Color.parseColor("#DEFFFFFF"));
////                        timelineBox.setFractionSecondaryTextColor(Color.parseColor("#DEFFFFFF"));
////                        timelineBox.setFractionPrimaryTextColor(Color.parseColor("#DEFFFFFF"));
////                        timelineBox.setHighlightTimeRange("10:00-23:00");
////                        timelineBox.setFractionPrimaryTextColor(R.color.available_time_default_color);
////                        timelineBox.setBarColorAvailable(R.color.timeline_default_color);
////                        timelineBox.setBarColorNotAvailable(R.color.available_time_default_color);
//                        boxBlock.addView(textView);
//                        boxBlock.addView(timelineBox);
//                        timelineBox.setOnClickListener(CarWashActivity.this);
//                    }
//                    TimelineView timelineBox0 = boxBlock.findViewById(0);
//                    List<String> timeRange = new ArrayList<>();
//                    timeRange.add("11:00-12:00");
//                    timeRange.add("14:00-16:00");
//                    timeRange.add("17:30-19:00");
//                    timelineBox0.setAvailableTimeRange(timeRange);
//
//                    String MONDAY = "";
//                    String TUESDAY = "";
//                    String WEDNESDAY = "";
//                    String THURSDAY = "";
//                    String FRIDAY = "";
//                    String SATURDAY = "";
//                    String SUNDAY = "";
//                    for (int i = 0; i < carWash.getWorkTimes().size(); i++) {
//                        switch (carWash.getWorkTimes().get(i).getDayOfWeek()) {
//                            case "MONDAY":
//                                MONDAY = "MONDAY: " + carWash.getWorkTimes().get(i).getFrom() + "-" + carWash.getWorkTimes().get(i).getTo();
//                                break;
//                            case "TUESDAY":
//                                TUESDAY = "TUESDAY: " + carWash.getWorkTimes().get(i).getFrom() + "-" + carWash.getWorkTimes().get(i).getTo();
//                                break;
//                            case "WEDNESDAY":
//                                WEDNESDAY = "WEDNESDAY: " + carWash.getWorkTimes().get(i).getFrom() + "-" + carWash.getWorkTimes().get(i).getTo();
//                                break;
//                            case "THURSDAY":
//                                THURSDAY = "THURSDAY: " + carWash.getWorkTimes().get(i).getFrom() + "-" + carWash.getWorkTimes().get(i).getTo();
//                                break;
//                            case "FRIDAY":
//                                FRIDAY = "FRIDAY: " + carWash.getWorkTimes().get(i).getFrom() + "-" + carWash.getWorkTimes().get(i).getTo();
//                                break;
//                            case "SATURDAY":
//                                SATURDAY = "SATURDAY: " + carWash.getWorkTimes().get(i).getFrom() + "-" + carWash.getWorkTimes().get(i).getTo();
//                                break;
//                            case "SUNDAY":
//                                SUNDAY = "SUNDAY: " + carWash.getWorkTimes().get(i).getFrom() + "-" + carWash.getWorkTimes().get(i).getTo();
//                                break;
//                        }
//
//                    }

//                    workTimesDropdown.setContentText(MONDAY + "\n" + TUESDAY + "\n" + WEDNESDAY + "\n" + THURSDAY + "\n" + FRIDAY + "\n" + SATURDAY + "\n" + SUNDAY);


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
    }

    private void setServicePricesBlock(AllCarWashResponse carWash) {
//        for (int i = 0; i < carWash.getServicePrices().size(); i++) {
        for (int i = 0; i < 10; i++) {
            CheckBox checkBox = new CheckBox(CarWashActivity.this);
            checkBox.setText("Услуга №" + i);
            servicePricesBlock.addView(checkBox);
        }
    }

    private void setPackageBlock(AllCarWashResponse carWash) {
        Section<String, PackagesItem> section = new Section<>();
        section.parent = "Пакеты услуг";
        for (int i = 0; i < carWash.getPackages().size(); i++) {
            section.children.add(carWash.getPackages().get(i));
        }
        expandablePackage.addSection(section);


    }

    private void setBoxTime(AllCarWashResponse carWash) {
        for (int i = 0; i < carWash.getSpaces().size(); i++) {
            View layout2 = LayoutInflater.from(this).inflate(R.layout.layout_boxline, boxLinearLayout, false);
            TimelineView timelineBox = layout2.findViewById(R.id.timelineView);
            Log.d(TAG, "setBoxTime: " + i);
            Calendar calendar = Calendar.getInstance();
            int intDay = calendar.get(Calendar.DAY_OF_WEEK);
            timelineBox.setTimeRange(Util.getStringTime(workTimeMap.get(getCurrentDayOfWeek(intDay)).getFrom()) + "-" + Util.getStringTime(workTimeMap.get(getCurrentDayOfWeek(intDay)).getTo()));
            timelineBox.setTimeTextInterval(4);
            timelineBox.setFractionTextSize(30);
            timelineBox.setId(i);
            timelineBox.setBarWidth(75);
            timelineBox.setFractionLineLength(20);
            timelineBox.setBarColorAvailable(Color.parseColor("#FF0000"));
            timelineBox.setBarColorNotAvailable(Color.parseColor("#00FF00"));
            timelineBox.setAvailableTimeRange(getTimeInBox(carWash.getRecords(), carWash.getSpaces().get(i).getId()));
            boxLinearLayout.addView(layout2);
        }

//        ((TextView) view.findViewById(R.id.boxName)).setText("Бокс №" + model.getIndexNumber());
//        Log.d(TAG, "renderChild: " + "Бокс №" + model.getIndexNumber());
//        ((TextView) view.findViewById(R.id.boxId)).setText(model.getId());
//        LinearLayout linearLayout = view.findViewById(R.id.boxView);
//        TimelineView timelineBox = new TimelineView(CarWashActivity.this);
//        Calendar calendar = Calendar.getInstance();
//        int intDay = calendar.get(Calendar.DAY_OF_WEEK);
//        timelineBox.setTimeRange(workTimeMap.get(getCurrentDayOfWeek(intDay)).getFrom() + "-" + workTimeMap.get(getCurrentDayOfWeek(intDay)).getTo());
//        timelineBox.setTimeTextInterval(2);
//        timelineBox.setFractionTextSize(30);
//        timelineBox.setBarWidth(75);
//        timelineBox.setFractionLineLength(20);
//        timelineBox.setBarColorAvailable(Color.parseColor("#FF0000"));
//        timelineBox.setBarColorNotAvailable(Color.parseColor("#00FF00"));
//        timelineBox.setAvailableTimeRange(getTimeInBox(carWash.getRecords(), model.getId()));
//        boxLinearLayout.addView(timelineBox);
//                timelineBox.setOnClickListener(CarWashActivity.this);
//        linearLayout.addView(timelineBox);

//        Section<String, SpacesItem> section = new Section<>();
//        section.parent = "Загруженность боксов";
//        section.children.addAll(carWash.getSpaces());
//        expandableBoxBlock.addSection(section);

    }

    private void setWorkTime(AllCarWashResponse carWash) {
        powerMenu1 = new PowerMenu.Builder(CarWashActivity.this)
                .addItem(new PowerMenuItem("Пн: " + Util.getStringTime(workTimeMap.get("MONDAY").getFrom()) + "-" + Util.getStringTime(workTimeMap.get("MONDAY").getTo()), false))
                .addItem(new PowerMenuItem("Вт: " + Util.getStringTime(workTimeMap.get("TUESDAY").getFrom()) + "-" + Util.getStringTime(workTimeMap.get("TUESDAY").getTo()), false))
                .addItem(new PowerMenuItem("Ср: " + Util.getStringTime(workTimeMap.get("WEDNESDAY").getFrom()) + "-" + Util.getStringTime(workTimeMap.get("WEDNESDAY").getTo()), false))
                .addItem(new PowerMenuItem("Чт: " + Util.getStringTime(workTimeMap.get("THURSDAY").getFrom()) + "-" + Util.getStringTime(workTimeMap.get("THURSDAY").getTo()), false))
                .addItem(new PowerMenuItem("Пт: " + Util.getStringTime(workTimeMap.get("FRIDAY").getFrom()) + "-" + Util.getStringTime(workTimeMap.get("FRIDAY").getTo()), false))
                .addItem(new PowerMenuItem("Сб: " + Util.getStringTime(workTimeMap.get("SATURDAY").getFrom()) + "-" + Util.getStringTime(workTimeMap.get("SATURDAY").getTo()), false))
                .addItem(new PowerMenuItem("Вс: " + Util.getStringTime(workTimeMap.get("SUNDAY").getFrom()) + "-" + Util.getStringTime(workTimeMap.get("SUNDAY").getTo()), false))
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


//        Section<String, WorkTimesItem> section = new Section<>();
//        section.parent = "Рабочее время";
//        for (int i = 0; i < carWash.getWorkTimes().size(); i++) {
//            section.children.add(carWash.getWorkTimes().get(i));
//        }
//        expandableWorkTime.addSection(section);
    }

    private void initView() {
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
        boxLinearLayout = (LinearLayout) findViewById(R.id.boxLinearLayout);
        address = (TextView) findViewById(R.id.address);
        calendarView = (CollapsibleCalendar) findViewById(R.id.calendarView);
        packagesBlock = (LinearLayout) findViewById(R.id.packagesBlock);
        imageView2 = (ImageView) findViewById(R.id.imageView2);
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                powerMenu1.showAsDropDown(imageView2); // view is an anchor
            }
        });


        expandablePackage = (ExpandableLayout) findViewById(R.id.expandablePackage);

        expandablePackage = findViewById(R.id.expandablePackage);
        expandablePackage.setRenderer(new ExpandableLayout.Renderer<String, PackagesItem>() {
            @Override
            public void renderParent(View view, String model, boolean isExpanded, int parentPosition) {
                ((TextView) view.findViewById(R.id.parent)).setText(model);
                view.findViewById(R.id.arrow).setBackgroundResource(isExpanded ? R.drawable.arrow_up : R.drawable.arrow_down);
            }

            @Override
            public void renderChild(View view, PackagesItem model, int parentPosition, int childPosition) {
                ((TextView) view.findViewById(R.id.child)).setText(model.getName());
            }
        });

//        expandableWorkTime = findViewById(R.id.expandableWorkTime);
//        expandableWorkTime.setRenderer(new ExpandableLayout.Renderer<String, WorkTimesItem>() {
//            @Override
//            public void renderParent(View view, String model, boolean isExpanded, int parentPosition) {
//                ((TextView) view.findViewById(R.id.parent)).setText(model);
//                view.findViewById(R.id.arrow).setBackgroundResource(isExpanded ? R.drawable.arrow_up : R.drawable.arrow_down);
//            }
//
//            @Override
//            public void renderChild(View view, WorkTimesItem model, int parentPosition, int childPosition) {
//                switch (model.getDayOfWeek()) {
//                    case "MONDAY":
//                        ((TextView) view.findViewById(R.id.child)).setText("MONDAY: " + model.getFrom() + "-" + model.getTo());
//                        break;
//                    case "TUESDAY":
//                        ((TextView) view.findViewById(R.id.child)).setText("TUESDAY: " + model.getFrom() + "-" + model.getTo());
//                        break;
//                    case "WEDNESDAY":
//                        ((TextView) view.findViewById(R.id.child)).setText("WEDNESDAY: " + model.getFrom() + "-" + model.getTo());
//                        break;
//                    case "THURSDAY":
//                        ((TextView) view.findViewById(R.id.child)).setText("THURSDAY: " + model.getFrom() + "-" + model.getTo());
//                        break;
//                    case "FRIDAY":
//                        ((TextView) view.findViewById(R.id.child)).setText("FRIDAY: " + model.getFrom() + "-" + model.getTo());
//                        break;
//                    case "SATURDAY":
//                        ((TextView) view.findViewById(R.id.child)).setText("SATURDAY: " + model.getFrom() + "-" + model.getTo());
//                        break;
//                    case "SUNDAY":
//                        ((TextView) view.findViewById(R.id.child)).setText("SUNDAY: " + model.getFrom() + "-" + model.getTo());
//                        break;
//                }
//
//            }
//        });

//        expandableBoxBlock = findViewById(R.id.expandableBoxBlock);
//        expandableBoxBlock.setRenderer(new ExpandableLayout.Renderer<String, SpacesItem>() {
//            @Override
//            public void renderParent(View view, String model, boolean isExpanded, int parentPosition) {
//                ((TextView) view.findViewById(R.id.parent)).setText(model);
//                view.findViewById(R.id.arrow).setBackgroundResource(isExpanded ? R.drawable.arrow_up : R.drawable.arrow_down);
//            }
//
//            @Override
//            public void renderChild(View view, SpacesItem model, int parentPosition, int childPosition) {
//                ((TextView) view.findViewById(R.id.boxName)).setText("Бокс №" + model.getIndexNumber());
//                Log.d(TAG, "renderChild: " + "Бокс №" + model.getIndexNumber());
//                ((TextView) view.findViewById(R.id.boxId)).setText(model.getId());
//                LinearLayout linearLayout = view.findViewById(R.id.boxView);
//                TimelineView timelineBox = new TimelineView(CarWashActivity.this);
//                Calendar calendar = Calendar.getInstance();
//                int intDay = calendar.get(Calendar.DAY_OF_WEEK);
//                timelineBox.setTimeRange(workTimeMap.get(getCurrentDayOfWeek(intDay)).getFrom() + "-" + workTimeMap.get(getCurrentDayOfWeek(intDay)).getTo());
//                timelineBox.setTimeTextInterval(2);
//                timelineBox.setFractionTextSize(30);
//                timelineBox.setBarWidth(75);
//                timelineBox.setFractionLineLength(20);
//                timelineBox.setBarColorAvailable(Color.parseColor("#FF0000"));
//                timelineBox.setBarColorNotAvailable(Color.parseColor("#00FF00"));
////                timelineBox.setOnClickListener(CarWashActivity.this);
//                linearLayout.addView(timelineBox);
//                timelineBox.setAvailableTimeRange(getTimeInBox(carWash.getRecords(), model.getId()));
//            }
//        });
        packageScroll = (LinearLayout) findViewById(R.id.packageScroll);
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
        progressDialog = ProgressDialog.show(this, "title", "message");

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
            View layout2 = LayoutInflater.from(this).inflate(R.layout.layout_package, packageScroll, false);
//            TextView packageId = layout2.findViewById(R.id.packageId);
            MaterialButton packageBtn = layout2.findViewById(R.id.packageBtn);
//            MaterialButton packageBtn = new MaterialButton(CarWashActivity.this);
//            packageId.setText(carWash.getPackages().get(i).getId());
            packageBtn.setText(carWash.getPackages().get(i).getName());
            packageBtn.setTag(carWash.getPackages().get(i).getId());
            packageBtn.setCornerRadius(50);


            packageBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<String> serviceNameList = new ArrayList<>();
                    for (int j = 0; j < packageMap.get(v.getTag()).getServices().size(); j++) {
                        serviceNameList.add(packageMap.get(v.getTag()).getServices().get(j).getName());
                    }
                    Toast.makeText(CarWashActivity.this, (String) v.getTag(), Toast.LENGTH_SHORT).show();
                    packagePowerMenu = new PowerMenu.Builder(CarWashActivity.this)
                            .addItem(new PowerMenuItem(packageMap.get(v.getTag()).getName(), false))
                            .addItem(new PowerMenuItem("Скидка = " + packageMap.get(v.getTag()).getDiscount() + "%", false))
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

//            packageScroll.addView(packageBtn);
            packageScroll.addView(layout2);
        }
//        packageScroll.getChildAt(0).performClick();
//        setServicePrices(carWash);

    }

}
