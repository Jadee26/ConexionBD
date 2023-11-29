/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package conexionBD;

/**
 *
 * @author jatis
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class ConexionBD {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/jcvd";
    private static final String USER = "dam";
    private static final String PASS = "1234";

    public static boolean buscaNombre(String nombre) {
        boolean existe = false;

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String query = "SELECT COUNT(*) FROM videojuegos WHERE Nombre = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, nombre);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        existe = rs.getInt(1) > 0;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return existe;
    }

    public static void lanzaConsulta(String consulta) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            try (PreparedStatement stmt = conn.prepareStatement(consulta)) {
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        System.out.println("ID: " + rs.getInt("ID"));
                        System.out.println(", Nombre: " + rs.getString("Nombre"));
                        System.out.println(", Genero: " + rs.getString("Genero"));
                        System.out.println(", Fecha Lanzamiento: " + rs.getString("FechaLanzamiento"));
                        System.out.println(", Compañía: " + rs.getString("Compañia"));
                        System.out.println(", Precio: " + rs.getFloat("Precio"));
                        System.out.println("--------------------");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void nuevoRegistro(String nombre, String genero, String fechaLanzamiento, String compañia, float precio) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String query = "INSERT INTO videojuegos (Nombre, Género, FechaLanzamiento, Compañia, Precio) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, nombre);
                stmt.setString(2, genero);
                stmt.setString(3, fechaLanzamiento);
                stmt.setString(4, compañia);
                stmt.setFloat(5, precio);

                int rowCount = stmt.executeUpdate();
                if (rowCount > 0) {
                    System.out.println("Nuevo videojuego registrado con éxito.");
                } else {
                    System.out.println("No se pudo insertar el nuevo videojuego.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void nuevoRegistro() {
        try (Scanner scanner = new Scanner(System.in); Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {

            System.out.println("Ingrese los datos para un nuevo videojuego:");

            System.out.print("Nombre: ");
            String nuevoNombre = scanner.nextLine();

            if (buscaNombre(nuevoNombre)) {
                System.out.println("¡Error! El nombre ya existe en la tabla videojuegos.");
                return;
            }

            System.out.print("Género: ");
            String nuevoGenero = scanner.nextLine();
            System.out.print("Fecha de Lanzamiento (YYYY-MM-DD): ");
            String nuevaFechaLanzamiento = scanner.nextLine();
            System.out.print("Compañía: ");
            String nuevaCompañia = scanner.nextLine();
            System.out.print("Precio: ");
            float nuevoPrecio = scanner.nextFloat();

            scanner.nextLine();  // Limpiar el búfer después de leer el precio

            String query = "INSERT INTO videojuegos (Nombre, Género, FechaLanzamiento, Compañia, Precio) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, nuevoNombre);
                stmt.setString(2, nuevoGenero);
                stmt.setString(3, nuevaFechaLanzamiento);
                stmt.setString(4, nuevaCompañia);
                stmt.setFloat(5, nuevoPrecio);

                int rowCount = stmt.executeUpdate();
                if (rowCount > 0) {
                    System.out.println("Nuevo videojuego registrado con éxito.");
                } else {
                    System.out.println("No se pudo insertar el nuevo videojuego.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean eliminarRegistro(String nombre) {
        boolean eliminado = false;

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            if (!buscaNombre(nombre)) {
                System.out.println("¡Error! El nombre no existe en la tabla videojuegos.");
                return false;
            }

            String query = "DELETE FROM videojuegos WHERE Nombre = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, nombre);

                int rowCount = stmt.executeUpdate();
                if (rowCount > 0) {
                    System.out.println("Videojuego eliminado con éxito.");
                    eliminado = true;
                } else {
                    System.out.println("No se pudo eliminar el videojuego.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return eliminado;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Seleccione una opción:");
            System.out.println("1. Buscar nombre de videojuego");
            System.out.println("2. Mostrar todos los videojuegos");
            System.out.println("3. Agregar nuevo videojuego");
            System.out.println("4. Eliminar videojuego");
            System.out.println("5. Salir");

            int opcion = scanner.nextInt();
            scanner.nextLine(); // Limpiar el búfer después de leer la opción

            switch (opcion) {
                case 1:
                    System.out.print("Ingrese el nombre de un videojuego a buscar: ");
                    String nombreBuscado = scanner.nextLine();
                    if (buscaNombre(nombreBuscado)) {
                        System.out.println("El nombre '" + nombreBuscado + "' existe en la tabla videojuegos.");
                    } else {
                        System.out.println("El nombre '" + nombreBuscado + "' NO existe en la tabla videojuegos.");
                    }
                    break;
                case 2:
                    String consulta = "SELECT * FROM videojuegos";
                    lanzaConsulta(consulta);
                    break;
                case 3:
                    nuevoRegistro();
                    break;
                case 4:
                    System.out.print("Ingrese el nombre del videojuego a eliminar: ");
                    String nombreEliminar = scanner.nextLine();
                    if (eliminarRegistro(nombreEliminar)) {
                        System.out.println("El videojuego con nombre '" + nombreEliminar + "' ha sido eliminado.");
                    } else {
                        System.out.println("No se pudo eliminar el videojuego con nombre '" + nombreEliminar + "'.");
                    }
                    break;
                case 5:
                    System.out.println("Saliendo del programa. ¡Hasta luego!");
                    System.exit(0);
                default:
                    System.out.println("Opción no válida. Por favor, seleccione una opción del 1 al 5.");
                    break;
            }
        }
    }

}
