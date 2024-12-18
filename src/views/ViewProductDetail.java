package views;

import controllers.ProductController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import models.Product;
import utils.PageManager;
import utils.UserSession;

public class ViewProductDetail {

    private ProductController controller;
    private Product product;
    private String user_id;

    public ViewProductDetail(ProductController controller, Product product) {
        this.controller = controller;
        this.product = product;
        this.user_id = UserSession.getInstance().getCurrUser().getUser_id();
    }

    public Scene getScene() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        // Back Button
        Button back = new Button(" < ");
        back.setPadding(new Insets(10));
        back.setOnAction(action -> PageManager.getInstance().goBack());

        VBox backButtonContainer = new VBox();
        backButtonContainer.setPadding(new Insets(10));
        backButtonContainer.setAlignment(Pos.TOP_LEFT);
        backButtonContainer.getChildren().add(back);
        root.setLeft(backButtonContainer);

        VBox container = new VBox(10);
        container.setAlignment(Pos.CENTER);

        Label nameLbl = new Label(product.getProduct_id());
        nameLbl.setFont(Font.font("Arial", FontWeight.BOLD, 32));

        GridPane formContainer = new GridPane();
        formContainer.setHgap(10);
        formContainer.setVgap(10);

        Label nameLabel = new Label("Name ");
        TextField nameInputField = new TextField(product.getProduct_name());
        nameInputField.setPrefWidth(300);
        nameInputField.setMaxWidth(Double.MAX_VALUE);

        Label descriptionLabel = new Label("Description ");
        TextField descriptionInputLabel = new TextField(product.getProduct_description());
        descriptionInputLabel.setPrefWidth(300);
        descriptionInputLabel.setMaxWidth(Double.MAX_VALUE);
        descriptionInputLabel.setPrefHeight(100);
        descriptionInputLabel.setMaxHeight(Double.MAX_VALUE);

        Button saveBtn = new Button("Save Changes");
        saveBtn.setOnAction(action -> {
            String result = controller.updateVendor(nameInputField.getText(), descriptionInputLabel.getText(), product.getProduct_id(), user_id);

            if (result.equals("Product successfully updated")) {
                PageManager.getInstance().showViewManageProduct(user_id);
            }
        });

        formContainer.add(nameLabel, 0, 0);
        formContainer.add(nameInputField, 1, 0);
        formContainer.add(descriptionLabel, 0, 1);
        formContainer.add(descriptionInputLabel, 1, 1);
        formContainer.add(saveBtn, 1, 2);

        formContainer.setAlignment(Pos.CENTER);

        container.getChildren().addAll(nameLbl, formContainer);

        root.setCenter(container);

        return new Scene(root, 400, 300);
    }
}