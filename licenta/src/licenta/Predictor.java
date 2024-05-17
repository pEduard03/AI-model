package licenta;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Predictor {
	private static final String JDBC_URL = "jdbc:mysql://127.0.0.1:47335/editor?user=mindsdb";
    public static void main(String[] args) {


        Connection connection = null;
        Statement statement = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(JDBC_URL);
            System.out.println("Connection to the database created successfully");

            String query = "CREATE MODEL mindsdb.home_rentals_predictor FROM example_db (SELECT * FROM home_rentals)PREDICT rental_price;";
            statement = connection.createStatement();
            boolean isSuccess = statement.execute(query);

            System.out.println("Is the predictor model created successfully: " + isSuccess);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("An error occurred while creating the predictor. " + e);
        }
    }
}