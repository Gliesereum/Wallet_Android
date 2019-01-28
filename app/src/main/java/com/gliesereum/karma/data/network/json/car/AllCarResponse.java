package com.gliesereum.karma.data.network.json.car;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class AllCarResponse {

	@SerializedName("note")
	private Object note;

	@SerializedName("carBody")
	private String carBody;

	@SerializedName("modelId")
	private String modelId;

	@SerializedName("year")
	private Year year;

	@SerializedName("description")
	private String description;

	@SerializedName("services")
	private List<ServicesItem> services;

	@SerializedName("userId")
	private String userId;

	@SerializedName("yearId")
	private String yearId;

	@SerializedName("interior")
	private String interior;

	@SerializedName("colour")
	private String colour;

	@SerializedName("registrationNumber")
	private String registrationNumber;

	@SerializedName("brandId")
	private String brandId;

	@SerializedName("model")
	private Model model;

	@SerializedName("id")
	private String id;

	@SerializedName("brand")
	private Brand brand;

	public void setNote(Object note) {
		this.note = note;
	}

	public Object getNote() {
		return note;
	}

	public void setCarBody(String carBody) {
		this.carBody = carBody;
	}

	public String getCarBody() {
		return carBody;
	}

	public void setModelId(String modelId) {
		this.modelId = modelId;
	}

	public String getModelId() {
		return modelId;
	}

	public void setYear(Year year) {
		this.year = year;
	}

	public Year getYear() {
		return year;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setServices(List<ServicesItem> services) {
		this.services = services;
	}

	public List<ServicesItem> getServices() {
		return services;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserId() {
		return userId;
	}

	public void setYearId(String yearId) {
		this.yearId = yearId;
	}

	public String getYearId() {
		return yearId;
	}

	public void setInterior(String interior) {
		this.interior = interior;
	}

	public String getInterior() {
		return interior;
	}

	public void setColour(String colour) {
		this.colour = colour;
	}

	public String getColour() {
		return colour;
	}

	public void setRegistrationNumber(String registrationNumber) {
		this.registrationNumber = registrationNumber;
	}

	public String getRegistrationNumber() {
		return registrationNumber;
	}

	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}

	public String getBrandId() {
		return brandId;
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public Model getModel() {
		return model;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}

	public Brand getBrand() {
		return brand;
	}

	public AllCarResponse(String brandId, String modelId, String yearId, String registrationNumber, String description, String interior, String carBody, String colour) {
		this.brandId = brandId;
		this.modelId = modelId;
		this.yearId = yearId;
		this.registrationNumber = registrationNumber;
		this.description = description;
		this.interior = interior;
		this.carBody = carBody;
		this.colour = colour;
	}

    public AllCarResponse(String brandId, String modelId, String yearId, String registrationNumber, String description) {
        this.brandId = brandId;
        this.modelId = modelId;
        this.yearId = yearId;
        this.registrationNumber = registrationNumber;
        this.description = description;
    }
}