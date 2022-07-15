package cn.hyy.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author hyy
 */
@ConfigurationProperties(prefix = "swagger")
public class SwaggerProperties {

    private String basePackage;

    private Boolean enabled = false;

    private String version = "1.0";

    private String title = "接口文档";

    public String getBasePackage() {
        return basePackage;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
