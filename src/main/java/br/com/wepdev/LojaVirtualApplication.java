package br.com.wepdev;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EntityScan(basePackages = "br.com.wepdev.model")
@ComponentScan(basePackages = {"br.*"})
@EnableJpaRepositories(basePackages = {"br.com.wepdev.repository"})
@EnableTransactionManagement
public class LojaVirtualApplication {

	public static void main(String[] args) {
		SpringApplication.run(LojaVirtualApplication.class, args);
	}

}
