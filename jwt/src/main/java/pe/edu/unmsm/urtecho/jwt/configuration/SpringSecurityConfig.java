package pe.edu.unmsm.urtecho.jwt.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import pe.edu.unmsm.urtecho.jwt.auth.filter.JWTAuthenticationFilter;
import pe.edu.unmsm.urtecho.jwt.auth.filter.JWTAuthorizationFilter;
import pe.edu.unmsm.urtecho.jwt.auth.service.JWTService;

@EnableWebSecurity
//@EnableGlobalMethodSecurity(securedEnabled = true)
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private JWTService jwtService;
	
	/**
	 * Este metodo es un parte de la clase WebSecurityConfigurerAdapter y aqui colocamos los filtros
	 * que usaremos para nuestro token, el primer filtro por el que pasará es el filtro de autenticacion
	 * en donde validamos las credenciales del usuario y generamos el token en el inicio de sesion
	 * si todo es correcto, OJO, dentro de las clases filtro no puede inyectarse nada, es decir, no podemos
	 * usar autowired o bean o cualquier otra anotacion que implique inyeccion de dependencias por eso pasamos
	 * como segundo parametro en el constructor la clase de servicio de jwt que es inyectada en esta 
	 * clase de configuracion,
	 * Debemos deshabilitar la proteccion csrf e indicar que el tipo de politica es stateless, es decir sin estado
	 * no se manejaran sesiones, sino, manejaremos tokens
	 * */	
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.authorizeRequests().antMatchers(HttpMethod.GET,"/api/cliente/").permitAll()
		.anyRequest().authenticated()
		.and()
		.addFilter(new JWTAuthenticationFilter(authenticationManager(), jwtService))
		.addFilter(new JWTAuthorizationFilter(authenticationManager(), jwtService))
		.csrf().disable()
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

	}
	
	/**
	 * Aqui creamos el AuthenticationManager usando el tipo UserDetailsService y pasando el tipo de encriptacion que usaremos
	 * en este caso BCrypt
	 * */
	@Autowired
	public void configurerGlobal(AuthenticationManagerBuilder build) throws Exception {
		build.userDetailsService(userDetailsService)
		.passwordEncoder(passwordEncoder());
	}
		
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
