package cn.hyy.common.exception;

import cn.hyy.common.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @author hyy
 */
@ControllerAdvice
@ResponseBody
public class ExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(ExceptionHandler.class);

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public Object handleException(HttpServletRequest request, Exception e) {
        if (e instanceof CustomException) {
            log.info("自定义异常: ", e);
            CustomException ex = (CustomException) e;
            return Result.error(ex.getMsg(),ex.getCode());
        } else if(e instanceof MethodArgumentNotValidException){
            MethodArgumentNotValidException ex = (MethodArgumentNotValidException) e;
            BindingResult bindingResult = ex.getBindingResult();
            String field = Objects.requireNonNull(bindingResult.getFieldError()).getField();
            String error = field + ": " + bindingResult.getFieldError().getDefaultMessage();
            log.error(request.getServletPath() + " - 参数异常：{}", error);
            return Result.error(error);
        } else if (e instanceof BindException) {
            BindingResult bindingResult = Objects.requireNonNull(((BindException) e).getBindingResult());
            String field = Objects.requireNonNull(bindingResult.getFieldError()).getField();
            String error = field + ": " + bindingResult.getFieldError().getDefaultMessage();
            log.error(request.getServletPath() + " - 参数异常：", error);
            return Result.error(error);
        } else if (e instanceof MissingServletRequestParameterException) {
            log.error(request.getServletPath() + " - 参数异常：", e);
            return Result.error(e.getMessage());
        } else if (e instanceof MethodArgumentTypeMismatchException) {
            String name = ((MethodArgumentTypeMismatchException) e).getName();
            String error = name + " 非法参数";
            log.error(error);
            return Result.error(error);
        } else if (e instanceof MaxUploadSizeExceededException) {
            log.error(request.getServletPath() + " - 上传文件过大：", e);
            return Result.error("上传文件过大");
        } else if (e instanceof HttpMessageNotReadableException) {
            return Result.error("反序列化出现异常，请检查输入的值是否正常");
        } else {
            log.error(request.getServletPath() + " - 未知异常：", e);
            return Result.error("未知异常");
        }
    }
}
