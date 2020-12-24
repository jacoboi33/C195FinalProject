package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class MainMenuController {
    @FXML
    private Label labelLoginTitle;

    @FXML
    private Button appointmentsButton;

    @FXML
    private Button customerButton;

    @FXML
    private Button viewReports;

    @FXML
    private Button exitButton;

    @FXML
    void onActionViewCustomer(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent scene = FXMLLoader.load(getClass().getResource("/view/ViewCustomerMenu.fxml"));
        stage.setScene(new Scene(scene));

    }

    @FXML
    void onActionExit(ActionEvent event) {
        if (event.getSource() == exitButton)
            System.exit(0);
    }

    @FXML
    void onActionViewAppointments(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent scene = FXMLLoader.load(getClass().getResource("/view/ViewAppointmentsMenu.fxml"));
        stage.setScene(new Scene(scene));
    }

    @FXML
    void onActionViewReports(ActionEvent event) throws IOException {

    }
}
