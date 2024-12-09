package views;

import java.util.List;

import controllers.EventController;
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
import models.Event;
import models.User;
import utils.PageManager;

public class ViewEvents {
	private EventController controller;
    List<Event> events;
    
    Label userCount;

	public ViewEvents(EventController controller) {
		super();
		this.controller = controller;
	}
	
	@SuppressWarnings("unchecked")
	public Scene getScene() {
		// Mapping
		events = controller.getAllEvent();
        
        // Main Container
		VBox container = new VBox(12);
		container.setAlignment(Pos.CENTER_LEFT);
		container.setPrefWidth(800);
		container.setPadding(new Insets(10,400,10,400));
		
		// Subheader
		Label subHeader = new Label("Manage events");
		subHeader.setFont(Font.font("Arial", FontWeight.NORMAL, 18));
		
		// Header
		Label header = new Label("Events");
		header.setFont(Font.font("Arial", FontWeight.BOLD, 32));
		
		// Dynamic User Count
		userCount =  new Label(events.size() + " events");
		userCount.setFont(Font.font("Arial", FontPosture.ITALIC, 18));
		
		//  Header HBox
		HBox hbox = new HBox(20);
		hbox.setAlignment(Pos.BASELINE_LEFT);
		hbox.getChildren().addAll(header, userCount);
        
		// TableView
        TableView<Event> table = new TableView<>();
        table.setEditable(true);
        TableColumn<Event, String> eventId = new TableColumn<>("ID");
        TableColumn<Event, String> eventName = new TableColumn<>("Name");
        TableColumn<Event, String> eventDate = new TableColumn<>("Date");
        TableColumn<Event, String> eventLocation = new TableColumn<>("Location");
        TableColumn<Event, String> eventDescription = new TableColumn<>("Description");
        TableColumn<Event, String> organizerId = new TableColumn<>("Organizer ID");
        
        // Binding Columns
        eventId.setCellValueFactory(new PropertyValueFactory<>("event_id"));
        eventName.setCellValueFactory(new PropertyValueFactory<>("event_name"));
        eventDate.setCellValueFactory(new PropertyValueFactory<>("event_date"));
        eventLocation.setCellValueFactory(new PropertyValueFactory<>("event_location"));
        eventDescription.setCellValueFactory(new PropertyValueFactory<>("event_description"));
        organizerId.setCellValueFactory(new PropertyValueFactory<>("organizer_id"));
        
        // Adding the column to TableView
        table.getColumns().addAll(eventId, eventName, eventDate,eventLocation,eventDescription,organizerId);
        addActionButtons(table);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        ObservableList<Event> userObservableList = FXCollections.observableArrayList(events);
        
        table.setItems(userObservableList);
		
		container.getChildren().addAll(subHeader, hbox, table);
		
		// Pane
		BorderPane bp = new BorderPane();
		bp.setCenter(container);
		
		return new Scene(bp,1600,900);
	}
	
	private void addActionButtons(TableView<Event> table) {
	    Callback<TableColumn<Event, Void>, TableCell<Event, Void>> cellFactory = new Callback<TableColumn<Event, Void>, TableCell<Event, Void>>() {
	        @Override
	        public TableCell<Event, Void> call(TableColumn<Event, Void> param) {
	            return new TableCell<Event, Void>() {
	                private final Button deleteButton = new Button("Delete");
	                private final Button detailButton = new Button("Detail");

	                {
	                    // Set the action for the delete button
	                    deleteButton.setOnAction((ActionEvent event) -> {
	                        // Get the current row's user object
	                        Event ev = getTableRow().getItem();
	                        int index = getIndex();
	                        if (ev != null) {
	                            controller.deleteEvent(ev.getEvent_id());
	                            
	                            table.getItems().remove(index);
	                        
	                            updateUserCounter();
	                        }
	                    });
	                    
	                    // Set action for detail button
	                    detailButton.setOnAction((ActionEvent event) -> {
	                    	Event ev = getTableRow().getItem();
	                    	if(ev != null) {
	                    		PageManager.getInstance().showEventDetail();
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
	                        hbox.getChildren().addAll(detailButton, deleteButton);
	                        setGraphic(hbox);
	                    }
	                }
	            };
	        }
	    };

	    TableColumn<Event, Void> actionCol = new TableColumn<>("Actions");
	    actionCol.setCellFactory(cellFactory);

	    table.getColumns().add(actionCol);
	}
	
	private void updateUserCounter() {
        events = controller.getAllEvent();
        userCount.setText(events.size() + " events");
    }

}
