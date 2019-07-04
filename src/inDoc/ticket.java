package inDoc;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
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
	
	Document document;
    DocumentBuilderFactory builderFactory;
    DocumentBuilder builder = null;
    
    DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
    
    private String transportID;
    private String regID;
    private String docType;
    private String resConcl;
    private String resComment;
    private String operName;
    private String operResult;
    private String operComment;
    private Date tickDate;
    private Date resDate;
    private Date operDate;
    private String  identity;
    private String  docId;
    private String docHash;
    
    
	public ticket(String adr) {
		
		
		HttpURLConnection lconn = null;
		URL url = null;
		
		try {
			
			url 	= new URL(adr);
			lconn 	= (HttpURLConnection)url.openConnection();
			lconn.setRequestMethod("GET");
			
		} catch (ProtocolException | MalformedURLException  e1) {
			
			e1.printStackTrace();
			
		} catch (IOException e) {
			
			e.printStackTrace();
			
		}
		
		try {
	
			DocumentBuilderFactory dbf  = DocumentBuilderFactory.newInstance();
            DocumentBuilder db          = dbf.newDocumentBuilder();
            Document doc                = db.parse(new InputSource(lconn.getInputStream()));
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("ns:Ticket");
            NodeList nl = doc.getChildNodes();
            String content;
            
            for (int z=0 ; z<nl.getLength();z++) {
            	
                Node node = nodeList.item(z);
                NodeList n1 = node.getChildNodes();
                
                for (int z1 =0 ; z1<n1.getLength();z1++) {
                	
                	String pp1 = n1.item(z1).getNodeName();
                	
                	if (pp1.equals("#text")) {
                		
                		continue;
                		
                	}

                	//System.out.print(pp1);
                	//System.out.print(" ");
                	
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
                		
                		System.out.print(pp1);
                    	System.out.print(" ");
                		
                		NodeList n2 = n1.item(z1).getChildNodes();
                		                		                		
                		for (int z2 =0 ; z2<n2.getLength();z2++) {
                        	
                        	String pp2 = n2.item(z2).getNodeName();
                        	
                        	if (pp2.equals("#text")) {
                        		
                        		continue;
                        		
                        	}
                        	
                        	System.out.print("-----");
                        	System.out.print(pp2);
                        	System.out.print(" ");
                        	System.out.println(n2.item(z2).getTextContent());
                        	//#text
                        	
                		}
	
                	}
                	
                }
                
            }//for z
            
		} catch (ParserConfigurationException e) {
			
			e.printStackTrace();
			
		} catch (SAXException e) {
			
			e.printStackTrace();
			
		} catch (IOException e) {
			
			e.printStackTrace();
			
		}
	
	}
}
