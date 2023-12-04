package fr.insa.msa.DiscoveryServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class DiscoverryServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(DiscoverryServerApplication.class, args);
	}

}
