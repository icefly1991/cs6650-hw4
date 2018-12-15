import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SalaryDeductionReporter {
  
  static String server = "http://localhost:8080/myapp/user/";
//  static String server = "http://ec2-34-220-36-237.us-west-2.compute.amazonaws.com:8080/server-hw4/myresource/";
  private static final String CSV_FILE_NAME = "salary report.csv";
  static int USER_POPULATION = 3000;
  
  
  
  public static void main(String[] args) {
    FileWriter fileWriter = null;
    List<Double> latencies = new ArrayList<Double>();
    try {
      fileWriter = new FileWriter(CSV_FILE_NAME);
      fileWriter.append("user_id,steps,salary_deduction\n");
    } catch (IOException io) {
      System.out.println("CSV writer init problem.");
      io.printStackTrace();
    }
    System.out.println("Salary Deduction Report is generating...");
//    System.out.println("This is testing sql data without hadoop treatment");
    System.out.println("This is testing sql data with hadoop treatment");
    double start = System.currentTimeMillis();
    System.out.println("Current time is " + start);
    for (int i = 1; i <= USER_POPULATION; i++) {
      double startPoint = System.currentTimeMillis();
      Query query = new Query(server + i);
      int step = query.getSteps();
      double endPoint = System.currentTimeMillis();
      latencies.add(endPoint - startPoint);
      double deduction = step >= 6000 ? 0 : (6000 - step) * 0.01;
      String record = String.format("%d,%d,%.2f\n", i, step, deduction);
      try {
        fileWriter.append(record);
      } catch (IOException io) {
        System.out.println("CSV writer init problem.");
        io.printStackTrace();
      }
//      System.out.println(record);
    }
    double end = System.currentTimeMillis();
    System.out.println("Current time is " + end);
    double latencySum = 0;
    for (double lat: latencies) {
      latencySum += lat;
    }
    try {
      fileWriter.flush();
      fileWriter.close();
    } catch (IOException io) {
      System.out.println("CSV writer init problem.");
      io.printStackTrace();
    }
//    System.out.println("Latency mean is " + latencySum / latencies.size());
//    System.out.println("Latency median is " + latencies.get(latencies.size() / 2));
//    System.out.println("Latency 95% is " + latencies.get((int)(latencies.size() % 0.95)));
    System.out.println("In total, generating the report takes " + (end - start) / 1000
        + " seconds for " + USER_POPULATION + " employees");
  }
}
