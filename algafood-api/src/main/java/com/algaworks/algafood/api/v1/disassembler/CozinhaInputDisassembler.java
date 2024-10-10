package com.algaworks.algafood.api.v1.disassembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.v1.model.input.CozinhaInput;
import com.algaworks.algafood.domain.model.Cozinha;

@Component
public class CozinhaInputDisassembler {

	@Autowired
	private ModelMapper modelMapper;

	public Cozinha toDomainObject(CozinhaInput cidadeInput) {
		return modelMapper.map(cidadeInput, Cozinha.class);
	}
	
	public void copyToDomainObject(CozinhaInput cidadeInput, Cozinha cidade) {
		modelMapper.map(cidadeInput, cidade);
	}
}
