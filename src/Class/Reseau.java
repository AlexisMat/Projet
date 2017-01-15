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


public class Reseau  {
    
private Socket socket;
private BufferedReader in;
private PrintWriter out;
private String msg;
//private Capteur capteur;
private Adresse adresse;


public Adresse getAdresse() {
        return adresse;
}

public void setAdresse(Adresse adresse) {
        this.adresse = adresse;
}
  
/*public Capteur getCapteur() {
        return capteur;
}

public void setCapteur(Capteur capteur) {
        this.capteur = capteur;
}*/

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
     
public Reseau(Socket socket, BufferedReader in, PrintWriter out, String msg, Adresse adresse) {
        this.socket = socket;
        this.in = in;
        this.out = out;
        this.msg = msg;
        //this.capteur = capteur;  
        this.adresse = adresse;
       
}
    

  	
        /*@Méthode pour connecter une interface de visualisation au serveur
        
        */
	public  void Connexion(Socket socket, BufferedReader in, PrintWriter out, String msg,Adresse adresse, Capteur capteur) throws UnknownHostException, IOException {
		
	  	msg = "ConnexionVisu;Visualisation"; 
	  	try{ 		
	  		System.out.println("Demande de connexion");
	  		out.println(msg);//On envoie la demande de connexion d'une interface de visualisation
	  		out.flush();//Vidage du buffer
	  		String message_distant = in.readLine();
	  		System.out.println(message_distant);
	  		if(!message_distant.equals("ConnexionOK")){ //Si la connection n'est pas OK alors ...
	  			System.out.println("ERREUR");
	  			/*
	  			 * @il faut mettre un pop up ici
	  			 */
                                                        socket.close(); /*Si il y a une erreur on close la socket sinon on la laisse ouverte 
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
	public  void Deconnexion(Socket socket, BufferedReader in, PrintWriter out, String msg, Adresse adresse) throws UnknownHostException, IOException {
	
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
	public  void InscriptionVisu(Socket socket,BufferedReader in, PrintWriter out, String msg,Capteur capteur) throws IOException{
		 msg = "InscriptionCapteur;"+capteur.getIdentifant();//FAUT RECUP LEURS PUTAIN D ID MDRRRRRR			
	 	System.out.println("Inscription interface de visu aux capteur"+capteur.getIdentifant());	 	
	 	out.println(msg);
	 	out.flush();	 	
		String message_distant = in.readLine();
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
	public static void DesinscriptionVisu(Socket socket,BufferedReader in, PrintWriter out, String msg,Capteur capteur) throws IOException{
		msg = "DesinscriptionCapteur;"+capteur.getIdentifant();//FAUT RECUP LEURS PUTAIN D ID MDRRRRRR
                
		System.out.println("Desinscription interface de visu aux capteur"+capteur.getIdentifant());	 	
	 	out.println(msg);
	 	out.flush();	 		
		String message_distant = in.readLine();
  		System.out.println(message_distant);	  
	 	
  		if(!message_distant.equals("DesinscriptionCapteurOK;Id1")){ // Si  la Desinscription n'est pas OK  pour le capteur d'id  = id1 alors ... 
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


