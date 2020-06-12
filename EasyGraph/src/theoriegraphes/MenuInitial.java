/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package theoriegraphes;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.Box;
import javax.swing.JCheckBox;
import java.awt.GridLayout;

public class MenuInitial extends JDialog {
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
		
		JCheckBox chckbxOriente = new JCheckBox("Oriente");
		chckbxOriente.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panel.add(chckbxOriente);
		
		JCheckBox chckbxPondere = new JCheckBox("Pondere");
		chckbxPondere.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panel.add(chckbxPondere);
		
		JPanel panel_2 = new JPanel();
		
		JButton newButton = new JButton("Créer le graphe");
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
			}
		});
		helpButton.setBounds(316, 315, 127, 25);
		setLocationRelativeTo(null);
		getContentPane().add(helpButton);
	}
}
