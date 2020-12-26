package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Report;
import utils.DatabaseConnectionManager;
import utils.TimeConverter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ReportsDAO {

    private static final String REPORT_1 = "select count(type), \n" +
            "       type, \n" +
            "       MONTH(start), YEAR(start)\n" +
            "from appointment \n" +
            "group by type, MONTH(start), YEAR(start)";

    private static final String REPORT_2 = "select userName, \n" +
            "       a.title, \n" +
            "       a.description, \n" +
            "       a.start, \n" +
            "       a.end \n" +
            "from user\n" +
            "inner join appointment a on user.userId = a.userId order by start desc";

    private static final String REPORT_3 = "select count(userId),\n" +
            "       (select userName from user where appointment.userId = user.userId) as username,\n" +
            "       MONTH(start),\n" +
            "       YEAR(start)\n" +
            "from appointment\n" +
            "group by userId, MONTH(start), YEAR(start)";


    /**
     *
     * @return ObservableList
     */
    public static ObservableList<Report> generateReport1() {
        ObservableList<Report> report = FXCollections.observableArrayList();

        try {
            DatabaseConnectionManager dcm = new DatabaseConnectionManager();
            PreparedStatement stmt = dcm.prepareStatement(REPORT_1);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Long count = rs.getLong(1);
                String type = rs.getString(2);
                String month = rs.getString(3);
                String year = rs.getString(4);
                String monthYear = month + "-" + year;


                Report rep = new Report(
                        count,
                        type,
                        monthYear
                );

                report.add(rep);
            }

        } catch (Exception e) {
            System.out.println("Report1 Error " + e.getMessage());
        }

        return report;

    }

    /**
     *
     * @return ObservableList
     */
    public static ObservableList<Report> generateReport2() {
        ObservableList<Report> report2 = FXCollections.observableArrayList();

        try {
            DatabaseConnectionManager dcm = new DatabaseConnectionManager();
            PreparedStatement stmt = dcm.prepareStatement(REPORT_2);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                String username = rs.getString(1);
                String title = rs.getString(2);
                String description = rs.getString(3);
                String startTime = TimeConverter.utcDateConverter(rs.getString(4));
                String endTime = TimeConverter.utcDateConverter(rs.getString(5));

                Report rep2 = new Report(
                        username,
                        title,
                        description,
                        startTime,
                        endTime
                );

                report2.add(rep2);
            }

        } catch (Exception e) {
            System.out.println("Report2 Error " + e.getMessage());
        }

        return report2;
    }

    /**
     *
     * @return ObservableList
     */
    public static ObservableList<Report> generateReport3() {
        ObservableList<Report> report3 = FXCollections.observableArrayList();

        try {
            DatabaseConnectionManager dcm = new DatabaseConnectionManager();
            PreparedStatement stmt = dcm.prepareStatement(REPORT_3);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Long count = rs.getLong(1);
                String username = rs.getString(2);
                String month = rs.getString(3);
                String year = rs.getString(4);
                String monthYear = month + "-" + year;

                Report rep = new Report(
                        count,
                        username,
                        monthYear
                );

                report3.add(rep);
            }

        } catch (Exception e) {
            System.out.println("Report1 Error " + e.getMessage());
        }
        return report3;
    }
}
