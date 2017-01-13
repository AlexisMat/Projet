/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projet;

import Class.Adresse;
import Class.BatimentEtage;
import Class.Capteur;
import Class.CapteurExterieur;
import Class.CapteurInterieur;
import Class.CustomRenderer;
import Class.TypeCapteur;
import java.awt.event.ActionListener;

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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
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
    private final Set<Capteur> listeCapteurReseau;
    
    /*Liste des capteur inscrit*/
    private Set<CapteurInterieur> listeCapteurInt = new HashSet<CapteurInterieur>();
    private Set<CapteurExterieur> listeCapteurExt = new HashSet<CapteurExterieur>();
    

    /* Liste pour laliste des batiment et lleur etage ainsi que leur salle qui leur sont lié*/
    Set<String> listeBatiment =  new HashSet<>();
    Map<String, Set<String>> listeEtage = new HashMap<>(); /* Prend un batiment en cle et donne un Set des Etage*/
    Map<BatimentEtage , Set<String>>listeClasse =  new HashMap<>();
   
    Map<Capteur,Float> listeAlerte = new HashMap<>();
    
    /*Modele du tableau des alertes*/
    DefaultTableModel tabAlerte = null ;
    
    public InterfaceVisu(Set<Capteur> l ) {
        this.listeCapteurReseau = l;
      
         
        initComponents();
        
     
        this.initLocalisation(); /* On charge le fichier des Batiment, Etage Salle*/
        this.initArbre(); /*Arbre vide tant qu'on est pas connecter au reseau*/
        if ( this.listeCapteurExt != null)
        {
            this.updateCapteurExterieur(); /* Fonction qui met a jour les Capteur Exterieur */
        }
        if ( this.listeCapteurInt != null )
        {
                this.updateCapteurInterieur();
        }
        this.filtrage();
       
        
        /* On Ajoute le filtrage */
        
        
        
    }

    private void filtrage ()
    {
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
       //Collections.sort(this.listeCapteurInt); //On trie par longitutude X grace a des compareTo dans Capteur Exterieur et GPS
       
       DefaultMutableTreeNode nodeCapteurInterieur =(DefaultMutableTreeNode) root.getChildAt(0);
       
      
       
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
       //Collections.sort(this.listeCapteurExt); //On trie par longitutude X grace a des compareTo dans Capteur Exterieur et GPS
       
       
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

        jCheckBoxMenuItem1 = new javax.swing.JCheckBoxMenuItem();
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
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();

        jCheckBoxMenuItem1.setSelected(true);
        jCheckBoxMenuItem1.setText("jCheckBoxMenuItem1");

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
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(jLabel2)
                .addGap(37, 37, 37)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 1054, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 627, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 16, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("tab1", jPanel1);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1054, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 669, Short.MAX_VALUE)
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

        jMenu1.setText("Parametre");
        jMenu1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu1MouseClicked(evt);
            }
        });

        jMenuItem1.setText("Connexion");
        jMenuItem1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenuItem1MouseClicked(evt);
            }
        });
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem2.setText("Ajouter/Enlever Capteur");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Gerer les Alertes");
        jMenu2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu2MouseClicked(evt);
            }
        });
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1089, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    public Capteur findCapteur (String  str)
    {
        Capteur capteur = null ;
        
        if ( listeCapteurExt != null)
        {
         for (CapteurExterieur capt : this.listeCapteurExt)
            {
                if ( capt.getIdentifant().equals(str))
                     capteur = capt;           
            }
        }
        if ( listeCapteurInt != null)
        {
            for (CapteurInterieur capt : this.listeCapteurInt)
            {
                if ( capt.getIdentifant().equals(str))          
                    capteur= capt;                      
            }
        }
            
            return capteur;
    }
    private void reloadAbre ()
    {
         
        DefaultMutableTreeNode n = (DefaultMutableTreeNode) jTree1.getLastSelectedPathComponent();
        if ( n != null)
        {
        
            String parcours =  n.toString();
            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            model.setNumRows(0); //on remet le tableau a zero a chauqe clique
            model.setRowCount(0); 
            Capteur capteurSeule = findCapteur(parcours);
            
    
            if ( parcours.equals ("Capteur"))
            {
                if ( listeCapteurExt!= null)
                {
                                  
                    for ( CapteurExterieur capteur : this.listeCapteurExt)
                    {
                        model.addRow(new Object[]{capteur.getIdentifant(),capteur.getType(),capteur.getLocalisation().getX()+","+capteur.getLocalisation().getY()
                            ,capteur.getVal() });
                    }
                }
                if ( listeCapteurInt != null)
                {
                    for ( CapteurInterieur capteur : this.listeCapteurInt)
                    {
                        model.addRow(new Object[]{capteur.getIdentifant(),capteur.getType(),capteur.getLocalisation().getBatiment()+"/"+capteur.getLocalisation().getEtage()+
                                "/"+capteur.getLocalisation().getSalle(),capteur.getVal() });
                    }
                }

            }
            else if ( parcours.equals("Capteur Exterieur")) // On affiche tous les capteur Exterieur
            {
                if ( this.listeBatiment != null)
                {
                    if (this.listeCapteurExt != null)
                    {
                        for ( CapteurExterieur capteur : this.listeCapteurExt)
                        {
                            model.addRow(new Object[]{capteur.getIdentifant(),capteur.getType(),capteur.getLocalisation().getX()+","+capteur.getLocalisation().getY()
                                ,capteur.getVal() });
                        }
                    }
                }
            }
            else if (parcours.equals("Capteur Interieur") ) // on affiche tous les capteurs Interieur
            {
                if (this.listeCapteurInt != null)
                {
                    for ( CapteurInterieur capteur : this.listeCapteurInt)
                    {
                        model.addRow(new Object[]{capteur.getIdentifant(),capteur.getType(),capteur.getLocalisation().getBatiment()+"/"+capteur.getLocalisation().getEtage()+
                                "/"+capteur.getLocalisation().getSalle(),capteur.getVal() });
                    }
                }
            }
            else if ( listeBatiment.contains(parcours)) // on affiche tous les capteur d'un batiment
            {
                if ( this.listeCapteurInt != null)
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
            }             
            else if ( capteurSeule != null) // On affiche un seul capteur
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
                
                if ( this.listeCapteurInt != null)
                {
                    for (CapteurInterieur capteur : this.listeCapteurInt)
                    {
                        if ( capteur.getLocalisation().getBatiment().equals(parcours) || capteur.getLocalisation().getBatiment().equals(bat) && capteur.getLocalisation().getEtage().equals(parcours)
                                || capteur.getLocalisation().getSalle().equals(parcours))
                        {
                            model.addRow(new Object[]{capteur.getIdentifant(),capteur.getType(),capteur.getLocalisation().getBatiment()+"/"+capteur.getLocalisation().getEtage()+
                                 "/"+capteur.getLocalisation().getSalle(),capteur.getVal() });
                        }
                    }
              
                }
            
            }   
               
               
     
        }
        
        /* Juste le capteur selectioné */
        
       
    }
    private void jTree1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTree1MouseClicked
        this.reloadAbre();
  
    }//GEN-LAST:event_jTree1MouseClicked

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jMenu1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu1MouseClicked
        // TODO add your handling code here
      
    }//GEN-LAST:event_jMenu1MouseClicked

    private void jMenuItem1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem1MouseClicked
        // TODO add your handling code here:
        
    }//GEN-LAST:event_jMenuItem1MouseClicked

    /*Incrire un Capteur*/
    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        // TODO add your handling code here:
        FenetreIncription p =  new FenetreIncription(this.listeCapteurReseau,this.listeCapteurInt,this.listeCapteurExt);
          
        int result =JOptionPane.showConfirmDialog(null, p,"Inscription",JOptionPane.OK_CANCEL_OPTION);
        if( result  == JOptionPane.OK_OPTION)
        {
            /* On recupure le model du tableau*/
            DefaultTableModel model = p.getTable();
            
            for ( int i= 0 ;i < model.getRowCount() ; i++)
            {
                String str = model.getValueAt(i,0).toString();
                if ( model.getValueAt(i,1) != null  ) //Si la case n'a jamais etait coche
                {
                    String bol = model.getValueAt(i, 1).toString();
                    System.out.println(bol);
                    Capteur capteur  = null;
                    for ( Capteur capt : this.listeCapteurReseau) //On recu
                    {
                        if ( capt.getIdentifant().equals(str) && bol.equals("true")) //Si jamais on met pas true on peut cocher et decocher et le capteut sera ajouter
                        {
                         
                             if ( capt  instanceof CapteurExterieur)
                             {                             
                                 CapteurExterieur c  = (CapteurExterieur) capt; //on caste puis on ajoute a la list
                                 this.listeCapteurExt.add(c );
                             }
                             else if ( capt instanceof CapteurInterieur)
                             {
                                CapteurInterieur c = (CapteurInterieur) capt;
                                this.listeCapteurInt.add(c);
                             }
                        }
                        else if  ( capt.getIdentifant().equals(str) && bol.equals("false")) // On enlev le capteur present
                        {
                            System.out.println(capt);
                            if ( capt  instanceof CapteurExterieur)
                             {                              
                                 CapteurExterieur c  = (CapteurExterieur) capt; //on caste puis on ajoute a la list
                                 this.listeCapteurExt.remove(c );
                             }
                             else if ( capt instanceof CapteurInterieur)
                             {
                                CapteurInterieur c = (CapteurInterieur) capt;
                                this.listeCapteurInt.remove(c);
                             }
                                
                        }    
                        
                       
                     }
                     
                   
                    
                }
                
                
            }
                    
                
            this.reloadAbre();
          
        }
        
        
        
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    
    /* Connexion au serveur*/
    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
        FenetreConnexion p =  new FenetreConnexion();
        int result =JOptionPane.showConfirmDialog(null, p,"Connexion",JOptionPane.OK_CANCEL_OPTION);
        if( result  == JOptionPane.OK_OPTION)
        {
            /* On recupure ici les donne du Server*/
                    
           Adresse adresse = new Adresse(p.getIP(),p.getPort());
        }
        
        /* Ajouter ici tous les capteur du reseau dans cette liste*/
        // this.listeCapteurReseau.add
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenu2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu2MouseClicked
        // TODO add your handling code here:
        FenetreAlerte p =  new FenetreAlerte(this.listeCapteurExt,this.listeCapteurInt,this.listeAlerte);
        int result =JOptionPane.showConfirmDialog(null, p,"Alerte",JOptionPane.OK_CANCEL_OPTION);
        if( result  == JOptionPane.OK_OPTION)
        {
                tabAlerte = p.getTable();
               
                
               for ( int i = 0 ; i < tabAlerte.getRowCount(); i++)
               {
                   Capteur capteur = findCapteur(tabAlerte.getValueAt(i, 0).toString());
                   String valeur = tabAlerte.getValueAt(i,1).toString();
                   System.out.println(capteur.getIdentifant()+"="+valeur);
                   if ( !"".equals(valeur)) //Si jamais on ajoute une valeur a l'alerte
                   {                     
                       this.listeAlerte.put(capteur, Float.parseFloat(valeur)); // Si jamais on veut supprimer une alerte
                   }
                   else if ( valeur.equals("") && this.listeAlerte.containsKey(capteur)) //
                   {
                       System.out.println(capteur.toString());
                       this.listeAlerte.remove(capteur);
                   }
                  
                 
                   
               }
               
               jTable1.getColumn("Nom").setCellRenderer(new CustomRenderer(this.listeAlerte,this.tabAlerte));
               this.reloadAbre();
                       
               
          
        }
        
       
    }//GEN-LAST:event_jMenu2MouseClicked

    /**
     * @param args the command line arguments
     */
  
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
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
