/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algos;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import metier.Configuration;
import metier.Graphe;
import metier.Sommet;
import theoriegraphes.Canvas;

/**
 *
 * @author abdel
 */
public class WelchPowell extends Algorithme{
	
	public static Color colors[] = {
			Color.MAGENTA,
			Color.ORANGE,
			Color.RED,
			Color.CYAN,
			Color.YELLOW,
			Color.PINK,
			Color.BLUE,
			Color.GRAY
	};

    public WelchPowell(Graphe g) {
        super("Welch et Powell", g);
    }

    @Override
    public void preRun() {
        Configuration.resetImages();
        Canvas.getInstance().screenShot();
        Configuration.backup();
        Configuration.current_algo=this;
        trace.append("Algorithme: "+nom+" Debut\n");
        Canvas.getInstance().repaint();
        Canvas.getInstance().screenShot();
        
    }

    @Override
    public void run() {
        preRun();
        
        ArrayList<Sommet> listSommets = new ArrayList<Sommet>(g.getSommets());
        
        // Trier les sommets selon leurs degre et alphabetiquement
        listSommets.sort((s1,s2)->{
        	if(g.degree(s1) == g.degree(s2))
        		return s1.getLabel().compareTo(s2.getLabel());
        	return (int) (g.degree(s2) - g.degree(s1));
        });
        
        trace.append("\nListe ordonnée: \n");
        for (Sommet sommet : listSommets) {
			trace.append(" " + sommet);
		}
        
        Sommet current;
        while(!listSommets.isEmpty()) {
            Iterator<Sommet> it = listSommets.iterator();
            try {
                Thread.sleep(Configuration.sleep_time);
            } catch (InterruptedException ex) {
                Logger.getLogger(WelchPowell.class.getName()).log(Level.SEVERE, null, ex);
            }
            Canvas.getInstance().screenShot();
    		trace.append("\nCouleur " + (iteration+1) +" pour: ");
        	while(it.hasNext()) {
        		current = it.next();
        		current.setSelected(true);
        		boolean found = false;
        		for (Sommet sommet : g.getVoisins(current)) {
    				if(sommet.getCouleur() == colors[iteration]) {
    					found = true;
    					break;
    				}
    			}	
        		if(found) continue;
        		current.setCouleur(colors[iteration]);
        		trace.append(" "+current);
        		Canvas.getInstance().repaint();
        		it.remove();
        	}

    		iteration++;
        }
        
        trace.append("\n\nNombre de coleur minimum: " + iteration +"\n\n");
        
        postRun();
    }
    
    
    
    @Override
    public void postRun() {
        trace.append("--graphe final: |V|= "+g.getV()+", |E|= "+g.getE()+", Densite= "+g.getDensite()+"\n");
        trace.append("Algorithme: "+nom+" Fin.\n");
        Canvas.getInstance().repaint();
        Canvas.getInstance().screenShot();
    }
    
}
