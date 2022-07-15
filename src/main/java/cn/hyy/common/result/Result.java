package cn.hyy.common.result;

/**
 * @author hyy
 */
public class Result<T> {
    private Integer code;
    private String msg;
    private long total;
    private T data;

    public static <T> Result<T> success(T data) {
        return new Result<>(200,null,0,data);
    }
    public static <T> Result<T> success(T data,long total) {
        return new Result<>(200,null,total,data);
    }

    public static <T> Result<T> error(String msg) {
        return new Result<>(500,msg,0,null);
    }

    public static <T> Result<T> error(String msg,Integer code) {
        return new Result<>(code,msg,0,null);
    }

    private Result(Integer code,String msg,long total,T data) {
        this.code = code;
        this.msg = msg;
        this.total = total;
        this.data = data;
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

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}