package pe.edu.unmsm.urtecho.jwt.auth;

import java.security.Key;
import java.security.KeyPair;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class Constantes {
	public static final Key SECRET = Keys.secretKeyFor(SignatureAlgorithm.HS512);
	
	/*Aqui se genera el par de llaves privadas y publicas */
	public static final KeyPair KEY_PAIR = Keys.keyPairFor(SignatureAlgorithm.RS256);	
	public static final long EXPIRATION_DATE = 14000000L;
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String HEADER_STRING = "Authorization";
}
