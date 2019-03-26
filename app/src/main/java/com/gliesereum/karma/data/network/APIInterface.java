package com.gliesereum.karma.data.network;

import com.gliesereum.karma.data.network.json.car.AllCarResponse;
import com.gliesereum.karma.data.network.json.car.BrandResponse;
import com.gliesereum.karma.data.network.json.car.CarDeleteResponse;
import com.gliesereum.karma.data.network.json.carwash.AllCarWashResponse;
import com.gliesereum.karma.data.network.json.carwash.CommentsItem;
import com.gliesereum.karma.data.network.json.carwash.FilterCarWashBody;
import com.gliesereum.karma.data.network.json.carwash.Rating;
import com.gliesereum.karma.data.network.json.carwash.RecordsItem;
import com.gliesereum.karma.data.network.json.classservices.ClassServiceResponse;
import com.gliesereum.karma.data.network.json.code.CodeResponse;
import com.gliesereum.karma.data.network.json.code.SigninBody;
import com.gliesereum.karma.data.network.json.filter.FilterResponse;
import com.gliesereum.karma.data.network.json.order.OrderBody;
import com.gliesereum.karma.data.network.json.order.OrderResponse;
import com.gliesereum.karma.data.network.json.record.AllRecordResponse;
import com.gliesereum.karma.data.network.json.service.ServiceResponse;
import com.gliesereum.karma.data.network.json.status.StatusResponse;
import com.gliesereum.karma.data.network.json.user.TokenInfo;
import com.gliesereum.karma.data.network.json.user.User;
import com.gliesereum.karma.data.network.json.user.UserResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIInterface {

    //STATUS
    @GET("status")
    Call<StatusResponse> checkStatus();

    //ACCOUNT
    @GET("account/v1/phone/code")
    Call<CodeResponse> getPhoneCode(@Query("phone") String phone);

    @POST("account/v1/auth/signin")
    Call<UserResponse> signIn(@Body SigninBody signinBody);

    @GET("account/v1/user/me")
    Call<User> getUser(@Header("Authorization") String accessToken);

    @PUT("account/v1/user")
    Call<User> updateUser(@Header("Authorization") String accessToken, @Body User user);

    @POST("account/v1/auth/refresh")
    Call<TokenInfo> refreshAccessToken(@Query("accessToken") String accessToken, @Query("refreshToken") String refreshToken);

    @GET("account/v1/auth/check")
    Call<UserResponse> checkAccessToken(@Query("accessToken") String accessToken);


    //CAR
    @GET("karma/v1/car/brands")
    Call<List<BrandResponse>> getBrands();

    @GET("karma/v1/car/models/by-brand/{brandId}")
    Call<List<BrandResponse>> getModels(@Path("brandId") String id);

    @GET("karma/v1/car/years")
    Call<List<BrandResponse>> getYears();

    @GET("karma/v1/car/user")
    Call<List<AllCarResponse>> getAllCars(@Header("Authorization") String accessToken);

    @GET("karma/v1/car/{carId}")
    Call<AllCarResponse> getCarById(@Header("Authorization") String accessToken, @Path("carId") String id);

    @POST("karma/v1/car")
    Call<AllCarResponse> addCar(@Header("Authorization") String accessToken, @Body AllCarResponse object);

    @DELETE("karma/v1/car/{idCar}")
    Call<CarDeleteResponse> deleteCar(@Header("Authorization") String accessToken, @Path("idCar") String idCar);

    @GET("karma/v1/class")
    Call<List<ClassServiceResponse>> getAllClassService();

    @POST("karma/v1/car/service/{idCar}/{idService}")
    Call<CarDeleteResponse> addClassService(@Path("idCar") String idCar, @Path("idService") String idService, @Header("Authorization") String accessToken);

    @POST("karma/v1/car/filter-attribute/{idCar}/{idAttribute}")
    Call<CarDeleteResponse> addCarFilter(@Path("idCar") String idCar, @Path("idAttribute") String idAttribute, @Header("Authorization") String accessToken);

    @POST("karma/v1/car/set-favorite/{idCar}")
    Call<AllCarResponse> setFavoriteCar(@Header("Authorization") String accessToken, @Path("idCar") String idCar);




    //FILTER
    @GET("karma/v1/filter/by-service-type")
    Call<List<FilterResponse>> getFilters(@Query("serviceType") String serviceType);


    //CARWASH
    @POST("karma/v1/business/search")
    Call<List<AllCarWashResponse>> getAllCarWash(@Body FilterCarWashBody filterCarWashBody);

    @GET("karma/v1/business/{carwashId}/full-model")
    Call<AllCarWashResponse> getCarWashFull(@Path("carwashId") String id);

//    @GET("karma/v1/business/{carwashId}")
//    Call<AllCarWashResponse> getCarWash(@Path("carwashId") String id);

    @GET("karma/v1/service")
    Call<List<ServiceResponse>> getAllService();

    @GET("karma/v1/business/{carwashId}/rating")
    Call<Rating> getRating(@Path("carwashId") String id);




    //RECORD
    @POST("karma/v1/record/free-time")
    Call<OrderResponse> preOrder(@Header("Authorization") String accessToken, @Body OrderBody orderBody);

    @POST("karma/v1/record")
    Call<OrderResponse> doOrder(@Header("Authorization") String accessToken, @Body OrderBody orderBody);

    @GET("karma/v1/record/client/all")
    Call<List<AllRecordResponse>> getAllRecord(@Header("Authorization") String accessToken, @Query("serviceType") String serviceType);

    @GET("karma/v1/record/{recordId}")
    Call<RecordsItem> getSingleRecord(@Header("Authorization") String accessToken, @Path("recordId") String recordId);

    @PUT("karma/v1/record/record/canceled")
    Call<AllRecordResponse> canceleRecord(@Header("Authorization") String accessToken, @Query("idRecord") String idRecord);

    //COMMENT
    @POST("karma/v1/business/{carwashId}/comment")
    Call<CommentsItem> sendComment(@Header("Authorization") String accessToken, @Path("carwashId") String carwashId, @Body CommentsItem orderBody);

    @GET("karma/v1/business/{carwashId}/comment/current-user")
    Call<CommentsItem> getMyComment(@Header("Authorization") String accessToken, @Path("carwashId") String id);

    @PUT("karma/v1/business/comment")
    Call<CommentsItem> editComment(@Header("Authorization") String accessToken, @Body CommentsItem orderBody);

    @DELETE("karma/v1/business/comment/{commentId}")
    Call<CarDeleteResponse> deleteComment(@Header("Authorization") String accessToken, @Path("commentId") String id);



}