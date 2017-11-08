package jsf.career.planner;

import java.sql.*;

public class DbConnect {
	// JDBC driver name and database URL
	public static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	public static final String DB_URL = "jdbc:mysql://localhost:3306/careeradvising";
	public static String sql;

	// Database credentials
	static final String USER = "root";
	static final String PASS = "root";

	Connection conn = null;
	Statement stmt = null;

	void DbOperate(String PreparedStatement, String mode, String TableName, String Database) {
		try {
			// STEP 2: Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");

			// STEP 3: Open a connection
			//System.out.println("Connecting to a selected database...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			//System.out.println("Connected database successfully...");

			// STEP 4: Execute a query
			//System.out.println("Creating statement...");
			stmt = conn.createStatement();
			switch (mode) {
			case "I":
				sql = "INSERT INTO " + TableName + " VALUES (" + PreparedStatement + ")";
				break;
			case "U":
				break;
			default:
				break;
			}
//System.out.println(".................... "+sql+" .....................");
			stmt.executeUpdate(sql);

		} catch (SQLException se)

		{
			// Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e)

		{
			// Handle errors for Class.forName
			e.printStackTrace();
		}
		finally {
			// finally block used to close resources
			try {
				if (stmt != null)
					conn.close();
			} catch (SQLException se) {
			} // do nothing
			try {
				if (conn != null)
					conn.close();
			} 
			catch (SQLException se) {
			}
	}
	}
}