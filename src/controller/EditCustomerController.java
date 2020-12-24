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
import model.Customer;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class EditCustomerController implements Initializable {
    Stage stage;
    Parent scene;

    @FXML
    private Label editCustomerLabel;

    @FXML
    private Label customerNameLabel;

    @FXML
    private Label statusLabel;

    @FXML
    private TextField editCustomerNameTextField;

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
    private Label idLabel;

    @FXML
    private TextField idTextField;

    @FXML
    private Label addressLabel;

    @FXML
    private Label address2Label;

    @FXML
    private TextField editCustomerAddress1TextField;

    @FXML
    private TextField editAddress2TextField;

    @FXML
    private TextField editCustomerCityTextField;

    @FXML
    private TextField editCustomerPostalCodeTextField;

    @FXML
    private TextField editCustomerPhoneNumberTextField;

    @FXML
    private TextField editCustomerCountryTextField;

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
    private Button cancelButton;

    /**
     *
     * @param event
     * @throws IOException
     */
    @FXML
    void onActionCancelUpdateButton(ActionEvent event) throws IOException {
        Stage stage;
        if(event.getSource().equals(cancelButton)) {
            stage = (Stage) cancelButton.getScene().getWindow();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Cancel Button Selected");
            alert.setHeaderText("Confirm Cancellation");
            alert.setContentText("Are you sure you want to cancel ? ");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                stage.close();
            } else {
                alert.close();
            }
        } else{
            stage = (Stage) saveButton.getScene().getWindow();
            stage.close();
        }
    }

// TODO Take out redirect method its not being used.
    public void redirect(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent scene = FXMLLoader.load(getClass().getResource("/view/ViewCustomersController")); // Wrong file and needs to be .fxml
        stage.setScene(new Scene(scene));
    }

    /**
     * Save a customer from the CreateCustomerMenu.fxml
     * @param event
     * @throws IOException
     */
    @FXML
    void onActionSaveCustomer(ActionEvent event) throws IOException {
        if(!isNoErrors(true)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Parameters");
            alert.setContentText("Please make sure all fields containing the * are filled");
            alert.showAndWait();
        } else {
            Long id = Long.parseLong(idTextField.getText());
            String customerName = editCustomerNameTextField.getText();
            Long active = inactiveRadioButton.isSelected() ? Long.valueOf(0) : Long.valueOf(1);
            String address = editCustomerAddress1TextField.getText();
            String address2 = editAddress2TextField.getText() == null ? "empty" : editAddress2TextField.getText();
            String city = editCustomerCityTextField.getText();
            String postalCode = editCustomerPostalCodeTextField.getText();
            String phone = editCustomerPhoneNumberTextField.getText();
            String country = editCustomerCountryTextField.getText();

            CustomerDAO.updateCustomer(id, customerName, active, address, address2, postalCode, phone, city, country);
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/ViewCustomerMenu.fxml"));
            loader.load();

            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();

        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) { }

    /**
     * Called from ViewCustomerController to populate the text fields with
     * the selected data.
     * @param customer
     */
    public void updateCustomerTextFields(Customer customer) {
        editCustomerNameTextField.setText(customer.getCustomerName());
        // radio button
        if (customer.getActive() == 1) {
            activeRadioButton.setSelected(true);
        } else {
            inactiveRadioButton.setSelected(false);
        }
        idTextField.setText(Long.toString(customer.getId()));
        editCustomerAddress1TextField.setText(customer.getAddress());
        editAddress2TextField.setText(customer.getAddress2());
        editCustomerCityTextField.setText(customer.getCity());
        editCustomerPostalCodeTextField.setText(customer.getPostalCode());
        editCustomerPhoneNumberTextField.setText(customer.getPhone());
        editCustomerCountryTextField.setText(customer.getCountry());
    }

    /**
     * Validates the form fields to make sure they are filled in
     * if not there will be a red * where there needs to be text.
     * @param noErrors
     * @return
     */
    private boolean isNoErrors(boolean noErrors) {
        noErrors = true;
        if(editCustomerCountryTextField.getText().isEmpty()) {
            invalidCountryLabel.setTextFill(Color.web("#ff0000"));
            invalidCountryLabel.setText("*");
            noErrors = false;
        }
        if(editCustomerPostalCodeTextField.getText().isEmpty()) {
            invalidPostalCodeLabel.setTextFill(Color.web("#ff0000"));
            invalidPostalCodeLabel.setText("*");
            noErrors = false;
        }
        if(editCustomerPhoneNumberTextField.getText().isEmpty()) {
            invalidPhoneNumberLabel.setTextFill(Color.web("#ff0000"));
            invalidPhoneNumberLabel.setText("*");
            noErrors = false;
        }
        if(editCustomerNameTextField.getText().isEmpty()) {
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
//TODO Take out bellow method.
    private void switchScenes(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent scene = FXMLLoader.load(getClass().getResource("/view/ViewCustomerController.fxml"));
        stage.setScene(new Scene(scene));
    }
}
