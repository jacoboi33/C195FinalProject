package utils;

import model.User;

import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class SingletonLogin {

    private static String loginName = null;
    private static long loginId = 0;
    private static final String GET_USER = "SELECT userId, userName, password, " +
            "active, createDate, createdBy, lastUpdate, lastUpdateBy " +
            "FROM user WHERE userName=? AND password=?";

    private SingletonLogin(){}

    public static Boolean validateLogin(String username, String password) {
        try {
            DatabaseConnectionManager databaseConnectionManager = new DatabaseConnectionManager();
//            Connection conn = databaseConnectionManager.getConnection();
//            PreparedStatement preparedStatement = conn.prepareStatement(GET_USER);
            PreparedStatement preparedStatement = databaseConnectionManager.prepareStatement(GET_USER);
            List<User> users = new ArrayList<User>();

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                loginId = rs.getLong("userId");
                loginName = rs.getString("userName");
                String pwd = "secret";
                Long active = rs.getLong("Active");
                Calendar createDate = TimeConverter.stringToCalendar(rs.getString("createDate").substring(0, 19));
                String createdBy = rs.getString("createdBy");
                Calendar lastUpdate = TimeConverter.stringToCalendar(rs.getString("lastUpdate").substring(0, 19));
                String lastUpdateBy = rs.getString("lastUpdateBy");
                System.out.println("Userid " + loginId);
                System.out.println("Username " + loginName);
                String lId = rs.getString("userId");
                Long myId = loginId;
                User myUser = new User(myId,
                        loginName,
                        pwd,
                        active,
                        createDate,
                        createdBy,
                        lastUpdate,
                        lastUpdateBy);

                users.add(myUser);

//                users.add(lId);
//                users.add(loginName);
//                users.add(pwd);
//                users.add(active);
//                users.add(createDate);
//                users.add(createdBy);
//                users.add(lastUpdate);
//                users.add(lastUpdateBy);

                createLoginFile(users);

                return true;
            }
            else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void createLoginFile(List<User> users) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("log.txt", true))) {
            String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
//            writer.write("Date and Time User Logged in " + timeStamp + " Location: " + SingletonLogin.getZoneId());
            writer.write(String.format("DateTime user logged in %s at %s location ", timeStamp, SingletonLogin.getLocation()));
            for (User user : users)
                writer.write(user.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static String getLoginName() {
        return loginName;
    }
    public static long getLoginId() {
        return loginId;
    }
    public static String getLocation() { return ZoneId.of(TimeZone.getDefault().getID()).getId(); }

}
