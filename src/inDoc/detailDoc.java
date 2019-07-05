package inDoc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.stream.Collectors;

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
	
	public static String convert(InputStream inputStream, Charset charset) throws IOException {
		 
		try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, charset))) {
			
			return br.lines().collect(Collectors.joining(System.lineSeparator()));
			
		}
		
	}//convert
	
}
