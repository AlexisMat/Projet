/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projet;

import Class.Capteur;
import Class.CapteurExterieur;
import Class.CapteurInterieur;
import Class.CustomInscriptionRenderer;
import java.util.ArrayList;
import java.util.Set;
import javax.swing.JCheckBox;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author Quentin
 */
public class FenetreIncription extends javax.swing.JPanel {

    /**
     * Creates new form FenetreIncription
     * @param l
     * @param i
     * @param e
     */
  
    

    public FenetreIncription(Set<Capteur> listeCapteurReseau, ArrayList<CapteurInterieur> listeCapteurInt, ArrayList<CapteurExterieur> listeCapteurExt) {
        
        initComponents();
        jTable1.getColumnModel().getColumn(0).setCellRenderer(new CustomInscriptionRenderer());
        DefaultTableModel model= (DefaultTableModel) jTable1.getModel();
        
        for ( int  cpt = 0 ; cpt < model.getRowCount() ; cpt++)
        {
            model.removeRow(cpt);
        }
     
      
        if ( listeCapteurReseau != null)
        {
            for (Capteur capt : listeCapteurReseau)
            {
                if (  !listeCapteurInt.contains(capt) && ! listeCapteurExt.contains(capt) )
                model.addRow(new Object[]{capt.getIdentifant()});
                else
                 model.addRow(new Object[]{capt.getIdentifant(),true});

            }
        }
   
    }
    
    public DefaultTableModel getTable()
    {
        
        return  (DefaultTableModel) jTable1.getModel();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Capteur", "Title 2"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 375, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(35, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 69, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
