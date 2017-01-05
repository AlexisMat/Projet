/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Class;

import java.sql.Time;
import java.util.Date;

/**
 *
 * @author Quentin
 * La classe Capteur est abstract car il ne peut avoir que soit un capteur interieur ou exterieur.Le capteur seul est un concept
 */
public abstract class  Capteur {

    public String getUniteDeMesure() {
        return uniteDeMesure;
    }

    public void setUniteDeMesure(String uniteDeMesure) {
        this.uniteDeMesure = uniteDeMesure;
    }

    public String getIdentifant() {
        return Identifant;
    }

    public void setIdentifant(String Identifant) {
        this.Identifant = Identifant;
    }

    public Intervalle getI() {
        return i;
    }

    public void setI(Intervalle i) {
        this.i = i;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getPrecision() {
        return precision;
    }

    public void setPrecision(float precision) {
        this.precision = precision;
    }

    public float getMarge() {
        return marge;
    }

    public void setMarge(float marge) {
        this.marge = marge;
    }

    public Integer getFrequence() {
        return frequence;
    }

    public void setFrequence(Integer frequence) {
        this.frequence = frequence;
    }

    public Capteur(String type ,String uniteDeMesure, String Identifant, Intervalle i, String date, float precision, float marge, Integer frequence) {
        this.uniteDeMesure = uniteDeMesure;
        this.Identifant = Identifant;
        this.i = i;
        this.date = date;
        this.precision = precision;
        this.marge = marge;
        this.frequence = frequence;
        this.type = type;
    }
    
    private String uniteDeMesure,Identifant;
    private Intervalle i;
    private String  date; // Sous fome jj/mm/aaaa  peut etre faire des classe pour date et heure
    private float precision;
    private float marge ;
    private Integer frequence;
    private String type;
    private float val;

    public float getVal() {
        return val;
    }

    public void setVal(float val) {
        this.val = val;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    
    
}
