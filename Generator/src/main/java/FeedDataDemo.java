//STEP 1. Import required packages
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

public class FeedDataDemo {
  // JDBC driver name and database URL
  static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
  static final String DB_URL = "jdbc:mysql://db-aggre.chptyir9bzg1.us-west-2.rds.amazonaws.com:3306/root";
  private static final String CSV_FILE_NAME = "3000-aggre.txt";
  
  
  //  Database credentials
  static final String USER = "root";
  static final String PASS = "12345678";
  
  public static void main(String[] args) {
    Connection conn = null;
    Statement stmt = null;
    FileReader fileReader = null;
    BufferedReader bufferedReader = null;
    String line = null;
    try {
      File file = new File(CSV_FILE_NAME);
      fileReader = new FileReader(file);
      bufferedReader = new BufferedReader(fileReader);
    } catch (IOException io) {
      System.out.println("CSV writer init problem.");
      io.printStackTrace();
    }
    try{
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
          " step INTEGER not NULL," +
          " PRIMARY KEY ( id ))";
      stmt.executeUpdate(sql);
      System.out.println("Created table in given database...");
  
      while ((line = bufferedReader.readLine()) != null) {
        String[] record = line.split("\\s+");
        int id = Integer.parseInt(record[0]);
        int steps = Integer.parseInt(record[1]);
        String insert = String.format("INSERT INTO StepRecord VALUES (%d, %d)", id, steps);
        System.out.println(insert);
        stmt.execute(insert);
      }
      
    }catch(SQLException se){
      //Handle errors for JDBC
      se.printStackTrace();
    }catch(Exception e){
      //Handle errors for Class.forName
      e.printStackTrace();
    }finally{
      //finally block used to close resources
      try{
        if(stmt!=null)
          conn.close();
      }catch(SQLException se){
      }// do nothing
      try{
        if(conn!=null)
          conn.close();
      }catch(SQLException se){
        se.printStackTrace();
      }//end finally try
    }//end try
    System.out.println("Goodbye!");
  }//end main
}//end JDBCExample