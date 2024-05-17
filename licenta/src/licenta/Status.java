package licenta;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Status {
	private static final String JDBC_URL = "jdbc:mysql://127.0.0.1:47335/editor?user=mindsdb";
    public static void main(String[] args) {
        Connection connection = null;
        Statement statement = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(JDBC_URL);
            System.out.println("Connection to the database created successfully");

            String statusQuery = "SELECT status FROM mindsdb.predictors WHERE name='home_rentals_predictor'";
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(statusQuery);

            
            while (rs.next()) {
                String status = rs.getString("status");
                System.out.println("\n\n Status: " + status);
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("An error occurred while fetching table data.");
        }
    }
}