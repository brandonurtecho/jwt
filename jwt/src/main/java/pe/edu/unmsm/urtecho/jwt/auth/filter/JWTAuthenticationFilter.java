package pe.edu.unmsm.urtecho.jwt.auth.filter;

import static pe.edu.unmsm.urtecho.jwt.auth.Constantes.HEADER_STRING;
import static pe.edu.unmsm.urtecho.jwt.auth.Constantes.TOKEN_PREFIX;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import pe.edu.unmsm.urtecho.jwt.auth.service.JWTService;
import pe.edu.unmsm.urtecho.jwt.model.Usuario;

/**
 * En este filtro validamos las credenciales del usuario y creamos el token para devolverlo en la respuesta
 * cuando inicie sesion, extendemos de UsernamePasswordAuthenticationFilter, en el constructor
 * recibimos el manejador de autenticacion y el objeto jwtService
 * */
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	
	private AuthenticationManager authenticationManager;
	private JWTService jwtService;

	
	/**
	 * Podemos cambiar la ruta por defecto del login que es "http://localhost:8080/login"
	 * con el metodo setRequiresAuthenticationRequestMatcher
	 * */
	public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JWTService jwtService) {
		this.authenticationManager = authenticationManager;
//		setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/api/login", "POST"));
		this.jwtService = jwtService;
	}
	
	/**
	 * Aqui se extrae el usuario y password de la peticion, tenemos dos formas, la primera que esta comentada
	 * es a partir de un form-data como podriamos hacerlo con thymeleaf o jsp o jstl, la segunda es solo mandando
	 * en el body del request en formato json el username y password, se pueden mezclar ambos contextos pero no
	 * es recomendable.
	 * Segun la segunda forma extraemos los datos del body del request y debemos transformarlo a un objeto de tipo Usuario
	 * para ello usamos ObjectMapper().readValue como primer parametro es el stream que son los datos del body y segund parametro
	 * es el tipo clase al que se transformará
	 * 
	 * Luego devolvemos el usuario y password, debemos considerar las excepciones.
	 * 
	 * Guardamos los datos en un objeto UsernamePasswordAuthenticationToken y retornamos el Authentication
	 * La clase Authentication se maneja a traves de todo el ciclo de vida de seguridad, contiene los datos
	 * importantes del usuario que se autentica
	 * 
	 * LUEGO: cuando se autentica bien entra al metodo successfulAuthentication, en caso contrario
	 * unsuccessfulAuthentication
	 * 
	 * */
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		
		String username = null;
		String password = null;
//		String username = obtainUsername(request);
//		String password = obtainPassword(request);
//		
//		if(username != null && password !=null) {
//			logger.info("Username desde request parameter (form-data): " + username);
//			logger.info("Password desde request parameter (form-data): " + password);
//			
//		} else {
			Usuario user = null;
			try {
				
				user = new ObjectMapper().readValue(request.getInputStream(), Usuario.class);
				
				username = user.getUsername();
				password = user.getPassword();
				
				logger.info("Username desde request InputStream (raw): " + username);
				logger.info("Password desde request InputStream (raw): " + password);
				
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
//		}

		username = username.trim();
		
		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
		
		return authenticationManager.authenticate(authToken);
	}

	
	/**
	 * Este metodo se ejecuta de forma automatica en caso de que la autenticacion sea correcta, aqui creamos el token y preparamos
	 * la respuesta que sera enviada con el token y datos adicionales al usuarios que le serviran para navegar autenticado
	 * Primero creamos el token y pasamos como parametro el tipo Authentication que contiene los datos mas importantes del usuario
	 * como username, credenciales, roles, etc. Luego agregamos en la cabecera la propiedad Authorization con el token
	 * 
	 * Creamos un Map en donde añadiremos datos adicionales que estaran en el cuerpo de la respuesta,
	 * Agregamos el body pero primero debemos transformar ese map a un json usando "new ObjectMapper().writeValueAsString(body)"
	 * Hacemos el status de la respuesta de tipo 200
	 * y especificamos el tipo de contenido que sera, es application/json
	 * */
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {

		String token = jwtService.crearToken(authResult);
		
		response.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
		
		Map<String, Object> body = new HashMap<String, Object>();
		body.put("token", token);
		body.put("user", (User) authResult.getPrincipal());
		body.put("mensaje", String.format("Hola %s, has iniciado sesión con éxito!", ((User)authResult.getPrincipal()).getUsername()) );
		
		response.getWriter().write(new ObjectMapper().writeValueAsString(body));
		response.setStatus(200);
		response.setContentType("application/json");
	}
	
	/**
	 * Esto se ejecuta cuando algun dato del usuario ha sido incorrecto al intentar iniciar sesion
	 * , enviamos un mensaje configurado de error, de la misma forma que en successfulAuthentication, mandamos el body
	 * parseado a un JSON luego establecemos el status de tipo 401 que es, no autorizado, el de 403 es acceso denegado
	 * cualquiera esta bien pero para este caso mejor usamos el 401, el 403 se presta mas para lo relacionado a roles
	 * */
	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {

		Map<String, Object> body = new HashMap<String, Object>();
		body.put("mensaje", "Error de autenticación: username o password incorrecto!");
		body.put("error", failed.getMessage());
		
		response.getWriter().write(new ObjectMapper().writeValueAsString(body));
		response.setStatus(401);
		response.setContentType("application/json");
	}
	
}
