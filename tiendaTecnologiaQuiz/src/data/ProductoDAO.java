package data;

import java.sql.CallableStatement;
import oracle.jdbc.OracleTypes;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import application.Main;
import model.Producto;


public class ProductoDAO {
    private Connection connection;

    public ProductoDAO(Connection connection) {
        this.connection = connection;
    }

	
	public void save(Producto producto) {
        //String sql = "INSERT INTO ProgrammingII.Producto (referencia, nombre, precio, cantidad) VALUES (?, ?, ?, ?)";
		String sql = "{call ProgrammingII.InsertProductos(?, ?, ?, ?)} ";
        try (CallableStatement stmt = connection.prepareCall(sql)) {
            stmt.setInt(1, producto.getReferencia());
            stmt.setString(2, producto.getNombre());
            stmt.setDouble(3, producto.getPrecio());
            stmt.setInt(4, producto.getCantidad());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
		
	}

	
	public ArrayList<Producto> fetch() {
        ArrayList<Producto> productos= new ArrayList<>();
        //String sql = "SELECT * FROM ProgrammingII.Producto";
        String sql = "{ ? = call ProgrammingII.FetchProductos()} ";
        
        try (CallableStatement stmt = connection.prepareCall(sql)) {
        	stmt.registerOutParameter(1, OracleTypes.CURSOR);
        	stmt.execute();
            
        	try (ResultSet rs = (ResultSet) stmt.getObject(1)) {
        		
            while (rs.next()) {
            	int referencia = rs.getInt("referencia");
                String nombre = rs.getString("nombre");
                double precio = rs.getDouble("precio");
                int cantidad = rs.getInt("cantidad");
          
  
                Producto producto = new Producto(referencia, nombre, precio, cantidad);
                productos.add(producto);
             }               
            
        }} catch (SQLException e) {
            e.printStackTrace();
            Main.showAlert("Error", "Error",e.getMessage(), null);
        }
        return productos;
	}

	public void delete(int referencia) {
        String sql = "DELETE FROM ProgrammingII.Producto WHERE referencia=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, referencia);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
		
	}

	
	public void update(Producto producto) {
        //String sql = "UPDATE ProgrammingII.Producto SET nombre=?, precio=?, cantidad=? WHERE referencia=?";
        String sql = "{call ProgrammingII.UpdateProduct(?, ?, ?, ?)} ";
		try (CallableStatement stmt = connection.prepareCall(sql)) {
        	stmt.setString(1, producto.getNombre());
        	stmt.setDouble(2, producto.getPrecio());
        	stmt.setInt(3, producto.getCantidad());
        	stmt.setInt(4, producto.getReferencia());
        	stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
		
	}
	
	public boolean authenticate(int referencia) {
	       //String sql = "SELECT * FROM ProgrammingII.Usuario WHERE nickname=? AND contraseña=? AND rol=?";

	       String sql = "{ ? = call PROGRAMMINGII.AuthenticateProducto(?) }";
	       
	       try (CallableStatement stmt = connection.prepareCall(sql)) {
	           stmt.registerOutParameter(1, java.sql.Types.INTEGER);
	           stmt.setInt(2, referencia);
	           
	           stmt.execute();
	           int result = stmt.getInt(1);
	           
	           return result==1;

	           //ResultSet rs = stmt.executeQuery();    

	       } catch (SQLException e) {
	           e.printStackTrace();
	       }

	       return false;
	   }


}
