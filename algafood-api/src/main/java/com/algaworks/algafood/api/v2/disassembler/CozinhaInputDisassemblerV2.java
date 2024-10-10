package com.algaworks.algafood.api.v2.disassembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.v2.model.input.CozinhaInputV2;
import com.algaworks.algafood.domain.model.Cozinha;

@Component
public class CozinhaInputDisassemblerV2 {

	@Autowired
	private ModelMapper modelMapper;

	public Cozinha toDomainObject(CozinhaInputV2 cidadeInput) {
		return modelMapper.map(cidadeInput, Cozinha.class);
	}
	
	public void copyToDomainObject(CozinhaInputV2 cidadeInput, Cozinha cidade) {
		modelMapper.map(cidadeInput, cidade);
	}
}
