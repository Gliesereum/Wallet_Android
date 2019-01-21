package com.gliesereum.karma;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.appizona.yehiahd.fastsave.FastSave;
import com.gliesereum.karma.data.network.APIClient;
import com.gliesereum.karma.data.network.APIInterface;
import com.gliesereum.karma.data.network.json.record.AllRecordResponse;
import com.gliesereum.karma.util.ErrorHandler;
import com.gliesereum.karma.util.Util;

import org.json.JSONObject;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.gliesereum.karma.util.Constants.ACCESS_TOKEN;

public class RecordListActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private RecordListAdapter recordListAdapter;
    private List<AllRecordResponse> recordsList;
    private APIInterface apiInterface;
    private ErrorHandler errorHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);
        FastSave.init(getApplicationContext());
        initView();
        getAllRecord();
    }

    private void getAllRecord() {
        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<List<AllRecordResponse>> call = apiInterface.getAllRecord(FastSave.getInstance().getString(ACCESS_TOKEN, ""));
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
                        Toast.makeText(RecordListActivity.this, "У вас пока нет записей", Toast.LENGTH_SHORT).show();
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
            public void onFailure(Call<List<AllRecordResponse>> call, Throwable t) {
                errorHandler.showCustomError(t.getMessage());
            }
        });
    }

    private void initView() {
        errorHandler = new ErrorHandler(this, this);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recordListAdapter = new RecordListAdapter();
        recyclerView.setAdapter(recordListAdapter);
        recyclerView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });

        new Util(this, toolbar).addNavigation();
    }
}
