package cn.hyy.common.utils;

import com.github.junrar.Junrar;
import com.github.junrar.exception.RarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * 解压文件工具类
 * @author huyangyang
 */
public class UnFileUtils {
    private static final Logger log = LoggerFactory.getLogger(UnFileUtils.class);
    private static final String ZIP = "zip";
    private static final String RAR = "rar";

    /**
     * 解压文件
     * @param sourcePath 源文件路径
     * @param targetPath 目标路径
     */
    public static void unFile(String sourcePath, String targetPath) throws Exception{
        log.info("开启处理压缩文件,文件路径={}",sourcePath);
        if(sourcePath.toLowerCase().contains(ZIP)){
            unzipFile(sourcePath,targetPath);
        } else if(sourcePath.toLowerCase().contains(RAR)) {
            unRarFile(sourcePath,targetPath + "/");
        }
        log.info("压缩文件已经解压,解压路径={}",targetPath);
    }

    public static void unzipFile(String sourcePath, String targetPath) throws Exception{
        File file = new File(sourcePath);
        if (!file.exists()) {
            throw new Exception("源目标路径：[" + sourcePath + "] 不存在...");
        }
        if(file.getName().endsWith(ZIP)){
            // 开始解压
            ZipFile zipFile = null;
            try {
                zipFile = new ZipFile(file, StandardCharsets.UTF_8);
                Enumeration<?> entries = zipFile.entries();
                while (entries.hasMoreElements()) {
                    ZipEntry entry = (ZipEntry) entries.nextElement();
                    // 如果是文件夹，就创建个文件夹
                    if (entry.isDirectory()) {
                        String dirPath = targetPath + "/" + entry.getName();
                        File dir = new File(dirPath);
                        dir.mkdirs();
                    } else {
                        // 如果是文件，就先创建一个文件，然后用io流把内容copy过去
                        File targetFile = new File(targetPath + "/" + entry.getName());
                        // 保证这个文件的父文件夹必须要存在
                        if (!targetFile.getParentFile().exists()) {
                            targetFile.getParentFile().mkdirs();
                        }
                        targetFile.createNewFile();
                        // 将压缩文件内容写入到这个文件中
                        InputStream is = zipFile.getInputStream(entry);
                        FileOutputStream fos = new FileOutputStream(targetFile);
                        int len;
                        int BUFFER_SIZE = 1024;
                        byte[] buf = new byte[BUFFER_SIZE];
                        while ((len = is.read(buf)) != -1) {
                            fos.write(buf, 0, len);
                        }
                        // 关流顺序，先打开的后关闭
                        fos.close();
                        is.close();
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException("unzip error from ZipUtils", e);
            } finally {
                if (zipFile != null) {
                    try {
                        zipFile.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    /**
     * rar5以下的能用工具解压，rar5及以上的用命令解压
     * 解压rar格式文件，先使用工具类，不行的话使用命令行
     */
    public static void unRarFile(String sourcePath, String targetPath) {
        try {
            File file = new File(targetPath);
            if(!file.exists()) file.mkdirs();
            Junrar.extract(sourcePath, targetPath);
        } catch (IOException e) {
            log.error("解压rar文件出现异常",e);
        } catch (RarException e) {
            log.info("工具解压失败，尝试命令解压",e);
            unRarByCommand(sourcePath,targetPath);
        }
    }

    /**
     * 工具下载地址：https://www.rarlab.com/download.htm
     * 通过命令解压rar文件，需要先在服务器安装winrar解压工具
     */
    public static void unRarByCommand(String sourcePath, String target) {
        File sourceFile = new File(sourcePath);
        if(!sourceFile.exists()){
            return;
        }
        // 此处地址为工具解压后的地址
        String cmd = "/opt/datafile/rar/rar" + " X " + sourceFile.getAbsolutePath() + " "+target;
        try {
            log.info("执行命令：{}",cmd);
            Process proc = Runtime.getRuntime().exec(cmd);
            proc.waitFor();
            proc.destroy();
        } catch (Exception e) {
            log.error("命令解压rar文件出现异常",e);
        }
    }
}
