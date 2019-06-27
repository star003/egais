package app;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import monitor.monitorUTM;
import workBD.newDB;

public class mainForm  extends JFrame {
	
    private static final long serialVersionUID = 1L;
    
  //********************************************************************************************************************
    
    public mainForm() {
    	
        super("ЕГАИС 2019 (c 24-06-2019)");
        
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(createFileMenu());
        menuBar.add(createPurchasesMenu());
        setJMenuBar(menuBar);
        Dimension sSize = Toolkit.getDefaultToolkit ().getScreenSize ();
        setSize (sSize);
        setExtendedState(MAXIMIZED_BOTH);
        setVisible(true);
                
    } //mainForm
    
  //********************************************************************************************************************
    
    private JMenu createFileMenu(){
        
        JMenu file = new JMenu("Файл");
        JMenuItem m10 = new JMenuItem("УТМ");
        JMenuItem m11 = new JMenuItem("Настройки");
        JMenuItem m12 = new JMenuItem(new ExitAction());
        file.add(m10);
        file.addSeparator();
        file.add(m11);
        file.addSeparator();
        file.add(m12);
        
        m10.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent arg0) {
            	
                System.out.println ("ActionListener.actionPerformed : УТМ");
                monitorUTM app = new monitorUTM();
                app.setVisible(true);
                
            }
            
        });
        
        m11.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent arg0) {
            	
                System.out.println ("ActionListener.actionPerformed : Настройки");
                
            }
            
        });
        
        m12.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent arg0) {
            	
                System.out.println ("ActionListener.actionPerformed : Выйти");
                
            }
            
        });
        
        return file;
        
    } //createFileMenu
    
  //********************************************************************************************************************
    
    private JMenu createPurchasesMenu(){
        
        JMenu purchases = new JMenu("Закупки");
        JMenuItem m21 = new JMenuItem("Приходная накладная ЕГАИС");
        JMenuItem m22 = new JMenuItem("Акт приема товара");
        JMenuItem m23 = new JMenuItem("Запрос не обработанных документов");
        purchases.add(m21);
        purchases.addSeparator();
        purchases.add(m22);
        purchases.addSeparator();
        purchases.add(m23);

        return purchases;
        
    } //createPurchasesMenu
     
  //********************************************************************************************************************
    
    class ExitAction extends AbstractAction {
    	
        private static final long serialVersionUID = 1L;
        
        ExitAction() {
        	
        	putValue(NAME, "Выход");
        	
        }//ExitAction
        
        public void actionPerformed(ActionEvent e) {
        	
            System.exit(0);
            
        }//actionPerformed
        
    } //ExitAction
    
  //********************************************************************************************************************
    
    public static void main(String[] args){
    	
    	//***инициализация баз данных
    	try {
    		
			newDB.createTables();
			
		} catch (ClassNotFoundException | SQLException e) {
			
			e.printStackTrace();
			System.exit(0);
			
		}
    	
    	JFrame.setDefaultLookAndFeelDecorated(true);
        new mainForm();
        
    } //main
    
} //mainForm

class goDaemon extends Thread {
	
    boolean started;
    public void run() {
    	
        while (true) {
        	
            
        }
        
    } //public void run()
    
    void beginStart() {
    	
        started = true;
        
    }//void beginStart()
    
    boolean startedTr() {
    	
        return started;
        
    }//boolean startedTr()
    
}   //goDaemon
    