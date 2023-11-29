/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package khadijalabib.com.mavenproject1;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author jatis
 */
public class JDBC2 {
    //Datos de conexión a la base de datos
    static final String DB_URL = "jdbc:mysql://localhost:3306/jcvd";
    static final String USER = "dam";
    static final   String PASS = "dam";
    static final String QUERY = " SELECT  * FROM videojuegos";
    

    public static void main(String[] args) throws SQLException {
        //abre la conexión
        try {Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(QUERY);
         // Extrae la información del set de resultados
         while (rs.next()){
             
             //Obtiene la info según el nombre de la columna
             System.out.println("ID: " + rs.getInt("ID"));
             System.out.println(", Nombre: " + rs.getNString("Nombre"));
             System.out.println(",Género:  " + rs.getNString("Género"));
             System.out.println(", Fecha Lanzamiento: " + rs.getDate("FechaLanzamiento"));
             System.out.println(", Compañía: " +rs.getNString("Compañia"));
             System.out.println(",Precio: " + rs.getFloat("Precio"));
             
         }
         stmt.close();
             
        }catch(SQLException e){
            e.printStackTrace();
            
        }
    
    }
}
