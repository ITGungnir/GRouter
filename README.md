# GRouter

[![](https://jitpack.io/v/ITGungnir/GRouter.svg)](https://jitpack.io/#ITGungnir/GRouter)
![License](https://img.shields.io/badge/License-Apache2.0-blue.svg)
![](https://img.shields.io/badge/Email-itgungnir@163.com-ff69b4.svg)

## **！！！！！！注意：使用前请先阅读下文中的注意事项！！！！！**

## 0、GRouter简介
`GRouter`是基于`Kotlin Annotation Processing Tools`和`ASM`技术开发的一款适用于`Android`平台的模块化路由组件，具备以下特点：
* 支持代码自动注入，用户无需编写额外的初始化代码；
* 支持路由按分组进行按需加载；
* 支持配置局部路由拦截器`Interceptor`和全局的路由拦截器`GlobalInterceptor`，且支持自定义优先级；
* 支持配置路由匹配器`Matcher`，且支持自定义优先级；
* 支持响应式的`startActivityForResult`操作；
* 支持`clearGo`操作，即返回到几个页面之前的某个页面；
* 支持自定义匹配`Activity`失败时的回调；
* 支持`URL Scheme`路由（`DeepLink`）；
* 支持`MultiDex`；

以下功能将在后续版本中陆续加入：
* 支持参数自动注入；
* 支持生成路由文档；

## 1、配置
#### 1）在工程根目录下的`build.gradle`文件中添加依赖：
```groovy
buildscript {
    // ... Your codes
    repositories {
        // ... Your codes
        maven { url 'https://jitpack.io' }
    }
    dependencies {
        // ... Your codes
        classpath "com.github.ITGungnir.GRouter:router_plugin:$router_version"
    }
}
```

#### 2）在项目的底层模块（如common模块）的`build.gradle`文件中添加依赖：
```groovy
dependencies {
    // ... Your codes
    api "com.github.ITGungnir.GRouter:router_api:$router_version"
    // 如果要使用goForResult()功能，请导入RxJava 2.x的依赖
    api "io.reactivex.rxjava2:rxjava:$rxjava_version"
}
```

#### 3）在想要使用框架中注解的模块的`build.gradle`文件中添加依赖：
```groovy
apply plugin: 'kotlin-kapt'

dependencies {
    // ... Your codes
    kapt "com.github.ITGungnir.GRouter:router_compiler:$router_version"
}
```

#### 4）在顶层模块（如app模块）的`build.gradle`文件中添加插件：
```groovy
apply plugin: 'grouter'
```

#### 5）创建`Application`类：
在项目的顶层模块（如app模块）中创建一个`Application`类，重写`onCreate()`方法，并在`AndroidManifest.xml`文件中配置该类。

**注意：** 本步骤必须要做，否则无法注入代码！

## 2、基础使用
#### 0）分组的概念
`GRouter`在`v1.2.0`版本后增加了分组的概念，可以按需初始化，其具体特点如下：
* 在配置页面的路由时，`@Route`注解中的`path`字符串必须以`/`开头，否则`GRouter`在扫描路由时不会对它做处理；
* `@Route`注解中的`path`字符串以`/`分级，如`/main`被视为一级；`/app/app1`被视为二级；以此类推；
* 二级及以上的路由中被`/`隔开的第一部分视为路由的分组，如`/app/app1`的分组就是`app`；`/account/login/code`的分组是`account`；
* 一级路由是默认路由，在`GRouter`初始化时会加载这些路由，而不会加载非一级路由；
* 当用户要跳转到某个非一级路由时，`GRouter`会先判断当前路由表中是否有该路由，如果没有则说明该分组的路由还没有被初始化，此时才会初始化这个分组下的所有路由并添加到路由表中。

#### 1）添加`@Route`注解
在一个`Activity`类上添加`@Route`注解，表示当前`Activity`所代表的路由字符串。示例代码：
```kotlin
@Route("/main")
class MainActivity : AppCompatActivity()
@Route("/app/app1")
class AppActivity1 : AppCompatActivity()
```

#### 2）路由跳转
将如下代码写到`MainActivity`中某个按钮的点击事件中，点击按钮时即可从`MainActivity`跳转到`AppActivity1`中。
```kotlin
Router.instance.with(this)
    .target("/app/app1")
    .go()
```

## 3、进阶使用
#### 1）携带参数跳转
```kotlin
Router.instance.with(this)
    .target("/app/app1")
    .addParam("key1", "value1")
    .addParam("key2", 222)
    .go()
```

#### 2）`goForResult()操作`
**注意：** 如果要使用本功能，请确保项目中引入了`RxJava 2.x`的依赖，否则会报错！
```kotlin
Router.instance.with(this)
    .target(AppAppActivity2)
    .goForResult(1)?.subscribe {
        if (it.code == ProxyResult.ResultCode.RESULT_OK) {
            it.extras.getString("backKey")?.let { str -> toast(str) }
        }
    }
```

#### 3）`clearGo()`操作
`clearGo()`操作类似`SingleTask`的跳转，即清除任务栈中目标`Activity`之上的所有其他`Activity`。
如果目标`Activity`尚不存在于当前任务栈中，则会在任务栈顶压入一个新的目标`Activity`。
```kotlin
Router.instance.with(this)
    .target("/main")
    .clearGo()
```
**注意：** 本操作仅适用于应用内`Activity`之间的跳转，不适合网页跳转、电话跳转等自定义跳转方式。

#### 4）匹配失败回调
`go()`、`goForResult()`、`clearGo()`三个方法中都可以多传入一个`notMatchedCallback: (() -> Unit)?`参数，如果`GRouter`没有找到匹配目标路由的`Mathcer`，就会调用这个方法，
用户可以在这个方法中做特殊处理，或者对页面请求进行降级。
```kotlin
Router.instance.with(this)
    .target("/ghost_grout/ghost_page")
    .go { toast("/ghost_grout/ghost_page页面不存在") }
```

#### 5）添加`Flag`
```kotlin
val intent = Router.instance.with(this)
    .target("/main")
    .addFlag(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    .addFlag(Intent.FLAG_ACTIVITY_SINGLE_TOP)
    .getIntent()
```

#### 6）添加单体拦截器
拦截器的使用场景：如果某些页面要求用户只有在登录之后才可以访问，则可以设置一个登录拦截器，可以简化场景判断。一个登录拦截器的代码示例如下：
```kotlin
class LoginInterceptor(errorCallback: () -> Unit) : BaseInterceptor(errorCallback) {

    override fun intercept(chain: Interceptor.Chain): RouterResponse? {

        val isUserLogin = // 判断用户是否已登录的业务逻辑

        return if (isUserLogin) {
            chain.proceed(chain.request())
        } else {
            RouterResponse.failed(errorCallback)
        }
    }
}
```
在路由跳转时添加拦截器，示例代码：
```kotlin
Router.instance.with(this)
    .target(SubAccountActivity4)
    .addInterceptor(LoginInterceptor { toast("用户尚未登录，不能跳转到account4") })
    .go()
```
单体拦截器仅针对当前路由，随用随配置。先调用`addInterceptor()`的拦截器会先被执行。

如果想设置全局路由器，参考下面的`@GlobalInterceptor`注解用法。

#### 7）`DeepLink`路由
`GRouter`中提供了对`DeepLink`路由跳转的支持，可以通过`URL Scheme`的方式跳转到当前应用之外的其他应用，例如用系统浏览器打开网页：
```kotlin
Router.instance.with(this)
    .target("https://www.baidu.com/")
    .go()
```

也可以通过其他应用中自定义的`Scheme`协议进行跳转，如跳转到我自己创建的`TestDeepLink`应用：
```kotlin
Router.instance.with(this)
    .target("deeplink://test.deeplink.com?param=hello,deepLink")
    .go { toast("当前设备上没有安装TestDeepLink应用！") }
```

#### 8）`@Matcher`注解
用于指定某个类为路由匹配器，用于匹配不同类型的`Uri`，从而进行不同的跳转，该类必须继承自`BaseMatcher`抽象类。示例代码：
```kotlin
@Matcher
class TelMatcher : BaseMatcher(priority = 3) {

    override fun matched(request: RouterRequest): Boolean =
        request.target.toString().startsWith("tel:")

    override fun proceed(request: RouterRequest): Intent =
        Intent(Intent.ACTION_DIAL, request.target).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
}
```
上述代码编写了一个用于跳转拨号界面的匹配器，指定优先级为3 **（1~9，1最高）** ，使用时的代码如下：
```kotlin
Router.instance.with(this)
    .target("tel:88888888")
    .go()
```

`GRouter`中默认提供了一些默认的`BaseMatcher`，介绍如下：
* `RouteMapMather(priority = 4)`：匹配路由表中的路由；
* `DeepLinkMatcher(priority = 6)`：匹配DeepLink路由，跳转到其他APP；

#### 9）`@GlobalInterceptor`注解
`@GlobalInterceptor`用于设置全局路由器，配置之后所有路由操作都会添加该拦截器拦截。配置的拦截器必须实现`Interceptor`接口，示例代码：
```kotlin
@GlobalInterceptor
class LogGlobalInterceptor : BaseGlobalInterceptor(priority = 1) {

    override fun intercept(chain: Interceptor.Chain): RouterResponse? {

        val request = chain.request()

        println("------>>Router: ${request.safeContext()!!::class.java.name} -> ${request.target.toString()}")

        return chain.proceed(request)
    }
}
```
从`GRouter v1.2.0`版本开始，全局路由拦截器需要配置`priority`属性标识其优先级，优先级为`Int`类型，值在`[1, 9]`之间，1最高。

配置该日志拦截器后，当发生路由跳转时会打印如下日志：
```text
------>>Router: test.itgungnir.grouter.MainActivity -> app1
------>>Router: test.itgungnir.grouter.MainActivity -> https://www.baidu.com/
------>>Router: test.itgungnir.grouter.MainActivity -> tel:88888888
```
**注意：** 拦截器的拦截顺序为：`局部拦截器列表 -> VerifyInterceptor -> 自定义的全局拦截器 -> IntentInterceptor`。

`GRouter`中提供了一些默认的全局拦截器，介绍如下：
* `VerifyInterceptor`：判断发起路由的是否是`Context`或`Fragment`；
* `IntentInterceptor`：调用`Matcher`列表匹配路由，判断是否能匹配到路由；

## 4、使用本框架的注意事项（重要！！！）
#### 1）所有通过`@Route`注解配置的路由字符串必须以`/`开头！

#### 2）项目的单级目录中请不要含有`intermediates`，否则无法注入代码，如`com.xxx.intermediates.yyy`下的文件不会被扫描！

#### 3）必须在`app`模块下创建`android.app.Application`的子类，重写`onCreate()`方法，并在`AndroidManifest.xml`文件中配置，否则无法注入代码！

#### 4）如果要使用`goForResult()`操作，必须确保项目中导入了`RxJava 2.x`依赖，否则运行时报错：找不到`Observable`类！

#### 5）建议将app模块作为壳子模块，其中的类越少越好~

#### 6）建议使用`Kotlin`语言结合本框架使用，`goForResult()`等功能语法会略显不优雅～

## 5、`router_compiler`模块调试方法
#### 1）在`Terminal`中输入以下代码：
```text
./gradlew clean build --no-daemon -Dorg.gradle.debug=true -Dkotlin.compiler.execution.strategy="in-process" -Dkotlin.daemon.jvm.options="-Xdebug,-Xrunjdwp:transport=dt_socket\,address=5005\,server=y\,suspend=n"
```
**注意：** `Mac`系统中需要使用`./gradlew`命令，如果提示`command not found`，需要先运行`chmod +x gradlew`。

#### 2）新建一个`Remote`任务

#### 3）选中这个`Remote`任务，点击调试按钮即可

## 6、本地调试插件模块
#### 1）修改`router_plugin`模块的`build.gradle`文件中的代码：
```groovy
apply plugin: 'java-library'
apply plugin: 'kotlin'
apply plugin: 'maven'

repositories {
    jcenter()
}

dependencies {
    implementation gradleApi()
    implementation "com.android.tools.build:gradle:$gradle_version"
    implementation "org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version"
}

uploadArchives {
    repositories {
        mavenDeployer {
            repository(url: uri('../grouter'))
        }
    }
}

sourceCompatibility = jdk_version
targetCompatibility = jdk_version

group 'my.itgungnir.grouter.plugin'
version '1.0.0'
```

#### 2）修改工程根目录下`build.gradle`文件中的`classpath`：
```groovy
buildscript {
    // ... Your codes
    dependencies {
        // ... Your codes
        classpath 'my.itgungnir.grouter.plugin:router_plugin:1.0.0'
    }
}
```

#### 3）打开`Gradle`面板，进行以下操作：
第一步：`router_plugin -> Tasks -> build -> build`

第二步：`router_plugin -> Tasks -> upload -> uploadArchives`

第三步：`app -> Tasks -> build -> build`，此时在`Run`面板中即可看到日志

## 7、ASM代码编写心得
可以先在`Android Studio`或`IntelliJ IDEA`中写好目标代码，然后通过`Kotlin`工具中的`Show Kotlin Bytecode`生成字节码，再对应调用响应的方法即可。

例如，我想要在`Application`子类的`onCreate()`方法中注入以下代码：
```kotlin
Router.instance.registerAdditionalRouteMap("TargetRouteMapFileName")
```

此时我会先将这行代码写到`Application`的`onCreate()`方法的最后，然后在菜单栏上点击`Tools -> Kotlin -> Show Kotlin Bytecode`，就可以在`Kotlin Bytecode`面板中查看到字节码：
```text
 LINENUMBER 17 L4
 GETSTATIC my/itgungnir/grouter/api/Router.Companion : Lmy/itgungnir/grouter/api/Router$Companion;
 INVOKEVIRTUAL my/itgungnir/grouter/api/Router$Companion.getInstance ()Lmy/itgungnir/grouter/api/Router;
 LDC "TargetRouteMapFileName"
 INVOKEVIRTUAL my/itgungnir/grouter/api/Router.registerAdditionalRouteMap (Ljava/lang/String;)V
L5
```

`LINENUMBER`和`L5`两行可以忽略，只看它们中间的这几行，我们可以通过每行行首的操作符，去`Google`它们的用法。

## Change Log
#### v1.2.0
* 增加路由分组的概念，不同分组的路由按需加载
* 全局路由拦截器支持设置优先级
* 支持为路由增加匹配失败的回调
* 支持`URL Scheme`路由（`DeepLink`）

#### v1.1.2
* 通过ASM技术将代码注入到Application的onCreate()方法中，避免用户自己配置

## License
```text
Copyright 2019 ITGungnir

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
