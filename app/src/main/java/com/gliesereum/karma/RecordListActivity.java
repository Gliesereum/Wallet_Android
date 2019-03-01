package com.gliesereum.karma;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.appizona.yehiahd.fastsave.FastSave;
import com.gliesereum.karma.adapter.RecordListAdapter;
import com.gliesereum.karma.data.network.APIClient;
import com.gliesereum.karma.data.network.APIInterface;
import com.gliesereum.karma.data.network.json.record.AllRecordResponse;
import com.gliesereum.karma.util.ErrorHandler;
import com.gliesereum.karma.util.Util;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.gliesereum.karma.util.Constants.ACCESS_TOKEN;
import static com.gliesereum.karma.util.Constants.SERVICE_TYPE;

public class RecordListActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private RecordListAdapter recordListAdapter;
    private List<AllRecordResponse> recordsList;
    private Map<String, String> carWashNameMap = new HashMap<>();
    private APIInterface apiInterface;
    private ErrorHandler errorHandler;
    private TextView splashTextView;
    private ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);
        FastSave.init(getApplicationContext());
        initView();
        getAllRecord();
    }

    private void getAllRecord() {
        showProgressDialog();
        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<List<AllRecordResponse>> call = apiInterface.getAllRecord(FastSave.getInstance().getString(ACCESS_TOKEN, ""), SERVICE_TYPE);
        call.enqueue(new Callback<List<AllRecordResponse>>() {
            @Override
            public void onResponse(Call<List<AllRecordResponse>> call, Response<List<AllRecordResponse>> response) {
                if (response.code() == 200) {
                    recordsList = response.body();
                    if (recordsList != null && recordsList.size() > 0) {
                        recordListAdapter.setItems(recordsList);
                    }
                } else {
                    if (response.code() == 204) {
                        splashTextView.setVisibility(View.VISIBLE);
                        Toast.makeText(RecordListActivity.this, "У вас пока нет записей", Toast.LENGTH_SHORT).show();
                    } else {
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            errorHandler.showError(jObjError.getInt("code"));
                        } catch (Exception e) {
                            errorHandler.showCustomError(e.getMessage());
                            closeProgressDialog();
                        }
                    }
                }
                closeProgressDialog();
            }

            @Override
            public void onFailure(Call<List<AllRecordResponse>> call, Throwable t) {
                errorHandler.showCustomError(t.getMessage());
                closeProgressDialog();
            }
        });
    }

    private void initView() {
        errorHandler = new ErrorHandler(this, this);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Список записей");
        setSupportActionBar(toolbar);
        splashTextView = findViewById(R.id.splashTextView);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recordListAdapter = new RecordListAdapter(RecordListActivity.this);
        recyclerView.setAdapter(recordListAdapter);
        new Util(this, toolbar, 3).addNavigation();
    }

    public void showProgressDialog() {
        progressDialog = ProgressDialog.show(this, "Ща сек...", "Ща все сделаю...");
    }

    public void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
