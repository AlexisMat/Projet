/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Class;

import java.util.Random;

/**
 *
 * @author Quentin
 * Cette classe permet de cree des intervalle 
 */


public class Intervalle {

    public float getBorneInf() {
        return borneInf;
    }

    public void setBorneInf(float borneInf) {
        this.borneInf = borneInf;
    }

    public float getBorneSup() {
        return borneSup;
    }

    public void setBorneSup(float borneSup) {
        this.borneSup = borneSup;
    }

    @Override
    public String toString() {
        return borneInf + "et" + borneSup ;
    }
    
    private float borneInf ;
    private float  borneSup;
    
    public Intervalle(float a , float b)
    {
        this.borneInf = a ;
        this.borneSup =b;
    }
    
    public float GenerateValue () //Retourne une valleur aleatoire compris dans l'intervalle
    {
        Random rand = new Random();

         float i =(rand.nextFloat() * (this.borneSup - this.borneInf) + this.borneInf);
         
         
         return i;

    }
    
    public boolean appartient (float val) //Dit si une valeur appartient a l'intervalle
    {
        return (val > borneInf && val < borneSup);
            
    }
        
    
    private float getborneInf ()
    {
        return borneInf;
    }
    private float getborneSUp ()
    {
        return borneSup;
    }
   
}

