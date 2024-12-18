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
}
