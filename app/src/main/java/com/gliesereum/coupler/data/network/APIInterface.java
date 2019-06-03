package com.gliesereum.coupler.data.network;

import com.gliesereum.coupler.data.network.json.businesscategory.BusinesCategoryResponse;
import com.gliesereum.coupler.data.network.json.car.AllCarResponse;
import com.gliesereum.coupler.data.network.json.car.BrandResponse;
import com.gliesereum.coupler.data.network.json.car.CarDeleteResponse;
import com.gliesereum.coupler.data.network.json.carwash.AllCarWashResponse;
import com.gliesereum.coupler.data.network.json.carwash.CommentsItem;
import com.gliesereum.coupler.data.network.json.carwash.FilterCarWashBody;
import com.gliesereum.coupler.data.network.json.carwash.Rating;
import com.gliesereum.coupler.data.network.json.carwashnew.CarWashResponse;
import com.gliesereum.coupler.data.network.json.classservices.ClassServiceResponse;
import com.gliesereum.coupler.data.network.json.code.CodeResponse;
import com.gliesereum.coupler.data.network.json.code.SigninBody;
import com.gliesereum.coupler.data.network.json.filter.FilterResponse;
import com.gliesereum.coupler.data.network.json.notificatoin.NotificatoinBody;
import com.gliesereum.coupler.data.network.json.notificatoin.RegistrationTokenDeleteResponse;
import com.gliesereum.coupler.data.network.json.order.OrderBody;
import com.gliesereum.coupler.data.network.json.order.OrderResponse;
import com.gliesereum.coupler.data.network.json.record.AllRecordResponse;
import com.gliesereum.coupler.data.network.json.service.ServiceResponse;
import com.gliesereum.coupler.data.network.json.status.StatusResponse;
import com.gliesereum.coupler.data.network.json.user.User;
import com.gliesereum.coupler.data.network.json.user.UserResponse;

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

    @GET("karma/v1/business-category")
    Call<List<BusinesCategoryResponse>> getBusinessCategory();

    //NOTIFICATION
    @POST("notification/v1/user-device")
    Call<NotificatoinBody> sendRegistrationToken(@Header("Authorization") String accessToken, @Body NotificatoinBody notificatoinBody);

    @DELETE("notification/v1/user-device")
    Call<RegistrationTokenDeleteResponse> deleteRegistrationToken(@Header("Authorization") String accessToken, @Query("registrationToken") String registrationToken);


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
    Call<UserResponse> refreshAccessToken(@Query("refreshToken") String refreshToken);

    @GET("account/v1/auth/check")
    Call<UserResponse> checkAccessToken(@Query("accessToken") String accessToken);


    //CAR
    @GET("karma/v1/car/brands")
    Call<List<BrandResponse>> getBrands(@Header("Authorization") String accessToken);

    @GET("karma/v1/car/models/by-brand/{brandId}")
    Call<List<BrandResponse>> getModels(@Header("Authorization") String accessToken, @Path("brandId") String id);

    @GET("karma/v1/car/years")
    Call<List<BrandResponse>> getYears(@Header("Authorization") String accessToken);

    @GET("karma/v1/car/user")
    Call<List<AllCarResponse>> getAllCars(@Header("Authorization") String accessToken);

    @GET("karma/v1/car/{carId}")
    Call<AllCarResponse> getCarById(@Header("Authorization") String accessToken, @Path("carId") String id);

    @POST("karma/v1/car")
    Call<AllCarResponse> addCar(@Header("Authorization") String accessToken, @Body AllCarResponse object);

    @DELETE("karma/v1/car/{idCar}")
    Call<CarDeleteResponse> deleteCar(@Header("Authorization") String accessToken, @Path("idCar") String idCar);

    @GET("karma/v1/class")
    Call<List<ClassServiceResponse>> getAllClassService(@Header("Authorization") String accessToken);

    @POST("karma/v1/car/service/{idCar}/{idService}")
    Call<CarDeleteResponse> addClassService(@Path("idCar") String idCar, @Path("idService") String idService, @Header("Authorization") String accessToken);

    @POST("karma/v1/car/filter-attribute/{idCar}/{idAttribute}")
    Call<CarDeleteResponse> addCarFilter(@Path("idCar") String idCar, @Path("idAttribute") String idAttribute, @Header("Authorization") String accessToken);

    @POST("karma/v1/car/set-favorite/{idCar}")
    Call<AllCarResponse> setFavoriteCar(@Header("Authorization") String accessToken, @Path("idCar") String idCar);


    //FILTER
    @GET("karma/v1/filter/by-business-category")
    Call<List<FilterResponse>> getFilters(@Query("businessCategoryId") String businessCategoryId);

    @GET("karma/v1/filter/by-business-type")
    Call<List<FilterResponse>> getFiltersBusinessType(@Query("businessType") String businessType);


    //CARWASH
    @POST("karma/v1/business/search")
    Call<List<AllCarWashResponse>> getAllCarWash(@Body FilterCarWashBody filterCarWashBody);

    @POST("karma/v1/business/search/document")
    Call<List<CarWashResponse>> getAllCarWashNew(@Body FilterCarWashBody filterCarWashBody);

    @GET("karma/v1/business/{carwashId}/full-model")
    Call<AllCarWashResponse> getCarWashFull(@Header("Authorization") String accessToken, @Path("carwashId") String id);

//    @GET("karma/v1/business/{carwashId}")
//    Call<AllCarWashResponse> getCarWash(@Path("carwashId") String id);

    @GET("karma/v1/service/by-business-category")
    Call<List<ServiceResponse>> getAllService(@Query("businessCategoryId") String businessCategoryId);

    @GET("karma/v1/business/{carwashId}/rating")
    Call<Rating> getRating(@Header("Authorization") String accessToken, @Path("carwashId") String id);


    //RECORD
    @POST("karma/v1/record/free-time")
    Call<OrderResponse> preOrder(@Header("Authorization") String accessToken, @Body OrderBody orderBody);

    @POST("karma/v1/record")
    Call<AllRecordResponse> doOrder(@Header("Authorization") String accessToken, @Body OrderBody orderBody);

    @GET("karma/v1/record/client/all")
    Call<List<AllRecordResponse>> getAllRecord(@Header("Authorization") String accessToken);

    @GET("karma/v1/record/{recordId}")
    Call<AllRecordResponse> getSingleRecord(@Header("Authorization") String accessToken, @Path("recordId") String recordId);

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