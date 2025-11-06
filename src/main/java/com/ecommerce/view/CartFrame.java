package com.ecommerce.view;

import com.ecommerce.model.CartItem;
import com.ecommerce.service.CartService;
import com.ecommerce.service.OrderService;
import com.ecommerce.util.SessionManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class CartFrame extends JFrame {
    private CartService cartService;
    private MainFrame parentFrame;
    private OrderService orderService;
    private JList<CartItem> cartList;
    private DefaultListModel<CartItem> listModel;
    private JLabel totalLabel;

    public CartFrame(CartService cartService, MainFrame parentFrame) {
        this.cartService = cartService;
        this.parentFrame = parentFrame;
        this.orderService = new OrderService();
        initComponents();
        loadCartItems();
    }

    private void initComponents() {
        setTitle("Carrinho de Compras");
        setSize(600, 500);
        setLocationRelativeTo(parentFrame);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());

        // Title
        JLabel titleLabel = new JLabel("MEU CARRINHO", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Cart List
        listModel = new DefaultListModel<>();
        cartList = new JList<>(listModel);
        cartList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        cartList.setCellRenderer(new CartItemRenderer());

        JScrollPane scrollPane = new JScrollPane(cartList);
        scrollPane.setPreferredSize(new Dimension(550, 300));

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton updateButton = new JButton("Atualizar Quantidade");
        JButton removeButton = new JButton("Remover Item");
        JButton clearButton = new JButton("Limpar Carrinho");

        updateButton.addActionListener(this::updateQuantity);
        removeButton.addActionListener(this::removeItem);
        clearButton.addActionListener(this::clearCart);

        buttonPanel.add(updateButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(clearButton);

        // Total Panel
        JPanel totalPanel = new JPanel(new BorderLayout());
        totalLabel = new JLabel("Total: R$ 0,00", JLabel.CENTER);
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JPanel checkoutPanel = new JPanel(new FlowLayout());
        JButton checkoutButton = new JButton("Finalizar Compra");
        JButton continueButton = new JButton("Continuar Comprando");

        checkoutButton.addActionListener(this::checkout);
        continueButton.addActionListener(e -> dispose());

        checkoutPanel.add(continueButton);
        checkoutPanel.add(checkoutButton);

        totalPanel.add(totalLabel, BorderLayout.CENTER);
        totalPanel.add(checkoutPanel, BorderLayout.SOUTH);

        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.EAST);
        mainPanel.add(totalPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void loadCartItems() {
        listModel.clear();
        for (CartItem item : cartService.getItems()) {
            listModel.addElement(item);
        }
        updateTotal();
    }

    private void updateTotal() {
        totalLabel.setText("Total: R$ " + cartService.getTotal());
    }

    private void updateQuantity(ActionEvent e) {
        CartItem selected = cartList.getSelectedValue();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Selecione um item!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String input = JOptionPane.showInputDialog(this, "Nova quantidade:", selected.getQuantity());
        if (input != null) {
            try {
                int newQuantity = Integer.parseInt(input);
                if (newQuantity <= 0) {
                    cartService.removeItem(selected.getProduct().getId());
                } else if (newQuantity > selected.getProduct().getEstoque()) {
                    JOptionPane.showMessageDialog(this, "Quantidade indisponível em estoque!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                } else {
                    cartService.updateQuantity(selected.getProduct().getId(), newQuantity);
                }
                loadCartItems();
                parentFrame.updateCart();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Quantidade inválida!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void removeItem(ActionEvent e) {
        CartItem selected = cartList.getSelectedValue();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Selecione um item!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Remover item do carrinho?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            cartService.removeItem(selected.getProduct().getId());
            loadCartItems();
            parentFrame.updateCart();
        }
    }

    private void clearCart(ActionEvent e) {
        if (cartService.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Carrinho já está vazio!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Limpar todo o carrinho?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            cartService.clear();
            loadCartItems();
            parentFrame.updateCart();
        }
    }

    private void checkout(ActionEvent e) {
        if (cartService.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Carrinho está vazio!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String address = JOptionPane.showInputDialog(this, "Endereço de entrega:", 
                                                    SessionManager.getInstance().getCurrentUser().getEndereco());
        if (address != null && !address.trim().isEmpty()) {
            orderService.createOrder(SessionManager.getInstance().getCurrentUser().getId(), 
                                   cartService.getItems(), address.trim());
            
            cartService.clear();
            loadCartItems();
            parentFrame.updateCart();
            
            JOptionPane.showMessageDialog(this, "Pedido realizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        }
    }

    private class CartItemRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            
            if (value instanceof CartItem) {
                CartItem item = (CartItem) value;
                setText(String.format("<html><b>%s</b><br/>Quantidade: %d - Subtotal: R$ %s</html>",
                       item.getProduct().getNome(), item.getQuantity(), item.getSubtotal()));
            }
            
            return this;
        }
    }
}