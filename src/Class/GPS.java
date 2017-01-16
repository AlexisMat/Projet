/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Class;

/**
 *
 * @
 * Quentin
 */

public class GPS implements Comparable<GPS>{

    
    float x;
    float y;

    public GPS(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

   
    
    
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
