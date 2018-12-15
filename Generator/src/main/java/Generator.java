//STEP 1. Import required packages
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Generator {
  static final int THREADS = 50;
  private static final String CSV_FILE_NAME = "steps data.csv";
  
  // JDBC driver name and database URL
  static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
  
  static final String DB_URL = "jdbc:mysql://db-tao.chptyir9bzg1.us-west-2.rds.amazonaws.com:3306/db_example";
  
  
  
  //  Database credentials
  static final String USER = "root";
  static final String PASS = "12345678";
  
  public static void main(String[] args) {
  
    FileWriter fileWriter = null;
    try {
      fileWriter = new FileWriter(CSV_FILE_NAME);
    } catch (IOException io) {
      System.out.println("CSV writer init problem.");
      io.printStackTrace();
    }
    Connection conn = null;
    Statement stmt = null;
  
    try {
      //STEP 2: Register JDBC driver
      Class.forName(JDBC_DRIVER);
    
      //STEP 3: Open a connection
      System.out.println("Connecting to a selected database...");
      conn = DriverManager.getConnection(DB_URL, USER, PASS);
      System.out.println("Connected database successfully...");
    
      //STEP 4: Execute a query
      System.out.println("Creating table in given database...");
      stmt = conn.createStatement();
    
      String drop = "DROP TABLE IF EXISTS StepRecord ";
      stmt.executeUpdate(drop);
    
      String sql = "CREATE TABLE StepRecord" +
          "(id INTEGER not NULL, " +
          " day INTEGER not NULL, " +
          " step INTEGER not NULL)";
    
      stmt.executeUpdate(sql);
      System.out.println("Created table in given database...");

//      fileWriter.flush();
//      fileWriter.close();
    
    
    } catch (SQLException se) {
      //Handle errors for JDBC
      se.printStackTrace();
    } catch (Exception e) {
      //Handle errors for Class.forName
      e.printStackTrace();
    }
    ExecutorService executorService = Executors.newFixedThreadPool(THREADS);
    for (int i = 0; i < THREADS; i++) {
      executorService.execute(new Data(fileWriter));
    }
  }

}