package licenta;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class JobEvery2Days {
    private static final String JDBC_URL = "jdbc:mysql://127.0.0.1:47335/mindsdb?user=mindsdb"; // updated URL

    public static void main(String[] args) {
        Connection connection = null;
        Statement statement = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(JDBC_URL);
            System.out.println("Connection to the database created successfully");

            String createJobQuery = "CREATE JOB improve_model4 ( " +
                    "RETRAIN mindsdb.home_rentals_predictor " +
                    "FROM example_db (SELECT * FROM home_rentals)) " +
                    "EVERY 2 days " +
                    "IF (SELECT * FROM example_db.home_rentals WHERE created_at > LAST)";

            statement = connection.createStatement();
            statement.execute(createJobQuery);

            // Verify if the job was created
            String verifyJobQuery = "SHOW JOBS";
            ResultSet resultSet = statement.executeQuery(verifyJobQuery);
            boolean jobCreated = false;
            while (resultSet.next()) {
                if ("improve_model4".equals(resultSet.getString("name"))) {
                    jobCreated = true;
                    break;
                }
            }

            System.out.println("Is the job created successfully: " + jobCreated);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("An error occurred while creating the job. " + e);
        } finally {
            try {
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}