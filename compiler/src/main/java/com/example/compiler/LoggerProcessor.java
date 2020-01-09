package com.example.compiler;


import com.example.annotation.Logger;
import com.google.auto.service.AutoService;
import com.google.common.collect.ImmutableList;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic;



//通过AutoService 注册自动生成处理的文件
@AutoService(Processor.class)
@SupportedAnnotationTypes("com.example.annotation.Logger")
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class LoggerProcessor extends AbstractProcessor {

    //processor 运行在一个独立的JVM实例中，javac为我们的processor启动一个新的进程，要使我们的processor被javac检测到，
    //需要使用ServiceLoader进行注册

    private static final String KEY_PATH_NAME="args";

    private static final String METHOD_LOG="log";
    private static final String CLASS_SUFFIX="_log";

    private Messager messager;

    private Filer filer;

    /**
     * 用于做一些初始化的内容，工具类
     * @param processingEnvironment
     */
    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        messager=processingEnvironment.getMessager();
        filer=processingEnvironment.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        //获取所有被Logger注解的元素
        Collection<? extends Element>annotatedElements=roundEnvironment.getElementsAnnotatedWith(Logger.class);

        //删除我们不需要的元素
        List<TypeElement>typeElements=new ImmutableList.Builder<TypeElement>().addAll(ElementFilter.typesIn(annotatedElements)).build();


        for (TypeElement typeElement:typeElements){
            //interface也可能是type类型，只筛选出我们需要的class
            System.out.println("++++");
            if (isValidClass(typeElement)){
                System.out.println("++++");
                writeSourceFile(typeElement);
            };


        }

        return false;
    }



    private boolean isValidClass(TypeElement typeElement){
        if (typeElement.getKind()!= ElementKind.CLASS){
            messager.printMessage(Diagnostic.Kind.ERROR,typeElement.getSimpleName()+"只有class可以使用log注解");
            return false;
        }

        if (typeElement.getModifiers().contains(Modifier.PRIVATE))
        {
            messager.printMessage(Diagnostic.Kind.ERROR,typeElement.getSimpleName()+"只有public类型的类可以使用log");
        }


        return true;
    }


    //用于写入源文件
    private void writeSourceFile(TypeElement originatingType){

        //从android.util.package中获取log类
        //可以保证Log类被正确引入我们的生成类中
        ClassName logClassName=ClassName.get("android.util","Log");

        //用于获取当前注解的class名称
        TypeVariableName typeVariableName=TypeVariableName.get(originatingType.getSimpleName().toString());


        //创建一个名称为log的静态方法
        MethodSpec log=MethodSpec.methodBuilder(METHOD_LOG)
                .addModifiers(Modifier.PUBLIC,Modifier.STATIC)
                .returns(void.class)
                //参数变量由注解的类决定
                .addParameter(typeVariableName,KEY_PATH_NAME)
                //创建一个 Log.d("className",String.format(class fields))
                .addStatement("$T.d($S,$L)",logClassName,originatingType.getSimpleName().toString(),generateFormater(originatingType))
                .build();


        //创建一个class，创建的class的名字为： 被注解的class名+_Log

        TypeSpec loggerClass=TypeSpec.classBuilder(originatingType.getSimpleName().toString()+CLASS_SUFFIX)
                .addModifiers(Modifier.PUBLIC,Modifier.FINAL)
                //天啊及log方法
                .addMethod(log).build();

        //创建文件
        JavaFile javaFile=JavaFile.builder(originatingType.getEnclosingElement().toString(),loggerClass).build();

        try {
            javaFile.writeTo(filer);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * 注释掉的内容使用注解来实现
     * @param originatingType
     * @return
     */

//    /**
//     * 获取支持类的注解
//     * @return
//     */
//    @Override
//    public Set<String> getSupportedAnnotationTypes() {
//        return super.getSupportedAnnotationTypes();
//    }
//
//    /**
//     * 通过某一JDK的版本进行编译，必填
//     * @return
//     */
//    @Override
//    public SourceVersion getSupportedSourceVersion() {
//        return super.getSupportedSourceVersion();
//    }
//
//    /**
//     * 接受外部传来的属性
//     * @return
//     */
//    @Override
//    public Set<String> getSupportedOptions() {
//        return super.getSupportedOptions();
//    }

    private String generateFormater(TypeElement originatingType){

        List<VariableElement>fields=new ImmutableList.Builder<VariableElement>().addAll(ElementFilter.fieldsIn(originatingType.getEnclosedElements())).build();


        String sFormat="String.format(\"";

        for (VariableElement e:fields){
            sFormat+=e.getSimpleName()+" - %s";
        }

        sFormat+="\"";


        for (VariableElement f:fields){
            sFormat+=", "+KEY_PATH_NAME+" ."+f.getSimpleName();
        }

        sFormat+=")";

        return sFormat;


    }

}
