package pe.edu.unmsm.urtecho.jwt.dao;

import org.springframework.data.repository.CrudRepository;

import pe.edu.unmsm.urtecho.jwt.model.Usuario;

public interface IUsuarioDao extends CrudRepository<Usuario, Long>{
	
	public Usuario findByUsername(String username);

}
