package licenta;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import org.json.JSONObject;
public class SinglePrediction {
	private static final String JDBC_URL = "jdbc:mysql://127.0.0.1:47335/editor?user=mindsdb";
    public static void main(String[] args) {
      

        Connection connection = null;
        Statement prediction = null;
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(JDBC_URL);
            System.out.println("Connection to the database created successfully");

            String query = "SELECT rental_price,rental_price_explain FROM mindsdb.home_rentals_predictor  WHERE sqft = 823 AND location='good'  AND neighborhood='downtown'  AND days_on_market=10;";
            prediction = connection.createStatement();
            ResultSet rs = prediction.executeQuery(query);

            while (rs.next()) {
            	
                int rental_price = rs.getInt("rental_price");
               
                String rentalPriceExplainJson = rs.getString("rental_price_explain");
                JSONObject explainObject = new JSONObject(rentalPriceExplainJson);
                double confidence = explainObject.getDouble("confidence"); // Obține valoarea ca un număr
                float confidenceFloat = (float) confidence; // Converteste la float dacă este necesar
               
                String rental_sales_minJSON = rs.getString("rental_price_explain");
                JSONObject explainObject2 = new JSONObject(rental_sales_minJSON);
                int rental_price_min = explainObject2.getInt("confidence_lower_bound"); // Obține valoarea ca un număr
               // int rental_salesminFloat = (int) rental_price_min; // Converteste la float dacă este necesar
               
                String rental_sales_maxJSON = rs.getString("rental_price_explain");
                JSONObject explainObject3 = new JSONObject(rental_sales_maxJSON);
                int rental_price_max = explainObject3.getInt("confidence_upper_bound"); // Obține valoarea ca un număr
                //int rental_salesmaxFloat = (int) rental_price_max; // Converteste la float dacă este necesar
               
                System.out.println("\n\n rental_sales: " + rental_price + "\n Confidence: " + confidenceFloat +"\n Rental sales min:" + rental_price_min+ "\n Rental sales max:" +rental_price_max);
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("An error occurred. Please try again!" + e);
        }
    }
}