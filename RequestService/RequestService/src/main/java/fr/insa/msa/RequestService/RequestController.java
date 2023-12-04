package fr.insa.msa.RequestService;

import java.util.ArrayList;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import java.sql.*;

@RestController
@RequestMapping("/request")
public class RequestController {
	
	@Autowired
	private RestTemplate restTemplate;
	
	
	
	@GetMapping("/{id}")
	public Request getRequest(@PathVariable int id) {
		Request request = null;
		String dbHost = restTemplate.getForObject("http://ConfigurationService/config/host", String.class);
		String dbPort = restTemplate.getForObject("http://ConfigurationService/config/port", String.class);
		String dbProject = restTemplate.getForObject("http://ConfigurationService/config/project", String.class);
		String dbUser = restTemplate.getForObject("http://ConfigurationService/config/username", String.class);
		String dbPassword = restTemplate.getForObject("http://ConfigurationService/config/password", String.class);
		try(Connection con = DriverManager.getConnection("jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbProject, dbUser, dbPassword)){
			Statement stm = con.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM request WHERE idRequest=" + id + ";");
			if(rs.next()) {
				int idRequest = rs.getInt("idRequest");
				int idCreator = rs.getInt("idCreator");
				int idVolunteer = rs.getInt("idVolunteer");
				String content = rs.getString("description");
				request = new Request(idRequest,content,idCreator,idVolunteer);
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return request;
	}
	
	@GetMapping("/all")
	public ArrayList<Request> getAllRequests() {
		ArrayList<Request> requests = new ArrayList<Request>();
		String dbHost = restTemplate.getForObject("http://ConfigurationService/config/host", String.class);
		String dbPort = restTemplate.getForObject("http://ConfigurationService/config/port", String.class);
		String dbProject = restTemplate.getForObject("http://ConfigurationService/config/project", String.class);
		String dbUser = restTemplate.getForObject("http://ConfigurationService/config/username", String.class);
		String dbPassword = restTemplate.getForObject("http://ConfigurationService/config/password", String.class);
		try(Connection con = DriverManager.getConnection("jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbProject, dbUser, dbPassword)){
			Statement stm = con.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM request;");
			while (rs.next()) {
				requests.add(new Request(rs.getInt("idRequest"),rs.getString("description"),rs.getInt("idCreator"),rs.getInt("idVolunteer")));
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return requests;
	}
	// TODO faire return un tableau de requête
	
	@PostMapping("/create")
	public void createRequest(String content, int creator) {
		String dbHost = restTemplate.getForObject("http://ConfigurationService/config/host", String.class);
		String dbPort = restTemplate.getForObject("http://ConfigurationService/config/port", String.class);
		String dbProject = restTemplate.getForObject("http://ConfigurationService/config/project", String.class);
		String dbUser = restTemplate.getForObject("http://ConfigurationService/config/username", String.class);
		String dbPassword = restTemplate.getForObject("http://ConfigurationService/config/password", String.class);
		try(Connection con = DriverManager.getConnection("jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbProject, dbUser, dbPassword)){
			PreparedStatement stm = con.prepareStatement("INSERT INTO request (idCreator,description) VALUES (?,?)");
			stm.setInt(1, creator);
			stm.setString(2, content);
			stm.executeUpdate();
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@DeleteMapping("/{id}")
	public void deleteRequest(@PathVariable int id,int idSender) {
		String dbHost = restTemplate.getForObject("http://ConfigurationService/config/host", String.class);
		String dbPort = restTemplate.getForObject("http://ConfigurationService/config/port", String.class);
		String dbProject = restTemplate.getForObject("http://ConfigurationService/config/project", String.class);
		String dbUser = restTemplate.getForObject("http://ConfigurationService/config/username", String.class);
		String dbPassword = restTemplate.getForObject("http://ConfigurationService/config/password", String.class);
		try(Connection con = DriverManager.getConnection("jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbProject, dbUser, dbPassword)){
			Statement stm = con.createStatement();
			ResultSet rs = stm.executeQuery("SELECT admin FROM user WHERE iduser = " + idSender + ";");
			Statement stm1 = con.createStatement();
			ResultSet rs1 = stm1.executeQuery("SELECT idCreator from request WHERE idRequest = " + id + ";");
			if(rs.next() && rs1.next()) {
				if(rs.getBoolean("admin") || rs1.getInt("idCreator") == idSender) {
					PreparedStatement stm2 = con.prepareStatement("DELETE FROM request WHERE idRequest = ?");
					stm2.setInt(1, id);
					stm2.executeUpdate();
				}
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@PutMapping("/{id}")
	public void verifyRequest(@PathVariable int id, int idSender) {
		String dbHost = restTemplate.getForObject("http://ConfigurationService/config/host", String.class);
		String dbPort = restTemplate.getForObject("http://ConfigurationService/config/port", String.class);
		String dbProject = restTemplate.getForObject("http://ConfigurationService/config/project", String.class);
		String dbUser = restTemplate.getForObject("http://ConfigurationService/config/username", String.class);
		String dbPassword = restTemplate.getForObject("http://ConfigurationService/config/password", String.class);
		try(Connection con = DriverManager.getConnection("jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbProject, dbUser, dbPassword)){
			Statement stm = con.createStatement();
			ResultSet rs = stm.executeQuery("SELECT admin FROM user WHERE iduser = " + idSender + ";");
			Statement stm1 = con.createStatement();
			ResultSet rs1 = stm1.executeQuery("SELECT idCreator from request WHERE idRequest = " + id + ";");
			if(rs.next() && rs1.next()) {
				if(rs.getBoolean("admin")) {
					PreparedStatement stm2 = con.prepareStatement("UPDATE request SET validated = 1 WHERE idRequest = ?");
					stm2.setInt(1, id);
					stm2.executeUpdate();
				}
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
