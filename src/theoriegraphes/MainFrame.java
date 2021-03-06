package theoriegraphes;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.sun.javafx.application.PlatformImpl;

import algos.BFS;
import algos.BellmanFord;
import algos.DFS;
import algos.Dijikstra;
import algos.FordFolkersonMarquage;
import algos.FordFolkersonResiduelle;
import algos.Kruscal;
import algos.Prim;
import algos.WelchPowell;
import algos.Wireshall;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import metier.Configuration;
import metier.Graphe;

public class MainFrame extends javax.swing.JFrame {
	private static MainFrame instance;
    
    
    public static MainFrame getInstance(){
        if(instance==null){
            instance = new MainFrame();
            instance.setBounds(100, 100, 1400, 800);
        }
        return instance;
    }
    
    private MainFrame() {
    	setIconImage(Toolkit.getDefaultToolkit().getImage(MainFrame.class.getResource("/assets/graph.png")));
    	setTitle("EasyGraph");
        initComponents();
        
        JMenuBar mb = new JMenuBar();
        
        JMenu fileMenu = new JMenu("Fichier");
    	JMenuItem openMenuItem = new JMenuItem("Ouvrir");
    	JMenuItem saveMenuItem = new JMenuItem("Enregistrer");
    	JMenuItem imageMenuItem = new JMenuItem("Enregistrer comme image");
    	JMenuItem exitMenuItem = new JMenuItem("Quitter");
    	
    	

    	openMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	openSave();
            }
        });
    	
    	saveMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	save();
            }
        });
    	
    	imageMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	saveImage();
            }
        });
    	

    	exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	exit();
            }
        });
        
    	fileMenu.add(openMenuItem);
    	fileMenu.add(saveMenuItem);
    	fileMenu.add(new JSeparator());
    	fileMenu.add(imageMenuItem);
    	fileMenu.add(new JSeparator());
    	fileMenu.add(exitMenuItem);
    	
    	mb.add(fileMenu);
    	

        JMenu helpMenu = new JMenu("Aide");
    	JMenuItem helpMenuItem = new JMenuItem("Mode d'emploi");
    	
    	helpMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	showHelp();
            }
        });
    	
    	helpMenu.add(helpMenuItem);
        
    	mb.add(helpMenu);
    	
        this.setJMenuBar(mb);
    }
    
    public void setCentrePanel(JPanel p){
        jScrollPane2.setViewportView(p);
    }
    
    public boolean openSave(){
		try {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		    fileChooser.setFileFilter(new FileNameExtensionFilter("*.graph", "graph"));
			
			int result = fileChooser.showOpenDialog(this);
			
			if (result != JFileChooser.APPROVE_OPTION) return false;
			
			File selectedFile = fileChooser.getSelectedFile();

			FileInputStream fis = new FileInputStream(selectedFile);

	        ObjectInputStream ois = new ObjectInputStream(fis);
	        Graphe newGraph = (Graphe) ois.readObject();
	        ois.close();
	        
	        System.out.println("Pondere:");
	        System.out.println(newGraph.isPondere());
	        
	        System.out.println("Oriente:");
	        System.out.println(newGraph.isOriente());
	        
	        Configuration.pondere = newGraph.isPondere();
	        Configuration.oriente = newGraph.isOriente();
	        
	        Canvas.getInstance().setGraphe(newGraph);
	        Canvas.getInstance().repaint();
	        return true;
		} catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Une erreur s'est produite lors la lecture de ce enregistrement.");
			e.printStackTrace();
			return false;
		}
    }
    public void save() {

    	try {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		    fileChooser.setFileFilter(new FileNameExtensionFilter("*.graph", "graph"));
		    
			int result = fileChooser.showOpenDialog(this);
			if (result != JFileChooser.APPROVE_OPTION) return;
			
			File selectedFile = new File(fileChooser.getSelectedFile() + ".graph");
			
			Graphe graph = Canvas.getInstance().getGraphe();
			graph.setOriente(Configuration.oriente);
			graph.setPondere(Configuration.pondere);
    		 
            FileOutputStream fileOut = new FileOutputStream(selectedFile);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(graph);
            objectOut.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Une erreur s'est produite lors de la tentative d'enregistrement");
        }
    	
    }
    
    public void exit() {
    	if(Canvas.getInstance() != null && 
    			Canvas.getInstance().getGraphe() != null &&
    			Canvas.getInstance().getGraphe().getSommets().size() > 0) {
    		
    		int input = JOptionPane.showConfirmDialog(null,
                    						"Vous avez du progres non enregistré, voulez-vous le sauvegarder?",
                							"Attention" ,JOptionPane.YES_NO_CANCEL_OPTION);
    		if(input == 2) return; // Annuler
    		
    		if(input == 0) // Oui
    			save();
    		
			System.exit(0);
    	}
    	
		System.exit(0);
    }
    
    public void saveImage() {
    	try {
			JFileChooser fileChooser = new JFileChooser();
		    fileChooser.setFileFilter(new FileNameExtensionFilter("*.png", "png"));
			fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
			
			int result = fileChooser.showOpenDialog(this);
			
			if (result != JFileChooser.APPROVE_OPTION) return;
			
			File selectedFile = fileChooser.getSelectedFile();
    		 
            Thread thread = new Thread(()->{
            	try {
            		Thread.sleep(1000);
					ImageIO.write(Canvas.getInstance().getCapture(), "png", new File(selectedFile.getAbsolutePath() + ".png"));
				} catch (IOException | AWTException | InterruptedException e) {
					e.printStackTrace();
				}
            });
            
            thread.start();
 
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Une erreur s'est produite lors de la tentative d'enregistrement");
        }
    }
    
    public static void showInitialMenu() {
    	MainFrame.getInstance().setVisible(true);
    	MenuInitial menuInitial = new MenuInitial();
		MainFrame.getInstance().setLocationRelativeTo(null);
    	menuInitial.setVisible(true);
    }


    private void initComponents() {

        jSplitPane1 = new javax.swing.JSplitPane();
        jSplitPane1.setResizeWeight(0.7);
        jSplitPane2 = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        btn_bfs = new javax.swing.JButton();
        btn_dfs = new javax.swing.JButton();
        btn_matrice = new javax.swing.JButton();
        btn_prim = new javax.swing.JButton();
        btn_kruskal = new javax.swing.JButton();
        btn_restore = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        btn_wireshall = new javax.swing.JButton();
        btn_dijikstra = new javax.swing.JButton();
        btn_billmanford = new javax.swing.JButton();
        btn_fordfolkerson1 = new javax.swing.JButton();
        btn_fordfolkerson2 = new javax.swing.JButton();
        btn_enregistrer = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jSlider1 = new javax.swing.JSlider();
        jLabel8 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        menuInitial1 = new theoriegraphes.MenuInitial();
        jSplitPane3 = new javax.swing.JSplitPane();
        jSplitPane3.setResizeWeight(0.5);
        jPanel5 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        console = new javax.swing.JTextArea();
        btn_exporter_pdf = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        btn_bfs.setText("BFS");
        btn_bfs.setEnabled(false);
        btn_bfs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_bfsActionPerformed(evt);
            }
        });

        btn_dfs.setText("DFS");
        btn_dfs.setEnabled(false);
        btn_dfs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_dfsActionPerformed(evt);
            }
        });

        btn_matrice.setText("Matrice d'Adjacence");
        btn_matrice.setEnabled(false);
        btn_matrice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_matriceActionPerformed(evt);
            }
        });

        btn_prim.setText("Prim");
        btn_prim.setEnabled(false);
        btn_prim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_primActionPerformed(evt);
            }
        });

        btn_kruskal.setText("Kruskal");
        btn_kruskal.setEnabled(false);
        btn_kruskal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_kruskalActionPerformed(evt);
            }
        });

        btn_restore.setText("Restaurer");
        btn_restore.setEnabled(false);
        btn_restore.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_restoreActionPerformed(evt);
            }
        });

        jButton7.setText("arretes");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jLabel4.setBackground(Color.RED);
        jLabel4.setOpaque(true);

        jLabel5.setBackground(Color.green);
        jLabel5.setOpaque(true);

        jButton8.setText("sommets");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jButton9.setText("label");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jLabel6.setBackground(Color.black);
        jLabel6.setOpaque(true);

        btn_wireshall.setText("Wireshall");
        btn_wireshall.setEnabled(false);
        btn_wireshall.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_wireshallActionPerformed(evt);
            }
        });

        btn_dijikstra.setText("Dijekstra");
        btn_dijikstra.setEnabled(false);
        btn_dijikstra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_dijikstraActionPerformed(evt);
            }
        });

        btn_billmanford.setText("Billman Ford");
        btn_billmanford.setEnabled(false);
        btn_billmanford.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_billmanfordActionPerformed(evt);
            }
        });

        btn_fordfolkerson1.setText("Ford Fullkirson (marquage)");
        btn_fordfolkerson1.setEnabled(false);
        btn_fordfolkerson1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_fordfolkerson1ActionPerformed(evt);
            }
        });

        btn_fordfolkerson2.setText("Ford Fullkirson (residuel)");
        btn_fordfolkerson2.setEnabled(false);
        btn_fordfolkerson2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_fordfolkerson2ActionPerformed(evt);
            }
        });

        btn_enregistrer.setText("Sauvegarder");
        btn_enregistrer.setEnabled(false);
        btn_enregistrer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_enregistrerActionPerformed(evt);
            }
        });

        jButton2.setText("BFS/DFS");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel7.setBackground(Color.blue);
        jLabel7.setOpaque(true);

        jSlider1.setMaximum(5000);
        jSlider1.setValue(1000);
        jSlider1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider1StateChanged(evt);
            }
        });

        jLabel8.setText("Delai d'animation:");

        jButton4.setText("Recommencer");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        
        btn_welch_powell = new JButton();
        btn_welch_powell.setEnabled(false);
        btn_welch_powell.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
            
        		new Thread(new WelchPowell(Canvas.getInstance().getGraphe())).start();
                
        	}
        });
        btn_welch_powell.setText("Welch et Powell");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1Layout.setHorizontalGroup(
        	jPanel1Layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(jPanel1Layout.createSequentialGroup()
        			.addContainerGap()
        			.addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING)
        				.addComponent(jButton4, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 231, Short.MAX_VALUE)
        				.addComponent(btn_bfs, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 231, Short.MAX_VALUE)
        				.addComponent(btn_dfs, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 231, Short.MAX_VALUE)
        				.addComponent(btn_matrice, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 231, Short.MAX_VALUE)
        				.addComponent(btn_prim, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 231, Short.MAX_VALUE)
        				.addComponent(btn_kruskal, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 231, Short.MAX_VALUE)
        				.addGroup(Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
        					.addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING)
        						.addGroup(jPanel1Layout.createSequentialGroup()
        							.addComponent(jButton7)
        							.addPreferredGap(ComponentPlacement.RELATED)
        							.addComponent(jLabel4, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)
        							.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        							.addComponent(jLabel5, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE))
        						.addGroup(jPanel1Layout.createSequentialGroup()
        							.addComponent(jButton2)
        							.addPreferredGap(ComponentPlacement.RELATED)
        							.addComponent(jLabel7)
        							.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        							.addComponent(jLabel6)))
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING, false)
        						.addComponent(jButton9, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        						.addComponent(jButton8, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        				.addComponent(btn_wireshall, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 231, Short.MAX_VALUE)
        				.addComponent(btn_dijikstra, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 231, Short.MAX_VALUE)
        				.addComponent(btn_billmanford, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 231, Short.MAX_VALUE)
        				.addComponent(btn_fordfolkerson1, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 231, Short.MAX_VALUE)
        				.addComponent(btn_fordfolkerson2, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 231, Short.MAX_VALUE)
        				.addGroup(Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
        					.addComponent(btn_restore, GroupLayout.PREFERRED_SIZE, 94, GroupLayout.PREFERRED_SIZE)
        					.addGap(18)
        					.addComponent(btn_enregistrer, GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE))
        				.addGroup(Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
        					.addComponent(jLabel8, GroupLayout.DEFAULT_SIZE, 128, Short.MAX_VALUE)
        					.addPreferredGap(ComponentPlacement.UNRELATED)
        					.addComponent(jSlider1, GroupLayout.PREFERRED_SIZE, 91, GroupLayout.PREFERRED_SIZE))
        				.addComponent(btn_welch_powell, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 231, GroupLayout.PREFERRED_SIZE))
        			.addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
        	jPanel1Layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(jPanel1Layout.createSequentialGroup()
        			.addContainerGap()
        			.addComponent(btn_matrice)
        			.addPreferredGap(ComponentPlacement.UNRELATED)
        			.addGroup(jPanel1Layout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(btn_restore)
        				.addComponent(btn_enregistrer))
        			.addGap(18)
        			.addComponent(btn_bfs)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(btn_dfs)
        			.addPreferredGap(ComponentPlacement.UNRELATED)
        			.addComponent(btn_prim)
        			.addPreferredGap(ComponentPlacement.UNRELATED)
        			.addComponent(btn_kruskal)
        			.addPreferredGap(ComponentPlacement.UNRELATED)
        			.addComponent(btn_wireshall)
        			.addPreferredGap(ComponentPlacement.UNRELATED)
        			.addComponent(btn_dijikstra)
        			.addPreferredGap(ComponentPlacement.UNRELATED)
        			.addComponent(btn_billmanford)
        			.addPreferredGap(ComponentPlacement.UNRELATED)
        			.addComponent(btn_fordfolkerson1)
        			.addPreferredGap(ComponentPlacement.UNRELATED)
        			.addComponent(btn_fordfolkerson2)
        			.addGap(18)
        			.addComponent(btn_welch_powell)
        			.addGap(27)
        			.addComponent(jButton4)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING, false)
        				.addComponent(jSlider1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        				.addComponent(jLabel8, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addGroup(jPanel1Layout.createParallelGroup(Alignment.TRAILING)
        				.addGroup(jPanel1Layout.createParallelGroup(Alignment.BASELINE)
        					.addComponent(jButton9)
        					.addComponent(jLabel6))
        				.addGroup(jPanel1Layout.createParallelGroup(Alignment.BASELINE)
        					.addComponent(jButton2)
        					.addComponent(jLabel7)))
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING)
        				.addComponent(jLabel4)
        				.addComponent(jButton7)
        				.addComponent(jLabel5, GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
        				.addComponent(jButton8))
        			.addContainerGap())
        );
        jPanel1Layout.linkSize(SwingConstants.VERTICAL, new Component[] {btn_restore, btn_enregistrer});
        jPanel1Layout.linkSize(SwingConstants.VERTICAL, new Component[] {jButton7, jLabel4, jLabel5, jLabel6, jLabel7});
        jPanel1Layout.linkSize(SwingConstants.HORIZONTAL, new Component[] {jButton7, jButton8, jButton2});
        jPanel1Layout.linkSize(SwingConstants.HORIZONTAL, new Component[] {jLabel4, jLabel5, jLabel6, jLabel7});
        jPanel1.setLayout(jPanel1Layout);

        jSplitPane2.setLeftComponent(jPanel1);

//        jScrollPane2.setViewportView(menuInitial1);

        jSplitPane2.setRightComponent(jScrollPane2);

        jSplitPane1.setLeftComponent(jSplitPane2);

        jSplitPane3.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jScrollPane1.setBorder(javax.swing.BorderFactory.createTitledBorder("Trace"));

        console.setEditable(false);
        console.setColumns(20);
        console.setRows(5);
        jScrollPane1.setViewportView(console);

        btn_exporter_pdf.setText("Exporter PDF");
        btn_exporter_pdf.setEnabled(false);
        btn_exporter_pdf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_exporter_pdfActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(btn_exporter_pdf, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 344, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn_exporter_pdf)
                .addContainerGap())
        );

        jSplitPane3.setRightComponent(jPanel5);

        jSplitPane1.setRightComponent(jSplitPane3);
        
        panel = new JPanel();
        jSplitPane3.setLeftComponent(panel);
        panel.setLayout(new BorderLayout(0, 0));
        
        infoTable = new JTable();
        panel.add(infoTable);
        infoTable.setShowVerticalLines(false);
        infoTable.setShowHorizontalLines(false);
        infoTable.setShowGrid(false);
        infoTable.setTableHeader(null);
        infoTable.setFont(new Font("Roboto", Font.PLAIN, 15));
        infoTable.setEnabled(false);
        infoTable.setModel(new DefaultTableModel(
        	new Object[][] {
        		{"Nombre des sommets", null},
        		{"Nombre d'arrets", null},
        		{"Densit\u00E9", null},
        		{"Connexe", null},
        		{"Complet", null},
        	},
        	new String[] {
        		"New column", "New column"
        	}
        ));
        
        JButton btnModeDemploi = new JButton("Mode d'emploi");
        btnModeDemploi.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
			  showHelp();
        	}
        });
        panel.add(btnModeDemploi, BorderLayout.NORTH);
        infoTable.getColumnModel().getColumn(1).setPreferredWidth(15);
        infoTable.getColumnModel().getColumn(1).setMinWidth(5);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1228, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSplitPane1)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    public void showHelp() {
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

    private void btn_matriceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_matriceActionPerformed
        new MatriceAdj(this, Canvas.getInstance().getGraphe().matriceAdj(), Canvas.getInstance().getGraphe().getSommets()).setVisible(true);
    }//GEN-LAST:event_btn_matriceActionPerformed

    private void btn_bfsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_bfsActionPerformed
        if(Canvas.getInstance().getSelectionne()!=null){
            new Thread(new BFS(Canvas.getInstance().getGraphe(), Canvas.getInstance().getSelectionne())).start();
        }
        
    }//GEN-LAST:event_btn_bfsActionPerformed

    private void btn_dfsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_dfsActionPerformed
        if(Canvas.getInstance().getSelectionne()!=null){
            new Thread(new DFS(Canvas.getInstance().getGraphe(), Canvas.getInstance().getSelectionne())).start();
        }
    }//GEN-LAST:event_btn_dfsActionPerformed

    private void btn_primActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_primActionPerformed
        new Thread(new Prim(Canvas.getInstance().getGraphe())).start();
    }//GEN-LAST:event_btn_primActionPerformed

    private void btn_kruskalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_kruskalActionPerformed
        new Thread(new Kruscal(Canvas.getInstance().getGraphe())).start();
    }//GEN-LAST:event_btn_kruskalActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
        JColorChooser jcc = new JColorChooser(Configuration.coleur_arret);
        JColorChooser.createDialog(MainFrame.getInstance(), "choisir une coleur", true, jcc, null, null).setVisible(true);
        Configuration.coleur_arret = jcc.getColor();
        jLabel4.setBackground(jcc.getColor());
        if(Canvas.getInstance()!=null)Canvas.getInstance().getGraphe().repaint();
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        JColorChooser jcc = new JColorChooser(Configuration.coleur_sommet);
        JColorChooser.createDialog(MainFrame.getInstance(), "choisir une coleur", true, jcc, null, null).setVisible(true);
        Configuration.coleur_sommet = jcc.getColor();
        jLabel5.setBackground(jcc.getColor());
        if(Canvas.getInstance()!=null)Canvas.getInstance().getGraphe().repaint();
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        JColorChooser jcc = new JColorChooser(Configuration.coleur_label);
        JColorChooser.createDialog(MainFrame.getInstance(), "choisir une coleur", true, jcc, null, null).setVisible(true);
        Configuration.coleur_label = jcc.getColor();
        jLabel6.setBackground(jcc.getColor());
        if(Canvas.getInstance()!=null)Canvas.getInstance().getGraphe().repaint();
    }//GEN-LAST:event_jButton9ActionPerformed

    private void btn_wireshallActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_wireshallActionPerformed
        new Thread(new Wireshall(Canvas.getInstance().getGraphe())).start();
    }//GEN-LAST:event_btn_wireshallActionPerformed

    private void btn_restoreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_restoreActionPerformed
          Configuration.restore();
    }//GEN-LAST:event_btn_restoreActionPerformed

    private void btn_dijikstraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_dijikstraActionPerformed
        if(Canvas.getInstance().getSelectionne()!=null){
            new Thread(new Dijikstra(Canvas.getInstance().getGraphe(), Canvas.getInstance().getSelectionne())).start();
        }else{
            
        }
    }//GEN-LAST:event_btn_dijikstraActionPerformed

    private void btn_exporter_pdfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_exporter_pdfActionPerformed
        JFileChooser file_chooser = new JFileChooser();
        file_chooser.setFileFilter(new FileNameExtensionFilter("Fichiers pdf", new String[]{"pdf","PDF"}));
        String chemin=null;
        
        if(file_chooser.showDialog(MainFrame.instance, "Exporter vers")==0) {
	        if(file_chooser.getSelectedFile().exists()) {       	
	        	if(JOptionPane.showConfirmDialog(MainFrame.instance, "Voulez vous vraiments ecraser lecontenu du fichier!!", "Fichier deja Exist!!!", JOptionPane.WARNING_MESSAGE )!=0)return;
	        	chemin = file_chooser.getSelectedFile().getAbsolutePath();
	        }
	        if(chemin==null)chemin = file_chooser.getSelectedFile().getAbsolutePath()+".pdf";
            try {
                FileOutputStream fos = new FileOutputStream(new File(chemin));
                Document document = new Document();
                PdfWriter.getInstance(document, fos);
                document.open();
                document.add(new Paragraph("Rapport d'application d'algorithme: "+Configuration.current_algo.getNom()));
                File fichier_tmp = File.createTempFile("tmp", ".png");
                BufferedImage bf=Configuration.images.get(0);
                ImageIO.write(bf, "png", fichier_tmp);
                document.add(new Paragraph("Graphe initial: "));
                document.add(Image.getInstance(fichier_tmp.toURI().toURL()));
                document.add(new Paragraph("Trace: "));
                document.add(new Paragraph(Configuration.current_algo.getTrace().toString()));
                document.add(new Paragraph("Trace - image pour chaque etape: "));
                for(int i=1;i<Configuration.images.size();i++){
                    ImageIO.write(Configuration.images.get(i), "png", fichier_tmp);
                    document.add(Image.getInstance(fichier_tmp.toURI().toURL()));
                }
                document.close();
            } catch (DocumentException | FileNotFoundException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
              
                
        }
        
    }//GEN-LAST:event_btn_exporter_pdfActionPerformed

    private void btn_enregistrerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_enregistrerActionPerformed
        Configuration.backup();
    }//GEN-LAST:event_btn_enregistrerActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        JColorChooser jcc = new JColorChooser(Configuration.coleur_label);
        JColorChooser.createDialog(MainFrame.getInstance(), "choisir une coleur", true, jcc, null, null).setVisible(true);
        Configuration.coleur_parcour = jcc.getColor();
        jLabel7.setBackground(jcc.getColor());
        if(Canvas.getInstance()!=null)Canvas.getInstance().getGraphe().repaint();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jSlider1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider1StateChanged
        Configuration.sleep_time = jSlider1.getValue();
    }//GEN-LAST:event_jSlider1StateChanged

    private void btn_billmanfordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_billmanfordActionPerformed
        if(Canvas.getInstance().getSelectionne()!=null){
            new Thread(new BellmanFord(Canvas.getInstance().getGraphe(), Canvas.getInstance().getSelectionne())).start();
        }else{
            
        }
    }//GEN-LAST:event_btn_billmanfordActionPerformed

    private void btn_fordfolkerson2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_fordfolkerson2ActionPerformed
        new Thread(new FordFolkersonResiduelle(Canvas.getInstance().getGraphe(), Canvas.getInstance().getGraphe().getS(),Canvas.getInstance().getGraphe().getP())).start();
    }//GEN-LAST:event_btn_fordfolkerson2ActionPerformed

    private void btn_fordfolkerson1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_fordfolkerson1ActionPerformed
    	new Thread(new FordFolkersonMarquage(Canvas.getInstance().getGraphe(), Canvas.getInstance().getGraphe().getS(),Canvas.getInstance().getGraphe().getP())).start();
    }//GEN-LAST:event_btn_fordfolkerson1ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        showInitialMenu();
    }//GEN-LAST:event_jButton4ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                showInitialMenu();
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JButton btn_bfs;
    public static javax.swing.JButton btn_billmanford;
    public static javax.swing.JButton btn_dfs;
    public static javax.swing.JButton btn_dijikstra;
    public static javax.swing.JButton btn_enregistrer;
    public static javax.swing.JButton btn_exporter_pdf;
    public static javax.swing.JButton btn_fordfolkerson1;
    public static javax.swing.JButton btn_fordfolkerson2;
    public static javax.swing.JButton btn_welch_powell;
    public static javax.swing.JButton btn_kruskal;
    public static javax.swing.JButton btn_matrice;
    public static javax.swing.JButton btn_prim;
    public static javax.swing.JButton btn_restore;
    public static javax.swing.JButton btn_wireshall;
    public static javax.swing.JTextArea console;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSlider jSlider1;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JSplitPane jSplitPane3;
    private theoriegraphes.MenuInitial menuInitial1;
    private JPanel panel;
    public JTable infoTable;
}
