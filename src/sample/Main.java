package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../view/Login.fxml"));
        primaryStage.setTitle("Schedule an appointment");
        primaryStage.setScene(new Scene(root, 500, 350));
        primaryStage.show();
    }


    public static void main(String[] args) {

//        ResourceBundle rb = ResourceBundle.getBundle("properties/Nat", Locale.FRENCH);

//        if(Locale.getDefault().getLanguage().equals("es") || Locale.getDefault().getLanguage().equals("fr")){
//            System.out.println(rb.getString("lLabelUserName"));
//        }

//        DBConnection db = new DBConnection("wgudb.ucertify.com", "U05cx3", "U05cx3", "53688467011");
//
//        try {
//            Connection conn = db.getConnection();
//            Statement statement = conn.createStatement();
//            ResultSet resultSet = statement.executeQuery("SELECT * FROM user");
//
//        } catch (SQLException e) {
//            Alert alert = new Alert(Alert.AlertType.ERROR, e.toString());
//            alert.showAndWait();
//
//        }
        launch(args);
    }
}