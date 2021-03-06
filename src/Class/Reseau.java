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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import javax.swing.JOptionPane;
import projet.InterfaceVisu;


public class Reseau  {
    
private Socket socket;
private BufferedReader in;
private PrintWriter out;
private String msg;
private String idVisu;


public Socket getSocket() {
        return socket;
 }

 public void setSocket(Socket socket) {
        this.socket = socket;
 }

public BufferedReader getIn() {
        return in;
}


public void setIn(BufferedReader in) {
        this.in = in;
}


public PrintWriter getOut() {
        return out;
}

public void setOut(PrintWriter out) {
        this.out = out;
}

   
public String getMsg() {
        return msg; 
}

public void setMsg(String msg) {
        this.msg = msg;
}
     
public Reseau(Socket socket, BufferedReader in, PrintWriter out, String msg ,String idVisu) {
        this.socket = socket;
        this.in = in;
        this.out = out;
        this.msg = msg;
        this.idVisu = idVisu;
        //this.capteur = capteur;  
        
       
}
    

  	
        /*@Méthode pour connecter une interface de visualisation au serveur
        
        */
	public  void Connexion() throws UnknownHostException, IOException {
		
	  	msg = "ConnexionVisu;"+this.idVisu; 
	  	try{ 		
	  		System.out.println("Demande de connexion");
	  		this.out.println(msg);//On envoie la demande de connexion d'une interface de visualisation
	  		this.out.flush();//Vidage du buffer
	  		String message_distant = in.readLine();
                        System.out.println(message_distant);
	  		if(!message_distant.equals("ConnexionOK")){ //Si la connection n'est pas OK alors ...
	  			System.out.println("ERREUR");
	  			/*
	  			 * @il faut mettre un pop up ici
                                
	  			 */
                                 
                                 //JOptionPane.showMessageDialog(, "Error", "error", JOptionPane.ERROR_MESSAGE);
                                 this. socket.close(); /*Si il y a une erreur on close la socket sinon on la laisse ouverte 
                                                        tant que l'on a pas de demande de deco
                                                        */
	  		}	  		
	  	}catch(UnknownHostException e){
	  		e.printStackTrace();
	  	}catch(IOException e) {
	  		e.printStackTrace();
	  	}
	}
	
	/*@Méthode pour deconnecter une interface de visualisation du serveur.
        */
	public  void Deconnexion() throws UnknownHostException, IOException {
	
	    msg = "DeconnexionVisu";
	  	try{ 	  		
	  		System.out.println("Demande de Deconnexion");	  		
	  		out.println(msg);//On envoie la demande de deconnexion d'une interface de visualisation
	  		out.flush();//Vidage du buffer	  		
	  		String message_distant = in.readLine();
	  		System.out.println(message_distant);	  	
                                  	socket.close();
	  	}catch(UnknownHostException e){
	  		e.printStackTrace();
	  	}catch(IOException e) {
	  		e.printStackTrace();
	  	}
	}
	
        /*@Méthode permettant d'inscrire les capteurs sélectionné via l'arbre au Tableau
        
        */
	public  void InscriptionVisu(Capteur capteur) throws IOException{
		this.msg = "InscriptionCapteur;"+capteur.getIdentifant();//FAUT RECUP LEURS PUTAIN D ID MDRRRRRR			
	 	System.out.println("Inscription interface de visu aux capteur"+capteur.getIdentifant());	 	
	 	this.out.println(msg);
	 	this.out.flush();	 	
		String message_distant = this.in.readLine();
  		System.out.println(message_distant);	  
	 	/*
	 	 * @il faudrat compter le nombre de capteurs que l'on inscrip pour pouvoir Recupéré leur données.
	 	 * Pour un capteur intérieur :
	 	 * CapteurPresent;<IdentifiantDuCapteur>;<TypeDuCapteur>;<Bâtiment>;<Etage>;<Salle>;<PositionRelative>
		 * Pour un capteur extérieur:
		 * CapteurPresent;<IdentifiantDuCapteur>;<TypeDuCapteur>;<CoordonnéeGPS_Lat>;<CoordonnéeGPS_Long>
	 	 */
  		if(message_distant.equals("InscriptionCapteurKO;"+capteur.getIdentifant())){
  			System.out.println("impossible de valider l’inscription de capteur");
  			/*
  			 * @il faut mettre un pop up ici
  			 */
  		}
	}
	
	/*@Méthode permettant de Déconnecter un ou plusieurs capteurs de l'interface de visu
        
        */
	public void DesinscriptionVisu(Capteur capteur) throws IOException{
		this.msg = "DesinscriptionCapteur;"+capteur.getIdentifant();//FAUT RECUP LEURS PUTAIN D ID MDRRRRRR
                
		System.out.println("Desinscription interface de visu aux capteur"+capteur.getIdentifant());	 	
	 	out.println(msg);
	 	out.flush();	 		
		String message_distant = in.readLine();
  		System.out.println(message_distant);	  
	 	
  		if(message_distant.equals("DesinscriptionCapteurKO;"+capteur.getIdentifant())){ // Si  la Desinscription n'est pas OK  pour le capteur d'id  = id1 alors ... 
  			System.out.println("impossible de valider la desinscription de capteurs");
  			/*
  			 * @il faut mettre un pop up ici
  			 */
  		}
		
	}

/*@Il manque la méthod de transmission des valeur depuis le serveur vers l'interface afin de remplir le tableau
        
        */


		
	} // Fin de la classe

	/*
	 * @On doit check tout le temps si on reçoit une Deconnexion de capteur d'interface de simulation
	 * et le géré du côter visualisation (le retiré de la liste des capteurs)
	 * Le msg reçu étant le suivant "CapteurDeco;<IdentifiantDuCapteur>";
	 * La mise à jour des données sera reçu grâce au msg suivant
	 * ValeurCapteur;<IdentifiantDuCapteur>;<ValeurDuCapteur>

	 */


