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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Address;
import model.Appointment;
import model.Customer;

import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.Optional;
import java.util.ResourceBundle;

public class ViewCustomersController implements Initializable {
    Stage stage;
    Parent scene;

    @FXML
    private Label viewCustomersLabel;

    @FXML
    private Button mainMenuButton;

    @FXML
    private TableView<Customer> customerTable;

    @FXML
    private TableColumn<Customer, Long> idColumn;

    @FXML
    private TableColumn<Customer, String> customerNameColumn;

    @FXML
    private TableColumn<Customer, Byte> activeColumn;

    @FXML
    private TableColumn<Address, String> addressColumn;

    @FXML
    private TableColumn<Address, String> postalCodeColumn;

    @FXML
    private TableColumn<Address, String> phoneColumn;

    @FXML
    private TableColumn<Address, String> cityColumn;

    @FXML
    private TableColumn<Address, String> countryColumn;

    @FXML
    private TableColumn<Address, String> dateColumn;

    @FXML
    private Button createButton;

    @FXML
    private Button editButton;

    @FXML
    private Button deleteButton;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        customerTableView();
    }

    @FXML
    void onActionCreateCustomer(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent scene = FXMLLoader.load(getClass().getResource("/view/CreateCustomerMenu.fxml"));
        stage.setScene(new Scene(scene));
    }

    @FXML
    void onActionDeleteCustomer(ActionEvent event) throws Exception {
        Long selectedCustomerId;
        if(customerTable.getSelectionModel().isEmpty())
            return;
        else {
            selectedCustomerId = customerTable.getSelectionModel().getSelectedItem().getId();
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Customer");
        alert.setContentText("Are you sure you want to delete this customer? ");

        Optional<ButtonType> alertType = alert.showAndWait();
        if(alertType.get().equals(ButtonType.OK))
            CustomerDAO.deleteCustomer(selectedCustomerId);
        else
            alert.close();

        customerTableView();
    }

    @FXML
    void onActionEditCustomer(ActionEvent event) throws IOException {

        loadAndSendCustomer(event);
//        customerTableView();
//        Customer getSelectedCustomer = customerTable.getSelectionModel().getSelectedItem();
//
//        if (getSelectedCustomer != null) {
//            EditCustomerController controller = new EditCustomerController();
//            controller.updateCustomerTextFields(getSelectedCustomer);
//
//            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
//            Parent scene = FXMLLoader.load(getClass().getResource("/view/EditCustomerMenu.fxml"));
//            stage.setScene(new Scene(scene));

//            FXMLLoader loader = new FXMLLoader();
//            loader.setLocation(getClass().getResource("/view/EditCustomerMenu.fxml"));
//            Parent tableViewScene = loader.load();
//            Scene customerScene = new Scene(tableViewScene);
//
//
//
//            Stage window = new Stage();
//            window.setTitle("Edit Customer");
//            window.setScene(customerScene);
//            window.show();
//        } else {
//            Alert alert = new Alert(Alert.AlertType.WARNING);
//            alert.setTitle("Not Selected");
//            alert.setContentText("No customer selected, Please select a customer");
//            alert.showAndWait();
//        }

//        Customer customer = customerTable.getSelectionModel().getSelectedItem();
//        if(customer == null)
//            return;
//
//        FXMLLoader loader = new FXMLLoader();
//        loader.setLocation(getClass().getResource("/view/EditCustomerMenu.fxml"));
//        EditCustomerController cc = loader.getController();
//
//        cc.updateCustomerTextFields(customer);
//
//        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
////        Parent scene = FXMLLoader.load(getClass().getResource("/view/EditCustomerMenu.fxml"));
//        Parent scene = loader.getRoot();
//        stage.setScene(new Scene(scene));
//        stage.show();
    }

    private void loadAndSendCustomer(ActionEvent event) {


        Customer getSelectedCustomer = customerTable.getSelectionModel().getSelectedItem();

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/EditCustomerMenu.fxml"));
            loader.load();

            EditCustomerController controller = loader.getController();
            controller.updateCustomerTextFields(getSelectedCustomer);

            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();

//            FXMLLoader loader = new FXMLLoader();
//            loader.setLocation(getClass().getResource("/view/EditCustomerMenu.fxml"));
//            Parent tableViewScene = loader.load();
//            Scene customerScene = new Scene(tableViewScene);
//
//            EditCustomerController controller = loader.getController();
//            controller.updateCustomerTextFields(getSelectedCustomer);
//
//            Stage window = new Stage();
//            window.setScene(customerScene);
//            window.show();

        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Not Selected");
            alert.setContentText("No customer selected, Please select a customer " + e.getMessage());
            alert.showAndWait();
        }

    }

//    @Override
//    public void quit() {
//        stage.close();
//    }

    @FXML
    void onActionMainMenu(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
        stage.setScene(new Scene(scene));
    }

    private void customerTableView() {
        ObservableList<Customer> customers = CustomerDAO.findAllCustomers();

        customers.forEach(entry -> {
            System.out.println(entry.getId());
            System.out.println(entry.getCustomerName());
            System.out.println(entry.getActive());
            System.out.println(entry.getAddress());
            System.out.println(entry.getPostalCode());
            System.out.println(entry.getPhone());
            System.out.println(entry.getCity());
            System.out.println(entry.getCountry());
            System.out.println(entry.getCreateDate());
        });

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        activeColumn.setCellValueFactory(new PropertyValueFactory<>("active"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));

        postalCodeColumn.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        cityColumn.setCellValueFactory(new PropertyValueFactory<>("city"));
        countryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("createDate"));
        customerTable.setItems(customers);
    }
}
