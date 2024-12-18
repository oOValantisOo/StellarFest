package views;

import java.util.List;

import controllers.InvitationController;
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

public class ViewInvitations {
	private InvitationController controlller;
	List<Invitation> invitations;
	
	public ViewInvitations(InvitationController controlller) {
		this.controlller = controlller;
	}
	
	public Scene getScene() {
		// Mapping
		invitations = controlller.getInvitations("@example.com");
		
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
		Label userIdLabel = new Label(UserSession.getInstance().getCurrUser().getUser_id() + " Pending Invitation's");
		userIdLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
		
		// Button redirect to View
		Button viewAcceptedInvitationsButton = new Button("See accepted invitations");
		viewAcceptedInvitationsButton.setOnAction(action -> {
			PageManager.getInstance().showViewAcceptedEvents(UserSession.getInstance().getCurrUser().getUser_id());
		});
	
		pane.setLeft(userIdLabel);
		pane.setRight(viewAcceptedInvitationsButton);
		
		// TableView
		TableView<Invitation> table = new TableView<>();
		table.setPrefWidth(800);
        table.setEditable(false);
        
        TableColumn<Invitation, String> invitationId = new TableColumn<>("invitation_id");
        TableColumn<Invitation, String> eventId = new TableColumn<>("event_id");
        TableColumn<Invitation, String> userId = new TableColumn<>("user_id");
        TableColumn<Invitation, String> invitationStatus = new TableColumn<>("invitation_status");
        TableColumn<Invitation, String> invitationRole = new TableColumn<>("invitation_role");
        
        // Binding Columns
        invitationId.setCellValueFactory(new PropertyValueFactory<>("invitation_id"));
        eventId.setCellValueFactory(new PropertyValueFactory<>("event_id"));
        userId.setCellValueFactory(new PropertyValueFactory<>("user_id"));
        invitationStatus.setCellValueFactory(new PropertyValueFactory<>("invitation_status"));
        invitationRole.setCellValueFactory(new PropertyValueFactory<>("invitation_role"));
        
        // Adding the columns to TableView
        table.getColumns().addAll(invitationId, eventId, userId, invitationStatus, invitationRole);
        addActionButtons(table);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        ObservableList<Invitation> userObservableList = FXCollections.observableArrayList(invitations);
        table.setItems(userObservableList);
		
		container.getChildren().addAll(pane,table);
		
		BorderPane bp = new BorderPane();
		bp.setCenter(container);
		bp.setTop(back);
		
		return new Scene(bp, 300, 200);
	}
	
	private void addActionButtons(TableView<Invitation> table) {
		 Callback<TableColumn<Invitation, Void>, TableCell<Invitation, Void>> cellFactory = new Callback<TableColumn<Invitation, Void>, TableCell<Invitation, Void>>() {
	        @Override
	        public TableCell<Invitation, Void> call(TableColumn<Invitation, Void> param) {
	            return new TableCell<Invitation, Void>() {
	                private final Button acceptButton = new Button("Accept");

	                {
	                    // Set the action for the delete button
	                	acceptButton.setOnAction((ActionEvent event) -> {
	                        // Get the current row's user object
	                        Invitation ev = getTableRow().getItem();
	                        int index = getIndex();
	                        if (ev != null) {
	                        	String response = controlller.acceptInvitation(ev.getEvent_id());
	                        	
	                        	if (response.equals("Invitation accepted")) {
	                                ev.setInvitation_status("Accepted");
	                                table.refresh();
	                            }
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
	                        hbox.getChildren().addAll(acceptButton);
	                        setGraphic(hbox);
	                    }
	                }
	            };
	        }
	    };

	    TableColumn<Invitation, Void> actionCol = new TableColumn<>("Actions");
	    actionCol.setCellFactory(cellFactory);

	    table.getColumns().add(actionCol);
	}
}
