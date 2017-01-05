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


public class Reseau {
	Socket socket;
  	BufferedReader in;
  	PrintWriter out;
  	String msg;
        /*@Méthode pour connecter une interface de visualisation au serveur
        
        */
	public static void Connexion(Socket socket, BufferedReader in, PrintWriter out, String msg) throws UnknownHostException, IOException {
		
	  	msg = "ConnexionVisu;IDENT";//Mettre le getID de l'interface de visualisation
	  	try{ 
	  		socket = new Socket(InetAddress.getLocalHost(),7888);
	  		System.out.println("Demande de connexion");
	  		out = new PrintWriter(socket.getOutputStream());
	  		out.println(msg);//On envoie la demande de connexion d'une interface de visualisation
	  		out.flush();//Vidage du buffer
	  		
	  		in = new BufferedReader(new InputStreamReader (socket.getInputStream()));
	  		String message_distant = in.readLine();
	  		System.out.println(message_distant);
	  		if(!message_distant.equals("ConnexionOK")){
	  			System.out.println("ERREUR");
	  			/*
	  			 * @il faut mettre un pop up ici
	  			 */
                                                        socket.close();
	  		}
	  		
	  		  		
	  	}catch(UnknownHostException e){
	  		e.printStackTrace();
	  	}catch(IOException e) {
	  		e.printStackTrace();
	  	}
	}
	
	/*@Méthode pour deconnecter une interface de visualisation du serveur.
        */
	public static void Deconnexion(Socket socket, BufferedReader in, PrintWriter out, String msg) throws UnknownHostException, IOException {
	
	    msg = "DeconnexionVisu";
	  	try{ 
	  		socket = new Socket(InetAddress.getLocalHost(),7888);//getIp et getPort ici
	  		System.out.println("Demande de Deconnexion");
	  		out = new PrintWriter(socket.getOutputStream());
	  		out.println(msg);//On envoie la demande de deconnexion d'une interface de visualisation
	  		out.flush();//Vidage du buffer
	  		in = new BufferedReader(new InputStreamReader (socket.getInputStream()));
	  		String message_distant = in.readLine();
	  		System.out.println(message_distant);	  	
	  		if(!message_distant.equals("DeconnexionOK")){
	  			System.out.println("ERREUR");
	  			/*
	  			 * @il faut mettre un pop up ici
	  			 */
	  		}
	  		
	  		socket.close();
	  		
	  	}catch(UnknownHostException e){
	  		e.printStackTrace();
	  	}catch(IOException e) {
	  		e.printStackTrace();
	  	}
	}
	
        /*@Méthode permettant d'inscrire les capteurs sélection via l'interface à l'interface de visu (Tableau)
        
        */
	public static void InscriptionVisu(Socket socket,BufferedReader in, PrintWriter out, String msg) throws IOException{
	
	 	msg = "InscriptionCapteur;Id1";//Remplacer ID par l'id des capteurs sélectionné
		
	 	System.out.println("Inscription interface de visu aux capteur Id1");
	 	out = new PrintWriter(socket.getOutputStream());
	 	out.println(msg);
	 	out.flush();
	 	
	 	in = new BufferedReader(new InputStreamReader (socket.getInputStream()));
		String message_distant = in.readLine();
  		System.out.println(message_distant);	  
	 	/*
	 	 * @il faudrat compter le nombre de capteurs que l'on inscrip pour pouvoir Recupéré leur données.
	 	 * Pour un capteur intérieur :
	 	 * CapteurPresent;<IdentifiantDuCapteur>;<TypeDuCapteur>;<Bâtiment>;<Etage>;<Salle>;<PositionRelative>
		 * Pour un capteur extérieur:
		 * CapteurPresent;<IdentifiantDuCapteur>;<TypeDuCapteur>;<CoordonnéeGPS_Lat>;<CoordonnéeGPS_Long>
	 	 */
  		if(message_distant.equals("InscriptionCapteurKO;Id1")){
  			System.out.println("impossible de valider l’inscription de capteurs");
  			/*
  			 * @il faut mettre un pop up ici
  			 */
  		}
	}
	
	/*@Méthode permettant de Déconnecter un ou plusieurs capteurs de l'interface de visu
        
        */
	public static void DesinscriptionVisu(Socket socket,BufferedReader in, PrintWriter out, String msg) throws IOException{
		msg = "DesinscriptionCapteur;Id1";
		System.out.println("Desinscription interface de visu aux capteur Id1");
	 	out = new PrintWriter(socket.getOutputStream());
	 	out.println(msg);
	 	out.flush();
	 	
	 	in = new BufferedReader(new InputStreamReader (socket.getInputStream()));
		String message_distant = in.readLine();
  		System.out.println(message_distant);	  
	 	
  		if(message_distant.equals("DesinscriptionCapteurKO;Id1")){
  			System.out.println("impossible de valider la desinscription de capteurs");
  			/*
  			 * @il faut mettre un pop up ici
  			 */
  		}
		
	}

/*@Il manque la méthod de transmission des valeur depuis le serveur vers l'interface afin de remplir le tableau
        
        */

	public static void main() {
	/*	// TODO Auto-generated method stub
		System.out.println("On ce co");
	 	try {
			Connexion(socket,in,out,msg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 	System.out.println("Voulez vous deco?");
	 	@SuppressWarnings("resource")
		Scanner sc = new Scanner (System.in);
	 	String clavier = sc.nextLine();
	 	if(clavier.equals("oui")){
	 		try {
				Deconnexion(socket,in,out,msg);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	 	}*/
		
	}
	/*
	 * @On doit check tout le temps si on reçoit une Deconnexion de capteur d'interface de simulation
	 * et le géré du côter visualisation (le retiré de la liste des capteurs)
	 * Le msg reçu étant le suivant "CapteurDeco;<IdentifiantDuCapteur>";
	 * La mise à jour des données sera reçu grâce au msg suivant
	 * ValeurCapteur;<IdentifiantDuCapteur>;<ValeurDuCapteur>

	 */


}
