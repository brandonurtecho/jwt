package pe.edu.unmsm.urtecho.jwt.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import pe.edu.unmsm.urtecho.jwt.model.Cliente;

public interface IClienteDao extends JpaRepository<Cliente, Long>{
}
