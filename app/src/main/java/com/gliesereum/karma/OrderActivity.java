package com.gliesereum.karma;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.appizona.yehiahd.fastsave.FastSave;
import com.elconfidencial.bubbleshowcase.BubbleShowCaseBuilder;
import com.elconfidencial.bubbleshowcase.BubbleShowCaseSequence;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.gliesereum.karma.util.Constants.ACCESS_TOKEN;
import static com.gliesereum.karma.util.Constants.CAR_BODY;
import static com.gliesereum.karma.util.Constants.CAR_FILTER_LIST;
import static com.gliesereum.karma.util.Constants.CAR_ID;

public class OrderActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "TAG";
    private Button orderButton;
    private APIInterface apiInterface;
    private ErrorHandler errorHandler;
    private OrderBody orderBody = new OrderBody();
    private ProgressDialog progressDialog;
    private MaterialButton nowOrderBtn;
    private MaterialButton timeOrderBtn;
    private HorizontalScrollView horizontalScrollView;
    private LinearLayout packageScroll;
    private TextView textView10;
    private TextView textView11;
    private TextView textView12;
    private TextView packagesDescription;
    private TextView textView14;
    private AllCarWashResponse carWash;
    private Long begin = 0L;
    private Boolean nowFlag = true;
    private Map<String, PackagesItem> packageMap = new HashMap<>();
    private Map<String, ServicePricesItem> servicePriceMap = new HashMap<>();
    private Map<String, ServicesItem> serviceMap = new HashMap<>();
    private LinearLayout servicePriceBlock;
    private List<String> nameOfServiceList = new ArrayList<>();
    private Calendar date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FastSave.init(getApplicationContext());
        setContentView(R.layout.activity_order);
        carWash = FastSave.getInstance().getObject("carWash", AllCarWashResponse.class);

        for (int i = 0; i < carWash.getPackages().size(); i++) {
            packageMap.put(carWash.getPackages().get(i).getId(), carWash.getPackages().get(i));
        }
        for (int i = 0; i < carWash.getServicePrices().size(); i++) {
            servicePriceMap.put(carWash.getServicePrices().get(i).getId(), carWash.getServicePrices().get(i));
        }
        initView();
        nowOrderBtn.performClick();
        setPackages(carWash);
        showTutorial();
    }

    private void showTutorial() {
        BubbleShowCaseBuilder nowOrderTutorial = new BubbleShowCaseBuilder(this) //Activity instance
                .title("Нажмите тут что б заказать мойку на ближайшее время") //Any title for the bubble view
                .backgroundColorResourceId(R.color.colorAccent)
                .textColorResourceId(R.color.black)
                .showOnce("OrderActivity")
                .targetView(nowOrderBtn); //View to point out

        BubbleShowCaseBuilder timeOrderTutorial = new BubbleShowCaseBuilder(this) //Activity instance
                .title("Нажмите тут что б заказать мойку на удобное для Вас время")//Any title for the bubble view
                .backgroundColorResourceId(R.color.colorAccent)
                .textColorResourceId(R.color.black)
                .showOnce("OrderActivity")
                .targetView(timeOrderBtn); //View to point out

        BubbleShowCaseBuilder packageTutorial = new BubbleShowCaseBuilder(this) //Activity instance
                .title("Выберите пакет услуг. Будет скидка!)")//Any title for the bubble view
                .backgroundColorResourceId(R.color.colorAccent)
                .textColorResourceId(R.color.black)
                .showOnce("OrderActivity")
                .targetView(packageScroll); //View to point out

        BubbleShowCaseBuilder serviceTutorial = new BubbleShowCaseBuilder(this) //Activity instance
                .title("Так же можете выбрать дополнительные услуги к пакетам")//Any title for the bubble view
                .backgroundColorResourceId(R.color.colorAccent)
                .textColorResourceId(R.color.black)
                .showOnce("OrderActivity")
                .targetView(servicePriceBlock); //View to point out

        BubbleShowCaseBuilder orderTutorial = new BubbleShowCaseBuilder(this) //Activity instance
                .title("После того как все выбрали, можете заказать мойку автомобиля")//Any title for the bubble view
                .backgroundColorResourceId(R.color.colorAccent)
                .textColorResourceId(R.color.black)
                .showOnce("OrderActivity")
                .targetView(orderButton); //View to point out

        new BubbleShowCaseSequence()
                .addShowCase(nowOrderTutorial)
                .addShowCase(timeOrderTutorial)
                .addShowCase(packageTutorial)
                .addShowCase(serviceTutorial)
                .addShowCase(orderTutorial)
                .show();
    }

    private void setServicePrices(AllCarWashResponse carWash) {
        servicePriceBlock.removeAllViews();
//        List<ServiceClassItem> serviceClassItemList = FastSave.getInstance().getObjectsList(CAR_SERVICE_CLASS, ServiceClassItem.class);
        for (int i = 0; i < carWash.getServicePrices().size(); i++) {
            if (FastSave.getInstance().getObjectsList(CAR_FILTER_LIST, AttributesItem.class).containsAll(carWash.getServicePrices().get(i).getAttributes())) {
                CheckBox checkBox = new CheckBox(OrderActivity.this);
                checkBox.setText(carWash.getServicePrices().get(i).getName() + " | +" + carWash.getServicePrices().get(i).getDuration() + "мин | +" + carWash.getServicePrices().get(i).getPrice() + "грн");
                checkBox.setTag(carWash.getServicePrices().get(i).getId());
                servicePriceBlock.addView(checkBox);
            } else {
                Log.d(TAG, "false: " + carWash.getServicePrices().get(i).getName());

            }
        }

    }

    private boolean checkCarBody(ServicePricesItem servicePricesItem, String carBody) {
        if (servicePricesItem.getCarBodies().size() == 0) {
            Log.d(TAG, "servicePricesItem.getCarBodies().size()==0");
            return true;
        } else {
            Log.d(TAG, "contains(carBody): " + servicePricesItem.getCarBodies().contains(carBody) + " " + servicePricesItem.getCarBodies().toString() + " " + FastSave.getInstance().getString(CAR_BODY, ""));
            return servicePricesItem.getCarBodies().contains(carBody);
        }
    }

    private boolean checkCarInterior(ServicePricesItem servicePricesItem, String carInterior) {
        if (servicePricesItem.getAttributes().size() == 0) {
            return true;
        } else {
            for (int i = 0; i < servicePricesItem.getAttributes().size(); i++) {
                if (servicePricesItem.getAttributes().get(i).getValue().equals(carInterior)) {
                    return true;
                }
            }
            return false;
        }
//        if (servicePricesItem.getInteriorTypes().size() == 0) {
//            Log.d(TAG, "servicePricesItem.getInteriorTypes().size()==0");
//            return true;
//        } else {
//            Log.d(TAG, "contains(carInterior): " + servicePricesItem.getInteriorTypes().contains(carInterior) + " " + servicePricesItem.getInteriorTypes().toString() + " " + FastSave.getInstance().getString(CAR_INTERIOR, ""));
//            return servicePricesItem.getInteriorTypes().contains(carInterior);
//        }
    }

//    private boolean checkCarParametr(List<ServicePricesItem> servicePricesItemList, String carBody, String carInterior) {
//        boolean result = false;
//
//        if (servicePricesItemList.size()!=0){
//            for (int i = 0; i < servicePricesItemList.size(); i++) {
//                if (servicePricesItemList.get(i).getCarBodies().size()==0 || servicePricesItemList.get(i).getCarBodies().contains(carBody)){
//                    if ()
//                }
//            }
//        }else {
//            result = false;
//        }
//        return result;
//
//    }

    private void setPackages(AllCarWashResponse carWash) {
        View layout2 = LayoutInflater.from(this).inflate(R.layout.package_btn2, packageScroll, false);
        MaterialButton packageBtn = layout2.findViewById(R.id.packageBtn);
        packageBtn.setText("Не выбран");
        packageBtn.setTag("default");
        packageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: NOW");
                for (int j = 0; j < packageScroll.getChildCount(); j++) {
                    ConstraintLayout constraintLayout = ((ConstraintLayout) packageScroll.getChildAt(j));
                    if (constraintLayout.getChildAt(0).getTag().equals(v.getTag())) {
                        ((MaterialButton) constraintLayout.getChildAt(0)).setTextColor(getResources().getColor(R.color.black));
                    } else {
                        ((MaterialButton) constraintLayout.getChildAt(0)).setTextColor(getResources().getColor(R.color.white));
                    }
                }
                serviceMap.clear();
                nameOfServiceList.clear();
                packagesDescription.setText("");
                packagesDescription.setVisibility(View.GONE);
                textView12.setVisibility(View.GONE);
                setServicePrices(carWash);
            }
        });
        packageBtn.performClick();
        packageScroll.addView(layout2);
//        packageBtn.performClick();
        for (int i = 0; i < carWash.getPackages().size(); i++) {
            layout2 = LayoutInflater.from(this).inflate(R.layout.package_btn3, packageScroll, false);
            packageBtn = layout2.findViewById(R.id.packageBtn);
            packageBtn.setText(carWash.getPackages().get(i).getName());
            packageBtn.setTag(carWash.getPackages().get(i).getId());
            packageBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    serviceMap.clear();
                    nameOfServiceList.clear();
                    orderBody.setPackageId((String) v.getTag());
                    for (int j = 0; j < packageScroll.getChildCount(); j++) {
                        ConstraintLayout constraintLayout = ((ConstraintLayout) packageScroll.getChildAt(j));
                        if (constraintLayout.getChildAt(0).getTag().equals(v.getTag())) {
                            ((MaterialButton) constraintLayout.getChildAt(0)).setTextColor(getResources().getColor(R.color.black));
                        } else {
                            ((MaterialButton) constraintLayout.getChildAt(0)).setTextColor(getResources().getColor(R.color.white));
                        }
                    }
                    Toast.makeText(OrderActivity.this, packageMap.get(v.getTag()).getId(), Toast.LENGTH_SHORT).show();
                    for (int j = 0; j < packageMap.get(v.getTag()).getServices().size(); j++) {
                        nameOfServiceList.add(packageMap.get(v.getTag()).getServices().get(j).getName());
                        serviceMap.put(packageMap.get(v.getTag()).getServices().get(j).getId(), packageMap.get(v.getTag()).getServices().get(j));
                    }
                    String string = "Скидка = " + packageMap.get(v.getTag()).getDiscount() + ", " + nameOfServiceList.toString();
                    packagesDescription.setText(string);
                    packagesDescription.setVisibility(View.VISIBLE);
                    textView12.setVisibility(View.VISIBLE);
                    setServicePrices(carWash);
                }
            });
            packageScroll.addView(layout2);
        }
        setServicePrices(carWash);
//        Log.d(TAG, "setPackages: do");
//        packageScroll.getChildAt(0).performClick();
//        ((View)packageScroll.getChildAt(0)).get
//        Log.d(TAG, "setPackages: out");

    }

    private void initView() {
        errorHandler = new ErrorHandler(this, this);
        orderButton = findViewById(R.id.chooseCarBtn);
        nowOrderBtn = findViewById(R.id.nowOrderBtn);
        timeOrderBtn = findViewById(R.id.timeOrderBtn);
//        timeOrderBtn.setCornerRadius(25);
        orderButton.setOnClickListener(this);
        nowOrderBtn.setOnClickListener(this);
//        nowOrderBtn.setCornerRadius(25);
        timeOrderBtn.setOnClickListener(this);
        horizontalScrollView = (HorizontalScrollView) findViewById(R.id.horizontalScrollView);
        packageScroll = (LinearLayout) findViewById(R.id.packageScroll);
        textView10 = (TextView) findViewById(R.id.textView10);
        textView11 = (TextView) findViewById(R.id.textView11);
        textView12 = (TextView) findViewById(R.id.textView12);
        packagesDescription = (TextView) findViewById(R.id.packagesDescription);
        textView14 = (TextView) findViewById(R.id.textView14);
        servicePriceBlock = (LinearLayout) findViewById(R.id.servicePriceBlock);
    }

    private void perOrder() {
        showProgressDialog();
        orderBody.setWorkingSpaceId(null);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        orderBody.setTargetId(FastSave.getInstance().getString(CAR_ID, ""));
        orderBody.setBusinessId(carWash.getId());
        if (nowFlag) {
            orderBody.setBegin(System.currentTimeMillis() + (carWash.getTimeZone() * 60000));
        } else {
            orderBody.setBegin(begin);
        }
        orderBody.setDescription("Android");
        List<String> list = new ArrayList<>();
        for (int i = 0; i < servicePriceBlock.getChildCount(); i++) {
            if (((CheckBox) servicePriceBlock.getChildAt(i)).isChecked()) {
                list.add((String) ((CheckBox) servicePriceBlock.getChildAt(i)).getTag());
            }
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
//                        ButtonClickListener buttonClickListener = new ButtonClickListener() {
//                            @Override
//                            public void onClick(int button) {
//                                switch (button) {
//                                    case NDialog.BUTTON_POSITIVE:
//                                        showProgressDialog();
//                                        apiInterface = APIClient.getClient().create(APIInterface.class);
//                                        Call<OrderResponse> call = apiInterface.doOrder(FastSave.getInstance().getString(ACCESS_TOKEN, ""), orderBody);
//                                        call.enqueue(new Callback<OrderResponse>() {
//                                            @Override
//                                            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
//                                                if (response.code() == 200) {
//                                                    Toast.makeText(OrderActivity.this, "Ok", Toast.LENGTH_SHORT).show();
//                                                    closeProgressDialog();
//                                                } else {
//                                                    try {
//                                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
//                                                        errorHandler.showError(jObjError.getInt("code"));
//                                                        closeProgressDialog();
//                                                    } catch (Exception e) {
//                                                        errorHandler.showCustomError(e.getMessage());
//                                                        closeProgressDialog();
//                                                    }
//                                                }
//                                            }
//
//                                            @Override
//                                            public void onFailure(Call<OrderResponse> call, Throwable t) {
//                                                errorHandler.showCustomError(t.getMessage());
//                                                closeProgressDialog();
//                                            }
//                                        });
//                                        break;
//                                }
//                            }
//                        };
//                        nDialog.setPositiveButtonText("Заказать");
//                        nDialog.setPositiveButtonTextColor(Color.RED);
//                        nDialog.setPositiveButtonOnClickDismiss(true); // default : true
//                        nDialog.setPositiveButtonClickListener(buttonClickListener);
//
//                        nDialog.setNeutralButtonText("Назад");
//                        nDialog.setNeutralButtonTextColor(Color.RED);
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
                                            apiInterface = APIClient.getClient().create(APIInterface.class);
                                            Call<OrderResponse> call = apiInterface.doOrder(FastSave.getInstance().getString(ACCESS_TOKEN, ""), orderBody);
                                            call.enqueue(new Callback<OrderResponse>() {
                                                @Override
                                                public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                                                    if (response.code() == 200) {
                                                        closeProgressDialog();
                                                        nDialog.dismiss();
                                                        Toast.makeText(OrderActivity.this, "Запись добавленна в список", Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(OrderActivity.this, MapsActivity.class));
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

    public static String getStringTime(Long millisecond) {
        if (millisecond != null) {
            SimpleDateFormat format = new SimpleDateFormat("HH:mm");
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(millisecond);
            return format.format(calendar.getTime());
        }
        return "";
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
                        Log.d(TAG, "onTimeSet: ");
                    }
                }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), true).show();
            }
        },
                currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.timeOrderBtn:
                nowFlag = false;
                showDateTimePicker();
                timeOrderBtn.setTextColor(getResources().getColor(R.color.black));
                nowOrderBtn.setTextColor(getResources().getColor(R.color.white));
                break;
            case R.id.nowOrderBtn:
                nowFlag = true;
                timeOrderBtn.setTextColor(getResources().getColor(R.color.white));
                nowOrderBtn.setTextColor(getResources().getColor(R.color.black));
                break;
            case R.id.chooseCarBtn:
                perOrder();
                break;
        }
    }
}
