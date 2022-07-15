package cn.hyy.common.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * 生成pdf的工具类
 * @author hyy
 */
public class PdfUtil {
    private static final Logger logger = LoggerFactory.getLogger(PdfUtil.class);

    /**
     * 基于 flying-saucer-pdf-itext5
     * html 字符串生成pdf
     * @param content 带css样式的富文本内容
     * @param name 文件名称（带pdf后缀）
     * @return 返回生成的pdf本地路径
     */
    public static String htmlStringToPdf(String content, String name) {
        content = content.replace("&nbsp;","&#160;")
                .replace("&ldquo;","\"")
                .replace("&rdquo;","\"");
        String path = System.getProperty("user.dir") + "//" + name;

        try(OutputStream outputStream = new FileOutputStream(path)) {
            ITextRenderer renderer = new ITextRenderer();

            renderer.setDocumentFromString("<html><body style=\"font-family: SimSun\">" + content + "</body></html>");
            //设置字符集(宋体),此处必须与模板中的<body style="font-family: SimSun">一致,区分大小写,不能写成汉字"宋体"
            ITextFontResolver fontResolver = renderer.getFontResolver();
            fontResolver.addFont("simsun.ttf", com.lowagie.text.pdf.BaseFont.IDENTITY_H, com.lowagie.text.pdf.BaseFont.NOT_EMBEDDED);
            //展现和输出pdf
            renderer.layout();
            renderer.createPDF(outputStream);
            renderer.finishPDF();
            return path;
        } catch (Exception e) {
            logger.error("生成pdf发生异常",e);
            return null;
        }
    }

}
