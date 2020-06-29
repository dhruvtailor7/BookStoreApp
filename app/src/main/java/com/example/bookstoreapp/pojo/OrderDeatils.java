package com.example.bookstoreapp.pojo;

import com.google.gson.annotations.SerializedName;

public class OrderDeatils{

	@SerializedName("timeStamp")
	private String timeStamp;

	@SerializedName("quantity")
	private String quantity;

	@SerializedName("cost")
	private String cost;

	@SerializedName("productId")
	private String productId;

	@SerializedName("orderId")
	private String orderId;

	@SerializedName("merchantId")
	private String merchantId;

	@SerializedName("price")
	private String price;

	@SerializedName("customerEmail")
	private String customerEmail;

	@SerializedName("customerId")
	private String customerId;

	@SerializedName("productName")
	private String productName;

	@SerializedName("url")
	private String url;

	public void setTimeStamp(String timeStamp){
		this.timeStamp = timeStamp;
	}

	public String getTimeStamp(){
		return timeStamp;
	}

	public void setQuantity(String quantity){
		this.quantity = quantity;
	}

	public String getQuantity(){
		return quantity;
	}

	public void setCost(String cost){
		this.cost = cost;
	}

	public String getCost(){
		return cost;
	}

	public void setProductId(String productId){
		this.productId = productId;
	}

	public String getProductId(){
		return productId;
	}

	public void setOrderId(String orderId){
		this.orderId = orderId;
	}

	public String getOrderId(){
		return orderId;
	}

	public void setMerchantId(String merchantId){
		this.merchantId = merchantId;
	}

	public String getMerchantId(){
		return merchantId;
	}

	public void setPrice(String price){
		this.price = price;
	}

	public String getPrice(){
		return price;
	}

	public void setCustomerEmail(String customerEmail){
		this.customerEmail = customerEmail;
	}

	public String getCustomerEmail(){
		return customerEmail;
	}

	public void setCustomerId(String customerId){
		this.customerId = customerId;
	}

	public String getCustomerId(){
		return customerId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
 	public String toString(){
		return 
			"OrderDeatils{" + 
			"timeStamp = '" + timeStamp + '\'' + 
			",quantity = '" + quantity + '\'' + 
			",cost = '" + cost + '\'' + 
			",productId = '" + productId + '\'' + 
			",orderId = '" + orderId + '\'' + 
			",merchantId = '" + merchantId + '\'' + 
			",price = '" + price + '\'' + 
			",customerEmail = '" + customerEmail + '\'' + 
			",customerId = '" + customerId + '\'' +
			",productName = '" + productName + '\'' +
			",url = '" + url + '\'' +"}";
		}
}