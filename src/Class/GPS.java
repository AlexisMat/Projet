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
public class GPS implements Comparable<GPS>{

    public GPS(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    int x;
    int y;
    
    
    @Override
    public int compareTo(GPS o) {
        if (this.getX() > o.getX())
            return 1;
        else if (this.getX() > o.getX())
            return 0;
        else 
            return -1;
    }
    
    
    
   
}
