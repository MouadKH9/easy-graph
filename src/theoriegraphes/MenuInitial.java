/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package theoriegraphes;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;

import com.sun.javafx.application.PlatformImpl;

import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import metier.Configuration;

public class MenuInitial extends JDialog {
	
	private JCheckBox chckbxOriente;
	private JCheckBox chckbxPondere;
	
	public MenuInitial() {
		setBounds(100, 100, 773, 400);
		setResizable(false);
		getContentPane().setLayout(null);
		
		JLabel lblBienvenueA = new JLabel("Bienvenue \u00E0 EasyGraph ");
		lblBienvenueA.setFont(new Font("Roboto", Font.PLAIN, 24));
		lblBienvenueA.setBounds(255, 5, 262, 48);
		getContentPane().add(lblBienvenueA);
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setBounds(50, 74, 662, 220);
		getContentPane().add(splitPane);
		
		JPanel openPanel = new JPanel();
		splitPane.setLeftComponent(openPanel);
		
		JButton openButton = new JButton("Ouvrir un graphe");
		openButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				openGraph();
			}
		});
		openButton.setFont(new Font("Tahoma", Font.PLAIN, 18));
		ImageIcon icon = new ImageIcon(MenuInitial.class.getResource("/assets/open.png"));
		Image img = icon.getImage() ;  
		Image newimg = img.getScaledInstance( 75, 75,  java.awt.Image.SCALE_SMOOTH);  
		icon = new ImageIcon( newimg );
		openPanel.setLayout(new BorderLayout(0, 0));
		openButton.setIcon(icon);
		openButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		openButton.setHorizontalTextPosition(SwingConstants.CENTER);
		openPanel.add(openButton);
		
		Component verticalStrut = Box.createVerticalStrut(40);
		openPanel.add(verticalStrut, BorderLayout.SOUTH);
		
		Component verticalStrut_1 = Box.createVerticalStrut(40);
		openPanel.add(verticalStrut_1, BorderLayout.NORTH);
		
		Component horizontalStrut = Box.createHorizontalStrut(30);
		openPanel.add(horizontalStrut, BorderLayout.WEST);
		
		Component horizontalStrut_1 = Box.createHorizontalStrut(30);
		openPanel.add(horizontalStrut_1, BorderLayout.EAST);
		
		JPanel newPanel = new JPanel();
		splitPane.setRightComponent(newPanel);
		newPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_1 = new JPanel();
		newPanel.add(panel_1, BorderLayout.NORTH);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		JLabel lblNewLabel = new JLabel("Options de graphe:");
		lblNewLabel.setHorizontalAlignment(SwingConstants.LEFT);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		panel_1.add(lblNewLabel);
		
		Component horizontalStrut_2 = Box.createHorizontalStrut(40);
		panel_1.add(horizontalStrut_2, BorderLayout.WEST);
		
		Component verticalStrut_2 = Box.createVerticalStrut(20);
		panel_1.add(verticalStrut_2, BorderLayout.SOUTH);
		
		JPanel panel = new JPanel();
		newPanel.add(panel, BorderLayout.CENTER);
		
		chckbxOriente = new JCheckBox("Orient\u00E9");
		chckbxOriente.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panel.add(chckbxOriente);
		
		chckbxPondere = new JCheckBox("Pond\u00E9r\u00E9");
		chckbxPondere.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panel.add(chckbxPondere);
		
		JPanel panel_2 = new JPanel();
		
		JButton newButton = new JButton("Créer le graphe");
		newButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createGraph();
			}
		});
		ImageIcon newIcon = new ImageIcon(MenuInitial.class.getResource("/assets/new.png"));
		Image newImg = newIcon.getImage() ;  
		Image newNewimg = newImg.getScaledInstance( 75, 75,  java.awt.Image.SCALE_SMOOTH);  
		newIcon = new ImageIcon( newNewimg );
		newButton.setLayout(new BorderLayout(0, 0));
		newButton.setIcon(newIcon);
		newButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		newButton.setHorizontalTextPosition(SwingConstants.CENTER);
		newButton.setFont(new Font("Tahoma", Font.PLAIN, 18));
		panel_2.add(newButton, BorderLayout.CENTER);
		
		newPanel.add(panel_2, BorderLayout.SOUTH);
		
		
		JButton helpButton = new JButton("Besoin de l'aide?");
		helpButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
		    	try {
					PlatformImpl.startup(() -> {
					    	JFXPanel fxPanel;
							WebView wv;
							JFrame frame;
					        fxPanel = new JFXPanel ();
					    	wv = new WebView ();
					    	URL url = this.getClass().getResource("/docs/index.html");
					    	wv.getEngine().load(url.toString());
							fxPanel.setScene ( new Scene ( wv, 1200, 750 ) );
							frame = new JFrame ("Mode d'emploi");
				            frame.add ( new JScrollPane ( fxPanel ) );
				            frame.setVisible ( true );
				            frame.pack ();
			        });
				
				} catch ( Exception ex ) {
					ex.printStackTrace();
				}
			}
		});
		helpButton.setBounds(316, 315, 127, 25);
		setLocationRelativeTo(null);
		getContentPane().add(helpButton);
	}
	
	public void openGraph() {
		MainFrame.getInstance().setCentrePanel(new Canvas());
		
        if(MainFrame.getInstance().openSave()) this.setVisible(false);
        
        Configuration.checkAlgos();
	}
	
	public void createGraph() {
		Configuration.oriente = chckbxOriente.isSelected();
        Configuration.pondere = chckbxPondere.isSelected();
        MainFrame.getInstance().setCentrePanel(new Canvas());
        Configuration.checkAlgos();
        
        this.setVisible(false);
	}
}
