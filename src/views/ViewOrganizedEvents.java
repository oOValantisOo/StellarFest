package views;



import java.util.List;

import controllers.EventOrganizerController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import models.Event;
import utils.PageManager;
import utils.UserSession;

public class ViewOrganizedEvents {
	private EventOrganizerController controller;
	private List<Event> events;

	public ViewOrganizedEvents(EventOrganizerController controller) {
		super();
		this.controller = controller;
		
		events = controller.viewOrganizedEvents(UserSession.getInstance().getCurrUser().getUser_id());
		System.out.println(UserSession.getInstance().getCurrUser().getUser_id());
	}
	
	@SuppressWarnings("unchecked")
	public Scene getScene() {
		VBox container = new VBox();
		container.setAlignment(Pos.CENTER);
		container.setPadding(new Insets(50));
		
		VBox main = new VBox(10);
		
		Label pageLbl = new Label("View Organized Events");
		pageLbl.setFont(Font.font("Arial", FontWeight.BOLD, 24));
		
        TableView<Event> eventsTable = new TableView<>();

        ObservableList<Event> eventList = FXCollections.observableArrayList(events);

        eventsTable.setItems(eventList);

        TableColumn<Event, String> idColumn = new TableColumn<>("Event ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("event_id"));

        TableColumn<Event, String> nameColumn = new TableColumn<>("Event Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("event_name"));

        TableColumn<Event, String> dateColumn = new TableColumn<>("Event Date");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("event_date"));

        TableColumn<Event, String> locationColumn = new TableColumn<>("Event Location");
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("event_location"));


        eventsTable.getColumns().addAll(idColumn, nameColumn, dateColumn, locationColumn);

        eventsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        eventsTable.setOnMouseClicked(event -> {                       
            if (event.getClickCount() == 2) { 
                Event e = eventsTable.getSelectionModel().getSelectedItem();
                if (e != null) {
                   String eventId = e.getEvent_id();
                   
                   // TODO: Add navigate to detail page
                }
            }
        });
        
        Label pageGuide = new Label("Double-Click a row to view the full detail of an event!");
        pageGuide.setFont(Font.font("Arial", FontWeight.BOLD, 16));
		
		main.getChildren().addAll(pageLbl, eventsTable, pageGuide);
		
		container.getChildren().add(main);
		
		return new Scene(container, 400, 300);
		
	}
}
