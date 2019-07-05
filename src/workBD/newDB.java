package workBD;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class newDB {

	/*
	 * 
	 * генерация новой базы данных и таблиц , если оно не существует
	 * 
	 */
	
	newDB() throws ClassNotFoundException, SQLException {
		 
		createTables();
		clearTables();
 
	}//newDb()
	
	static String DB_NAME 		= "egais";
	 
	static String TBL_historyUTM = 
			"CREATE TABLE IF NOT EXISTS historyUTM(ID INTEGER PRIMARY KEY AUTOINCREMENT,a1 TEXT,a2 TEXT ,data DATETIME);";
	
	
	static String TBL_DocTicket = "CREATE TABLE IF NOT EXISTS docTicket(ID INTEGER PRIMARY KEY AUTOINCREMENT,"+
			"transportID TEXT,"+
			"regID TEXT ,"+
			"docType TEXT ,"+
			"resConcl TEXT ,"+
			"resComment TEXT ,"+
			"operName TEXT ,"+
			"operResult TEXT ,"+
			"operComment TEXT ,"+
			"tickDate DATETIME ,"+
			"resDate DATETIME ,"+
			"operDate DATETIME ,"+
			"identity TEXT,"+
			"docId TEXT,"+
			"docHash TEXT,"+
			"xmlStr TEXT );";
	
 	public static boolean isWrite = false;
	public static boolean isRead = false;
 
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//	описание:
	//		создадим таблицы в базе , если их нет.
	static public void createTables() throws SQLException, ClassNotFoundException {
 
		isWrite = true;
		Class.forName("org.sqlite.JDBC");
		Connection bd = DriverManager.getConnection("jdbc:sqlite:"+DB_NAME+".db");
		Statement st  = bd.createStatement();
		st.setQueryTimeout(60);
 
		st.execute(TBL_historyUTM);
		st.execute(TBL_DocTicket);
		 
		st.close();
		bd.close();
		isWrite = false;
 
	}//static public void createTables() throws SQLException, ClassNotFoundException
 
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//	описание:
	//		создадим таблицы в базе , если их нет.
	static public void clearTables() throws SQLException, ClassNotFoundException {
 
		isWrite = true;
		Class.forName("org.sqlite.JDBC");
		Connection bd = DriverManager.getConnection("jdbc:sqlite:"+DB_NAME+".db");
		Statement st  = bd.createStatement();
		st.setQueryTimeout(60);
 
		st.execute("delete from historyUTM;");
		st.execute("delete from docTicket;");
		st.close();
		bd.close();
		isWrite = false;
 
	}//static public void clearTables() throws SQLException, ClassNotFoundException 
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		
		//createTables();
		
	}//main

}//newDB
