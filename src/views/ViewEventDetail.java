package views;

import java.sql.SQLException;
import java.util.List;

import controllers.EventOrganizerController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import models.Event;
import models.User;
import utils.PageManager;
import utils.UserSession;

public class ViewEventDetail {
    private EventOrganizerController controller;
    private Event event;

    public ViewEventDetail(EventOrganizerController controller, String eventId) {
        this.controller = controller;
        this.event = this.controller.viewOrganizedEventDetails(eventId);
    }

    public Scene getScene() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        Button back = new Button(" < ");
        back.setPadding(new Insets(10));
        back.setOnAction(action -> PageManager.getInstance().goBack());

        VBox backButtonContainer = new VBox();
        backButtonContainer.setPadding(new Insets(10));
        backButtonContainer.setAlignment(Pos.TOP_LEFT);
        backButtonContainer.getChildren().add(back);
        root.setLeft(backButtonContainer);

        VBox container = new VBox(20);
        container.setAlignment(Pos.TOP_CENTER);
        container.setPadding(new Insets(20));

        Label eventId = new Label("Event ID : " + event.getEvent_id());
        Label organizerId = new Label("Organized By : " + event.getOrganizer_id());
        Label eventName = new Label(event.getEvent_name());
        eventName.setFont(Font.font("Arial", FontWeight.BOLD, 32));

        HBox header = new HBox(50);
        header.setAlignment(Pos.CENTER);
        header.getChildren().addAll(eventId, organizerId);

        Label eventDate = new Label(event.getEvent_date());
        Label eventLocation = new Label(event.getEvent_location());
        Label eventDescription = new Label(event.getEvent_description());

        container.getChildren().addAll(header, eventName, eventDate, eventLocation, eventDescription);

        // Check if the user's role is "Event Organizer"
        if ("Event Organizer".equals(UserSession.getInstance().getCurrUser().getUser_role())) {
            Button addVendorBtn = new Button("Add Vendor");
            Button addGuestBtn = new Button("Add Guest");
            Button editEventNameBtn = new Button("Edit Event Name");

            addVendorBtn.setOnAction(event -> {
                try {
                    PageManager.getInstance().showAddVendor(this.event.getEvent_id());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });

            addGuestBtn.setOnAction(event -> {
                try {
                    PageManager.getInstance().showAddGuest(this.event.getEvent_id());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });

            editEventNameBtn.setOnAction(event -> {
                try {
                    PageManager.getInstance().showEditEventName(this.event.getEvent_id());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });

            HBox buttonContainer = new HBox(10);
            buttonContainer.setAlignment(Pos.CENTER);
            buttonContainer.getChildren().addAll(addVendorBtn, addGuestBtn, editEventNameBtn);
            container.getChildren().add(buttonContainer);

            Label guestsLabel = new Label("Guests Attending:");
            guestsLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
            TableView<User> guestsTable = createUserTable("Guests", getGuestsData());

            Label vendorsLabel = new Label("Vendors Attending:");
            vendorsLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
            TableView<User> vendorsTable = createUserTable("Vendors", getVendorsData());

            container.getChildren().addAll(guestsLabel, guestsTable, vendorsLabel, vendorsTable);
        }

        root.setCenter(container);
        return new Scene(root, 800, 600);
    }

    private TableView<User> createUserTable(String type, ObservableList<User> data) {
        TableView<User> table = new TableView<>();

        TableColumn<User, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("user_name"));
        nameColumn.setPrefWidth(200);

        TableColumn<User, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("user_email"));
        emailColumn.setPrefWidth(300);

        table.getColumns().addAll(nameColumn, emailColumn);
        table.setItems(data);
        table.setPrefHeight(200);
        return table;
    }

    private ObservableList<User> getGuestsData() {
        List<User> guests;
        try {
            guests = controller.getGuestFromEvent(event.getEvent_id());
        } catch (SQLException e) {
            e.printStackTrace();
            guests = List.of();
        }
        return FXCollections.observableArrayList(guests);
    }

    private ObservableList<User> getVendorsData() {
        List<User> vendors;
        try {
            vendors = controller.getVendorFromEvent(event.getEvent_id());
        } catch (SQLException e) {
            e.printStackTrace();
            vendors = List.of();
        }
        return FXCollections.observableArrayList(vendors);
    }
}