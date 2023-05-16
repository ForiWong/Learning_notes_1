package com.wlp.myanjunote.kotlin.generics10;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wlp on 2023/4/27
 * Description:
 * 编译Erase.Java、反编译
 */
public class Erase {

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("123");
        String out = list.get(0);

        List<Integer> list2 = new ArrayList<>();
        list2.add(22);
        list2.get(0);

        Class clazz1 = list.getClass();
        Class clazz2 = list2.getClass();
    }
}

/**
//编译Java文件 javac
C:\WINDOWS\system32>Javac Erase.java

//反编译class文件 javap -c
C:\WINDOWS\system32>Javap -c Erase.class

Compiled from "Erase.java"
public class com.wlp.myanjunote.kotlin.generics10.Erase {
  public com.wlp.myanjunote.kotlin.generics10.Erase();
    Code:
       0: aload_0
       1: invokespecial #1                  // Method java/lang/Object."<init>":()V
       4: return

  public static void main(java.lang.String[]);
    Code:
       0: new           #2                  // class java/util/ArrayList
       3: dup
       4: invokespecial #3                  // Method java/util/ArrayList."<init>":()V
       7: astore_1
       8: aload_1
       9: ldc           #4                  // String 123
      11: invokeinterface #5,  2            // InterfaceMethod java/util/List.add:(Ljava/lang/Object;)Z
      16: pop
      17: aload_1
      18: iconst_0
      19: invokeinterface #6,  2            // InterfaceMethod java/util/List.get:(I)Ljava/lang/Object;
      24: checkcast     #7                  // class java/lang/String
      27: astore_2


      28: new           #2                  // class java/util/ArrayList
      31: dup
      32: invokespecial #3                  // Method java/util/ArrayList."<init>":()V
      35: astore_3
      36: aload_3
      37: bipush        22
      39: invokestatic  #8                  // Method java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
      42: invokeinterface #5,  2            // InterfaceMethod java/util/List.add:(Ljava/lang/Object;)Z
      47: pop
      48: aload_3
      49: iconst_0
      50: invokeinterface #6,  2            // InterfaceMethod java/util/List.get:(I)Ljava/lang/Object;
      55: pop


      56: aload_1
      57: invokevirtual #9                  // Method java/lang/Object.getClass:()Ljava/lang/Class;
      60: astore        4
      62: aload_3
      63: invokevirtual #9                  // Method java/lang/Object.getClass:()Ljava/lang/Class;
      66: astore        5
      68: return
}

//查看更加详细的字节码信息：javap –verbose
C:\WINDOWS\system32>Javap -verbose Erase.class
Classfile /E:/upgithub/MyAnjuNote/app/src/main/java/com/wlp/myanjunote/kotlin/generics10/Erase.class
  Last modified 2023-4-27; size 664 bytes
  MD5 checksum 63e90159862f5c887b7735d722b77a5e
  Compiled from "Erase.java"
public class com.wlp.myanjunote.kotlin.generics10.Erase
  minor version: 0
  major version: 52
  flags: ACC_PUBLIC, ACC_SUPER
Constant pool:
   #1 = Methodref          #11.#20        // java/lang/Object."<init>":()V
   #2 = Class              #21            // java/util/ArrayList
   #3 = Methodref          #2.#20         // java/util/ArrayList."<init>":()V
   #4 = String             #22            // 123
   #5 = InterfaceMethodref #23.#24        // java/util/List.add:(Ljava/lang/Object;)Z
   #6 = InterfaceMethodref #23.#25        // java/util/List.get:(I)Ljava/lang/Object;
   #7 = Class              #26            // java/lang/String
   #8 = Methodref          #27.#28        // java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
   #9 = Methodref          #11.#29        // java/lang/Object.getClass:()Ljava/lang/Class;
  #10 = Class              #30            // com/wlp/myanjunote/kotlin/generics10/Erase
  #11 = Class              #31            // java/lang/Object
  #12 = Utf8               <init>
  #13 = Utf8               ()V
  #14 = Utf8               Code
  #15 = Utf8               LineNumberTable
  #16 = Utf8               main
  #17 = Utf8               ([Ljava/lang/String;)V
  #18 = Utf8               SourceFile
  #19 = Utf8               Erase.java
  #20 = NameAndType        #12:#13        // "<init>":()V
  #21 = Utf8               java/util/ArrayList
  #22 = Utf8               123
  #23 = Class              #32            // java/util/List
  #24 = NameAndType        #33:#34        // add:(Ljava/lang/Object;)Z
  #25 = NameAndType        #35:#36        // get:(I)Ljava/lang/Object;
  #26 = Utf8               java/lang/String
  #27 = Class              #37            // java/lang/Integer
  #28 = NameAndType        #38:#39        // valueOf:(I)Ljava/lang/Integer;
  #29 = NameAndType        #40:#41        // getClass:()Ljava/lang/Class;
  #30 = Utf8               com/wlp/myanjunote/kotlin/generics10/Erase
  #31 = Utf8               java/lang/Object
  #32 = Utf8               java/util/List
  #33 = Utf8               add
  #34 = Utf8               (Ljava/lang/Object;)Z
  #35 = Utf8               get
  #36 = Utf8               (I)Ljava/lang/Object;
  #37 = Utf8               java/lang/Integer
  #38 = Utf8               valueOf
  #39 = Utf8               (I)Ljava/lang/Integer;
  #40 = Utf8               getClass
  #41 = Utf8               ()Ljava/lang/Class;
{
  public com.wlp.myanjunote.kotlin.generics10.Erase();
    descriptor: ()V
    flags: ACC_PUBLIC
    Code:
      stack=1, locals=1, args_size=1
         0: aload_0
         1: invokespecial #1                  // Method java/lang/Object."<init>":()V
         4: return
      LineNumberTable:
        line 10: 0

  public static void main(java.lang.String[]);
    descriptor: ([Ljava/lang/String;)V
    flags: ACC_PUBLIC, ACC_STATIC
    Code:
      stack=2, locals=6, args_size=1
         0: new           #2                  // class java/util/ArrayList
         3: dup
         4: invokespecial #3                  // Method java/util/ArrayList."<init>":()V
         7: astore_1
         8: aload_1
         9: ldc           #4                  // String 123
        11: invokeinterface #5,  2            // InterfaceMethod java/util/List.add:(Ljava/lang/Object;)Z
        16: pop
        17: aload_1
        18: iconst_0
        19: invokeinterface #6,  2            // InterfaceMethod java/util/List.get:(I)Ljava/lang/Object;
        24: checkcast     #7                  // class java/lang/String
        27: astore_2
        28: new           #2                  // class java/util/ArrayList
        31: dup
        32: invokespecial #3                  // Method java/util/ArrayList."<init>":()V
        35: astore_3
        36: aload_3
        37: bipush        22
        39: invokestatic  #8                  // Method java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        42: invokeinterface #5,  2            // InterfaceMethod java/util/List.add:(Ljava/lang/Object;)Z
        47: pop
        48: aload_3
        49: iconst_0
        50: invokeinterface #6,  2            // InterfaceMethod java/util/List.get:(I)Ljava/lang/Object;
        55: pop
        56: aload_1
        57: invokevirtual #9                  // Method java/lang/Object.getClass:()Ljava/lang/Class;
        60: astore        4
        62: aload_3
        63: invokevirtual #9                  // Method java/lang/Object.getClass:()Ljava/lang/Class;
        66: astore        5
        68: return
      LineNumberTable:
        line 13: 0
        line 14: 8
        line 15: 17
        line 17: 28
        line 18: 36
        line 19: 48
        line 21: 56
        line 22: 62
        line 24: 68
}
SourceFile: "Erase.java"

//查看class文件 signature：javap -s
C:\WINDOWS\system32>Javap -s Erase.class
Compiled from "Erase.java"
public class com.wlp.myanjunote.kotlin.generics10.Erase {
  public com.wlp.myanjunote.kotlin.generics10.Erase();
    descriptor: ()V

  public static void main(java.lang.String[]);
    descriptor: ([Ljava/lang/String;)V
}

//直接AS查看class文件
public class Erase {
    public Erase() {
    }

    public static void main(String[] var0) {
        ArrayList var1 = new ArrayList();//擦除了<String>
        var1.add("123");
        String var2 = (String)var1.get(0);//直接强转，因为编译时通过了校验

        ArrayList var3 = new ArrayList();
        var3.add(22);
        var3.get(0);

        Class var4 = var1.getClass();
        Class var5 = var3.getClass();
    }
}
*/
