package br.com.ada.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@EnableAutoConfiguration
public class SwaggerConfig {

	private static final String PACKAGE_ENTRYPOINTS = "br.com.ada.controller";
	private static final String TITLE = "Movies Battle API";
	private static final String DESCRIPTION = "Resources Movies Battle API";
	private static final String VERSION = "1.0.0";
	private static final String REGEX = "/.*";

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage(PACKAGE_ENTRYPOINTS)).paths(PathSelectors.regex(REGEX))
				.build().useDefaultResponseMessages(false).directModelSubstitute(Object.class, Void.class)
				.apiInfo(apiInfo());
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title(TITLE).description(DESCRIPTION).version(VERSION).build();
	}
}
