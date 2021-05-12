
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

//Map Application
//Author: Maksim Zakharau, 256629 
//Data: October 2020;

public class GroupManagerApp extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;

	private static final String GREETING_MESSAGE =
			"Program do zarz¹dzania grupami osób " +
	        "- wersja okienkowa\n\n" +
	        "Autor: Pawe³ Rogalinski\n" +
			"Data:  paŸdziernik 2017 r.\n";

	private static final String ALL_GROUPS_FILE = "GROUPLIST.BIN";

	
	public static void main(String[] args) {
		new GroupManagerApp();
	}
	
	
	WindowAdapter windowListener = new WindowAdapter() {

		@Override
		public void windowClosed(WindowEvent e) {
			JOptionPane.showMessageDialog(null, "Application has been closed");
		}
		
		@Override
		public void windowClosing(WindowEvent e) {
			
			windowClosed(e);
		}
	};
	
	
	private List<GroupOfMap> currentList = new ArrayList<GroupOfMap>();
	
	JMenuBar menuBar        = new JMenuBar();
	JMenu menuGroups        = new JMenu("Groups");
	JMenu menuAbout         = new JMenu("About");
	
	JMenuItem menuNewGroup           = new JMenuItem("Make a group");
	JMenuItem menuEditGroup          = new JMenuItem("Edit group");
	JMenuItem menuDeleteGroup        = new JMenuItem("Delete group");
	JMenuItem menuLoadGroup          = new JMenuItem("Load group to file");
	JMenuItem menuSaveGroup          = new JMenuItem("Save group to file");

	JMenuItem menuAuthor             = new JMenuItem("Author");
	
	JButton buttonNewGroup = new JButton("Make");
	JButton buttonEditGroup = new JButton("Edit");
	JButton buttonDeleteGroup = new JButton("Delete");
	JButton buttonLoadGroup = new JButton("Open");
	JButton buttonSavegroup = new JButton("Save");
	
	ViewGroupList viewList;

	public GroupManagerApp() {
		setTitle("GroupManager - manage to groups of maps");
		setSize(450, 400);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosed(WindowEvent event) {
				try {
					saveGroupListToFile(ALL_GROUPS_FILE);
					JOptionPane.showMessageDialog(null, "Data has been writed to file " + ALL_GROUPS_FILE);
				} catch (MapException e) {
					JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}

			@Override
			public void windowClosing(WindowEvent e) {
				windowClosed(e);
			}
		} 
		); 

		try {
			loadGroupListFromFile(ALL_GROUPS_FILE);
			JOptionPane.showMessageDialog(null, "Data has been readed from file" + ALL_GROUPS_FILE);
		} catch (MapException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
		
		setJMenuBar(menuBar);
		menuBar.add(menuGroups);
		menuBar.add(menuAbout);
		menuGroups.add(menuNewGroup);
		menuGroups.add(menuEditGroup);
		menuGroups.add(menuDeleteGroup);
		menuGroups.addSeparator();
		menuGroups.add(menuLoadGroup);
		menuGroups.add(menuSaveGroup);
		menuAbout.add(menuAuthor);
		
		menuNewGroup.addActionListener(this);
		menuEditGroup.addActionListener(this);
		menuDeleteGroup.addActionListener(this);
		menuLoadGroup.addActionListener(this);
		menuSaveGroup.addActionListener(this);
		menuAuthor.addActionListener(this);
		
		buttonNewGroup.addActionListener(this);
		buttonEditGroup.addActionListener(this);
		buttonDeleteGroup.addActionListener(this);
		buttonLoadGroup.addActionListener(this);
		buttonSavegroup.addActionListener(this);
		
		viewList = new ViewGroupList(currentList, 400, 250);
		viewList.refreshView();
		
		JPanel panel = new JPanel();
		
		panel.add(viewList);
		panel.add(buttonNewGroup);
		panel.add(buttonEditGroup);
		panel.add(buttonDeleteGroup);
		panel.add(buttonLoadGroup);
		panel.add(buttonSavegroup);
		
		setContentPane(panel);
		setVisible(true);
		}
	
	@SuppressWarnings("unchecked")
	void loadGroupListFromFile(String file_name) throws MapException {
		try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file_name))) {
		currentList = (List<GroupOfMap>)in.readObject();
		} catch (FileNotFoundException e) {
			throw new MapException("File not found " + file_name);
		} catch (Exception e) {
			throw new MapException("Error on the file reading process");
		}
	}
	
	
	void saveGroupListToFile(String file_name) throws MapException {
		try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file_name))) {
			out.writeObject(currentList);
		} catch (FileNotFoundException e) {
			throw new MapException("File not found " + file_name);
		} catch (IOException e) {
			throw new MapException("Error on the file reading process");
		}
	}
	
	@SuppressWarnings("unused")
	private  GroupOfMap chooseGroup(Window parent, String message){
		Object[] groups = currentList.toArray();
		GroupOfMap group = (GroupOfMap)JOptionPane.showInputDialog(
		                    parent, message,
		                    "Choose a group",
		                    JOptionPane.QUESTION_MESSAGE,
		                    null,
		                    groups,
		                    null);
		return group;
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		Object source = event.getSource();

		try {
			if (source == menuNewGroup || source == buttonNewGroup) {
				GroupOfMap group = GroupOfMapWindowDialog.createNewGroupOfMap(this);
				if (group != null) {
					currentList.add(group);
				}
			}

			if (source == menuEditGroup || source == buttonEditGroup) {
				int index = viewList.getSelectedIndex();
				if (index >= 0) {
					Iterator<GroupOfMap> iterator = currentList.iterator();
					while (index-- > 0)
						iterator.next();
					new GroupOfMapWindowDialog(this, iterator.next());
				}
			}

			if (source == menuDeleteGroup || source == buttonDeleteGroup) {
				int index = viewList.getSelectedIndex();
				if (index >= 0) {
					Iterator<GroupOfMap> iterator = currentList.iterator();
					while (index-- >= 0)
						iterator.next();
					iterator.remove();
				}
			}

			if (source == menuLoadGroup || source == buttonLoadGroup) {
				JFileChooser chooser = new JFileChooser(".");
				int returnVal = chooser.showOpenDialog(this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					GroupOfMap group = GroupOfMap.readFromFile(chooser.getSelectedFile().getName());
					currentList.add(group);
				}
			}

			if (source == menuSaveGroup || source == buttonSavegroup) {
				int index = viewList.getSelectedIndex();
				if (index >= 0) {
					Iterator<GroupOfMap> iterator = currentList.iterator();
					while (index-- > 0)
						iterator.next();
					GroupOfMap group = iterator.next();

					JFileChooser chooser = new JFileChooser(".");
					int returnVal = chooser.showSaveDialog(this);
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						GroupOfMap.printToFile( chooser.getSelectedFile().getName(), group );
					}
				}
			}

			if (source == menuAuthor) {
				JOptionPane.showMessageDialog(this, GREETING_MESSAGE);
			}

		} catch (MapException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
		viewList.refreshView();
	}
}





class ViewGroupList extends JScrollPane {
private static final long serialVersionUID = 1L;

	private List<GroupOfMap> list;
	private JTable table;
	private DefaultTableModel tableModel;

	public ViewGroupList(List<GroupOfMap> list, int width, int height){
		this.list = list;
		setPreferredSize(new Dimension(width, height));
		setBorder(BorderFactory.createTitledBorder("Grouplist:"));

		String[] tableHeader = { "Name of group", "Collection type", "Number of countries" };
		tableModel = new DefaultTableModel(tableHeader, 0);
		table = new JTable(tableModel) {

			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int rowIndex, int colIndex) {
				return false; 
			}
		};
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setRowSelectionAllowed(true);
		setViewportView(table);
	}

	void refreshView(){
		tableModel.setRowCount(0);
		for (GroupOfMap group : list) {
			if (group != null) {
				String[] row = { group.getName(), group.getType().toString(), "" + group.size() };
				tableModel.addRow(row);
			}
		}
	}

	int getSelectedIndex(){
		int index = table.getSelectedRow();
		if (index<0) {
			JOptionPane.showMessageDialog(this, "No group selected", "Error", JOptionPane.ERROR_MESSAGE);
		}
		return index;
	}

}