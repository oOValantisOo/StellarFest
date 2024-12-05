package views;

import java.util.List;

import controllers.UserController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import models.User;

public class ViewUsers {
	private UserController controller;

	public ViewUsers(UserController controller) {
		super();
		this.controller = controller;
	}
	
	@SuppressWarnings("unchecked")
	public Scene getScene() {
        // Mapping
        List<User> users = controller.getAllUser();
        
        // Main Container
		VBox container = new VBox(12);
		container.setAlignment(Pos.CENTER_LEFT);
		container.setPrefWidth(800);
		container.setPadding(new Insets(10,400,10,400));
		
		// Subheader
		Label subHeader = new Label("Manage users access");
		subHeader.setFont(Font.font("Arial", FontWeight.NORMAL, 18));
		
		// Header
		Label header = new Label("Users");
		header.setFont(Font.font("Arial", FontWeight.BOLD, 32));
		
		// Dynamic User Count
		Label userCount =  new Label(users.size() + " members");
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
        
        table.getColumns().addAll(userNameCol, emailCol, roleCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        ObservableList<User> userObservableList = FXCollections.observableArrayList(users);
        
        table.setItems(userObservableList);
		
		container.getChildren().addAll(subHeader, hbox, table);
		
		// Pane
		BorderPane bp = new BorderPane();
		bp.setCenter(container);
		
		return new Scene(bp,1600,900);
	}
}
