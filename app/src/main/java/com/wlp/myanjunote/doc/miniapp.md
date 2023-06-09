微信小程序技术原理分析-匠心博客
https://zhaomenghuan.js.org/blog/wechat-miniprogram-principle-analysis.html
=---------------------------------------------
一方面微信、支付宝等各家小程序平台遍地开花，另一方面移动开发插件化技术逐渐没落，移动应用构建的方式在悄悄的发生变化。  
对于企业应用形态而言，也在逐步发生变化，超级 APP（移动门户）+ 轻应用是一种新的流行趋势。微信、支付宝是互联网生态下的  
“移动门户”，手机银行是金融典型的 ToC “移动门户”。

小程序方式构建应用是大趋势，被越来越多的企业用户看到其中的优势，构建一个跨多端平台的小程序开发平台是一种思路，帮助企业  
用户构建一个具备小程序能力的“移动门户”也是一种思路。本文主要调研微信小程序运行时的基本原理，从而构建一个适合我们自己  
平台的小程序运行框架。

双线程模型
小程序的渲染层和逻辑层分别由两个线程管理：渲染层的界面使用 WebView 进行渲染；逻辑层采用 JSCore 运行 JavaScript 代码。一个  
小程序存在多个界面，所以渲染层存在多个 WebView。这两个线程间的通信经由小程序 Native 侧中转，逻辑层发送网络请求也经由 Native  
侧转发，小程序的通信模型下图所示。

小程序的双层架构思想可以追溯到 PWA，但又有所扬弃。

PWA	小程序框架
逻辑层	以 Service Worker 为载体。开发者需编写业务逻辑、管理资源缓存。	以 JSCore 或 V8 引擎为载体。开发者只需编写业务逻辑。
渲染层	基于 Web 网页的单页或多标签页方案。	基于多个 WebView 组成的页面栈。

小程序框架与 PWA 相比，小程序的开发者可以更聚焦于业务逻辑，而无需关注静态资源的缓存。  
小程序包的缓存和更新机制交由小程序框架自动完成，开发者可以在适当时机通过 API 影响这一过程。  
小程序的渲染层由多个 WebView 组成的页面栈构成，这与 PWA 相比有着更接近移动端原生应用的用户体验。  
同时，小程序的开发者也能更从容地处理多页面间跳转时页面状态的变化。

类似于微信 JSSDK 这样的 Hybrid 技术，微信小程序的界面主要由成熟的 Web 技术渲染，辅之以大量的接口提供丰富的客户端原生能力。  
同时，每个小程序页面都是用不同的 WebView 去渲染，这样可以提供更好的交互体验，更贴近原生体验，也避免了单个 WebView 的任务过于  
繁重。此外，界面渲染这一块我们定义了一套内置组件以统一体验，并且提供一些基础和通用的能力，进一步降低开发者的学习门槛。值得一  
提的是，内置组件有一部分较复杂组件是用客户端原生实现的同层渲染，以提供更好的性能。

为什么要这么设计呢？
为了管控和安全，微信小程序阻止开发者使用一些浏览器提供的，诸如跳转页面、操作 DOM、动态执行脚本的开放性接口。将逻辑层与视图层进  
行分离，视图层和逻辑层之间只有数据的通信，可以防止开发者随意操作界面，更好的保证了用户数据安全。

微信小程序视图层是 WebView，逻辑层是 JS 引擎。三端的脚本执行环境以及用于渲染非原生组件的环境是各不相同的：

运行环境	逻辑层	渲染层
Android	V8	Chromium 定制内核
iOS	JavaScriptCore	WKWebView
小程序开发者工具	NWJS	Chrome WebView
我们看一下单 WebView 实例与小程序双线程多实例下代码执行的差异点。

单 WebView 模式下，Page 视图与 App 逻辑共享同一个 JSContext，这样所有的页面可以共享全局的数据和方法，能够实现全局的状态管理。  
多 WebView 模式下，每一个 WebView 都有一个独立的 JSContext，虽然可以通过窗口通信实现数据传递，但是无法共享数据和方法，对于全局  
的状态管理也相对比较复杂，抽离一个通用的 WebView 或者 JS Engine 作为应用的 JSContext 就可以解决这些问题，但是同时引入了其他问题  
：视图和逻辑如何通信，在小程序里面数据更新后视图是异步更新的。

json 后缀的 JSON 配置文件
wxml 后缀的 WXML 模板文件
wxss 后缀的 WXSS 样式文件
=---------------------------------------------
js 后缀的 JS 脚本逻辑文件
JSON 配置
JSON 是一种数据格式，并不是编程语言，在小程序中，JSON扮演的静态配置的角色。

WXML 模板
从事过网页编程的人知道，网页编程采用的是 HTML + CSS + JS 这样的组合，其中 HTML 是用来描述当前这个页面的结构，  
CSS 用来描述页面的样子，JS 通常是用来处理这个页面和用户的交互。
同样道理，在小程序中也有同样的角色，其中 WXML 充当的就是类似 HTML 的角色。

WXSS 样式
WXSS 具有 CSS 大部分的特性，小程序在 WXSS 也做了一些扩充和修改。
新增了尺寸单位,WXSS 在底层支持新的尺寸单位 rpx ，开发者可以免去换算的烦恼

JS 逻辑交互
一个服务仅仅只有界面展示是不够的，还需要和用户做交互：响应用户的点击、获取用户的位置等等。在小程序里边，  
我们就通过编写 JS 脚本文件来处理用户的操作。

=---------------------------------------------
百度早起的轻应用，腾讯小程序。
native+小程序的架构模式。
小程序容器技术
FinClip uni-app
mPaas小程序架构

小程序采用双线程模式将页面渲染和业务逻辑分别放在两个单独的线程中，renderer 运行在 WebView 中，负责渲染界面；小程序业务逻辑运行在单独的  
 worker 线程，负责事件处理、API 调用和生命周期管理。两个线程之间通过postMessage 以及 onMessage 进行数据交换，数据可以从 worker 线程传递  
 到 render 重新渲染界面，同时renderer也可以将事件传递给对应的 worker 处理。一个 worker 可以对应多个 renderer，方便页面间数据共享和交互。

我们选择类似于微信 JSSDK 这样的 Hybrid 技术，即界面主要由成熟的 Web 技术渲染，辅之以大量的接口提供丰富的客户端原生能力。同时，每个小程序页  
面都是用不同的WebView去渲染，这样可以提供更好的交互体验，更贴近原生体验，也避免了单个WebView的任务过于繁重。

Saas App需要解决的问题
1、不同的定制化功能，如何进行权限控制
2、不同的服务器配置
3、版本控制，版本更新，bug修复如何只对相关业务的用户进行更新
4、插件化、组件化、热更新
5、其实就是不同模块解耦

组件化：单一职责原则划分模块
热更新/热修复，用于修复bug.
插件化的内容是原来的app中没有的东西，而热更新是将原有的东西做了改动
插件化在代码中有固定的入口，而热更新则可能改变任何一个位置的代码
可能的方案：
1）APP的基础功能模块实现组件化
2）热更新进行修复bug
3）大部分通过后台进口配置权限
4）使用小程序容器技术是一个好方法
5）插件化还是用不上

FinClip的SAAS版本的推出，意味着将 小程序“服务化”，进而打造“小程序运行时+SAAS”的跨端技术方案，相比于
传统的 App，基于小程序所设计导出的 App 还具有如下优点：
App 不需要更新，因此也不需要提交应用商店审核，只需要在 FinClip 后台更新发布小程序代码包即可；
可以将大多数功能使用小程序实际实现，App 中仅保留基本主页（或者完全作为空壳应用）；
App 体积十分小，用户获取与安装十分简单。

小程序容器技术：以小程序为载体的轻应用方案
虽然互联网大厂并未将这部分小程序运行能力开放出来，但是我们也不必望而生羡，市面上早就推出了类似的技术能力，
我们一般称之为小程序容器技术。
今天要给大家分享的也正是国产自研发的前端容器技术 —— FinClip。
一句话介绍 FinClip ：可以让小程序脱离微信环境最快运行在自有APP中
只需简单集成 FinClip SDK, 即可在 iPhone、Android、Windows、Linux、macOS、统信等平台下的应用中运行你的小程序。

最后简单总结一下 FinClip 可以帮助企业/开发者实现什么：
促进连接。只要把FinClip SDK嵌入到自己的App中，马上获得小程序运行能力。小程序已经在互联网上被充分证明
是一个非常有效地促进连接的技术形态。
动态更新。借助 FinClip 将应用中业务功能均以小程序形式替代，功能模块互相解耦，实现模块化开发，极大地提
升开发效率，降低开发成本。
多端支持。同一个业务场景，小程序化之后，可以展现在手机端、也可以运行在PC端、更可以出现在智能电视和车
载大屏上，多端同步、转发分享、一致体验，甚至可以无缝对接至互联网公共平台，代码只写一次，多处运行。
生态共建。让开发者、企业拥有自己的小程序应用商店，在这里可以实现与合作伙伴的资源整合 - 例如让合作伙伴
把数字服务以小程序方式上架、投放到自己的App中。

=---------------------------------------------
关于多进程
在计算机操作系统中，进程是进行资源分配和调度的基本单位。这对于基于Linux内核的Android系统也不例外。在Android的设计中，一个应用默认有一个(主)  
进程。但是我们通过配置可以实现一个应用对应多个进程。

本文将试图对于Android中应用多进程做一些整理总结。
android:process
应用实现多进程需要依赖于android:process这个属性
适用元素：Application, Activity, BroadcastReceiver, Service, ContentProvider。
通常情况下，这个属性的值应该是”:“开头。表示这个进程是应用私有的。无法在在跨应用之间共用。
如果该属性值以小写字母开头，表示这个进程为全局进程。可以被多个应用共用。（文章结尾会探讨这个问题）

一个应用 android:process 简单示例
<activity android:name=".MusicPlayerActivity" android:process=":music"/>
<activity android:name=".AnotherActivity" android:process="droidyue.com"/>

应用多进程有什么好处

增加App可用内存

在Android中，默认情况下系统会为每个App分配一定大小的内存。比如从最早的16M到后面的32M或者48M等。具体的内存大小取决于硬件和系统版本。
这些有限的内存对于普通的App还算是够用，但是对于展示大量图片的应用来说，显得实在是捉襟见肘。
仔细研究一下，你会发现原来系统的这个限制是作用于进程的(毕竟进程是作为资源分配的基本单位)。意思就是说，如果一个应用实现多个进程，那么  
这个应用可以获得更多的内存。
于是，增加App可用内存成了应用多进程的重要原因。

独立于主进程
除了增加App可用内存之外，确保使用多进程，可以独立于主进程，确保某些任务的执行和完成。
举一个简单的例子，之前的一个项目存在退出的功能，其具体实现为杀掉进程。为了保证某些统计数据上报正常，不受当前进程退出的影响，我们可以使用  
独立的进程来完成。

多进程的不足与缺点

数据共享问题

由于处于不同的进程导致了数据无法共享内容，无论是static变量还是单例模式的实现。
SharedPreferences 还没有增加对多进程的支持。
跨进程共享数据可以通过Intent,Messenger，AIDL等。

SQLite容易被锁

由于每个进程可能会使用各自的SQLOpenHelper实例，如果两个进程同时对数据库操作，则会发生SQLiteDatabaseLockedException等异常。
解决方法：可以使用ContentProvider来实现或者使用其他存储方式。

不必要的初始化

多进程之后，每个进程在创建的时候，都会执行自己的Application.onCreate方法。
通常情况下，onCreate中包含了我们很多业务相关的初始化，更重要的这其中没有做按照进程按需初始化，即每个进程都会执行全部的初始化。
按需初始化需要根据当前进程名称，进行最小需要的业务初始化。
按需初始化可以选择简单的if else判断，也可以结合工厂模式
