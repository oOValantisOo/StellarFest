package views;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import controllers.EventOrganizerController;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import models.Event;
import models.User;
import utils.PageManager;

public class AddGuest {
    private EventOrganizerController controller;
    private List<User> guests;
    private Event currEvent;

    private HashMap<User, Boolean> selectedMap = new HashMap<User, Boolean>();

    public AddGuest(EventOrganizerController controller, String eventID) throws SQLException {
        super();
        this.controller = controller;

        guests = controller.getUninvitedGuests(eventID);
        currEvent = controller.viewOrganizedEventDetails(eventID);
    }

    @SuppressWarnings("unchecked")
    private TableView<User> createUserTable() {
        TableView<User> tableView = new TableView<>();
        
        TableColumn<User, String> nameColumn = new TableColumn<>("Name");
        TableColumn<User, String> emailColumn = new TableColumn<>("Email");
        TableColumn<User, CheckBox> selectColumn = new TableColumn<>("Select");

        selectColumn.setCellValueFactory(param -> {
            User guest = param.getValue();
            CheckBox checkBox = new CheckBox();
            checkBox.selectedProperty().addListener((obs, wasSelected, isSelected) -> selectedMap.put(guest, isSelected));
            return new SimpleObjectProperty<>(checkBox);
        });

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("user_name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("user_email"));

        selectColumn.setPrefWidth(100);
        nameColumn.setPrefWidth(250);
        emailColumn.setPrefWidth(350);

        tableView.getColumns().addAll(selectColumn, nameColumn, emailColumn);

        tableView.setItems(getUserData());

        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        return tableView;
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
        container.setPadding(new Insets(20));
        container.setAlignment(Pos.CENTER);

        Label pageLbl = new Label("Add Guest");
        pageLbl.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        TableView<User> guestTable = createUserTable();
        
        Label errorLbl = new Label("");
        errorLbl.setStyle("-fx-text-fill: red;");

        Button inviteButton = new Button("Invite Selected Guests");
        inviteButton.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        inviteButton.setOnAction(event -> {
            ObservableList<User> selectedGuests = getSelectedUsers(guestTable);
            
            if (selectedGuests.isEmpty()) {
                showAlert("Error", "Please select at least one guest to invite.");
            } else {
                String response = "";
                for (User u : selectedGuests) {
                    response = controller.sendInvitation(currEvent.getEvent_id(), u.getUser_email());
                }
                
                showAlert("Success", response);
            }
        });

        container.getChildren().addAll(pageLbl, guestTable, errorLbl, inviteButton);
        root.setCenter(container);

        return new Scene(root, 800, 600);
    }

    private ObservableList<User> getUserData() {
        return FXCollections.observableArrayList(guests);
    }

    private ObservableList<User> getSelectedUsers(TableView<User> guestTable) {
        ObservableList<User> selectedUsers = FXCollections.observableArrayList();
        for (User guest : guestTable.getItems()) {
            if (selectedMap.containsKey(guest)) {
                selectedUsers.add(guest);
            }
        }
        return selectedUsers;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}