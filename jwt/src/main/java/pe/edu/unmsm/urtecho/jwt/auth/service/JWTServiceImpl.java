package pe.edu.unmsm.urtecho.jwt.auth.service;

import static pe.edu.unmsm.urtecho.jwt.auth.Constantes.EXPIRATION_DATE;
import static pe.edu.unmsm.urtecho.jwt.auth.Constantes.SECRET;
import static pe.edu.unmsm.urtecho.jwt.auth.Constantes.TOKEN_PREFIX;
//import static pe.edu.unmsm.urtecho.jwt.auth.Constantes.KEY_PAIR;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import pe.edu.unmsm.urtecho.jwt.auth.SimpleGrantedAuthorityMixin;

@Component
public class JWTServiceImpl implements JWTService {
	
	/**
	 * Este metodo sirve para crear el token, se obtiene el username, los roles se insertan en los claims,
	 * dentro de authorities, establecemos la clave secreta, la fecha y la fecha de expiracion
	 * y en base a todo eso construimos el token y retornamos la cadena con el token.
	 * Aqui la clave secreta puede ser tambien de tipo RS256 y aqui en la construccion iria la clave privada
	 * */	
	@Override
	public String crearToken(Authentication auth) throws IOException {

		String username = ((User) auth.getPrincipal()).getUsername();

		Collection<? extends GrantedAuthority> roles = auth.getAuthorities();
		
		Claims claims = Jwts.claims();
		claims.put("authorities", new ObjectMapper().writeValueAsString(roles));		
			    		
		String token = Jwts.builder()
				.setClaims(claims)
				.setSubject(username)
				.signWith(SECRET)
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_DATE))
				.compact();
		return token;
	}
	
	/**
	 * Aqui en la validacion vemos la integridad del token y si el que nos esta llegando corresponde con el que 
	 * se le generó al usuario
	 * */	
	@Override
	public boolean validarToken(String token) {

		try {
			getClaims(token);
			return true;
		} catch (JwtException | IllegalArgumentException e) {
			return false;
		}
	}
	
	/**
	 * Aqui obtenemos los claims con la llave secreta y usamos el token tambien pero ojo, si vamos a usar el algoritmo
	 * RSA entonces aqui usamos en la clave publica
	 * */
	@Override
	public Claims getClaims(String token) {		
		
		Claims claims = Jwts.parser().setSigningKey(SECRET)
				.parseClaimsJws(resolver(token)).getBody();
		return claims;
	}
	
	@Override
	public String getUsername(String token) {
		return getClaims(token).getSubject();
	}
		
	/**
	 * Aqui obtenemos los roles, notar aqui que necesitabamos usar una clase auxiliar, mixin para poder parsear bien
	 * los roles, la clase es SimpleGrantedAuthorityMixin, puesto que la clase SimpleGrantedAuthority no tiene un constructor
	 * */
	@Override
	public Collection<? extends GrantedAuthority> getRoles(String token) throws IOException {
		Object roles = getClaims(token).get("authorities");

		Collection<? extends GrantedAuthority> authorities = Arrays
				.asList(new ObjectMapper().addMixIn(SimpleGrantedAuthority.class, SimpleGrantedAuthorityMixin.class)
						.readValue(roles.toString().getBytes(), SimpleGrantedAuthority[].class));

		return authorities;
	}
	
	/**
	 * Si el token empieza con Bearer entonces hacemos que retorne el token sin el prefijo Bearer
	 * */
	@Override
	public String resolver(String token) {
		if (token != null && token.startsWith(TOKEN_PREFIX)) {
			return token.replace(TOKEN_PREFIX, "");
		}
		return null;
	}

}
