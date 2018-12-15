import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Data implements Runnable {
  // JDBC driver name and database URL
  static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
  
  static final String DB_URL = "jdbc:mysql://db-tao.chptyir9bzg1.us-west-2.rds.amazonaws.com:3306/db_example";
  
  
  
  //  Database credentials
  static final String USER = "root";
  static final String PASS = "12345678";
  
  
  private static int DAYS = 30;
  private static int USER_POPULATION = 3000;
  private static int TIMES = USER_POPULATION * DAYS * 4;
  private static String COMMA = ",";
  private static String NEW_LINE = "\n";
  
  private Random random;
  private Statement stmt;
  private FileWriter fileWriter;
  private Connection conn;
  
  public Data(FileWriter fileWriter) {
    random = new Random();
    this.fileWriter = fileWriter;
    this.conn = null;
    this.stmt = null;
  }
  
  public void GenerateData() {
    for (int i = 0; i < TIMES; i++) {
      GenerateSingleStatement();
    }
  }
  
  private void GenerateSingleStatement() {
    int userId = random.nextInt(USER_POPULATION) + 1;
    int day = random.nextInt(DAYS) + 1;
    int step = random.nextInt(60) + 20;
    String insertStatement = String.format("INSERT INTO StepRecord VALUES (%d, %d, %d)", userId, day, step);
    String stepInfo = userId + COMMA + day + COMMA + step + NEW_LINE;
    try {
      stmt.execute(insertStatement);
      fileWriter.append(stepInfo);
    } catch (SQLException se) {
      se.printStackTrace();
      System.out.println("Insert Failure.");
    } catch (IOException io) {
      io.printStackTrace();
      System.out.println("Writer Error.");
    }
  }
  
  public void run() {
    try{
      //STEP 2: Register JDBC driver
      Class.forName(JDBC_DRIVER);
    
      //STEP 3: Open a connection
      System.out.println("Connecting to a selected database...");
      conn = DriverManager.getConnection(DB_URL, USER, PASS);
      System.out.println("Connected database successfully...");
      stmt = conn.createStatement();
    }catch(SQLException se){
      //Handle errors for JDBC
      se.printStackTrace();
    }catch(Exception e) {
      //Handle errors for Class.forName
      e.printStackTrace();
    }
    for (int i = 0; i < TIMES / 50; i++) {
      GenerateSingleStatement();
    }
  }
}
