package licenta;

import java.sql.Connection; 
import java.sql.DriverManager;
import java.sql.ResultSet; 
import java.sql.Statement; 
import org.json.JSONObject;
public class BatchPrediction
{ 
		private static final String JDBC_URL ="jdbc:mysql://127.0.0.1:47335/editor?user=mindsdb"; 
		public static void main(String[] args) 
		{
		Connection connection = null; Statement prediction = null;
		
		try { 
				Class.forName("com.mysql.cj.jdbc.Driver"); connection =
				DriverManager.getConnection(JDBC_URL);
				System.out.println("Connection to the database created successfully");
		
				String query ="SELECT t.rental_price as real_price,m.rental_price as predicted_price," +
				" t.number_of_rooms,  t.number_of_bathrooms, t.sqft, t.location, t.days_on_market"
				+ " FROM example_db.home_rentals as t" +
				" JOIN mindsdb.home_rentals_predictor as m" + " LIMIT 100;"; 
				prediction =connection.createStatement(); 
				ResultSet rs = prediction.executeQuery(query);
				int i=1; 
				while (rs.next()) 
				{
					int real_price = rs.getInt("real_price"); int predicted_price =
					rs.getInt("predicted_price"); int number_of_rooms=
					rs.getInt("number_of_rooms"); String location=rs.getString("location");
					if(number_of_rooms>1)
						System.out.println("\n\nHouse "+i++ + ": \nreal_price: " + real_price +
					"\npredicted price: " + predicted_price+"\nNumber of rooms: "+number_of_rooms+"\nLocation: "+location);
					
				}
 
			} catch (Exception e) 
				{ 
					e.printStackTrace();
					System.out.println("An error occurred. Please try again!" + e); 
				} 
		} 
}
