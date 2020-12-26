package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;
import utils.DataAccessObject;
import utils.DatabaseConnectionManager;
import utils.TimeConverter;

import java.io.IOException;
import java.sql.*;
import java.util.Arrays;

public class AppointmentDAO extends DataAccessObject<Appointment> {
    /**
     *
     */
    private static final String[] time = new String[]{
            "00:00:00", "00:15:00", "00:30:00", "00:45:00", "01:00:00", "01:15:00", "01:30:00", "01:45:00", "02:00:00", "02:15:00", "02:30:00", "02:45:00", "03:00:00", "03:15:00", "03:30:00", "03:45:00", "04:00:00", "04:15:00", "04:30:00", "04:45:00", "05:00:00", "05:15:00", "05:30:00", "05:45:00", "06:00:00", "06:15:00", "06:30:00", "06:45:00", "07:00:00", "07:15:00", "07:30:00", "07:45:00", "08:00:00", "08:15:00", "08:30:00", "08:45:00", "09:00:00", "09:15:00", "09:30:00", "09:45:00", "10:00:00", "10:15:00", "10:30:00", "10:45:00", "11:00:00", "11:15:00", "11:30:00", "11:45:00", "12:00:00", "12:15:00", "12:30:00", "12:45:00", "13:00:00", "13:15:00", "13:30:00", "13:45:00", "14:00:00", "14:15:00", "14:30:00", "14:45:00", "15:00:00", "15:15:00", "15:30:00", "15:45:00", "16:00:00", "16:15:00", "16:30:00", "16:45:00", "17:00:00", "17:15:00", "17:30:00", "17:45:00", "18:00:00", "18:15:00", "18:30:00", "18:45:00", "19:00:00", "19:15:00", "19:30:00", "19:45:00", "20:00:00", "20:15:00", "20:30:00", "20:45:00", "21:00:00", "21:15:00", "21:30:00", "21:45:00", "22:00:00", "22:15:00", "22:30:00", "22:45:00", "23:00:00", "23:15:00", "23:30:00", "23:45:00"
    };
    /**
     *
     */
    private static final String GET_APPOINTMENTS_BY_WEEK = "select      a.appointmentId,\n" +
            "            a.customerId,\n" +
            "            a.userId,\n" +
            "            c.customerName,\n" +
            "            u.userName,\n" +
            "            a.title,\n" +
            "            a.description,\n" +
            "            a.location,\n" +
            "            a.contact,\n" +
            "            a.type,\n" +
            "            a.url,\n" +
            "            a.start,\n" +
            "            a.end,\n" +
            "            a.createDate,\n" +
            "            a.createdBy,\n" +
            "            a.lastUpdate,\n" +
            "            a.lastUpdateBy\n" +
            "                     from appointment a\n" +
            "                      inner join customer c on a.customerId = c.customerId\n" +
            "                      inner join user u on a.userId = u.userId\n" +
            "            WHERE yearweek(DATE(start), 1) = yearweek(curdate(), 1)\n" +
            "                        order by a.start";

    /**
     *
     */
    private static final String GET_APPOINTMENTS_BY_MONTH = "select      a.appointmentId,\n" +
            "            a.customerId,\n" +
            "            a.userId,\n" +
            "            c.customerName,\n" +
            "            u.userName,\n" +
            "            a.title,\n" +
            "            a.description,\n" +
            "            a.location,\n" +
            "            a.contact,\n" +
            "            a.type,\n" +
            "            a.url,\n" +
            "            a.start,\n" +
            "            a.end,\n" +
            "            a.createDate,\n" +
            "            a.createdBy,\n" +
            "            a.lastUpdate,\n" +
            "            a.lastUpdateBy\n" +
            "                     from appointment a\n" +
            "                      inner join customer c on a.customerId = c.customerId\n" +
            "                      inner join user u on a.userId = u.userId\n" +
            "            WHERE (MONTH(start)) = month(curdate())\n" +
            "                        order by a.start";

    /**
     *
     */
    private static final String GET_APPOINTMENTS = "select\n" +
            "   a.appointmentId,\n" +
            "   a.customerId,\n" +
            "   a.userId,\n" +
            "   c.customerName,\n" +
            "   u.userName,\n" +
            "   a.title,\n" +
            "   a.description,\n" +
            "   a.location,\n" +
            "   a.contact,\n" +
            "   a.type,\n" +
            "   a.url,\n" +
            "   a.start,\n" +
            "   a.end,\n" +
            "   a.createDate,\n" +
            "   a.createdBy,\n" +
            "   a.lastUpdate,\n" +
            "   a.lastUpdateBy\n" +
            "            from appointment a\n" +
            "             inner join customer c on a.customerId = c.customerId\n" +
            "             inner join user u on a.userId = u.userId\n" +
            "            order by a.start";
    /**
     *
     */
    private static final String CREATE_APPOINTMENT = "INSERT INTO appointment (customerId, \n" +
            "                         userId, \n" +
            "                         title, \n" +
            "                         description, \n" +
            "                         location, \n" +
            "                         contact, \n" +
            "                         type,\n" +
            "                         url, \n" +
            "                         start, \n" +
            "                         end, \n" +
            "                         createDate, \n" +
            "                         createdBy, \n" +
            "                         lastUpdate, \n" +
            "                         lastUpdateBy) \n" +
            "            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, now(), ?, now(), ?)";
    /**
     *
     */
    private static final String UPDATE_APPOINTMENT = "UPDATE appointment SET customerId = ?,\n" +
            "                        userId = ?,\n" +
            "                        title = ?,\n" +
            "                        description = ?,\n" +
            "                        location = ?,\n" +
            "                        contact = ?,\n" +
            "                        type = ?,\n" +
            "                        url = ?,\n" +
            "                        start = ?,\n" +
            "                        end = ?,\n" +
            "                        lastUpdate = (now()),\n" +
            "                        lastUpdateBy = ?\n" +
            "            WHERE appointmentId = ?";
    /**
     *
     */
    private static final String DELETE_APPOINTMENT = "DELETE FROM appointment WHERE appointmentId IN (?)";

    /**
     *
     */
    private static final String GET_START_DATE_TIME = "SELECT start, end FROM appointment ORDER BY start";

    /**
     *
     * @param connection
     */
    public AppointmentDAO(Connection connection) {
        super(connection);
    }

    /**
     *
     * @param selectedAppointmentId
     * @throws Exception
     */
    public static void deleteAppointment(Long selectedAppointmentId) throws Exception {
        CustomerDAO.deleteRows(selectedAppointmentId, DELETE_APPOINTMENT);
    }

    /**
     *
     * @param id
     * @return
     */
    @Override
    public Appointment findById(Long id) {
        return null;
    }

    /**
     *
     * @return
     * @throws Exception
     */
    public static ResultSet findAll() throws Exception {
        DatabaseConnectionManager dcm = new DatabaseConnectionManager();
        PreparedStatement preparedStatement = dcm.prepareStatement(GET_APPOINTMENTS);
        return preparedStatement.executeQuery();
    }

    /**
     * RETURNS AN OBSERVABLE LIST OF TIMES FROM A PRIVATE STRING ARRAY.
     * @return
     */
    public static ObservableList<String> getAppointmentTimes() {
        ObservableList<String> times = FXCollections.observableArrayList();
        times.addAll(Arrays.asList(time));
        return times;
    }

    /**
     *
     * @return
     */
    public static ObservableList<String> getStartDateTime() {
        ObservableList<String> startDateTime = FXCollections.observableArrayList();

        try {
            DatabaseConnectionManager dcm = new DatabaseConnectionManager();
            PreparedStatement stmt = dcm.prepareStatement(GET_START_DATE_TIME);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String start = TimeConverter.utcDateConverter(rs.getString("start").substring(0, 19));
                startDateTime.add(start);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return startDateTime;
    }

    /**
     *
     * @return
     * @throws Exception
     */
    public static ObservableList<String> getStartAndEndDateTime() throws Exception {
        ObservableList<String> startEndDateTime = FXCollections.observableArrayList();
        String[] startEnd;

        DatabaseConnectionManager dcm = new DatabaseConnectionManager();
        PreparedStatement stmt = dcm.prepareStatement(GET_START_DATE_TIME);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            String start = TimeConverter.utcDateConverter(rs.getString("start").substring(0, 19));
            String end = TimeConverter.utcDateConverter(rs.getString("end").substring(0,19));
            startEndDateTime.add(start + ", " + end);
        }
        return startEndDateTime;
    }

    /**
     *
     * @return
     */
    public static ObservableList<Appointment> findAllAppointments() {

        ObservableList<Appointment> appointments = FXCollections.observableArrayList();

        try {

            DatabaseConnectionManager dcm = new DatabaseConnectionManager();
            PreparedStatement stmt = dcm.prepareStatement(GET_APPOINTMENTS);
            appointmentList(appointments, dcm, stmt);

        } catch (Exception e) {
            System.out.println("Appointment Error " + e.getMessage());
        }
        return appointments;
    }

    /**
     *
     * @param weekOrMonth
     * @return
     */
    public static ObservableList<Appointment> findAllAppointmentsByWeek(String weekOrMonth) {

        ObservableList<Appointment> appointments = FXCollections.observableArrayList();

        try {
            DatabaseConnectionManager dcm = new DatabaseConnectionManager();
            PreparedStatement stmt = weekOrMonth.equals("week") ? dcm.prepareStatement(GET_APPOINTMENTS_BY_WEEK) : dcm.prepareStatement(GET_APPOINTMENTS_BY_MONTH);
            appointmentList(appointments, dcm, stmt);

        } catch (Exception e) {
            System.out.println("Appointment Error " + e.getMessage());
        }
        return appointments;
    }


    /**
     *
     * @param appointments
     * @param dcm
     * @param stmt
     * @throws SQLException
     */
    private static void appointmentList(ObservableList<Appointment> appointments, DatabaseConnectionManager dcm, PreparedStatement stmt) throws SQLException {
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {

            Long appointmentId = rs.getLong("appointmentId");
            Long customerId = rs.getLong("customerId");
            Long userId = rs.getLong("userId");
            String customerName = rs.getString("customerName");
            String userName = rs.getString("userName");
            String title = rs.getString("title");
            String description = rs.getString("description");
            String location = rs.getString("location");
            String contact = rs.getString("contact");
            String type = rs.getString("type");
            String url = rs.getString("url");
            String start = TimeConverter.utcDateConverter(rs.getString("start").substring(0, 19));
            String end = TimeConverter.utcDateConverter(rs.getString("end").substring(0, 19));
            String createDate = TimeConverter.utcDateConverter(rs.getString("createDate").substring(0, 19));
            String createdBy = rs.getString("createdBy");
            String lastUpdate = TimeConverter.utcDateConverter(rs.getString("lastUpdate").substring(0,19));
            String lastUpdateBy = rs.getString("lastUpdateBy");

            Appointment appointment = new Appointment(
                    appointmentId,
                    customerId,
                    userId,
                    customerName,
                    userName,
                    title,
                    description,
                    location,
                    contact,
                    type,
                    url,
                    start,
                    end,
                    createDate,
                    createdBy,
                    lastUpdate,
                    lastUpdateBy
            );

            appointments.add(appointment);


        }
        rs.close();
        stmt.close();
        dcm.closeConnection();
    }

    /**
     *
     * @param query
     * @return
     * @throws Exception
     */
    private static PreparedStatement getData(String query) throws Exception {
        DatabaseConnectionManager dcm = new DatabaseConnectionManager();
        return dcm.prepareStatement(query);
    }

    /**
     *
     * @param customerId
     * @param userId
     * @param title
     * @param description
     * @param location
     * @param contact
     * @param type
     * @param url
     * @param start
     * @param end
     * @param createBy
     * @param lastUpdateBy
     * @return String
     */
    public static String insertAppointment(Long customerId, Long userId, String title, String description, String location, String contact, String type, String url, String start, String end, String createBy, String lastUpdateBy) {
        String status;
        try {
            PreparedStatement preparedStatement = getData(CREATE_APPOINTMENT);
            preparedStatement.setLong(1, customerId);
            preparedStatement.setLong(2, userId);
            preparedStatement.setString(3, title);
            preparedStatement.setString(4, description);
            preparedStatement.setString(5, location);
            preparedStatement.setString(6, contact);
            preparedStatement.setString(7, type);
            preparedStatement.setString(8, url);
            preparedStatement.setString(9, start);
            preparedStatement.setString(10, end);
            preparedStatement.setString(11, createBy);
            preparedStatement.setString(12, lastUpdateBy);

            preparedStatement.execute();
            preparedStatement.close();
            status = "Inserted row successfully";
            
        } catch (Exception e) {
            status = "Failed to insert row";
            System.out.printf("%s %s \n", status, e.getMessage());
        }
        return status;
    }

    /**
     * Updates the appointment and returns string message
     * of success or failure
     * @param customerId
     * @param userId
     * @param title
     * @param description
     * @param location
     * @param contact
     * @param type
     * @param url
     * @param start
     * @param end
     * @param lastUpdateBy
     * @param appointmentId
     * @return String
     */
    public static String updateAppointment(Long customerId, Long userId, String title, String description, String location, String contact, String type, String url, String start, String end, String lastUpdateBy, Long appointmentId) {
        String status;

        try {
            PreparedStatement preparedStatement = getData(UPDATE_APPOINTMENT);
            preparedStatement.setLong(1, customerId);
            preparedStatement.setLong(2, userId);
            preparedStatement.setString(3, title);
            preparedStatement.setString(4, description);
            preparedStatement.setString(5, location);
            preparedStatement.setString(6, contact);
            preparedStatement.setString(7, type);
            preparedStatement.setString(8, url);
            preparedStatement.setString(9, start);
            preparedStatement.setString(10, end);
            preparedStatement.setString(11, lastUpdateBy);
            preparedStatement.setLong(12, appointmentId);

            preparedStatement.executeUpdate();
            preparedStatement.close();
            status = "update appointment successfully";

        } catch (Exception e) {
            status = "failed to update appointment";
            System.out.printf("%s %s \n", status, e.getMessage());
        }
        return status;
    }

    @Override
    public Appointment update(Appointment dto) {
        return null;
    }

    @Override
    public Appointment create(Appointment dto) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }


}
