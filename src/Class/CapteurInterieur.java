/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Class;

import java.util.Objects;

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

    public CapteurInterieur(String type ,Localisation localisation,String uniteDeMesure, String identifant, Intervalle i, String date, float precision, float marge, Integer frequence,float v) {
        super(type,uniteDeMesure, identifant, i, date, precision, marge, frequence,v);
        this.localisation = localisation;
    }

    @Override
    public int compareTo(CapteurInterieur o) {
        return this.getIdentifant().compareTo(o.getIdentifant());
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + Objects.hashCode(this.localisation);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CapteurInterieur other = (CapteurInterieur) obj;
        if (!Objects.equals(this.localisation, other.localisation)) {
            return false;
        }
        return true;
    }
    
}
