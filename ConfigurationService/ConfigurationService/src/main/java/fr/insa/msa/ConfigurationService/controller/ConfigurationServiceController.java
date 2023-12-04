package fr.insa.msa.ConfigurationService.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/config")
public class ConfigurationServiceController {
	@Value("${db.host}")
	private String dbHost;
	
	@Value("${db.port}")
	private String dbPort;
	
	@Value("${db.project}")
	private String dbProject;
	
	@Value("${db.user}")
	private String dbUsername;
	
	@Value("${db.pass}")
	private String dbPassword;
	
	@GetMapping("/host")
	public String getDBHost() {
		return dbHost;
	}
	
	 @GetMapping("/port")
	public String getDBPort() {
		 return dbPort;
	 }
	 
	 @GetMapping("/project")
	 public String getDBProject() {
		 return dbProject;
	 }
	 
	 @GetMapping("/username")
	 public String getDBUsername() {
		 return dbUsername;
	 }
	 
	 @GetMapping("/password")
	 public String getDBPassword() {
		 return dbPassword;
	 }
}
