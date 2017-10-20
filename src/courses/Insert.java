package courses;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

public class Insert {
  private Connection connect = null;
  private Statement statement = null;
  private PreparedStatement preparedStatement = null;
  private ResultSet resultSet = null;

  public void readDataBase() throws Exception {
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
      //writeResultSet(resultSet);
      preparedStatement = connect
          .prepareStatement("insert into  courses values (default, ?, ?, ?, ? , ?)");
      // Parameters start with 1
      String code = JOptionPane.showInputDialog("Enter Course Code");
      preparedStatement.setString(1, code);
      preparedStatement.setString(2, JOptionPane.showInputDialog("Enter Course Name"));
      preparedStatement.setString(3, JOptionPane.showInputDialog("Enter Course Description"));
      resultSet = statement.executeQuery("SELECT id FROM departments WHERE name='"+ JOptionPane.showInputDialog("Enter Course Department") +"'");
      int departId = getId(resultSet);
      preparedStatement.setInt(4, departId);
      preparedStatement.setInt(5, Integer.parseInt(JOptionPane.showInputDialog("Enter Number of Meeting Days")));
      preparedStatement.executeUpdate();

      preparedStatement = connect
          .prepareStatement("SELECT code, name, description, department_id, meeting_days from courses");
      resultSet = preparedStatement.executeQuery();
      writeResultSet(resultSet);
      
      //preparedStatement = connect
      //.prepareStatement("delete from courses where code=? ; ");
      //preparedStatement.setString(1, code);
      //preparedStatement.executeUpdate();
      
    } catch (Exception e) {
      throw e;
    } finally {
      close();
    }

  }

  private int getId(ResultSet resultSet) throws SQLException {
		while (resultSet.next()){
			  int id = resultSet.getInt("id");
			  return id;
		  }
		return 1;
	  }
  
public static String getName(ResultSet resultSet) throws SQLException {
	  while (resultSet.next()){
		  String name = resultSet.getString("name");
		  return name;
	  }
	return null;
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