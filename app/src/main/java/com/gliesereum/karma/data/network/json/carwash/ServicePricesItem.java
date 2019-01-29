package com.gliesereum.karma.data.network.json.carwash;

import com.gliesereum.karma.data.network.json.filter.AttributesItem;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class ServicePricesItem {

	@SerializedName("duration")
	private int duration;

	@SerializedName("serviceClass")
	private List<ServiceClassItem> serviceClass;

	@SerializedName("attributes")
	private List<AttributesItem> attributes;

	@SerializedName("price")
	private int price;

	@SerializedName("corporationServiceId")
	private String corporationServiceId;

	@SerializedName("description")
	private String description;

	@SerializedName("service")
	private Service service;

	@SerializedName("name")
	private String name;

	@SerializedName("id")
	private String id;

	@SerializedName("serviceId")
	private String serviceId;

	@SerializedName("businessId")
	private String businessId;

	@SerializedName("carBodies")
	private List<String> carBodies;

	@SerializedName("interiorTypes")
	private List<String> interiorTypes;

	public List<AttributesItem> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<AttributesItem> attributes) {
		this.attributes = attributes;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getDuration() {
		return duration;
	}

	public void setServiceClass(List<ServiceClassItem> serviceClass) {
		this.serviceClass = serviceClass;
	}

	public List<ServiceClassItem> getServiceClass() {
		return serviceClass;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getPrice() {
		return price;
	}

	public void setCorporationServiceId(String corporationServiceId) {
		this.corporationServiceId = corporationServiceId;
	}

	public String getCorporationServiceId() {
		return corporationServiceId;
	}

	public void setService(Service service) {
		this.service = service;
	}

	public Service getService() {
		return service;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setCarBodies(List<String> carBodies) {
		this.carBodies = carBodies;
	}

	public List<String> getCarBodies() {
		return carBodies;
	}

	public void setInteriorTypes(List<String> interiorTypes) {
		this.interiorTypes = interiorTypes;
	}

	public List<String> getInteriorTypes() {
		return interiorTypes;
	}
}