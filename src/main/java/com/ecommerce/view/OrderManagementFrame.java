package com.ecommerce.view;

import com.ecommerce.model.Order;
import com.ecommerce.service.OrderService;
import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;

public class OrderManagementFrame extends JFrame {
    private OrderService orderService;
    private JList<Order> orderList;
    private DefaultListModel<Order> listModel;
    private JTextArea detailsArea;
    private JComboBox<Order.OrderStatus> statusCombo;

    public OrderManagementFrame() {
        this.orderService = new OrderService();
        initComponents();
        loadOrders();
    }

    private void initComponents() {
        setTitle("Gerenciamento de Pedidos");
        setSize(900, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());

        // Title
        JLabel titleLabel = new JLabel("GERENCIAR PEDIDOS", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Order List
        listModel = new DefaultListModel<>();
        orderList = new JList<>(listModel);
        orderList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        orderList.setCellRenderer(new OrderListCellRenderer());

        JScrollPane orderScrollPane = new JScrollPane(orderList);
        orderScrollPane.setPreferredSize(new Dimension(400, 500));

        // Details Panel
        JPanel detailsPanel = new JPanel(new BorderLayout());
        detailsPanel.setBorder(BorderFactory.createTitledBorder("Detalhes do Pedido"));

        detailsArea = new JTextArea();
        detailsArea.setEditable(false);
        detailsArea.setWrapStyleWord(true);
        detailsArea.setLineWrap(true);
        detailsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JScrollPane detailsScrollPane = new JScrollPane(detailsArea);

        // Status Panel
        JPanel statusPanel = new JPanel(new FlowLayout());
        statusPanel.add(new JLabel("Status:"));
        statusCombo = new JComboBox<>(Order.OrderStatus.values());
        JButton updateStatusButton = new JButton("Atualizar Status");

        updateStatusButton.addActionListener(e -> updateOrderStatus());

        statusPanel.add(statusCombo);
        statusPanel.add(updateStatusButton);

        detailsPanel.add(detailsScrollPane, BorderLayout.CENTER);
        detailsPanel.add(statusPanel, BorderLayout.SOUTH);

        // Selection listener
        orderList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Order selected = orderList.getSelectedValue();
                if (selected != null) {
                    showOrderDetails(selected);
                    statusCombo.setSelectedItem(selected.getStatus());
                }
            }
        });

        // Close button
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton refreshButton = new JButton("Atualizar");
        JButton closeButton = new JButton("Fechar");

        refreshButton.addActionListener(e -> loadOrders());
        closeButton.addActionListener(e -> dispose());

        buttonPanel.add(refreshButton);
        buttonPanel.add(closeButton);

        // Layout
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(new JLabel("Lista de Pedidos", JLabel.CENTER), BorderLayout.NORTH);
        leftPanel.add(orderScrollPane, BorderLayout.CENTER);

        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(detailsPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void loadOrders() {
        listModel.clear();
        for (Order order : orderService.getAllOrders()) {
            listModel.addElement(order);
        }

        if (orderService.getAllOrders().isEmpty()) {
            detailsArea.setText("Nenhum pedido encontrado.");
        }
    }

    private void showOrderDetails(Order order) {
        StringBuilder details = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        details.append("PEDIDO #").append(order.getId()).append("\n");
        details.append("Cliente ID: ").append(order.getUserId()).append("\n");
        details.append("Data: ").append(order.getOrderDate().format(formatter)).append("\n");
        details.append("Status: ").append(getStatusText(order.getStatus())).append("\n");
        details.append("Endereço de Entrega: ").append(order.getDeliveryAddress()).append("\n\n");

        details.append("ITENS:\n");
        details.append("----------------------------------------\n");

        order.getItems().forEach(item -> {
            details.append(String.format("%-30s Qtd: %2d  R$ %8s\n",
                    item.getProduct().getNome(),
                    item.getQuantity(),
                    item.getSubtotal()));
        });

        details.append("----------------------------------------\n");
        details.append(String.format("TOTAL: R$ %s", order.getTotal()));

        detailsArea.setText(details.toString());
    }

    private void updateOrderStatus() {
        Order selected = orderList.getSelectedValue();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Selecione um pedido!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Order.OrderStatus newStatus = (Order.OrderStatus) statusCombo.getSelectedItem();
        orderService.updateOrderStatus(selected.getId(), newStatus);

        // Refresh the list to show updated status
        loadOrders();

        JOptionPane.showMessageDialog(this, "Status atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }

    private String getStatusText(Order.OrderStatus status) {
        return switch (status) {
            case PENDING -> "Pendente";
            case CONFIRMED -> "Confirmado";
            case SHIPPED -> "Enviado";
            case DELIVERED -> "Entregue";
            case CANCELLED -> "Cancelado";
        };
    }

    private class OrderListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            if (value instanceof Order) {
                Order order = (Order) value;
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                setText(String.format("<html><b>Pedido #%d</b><br/>Cliente: %d - %s<br/>R$ %s - %s</html>",
                        order.getId(),
                        order.getUserId(),
                        order.getOrderDate().format(formatter),
                        order.getTotal(),
                        getStatusText(order.getStatus())));
            }

            return this;
        }
    }
}