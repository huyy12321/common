package cn.hyy.common.interceptors;

import cn.hutool.json.JSONUtil;
import cn.hyy.common.properties.LogProperties;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;

import java.util.Arrays;
import java.util.HashMap;

/**
 * @author hyy
 */
public class LogInterceptor implements MethodInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(LogInterceptor.class);
    private static HashMap<String,Integer> OVERTIME = new HashMap<>(16);
    private final LogProperties logProperties;
    public LogInterceptor(LogProperties logProperties){
        this.logProperties = logProperties;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        String name = invocation.getMethod().toGenericString();
        name = name.substring(name.lastIndexOf(" "),name.lastIndexOf("("));
        Object[] arguments = invocation.getArguments();
        logger.info("日志开始--->方法名={},参数={}",name, Arrays.toString(arguments));
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Object proceed = invocation.proceed();
        String rs = "";
        if(proceed != null) rs = JSONUtil.toJsonStr(proceed);
        if(rs.length() > 50) rs = rs.substring(0,50);
        stopWatch.stop();
        long lastTaskTimeMillis = stopWatch.getLastTaskTimeMillis();
        if(lastTaskTimeMillis > logProperties.getOvertime()) {
            Integer integer = OVERTIME.get(name);
            if(integer == null) {
                integer = 1;
            } else{
                integer += 1;
            }
            OVERTIME.put(name,integer);
            logger.warn("慢接口警告----->耗时={}ms,次数={},方法名={}",lastTaskTimeMillis,integer,name);
        }
        logger.info("日志结束--->方法名={},运行时间={}ms,返回结果={}",name,lastTaskTimeMillis,rs);
        return proceed;
    }

    public static void clear() {
        OVERTIME = new HashMap<>(16);
    }
}
