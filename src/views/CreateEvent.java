package views;

import controllers.EventOrganizerController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import utils.UserSession;

public class CreateEvent {
    private EventOrganizerController controller;

    public CreateEvent(EventOrganizerController controller) {
        super();
        this.controller = controller;
    }

    public Scene getScene() {
        VBox container = new VBox(15);
        container.setAlignment(Pos.CENTER_LEFT);
        container.setPadding(new Insets(20));

        Label formTitle = new Label("Create Event");
        formTitle.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        TextField eventNameField = new TextField();
        eventNameField.setPromptText("Enter event name");
        eventNameField.setPrefWidth(400);

        DatePicker eventDatePicker = new DatePicker();

        TextField eventLocationField = new TextField();
        eventLocationField.setPromptText("Enter event location");
        eventLocationField.setPrefWidth(400);

        TextArea eventDescriptionArea = new TextArea();
        eventDescriptionArea.setPromptText("Enter event description");
        eventDescriptionArea.setPrefWidth(400);
        eventDescriptionArea.setPrefHeight(100);

        Label feedbackLabel = new Label("");
        feedbackLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));

        Button submitButton = new Button("Submit");
        submitButton.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        submitButton.setOnAction(e -> {
            String eventName = eventNameField.getText().trim();
            String eventDate = (eventDatePicker.getValue() != null) ? eventDatePicker.getValue().toString() : "";
            String eventLocation = eventLocationField.getText().trim();
            String eventDescription = eventDescriptionArea.getText().trim();

            if (eventName.isEmpty() || eventDate.isEmpty() || eventLocation.isEmpty() || eventDescription.isEmpty()) {
                feedbackLabel.setText("All fields must be filled.");
                feedbackLabel.setStyle("-fx-text-fill: red;");
                return;
            }

            String response = controller.createEvent(eventName, eventDate, eventLocation, eventDescription, UserSession.getInstance().getCurrUser().getUser_id());
            if (response.equals("Event created successfully!")) {
                feedbackLabel.setText(response);
                feedbackLabel.setStyle("-fx-text-fill: green;");
                eventNameField.clear();
                eventDatePicker.setValue(null);
                eventLocationField.clear();
                eventDescriptionArea.clear();
            } else {
                feedbackLabel.setText(response);
                feedbackLabel.setStyle("-fx-text-fill: red;");
            }
        });

        container.getChildren().addAll(
            formTitle,
            new Label("Event Name:"), eventNameField,
            new Label("Event Date:"), eventDatePicker,
            new Label("Event Location:"), eventLocationField,
            new Label("Event Description:"), eventDescriptionArea,
            submitButton,
            feedbackLabel
        );

        return new Scene(container, 800, 600);
    }
}