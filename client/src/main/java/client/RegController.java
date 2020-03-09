package client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class RegController {
    @FXML
    public TextField loginField;
    @FXML
    public PasswordField passwordField;
    @FXML
    public TextField nicknameField;
    @FXML
    Controller controller;

    public void clickRegistration() {
        controller.tryRegistration(loginField.getText(), passwordField.getText(), nicknameField.getText());
        controller.regStage.close();
    }

    public void pressEnter(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            clickRegistration();
        }
    }
}
