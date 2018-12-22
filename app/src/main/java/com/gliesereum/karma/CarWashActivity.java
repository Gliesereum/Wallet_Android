package com.gliesereum.karma;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.gliesereum.karma.data.network.APIClient;
import com.gliesereum.karma.data.network.APIInterface;
import com.gliesereum.karma.data.network.json.carwash.AllCarWashResponse;
import com.gliesereum.karma.data.network.json.carwash.PackagesItem;
import com.gliesereum.karma.data.network.json.carwash.SpacesItem;
import com.gliesereum.karma.data.network.json.carwash.WorkTimesItem;
import com.gliesereum.karma.util.ErrorHandler;
import com.google.android.material.button.MaterialButton;
import com.shrikanthravi.collapsiblecalendarview.widget.CollapsibleCalendar;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.HashSet;
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

    String string;
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
    private MaterialButton materialButton3;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_wash_test);

        string = getIntent().getStringExtra("carWash_ID");
        initView();
        getCarWash();

    }

    private void getCarWash() {
        showProgressDialog();
        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<AllCarWashResponse> call = apiInterface.getCarWash(string);
        call.enqueue(new Callback<AllCarWashResponse>() {
            @Override
            public void onResponse(Call<AllCarWashResponse> call, Response<AllCarWashResponse> response) {
                if (response.code() == 200) {
                    carWash = response.body();
                    name.setText(carWash.getName());
                    adres.setText(carWash.getAddress());
                    description.setText(carWash.getDescription());
                    setWorkTime(carWash);
                    setBoxTime(carWash);
                    setPackageBlock(carWash);
//                    setServicePricesBlock(carWash);

                    for (int i = 0; i < carWash.getServicePrices().size(); i++) {
                        if (carWash.getServicePrices().get(i).getName() != null) {
                            customServiceMap.put(carWash.getServicePrices().get(i).getName(), carWash.getServicePrices().get(i).getServiceId());
                        } else {
//                            customServiceMap.put(carWash.getServicePrices().get(i).getOriginalName(), carWash.getServicePrices().get(i).getServiceId());
                        }

                        if (idSet.contains(carWash.getServicePrices().get(i).getBusinessServiceId())) {
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
//        for (int i = 0; i < carWash.getPackages().size(); i++) {
////        for (int i = 0; i < 10; i++) {
//            Button button = new Button(CarWashActivity.this);
//            button.setText(carWash.getPackages().get(i).getName());
//            packagesBlock.addView(button);
//        }

    }

    private void setBoxTime(AllCarWashResponse carWash) {
        Section<String, SpacesItem> section = new Section<>();
        section.parent = "Загруженность боксов";
        for (int i = 0; i < carWash.getSpaces().size(); i++) {
            section.children.add(carWash.getSpaces().get(i));
        }
        expandableBoxBlock.addSection(section);

    }

    private void setWorkTime(AllCarWashResponse carWash) {
        Section<String, WorkTimesItem> section = new Section<>();
        section.parent = "Рабочее время";
        for (int i = 0; i < carWash.getWorkTimes().size(); i++) {
            section.children.add(carWash.getWorkTimes().get(i));
        }
        expandableWorkTime.addSection(section);
    }

    private void initView() {
        name = (TextView) findViewById(R.id.name);
        adres = (TextView) findViewById(R.id.address);
        description = (TextView) findViewById(R.id.description);
        boxBlock = (LinearLayout) findViewById(R.id.boxBlock);
//        workTimesDropdown = (DropdownTextView) findViewById(R.id.workTimes_dropdown);
        materialButton = (MaterialButton) findViewById(R.id.materialButton);
        materialButton3 = (MaterialButton) findViewById(R.id.materialButton3);

        address = (TextView) findViewById(R.id.address);
        calendarView = (CollapsibleCalendar) findViewById(R.id.calendarView);
        packagesBlock = (LinearLayout) findViewById(R.id.packagesBlock);
        imageView2 = (ImageView) findViewById(R.id.imageView2);
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

        expandableWorkTime = findViewById(R.id.expandableWorkTime);
        expandableWorkTime.setRenderer(new ExpandableLayout.Renderer<String, WorkTimesItem>() {
            @Override
            public void renderParent(View view, String model, boolean isExpanded, int parentPosition) {
                ((TextView) view.findViewById(R.id.parent)).setText(model);
                view.findViewById(R.id.arrow).setBackgroundResource(isExpanded ? R.drawable.arrow_up : R.drawable.arrow_down);
            }

            @Override
            public void renderChild(View view, WorkTimesItem model, int parentPosition, int childPosition) {
                switch (model.getDayOfWeek()) {
                    case "MONDAY":
                        ((TextView) view.findViewById(R.id.child)).setText("MONDAY: " + model.getFrom() + "-" + model.getTo());
                        break;
                    case "TUESDAY":
                        ((TextView) view.findViewById(R.id.child)).setText("TUESDAY: " + model.getFrom() + "-" + model.getTo());
                        break;
                    case "WEDNESDAY":
                        ((TextView) view.findViewById(R.id.child)).setText("WEDNESDAY: " + model.getFrom() + "-" + model.getTo());
                        break;
                    case "THURSDAY":
                        ((TextView) view.findViewById(R.id.child)).setText("THURSDAY: " + model.getFrom() + "-" + model.getTo());
                        break;
                    case "FRIDAY":
                        ((TextView) view.findViewById(R.id.child)).setText("FRIDAY: " + model.getFrom() + "-" + model.getTo());
                        break;
                    case "SATURDAY":
                        ((TextView) view.findViewById(R.id.child)).setText("SATURDAY: " + model.getFrom() + "-" + model.getTo());
                        break;
                    case "SUNDAY":
                        ((TextView) view.findViewById(R.id.child)).setText("SUNDAY: " + model.getFrom() + "-" + model.getTo());
                        break;
                }

            }
        });

        expandableBoxBlock = findViewById(R.id.expandableBoxBlock);
        expandableBoxBlock.setRenderer(new ExpandableLayout.Renderer<String, SpacesItem>() {
            @Override
            public void renderParent(View view, String model, boolean isExpanded, int parentPosition) {
                ((TextView) view.findViewById(R.id.parent)).setText(model);
                view.findViewById(R.id.arrow).setBackgroundResource(isExpanded ? R.drawable.arrow_up : R.drawable.arrow_down);
            }

            @Override
            public void renderChild(View view, SpacesItem model, int parentPosition, int childPosition) {
                ((TextView) view.findViewById(R.id.boxName)).setText("Бокс №2");
                LinearLayout linearLayout = view.findViewById(R.id.boxView);
                TimelineView timelineBox = new TimelineView(CarWashActivity.this);
                timelineBox.setTimeRange(carWash.getWorkTimes().get(0).getFrom() + "-" + carWash.getWorkTimes().get(0).getTo());
                timelineBox.setTimeTextInterval(2);
                timelineBox.setFractionTextSize(30);
                timelineBox.setBarWidth(75);
                timelineBox.setFractionLineLength(20);
                timelineBox.setBarColorAvailable(Color.parseColor("#FF0000"));
                timelineBox.setBarColorNotAvailable(Color.parseColor("#00FF00"));
                timelineBox.setOnClickListener(CarWashActivity.this);
                linearLayout.addView(timelineBox);

//                TimelineView timelineBox0 = boxBlock.findViewById(0);
//                List<String> timeRange = new ArrayList<>();
//                timeRange.add("11:00-12:00");
//                timeRange.add("14:00-16:00");
//                timeRange.add("17:30-19:00");
//                timelineBox0.setAvailableTimeRange(timeRange);
            }
        });
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
}
