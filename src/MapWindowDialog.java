import java.awt.Color;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

//Map Application
//Author: Maksim Zakharau, 256629 
//Data: October 2020;

public class MapWindowDialog extends JDialog implements ActionListener {
	
	private static final long serialVersionUID = 1L;

	
	private Map map;

	
	Font font = new Font("MonoSpaced", Font.BOLD, 12);
	
	JLabel countrynameLabel = new JLabel("   Country: ");
	JLabel scaleLabel       = new JLabel("   Scale: ");
	JLabel infoLabel = new JLabel("ONLY BELARUS, POLAND AND GERMANY COUNTRIES IS SUPPORTED");
	
	JTextField countrynameField = new JTextField(10);
	JTextField scaleField = new JTextField(10);
	

	
	JButton OKButton = new JButton("  OK  ");
	JButton CancelButton = new JButton("Cancel");
	
	
	
	private MapWindowDialog(Window parent, Map map) {
		super(parent, Dialog.ModalityType.DOCUMENT_MODAL);
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(400, 300);
		setLocationRelativeTo(parent);
		
		this.map = map;
		
		if (map==null){
			setTitle("New map");
		} else{
			setTitle(map.toString());
			
			countrynameField.setText(map.getCountryname());
			scaleField.setText("" + map.getScale());
			
		}
		
		
		OKButton.addActionListener( this );
		CancelButton.addActionListener( this );
		
		
		JPanel panel = new JPanel();
		
		
		panel.setBackground(Color.yellow);

		
		panel.add(countrynameLabel);
		panel.add(countrynameField);
		
		panel.add(scaleLabel);
		panel.add(scaleField);
		
		panel.add(infoLabel);
		
	
		panel.add(OKButton);
		panel.add(CancelButton);
		
		
		setContentPane(panel);
		
		
		
		setVisible(true);
	}
	
	
	@Override
	public void actionPerformed(ActionEvent event) {
		
		Object source = event.getSource();
		
		if (source == OKButton) {
			try {
				if (map == null) { 
					map = new Map(countrynameField.getText(), Integer.parseInt(scaleField.getText()));
				} else {
					try {
					map.setCountryname(countrynameField.getText().toUpperCase());
					}
					catch (MapException e)
					{
						JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					}
					map.setScale(Integer.parseInt(scaleField.getText()));
				}
			
				map.setCountry(countrynameField.getText());
				map.setCitylist(map.createcityarray(map.getCountry()));
				
				
				dispose();
			} catch (MapException e) {
				
				JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
		}

		if (source == CancelButton) {
			dispose();
		}
	}
	

	
	public static Map createNewMap(Window parent) {
		MapWindowDialog dialog = new MapWindowDialog(parent, null);
		return dialog.map;
	}

	
	
	public static void changeMapData(Window parent, Map map) {
		new MapWindowDialog(parent, map);
	}

}


