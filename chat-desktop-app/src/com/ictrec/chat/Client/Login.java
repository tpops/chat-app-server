/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Macropax.BadguysChat.Client;
import java.util.*;
import java.net.*;
import java.lang.*;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author POPOOLA
 */
public class Login extends javax.swing.JFrame {
    Client client;
    private Thread Recieve;
    private boolean running = false;
    private String Name;
    /**
     * Creates new form Login
     */
    public Login() {
        running = true;
          setVisible(true);
          client = new Client(Name,"192.168.43.22",8192);
          client.OpenConnection();
          
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
//    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel3 = new javax.swing.JLabel();
        jDialog1 = new javax.swing.JDialog();
        jtxtUName = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        BSubmit = new javax.swing.JButton();
        txtPassWord = new javax.swing.JPasswordField();
        txtInfo = new javax.swing.JLabel();
        BtRegister = new javax.swing.JButton();

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/Macropax/BadguysChat/Client/Images/bg.png"))); // NOI18N

        javax.swing.GroupLayout jDialog1Layout = new javax.swing.GroupLayout(jDialog1.getContentPane());
        jDialog1.getContentPane().setLayout(jDialog1Layout);
        jDialog1Layout.setHorizontalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jDialog1Layout.setVerticalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Login");
        setName("Login"); // NOI18N
        setResizable(false);

        jtxtUName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtxtUNameActionPerformed(evt);
            }
        });

        jLabel1.setText("Password");

        jLabel2.setText("User Name");

        BSubmit.setText("Login");
        BSubmit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BSubmitActionPerformed(evt);
            }
        });

        txtInfo.setForeground(new java.awt.Color(255, 0, 102));

        BtRegister.setText("Register");
        BtRegister.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtRegisterActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(84, 84, 84)
                        .addComponent(txtInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(84, 84, 84)
                        .addComponent(BSubmit, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(jLabel2))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(84, 84, 84)
                        .addComponent(jtxtUName, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(227, 227, 227)
                        .addComponent(BtRegister))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(84, 84, 84)
                        .addComponent(txtPassWord, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(11, 11, 11))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(96, 96, 96)
                        .addComponent(jLabel1)
                        .addGap(21, 21, 21)
                        .addComponent(txtInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(BSubmit))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addComponent(jLabel2))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addComponent(jtxtUName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(214, 214, 214)
                        .addComponent(BtRegister))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(93, 93, 93)
                        .addComponent(txtPassWord, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(14, 14, 14))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jtxtUNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtxtUNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtUNameActionPerformed
  /**
   Login stuff her
   **/
    private void login (final String name, String Password)
  {
     
      client.Send(("/A/"+name+"/U/"+Password+"/e/").getBytes());
      txtInfo.setText("Loging in...Please Wait");
      Recieve = new Thread ("Recieve")
      {
          public void run()
          {
              int count =0;
          while (running)
          {
              String msg ="" ;
                 
                  try
                  {
             msg = new String ( client.Recieve().getData());
                  } catch (Exception e)
                  {
                      txtInfo.setText("Check Network");
                      return;
                  }
              if (msg.startsWith("/A/"))
              {
                  Integer d;
                 
                     d = new Integer (Integer.parseInt( msg.split("/A/|/e/")[1]));
                     if (d==-1)
                     {
                     txtInfo.setText("Invalid UserName/Password");
                     }
                     else
                     {
                         
                         client.SetName(name);
                     dispose ();   
                      new HomePage(client,jtxtUName.getText(),d);
                     
                       break;  
                     }
                 
              }
              count++; 
          }
          }
      }; Recieve.start();
  }
  
    
    private void BSubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BSubmitActionPerformed
        // TODO add ClientWindowandling code here:
        String name = jtxtUName.getText();
        String Password =CryptMD5.cryptWithMD5(txtPassWord.getText());
       
        if (!name.equals("")&&!Password.equals(""))
        {
            
                        name=jtxtUName.getText();
        login (name,Password);
        }
        else
        {
            txtInfo.setText("Fill in fields");
        }
    }//GEN-LAST:event_BSubmitActionPerformed

    private void BtRegisterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtRegisterActionPerformed
        // TODO add your handling code here:
        dispose();
       new  Registeration (client);
    }//GEN-LAST:event_BtRegisterActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
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
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                
                new Login().setVisible(true);
                
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BSubmit;
    private javax.swing.JButton BtRegister;
    private javax.swing.JDialog jDialog1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JTextField jtxtUName;
    private javax.swing.JLabel txtInfo;
    private javax.swing.JPasswordField txtPassWord;
    // End of variables declaration//GEN-END:variables
}
