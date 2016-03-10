# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\adt-bundle-windows-x86_64-20140702\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}


-optimizationpasses 5                    # 指定代码的压缩级别
-dontusemixedcaseclassnames             # 是否使用大小写混合
-dontoptimize                           #优化  不优化输入的类文件
-dontpreverify                          # 混淆时是否做预校验
-verbose                                # 混淆时是否记录日志
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*            # 混淆时所采用的算法
-keepattributes *Annotation*            #保护注解


-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }

-keepclassmembers class * extends android.app.Activity {

public void *(android.view.View);

}


-keep class * implements android.os.Parcelable {

public static final android.os.Parcelable$Creator *;

}


-dontwarn android.support.**

###############R.java文件不被混淆#################
-keepclassmembers class **.R$* {

public static ;

}

###############关闭系统日志########################
-assumenosideeffects class android.util.Log {

public static boolean isLoggable(java.lang.String,int);

public static int v(...);

public static int i(...);

public static int w(...);

public static int d(...);

public static int e(...);

}




# 保持 native 方法不被混淆
-keepclasseswithmembernames class * {
    native <methods>;
}

   #保护指定的类和类的成员，但条件是所有指定的类和类成员是要存在
   -keepclasseswithmembers class * {
       public <init>(android.content.Context, android.util.AttributeSet);
   }


   -keepclasseswithmembers class * {
       public <init>(android.content.Context, android.util.AttributeSet, int);
   }

   #保护指定类的成员，如果此类受到保护他们会保护的更好
   -keepclassmembers class * extends android.app.Activity {
      public void *(android.view.View);
   }

   -keepclassmembers enum * {
       public static **[] values();
       public static ** valueOf(java.lang.String);
   }

   #保护指定的类文件和类成员
   -keep class * implements android.os.Parcelable {
     public static final android.os.Parcelable$Creator *;
   }

    #忽略警告
    -ignorewarning
    #apk 包内所有 class 的内部结构
    -dump class_files.txt
    #未混淆的类和成员
    -printseeds seeds.txt
    #列出从 apk 中删除的代码
    -printusage unused.txt
    #混淆前后的映射
    -printmapping mapping.txt


    -dontwarn butterknife.internal.**
    -keep class **$$ViewInjector { *; }
    -keepnames class * { @butterknife.InjectView *;}

    -keepclassmembers class **.R$* {
        public static <fields>;
    }

    -keep class org.json.**{*;}


    # volley混淆
    # # -------------------------------------------
    # #  ############### volley混淆  ###############
    # # -------------------------------------------
    -keep class com.android.volley.** {*;}
    -keep class com.android.volley.toolbox.** {*;}
    -keep class com.android.volley.Response$* { *; }
    -keep class com.android.volley.Request$* { *; }
    -keep class com.android.volley.RequestQueue$* { *; }
    -keep class com.android.volley.toolbox.HurlStack$* { *; }
    -keep class com.android.volley.toolbox.ImageLoader$* { *; }

    -keep class org.apache.http.**{*;}


    # Gson混淆
    ## ----------------------------------
    ##   ########## Gson混淆    ##########
    ## ----------------------------------
    -keepattributes Signature
    -keep class sun.misc.Unsafe { *; }
    -keep class com.google.gson.examples.android.model.** { *; }


    -keepclassmembers class ** {
        public void onClick(**); #所有监听的方法都要列在这里
    }

    #==================gson==========================
    -dontwarn com.google.**
    -keep class com.google.gson.** {*;}

    #==================protobuf======================
    -dontwarn com.google.**
    -keep class com.google.protobuf.** {*;}











