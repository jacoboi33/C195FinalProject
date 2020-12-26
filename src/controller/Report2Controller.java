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

public class Report2Controller implements Initializable {

    @FXML
    private Button mainMenuButton;

    @FXML
    private TableView<Report> scheduleReportTableView;

    @FXML
    private TableColumn<Report, String> userNameColumn;

    @FXML
    private TableColumn<Report, String>  titleColumn;

    @FXML
    private TableColumn<Report, String>  descriptionColumn;

    @FXML
    private TableColumn<Report, String>  startTimeColumn;

    @FXML
    private TableColumn<Report, String>  endTimeColumn;

    @FXML
    void onActionGoBackToMainMenu(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
        stage.setScene(new Scene(scene));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<Report> report2 = ReportsDAO.generateReport2();
        userNameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        startTimeColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        endTimeColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        scheduleReportTableView.setItems(report2);
    }
}
