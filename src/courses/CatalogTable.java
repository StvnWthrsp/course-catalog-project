package courses;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.table.DefaultTableModel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

public class CatalogTable extends JFrame implements Runnable {
	//Start with table with empty dataModel
	private static DefaultTableModel dataModel = new DefaultTableModel();
	public static JTable table = new JTable(dataModel);
	//Setup columnNames, because these won't change
	final String[] columnNames = {"Course Code",
            "Course Name",
            "Description",
            "Department",
            "# Meeting Days"};
	//Setup a clearModel to provide a clear table with columnNames still there
	private DefaultTableModel clearModel = new DefaultTableModel(null, columnNames);
	
    public CatalogTable()
    {
    }
    
    //This class only to be called when application initially runs
    //It exists for if you wish to start with a non-empty table on application launch
    public JScrollPane makeTable(Object[][] items) {
    	//Add items to the table & set dimensions
        dataModel = new DefaultTableModel(items,columnNames){
        	@Override
        	public boolean isCellEditable(int row, int column) {
        		return false;
        	}
        };
        table.setModel(dataModel);
        table.setPreferredScrollableViewportSize(new Dimension(800, 200));
        table.setFillsViewportHeight(true);
        table.setAutoCreateRowSorter(true);

        //Create the scroll pane and add the table to it.
        JScrollPane scrollPane = new JScrollPane(table);

        //Add the scroll pane to this panel.
        return scrollPane;
    }
    
    //Reads information from a file and creates Array
    public Object[][] readFile(File file)
    {
    	InsertFromFile insf = new InsertFromFile();
    	try {
			insf.readDataBase(file);
		} catch (Exception e) {
			
		}
    	ArrayList<ArrayList<Object>> whatever = insf.getCourse();
    	Object[][] a = new Object[whatever.size()][];
    	for(int k = 0; k < whatever.size(); k++)
    	{
    		ArrayList<Object> blech = whatever.get(k);
    		a[k] = blech.toArray(new Object[blech.size()]);
    	}
    	return a;
    }
    
    //Add things to the GUI
    private void addComponents()
    {
    	
    	JPanel controlPanel = new JPanel();
    	JButton insertButton = new JButton("Insert");
    	JButton deleteButton = new JButton("Delete");
    	JButton clearButton = new JButton("Clear");
        getContentPane().add(BorderLayout.SOUTH, controlPanel);
        controlPanel.setLayout(new GridLayout(1,3));
        controlPanel.add(insertButton);
        insertButton.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e)
        	{
        		Insert ins = new Insert();
        		MakeTable newTable = new MakeTable();
        		try {
        			ins.readDataBase();
					Object[][] stuff = newTable.getData();
					dataModel.addRow(stuff[stuff.length-1]);
        			table.setModel(dataModel);
					getContentPane().revalidate();
					getContentPane().repaint();
				} catch (Exception e1) {
				}
        	}
        });
        controlPanel.add(deleteButton);
        deleteButton.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e)
        	{
        		Delete del = new Delete();
        		try {
        			del.readDataBase();
        			dataModel.removeRow(table.getSelectedRow());
        			table.setModel(dataModel);
        			getContentPane().revalidate();
					getContentPane().repaint();
        		} catch (Exception e1) {
        		}
        	}
        });
        controlPanel.add(clearButton);
        clearButton.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e)
        	{
        		Delete del = new Delete();
        		try {
					del.deleteAll();
					dataModel.setRowCount(0);
					dataModel = clearModel;
					table.setModel(dataModel);
					getContentPane().revalidate();
					getContentPane().repaint();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
        	}
        });
    }
    
    //Add JMenu
    private void makeMenus()
    {
      JMenuBar mbar = new JMenuBar();
      setJMenuBar(mbar);
      JMenu fileMenu = new JMenu("File");
      mbar.add(fileMenu);
      JMenuItem quit = new JMenuItem("Quit");
      fileMenu.add(quit);
      quit.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          System.exit(0);
        }
      });
      JMenuItem open = new JMenuItem("Open");
      fileMenu.add(open);
      open.addActionListener(new ActionListener()
      {
    	  public void actionPerformed(ActionEvent e)
    	  {
    		  File file = null;
    		  Delete del = new Delete();
      		try {
      				JFileChooser fc = new JFileChooser();
      				int choice = fc.showOpenDialog(CatalogTable.this);
      				if(choice == JFileChooser.APPROVE_OPTION)
      				{
      					file = fc.getSelectedFile();
      				}
					del.deleteAll();
					dataModel.setRowCount(0);
					dataModel = clearModel;
					dataModel = new DefaultTableModel(readFile(file), columnNames);
					table.setModel(dataModel);
					getContentPane().revalidate();
					getContentPane().repaint();
					
				} catch (Exception e1) {
				}
    	  }
      });
    }
    
    public void run()
    {
      setSize(800,300);
      setDefaultCloseOperation(EXIT_ON_CLOSE);
      setTitle("Course Catalog");
      makeMenus();
      //Adds the initial empty table to the GUI.
      try {
		getContentPane().add(BorderLayout.NORTH, makeTable(new MakeTable().getData()));
	} catch (Exception e) {
	}
      addComponents();
      setVisible(true);
    }

    public static void main(String[] args)
    {
      CatalogTable cc = new CatalogTable();
      javax.swing.SwingUtilities.invokeLater(cc);
    }
}