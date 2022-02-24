package UserData;

import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;

public class MySQLManager {
	static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";  
	static final String DB_URL = "jdbc:mysql://localhost:3306/chess?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
	static final String USER="root";
	static final String PASS="12345";
	public static Connection conn=null;
	public static Connection getConnection() {
		
		try {
			Class.forName(JDBC_DRIVER);
			conn=DriverManager.getConnection(DB_URL,USER,PASS);
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
	public static void close(Connection conn) {
		if(conn!=null) {
			try {
				conn.close();
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static  List<users> findAll(){
		List<users>list=new ArrayList<users>();
		try {
			String sql="select *from userdata";
			PreparedStatement ps=conn.prepareStatement(sql);
			ResultSet rs=ps.executeQuery();
			while(rs.next()) {
				String name=rs.getString("name");
				String account=rs.getString("account");
				String password=rs.getString("password");
				users use=new users(name,account,password);
				list.add(use);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	public static boolean add(String name,String account,String password) {
		users use=new users(name,account,password);
		try {
			String sql="insert into userdata(name,account,password) values(?,?,?)";
			PreparedStatement ps=conn.prepareStatement(sql);
			ps.setString(1,use.getName());
			ps.setString(2,use.getAccount());
			ps.setString(3,use.getPassword());
			boolean rel=ps.execute();
			return true;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	public static String verify(String account,String password) {
		List<users>list=findAll();
		int flag=0;String name="";String x="";
		for(int i=0;i<list.size();i++) {
			users xx=list.get(i);
			if(account.equals(xx.account)) {
				if(password.equals(xx.password))
				{
					flag=1;name=xx.name;break;
				}
				else break;
			}
		}
		if(flag==1)return name;
		return x;
	}
	public static boolean isExist(String name,String account) {
		List<users>list=findAll();
		int flag=0;
		for(int i=0;i<list.size();i++) {
			users xx=list.get(i);
			if(xx.name.equals(name)||xx.account.equals(account))
			{
				flag=1;break;
			}
		}
		if(flag==1)return true;
		return false;
	}
}
