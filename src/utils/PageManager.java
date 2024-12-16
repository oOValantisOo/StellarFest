 package utils;

import java.util.Stack;

import controllers.EventController;
import controllers.EventOrganizerController;
import controllers.UserController;
import javafx.scene.Scene;
import javafx.stage.Stage;
import views.HomeAdmin;
import views.LoginPage;
import views.RegisterPage;
import views.ViewEventDetail;
import views.ViewEvents;
import views.ViewOrganizedEvents;
import views.ViewUsers;

public class PageManager {
	private static PageManager instance;
	private Stage stage;
	private Stack<Scene> historyScene;

	private PageManager(Stage primaryStage) {
		stage = primaryStage;
		historyScene = new Stack<Scene>();
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
	
	public void goBack() {
		if(!historyScene.isEmpty()) {
			Scene previous = historyScene.pop();
			stage.setScene(previous);
		}
	}
	
	private void navigateTo(Scene scene) {
		if(stage.getScene() != null) {
			historyScene.push(stage.getScene());
		}
		stage.setScene(scene);
	}

	public void showLoginPage() {
		UserController uc = new UserController();
		LoginPage loginPage = new LoginPage(uc);
		navigateTo(loginPage.getScene());
	}

	public void showRegisterPage() {
		UserController uc = new UserController();
		RegisterPage regisPage = new RegisterPage(uc);
		navigateTo(regisPage.getScene());
	}

	public void showViewUsers() {
		UserController uc = new UserController();
		ViewUsers viewUserPage = new ViewUsers(uc);
		navigateTo(viewUserPage.getScene());
	}

	public void showViewOrganizedEvents() {
		EventOrganizerController controller = new EventOrganizerController();
		ViewOrganizedEvents view = new ViewOrganizedEvents(controller);
		navigateTo(view.getScene());
	}

    public void showViewEvents() {
    	EventController ec = new EventController();
    	ViewEvents viewEventsPage = new ViewEvents(ec);
    	navigateTo(viewEventsPage.getScene());
    }
    
    public void showEventDetail(String id) {
    	EventOrganizerController eoc = new EventOrganizerController();
    	ViewEventDetail viewEventDetail = new ViewEventDetail(eoc, id);
    	navigateTo(viewEventDetail.getScene());
    }
    
    public void showHomePage(String role) {
    	if(role == "Admin") {
    		
    		HomeAdmin homeAdmin = new HomeAdmin();
    		navigateTo(homeAdmin.getScene());
    	}
    }

}
