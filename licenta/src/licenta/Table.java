package licenta;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
public class Table {
	
	private static final String JDBC_URL = "jdbc:mysql://127.0.0.1:47335/editor?user=mindsdb";
	public static void main(String[] args) {
		// Import the necessary packages
	
				
		        Connection connection = null;
		        Statement statement = null;

		        try {
//		            Class.forName("com.mysql.cj.jdbc.Driver");
//		            connection = DriverManager.getConnection(JDBC_URL);
//		            System.out.println("Connection to the database created successfully");
//
//		            String query = "SELECT * FROM files.UniTech_Sales_Data LIMIT 3";
//		            statement = connection.createStatement();
//		            ResultSet rs = statement.executeQuery(query);
//
//		            while (rs.next()) {
//		                String date = rs.getString("Date of Order");
//		                String storeCode = rs.getString("Store Code");
//		                String itemCode = rs.getString("Item Code");
//		                String sales = rs.getString("sales");
//		                String district = rs.getString("District");
//		                String state = rs.getString("State");
//
//		                System.out.println("\n\n Date: " + date + "\n Store Code: " + storeCode + "\n Item Code: " + itemCode
//		                        + "\n Sales: " + sales + "\n District: " + district + "\n State: " + state);
//		            }
		        	 Class.forName("com.mysql.cj.jdbc.Driver");
			            connection = DriverManager.getConnection(JDBC_URL);
			            System.out.println("Connection to the database created successfully");

			            String query = "SELECT * FROM example_db.home_rentals LIMIT 3";
			            statement = connection.createStatement();
			            ResultSet rs = statement.executeQuery(query);

			            while (rs.next()) {
			                String NumberOfRooms = rs.getString("number_of_rooms");
			                String NumberOfBathrooms = rs.getString("number_of_bathrooms");
			                String sqft = rs.getString("sqft");
			                String DaysOnMarket = rs.getString("days_on_market");
			                String Neighborhood = rs.getString("neighborhood");
			                String RentalPrice = rs.getString("rental_price");

			                System.out.println("\n\nNumber of Rooms: " + NumberOfRooms + "\nNumber of bathrooms: " + NumberOfBathrooms + "\nsqft: " + sqft
			                        + "\nDays On Market: " + DaysOnMarket + "\nNeighborhood: " + Neighborhood + "\nRentalPrice: " + RentalPrice);
			            }

		        } catch (Exception e) {
		            e.printStackTrace();
		            System.out.println("An error occurred while fetching table data.");
		        }
		    }

}
