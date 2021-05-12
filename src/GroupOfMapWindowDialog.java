
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

//Map Application
//Author: Maksim Zakharau, 256629 
//Data: October 2020;

public class GroupOfMapWindowDialog extends JDialog implements ActionListener {
  private static final long serialVersionUID = 1L;
  
  private static final String GREETING_MESSAGE = "Map Groups Editor\n\nAuthor: Maksim Zakharau\nDate:november 2020\n";
  
  private GroupOfMap currentGroup;
  
  public static void main(String[] args) {
    try {
      GroupOfMap currentGroup = new GroupOfMap(GroupType.ARRAY_LIST, "Test Group");
    } catch (MapException e) {
      JOptionPane.showMessageDialog(null, e.getMessage(), "Error", 0);
    } 
  }
  
  private static String enterGroupName(Window parent) {
    return JOptionPane.showInputDialog(parent, "Give the name of group: ");
  }
  
  private static GroupType chooseGroupType(Window parent, GroupType current_type) {
    GroupType[] arrayOfGroupType = GroupType.values();
    GroupType type = (GroupType)JOptionPane.showInputDialog(
        parent, 
        "Choose a type of collection", 
        
        "Change type of collection", 
        3, null, (Object[])arrayOfGroupType, 
        
        current_type);
    return type;
  }
  
  public static GroupOfMap createNewGroupOfMap(Window parent) {
    GroupOfMap new_group;
    String name = enterGroupName(parent);
    if (name == null || name.equals(""))
      return null; 
    GroupType type = chooseGroupType(parent, (GroupType)null);
    if (type == null)
      return null; 
    try {
      new_group = new GroupOfMap(type, name);
    } catch (MapException e) {
      JOptionPane.showMessageDialog(null, e.getMessage(), "Error", 0);
      return null;
    } 
    GroupOfMapWindowDialog dialog = new GroupOfMapWindowDialog(parent, new_group);
    return dialog.currentGroup;
  }
  
  public static void changeGroupOfPeople(Window parent, GroupOfMap group) {}
  
  JMenuBar menuBar = new JMenuBar();
  
  JMenu menuMap = new JMenu("List of maps");
  
  JMenu menuSort = new JMenu("Sorting");
  
  JMenu menuProperty = new JMenu("Properties");
  
  JMenuItem menuNewMap = new JMenuItem("Add");
  
  JMenuItem menuEditMap = new JMenuItem("Edit");
  
  JMenuItem menuDeleteMap = new JMenuItem("Delete");
  
  JMenuItem menuLoadMap = new JMenuItem("Load");
  
  JMenuItem menuSaveMap = new JMenuItem("Save");
  
  JMenuItem menuSortName = new JMenuItem("Sort alfabetical");
  
  JMenuItem menuSortScale = new JMenuItem("Sort use scale");
 
  JMenuItem menuChangeName = new JMenuItem("Edit name");
  
  JMenuItem menuChangeType = new JMenuItem("Edit collection type");
  
  JMenuItem menuAuthor = new JMenuItem("About");
  
  Font font = new Font("MonoSpaced", 1, 12);
  
  JLabel labelGroupName = new JLabel("        Group name: ");
  
  JLabel labelGroupType = new JLabel("   Collection type: ");
  
  JTextField fieldGroupName = new JTextField(15);
  
  JTextField fieldGroupType = new JTextField(15);
  
  JButton buttonNewMap = new JButton("Add country");
  
  JButton buttonEditMap = new JButton("Edit");
  
  JButton buttonDeleteMap = new JButton("Delete");
  
  JButton buttonLoadMap = new JButton("Load");
  
  JButton buttonSaveMap = new JButton("Save");
  
  ViewGroupOfMap viewList;
  
  public GroupOfMapWindowDialog(Window parent, GroupOfMap group) {
	    super(parent, Dialog.ModalityType.DOCUMENT_MODAL);
	    setTitle("Modification group of maps");
	    setDefaultCloseOperation(2);
	    setSize(450, 450);
	    setResizable(false);
	    if (parent != null) {
	      Point location = parent.getLocation();
	      location.translate(100, 100);
	      setLocation(location);
	    } else {
	      setLocationRelativeTo((Component)null);
	    } 
	    this.currentGroup = group;
	    setJMenuBar(this.menuBar);
	    this.menuBar.add(this.menuMap);
	    this.menuBar.add(this.menuSort);
	    this.menuBar.add(this.menuProperty);
	    this.menuBar.add(this.menuAuthor);
	    this.menuMap.add(this.menuNewMap);
	    this.menuMap.add(this.menuEditMap);
	    this.menuMap.add(this.menuDeleteMap);
	    this.menuMap.addSeparator();
	    this.menuMap.add(this.menuLoadMap);
	    this.menuMap.add(this.menuSaveMap);
	    this.menuSort.add(this.menuSortName);
	    this.menuSort.add(this.menuSortScale);
	    this.menuProperty.add(this.menuChangeName);
	    this.menuProperty.add(this.menuChangeType);
	    this.menuNewMap.addActionListener(this);
	    this.menuEditMap.addActionListener(this);
	    this.menuDeleteMap.addActionListener(this);
	    this.menuLoadMap.addActionListener(this);
	    this.menuSaveMap.addActionListener(this);
	    this.menuSortName.addActionListener(this);
	    this.menuSortScale.addActionListener(this);
	    this.menuChangeName.addActionListener(this);
	    this.menuChangeType.addActionListener(this);
	    this.menuAuthor.addActionListener(this);
	    this.labelGroupName.setFont(this.font);
	    this.labelGroupType.setFont(this.font);
	    this.fieldGroupName.setEditable(false);
	    this.fieldGroupType.setEditable(false);
	    this.viewList = new ViewGroupOfMap(this.currentGroup, 400, 250);
	    this.viewList.refreshView();
	    this.buttonNewMap.addActionListener(this);
	    this.buttonEditMap.addActionListener(this);
	    this.buttonDeleteMap.addActionListener(this);
	    this.buttonLoadMap.addActionListener(this);
	    this.buttonSaveMap.addActionListener(this);
	    JPanel panel = new JPanel();
	    panel.add(this.labelGroupName);
	    panel.add(this.fieldGroupName);
	    panel.add(this.labelGroupType);
	    panel.add(this.fieldGroupType);
	    panel.add(this.viewList);
	    panel.add(this.buttonNewMap);
	    panel.add(this.buttonEditMap);
	    panel.add(this.buttonDeleteMap);
	    panel.add(this.buttonLoadMap);
	    panel.add(this.buttonSaveMap);
	    this.fieldGroupName.setText(this.currentGroup.getName());
	    this.fieldGroupType.setText(this.currentGroup.getType().toString());
	    setContentPane(panel);
	    setVisible(true);
	  }
  
  public void actionPerformed(ActionEvent event) {
	    Object source = event.getSource();
	    try {
	      if (source == this.menuNewMap || source == this.buttonNewMap) {
	        Map newMap = MapWindowDialog.createNewMap(this);
	        if (newMap != null)
	          this.currentGroup.add(newMap); 
	      } 
	      if (source == this.menuEditMap || source == this.buttonEditMap) {
	        int index = this.viewList.getSelectedIndex();
	        if (index >= 0) {
	          Iterator<Map> iterator = this.currentGroup.iterator();
	          while (index-- > 0)
	            iterator.next(); 
	          MapWindowDialog.changeMapData(this, iterator.next());
	        } 
	      } 
	      if (source == this.menuDeleteMap || source == this.buttonDeleteMap) {
	        int index = this.viewList.getSelectedIndex();
	        if (index >= 0) {
	          Iterator<Map> iterator = this.currentGroup.iterator();
	          while (index-- >= 0)
	            iterator.next(); 
	          iterator.remove();
	        } 
	      } 
	      if (source == this.menuLoadMap || source == this.buttonLoadMap) {
	        JFileChooser chooser = new JFileChooser(".");
	        int returnVal = chooser.showOpenDialog(this);
	        if (returnVal == 0) {
	          Map map = Map.readFromFile(chooser.getSelectedFile().getName());
	          this.currentGroup.add(map);
	        } 
	      } 
	      if (source == this.menuSaveMap || source == this.buttonSaveMap) {
	        int index = this.viewList.getSelectedIndex();
	        if (index >= 0) {
	          Iterator<Map> iterator = this.currentGroup.iterator();
	          while (index-- > 0)
	            iterator.next(); 
	          Map map = iterator.next();
	          JFileChooser chooser = new JFileChooser(".");
	          int returnVal = chooser.showSaveDialog(this);
	          if (returnVal == 0)
	            Map.printToFile(chooser.getSelectedFile().getName(), map); 
	        } 
	      } 
	      if (source == this.menuSortName)
	        this.currentGroup.sortName(); 
	      if (source == this.menuSortScale)
	        this.currentGroup.sortScale(); 
	      if (source == this.menuChangeName) {
	        String newName = enterGroupName(this);
	        if (newName == null)
	          return; 
	        this.currentGroup.setName(newName);
	        this.fieldGroupName.setText(newName);
	      } 
	      if (source == this.menuChangeType) {
	        GroupType type = chooseGroupType(this, this.currentGroup.getType());
	        if (type == null)
	          return; 
	        this.currentGroup.setType(type);
	        this.fieldGroupType.setText(type.toString());
	      } 
	      if (source == this.menuAuthor)
	        JOptionPane.showMessageDialog(this, "Map Groups Editor\n\nAuthor: Maksim Zakharau\nDate:november 2020\n"); 
	    } catch (MapException e) {
	      JOptionPane.showMessageDialog(this, e.getMessage(), "Error", 0);
	    } 
	    this.viewList.refreshView();
	  }
	}



class ViewGroupOfMap extends JScrollPane {
  private static final long serialVersionUID = 1L;
  
  private GroupOfMap group;
  
  private JTable table;
  
  private DefaultTableModel tableModel;
  
  public ViewGroupOfMap(GroupOfMap group, int width, int height) {
    this.group = group;
    setPreferredSize(new Dimension(width, height));
    setBorder(BorderFactory.createTitledBorder("Map list"));
    String[] tableHeader = { "Country", "Scale"};
    this.tableModel = new DefaultTableModel((Object[])tableHeader, 0);
    this.table = new JTable(this.tableModel) {
        private static final long serialVersionUID = 1L;
        
        public boolean isCellEditable(int rowIndex, int colIndex) {
          return false;
        }
      };
    this.table.setSelectionMode(0);
    this.table.setRowSelectionAllowed(true);
    setViewportView(this.table);
  }
  
  void refreshView() {
    this.tableModel.setRowCount(0);
    for (Map m : this.group) {
      String[] row = { m.getCountryname(), Integer.toString(m.getScale())};
      this.tableModel.addRow((Object[])row);
    } 
  }
  
  int getSelectedIndex() {
    int index = this.table.getSelectedRow();
    if (index < 0)
      JOptionPane.showMessageDialog(this, "No one country has been choosed", "Error", 0); 
    return index;
  }
}
