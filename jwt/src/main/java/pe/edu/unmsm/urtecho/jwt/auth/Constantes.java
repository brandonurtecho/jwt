package pe.edu.unmsm.urtecho.jwt.auth;

import org.springframework.util.Base64Utils;

public class Constantes {
	public static final String SECRET = Base64Utils.encodeToString("cidyYb9F-gDw3PzNNvIv__cvVcsMq0mSJpNGGL1B2uxdHRYLLCJH9acVX7j_yQt65LxGxtkbjPA3_5LOWoyfc4C9OWQzFu9GgtUZyCx5HEAZvaJAOP4HibwoDdhm12ztYQsUvRUPQn1NteXSz7OaHrTGYCsT3NMcT8ZJi9Lc3Xu_t4i9k56Sszu4XT4CYraRdSLlGxeDfBY0oN7Ft6xauaLx_nWJ7BMFazXwGrleb0pnmkZTaizpdMXdIMhqsFu4N_bWkx_WvJdQvrFYt3XeuQzZtmdJ0RpWYQFqWML6XflIfOyu76B_JLBVaAgMoqbBcBLI5ABg5hveHMqQnXeeIQ".getBytes());		
	public static final long EXPIRATION_DATE = 14000000L;
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String HEADER_STRING = "Authorization";
}
