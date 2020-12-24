package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customer;
import utils.DatabaseConnectionManager;
import utils.SingletonLogin;
import utils.TimeConverter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.concurrent.atomic.AtomicReference;

public class CustomerDAO {
    private static final String loginName =  SingletonLogin.getLoginName();
    private static final Long loginId = SingletonLogin.getLoginId();

    /**
     * GET ALL CUSTOMERS
     */
    private static final String GET_CUSTOMERS = "SELECT " +
            "       customerId, \n" +
            "       customerName, \n" +
            "       addressId, \n" +
            "       active, \n" +
            "       createDate, \n" +
            "       createdBy, \n" +
            "       lastUpdate, \n" +
            "       lastUpdateBy\n" +
            "FROM customer";
    /**
     * GET CUSTOMERS ADDRESSES CITY AND COUNTRY
     */
    private static final String GET_ALL_CUSTOMERS = "SELECT c.customerId,\n" +
            "       c.customerName,\n" +
            "       c.active,\n" +
            "       a.address,\n" +
            "       a.postalCode,\n" +
            "       a.phone,\n" +
            "       c2.city,\n" +
            "       c3.country,\n" +
            "       c.createDate\n" +
            "FROM customer c\n" +
            "    INNER JOIN address a ON c.addressId = a.addressId\n" +
            "    INNER JOIN city c2 ON a.cityId = c2.cityId\n" +
            "    INNER JOIN country c3 ON c2.countryId = c3.countryId";

    /**
     * GET CUSTOMER BY ID
     */
    private static final String GET_CUSTOMER_BY_ID = "SELECT " +
            "       customerId, \n" +
            "       customerName, \n" +
            "       addressId, \n" +
            "       active, \n" +
            "       createDate, \n" +
            "       createdBy, \n" +
            "       lastUpdate, \n" +
            "       lastUpdateBy\n" +
            "FROM customer WHERE customerId = ?";

    /**
     * INSERT INTO THE CUSTOMER TABLE
     */
    private static final String INSERT_CUSTOMER = "" +
            "INSERT INTO customer (customerName, " +
            "                      addressId, " +
            "                      active, " +
            "                      createDate, " +
            "                      createdBy, " +
            "                      lastUpdate, " +
            "                      lastUpdateBy) " +
            "              VALUES (?, ?, ?, now(), ?, now(), ?)";

    private static final String INSERT_ADDRESS = "INSERT INTO address (address, address2, cityId, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdateBy) VALUES (?, ?, ?, ?, ?, now(), ?, now(), ?)";
    private static final String INSERT_CITY = "INSERT INTO city (city, countryId, createDate, createdBy, lastUpdate, lastUpdateBy) VALUES (?, ?, now(), ?, now(), ?)";
    private static final String INSERT_COUNTRY = "INSERT INTO country (country, createDate, createdBy, lastUpdate, lastUpdateBy) VALUES (?, now(), ?, now(), ?)";

    /**
     * UPDATE THE CUSTOMER TABLE
     */
    private static final String UPDATE_CUSTOMER = "" +
            "UPDATE customer SET customerName = ?, " +
            "                    addressId = ?, " +
            "                    active = ?, " +
            "                    lastUpdate = now(), " +
            "                    lastUpdateBy = ? " +
            "              WHERE customerId = ?";

    /**
     * Update status if user just wants to disable an account that hasn't
     * been active you can set the status to 0
     */
    private static final String UPDATE_CUSTOMER_STATUS = "" +
            "UPDATE customer SET active = 0, " +
            "                    lastUpdate = now(), " +
            "                    lastUpdateBy = ? " +
            "              WHERE customerId = ?";

    /**
     * Just updates the address
     */
    private static final String UPDATE_ADDRESS = "UPDATE address SET address = ?, \n" +
            "                   address2 = ?, \n" +
            "                   cityId = ?, \n" +
            "                   postalCode = ?, \n" +
            "                   phone = ?, \n" +
            "                   createDate = ?, \n" +
            "                   createdBy = ?, \n" +
            "                   lastUpdate = ?, \n" +
            "                   lastUpdateBy = ? \n" +
            "             WHERE addressId = ?";
    /**
     *  Updates the customer and address
     */
    private static final String UPDATE_CUSTOMER_ADDRESS = "UPDATE customer c, address a\n" +
            "               SET c.customerName = ?,\n" +
            "                   c.active = ?,\n" +
            "                   c.lastUpdate = ?,\n" +
            "                   c.lastUpdateBy = ?,\n" +
            "                   a.address = ?,\n" +
            "                   a.address2 = ?,\n" +
            "                   a.cityId = ?,\n" +
            "                   a.postalCode = ?,\n" +
            "                   a.phone = ?,\n" +
            "                   a.lastUpdate = ?,\n" +
            "                   a.lastUpdateBy = ?\n" +
            "             WHERE a.addressId = c.addressId\n" +
            "               AND c.customerId in (?)";

// Updates the customer address city and country. This is the one I am going to use for the project.
//    If i had time i would implement the other ones.
    private static final String UPDATE_CUSTOMER_ADDRESS_CITY_COUNTRY = "UPDATE customer c, address a, country c1, city c2\n" +
            " SET  c.customerName = ?,\n" +
            "      c.active = ?,\n" +
            "      c.lastUpdate = now(),\n" +
            "      c.lastUpdateBy = ?,\n" +
            "      a.address = ?,\n" +
            "      a.address2 = ?,\n" +
            "      a.postalCode = ?,\n" +
            "      a.phone = ?,\n" +
            "      a.lastUpdate = now(),\n" +
            "      a.lastUpdateBy = ?,\n" +
            "     c2.city = ?,\n" +
            "     c2.lastUpdate = now(),\n" +
            "     c2.lastUpdateBy = ?,\n" +
            "     c1.country = ?,\n" +
            "     c1.lastUpdate = now(),\n" +
            "     c1.lastUpdateBy = ?\n" +
            "WHERE c.addressId = a.addressId\n" +
            "  AND a.cityId = c2.cityId\n" +
            "  AND c2.countryId = c1.countryId" +
            "  AND c.customerId = ?";

    /**
     * MUST DELETE APPOINTMENT THEN DELETE FROM CUSTOMER THEN DELETE FROM ADDRESS
     * I NEED TO MOVE THE DELETE APPOINTMENT TO THE APPOINTMENT DAO
     */
    private static final String DELETE_APPOINTMENT = "DELETE FROM appointment WHERE customerId in (?)";
    private static final String DELETE_CUSTOMER = "DELETE FROM customer WHERE customerId IN (?)";
    private static final String DELETE_ADDRESS = "DELETE FROM address WHERE addressId IN (SELECT addressId FROM customer WHERE customerId in (?))";

    /**
     * CREATE AN OBSERVABLE LIST OF CUSTOMERS FROM THE DATABASE
     * @return
     */
    public static ObservableList<Customer> findAllCustomers() {
        ObservableList<Customer> customers = FXCollections.observableArrayList();

        try {
            DatabaseConnectionManager dcm = new DatabaseConnectionManager();
            PreparedStatement ps = dcm.prepareStatement(GET_ALL_CUSTOMERS);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                Long customerId = rs.getLong("customerId");
                String customerName = rs.getString("customerName");
                Byte active = rs.getByte("active");
                String address = rs.getString("address");
                String postalCode = rs.getString("postalCode");
                String phone = rs.getString("phone");
                String city = rs.getString("city");
                String country = rs.getString("country");
                String createDate = TimeConverter.utcDateConverter(rs.getString("createDate").substring(0, 19));

                Customer customer = new Customer(
                        address,
                        city,
                        country,
                        postalCode,
                        phone,
                        customerId,
                        customerName,
                        active,
                        createDate
                );
                customers.add(customer);
            }
            rs.close();
            ps.close();
            dcm.closeConnection();

        } catch (Exception e) {
            System.out.println("Customer Error " + e.getMessage());
        }

        return customers;
    }

    /**
     * ADD NEW CUSTOMER CALLED FROM CREATING A CUSTOMER FORM
     * @param customerName
     * @param active
     * @param address
     * @param address2
     * @param postalCode
     * @param phone
     * @param city
     * @param country
     * @return
     */
    public static String addNewCustomer(String customerName,
                                        Long active,
                                        String address,
                                        String address2,
                                        String postalCode,
                                        String phone,
                                        String city,
                                        String country) {

        System.out.println("Login name Singleton " + loginName);

        AtomicReference<String> status = new AtomicReference<>("Success");

        try {
            DatabaseConnectionManager dcm = new DatabaseConnectionManager();
            PreparedStatement ps = dcm.prepareStatement(INSERT_CUSTOMER, Statement.RETURN_GENERATED_KEYS);
            PreparedStatement ps1 = dcm.prepareStatement(INSERT_ADDRESS, Statement.RETURN_GENERATED_KEYS);
            PreparedStatement ps2 = dcm.prepareStatement(INSERT_CITY, Statement.RETURN_GENERATED_KEYS);
            PreparedStatement ps3 = dcm.prepareStatement(INSERT_COUNTRY, Statement.RETURN_GENERATED_KEYS);

            ps3.setString(1, country);
            ps3.setString(2, loginName);
            ps3.setString(3, loginName);
            ps3.executeUpdate();
            ResultSet rs = ps3.getGeneratedKeys();
            while(rs.next()) {
                long countryId = rs.getLong(1);
                ps2.setString(1, city);
                ps2.setLong(2, countryId);
                ps2.setString(3, loginName);
                ps2.setString(4, loginName);
                ps2.executeUpdate();
            }
            ResultSet rs1 = ps2.getGeneratedKeys();
            while(rs1.next()) {
                long cityId = rs1.getLong(1);
                ps1.setString(1, address);
                ps1.setString(2, address2);
                ps1.setLong(3, cityId);
                ps1.setString(4, postalCode);
                ps1.setString(5, phone);
                ps1.setString(6, loginName);
                ps1.setString(7, loginName);
                ps1.executeUpdate();
            }
            ResultSet rs2 = ps1.getGeneratedKeys();
            while (rs2.next()) {
                long addressId = rs2.getLong(1);
                ps.setString(1, customerName);
                ps.setLong(2, addressId);
                ps.setLong(3, active);
                ps.setString(4, loginName);
                ps.setString(5, loginName);
                ps.executeUpdate();
            }

        } catch (Exception e) {
            status.set("Add customer error");
            System.out.println(status + " " + e.getMessage());
        }
        return status.get();
    }

    /**
     *
     * @param id
     * @param customerName
     * @param active
     * @param address
     * @param address2
     * @param postalCode
     * @param phone
     * @param city
     * @param country
     * @return
     */
    public static String updateCustomer(Long id,
                                        String customerName,
                                        Long active,
                                        String address,
                                        String address2,
                                        String postalCode,
                                        String phone,
                                        String city,
                                        String country) {

        AtomicReference<String> status = new AtomicReference<>("Success");

        try {
            DatabaseConnectionManager dcm = new DatabaseConnectionManager();
            PreparedStatement ps = dcm.prepareStatement(UPDATE_CUSTOMER_ADDRESS_CITY_COUNTRY);
            ps.setString(1, customerName);
            ps.setLong(2, active);
            ps.setString(3, loginName);
            ps.setString(4, address);
            ps.setString(5, address2);
            ps.setString(6, postalCode);
            ps.setString(7, phone);
            ps.setString(8, loginName);
            ps.setString(9, city);
            ps.setString(10, loginName);
            ps.setString(11, country);
            ps.setString(12, loginName);
            ps.setLong(13, id);
            ps.executeUpdate();
        } catch (Exception e) {
            status.set("Update customer error");
            System.out.println(status + " " + e.getMessage());
        }

        return status.get();
    }

    /**
     *
     * @param id
     * @throws Exception
     */
    public static void deleteCustomer(Long id) throws Exception {
        deleteRows(id, DELETE_APPOINTMENT);
        deleteRows(id, DELETE_CUSTOMER);
        deleteRows(id, DELETE_ADDRESS);

//        DatabaseConnectionManager dcm = new DatabaseConnectionManager();
//        PreparedStatement ps = dcm.prepareStatement(DELETE_CUSTOMER);
//        ps.setLong(1, id);
//        ps.execute();
//
//        PreparedStatement ps1 = dcm.prepareStatement(DELETE_ADDRESS);
//        ps1.setLong(1, id);
//        ps.execute();
    }

    /**
     *
     * @param id
     * @param query
     * @throws Exception
     */
    public static void deleteRows(Long id, String query) throws Exception {
        DatabaseConnectionManager dcm = new DatabaseConnectionManager();
        PreparedStatement ps = dcm.prepareStatement(query);
        ps.setLong(1, id);
        ps.execute();
    }
}
