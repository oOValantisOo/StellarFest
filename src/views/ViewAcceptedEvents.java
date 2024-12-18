package views;

import java.util.List;

import controllers.EventController;
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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Callback;
import models.Event;
import models.Invitation;
import utils.PageManager;
import utils.UserSession;

public class ViewAcceptedEvents {
	private EventController controlller;
	List<Event> events;
	
	public ViewAcceptedEvents(EventController controlller) {
		this.controlller = controlller;
	}
	
	public Scene getScene() {
		// Mapping
		events = controlller.viewAcceptedEvents(UserSession.getInstance().getCurrUser().getUser_email()); // Email nya dipake dalam query
		
		// Back Button
		Button back = new Button(" < ");
		back.setPadding(new Insets(10));
		back.setOnAction(action -> {
			PageManager.getInstance().goBack();
		});
	
		// Main container
		VBox container = new VBox(10);
		container.setAlignment(Pos.CENTER_LEFT);
		
		BorderPane pane = new BorderPane();
		
		// Label
		Label userIdLabel = new Label(UserSession.getInstance().getCurrUser().getUser_id() +  " Accepted Invitation's");
		userIdLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
	
		pane.setLeft(userIdLabel);
		
		// TableView
		TableView<Event> table = new TableView<>();
		table.setPrefWidth(800);
        table.setEditable(false);
        
        TableColumn<Event, String> eventId = new TableColumn<>("event_id");
        TableColumn<Event, String> eventName = new TableColumn<>("event_name");
        TableColumn<Event, String> eventDate = new TableColumn<>("event_date");
        TableColumn<Event, String> eventLocation = new TableColumn<>("event_location");
        TableColumn<Event, String> eventDescription = new TableColumn<>("event_description");
        TableColumn<Event, String> organizerId = new TableColumn<>("organizer_id");
        
        // Binding Columns
        eventId.setCellValueFactory(new PropertyValueFactory<>("event_id"));
        eventName.setCellValueFactory(new PropertyValueFactory<>("event_name"));
        eventDate.setCellValueFactory(new PropertyValueFactory<>("event_date"));
        eventLocation.setCellValueFactory(new PropertyValueFactory<>("event_location"));
        eventDescription.setCellValueFactory(new PropertyValueFactory<>("event_description"));
        organizerId.setCellValueFactory(new PropertyValueFactory<>("organizer_id"));
        
        // Adding the columns to TableView
        table.getColumns().addAll(eventId, eventName, eventDate, eventLocation, eventDescription,organizerId);
        addActionButtons(table);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        ObservableList<Event> userObservableList = FXCollections.observableArrayList(events);
        table.setItems(userObservableList);
		
		container.getChildren().addAll(pane,table);
		
		BorderPane bp = new BorderPane();
		bp.setCenter(container);
		bp.setTop(back);
		
		return new Scene(bp, 300, 200);
	}
	
	private void addActionButtons(TableView<Event> table) {
		 Callback<TableColumn<Event, Void>, TableCell<Event, Void>> cellFactory = new Callback<TableColumn<Event, Void>, TableCell<Event, Void>>() {
	        @Override
	        public TableCell<Event, Void> call(TableColumn<Event, Void> param) {
	            return new TableCell<Event, Void>() {
	                private final Button detailButton = new Button("View Detail");

	                {
	                    // Set the action for the delete button
	                	detailButton.setOnAction((ActionEvent event) -> {
	                        // Get the current row's user object
	                		Event ev = getTableRow().getItem();
	                        int index = getIndex();
	                        if (ev != null) {
	                        	PageManager.getInstance().showEventDetail(ev.getEvent_id());
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
	                        hbox.getChildren().addAll(detailButton);
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
}
