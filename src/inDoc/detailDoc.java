package inDoc;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

//***Расшифровка ответов УТМ

public class detailDoc {
	
	String s;
	
	detailDoc(String s) {
		
		this.s = s;
		
	}

	public static String typeDoc(String a) {
		/*
		 *	@a = http://192.168.100.55:8080/opt/out/FORM2REGINFO/142366
		 *  вернет FORM2REGINFO 
		 */
		
		if (a ==null) {
			
			return "";
			
		}
		String[] m = a.split("/");
		
		if (m.length >1 ) {
			
			return m[m.length-2];
			
		}
			
		return "";
		
	} //typeDoc
	
	
	public static void getParserDocument(String s) {
		/*
		 * 
		 * парсит документ по адресу @s
		 * 
		 */
		
		
		/*
		 * WayBillAct_v3,FORM2REGINFO,TTNHISTORYF2REG,
		 * ReplyRests_v2,Ticket
		 */
		if(typeDoc(s).equals("")) {
			
			return;
			
		}
		
		if(typeDoc(s).equals("WayBillAct_v3")) {
			
			return;
			
		}
		
		if(typeDoc(s).equals("FORM2REGINFO")) {
			
			return;
			
		}
		
		if(typeDoc(s).equals("TTNHISTORYF2REG")) {
			
			return;
			
		}
		
		if(typeDoc(s).equals("ReplyRests_v2")) {
			
			return;
			
		}

		if(typeDoc(s).equals("Ticket")) {
	
			return;
	
		}
		
	} //getParserDocument
	
	public static void parseTicket(String s) {
		/*
		 * парсим тикет по адресу @s
		 * 
		 */
		
		
        	
		
	} //parseTicket
	
}
