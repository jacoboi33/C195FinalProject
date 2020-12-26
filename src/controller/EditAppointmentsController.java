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
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import model.Appointment;
import model.Customer;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class EditAppointmentsController implements Initializable {
    private Long id;

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
    private Label errorLabelDate;

    @FXML
    private Label errorLabelTitle;

    @FXML
    private Label errorLabelDescription;

    @FXML
    private Label errorLabelLocation;

    @FXML
    private Label errorLabelContact;

    @FXML
    private Label errorLabelType;

    @FXML
    private Label errorLabelUrl;

    @FXML
    private Label errorLabelStartTime;

    @FXML
    private Label errorLabelEndTime;

    @FXML
    private Label errorLabelCustomerName;

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
    private Label labelId;

    @FXML
    private TextField idTextField;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    @FXML
    void onActionIdTextField(ActionEvent event) {

    }

    @FXML
    void onActionLabelId(MouseEvent event) {

    }

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

        /**
         * Setting the id when saving.
         */
        customerNameComboBox.valueProperty().addListener((obs, oldValue, newValue) -> {
            if(newValue != null)
                id = newValue.getId();
//
            System.out.println("id " + id);
        });
    }

    public void updateAppointmentTextFields(Appointment appointment) {
        ObservableList<Customer> customerData = CustomerDAO.findAllCustomers();

        idTextField.setText(Long.toString(appointment.getId()));
//        customerNameComboBox.getSelectionModel().select(appointment.getUserId().intValue());
        customerNameComboBox.getItems().stream()
                .filter(customer -> appointment.getCustomerName().equals(customer.getCustomerName()))
                .findAny()
                .ifPresent(customerNameComboBox.getSelectionModel()::select);
//        System.out.println("select customer " + appointment.getCustomerName());
//        customerNameComboBox.setValue(new StringConverter<Customer>() {
//
//        });
//customerData.getClass().getId();

//        customerNameComboBox.setItems(customerData.stream().filter(customerData.getId()));
        customerNameComboBox.setConverter(new StringConverter<Customer>() {

            /**
             * For adding customer name to the combo box when selected and then using the id to save the data
             * when clicking save.
             * @param customer
             * @return
             */
            @Override
            public String toString(Customer customer) {
                return customer.getCustomerName();
            }

            @Override
            public Customer fromString(String string) {
                return customerNameComboBox.getItems().stream().filter(e ->
                        e.getCustomerName().equals(string)).findFirst().orElse(null);
            }

        });

        editAppointmentTitleTextField.setText(appointment.getTitle());
        editAppointmentDescriptionTextArea.setText(appointment.getDescription());
        editAppointmentLocationTextField.setText(appointment.getLocation());
        editAppointmentContactTextField.setText(appointment.getContact());
        editAppointmentTypeTextField.setText(appointment.getType());
        editAppointmentUrlTextField.setText(appointment.getUrl());
        dateDatePicker.setValue(localDate(appointment.getStart()));
        startTimeComboBox.setValue(parseTime(appointment.getStart()));
        endTimeComboBox.setValue(parseTime(appointment.getEnd()));
    }

    public Customer getCustomerName(long id) {
        customerNameComboBox.setConverter(new StringConverter<Customer>() {

            /**
             * For adding customer name to the combo box when selected and then using the id to save the data
             * when clicking save.
             * @param customer
             * @return
             */
            @Override
            public String toString(Customer customer) {
                return customer.getCustomerName();
            }

            @Override
            public Customer fromString(String string) {
                return customerNameComboBox.getItems().stream().filter(e ->
                        e.getCustomerName().equals(string)).findFirst().orElse(null);
            }

        });

        return null;
    }

    private LocalDate localDate(String date) {
        return LocalDate.parse(date.substring(0, 10));
    }

    private String parseTime(String time) {
        return time.substring(11, 19);
    }

    /**
     * Validates the form fields to make sure they are filled in
     * if not there will be a red * where there needs to be text.
     * @param noErrors
     * @return
     */
    private boolean isNoErrors(boolean noErrors) {
//        noErrors = true;
        if(customerNameComboBox.getSelectionModel().isEmpty()) {
            errorLabelCustomerName.setTextFill(Color.web("#ff0000"));
            errorLabelCustomerName.setText("*");
            noErrors = false;
        }
        if(editAppointmentTitleTextField.getText().isEmpty()) {
            errorLabelTitle.setTextFill(Color.web("#ff0000"));
            errorLabelTitle.setText("*");
            noErrors = false;
        }
        if(editAppointmentLocationTextField.getText().isEmpty()) {
            errorLabelLocation.setTextFill(Color.web("#ff0000"));
            errorLabelLocation.setText("*");
            noErrors = false;
        }
        if(editAppointmentContactTextField.getText().isEmpty()) {
            errorLabelContact.setTextFill(Color.web("#ff0000"));
            errorLabelContact.setText("*");
            noErrors = false;
        }

        if(editAppointmentTypeTextField.getText().isEmpty()) {
            errorLabelType.setTextFill(Color.web("#ff0000"));
            errorLabelType.setText("*");
            noErrors = false;
        }

        if(editAppointmentUrlTextField.getText().isEmpty()) {
            errorLabelUrl.setTextFill(Color.web("#ff0000"));
            errorLabelUrl.setText("*");
            noErrors = false;
        }

        if(editAppointmentDescriptionTextArea.getText().isEmpty()) {
            errorLabelDescription.setTextFill(Color.web("#ff0000"));
            errorLabelDescription.setText("*");
            noErrors = false;
        }

        if(dateDatePicker.getValue() == null) {
            errorLabelDate.setTextFill(Color.web("#ff0000"));
            errorLabelDate.setText("*");
            noErrors = false;
        }

        if(startTimeComboBox.getSelectionModel().isEmpty()) {
            errorLabelStartTime.setTextFill(Color.web("#ff0000"));
            errorLabelStartTime.setText("*");
            noErrors = false;
        }

        if(endTimeComboBox.getSelectionModel().isEmpty()) {
            errorLabelEndTime.setTextFill(Color.web("#ff0000"));
            errorLabelEndTime.setText("*");
            noErrors = false;
        }
        return noErrors;
    }
}
