package pe.edu.unmsm.urtecho.jwt.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pe.edu.unmsm.urtecho.jwt.model.Cliente;
import pe.edu.unmsm.urtecho.jwt.service.IClienteService;

@RestController
@RequestMapping(value = "/api/cliente")
public class ClienteController {
	
	@Autowired
	private IClienteService clienteService;
	
	@GetMapping(value = "/")	
    public List<Cliente> buscarTodos() {
        return clienteService.buscar();
    }
	
	@GetMapping(value = "/{id}")
	@Secured({"ROLE_ADMIN", "ROLE_USER"})
    public Cliente buscarUno(@PathVariable Long id) {
        return clienteService.buscarPorId(id);
    }
    
    @PostMapping
    @Secured("ROLE_ADMIN")
    public ResponseEntity<?> registrar(@RequestBody Cliente cliente) {
        clienteService.guardar(cliente);
        return ResponseEntity.ok("Cliente creado con exito");
    }

    @PutMapping
    @Secured("ROLE_ADMIN")
    public ResponseEntity<?> actualizar(@RequestBody Cliente cliente) {
    	clienteService.guardar(cliente);
        return ResponseEntity.ok("Cliente actualizado con exito");
    }

    @DeleteMapping
    @Secured("ROLE_ADMIN")
    public ResponseEntity<?> eliminar(@RequestBody Cliente cliente) {
    	clienteService.eliminar(cliente.getId());
        return ResponseEntity.ok("Cliente eliminado con exito");
    }
	
}
