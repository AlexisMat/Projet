package Class;


import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.Iterator;
import java.util.Map;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Quentin
 */
public class CustomRenderer extends DefaultTableCellRenderer{

   private final Map<Capteur,Float> liste;
   private final DefaultTableModel tab;
   
    public CustomRenderer( Map<Capteur,Float> liste ,  DefaultTableModel tab) {
        this.tab = tab;
        this.liste= liste;
    }

    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column); //To change body of generated methods, choose Tools | Templates.
      
        if ( column == 0)
        {
            if ( tab != null)
            {
                Capteur capteur = null;
                for( Capteur capt : liste.keySet())
                {
                    if ( capt.getIdentifant().equals(value))
                    {
                       capteur = capt;
                    }
                }
                if (capteur != null )
                {

                   Float val = valeurAlerte(capteur);
                 
                   if  ( appartientListe(value.toString()) &&  val < capteur.getVal() )
                   {
                           c.setForeground(Color.RED);
                   }else
                   c.setForeground(Color.BLACK);
                      

                }else
                   c.setForeground(Color.BLACK);
            }else
                   c.setForeground(Color.BLACK);
               
                  
               
        }
        else
                   c.setForeground(Color.BLACK);
          
          return c;
    }
    private float valeurAlerte (Capteur capt)
    {
        float val = 0 ;
        for ( int i = 0 ; i < tab.getRowCount();i++)
        {
            if ( capt.getIdentifant().equals(tab.getValueAt(i, 0)))
            {
                  val = Float.parseFloat(tab.getValueAt(i, 1).toString());
            }
        }
        
        return val;
    }
    
    private boolean appartientListe (String value)
    {
        for( Capteur capt : liste.keySet())
        {
            if ( capt.getIdentifant().equals(value))
            {
                return true;
            }
        }
        
        return false;
    }
    
    
    
    
    


   
    
    
}
