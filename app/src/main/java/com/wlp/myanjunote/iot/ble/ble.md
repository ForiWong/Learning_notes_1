## 4.BlueDroid是什么
框架结构怎么样的，各层的关系。
Android BlueDroid（一）：BlueDroid概述 http://blog.csdn.net/xubin341719/article/details/40378205
Android BlueDroid（二）：BlueDroid蓝牙开启过程init http://blog.csdn.net/xubin341719/article/details/40393285
Android BlueDroid（三）：BlueDroid蓝牙开启过程enable http://blog.csdn.net/xubin341719/article/details/40402637

HSP和HFP蓝牙规格是典型单声道蓝牙耳机操作所需的规格，A2DP和AVRCP对于立体声耳机很重要。

HSP（手机规格，Head-Set-Profile）
这是最常用的配置，为当前流行支持蓝牙耳机与移动电话使用，提供手机与耳机之间通信所需的基本功能。 连接和配置好后，耳机可以
作为远程设备的音频输入和输出接口。

HFP（免提规格，Hands-Free-Profile）
1 HFP 基于SCO(Synchronous Connection Oriented)链路用于双向传输通话语音。
2 SCO链路具有同步传输特性，但无线传输有错误风险，为保证同步性数据的完整、正确性就无法保障，所以HFP采用的编码器需要具备容错能力
3 蓝牙通话过程的语音数据，这种音频对时效性有高要求，一般通过特殊的SCO或eSCO链路传输数据。编码方式有：CVSD、mSBC。
最基础的编码方式为CVSD，免提蓝牙设备都需支持该编码方式，支持宽带语音的设备可选mSBC编码数据

A2DP（高级音频传送规格， Advanced Audio Distribution Profile）
允许传输立体声音频信号（相比用于HSP和HFP的单声道加密，质量要好得多）。A2DP能够让两个同样支持蓝牙音效传输的装置互相连接，
都能输出如CD音质(16 bits，44.1 kHz)般的音乐。假如有一方没有支持A2DP的话，这时音效就只能输出Handsfree Profile(8 bits，8 kHz)，
就算耳机是采用双耳筒的设计，也只能有一般电话的单声道音质，与真正的立体声相去甚远。

AVRCP（音频/视频遥控规格，Audio/Video Remote Control Profile）
用于从控制器（如立体声耳机）向目标设备（如装有 MediaPlayer 的电脑）发送命令（如快进、快退、音量调节、暂停和播放。

RFCOMM（Serial Port Emulation）：串口仿真协议，上层协议蓝牙电话，蓝牙透传SPP等协议都是直接走的RFCOMM

HFP（Hands-Free）：蓝牙免提协议

SPP（SERIAL PORT PROFILE）：蓝牙串口协议

## 学习蓝牙协议栈

1）不要直接去抠协议细节，我觉得要对蓝牙协议栈有一个最基本的认知，市面的蓝牙芯片架构都有哪些，并且熟悉整个HCI蓝牙架构是怎么样，
每个协议是大致作用是什么。

2）等你对蓝牙协议有一个基本的认知，有一个做实验的开发板！后续到了协议理解部分，没有一个做实验的环境，如果强行“撸”协议，我觉得会“强撸灰飞烟灭”！

3）对研究协议有一个明确的顺序，有几种可选，可以由底层延伸到应用层或者由应用层延伸到底层！

对于传统蓝牙，底层到应用层我会给出一个顺序：TRANSPORT->HCI->L2CAP->RFCOMM->SPP,中间再了解点SDP的协议

## 对于传统蓝牙，应用层到底层我会给出一个顺序：SPP->RFOMM->L2CAP->HCI->TRANSPORT,中间再了解点SDP的协议

对于低功耗蓝牙，底层到应用层我会给出一个顺序：TRANSPORT->HCI->L2CAP->ATT->GATT->BAS,中间再了解点SM的协议

## 对于低功耗蓝牙，应用层到底层我会给出一个顺序：BAS->GATT->ATT->L2CAP->HCI->TRANSPORT,中间再了解点SM的协议


# 参考：
# 1.蓝牙低功耗BLE调研与开发 https://juejin.cn/post/7281276425219817472#heading-16
一： 蓝牙简介
1. 蓝牙技术的发展
2. Android版本中蓝牙简介
3. 蓝牙的广播和扫描
4. BLE通信基础
   二： BLE开发流程-扫描,连接,发送和接收数据,分包解包
   三： BLE客户端开发流程
   1、申请权限
   2、打开蓝牙
   3、搜索设备
   4、连接设备
   5、设备通信
   6、等待设备连接成功
   7、开启扫描服务
   8、获取负责通信的BluetoothGattCharacteristic
   9、开启监听
   10、写入数据
   11、接收数据
   12、断开连接
   四： BLE服务端开发流程
   1、设置广播以及初始化广播数据
   2、开始广播
   3、配置Services以及Characteristic
   4、Server回调以及操作
   5、Server主动写入数据通知client
   6、关闭广播，断开连接
   五：蓝牙操作的注意事项
   1、如何避免ble蓝牙连接出现133错误？
   2、单次写的数据大小有20字节限制，如何发送长数据？
   3、在Android不同版本或不同的手机扫描不到蓝牙设备
   4、读写问题
   5、一个完整的数据包分析


   