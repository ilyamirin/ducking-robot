/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.ilyamirin.little.hub.invasion;

import java.util.Properties;

/**
 *
 * @author ilamirin
 */
public class MainFrame extends javax.swing.JFrame {

    /**
     * Creates new form MainFrame
     */
    public MainFrame() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        actionGroup = new javax.swing.ButtonGroup();
        hubUUIDTextField = new javax.swing.JTextField();
        hubPasswordTextField = new javax.swing.JTextField();
        targetUUIDTextField = new javax.swing.JTextField();
        pathToSambaShareTextField = new javax.swing.JTextField();
        runButton = new javax.swing.JButton();
        backUpButton = new javax.swing.JRadioButton();
        restoreButton = new javax.swing.JRadioButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        hubUUIDTextField.setText("Hub UUID");
        hubUUIDTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hubUUIDTextFieldActionPerformed(evt);
            }
        });

        hubPasswordTextField.setText("Hub password");
        hubPasswordTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hubPasswordTextFieldActionPerformed(evt);
            }
        });

        targetUUIDTextField.setText("Traget UUID");
        targetUUIDTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                targetUUIDTextFieldActionPerformed(evt);
            }
        });

        pathToSambaShareTextField.setText("smb://127.0.0.1/test");
        pathToSambaShareTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pathToSambaShareTextFieldActionPerformed(evt);
            }
        });

        runButton.setText("Run");
        runButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                runButtonMouseClicked(evt);
            }
        });

        actionGroup.add(backUpButton);
        backUpButton.setSelected(true);
        backUpButton.setText("Back Up");

        actionGroup.add(restoreButton);
        restoreButton.setText("Restore");

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(14, 14, 14)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(backUpButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(restoreButton))
                    .add(runButton)
                    .add(pathToSambaShareTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 268, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(targetUUIDTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 268, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(hubPasswordTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 268, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(hubUUIDTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 268, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(14, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(14, 14, 14)
                .add(hubUUIDTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(14, 14, 14)
                .add(hubPasswordTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(14, 14, 14)
                .add(targetUUIDTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(14, 14, 14)
                .add(pathToSambaShareTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(14, 14, 14)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(backUpButton)
                    .add(restoreButton))
                .add(14, 14, 14)
                .add(runButton)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void hubUUIDTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hubUUIDTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_hubUUIDTextFieldActionPerformed

    private void hubPasswordTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hubPasswordTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_hubPasswordTextFieldActionPerformed

    private void targetUUIDTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_targetUUIDTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_targetUUIDTextFieldActionPerformed

    private void pathToSambaShareTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pathToSambaShareTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pathToSambaShareTextFieldActionPerformed

    private void runButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_runButtonMouseClicked
        Properties properties = new Properties();
        properties.setProperty("uuid", hubUUIDTextField.getText());
        properties.setProperty("password", hubPasswordTextField.getText());
        properties.setProperty("targetId", targetUUIDTextField.getText());
        properties.setProperty("path", pathToSambaShareTextField.getText());
    }//GEN-LAST:event_runButtonMouseClicked

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
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup actionGroup;
    private javax.swing.JRadioButton backUpButton;
    private javax.swing.JTextField hubPasswordTextField;
    private javax.swing.JTextField hubUUIDTextField;
    private javax.swing.JTextField pathToSambaShareTextField;
    private javax.swing.JRadioButton restoreButton;
    private javax.swing.JButton runButton;
    private javax.swing.JTextField targetUUIDTextField;
    // End of variables declaration//GEN-END:variables
}
