package courses;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

/*The entire purpose of this class is the getData() method
 * This retrieves the information from the existing MySQL database, returning Object[][],
 * which houses the information used to build a JTable. */
public class MakeTable {
	//MySQL connection & execution variables
	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	//Object Lists
	private static ArrayList<String> codeList = new ArrayList<String>();
	private static ArrayList<String> nameList = new ArrayList<String>();
	private static ArrayList<String> descripList = new ArrayList<String>();
	private static ArrayList<String> departList = new ArrayList<String>();
	private static ArrayList<String> dayList = new ArrayList<String>();
	private static ArrayList<ArrayList<Object>> course = new ArrayList<>();
	
	public Object[][] getData() throws Exception
	  {
		try {
			//Connection to MySQL
			Class.forName("com.mysql.jdbc.Driver");
	      connect = DriverManager
	    		//Make sure to enter correct location and username/password
	    		  //This means user "root" is logging in with password "null" 
	    		  //on the "localhost" server, database "dev"
	          .getConnection("jdbc:mysql://localhost/dev?"
	              + "user=root&password=null");
	      //Create default statement
	      statement = connect.createStatement();
	      //Select relevant information from Courses table
	      preparedStatement = connect
	              .prepareStatement("SELECT code, name, description, department_id, meeting_days from courses");
	          resultSet = preparedStatement.executeQuery();
	      //Iterate through all entries in the database, stores info in ArrayList
	      while(resultSet.next() )
	      {
	    	  String code = resultSet.getString("code");
	    	  String name = resultSet.getString("name");
	          String descript = resultSet.getString("description");
	          int depart_id = resultSet.getInt("department_id");
	          ResultSet rs = statement.executeQuery("SELECT name FROM departments WHERE id="+depart_id);
	          String depart = Insert.getName(rs);
	          int meeting_days = resultSet.getInt("meeting_days");
	          codeList.add(code);
	          nameList.add(name);
	          descripList.add(descript);
	          departList.add(depart);
	          dayList.add("" + meeting_days);
	          ArrayList<Object> courseThing = new ArrayList<>();
	          courseThing.add(code);
	          courseThing.add(name);
	          courseThing.add(descript);
	          courseThing.add(depart);
	          courseThing.add(meeting_days);
	          course.add(courseThing);
	      }
	      //Change ArrayList to Array
	      Object[][] a = new Object[course.size()][];
          for(int n = 0; n < course.size(); n++)
          {
          	ArrayList<Object> blech = course.get(n);
          	a[n] = blech.toArray(new Object[blech.size()]);
          }
	      return a;
		}
		catch (Exception e) {
	      throw e;
		}
	  }
}