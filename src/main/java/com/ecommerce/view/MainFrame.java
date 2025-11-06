package com.ecommerce.view;

import com.ecommerce.dao.ProductDAO;
import com.ecommerce.model.Product;
import com.ecommerce.service.CartService;
import com.ecommerce.util.SessionManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class MainFrame extends JFrame {
    private ProductDAO productDAO;
    private CartService cartService;
    private JList<Product> productList;
    private DefaultListModel<Product> listModel;
    private JTextField searchField;
    private JComboBox<String> categoryCombo;
    private JLabel cartLabel;

    public MainFrame() {
        this.productDAO = new ProductDAO();
        this.cartService = new CartService();
        initComponents();
        loadProducts();
        updateCartLabel();
    }

    private void initComponents() {
        setTitle("E-Commerce - " + SessionManager.getInstance().getCurrentUser().getNomeCompleto());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        // Menu Bar
        JMenuBar menuBar = new JMenuBar();
        JMenu userMenu = new JMenu("Usuário");
        JMenuItem profileItem = new JMenuItem("Perfil");
        JMenuItem ordersItem = new JMenuItem("Meus Pedidos");
        JMenuItem logoutItem = new JMenuItem("Sair");

        profileItem.addActionListener(e -> showProfile());
        ordersItem.addActionListener(e -> showOrders());
        logoutItem.addActionListener(this::logout);

        userMenu.add(profileItem);
        userMenu.add(ordersItem);
        userMenu.addSeparator();
        userMenu.add(logoutItem);

        if (SessionManager.getInstance().isAdmin()) {
            JMenu adminMenu = new JMenu("Administração");
            JMenuItem manageProductsItem = new JMenuItem("Gerenciar Produtos");
            JMenuItem manageOrdersItem = new JMenuItem("Gerenciar Pedidos");
            
            manageProductsItem.addActionListener(e -> showProductManagement());
            manageOrdersItem.addActionListener(e -> showOrderManagement());
            
            adminMenu.add(manageProductsItem);
            adminMenu.add(manageOrdersItem);
            menuBar.add(adminMenu);
        }

        menuBar.add(userMenu);
        setJMenuBar(menuBar);

        // Main Panel
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Top Panel - Search and Cart
        JPanel topPanel = new JPanel(new BorderLayout());
        
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchField = new JTextField(20);
        JButton searchButton = new JButton("Buscar");
        categoryCombo = new JComboBox<>(new String[]{"Todas", "Eletrônicos", "Roupas", "Calçados", "Livros"});
        
        searchButton.addActionListener(this::searchProducts);
        categoryCombo.addActionListener(this::filterByCategory);
        
        searchPanel.add(new JLabel("Buscar:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(new JLabel("Categoria:"));
        searchPanel.add(categoryCombo);

        JPanel cartPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        cartLabel = new JLabel("Carrinho: 0 itens");
        JButton cartButton = new JButton("Ver Carrinho");
        cartButton.addActionListener(e -> showCart());
        
        cartPanel.add(cartLabel);
        cartPanel.add(cartButton);

        topPanel.add(searchPanel, BorderLayout.WEST);
        topPanel.add(cartPanel, BorderLayout.EAST);

        // Product List
        listModel = new DefaultListModel<>();
        productList = new JList<>(listModel);
        productList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        productList.setCellRenderer(new ProductListCellRenderer());
        
        JScrollPane scrollPane = new JScrollPane(productList);
        scrollPane.setPreferredSize(new Dimension(600, 400));

        // Product Details Panel
        JPanel detailsPanel = new JPanel(new BorderLayout());
        detailsPanel.setBorder(BorderFactory.createTitledBorder("Detalhes do Produto"));
        
        JTextArea detailsArea = new JTextArea(10, 25);
        detailsArea.setEditable(false);
        detailsArea.setWrapStyleWord(true);
        detailsArea.setLineWrap(true);
        
        JScrollPane detailsScroll = new JScrollPane(detailsArea);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JSpinner quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        JButton addToCartButton = new JButton("Adicionar ao Carrinho");
        
        addToCartButton.addActionListener(e -> addToCart(quantitySpinner));
        
        buttonPanel.add(new JLabel("Quantidade:"));
        buttonPanel.add(quantitySpinner);
        buttonPanel.add(addToCartButton);
        
        detailsPanel.add(detailsScroll, BorderLayout.CENTER);
        detailsPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Update details when selection changes
        productList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Product selected = productList.getSelectedValue();
                if (selected != null) {
                    detailsArea.setText(String.format(
                        "Nome: %s\n\nDescrição: %s\n\nPreço: R$ %s\n\nEstoque: %d unidades\n\nCategoria: %s",
                        selected.getNome(), selected.getDescricao(), selected.getPreco(),
                        selected.getEstoque(), selected.getCategoria()
                    ));
                }
            }
        });

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(detailsPanel, BorderLayout.EAST);

        add(mainPanel);
    }

    private void loadProducts() {
        listModel.clear();
        List<Product> products = productDAO.findAll();
        for (Product product : products) {
            listModel.addElement(product);
        }
    }

    private void searchProducts(ActionEvent e) {
        String searchTerm = searchField.getText().trim();
        listModel.clear();
        
        List<Product> products = searchTerm.isEmpty() ? 
            productDAO.findAll() : productDAO.searchByName(searchTerm);
            
        for (Product product : products) {
            listModel.addElement(product);
        }
    }

    private void filterByCategory(ActionEvent e) {
        String category = (String) categoryCombo.getSelectedItem();
        listModel.clear();
        
        List<Product> products = "Todas".equals(category) ? 
            productDAO.findAll() : productDAO.findByCategory(category);
            
        for (Product product : products) {
            listModel.addElement(product);
        }
    }

    private void addToCart(JSpinner quantitySpinner) {
        Product selected = productList.getSelectedValue();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Selecione um produto!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int quantity = (Integer) quantitySpinner.getValue();
        if (quantity > selected.getEstoque()) {
            JOptionPane.showMessageDialog(this, "Quantidade indisponível em estoque!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        cartService.addItem(selected, quantity);
        updateCartLabel();
        JOptionPane.showMessageDialog(this, "Produto adicionado ao carrinho!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }

    private void updateCartLabel() {
        int itemCount = cartService.getItemCount();
        cartLabel.setText("Carrinho: " + itemCount + " itens");
    }

    private void showCart() {
        new CartFrame(cartService, this).setVisible(true);
    }

    private void showProfile() {
        new ProfileFrame().setVisible(true);
    }

    private void showOrders() {
        new OrderHistoryFrame().setVisible(true);
    }

    private void showProductManagement() {
        new ProductManagementFrame().setVisible(true);
    }

    private void showOrderManagement() {
        new OrderManagementFrame().setVisible(true);
    }

    private void logout(ActionEvent e) {
        SessionManager.getInstance().logout();
        dispose();
        new LoginFrame().setVisible(true);
    }

    public void refreshProducts() {
        loadProducts();
    }

    public void updateCart() {
        updateCartLabel();
    }

    // Custom cell renderer for product list
    private class ProductListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            
            if (value instanceof Product) {
                Product product = (Product) value;
                setText(String.format("<html><b>%s</b><br/>R$ %s - Estoque: %d</html>", 
                       product.getNome(), product.getPreco(), product.getEstoque()));
            }
            
            return this;
        }
    }
}