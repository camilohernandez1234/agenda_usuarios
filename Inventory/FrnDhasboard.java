package com.mycompany.Conection.views;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class FrnDhasboard extends javax.swing.JFrame {

    public JTable tablaDatos;
    public DefaultTableModel modelo;
    public JMenuBar menuBar;
    public JMenu menuFile;
    public JMenuItem menuItemUsuarios;
    public JButton btnAgregar, btnModificar, btnEliminar, btnCerrarSesion;
    private JPanel panelFormulario;

    public FrnDhasboard() {
        setTitle("Dashboard");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Tabla
        tablaDatos = new JTable();
        modelo = new DefaultTableModel();
        tablaDatos.setModel(modelo);
        tablaDatos.setRowHeight(30);
        tablaDatos.setFont(new Font("Arial", Font.PLAIN, 14));
        tablaDatos.setForeground(Color.WHITE);
        tablaDatos.setBackground(new Color(40, 40, 40));
        tablaDatos.setSelectionBackground(new Color(100, 100, 255));
        tablaDatos.setSelectionForeground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(tablaDatos);
        add(scrollPane, BorderLayout.CENTER);

        // Panel derecho (formulario)
        panelFormulario = new JPanel();
        panelFormulario.setLayout(new BoxLayout(panelFormulario, BoxLayout.Y_AXIS));
        panelFormulario.setPreferredSize(new Dimension(300, 0));
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Formulario"));
        panelFormulario.setBackground(Color.WHITE);
        add(panelFormulario, BorderLayout.EAST);

        // Barra de menú
        menuBar = new JMenuBar();
        menuBar.setBackground(new Color(50, 50, 50));
        menuFile = new JMenu("CONTACTOS");
        menuFile.setFont(new Font("Arial", Font.BOLD, 14));
        menuFile.setForeground(Color.WHITE);
        menuItemUsuarios = new JMenuItem("Usuarios");
        menuItemUsuarios.setFont(new Font("Arial", Font.PLAIN, 14));
        menuItemUsuarios.setBackground(new Color(80, 80, 80));
        menuItemUsuarios.setForeground(Color.WHITE);
        menuFile.add(menuItemUsuarios);
        menuBar.add(menuFile);
        setJMenuBar(menuBar);

        // Panel botones
        btnAgregar = new JButton("Agregar");
        btnModificar = new JButton("Modificar");
        btnEliminar = new JButton("Eliminar");
        btnCerrarSesion = new JButton("Cerrar sesión");
        btnAgregar.setBackground(new Color(76, 175, 80));
        btnAgregar.setForeground(Color.WHITE);
        btnModificar.setBackground(new Color(255, 193, 7));
        btnModificar.setForeground(Color.BLACK);
        btnEliminar.setBackground(new Color(244, 67, 54));
        btnEliminar.setForeground(Color.WHITE);
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelBotones.setBackground(new Color(240, 240, 240));
        panelBotones.add(btnAgregar);
        panelBotones.add(btnModificar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnCerrarSesion);
        add(panelBotones, BorderLayout.SOUTH);

        // Eventos
        menuItemUsuarios.addActionListener(e -> cargarUsuarios());
        btnAgregar.addActionListener(e -> agregarUsuario());
        btnModificar.addActionListener(e -> modificarUsuario());
        btnEliminar.addActionListener(e -> eliminarUsuario());
        btnCerrarSesion.addActionListener(e -> {
            dispose();
            new FrnLogin().setVisible(true);
        });

        cargarUsuarios();
    }

    private Connection obtenerConexion() {
        try {
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/inventory?serverTimezone=UTC", "root", "");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al conectar con la base de datos", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return null;
        }
    }

    private void mostrarFormulario(JPanel nuevoFormulario) {
        panelFormulario.removeAll();
        panelFormulario.add(nuevoFormulario);
        panelFormulario.revalidate();
        panelFormulario.repaint();
    }

    private void cargarUsuarios() {
        modelo.setRowCount(0);
        modelo.setColumnIdentifiers(new String[]{"ID", "Username", "Fullname"});
        try (Connection con = obtenerConexion();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id, username, fullname FROM usuario")) {
            while (rs.next()) {
                Object[] fila = {
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("fullname")
                };
                modelo.addRow(fila);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void agregarUsuario() {
        JPanel form = new JPanel(new GridLayout(4, 2, 5, 5));
        JTextField usernameField = new JTextField();
        JTextField fullnameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JButton btnGuardar = new JButton("Guardar");

        form.add(new JLabel("Username:"));
        form.add(usernameField);
        form.add(new JLabel("Fullname:"));
        form.add(fullnameField);
        form.add(new JLabel("Password:"));
        form.add(passwordField);
        form.add(new JLabel());
        form.add(btnGuardar);

        mostrarFormulario(form);

        btnGuardar.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String fullname = fullnameField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (!username.isEmpty() && !fullname.isEmpty() && !password.isEmpty()) {
                try (Connection con = obtenerConexion()) {
                    String query = "INSERT INTO usuario (username, fullname, password) VALUES (?, ?, ?)";
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, username);
                    ps.setString(2, fullname);
                    ps.setString(3, password);
                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Usuario agregado correctamente.");
                    cargarUsuarios();
                    panelFormulario.removeAll();
                    panelFormulario.revalidate();
                    panelFormulario.repaint();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error al agregar usuario.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Completa todos los campos.");
            }
        });
    }

    private void modificarUsuario() {
        int filaSeleccionada = tablaDatos.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un usuario para modificar.");
            return;
        }

        int id = (int) modelo.getValueAt(filaSeleccionada, 0);
        String usernameActual = (String) modelo.getValueAt(filaSeleccionada, 1);
        String fullnameActual = (String) modelo.getValueAt(filaSeleccionada, 2);

        JPanel form = new JPanel(new GridLayout(4, 2, 5, 5));
        JTextField usernameField = new JTextField(usernameActual);
        JTextField fullnameField = new JTextField(fullnameActual);
        JPasswordField passwordField = new JPasswordField();
        JButton btnActualizar = new JButton("Actualizar");

        form.add(new JLabel("Username:"));
        form.add(usernameField);
        form.add(new JLabel("Fullname:"));
        form.add(fullnameField);
        form.add(new JLabel("Nueva Password (opcional):"));
        form.add(passwordField);
        form.add(new JLabel());
        form.add(btnActualizar);

        mostrarFormulario(form);

        btnActualizar.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String fullname = fullnameField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (!username.isEmpty() && !fullname.isEmpty()) {
                try (Connection con = obtenerConexion()) {
                    String query;
                    PreparedStatement ps;
                    if (!password.isEmpty()) {
                        query = "UPDATE usuario SET username = ?, fullname = ?, password = ? WHERE id = ?";
                        ps = con.prepareStatement(query);
                        ps.setString(1, username);
                        ps.setString(2, fullname);
                        ps.setString(3, password);
                        ps.setInt(4, id);
                    } else {
                        query = "UPDATE usuario SET username = ?, fullname = ? WHERE id = ?";
                        ps = con.prepareStatement(query);
                        ps.setString(1, username);
                        ps.setString(2, fullname);
                        ps.setInt(3, id);
                    }
                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Usuario modificado correctamente.");
                    cargarUsuarios();
                    panelFormulario.removeAll();
                    panelFormulario.revalidate();
                    panelFormulario.repaint();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error al modificar usuario.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Completa todos los campos.");
            }
        });
    }

    private void eliminarUsuario() {
        int filaSeleccionada = tablaDatos.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un usuario para eliminar.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "¿Estás seguro de que deseas eliminar este usuario?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        int id = (int) modelo.getValueAt(filaSeleccionada, 0);

        try (Connection con = obtenerConexion()) {
            String query = "DELETE FROM usuario WHERE id = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, id);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Usuario eliminado correctamente.");
            cargarUsuarios();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al eliminar usuario.");
        }
    }




    // Método main para probar el Dashboard
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FrnDhasboard().setVisible(true));
    }
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

  


       

       
     
            
         


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
