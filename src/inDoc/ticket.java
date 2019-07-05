package inDoc;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.w3c.dom.Node;
import java.text.DateFormat;

public class ticket {
	
	static String DB_NAME 	= "egais";
	
	Document document;
    DocumentBuilderFactory builderFactory;
    DocumentBuilder builder = null;
    
    DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
    
    private String 	transportID;
    private String 	regID;
    private String 	docType;
    private String 	resConcl;
    private String 	resComment;
    private String 	operName;
    private String 	operResult;
    private String 	operComment;
    private Date 	tickDate;
    private Date 	resDate;
    private Date 	operDate;
    private String  identity;
    private String  docId;
    private String 	docHash;
    private String 	xmlStr;
    
    private String 	adr1;
    
    
	public ticket(String adr) {
				
		HttpURLConnection lconn = null;
		URL url = null;
		InputStream is;
		
		adr1 = adr;
		
		try {
			
			url 	= new URL(adr);
			lconn 	= (HttpURLConnection)url.openConnection();
			lconn.setRequestMethod("GET");
			
		} catch (ProtocolException | MalformedURLException  e1) {
			
			System.out.println("Ошибка загрузки тикета (ProtocolException | MalformedURLException) "+adr);
			return;
			
		} catch (IOException e) {
			
			System.out.println("Ошибка загрузки тикета (IOException)"+adr);
			return;
			
		}
		
		try {
	
			DocumentBuilderFactory dbf  = DocumentBuilderFactory.newInstance();
            DocumentBuilder db          = dbf.newDocumentBuilder();
            is			 				= lconn.getInputStream();
           
            Document doc                = db.parse(new InputSource(is));
            doc.getDocumentElement().normalize();
            NodeList nodeList 			= doc.getElementsByTagName("ns:Ticket");
            NodeList nl 				= doc.getChildNodes();
            String content;
            
            for (int z=0 ; z<nl.getLength();z++) {
            	
                Node node 	= nodeList.item(z);
                NodeList n1 = node.getChildNodes();
                
                for (int z1 =0 ; z1<n1.getLength();z1++) {
                	
                	String pp1 = n1.item(z1).getNodeName();
                	
                	if (pp1.equals("#text")) {
                		continue;
                	}

                	if (pp1.equals("tc:TicketDate")) {
                		System.out.println(n1.item(z1).getTextContent());
                		content = n1.item(z1).getTextContent();
                        try {
                            tickDate = format.parse(content);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                	}
                	
                	if (pp1.equals("tc:Identity")) {
                		identity = n1.item(z1).getTextContent();
                	}
                	
                	if (pp1.equals("tc:DocId")) {
                		docId = n1.item(z1).getTextContent();
                	}
                	
                	if (pp1.equals("tc:TransportId")) {
                		transportID =n1.item(z1).getTextContent();
                	}
                	
                	if (pp1.equals("tc:RegID")) {
                		regID =n1.item(z1).getTextContent();
                	}

                	if (pp1.equals("tc:DocHash")) {
                		docHash = n1.item(z1).getTextContent();
                	}
                	
                	if (pp1.equals("tc:DocType")) {
                		docType =n1.item(z1).getTextContent();
                	}

                	if (pp1.equals("tc:OperationResult")) {
	    
                		if (pp1.equals("#text")) {
                    		continue;
                    	}
                		
                		NodeList n2 = n1.item(z1).getChildNodes();
                		                		                		
                		for (int z2 =0 ; z2<n2.getLength();z2++) {

                			String pp2 = n2.item(z2).getNodeName();
                        	
                			if (pp2.equals("#text")) {
                        		continue;
                        	}
                        	
                        	if (pp2.equals("tc:OperationName")) {
                        		operName = "'" + n2.item(z2).getTextContent() + "'";
                        	}
                        	
                        	if (pp2.equals("tc:OperationResult")) {
                        		operResult = "'" + n2.item(z2).getTextContent() + "'";
                        	}

							if (pp2.equals("tc:OperationDate")) {
								
								content = n2.item(z2).getTextContent();
	                            try {
	                                operDate = format.parse(content);
	                            } catch (ParseException e) {
	                                e.printStackTrace();
	                            }
								
							}

							if (pp2.equals("tc:OperationComment")) {
								operComment = "'" + n2.item(z2).getTextContent() + "'";
							}
                        	
                		}
                		
                	}
                	
                	if (pp1.equals("tc:Result")) {
            		    
                		if (pp1.equals("#text")) {
                    		continue;
                    	}
                		
                		NodeList n3 = n1.item(z1).getChildNodes();
                		                		                		
                		for (int z3 =0 ; z3<n3.getLength();z3++) {
                        	
                        	String pp3 = n3.item(z3).getNodeName();
                        	
                        	if (pp3.equals("#text")) {
                        		continue;
                        	}
                        	
                        	if (pp3.equals("tc:Conclusion")) {
                        		resConcl = "'" + n3.item(z3).getTextContent() + "'";
							}
                        	
                        	if (pp3.equals("tc:ConclusionDate")) {
								
                        		content = n3.item(z3).getTextContent();
                        		try {
                                    resDate = format.parse(content);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
								
							}

                        	
                        	if (pp3.equals("tc:Comments")) {
                        		resConcl = "'" + n3.item(z3).getTextContent() + "'";
							}
                        	
                		}
                		
            		}
                	
                }
                
            }//for z
            
		} catch (ParserConfigurationException e) {
			
			System.out.println("Ошибка разбора тикета (ParserConfigurationException) "+adr);
			return;
			
		} catch (SAXException e) {
			
			System.out.println("Ошибка разбора тикета (SAXException) "+adr);
			return;
			
		} catch (IOException e) {
			
			System.out.println("Ошибка разбора тикета (IOException) "+adr);
			return;
			
		}
	
		 
		 
		try {
			
			url 	= new URL(adr);
			lconn 	= (HttpURLConnection)url.openConnection();
			lconn.setRequestMethod("GET");
			Charset charset 			= StandardCharsets.UTF_8;
			xmlStr 						= detailDoc.convert(lconn.getInputStream(),charset);
			
		} catch (ProtocolException | MalformedURLException  e1) {
			
			System.out.println("xmlStr Ошибка загрузки тикета (ProtocolException | MalformedURLException) "+adr);
			return;
			
		} catch (IOException e) {
			
			System.out.println("xmlStr Ошибка загрузки тикета (IOException e) "+adr);
			return;
			
		}

                  
		SaveTicket();
		
	} //ticket
	
	
	
	private void SaveTicket() {
	  		
  		Connection bd;
  		Statement st;
  		ResultSet rs = null;
  		
  		if(transportID ==null) {
  			
  			System.out.println("не передан transportID тикета "+adr1);
  			return;
  			
  		}
  		
  		try {
  		
  			Class.forName("org.sqlite.JDBC");
  			bd 		= DriverManager.getConnection("jdbc:sqlite:"+DB_NAME+".db");
  			
  			boolean isExist = false;
  				
			String sqEx = "SELECT COUNT(id) as c FROM docTicket WHERE transportID ='"+transportID+"';";
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
				//long lDateTime = new Date().getTime();
				String sqAdd = 
						"INSERT INTO docTicket ("+
				"transportID,regID,docType,resConcl,resComment,operName,operResult,operComment,tickDate,resDate,"
								+"operDate,identity,docId,docHash,xmlStr) "+
				"VALUES ('"+transportID+"',"+
						"'"+regID+"',"+
						"'"+docType+"',"+
						"'"+resConcl+"',"+
						"'"+resComment+"',"+
						"'"+operName+"',"+
						"'"+operResult+"',"+
						"'"+operComment+"',"+
						" "+tickDate+" ,"+
						" "+resDate+" ,"+
						" "+operDate+" ,"+
						"'"+identity+"',"+
						"'"+docId+"',"+
						"'"+docHash+"',"+
						"'"+xmlStr+"');";
						
						
				st 	= bd.createStatement();
		        st.execute("BEGIN TRANSACTION;"); ////***это важно (начать транзакцию)
		        st.setQueryTimeout(60);
		        st.execute(sqAdd);
		        st.execute("COMMIT;");
		        st.close();
				
			}
  	            
  			bd.close();
  			
  		}//try
  		
  		catch (ClassNotFoundException | SQLException e1) {
  			
  			System.out.println("ошибка записи тикета "+transportID);
  			
  		}
	  
		
	}//SaveTicket
	
}//class ticket
