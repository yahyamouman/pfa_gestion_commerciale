package gc.rec;

import gc.rec.dao.RoleRepository;
import gc.rec.dao.UserRepository;
import gc.rec.dao.UserRolesRepository;
import gc.rec.entities.Role;
import gc.rec.entities.User;
import gc.rec.entities.UsersRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication; 
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
 

@SpringBootApplication
@ServletComponentScan
public class Application implements ApplicationRunner {
	@Autowired
	UserRolesRepository userRolesRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	RoleRepository roleRepository;
	@Autowired
	PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	@Configuration
	static class ConfigFormatter extends WebMvcConfigurerAdapter {
		@Autowired private gc.rec.imetier.IClientMetier mc;
		@Autowired private gc.rec.imetier.IFournisseurMetier mf;
	    @Override
	    public void addFormatters(FormatterRegistry registry) { 
	    	registry.addConverter(new gc.rec.conversions.ClientConverter(mc));
	    	registry.addConverter(new gc.rec.conversions.FournisseurConverter(mf));
	    }  	    
	}
	@Override
	public void run(ApplicationArguments arg0) throws Exception {
		if (userRepository.findByUserName("youssef").equals(null)) {
			User user = new User("youssef", passwordEncoder.encode("azerty"), true);
			Role role = new Role("ROLE_ADMIN");
			userRepository.saveAndFlush(user);
			roleRepository.saveAndFlush(role);
			UsersRoles usersRoles = new UsersRoles();
			usersRoles.setRole(role);
			usersRoles.setUser(user);
			userRolesRepository.saveAndFlush(usersRoles);
		}
	}
	 
}
