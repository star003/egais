package monitor;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class UTMHost {
	
    public class HostElement {
    	
        public int recno;
        public String url;
        public String replyID;
        public String fileID;
        
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
                    
                    monitorFunctions.addHistoryUTM(he.url, he.replyID);
                    
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
    
} //UTMHost
