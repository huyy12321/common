package cn.hyy.common.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author hyy
 */
@ConfigurationProperties(prefix = "common.log")
public class LogProperties {

    /**
     * 是否开启日志功能
     */
    private Boolean enabled = false;

    /**
     * 日志扫描的包路径
     */
    private String basePackage;

    /**
     * 超时时间，超过则纪录成慢接口,单位ms
     */
    private long overtime = 10 * 1000;

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getBasePackage() {
        return basePackage;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    public long getOvertime() {
        return overtime;
    }

    public void setOvertime(long overtime) {
        this.overtime = overtime;
    }
}
