package main;

import dao.ProductoDAO;
import model.Producto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Ventana principal que integra la interfaz gráfica Swing y la interacción con la BDD.
 */
public class MainApp extends JFrame {
    private final ProductoDAO productoDAO = new ProductoDAO();
    private JTable tablaProductos;
    private DefaultTableModel modeloTabla;
    private JTextField txtNombre, txtCategoria, txtPrecio, txtStock;

    public MainApp() {
        setTitle("Gestión de Inventario - Sistema DAM");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Panel de Formulario (Norte)
        JPanel panelFormulario = new JPanel(new GridLayout(2, 5, 5, 5));
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Añadir Nuevo Producto"));

        txtNombre = new JTextField();
        txtCategoria = new JTextField();
        txtPrecio = new JTextField();
        txtStock = new JTextField();
        JButton btnGuardar = new JButton("Guardar");

        panelFormulario.add(new JLabel("Nombre:"));
        panelFormulario.add(new JLabel("Categoría:"));
        panelFormulario.add(new JLabel("Precio (€):"));
        panelFormulario.add(new JLabel("Stock:"));
        panelFormulario.add(new JLabel("")); // Espacio vacío

        panelFormulario.add(txtNombre);
        panelFormulario.add(txtCategoria);
        panelFormulario.add(txtPrecio);
        panelFormulario.add(txtStock);
        panelFormulario.add(btnGuardar);

        add(panelFormulario, BorderLayout.NORTH);

        // Tabla de Productos (Centro)
        modeloTabla = new DefaultTableModel(new String[]{"ID", "Nombre", "Categoría", "Precio", "Stock"}, 0);
        tablaProductos = new JTable(modeloTabla);
        add(new JScrollPane(tablaProductos), BorderLayout.CENTER);

        // Panel de Acciones (Sur)
        JPanel panelAcciones = new JPanel();
        JButton btnEliminar = new JButton("Eliminar Seleccionado");
        panelAcciones.add(btnEliminar);
        add(panelAcciones, BorderLayout.SOUTH);

        // Eventos
        btnGuardar.addActionListener(e -> guardarProducto());
        btnEliminar.addActionListener(e -> eliminarProducto());

        // Cargar datos al iniciar
        cargarDatosTabla();
    }

    private void cargarDatosTabla() {
        modeloTabla.setRowCount(0);
        for (Producto p : productoDAO.obtenerTodos()) {
            modeloTabla.addRow(new Object[]{p.getId(), p.getNombre(), p.getCategoria(), p.getPrecio() + " €", p.getStock()});
        }
    }

    private void guardarProducto() {
        try {
            String nombre = txtNombre.getText();
            String categoria = txtCategoria.getText();
            double precio = Double.parseDouble(txtPrecio.getText());
            int stock = Integer.parseInt(txtStock.getText());

            if (nombre.isEmpty() || categoria.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Completa todos los campos.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Producto p = new Producto(nombre, categoria, precio, stock);
            if (productoDAO.insertar(p)) {
                cargarDatosTabla();
                limpiarCampos();
                JOptionPane.showMessageDialog(this, "Producto guardado con éxito.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Precio y Stock deben ser valores numéricos válidos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarProducto() {
        int fila = tablaProductos.getSelectedRow();
        if (fila >= 0) {
            int id = (int) modeloTabla.getValueAt(fila, 0);
            if (productoDAO.eliminar(id)) {
                cargarDatosTabla();
                JOptionPane.showMessageDialog(this, "Producto eliminado.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecciona una fila primero.");
        }
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtCategoria.setText("");
        txtPrecio.setText("");
        txtStock.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainApp().setVisible(true));
    }
}