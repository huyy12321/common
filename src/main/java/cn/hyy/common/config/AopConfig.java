package cn.hyy.common.config;

import cn.hutool.core.util.StrUtil;
import cn.hyy.common.exception.CustomException;
import cn.hyy.common.interceptors.LogInterceptor;
import cn.hyy.common.properties.LogProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author hyy
 */
@Configuration
@ConditionalOnProperty(name = {"common.log.enabled"}, havingValue = "true")
@EnableConfigurationProperties({LogProperties.class})
public class AopConfig {
    private final LogProperties logProperties;
    private static final Logger logger = LoggerFactory.getLogger(AopConfig.class);

    public AopConfig(LogProperties logProperties){
        this.logProperties = logProperties;
        logger.info("-----------日志启用-----------");
    }

    @Bean
    public DefaultPointcutAdvisor defaultPointcutAdvisor2() {
        if(StrUtil.isBlank(logProperties.getBasePackage()))
            throw new CustomException("common.log.base-package 配置不能为空");
        LogInterceptor interceptor = new LogInterceptor(logProperties);
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution(* "+logProperties.getBasePackage()+".*.*(..))");
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor();
        advisor.setPointcut(pointcut);
        advisor.setAdvice(interceptor);
        return advisor;
    }
}
