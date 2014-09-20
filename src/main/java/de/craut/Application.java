package de.craut;

import javax.servlet.MultipartConfigElement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.embedded.MultipartConfigFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@ComponentScan
public class Application {

	public static void main(String[] args) {

		ApplicationContext ctx = SpringApplication.run(Application.class, args);
		if (ctx.containsBean("initDB")) {
			InitDB initDB = ctx.getBean("initDB", InitDB.class);
			initDB.init(ctx);
		}

	}

	@Bean
	public ServerProperties myServerProperties() {
		ServerProperties p = new ServerProperties();
		String portStr = System.getProperty("port");
		int port = (portStr != null) ? Integer.parseInt(portStr) : 8090;
		p.setPort(port); // comm
		return p;
	}

	@Bean
	MultipartConfigElement multipartConfigElement() {
		MultipartConfigFactory factory = new MultipartConfigFactory();
		factory.setMaxFileSize("512KB");
		factory.setMaxRequestSize("512KB");
		return factory.createMultipartConfig();
	}

}