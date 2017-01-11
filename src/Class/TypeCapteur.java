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
public enum TypeCapteur {
    
    Temperature ("Temperature"),
    Humidité ("Humidite"),
    Luminosité ("Luminosité"),
    VolumeSonore ("Volume Sonore"),
    ConsommationEclairage("Consommation Eclairage"),
    EauFroide ("Eau Froide"),
    EauChaude ("Eau Chaude"),
    VitesseVent("Vitesse Vent"),
    PressionAtmosphérique("Pression atmosphérique");
       
    private String name;
    
    TypeCapteur(String n)
    {
       this.name = n;
    }

    public String[] tabString ()
    {
        return new String []{"Temperature","Humidite","Luminosité","Volume Sonore","Consomation eclairage",
        "Eau Froide","Eau Chaude","Vitesse Vent","Pression Atmospherique"};
    }
    @Override
    public String toString() {
        return name;
    }
    
}
