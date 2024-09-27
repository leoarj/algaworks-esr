package com.algaworks.algafood.api.v1.openapi.controller;

import org.springframework.hateoas.CollectionModel;

import com.algaworks.algafood.api.v1.model.UsuarioModel;
import com.algaworks.algafood.api.v1.model.input.UsuarioAlteracaoSenhaInput;
import com.algaworks.algafood.api.v1.model.input.UsuarioComSenhaInput;
import com.algaworks.algafood.api.v1.model.input.UsuarioInput;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

//@Api(tags = "Usuários")
@SecurityRequirement(name = "security_auth")
public interface UsuarioControllerOpenApi {

//    @ApiOperation("Lista os usuários")
    CollectionModel<UsuarioModel> listar();

//    @ApiOperation("Busca um usuário por ID")
//    @ApiResponses({
//		@ApiResponse(responseCode = "400", description = "ID do usuário inválido",
//				content = @Content(mediaType = "application/json", schema = @Schema(implementation = Problem.class))),
//		@ApiResponse(responseCode = "404", description = "Usuário não encontrado",
//			content = @Content(mediaType = "application/json", schema = @Schema(implementation = Problem.class)))
//	})
    UsuarioModel buscar(
//            @ApiParam(value = "ID do usuário", example = "1", required = true)
            Long usuarioId);

//    @ApiOperation("Cadastra um usuário")
//    @ApiResponses({
//		@ApiResponse(responseCode = "201", description = "Usuário cadastrado")
//	})
    UsuarioModel adicionar(
//            @ApiParam(name = "corpo", value = "Representação de um novo usuário", required = true)
            UsuarioComSenhaInput usuarioInput);
    
//    @ApiOperation("Atualiza um usuário por ID")
//    @ApiResponses({
//		@ApiResponse(responseCode = "200", description = "Usuário atualizado"),
//		@ApiResponse(responseCode = "404", description = "Usuário não encontrado",
//			content = @Content(mediaType = "application/json", schema = @Schema(implementation = Problem.class)))
//	})
    UsuarioModel atualizar(
//            @ApiParam(value = "ID do usuário", example = "1", required = true)
            Long usuarioId,
            
//            @ApiParam(name = "corpo", value = "Representação de um usuário com os novos dados",
//                required = true)
            UsuarioInput usuarioInput);

//    @ApiOperation("Atualiza a senha de um usuário")
//    @ApiResponses({
//		@ApiResponse(responseCode = "204", description = "Senha alterada com sucesso"),
//		@ApiResponse(responseCode = "404", description = "Usuário não encontrado",
//			content = @Content(mediaType = "application/json", schema = @Schema(implementation = Problem.class)))
//	})
    void alterarSenha(
//            @ApiParam(value = "ID do usuário", example = "1", required = true)
            Long usuarioId,
            
//            @ApiParam(name = "corpo", value = "Representação de uma nova senha", 
//                required = true)
            UsuarioAlteracaoSenhaInput usuarioAlteracaoSenhaInput);
}
