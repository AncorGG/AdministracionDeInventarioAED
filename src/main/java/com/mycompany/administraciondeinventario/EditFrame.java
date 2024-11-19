/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.administraciondeinventario;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ancor
 */
public class EditFrame extends javax.swing.JFrame {

    private int id;
    private String code;
    private String name;
    private float price;
    private String description;
    private String consult;
    private String localization;
    private String state;
    private String shelf;
    private boolean isEditing;
    private int stock;
    private int[] composedID;
    private int testsPass;
    private int tests;

    private Map<String, Integer> productMap;
    private List<String> items;

    private DetailsFrame det;
    private View v;
    private boolean isUpdate = false;

    private ProductDAO dao = new ProductDAO();

    //ADD ENTITY
    public EditFrame(View v, String consult, String state) {
        this.v = v;
        this.consult = consult;
        this.state = state;
        OnStart();
    }

    //ADD RELATION
    public EditFrame(DetailsFrame det, int id, String consult, String state) {
        this.det = det;
        this.id = id;
        this.consult = consult;
        this.state = state;
        this.isEditing = false;
        OnStart();
        System.out.println(state);
    }

    //EDIT RELATION STORED
    public EditFrame(DetailsFrame det, int[] composedID, String consult, String state, int stock) {
        this.det = det;
        this.composedID = composedID;
        this.consult = consult;
        this.state = state;
        this.stock = stock;
        this.isEditing = true;

        OnStart();
        getData();
    }

    //EDIT DEPOSIT
    public EditFrame(View v, int id, String description, String localization, String consult, String Deposit) {
        this.v = v;
        this.id = id;
        this.description = description;
        this.localization = localization;
        this.consult = "Entity";
        this.state = "Deposit";
        OnStart();
        getData();
    }

    //EDIT PRODUCT
    public EditFrame(View v, int id, String code, String name, float price, String description, String shelf) {
        this.v = v;
        this.id = id;
        this.code = code;
        this.name = name;
        this.price = price;
        this.description = description;
        this.consult = "Entity";
        this.state = "Products";
        this.shelf = shelf;

        OnStart();
        getData();
    }

    private void OnStart() {
        initComponents();
        AlterConsult();
        HideErrors();

        ComboBoxRead();
    }

    private void AlterConsult() {
        if (consult.equals("Entity")) {
            if (state.equals("Deposit")) {
                idField.setVisible(false);
                idLabel.setVisible(false);
                descriptionField.setVisible(false);
                descriptionLabel.setVisible(false);
                shelfField.setVisible(false);
                shelfLabel.setVisible(false);
                nameLabel.setText("Description");
                priceLabel.setText("Localization");

            }
            jComboBox1.setVisible(false);
            comboBoxLabel.setVisible(false);

        } else {
            if (state.equals("Deposit")) {
                priceLabel.setText("Stock");
                if (isEditing) {
                    comboBoxLabel.setVisible(false);
                    jComboBox1.setVisible(false);
                }
            } else if (state.equals("Products")) {
                priceLabel.setText("Stock");
                comboBoxLabel.setText("Deposit");
            } else {
                priceLabel.setVisible(false);
                priceField.setVisible(false);
            }

            shelfField.setVisible(false);
            shelfLabel.setVisible(false);
            nameField.setVisible(false);
            nameLabel.setVisible(false);
            descriptionField.setVisible(false);
            descriptionLabel.setVisible(false);
            idField.setVisible(false);
            idLabel.setVisible(false);
        }

        jPanel1.revalidate();
        jPanel1.repaint();
    }

    private void getData() {
        isUpdate = true;
        if (consult.equals("Entity")) {
            if (state.equals("Products")) {
                idField.setText("" + code);
                nameField.setText(name);
                priceField.setText("" + price);
                descriptionField.setText(description);
                shelfField.setText(shelf);
            } else {
                idField.setText("" + id);
                nameField.setText(description);
                priceField.setText(localization);
            }
        } else {
            if (state.equals("Deposit")) {
                priceField.setText("" + stock);
            } else {
                priceLabel.setText("Product");
                priceField.setText("" + price);
            }
            idField.setText("" + id);

        }
    }

    public void ComboBoxRead() {
        productMap = new HashMap<>();
        int idToExclude;
        if (state.equals("Products")) {
            items = dao.getDeposits();
            idToExclude = -1;
        } else if (state.equals("Deposit")) {
            items = dao.getProducts();
            idToExclude = -1;
        } else {
            items = dao.getProducts();
            idToExclude = id;
        }
        jComboBox1.removeAllItems();
        for (String item : items) {
            String[] parts = item.split(" - ");
            if (parts.length > 2) {
                int productId = Integer.parseInt(parts[0].trim());
                String productName;
                if (state.equals("Products")) {
                    productName = parts[1].trim();
                } else {
                    productName = parts[2].trim();
                }
                if (productId != idToExclude) {
                    productMap.put(productName, productId);
                    jComboBox1.addItem(productName);
                }
            }
        }
    }

    private void HideErrors() {
        errorId.setText("");
        errorName.setText("");
        errorPrice.setText("");
        errorDescription.setText("");
        errorShelf.setText("");
        errorComboBox.setText("");
    }

    public boolean ErrorHandler() {
        tests = 0;
        testsPass = 0;

        if (nameField.isVisible()) {
            tests += 1;
            if (state.equals("Deposit")) {
                //Name as Description
                if (nameField.getText().length() == 0) {
                    errorName.setText("The description cannot be blank");
                } else if (nameField.getText().length() > 50) {
                    errorName.setText("The description is too long (50+)");
                } else {
                    testsPass += 1;
                    errorName.setText("");
                }
            } else {
                //Name as Name
                if (nameField.getText().length() == 0) {
                    errorName.setText("The name cannot be blank");
                } else if (nameField.getText().length() > 25) {
                    errorName.setText("The name is too long (25+)");
                } else {
                    testsPass += 1;
                    errorName.setText("");
                }
            }
        }

        if (priceField.isVisible()) {
            tests += 1;
            if (consult.equals("Entity") && state.equals("Products") || consult.equals("Relation") && !state.equals("Deposit")) {
                //Price as price
                if (priceField.getText().length() == 0) {
                    errorPrice.setText("The price cannot be blank");
                } else if (priceField.getText().length() > 25) {
                    errorPrice.setText("The price is too long(25+)");
                } else if (priceField.getText().matches("^\\d+(\\.\\d+)?$")) {
                    errorPrice.setText("");
                    testsPass += 1;
                } else {
                    errorPrice.setText("The price format is not adecuate (decimal)");
                }
            } else if (consult.equals("Entity") && state.equals("Deposit")) {
                //Price as Localization
                if (priceField.getText().length() == 0) {
                    errorPrice.setText("The localization cannot be blank");
                } else if (priceField.getText().length() > 25) {
                    errorPrice.setText("The localization is too long (25+)");
                } else {
                    testsPass += 1;
                    errorPrice.setText("");
                }
            } else {
                //Price as Stock
                if (priceField.getText().length() == 0) {
                    errorPrice.setText("The stock cannot be 0");
                } else if (priceField.getText().length() > 7) {
                    errorPrice.setText("The stock is too long(7)");
                } else if (priceField.getText().matches("^[0-9]+$")) {
                    errorPrice.setText("");
                    testsPass += 1;
                } else {
                    errorPrice.setText("The stock format is not adecuate (decimal)");
                }
            }
        }

        if (idField.isVisible()) {
            tests += 1;
            //Id Field
            if (idField.getText().matches("^[A-Z]{2}\\d{2}$")) {
                testsPass += 1;
                errorId.setText("");
            } else {
                errorId.setText("The ID format is not adecuate (AA00)");
            }
        }

        if (descriptionField.isVisible()) {
            tests += 1;
            //Description Field
            if (descriptionField.getText().length() == 0) {
                errorDescription.setText("The description cannot be blank");
            } else if (descriptionField.getText().length() > 50) {
                errorDescription.setText("The description is too long(50+)");
            } else {
                errorDescription.setText("");
                testsPass += 1;
            }
        }

        if (shelfField.isVisible()) {
            tests += 1;
            //Shelf Field
            if (shelfField.getText().matches("^[A-Z]{1}\\d{1}$")) {
                errorShelf.setText("");
                testsPass += 1;
            } else {
                errorShelf.setText("The shelf format is not adecuate (A0)");
            }
        }

        if (testsPass == tests) {
            return true;
        } else {
            return false;
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
        nameField = new javax.swing.JTextField();
        nameLabel = new javax.swing.JLabel();
        priceField = new javax.swing.JTextField();
        priceLabel = new javax.swing.JLabel();
        idField = new javax.swing.JTextField();
        idLabel = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        errorId = new javax.swing.JLabel();
        errorName = new javax.swing.JLabel();
        errorPrice = new javax.swing.JLabel();
        descriptionField = new javax.swing.JTextField();
        errorDescription = new javax.swing.JLabel();
        descriptionLabel = new javax.swing.JLabel();
        errorShelf = new javax.swing.JLabel();
        shelfField = new javax.swing.JTextField();
        shelfLabel = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        comboBoxLabel = new javax.swing.JLabel();
        errorComboBox = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        nameField.setPreferredSize(new java.awt.Dimension(222, 22));
        nameField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nameFieldActionPerformed(evt);
            }
        });

        nameLabel.setFont(new java.awt.Font("Microsoft YaHei Light", 0, 12)); // NOI18N
        nameLabel.setText("Name");

        priceField.setPreferredSize(new java.awt.Dimension(222, 22));

        priceLabel.setFont(new java.awt.Font("Microsoft YaHei Light", 0, 12)); // NOI18N
        priceLabel.setText("Price");

        idField.setPreferredSize(new java.awt.Dimension(222, 22));
        idField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                idFieldActionPerformed(evt);
            }
        });

        idLabel.setFont(new java.awt.Font("Microsoft YaHei Light", 0, 12)); // NOI18N
        idLabel.setText("Id");

        jButton1.setFont(new java.awt.Font("Microsoft YaHei Light", 0, 12)); // NOI18N
        jButton1.setText("Cancel");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Microsoft YaHei Light", 0, 12)); // NOI18N
        jButton2.setText("Confirm");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        errorId.setFont(new java.awt.Font("Microsoft YaHei Light", 0, 12)); // NOI18N
        errorId.setForeground(new java.awt.Color(255, 51, 51));
        errorId.setText("jLabel4");
        errorId.setMaximumSize(new java.awt.Dimension(222, 16));
        errorId.setMinimumSize(new java.awt.Dimension(222, 16));

        errorName.setFont(new java.awt.Font("Microsoft YaHei Light", 0, 12)); // NOI18N
        errorName.setForeground(new java.awt.Color(255, 51, 51));
        errorName.setText("jLabel5");
        errorName.setMaximumSize(new java.awt.Dimension(222, 16));
        errorName.setMinimumSize(new java.awt.Dimension(222, 16));

        errorPrice.setFont(new java.awt.Font("Microsoft YaHei Light", 0, 12)); // NOI18N
        errorPrice.setForeground(new java.awt.Color(255, 51, 51));
        errorPrice.setText("jLabel6");
        errorPrice.setMaximumSize(new java.awt.Dimension(222, 16));
        errorPrice.setMinimumSize(new java.awt.Dimension(222, 16));

        descriptionField.setPreferredSize(new java.awt.Dimension(222, 22));
        descriptionField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                descriptionFieldActionPerformed(evt);
            }
        });

        errorDescription.setFont(new java.awt.Font("Microsoft YaHei Light", 0, 12)); // NOI18N
        errorDescription.setForeground(new java.awt.Color(255, 51, 51));
        errorDescription.setText("jLabel7");
        errorDescription.setMaximumSize(new java.awt.Dimension(222, 16));
        errorDescription.setMinimumSize(new java.awt.Dimension(222, 16));

        descriptionLabel.setFont(new java.awt.Font("Microsoft YaHei Light", 0, 12)); // NOI18N
        descriptionLabel.setText("Description");

        errorShelf.setFont(new java.awt.Font("Microsoft YaHei Light", 0, 12)); // NOI18N
        errorShelf.setForeground(new java.awt.Color(255, 51, 51));
        errorShelf.setText("jLabel8");
        errorShelf.setMaximumSize(new java.awt.Dimension(222, 16));
        errorShelf.setMinimumSize(new java.awt.Dimension(222, 16));

        shelfField.setPreferredSize(new java.awt.Dimension(222, 22));

        shelfLabel.setFont(new java.awt.Font("Microsoft YaHei Light", 0, 12)); // NOI18N
        shelfLabel.setText("Shelf");

        jComboBox1.setFont(new java.awt.Font("Microsoft JhengHei Light", 0, 12)); // NOI18N
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox1.setPreferredSize(new java.awt.Dimension(222, 47));

        comboBoxLabel.setFont(new java.awt.Font("Microsoft YaHei Light", 0, 12)); // NOI18N
        comboBoxLabel.setText("Product");

        errorComboBox.setFont(new java.awt.Font("Microsoft YaHei Light", 0, 12)); // NOI18N
        errorComboBox.setForeground(new java.awt.Color(255, 51, 51));
        errorComboBox.setText("jLabel9");
        errorComboBox.setMaximumSize(new java.awt.Dimension(222, 16));
        errorComboBox.setMinimumSize(new java.awt.Dimension(222, 16));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(idLabel)
                                .addGap(18, 18, 18)
                                .addComponent(idField, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(nameLabel)
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(nameField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(errorName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(priceLabel)
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(errorPrice, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(priceField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(descriptionLabel)
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(errorDescription, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(descriptionField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(shelfLabel)
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(errorShelf, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(shelfField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(comboBoxLabel)
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(errorId, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jComboBox1, javax.swing.GroupLayout.Alignment.TRAILING, 0, 223, Short.MAX_VALUE)
                                    .addComponent(errorComboBox, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addGap(44, 44, 44))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton2)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(idField, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(idLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(errorId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(comboBoxLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(errorComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nameField, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nameLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                .addComponent(errorName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(priceField, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(priceLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(errorPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(descriptionField, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(descriptionLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(errorDescription, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(shelfField, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(shelfLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(errorShelf, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void idFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_idFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_idFieldActionPerformed

    private void nameFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nameFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nameFieldActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void descriptionFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_descriptionFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_descriptionFieldActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if (ErrorHandler()) {
            if (isUpdate == true) {
                if (consult.equals("Entity")) {
                    if (state.equals("Deposit")) {
                        //UPDATE DEPOSIT
                        dao.updateDeposit(id, nameField.getText(), priceField.getText());
                    } else {
                        //UPDATE PRODUCT
                        dao.updateProduct(id, idField.getText(), nameField.getText(), descriptionField.getText(), Double.parseDouble(priceField.getText()), shelfField.getText());
                    }
                    v.Reload();
                    this.dispose();
                } else {
                    if (state.equals("Deposit")) {
                        //UPDATE PRODUCT IN DEPOSIT
                        dao.updateStored(composedID[0], composedID[1], Integer.parseInt(priceField.getText()));
                    }
                    det.Reload();
                    this.dispose();
                }
            } else {
                if (consult.equals("Entity")) {
                    if (state.equals("Deposit")) {
                        //ADD DEPOSIT
                        dao.createDeposit(nameField.getText(), priceField.getText());
                    } else {
                        //ADD PRODUCT
                        dao.createProduct(idField.getText(), nameField.getText(), descriptionField.getText(), Double.parseDouble(priceField.getText()), shelfField.getText());
                    }
                    v.Reload();
                    this.dispose();
                } else {
                    if (state.equals("Deposit")) {
                        //ADD PRODUCT IN DEPOSIT
                        int productId = productMap.get(jComboBox1.getSelectedItem().toString());
                        if (!dao.isProductInDeposit(id, productId)) {
                            dao.createStored(id, productId, Integer.parseInt(priceField.getText()));
                            this.dispose();
                        } else {
                            errorComboBox.setText("El producto ya existe en el depósito.");
                        }
                    } else if (state.equals("Products")) {
                        //ADD DEPOSIT IN PRODUCT 
                        int depositId = productMap.get(jComboBox1.getSelectedItem().toString());
                        if (!dao.isDepositInProduct(id, depositId)) {
                            dao.createStored(depositId, id, Integer.parseInt(priceField.getText()));
                            this.dispose();
                        } else {
                            errorComboBox.setText("El depósito ya está asociado al producto.");
                        }

                    } else if (state.equals("Parent Products")) {
                        //ADD PARENT PRODUCT
                        int productId = productMap.get(jComboBox1.getSelectedItem().toString());
                        if (!dao.isParentComponentExists(productId, id)) {
                            dao.createComponent(productId, id);
                            this.dispose();
                        } else {
                            errorComboBox.setText("El producto padre ya está asociado a este producto hijo.");
                        }

                    } else {
                        //ADD CHILD PRODUCT
                        int productId = productMap.get(jComboBox1.getSelectedItem().toString());
                        if (!dao.isParentComponentExists(id, productId)) {
                            dao.createComponent(id, productId);
                            this.dispose();
                        } else {
                            errorComboBox.setText("El producto hijo ya está asociado al producto padre.");
                        }
                    }
                    det.Reload();
                }
            }
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel comboBoxLabel;
    private javax.swing.JTextField descriptionField;
    private javax.swing.JLabel descriptionLabel;
    private javax.swing.JLabel errorComboBox;
    private javax.swing.JLabel errorDescription;
    private javax.swing.JLabel errorId;
    private javax.swing.JLabel errorName;
    private javax.swing.JLabel errorPrice;
    private javax.swing.JLabel errorShelf;
    private javax.swing.JTextField idField;
    private javax.swing.JLabel idLabel;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField nameField;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JTextField priceField;
    private javax.swing.JLabel priceLabel;
    private javax.swing.JTextField shelfField;
    private javax.swing.JLabel shelfLabel;
    // End of variables declaration//GEN-END:variables
}
