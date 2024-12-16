package main;

import javafx.application.Application;
import javafx.stage.Stage;
import utils.PageManager;

public class Main extends Application{

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		PageManager.initialize(primaryStage);
		PageManager.getInstance().showHomePage("Admin");
		
		primaryStage.setWidth(800);
		primaryStage.setHeight(600);
		primaryStage.show();
	}

}
