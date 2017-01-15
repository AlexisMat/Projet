package Class;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Alexis
 */
public class Ecoute extends Thread{
    Thread t;
    private Socket s;
    private BufferedReader in;
    private PrintWriter out;
    private BufferedWriter outF;
    
    
    //Constructeur de la classe Ecoute
    public Ecoute(Socket s, BufferedReader in, PrintWriter out) {
        this.s = s;
        this.in = in;
        this.out = out;
        try {
            this.outF = new BufferedWriter(new FileWriter(new File("rsce.txt"),true));
        } catch (IOException e) {
			// TODO Auto-generated catch block			
                        e.printStackTrace();        
        }
    }
    //méthode qui permet d'écrire dans un fichier outF
   public void Ecrire(String msg_distant) throws IOException{
              try{
                 this.outF.write(msg_distant+"\r\n");
                 this.outF.flush();
              }catch (IOException e) {
                 e.printStackTrace();
              }  
   }
   //Destructeur pour le fichier   
   public void finalize()
   {
	   try {
		this.outF.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
   }
   
   
   //Méthode lancer lors de l'utilisation de l'objet Ecoute
    public void run(){
        String msg_dist;
        try {
            in = new BufferedReader(new InputStreamReader(s.getInputStream() ));
        } catch (IOException ex) {
            Logger.getLogger(Ecoute.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        while(!s.isClosed() ){
            try {
                msg_dist = in.readLine();
                System.out.println(msg_dist);
                Ecrire(msg_dist);
            } catch (IOException ex) {
                Logger.getLogger(Ecoute.class.getName()).log(Level.SEVERE, null, ex);
            }
        }           
    }
    
    
    //Méthode qui lance l'Ecoute
    public void StartEc(){
        t = new Ecoute(s,in,out);
        t.start();
    }
}
