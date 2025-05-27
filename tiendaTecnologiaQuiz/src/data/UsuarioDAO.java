package data;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import model.Usuario;

public class UsuarioDAO {
	private Connection connection;

	public UsuarioDAO(Connection connection) {
		this.connection = connection;
	}

	  public boolean authenticate(String nickname, String contraseña, String rol) {
	       //String sql = "SELECT * FROM ProgrammingII.Usuario WHERE nickname=? AND contraseña=? AND rol=?";

	       String sql = "{ ? = call PROGRAMMINGII.AuthenticateUsuario(?,?,?) }";
	       
	       try (CallableStatement stmt = connection.prepareCall(sql)) {
	           stmt.registerOutParameter(1, java.sql.Types.INTEGER);
	           stmt.setString(2, nickname);
	           stmt.setString(3, contraseña);
	           stmt.setString(4, rol);
	           
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
