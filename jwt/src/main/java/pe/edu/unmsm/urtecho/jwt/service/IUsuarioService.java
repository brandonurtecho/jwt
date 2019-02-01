package pe.edu.unmsm.urtecho.jwt.service;

import java.util.List;

import pe.edu.unmsm.urtecho.jwt.model.Usuario;

public interface IUsuarioService {
	
	public List<Usuario> buscar();
	
	public void guardar(Usuario usuario);
		
	public void eliminar(Long id);	
	
	public Usuario findByUsername(String username);
}
