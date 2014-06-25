package com.lge.warehouse.customer.ui;

import org.apache.log4j.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author kihyung2.lee
 */
public class OrderSysUi extends javax.swing.JFrame implements OrderSysUiUpdate{
	private static Logger logger = Logger.getLogger(OrderSysUi.class);
	OrderSysUiController mOrderSysUiController ;
    /**
     * Creates new form OrderSysUi
     */
    public OrderSysUi() {
        initComponents();
        requestCatalog();
        showCartInfo();
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
        jListWidget = new javax.swing.JList();
        jButtonOrder = new javax.swing.JButton();
        jSpinnerWidgetQuantity = new javax.swing.JSpinner();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextAreaCartInfo = new javax.swing.JTextArea();
        jButtonApplyToAll = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jListWidget.setBorder(javax.swing.BorderFactory.createTitledBorder("Ordering System"));
        jListWidget.setModel(new javax.swing.AbstractListModel() {
            public int getSize() { return com.lge.warehouse.customer.ui.OrderSysWidgetCart.getWidgetCatlogSize(); }
            public Object getElementAt(int i) { return com.lge.warehouse.customer.ui.OrderSysWidgetCart.getWidgetInfoByIndex(i); }
        });
        jListWidget.setToolTipText("");
        jListWidget.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jListWidgetMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jListWidget);
        jListWidget.getAccessibleContext().setAccessibleName("");

        jButtonOrder.setText("Place Order");
        jButtonOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOrderActionPerformed(evt);
            }
        });

        jSpinnerWidgetQuantity.setModel(new javax.swing.SpinnerNumberModel(0, 0, 1000, 1));
        jSpinnerWidgetQuantity.setToolTipText("");
        jSpinnerWidgetQuantity.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinnerWidgetQuantityStateChanged(evt);
            }
        });

        jTextAreaCartInfo.setEditable(false);
        jTextAreaCartInfo.setColumns(20);
        jTextAreaCartInfo.setRows(5);
        jScrollPane2.setViewportView(jTextAreaCartInfo);

        jButtonApplyToAll.setText(" Apply to all");
        jButtonApplyToAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonApplyToAllActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jSpinnerWidgetQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonApplyToAll))
                    .addComponent(jButtonOrder)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jSpinnerWidgetQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonApplyToAll))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonOrder)
                        .addContainerGap())
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jListWidgetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jListWidgetMouseClicked
        int quantity = OrderSysWidgetCart.getWidgetQuantity(jListWidget.getSelectedIndex());

        jSpinnerWidgetQuantity.setValue(new Integer(quantity));
    }//GEN-LAST:event_jListWidgetMouseClicked

    private void jSpinnerWidgetQuantityStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinnerWidgetQuantityStateChanged
        if(jListWidget.getSelectedIndex() == -1) {
            System.out.println("no focus");
            return;
        }
            
        //Update Cart
        Integer quantity = (Integer) jSpinnerWidgetQuantity.getValue();
        OrderSysWidgetCart.setWidgetQuantity(jListWidget.getSelectedIndex(), quantity.intValue());
        
        //Update Info Text
        showCartInfo();
    }//GEN-LAST:event_jSpinnerWidgetQuantityStateChanged

    private void jButtonOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOrderActionPerformed
        //Place Order
        mOrderSysUiController.requestPlaceOrder(OrderSysWidgetCart.getOrderFromCart());
        //Init spinner
        jSpinnerWidgetQuantity.setValue(new Integer(0));
        //Init Cart
        OrderSysWidgetCart.initWidgetCart();
        //Show Cart
        showCartInfo();
    }//GEN-LAST:event_jButtonOrderActionPerformed

    private void jButtonApplyToAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonApplyToAllActionPerformed
        int widgetQuantity = ((Integer)jSpinnerWidgetQuantity.getValue()).intValue();
        for(int i=0;i<OrderSysWidgetCart.getWidgetCatlogSize();i++) {
            OrderSysWidgetCart.setWidgetQuantity(i, widgetQuantity);
        }
        showCartInfo();
    }//GEN-LAST:event_jButtonApplyToAllActionPerformed
    @Override
	public void updateUi() {
		// TODO Auto-generated method stub
    	logger.info("updateUi");
    	jListWidget.updateUI();
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
            java.util.logging.Logger.getLogger(OrderSysUi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(OrderSysUi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(OrderSysUi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(OrderSysUi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new OrderSysUi().setVisible(true);
            }
        });
    }
    
    private void requestCatalog() {
        OrderSysWidgetCart.setUiUpdateListener(this);
        mOrderSysUiController = new OrderSysUiController();
        mOrderSysUiController.requestWidgetCatalog();
    }
    
    private void showCartInfo() {
        jTextAreaCartInfo.setText(OrderSysWidgetCart.getCartInfo());
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonApplyToAll;
    private javax.swing.JButton jButtonOrder;
    private javax.swing.JList jListWidget;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSpinner jSpinnerWidgetQuantity;
    private javax.swing.JTextArea jTextAreaCartInfo;
    // End of variables declaration//GEN-END:variables
}
