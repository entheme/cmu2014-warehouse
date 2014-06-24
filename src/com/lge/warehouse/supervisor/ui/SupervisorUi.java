/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lge.warehouse.supervisor.ui;

import com.lge.warehouse.supervisor.WidgetInfo;
import com.lge.warehouse.util.WarehouseInventoryInfo;
import com.lge.warehouse.util.WidgetCatalog;

/**
 *
 * @author kihyung2.lee
 */
public class SupervisorUi extends javax.swing.JFrame implements SupervisorUiUpdate{

    /**
     * Creates new form SupervisorUi
     */
    public SupervisorUi() {
        initComponents();
        initUiController();
        requestCatalog();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPaneInfo = new javax.swing.JTabbedPane();
        jScrollPaneIOrder = new javax.swing.JScrollPane();
        jTextAreaOrder = new javax.swing.JTextArea();
        jScrollPaneInventory = new javax.swing.JScrollPane();
        jTextAreaInventory = new javax.swing.JTextArea();
        jScrollPaneRobot = new javax.swing.JScrollPane();
        jTextAreaRobot = new javax.swing.JTextArea();
        jComboBoxInventory = new javax.swing.JComboBox();
        jComboBoxWidget = new javax.swing.JComboBox();
        jSpinnerWidgetQuantity = new javax.swing.JSpinner();
        jButtonInventoryAdd = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTabbedPaneInfo.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPaneInfoStateChanged(evt);
            }
        });

        jTextAreaOrder.setColumns(20);
        jTextAreaOrder.setRows(5);
        jScrollPaneIOrder.setViewportView(jTextAreaOrder);

        jTabbedPaneInfo.addTab("Order", jScrollPaneIOrder);

        jTextAreaInventory.setColumns(20);
        jTextAreaInventory.setRows(5);
        jScrollPaneInventory.setViewportView(jTextAreaInventory);

        jTabbedPaneInfo.addTab("Inventory", jScrollPaneInventory);

        jTextAreaRobot.setColumns(20);
        jTextAreaRobot.setRows(5);
        jScrollPaneRobot.setViewportView(jTextAreaRobot);

        jTabbedPaneInfo.addTab("Robot", jScrollPaneRobot);

        jComboBoxInventory.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Inventory1", "Inventory2", "Inventory3", "Inventory4" }));

        jComboBoxWidget.setModel(new javax.swing.DefaultComboBoxModel(){
            public int getSize() { return getCatalogSize(); }
            public Object getElementAt(int i) { return mWidgetCatalog.getWidgetInfoAt(i);}
        });

        jButtonInventoryAdd.setText("Add");
        jButtonInventoryAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonInventoryAddActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jComboBoxInventory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jComboBoxWidget, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jSpinnerWidgetQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 142, Short.MAX_VALUE)
                        .addComponent(jButtonInventoryAdd))
                    .addComponent(jTabbedPaneInfo))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jTabbedPaneInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBoxInventory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBoxWidget, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSpinnerWidgetQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonInventoryAdd))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonInventoryAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonInventoryAddActionPerformed
        // TODO add your handling code here:
        // Add Inventory
        //jScrollPaneInventory.get
        int inventoryID = jComboBoxInventory.getSelectedIndex();
        int quantity = ((Integer)jSpinnerWidgetQuantity.getValue()).intValue();
        WidgetInfo widgetInfo = (WidgetInfo) jComboBoxWidget.getItemAt(jComboBoxWidget.getSelectedIndex());
        WarehouseInventoryInfo warehouseInventoryInfo = new WarehouseInventoryInfo(1);
        System.out.println("quantity = " + quantity);
        
        //warehouseInventoryInfo.addInventory(InventoryName.INVENTORY_1, widgetInfo, quantity);
        mSupervisorUiController.sendWarehouseInventoryInfo(warehouseInventoryInfo);
    }//GEN-LAST:event_jButtonInventoryAddActionPerformed

    private void jTabbedPaneInfoStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPaneInfoStateChanged
        //System.out.println("focused tab is " + jTabbedPaneInfo.getSelectedIndex());
        switch(jTabbedPaneInfo.getSelectedIndex()) {
            case 0: // Order
                requestOrderStatus();
                enableInvenetoryManagement(false);
                break;
            case 1: // Inventory
                requestInventoryStatus();
                enableInvenetoryManagement(true);
                break;
            case 2: // Robot
                requestRobotStatus();
                enableInvenetoryManagement(false);
                break;
            default:
                break;
        }
    }//GEN-LAST:event_jTabbedPaneInfoStateChanged

    private boolean requestOrderStatus() {
        if(mSupervisorUiController == null)
            return false;
        
        System.out.println("request order status");
        mSupervisorUiController.requestOrderStatus();
        return true;
    }
    
    private boolean requestRobotStatus() {
        // To-Do
        return false;
    }
    
    private boolean requestInventoryStatus() {
        // To-Do
        return false;
    }
    
    private void enableInvenetoryManagement(boolean bEnable) {
        jComboBoxInventory.setEnabled(bEnable);
        jComboBoxWidget.setEnabled(bEnable);
        jSpinnerWidgetQuantity.setEnabled(bEnable);
        jButtonInventoryAdd.setEnabled(bEnable);
    }
    
    private int getCatalogSize() {
        if(mWidgetCatalog == null)
            return 0;
        else
            return mWidgetCatalog.getWidgetInfoCnt();
    }
    
    private void requestCatalog() {
        if(mSupervisorUiController != null)
            mSupervisorUiController.requestWidgetCatalog();
             
    }
    
    private void initUiController() {
        mSupervisorUiController = new SupervisorUiController();
        mSupervisorUiController.setWidgetCatalogUpdateListener(this);
        new Thread(mSupervisorUiController).start();
    }
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
            java.util.logging.Logger.getLogger(SupervisorUi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SupervisorUi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SupervisorUi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SupervisorUi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SupervisorUi().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonInventoryAdd;
    private javax.swing.JComboBox jComboBoxInventory;
    private javax.swing.JComboBox jComboBoxWidget;
    private javax.swing.JScrollPane jScrollPaneIOrder;
    private javax.swing.JScrollPane jScrollPaneInventory;
    private javax.swing.JScrollPane jScrollPaneRobot;
    private javax.swing.JSpinner jSpinnerWidgetQuantity;
    private javax.swing.JTabbedPane jTabbedPaneInfo;
    private javax.swing.JTextArea jTextAreaInventory;
    private javax.swing.JTextArea jTextAreaOrder;
    private javax.swing.JTextArea jTextAreaRobot;
    // End of variables declaration//GEN-END:variables
    private SupervisorUiController mSupervisorUiController;
    private WidgetCatalog mWidgetCatalog;

    @Override
    public void updateCatalog(WidgetCatalog widgetCatalog) {
        System.out.println("catalog success");
        mWidgetCatalog = widgetCatalog;
        jComboBoxWidget.updateUI();

    }
}
