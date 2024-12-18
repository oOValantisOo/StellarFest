package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import database.DatabaseConnection;

public class Product {
	
	private String product_id;
	private String product_name;
	private String product_description;
	private String user_id;
	private static Connection connection;
	
	static {
		connection = DatabaseConnection.getInstance().getConnection();
	}
	
	public Product() {
		
	}

	public String getProduct_id() {
		return product_id;
	}

	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}

	public String getProduct_name() {
		return product_name;
	}

	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}

	public String getProduct_description() {
		return product_description;
	}

	public void setProduct_description(String product_description) {
		this.product_description = product_description;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	
	public static List<Product> getAllProduct(){
		String query = "SELECT * FROM products";
		List<Product> products = new ArrayList<>();

		try (PreparedStatement stmt = connection.prepareStatement(query)) {

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Product product = new Product();
					product.setProduct_id(rs.getString("product_id"));
					product.setProduct_name(rs.getString("product_name"));
					product.setProduct_description(rs.getString("product_description"));
					product.setUser_id(rs.getString("user_id"));

					products.add(product);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return products;
	}
}
