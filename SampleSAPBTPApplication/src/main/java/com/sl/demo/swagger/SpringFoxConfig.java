package com.sl.demo.swagger;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@Import(BeanValidatorPluginsConfiguration.class)
public class SpringFoxConfig {

	String appTitle = "DMS Integration";

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select()
				.apis(RequestHandlerSelectors.basePackage(parentPackageName())).paths(PathSelectors.ant("/**")).build();
	}

	private String parentPackageName() {
		String thisPackageName = getClass().getPackage().getName();
		int lastDotPos = thisPackageName.lastIndexOf('.');
		String parentPackageName = thisPackageName.substring(0, lastDotPos);
		return parentPackageName;
	}

	private ApiInfo apiInfo() {
		return new ApiInfo(appTitle + " API", "This is the API for the " + appTitle + " application.", "v1", null,
				new Contact("SL", "", "test@sl.com") , null, null,
				Collections.EMPTY_LIST);
	}
}
