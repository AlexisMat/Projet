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
public class CapteurExterieur extends Capteur implements Comparable<CapteurExterieur>{

    public GPS getLocalisation() {
        return Localisation;
    }
    private GPS Localisation; // A recuopere dans un fichier
    
    public CapteurExterieur(TypeCapteur type,GPS localisation,String uniteDeMesure, String Identifant, Intervalle i, String date, float precision, float marge, Integer frequence,float v) {
        super(type,uniteDeMesure, Identifant, i, date, precision, marge, frequence,v);
        this.Localisation = localisation;
    }

    @Override
    public int compareTo(CapteurExterieur o) {
       return this.getLocalisation().compareTo(o.getLocalisation()); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + Objects.hashCode(this.Localisation);
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
        final CapteurExterieur other = (CapteurExterieur) obj;
        if (!Objects.equals(this.Localisation, other.Localisation)) {
            return false;
        }
        return true;
    }
    
}
