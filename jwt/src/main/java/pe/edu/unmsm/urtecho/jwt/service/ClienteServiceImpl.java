package pe.edu.unmsm.urtecho.jwt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.edu.unmsm.urtecho.jwt.dao.IClienteDao;
import pe.edu.unmsm.urtecho.jwt.model.Cliente;

@Service
public class ClienteServiceImpl implements IClienteService {

	@Autowired
	private IClienteDao clienteDao;

	@Override
	@Transactional(readOnly = true)
	public List<Cliente> buscar() {
		return (List<Cliente>) clienteDao.findAll();
	}
	
	@Override
	@Transactional(readOnly = true)
	public Cliente buscarPorId(Long id) {
		return clienteDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void guardar(Cliente cliente) {
		clienteDao.save(cliente);
	}

	@Override
	@Transactional
	public void eliminar(Long id) {
		clienteDao.deleteById(id);
	}

}
