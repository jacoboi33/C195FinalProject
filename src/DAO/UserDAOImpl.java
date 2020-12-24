package DAO;

import java.sql.Connection;

public class UserDAOImpl {

    private static final String GET_USER = "SELECT userId, userName, password, " +
            "active, createDate, createdBy, lastUpdate, lastUpdateBy " +
            "FROM user WHERE userName=? AND password=?";

    private static final String GET_USERS ="SELECT userId, userName, password, " +
            "active, createDate, createdBy, lastUpdate, lastUpdateBy " +
            "FROM user";

    private final Connection connection;

    public UserDAOImpl(Connection connection) {
        this.connection = connection;
    }

//    @Override
//    public User getUser(String userName, String password) {
//        User user = new User();
//        try(PreparedStatement statement = this.connection.prepareStatement(GET_USER);){
//            statement.setString(1, userName);
//            statement.setString(2, password);
//            ResultSet rs = statement.executeQuery();
//            while(rs.next()){
//                user.setUserId(Integer.parseInt("userId"));
//                user.setUsername("userName");
//                user.setPassword("password");
//                user.setActive(Integer.parseInt("active"));
//                String createDate = rs.getString("createDate");
//                user.setCreateDate(stringToCalendar(createDate));
//                user.setCreatedBy("createdBy");
//                String lastUpdate = rs.getString("lastUpdate");
//                user.setLastUpdate(stringToCalendar(lastUpdate));
//                user.setLastUpdateBy("lastUpdateBy");
//            }
//        }catch (SQLException | ParseException e){
//            e.printStackTrace();
//            throw new RuntimeException(e);
//        }
//        return user;
//    }

}








