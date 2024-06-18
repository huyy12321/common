package cn.hyy.common.utils.excel;

import com.alibaba.excel.annotation.ExcelProperty;

public class ExcelFile {
    @ExcelProperty(value = "年龄")
    private String age;
    @ExcelProperty(value = "姓名")
    private String name;

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ExcelFile(String age, String name) {
        this.age = age;
        this.name = name;
    }

    public ExcelFile() {
    }
}
