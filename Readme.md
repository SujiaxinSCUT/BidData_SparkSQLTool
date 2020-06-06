# 6月1日 星期一

创建初始工程项目，并测试完成底层jdbc连接hive。

# 6月2日 星期二

完成基本连接和获取数据库信息等操作，编写完成GUI界面雏形。

暂时无法通过元数据获取所有库表信息。

多线程更新UI卡顿等问题待解决。

# 6月3日 星期三

解决多线程更新UI卡顿问题。

确定展示动态表格实现方法。

# 6月4日 星期四

解决JDBC多线程共享数据库连接导致的线程安全问题。

基本实现在文本区域输入sql语句后点击运行，成功将sql命令传送到数据库并将返回结果集展示于表格中。

# 6月5日 星期五

简单查询分析器基本功能都已完备，能完成从建立连接，到选择数据库，对数据库内表进行CRUD等操作。

UI线程处理无明显BUG。

界面显示尚有一点瑕疵，待改进。

目前元素区只能展示从连接、数据库、表信息，待改进。

以下是一些基本界面和操作

![image.png](https://upload-images.jianshu.io/upload_images/17501422-27e0c2ffa976bbbd.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

![image.png](https://upload-images.jianshu.io/upload_images/17501422-1f5a8f2efadcd353.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

![image.png](https://upload-images.jianshu.io/upload_images/17501422-4f6f9a52af1abce3.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

![image.png](https://upload-images.jianshu.io/upload_images/17501422-e9a610dc5b824a26.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

![image.png](https://upload-images.jianshu.io/upload_images/17501422-ee026055dd6c2d62.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

![image.png](https://upload-images.jianshu.io/upload_images/17501422-2b7f41be748e8f1e.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

![image.png](https://upload-images.jianshu.io/upload_images/17501422-4e925d96b1c7123b.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

# 6月6日 星期六

一些界面问题得以解决

确定查询线程使用及其停止方法

目前正在编写使用说明书以及设计说明书

