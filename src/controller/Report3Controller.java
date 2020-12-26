package controller;

import DAO.ReportsDAO;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Report;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Report3Controller implements Initializable {

    @FXML
    private Button mainMenuButton;

    @FXML
    private TableView<Report> monthlyAppointmentsReport;

    @FXML
    private TableColumn<Report, Long> countColumn;

    @FXML
    private TableColumn<Report, String> usernameColumn;

    @FXML
    private TableColumn<Report, String> dateColumn;

    @FXML
    void onActionGoBackToMainMenu(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
        stage.setScene(new Scene(scene));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<Report> report3 = ReportsDAO.generateReport3();
        countColumn.setCellValueFactory(new PropertyValueFactory<>("count"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        monthlyAppointmentsReport.setItems(report3);
    }
}
