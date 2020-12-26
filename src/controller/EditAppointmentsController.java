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
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import model.Appointment;
import model.Customer;
import utils.SingletonLogin;
import utils.TimeConverter;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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
    void onActionSaveAppointment(ActionEvent event) throws IOException {
        if(!isNoErrors(true)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Parameters");
            alert.setContentText("Please make sure all fields containing the * are filled");
            alert.showAndWait();
        } else {
            Long appointmentId = Long.valueOf(idTextField.getText());
            Long selectedCustomerId = customerNameComboBox.getSelectionModel().getSelectedItem().getId();
            String title = editAppointmentLocationTextField.getText();
            String location = editAppointmentLocationTextField.getText();
            String contact = editAppointmentContactTextField.getText();
            String type = editAppointmentTypeTextField.getText();
            String url = editAppointmentUrlTextField.getText();
            String description = editAppointmentDescriptionTextArea.getText();
            LocalDate date = dateDatePicker.getValue();
            LocalTime startTime = LocalTime.parse(startTimeComboBox.getSelectionModel().getSelectedItem());
            LocalTime endTime = LocalTime.parse(endTimeComboBox.getSelectionModel().getSelectedItem());

            String val = AppointmentDAO.updateAppointment(selectedCustomerId,
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
                    appointmentId);

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("EDIT CUSTOMER");
            alert.setContentText("Edit Customer Message " + val);
            alert.showAndWait();
            onActionViewAppointments(event);
        }

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

        dateTimePicker();
    }

    public void updateAppointmentTextFields(Appointment appointment) {
        idTextField.setText(Long.toString(appointment.getId()));

        /**
         * For getting the correct customer value in the combobox
         * onloading the update appointments
         */
        customerNameComboBox.getItems().stream()
                .filter(customer -> appointment.getCustomerName().equals(customer.getCustomerName()))
                .findAny()
                .ifPresent(customerNameComboBox.getSelectionModel()::select);

        customerNameComboBox.setConverter(new StringConverter<Customer>() {
            /**
             * For adding customer name to the combo box from customer
             * and converting customer to a string
             * overriding the toString method
             *
             * @param customer
             * @return String
             */
            @Override
            public String toString(Customer customer) {
                return customer.getCustomerName();
            }

            /**
             * Converting String to a customer
             * overriding the fromString method
             * @param string
             * @return Customer
             */
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

    private LocalDate localDate(String date) {
        return LocalDate.parse(date.substring(0, 10));
    }

    private String parseTime(String time) {
        return time.substring(11, 19);
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
                            System.out.println("f " + f);
                            System.out.println("d " + d);
                            String startTime = d.substring(11, 19);
                            String endTime = d.substring(32, 40);
                            System.out.println("Start Time " + startTime + " end time " + endTime);

                            LocalTime startLocalTime = TimeConverter.getLocalTime2(startTime);
                            LocalTime endLocalTime = TimeConverter.getLocalTime2(endTime);

                            if(!startLocalTime.plusMinutes(15).equals(endLocalTime)) {
                                while (!startLocalTime.plusMinutes(15).equals(endLocalTime)) {
                                    System.out.println("startloctime " + startLocalTime);
                                    subTimes.add(startLocalTime.toString()+":00");
                                    startLocalTime = startLocalTime.plusMinutes(15);
                                    System.out.println("startlocaltime " + startLocalTime);
                                }
                                subTimes.add(startLocalTime.toString()+":00");
//                                subTimes.add(endLocalTime.toString()+":00");
                            } else {
                                subTimes.add(startLocalTime.toString()+":00");
                            }
                        });
                    }
                });
                subTimes.forEach(j -> System.out.println("time " + j));
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
        subTimes.removeAll(scheduledTimes);

        /**
         * Onload create appointment set start and end times
         * Also increase the endTimeComboBox when the startTimeComboBox changes
         * time.
         */
        startTimeComboBox.setItems(subTimes);
        startTimeComboBox.getSelectionModel().select(0);
        endTimeComboBox.setItems(subTimes);
        endTimeComboBox.getSelectionModel().select(1);
        startTimeComboBox.setOnAction((e) -> {
            int index = startTimeComboBox.getSelectionModel().getSelectedIndex() + 1;
            ObservableList<String> endTime = FXCollections.observableArrayList(subTimes.subList(index, subTimes.size() -1 ));
//                    System.out.println(index);
            endTimeComboBox.setItems(endTime);
            endTimeComboBox.getSelectionModel().select(0);
        });
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
