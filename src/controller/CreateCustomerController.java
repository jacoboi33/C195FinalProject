package controller;

import DAO.CustomerDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CreateCustomerController implements Initializable {

    Stage stage;
    Parent scene;

    @FXML
    private Label customerName;

    @FXML
    private Label statusLabel;

    @FXML
    private TextField createCustomerNameTextField;

    @FXML
    private RadioButton activeRadioButton;

    @FXML
    private ToggleGroup statusRadioButton;

    @FXML
    private RadioButton inactiveRadioButton;

    @FXML
    private Label invalidStatusLabel;

    @FXML
    private Label invalidCustomerNameLabel;

    @FXML
    private Label addressLabel;

    @FXML
    private Label address2Label;

    @FXML
    private TextField createCustomerAddress1TextField;

    @FXML
    private TextField createCustomerAddress2TextField;

    @FXML
    private TextField createCustomerCityTextField;

    @FXML
    private TextField createCustomerPostalCodeTextField;

    @FXML
    private TextField createCustomerPhoneNumberTextField;

    @FXML
    private TextField createCustomerCountryTextField;

    @FXML
    private Label cityLabel;

    @FXML
    private Label postalCodeLabel;

    @FXML
    private Label phoneNumberLabel;

    @FXML
    private Label countryLabel;

    @FXML
    private Label invalidStreetAddress;

    @FXML
    private Label invalidCityAddressLabel;

    @FXML
    private Label invalidPostalCodeLabel;

    @FXML
    private Label invalidPhoneNumberLabel;

    @FXML
    private Label invalidCountryLabel;

    @FXML
    private Button saveButton;

    @FXML
    private Button backButton;

    @FXML
    void onActionSaveCustomer(ActionEvent event) throws IOException {

        if(!isNoErrors(true)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Parameters");
            alert.setContentText("Please make sure all fields containing the * are filled");
            alert.showAndWait();
        } else {
            String customerName = createCustomerNameTextField.getText();
            Long active = inactiveRadioButton.isSelected() ? Long.valueOf(0) : Long.valueOf(1);
            String address = createCustomerAddress1TextField.getText();
            String address2 = createCustomerAddress2TextField.getText().isEmpty() ? "empty" : createCustomerAddress2TextField.getText();
            String city = createCustomerCityTextField.getText();
            String postalCode = createCustomerPostalCodeTextField.getText();
            String phoneNumber = createCustomerPhoneNumberTextField.getText();
            String country = createCustomerCountryTextField.getText();

            String val = CustomerDAO.addNewCustomer(customerName, active, address, address2, postalCode, phoneNumber, city, country);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("ADD CUSTOMER");
            alert.setContentText("Add Customer Message " + val);
            alert.showAndWait();
            onActionViewAppointments(event);

        }

    }

    private boolean isNoErrors(boolean noErrors) {
//        noErrors = true;
        if(createCustomerCountryTextField.getText().isEmpty()) {
            invalidCountryLabel.setTextFill(Color.web("#ff0000"));
            invalidCountryLabel.setText("*");
            noErrors = false;
        }
        if(createCustomerPostalCodeTextField.getText().isEmpty()) {
            invalidPostalCodeLabel.setTextFill(Color.web("#ff0000"));
            invalidPostalCodeLabel.setText("*");
            noErrors = false;
        }
        if(createCustomerPhoneNumberTextField.getText().isEmpty()) {
            invalidPhoneNumberLabel.setTextFill(Color.web("#ff0000"));
            invalidPhoneNumberLabel.setText("*");
            noErrors = false;
        }
        if(createCustomerNameTextField.getText().isEmpty()) {
            invalidCustomerNameLabel.setTextFill(Color.web("#ff0000"));
            invalidCustomerNameLabel.setText("*");
            noErrors = false;
        }

        if(!statusRadioButton.getSelectedToggle().isSelected()) {
            invalidStatusLabel.setTextFill(Color.web("#ff0000"));
            invalidStatusLabel.setText("*");
            noErrors = false;
        }

        return noErrors;
    }

    @FXML
    void onActionViewAppointments(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent scene = FXMLLoader.load(getClass().getResource("/view/ViewCustomerMenu.fxml"));
        stage.setScene(new Scene(scene));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        activeRadioButton.setSelected(true);
    }
}
