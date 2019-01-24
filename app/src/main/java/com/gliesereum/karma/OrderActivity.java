package com.gliesereum.karma;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
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
import com.gliesereum.karma.data.network.APIClient;
import com.gliesereum.karma.data.network.APIInterface;
import com.gliesereum.karma.data.network.json.carwash.AllCarWashResponse;
import com.gliesereum.karma.data.network.json.carwash.PackagesItem;
import com.gliesereum.karma.data.network.json.carwash.ServiceClassItem;
import com.gliesereum.karma.data.network.json.carwash.ServicePricesItem;
import com.gliesereum.karma.data.network.json.carwash.ServicesItem;
import com.gliesereum.karma.data.network.json.order.OrderBody;
import com.gliesereum.karma.data.network.json.order.OrderResponse;
import com.gliesereum.karma.util.ErrorHandler;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.gliesereum.karma.util.Constants.ACCESS_TOKEN;
import static com.gliesereum.karma.util.Constants.CAR_BODY;
import static com.gliesereum.karma.util.Constants.CAR_ID;
import static com.gliesereum.karma.util.Constants.CAR_INTERIOR;
import static com.gliesereum.karma.util.Constants.CAR_SERVICE_CLASS;

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

    }

    private void setServicePrices(AllCarWashResponse carWash) {
        servicePriceBlock.removeAllViews();
        List<ServiceClassItem> serviceClassItemList = FastSave.getInstance().getObjectsList(CAR_SERVICE_CLASS, ServiceClassItem.class);
        String carBody = FastSave.getInstance().getString(CAR_BODY, "");
        String carInterior = FastSave.getInstance().getString(CAR_INTERIOR, "");

        for (int i = 0; i < carWash.getServicePrices().size(); i++) {
            if (checkCarInterior(carWash.getServicePrices().get(i), carInterior) && checkCarBody(carWash.getServicePrices().get(i), carBody) && !serviceMap.containsKey(carWash.getServicePrices().get(i).getId())) {
                CheckBox checkBox = new CheckBox(OrderActivity.this);
                checkBox.setText(carWash.getServicePrices().get(i).getName() + " | +" + carWash.getServicePrices().get(i).getDuration() + "мин | +" + carWash.getServicePrices().get(i).getPrice() + "грн");
                checkBox.setTag(carWash.getServicePrices().get(i).getId());
                servicePriceBlock.addView(checkBox);
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
        if (servicePricesItem.getInteriorTypes().size() == 0) {
            Log.d(TAG, "servicePricesItem.getInteriorTypes().size()==0");
            return true;
        } else {
            Log.d(TAG, "contains(carInterior): " + servicePricesItem.getInteriorTypes().contains(carInterior) + " " + servicePricesItem.getInteriorTypes().toString() + " " + FastSave.getInstance().getString(CAR_INTERIOR, ""));
            return servicePricesItem.getInteriorTypes().contains(carInterior);
        }
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
        View layout2 = LayoutInflater.from(this).inflate(R.layout.package_btn, packageScroll, false);
        MaterialButton packageBtn = layout2.findViewById(R.id.packageBtn);
        packageBtn.setText("Не выбран");
        packageBtn.setTag("default");
        packageBtn.setCornerRadius(50);
        packageBtn.performClick();
        packageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: ");
                Toast.makeText(OrderActivity.this, "Test Defaul", Toast.LENGTH_SHORT).show();
                for (int j = 0; j < packageScroll.getChildCount(); j++) {
                    ConstraintLayout constraintLayout = ((ConstraintLayout) packageScroll.getChildAt(j));
                    if (constraintLayout.getChildAt(0).getTag().equals(v.getTag())) {
                        ((MaterialButton) constraintLayout.getChildAt(0)).setStrokeWidth(5);
                        ((MaterialButton) constraintLayout.getChildAt(0)).setStrokeColorResource(R.color.primary);
                    } else {
                        ((MaterialButton) constraintLayout.getChildAt(0)).setStrokeWidth(0);
                    }
                }
                serviceMap.clear();
                nameOfServiceList.clear();
                packagesDescription.setText("");
                setServicePrices(carWash);
//                orderBody.setPackageId((String) v.getTag());
//
//                for (int j = 0; j < packageScroll.getChildCount(); j++) {
//                    ConstraintLayout constraintLayout = ((ConstraintLayout) packageScroll.getChildAt(j));
//                    if (constraintLayout.getChildAt(0).getTag().equals(v.getTag())) {
//                        ((MaterialButton) constraintLayout.getChildAt(0)).setStrokeWidth(5);
//                        ((MaterialButton) constraintLayout.getChildAt(0)).setStrokeColorResource(R.color.primary);
//                    } else {
//                        ((MaterialButton) constraintLayout.getChildAt(0)).setStrokeWidth(0);
//                    }
//                }
//
////                    for (int j = 0; j < packageScroll.getChildCount(); j++) {
////                        if (packageScroll.getChildAt(j).getTag().equals(v.getTag())){
////                            ((MaterialButton)packageScroll.getChildAt(j)).setStrokeWidth(5);
////                            ((MaterialButton)packageScroll.getChildAt(j)).setStrokeColorResource(R.color.primary);
////                        }else {
////                            ((MaterialButton)packageScroll.getChildAt(j)).setStrokeWidth(0);
////                        }
////                    }
//
//                Toast.makeText(OrderActivity.this, packageMap.get(v.getTag()).getId(), Toast.LENGTH_SHORT).show();
//                for (int j = 0; j < packageMap.get(v.getTag()).getServices().size(); j++) {
//                    nameOfServiceList.add(packageMap.get(v.getTag()).getServices().get(j).getName());
//                    serviceMap.put(packageMap.get(v.getTag()).getServices().get(j).getId(), packageMap.get(v.getTag()).getServices().get(j));
//                }
//                String string = "Скидка = " + packageMap.get(v.getTag()).getDiscount() + ", " + nameOfServiceList.toString();
//                packagesDescription.setText(string);
//                setServicePrices(carWash);
            }
        });
        packageScroll.addView(layout2);
        packageBtn.performClick();
        for (int i = 0; i < carWash.getPackages().size(); i++) {
            layout2 = LayoutInflater.from(this).inflate(R.layout.package_btn, packageScroll, false);
            packageBtn = layout2.findViewById(R.id.packageBtn);
            packageBtn.setText(carWash.getPackages().get(i).getName());
            packageBtn.setTag(carWash.getPackages().get(i).getId());
            packageBtn.setCornerRadius(50);
            packageBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    serviceMap.clear();
                    nameOfServiceList.clear();
                    orderBody.setPackageId((String) v.getTag());

                    for (int j = 0; j < packageScroll.getChildCount(); j++) {
                        ConstraintLayout constraintLayout = ((ConstraintLayout) packageScroll.getChildAt(j));
                        if (constraintLayout.getChildAt(0).getTag().equals(v.getTag())) {
                            ((MaterialButton) constraintLayout.getChildAt(0)).setStrokeWidth(5);
                            ((MaterialButton) constraintLayout.getChildAt(0)).setStrokeColorResource(R.color.primary);
                        } else {
                            ((MaterialButton) constraintLayout.getChildAt(0)).setStrokeWidth(0);
                        }
                    }

//                    for (int j = 0; j < packageScroll.getChildCount(); j++) {
//                        if (packageScroll.getChildAt(j).getTag().equals(v.getTag())){
//                            ((MaterialButton)packageScroll.getChildAt(j)).setStrokeWidth(5);
//                            ((MaterialButton)packageScroll.getChildAt(j)).setStrokeColorResource(R.color.primary);
//                        }else {
//                            ((MaterialButton)packageScroll.getChildAt(j)).setStrokeWidth(0);
//                        }
//                    }

                    Toast.makeText(OrderActivity.this, packageMap.get(v.getTag()).getId(), Toast.LENGTH_SHORT).show();
                    for (int j = 0; j < packageMap.get(v.getTag()).getServices().size(); j++) {
                        nameOfServiceList.add(packageMap.get(v.getTag()).getServices().get(j).getName());
                        serviceMap.put(packageMap.get(v.getTag()).getServices().get(j).getId(), packageMap.get(v.getTag()).getServices().get(j));
                    }
                    String string = "Скидка = " + packageMap.get(v.getTag()).getDiscount() + ", " + nameOfServiceList.toString();
                    packagesDescription.setText(string);
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
        orderButton = findViewById(R.id.orderButton);
        nowOrderBtn = findViewById(R.id.nowOrderBtn);
        timeOrderBtn = findViewById(R.id.timeOrderBtn);
        orderButton.setOnClickListener(this);
        nowOrderBtn.setOnClickListener(this);
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
        orderBody.setCarId(FastSave.getInstance().getString(CAR_ID, ""));
        orderBody.setCarWashId(carWash.getId());
        if (nowFlag) {
            orderBody.setBegin(System.currentTimeMillis());
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
                                    time.setText(getStringTime(response.body().getBegin()));
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
                                                    Toast.makeText(OrderActivity.this, "Ok", Toast.LENGTH_SHORT).show();
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
                                            public void onFailure(Call<OrderResponse> call, Throwable t) {
                                                errorHandler.showCustomError(t.getMessage());
                                                closeProgressDialog();
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
        progressDialog = ProgressDialog.show(this, "title", "message");

    }

    public void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    Calendar date;

    public void showDateTimePicker() {
        final Calendar currentDate = Calendar.getInstance();
        date = Calendar.getInstance();
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
                    }
                }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), true).show();
            }
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.timeOrderBtn:
                nowFlag = false;
                showDateTimePicker();
                timeOrderBtn.setStrokeWidth(5);
                nowOrderBtn.setStrokeWidth(0);
                break;
            case R.id.nowOrderBtn:
                nowFlag = true;
                Toast.makeText(this, "Now Btn", Toast.LENGTH_SHORT).show();
                nowOrderBtn.setStrokeWidth(5);
                timeOrderBtn.setStrokeWidth(0);
                break;
            case R.id.orderButton:
                perOrder();
                break;
        }
    }
}
