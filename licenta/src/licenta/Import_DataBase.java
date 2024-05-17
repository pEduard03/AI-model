package licenta;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Import_DataBase {
    private static final String JDBC_URL = "jdbc:mysql://127.0.0.1:47335/mindsdb?user=mindsdb";

    public static void main(String[] args) {
        Connection connection = null;
        Statement statement = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(JDBC_URL);
            System.out.println("Connection to the database created successfully");

            statement = connection.createStatement();

            String uploadQuery = "CREATE TABLE sales_data AS SELECT * FROM 'C:/Users/EDUARD/Desktop/Sales_licenta.csv'";
            statement.execute(uploadQuery);
            System.out.println("CSV file uploaded and table created successfully");
            // Step 2: Create a table from the uploaded CSV file
            String createTableQuery = "CREATE TABLE sales_data " +
                    "FROM 'Sales_licenta.csv' ";
            statement.execute(createTableQuery);
            System.out.println("Table created from CSV file successfully");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("An error occurred while processing the CSV file.");
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