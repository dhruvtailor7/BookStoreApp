package com.example.bookstoreapp.pojo;

import com.google.gson.annotations.SerializedName;

public class Customer{

	@SerializedName("pincode")
	private String pincode;

	@SerializedName("password")
	private String password;

	@SerializedName("address")
	private String address;

	@SerializedName("loginType")
	private String loginType;

	@SerializedName("name")
	private String name;

	@SerializedName("phoneNumber")
	private String phoneNumber;

	@SerializedName("customerId")
	private String customerId;

	@SerializedName("email")
	private String email;



	public void setPincode(String pincode){
		this.pincode = pincode;
	}

	public String getPincode(){
		return pincode;
	}

	public void setPassword(String password){
		this.password = password;
	}

	public String getPassword(){
		return password;
	}

	public void setAddress(String address){
		this.address = address;
	}

	public String getAddress(){
		return address;
	}

	public void setLoginType(String loginType){
		this.loginType = loginType;
	}

	public String getLoginType(){
		return loginType;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setPhoneNumber(String phoneNumber){
		this.phoneNumber = phoneNumber;
	}

	public String getPhoneNumber(){
		return phoneNumber;
	}

	public void setCustomerId(String customerId){
		this.customerId = customerId;
	}

	public String getCustomerId(){
		return customerId;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}

	@Override
 	public String toString(){
		return 
			"Customer{" + 
			"pincode = '" + pincode + '\'' + 
			",password = '" + password + '\'' + 
			",address = '" + address + '\'' + 
			",loginType = '" + loginType + '\'' +
			",name = '" + name + '\'' + 
			",phone_number = '" + phoneNumber + '\'' + 
			",customer_id = '" + customerId + '\'' + 
			",email = '" + email + '\'' + 
			"}";
		}
}