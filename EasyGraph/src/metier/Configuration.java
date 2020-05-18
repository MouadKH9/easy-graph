/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metier;

import algos.Algorithme;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import theoriegraphes.Canvas;
import theoriegraphes.MainFrame;


/**
 *
 * @author abdel
 */
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
//        if(current_algo!=null){
//            try{
//                current_algo.interupt();
//            }catch(Exception e){}
//            
//        }
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
        boolean c1=false,c2=false,c3=false,c4=false,c5=false,c6=false,c7=false,c8=false;
        c1 = Canvas.getInstance()!=null;
        if(c1)c2 = Canvas.getInstance().getGraphe()!=null;
        if(c2)c3 = Canvas.getInstance().getGraphe().getSommets().size()>0;
        c4 = sommets_bkp!=null;
        if(c4)c5 = sommets_bkp.size()>0;
        if(c1)c6 = Canvas.getInstance().getSelectionne()!=null;
        if(c2)c7=Canvas.getInstance().getGraphe().getS()!=null;
        if(c2)c8=Canvas.getInstance().getGraphe().getP()!=null;
        MainFrame.btn_matrice.setEnabled(c1);
        MainFrame.btn_enregistrer.setEnabled(c1 && c2 && c3);
        MainFrame.btn_restore.setEnabled(c4 && c5);
        MainFrame.btn_bfs.setEnabled(c3 && c6);
        MainFrame.btn_dfs.setEnabled(c3 && c6);
        MainFrame.btn_wireshall.setEnabled(c1 && c2 && c3);
        MainFrame.btn_prim.setEnabled(c1 && c2 && c3);
        MainFrame.btn_kruskal.setEnabled(c1 && c2 && c3);
        MainFrame.btn_dijikstra.setEnabled(c1 && c2 && c3 && c6 && pondere_positive && oriente);
        MainFrame.btn_billmanford.setEnabled(c1 && c2 && c3 && c6 && pondere && oriente);
        MainFrame.btn_fordfolkerson2.setEnabled(c7 && c8 && oriente && pondere);
    }
}
