package licenta;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class connections {

    private static final String JDBC_URL = "jdbc:mysql://127.0.0.1:47335/editor?user=mindsdb";

    public static void main(String[] args) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(JDBC_URL);
            System.out.println("Connected to the database!");
        } catch (SQLException e) {
            System.err.println("Connection failed!");
            e.printStackTrace();
        }

		
//		  try { DatabaseMetaData metaData = connection.getMetaData();
//		  
//		  ResultSet tablesResultSet = metaData.getTables(null, null, "%", new
//		  String[]{"TABLE"}); System.out.println("Tables:"); while
//		 (tablesResultSet.next()) { String tableName =
//		  tablesResultSet.getString("TABLE_NAME"); System.out.println(tableName); }
//		  tablesResultSet.close();
//		  
//		 ResultSet modelsResultSet = metaData.getTables(null, null, "%", new
//		  String[]{"VIEW"}); System.out.println("\nModels:"); while
//		  (modelsResultSet.next()) { String viewName =
//		  modelsResultSet.getString("TABLE_NAME"); System.out.println(viewName); }
//		  modelsResultSet.close();
//		  
//		 } catch (SQLException e) { e.printStackTrace(); }
		 
    }
}