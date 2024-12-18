package views;

import java.util.List;

import controllers.ProductController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import models.Event;
import models.Product;
import utils.PageManager;

public class ManageVendor {
	
	private ProductController controller;
	private String user_id;
	ObservableList<Product> products;
	
	public ManageVendor(ProductController controller, String user_id) {
		this.controller = controller;
		this.user_id = user_id;
		this.products = FXCollections.observableArrayList(this.controller.getAllProduct());
	}
	
	private void refreshTable() {
		products.clear(); // Clear the current list
        products.addAll(this.controller.getAllProduct());
	}

	public Scene getScene() {
		VBox container = new VBox();
		container.setAlignment(Pos.CENTER_LEFT);
		
		// Back Button
		Button back = new Button(" < ");
		back.setPadding(new Insets(10));
		back.setOnAction(action -> {
			PageManager.getInstance().goBack();
		});
		
		// Table View of Products
		TableView<Product> table = new TableView<>();
        table.setEditable(true);
        TableColumn<Product, String> productId = new TableColumn<>("ID");
        TableColumn<Product, String> productName = new TableColumn<>("Name");
        TableColumn<Product, String> productDescription = new TableColumn<>("Description");
        TableColumn<Product, String> userId = new TableColumn<>("User ID");;
        
        // Binding Columns
        productId.setCellValueFactory(new PropertyValueFactory<>("product_id"));
        productName.setCellValueFactory(new PropertyValueFactory<>("product_name"));
        productDescription.setCellValueFactory(new PropertyValueFactory<>("product_description"));
        userId.setCellValueFactory(new PropertyValueFactory<>("user_id"));
        
        // Adding the column to TableView
        table.getColumns().addAll(productId, productName, productDescription,userId);
        addActionButtons(table);
        
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);        
        table.setItems(products);
        
        container.getChildren().addAll(table);
        
        // Add New Product
        GridPane formContainer = new GridPane();
        formContainer.setHgap(10);
        formContainer.setVgap(10);
        
        Label nameLabel = new Label("Name ");
        TextField nameInputField = new TextField();
        nameInputField.setPrefWidth(300);
        nameInputField.setMaxWidth(Double.MAX_VALUE);
        
        Label descriptionLabel = new Label("Description ");
        TextField descriptionInputLabel = new TextField();
        descriptionInputLabel.setPrefWidth(300);
        descriptionInputLabel.setMaxWidth(Double.MAX_VALUE);
        descriptionInputLabel.setPrefHeight(100);
        descriptionInputLabel.setMaxHeight(Double.MAX_VALUE);
        
        Button addButton = new Button("Add Product");
        addButton.setOnAction(action -> {
        	controller.manageVendor(nameInputField.getText(), descriptionInputLabel.getText());
        	refreshTable();
        });
        
        formContainer.add(nameLabel, 0, 0);
        formContainer.add(nameInputField, 1, 0);
        formContainer.add(descriptionLabel, 0, 1);
        formContainer.add(descriptionInputLabel, 1, 1);
        formContainer.add(addButton, 1, 2);
		
		BorderPane bp = new BorderPane();
		bp.setCenter(container);
		bp.setTop(back); 
		bp.setBottom(formContainer);
		
		return new Scene(bp, 300, 200);
	}
	
	private void addActionButtons(TableView<Product> table) {
	    Callback<TableColumn<Product, Void>, TableCell<Product, Void>> cellFactory = new Callback<TableColumn<Product, Void>, TableCell<Product, Void>>() {
	        @Override
	        public TableCell<Product, Void> call(TableColumn<Product, Void> param) {
	            return new TableCell<Product, Void>() {
	                private final Button deleteButton = new Button("Delete");
	                private final Button updateButton = new Button("Update");

	                {
	                    // Set the action for the delete button
	                    deleteButton.setOnAction((ActionEvent event) -> {
	                        // Get the current row's user object
	                    	Product ev = getTableRow().getItem();
	                        int index = getIndex();
	                        if (ev != null) {
	                            controller.deleteProduct(ev.getProduct_id());
	                            refreshTable();
	                        }
	                    });
	                    
	                    // Set action for detail button
	                    updateButton.setOnAction((ActionEvent event) -> {
	                    	Product ev = getTableRow().getItem();
	                    	if(ev != null) {
	                    		PageManager.getInstance().showViewProductDetail(ev);
	                    		refreshTable();
	                    	}
	                    });
	                }

	                @Override
	                protected void updateItem(Void item, boolean empty) {
	                    super.updateItem(item, empty);
	                    if (empty) {
	                        setGraphic(null);
	                    } else {
	                        HBox hbox = new HBox(10);
	                        hbox.setAlignment(Pos.CENTER_RIGHT);
	                        hbox.getChildren().addAll(updateButton, deleteButton);
	                        setGraphic(hbox);
	                    }
	                }
	            };
	        }
	    };

	    TableColumn<Product, Void> actionCol = new TableColumn<>("Actions");
	    actionCol.setCellFactory(cellFactory);

	    table.getColumns().add(actionCol);
	}
}
