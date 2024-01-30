package com.algaworks.algafood.core.validation;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * Personaliza a fonte de mensagens para a fábrica de validadores.
 */
@Configuration
public class ValidationConfig {

	@Bean
	public LocalValidatorFactoryBean validator(MessageSource messageSource) {
		LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
		// resource bundle do Spring (message.properties) se torna o recurso padrão
		bean.setValidationMessageSource(messageSource);
		return bean;
	}
	
}
