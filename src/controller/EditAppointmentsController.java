package controller;

import DAO.AppointmentDAO;
import DAO.CustomerDAO;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Appointment;
import model.Customer;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EditAppointmentsController implements Initializable {

    @FXML
    private Label createAppointmentLabel;

    @FXML
    private Label customerNameLabel;

    @FXML
    private TextField editAppointmentTitleTextField;

    @FXML
    private TextField editAppointmentLocationTextField;

    @FXML
    private TextField editAppointmentContactTextField;

    @FXML
    private Label titleLabel;

    @FXML
    private Label descriptionLabel;

    @FXML
    private Label locationLabel;

    @FXML
    private Label contactLabel;

    @FXML
    private Label typeLabel;

    @FXML
    private Label urlLabel;

    @FXML
    private TextField editAppointmentTypeTextField;

    @FXML
    private TextField editAppointmentUrlTextField;

    @FXML
    private TextArea editAppointmentDescriptionTextArea;

    @FXML
    private ComboBox<Customer> customerNameComboBox;

    @FXML
    private DatePicker dateDatePicker;

    @FXML
    private Label dateLabel;

    @FXML
    private Label startTimeLabel;

    @FXML
    private Label endTimeLabel;

    @FXML
    private ComboBox<String> startTimeComboBox;

    @FXML
    private ComboBox<String> endTimeComboBox;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    @FXML
    void onActionSaveAppointment(ActionEvent event) {

    }

    @FXML
    void onActionSelectCustomer(ActionEvent event) {

    }

    @FXML
    void onActionViewAppointments(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent scene = FXMLLoader.load(getClass().getResource("/view/ViewAppointmentsMenu.fxml"));
        stage.setScene(new Scene(scene));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<Customer> customerData = CustomerDAO.findAllCustomers();
        ObservableList<String> times = AppointmentDAO.getAppointmentTimes();

        customerNameComboBox.setItems(customerData);
        startTimeComboBox.setItems(times);
        endTimeComboBox.setItems(times);
    }

    public void updateAppointmentTextFields(Appointment appointment) {

    }
}
