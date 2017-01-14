/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Class;

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

    public Ecoute(Socket s, BufferedReader in, PrintWriter out) {
        this.s = s;
        this.in = in;
        this.out = out;
    }
   public void Ecrire(String msg_distant) throws IOException{
              BufferedWriter outF = null;
              try{
                 outF = new BufferedWriter(new FileWriter(new File("rsce.txt")));
                 outF.write(msg_distant);
                 outF.write("\n");
                 outF.flush();
                 outF.close();
              } catch (IOException e) {
                 e.printStackTrace();
              } 
             
   }
   
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
                Ecrire(msg_dist);
            } catch (IOException ex) {
                Logger.getLogger(Ecoute.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
    public void StartEc(){
        t = new Ecoute(s,in,out);
        t.start();
    }
}
