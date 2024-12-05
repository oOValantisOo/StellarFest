package utils;

import controllers.UserController;
import javafx.stage.Stage;
import views.LoginPage;
import views.RegisterPage;

public class PageManager {
	private static PageManager instance;
	private Stage stage;
	
	private PageManager(Stage primaryStage) {
		stage = primaryStage;
	}
	
	public static void initialize(Stage stage) {
        instance = new PageManager(stage);
    }

    public static PageManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("PageManager is not initialized. Call initialize() first.");
        }
        return instance;
    }
    
    
    public void showLoginPage() {
    	UserController uc = new UserController();
    	LoginPage loginPage = new LoginPage(uc);
    	stage.setScene(loginPage.getScene());
    }
    
    public void showRegisterPage() {
    	UserController uc = new UserController();
    	RegisterPage regisPage = new RegisterPage(uc);
    	stage.setScene(regisPage.getScene());
    }
    
	
	
}
