package courses;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Delete {
  private Connection connect = null;
  private Statement statement = null;
  private PreparedStatement preparedStatement = null;
  private ResultSet resultSet = null;

  public void readDataBase() throws Exception {
    try {
    	//Connect and setup statement
      Class.forName("com.mysql.jdbc.Driver");
      connect = DriverManager
    		//Make sure to enter correct location and username/password
    		  //This means user "root" is logging in with password "null" 
    		  //on the "localhost" server, database "dev"
          .getConnection("jdbc:mysql://localhost/dev?"
              + "user=root&password=null");
      statement = connect.createStatement();
      //Get the course name of the selected row
      Object name = CatalogTable.table.getModel().getValueAt(CatalogTable.table.getSelectedRow(), 1);
      //ResultSet rs = statement.executeQuery("SELECT id FROM courses WHERE name='"+name.toString()+"'");
      //int courseId = getId(rs);
      /*delete from instructor_course
      preparedStatement = connect.prepareStatement("delete from instructor_course where course_id=?");
      preparedStatement.setInt(1, courseId);
      preparedStatement.executeUpdate();
      //delete from trimester_course
      preparedStatement = connect.prepareStatement("delete from trimester_course where course_id=?");
      preparedStatement.setInt(1, courseId);
      preparedStatement.executeUpdate();
      */
      //delete from courses
      preparedStatement = connect.prepareStatement("delete from courses where name=?");
      preparedStatement.setString(1, name.toString());
      preparedStatement.executeUpdate();
      //write new course list
      preparedStatement = connect
          .prepareStatement("SELECT code, name, description, department_id, meeting_days from courses");
      resultSet = preparedStatement.executeQuery();
      writeResultSet(resultSet);
      
    } catch (Exception e) {
      throw e;
    } finally {
      close();
    }

  }
  
  private void writeResultSet(ResultSet resultSet) throws SQLException {
    // ResultSet is initially before the first data set
    while (resultSet.next()) {
      // It is possible to get the columns via name
      // also possible to get the columns via the column number
      // which starts at 1
      // e.g. resultSet.getSTring(2);
      String code = resultSet.getString("code");
      String name = resultSet.getString("name");
      String descript = resultSet.getString("description");
      int depart_id = resultSet.getInt("department_id");
      //TODO make a getFromResultSet with the following 2 lines
      //ResultSet rs = statement.executeQuery("SELECT name FROM departments WHERE id="+depart_id);
      //String depart = getName(rs);
      int meeting_days = resultSet.getInt("meeting_days");
      System.out.println("Code: " + code);
      System.out.println("Name: " + name);
      System.out.println("Description: " + descript);
      System.out.println("Department Id: " + depart_id);
      System.out.println("Meeting Days: " + meeting_days);
      System.out.println("--------------------------------------------------");
    }
  }

  private int getId(ResultSet resultSet) throws SQLException {
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
  public void deleteAll() throws Exception {
	    try {
	      Class.forName("com.mysql.jdbc.Driver");
	      connect = DriverManager
	          .getConnection("jdbc:mysql://localhost/dev?"
	              + "user=root&password=Attack0n3vaChu");
	      statement = connect.createStatement();
	      //write current course list
	      //ResultSet curr = statement
	      //    .executeQuery("select * from courses");
	      //TODO make a getResultSet
	      //writeResultSet(curr);
	      //delete from instructor_course
	      //preparedStatement = connect.prepareStatement("delete from instructor_course");
	      //preparedStatement.executeUpdate();
	      //delete from trimester_course
	      //preparedStatement = connect.prepareStatement("delete from trimester_course");
	      //preparedStatement.executeUpdate();
	      //delete from courses
	      preparedStatement = connect.prepareStatement("delete from courses");
	      preparedStatement.executeUpdate();
	      preparedStatement = connect
	              .prepareStatement("SELECT code, name, description, department_id, meeting_days from courses");
	          resultSet = preparedStatement.executeQuery();
	          writeResultSet(resultSet);
	      
	    } catch (Exception e) {
	      throw e;
	    } finally {
	      close();
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