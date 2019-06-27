package monitor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import workBD.handlerDB;

public class monitorFunctions {
	
	/*
	 * класс работы с монитором очереди. Тут разместим большинство функций
	 * 
	 */
	
	//*********************************************************************************************************************
	static String UTM_HOST 	= "http://127.0.0.1:8080";
	static String DB_NAME 	= "egais";
	
	//*********************************************************************************************************************
	public static ArrayList<UTMHost.HostElement> getUtmDocument(String rj) {
		
		Properties props = new Properties();
		
		try {
			
			String flName = System.getProperty("user.dir")+"\\EgaisUTM.ini";
            props.load(new FileInputStream(new File(flName)));
            UTM_HOST = props.getProperty("UTM_HOST", "http://localhost:8080/opt");
            
        } catch (IOException e) {
        	
            e.printStackTrace();
            
        }
		
		UTMHost uhost;
		
		String stRj = "";
		
		if (rj.equals("in")) {
			
			stRj = "/opt/in?refresh=true";
			
		}
		
		if (rj.equals("out")) {
			
			stRj = "/opt/out?refresh=true";
			
		}
		
        uhost 	= new UTMHost(UTM_HOST + stRj);
        return uhost.GetListDocs();
				
	} //getUtmDocument
	
	//*********************************************************************************************************************
	static boolean histryExist1(String a1,String a2) {
		
		/*
		 * на вход принимаем строку условий , на выходе 
		 * есть ли оно в базе ?
		 */
		String sq = "SELECT COUNT(id) as c FROM historyUTM WHERE a1 =? and a2=? ;";
		
		
		try {
			
			Class.forName("org.sqlite.JDBC");
			Connection bd = DriverManager.getConnection("jdbc:sqlite:"+DB_NAME+".db");
			PreparedStatement stm = bd.prepareStatement(sq);
			stm.setString(1, a1);
			stm.setString(2, a2);
			
			ResultSet rs = stm.executeQuery();
			
			while (rs.next()) {
				
				if (rs.getInt("c") ==0 ) {
					
					rs.close();
					stm.close();
					return false;
					
				}
			    
			}
			
			rs.close();
			stm.close();
			return true;
		} 
		
		catch (ClassNotFoundException | SQLException ex) {
			
			System.out.println("ошибка инициализации драйвера");
						
		}
		
		return true;
		
	} //histryExist1
	
	static boolean histryExist(String a1,String a2) {
		
		/*
		 * на вход принимаем строку условий , на выходе 
		 * есть ли оно в базе ?
		 */
		String sq = "SELECT COUNT(id) as c FROM historyUTM WHERE a1 ='"+a1+"' and a2='"+a2+"';";
		
		Connection bd;
		Statement st;
		ResultSet rs = null;
		
		try {
			
			Class.forName("org.sqlite.JDBC");
			bd 		= DriverManager.getConnection("jdbc:sqlite:"+DB_NAME+".db");
			st 		= bd.createStatement();
			rs 		= st.executeQuery(sq);
			
			while (rs.next()) {
				
				if (rs.getInt("c") ==0 ) {
					
					st.close();
					return false;
					
				}
			    
			}
			
			st.close();
			return true;
		} 
		
		catch (ClassNotFoundException | SQLException ex) {
			
			System.out.println("ошибка инициализации драйвера");
						
		}
		
		return true;
		
	} //histryExist
	
	//*********************************************************************************************************************
	public static void addHistoryUTM(String a1,String a2) {
	
		/*
		 * Запишем хронологию появления ответов в утм
		 */
		if(!histryExist1(a1,a2)) {
			
			long lDateTime = new Date().getTime();
			String sq = "INSERT INTO historyUTM (a1,a2,data) VALUES ('"+a1+"','"+a2+"',"+String.valueOf(lDateTime)+");";
			
			try {
				
				handlerDB.executeSQL(sq);
				
			} catch (ClassNotFoundException | SQLException e1) {
				
				e1.printStackTrace();
				
			}
			
		}
		
	} //addHistoryUTM
	
	//*********************************************************************************************************************
	public static void main(String[] args) {
		
		ArrayList<UTMHost.HostElement> docList = getUtmDocument("out");
		
        for (int i = 0; i < docList.size(); i++) {
            
            System.out.print((String)docList.get(i).url);
            System.out.print(" ");
            System.out.println((String)docList.get(i).replyID);
            addHistoryUTM((String)docList.get(i).url,(String)docList.get(i).replyID);
                            
        }
		
	} //main

} //monitorFunctions
