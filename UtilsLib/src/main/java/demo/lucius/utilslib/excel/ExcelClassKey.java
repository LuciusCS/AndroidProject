package demo.lucius.utilslib.excel;

public class ExcelClassKey {

    //用于标注列名
    private String title;
    //方法中的变量名
    private String fieldName;

    public ExcelClassKey(String title, String fieldName) {
        this.title = title;
        this.fieldName = fieldName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
}
