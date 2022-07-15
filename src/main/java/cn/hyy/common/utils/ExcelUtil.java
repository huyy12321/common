package cn.hyy.common.utils;


import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;

import cn.hutool.poi.excel.ExcelWriter;
import cn.hutool.poi.excel.StyleSet;

import cn.hyy.common.exception.CustomException;
import io.swagger.annotations.ApiModelProperty;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.ss.util.SheetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author hyy
 */
public class ExcelUtil {
    private static final Logger log = LoggerFactory.getLogger(ExcelUtil.class);

    /**
     * 导出excel  通过在导出类的属性上加ApiModelProperty注解来设置表头
     *
     * @param fileName 导出文件名
     * @param c        返回的对象
     * @param list     列表数据
     * @param response 响应流
     */
    public static <T> void export(String fileName, Class<T> c, List<T> list, HttpServletResponse response) {
        try (ExcelWriter writer = cn.hutool.poi.excel.ExcelUtil.getWriter();
             ServletOutputStream out = response.getOutputStream()) {
            Map<String, String> map = MapUtil.newHashMap(true);
            Field[] fields = c.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                ApiModelProperty annotation = fields[i].getAnnotation(ApiModelProperty.class);
                if (annotation != null) {
                    map.put(fields[i].getName(), annotation.value());
                    if (StrUtil.isNotBlank(annotation.name())) {
                        // 设置下拉框数据,导出的表格需要回传回来，控制输入内容
                        CellRangeAddressList addressList = new CellRangeAddressList(1, list.size(), i, i);
                        DataValidationHelper helper = writer.getSheet().getDataValidationHelper();
                        String[] str = annotation.name().split(",");
                        DataValidationConstraint constraint = helper.createExplicitListConstraint(str);
                        DataValidation dataValidation = helper.createValidation(constraint, addressList);
                        writer.addValidationData(dataValidation);
                    }
                }
            }
            // 设置表头
            writer.setHeaderAlias(map);

            writer.write(list, true);
            // 自动设置列宽
            StyleSet style = writer.getStyleSet();
            Font font = writer.createFont();
            font.setBold(true);
            font.setFontHeightInPoints((short) 12);
            // 设置中文字体
            font.setFontName("宋体");
            style.getHeadCellStyle().setFont(font);
            int columnCount = writer.getColumnCount();
            for (int i = 0; i < columnCount; ++i) {
                double width = SheetUtil.getColumnWidth(writer.getSheet(), i, false,0,0);
                if (width != -1.0D) {
                    width *= 256.0D;
                    // 此处可以适当调整，调整列空白处宽度
                    width += 220D;
                    writer.setColumnWidth(i, Math.toIntExact(Math.round(width / 256D)));
                }
            }

            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            fileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            writer.flush(out);
        } catch (Exception e) {
            log.error("导出excel发生异常", e);
        }
    }


    /**
     * 导入excel
     *
     * @param file 文件
     * @param c    映射对象
     */

    public static <T> List<T> importExcel(MultipartFile file, Class<T> c) throws CustomException {
        try (InputStream inputStream = file.getInputStream();
             ExcelReader reader = cn.hutool.poi.excel.ExcelUtil.getReader(inputStream)) {
            Field[] fields = c.getDeclaredFields();
            HashMap<String, String> map = new HashMap<>();
            for (Field field : fields) {
                ApiModelProperty annotation = field.getAnnotation(ApiModelProperty.class);
                if (annotation != null) {
                    map.put(annotation.value(), field.getName());
                }
            }
            reader.setHeaderAlias(map);
            return reader.read(0, 1, c);
        } catch (IOException e) {
            log.error("导入excel发生异常", e);
            throw new CustomException("导入excel发生异常");
        }
    }
}
