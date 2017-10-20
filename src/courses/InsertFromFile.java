package courses;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class InsertFromFile {
  private static Connection connect = null;
  private Statement statement = null;
  private PreparedStatement preparedStatement = null;
  private ResultSet resultSet = null;
  private static ArrayList<String> codeList = new ArrayList<String>();
  private static ArrayList<String> nameList = new ArrayList<String>();
  private static ArrayList<String> descripList = new ArrayList<String>();
  private static ArrayList<String> departList = new ArrayList<String>();
  private static ArrayList<String> dayList = new ArrayList<String>();
  private static ArrayList<ArrayList<Object>> course = new ArrayList<>();

  public void readDataBase(File file) throws Exception {
    try {
      Class.forName("com.mysql.jdbc.Driver");
      connect = DriverManager
    		//Make sure to enter correct location and username/password
    		  //This means user "root" is logging in with password "null" 
    		  //on the "localhost" server, database "dev"
          .getConnection("jdbc:mysql://localhost/dev?"
              + "user=root&password=null");
      statement = connect.createStatement();
      resultSet = statement
          .executeQuery("select * from courses");
     
      openFile(file);

      preparedStatement = connect
          .prepareStatement("SELECT code, name, description, department_id, meeting_days from courses");
      resultSet = preparedStatement.executeQuery();
      //writeResultSet(resultSet);
      
    } catch (Exception e) {
      throw e;
    } finally {
      close();
    }

  }
  public static void openFile(File file) throws SQLException
  {
	  Object[] lineList = null;
	  {
		  try
		  {
				BufferedReader br = new BufferedReader
						(new FileReader(file));
				lineList = br.lines().toArray();
				br.close();
				//System.out.println(lineList[0]);
				for(int k = 0; k < lineList.length; k++)
				{
					PreparedStatement preparation = null;
					Statement statement2 = null;
					ResultSet resultSet2 = null;
					statement2 = connect.createStatement();
					preparation = connect
					          .prepareStatement("insert into  courses values (default, ?, ?, ?, ? , ?)");
					String[] line = lineList[k].toString().split(", ");
					preparation.setString(1, line[0]);
					codeList.add(line[0]);
					preparation.setString(2, line[1]);
					nameList.add(line[1]);
					preparation.setString(3, line[2]);
					descripList.add(line[2]);
					resultSet2 = statement2.executeQuery("SELECT id FROM departments WHERE name='"+ line[3] + "'");
				    int departId = getId(resultSet2);
					preparation.setInt(4, departId);
					departList.add(line[3]);
					preparation.setInt(5, Integer.parseInt(line[4]));
					dayList.add(line[4]);
					preparation.executeUpdate();
					ArrayList<Object> courseThing = new ArrayList<>();
					courseThing.add(line[0]);
					courseThing.add(line[1]);
					courseThing.add(line[2]);
					courseThing.add(line[3]);
					courseThing.add(line[4]);
					course.add(courseThing);
				}
			}
			catch(IOException ex)
			{
				System.out.println("you die now");
			}
		}
  }
  private static int getId(ResultSet resultSet) throws SQLException {
		while (resultSet.next()){
			  int id = resultSet.getInt("id");
			  return id;
		  }
		return 1;
	  }
  
  private String getName(ResultSet resultSet) throws SQLException {
	  while (resultSet.next()){
		  String name = resultSet.getString("name");
		  return name;
	  }
	return null;
  }
  public ArrayList<ArrayList<Object>> getCourse(){
	  return course;
  }
  
private void writeResultSet(ResultSet resultSet) throws SQLException {
    // ResultSet is initially before the first data set
    while (resultSet.next()) {
      // It is possible to get the columns via name
      // also possible to get the columns via the column number
      // which starts at 1
      // e.g. resultSet.getString(2);
      String code = resultSet.getString("code");
      String name = resultSet.getString("name");
      String descript = resultSet.getString("description");
      int depart_id = resultSet.getInt("department_id");
      ResultSet rs = statement.executeQuery("SELECT name FROM departments WHERE id="+depart_id);
      String depart = getName(rs);
      int meeting_days = resultSet.getInt("meeting_days");
      System.out.println("Code: " + code);
      System.out.println("Name: " + name);
      System.out.println("Description: " + descript);
      System.out.println("Department: " + depart);
      System.out.println("Meeting Days: " + meeting_days);
      System.out.println("--------------------------------------------------");
    }
  }
  
  // You need to close the resultSet
  private void close() {
    try {
      if (resultSet != null) {
        resultSet.close();
      }

      if (statement != null) {
        statement.close();
      }

      if (connect != null) {
        connect.close();
      }
    } catch (Exception e) {

    }
  }

} 