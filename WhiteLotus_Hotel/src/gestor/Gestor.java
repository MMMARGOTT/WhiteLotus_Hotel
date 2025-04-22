/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gestor;

import java.sql.*;
import excepciones.MyException;
import interfaz.MenuPrincipal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import modelo.Cliente;
import modelo.EstadoReserva;
import modelo.Habitacion;
import modelo.Reserva;
import modelo.TipoHabitacion;

/**
 *
 * @author paolaschicote
 */
public class Gestor {

    private static final String driver = "com.mysql.cj.jdbc.Driver";

    private String user;
    private String db;
    private String conexion;
    private String password;
    private Connection conn;

    public Gestor(String user, String db, String conexion, String password) {
        this.user = user;
        this.db = db;
        this.conexion = conexion;
        this.password = password;
        this.conn = null;
    }

    public void initDataBase() throws MyException {
        try {
            Class.forName(driver);
            this.conn = DriverManager.getConnection(conexion + db, user, password);
        } catch (ClassNotFoundException ex) {
            throw new MyException("No has puesto la librería MySQL");
        } catch (SQLException ex) {
            throw new MyException(ex.getSQLState() + " Error al conectar");
        }
    }

    public void cerrarConexion() throws MyException {
        try {
            this.conn.close();
            System.out.println("La base de datos se ha cerrado con éxito");
        } catch (SQLException ex) {
            throw new MyException("Error al cerrar la conexión");
        }
    }

    // Con una expresión irregular validamos el id para poder hacer la reserva
    public boolean idValido(String idReserva) {
        String id = "^[0-9]+$";
        return idReserva.matches(id);
    }

    // Método para registrar una reserva 
    public void registrarReserva(Cliente cliente, Habitacion habitacion, Date fechaEntrada, Date fechaSalida, EstadoReserva estado) {
        try {
            String sql = "INSERT INTO Reservas (cliente_id, habitacion_id, fecha_entrada, fecha_salida, estado) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, cliente.getId());
            stmt.setInt(2, habitacion.getId());
            stmt.setDate(3, fechaEntrada);
            stmt.setDate(4, fechaSalida);
            stmt.setString(5, estado.toString());

            int reservasInsertadas = stmt.executeUpdate();
            if (reservasInsertadas > 0) {
                JOptionPane.showMessageDialog(null, "Reserva registrada con éxito.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        }
    }

    // Método para consultar clientes
    public ArrayList<Cliente> consultarClientes() {
        ArrayList<Cliente> listaClientes = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Clientes";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int idCliente = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String apellido = rs.getString("apellido");
                String telefono = rs.getString("telefono");

                Cliente c = new Cliente(idCliente, nombre, apellido, telefono);
                listaClientes.add(c);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return listaClientes;
    }

    // Método para consultar habitaciones
    public ArrayList<Habitacion> consultarHabitaciones() {
        ArrayList<Habitacion> listaHabitaciones = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Habitaciones";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int idHabitacion = rs.getInt("id");
                int numero = rs.getInt("numero");
                TipoHabitacion tipo = TipoHabitacion.valueOf(rs.getString("tipo"));
                double precioNoche = rs.getDouble("precio_noche");

                Habitacion h = new Habitacion(idHabitacion, numero, tipo, precioNoche);
                listaHabitaciones.add(h);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return listaHabitaciones;
    }

    // Método que elimina las reservas canceladas
    public void eliminarReservasCanceladas() {
        try {
            String sql = "DELETE FROM Reservas WHERE estado = 'CANCELADA'";
            PreparedStatement stmt = conn.prepareStatement(sql);
            int reservasEliminadas = stmt.executeUpdate();

            if (reservasEliminadas > 0) {
                JOptionPane.showMessageDialog(null, "Reservas canceladas eliminadas con éxito.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        }
    }

    // Centralización del método volver
    public void volverMenu(Gestor gestor) {
        MenuPrincipal mp = new MenuPrincipal(gestor);
        mp.setVisible(true);
        mp.setLocationRelativeTo(null);

    }

}
