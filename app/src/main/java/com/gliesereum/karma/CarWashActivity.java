package com.gliesereum.karma;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.gliesereum.karma.data.network.APIClient;
import com.gliesereum.karma.data.network.APIInterface;
import com.gliesereum.karma.data.network.json.carwash.AllCarWashResponse;
import com.gliesereum.karma.util.ErrorHandler;
import com.google.android.material.button.MaterialButton;

import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CarWashActivity extends AppCompatActivity {

    String string;
    private AllCarWashResponse carWash;
    private MaterialButton addCarBtn;
    private APIInterface apiInterface;
    private ErrorHandler errorHandler;
    private TextView debugTextView;
    private ProgressDialog progressDialog;


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
                    debugTextView.setText(response.toString());
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

    private void initView() {
        debugTextView = (TextView) findViewById(R.id.debugTextView);
    }

    public void showProgressDialog() {
        progressDialog = ProgressDialog.show(this, "title", "message");

    }

    public void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
