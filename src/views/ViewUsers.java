package views;

import java.util.List;

import controllers.UserController;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.util.Callback;
import models.User;
import utils.PageManager;

public class ViewUsers {
	private UserController controller;
    List<User> users;
    
    Label userCount;

	public ViewUsers(UserController controller) {
		super();
		this.controller = controller;
	}
	
	public Scene getScene() {
		// Mapping
        users = controller.getAllUser();
        
        // Main Container
		VBox container = new VBox(12);
		container.setAlignment(Pos.CENTER_LEFT);
		container.setPrefWidth(800);
		container.setPadding(new Insets(10,400,10,400));
		
		// Subheader
		Label subHeader = new Label("Manage users access");
		subHeader.setFont(Font.font("Arial", FontWeight.NORMAL, 18));
		
		// Back Button
		Button back = new Button(" < ");
		back.setPadding(new Insets(10));
		back.setOnAction(action -> {
			PageManager.getInstance().goBack();
		});
		
		// Header
		Label header = new Label("Users");
		header.setFont(Font.font("Arial", FontWeight.BOLD, 32));
		
		// Dynamic User Count
		userCount =  new Label(users.size() + " members");
		userCount.setFont(Font.font("Arial", FontPosture.ITALIC, 18));
		
		//  Header HBox
		HBox hbox = new HBox(20);
		hbox.setAlignment(Pos.BASELINE_LEFT);
		hbox.getChildren().addAll(header, userCount);
        
		// TableView
        TableView<User> table = new TableView<>();
        table.setEditable(true);
        TableColumn<User, String> userNameCol = new TableColumn<>("Username");
        TableColumn<User, String> emailCol = new TableColumn<>("Email");
        TableColumn<User, String> roleCol = new TableColumn<>("Role");
        
        // Binding Columns
        userNameCol.setCellValueFactory(new PropertyValueFactory<>("user_name"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("user_email"));
        roleCol.setCellValueFactory(new PropertyValueFactory<>("user_role"));
        
        // Adding the column to TableView
        table.getColumns().addAll(userNameCol, emailCol, roleCol);
        addActionButtons(table);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        ObservableList<User> userObservableList = FXCollections.observableArrayList(users);
        
        table.setItems(userObservableList);
		
		container.getChildren().addAll(subHeader, hbox, table);
		
		// Pane
		BorderPane bp = new BorderPane();
		bp.setCenter(container);
		bp.setTop(back);
		
		return new Scene(bp,800,600);
	}
	
	private void addActionButtons(TableView<User> table) {
	    Callback<TableColumn<User, Void>, TableCell<User, Void>> cellFactory = new Callback<TableColumn<User, Void>, TableCell<User, Void>>() {
	        @Override
	        public TableCell<User, Void> call(TableColumn<User, Void> param) {
	            return new TableCell<User, Void>() {
	                private final Button deleteButton = new Button("Delete");

	                {
	          
	                    deleteButton.setOnAction((ActionEvent event) -> {
	                        User user = getTableRow().getItem();
	                        int index = getIndex();
	                        if (user != null) {
	                            controller.deleteUser(user.getUser_id());
	                            
	                            table.getItems().remove(index);
	                        
	                            updateUserCounter();
	                        }
	                    });
	                }

	                @Override
	                protected void updateItem(Void item, boolean empty) {
	                    super.updateItem(item, empty);
	                    if (empty) {
	                        setGraphic(null);
	                    } else {
	                        setGraphic(deleteButton);
	                    }
	                }
	            };
	        }
	    };

	    TableColumn<User, Void> actionCol = new TableColumn<>("Actions");
	    actionCol.setCellFactory(cellFactory);

	    table.getColumns().add(actionCol);
	}
	
	private void updateUserCounter() {
        users = controller.getAllUser();
        userCount.setText(users.size() + " members");
    }

}
