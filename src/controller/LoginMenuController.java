package controller;

import DAO.AppointmentDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import utils.SingletonLogin;
import utils.TimeConverter;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.TimeZone;

public class LoginMenuController implements Initializable {
    private Label label;
    Stage stage;
    Parent scene;
    ResourceBundle rb;
    public static Long loggedInUserId;
    public static String loggedInUserName;


    private Connection connection;
    private static final String GET_USER = "SELECT userId, userName, password, " +
            "active, createDate, createdBy, lastUpdate, lastUpdateBy " +
            "FROM user WHERE userName=? AND password=?";

    @FXML
    private TextField usernameTextField;

    @FXML
    private Label labelUserName;

    @FXML
    private Label labelPassword;

    @FXML
    private PasswordField passwordPasswordField;

    @FXML
    private Label labelLoginTitle;

    @FXML
    private Label errorLabel;

    @FXML
    private Button exitButton;

    @FXML
    private Button loginButton;

    @FXML
    void onActionExit(ActionEvent event) {
        if (event.getSource() == exitButton)
            System.exit(0);
    }

    @FXML
    void onActionLogin(ActionEvent event) throws IOException {

        ResourceBundle rb = ResourceBundle.getBundle("properties/Nat", Locale.getDefault());


        if (usernameTextField.getText().isEmpty() || passwordPasswordField.getText().isEmpty()) {
            usernameTextField.setPromptText(rb.getString("usernamePromptText"));
            passwordPasswordField.setPromptText(rb.getString("passwordPromptText"));
        }
        else {
            String username = usernameTextField.getText();
            String password = passwordPasswordField.getText();
            System.out.printf("%s, %s ", username, password);
            Boolean valid = validateLogin(username, password, event);
            System.out.println(valid);
            if(valid) {
                showMainMenu(event);
            }
        }
    }

    /**
     *
     * @param event
     * @throws IOException
     */
    void showMainMenu(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
        stage.setScene(new Scene(scene));
    }

    /**
     *
     * @param event
     * @throws IOException
     */
    void showAppointmentsMenu(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/ViewAppointmentsMenu.fxml"));
        stage.setScene(new Scene(scene));
    }

    /**
     *
     * @param username
     * @param password
     * @param event
     * @return
     * @throws IOException
     */
    public Boolean validateLogin(String username, String password, ActionEvent event) throws IOException {
        Boolean valid = SingletonLogin.validateLogin(username, password);
        if(!valid) {
            setErrorLabel();
            return false;
        } else {
            checkTimeWithinFifteenMinutes(event);
            return true;
        }
    }

    /**
     *
     * @param event
     * @throws IOException
     */
    public void checkTimeWithinFifteenMinutes(ActionEvent event) throws IOException {

        /**
         * I used a Lambda expression because it was simpler to use with a
         * a foreach loop
         */
        AppointmentDAO.getStartDateTime().forEach(e -> {
            LocalDate startDate = TimeConverter.getLocalDate(e);
            LocalTime  startTime = TimeConverter.getLocalTime(e);
            System.out.println("date time " + startDate + " " + startTime);
            LocalDate currentDate = LocalDate.now();
            LocalTime currentTime = LocalTime.now();
            Long timeDifference = ChronoUnit.MINUTES.between(startTime, currentTime);
            Long interval = (timeDifference + -1) * -1;

            if(startDate.equals(currentDate)) {
                System.out.println("current date " + currentDate);
                if(interval > 0 && interval <= 15) {
                    System.out.println("You have an event in " + interval + " minutes");
                    try {
                        appointmentAlert(event, interval);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            }
        });
//        LocalTime startTime = LocalTime.of(14, 30);
    }

    private void setErrorLabel() {
        errorLabel.setTextFill(Color.web("#ff0000"));
        errorLabel.setText(rb.getString("lLabelError"));
    }

    public static String getLoggedInUserName() {
        System.out.println("logged in user " + loggedInUserName);
        return loggedInUserName;
    }

    public void appointmentAlert(ActionEvent event, Long minutes) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Appointment Alert");
        alert.setHeaderText("You have an appointment in " + minutes + " minutes ");
        alert.setContentText("Choose an option.");
        ButtonType mainButton = new ButtonType("Main Menu");
        ButtonType appointmentsButton = new ButtonType("View Appointments");

        alert.getButtonTypes().setAll(mainButton, appointmentsButton);

        Optional<ButtonType> option = alert.showAndWait();
        if (option.get() == mainButton){
            showMainMenu(event);
        } else  {
            showAppointmentsMenu(event);
        }
    }

    /**
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.rb = rb;
        System.out.println(Locale.getDefault());
//        ZoneId.getAvailableZoneIds().forEach(System.out::println);
//        ZoneId localZoneId = ZoneId.of(TimeZone.getDefault().getID());
//        System.out.println("localzoneid " + localZoneId.getId());
        rb = ResourceBundle.getBundle("properties/Nat", Locale.getDefault());
        labelUserName.setText(rb.getString("lLabelUserName"));
        labelPassword.setText(rb.getString("lLabelPassword"));

        labelLoginTitle.setText(rb.getString("lLabelTitle"));
        exitButton.setText(rb.getString("lButtonExit"));
        loginButton.setText(rb.getString("lButtonLogin"));
    }

    /**
     *
     * @return
     */
    public String getZoneId() {
        return ZoneId.of(TimeZone.getDefault().getID()).getId();
    }
}
