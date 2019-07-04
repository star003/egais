package monitor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Properties;

import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class UTMHost {
	
	static String UTM_HOST 	= "http://127.0.0.1:8080";
	static String DB_NAME 	= "egais";
	
    class HostElement {
    	
    	int recno;
    	String url;
    	String replyID;
    	String fileID;
        
    } //HostElement
    

    class SortHosts implements Comparator<HostElement> {

        @Override
        public int compare(HostElement o1, HostElement o2) {

            return (o2.recno - o1.recno);
            
        } //compare
        
    } //SortHosts
    
    public URL url;
    private String host;

    UTMHost(String string) {

        host = string;
        try {
        	
            url = new URL(host);
            
        } catch (MalformedURLException e) {
        	
            e.printStackTrace();
            
        }
        
    } //UTMHost
    
    public ArrayList<HostElement> GetListDocs() {

        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        Document document;
        URL docUrl;
        ArrayList<HostElement> result = new ArrayList<HostElement>();
        HttpURLConnection lconn;
        
        try {

            lconn = (HttpURLConnection)url.openConnection();
            lconn.setRequestMethod("GET");
            builder = builderFactory.newDocumentBuilder();
            document = builder.parse(lconn.getInputStream());
            
            if (document.hasChildNodes()) {
            	
                NodeList nl = document.getElementsByTagName("url");

                for (int i = 0; i < nl.getLength(); i++) {
                	
                    HostElement he = new HostElement();
                    he.url = nl.item(i).getTextContent();
                    docUrl = new URL(he.url);

                    String[] str = docUrl.getPath().split("/");
                    he.recno = Integer.valueOf(str[str.length - 1]);
                    he.replyID = ((Element)nl.item(i)).getAttribute("replyId");
                    result.add(he);
                    
                    //monitorFunctions.addHistoryUTM(he.url, he.replyID);
                    
                }
                
            }

        } catch (IOException e) {
        	
            e.printStackTrace();
            
        } catch (ParserConfigurationException e) {
        	
            e.printStackTrace();
            
        } catch (SAXException e) {
        	
            e.printStackTrace();
            
        }
        
        Collections.sort(result, new SortHosts());
        
        return result;
        
    } //GetListDocs
    
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
  	
  	public static void addHistoryUtmFromList(ArrayList<HostElement> l) throws SQLException {
  		
  		String a1 = "",a2 = "";
  		//System.out.print("m");
  		Connection bd;
  		Statement st;
  		ResultSet rs = null;
  		
  		try {
  		
  			Class.forName("org.sqlite.JDBC");
  			bd 		= DriverManager.getConnection("jdbc:sqlite:"+DB_NAME+".db");
  			
  			boolean isExist = false;
  			
  			for (int i = 0; i < l.size(); i++) {
  				
  				a1 = (String)l.get(i).url;
  				a2 = (String)l.get(i).replyID;
  				
  				String sqEx = "SELECT COUNT(id) as c FROM historyUTM WHERE a1 ='"+a1+"' and a2='"+a2+"';";
  				st 		= bd.createStatement();
  				rs 		= st.executeQuery(sqEx);
  				
  				while (rs.next()) {
  						
  					if (rs.getInt("c") ==0 ) {
  						
  						isExist = true;
  						break;
  												
  					}
  	
  				}//while
  				
  				st.close();
  				
  				if (!isExist) {
  					
  					//***нет записи - пишем
  					
  					long lDateTime = new Date().getTime();
  					String sqAdd = "INSERT INTO historyUTM (a1,a2,data) VALUES ('"+a1+"','"+a2+"',"+String.valueOf(lDateTime)+");";
  					st 	= bd.createStatement();
  			        st.execute("BEGIN TRANSACTION;"); ////***это важно (начать транзакцию)
  			        st.setQueryTimeout(60);
  			        st.execute(sqAdd);
  			        st.execute("COMMIT;");
  			        st.close();
  					
  				}
  	            
  			}//for
  			
  			bd.close();
  			
  		}//try
  		
  		catch (ClassNotFoundException | SQLException e1) {
  			
  			
  			e1.printStackTrace();
  			
  		}
  		
  	}	//addHistoryUtmFromList
  	
  //*********************************************************************************************************************
  	
  	public static void monitorUTM() {
  		
  		try {
  			
			addHistoryUtmFromList(getUtmDocument("out"));
			
		} catch (SQLException e) {
			
			System.out.println("ошибка записи истории появления документов в утм");
			
		}
  		
  	} //monitorUTM
  
  //*********************************************************************************************************************
  	
  	public static DefaultTableModel forTables(DefaultTableModel tableModel,String rj) {
  		
  		ArrayList<HostElement> p = UTMHost.getUtmDocument(rj);
		
		for (int i = 0; i < p.size(); i++) {
			
			String[] myArray = new String[4];
			myArray[0] =String.valueOf((p.get(i).recno)); 
			myArray[1] =(String)p.get(i).url;
			myArray[2] =(String)p.get(i).replyID;
			myArray[3] ="-";
			tableModel.addRow(myArray);
			
        }
		
		return tableModel;
  		
  	}//forTables
  	
} //UTMHost
