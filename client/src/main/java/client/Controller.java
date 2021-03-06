package client;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    public TextArea textArea;
    @FXML
    public TextField textField;
    @FXML
    public HBox authPanel;
    @FXML
    public HBox msgPanel;
    @FXML
    public TextField loginField;
    @FXML
    public PasswordField passwordField;
    @FXML
    public ListView<String> clientList;

    Socket socket;
    DataInputStream in;
    DataOutputStream out;

    private final String IP_ADDRESS = "localhost";
    private final int PORT = 5050;

    private boolean authenticated;
    private String nickname;

    Stage regStage;

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
        authPanel.setVisible(!authenticated);
        authPanel.setManaged(!authenticated);
        msgPanel.setVisible(authenticated);
        msgPanel.setManaged(authenticated);
        clientList.setVisible(authenticated);
        clientList.setManaged(authenticated);
        if (!authenticated) {
            nickname = "";
        }
        textArea.clear();
        setTitle("Messenger");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        regStage = createRegWindow();
        authenticated = false;
        Platform.runLater(() -> {
            Stage stage = (Stage) textField.getScene().getWindow();
            stage.setOnCloseRequest(windowEvent -> {
                if (socket != null && !socket.isClosed()) {
                    try {
                        out.writeUTF("/end");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        });
    }

    public void connect() {
        try {
            socket = new Socket(IP_ADDRESS, PORT);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            new Thread(() -> {
                try {
                    //цикл аутентификации
                    while (true) {
                        String str = in.readUTF();
                        if (str.startsWith("/authok ")) {
                            setAuthenticated(true);
                            nickname = str.split(" ")[1];
                            MessageHistory.fileConnect(nickname);
                            MessageHistory.loadMessageHistory(this);
                            break;
                        }
                        textArea.appendText(str + "\n");
                    }
                    setTitle("Messenger : " + nickname);

                    //цикл работы
                    while (true) {
                        String str = in.readUTF();
                        if (str.startsWith("/")) {
                            if (str.equals("/end")) {
                                setAuthenticated(false);
                                break;
                            }
                            if (str.startsWith("/cnick ")) {
                                String[] token = str.split(" ");
                                setTitle("Messenger : " + token[1]);
                            }
                            if (str.startsWith("/clientlist ")) {
                                String[] token = str.split(" ");
                                Platform.runLater(() -> {
                                    clientList.getItems().clear();
                                    for (int i = 1; i < token.length; i++) {
                                        clientList.getItems().add(token[i]);
                                    }
                                });
                            }
                        } else {
                            textArea.appendText(str + "\n");
                            MessageHistory.addMessageToFile(str);
                        }
                    }
                } catch (SocketException e) {
                    System.out.println("Сервер отключился");
                    setAuthenticated(false);
                } catch (IOException e) {
                    e.getStackTrace();
                } finally {
                    try {
                        MessageHistory.fileDisconnect();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage() {
        if (textField.getText().trim().length() > 0) {
            try {
                out.writeUTF(textField.getText());
            } catch (IOException e) {
                e.printStackTrace();
            }

            textField.clear();
            textField.requestFocus();
        }

    }

    public void pressEnter(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER)
            sendMessage();
    }

    public void tryToAuth(ActionEvent actionEvent) {
        if (socket == null || socket.isClosed()) {
            connect();
        }

        try {
            out.writeUTF("/auth " + loginField.getText() + " " + passwordField.getText());
            passwordField.clear();

        } catch (NullPointerException e) {
            System.out.println("Сервер не доступен");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void setTitle(String title) {
        Platform.runLater(() -> ((Stage) textField.getScene().getWindow()).setTitle(title));
    }

    public void clickClientList(MouseEvent mouseEvent) {
        String receiver = clientList.getSelectionModel().getSelectedItem();
        textField.setText("/w " + receiver + " ");
    }

    public void registration(ActionEvent actionEvent) {
        regStage.show();
    }

    private Stage createRegWindow() {
        Stage stage = null;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/reg.fxml"));
            Parent root = fxmlLoader.load();
            stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            RegController regController = fxmlLoader.getController();
            regController.controller = this;
            stage.setTitle("Registration");
            stage.setScene(new Scene(root, 300, 200));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stage;
    }

    public void tryRegistration(String login, String password, String nickname) {
        if (login.equals("") || password.equals("") || nickname.equals("")) {
            textArea.setText("Форма регистрации не заполнена");
            return;
        }
        String msg = String.format("/reg %s %s %s", login, password, nickname);

        if (socket == null || socket.isClosed()) {
            connect();
        }
        try {
            out.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
