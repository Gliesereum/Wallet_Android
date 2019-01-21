package com.gliesereum.karma;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.appizona.yehiahd.fastsave.FastSave;
import com.gliesereum.karma.data.network.APIClient;
import com.gliesereum.karma.data.network.APIInterface;
import com.gliesereum.karma.data.network.json.car.AllCarResponse;
import com.gliesereum.karma.util.ErrorHandler;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.gliesereum.karma.util.Constants.ACCESS_TOKEN;
import static com.gliesereum.karma.util.Constants.CAR_BODY;
import static com.gliesereum.karma.util.Constants.CAR_BRAND;
import static com.gliesereum.karma.util.Constants.CAR_COLOR;
import static com.gliesereum.karma.util.Constants.CAR_ID;
import static com.gliesereum.karma.util.Constants.CAR_INTERIOR;
import static com.gliesereum.karma.util.Constants.CAR_MODEL;
import static com.gliesereum.karma.util.Constants.CAR_SERVICE_CLASS;

public class CarListAdapter extends RecyclerView.Adapter<CarListAdapter.ViewHolder> {

    private List<AllCarResponse> allCarsList = new ArrayList<>();
    private APIInterface apiInterface;
    private ErrorHandler errorHandler;
    private String TAG = "TAG";

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_car_info, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(allCarsList.get(position));
    }

    @Override
    public int getItemCount() {
        return allCarsList.size();
    }

    // Предоставляет прямую ссылку на каждый View-компонент
    // Используется для кэширования View-компонентов и последующего быстрого доступа к ним
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        private TextView brandId;
        private TextView yearId;
        private TextView modelId;
        private TextView carId;
        private TextView registrationNumber;
        private TextView description;
        private TextView interior;
        private TextView carBody;
        private TextView colour;
        private Button chooseCar;

        public ViewHolder(View itemView) {
            super(itemView);
            brandId = itemView.findViewById(R.id.brandId);
            modelId = itemView.findViewById(R.id.modelId);
            yearId = itemView.findViewById(R.id.yearId);
            registrationNumber = itemView.findViewById(R.id.registrationNumber);
            description = itemView.findViewById(R.id.description);
            interior = itemView.findViewById(R.id.interior);
            carBody = itemView.findViewById(R.id.carBody);
            colour = itemView.findViewById(R.id.colour);
            carId = itemView.findViewById(R.id.carId);
            chooseCar = itemView.findViewById(R.id.chooseCar);


        }

        public void bind(AllCarResponse carInfo) {
            brandId.setText(carInfo.getBrand().getName());
            modelId.setText(carInfo.getModel().getName());
            yearId.setText(carInfo.getYear().getName());
            registrationNumber.setText(carInfo.getRegistrationNumber());
            description.setText(carInfo.getDescription());
            interior.setText(carInfo.getInterior());
            carBody.setText(carInfo.getCarBody());
            colour.setText(carInfo.getColour());
            carId.setText(carInfo.getId());
            chooseCar.setTag(carInfo.getId());
            if (carInfo.getId().equals(FastSave.getInstance().getString(CAR_ID, ""))) {
                chooseCar.setVisibility(View.GONE);
            } else {
                chooseCar.setVisibility(View.VISIBLE);
            }
            chooseCar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    apiInterface = APIClient.getClient().create(APIInterface.class);
                    Call<AllCarResponse> call = apiInterface.getCarById(FastSave.getInstance().getString(ACCESS_TOKEN, ""), (String) v.getTag());
                    call.enqueue(new Callback<AllCarResponse>() {
                        @Override
                        public void onResponse(Call<AllCarResponse> call, Response<AllCarResponse> response) {
                            AllCarResponse carById = response.body();
                            if (response.code() == 200) {
                                FastSave.getInstance().saveString(CAR_ID, carById.getId());
                                FastSave.getInstance().saveString(CAR_BRAND, carById.getBrand().getName());
                                FastSave.getInstance().saveString(CAR_MODEL, carById.getModel().getName());
                                FastSave.getInstance().saveObject(CAR_SERVICE_CLASS, carById.getServices());
                                FastSave.getInstance().saveObject(CAR_BODY, carById.getCarBody());
                                FastSave.getInstance().saveObject(CAR_INTERIOR, carById.getInterior());
                                FastSave.getInstance().saveObject(CAR_COLOR, carById.getColour());
                                notifyDataSetChanged();
                                Log.d(TAG, "onResponse: Choose " + carById.getBrand().getName());
                            } else {
                                if (response.code() == 204) {
//                        Toast.makeText(CarListActivity.this, "", Toast.LENGTH_SHORT).show();
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
                        public void onFailure(Call<AllCarResponse> call, Throwable t) {
                            errorHandler.showCustomError(t.getMessage());
                        }
                    });
                }
            });
        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }

    public void setItems(List<AllCarResponse> cars) {
        allCarsList.addAll(cars);
        notifyDataSetChanged();
    }
}
