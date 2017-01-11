/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Class;

/**
 *
 * @author Quentin
 */
public class CapteurInterieur extends Capteur implements Comparable<CapteurInterieur>{

    /* Convention pour la localisation  Batiment/Etage/salle*/
    
   
    private Localisation localisation;
    
    public Localisation getLocalisation() {
        return localisation;
    }

    public CapteurInterieur(TypeCapteur type ,Localisation localisation,String uniteDeMesure, String identifant, Intervalle i, String date, float precision, float marge, Integer frequence,float v) {
        super(type,uniteDeMesure, identifant, i, date, precision, marge, frequence,v);
        this.localisation = localisation;
    }

    @Override
    public int compareTo(CapteurInterieur o) {
        return this.getIdentifant().compareTo(o.getIdentifant());
    }
    
}
