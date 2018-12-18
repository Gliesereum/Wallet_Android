package com.gliesereum.karma;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gliesereum.karma.data.network.APIClient;
import com.gliesereum.karma.data.network.APIInterface;
import com.gliesereum.karma.data.network.json.carwash.AllCarWashResponse;
import com.gliesereum.karma.util.ErrorHandler;
import com.google.android.material.button.MaterialButton;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import de.ehsun.coloredtimebar.TimelinePickerView;
import de.ehsun.coloredtimebar.TimelineView;
import hakobastvatsatryan.DropdownTextView;
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
    private DropdownTextView workTimesDropdown;
    private MaterialButton materialButton;
    private MaterialButton materialButton3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_wash);

        string = getIntent().getStringExtra("carWash_ID");
        initView();

        Toast.makeText(this, string, Toast.LENGTH_SHORT).show();

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
                    setWorkTime();
                    setBoxTime();

                    int countBox = carWash.getCountBox();
                    for (int i = 0; i < countBox; i++) {
                        TextView textView = new TextView(CarWashActivity.this);
                        TimelineView timelineBox = new TimelineView(CarWashActivity.this);
                        textView.setText("Бокс №" + (i + 1));
                        timelineBox.setId(i);
                        timelineBox.setTimeRange("10:00-22:00");
                        timelineBox.setTimeTextInterval(2);
                        timelineBox.setFractionTextSize(30);
                        timelineBox.setBarWidth(75);
                        timelineBox.setFractionLineLength(20);
                        timelineBox.setBarColorAvailable(Color.parseColor("#FF0000"));
                        timelineBox.setBarColorNotAvailable(Color.parseColor("#00FF00"));
//                        timelineBox.setFractionLineColor(Color.parseColor("#DEFFFFFF"));
//                        timelineBox.setFractionSecondaryTextColor(Color.parseColor("#DEFFFFFF"));
//                        timelineBox.setFractionPrimaryTextColor(Color.parseColor("#DEFFFFFF"));
//                        timelineBox.setHighlightTimeRange("10:00-23:00");
//                        timelineBox.setFractionPrimaryTextColor(R.color.available_time_default_color);
//                        timelineBox.setBarColorAvailable(R.color.timeline_default_color);
//                        timelineBox.setBarColorNotAvailable(R.color.available_time_default_color);
                        boxBlock.addView(textView);
                        boxBlock.addView(timelineBox);
                        timelineBox.setOnClickListener(CarWashActivity.this);
                    }
                    TimelineView timelineBox0 = boxBlock.findViewById(0);
                    List<String> timeRange = new ArrayList<>();
                    timeRange.add("11:00-12:00");
                    timeRange.add("14:00-16:00");
                    timeRange.add("17:30-19:00");
                    timelineBox0.setAvailableTimeRange(timeRange);

                    String MONDAY = "";
                    String TUESDAY = "";
                    String WEDNESDAY = "";
                    String THURSDAY = "";
                    String FRIDAY = "";
                    String SATURDAY = "";
                    String SUNDAY = "";
                    for (int i = 0; i < carWash.getWorkTimes().size(); i++) {
                        switch (carWash.getWorkTimes().get(i).getDayOfWeek()) {
                            case "MONDAY":
                                MONDAY = "MONDAY: " + carWash.getWorkTimes().get(i).getFrom() + "-" + carWash.getWorkTimes().get(i).getTo();
                                break;
                            case "TUESDAY":
                                TUESDAY = "TUESDAY: " + carWash.getWorkTimes().get(i).getFrom() + "-" + carWash.getWorkTimes().get(i).getTo();
                                break;
                            case "WEDNESDAY":
                                WEDNESDAY = "WEDNESDAY: " + carWash.getWorkTimes().get(i).getFrom() + "-" + carWash.getWorkTimes().get(i).getTo();
                                break;
                            case "THURSDAY":
                                THURSDAY = "THURSDAY: " + carWash.getWorkTimes().get(i).getFrom() + "-" + carWash.getWorkTimes().get(i).getTo();
                                break;
                            case "FRIDAY":
                                FRIDAY = "FRIDAY: " + carWash.getWorkTimes().get(i).getFrom() + "-" + carWash.getWorkTimes().get(i).getTo();
                                break;
                            case "SATURDAY":
                                SATURDAY = "SATURDAY: " + carWash.getWorkTimes().get(i).getFrom() + "-" + carWash.getWorkTimes().get(i).getTo();
                                break;
                            case "SUNDAY":
                                SUNDAY = "SUNDAY: " + carWash.getWorkTimes().get(i).getFrom() + "-" + carWash.getWorkTimes().get(i).getTo();
                                break;
                        }

                    }

                    workTimesDropdown.setContentText(MONDAY + "\n" + TUESDAY + "\n" + WEDNESDAY + "\n" + THURSDAY + "\n" + FRIDAY + "\n" + SATURDAY + "\n" + SUNDAY);


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

    private void setBoxTime() {

    }

    private void setWorkTime() {

    }

    private void getAllBox() {

    }

    private void initView() {
        name = (TextView) findViewById(R.id.name);
        adres = (TextView) findViewById(R.id.address);
        description = (TextView) findViewById(R.id.description);
        boxBlock = (LinearLayout) findViewById(R.id.boxBlock);
        workTimesDropdown = (DropdownTextView) findViewById(R.id.workTimes_dropdown);
        materialButton = (MaterialButton) findViewById(R.id.materialButton);
        materialButton3 = (MaterialButton) findViewById(R.id.materialButton3);
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
