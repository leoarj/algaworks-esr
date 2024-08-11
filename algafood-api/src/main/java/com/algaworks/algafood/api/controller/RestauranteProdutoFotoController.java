package com.algaworks.algafood.api.controller;

import java.io.IOException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.algaworks.algafood.api.assembler.FotoProdutoModelAssembler;
import com.algaworks.algafood.api.model.FotoProdutoModel;
import com.algaworks.algafood.api.model.input.FotoProdutoInput;
import com.algaworks.algafood.api.openapi.controller.RestauranteProdutoFotoControllerOpenApi;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.model.FotoProduto;
import com.algaworks.algafood.domain.model.Produto;
import com.algaworks.algafood.domain.service.CadastroProdutoService;
import com.algaworks.algafood.domain.service.CatalogoFotoProdutoService;
import com.algaworks.algafood.domain.service.FotoStorageService;
import com.algaworks.algafood.domain.service.FotoStorageService.FotoRecuperada;

@RestController
@RequestMapping(path = "/restaurantes/{restauranteId}/produtos/{produtoId}/foto",
	produces = MediaType.APPLICATION_JSON_VALUE)
public class RestauranteProdutoFotoController implements RestauranteProdutoFotoControllerOpenApi {
	
	@Autowired
	private CadastroProdutoService cadastroProdutoService;
	
	@Autowired
	private CatalogoFotoProdutoService catalogoFotoProdutoService;
	
	@Autowired
	private FotoStorageService fotoStorageService;
	
	@Autowired
	private FotoProdutoModelAssembler fotoProdutoModelAssembler;
	
	@GetMapping
	public FotoProdutoModel buscar(@PathVariable Long restauranteId, @PathVariable Long produtoId) {
		FotoProduto fotoProduto = catalogoFotoProdutoService.buscarOuFalhar(restauranteId, produtoId);
		
		return fotoProdutoModelAssembler.toModel(fotoProduto);
	}
	
	@GetMapping(produces = MediaType.ALL_VALUE)
	public ResponseEntity<?> servir(@PathVariable Long restauranteId,
			@PathVariable Long produtoId, @RequestHeader(name = "accept") String acceptHeader) throws HttpMediaTypeNotAcceptableException {
		try {
			FotoProduto fotoProduto = catalogoFotoProdutoService.buscarOuFalhar(restauranteId, produtoId);
			
			MediaType mediaTypeFoto = MediaType.parseMediaType(fotoProduto.getContentType());
			List<MediaType> mediaTypesAceitas = MediaType.parseMediaTypes(acceptHeader);
			
			verificarCompatibilidadeMediaType(mediaTypeFoto, mediaTypesAceitas);
			
			FotoRecuperada fotoRecuperda = fotoStorageService.recuperar(fotoProduto.getNomeArquivo());
			
			if (fotoRecuperda.temUrl()) {
				return ResponseEntity.status(HttpStatus.FOUND)
						.header(HttpHeaders.LOCATION, fotoRecuperda.getUrl())
						.build();
			} else {
				return ResponseEntity.ok()
						.contentType(mediaTypeFoto)
						.body(new InputStreamResource(fotoRecuperda.getInputStream()));
			}
		} catch (EntidadeNaoEncontradaException e) {
			/*
			 * capturando exception e retornando 404 porque realmente não deve ter representação json
			 * caso recurso não for encontrado.
			 * previne tratamento do exceptionHandler e evita retorno indevido 406.
			 */
			return ResponseEntity.notFound().build();
		}
	}
	
	// ref: https://app.algaworks.com/forum/topicos/83619/o-combobox-response-content-type-nao-aparece-na-swagger-ui-v3
//	//
//	@GetMapping(produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE, MediaType.APPLICATION_JSON_VALUE})
//	public ResponseEntity<?> buscar(@PathVariable Long restauranteId, @PathVariable Long produtoId,
//												@RequestHeader(name="accept") String acceptHeader)
//			throws HttpMediaTypeNotAcceptableException  {
//		
//		if (acceptHeader.equals(MediaType.APPLICATION_JSON_VALUE)) {
//			return recuperarFoto(restauranteId, produtoId);
//		}
//		
//		try {
//			FotoProduto fotoProduto = catalogoFotoProdutoService.buscarOuFalhar(restauranteId, produtoId);
//			MediaType mediaTypeFoto = MediaType.parseMediaType(fotoProduto.getContentType());
//			
//			List<MediaType> mediaTypeAceitas = MediaType.parseMediaTypes(acceptHeader);
//			verificarCompatibilidadeMediaType(mediaTypeFoto,mediaTypeAceitas);
//			var fotoRecuperada =  fotoStorageService.recuperar(fotoProduto.getNomeArquivo());
//			if(fotoRecuperada.temUrl()) {
//				
//				return ResponseEntity
//						.status(HttpStatus.FOUND)
//						.header(HttpHeaders.LOCATION, fotoRecuperada.getUrl())
//						.build();
//			} else {
//				return ResponseEntity.ok()
//						.contentType(mediaTypeFoto)
//						.body(new InputStreamResource(fotoRecuperada.getInputStream()));
//			}	
//		} catch (EntidadeNaoEncontradaException e) {
//			return ResponseEntity.notFound().build();
//		}	
//	}
//	
//	public ResponseEntity<?> recuperarFoto(@PathVariable Long restauranteId,@PathVariable Long produtoId)  {
//		FotoProdutoModel fotoProdutoModel = fotoProdutoModelAssembler.toModel(catalogoFotoProdutoService.buscarOuFalhar(restauranteId, produtoId));
//		return ResponseEntity.ok(fotoProdutoModel);
//	}
//	//
	
	@PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public FotoProdutoModel atualizarFoto(@PathVariable Long restauranteId,
			@PathVariable Long produtoId, @Valid FotoProdutoInput fotoProdutoInput) throws IOException {
		Produto produto = cadastroProdutoService.buscarOuFalhar(restauranteId, produtoId);
		
		MultipartFile arquivo = fotoProdutoInput.getArquivo();
		
		FotoProduto foto = new FotoProduto();
		foto.setProduto(produto);
		foto.setDescricao(fotoProdutoInput.getDescricao());
		foto.setContentType(arquivo.getContentType());
		foto.setTamanho(arquivo.getSize());
		foto.setNomeArquivo(arquivo.getOriginalFilename());
		
//		FotoProduto foto = FotoProduto.builder()
//				.produto(produto)
//				.descricao(fotoProdutoInput.getDescricao())
//				.contentType(arquivo.getContentType())
//				.tamanho(arquivo.getSize())
//				.nomeArquivo(arquivo.getOriginalFilename())
//			.build();
		
		FotoProduto fotoSalva = catalogoFotoProdutoService.salvar(foto, arquivo.getInputStream());
		
		return fotoProdutoModelAssembler.toModel(fotoSalva);
	}
	
	@DeleteMapping
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long restauranteId, @PathVariable Long produtoId) {
		catalogoFotoProdutoService.excluir(restauranteId, produtoId);
	}
	
	private void verificarCompatibilidadeMediaType(MediaType mediaTypeFoto,
			List<MediaType> mediaTypesAceitas) throws HttpMediaTypeNotAcceptableException {
		boolean compativel = mediaTypesAceitas.stream()
				.anyMatch(mediaTypeAceita -> mediaTypeAceita.isCompatibleWith(mediaTypeFoto));
		
		if (!compativel) {
			throw new HttpMediaTypeNotAcceptableException(mediaTypesAceitas);
		}
	}

}
