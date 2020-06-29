package com.example.bookstoreapp.pojo;

import com.google.gson.annotations.SerializedName;

public class OrderHistory{

	@SerializedName("orderId")
	private String orderId;

	@SerializedName("timeStamp")
	private String timestamp;

	public void setOrderId(String orderId){
		this.orderId = orderId;
	}

	public String getOrderId(){
		return orderId;
	}

	public void setTimestamp(String timestamp){
		this.timestamp = timestamp;
	}

	public String getTimestamp(){
		return timestamp;
	}

	@Override
 	public String toString(){
		return 
			"OrderHistory{" + 
			"orderId = '" + orderId + '\'' + 
			",timestamp = '" + timestamp + '\'' + 
			"}";
		}
}