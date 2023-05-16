1、BlockCanary怎么检测到卡顿的?
所有在主线程运行的代码，其实都是在 Looper.loop()方法的循环中调用UI

BlockCanary.install(this, AppBlockCanaryContext()).start()

在调用start() 时，通过调用主线程的 Looper.setMessageLogging() 方法，为 Looper 的 mLogging 成员变量赋值
Looper.getMainLooper().setMessageLogging(mBlockCanaryCore.monitor);

在 Looper死循环中，println 方法分别会在 dispatchMessage(msg) 之前和之后被调用。

for(;;){
    //....
    Message msg = queue.next();
    
    //.......
    logging.println(">>>Dispatching to " + msg.target + " " + msg.callback + ": " + mag.what);

	msg.target.dispatchMessage(msg)
	
	logging.println ("<<< Finish to " + msg.target + " " + msg.callback);
}

所以，通过自定义 Printer 对象，我们就可以获得 dispatchMessage 的耗时，从而判断出是否有应用卡顿。

    @Override
    public void println(String x) {
        if (mStopWhenDebugging && Debug.isDebuggerConnected()) {
            return;
        }
        if (!mPrintingStarted) {
            mStartTimestamp = System.currentTimeMillis();
            mStartThreadTimestamp = SystemClock.currentThreadTimeMillis();
            mPrintingStarted = true;
            startDump();
        } else {
            final long endTime = System.currentTimeMillis();
            mPrintingStarted = false;
            if (isBlock(endTime)) { //在这里是判断前后的耗时
                notifyBlockEvent(endTime);
            }
            stopDump();
        }
    }

同时 BlockCanary 还会在子线程中执行一个获取主线程堆栈信息的定时任务，这个
任务会在 dispatchMessage 结束的时候被移除。

    @Override
    protected void doSample() {
        StringBuilder stringBuilder = new StringBuilder();

        for (StackTraceElement stackTraceElement : mCurrentThread.getStackTrace()) {
            stringBuilder
                    .append(stackTraceElement.toString())
                    .append(BlockInfo.SEPARATOR);
        }

        synchronized (sStackMap) {
            if (sStackMap.size() == mMaxEntryCount && mMaxEntryCount > 0) {
                sStackMap.remove(sStackMap.keySet().iterator().next());
            }
            sStackMap.put(System.currentTimeMillis(), stringBuilder.toString());
        }
    }

BlockCanary的缺陷
依靠定时获取堆栈的方法，定位不够精准。（需要根据提示进一步定位）
println 方法中会拼接字符串对象
还有，就是这个库也是很久没有更新维护了。

2、其他的获取方法运行时间的方法
（1）Hugo
com.jakewharton.hugo:hugo-plugin
//通过注解的方式，获取时间

hugo: 使用太麻烦了，每个方法都要注解，然后打印出对应的时间日志
如果要更换别的库的话，侵入太深，修改麻烦。
比较适合简单的业务场景。

（2）TraceView
TraceView；开销太大了，不是很准确。有可能一个小耗时的方法，结果测出来的时间大很多。

（3）Systrace 【其实，这个方法已经很好了】
Systrace 使用python 终端命令生成 Trace文件
//使用起来也挺麻烦的

android.os.Trace 一个标记类，在framework层

或者在API29之上通过，也可以通过真机手机来生成trace文件

（4）自定义 Plugin
重点：在 transform 方法中，遍历文件夹
然后通过ASM对class文件进行处理。

这种方式，直接使用腾讯的开源库 tencent.matrix-trace-canary
TraceCanary分为帧率监控、慢方法监控、ANR监控、启动耗时、主线程优先级检测、IdleHandler耗时检测这6个功能。

Matrix is an APM (Application Performance Manage) used in Wechat to monitor, locate and analyse performance
problems. It is a plugin style, non-invasive solution and is currently available on iOS, macOS and Android.

Matrix 是一款微信研发并日常使用的应用性能接入框架，支持iOS, macOS和Android。 Matrix 通过接入各种性能监控方案，
对性能监控项的异常数据进行采集和分析，输出相应的问题分析、定位与优化建议，从而帮助开发者开发出更高质量的应用。

Matrix for Android
Matrix-android 当前监控范围包括：应用安装包大小，帧率变化，启动耗时，卡顿，慢方法，SQLite 操作优化，文件读写，
内存泄漏等等。

APK Checker: 针对 APK 安装包的分析检测工具，根据一系列设定好的规则，检测 APK 是否存在特定的问题，并输出较为详细
的检测结果报告，用于分析排查问题以及版本追踪

Resource Canary: 基于 WeakReference 的特性和 Square Haha 库开发的 Activity 泄漏和 Bitmap 重复创建检测工具

Trace Canary: 监控ANR、界面流畅性、启动耗时、页面切换耗时、慢函数及卡顿等问题

SQLite Lint: 按官方最佳实践自动化检测 SQLite 语句的使用质量

IO Canary: 检测文件 IO 问题，包括：文件 IO 监控和 Closeable Leak 监控

Battery Canary: 监控 App 活跃线程（待机状态 & 前台 Loop 监控）、ASM 调用 (WakeLock/Alarm/Gps/Wifi/Bluetooth 
等传感器)、 后台流量 (Wifi/移动网络)等 Battery Historian 统计 App 耗电的数据

MemGuard

检测堆内存访问越界、使用释放后的内存、重复释放等问题
