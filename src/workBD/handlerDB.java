package workBD;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class handlerDB {

	/*
	 * 
	 * ������� ������ � ����� ������
	 * 
	 */
	
	static String DB_NAME 		= "egais";
	
	public static ResultSet getResult(String sql) {
		
		try {
			/*
			 * ������������� ������� , ���������� ��������� ������� @sql
			 */
			try {
				
				Class.forName("org.sqlite.JDBC");
				
			} 
			
			catch (ClassNotFoundException ex) {
				
				System.out.println("������ ������������� ��������");
				
				Logger.getLogger(handlerDB.class.getName()).log(Level.SEVERE, null, ex);
			}
			
			Connection bd;
			
			try {
				
				bd = DriverManager.getConnection("jdbc:sqlite:"+DB_NAME+".db");
				
			} catch (SQLException ex) {
				
				System.out.println("������ ������������� ���� ������");
				bd = null;
				
			}
			//ResultSet resultSet = null;
			Statement st;
			
			try {
				
				st = bd.createStatement();
				
			} catch (SQLException ex) {
				
				st = null;
				System.out.println("������ ������������� ��������� ����");
				
			}
			
			st.setQueryTimeout(60);
			return st.executeQuery( sql );
			
		} catch (SQLException ex) {
			
			System.out.println("������ ���������� ������� � ��");
			return null;
			
		}
		
	}  //static ResultSet getResult(String sql) 
	
	static public void executeSQL(String sq) throws ClassNotFoundException, SQLException {
		
        Class.forName("org.sqlite.JDBC");
        Connection bd = DriverManager.getConnection("jdbc:sqlite:"+DB_NAME+".db");
        Statement st = bd.createStatement();
        st.execute("BEGIN TRANSACTION;"); ////***��� ����� (������ ����������)
        st.setQueryTimeout(60);
        st.execute(sq);
        st.execute("COMMIT;");
        st.close();
        
  	}//static public void executeSQL(String sq)
	
	static void recordData(String sq) throws SQLException, ClassNotFoundException {
        /*
         ������ ������ � ����
         */
        Class.forName("org.sqlite.JDBC");
        Connection bd = DriverManager.getConnection("jdbc:sqlite:"+DB_NAME+".db");
        
        try (Statement st = bd.createStatement()) {
        	
            st.setQueryTimeout(60);
            st.execute(sq);
            
        }
        
    }//static void recordData(String sq) throws SQLException, ClassNotFoundException
		
	static String getTime(String s){
		String[] x = s.split(" ");
		return x[1]; 
	}//static String getTme(String s)
    
	public static void main(String[] args) {
		

	}

}
