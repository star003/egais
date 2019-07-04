package app;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.net.URL;
import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.UIManager;

import monitor.UTMHost;
import workBD.newDB;

public class mainForm  extends JFrame {
	
    private static final long serialVersionUID = 20190704;
    
    TrayIcon trayIcon;
	SystemTray tray;
	Timer timer = new Timer();
	
  //********************************************************************************************************************
    
    mainForm() {
    	
        super("ЕГАИС 2019 (c 24-06-2019)");
        
        try {
			
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			
		} catch (Exception e) {
			
			System.out.println("getSystemLookAndFeelClassName не поддерживается");
			
		}
        
        if (SystemTray.isSupported()) {

			tray = SystemTray.getSystemTray();
 
			Image image = Toolkit.getDefaultToolkit().getImage("bath.png");

			ActionListener exitListener = new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
					
					System.exit(0);
					
				}
				
			};
			
			PopupMenu popup 		= new PopupMenu();
			MenuItem defaultItem 			= new MenuItem("Отобразить");
			defaultItem.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
					
					setVisible(true);
					setExtendedState(JFrame.NORMAL);
					
				}
				
			});
			popup.add(defaultItem);
			
			defaultItem 			= new MenuItem("УТМ");
			defaultItem.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
					
					monitorUtmForm app = new monitorUtmForm();
	                app.setVisible(true);
					
				}
				
			});
			
			popup.add(defaultItem);
			
			defaultItem 	= new MenuItem("ВЫХОД");
			defaultItem.addActionListener(exitListener);
			popup.add(defaultItem);
			
			trayIcon = new TrayIcon(image, "Робот", popup);
			trayIcon.setImageAutoSize(true);
			
		}
        
        addWindowStateListener(new WindowStateListener() {
        	
			public void windowStateChanged(WindowEvent e) {
				
				if (e.getNewState() == ICONIFIED) {
					
					try {
						
						tray.add(trayIcon);
						setVisible(false);
						
					} catch (AWTException ex) {
						
						System.out.println("unable to add to tray");
						
					}
				}
				
				if (e.getNewState() == 7) {
					
					try {
						
						tray.add(trayIcon);
						setVisible(false);
						
					} catch (AWTException ex) {
						
						System.out.println("unable to add to system tray");
						
					}
				}
				
				if (e.getNewState() == MAXIMIZED_BOTH) {
					
					tray.remove(trayIcon);
					setVisible(true);
					
				}
				
				if (e.getNewState() == NORMAL) {
					
					tray.remove(trayIcon);
					setVisible(true);
					
				}
				
			}
			
		});
        
        setIconImage(Toolkit.getDefaultToolkit().getImage("bath.png"));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(createFileMenu());
        menuBar.add(createPurchasesMenu());
        setJMenuBar(menuBar);
        Dimension sSize = Toolkit.getDefaultToolkit ().getScreenSize ();
        setSize (sSize);
        setExtendedState(MAXIMIZED_BOTH);
        setVisible(true);
        
        timer.scheduleAtFixedRate(new TimerTask() {
			  
			@Override
			  public void run() {
			    
					UTMHost.monitorUTM();
					System.out.print("X");
				
			  }
			}, 5*1000, 30*1000);

        
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
                monitorUtmForm app = new monitorUtmForm();
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
    
    protected static Image createImage(String path, String description) {
    	
        URL imageURL = app.mainForm.class.getResource(path);
 
        if (imageURL == null) {
        	
            System.err.println("Resource not found: " + path);
            return null;
            
        } else {
        	
            return (new ImageIcon(imageURL, description)).getImage();
            
        }
        
    }
    
} //mainForm
   