package cn.hyy.common.utils.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.ArrayList;
import java.util.List;

public class  ExcelDataBatchListener extends AnalysisEventListener<ExcelFile> {
    private int max = 1000;
    private List<ExcelFile> rs = new ArrayList<>();

    @Override
    public void invoke(ExcelFile excelFile, AnalysisContext analysisContext) {
        rs.add(excelFile);
        if(rs.size() > max) {
            System.out.println("保存到数据库，然后清除list数据");
            rs.clear();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        if(!rs.isEmpty()) {
            System.out.println("保存到数据库，然后清除list数据");
            rs.clear();
        }
        System.out.println("读取完全部数据");
    }
}
