package com.example.testjavafx;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.sql.*;

import java.io.IOException;

public class DBUtils {

    //a utility class (helper class) in java is a class that contains only static methods
    //it cannot be instantiated
    //it is used to group related methods together


    //stage = the window
    //scene - we add the scene to the stage
    //      - a stage can have multiple scenes


    public static void changeScene(ActionEvent event, String fxmlFile, String title, String username) {
        Parent root = null;
        if (username != null) {
            try {
                FXMLLoader loader = new FXMLLoader(DBUtils.class.getResource(fxmlFile));
                root = loader.load();
                LoggedInController loggedInController = loader.getController();
                loggedInController.setUserInformation(username);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                root = FXMLLoader.load(DBUtils.class.getResource(fxmlFile));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }


    public static void signUpUser(ActionEvent event, String firstName, String lastName, String email, String password, String address, String phoneNumber, String userType, String username) {
        Connection connection = null;
        PreparedStatement psInsert = null;
        PreparedStatement psCheckUserExists = null;
        ResultSet resultSet = null;

        try{
//            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/onlinestore", "root", "root");
            connection = MySQLJDBCUtil.getConnection();
            psCheckUserExists = connection.prepareStatement("SELECT * FROM user WHERE username = ?");
            psCheckUserExists.setString(1, username);
            resultSet = psCheckUserExists.executeQuery();

            if(resultSet.isBeforeFirst()) {
                System.out.println("User already exists!");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("You cannot use this username.");
                alert.show();
            } else {
                psInsert = connection.prepareStatement("INSERT INTO user (firstName, lastName, email, password, address, phoneNumber, userType, username) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
                psInsert.setString(1, firstName);
                psInsert.setString(2, lastName);
                psInsert.setString(3, email);
                psInsert.setString(4, password);
                psInsert.setString(5, address);
                psInsert.setString(6, phoneNumber);
                psInsert.setString(7, userType);
                psInsert.setString(8, username);
                psInsert.executeUpdate();
                changeScene(event, "logged-in.fxml", "Welcome!", username);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        } finally {
            if(resultSet != null) {
                try{
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(psCheckUserExists != null) {
                try{
                    psCheckUserExists.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(psInsert != null) {
                try{
                    psInsert.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if(connection != null) {
                try{
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static void logInUser(ActionEvent event, String username, String password) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
//            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/onlinestore", "root", "root");
            connection = MySQLJDBCUtil.getConnection();
            preparedStatement = connection.prepareStatement("SELECT password FROM user WHERE username = ?");
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();
            if(!resultSet.isBeforeFirst()) {
                System.out.println("User not found in the database!");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Provided credentials are incorrect!");
                alert.show();
            } else {
                while(resultSet.next()) {
                    String retreivedPassword = resultSet.getString("password");
                    if(retreivedPassword.equals(password)) {
                        changeScene(event, "logged-in.fxml", "Welcome!", username);
                    } else {
                        System.out.println("Passwords did not match!");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Provided credentials are incorrect!");
                        alert.show();
                    }
                }
            }
        } catch(SQLException e) {
            e.printStackTrace();
        } finally {
            if(resultSet != null) {
                try{
                    resultSet.close();
                } catch(SQLException e) {
                    e.printStackTrace();
                }
            }
            if(preparedStatement != null) {
                try{
                    preparedStatement.close();
                } catch(SQLException e) {
                    e.printStackTrace();
                }
            }
            if(connection != null) {
                try{
                    connection.close();
                } catch(SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

