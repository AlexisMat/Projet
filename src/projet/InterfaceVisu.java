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
import Class.CustomRenderer;
import Class.Ecoute;
import Class.GPS;
import Class.Intervalle;
import Class.Localisation;
import Class.Reseau;
import Class.TypeCapteur;
import java.io.BufferedInputStream;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import static java.util.Date.from;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

/**
 *
 * @author Quentin
 */
public class InterfaceVisu extends javax.swing.JFrame {

    /**
     * Creates new form NewJFrame
     */
    
    /* Il faut recupere les donnees du reseau*/
    private final Set<Capteur> listeCapteurReseau = new HashSet<Capteur>();
    
    /*Liste des capteur inscrit*/
    private ArrayList<CapteurInterieur> listeCapteurInt = new ArrayList<CapteurInterieur>();
    private ArrayList<CapteurExterieur> listeCapteurExt = new ArrayList<CapteurExterieur>();
    

    /* Liste pour laliste des batiment et lleur etage ainsi que leur salle qui leur sont lié*/
    Set<String> listeBatiment =  new HashSet<>();
    Map<String, Set<String>> listeEtage = new HashMap<>(); /* Prend un batiment en cle et donne un Set des Etage*/
    Map<BatimentEtage , Set<String>>listeClasse =  new HashMap<>();
   
    Map<Capteur,Float> listeAlerte = new HashMap<>();
    
    /*Modele du tableau des alertes*/
    DefaultTableModel tabAlerte = null ;
    
    boolean ecoute =  false;
    
    Reseau reseau ;
    Ecoute ec;
    private Socket s;
    private BufferedReader in;
    private PrintWriter out;
    
    boolean b = true;
    public InterfaceVisu(Set<Capteur> l ) {
       
        initComponents();
        
        GPS posRad  = new GPS(50,60);
        Intervalle interRad = new Intervalle(-10.0f,50.0f);
        
        
        this.initLocalisation(); /* On charge le fichier des Batiment, Etage Salle*/
        this.initArbre(); /*Arbre vide tant qu'on est pas connecter au reseau*/
        //this.listeCapteurExt.add(new CapteurExterieur(TypeCapteur.Temperature,posRad,"Degres","RadiateurExt",interRad,"07/01/2017",0.1f,0.2f,60,30));
        //this.listeCapteurExt.add(new CapteurExterieur(TypeCapteur.Temperature,posRad,"Degres","RadiateurExt",interRad,"07/01/2017",0.1f,0.2f,60,30));
        this.updateCapteurExterieur();
        /*if ( this.listeCapteurExt != null)
        {
            this.updateCapteurExterieur(); // Fonction qui met a jour les Capteur Exterieur 
        }
        if ( this.listeCapteurInt != null )
        {
                this.updateCapteurInterieur();
        }*/
        this.filtrage();
        
           



  
   
              
        /* On Ajoute le filtrage */
        
        
        
}
    public void ecouteReseau()
    {
        
    Thread t = new Thread() {
      public void run() {
          
          while(b)
          {
              try {
                  sleep(1000);
              } catch (InterruptedException ex) {
                  Logger.getLogger(InterfaceVisu.class.getName()).log(Level.SEVERE, null, ex);
              }
                lireCapteurReseau();
                reloadTable();
          }
        
      }
    };
    t.start();
    
        
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
       arbre.reload();
       //root.add(nodeCapteurInterieur);
       
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
         
          
       }
        root.add(nodeCapteurInterieur);
        arbre.reload();
     
    }
    
    private DefaultMutableTreeNode findNode ( DefaultMutableTreeNode n , DefaultMutableTreeNode e)
    {
        if ( n != null)
        {
            for ( int i = 0 ; i < n.getChildCount(); i++)
            {

                DefaultMutableTreeNode courant = (DefaultMutableTreeNode) n.getChildAt(i);
                if ( courant.toString().equals(e.toString()))
                {
                    return (DefaultMutableTreeNode) n.getChildAt(i);
                }
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
       DefaultMutableTreeNode nodeCapteurInterieur =(DefaultMutableTreeNode) root.getChildAt(1);
       
      
       
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
           
            if ( this.findNode(nodeSalle, nodeCapteur) == null)
              nodeSalle.add(nodeCapteur); // On ajoute  le capteur a l'arboresence  si il n'y est pas deja
                
           
       }
       
          arbre.reload();   
    }
     private void updateCapteurExterieur()
    {
       DefaultTreeModel arbre;
       arbre= (DefaultTreeModel) jTree1.getModel();
       DefaultMutableTreeNode root = (DefaultMutableTreeNode) arbre.getRoot(); /* On recupre la racine*/
       Collections.sort(this.listeCapteurExt); //On trie par longitutude X grace a des compareTo dans Capteur Exterieur et GPS
       
       
       DefaultMutableTreeNode nodeCapteurExterieur = (DefaultMutableTreeNode) root.getChildAt(0); /*On contruit l'arbe a partir des liste des capteurs*/
        //System.out.println("Nombre de fils"+root.getChildCount()+"nom"+nodeCapteurExterieur.toString());
       for ( CapteurExterieur capteur : this.listeCapteurExt)
       {
                   
            DefaultMutableTreeNode nodeCapteur = new javax.swing.tree.DefaultMutableTreeNode(capteur.getIdentifant());
            if ( this.findNode(nodeCapteurExterieur, nodeCapteur) == null)
                nodeCapteurExterieur.add(nodeCapteur);
           
           
       }
       
       arbre.reload();
       
    }
    private void enleverNode (Capteur c)
    {   
       
       jTree1.setModel(new DefaultTreeModel(new DefaultMutableTreeNode("Capteur") ));
       jTree1.updateUI();
       this.initArbre();
       this.updateCapteurExterieur();
       this.updateCapteurInterieur();
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
        jButton1 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        test = new javax.swing.JLabel();
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

        jButton1.setText("Deconnexion");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
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
                .addGap(368, 368, 368)
                .addComponent(jButton1)
                .addContainerGap(248, Short.MAX_VALUE))
            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 627, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addGap(0, 13, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Tabeau des Capteurs", jPanel1);

        test.setText("jLabel1");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(292, 292, 292)
                .addComponent(test, javax.swing.GroupLayout.PREFERRED_SIZE, 523, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(325, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(140, 140, 140)
                .addComponent(test)
                .addContainerGap(515, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Valeur d'un capteur", jPanel2);

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
            .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1184, Short.MAX_VALUE)
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
    private void reloadTable ()
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
                 //jTable1.getColumn("Nom").setCellRenderer(new CustomRenderer(this.listeAlerte,this.tabAlerte));
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
               /* Ici on genere le graphique pour le capteur*/
                test.setText(this.chargerTableau(capteurSeule).toString());
                
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
        this.reloadTable();
  
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

    /* Fonction qui verifie si un Capteur est deja present dans le fichier des Capteur pour eviter qu'il est deux foix la meme ligne*/
    private boolean dejaPresent ( Capteur capt)
    {
        boolean b = false;
         try{
                    InputStream flux=new FileInputStream("./Capteur.txt"); 
                    InputStreamReader lecture=new InputStreamReader(flux);
                    BufferedReader buff=new BufferedReader(lecture);
                    String ligne;
                    while ((ligne=buff.readLine())!=null){
                        
                        String[] tab;
                        tab = ligne.split(":");
                        System.out.println("tab"+tab[0]);
                        if ( tab[0].equals(capt.getIdentifant()))
                          b = true;
                    }
                    buff.close(); 
                                                         
            }		
               catch (Exception e){
                  System.out.println(e.toString());
             
               }
         
         return b;
        
    }
    
    /*permet d'ajouter un capteur dans le fichier*/
     private void addFichierCapteur(Capteur capt) {
       
        if ( !this.dejaPresent(capt))
        {     
            BufferedWriter bufferedWriter ;
            try {
                bufferedWriter = new BufferedWriter(new FileWriter("./Capteur.txt", true));

                bufferedWriter.write(capt.getIdentifant()+":"+capt.getVal());
                bufferedWriter.newLine();
                bufferedWriter.close();
            } catch (IOException ex) {
                Logger.getLogger(InterfaceVisu.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
         
    }
     /*Ajoute une valeur dans le fichier il faut lui passer le capteur d'on on vient de recevoir une nouvelle valeur*/
    private void addValeurFichierCapteur(Capteur capt)
    {
        BufferedWriter bufferedWriter ;
        try{
                    bufferedWriter = new BufferedWriter(new FileWriter("./Capteur1.txt", true)); // On ecrit dans un deuxieme fihcier
                    InputStream flux=new FileInputStream("./Capteur.txt"); 
                     
                    InputStreamReader lecture=new InputStreamReader(flux);
                    BufferedReader buff=new BufferedReader(lecture);
                    String ligne;
                    while ((ligne=buff.readLine())!=null){
                                         
                        String[] tab;
                        tab = ligne.split(":");
                        System.out.println("tab"+tab[0]);
                        if ( tab[0].equals(capt.getIdentifant()))
                        {
                            for (String tab1 : tab) {
                                bufferedWriter.write(tab1);
                                bufferedWriter.write(":");
                            }
                             bufferedWriter.write(capt.getVal()+"");       // On ajoute la nouvel valeur
                                 
                        }
                        else
                             bufferedWriter.write(ligne);
                        
                         bufferedWriter.newLine();
                    }
                    buff.close(); 
                    bufferedWriter.close();
                                                         
            }		
               catch (Exception e){
                  System.out.println(e.toString());
             
               }
        
        new File("./Capteur.txt").delete(); //on Suprimel'ancien fichier et on le remplace par le nouveau
        new File("./Capteur1.txt").renameTo(new File("./Capteur.txt"));
    }
   
    private ArrayList<Float> chargerTableau (Capteur capt)
    {
        
          ArrayList<Float> l = new ArrayList<>();
         try{
                    InputStream flux=new FileInputStream("./Capteur.txt"); 
                    InputStreamReader lecture=new InputStreamReader(flux);
                    BufferedReader buff=new BufferedReader(lecture);
                    String ligne;
                    while ((ligne=buff.readLine())!=null){
                        
                        String[] tab;
                        tab = ligne.split(":");
                        if ( tab[0].equals(capt.getIdentifant()))
                        {   
                            for (int  i = 1 ; i < tab.length; i++)
                            {
                                l.add(Float.parseFloat(tab[i]));
                            }
                        }
                        
                    }
                    buff.close(); 
                                                         
            }		
               catch (Exception e){
                  System.out.println(e.toString());
             
               }
        
         return l;
    }
    /*Incrire un Capteur*/
    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        // TODO add your handling code here:
         lireCapteurReseau();
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
                    for ( Capteur capt : this.listeCapteurReseau) //On teste pour tout les capteur sur le reseau
                    {
                        if ( capt.getIdentifant().equals(str) && bol.equals("true")) //Si jamais on met pas true on peut cocher et decocher et le capteut sera ajouter
                        {
                          
                             if ( capt  instanceof CapteurExterieur)
                             {                             
                                 CapteurExterieur c  = (CapteurExterieur) capt; //on caste puis on ajoute a la list
                                 if( !listeCapteurExt.contains(c))
                                 {
                                     System.out.println("Incrit le capreut "+capt.getIdentifant());
                                     this.listeCapteurExt.add(c ); // On ajoute le capteur a la liste
                                     try {
                                         reseau.InscriptionVisu(c);//On a joute le capteur au reseai
                                     } catch (IOException ex) {
                                         Logger.getLogger(InterfaceVisu.class.getName()).log(Level.SEVERE, null, ex);
                                     }
                                 }
                                 this.updateCapteurExterieur();
                             }
                             else if ( capt instanceof CapteurInterieur)
                             {
                                CapteurInterieur c = (CapteurInterieur) capt;
                                if ( !listeCapteurInt.contains(c))
                                {
                                    this.listeCapteurInt.add(c);
                                    try {
                                         reseau.InscriptionVisu(c);//On a joute le capteur au reseai
                                     } catch (IOException ex) {
                                         Logger.getLogger(InterfaceVisu.class.getName()).log(Level.SEVERE, null, ex);
                                     }
                                }
                                this.updateCapteurInterieur();
                             }
                            
                                
                                 this.addFichierCapteur(capt);
                             
                        }
                        else if  ( capt.getIdentifant().equals(str) && bol.equals("false")) // On enlev le capteur present
                        {
                           
                            if ( capt  instanceof CapteurExterieur)
                             {                              
                                 CapteurExterieur c  = (CapteurExterieur) capt; //on caste puis on leretire de la liste
                                 /* Retirer du reseau*/
                                 this.listeCapteurExt.remove(c );
                             }
                             else if ( capt instanceof CapteurInterieur)
                             {
                                CapteurInterieur c = (CapteurInterieur) capt;
                                /*Retirer du reseau*/
                                this.listeCapteurInt.remove(c);
                             }
                                
                            this.enleverNode(capt);
                            try {
                                         reseau.DesinscriptionVisu(capt);//On a joute le capteur au reseai
                                     } catch (IOException ex) {
                                         Logger.getLogger(InterfaceVisu.class.getName()).log(Level.SEVERE, null, ex);
                                     }
                        }    
                        
                       
                     }
                     
                   
                    
                }
                
                
            }
           
            this.reloadTable();
          
        }
        
        
        
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    public boolean estPresent (String name)
    {
        for ( Capteur capt : this.listeCapteurReseau)
        {
            if (capt.getIdentifant().equals(name))
            {
                return true;
            }
        }
        
        return false;
    }
    public void lireCapteurReseau ()
    {
        try{
                    InputStream flux=new FileInputStream("./rsce.txt"); 
                    InputStreamReader lecture=new InputStreamReader(flux);
                    BufferedReader buff=new BufferedReader(lecture);
                    String ligne;
                    while ((ligne=buff.readLine())!=null){
                        
                        String[] tab;
                        tab = ligne.split(";");
                         
                        System.out.println("ligne = "+ligne+"tab = "+tab.length);
                        if ( tab[0].equals("CapteurPresent"))
                        {   
                            
                            System.out.println("ligne = "+ligne+"tab = "+tab.length);
                            if ( !this.estPresent(tab[1]))
                            {
                                if ( tab.length == 7 ) // Si jamais le tableau fait  7  alors c'est forcement un capteur interieur 
                                this.listeCapteurReseau.add(new CapteurInterieur(tab[2],new Localisation(tab[3],tab[4],tab[5]),"",tab[1],null,null,0.f,0.f,null,0.f));
                                else if (tab.length == 5)
                                this.listeCapteurReseau.add(new CapteurExterieur(tab[2],new GPS(Float.parseFloat(tab[3]),Float.parseFloat(tab[4])),"",tab[1],null,null,0.f,0.f,null,0.f));
                     
                            
                            
                            }
                         
                        }
                        else if ( tab[0].equals("ValeurCapteur"))
                        {
                           
                            Capteur capteurModfier = this .findCapteur(tab[1]);
                            System.out.println("Capteur"+capteurModfier);
                            if ( capteurModfier != null)
                                capteurModfier.setVal(Float.parseFloat(tab[2]));
                            //if ( Float.parseFloat(tab[2]) != capteurModfier.getVal()) //Si la valeur change alors on l'ecrit dans le fichier des valeurs
                                //this.addValeurFichierCapteur(capteurModfier);
                            else
                                b = false;
                                
                               
                        }
                        
                        
                        
                    }
                    buff.close(); 
                                                         
            }		
               catch (IOException | NumberFormatException e){
                  System.out.println("Erreuer"+e.toString());
             
               }
        
        
        
    }
    
    /* Connexion au serveur*/
    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
        FenetreConnexion p =  new FenetreConnexion();
        int result =JOptionPane.showConfirmDialog(null, p,"Connexion",JOptionPane.OK_CANCEL_OPTION);
        if( result  == JOptionPane.OK_OPTION)
            
        {
           
           if ( p.getId() != null && p.getPort() != 0 && p.getIP()!= null)
           {
                try {
                     /* On recupure ici les donne du Server*/    
                     s = new Socket(p.getIP(),p.getPort());
                     in = new  BufferedReader(new InputStreamReader(s.getInputStream()));
                     out = new PrintWriter(s.getOutputStream());

                     reseau =  new Reseau(s, in, out, "", p.getId());
                     reseau.Connexion();
                     ec =  new Ecoute(s,in,out);
                     ec.StartEc();
                     ecoute=true;
                     this.ecouteReseau();



                 } catch (IOException ex) {
                     Logger.getLogger(InterfaceVisu.class.getName()).log(Level.SEVERE, null, ex);
                     b = false;
                 }
           }
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
                   
                   if ( !"".equals(valeur)) //Si jamais on ajoute une valeur a l'alerte
                   {                     
                       this.listeAlerte.put(capteur, Float.parseFloat(valeur)); // Si jamais on veut supprimer une alerte
                   }
                   else if ( valeur.equals("") && this.listeAlerte.containsKey(capteur)) //
                   {
                      
                       this.listeAlerte.remove(capteur);
                   }
                  
                 
                   
               }
               
               jTable1.getColumn("Nom").setCellRenderer(new CustomRenderer(this.listeAlerte,this.tabAlerte));
               this.reloadTable();
                       
               
          
        }
        
       
    }//GEN-LAST:event_jMenu2MouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        b =false;
        try {
            reseau.Deconnexion();
        } catch (IOException ex) {
            Logger.getLogger(InterfaceVisu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
  
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
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
    private javax.swing.JLabel test;
    // End of variables declaration//GEN-END:variables

   
}
