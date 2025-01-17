package com.tsswebapps.algafood.api.controller;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tsswebapps.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.tsswebapps.algafood.domain.model.Restaurante;
import com.tsswebapps.algafood.domain.repository.RestauranteRepositoty;
import com.tsswebapps.algafood.domain.service.CadastroRestauranteService;

@RestController
@RequestMapping("/restaurantes")
public class RestauranteController {
	
	@Autowired
	private RestauranteRepositoty restauranteRepository;
	
	@Autowired
	private CadastroRestauranteService cadastroRestauranteService;
	
	@GetMapping
	public List<Restaurante> listar(){
		return restauranteRepository.listar();
	}
	
	@GetMapping("/{restauranteId}")
	public ResponseEntity<Restaurante> buscar(@PathVariable  Long restauranteId) {
		Restaurante restaurante = restauranteRepository.buscar(restauranteId);
		
		if(restaurante != null)
			return ResponseEntity.ok(restaurante);
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
	
	
	@PostMapping
	public ResponseEntity<?> adicionar(@RequestBody Restaurante restaurante) {
		try {
			restaurante = cadastroRestauranteService.salvar(restaurante);
			
			return ResponseEntity.status(HttpStatus.CREATED).body(restaurante);
		}catch (EntidadeNaoEncontradaException e){
			return ResponseEntity.badRequest().body(e.getMessage());
		}	
	}
	
	@PutMapping("/{restauranteId}")
	public ResponseEntity<?> alterar(@PathVariable Long restauranteId, @RequestBody Restaurante restaurante){
		try {
			Restaurante restauranteAtual = restauranteRepository.buscar(restauranteId);
			
			if(restauranteAtual != null) {
				BeanUtils.copyProperties(restaurante, restauranteAtual, "id");
				
				restauranteAtual = cadastroRestauranteService.salvar(restauranteAtual);
				
				return ResponseEntity.status(HttpStatus.OK).body(
						restauranteAtual
					);
			}
			
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Recurso não encontrado.");
			
		}catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	
	

}
