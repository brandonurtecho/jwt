package pe.edu.unmsm.urtecho.jwt.auth.filter;

import static pe.edu.unmsm.urtecho.jwt.auth.Constantes.HEADER_STRING;
import static pe.edu.unmsm.urtecho.jwt.auth.Constantes.TOKEN_PREFIX;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import pe.edu.unmsm.urtecho.jwt.auth.service.JWTService;

/**
 * El filtro de autorizacion nos permite recibir el token cada que el cliente realice una solicitud al servidor
 * verificar que el token este integro y correcto y luego permitir el acceso a algun recurso.
 * */
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {
	
	private JWTService jwtService;

	public JWTAuthorizationFilter(AuthenticationManager authenticationManager, JWTService jwtService) {
		super(authenticationManager);
		this.jwtService = jwtService;
	}
	
	/**
	 * Extraemos el token desde la cabecera y luego verificamos que el token exista o que empiece con Bearer 
	 * si no es asi se continua con el filtro y salimos del metodo
	 * si al menos el token existe entonces validamos la integridad del token y si todo es correcto entonces
	 * asignamos en el contexto de autenticacion el objeto authentication y continuamos con el filtro
	 * */	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		String token = request.getHeader(HEADER_STRING);
		
		System.out.println(token);

		if (!requiereAutenticacion(token)) {
			chain.doFilter(request, response);
			return;
		}

		UsernamePasswordAuthenticationToken authentication = null;
		
		if(jwtService.validarToken(token)) {
			authentication = new UsernamePasswordAuthenticationToken(jwtService.getUsername(token), null, jwtService.getRoles(token));
		}
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		chain.doFilter(request, response);
		
	}

	protected boolean requiereAutenticacion(String token) {

		if (token == null || !token.startsWith(TOKEN_PREFIX)) {
			return false;
		}
		return true;
	}

}
