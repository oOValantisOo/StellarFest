package controllers;

import java.sql.Connection;
import java.util.List;

import database.DatabaseConnection;
import models.Product;

public class ProductController {
	private Connection connection;
	
	public ProductController() {
		this.connection = DatabaseConnection.getInstance().getConnection();
	}
	
	public List<Product> getAllProduct(){
		return Product.getAllProduct();
	}

	public void deleteProduct(String product_id){
		Product.deleteProduct(product_id);
		return;
	}

	public void manageVendor(String description, String name){
		Product.manageVendor(description, name);
		return;
	}
	
	public String updateVendor(String name, String description, String product_id, String user_id) {
		return Product.updateProfile(name, description, product_id, user_id);
	}
}
