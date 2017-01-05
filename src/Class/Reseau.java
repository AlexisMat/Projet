/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Class;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 * @author Alexis
 */
public class Reseau {
    
    private Capteur capteur;
    private Adresse adresse;
    
    public Reseau (Capteur capteur, Adresse adress){
        this.capteur = capteur;
        this.adresse = adresse;
}
    
    public void Connexion()throws UnknownHostException, IOException
    {
         
            String testCo = null;
   
        if(capteur instanceof CapteurInterieur){
           CapteurInterieur captInt = (CapteurInterieur) this.capteur;
           testCo = "ConnexionCapteur;"+captInt.getIdentifant()+";"+captInt.getType()+";"+captInt.getLocalisation();//ConnexionCapteur;IdentifiantCapteur;TypeCapteur;Bâtiment;Etage;Salle;PositionRelative
           
        }else{
            CapteurExterieur captExt = (CapteurExterieur) this.capteur;
            testCo = "ConnexionCapteur"+captExt.getIdentifant()+";"+captExt.getType()+";"+captExt.getLocalisation();//ConnexionCapteur;Id;type;coordLat;coordLong
             
        }  
       
          try{
                //System.out.println("Port : " +adresse.getPort());
                Socket client = new Socket(InetAddress.getLocalHost(),7888);
                System.out.println("Demande de connexion");
                
                OutputStream co =  client.getOutputStream();
                 
                for (char c : testCo.toCharArray())
                     co.write(c);
                client.close();
          }catch(UnknownHostException e){
                 e.printStackTrace();
          }     
    }
    public void TransmissionDonnee() throws UnknownHostException, IOException{
        String dataM = new String("ValeurCapteur;"+capteur.getVal());
        
        try{
           Socket data = new Socket(adresse.getIp(),adresse.getPort());
           System.out.println("Envoie de donnée");
           
            OutputStream dat =  data.getOutputStream();
                 
                for (char c : dataM.toCharArray())
                     dat.write(c);
           data.close();
        }catch(UnknownHostException e){
            e.printStackTrace();
        }
    }
    public void Deconnexion() throws UnknownHostException, IOException{
         String testDeco = new String("DeconnexionCapteur;"+capteur.getIdentifant());//DeconnexionCapteur;IdentifiantCapteur
         
         try{
             Socket  client =  new Socket(InetAddress.getLocalHost(),7888);
             System.out.println("Demande de déconnexion");
             
             OutputStream deco =  client.getOutputStream();
              for (char c : testDeco.toCharArray())
                     deco.write(c);
                client.close();
         }catch(UnknownHostException e){
                 e.printStackTrace();
          } 
    }
}
