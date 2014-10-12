package de.craut;

import java.util.Map;

import javax.servlet.MultipartConfigElement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.embedded.MultipartConfigFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import de.craut.service.UserService;

@Configuration
@EnableAutoConfiguration
@ComponentScan
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@Controller
public class Application {

	public static void main(String[] args) {

		ApplicationContext ctx = SpringApplication.run(Application.class, args);
		if (ctx.containsBean("initDB")) {
			InitDB initDB = ctx.getBean("initDB", InitDB.class);
			initDB.init(ctx);
		}

	}

	@RequestMapping("/")
	public String home(Map<String, Object> model) {
		return "redirect:activities/list";
	}

	@Bean
	public ApplicationSecurity applicationSecurity() {
		return new ApplicationSecurity();
	}

	@Configuration
	@Order(Ordered.HIGHEST_PRECEDENCE)
	protected static class AuthenticationSecurity extends GlobalAuthenticationConfigurerAdapter {

		@Autowired
		private UserService userservice;

		@Override
		public void init(AuthenticationManagerBuilder auth) throws Exception {
			auth.userDetailsService(userservice);
		}
	}

	@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
	protected static class ApplicationSecurity extends WebSecurityConfigurerAdapter {

		@Autowired
		private SecurityProperties security;

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.authorizeRequests().anyRequest().fullyAuthenticated().and().formLogin().loginPage("/login").failureUrl("/login?error").permitAll();
		}

		// @Override
		// public void configure(AuthenticationManagerBuilder auth) throws
		// Exception {
		// auth.inMemoryAuthentication().withUser("admin").password("admin").roles("ADMIN",
		// "USER").and().withUser("user").password("user").roles("USER");
		// }

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
		factory.setMaxFileSize("10MB");
		factory.setMaxRequestSize("10MB");
		return factory.createMultipartConfig();
	}

}