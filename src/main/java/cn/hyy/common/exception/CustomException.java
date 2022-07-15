package cn.hyy.common.exception;

/**
 * @author hyy
 */
public class CustomException extends RuntimeException {
    private Integer code;
    private String msg;

    public CustomException(Integer code,String msg) {
        this.code = code;
        this.msg = msg;
    }
    public CustomException(String msg) {
        this.code = 500;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
