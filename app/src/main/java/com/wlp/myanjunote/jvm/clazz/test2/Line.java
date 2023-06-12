package com.wlp.myanjunote.jvm.clazz.test2;

public class Line {

    //forName()执行
	static {  
        System.out.println("静态代码块执行：loading line");  
    }  
	
	public static String s = getString();

    //forName()执行
    private static String getString() {  
        System.out.println("给静态变量赋值的静态方法执行：loading line");  
        return "ss";  
    }  
  
    public static void test() {  
        System.out.println("普通静态方法执行：loading line");  
    }  
  
    {  
        System.out.println("要是普通的代码块呢？");  
    }  
  
    public Line() {  
        System.out.println("构造方法执行");  
    }  
}