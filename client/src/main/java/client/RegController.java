package client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegController {
    @FXML
    public TextField loginField;
    @FXML
    public PasswordField passwordField;
    @FXML
    public TextField nicknameField;
    @FXML
    Controller controller;

    public void clickRegistration(ActionEvent actionEvent) {
        controller.tryRegistration(loginField.getText(), passwordField.getText(), nicknameField.getText());
        controller.regStage.close();
    }
}
