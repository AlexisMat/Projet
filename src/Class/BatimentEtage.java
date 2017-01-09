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
public class BatimentEtage {

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + Objects.hashCode(this.batiment);
        hash = 73 * hash + Objects.hashCode(this.etage);
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
        final BatimentEtage other = (BatimentEtage) obj;
        if (!Objects.equals(this.batiment, other.batiment)) {
            return false;
        }
        if (!Objects.equals(this.etage, other.etage)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "BatimentEtage{" + "batiment=" + batiment + ", etage=" + etage + '}';
    }

    public String getBatiment() {
        return batiment;
    }

    public void setBatiment(String batiment) {
        this.batiment = batiment;
    }

    public String getEtage() {
        return etage;
    }

    public void setEtage(String etage) {
        this.etage = etage;
    }

    public BatimentEtage(String batiment, String etage) {
        this.batiment = batiment;
        this.etage = etage;
    }
    
    private String batiment ;
    private String etage;
    
    
}
