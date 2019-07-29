package demo.lucius.utilslib.excel;

import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class ExcelManager {

    private Map<String,Field>fieldCache=new HashMap<>();

    public boolean exportExcel(OutputStream outputStream, List<List<?>> datalists) throws Exception {

        WritableWorkbook workbook= Workbook.createWorkbook(outputStream);

        for (int i=datalists.size()-1;i>=0;i--){
            List<?>datalist=datalists.get(i);
            Class<?>dataType=datalist.get(0).getClass();
            String sheetName=getSheetName(dataType);
            List<ExcelClassKey>keys=getKeys(dataType);

            WritableSheet sheet=workbook.createSheet(sheetName,i);

            //用于填入列名
            for (int j=keys.size()-1;j>=0;j--){
                sheet.addCell(new Label(j,0,keys.get(j).getTitle()));
            }
            fieldCache.clear();
            //在填充的时候按照一横行，从上向下进行填充
            for (int k=datalist.size()-1;k>=0;k--){
                for (int j=keys.size()-1;j>=0;j--){
                    //用于获取变量名
                    String fieldName = keys.get(j).getFieldName();
                    //用于获取变量的值
                    Field field = getField(dataType, fieldName);
                    //用于获取list中的第k个项的值
                    Object value = field.get(datalist.get(k));
                    String content = value != null ? value.toString() : "";
                    //j 表示列，k+1表示行
                    sheet.addCell(new Label(j, k + 1, content));
                }
            }

        }
        workbook.write();
        workbook.close();
        outputStream.close();

        return true;
    }


    //用于获取列名
    private List<ExcelClassKey>getKeys(Class<?>clazz){
        //获取成员变量
        Field[] fields=clazz.getDeclaredFields();
        List<ExcelClassKey>keys=new ArrayList<>();
        for (int i=fields.length-1;i>=0;i--){
            //用于获取成员变量上方的注解的内容(即列名)
            ExcelContent content=fields[i].getAnnotation(ExcelContent.class);
            if (content!=null){
                keys.add(new ExcelClassKey(content.titleName(),fields[i].getName()));
            }
        }
        return keys;
    }


    //用于获取变量的值
    private Field getField(Class<?>type,String fieldName)throws Exception{
        Field f=null;
        if (fieldCache.containsKey(fieldName)){
            f=fieldCache.get(fieldName);
        }else {
            f=type.getDeclaredField(fieldName);
            fieldCache.put(fieldName,f);
        }
        f.setAccessible(true);
        return f;
    }

    //用于获取sheet名称
    private String getSheetName(Class<?>clazz){
        ExcelSheet sheet=clazz.getAnnotation(ExcelSheet.class);
        if (sheet==null){
            throw new RuntimeException(clazz.getSimpleName()+":sheet名不存在");
        }
        String sheetName=sheet.sheetName();
        return sheetName;

    }
}
