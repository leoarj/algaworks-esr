package com.algaworks.algafood.api.exceptionhandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.catalina.WebResource;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.algaworks.algafood.core.validation.ValidacaoException;
import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;

@ControllerAdvice // Para capturar globalmente as exceptions dos controllers
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

	private static final String MSG_ERRO_GENERICA_USUARIO_FINAL =
			"Ocorreu um erro interno inesperado no sistema. Tente novamente e se o "
			+ "problema persistir, entre em contato com o administrador do sistema.";
	
	/**
	 * Para ter acesso ao recurso de mensagens customizadas (messages.properties)
	 */
	@Autowired
	private MessageSource messageSource;

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleUncaught(Exception ex, WebRequest request) {
		
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		ProblemType problemType = ProblemType.ERRO_DE_SISTEMA;
		String detail = MSG_ERRO_GENERICA_USUARIO_FINAL;
		
		// Para fins de desenvolvimento
		ex.printStackTrace();
		
		Problem problem = createProblemBuilder(status, problemType, detail)
				.userMessage(detail)
				.build();
		
		return handleExceptionInternal(ex, problem, null, status, request);
	}
	
	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		
		ProblemType problemType = ProblemType.RECURSO_NAO_ENCONTRADO;
		
		String nomeMetodoHttp = ex.getHttpMethod();
		String nomeRecurso = ex.getRequestURL();
		String detail = String.format("O recurso %s %s que você tentou acessar, é inexistente.", nomeMetodoHttp, nomeRecurso);
		
		Problem problem = createProblemBuilder(status, problemType, detail)
				.userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL)
				.build();
		
		return handleExceptionInternal(ex, problem, headers, status, request);
	}
	
	/**
	 * Trata diferença de tipos de forma mais abrangente.
	 * Se a exceção for relacionada a parâmetros da requisição,
	 * repassa para tratamento especializado.
	 */
	@Override
	protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		
		if (ex instanceof MethodArgumentTypeMismatchException) {
			return handleMethodArgumentTypeMismatch((MethodArgumentTypeMismatchException) ex, headers, status, request);
		}
		
		return super.handleTypeMismatch(ex, headers, status, request);
	}
	
	private ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
	
		String parametroNome = ex.getParameter().getParameterName();
		Object parametroValor = ex.getValue();
		String tipoRequeridoNome = ex.getRequiredType().getSimpleName();
		
		ProblemType problemType = ProblemType.PARAMETRO_INVALIDO;
		
		String detail = String.format("O parâmetro de URL '%s' recebeu o valor '%s', que é de um tipo inválido. "
				+ "Corrija e informe um valor compatível com o tipo %s.",
				parametroNome, parametroValor, tipoRequeridoNome);
		
		Problem problem = createProblemBuilder(status, problemType, detail)
				.userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL)
				.build();
		
		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
	}
	
	/**
	 * Customiza exception handler específico de ResponseEntityExceptionHandler. 
	 */
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		// Para obter a causa raíz da exceção
		Throwable rootCause = ExceptionUtils.getRootCause(ex);
		if (rootCause instanceof InvalidFormatException) {
			return handleInvalidFormat((InvalidFormatException) rootCause, headers, status, request);
		} else if (rootCause instanceof PropertyBindingException) {
			return handlePropertyBinding((PropertyBindingException) rootCause, headers, status, request);
		}
		
		ProblemType problemType = ProblemType.MENSAGEM_INCOMPREENSIVEL;
		String detail = "O corpo da requisição está inválido. Verifique erro de sintaxe.";
		
		Problem problem = createProblemBuilder(status, problemType, detail)
				.userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL)
				.build();
		
		return handleExceptionInternal(ex, problem, headers, status, request);
	}
	
	/**
	 * Especializa o tratamento de uma exceção InvalidFormatException
	 * a fim de obter maiores detalhes, como os campos com dados em formato inválido.
	 */
	private ResponseEntity<Object> handleInvalidFormat(InvalidFormatException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		// Obtém a lista de campos (em hierarquia) com formato inválido e os concatena
		String path = joinPath(ex.getPath());
		
		Object value = ex.getValue(); // Valor inválido
		String targetTypeSimpleName = ex.getTargetType().getSimpleName(); // Tipo de dado esperado para a propriedade
		
		ProblemType problemType = ProblemType.MENSAGEM_INCOMPREENSIVEL;
		String detail = String.format("A propriedade '%s' recebeu o valor '%s', "
				+ "que é de um tipo inválido. Corrija e informe um valor compatível com o tipo %s.",
				path, value, targetTypeSimpleName);
		
		Problem problem = createProblemBuilder(status, problemType, detail)
				.userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL)
				.build();
		
		return handleExceptionInternal(ex, problem, headers, status, request);
	}
	
	/**
	 * Para tratar propriedades inexistentes ou ignoradas (UnrecognizedPropertyException, IgnoredPropertyException).
	 */
	private ResponseEntity<Object> handlePropertyBinding(PropertyBindingException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		String path = joinPath(ex.getPath());
		
		ProblemType problemType = ProblemType.MENSAGEM_INCOMPREENSIVEL;
		String detail = String.format("A propriedade '%s' não existe. "
				+ "Corrija ou remova essa propriedade e tente novamente.",
				path);
		
		Problem problem = createProblemBuilder(status, problemType, detail)
				.userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL)
				.build();
		
		return handleExceptionInternal(ex, problem, headers, status, request);
	}
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		return handleValidationInternal(ex, ex.getBindingResult(), headers, status, request);
	}
	
	@ExceptionHandler(ValidacaoException.class)
	private ResponseEntity<Object> handleValidacao(ValidacaoException ex, WebRequest request) {
		
		return handleValidationInternal(ex, ex.getBindingResult(), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}

	/**
	 * Para reutilização de código comum, referente ao tratamento de validações de propriedades.
	 */
	private ResponseEntity<Object> handleValidationInternal(Exception ex, BindingResult bindingResult, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		
		ProblemType problemType = ProblemType.DADOS_INVALIDOS;
		String detail = "Um ou mais campos estão inválidos. "
				+ "Faça o preenchimento correto e tente novamente.";
		
		// Recupera resultado de associação de propriedades referentes ao model
		//BindingResult bindingResult = ;//ex.getBindingResult();
		
		// Realiza o mapeamento dos campos e suas respectivas mensagens de erro
		List<Problem.Object> problemObjects = bindingResult.getAllErrors().stream()
				.map(objectError -> {
					String message = messageSource.getMessage(objectError, LocaleContextHolder.getLocale());
					
					// Obtém nome do objeto (classe)
					String name = objectError.getObjectName();
					
					// Se for uma instância de erro referente a um campo da classe
					// obtém o nome do campo
					if (objectError instanceof FieldError) {
						name = ((FieldError) objectError).getField();
					}
					
					return Problem.Object.builder()
						.name(name)
						.userMessage(message)
						.build();
					})
				.collect(Collectors.toList());
		
		Problem problem = createProblemBuilder(status, problemType, detail)
				.userMessage(detail)
				.objects(problemObjects)
				.build();
		
		return handleExceptionInternal(ex, problem, headers, status, request);
	}
	
	@ExceptionHandler(EntidadeNaoEncontradaException.class)
	public ResponseEntity<?> handleEntidadeNaoEncontradaException(EntidadeNaoEncontradaException ex, WebRequest request) {
		
		HttpStatus status = HttpStatus.NOT_FOUND;
		ProblemType problemType = ProblemType.RECURSO_NAO_ENCONTRADO;
		String detail = ex.getMessage();
		
		Problem problem = createProblemBuilder(status, problemType, detail)
				.userMessage(detail)
				.build();
		
		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
	}
	
	@ExceptionHandler(EntidadeEmUsoException.class)
	public ResponseEntity<?> handleEntidadeEmUsoException(EntidadeEmUsoException ex, WebRequest request) {
		
		HttpStatus status = HttpStatus.CONFLICT;
		ProblemType problemType = ProblemType.ENTIDADE_EM_USO;
		String detail = ex.getMessage();
		
		Problem problem = createProblemBuilder(status, problemType, detail)
				.userMessage(detail)
				.build();
		
		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
	}
	
	@ExceptionHandler(NegocioException.class)
	public ResponseEntity<?> handleNegocioException(NegocioException ex, WebRequest request) {
		
		HttpStatus status = HttpStatus.BAD_REQUEST;
		ProblemType problemType = ProblemType.ERRO_NEGOCIO;
		String detail = ex.getMessage();
		
		Problem problem = createProblemBuilder(status, problemType, detail)
				.userMessage(detail)
				.build();
		
		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
	}
	
	/**
	 * Tratamento global da exceções.
	 */
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		
		if (body == null) {
			body = Problem.builder()
					.title(status.getReasonPhrase())
					.status(status.value())
					.timestamp(LocalDateTime.now())
					.userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL)
					.build();
		} else if (body instanceof String) {
			body = Problem.builder()
					.title((String) body)
					.status(status.value())
					.timestamp(LocalDateTime.now())
					.userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL)
					.build();
		}
		
		
		return super.handleExceptionInternal(ex, body, headers, status, request);
	}
	
	/**
	 * Para reutilização de código referente à extração dos nomes das propriedades.
	 */
	private String joinPath(List<Reference> references) {
		return references.stream()
				.map(ref -> ref.getFieldName())
				.collect(Collectors.joining("."));
	}
	
	/**
	 * Para facilitar a criação de um Problem, especificando melhor o builder.
	 */
	private Problem.ProblemBuilder createProblemBuilder(HttpStatus status,
			ProblemType problemType, String detail) {
		
		return Problem.builder()
				.status(status.value())
				.type(problemType.getUri())
				.title(problemType.getTitle())
				.detail(detail)
				.timestamp(LocalDateTime.now());
	}
	
}
