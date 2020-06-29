package com.example.bookstoreapp.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.LinkedHashMap;

public class Books{

	@SerializedName("merchantId")
	private String merchantId;

	@SerializedName("productId")
	private String productId;

	@SerializedName("author")
	private String author;

	@SerializedName("price")
	private String price;

	@SerializedName("isbn")
	private String isbn;

	@SerializedName("genre")
	private String genre;

	@SerializedName("rating")
	private String rating;

	@SerializedName("description")
	private String description;

	@SerializedName("attributes")
	private LinkedHashMap<String,String> Attributes;

	@SerializedName("productName")
	private String productName;

	@SerializedName("url")
	private String url;

	@SerializedName("quantity")
	private String quantity;

	public void setProductId(String productId){
		this.productId = productId;
	}

	public String getProductId(){
		return productId;
	}

	public void setAuthor(String author){
		this.author = author;
	}

	public String getAuthor(){
		return author;
	}

	public void setPrice(String price){
		this.price = price;
	}

	public String getPrice(){
		return price;
	}

	public void setIsbn(String isbn){
		this.isbn = isbn;
	}

	public String getIsbn(){
		return isbn;
	}

	public void setGenre(String genre){
		this.genre = genre;
	}

	public String getGenre(){
		return genre;
	}

	public void setRating(String rating){
		this.rating = rating;
	}

	public String getRating(){
		return rating;
	}

	public void setDescription(String description){
		this.description = description;
	}

	public String getDescription(){
		return description;
	}

	public void setAttributes(LinkedHashMap<String,String> attributes){
		this.Attributes = attributes;
	}

	public LinkedHashMap<String, String> getAttributes(){
		return Attributes;
	}

	public void setProductName(String productName){
		this.productName = productName;
	}

	public String getProductName(){
		return productName;
	}

	public void setUrl(String url){
		this.url = url;
	}

	public String getUrl(){
		return url;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	@Override
 	public String toString(){
		return 
			"Books{" + 
			"productId = '" + productId + '\'' + 
			",author = '" + author + '\'' + 
			",price = '" + price + '\'' + 
			",isbn = '" + isbn + '\'' + 
			",genre = '" + genre + '\'' + 
			",rating = '" + rating + '\'' + 
			",description = '" + description + '\'' + 
			",Attributes = '" + Attributes + '\'' +
			",productName = '" + productName + '\'' + 
			",url = '" + url + '\'' +
			",merchantId = '" + merchantId + '\'' +
			",quantity = '" + quantity + '\'' +
			"}";
		}
}