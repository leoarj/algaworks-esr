package com.algaworks.algafood.api.v1.disassembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.v1.model.input.RestauranteInput;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.Endereco;
import com.algaworks.algafood.domain.model.Restaurante;

@Component
public class RestauranteInputDisassembler {
	
	@Autowired
	private ModelMapper modelMapper;
	
	public Restaurante toDomainObject(RestauranteInput restauranteInput) {
		return modelMapper.map(restauranteInput, Restaurante.class);
	}
	
	/**
	 * Mapeando para instância atual (manipular objeto atual e, não retornar uma nova instância).
	 */
	public void copyToDomainObject(RestauranteInput restauranteInput, Restaurante restaurante) {
		// Para evitar org.hibernate.HibernateException: identifier of an instance of 
		// com.algaworks.algafood.domain.model.Cozinha was altered from 1 to 2
		restaurante.setCozinha(new Cozinha());
		
		if (restaurante.getEndereco() != null) {
			restaurante.setEndereco(new Endereco());
		}
		
		modelMapper.map(restauranteInput, restaurante);
	}

}
