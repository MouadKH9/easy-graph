package theoriegraphes;

import java.awt.AWTException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import metier.Configuration;
import metier.Graphe;
import metier.Sommet;


public class Canvas extends javax.swing.JPanel {
    public static Canvas instance;
    public static Canvas getInstance(){
        return instance;
    }
    
    private Graphe graphe;
    private Sommet selectionne;
    private boolean covered;
    private boolean deplacement;
    private final JPopupMenu popup;

    public Canvas() {
        graphe = new Graphe();
        popup = new JPopupMenu();
        JMenuItem supprimer = new JMenuItem("Supprimer");
        supprimer.addActionListener((ActionEvent e) -> {
            System.out.println("supprimer");
            graphe.removeSommet(selectionne);
            deselectionne();
            covered=false;
            repaint();
        });
        JMenuItem informations = new JMenuItem("Informations");
        informations.addActionListener((ActionEvent e) -> {
            Information dialog = new Information(MainFrame.getInstance(), graphe,selectionne);
            dialog.setVisible(true);
            deselectionne();
            covered=false;
        });
        popup.add(supprimer);
        popup.add(informations);
        popup.setInvoker(this);
        instance = this;
        initComponents();
        Configuration.checkAlgos();
    }

    public Graphe getGraphe() {
        return graphe;
    }

    public void setGraphe(Graphe graphe) {
        this.graphe = graphe;
    }

    public Sommet getSelectionne() {
        return selectionne;
    }

    public void setSelectionne(Sommet selectionne) {
        this.selectionne = selectionne;
    }

    public boolean isCovered() {
        return covered;
    }

    public void setCovered(boolean covered) {
        this.covered = covered;
    }

    public boolean isDeplacement() {
        return deplacement;
    }

    public void setDeplacement(boolean deplacement) {
        this.deplacement = deplacement;
    } 

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        if(graphe!=null)graphe.draw(g2d);
    }
    public void deselectionne(){
        if(selectionne!=null){
            selectionne.setSelected(false);
            selectionne = null;
        }
    }
    
    public void screenShot(){
        try {
            Configuration.images.add(getCapture());
        } catch (AWTException ex) {
            Logger.getLogger(Canvas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public BufferedImage getCapture() throws AWTException {
    	Point l = getLocationOnScreen();       
        Rectangle g_rec = graphe.getBounds();
        Rectangle screenRect = new Rectangle(g_rec.x+l.x, g_rec.y+l.y, g_rec.width, g_rec.height);
        BufferedImage capture;
        capture = new Robot().createScreenCapture(screenRect);
        return capture;
    }


    private void initComponents() {


        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
            	formMouseDragged(evt);
            }
        });
        
        
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1124, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 709, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        if(covered){
            covered=false;
            deselectionne();
            return;
        }
        if(SwingUtilities.isLeftMouseButton(evt)) clickGauch(evt);
        else if(SwingUtilities.isRightMouseButton(evt)) clickDroit(evt);
    }//GEN-LAST:event_formMouseClicked
    
    private void formMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
    	
    	// On veut deplacer le sommet seulement si le bouton gauche est cliquer.
    	if(!SwingUtilities.isLeftMouseButton(evt)) return;
    	
        Sommet s = graphe.getSommetAtPosition(evt.getX(), evt.getY());
        
    	if(s!=null){
            s.setPosition_x(evt.getX());
            s.setPosition_y(evt.getY());
            repaint();
        }
    }


    private void clickGauch(java.awt.event.MouseEvent evt){
        Sommet s = graphe.getSommetAtPosition(evt.getX(), evt.getY());
        if(s!=null){// un sommet a ete selectionne
            if(deplacement){//on ete deja on mode de deplacement et maintenant on doit arreter le deplacement
                deplacement = false;
                selectionne=null;
                return;
            }
            if(selectionne==null){//si on a rien selectionne avant alors maintenant on selectionne
                selectionne = s;
                selectionne.setSelected(true);
            }else{// sinon on ajoute un arret qui lie les deux sommets selectionnes
                selectionne.setSelected(false);
                graphe.addArret(selectionne, s);
                deselectionne();
            }
        }else{// l'emplacement du click est vide alors on va ajouter un nouveau sommet
            if(selectionne!=null)
                deselectionne();
            else graphe.addSommet(evt.getX(), evt.getY());
        }
        repaint();
    }
    private void clickDroit(java.awt.event.MouseEvent evt){
        Sommet s = graphe.getSommetAtPosition(evt.getX(), evt.getY());
        if(s!=null){//click droit sur un sommet: on affiche menu popup
            deselectionne();
            repaint();
            selectionne = s;
            covered=true;
            popup.setLocation(evt.getXOnScreen(), evt.getYOnScreen());
            popup.setVisible(true);
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
