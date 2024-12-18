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

public class AddVendor {
    private EventOrganizerController controller;
    private List<User> vendors;
    private Event currEvent;

    private HashMap<User, Boolean> selectedMap = new HashMap<>();

    public AddVendor(EventOrganizerController controller, String eventID) throws SQLException {
        super();
        this.controller = controller;

        vendors = controller.getUninvitedVendor(eventID);
        currEvent = controller.viewOrganizedEventDetails(eventID);
    }

    @SuppressWarnings("unchecked")
    private TableView<User> createVendorTable() {
        TableView<User> tableView = new TableView<>();

        TableColumn<User, String> nameColumn = new TableColumn<>("Name");
        TableColumn<User, String> emailColumn = new TableColumn<>("Email");
        TableColumn<User, CheckBox> selectColumn = new TableColumn<>("Select");

        selectColumn.setCellValueFactory(param -> {
            User vendor = param.getValue();
            CheckBox checkBox = new CheckBox();
            checkBox.selectedProperty().addListener((obs, wasSelected, isSelected) -> selectedMap.put(vendor, isSelected));
            return new SimpleObjectProperty<>(checkBox);
        });

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("user_name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("user_email"));

        selectColumn.setPrefWidth(100);
        nameColumn.setPrefWidth(250);
        emailColumn.setPrefWidth(350);

        tableView.getColumns().addAll(selectColumn, nameColumn, emailColumn);

        tableView.setItems(getVendorData());

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

        Label pageLbl = new Label("Add Vendor");
        pageLbl.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        TableView<User> vendorTable = createVendorTable();

        Label errorLbl = new Label("");
        errorLbl.setStyle("-fx-text-fill: red;");

        Button inviteButton = new Button("Invite Selected Vendors");
        inviteButton.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        inviteButton.setOnAction(event -> {
            ObservableList<User> selectedVendors = getSelectedVendors(vendorTable);

            if (selectedVendors.isEmpty()) {
                showAlert("Error", "Please select at least one vendor to invite.");
            } else {
                String response = "";
                for (User u : selectedVendors) {
                    System.out.println(u.getUser_email());
                    response = controller.sendInvitation(currEvent.getEvent_id(), u.getUser_email());
                }

                showAlert("Success", response);
            }
        });

        container.getChildren().addAll(pageLbl, vendorTable, errorLbl, inviteButton);
        root.setCenter(container);

        return new Scene(root, 800, 600);
    }

    private ObservableList<User> getVendorData() {
        return FXCollections.observableArrayList(vendors);
    }

    private ObservableList<User> getSelectedVendors(TableView<User> vendorTable) {
        ObservableList<User> selectedVendors = FXCollections.observableArrayList();
        for (User vendor : vendorTable.getItems()) {
            if (selectedMap.containsKey(vendor)) {
                selectedVendors.add(vendor);
            }
        }
        return selectedVendors;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}