package metier;

import algos.Algorithme;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import theoriegraphes.Canvas;
import theoriegraphes.MainFrame;


public class Configuration {
    public static boolean pondere = true;
    public static boolean oriente = true;
    public static int taille_arret = 2;
    public static int taille_sommet = 30;
    public static Color coleur_arret = Color.RED;
    public static Color coleur_sommet = Color.green;
    public static Color coleur_label = Color.BLACK;
    public static Color coleur_parcour = Color.blue;
    public static long sleep_time = 1000;
    public static double default_cout = 1;
    public static Algorithme current_algo = null;
    public static boolean pondere_positive = true;
    
    private static ArrayList<Sommet> sommets_bkp;
    private static ArrayList<Arret> arrets_bkp;
    
    public static ArrayList<BufferedImage> images = new ArrayList<>();
    
    public static void restore(){
        Canvas.getInstance().deselectionne();
        Canvas.getInstance().getGraphe().getSommets().clear();
        Canvas.getInstance().getGraphe().getSommets().addAll(sommets_bkp);
        Canvas.getInstance().getGraphe().getArrets().clear();
        Canvas.getInstance().getGraphe().getArrets().addAll(arrets_bkp);
        Canvas.getInstance().repaint();
    }
    public static void backup() {
        Canvas.getInstance().deselectionne();
        sommets_bkp = new ArrayList<>();
        for(Sommet s:Canvas.getInstance().getGraphe().getSommets()){
            sommets_bkp.add(new Sommet(s));
        }
        arrets_bkp = new ArrayList<>();
        for(Arret ar:Canvas.getInstance().getGraphe().getArrets()){
            arrets_bkp.add(new Arret(ar, getCopie(ar.getSommetA()), getCopie(ar.getSommetB())));
        }
        MainFrame.btn_restore.setEnabled(true);
    }
    public static Sommet getCopie(Sommet o){
        for(Sommet s:sommets_bkp){
            if(s.getPosition_x()==o.getPosition_x() && s.getPosition_y()==o.getPosition_y())return s;
        }
        return null;
    }

    public static void resetImages(){
        images = new ArrayList<BufferedImage>();
    }
    static void addScreen(BufferedImage screenShot) {
        images.add(screenShot);
    }
    public static void checkAlgos(){
        boolean canvasExists=false,graphExists=false,
        		sommetsExist=false,backedUp=false,
        		backupSommetsExist=false,selectionExists=false,
        		sourceExists=false,sinkExists=false;
        
        canvasExists = Canvas.getInstance()!=null;
        if(canvasExists) graphExists = Canvas.getInstance().getGraphe()!=null;
        if(graphExists) sommetsExist = Canvas.getInstance().getGraphe().getSommets().size()>0;
        backedUp = sommets_bkp != null;
        if(backedUp) backupSommetsExist = sommets_bkp.size()>0;
        if(canvasExists) selectionExists = Canvas.getInstance().getSelectionne()!=null;
        if(graphExists) sourceExists=Canvas.getInstance().getGraphe().getS()!=null;
        if(graphExists) sinkExists=Canvas.getInstance().getGraphe().getP()!=null;
        
        MainFrame.btn_matrice.setEnabled(canvasExists);
        MainFrame.btn_enregistrer.setEnabled(canvasExists && graphExists && sommetsExist);
        MainFrame.btn_restore.setEnabled(backedUp && backupSommetsExist);
        MainFrame.btn_bfs.setEnabled(sommetsExist && selectionExists);
        MainFrame.btn_dfs.setEnabled(sommetsExist && selectionExists);
        MainFrame.btn_wireshall.setEnabled(canvasExists && graphExists && sommetsExist);
        MainFrame.btn_prim.setEnabled(canvasExists && graphExists && sommetsExist && pondere);
        MainFrame.btn_kruskal.setEnabled(canvasExists && graphExists && sommetsExist && pondere);
        MainFrame.btn_dijikstra.setEnabled(canvasExists && graphExists && sommetsExist && selectionExists && pondere_positive && oriente);
        MainFrame.btn_billmanford.setEnabled(canvasExists && graphExists && sommetsExist && selectionExists && pondere && oriente);
        MainFrame.btn_fordfolkerson1.setEnabled(sourceExists && sinkExists && oriente && pondere);
        MainFrame.btn_fordfolkerson2.setEnabled(sourceExists && sinkExists && oriente && pondere);
        
        MainFrame.btn_welch_powell.setEnabled(canvasExists && sommetsExist && !oriente);
    }
}
