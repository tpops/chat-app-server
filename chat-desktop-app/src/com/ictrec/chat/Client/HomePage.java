/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ictrec.chat.Client;

import com.Macropax.BadguysChat.Profile;
import com.Macropax.BadguysChat.Users;
import java.awt.Color;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import sun.audio.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.*;
import java.io.BufferedWriter;
import java.net.DatagramPacket;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JList;
import java.net.URL;
/**
 *
 * @author POPOOLA
 */
public class HomePage extends javax.swing.JFrame {

    private void PeopleSearch() {
        if (!"".equals(SearchField.getText())){
        client.Send(("¬PS¬"+SearchField.getText().toLowerCase()+"¬N¬").getBytes());
        SLoading.setVisible(false);
        
        }
    }

    private void SendFriendRequest() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    private List <Integer> SearchId;
    enum ScreenState
            {
        HomePage,
        Profile,
        Settings,
        Loading,
        Searching,
        SearchingPanel
    };
    int NewMessages;
    ScreenState SS;
    List <Users> FL ;
    boolean chat;
    Client client;
    String UserName;
    final int UserId;
    FriendListModel FLM ;
    int CurrentChatId =-1;
    String CurrentChatName="";
    private Thread recieve ,friends,ManageGUI;
    private boolean running = false;
    DatagramPacket pckt ;
    MessageHandling MH ;
   SettingsHandler SH;
   SettingsC s;
   Profile pf;
   boolean RequestForloading;
   boolean RequestForHomePage;
   boolean RequestForSettings;
   boolean RequestForProfile;
   int CurrentSearchIndex =-1;
   String CurrentSearchInd ="";
     
   private List<Integer> FriendsId ;
   
    /**
     * Creates new form HomePage
     */
    public HomePage(final Client Client,String UsrName, int Userid ) {
          
          initComponents();
          FNwMessage.setVisible(false);
          SS = ScreenState.HomePage;
          
          RequestForHomePage = true;
          RequestForloading = false;
          RequestForSettings = false;
          RequestForProfile = false;
          LoadingThread();
          HomePanel.setVisible(true);
          SearchPanel.setVisible(false);  
          Profile.setVisible(false);
          
          
//             ManageGUI  = new Thread ("ManageGUI")
//                {
//                    public void run ()
//                {
//                    while(running)
//                {
//                    GUIhandler();
//                    
//                    
//                }
//                }
//                }; ManageGUI.start();
          this.UserName = UsrName;
              
         this.client = Client;
        
        this.UserId = Userid;
        running = true;
      setVisible(true);
               SH = new SettingsHandler(UserName); 
               MH = new MessageHandling(UserName);
               s = SH.GetSetting();
              
           NewMessages = 0;    
              
        chat = false;
        FriendsId = new ArrayList<Integer>();
        
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
       
        client.Send(("/p/Stati/inf/"+UserId+"/id//e/").getBytes());
       
        
        txtName.setText(UsrName);
 
        
        
        friends = new Thread("FriendManager")
        {
            public void run()
            {
                 
                while (running)
                {  
                    if (SS == ScreenState.HomePage){
                    try {
                        Thread.sleep(700);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(HomePage.class.getName()).log(Level.SEVERE, null, ex);
                       
                    }
                   
                     client.Send(("/p/FL/inf/"+UserId+"/id//e/").getBytes());
                     
                    try {
                        Thread.sleep(3700);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(HomePage.class.getName()).log(Level.SEVERE, null, ex);
                       
                    }
                    
                    }
                       
                }
            }
        };friends.start();
        recieve = new Thread("Recieve")
        {
            public void run()
            {
                while(running)
                {
                   
                    pckt = client.Recieve();
                    String msg = new String (pckt.getData());
                  
                    if (msg.startsWith("/p/"))
                    {
                       String type = msg.split("/p/|/id/|/e/")[1];
                       if ("Stat".equals(type))
                       {
                           try {
                          txtStatus.setText( msg.split("/p/|/id/|/e/")[2]);
                           } catch (Exception e)
                           {
                               
                           }
                       }
                       
                    } else if (msg.contains("Profile")&&!msg.startsWith("/pm/"))
                    {
                        
                        try{
                        ByteArrayInputStream bis = new ByteArrayInputStream(pckt.getData());
                        ObjectInputStream Is = new ObjectInputStream(bis);
                        
                    pf = (Profile)Is.readObject();
                    if (SS == ScreenState.Profile){
                          Pcountry1.setText(pf.Country);
                          Pname.setText(CurrentChatName);
                          Pinfo.setText(CurrentChatName+"'s Profile Page");
                          PBlckd.setText("N/A");
                          PPhoneNum.setText(pf.PN);
                          PStatus.setText(pf.Status);
                          HomePanel.setVisible(false);
                          Profile.setVisible(true);     
                          SearchPanel.setVisible(false);  
                          loadingPanel.setVisible(false);
                          Search.setVisible(false);
                    } else if (SS == ScreenState.SearchingPanel){
                        SPcountry.setText(pf.Country);
                          SPname.setText(CurrentSearchInd);
                          
                          SPBlckd.setText("N/A");
                          SPPhoneNum.setText(pf.PN);
                          SearchResultPanel.setVisible(true);
                          loadingPanel.setVisible(false);
                          
                    }
                          
                          
                     
                            
//                        new ProfileFrm(CurrentChatName,pf);
                       
                        } catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                        
                    }
                    else if (msg.contains("Users")&&!msg.startsWith("/pm/"))
                       {
                          
                           
                    try {
                           FL = new ArrayList<>();
                          
                        ByteArrayInputStream bis = new ByteArrayInputStream(pckt.getData());
                        ObjectInputStream Is = new ObjectInputStream (bis);
                        
                        FL = (List<Users>)Is.readObject();
                        ProcessFL(FL);
                          
                        
                    } catch (IOException ex) {
                        System.out.println("couldnt read file");
                                            } catch (ClassNotFoundException ex) {
                        System.out.println("No list has been sent over yet");
                    }
                       } else if (msg.startsWith("/pm/"))
                       {
                           int RecieveId = Integer.parseInt(msg.substring(4,msg.length()).split("¬FId¬|/e/")[0]);
                           String RecieveMsg =msg.substring(4,msg.length()).split("¬FId¬|/e/")[1];
                          
                           // check if the user isblocked if he is dun recieve d message
                           
                         
                        MH.PushMessages(RecieveId,RecieveMsg);
                        if (s.EN)
                        {
                            if (SS == ScreenState.Profile|| SS == ScreenState.Settings||
                                    SS == ScreenState.Searching)
                            {
                                FNwMessage.setVisible(true);
                                NewMessages++;
                                FNwMessage.setText(""+NewMessages);
                             
                               
                            }
                            else {
                                FNwMessage.setVisible(false);
                                NewMessages =0;
                            }
                            
                        }
                          if (s.sound)
                          {
                              // play input Message
                            try {
                                InputStream Is;
                                 AudioStream CS;
                                 Is = new FileInputStream(this.getClass().getResource("/com/ictrec/chat/Client/Sounds/chatNotification.wav").getPath());
                                 
                               CS = new AudioStream (Is);
                               
                               AudioPlayer.player.start(CS);
                               
                               
                                       
        } catch (IOException ex) {
            Logger.getLogger(HomePage.class.getName()).log(Level.SEVERE, null, ex);
        }
                             
                             
                          }             
                                
                             
                          
                          if (CurrentChatId == RecieveId)
                          {
                              Console(RecieveMsg);
                              
                          }else
                          {
                              String Display="";
                              for (int i =0; i<FL.size();i++)
                              {
                                  Users s = FL.get(i);
                                  if (s.Id==RecieveId)
                                  {
                                    Display =s.Display();
                                    break;
                                  }
                              }
                              // set selected value if the display is thesame thing
                              // as that in the model
                              if (!"".equals(Display))
                              {
                                  FriendList.setSelectedValue(Display, false);
                              }
                          }
                          
                          
                       
                }
                       }
                       
            
            }
        };recieve.start();
     
      
    }


    private void LoadingThread() {
        Thread TLoading = new Thread("loading")
        {
            public void run()
            {
                loadingPanel.setVisible(true);
                try
                {
                    Thread.sleep(1400);
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
                loadingPanel.setVisible(false);
                
            }
        };TLoading.start();
    }

    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel5 = new javax.swing.JLabel();
        buttonGroup1 = new javax.swing.ButtonGroup();
        jMenuItem3 = new javax.swing.JMenuItem();
        loadingPanel = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        LoadingGIF = new javax.swing.JLabel();
        LoadingBG = new javax.swing.JLabel();
        SearchPanel = new javax.swing.JPanel();
        HeaderMessage = new javax.swing.JLabel();
        HeaderMessage1 = new javax.swing.JLabel();
        SearchResultPanel = new javax.swing.JPanel();
        Seaback2 = new javax.swing.JLabel();
        ProfilePix = new javax.swing.JLabel();
        AddasFriend = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        SPPhoneNum = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        SPname = new javax.swing.JLabel();
        SPBlckd = new javax.swing.JLabel();
        SPcountry = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        Search1 = new javax.swing.JLabel();
        Header = new javax.swing.JLabel();
        SearchField = new javax.swing.JTextField();
        jScrollPane5 = new javax.swing.JScrollPane();
        SList = new javax.swing.JList();
        SLoading = new javax.swing.JLabel();
        SideBar = new javax.swing.JLabel();
        Seaback1 = new javax.swing.JLabel();
        HeaderMessage2 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        Profile = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        Pinfo = new javax.swing.JLabel();
        WelcomeProfile = new javax.swing.JLabel();
        Ppix = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        PPhoneNum = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        Pname = new javax.swing.JLabel();
        PBlckd = new javax.swing.JLabel();
        Pcountry1 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        PStatus = new javax.swing.JTextArea();
        InfoBackGround = new javax.swing.JLabel();
        Pback = new javax.swing.JLabel();
        BGP = new javax.swing.JLabel();
        Footer = new javax.swing.JPanel();
        help = new javax.swing.JLabel();
        FNwMessage = new javax.swing.JLabel();
        Search = new javax.swing.JLabel();
        FooterBG = new javax.swing.JLabel();
        HomePanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        FriendList = new javax.swing.JList();
        jLabel8 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtChatName = new javax.swing.JTextPane();
        jLabel2 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtName = new javax.swing.JLabel();
        BSubmit = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        VProfile = new javax.swing.JButton();
        txtStatus = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        StatusMsg = new javax.swing.JLabel();
        Grpchat7 = new javax.swing.JButton();
        Background = new javax.swing.JLabel();
        txtMsg = new javax.swing.JTextField();
        Send = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtDisplay = new javax.swing.JTextArea();
        jLabel3 = new javax.swing.JLabel();
        HeaderPix = new javax.swing.JLabel();
        BG = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        Msettings = new javax.swing.JMenuItem();
        MLogout = new javax.swing.JMenuItem();
        MExit = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        MProfile = new javax.swing.JMenuItem();

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/Macropax/BadguysChat/Client/Images/bg.png"))); // NOI18N

        jMenuItem3.setText("jMenuItem3");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle(UserName);
        setBackground(new java.awt.Color(102, 102, 0));
        setResizable(false);
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        loadingPanel.setEnabled(false);
        loadingPanel.setLayout(null);

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("LOADING...... PLEASE WAIT");
        loadingPanel.add(jLabel13);
        jLabel13.setBounds(80, 100, 320, 70);

        LoadingGIF.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/Macropax/BadguysChat/Client/Images/HomePage/loading_indicator.gif"))); // NOI18N
        loadingPanel.add(LoadingGIF);
        LoadingGIF.setBounds(170, 180, 100, 20);

        LoadingBG.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/Macropax/BadguysChat/Client/Images/HomePage/LoadingBG.jpg"))); // NOI18N
        loadingPanel.add(LoadingBG);
        LoadingBG.setBounds(0, 0, 470, 300);

        SearchPanel.setLayout(null);

        HeaderMessage.setFont(new java.awt.Font("Tekton Pro", 1, 18)); // NOI18N
        HeaderMessage.setForeground(new java.awt.Color(255, 255, 255));
        HeaderMessage.setText("Search ");
        SearchPanel.add(HeaderMessage);
        HeaderMessage.setBounds(10, 50, 100, 40);

        HeaderMessage1.setFont(new java.awt.Font("Tekton Pro", 1, 18)); // NOI18N
        HeaderMessage1.setForeground(new java.awt.Color(255, 255, 255));
        HeaderMessage1.setText("Search For New Friends");
        SearchPanel.add(HeaderMessage1);
        HeaderMessage1.setBounds(20, 0, 190, 40);

        SearchResultPanel.setLayout(null);

        Seaback2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/Macropax/BadguysChat/Client/Images/Profile/BackNo1.png"))); // NOI18N
        Seaback2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Seaback2MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                Seaback2MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                Seaback2MouseExited(evt);
            }
        });
        SearchResultPanel.add(Seaback2);
        Seaback2.setBounds(570, 370, 90, 40);

        ProfilePix.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/Macropax/BadguysChat/Client/Images/HomePage/icon_myaccount.png"))); // NOI18N
        SearchResultPanel.add(ProfilePix);
        ProfilePix.setBounds(0, 0, 150, 120);

        AddasFriend.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/Macropax/BadguysChat/Client/Images/Search/AddasFriendNo.png"))); // NOI18N
        AddasFriend.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                AddasFriendMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                AddasFriendMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                AddasFriendMouseExited(evt);
            }
        });
        SearchResultPanel.add(AddasFriend);
        AddasFriend.setBounds(480, 0, 180, 30);

        jLabel17.setFont(new java.awt.Font("Tekton Pro", 0, 18)); // NOI18N
        jLabel17.setText("Name:");
        jLabel17.setToolTipText("");
        SearchResultPanel.add(jLabel17);
        jLabel17.setBounds(170, 130, 60, 20);

        jLabel20.setFont(new java.awt.Font("Tekton Pro", 0, 18)); // NOI18N
        jLabel20.setText("Blocked:");
        jLabel20.setToolTipText("");
        SearchResultPanel.add(jLabel20);
        jLabel20.setBounds(170, 190, 70, 20);

        SPPhoneNum.setFont(new java.awt.Font("Tekton Pro", 0, 18)); // NOI18N
        SPPhoneNum.setToolTipText("");
        SearchResultPanel.add(SPPhoneNum);
        SPPhoneNum.setBounds(310, 310, 180, 20);

        jLabel21.setFont(new java.awt.Font("Tekton Pro", 0, 18)); // NOI18N
        jLabel21.setText("Country:");
        jLabel21.setToolTipText("");
        SearchResultPanel.add(jLabel21);
        jLabel21.setBounds(170, 250, 70, 20);

        jLabel22.setFont(new java.awt.Font("Tekton Pro", 0, 18)); // NOI18N
        jLabel22.setText("PhoneNum:");
        jLabel22.setToolTipText("");
        SearchResultPanel.add(jLabel22);
        jLabel22.setBounds(170, 310, 90, 20);

        SPname.setFont(new java.awt.Font("Tekton Pro", 0, 18)); // NOI18N
        SPname.setToolTipText("");
        SearchResultPanel.add(SPname);
        SPname.setBounds(310, 130, 320, 20);

        SPBlckd.setFont(new java.awt.Font("Tekton Pro", 0, 18)); // NOI18N
        SPBlckd.setToolTipText("");
        SearchResultPanel.add(SPBlckd);
        SPBlckd.setBounds(310, 190, 130, 20);

        SPcountry.setFont(new java.awt.Font("Tekton Pro", 0, 18)); // NOI18N
        SPcountry.setToolTipText("");
        SearchResultPanel.add(SPcountry);
        SPcountry.setBounds(310, 250, 140, 20);

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/Macropax/BadguysChat/Client/Images/HomePage/Background.png"))); // NOI18N
        SearchResultPanel.add(jLabel11);
        jLabel11.setBounds(0, 0, 660, 410);

        SearchPanel.add(SearchResultPanel);
        SearchResultPanel.setBounds(200, 40, 660, 410);

        Search1.setForeground(new java.awt.Color(0, 102, 204));
        Search1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/Macropax/BadguysChat/Client/Images/HomePage/icon_search.png"))); // NOI18N
        Search1.setText("Search ");
        Search1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Search1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Search1MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                Search1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                Search1MouseExited(evt);
            }
        });
        SearchPanel.add(Search1);
        Search1.setBounds(110, 130, 80, 20);

        Header.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/Macropax/BadguysChat/Client/Images/HomePage/header_back_inner_rtl.png"))); // NOI18N
        SearchPanel.add(Header);
        Header.setBounds(0, 0, 860, 40);
        SearchPanel.add(SearchField);
        SearchField.setBounds(10, 90, 180, 30);

        SList.setBackground(new java.awt.Color(0, 0, 0));
        SList.setForeground(new java.awt.Color(255, 255, 255));
        SList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        SList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                SListMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(SList);

        SearchPanel.add(jScrollPane5);
        jScrollPane5.setBounds(0, 190, 200, 260);

        SLoading.setForeground(new java.awt.Color(0, 102, 102));
        SLoading.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/Macropax/BadguysChat/Client/Images/HomePage/loading.gif"))); // NOI18N
        SLoading.setText("Searching...");
        SearchPanel.add(SLoading);
        SLoading.setBounds(60, 170, 110, 16);

        SideBar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/Macropax/BadguysChat/Client/Images/HomePage/THEME_3_BEACH_GROUND.png"))); // NOI18N
        SearchPanel.add(SideBar);
        SideBar.setBounds(0, 40, 200, 410);

        Seaback1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/Macropax/BadguysChat/Client/Images/Profile/BackNo1.png"))); // NOI18N
        Seaback1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Seaback1MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                Seaback1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                Seaback1MouseExited(evt);
            }
        });
        SearchPanel.add(Seaback1);
        Seaback1.setBounds(750, 400, 90, 40);

        HeaderMessage2.setFont(new java.awt.Font("Tekton Pro", 1, 18)); // NOI18N
        HeaderMessage2.setForeground(new java.awt.Color(255, 255, 255));
        HeaderMessage2.setText("IGNITE YOUR PASSION");
        SearchPanel.add(HeaderMessage2);
        HeaderMessage2.setBounds(430, 120, 190, 120);

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/Macropax/BadguysChat/Client/Images/HomePage/Background.png"))); // NOI18N
        SearchPanel.add(jLabel10);
        jLabel10.setBounds(200, 40, 660, 410);

        Profile.setEnabled(false);
        Profile.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ProfileKeyPressed(evt);
            }
        });
        Profile.setLayout(null);

        jLabel14.setFont(new java.awt.Font("Tekton Pro", 0, 18)); // NOI18N
        jLabel14.setText("Status:");
        jLabel14.setToolTipText("");
        Profile.add(jLabel14);
        jLabel14.setBounds(360, 50, 70, 20);

        Pinfo.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        Pinfo.setForeground(new java.awt.Color(204, 204, 204));
        Profile.add(Pinfo);
        Pinfo.setBounds(10, 10, 260, 20);

        WelcomeProfile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/Macropax/BadguysChat/Client/Images/HomePage/header_back_inner_rtl.png"))); // NOI18N
        Profile.add(WelcomeProfile);
        WelcomeProfile.setBounds(0, 0, 860, 40);

        Ppix.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/Macropax/BadguysChat/Client/Images/HomePage/icon_myaccount.png"))); // NOI18N
        Ppix.setText("PPIX");
        Profile.add(Ppix);
        Ppix.setBounds(0, 40, 270, 200);

        jLabel15.setFont(new java.awt.Font("Tekton Pro", 0, 18)); // NOI18N
        jLabel15.setText("Name:");
        jLabel15.setToolTipText("");
        Profile.add(jLabel15);
        jLabel15.setBounds(360, 160, 60, 20);

        jLabel16.setFont(new java.awt.Font("Tekton Pro", 0, 18)); // NOI18N
        jLabel16.setText("Blocked:");
        jLabel16.setToolTipText("");
        Profile.add(jLabel16);
        jLabel16.setBounds(360, 220, 70, 20);

        PPhoneNum.setFont(new java.awt.Font("Tekton Pro", 0, 18)); // NOI18N
        PPhoneNum.setToolTipText("");
        Profile.add(PPhoneNum);
        PPhoneNum.setBounds(500, 340, 180, 20);

        jLabel18.setFont(new java.awt.Font("Tekton Pro", 0, 18)); // NOI18N
        jLabel18.setText("Country:");
        jLabel18.setToolTipText("");
        Profile.add(jLabel18);
        jLabel18.setBounds(360, 280, 70, 20);

        jLabel19.setFont(new java.awt.Font("Tekton Pro", 0, 18)); // NOI18N
        jLabel19.setText("PhoneNum:");
        jLabel19.setToolTipText("");
        Profile.add(jLabel19);
        jLabel19.setBounds(360, 340, 90, 20);

        Pname.setFont(new java.awt.Font("Tekton Pro", 0, 18)); // NOI18N
        Pname.setToolTipText("");
        Profile.add(Pname);
        Pname.setBounds(500, 160, 320, 20);

        PBlckd.setFont(new java.awt.Font("Tekton Pro", 0, 18)); // NOI18N
        PBlckd.setToolTipText("");
        Profile.add(PBlckd);
        PBlckd.setBounds(500, 220, 130, 20);

        Pcountry1.setFont(new java.awt.Font("Tekton Pro", 0, 18)); // NOI18N
        Pcountry1.setToolTipText("");
        Profile.add(Pcountry1);
        Pcountry1.setBounds(500, 280, 140, 20);

        PStatus.setEditable(false);
        PStatus.setColumns(20);
        PStatus.setRows(5);
        jScrollPane4.setViewportView(PStatus);

        Profile.add(jScrollPane4);
        jScrollPane4.setBounds(440, 50, 360, 80);

        InfoBackGround.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/Macropax/BadguysChat/Client/Images/HomePage/THEME_3_BEACH_GROUND_1.png"))); // NOI18N
        Profile.add(InfoBackGround);
        InfoBackGround.setBounds(0, 240, 270, 210);

        Pback.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/Macropax/BadguysChat/Client/Images/Profile/BackNo1.png"))); // NOI18N
        Pback.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                PbackMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                PbackMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                PbackMouseExited(evt);
            }
        });
        Profile.add(Pback);
        Pback.setBounds(750, 400, 90, 40);

        BGP.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/Macropax/BadguysChat/Client/Images/HomePage/Background.png"))); // NOI18N
        Profile.add(BGP);
        BGP.setBounds(270, 40, 590, 410);

        Footer.setLayout(null);

        help.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/Macropax/BadguysChat/Client/Images/FooterBar/msg_question.gif"))); // NOI18N
        Footer.add(help);
        help.setBounds(10, 10, 20, 20);

        FNwMessage.setForeground(new java.awt.Color(255, 0, 0));
        FNwMessage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/Macropax/BadguysChat/Client/Images/FooterBar/NewMsg.gif"))); // NOI18N
        Footer.add(FNwMessage);
        FNwMessage.setBounds(50, 10, 80, 20);

        Search.setForeground(new java.awt.Color(255, 0, 0));
        Search.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/Macropax/BadguysChat/Client/Images/HomePage/icon_search.png"))); // NOI18N
        Search.setText("Search For Users");
        Search.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                SearchMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                SearchMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                SearchMouseExited(evt);
            }
        });
        Footer.add(Search);
        Search.setBounds(660, 10, 190, 20);

        FooterBG.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/Macropax/BadguysChat/Client/Images/HomePage/header_back_inner_rtl.png"))); // NOI18N
        Footer.add(FooterBG);
        FooterBG.setBounds(0, 0, 860, 40);

        HomePanel.setLayout(null);

        FriendList.setBackground(new java.awt.Color(0, 0, 0));
        FriendList.setFont(new java.awt.Font("Tekton Pro", 0, 14)); // NOI18N
        FriendList.setForeground(new java.awt.Color(255, 255, 255));
        FriendList.setSelectionBackground(new java.awt.Color(0, 255, 255));
        FriendList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                FriendListMouseClicked(evt);
            }
        });
        FriendList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                FriendListValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(FriendList);

        HomePanel.add(jScrollPane1);
        jScrollPane1.setBounds(198, 144, 132, 238);

        jLabel8.setText("Adv");
        HomePanel.add(jLabel8);
        jLabel8.setBounds(0, 296, 166, 86);

        jLabel4.setText("Status");
        HomePanel.add(jLabel4);
        jLabel4.setBounds(200, 50, 40, 14);

        txtChatName.setEditable(false);
        txtChatName.setBackground(new java.awt.Color(204, 204, 204));
        txtChatName.setAutoscrolls(false);
        jScrollPane2.setViewportView(txtChatName);

        HomePanel.add(jScrollPane2);
        jScrollPane2.setBounds(640, 112, 100, 30);

        jLabel2.setFont(new java.awt.Font("Tekton Pro", 1, 14)); // NOI18N
        jLabel2.setText("Currently chatting with");
        HomePanel.add(jLabel2);
        jLabel2.setBounds(500, 120, 138, 23);

        jLabel6.setFont(new java.awt.Font("Tekton Pro Cond", 1, 14)); // NOI18N
        jLabel6.setText("Friends ");
        HomePanel.add(jLabel6);
        jLabel6.setBounds(234, 122, 37, 15);

        txtName.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        txtName.setForeground(new java.awt.Color(204, 204, 204));
        HomePanel.add(txtName);
        txtName.setBounds(140, 4, 180, 22);

        BSubmit.setBackground(new java.awt.Color(153, 0, 51));
        BSubmit.setForeground(new java.awt.Color(255, 255, 0));
        BSubmit.setText("Submit");
        BSubmit.setEnabled(false);
        BSubmit.setOpaque(false);
        BSubmit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BSubmitActionPerformed(evt);
            }
        });
        HomePanel.add(BSubmit);
        BSubmit.setBounds(762, 86, 80, 20);

        jLabel9.setText("Status:");
        HomePanel.add(jLabel9);
        jLabel9.setBounds(297, 418, 40, 30);

        VProfile.setFont(new java.awt.Font("Tekton Pro Cond", 1, 14)); // NOI18N
        VProfile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/Macropax/BadguysChat/Client/Images/HomePage/ViewProfileNo.png"))); // NOI18N
        VProfile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                VProfileActionPerformed(evt);
            }
        });
        VProfile.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                VProfileMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                VProfileMouseExited(evt);
            }
        });
        HomePanel.add(VProfile);
        VProfile.setBounds(770, 113, 90, 30);

        txtStatus.setEditable(false);
        txtStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtStatusActionPerformed(evt);
            }
        });
        txtStatus.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtStatusKeyPressed(evt);
            }
        });
        txtStatus.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtStatusMouseClicked(evt);
            }
        });
        HomePanel.add(txtStatus);
        txtStatus.setBounds(240, 40, 620, 43);

        jLabel1.setFont(new java.awt.Font("Tekton Pro", 1, 14)); // NOI18N
        jLabel1.setText("Want to meet new friends?");
        HomePanel.add(jLabel1);
        jLabel1.setBounds(0, 144, 153, 15);

        StatusMsg.setForeground(new java.awt.Color(204, 0, 0));
        HomePanel.add(StatusMsg);
        StatusMsg.setBounds(355, 434, 501, 14);

        Grpchat7.setFont(new java.awt.Font("Tekton Pro Cond", 1, 14)); // NOI18N
        Grpchat7.setForeground(new java.awt.Color(255, 255, 255));
        Grpchat7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/Macropax/BadguysChat/Client/Images/HomePage/EnterChatRoomNoHighlig.png"))); // NOI18N
        Grpchat7.setOpaque(false);
        Grpchat7.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/Macropax/BadguysChat/Client/Images/HomePage/EnterChatRoomNoHighlig.png"))); // NOI18N
        Grpchat7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Grpchat7ActionPerformed(evt);
            }
        });
        Grpchat7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                Grpchat7MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                Grpchat7MouseExited(evt);
            }
        });
        HomePanel.add(Grpchat7);
        Grpchat7.setBounds(10, 160, 180, 30);

        Background.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/Macropax/BadguysChat/Client/Images/HomePage/THEME_3_BEACH_GROUND.png"))); // NOI18N
        Background.setText("jLabel10");
        HomePanel.add(Background);
        Background.setBounds(0, 40, 200, 410);

        txtMsg.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtMsgKeyPressed(evt);
            }
        });
        HomePanel.add(txtMsg);
        txtMsg.setBounds(340, 383, 400, 40);

        Send.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/Macropax/BadguysChat/Client/Images/HomePage/SendNo.png"))); // NOI18N
        Send.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SendActionPerformed(evt);
            }
        });
        Send.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                SendMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                SendMouseExited(evt);
            }
        });
        HomePanel.add(Send);
        Send.setBounds(757, 390, 90, 20);

        jLabel7.setText("Adv");
        HomePanel.add(jLabel7);
        jLabel7.setBounds(10, 190, 166, 86);

        jScrollPane3.setHorizontalScrollBar(null);

        txtDisplay.setEditable(false);
        txtDisplay.setColumns(20);
        txtDisplay.setRows(5);
        jScrollPane3.setViewportView(txtDisplay);

        HomePanel.add(jScrollPane3);
        jScrollPane3.setBounds(336, 144, 520, 238);

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(204, 204, 204));
        jLabel3.setText("Welcome:");
        HomePanel.add(jLabel3);
        jLabel3.setBounds(19, 4, 91, 22);

        HeaderPix.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/Macropax/BadguysChat/Client/Images/HomePage/header_back_inner_rtl.png"))); // NOI18N
        HomePanel.add(HeaderPix);
        HeaderPix.setBounds(0, 0, 860, 40);

        BG.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/Macropax/BadguysChat/Client/Images/HomePage/Background.png"))); // NOI18N
        HomePanel.add(BG);
        BG.setBounds(200, 40, 660, 410);

        jMenuBar1.setForeground(new java.awt.Color(0, 51, 153));

        jMenu1.setText("File");

        Msettings.setText("Settings");
        Msettings.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MsettingsActionPerformed(evt);
            }
        });
        jMenu1.add(Msettings);

        MLogout.setText("Log Out");
        MLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MLogoutActionPerformed(evt);
            }
        });
        jMenu1.add(MLogout);

        MExit.setText("Exit");
        jMenu1.add(MExit);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");

        MProfile.setText("Profile");
        jMenu2.add(MProfile);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Profile, javax.swing.GroupLayout.PREFERRED_SIZE, 860, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(layout.createSequentialGroup()
                .addGap(190, 190, 190)
                .addComponent(loadingPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 470, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(HomePanel, javax.swing.GroupLayout.PREFERRED_SIZE, 860, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(SearchPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 860, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(Footer, javax.swing.GroupLayout.PREFERRED_SIZE, 860, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Profile, javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(90, 90, 90)
                        .addComponent(loadingPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(HomePanel, javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(SearchPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addComponent(Footer, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtStatusActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtStatusActionPerformed

    private void Grpchat7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Grpchat7ActionPerformed
        // TODO add your handling code here:
        
        new ClientWindow(UserName,client);
    }//GEN-LAST:event_Grpchat7ActionPerformed

    private void txtStatusMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtStatusMouseClicked
        // TODO add your handling code here:
        txtStatus.setEditable(true);
        BSubmit.setEnabled(true);
    }//GEN-LAST:event_txtStatusMouseClicked
    private void ProcessFL (List <Users> Fl)
    {
         FriendList.removeAll();
      
//          for(int i=0;i <Fl.size();i++)
//          {
//             System.out.println(Fl.get(i).Id+"   "+Fl.get(i).Display());
//          }
         if (SS == ScreenState.HomePage)
         {
        FLM = new FriendListModel( false) ;
         FLM.AddValues(Fl);
         FriendsId=FLM.ID();
     FriendList.setModel(FLM.ReturnModel());
         } else if  (SS == ScreenState.Searching)
         {
            
             FLM = new FriendListModel(true);
             FLM.AddValues(Fl);
             SearchId = FLM.ID();
             SList.setModel(FLM.ReturnModel());
             SLoading.setVisible(false);
         }
    }
    private void txtStatusKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtStatusKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER)
        {
            UpdateStatus();
           
        }
    }//GEN-LAST:event_txtStatusKeyPressed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // TODO add your handling code here:
        client.Send(("/p/di/inf/"+UserId+"/id//e/").getBytes());
    }//GEN-LAST:event_formWindowClosed
// this manages what happpens
    // when a user clicks any friend from the list it stores the message in
    // the message handler clears the console and displays the new message
    private void FriendListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_FriendListValueChanged
        // TODO add your handling code here:
        // storing Id of who we are chating with right sfdnow
       
       
       
    }//GEN-LAST:event_FriendListValueChanged

    private void BSubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BSubmitActionPerformed
        // TODO add your handling code here:
          UpdateStatus();
    }//GEN-LAST:event_BSubmitActionPerformed

    private void FriendListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_FriendListMouseClicked
        // TODO add your handling code here:
         if (FriendList.getSelectedIndex()>=0)
        {
       CurrentChatId = FriendsId.
   get(FriendList.getSelectedIndex());
         txtDisplay.setText("");
         CurrentChatName =FriendList.getSelectedValue().toString().
                substring(2,FriendList.getSelectedValue().toString().length() );
        txtChatName.setText(CurrentChatName);
        
     List <String>MessageHistory;
        MessageHistory = MH.MessageHistory(FriendsId.
   get(FriendList.getSelectedIndex()).toString());
       for (int i =0;i < MessageHistory.size();i++)
       {
           Console(MessageHistory.get(i)); 
       }
        }
    }//GEN-LAST:event_FriendListMouseClicked

    private void txtMsgKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMsgKeyPressed
        // TODO add your handling code here:
         
        if (evt.getKeyCode()==KeyEvent.VK_ENTER)
        {
            SendPM();
            
        }
        
    }//GEN-LAST:event_txtMsgKeyPressed

    private void SendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SendActionPerformed
        // TODO add your handling code here:
        SendPM();
    }//GEN-LAST:event_SendActionPerformed

    // requesting for profile of a user
    private void VProfileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_VProfileActionPerformed
        // TODO add your handling code here:
        if (CurrentChatId!=-1)
        {
            
         
          loadingPanel.setVisible(true);
          Search.setVisible(true);
          SS = ScreenState.Loading;
         client.Send(("/pf/"+CurrentChatId+"/id//e/").getBytes());
         SS = ScreenState.Profile;
         
        }
        
    }//GEN-LAST:event_VProfileActionPerformed

    private void MsettingsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MsettingsActionPerformed
        // TODO add your handling code here:]
        new Settings(s,FL,MH,txtDisplay,FriendsId);
    }//GEN-LAST:event_MsettingsActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        SavingUp();
    }//GEN-LAST:event_formWindowClosing

    private void MLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MLogoutActionPerformed
        // TODO add your handling code here:
         SavingUp();
    }//GEN-LAST:event_MLogoutActionPerformed

    private void VProfileMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_VProfileMouseEntered
        // TODO add your handling code here:
         VProfile.setIcon(new javax.swing.ImageIcon(getClass().
                 getResource("/com/Macropax/BadguysChat/Client/Images/HomePage/ViewProfileOn.png")));
   
    }//GEN-LAST:event_VProfileMouseEntered

    private void Grpchat7MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Grpchat7MouseEntered
        // TODO add your handling code here:
        Grpchat7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/ictrec/chat/Client/Images/HomePage/EnterChatRoomHighlight.png")));
    }//GEN-LAST:event_Grpchat7MouseEntered

    private void Grpchat7MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Grpchat7MouseExited
        // TODO add your handling code here:
         Grpchat7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/ictrec/chat/Client/Images/HomePage/EnterChatRoomNoHighlig.png")));
    }//GEN-LAST:event_Grpchat7MouseExited

    private void VProfileMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_VProfileMouseExited
        // TODO add your handling code here:
         VProfile.setIcon(new javax.swing.ImageIcon(getClass().
                 getResource("/com/ictrec/chat/Client/Images/HomePage/ViewProfileNo.png")));
    }//GEN-LAST:event_VProfileMouseExited

    private void SendMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SendMouseEntered
        // TODO add your handling code here:
        Send.setIcon(new javax.swing.ImageIcon(getClass().
                 getResource("/com/ictrec/chat/Client/Images/HomePage/SendOn.png")));
    }//GEN-LAST:event_SendMouseEntered

    private void SendMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SendMouseExited
        // TODO add your handling code here:
          Send.setIcon(new javax.swing.ImageIcon(getClass().
                 getResource("/com/ictrec/chat/Client/Images/HomePage/SendNo.png")));
    }//GEN-LAST:event_SendMouseExited

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        // TODO add your handling code here:
         
        if(evt.getKeyCode()== KeyEvent.VK_ESCAPE)
        {
            if (SS == ScreenState.Loading)
            {
                
                loadingPanel.setVisible(false);
                HomePanel.setVisible(true);
                Profile.setVisible(false);
            }
            else if (SS==ScreenState.Profile)
            {
                TranstoH();
            }
        }
        
    }//GEN-LAST:event_formKeyPressed

    private void TranstoH() {
         
        LoadingThread();
        HomePanel.setVisible(true);
        Profile.setVisible(false);
        FNwMessage.setVisible(false);
        SearchPanel.setVisible(false);    
        Search.setVisible(true);
        NewMessages =0;
        
        SS = ScreenState.HomePage;
                
    }

    private void ProfileKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ProfileKeyPressed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_ProfileKeyPressed

    private void SearchMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SearchMouseEntered
        // TODO add your handling code here:
        Search.setForeground(new java.awt.Color(204,204, 204));
    }//GEN-LAST:event_SearchMouseEntered

    private void SearchMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SearchMouseExited
        // TODO add your handling code here:
        Search.setForeground(new java.awt.Color(255, 0, 0));
    }//GEN-LAST:event_SearchMouseExited

    private void PbackMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PbackMouseClicked
        // TODO add your handling code here:
        if (evt.getButton()==MouseEvent.BUTTON1){
            TranstoH();
        }
    }//GEN-LAST:event_PbackMouseClicked

    private void PbackMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PbackMouseEntered
        // TODO add your handling code here:
         Pback.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/ictrec/chat/Client/Images/Profile/BackOn1.png"))); 
    }//GEN-LAST:event_PbackMouseEntered

    private void PbackMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PbackMouseExited
        // TODO add your handling code here:
         Pback.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/ictrec/chat/Client/Images/Profile/BackNo1.png"))); 
    }//GEN-LAST:event_PbackMouseExited

    private void Search1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Search1MouseEntered
        // TODO add your handling code here:
        Search1.setForeground(new java.awt.Color(204,204, 204));
    }//GEN-LAST:event_Search1MouseEntered

    private void Search1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Search1MouseExited
        // TODO add your handling code here:
        Search1.setForeground(new java.awt.Color(0,102, 204));
    }//GEN-LAST:event_Search1MouseExited

    private void Search1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Search1MouseClicked
        // TODO add your handling code here:
        if (evt.getButton()==MouseEvent.BUTTON1){
            PeopleSearch();
        }
    }//GEN-LAST:event_Search1MouseClicked

    private void SearchMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SearchMouseClicked
        // TODO add your handling code here:
        if (evt.getButton()==MouseEvent.BUTTON1){
            LoadingThread();
            SS = ScreenState.Searching;
            HomePanel.setVisible(false);
            Profile.setVisible(false);
            SearchPanel.setVisible(true);
            Search.setVisible(false);
            SLoading.setVisible(false);
            SearchResultPanel.setVisible(false);
            
        }
    }//GEN-LAST:event_SearchMouseClicked

    private void Seaback1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Seaback1MouseClicked
        // TODO add your handling code here:
          if (evt.getButton()==MouseEvent.BUTTON1){
              TranstoH();
          }
          
    }//GEN-LAST:event_Seaback1MouseClicked

    private void Seaback1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Seaback1MouseEntered
        // TODO add your handling code here:
        Seaback1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/ictrec/chat/Client/Images/Profile/BackOn1.png")));
    }//GEN-LAST:event_Seaback1MouseEntered

    private void Seaback1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Seaback1MouseExited
        // TODO add your handling code here:
        Seaback1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/ictrec/chat/Client/Images/Profile/BackNo1.png")));
    }//GEN-LAST:event_Seaback1MouseExited

    private void AddasFriendMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_AddasFriendMouseClicked
        // TODO add your handling code here:
        if (evt.getButton()== MouseEvent.BUTTON1)
        {
            SendFriendRequest();
        }
    }//GEN-LAST:event_AddasFriendMouseClicked

    private void AddasFriendMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_AddasFriendMouseEntered
        // TODO add your handling code here:
        AddasFriend.setIcon(new javax.swing.ImageIcon(getClass().
                getResource("/com/ictrec/chat/Client/Images/Search/AddasFriendOn.png")));
    }//GEN-LAST:event_AddasFriendMouseEntered

    private void AddasFriendMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_AddasFriendMouseExited
        // TODO add your handling code here:
        AddasFriend.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/ictrec/chat/Client/Images/Search/AddasFriendNo.png")));
    }//GEN-LAST:event_AddasFriendMouseExited

    private void SListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SListMouseClicked
        // TODO add your handling code here:
        // serve the current search index
        if (evt.getButton() == MouseEvent.BUTTON1){
            CurrentSearchIndex =  SearchId.get(SList.getSelectedIndex());
            loadingPanel.setVisible(true);
            CurrentSearchInd = SList.getSelectedValue().toString();
            client.Send(("/pf/"+CurrentSearchIndex+"/id//e/").getBytes());
            SS = ScreenState.SearchingPanel;
            
        }
    }//GEN-LAST:event_SListMouseClicked

    private void Seaback2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Seaback2MouseClicked
        // TODO add your handling code here:
        if (evt.getButton() == MouseEvent.BUTTON1){
            SearchResultPanel.setVisible (false);
        }
    }//GEN-LAST:event_Seaback2MouseClicked

    private void Seaback2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Seaback2MouseEntered
        // TODO add your handling code here:
          Seaback2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/ictrec/chat/Client/Images/Profile/BackOn1.png")));
    }//GEN-LAST:event_Seaback2MouseEntered

    private void Seaback2MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Seaback2MouseExited
        // TODO add your handling code here:
         Seaback2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/ictrec/chat/Client/Images/Profile/BackOn1.png")));
    }//GEN-LAST:event_Seaback2MouseExited

    /**
     * @param args the command line arguments
     */
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel AddasFriend;
    private javax.swing.JLabel BG;
    private javax.swing.JLabel BGP;
    private javax.swing.JButton BSubmit;
    private javax.swing.JLabel Background;
    private javax.swing.JLabel FNwMessage;
    private javax.swing.JPanel Footer;
    private javax.swing.JLabel FooterBG;
    private javax.swing.JList FriendList;
    private javax.swing.JButton Grpchat7;
    private javax.swing.JLabel Header;
    private javax.swing.JLabel HeaderMessage;
    private javax.swing.JLabel HeaderMessage1;
    private javax.swing.JLabel HeaderMessage2;
    private javax.swing.JLabel HeaderPix;
    private javax.swing.JPanel HomePanel;
    private javax.swing.JLabel InfoBackGround;
    private javax.swing.JLabel LoadingBG;
    private javax.swing.JLabel LoadingGIF;
    private javax.swing.JMenuItem MExit;
    private javax.swing.JMenuItem MLogout;
    private javax.swing.JMenuItem MProfile;
    private javax.swing.JMenuItem Msettings;
    private javax.swing.JLabel PBlckd;
    private javax.swing.JLabel PPhoneNum;
    private javax.swing.JTextArea PStatus;
    private javax.swing.JLabel Pback;
    private javax.swing.JLabel Pcountry1;
    private javax.swing.JLabel Pinfo;
    private javax.swing.JLabel Pname;
    private javax.swing.JLabel Ppix;
    private javax.swing.JPanel Profile;
    private javax.swing.JLabel ProfilePix;
    private javax.swing.JList SList;
    private javax.swing.JLabel SLoading;
    private javax.swing.JLabel SPBlckd;
    private javax.swing.JLabel SPPhoneNum;
    private javax.swing.JLabel SPcountry;
    private javax.swing.JLabel SPname;
    private javax.swing.JLabel Seaback1;
    private javax.swing.JLabel Seaback2;
    private javax.swing.JLabel Search;
    private javax.swing.JLabel Search1;
    private javax.swing.JTextField SearchField;
    private javax.swing.JPanel SearchPanel;
    private javax.swing.JPanel SearchResultPanel;
    private javax.swing.JButton Send;
    private javax.swing.JLabel SideBar;
    private javax.swing.JLabel StatusMsg;
    private javax.swing.JButton VProfile;
    private javax.swing.JLabel WelcomeProfile;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel help;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JPanel loadingPanel;
    private javax.swing.JTextPane txtChatName;
    private javax.swing.JTextArea txtDisplay;
    private javax.swing.JTextField txtMsg;
    private javax.swing.JLabel txtName;
    private javax.swing.JTextField txtStatus;
    // End of variables declaration//GEN-END:variables

    private void UpdateStatus() {
        txtStatus.setEditable(false);
        BSubmit.setEnabled(false);
        client.Send(("/p/Statr/inf/"+UserId+"/id/"+txtStatus.getText()+"/e/").getBytes());
    }
    private  void Console (String Message)
    {
       
        txtDisplay.append("\n"+Message);
//        String Indent = "                                                        ";
//        if (Message.startsWith("Me:"))
//        {
//            
//           if (Message.length() >= 24)
//        {
//            int Mod = Message.length()/24;
//            
//            for (int i =0; i<Mod;i++)
//            {
//                txtDisplay.append(Message.substring((i==0?1:24*i),24)+"\n");
//            }
//            
//        }
//        else
//        {
//       txtDisplay.append(Indent+Message+"\n");
//        } 
//        } else 
//        {
//            
//        }
        
    }

    private void SendPM() {
        String Msg =txtMsg.getText();
        if (!Msg.equals(""))
        {
        
        Console("Me:"+txtMsg.getText());
        MH.PushMessages(CurrentChatId, "Me:"+Msg);
        client.Send(("/pm/"+UserId+"¬Fid¬"+CurrentChatId+"¬Tid¬"+UserName+":"+Msg+"/e/").getBytes());
        txtMsg.setText("");
        }
    }

    private void SavingUp() {
        // TODO add your handling code here:
         client.Send(("/p/di/inf/"+UserId+"/id//e/").getBytes());
        try {
            MH.CloseStream();
        } catch (IOException ex) {
            Logger.getLogger(HomePage.class.getName()).log(Level.SEVERE, null, ex);
        }
        SH.StoreSettings(s);
        friends.stop();
        client.Disconnect();
        recieve.stop();
        dispose();
        new Login();
    }
}
