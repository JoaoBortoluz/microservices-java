package br.edu.atitus.paradigma.cambio_service.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.atitus.paradigma.cambio_service.entities.CambioEntity;
import br.edu.atitus.paradigma.cambio_service.repositories.CambioRepository;

@RestController
@RequestMapping("cambio-service")
public class CambioController {
	private final CambioRepository repository;

	public CambioController(CambioRepository repository) {
		super();
		this.repository = repository;
	}
	
	@Value("${server.port}")
	private int porta;
	
	@GetMapping("/{valor}/{origem}/{destino}")
	public ResponseEntity<CambioEntity> getCambio(
			@PathVariable double valor, 
			@PathVariable String origem,
			@PathVariable String destino) throws Exception {
		CambioEntity cambio = repository.findByOrigemAndDestino(origem, destino)
				.orElseThrow(() -> new Exception("Cambio não encontrado"));
		cambio.setAmbiente("Servidor rodando na porta: " + porta);
		cambio.setValorConvertido(valor * cambio.getFator());
		return ResponseEntity.ok(cambio);
		
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handler(Exception e){
		String message = e.getMessage().replaceAll("[\\r\\n]", "");
		return ResponseEntity.badRequest().body(message);
	}
}
