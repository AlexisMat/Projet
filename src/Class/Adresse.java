 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Class;

/**
 *
 * @author Alexis
 */
public class Adresse {

    public Adresse(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }
    
    private  String ip;
    private int port;
    
    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setPort(int port) {
        this.port = port;
    }

        
    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }
   
}
