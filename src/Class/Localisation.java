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
public class Localisation {
    
    private String Batiment ;
    private String Etage;
    private String Salle;

    public Localisation(String Batiment, String Etage, String Salle) {
        this.Batiment = Batiment;
        this.Etage = Etage;
        this.Salle = Salle;
    }

    public String getBatiment() {
        return Batiment;
    }

    public String getEtage() {
        return Etage;
    }

    public String getSalle() {
        return Salle;
    }
    
    
    
}
