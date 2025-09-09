import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
 
public class ConnectingToDBExample {
 
	public static void main(String[] args) throws SQLException, ClassNotFoundException {
		
		//Loading the required JDBC Driver class
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");	
		
		//Creating a connection to the database
		Connection conn = DriverManager.getConnection("jdbc:sqlserver://192.9.200.52:1433;databaseName=VST_DMS","sa","VST.2022#");
//		Connection conn = DriverManager.getConnection("jdbc:sqlserver://10.12.133.3:21443;databaseName=ecatalog","ecatalog","V#$379klq268");
		
		//Executing SQL query and fetching the result
		Statement st = conn.createStatement();
		String sqlStr = "select * from adm_user";
		ResultSet rs = st.executeQuery(sqlStr);
		while (rs.next()) {
			System.out.println(rs.getString("UserCode"));
		}		
	}
}