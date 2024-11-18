/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.administraciondeinventario;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ancor
 */
public class DetailsFrame extends javax.swing.JFrame {

    private DefaultTableModel tableModel;
    private String state;
    private ProductDAO dao;
    private int row;
    private View v;
    private int[] composedID;

    public DetailsFrame(String state, int row, View v, String name, int id) {
        this.state = state;
        this.dao = new ProductDAO();
        this.row = row;
        this.v = v;
        this.composedID = new int[2];
        composedID[0] = id;

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
        v.setEnabled(false);
        panelTitle.setText(name);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                OnDispose();
            }
        });

        if (state.equals("Products")) {
            jTabbedPane1.addChangeListener(e -> {
                int selectedIndex = jTabbedPane1.getSelectedIndex();
                switch (selectedIndex) {
                    case 0:
                        this.state = "Products";
                        break;
                    case 1:
                        this.state = "Parent Products";
                        break;
                    case 2:
                        this.state = "Child Products";
                        break;
                    default:
                        this.state = "Deposit";
                }
            });
        }
        Reload();
    }

    public void Reload() {
        ClearTables();
        DisplayTables();
    }

    public void DisplayTables() {
        if (state.equals("Deposit")) {
            LoadProductsOfDeposit(row + 1);
            jTabbedPane1.remove(jPanel3);
            jTabbedPane1.remove(jPanel4);
        } else {
            LoadDepositsOfProduct(row + 1);
            LoadChildComponentsOfProduct(row + 1);
            LoadParentComponentsOfProduct(row + 1);
        }
    }

    public void ClearTables() {
        if (jTable1.getModel() instanceof DefaultTableModel) {
            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            model.setRowCount(0);
        }

        if (jTable2.getModel() instanceof DefaultTableModel) {
            DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
            model.setRowCount(0);
        }

        if (jTable3.getModel() instanceof DefaultTableModel) {
            DefaultTableModel model = (DefaultTableModel) jTable3.getModel();
            model.setRowCount(0);
        }
    }

    public void LoadDepositsOfProduct(int id_product) {

        List<String> productos = dao.getDepositsOfProduct(id_product);
        Object[][] data = convertProductListToData(productos);

        String[] columnNames = {"Id", "Description", "Localization", "Delete"};

        tableModel = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {

                return column == 3;
            }
        };

        jTable1.setModel(tableModel);

        jTable1.getColumnModel().getColumn(3).setCellRenderer(new ButtonRenderer());
        jTable1.getColumnModel().getColumn(3).setCellEditor(new ButtonEditor(jTable1, this));

        jTable1.setSelectionBackground(jTable1.getBackground());
        jTable1.setSelectionForeground(jTable1.getForeground());
    }

    private Object[][] convertProductListToData(List<String> dbList) {

        int productCount = dbList.size();
        Object[][] data = new Object[productCount][4];

        for (int i = 0; i < productCount; i++) {
            String[] productData = dbList.get(i).split(" - ");

            data[i][0] = productData.length > 0 ? productData[0] : "";
            data[i][1] = productData.length > 1 ? productData[1] : "";
            data[i][2] = productData.length > 2 ? productData[2] : "";
            data[i][3] = "Delete";
        }
        return data;
    }

    public void LoadProductsOfDeposit(int id_deposit) {

        List<String> depositos = dao.getProductsInDeposit(id_deposit);

        Object[][] data = convertDepositListToData(depositos);

        String[] columnNames = {"hidden", "Id", "Name", "Stock", "Configuration", "Delete"};

        tableModel = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4 || column == 5;
            }
        };

        jTable1.setModel(tableModel);

        jTable1.getColumnModel().removeColumn(jTable1.getColumnModel().getColumn(0));

        // Botón de configuración
        jTable1.getColumnModel().getColumn(3).setCellRenderer(new ButtonRenderer());
        jTable1.getColumnModel().getColumn(3).setCellEditor(new ButtonEditor(jTable1, this));

        // Botón de eliminar
        jTable1.getColumnModel().getColumn(4).setCellRenderer(new ButtonRenderer());
        jTable1.getColumnModel().getColumn(4).setCellEditor(new ButtonEditor(jTable1, this));

        jTable1.setSelectionBackground(jTable1.getBackground());
        jTable1.setSelectionForeground(jTable1.getForeground());

    }

    private Object[][] convertDepositListToData(List<String> depositos) {
        Object[][] data = new Object[depositos.size()][6];

        for (int i = 0; i < depositos.size(); i++) {
            String[] depositoDetails = depositos.get(i).split(" - ");
            for (int j = 0; j < depositoDetails.length; j++) {
                data[i][j] = depositoDetails[j];
            }
            data[i][4] = "Configure";
            data[i][5] = "Delete";
        }
        return data;
    }

    public void LoadChildComponentsOfProduct(int id_prod_parent) {

        List<String> childComponents = dao.getChildComponents(id_prod_parent);

        Object[][] data = convertComponentListToData(childComponents);

        String[] columnNames = {"hidden", "Id", "Name", "Delete"};

        tableModel = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3;
            }
        };

        jTable3.setModel(tableModel);

        jTable3.getColumnModel().removeColumn(jTable3.getColumnModel().getColumn(0));

        jTable3.getColumnModel().getColumn(2).setCellRenderer(new ButtonRenderer());
        jTable3.getColumnModel().getColumn(2).setCellEditor(new ButtonEditor(jTable3, this));

        jTable3.setSelectionBackground(jTable3.getBackground());
        jTable3.setSelectionForeground(jTable3.getForeground());
    }

    public void LoadParentComponentsOfProduct(int id_prod_child) {

        List<String> parentComponents = dao.getParentComponents(id_prod_child);

        Object[][] data = convertComponentListToData(parentComponents);

        String[] columnNames = {"hidden", "Id", "Name", "Delete"};

        tableModel = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3;
            }
        };

        jTable2.setModel(tableModel);

        jTable2.getColumnModel().removeColumn(jTable2.getColumnModel().getColumn(0));

        jTable2.getColumnModel().getColumn(2).setCellRenderer(new ButtonRenderer());
        jTable2.getColumnModel().getColumn(2).setCellEditor(new ButtonEditor(jTable2, this));

        jTable2.setSelectionBackground(jTable2.getBackground());
        jTable2.setSelectionForeground(jTable2.getForeground());
    }

    private Object[][] convertComponentListToData(List<String> components) {
        Object[][] data = new Object[components.size()][4];

        for (int i = 0; i < components.size(); i++) {

            String[] componentDetails = components.get(i).split(" - ");

            data[i][0] = componentDetails.length > 0 ? componentDetails[0] : "";
            data[i][1] = componentDetails.length > 0 ? componentDetails[1] : "";
            data[i][2] = componentDetails.length > 1 ? componentDetails[2] : "";
            data[i][3] = "Delete";
        }
        return data;
    }

    public void OnDispose() {
        v.setEnabled(true);
    }

    public void openEditFrame(int row) {
        //ID = [0]: Child | Deposit    -   [1]: Parent |Product

        if (state.equals("Deposit")) {
            composedID[1] = Integer.parseInt(jTable1.getModel().getValueAt(row, 0).toString());
            int stock = Integer.parseInt(jTable1.getModel().getValueAt(row, 3).toString());
            EditFrame editFrame = new EditFrame(this, composedID, "Relation", state, stock);
            editFrame.setVisible(true);
            editFrame.setLocationRelativeTo(null);
        }
    }

    public void DeleteItem(int row) {

        switch (state) {

            case "Products":
                composedID[1] = Integer.parseInt(jTable1.getModel().getValueAt(row, 0).toString());
                dao.deleteStored(composedID[1], composedID[0]);
                break;

            case "Deposit":
                composedID[1] = Integer.parseInt(jTable1.getModel().getValueAt(row, 0).toString());
                dao.deleteStored(composedID[0], composedID[1]);
                break;

            case "Parent Products":
                composedID[1] = Integer.parseInt(jTable2.getModel().getValueAt(row, 0).toString());
                dao.deleteCompone(composedID[1], composedID[0]);
                break;

            case "Child Products":
                composedID[1] = Integer.parseInt(jTable3.getModel().getValueAt(row, 0).toString());
                dao.deleteCompone(composedID[0], composedID[1]);
                break;

            default:
                throw new AssertionError();
        }

        ClearTables();
        DisplayTables();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        addBtn1 = new javax.swing.JButton();
        panelTitle = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        addBtn1.setFont(new java.awt.Font("Microsoft YaHei Light", 0, 12)); // NOI18N
        addBtn1.setText("Add");
        addBtn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addBtn1ActionPerformed(evt);
            }
        });

        panelTitle.setFont(new java.awt.Font("Microsoft JhengHei Light", 0, 24)); // NOI18N
        panelTitle.setText("- Product NAME");

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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 833, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 559, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Deposits", jPanel1);

        jTable2.setFont(new java.awt.Font("Microsoft YaHei Light", 0, 12)); // NOI18N
        jTable2.setModel(new javax.swing.table.DefaultTableModel(
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
        jTable2.setGridColor(new java.awt.Color(255, 255, 255));
        jTable2.setIntercellSpacing(new java.awt.Dimension(10, 1));
        jTable2.setMinimumSize(new java.awt.Dimension(90, 100));
        jTable2.setRowHeight(40);
        jTable2.setSelectionBackground(new java.awt.Color(0, 0, 0));
        jTable2.setShowGrid(true);
        jTable2.setShowVerticalLines(false);
        jScrollPane2.setViewportView(jTable2);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 833, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 559, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Parent Product", jPanel3);

        jTable3.setFont(new java.awt.Font("Microsoft YaHei Light", 0, 12)); // NOI18N
        jTable3.setModel(new javax.swing.table.DefaultTableModel(
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
        jTable3.setGridColor(new java.awt.Color(255, 255, 255));
        jTable3.setIntercellSpacing(new java.awt.Dimension(10, 1));
        jTable3.setMinimumSize(new java.awt.Dimension(90, 100));
        jTable3.setRowHeight(40);
        jTable3.setSelectionBackground(new java.awt.Color(0, 0, 0));
        jTable3.setShowGrid(true);
        jTable3.setShowVerticalLines(false);
        jScrollPane3.setViewportView(jTable3);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 833, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 559, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Child Product", jPanel4);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(781, 781, 781)
                .addComponent(addBtn1, javax.swing.GroupLayout.DEFAULT_SIZE, 126, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 860, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGap(24, 24, 24)
                    .addComponent(panelTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(663, Short.MAX_VALUE)))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(addBtn1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 615, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(27, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(panelTitle)
                    .addContainerGap(649, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 970, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(23, 23, 23)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGap(24, 24, 24)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 725, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void addBtn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addBtn1ActionPerformed
        EditFrame editFrame = new EditFrame(this, composedID[0], "Relation", state);
        editFrame.setVisible(true);
        editFrame.setLocationRelativeTo(null);
    }//GEN-LAST:event_addBtn1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addBtn1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JLabel panelTitle;
    // End of variables declaration//GEN-END:variables
}
