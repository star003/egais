package monitor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import monitor.UTMHost.HostElement;

import java.awt.FlowLayout;

public class monitorUTM  extends JFrame{
	
	private static final long serialVersionUID = 2L;
	Timer timer = new Timer();
	
	static JTable table;
	JButton bt_autoRefresh 	= new JButton("Автообновление");
	
	boolean autoRefresh = false;
	
	public monitorUTM() {
		
		setTitle("Монитор очереди");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		table = new JTable(refreshTable.buildEmtyTableModel());
		
		JPanel pn_TopBotton = new JPanel();
		
		JButton bt_In 			= new JButton("Исходящие запросы");
		JButton bt_out 			= new JButton("Входящие запросы");
		JButton bt_details 		= new JButton("Расшифровать");
		
		bt_In.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				refreshTable.refreshUtmTable(table,"in");
				
			}
			
		});
		
		bt_out.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				refreshTable.refreshUtmTable(table,"out");
				
			}
			
		});
		
		bt_autoRefresh.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				autoRefresh = (!autoRefresh);
				System.out.println(autoRefresh);
								
				if(autoRefresh) {
				
					bt_autoRefresh.setText("Обновление вкл.");
					timer.scheduleAtFixedRate(new TimerTask() {
						  
						@Override
						  public void run() {
						    
								refreshTable.refreshUtmTable(table,"out");
								System.out.print("*");
							
						  }
						}, 5*1000, 30*1000);

				}
				
				else {
					
					bt_autoRefresh.setText("Обновление откл.");
					timer.cancel();
					timer.purge();
					System.out.print("stop");
					
				}
				
			}
			
		});
		JScrollPane scrollPane_1 = new JScrollPane();
		
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(10)
							.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 764, Short.MAX_VALUE))
						.addComponent(pn_TopBotton, GroupLayout.PREFERRED_SIZE, 763, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(pn_TopBotton, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 505, Short.MAX_VALUE)
					.addContainerGap())
		);
		pn_TopBotton.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		pn_TopBotton.add(bt_In);
		pn_TopBotton.add(bt_out);
		pn_TopBotton.add(bt_details);
		pn_TopBotton.add(bt_autoRefresh);
		scrollPane_1.setViewportView(table);
		getContentPane().setLayout(groupLayout);
		setSize(1024, 768);
		setLocationRelativeTo(null);
		
	} //startingForm
		
	public static void main(String[] args) {
    	
		monitorUTM app = new monitorUTM();
		app.setVisible(true);
        
    }//main
		
} //startingForm

class refreshTable {
	
	refreshTable() {
		
	}
	
	public static DefaultTableModel buildEmtyTableModel()  {
		
		Vector<String> columnNames = new Vector<String>();
		columnNames.add("recno");
		columnNames.add("url");
		columnNames.add("replyID");
		columnNames.add("fileID");
		Vector<Vector<Object>> data = new Vector<Vector<Object>>();
		return new DefaultTableModel(data, columnNames);
	    
	}//buildEmtyTableModel
	
	public static DefaultTableModel buildTableModel(String rj)  {
			    	    
	    DefaultTableModel tableModel = new DefaultTableModel();
	    Object[] columnsHeader = new String[] {"recno", "url", "replyID","fileID"};
	    tableModel.setColumnIdentifiers(columnsHeader);
	    	    
	    ArrayList<HostElement> p = monitorFunctions.getUtmDocument(rj);
		
		for (int i = 0; i < p.size(); i++) {
			
			String[] myArray = new String[4];
			myArray[0] =String.valueOf((p.get(i).recno)); 
			myArray[1] =(String)p.get(i).url;
			myArray[2] =(String)p.get(i).replyID;
			myArray[3] ="-";
			tableModel.addRow(myArray);
			
        }
		
		return tableModel;
    
	} //buildTableModel
	
	static void refreshUtmTable (JTable table,String rj) {
		
		table.setModel(buildTableModel(rj));
		TableColumnModel columnModel = table.getColumnModel();
		columnModel.getColumn(0).setPreferredWidth(100);
		columnModel.getColumn(1).setPreferredWidth(370);
		columnModel.getColumn(2).setPreferredWidth(370);
		
	} //refreshUtmTable
	
} //refreshTable