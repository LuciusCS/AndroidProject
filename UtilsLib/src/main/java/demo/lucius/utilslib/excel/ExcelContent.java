package demo.lucius.utilslib.excel;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelContent {
    String titleName();
}