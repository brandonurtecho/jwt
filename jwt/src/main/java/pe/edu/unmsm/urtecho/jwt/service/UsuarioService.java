package pe.edu.unmsm.urtecho.jwt.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.edu.unmsm.urtecho.jwt.dao.IUsuarioDao;
import pe.edu.unmsm.urtecho.jwt.model.Usuario;

/**
 * Se implementa una interfaz propia de springsecurity para trabajar en este caso con JPA
 * pero tambien se puede trabajar con cualquier tipo de proveedor para implementar el proceso
 * de autenticacion, puede ser con jdbc, jpa, mybatis, etc.
 * Nos obligará a implementar el metodo loadUserByUsername()
 * */
@Service
public class UsuarioService implements UserDetailsService, IUsuarioService {
	
	private Logger logger = LoggerFactory.getLogger(UsuarioService.class);

	@Autowired
	private IUsuarioDao usuarioDao;
	
	/**
	 * El metodo retorna el usuario de spring security, retornamos un tipo User que es la implementacion
	 * de UserDetails en donde indicamos el usuario, el pass, el enabled y los 3 siguientes con true o cualquiera
	 * valor que queramos. El ultimo parametro "authorities" que es una lista del tipo grantedauthorities.
	 * Debemos obtener los roles de los usuarios pero para eso debemos usar STREAMS para transformar los roles a una lista
	 * de tipo GrantedAuthority, que es una INTERFAZ, 
	 * para eso debemos usar la implementacion que es SimpleGrantedAuthority la CLASE
	 * Luego lo transformamos a la lista.
	 * */
	@Override
	@Transactional(readOnly=true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario = usuarioDao.findByUsername(username);	
		
		if(usuario == null) {
			logger.error("Error en el login: no existe el usuario '"+username+ "' en el sistema!");
			throw new UsernameNotFoundException("Error en el login: no existe el usuario '"+username+ "' en el sistema!");
		}
		
		List<GrantedAuthority> authorities = usuario.getRoles()
				.stream()
				.map(role -> new SimpleGrantedAuthority(role.getNombre()))
				.peek(authority -> logger.info(authority.getAuthority())) //esto es solo para mostrar los roles a medida que avanza el stream
				.collect(Collectors.toList());
		return new User(usuario.getUsername(), usuario.getPassword(), usuario.getEnabled(), true, true, true, authorities);
	}

	@Override
	@Transactional(readOnly=true)
	public List<Usuario> buscar() {
		return (List<Usuario>) usuarioDao.findAll();
	}

	@Override
	@Transactional
	public void guardar(Usuario usuario) {
		usuarioDao.save(usuario);
	}

	@Override
	@Transactional
	public void eliminar(Long id) {
		usuarioDao.deleteById(id);
	}	
	
	@Override
	@Transactional
	public Usuario findByUsername(String username) {		
		return usuarioDao.findByUsername(username);
	}	
}
