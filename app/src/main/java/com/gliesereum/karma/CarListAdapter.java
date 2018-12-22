package com.gliesereum.karma;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.appizona.yehiahd.fastsave.FastSave;
import com.gliesereum.karma.data.network.json.car.AllCarResponse;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static com.gliesereum.karma.util.Constants.CAR_BRAND;
import static com.gliesereum.karma.util.Constants.CAR_ID;
import static com.gliesereum.karma.util.Constants.CAR_MODEL;

public class CarListAdapter extends RecyclerView.Adapter<CarListAdapter.TweetViewHolder> {

    private List<AllCarResponse> allCarsList = new ArrayList<>();


    @NonNull
    @Override
    public TweetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_car_info, parent, false);
        return new TweetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TweetViewHolder holder, int position) {
        holder.bind(allCarsList.get(position));
    }

    @Override
    public int getItemCount() {
        return allCarsList.size();
    }

    // Предоставляет прямую ссылку на каждый View-компонент
    // Используется для кэширования View-компонентов и последующего быстрого доступа к ним
    class TweetViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
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

        public TweetViewHolder(View itemView) {
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
            colour.setText(carInfo.getCarBody());
            carId.setText(carInfo.getId());
            if (carInfo.getId().equals(FastSave.getInstance().getString(CAR_ID, ""))) {
                chooseCar.setVisibility(View.GONE);
            } else {
                chooseCar.setVisibility(View.VISIBLE);
            }
            chooseCar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FastSave.getInstance().saveString(CAR_ID, carId.getText().toString());
                    FastSave.getInstance().saveString(CAR_BRAND, brandId.getText().toString());
                    FastSave.getInstance().saveString(CAR_MODEL, modelId.getText().toString());
                    Log.d("TAG", "onClick: " + FastSave.getInstance().getString(CAR_ID, ""));
                    notifyDataSetChanged();
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
