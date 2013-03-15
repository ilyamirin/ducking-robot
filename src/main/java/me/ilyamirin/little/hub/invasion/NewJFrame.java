/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.ilyamirin.little.hub.invasion;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingWorker;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import lombok.extern.slf4j.Slf4j;
import me.ilyamirin.little.hub.invasion.cache.Cache;
import me.ilyamirin.little.hub.invasion.clients.CAFSClient;
import me.ilyamirin.little.hub.invasion.interaction.cafs.File;
import me.ilyamirin.little.hub.invasion.interaction.cafs.FilePartUpload;
import me.ilyamirin.little.hub.invasion.interaction.cafs.FileVersion;

/**
 *
 * @author ilamirin
 */
@Slf4j
public class NewJFrame extends javax.swing.JFrame {

    private Properties p;
    private static final int CHUNK_SIZE = 64000;
    private static final int CHUNK_BUCKET_SIZE = 5;
    private CAFSClient client;
    private SessionHolder sessionHolder;
    private Cache cache;

    @Inject
    public NewJFrame(Properties p, CAFSClient client, SessionHolder sessionHolder, Cache cache) {
        this.client = client;
        this.sessionHolder = sessionHolder;
        this.cache = cache;
        this.p = p;
    }

    private void processFile(SmbFile smbFile, String pathTo, String targetId) throws SmbException, IOException {
        log.info("File {} has been found.", smbFile.getPath());

        String smbFileCacheKey = smbFile.getPath().concat(pathTo).concat(targetId);
        if (cache.contains(smbFileCacheKey)) {
            log.trace("File {} has been already processed at {}.",
                    smbFile, cache.get(smbFileCacheKey, Date.class));
            return;
        }

        //Upload chunks

        String fileVersionKey = String.valueOf(smbFile.getPath().concat(new Date().toString()).hashCode());
        SmbFileInputStream in = (SmbFileInputStream) smbFile.getInputStream();
        Collection<FilePartUpload> filePartUploads = new HashSet<FilePartUpload>();

        byte[] chunk;
        int totalChunksCount = 0;

        for (int cursor = 0; cursor < smbFile.length(); cursor += CHUNK_SIZE) {
            if (smbFile.length() - cursor < CHUNK_SIZE) {
                chunk = new byte[(int) (smbFile.length() - cursor)];
            } else {
                chunk = new byte[CHUNK_SIZE];
            }
            log.trace("I intend to read chunk with size {}", chunk.length);
            in.read(chunk);

            FilePartUpload partUpload = new FilePartUpload();
            partUpload.setIndex(totalChunksCount);
            partUpload.setFileVersionKey(fileVersionKey);
            partUpload.setContent(chunk);
            filePartUploads.add(partUpload);

            totalChunksCount++;

            if (filePartUploads.size() >= CHUNK_BUCKET_SIZE) {
                client.uploadChunks(filePartUploads, sessionHolder.getSessionId());
                filePartUploads.clear();
            }
        }

        if (!filePartUploads.isEmpty()) {
            client.uploadChunks(filePartUploads, sessionHolder.getSessionId());
        }

        in.close();

        //Creaet file on CAFS

        File file = new File();
        file.setFolder(false);
        //TODO: media type?
        file.setTargetId(targetId);
        file.setPath(pathTo + smbFile.getName());

        FileVersion fileVersion = new FileVersion();
        fileVersion.setVersionId(System.currentTimeMillis());
        fileVersion.setFile(file);
        fileVersion.setKey(fileVersionKey);
        fileVersion.setLastModificationDate(smbFile.getLastModified());
        fileVersion.setSizeInBytes(smbFile.length());
        fileVersion.setChunksCount(totalChunksCount);
        fileVersion.setHashes(null);

        if (client.createNewFileVersion(fileVersion, sessionHolder.getSessionId())) {
            cache.put(smbFileCacheKey, new Date());
        }
    }

    public void process(SmbFile root, String pathTo, String targetId, boolean isRoot) throws SmbException, IOException {
        if (root.isDirectory()) {
            log.trace("Directory {} has been found and she wanna {} bytes of a disk space.",
                    root.getPath(), root.length());

            String pathToSmbFiles = isRoot ? pathTo : pathTo + root.getName();
            for (SmbFile smbFile : root.listFiles()) {
                try {
                    process(smbFile, pathToSmbFiles, targetId, false);
                } catch (Exception e) {
                    log.error("Oops!", e);
                }
            }
        } else {
            processFile(root, pathTo, targetId);
        }
    }//process

    /**
     * Creates new form NewJFrame
     */
    public NewJFrame() {
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
        smbShareRootTextField = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        mainTextArea = new javax.swing.JTextArea();
        runButton = new javax.swing.JButton();
        backUpRadioButton = new javax.swing.JRadioButton();
        restoreRadioButton = new javax.swing.JRadioButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        smbShareRootTextField.setText("smb://127.0.0.1/test/");

        mainTextArea.setColumns(20);
        mainTextArea.setRows(5);
        jScrollPane1.setViewportView(mainTextArea);

        runButton.setText("Run");
        runButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                runButtonMouseClicked(evt);
            }
        });
        runButton.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                runButtonKeyPressed(evt);
            }
        });

        actionGroup.add(backUpRadioButton);
        backUpRadioButton.setText("Back Up");

        actionGroup.add(restoreRadioButton);
        restoreRadioButton.setText("Restore");

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(0, 0, 0)
                        .add(jScrollPane1))
                    .add(layout.createSequentialGroup()
                        .add(smbShareRootTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 329, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(18, 18, 18)
                        .add(backUpRadioButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(restoreRadioButton)
                        .add(18, 18, 18)
                        .add(runButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(smbShareRootTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(runButton)
                    .add(backUpRadioButton)
                    .add(restoreRadioButton))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 459, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void runButtonKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_runButtonKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_runButtonKeyPressed

    private void runButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_runButtonMouseClicked
        p.setProperty("path", smbShareRootTextField.getText());

        Goal goal = null;
        if (actionGroup.getSelection().equals(backUpRadioButton.getModel())) {
            goal = Goal.BACKUP;
        } else if (actionGroup.getSelection().equals(restoreRadioButton.getModel())) {
            goal = Goal.RESTORE;
        }

        try {
            final SmbFile root = new SmbFile(p.getProperty("path"));
            new SwingWorker<String, Object>() {

                @Override
                protected String doInBackground() throws Exception {
                    mainTextArea.append("Start backup of " + root.getCanonicalPath() + "/n");
                    return null;
                }
            }.run();
            process(root, "/", p.getProperty("targetId"), true);
            mainTextArea.append("Finish backup of {}" + root);
        } catch (Exception ex) {
            log.error("Oops!", ex);
        }
    }//GEN-LAST:event_runButtonMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) throws IOException {
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
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Properties p = new Properties();
                try {
                    p.load(new FileInputStream("default.properties"));
                } catch (IOException ex) {
                    log.error("Can not find props: ", ex);
                }

                log.info("Properties has been loaded: {}", p);

                Injector injector = Guice.createInjector(new NanoPodModule(p));

                NewJFrame frame = injector.getInstance(NewJFrame.class);
                frame.initComponents();
                frame.setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup actionGroup;
    private javax.swing.JRadioButton backUpRadioButton;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea mainTextArea;
    private javax.swing.JRadioButton restoreRadioButton;
    private javax.swing.JButton runButton;
    private javax.swing.JTextField smbShareRootTextField;
    // End of variables declaration//GEN-END:variables
}
