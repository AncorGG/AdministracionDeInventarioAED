/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.administraciondeinventario;

import java.util.List;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ancor
 */
public class View extends javax.swing.JFrame {

    private DefaultTableModel tableModel;
    ProductDAO dao = new ProductDAO();
    String state = "Products";

    private java.awt.event.MouseListener tableMouseListener;

    public View() {
        //Look and Feel
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(View.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        initComponents();

        //Other configs
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        Reload();
    }

    public void Reload() {
        ClearTable();
        if (state.equals("Products")) {
            LoadProductTable();
        } else {
            LoadDepositTable();
        }
    }

    public void ClearTable() {
        if (jTable1.getModel() instanceof DefaultTableModel) {
            jTable1.removeMouseListener(tableMouseListener);
            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            model.setRowCount(0);
        }
    }

    public void LoadProductTable() {

        Object[][] data = convertProductListToData(dao.getProducts());
        String[] columnNames = {"Hidden", "Id", "Name", "Price", "Description", "Shelf", "Configuration", "Delete"};

        tableModel = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                if (column == 6 || column == 7) {
                    return true;
                }
                return false;
            }
        };

        jTable1.setModel(tableModel);

        jTable1.getColumnModel().removeColumn(jTable1.getColumnModel().getColumn(0));

        jTable1.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer());
        jTable1.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor(jTable1, this));

        jTable1.getColumnModel().getColumn(6).setCellRenderer(new ButtonRenderer());
        jTable1.getColumnModel().getColumn(6).setCellEditor(new ButtonEditor(jTable1, this));

        jTable1.setSelectionBackground(jTable1.getBackground());
        jTable1.setSelectionForeground(jTable1.getForeground());

        tableMouseListener = new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int row = jTable1.getSelectedRow();
                    if (row != -1) {
                        int id = Integer.parseInt(jTable1.getModel().getValueAt(row, 0).toString());
                        DetailsFrame details = new DetailsFrame(state, row, View.this, jTable1.getModel().getValueAt(row, 2).toString(), id);
                        details.setVisible(true);
                    }
                }
            }
        };
        jTable1.addMouseListener(tableMouseListener);
    }

    public void LoadDepositTable() {

        Object[][] data = convertDepositListToData(dao.getDeposits());
        String[] columnNames = {"Id", "Description", "Localizaton", "Configuration", "Delete"};

        tableModel = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                if (column == 3 || column == 4) {
                    return true;
                }
                return false;
            }
        };

        jTable1.setModel(tableModel);

        jTable1.getColumnModel().getColumn(3).setCellRenderer(new ButtonRenderer());
        jTable1.getColumnModel().getColumn(3).setCellEditor(new ButtonEditor(jTable1, this));

        jTable1.getColumnModel().getColumn(4).setCellRenderer(new ButtonRenderer());
        jTable1.getColumnModel().getColumn(4).setCellEditor(new ButtonEditor(jTable1, this));

        jTable1.setSelectionBackground(jTable1.getBackground());
        jTable1.setSelectionForeground(jTable1.getForeground());

        tableMouseListener = new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int row = jTable1.getSelectedRow();
                    if (row != -1) {
                        int id = Integer.parseInt(jTable1.getModel().getValueAt(row, 0).toString());
                        DetailsFrame details = new DetailsFrame(state, row, View.this, jTable1.getModel().getValueAt(row, 1).toString(), id);
                        details.setVisible(true);
                    }
                }
            }
        };
        jTable1.addMouseListener(tableMouseListener);
    }

    private Object[][] convertProductListToData(List<String> dbList) {

        int productCount = dbList.size();
        Object[][] data = new Object[productCount][8];

        for (int i = 0; i < productCount; i++) {
            String[] productData = dbList.get(i).split(" - ");

            data[i][0] = productData.length > 1 ? productData[0] : "";
            data[i][1] = productData.length > 2 ? productData[1] : "";
            data[i][2] = productData.length > 5 ? productData[2] : "";
            data[i][3] = productData.length > 4 ? productData[4] : "";
            data[i][4] = productData.length > 3 ? productData[3] : "";
            data[i][5] = productData.length > 3 ? productData[5] : "";
            data[i][6] = "Configure";
            data[i][7] = "Delete";
        }
        return data;
    }

    private Object[][] convertDepositListToData(List<String> dbList) {

        int productCount = dbList.size();
        Object[][] data = new Object[productCount][5];

        for (int i = 0; i < productCount; i++) {
            String[] productData = dbList.get(i).split(" - ");

            data[i][0] = productData.length > 0 ? productData[0] : "";
            data[i][1] = productData.length > 1 ? productData[1] : "";
            data[i][2] = productData.length > 2 ? productData[2] : "";
            data[i][3] = "Configure";
            data[i][4] = "Delete";
        }
        return data;
    }

    public void openEditFrame(int row) {
        if (state.equals("Products")) {
            String[] product = dao.getProductByTable(row);
            String id = product[0];
            String name = product[1];
            float price = Float.parseFloat(product[2]);
            String description = product[3];
            String shelf = product[4];

            EditFrame editFrame = new EditFrame(this, id, name, price, description, shelf);
            editFrame.setVisible(true);
            editFrame.setLocationRelativeTo(null);
        } else {
            String[] product = dao.getDepositByTable(row);
            int id = Integer.parseInt(product[0]);
            String description = product[1];
            String localization = product[2];

            EditFrame editFrame = new EditFrame(this, id, description, localization, "Entity", state);
            editFrame.setVisible(true);
            editFrame.setLocationRelativeTo(null);
        }
    }

    public void DeleteItem(int row) {
        int id = Integer.parseInt(jTable1.getModel().getValueAt(row, 0).toString());
        if (state.equals("Products")) {
            dao.deleteProduct(id);
            System.out.println("Producto borrado: " + id);
            Reload();
        } else if (state.equals("Deposit")) {
            dao.deleteDeposit(id);
            System.out.println("Deposito borrado: " + id);
            Reload();
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

        jPanel1 = new javax.swing.JPanel();
        storageBtn = new javax.swing.JButton();
        productBtn = new javax.swing.JButton();
        exitBtn = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        addBtn = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        panelTitle = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        storageBtn.setFont(new java.awt.Font("Microsoft JhengHei Light", 0, 12)); // NOI18N
        storageBtn.setText("Storage List");
        storageBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                storageBtnActionPerformed(evt);
            }
        });

        productBtn.setFont(new java.awt.Font("Microsoft YaHei Light", 0, 12)); // NOI18N
        productBtn.setText("Product List");
        productBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                productBtnActionPerformed(evt);
            }
        });

        exitBtn.setFont(new java.awt.Font("Microsoft YaHei Light", 0, 12)); // NOI18N
        exitBtn.setText("Exit");
        exitBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitBtnActionPerformed(evt);
            }
        });

        jLabel3.setForeground(new java.awt.Color(204, 204, 204));
        jLabel3.setText("______________________");

        jLabel4.setForeground(new java.awt.Color(204, 204, 204));
        jLabel4.setText("________________________");

        addBtn.setFont(new java.awt.Font("Microsoft YaHei Light", 0, 12)); // NOI18N
        addBtn.setText("Add");
        addBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(storageBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(productBtn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 126, Short.MAX_VALUE)
                    .addComponent(exitBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(addBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 126, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addComponent(productBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(storageBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addGap(18, 18, 18)
                .addComponent(addBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(383, 383, 383)
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addComponent(exitBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        panelTitle.setFont(new java.awt.Font("Microsoft JhengHei Light", 0, 24)); // NOI18N
        panelTitle.setText("- Product List");

        jTable1.setFont(new java.awt.Font("Microsoft YaHei Light", 0, 12)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"12CD", "Disco", "12.12", "1ad2q", "btn", "btn"},
                {"32DA", "Radio", "41.42", "d231q", "btn", "btn"},
                {"42QR", "Codigo", "31.47", "d12e12", "btn", "btn"},
                {"52WD", "Ordenador", "12.92", "rw21hb", "btn", "btn"}
            },
            new String [] {
                "Id", "Name", "Price", "Other", "Edit", "Delete"
            }
        ));
        jTable1.setGridColor(new java.awt.Color(255, 255, 255));
        jTable1.setIntercellSpacing(new java.awt.Dimension(10, 1));
        jTable1.setMinimumSize(new java.awt.Dimension(90, 100));
        jTable1.setRowHeight(40);
        jTable1.setSelectionBackground(new java.awt.Color(0, 0, 0));
        jTable1.setShowGrid(true);
        jTable1.setShowVerticalLines(false);
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(panelTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 870, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 852, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(37, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelTitle)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 604, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void storageBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_storageBtnActionPerformed
        panelTitle.setText("- Storage List");
        state = "Deposit";
        Reload();
    }//GEN-LAST:event_storageBtnActionPerformed

    private void productBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_productBtnActionPerformed
        panelTitle.setText("- Product List");
        state = "Products";
        Reload();
    }//GEN-LAST:event_productBtnActionPerformed

    private void exitBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitBtnActionPerformed
        this.dispose();
    }//GEN-LAST:event_exitBtnActionPerformed

    private void addBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addBtnActionPerformed
        EditFrame editFrame = new EditFrame(this, "Entity", state);
        editFrame.setVisible(true);
        editFrame.setLocationRelativeTo(null);
    }//GEN-LAST:event_addBtnActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addBtn;
    private javax.swing.JButton exitBtn;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel panelTitle;
    private javax.swing.JButton productBtn;
    private javax.swing.JButton storageBtn;
    // End of variables declaration//GEN-END:variables
}
