# LiteRead

## 存在的问题&&不足

##### 1. 虽然写代码的时候刻意去注意了内存方面的管理，也查阅了大量的相关资料[Android内存泄漏总结](https://github.com/GeniusVJR/LearningNotes),但是实际测试中发现代码依然存在内存泄漏的问题。比如：Fragment中Adapter的数据刷新；Animator造成的内存泄漏等

##### 2. 没有注意横竖屏切换的问题（写代码的时候我根本就没有考虑横屏....）。

##### 3. 对于com.android.support:appcompat-v7以及com.android.support:design中的新控件的使用存在不规范的地方，比如CollapsingToolbarLayout的Title存在从新Activity返回至老Activity时，老Activity的Title存在位置偏移错误的问题。

##### 4. Okhttp+retrofit+RxAndroid结合使用，在一个Http请求还未完成，当前Activity即被销毁时，因为正在执行的Request未能及时地被正常取消，造成Okhttp的日志拦截器出现空指针异常。

##### 5. 开发过程中虽然极力地去追求clean架构，并且也对应设计出了自己的MVP架构，但是仍然存在不合理的地方，架构方面还需继续学习。

##### 6. 没有考虑到6.0之后（包括6.0）的特性（自己都是用真机开发，手机比较老），可能造成部分异常。

##### 7. 可能依然存在未被测试的问题，没有进行有效的单元测试。

***

## 学习&&收获

##### 1. 认真进行了retrofit+RxAndroid+MVP的开发实践，也从中学习了很多关于[RxAndroid](https://github.com/ReactiveX/RxAndroid)以及[retrofit](https://github.com/square/retrofit)的知识。

##### 2.在查阅大量关于Android编程规范的同时着力去纠正自己不好的编程习惯。

##### 3.认真学习并且实践了多种设计模式

***

### 图片数据来源于[天狗开放阅图](http://apistore.baidu.com/apiworks/servicedetail/992.html)

### Android,IOS干货分享来源于[Gank.io](http://gank.io/api)

### 直播数据来源于[斗鱼](http://www.douyu.com),目前因为直播地址的加密,只解析到了直播列表

### 知乎日报数据来源于[知乎](http://www.zhihu.com)

### GitHub数据来源于[GitHub](https://www.github.com)



## 截图

<img src="/img/Screenshot_2016-09-01-23-05-48.jpeg" width="30%"/>
<img src="/img/Screenshot_2016-09-13-16-06-22.jpeg" width="30%"/>
<img src="/img/Screenshot_2016-09-01-23-06-32.jpeg" width="30%"/>
<img src="/img/Screenshot_2016-09-01-23-06-51.jpeg" width="30%"/>
<img src="/img/Screenshot_2016-09-01-23-07-12.jpeg" width="30%"/>
<img src="/img/Screenshot_2016-09-01-23-07-30.jpeg" width="30%"/>
<img src="/img/Screenshot_2016-09-01-23-05-48.jpeg" width="30%"/>
<img src="/img/Screenshot_2016-09-05-15-28-44.jpeg" width="30%"/>
<img src="/img/Screenshot_2016-09-05-15-28-51.jpeg" width="30%"/>
<img src="/img/Screenshot_2016-09-09-10-22-38.jpeg" width="30%"/>

### 特别鸣谢:

[rxAndroid](https://github.com/ReactiveX/RxAndroid)

[retrofit](https://github.com/square/retrofit)

[butterknife](https://github.com/JakeWharton/butterknife)

[universal-image-loader](https://github.com/nostra13/Android-Universal-Image-Loader)

### 另外也非常感谢[Trinea](http://www.trinea.cn)的博客以及他的[CodeKK](http://www.codekk.com)给我提供了大量学习的机会

