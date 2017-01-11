/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projet;

import Class.BatimentEtage;
import Class.Capteur;
import Class.CapteurExterieur;
import Class.CapteurInterieur;
import Class.TypeCapteur;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.RowFilter;
import javax.swing.RowFilter.Entry;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 *
 * @author Quentin
 */
public class InterfaceVisu extends javax.swing.JFrame {

    /**
     * Creates new form NewJFrame
     */
    /* Il faut recupere les donnees du reseau*/
    private ArrayList<CapteurInterieur> listeCapteurInt;
    private ArrayList<CapteurExterieur> listeCapteurExt;
    

    /* Liste pour laliste des batiment et lleur etage ainsi que leur salle qui leur sont lié*/
    Set<String> listeBatiment =  new HashSet<>();
    Map<String, Set<String>> listeEtage = new HashMap<>(); /* Prend un batiment en cle et donne un Set des Etage*/
    Map<BatimentEtage , Set<String>>listeClasse =  new HashMap<>();
   
    
    public InterfaceVisu(ArrayList<CapteurInterieur> l,ArrayList<CapteurExterieur> l2 ) {
        this.listeCapteurInt =  l;
        this.listeCapteurExt = l2;
      
         
        initComponents();
        
        this.initLocalisation(); /* On charge le fichier des Batiment, Etage Salle*/
        this.initArbre(); /*Arbre vide tant qu'on est pas connecter au reseau*/
        this.updateCapteurExterieur(); /* Fonction qui met a jour les Capteur Exterieur */
        this.updateCapteurInterieur();
        
        /* On Ajoute le filtrage */
        
        TableRowSorter<TableModel> rowSorter = new TableRowSorter<TableModel>(jTable1.getModel()); /* on met lemodel du tableau dans un Table RowSorted*/
        jTable1.setRowSorter(rowSorter);
        jTextField1.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                String text = jTextField1.getText();
                if ( text.trim().length() == 0)
                  rowSorter.setRowFilter(RowFilter.regexFilter(null));
                else
                   rowSorter.setRowFilter(RowFilter.regexFilter("(?i)"+ text)); 
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                String text = jTextField1.getText();
                /*if ( text.trim().length() == 0)
                  rowSorter.setRowFilter(RowFilter.regexFilter(null));
                else*/
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)"+ text));
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
        
    }

    private void initLocalisation ()
    {
      
        try{
                    InputStream flux=InterfaceVisu.class.getResourceAsStream("/Localisation.txt"); 
                    InputStreamReader lecture=new InputStreamReader(flux);
                    BufferedReader buff=new BufferedReader(lecture);
                    String ligne;
                    while ((ligne=buff.readLine())!=null){
                        
                        String[] tab;
                        tab = ligne.split("/");
                        listeBatiment.add(tab[0]);
                             
                        if (!listeEtage.containsKey(tab[0]))
                        {
                            listeEtage.put(tab[0], new HashSet<String>());
                        }  
                            listeEtage.get(tab[0]).add(tab[1]);
                            BatimentEtage batEta = new BatimentEtage(tab[0], tab[1]);
                             
                         
                          if ( !listeClasse.containsKey(batEta))
                          {
                                 //System.out.println("Hey2");
                                 listeClasse.put(batEta,new HashSet<String>());
                                 listeClasse.get(batEta).add(tab[2]);
                                
                          } 
                          else
                              listeClasse.get(batEta).add(tab[2]);
                          
                                                                                                      
                       }
                    
                    buff.close(); 
                                                         
            }		
               catch (Exception e){
                  System.out.println(e.toString());
             
               }
        System.out.println  (this.listeBatiment);
        System.out.println  (this.listeEtage);
        System.out.println  (this.listeClasse);
        
    }
    private void initArbre ()
    {
       DefaultTreeModel arbre;
       arbre= (DefaultTreeModel) jTree1.getModel();
       DefaultMutableTreeNode root = (DefaultMutableTreeNode) arbre.getRoot(); /* On recupre la racine*/     
       
       DefaultMutableTreeNode nodeCapteurExterieur = new javax.swing.tree.DefaultMutableTreeNode("Capteur Exterieur"); /*On contruit l'arbe a partir des liste des capteurs*/
       DefaultMutableTreeNode nodeCapteurInterieur = new javax.swing.tree.DefaultMutableTreeNode("Capteur Interieur");
       root.add(nodeCapteurExterieur);
       
       /*On affiche l'arborescance des Batiment , Etage et salle malgres qu'il n'y a pas de capteur*/
       for ( String batiment: this.listeBatiment)
       {
           
          DefaultMutableTreeNode nodeBatiment = new javax.swing.tree.DefaultMutableTreeNode(batiment);
          for (  String etage : listeEtage.get(batiment))
          {
              DefaultMutableTreeNode nodeEtage = new javax.swing.tree.DefaultMutableTreeNode(etage);
              BatimentEtage batEtage =  new BatimentEtage(batiment, etage);
              for (String salle : listeClasse.get(batEtage))
              {
                  DefaultMutableTreeNode nodeSalle = new javax.swing.tree.DefaultMutableTreeNode(salle);
                  nodeEtage.add(nodeSalle);
                  arbre.reload();
              }
              nodeBatiment.add(nodeEtage);
              arbre.reload();
          }
          nodeCapteurInterieur.add(nodeBatiment);
          arbre.reload();
          root.add(nodeCapteurInterieur);
          
       }
     
    }
    
    private DefaultMutableTreeNode findNode ( DefaultMutableTreeNode n , DefaultMutableTreeNode e)
    {
        
        for ( int i = 0 ; i < n.getChildCount(); i++)
        {
          
            DefaultMutableTreeNode courant = (DefaultMutableTreeNode) n.getChildAt(i);
            if ( courant.toString().equals(e.toString()))
            {
                return (DefaultMutableTreeNode) n.getChildAt(i);
            }
        }
       
        return null;
    }
    private void updateCapteurInterieur()
    {
        
      
       DefaultTreeModel arbre;
       arbre= (DefaultTreeModel) jTree1.getModel();
       DefaultMutableTreeNode root = (DefaultMutableTreeNode) arbre.getRoot(); /* On recupre la racine*/
       Collections.sort(this.listeCapteurInt); //On trie par longitutude X grace a des compareTo dans Capteur Exterieur et GPS
       
       DefaultMutableTreeNode nodeCapteurInterieur =(DefaultMutableTreeNode) root.getChildAt(0);
       System.out.println(nodeCapteurInterieur);
       for ( CapteurInterieur capteur : this.listeCapteurInt)
       {
            /* On cree les node en fonction du batiment, salle et etage */
            DefaultMutableTreeNode nodeBatimentCapteur = new javax.swing.tree.DefaultMutableTreeNode(capteur.getLocalisation().getBatiment());
            DefaultMutableTreeNode nodeEtageCapteur = new javax.swing.tree.DefaultMutableTreeNode(capteur.getLocalisation().getEtage());
            DefaultMutableTreeNode nodeSalleCapteur = new javax.swing.tree.DefaultMutableTreeNode(capteur.getLocalisation().getSalle());       
            DefaultMutableTreeNode nodeCapteur= new javax.swing.tree.DefaultMutableTreeNode(capteur.getIdentifant());
           
           
            DefaultMutableTreeNode nodeBatiment = this.findNode(nodeCapteurInterieur, nodeBatimentCapteur); //Onrecupre le node du batiment          
            DefaultMutableTreeNode nodeEtage = this.findNode(nodeBatiment, nodeEtageCapteur);         
            DefaultMutableTreeNode nodeSalle = this.findNode(nodeEtage,nodeSalleCapteur);
           
            nodeSalle.add(nodeCapteur); // On ajoute  le capteur a l'arboresence 
            arbre.reload();          
           
       }
    }
    private void updateCapteurExterieur()
    {
       DefaultTreeModel arbre;
       arbre= (DefaultTreeModel) jTree1.getModel();
       DefaultMutableTreeNode root = (DefaultMutableTreeNode) arbre.getRoot(); /* On recupre la racine*/
       Collections.sort(this.listeCapteurExt); //On trie par longitutude X grace a des compareTo dans Capteur Exterieur et GPS
       
       DefaultMutableTreeNode nodeCapteurExterieur = (DefaultMutableTreeNode) root.getChildAt(0); /*On contruit l'arbe a partir des liste des capteurs*/
      
          for ( CapteurExterieur capteur : this.listeCapteurExt)
       {
                     
            DefaultMutableTreeNode nodeCapteur = new javax.swing.tree.DefaultMutableTreeNode(capteur.getIdentifant());
            nodeCapteurExterieur.add(nodeCapteur);
            arbre.reload();          
            root.add(nodeCapteurExterieur);
            arbre.reload();
       }
       
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane1 = new javax.swing.JSplitPane();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTree1 = new javax.swing.JTree();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jSplitPane1.setDividerLocation(250);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nom", "Type de Mesure", "Localisation", "Valeur"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(jTable1);

        jLabel2.setText("Rechercher");

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 766, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(214, 214, 214)
                .addComponent(jLabel2)
                .addGap(50, 50, 50)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 503, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 11, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("tab1", jPanel1);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 766, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 540, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("tab2", jPanel2);

        jSplitPane1.setRightComponent(jTabbedPane1);

        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("Capteur");
        jTree1.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        jTree1.setToolTipText("");
        jTree1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTree1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTree1);

        jSplitPane1.setLeftComponent(jScrollPane1);

        jMenu1.setText("Connexion");
        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1027, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTree1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTree1MouseClicked
        // TODO add your handling code here:
        String parcours =  jTree1.getLastSelectedPathComponent().toString();
     
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setNumRows(0); //on remet le tableau a zero a chauqe clique
        model.setRowCount(0); 
        Capteur capteurSeule = null;
        for (CapteurExterieur capt : this.listeCapteurExt)
        {
            if ( capt.getIdentifant().equals(parcours))
                 capteurSeule = capt;           
        }
        for (CapteurInterieur capt : this.listeCapteurInt)
        {
            if ( capt.getIdentifant().equals(parcours))          
                capteurSeule = capt;                      
        }
        
        if ( parcours.equals ("Capteur"))
        {
            for ( CapteurExterieur capteur : this.listeCapteurExt)
            {
                model.addRow(new Object[]{capteur.getIdentifant(),capteur.getType(),capteur.getLocalisation().getX()+","+capteur.getLocalisation().getY()
                    ,capteur.getVal() });
            }
            for ( CapteurInterieur capteur : this.listeCapteurInt)
            {
                model.addRow(new Object[]{capteur.getIdentifant(),capteur.getType(),capteur.getLocalisation().getBatiment()+"/"+capteur.getLocalisation().getEtage()+
                        "/"+capteur.getLocalisation().getSalle(),capteur.getVal() });
            }
            
        }
        else if ( parcours.equals("Capteur Exterieur")) // On affiche tous les capteur Exterieur
        {
            for ( CapteurExterieur capteur : this.listeCapteurExt)
            {
                model.addRow(new Object[]{capteur.getIdentifant(),capteur.getType(),capteur.getLocalisation().getX()+","+capteur.getLocalisation().getY()
                    ,capteur.getVal() });
            }
        }
        else if (parcours.equals("Capteur Interieur") ) // on affiche tous les capteurs Interieur
        {
            for ( CapteurInterieur capteur : this.listeCapteurInt)
            {
                model.addRow(new Object[]{capteur.getIdentifant(),capteur.getType(),capteur.getLocalisation().getBatiment()+"/"+capteur.getLocalisation().getEtage()+
                        "/"+capteur.getLocalisation().getSalle(),capteur.getVal() });
            }
        }
        else if ( listeBatiment.contains(parcours)) // on affiche tous les capteur d'un batiment
        {
           for (CapteurInterieur capteur : this.listeCapteurInt)
           {
               if ( capteur.getLocalisation().getBatiment().equals(parcours))
               {
                   model.addRow(new Object[]{capteur.getIdentifant(),capteur.getType(),capteur.getLocalisation().getBatiment()+"/"+capteur.getLocalisation().getEtage()+
                        "/"+capteur.getLocalisation().getSalle(),capteur.getVal() });
               }
           }
        }             
        else if ( capteurSeule != null)
        {
                   
            if (capteurSeule instanceof CapteurExterieur)
            {
                CapteurExterieur capteurExt = (CapteurExterieur) capteurSeule ;
            
            model.addRow(new Object[]{capteurSeule.getIdentifant(),capteurSeule.getType(),capteurExt.getLocalisation().getX()+","+capteurExt.getLocalisation().getY()
                    ,capteurSeule.getVal() });
            }
            else {
                
                 CapteurInterieur capteurInt = (CapteurInterieur) capteurSeule;
            
                 model.addRow(new Object[]{capteurSeule.getIdentifant(),capteurSeule.getType(),capteurInt.getLocalisation().getBatiment()+"/"+capteurInt.getLocalisation().getEtage()+
                        "/"+capteurInt.getLocalisation().getSalle(),capteurSeule.getVal() });
            }
        }
        
        else //Les capteur des salle et des etages
        {
            Object[] tab =  jTree1.getSelectionPath().getPath();
            String bat = tab[2].toString();  /* On recupere  le baitment*/
            String salle = tab[3].toString();
               
           for (CapteurInterieur capteur : this.listeCapteurInt)
           {
               if ( capteur.getLocalisation().getBatiment().equals(parcours) || capteur.getLocalisation().getBatiment().equals(bat) && capteur.getLocalisation().getEtage().equals(parcours)
                       || capteur.getLocalisation().getSalle().equals(parcours))
               {
                   model.addRow(new Object[]{capteur.getIdentifant(),capteur.getType(),capteur.getLocalisation().getBatiment()+"/"+capteur.getLocalisation().getEtage()+
                        "/"+capteur.getLocalisation().getSalle(),capteur.getVal() });
               }
           }
               System.out.println(bat);
               
               
               
     
        }
        
        /* Juste le capteur selectioné */
        
       
       
  
    }//GEN-LAST:event_jTree1MouseClicked

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    /**
     * @param args the command line arguments
     */
  
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTree jTree1;
    // End of variables declaration//GEN-END:variables

   
}
