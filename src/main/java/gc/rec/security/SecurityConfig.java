package gc.rec.security;

import java.util.HashMap;

import javax.sql.DataSource;

import gc.rec.security.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder; 
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter; 
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter 
{

	@Autowired
	MyUserDetailsService userDetailsService;


	static HashMap<String,String> pages = new HashMap<String,String>(){{
		put("SHOW_DASHBOARD","/");
		put("SHOW_STATISTIQUES","/statistiques");
		put("SHOW_DOSSIERS","/dossiers");
		put("SHOW_COMMANDES","/commandes");
		put("SHOW_FACTURES","/factures");
		put("SHOW_PRODUCTS","/produits");
		put("SHOW_CLIENTS","/clients");
		put("SHOW_FOURNISSEURS","/fournisseurs");
	}}; 
	
	static HashMap<String[],String[]> routes = new HashMap<String[],String[]>(){{
		
		put(new String[]{"/dossiers/add","/dossiers/update","/dossiers/delete"},
				new String[]{"UPDATE_DOSSIERS"} );  
		
		put(new String[]{"/commandes/nouveau","/commandes/edit","/commandes/add","/commandes/update","/commandes/delete"},
				new String[]{"UPDATE_COMMANDES"} );  
		
		put(new String[]{"/factures/nouveau","/factures/edit","/factures/add","/factures/update","/factures/delete"},
				new String[]{"UPDATE_FACTURES"});
		put(new String[]{"/factures/print","/factures/preview"},
				new String[]{"ROLE_PRINT_FACTURES"});
		

		put(new String[]{"/fournisseurs/add","/fournisseurs/update","/fournisseurs/delete"},
				new String[]{"UPDATE_FOURNISSEURS"});  
		
		put(new String[]{"/produits/add","/produits/update","/produits/delete",
						 "/familles/save","/familles/delete","/tva/dave","/tva/delete"},
				new String[]{"UPDATE_PRODUITS"});  
	}};

	@Bean
	public PasswordEncoder getPaswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService)
				.passwordEncoder(getPaswordEncoder());
	}
	 
	@Override 
	protected void configure(HttpSecurity http) throws Exception
	{
		http.csrf().disable();
		 
		http.authorizeRequests().antMatchers("/login","/resources/**", "/static/**").permitAll();

		
		http.authorizeRequests().antMatchers("/users/**","/roles/**").hasRole("ADMIN");
		   
		http.authorizeRequests().antMatchers("/masociete").authenticated();
		
		/*
			"/","/produits/**","/commandes/**","/factures/**", 
			"/masociete/**","/404","/statistiques/**","/dossiers/**","/clients/**","/fournisseurs/**"
		*/
		
		 
		pages.forEach((k,v) -> {
			try { http.authorizeRequests().antMatchers(v).hasAnyRole(k,"ADMIN"); } catch (Exception e) {} 
		}); 
		
		routes.forEach((k,v) -> {
			try { http.authorizeRequests().antMatchers(k).hasAnyRole(v[0],"ADMIN"); } catch (Exception e) {} 
		});  


		
		http.formLogin().loginPage("/login");  
		http.exceptionHandling().accessDeniedPage("/403"); 
	}
	
	@Override
    public void configure(WebSecurity web) throws Exception {
		web.ignoring() 
		           .antMatchers("/resources/**", "/static/**");
    }
	 
}











