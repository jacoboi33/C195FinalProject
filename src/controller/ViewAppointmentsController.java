package controller;

import DAO.AppointmentDAO;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointment;
import model.Customer;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Optional;
import java.util.ResourceBundle;

public class ViewAppointmentsController implements Initializable {

    @FXML
    private Label viewAppointmentsLabel;

    @FXML
    private RadioButton weekRadioButton;

    @FXML
    private RadioButton monthRadioButton;

    @FXML
    private RadioButton allRadioButton;

    @FXML
    private ToggleGroup weeklyMonthlyAllToggleGroup;

    @FXML
    private TableView<Appointment> appointmentsTable;

    @FXML
    private TableColumn<Appointment, Long> idColumn;

    @FXML
    private TableColumn<Customer, String> customerNameColumn;

    @FXML
    private TableColumn<Appointment, String> titleColumn;

    @FXML
    private TableColumn<Appointment, String> descriptionColumn;

    @FXML
    private TableColumn<Appointment, String> locationColumn;

    @FXML
    private TableColumn<Appointment, String> typeColumn;

    @FXML
    private TableColumn<Appointment, Calendar> startTimeColumn;

    @FXML
    private TableColumn<Appointment, Calendar> endTimeColumn;

    @FXML
    private Button mainMenuButton;

    @FXML
    private Button createButton;

    @FXML
    private Button editButton;

    @FXML
    private Button deleteButton;

    @FXML
    void onActionMonthSelected(ActionEvent event) {
        setAppointmentView(AppointmentDAO.findAllAppointmentsByWeek("month"));
    }

    @FXML
    void onActionWeekSelected(ActionEvent event) {
        setAppointmentView(AppointmentDAO.findAllAppointmentsByWeek("week"));
    }

    @FXML
    void onActionAllSelected(ActionEvent event) {
        setAppointmentView(AppointmentDAO.findAllAppointments());
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        allRadioButton.setSelected(true);
        setAppointmentView(AppointmentDAO.findAllAppointments());
    }

    @FXML
    void onActionCreateAppointment(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent scene = FXMLLoader.load(getClass().getResource("/view/CreateAppointmentsMenu.fxml"));
        stage.setScene(new Scene(scene));
    }

    @FXML
    void onActionDeleteAppointment(ActionEvent event) throws Exception {
//        deleteButton.setOnAction(e -> {
//            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//            alert.setTitle("Delete Appointment");
//            alert.setHeaderText("Confirm Delete Appointment");
//            alert.setContentText("Are you sure you want to delete this appointment ? ");
//            Optional<ButtonType> result = alert.showAndWait();
//            if (result.get() == ButtonType.OK) {
//                try {
//                    AppointmentDAO.deleteAppointment(appointmentsTable.getSelectionModel().getSelectedItem().getId());
//                    setAppointmentView(AppointmentDAO.findAllAppointments());
////                    alert.close();
//                } catch (Exception ioException) {
//                    ioException.printStackTrace();
//                }
//            } else {
//                alert.close();
//            }
//
//        });
        Long selectedAppointmentId;
        if(appointmentsTable.getSelectionModel().isEmpty())
            return;
        else {
            selectedAppointmentId = appointmentsTable.getSelectionModel().getSelectedItem().getId();
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Appointment");
        alert.setContentText("Are you sure you want to delete this appointment? ");

        Optional<ButtonType> alertType = alert.showAndWait();
        if(alertType.get().equals(ButtonType.OK)) {
            AppointmentDAO.deleteAppointment(selectedAppointmentId);
            setAppointmentView(AppointmentDAO.findAllAppointments());
        }
        else
            alert.close();
    }

    @FXML
    void onActionEditAppointment(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent scene = FXMLLoader.load(getClass().getResource("/view/EditAppointmentsMenu.fxml"));
        stage.setScene(new Scene(scene));
    }

    @FXML
    void onActionMainMenu(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
        stage.setScene(new Scene(scene));

    }

    void setAppointmentView(ObservableList<Appointment> appointments) {
//        appointments.forEach(entry -> System.out.println(entry.getCustomerName()));

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        startTimeColumn.setCellValueFactory(new PropertyValueFactory<>("start"));
        endTimeColumn.setCellValueFactory(new PropertyValueFactory<>("end"));

        appointmentsTable.setItems(appointments);


    }
}
