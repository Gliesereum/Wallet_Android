package com.gliesereum.coupler.ui;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.github.okdroid.checkablechipview.CheckableChipView;
import com.gliesereum.coupler.R;
import com.gliesereum.coupler.data.network.APIClient;
import com.gliesereum.coupler.data.network.APIInterface;
import com.gliesereum.coupler.data.network.CustomCallback;
import com.gliesereum.coupler.data.network.json.carwash.AllCarWashResponse;
import com.gliesereum.coupler.data.network.json.carwash.PackagesItem;
import com.gliesereum.coupler.data.network.json.carwash.ServicePricesItem;
import com.gliesereum.coupler.data.network.json.carwash.ServicesItem;
import com.gliesereum.coupler.data.network.json.filter.AttributesItem;
import com.gliesereum.coupler.data.network.json.filter.ServiceClassItem;
import com.gliesereum.coupler.data.network.json.order.OrderBody;
import com.gliesereum.coupler.data.network.json.order.OrderResponse;
import com.gliesereum.coupler.data.network.json.record.AllRecordResponse;
import com.gliesereum.coupler.util.FastSave;
import com.gliesereum.coupler.util.Util;
import com.gohn.nativedialog.ButtonType;
import com.gohn.nativedialog.NDialog;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Response;
import smartdevelop.ir.eram.showcaseviewlib.GuideView;
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType;
import smartdevelop.ir.eram.showcaseviewlib.listener.GuideListener;

import static com.gliesereum.coupler.util.Constants.ACCESS_TOKEN;
import static com.gliesereum.coupler.util.Constants.CARWASH;
import static com.gliesereum.coupler.util.Constants.CAR_FILTER_LIST;
import static com.gliesereum.coupler.util.Constants.CAR_ID;
import static com.gliesereum.coupler.util.Constants.CAR_SERVICE_CLASS;
import static com.gliesereum.coupler.util.Constants.FRIDAY;
import static com.gliesereum.coupler.util.Constants.MONDAY;
import static com.gliesereum.coupler.util.Constants.ORDER_ACTIVITY;
import static com.gliesereum.coupler.util.Constants.RECORD;
import static com.gliesereum.coupler.util.Constants.SATURDAY;
import static com.gliesereum.coupler.util.Constants.SUNDAY;
import static com.gliesereum.coupler.util.Constants.THURSDAY;
import static com.gliesereum.coupler.util.Constants.TUESDAY;
import static com.gliesereum.coupler.util.Constants.WEDNESDAY;

public class OrderActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "TAG";
    private APIInterface API;
    private CustomCallback customCallback;
    private OrderBody orderBody;
    private AllCarWashResponse carWash;
    private Long begin = 0L;
    private Map<String, PackagesItem> packageMap;
    private Map<String, ServicePricesItem> servicePriceMap;
    private Map<String, ServicesItem> serviceMap;
    private List<String> nameOfServiceList;
    private Calendar date;
    private CardView cardView2;
    private TextView durationLabel;
    private TextView priceLabel;
    private TextView discountTextView;
    private HorizontalScrollView horizontalScrollView;
    private LinearLayout packageScroll;
    private ConstraintLayout packageBlock;
    private LinearLayout packageItems;
    private TextView textView19;
    private LinearLayout servicePriceItem;
    private Button orderButton;
    private TextView packageLabel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (FastSave.getInstance().getBoolean("NIGHT_THEME", false)) {
            setTheme(R.style.AppThemeDark);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

//        ContextThemeWrapper themeWrapper = new ContextThemeWrapper(this, R.style.AppThemeRed);
//        LayoutInflater layoutInflater = LayoutInflater.from(themeWrapper);
//        ViewGroup viewContainer = findViewById(R.id.testColor);
//        viewContainer.removeAllViews();
//        layoutInflater.inflate(R.layout.activity_order, viewContainer, true);

        initData();
        initView();
        setPackages(carWash);
        showTutorial();
    }

    private void initData() {
        API = APIClient.getClient().create(APIInterface.class);
        customCallback = new CustomCallback(this, this);
        carWash = FastSave.getInstance().getObject(CARWASH, AllCarWashResponse.class);
        orderBody = new OrderBody();
        packageMap = new HashMap<>();
        servicePriceMap = new HashMap<>();
        serviceMap = new HashMap<>();
        nameOfServiceList = new ArrayList<>();
        fillServicePackageMap();
    }

    private void initView() {
        cardView2 = findViewById(R.id.cardView2);
        durationLabel = findViewById(R.id.durationLabel);
        priceLabel = findViewById(R.id.priceLabel);
        discountTextView = findViewById(R.id.discountTextView);
        horizontalScrollView = findViewById(R.id.horizontalScrollView);
        packageScroll = findViewById(R.id.packageScroll);
        packageBlock = findViewById(R.id.packageBlock);
        packageItems = findViewById(R.id.packageItems);
        textView19 = findViewById(R.id.servicePriceLabel);
        servicePriceItem = findViewById(R.id.servicePriceItem);
        orderButton = findViewById(R.id.orderButton);
        orderButton.setOnClickListener(this);
        packageLabel = findViewById(R.id.packageLabel);
    }

    private void showTutorial() {
        if (FastSave.getInstance().getBoolean(ORDER_ACTIVITY, true)) {
            new GuideView.Builder(OrderActivity.this)
                    .setTitle("Информация о заказе")
                    .setContentText("Тут отображается время и цена выбранных вами услуг")
                    .setTargetView(cardView2)
                    .setDismissType(DismissType.anywhere)
                    .setGuideListener(view -> new GuideView.Builder(OrderActivity.this)
                            .setTitle("Пакеты услуг")
                            .setContentText("Ознакомтесь с пакетами услуг тут")
                            .setTargetView(horizontalScrollView)
                            .setDismissType(DismissType.anywhere)
                            .setGuideListener(new GuideListener() {
                                @Override
                                public void onDismiss(View view) {
                                    new GuideView.Builder(OrderActivity.this)
                                            .setTitle("Заказать мойку")
                                            .setContentText("После того как Вы все выбрали, можете совершить заказ. У Вас будет возможность совершить заказ на ближайшее вермя или забронировать на удобное для Вас")
                                            .setTargetView(orderButton)
                                            .setDismissType(DismissType.anywhere)
                                            .setGuideListener(new GuideListener() {
                                                @Override
                                                public void onDismiss(View view) {
                                                    FastSave.getInstance().saveBoolean(ORDER_ACTIVITY, false);
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

    private void setServicePrices(AllCarWashResponse carWash) {
        servicePriceItem.removeAllViews();
        packageItems.removeAllViews();
        List<AttributesItem> objectsList = FastSave.getInstance().getObjectsList(CAR_FILTER_LIST, AttributesItem.class);
        List<ServiceClassItem> objectsList1 = FastSave.getInstance().getObjectsList(CAR_SERVICE_CLASS, ServiceClassItem.class);
        for (int i = 0; i < carWash.getServicePrices().size(); i++) {
            if (FastSave.getInstance().getObjectsList(CAR_FILTER_LIST, AttributesItem.class).containsAll(carWash.getServicePrices().get(i).getAttributes()) && FastSave.getInstance().getObjectsList(CAR_SERVICE_CLASS, ServiceClassItem.class).containsAll(carWash.getServicePrices().get(i).getServiceClass())) {
                if (!serviceMap.containsKey(carWash.getServicePrices().get(i).getId())) {
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams.setMargins(0, 4, 0, 4);
                    CheckableChipView checkableChipView = new CheckableChipView(OrderActivity.this);
                    checkableChipView.setText(carWash.getServicePrices().get(i).getName() + "\n" + getString(R.string.timeUNICODE) + carWash.getServicePrices().get(i).getDuration() + " мин        " + getString(R.string.moneyUNICODE) + carWash.getServicePrices().get(i).getPrice() + " грн");
                    checkableChipView.setTag(carWash.getServicePrices().get(i).getId());
                    checkableChipView.setTag(R.string.tagKeyDuration, carWash.getServicePrices().get(i).getDuration());
                    checkableChipView.setTag(R.string.tagKeyPrice, carWash.getServicePrices().get(i).getPrice());
                    checkableChipView.setOutlineCornerRadius(10f);
                    checkableChipView.setBackgroundColor(getResources().getColor(R.color.white));
                    checkableChipView.setOutlineColor(getResources().getColor(R.color.black));
                    checkableChipView.setCheckedColor(getResources().getColor(R.color.accent));
                    checkableChipView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (((CheckableChipView) v).isChecked()) {
                                durationLabel.setText(String.valueOf(Integer.parseInt(durationLabel.getText().toString()) + ((int) v.getTag(R.string.tagKeyDuration))));
                                priceLabel.setText(String.valueOf(Integer.parseInt(priceLabel.getText().toString()) + ((int) v.getTag(R.string.tagKeyPrice))));
                            } else {
                                durationLabel.setText(String.valueOf(Integer.parseInt(durationLabel.getText().toString()) - ((int) v.getTag(R.string.tagKeyDuration))));
                                priceLabel.setText(String.valueOf(Integer.parseInt(priceLabel.getText().toString()) - ((int) v.getTag(R.string.tagKeyPrice))));
                            }
                        }
                    });
                    servicePriceItem.addView(checkableChipView, layoutParams);
                } else {
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams.setMargins(0, 4, 0, 4);
                    CheckableChipView checkableChipView = new CheckableChipView(OrderActivity.this);
                    checkableChipView.setText(carWash.getServicePrices().get(i).getName() + "\n" + getString(R.string.timeUNICODE) + carWash.getServicePrices().get(i).getDuration() + " мин        " + getString(R.string.moneyUNICODE) + carWash.getServicePrices().get(i).getPrice() + " грн");
                    checkableChipView.setTag(carWash.getServicePrices().get(i).getId());
                    checkableChipView.setOutlineCornerRadius(10f);
                    checkableChipView.setBackgroundColor(getResources().getColor(R.color.white));
                    checkableChipView.setOutlineColor(getResources().getColor(R.color.black));
                    checkableChipView.setCheckedColor(getResources().getColor(R.color.material_drawer_selected));
                    checkableChipView.setChecked(true);
                    checkableChipView.setEnabled(false);
                    packageItems.addView(checkableChipView, 0, layoutParams);
                }
            } else {
                Log.d(TAG, "false: " + carWash.getServicePrices().get(i).getName());
            }
        }

    }

    private void fillServicePackageMap() {
        for (int i = 0; i < carWash.getPackages().size(); i++) {
            packageMap.put(carWash.getPackages().get(i).getId(), carWash.getPackages().get(i));
        }
        for (int i = 0; i < carWash.getServicePrices().size(); i++) {
            servicePriceMap.put(carWash.getServicePrices().get(i).getId(), carWash.getServicePrices().get(i));
        }
    }

    private void setPackages(AllCarWashResponse carWash) {
        if (carWash.getPackages().size() != 0) {
            View layout2 = LayoutInflater.from(this).inflate(R.layout.package_btn2, packageScroll, false);
            MaterialButton packageBtn = layout2.findViewById(R.id.packageBtn);
            packageBtn.setText("Не выбран");
            packageBtn.setTag("default");
            packageBtn.setOnClickListener(v -> {
                for (int j = 0; j < packageScroll.getChildCount(); j++) {
                    ConstraintLayout constraintLayout = ((ConstraintLayout) packageScroll.getChildAt(j));
                    if (constraintLayout.getChildAt(0).getTag().equals(v.getTag())) {
                        ((MaterialButton) constraintLayout.getChildAt(0)).setBackgroundTintList(ContextCompat.getColorStateList(OrderActivity.this, R.color.accent));
                    } else {
                        ((MaterialButton) constraintLayout.getChildAt(0)).setBackgroundTintList(ContextCompat.getColorStateList(OrderActivity.this, R.color.white));
                    }
                }
                serviceMap.clear();
                nameOfServiceList.clear();
                orderBody.setPackageId(null);
                priceLabel.setText("0");
                durationLabel.setText("0");
                discountTextView.setText("0%");
                textView19.setText("Выберите услуги");
                packageBlock.setVisibility(View.GONE);
                setServicePrices(carWash);
            });
            packageScroll.addView(layout2);
            for (int i = 0; i < carWash.getPackages().size(); i++) {
                layout2 = LayoutInflater.from(this).inflate(R.layout.package_btn2, packageScroll, false);
                packageBtn = layout2.findViewById(R.id.packageBtn);
                packageBtn.setText(carWash.getPackages().get(i).getName());
                packageBtn.setTag(carWash.getPackages().get(i).getId());
                packageBtn.setOnClickListener(v -> {
                    serviceMap.clear();
                    nameOfServiceList.clear();
                    orderBody.setPackageId((String) v.getTag());
                    for (int j = 0; j < packageScroll.getChildCount(); j++) {
                        ConstraintLayout constraintLayout = ((ConstraintLayout) packageScroll.getChildAt(j));
                        if (constraintLayout.getChildAt(0).getTag().equals(v.getTag())) {
                            ((MaterialButton) constraintLayout.getChildAt(0)).setBackgroundTintList(ContextCompat.getColorStateList(OrderActivity.this, R.color.accent));


                        } else {
                            ((MaterialButton) constraintLayout.getChildAt(0)).setBackgroundTintList(ContextCompat.getColorStateList(OrderActivity.this, R.color.white));

                        }
                    }
                    for (int j = 0; j < packageMap.get(v.getTag()).getServices().size(); j++) {
                        nameOfServiceList.add(packageMap.get(v.getTag()).getServices().get(j).getName());
                        serviceMap.put(packageMap.get(v.getTag()).getServices().get(j).getId(), packageMap.get(v.getTag()).getServices().get(j));
                    }
                    discountTextView.setText(String.valueOf(packageMap.get(v.getTag()).getDiscount()) + "%");
                    setServicePrices(carWash);
                    String duration = String.valueOf(packageMap.get(v.getTag()).getDuration());
                    durationLabel.setText(duration);
                    double price = 0;
                    for (int j = 0; j < packageMap.get(v.getTag()).getServices().size(); j++) {
                        price += packageMap.get(v.getTag()).getServices().get(j).getPrice();
                    }
                    priceLabel.setText(String.valueOf((int) (price - ((price / 100) * packageMap.get(v.getTag()).getDiscount()))));
                    packageBlock.setVisibility(View.VISIBLE);
                    textView19.setText("Дополнительные услуги");
                });
                packageScroll.addView(layout2);
                ConstraintLayout constraintLayout = ((ConstraintLayout) packageScroll.getChildAt(0));
                ((MaterialButton) constraintLayout.getChildAt(0)).performClick();
            }

            setServicePrices(carWash);
        } else {
            packageScroll.setVisibility(View.GONE);
            packageLabel.setVisibility(View.GONE);
            setServicePrices(carWash);
        }
    }

    public void showDateTimePicker() {
        final Calendar currentDate = Calendar.getInstance();
        date = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        new DatePickerDialog(OrderActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                date.set(year, monthOfYear, dayOfMonth);
                new TimePickerDialog(OrderActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        date.set(Calendar.MINUTE, minute);
                        begin = date.getTimeInMillis();
                        getRecordFreeTime(false);
                    }
                }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), true).show();
            }
        },
                currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.orderButton:
                openPreOrderDialog();
                break;
        }
    }

    private boolean checkCarWashWorkTime() {
        String dayOfWeek = "";
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        switch (day) {
            case Calendar.MONDAY:
                dayOfWeek = MONDAY;
                break;
            case Calendar.TUESDAY:
                dayOfWeek = TUESDAY;
                break;
            case Calendar.WEDNESDAY:
                dayOfWeek = WEDNESDAY;
                break;
            case Calendar.THURSDAY:
                dayOfWeek = THURSDAY;
                break;
            case Calendar.FRIDAY:
                dayOfWeek = FRIDAY;
                break;
            case Calendar.SATURDAY:
                dayOfWeek = SATURDAY;
                break;
            case Calendar.SUNDAY:
                dayOfWeek = SUNDAY;
                break;
        }

        for (int i = 0; i < carWash.getWorkTimes().size(); i++) {
            if (carWash.getWorkTimes().get(i).getDayOfWeek().equals(dayOfWeek)) {
                if (carWash.getWorkTimes().get(i).getFrom() < (System.currentTimeMillis() + (carWash.getTimeZone() * 60000)) && carWash.getWorkTimes().get(i).getTo() > (System.currentTimeMillis() + (carWash.getTimeZone() * 60000))) {
                    return true;
                }
            }
        }
        return false;
    }

    private void openPreOrderDialog() {
        NDialog preOrderNewDialog = new NDialog(OrderActivity.this, ButtonType.NO_BUTTON);
        preOrderNewDialog.isCancelable(true);
        preOrderNewDialog.setCustomView(R.layout.pre_order_new_dialod);
        List<View> childViews = preOrderNewDialog.getCustomViewChildren();
        for (View childView : childViews) {
            switch (childView.getId()) {
                case R.id.preOrderLabel1:
                    TextView preOrderLabel1 = childView.findViewById(R.id.preOrderLabel1);
                    if (Util.checkCarWashWorkTime(carWash)) {
                        preOrderLabel1.setText("У Вас есть возможность заказать мойку на ближайшее или на выбраное время");
                    } else {
                        preOrderLabel1.setText("Мойка сейчас не работает. у  Вас есть возможность заказать мойку только на выбранное время");
                    }
                    break;
                case R.id.preOrderLabel2:
                    TextView preOrderLabel2 = childView.findViewById(R.id.preOrderLabel2);
                    if (Util.checkCarWashWorkTime(carWash)) {
                        preOrderLabel2.setText("Пожайлуста, сделайте свой выбор");
                    } else {
                        preOrderLabel2.setText("");
                    }
                    break;
                case R.id.timeOrderBtn:
                    Button okBtn = childView.findViewById(R.id.timeOrderBtn);
                    okBtn.setOnClickListener(v -> {
                        preOrderNewDialog.dismiss();
                        showDateTimePicker();
                    });
                    break;
                case R.id.nowOrderBtn:
                    if (Util.checkCarWashWorkTime(carWash)) {
                        Button nowOrderBtn = childView.findViewById(R.id.nowOrderBtn);
                        nowOrderBtn.setOnClickListener(v -> {
                            preOrderNewDialog.dismiss();
                            getRecordFreeTime(true);
                        });
                    } else {
                        Button nowOrderBtn = childView.findViewById(R.id.nowOrderBtn);
                        nowOrderBtn.setEnabled(false);
                    }
                    break;
            }
        }
        preOrderNewDialog.show();
    }

    private void getRecordFreeTime(boolean nowFlag) {
        orderBody.setWorkingSpaceId(null);
        orderBody.setTargetId(FastSave.getInstance().getString(CAR_ID, ""));
        orderBody.setBusinessId(carWash.getId());
        if (nowFlag) {
            orderBody.setBegin(System.currentTimeMillis() + (carWash.getTimeZone() * 60000));
        } else {
            orderBody.setBegin(begin);
        }
        orderBody.setDescription("Android");
        List<String> list = new ArrayList<>();
        for (int i = 0; i < servicePriceItem.getChildCount(); i++) {
            if (((CheckableChipView) servicePriceItem.getChildAt(i)).isChecked()) {
                list.add((String) ((CheckableChipView) servicePriceItem.getChildAt(i)).getTag());
            }
        }
        orderBody.setServicesIds(list);
        API.preOrder(FastSave.getInstance().getString(ACCESS_TOKEN, ""), orderBody)
                .enqueue(customCallback.getResponseWithProgress(new CustomCallback.ResponseCallback<OrderResponse>() {
                    @Override
                    public void onSuccessful(Call<OrderResponse> call, Response<OrderResponse> response) {
                        orderBody.setWorkingSpaceId(response.body().getWorkingSpaceId());
                        orderBody.setBegin(response.body().getBegin());
                        NDialog nDialog = new NDialog(OrderActivity.this, ButtonType.NO_BUTTON);
                        nDialog.isCancelable(false);
                        nDialog.setCustomView(R.layout.order_view);
                        List<View> childViews = nDialog.getCustomViewChildren();
                        for (View childView : childViews) {
                            switch (childView.getId()) {
                                case R.id.time:
                                    TextView time = childView.findViewById(R.id.time);
                                    time.setText(Util.getStringTime(response.body().getBegin()));
                                    break;
                                case R.id.okBtn:
                                    Button okBtn = childView.findViewById(R.id.okBtn);
                                    okBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            API.doOrder(FastSave.getInstance().getString(ACCESS_TOKEN, ""), orderBody)
                                                    .enqueue(customCallback.getResponseWithProgress(new CustomCallback.ResponseCallback<AllRecordResponse>() {
                                                        @Override
                                                        public void onSuccessful(Call<AllRecordResponse> call, Response<AllRecordResponse> response) {
                                                            nDialog.dismiss();
                                                            Toast.makeText(OrderActivity.this, "Запись добавленна в список", Toast.LENGTH_SHORT).show();
//                                                            startActivity(new Intent(OrderActivity.this, RecordListActivity.class));
                                                            FastSave.getInstance().saveObject(RECORD, response.body());
                                                            startActivity(new Intent(OrderActivity.this, SingleRecordActivity.class));
                                                            finish();
                                                        }

                                                        @Override
                                                        public void onEmpty(Call<AllRecordResponse> call, Response<AllRecordResponse> response) {

                                                        }
                                                    }));
                                        }
                                    });
                                    break;
                                case R.id.backBtn:
                                    Button backBtn = childView.findViewById(R.id.backBtn);
                                    backBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            nDialog.dismiss();
                                        }
                                    });
                                    break;
                            }
                        }
                        nDialog.show();
                    }

                    @Override
                    public void onEmpty(Call<OrderResponse> call, Response<OrderResponse> response) {

                    }
                }));
    }
}
