package com.wlp.myanjunote.jvm.clazz;

import com.wlp.myanjunote.jvm.clazz.test2.Line;

public class ReflectTest {
	/**
	 * java中class.forName()和classLoader都可用来对类进行加载。
	 *
	 * 不同：
	 * 1）class.forName()除了将类的.class文件加载到jvm中之外，还会对类进行解释，执行类中的static块，还会
	 * 执行给静态变量赋值的静态方法
	 *
	 *  2）classLoader只干一件事情，就是将.class文件加载到jvm中，不会执行static中的内容,只有在newInstance
	 *  才会去执行static块。
	 *
	 * */
	public static void main(String[] args) {
		String wholeNameLine = "com.wlp.myanjunote.jvm.clazz.test1.Line";
		String wholeNamePoint = "com.wlp.myanjunote.jvm.clazz.test1.Point";
		System.out.println("下面是测试Classloader的效果");
		testClassloader(wholeNameLine, wholeNamePoint);
		
		System.out.println("@----------------------------------");
		System.out.println("下面是测试Class.forName的效果");
		testForName(wholeNameLine, wholeNamePoint);

		System.out.println("@@----------------------------------");
		String wholeNameLine2 = "com.wlp.myanjunote.jvm.clazz.test2.Line";
		/**
		 * 静态代码块执行：loading line
		 * 给静态变量赋值的静态方法执行：loading line
		 * line   com.wlp.myanjunote.jvm.clazz.test2.Line
		 * 要是普通的代码块呢？
		 * 构造方法执行
		 */
		try {
			Class<?> line = Class.forName(wholeNameLine2);
			System.out.println("line   " + line.getName());
			line.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}

		/**
		 * 没有打印
		 */
		//Line line = null;

		//Class clazz = Line.class;

		/**
		 * 静态代码块执行：loading line
		 * 给静态变量赋值的静态方法执行：loading line
		 * 要是普通的代码块呢？
		 * 构造方法执行
		 */
		//Line line = new Line();

	}
 
	private static void testClassloader(String wholeNameLine, String wholeNamePoint) {
		Class<?> line;
		Class<?> point;
		ClassLoader classLoader = ClassLoader.getSystemClassLoader();
		try {
			line = classLoader.loadClass(wholeNameLine);
			point = classLoader.loadClass(wholeNamePoint);
 
			System.out.println("line   " + line.getName());
			System.out.println("point   " + point.getName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void testForName(String wholeNameLine, String wholeNamePoint) {
		try {
			Class<?> line = Class.forName(wholeNameLine);
			Class<?> point = Class.forName(wholeNamePoint);
			System.out.println("line   " + line.getName());  
            System.out.println("point   " + point.getName());  
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}