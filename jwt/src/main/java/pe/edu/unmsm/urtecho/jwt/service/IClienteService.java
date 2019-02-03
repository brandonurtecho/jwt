package pe.edu.unmsm.urtecho.jwt.service;

import java.util.List;

import pe.edu.unmsm.urtecho.jwt.model.Cliente;

public interface IClienteService {

	public List<Cliente> buscar();
		
	public Cliente buscarPorId(Long id);	
	
	public void guardar(Cliente usuario);
		
	public void eliminar(Long id);
	
}
