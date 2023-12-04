package fr.insa.msa.UserService;
import java.util.ArrayList;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import java.sql.*;

@RestController
@RequestMapping("/user")
public class UserServiceController {

	@Autowired
	private RestTemplate restTemplate;
	
	
	
	@GetMapping("/{id}")
	public User getUser(@PathVariable int id) {
		User user = null;
		String dbHost = restTemplate.getForObject("http://ConfigurationService/config/host", String.class);
		String dbPort = restTemplate.getForObject("http://ConfigurationService/config/port", String.class);
		String dbProject = restTemplate.getForObject("http://ConfigurationService/config/project", String.class);
		String dbUser = restTemplate.getForObject("http://ConfigurationService/config/username", String.class);
		String dbPassword = restTemplate.getForObject("http://ConfigurationService/config/password", String.class);
		try(Connection con = DriverManager.getConnection("jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbProject, dbUser, dbPassword)){
			Statement stm = con.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM user WHERE iduser=" + id + ";");
			if(rs.next()) {
				user = new User(rs.getInt("iduser"),rs.getString("name"),rs.getString("city"));
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}
	
	@GetMapping("/all")
	public ArrayList<User> getAllUsers(){
		ArrayList<User> userList = new ArrayList<User>();
		String dbHost = restTemplate.getForObject("http://ConfigurationService/config/host", String.class);
		String dbPort = restTemplate.getForObject("http://ConfigurationService/config/port", String.class);
		String dbProject = restTemplate.getForObject("http://ConfigurationService/config/project", String.class);
		String dbUser = restTemplate.getForObject("http://ConfigurationService/config/username", String.class);
		String dbPassword = restTemplate.getForObject("http://ConfigurationService/config/password", String.class);
		try(Connection con = DriverManager.getConnection("jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbProject, dbUser, dbPassword)){
			Statement stm = con.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM user;");
			while (rs.next()) {
				userList.add(new User(rs.getInt("iduser"),rs.getString("name"),rs.getString("city")));
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return userList;
	}
	
	
	@PostMapping(value="/create")
	public void createUser(String name, String city) {
		String dbHost = restTemplate.getForObject("http://ConfigurationService/config/host", String.class);
		String dbPort = restTemplate.getForObject("http://ConfigurationService/config/port", String.class);
		String dbProject = restTemplate.getForObject("http://ConfigurationService/config/project", String.class);
		String dbUser = restTemplate.getForObject("http://ConfigurationService/config/username", String.class);
		String dbPassword = restTemplate.getForObject("http://ConfigurationService/config/password", String.class);
		try(Connection con = DriverManager.getConnection("jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbProject, dbUser, dbPassword)){
			System.out.println("name : " + name + " et city : " + city);
			PreparedStatement stm = con.prepareStatement("INSERT INTO user (name,city) VALUES (?, ?)");
			stm.setString(1, name);
			stm.setString(2, city);
			
			stm.executeUpdate();
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	@DeleteMapping("/delete/{id}")
	public void deleteUser(@PathVariable int id, int idSender) {
		String dbHost = restTemplate.getForObject("http://ConfigurationService/config/host", String.class);
		String dbPort = restTemplate.getForObject("http://ConfigurationService/config/port", String.class);
		String dbProject = restTemplate.getForObject("http://ConfigurationService/config/project", String.class);
		String dbUser = restTemplate.getForObject("http://ConfigurationService/config/username", String.class);
		String dbPassword = restTemplate.getForObject("http://ConfigurationService/config/password", String.class);
		try(Connection con = DriverManager.getConnection("jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbProject, dbUser, dbPassword)){
			Statement stm = con.createStatement();
			ResultSet rs = stm.executeQuery("SELECT admin FROM user WHERE iduser = " + idSender + ";");
			Statement stm1 = con.createStatement();
			ResultSet rs1 = stm1.executeQuery("SELECT iduser from user WHERE iduser = " + id + ";");
			if(rs.next() && rs1.next()) {
				if(rs.getBoolean("admin") || rs1.getInt("iduser") == idSender) {
					PreparedStatement stm2 = con.prepareStatement("DELETE FROM user WHERE iduser = ?");
					stm2.setInt(1,id);
					stm2.executeUpdate();
				}
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@PutMapping("/{id}")
	public void modifyUser(@PathVariable int id, String name, String city, int idSender) {
		String dbHost = restTemplate.getForObject("http://ConfigurationService/config/host", String.class);
		String dbPort = restTemplate.getForObject("http://ConfigurationService/config/port", String.class);
		String dbProject = restTemplate.getForObject("http://ConfigurationService/config/project", String.class);
		String dbUser = restTemplate.getForObject("http://ConfigurationService/config/username", String.class);
		String dbPassword = restTemplate.getForObject("http://ConfigurationService/config/password", String.class);
		try(Connection con = DriverManager.getConnection("jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbProject, dbUser, dbPassword)){
			Statement stm = con.createStatement();
			ResultSet rs = stm.executeQuery("SELECT admin FROM user WHERE iduser = " + idSender + ";");
			Statement stm1 = con.createStatement();
			ResultSet rs1 = stm1.executeQuery("SELECT iduser FROM user WHERE iduser = " + id + ";");
			if(rs.next() && rs1.next()) {
				if(rs.getBoolean("admin") || id == idSender) {
					PreparedStatement stm2 = con.prepareStatement("UPDATE user SET city = ?, name = ? WHERE iduser = ?");
					stm2.setString(1, city);
					stm2.setString(2,name);
					stm2.setInt(3, id);
					stm2.executeUpdate();
				}
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
