package com.gliesereum.karma.ui;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
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

import com.appizona.yehiahd.fastsave.FastSave;
import com.github.okdroid.checkablechipview.CheckableChipView;
import com.gliesereum.karma.R;
import com.gliesereum.karma.RecordListActivity;
import com.gliesereum.karma.data.network.APIClient;
import com.gliesereum.karma.data.network.APIInterface;
import com.gliesereum.karma.data.network.json.carwash.AllCarWashResponse;
import com.gliesereum.karma.data.network.json.carwash.PackagesItem;
import com.gliesereum.karma.data.network.json.carwash.ServicePricesItem;
import com.gliesereum.karma.data.network.json.carwash.ServicesItem;
import com.gliesereum.karma.data.network.json.filter.AttributesItem;
import com.gliesereum.karma.data.network.json.order.OrderBody;
import com.gliesereum.karma.data.network.json.order.OrderResponse;
import com.gliesereum.karma.util.ErrorHandler;
import com.gliesereum.karma.util.Util;
import com.gohn.nativedialog.ButtonType;
import com.gohn.nativedialog.NDialog;
import com.google.android.material.button.MaterialButton;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import smartdevelop.ir.eram.showcaseviewlib.GuideView;
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType;
import smartdevelop.ir.eram.showcaseviewlib.listener.GuideListener;

import static com.gliesereum.karma.util.Constants.ACCESS_TOKEN;
import static com.gliesereum.karma.util.Constants.CAR_FILTER_LIST;
import static com.gliesereum.karma.util.Constants.CAR_ID;
import static com.gliesereum.karma.util.Constants.ORDER_ACTIVITY;

public class OrderActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "TAG";
    private APIInterface apiInterface;
    private ErrorHandler errorHandler;
    private OrderBody orderBody;
    private ProgressDialog progressDialog;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        initData();
        initView();
        setPackages(carWash);
        showTutorial();
    }

    private void initData() {
        apiInterface = APIClient.getClient().create(APIInterface.class);
        errorHandler = new ErrorHandler(this, this);
        carWash = FastSave.getInstance().getObject("carWash", AllCarWashResponse.class);
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
        textView19 = findViewById(R.id.textView19);
        servicePriceItem = findViewById(R.id.servicePriceItem);
        orderButton = findViewById(R.id.orderButton);
        orderButton.setOnClickListener(this);
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
                            .setContentText("Ознакомтесь с пакетами услуг данной мойки тут")
                            .setTargetView(horizontalScrollView)
                            .setDismissType(DismissType.anywhere)
                            .setGuideListener(new GuideListener() {
                                @Override
                                public void onDismiss(View view) {
                                    new GuideView.Builder(OrderActivity.this)
                                            .setTitle("Заказать мойку")
                                            .setContentText("После того как Вы все выбрали, можете заказывать мойку. У Вас будет возмодность заказать мойку на ближайшее вермя или забронировать на удобное для Вас")
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
        for (int i = 0; i < carWash.getServicePrices().size(); i++) {
            if (FastSave.getInstance().getObjectsList(CAR_FILTER_LIST, AttributesItem.class).containsAll(carWash.getServicePrices().get(i).getAttributes())) {
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
        View layout2 = LayoutInflater.from(this).inflate(R.layout.package_btn2, packageScroll, false);
        MaterialButton packageBtn = layout2.findViewById(R.id.packageBtn);
        packageBtn.setText("Не выбран");
        packageBtn.setTag("default");
        packageBtn.setOnClickListener(v -> {
            for (int j = 0; j < packageScroll.getChildCount(); j++) {
                ConstraintLayout constraintLayout = ((ConstraintLayout) packageScroll.getChildAt(j));
                if (constraintLayout.getChildAt(0).getTag().equals(v.getTag())) {
                    Log.d(TAG, "onClick: 1");
                    ((MaterialButton) constraintLayout.getChildAt(0)).setBackgroundTintList(ContextCompat.getColorStateList(OrderActivity.this, R.color.accent));


                } else {
                    Log.d(TAG, "onClick: 2");
                    ((MaterialButton) constraintLayout.getChildAt(0)).setBackgroundTintList(ContextCompat.getColorStateList(OrderActivity.this, R.color.white));


                }
            }
            serviceMap.clear();
            nameOfServiceList.clear();
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
                        Log.d(TAG, "onClick: 3");
                        ((MaterialButton) constraintLayout.getChildAt(0)).setBackgroundTintList(ContextCompat.getColorStateList(OrderActivity.this, R.color.accent));


                    } else {
                        Log.d(TAG, "onClick: 4");
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

    }

    public void showProgressDialog() {
        progressDialog = ProgressDialog.show(this, "Ща сек...", "Ща все сделаю...");

    }

    public void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
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

    private void openPreOrderDialog() {
        NDialog preOrderNewDialog = new NDialog(OrderActivity.this, ButtonType.NO_BUTTON);
        preOrderNewDialog.isCancelable(true);
        preOrderNewDialog.setCustomView(R.layout.pre_order_new_dialod);
        List<View> childViews = preOrderNewDialog.getCustomViewChildren();
        for (View childView : childViews) {
            switch (childView.getId()) {
                case R.id.timeOrderBtn:
                    Button okBtn = childView.findViewById(R.id.timeOrderBtn);
                    okBtn.setOnClickListener(v -> {
                        preOrderNewDialog.dismiss();
                        showDateTimePicker();
                    });
                    break;
                case R.id.nowOrderBtn:
                    Button nowOrderBtn = childView.findViewById(R.id.nowOrderBtn);
                    nowOrderBtn.setOnClickListener(v -> {
                        preOrderNewDialog.dismiss();
                        getRecordFreeTime(true);
                    });
                    break;
            }
        }
        preOrderNewDialog.show();
    }

    private void getRecordFreeTime(boolean nowFlag) {
        showProgressDialog();
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
            Log.d(TAG, "getRecordFreeTime: ");
        }
        orderBody.setServicesIds(list);
        Call<OrderResponse> call = apiInterface.preOrder(FastSave.getInstance().getString(ACCESS_TOKEN, ""), orderBody);
        call.enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                if (response.code() == 200) {
                    if (response.body().getBegin() != null && response.body().getWorkingSpaceId() != null) {
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
                                            showProgressDialog();
                                            Call<OrderResponse> call = apiInterface.doOrder(FastSave.getInstance().getString(ACCESS_TOKEN, ""), orderBody);
                                            call.enqueue(new Callback<OrderResponse>() {
                                                @Override
                                                public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                                                    if (response.code() == 200) {
                                                        closeProgressDialog();
                                                        nDialog.dismiss();
                                                        Toast.makeText(OrderActivity.this, "Запись добавленна в список", Toast.LENGTH_SHORT).show();
//                                                        startActivity(new Intent(OrderActivity.this, MapsActivity.class));
                                                        startActivity(new Intent(OrderActivity.this, RecordListActivity.class));
                                                        finish();
                                                    } else {
                                                        try {
                                                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                                                            errorHandler.showError(jObjError.getInt("code"));
                                                            closeProgressDialog();
                                                            nDialog.dismiss();
                                                        } catch (Exception e) {
                                                            errorHandler.showCustomError(e.getMessage());
                                                            closeProgressDialog();
                                                            nDialog.dismiss();
                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<OrderResponse> call, Throwable t) {
                                                    errorHandler.showCustomError(t.getMessage());
                                                    closeProgressDialog();
                                                    nDialog.dismiss();
                                                }
                                            });
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
                        closeProgressDialog();
                    } else {
                        Toast.makeText(OrderActivity.this, "Что то пошло не так", Toast.LENGTH_SHORT).show();
                        closeProgressDialog();
                    }
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
            public void onFailure(Call<OrderResponse> call, Throwable t) {
                errorHandler.showCustomError(t.getMessage());
                closeProgressDialog();
            }
        });
    }
}
