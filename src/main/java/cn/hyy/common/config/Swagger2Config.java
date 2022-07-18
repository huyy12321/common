package cn.hyy.common.config;

import cn.hyy.common.properties.SwaggerProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author hyy
 */
@EnableSwagger2
@ConditionalOnProperty(name = {"common.swagger.enabled"}, havingValue = "true")
@EnableConfigurationProperties({SwaggerProperties.class})
public class Swagger2Config {
    private final SwaggerProperties swaggerProperties;
    private static final Logger logger = LoggerFactory.getLogger(Swagger2Config.class);

    public Swagger2Config(SwaggerProperties swaggerProperties) {
        logger.info("-----------swagger启用-----------");
        this.swaggerProperties = swaggerProperties;
    }

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder().title(swaggerProperties.getTitle()).version(swaggerProperties.getVersion()).build())
                .select()
                .apis(RequestHandlerSelectors.basePackage(swaggerProperties.getBasePackage()))
                .paths(PathSelectors.any())
                .build();
    }
}
