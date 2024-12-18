package main;

import javafx.application.Application;
import javafx.stage.Stage;
import models.User;
import utils.PageManager;
import utils.UserSession;

public class Main extends Application{

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		//TODO: to be removed dummy authentication
		UserSession.authenticate(User.getUserById("US007"));
		
		// TODO Auto-generated method stub
		PageManager.initialize(primaryStage);
		PageManager.getInstance().showHomePage("Vendor");
		
		primaryStage.setWidth(1600);
		primaryStage.setHeight(900);
		primaryStage.show();
	}

}
