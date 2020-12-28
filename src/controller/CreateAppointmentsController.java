package controller;

import DAO.AppointmentDAO;
import DAO.CustomerDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import model.Appointment;
import model.Customer;
import utils.SingletonLogin;
import utils.TimeConverter;

import java.io.IOException;
import java.net.URL;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

public class CreateAppointmentsController implements Initializable {
    private Long id;

    @FXML
    private Label createAppointmentLabel;

    @FXML
    private Label customerNameLabel;

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
    private DatePicker dateDatePicker;

    @FXML
    private Label invalidDatePickerLabel;

    @FXML
    private Label dateLabel;

    @FXML
    private Label startTimeLabel;

    @FXML
    private Label endTimeLabel;

    @FXML
    private ComboBox<Customer> customerNameComboBox;

    @FXML
    private Label invalidCustomerNameSelection;

    @FXML
    private TextField createAppointmentTitleTextField;

    @FXML
    private Label invalidTitleLabel;

    @FXML
    private TextArea createAppointmentDescriptionTextArea;

    @FXML
    private Label invalidDescriptionLabel;

    @FXML
    private TextField createAppointmentLocationTextField;

    @FXML
    private Label invalidLocationLabel;

    @FXML
    private TextField createAppointmentContactTextField;

    @FXML
    private Label invalidContactLabel;

    @FXML
    private TextField createAppointmentTypeTextField;

    @FXML
    private Label invalidTypeLabel;

    @FXML
    private TextField createAppointmentUrlTextField;

    @FXML
    private Label invalidURLLabel;

    @FXML
    private ComboBox<String> startTimeComboBox;

    @FXML
    private Label invalidStartTimeSelectionLabel;

    @FXML
    private ComboBox<String> endTimeComboBox;

    @FXML
    private Label invalidEndTimeSelectionLabel;

    @FXML
    private Button createButton;

    @FXML
    private Button cancelButton;

    @FXML
    void onActionCreateAppointment(ActionEvent event) throws Exception {

        if(!isNoErrors(true)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Parameters");
            alert.setContentText("Please make sure all fields containing the * are filled");
            alert.showAndWait();
        } else {
            Long selectedCustomerId = customerNameComboBox.getSelectionModel().getSelectedItem().getId();
//        String selectedCustomerName = customerNameComboBox.getSelectionModel().getSelectedItem().getCustomerName();
            String title = createAppointmentTitleTextField.getText();
            String location = createAppointmentLocationTextField.getText();
            String contact = createAppointmentContactTextField.getText();
            String type = createAppointmentTypeTextField.getText();
            String url = createAppointmentUrlTextField.getText();
            String description = createAppointmentDescriptionTextArea.getText();
            LocalDate date = dateDatePicker.getValue();
            LocalTime startTime = LocalTime.parse(startTimeComboBox.getSelectionModel().getSelectedItem());
            LocalTime endTime = LocalTime.parse(endTimeComboBox.getSelectionModel().getSelectedItem());

            String val = AppointmentDAO.insertAppointment(selectedCustomerId,
                    SingletonLogin.getLoginId(),
                    title,
                    description,
                    location,
                    contact,
                    type,
                    url,
                    TimeConverter.getStartAndEndDate(date, startTime),
                    TimeConverter.getStartAndEndDate(date, endTime),
                    SingletonLogin.getLoginName(),
                    SingletonLogin.getLoginName());

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("ADD CUSTOMER");
            alert.setContentText("Add Customer Message " + val);
            alert.showAndWait();
            onActionViewAppointments(event);
        }
    }

    @FXML
    void onActionSelectCustomer(ActionEvent event) throws IOException {

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
        createAppointmentLocationTextField.setText(SingletonLogin.getLocation());
//        System.out.println("Location " + SingletonLogin.getLocation());

        customerNameComboBox.setItems(customerData);
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

        /**
         * Setting the id when saving.
         */
        customerNameComboBox.valueProperty().addListener((obs, oldValue, newValue) -> {
            if(newValue != null)
                id = newValue.getId();
        });

        /**
         * USED TO ALERT THE USER IF THEY SELECT A SATURDAY OR SUNDAY
         */
        dateTimePicker();


    }

    /**
     * Datepicker for selecting dates might move a portion of this method to a utilities class so both
     * create and update can use a lot of the same features.
     */
    private void dateTimePicker() {
        ObservableList<String> subTimes = FXCollections.observableArrayList();

        dateDatePicker.valueProperty().addListener(e -> {
            LocalDate localDate = dateDatePicker.getValue();
            try {
                AppointmentDAO.getStartAndEndDateTime().forEach(d -> {
                    if(d.contains(localDate.toString())) {
                        AppointmentDAO.getAppointmentTimes().stream().filter(d::contains).forEach(f -> {
                            String startTime = d.substring(11, 19);
                            String endTime = d.substring(32, 40);
                            LocalTime startLocalTime = TimeConverter.getLocalTime2(startTime);
                            LocalTime endLocalTime = TimeConverter.getLocalTime2(endTime);

                            if(!startLocalTime.plusMinutes(15).equals(endLocalTime)) {
                                while (!startLocalTime.plusMinutes(15).equals(endLocalTime)) {
                                    subTimes.add(startLocalTime.toString()+":00");
                                    startLocalTime = startLocalTime.plusMinutes(15);
                                }
                                subTimes.add(startLocalTime.toString()+":00");
//                                subTimes.add(endLocalTime.toString()+":00");
                            } else {
                                subTimes.add(startLocalTime.toString()+":00");
                            }
                        });
                    }
                });
//                subTimes.forEach(j -> System.out.println("time " + j));
                generateTimes(subTimes);
            } catch (Exception exception) {
                exception.printStackTrace();
            }

            if(localDate.getDayOfWeek().toString().equalsIgnoreCase("saturday") || localDate.getDayOfWeek().toString().equalsIgnoreCase("sunday")) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Not Available");
                alert.setContentText("Sorry you cant schedule appointments on  " + localDate.getDayOfWeek().toString());
                alert.showAndWait();
                dateDatePicker.setValue(null);
            }
        });
    }

    /**
     * Get available times slots for appointments.
     * @param scheduledTimes
     */
    public void generateTimes(ObservableList<String> scheduledTimes) {
        ObservableList<String> times = AppointmentDAO.getAppointmentTimes();
        ObservableList<String> subTimes = FXCollections.observableArrayList(times.subList(32, 69));
//        ObservableList<String> subEndTimes = FXCollections.observableArrayList();

        subTimes.removeAll(scheduledTimes);


        /**
         * Onload create appointment set start and end times
         * Also increase the endTimeComboBox when the startTimeComboBox changes
         * time.
         */
//        createAppointmentLocationTextField.setText(SingletonLogin.getLocation());
        System.out.println("Location " + SingletonLogin.getLocation());

        startTimeComboBox.setItems(subTimes);
        startTimeComboBox.getSelectionModel().select(0);
        selectTimes(subTimes);
//        endTimeComboBox.setItems(subTimes);
        endTimeComboBox.getSelectionModel().select(1);
        startTimeComboBox.setOnAction((e) -> {
            selectTimes(subTimes);
        });
    }

    public void selectTimes(ObservableList<String> subTimes){
        int index = startTimeComboBox.getSelectionModel().getSelectedIndex() + 1;

        ObservableList<String> endTime = FXCollections.observableArrayList(subTimes.subList(index, subTimes.size() -1));
        final int[] index1 = {-1};
        boolean status = endTime.stream().anyMatch(be -> {
            endTime.indexOf(be);
            int i = endTime.indexOf(be);
            if(endTime.indexOf(be) < endTime.size() -1) {
                LocalTime l = LocalTime.parse(endTime.get(i));
                LocalTime ll = LocalTime.parse(endTime.get(i + 1));
                if (!l.plusMinutes(15).equals(ll)) {
                    index1[0] = i+1;
                    return true;

                }
            }
            return false;
        });
        System.out.println("index[0] " + index1[0]);
        ObservableList<String> endOfTimes;
        if(status)
            endOfTimes = FXCollections.observableArrayList(endTime.subList(1, index1[0]));
        else
            endOfTimes = endTime;

        endTimeComboBox.setItems(endOfTimes);
        endTimeComboBox.getSelectionModel().select(0);
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
            invalidCustomerNameSelection.setTextFill(Color.web("#ff0000"));
            invalidCustomerNameSelection.setText("*");
            noErrors = false;
        }
        if(createAppointmentTitleTextField.getText().isEmpty()) {
            invalidTitleLabel.setTextFill(Color.web("#ff0000"));
            invalidTitleLabel.setText("*");
            noErrors = false;
        }
        if(createAppointmentLocationTextField.getText().isEmpty()) {
            invalidLocationLabel.setTextFill(Color.web("#ff0000"));
            invalidLocationLabel.setText("*");
            noErrors = false;
        }
        if(createAppointmentContactTextField.getText().isEmpty()) {
            invalidContactLabel.setTextFill(Color.web("#ff0000"));
            invalidContactLabel.setText("*");
            noErrors = false;
        }

        if(createAppointmentTypeTextField.getText().isEmpty()) {
            invalidTypeLabel.setTextFill(Color.web("#ff0000"));
            invalidTypeLabel.setText("*");
            noErrors = false;
        }

        if(createAppointmentUrlTextField.getText().isEmpty()) {
            invalidURLLabel.setTextFill(Color.web("#ff0000"));
            invalidURLLabel.setText("*");
            noErrors = false;
        }

        if(createAppointmentDescriptionTextArea.getText().isEmpty()) {
            invalidDescriptionLabel.setTextFill(Color.web("#ff0000"));
            invalidDescriptionLabel.setText("*");
            noErrors = false;
        }

        if(dateDatePicker.getValue() == null) {
            invalidDatePickerLabel.setTextFill(Color.web("#ff0000"));
            invalidDatePickerLabel.setText("*");
            noErrors = false;
        }

        if(startTimeComboBox.getSelectionModel().isEmpty()) {
            invalidStartTimeSelectionLabel.setTextFill(Color.web("#ff0000"));
            invalidStartTimeSelectionLabel.setText("*");
            noErrors = false;
        }

        if(endTimeComboBox.getSelectionModel().isEmpty()) {
            invalidEndTimeSelectionLabel.setTextFill(Color.web("#ff0000"));
            invalidEndTimeSelectionLabel.setText("*");
            noErrors = false;
        }
        return noErrors;
    }

}
