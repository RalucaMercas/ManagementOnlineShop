package com.example.testjavafx;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class SignUpController implements Initializable {
    @FXML
    private TextField tf_firstName;
    @FXML
    private TextField tf_lastName;
    @FXML
    private TextField tf_username;
    @FXML
    private TextField tf_email;
    @FXML
    private PasswordField pf_password;
    @FXML
    private TextField tf_address;
    @FXML
    private TextField tf_phoneNumber;
    @FXML
    private TextField tf_userType;
    @FXML
    private Button button_sign_up;
    @FXML
    private Button button_login;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        button_sign_up.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(!tf_firstName.getText().trim().isEmpty() && !tf_lastName.getText().trim().isEmpty()
                    && !tf_username.getText().trim().isEmpty() && !tf_email.getText().trim().isEmpty()
                    && !pf_password.getText().trim().isEmpty() && !tf_address.getText().trim().isEmpty()
                    && !tf_phoneNumber.getText().trim().isEmpty() && !tf_userType.getText().trim().isEmpty() ) {
                        DBUtils.signUpUser(event, tf_firstName.getText(), tf_lastName.getText(),
                                tf_email.getText(), pf_password.getText(), tf_address.getText(),
                                tf_phoneNumber.getText(), tf_userType.getText(), tf_username.getText());
                } else {
                    System.out.println("Please fill in all information!");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Please fill in all information to sign up!");
                    alert.show();
                }
            }
        });

        button_login.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "log-in.fxml", "Log in!", null);
            }
        });
    }
}
