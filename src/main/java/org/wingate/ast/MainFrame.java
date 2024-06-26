/*
 * Copyright (C) 2024 util2
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.wingate.ast;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Locale;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.TableColumn;
import net.suuft.libretranslate.Translator;
import org.wingate.ast.sub.ASS;
import org.wingate.ast.sub.SRT;
import org.wingate.ast.sub.Sentence;
import org.wingate.ast.util.AssTableModel;
import org.wingate.ast.util.AssTableRenderer;
import org.wingate.ast.util.GenericFileFilter;
import org.wingate.ast.util.Language;

/**
 *
 * @author util2
 */
public class MainFrame extends javax.swing.JFrame implements Runnable {
    
    private volatile boolean active = false;
    private Thread threadTranslate = null;
    
    private static final String URL = "http://127.0.0.1:5000/translate";
    private static final int ASS_MAX_TABLE_SIZE = 1000;
    private File selectedFile = null;

    private final DefaultComboBoxModel modelFrom;
    private final DefaultComboBoxModel modelTo;
    private final AssTableModel assModel;
    private final AssTableRenderer assRenderer;
    
    /**
     * Creates new form MainFrame
     */
    public MainFrame() {
        initComponents();
        
        modelFrom = new DefaultComboBoxModel();
        cbFrom.setModel(modelFrom);
        modelTo = new DefaultComboBoxModel();
        cbTo.setModel(modelTo);
        
        for(Language lng : Language.values()){
            modelFrom.addElement(lng);
            modelTo.addElement(lng);
        }
        
        cbFrom.setSelectedItem(Language.JAPANESE);        
        cbTo.setSelectedItem(getSystemLanguage());
        
        assModel = new AssTableModel();
        assRenderer = new AssTableRenderer();
        
        tableAss.setModel(assModel);
        tableAss.setDefaultRenderer(Integer.class, assRenderer);
        tableAss.setDefaultRenderer(String.class, assRenderer);
        tableAss.setDefaultRenderer(Sentence.class, assRenderer);     
        
        resetColumnsWidth();
        tableAss.updateUI();
        
        fcOpen.setAcceptAllFileFilterUsed(false);
        fcOpen.addChoosableFileFilter(new GenericFileFilter("ass", "Advanced SubStation files"));
        fcOpen.addChoosableFileFilter(new GenericFileFilter("srt", "SubRip Text files"));
        
        fcSave.setAcceptAllFileFilterUsed(false);
        fcSave.addChoosableFileFilter(new GenericFileFilter("ass", "Advanced SubStation files"));
        fcSave.addChoosableFileFilter(new GenericFileFilter("srt", "SubRip Text files"));
        
        addKeyListener(new KeyAdapter(){
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                if(active == true && e.getKeyCode() == KeyEvent.VK_SPACE){
                    stopTranslation();
                }
            }
        });
    }
    
    private Language getSystemLanguage(){
        Language lng = Language.ENGLISH;
        
        Locale loc = Locale.getDefault();
        for(Language language : Language.values()){
            if(language.getISO_639().equalsIgnoreCase(loc.getLanguage())){
                lng = language;
                break;
            }
        }
        
        return lng;
    }
    
    private void resetColumnsWidth(){
        int max = ASS_MAX_TABLE_SIZE, size;
        TableColumn col;
        for(int i=0; i<tableAss.getColumnCount(); i++){
            switch(i){
                case 0 -> { size = 40; }
                case 1, 2 -> { size = 80; }
                case 3 -> { size = max; }
                default -> { size = 0; }
            }
            col = tableAss.getColumnModel().getColumn(i);
            col.setPreferredWidth(size);
            max -= size;
        }
    }
    
    private String clearArtifacts(String s){
        String str = s;
        str = str.replaceAll("\\{[^\\}]+\\}", "");
        str = str.replace("\\N", "");
        str = str.replace("\\n", "");
        str = str.replace("\\t", "");
        return str;
    }
    
    private void startTranslation(){
        stopTranslation();
        active = true;
        threadTranslate = new Thread(this);
        threadTranslate.start();
    }
    
    private void stopTranslation(){
        if(threadTranslate != null && (threadTranslate.isAlive() || !threadTranslate.isInterrupted())){
            active = false;
            threadTranslate.interrupt();
            threadTranslate = null;
            pbTranslate.setValue(0);
        }
    }
    
    private void open(String path){
        switch(path.substring(path.lastIndexOf(".") + 1).toLowerCase()){
            case "ass" -> {
                ASS ass = new ASS();
                ass.read(path);
                assModel.setAss(ass);
            }
            case "srt" -> {
                SRT srt = new SRT();
                srt.read(path);
                // Styles
                assModel.getAss().getStyles().clear();
                assModel.getAss().getStyles().addAll(srt.getStyles());
                // Events
                assModel.getAss().getEvents().clear();
                assModel.getAss().getEvents().addAll(srt.getEvents());
            }
        }
    }    
    
    private void save(String path){        
        switch(path.substring(path.lastIndexOf(".") + 1).toLowerCase()){
            case "ass" -> {
                ASS ass = assModel.getAss();
                ass.write(selectedFile.getPath());
            }
            case "srt" -> {
                SRT srt = new SRT();
                // Styles
                srt.getStyles().clear();
                srt.getStyles().addAll(assModel.getAss().getStyles());
                // Events
                srt.getEvents().clear();
                srt.getEvents().addAll(assModel.getAss().getEvents());
                srt.write(path);
            }
            default -> {
                // Si on arrive ici c'est que l'on a pas trouvé d'extension
                // On va rappeler la fonction après une sélection suivant
                // le filefilter sélectionné. Et ajouter l'extension avant.
                if(fcSave.getFileFilter() instanceof GenericFileFilter filter){
                    selectedFile = new File(selectedFile.getPath() + filter.getExtension());
                    save(selectedFile.getPath());
                }
            }
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

        fcOpen = new javax.swing.JFileChooser();
        fcSave = new javax.swing.JFileChooser();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        panTranslate = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableAss = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        cbFrom = new javax.swing.JComboBox<>();
        cbTo = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tpFrom = new javax.swing.JTextPane();
        jScrollPane4 = new javax.swing.JScrollPane();
        tpTo = new javax.swing.JTextPane();
        jPanel3 = new javax.swing.JPanel();
        btnGetSt = new javax.swing.JButton();
        btnSetSt = new javax.swing.JButton();
        btnTranslate = new javax.swing.JButton();
        pbTranslate = new javax.swing.JProgressBar();
        jMenuBar1 = new javax.swing.JMenuBar();
        mnuFile = new javax.swing.JMenu();
        mnuFileOpen = new javax.swing.JMenuItem();
        mnuFileSaveAs = new javax.swing.JMenuItem();
        mnuFileSave = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        tableAss.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(tableAss);

        jPanel1.setLayout(new java.awt.GridLayout(2, 2, 2, 2));

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Translate from :");
        jPanel1.add(jLabel1);

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("To :");
        jPanel1.add(jLabel2);

        cbFrom.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel1.add(cbFrom);

        cbTo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel1.add(cbTo);

        jPanel2.setLayout(new java.awt.GridLayout(1, 2, 2, 2));

        jScrollPane3.setViewportView(tpFrom);

        jPanel2.add(jScrollPane3);

        jScrollPane4.setViewportView(tpTo);

        jPanel2.add(jScrollPane4);

        jPanel3.setLayout(new java.awt.GridLayout(1, 2, 2, 2));

        btnGetSt.setText("Get sentence");
        btnGetSt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGetStActionPerformed(evt);
            }
        });
        jPanel3.add(btnGetSt);

        btnSetSt.setText("Set sentence");
        btnSetSt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSetStActionPerformed(evt);
            }
        });
        jPanel3.add(btnSetSt);

        btnTranslate.setText("Auto translate");
        btnTranslate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTranslateActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panTranslateLayout = new javax.swing.GroupLayout(panTranslate);
        panTranslate.setLayout(panTranslateLayout);
        panTranslateLayout.setHorizontalGroup(
            panTranslateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 822, Short.MAX_VALUE)
            .addComponent(btnTranslate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pbTranslate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panTranslateLayout.setVerticalGroup(
            panTranslateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panTranslateLayout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnTranslate)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pbTranslate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 344, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Translate", panTranslate);

        getContentPane().add(jTabbedPane1, java.awt.BorderLayout.CENTER);

        mnuFile.setText("File");

        mnuFileOpen.setText("Open subtitles...");
        mnuFileOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuFileOpenActionPerformed(evt);
            }
        });
        mnuFile.add(mnuFileOpen);

        mnuFileSaveAs.setText("Save subtitles as...");
        mnuFileSaveAs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuFileSaveAsActionPerformed(evt);
            }
        });
        mnuFile.add(mnuFileSaveAs);

        mnuFileSave.setText("Save subtitles");
        mnuFileSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuFileSaveActionPerformed(evt);
            }
        });
        mnuFile.add(mnuFileSave);

        jMenuBar1.add(mnuFile);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void mnuFileOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuFileOpenActionPerformed
        // Open subtitles
        int z = fcOpen.showOpenDialog(this);
        if(z == JFileChooser.APPROVE_OPTION){
            open(fcOpen.getSelectedFile().getPath());
            tableAss.updateUI();
        }
    }//GEN-LAST:event_mnuFileOpenActionPerformed

    private void mnuFileSaveAsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuFileSaveAsActionPerformed
        // Save subtitles as
        if(selectedFile == null){            
            int z = fcSave.showSaveDialog(this);
            if(z == JFileChooser.APPROVE_OPTION){
                selectedFile = fcSave.getSelectedFile();
                save(selectedFile.getPath());
            }
        }else{
            int z = JOptionPane.showConfirmDialog(this, "This filename already exists,\nwould you replace it?",
                    "Issue", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if(z == JOptionPane.YES_OPTION){
                save(selectedFile.getPath());
            }else{
                int zb = fcSave.showSaveDialog(this);
                if(zb == JFileChooser.APPROVE_OPTION){
                    selectedFile = fcSave.getSelectedFile();
                    save(selectedFile.getPath());
                }
            }            
        }
    }//GEN-LAST:event_mnuFileSaveAsActionPerformed

    private void mnuFileSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuFileSaveActionPerformed
        // Save subtitles with known file
        if(selectedFile == null){
            JOptionPane.showMessageDialog(this, "Save cannot use unknown path,\nplease use 'Save as...' menu item !",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        save(selectedFile.getPath());
    }//GEN-LAST:event_mnuFileSaveActionPerformed

    private void btnGetStActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGetStActionPerformed
        // Get text from script
        if(tableAss.getSelectedRow() != -1){
            Object o = assModel.getValueAt(tableAss.getSelectedRow(), 3);
            if(o instanceof Sentence s){
                tpFrom.setText(s.getText());
            }
        }
    }//GEN-LAST:event_btnGetStActionPerformed

    private void btnSetStActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSetStActionPerformed
        // Replace text to script
        if(tableAss.getSelectedRow() != -1){
            Sentence s = new Sentence(tpTo.getText(), tpFrom.getText());
            assModel.setValueAt(s, tableAss.getSelectedRow(), 3);
            tableAss.updateUI();
        }
    }//GEN-LAST:event_btnSetStActionPerformed

    private void btnTranslateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTranslateActionPerformed
        // Auto translate
        startTranslation();
    }//GEN-LAST:event_btnTranslateActionPerformed

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
    private javax.swing.JButton btnGetSt;
    private javax.swing.JButton btnSetSt;
    private javax.swing.JButton btnTranslate;
    private javax.swing.JComboBox<String> cbFrom;
    private javax.swing.JComboBox<String> cbTo;
    private javax.swing.JFileChooser fcOpen;
    private javax.swing.JFileChooser fcSave;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JMenu mnuFile;
    private javax.swing.JMenuItem mnuFileOpen;
    private javax.swing.JMenuItem mnuFileSave;
    private javax.swing.JMenuItem mnuFileSaveAs;
    private javax.swing.JPanel panTranslate;
    private javax.swing.JProgressBar pbTranslate;
    private javax.swing.JTable tableAss;
    private javax.swing.JTextPane tpFrom;
    private javax.swing.JTextPane tpTo;
    // End of variables declaration//GEN-END:variables

    @SuppressWarnings("CallToPrintStackTrace")
    private void doInRun(){
        try{
            Translator.setUrlApi(URL);
            Language lngFrom = (Language)cbFrom.getSelectedItem();
            Language lngTo = (Language)cbTo.getSelectedItem();
            
            int eventsSize = assModel.getAss().getEvents().size();
            
            pbTranslate.setMinimum(0);
            pbTranslate.setMaximum(eventsSize);
            pbTranslate.setValue(0);
            
            for(int i=0; i<eventsSize; i++){            
                String source = clearArtifacts(((Sentence)assModel.getValueAt(i, 3)).getText());
                if(source.isEmpty()) continue;
                tpFrom.setText(source);
                
                String result = Translator.translate(lngFrom.getISO_639(), lngTo.getISO_639(), source);
                
                tpTo.setText(result);                
                Sentence s = new Sentence(result, source);
                assModel.setValueAt(s, i, 3);
                tableAss.updateUI();
                
                pbTranslate.setValue(i+1);
            }
        }catch(Exception exc){
            exc.printStackTrace();
        }finally{
            stopTranslation();
        }
    }
    
    @Override    
    public void run() {
        if(active == true){
            doInRun();
        }
    }
    
}
