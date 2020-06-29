package com.example.bookstoreapp.pojo;

import com.google.gson.annotations.SerializedName;

public class MerchantDetails{

	@SerializedName("merchantId")
	private String merchantId;

	@SerializedName("price")
	private String price;

	@SerializedName("merchantRating")
	private String merchantRating;

	@SerializedName("merchantName")
	private String merchantName;

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

	public void setMerchantRating(String merchantRating){
		this.merchantRating = merchantRating;
	}

	public String getMerchantRating(){
		return merchantRating;
	}

	public void setMerchantName(String merchantName){
		this.merchantName = merchantName;
	}

	public String getMerchantName(){
		return merchantName;
	}

	@Override
 	public String toString(){
		return 
			"MerchantDetails{" + 
			"merchantId = '" + merchantId + '\'' + 
			",price = '" + price + '\'' + 
			",merchantRating = '" + merchantRating + '\'' + 
			",merchantName = '" + merchantName + '\'' + 
			"}";
		}
}