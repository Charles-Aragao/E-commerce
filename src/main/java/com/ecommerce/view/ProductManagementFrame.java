package com.ecommerce.view;

import com.ecommerce.dao.ProductDAO;
import com.ecommerce.model.Product;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;

public class ProductManagementFrame extends JFrame {
    private ProductDAO productDAO;
    private JList<Product> productList;
    private DefaultListModel<Product> listModel;
    private JTextField nameField, descriptionField, priceField, stockField, categoryField;

    public ProductManagementFrame() {
        this.productDAO = new ProductDAO();
        initComponents();
        loadProducts();
    }

    private void initComponents() {
        setTitle("Gerenciamento de Produtos");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());

        // Title
        JLabel titleLabel = new JLabel("GERENCIAR PRODUTOS", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Product List
        listModel = new DefaultListModel<>();
        productList = new JList<>(listModel);
        productList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane listScrollPane = new JScrollPane(productList);
        listScrollPane.setPreferredSize(new Dimension(400, 400));

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Dados do Produto"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Form fields
        addFormField(formPanel, gbc, "Nome:", nameField = new JTextField(20), 0);
        addFormField(formPanel, gbc, "Descrição:", descriptionField = new JTextField(20), 1);
        addFormField(formPanel, gbc, "Preço:", priceField = new JTextField(20), 2);
        addFormField(formPanel, gbc, "Estoque:", stockField = new JTextField(20), 3);
        addFormField(formPanel, gbc, "Categoria:", categoryField = new JTextField(20), 4);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton newButton = new JButton("Novo");
        JButton saveButton = new JButton("Salvar");
        JButton deleteButton = new JButton("Excluir");
        JButton clearButton = new JButton("Limpar");

        newButton.addActionListener(this::newProduct);
        saveButton.addActionListener(this::saveProduct);
        deleteButton.addActionListener(this::deleteProduct);
        clearButton.addActionListener(this::clearForm);

        buttonPanel.add(newButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);

        // Selection listener
        productList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Product selected = productList.getSelectedValue();
                if (selected != null) {
                    loadProductToForm(selected);
                }
            }
        });

        // Layout
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(new JLabel("Lista de Produtos", JLabel.CENTER), BorderLayout.NORTH);
        leftPanel.add(listScrollPane, BorderLayout.CENTER);

        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Close button
        JPanel closePanel = new JPanel(new FlowLayout());
        JButton closeButton = new JButton("Fechar");
        closeButton.addActionListener(e -> dispose());
        closePanel.add(closeButton);
        mainPanel.add(closePanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void addFormField(JPanel panel, GridBagConstraints gbc, String label, JTextField field, int row) {
        gbc.gridx = 0; gbc.gridy = row; gbc.anchor = GridBagConstraints.EAST; gbc.gridwidth = 1;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        panel.add(field, gbc);
    }

    private void loadProducts() {
        listModel.clear();
        for (Product product : productDAO.findAll()) {
            listModel.addElement(product);
        }
    }

    private void loadProductToForm(Product product) {
        nameField.setText(product.getNome());
        descriptionField.setText(product.getDescricao());
        priceField.setText(product.getPreco().toString());
        stockField.setText(String.valueOf(product.getEstoque()));
        categoryField.setText(product.getCategoria());
    }

    private void clearForm(ActionEvent e) {
        nameField.setText("");
        descriptionField.setText("");
        priceField.setText("");
        stockField.setText("");
        categoryField.setText("");
        productList.clearSelection();
    }

    private void newProduct(ActionEvent e) {
        clearForm(e);
        nameField.requestFocus();
    }

    private void saveProduct(ActionEvent e) {
        if (!validateForm()) return;

        try {
            Product product = productList.getSelectedValue();
            if (product == null) {
                product = new Product();
            }

            product.setNome(nameField.getText().trim());
            product.setDescricao(descriptionField.getText().trim());
            product.setPreco(new BigDecimal(priceField.getText().trim()));
            product.setEstoque(Integer.parseInt(stockField.getText().trim()));
            product.setCategoria(categoryField.getText().trim());

            productDAO.save(product);
            loadProducts();
            clearForm(e);

            JOptionPane.showMessageDialog(this, "Produto salvo com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Valores numéricos inválidos!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteProduct(ActionEvent e) {
        Product selected = productList.getSelectedValue();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Selecione um produto!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Excluir produto selecionado?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            productDAO.delete(selected.getId());
            loadProducts();
            clearForm(e);
            JOptionPane.showMessageDialog(this, "Produto excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private boolean validateForm() {
        if (nameField.getText().trim().isEmpty() ||
            descriptionField.getText().trim().isEmpty() ||
            priceField.getText().trim().isEmpty() ||
            stockField.getText().trim().isEmpty() ||
            categoryField.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(this, "Preencha todos os campos!", "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        try {
            new BigDecimal(priceField.getText().trim());
            Integer.parseInt(stockField.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Preço e estoque devem ser números válidos!", "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }
}