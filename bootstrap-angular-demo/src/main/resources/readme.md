## 7.7 基于Bootstrap和AngularJS的现代Web应用
现代的B/S系统软件有下面几个特色
1.单页面应用
   单页面应用(single-page application,简称SPA)指的是一种类似于原生客户端软件的更流畅的用户体验的页面。在单页面
应用中，所有的资源(HTML\JavaScript\CSS)都是按需动态加载到页面上的，且不需要服务端控制页面的转向。
2.响应式设计
  响应式设计(Responsive web design,简称RWD)指的是不同的设备(电脑、平板、手机)访问相同的页面的时候，得到不同的页面视图，而得到的视图是适应当前
屏幕的。当然就算在电脑上，我们通过拖动浏览器窗口的大小，也能得到合适的视图。
3.数据导向
  数据导向是对于页面导向而言的，页面上的数据获得是通过消费后台的REST服务来实现的，一般数据交换使用的格式是JSON.
### 7.7.1 Bootstrap
1.什么是Bootstrap
  Bootstrap官方定义：Bootstrap是开发响应式和移动优先的Web应用的最流行的HTML、CSS、JavaScript框架。
  Bootstrap实现了只使用一套代码就可以在不同的设备显示你想要的视图的功能。Bootstrap还为我们提供了大量美观的HTML元素前端组件和jQuery插件。
2.下载并引入Bootstrap
```html
<!DOCTYPE html>
<html lang="zh-cn">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="x-ua-compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width,initial-scale=1">
    <!--上面3个meta标签必须是head头三个标签，其余的head内标签在此3个之后-->
    <title>Bootstrap基本模板</title>

    <!--Bootstarp的CSS-->
    <link href="static/bootstrap/css/bootstrap.min.css" rel="stylesheet">

    <!--HTML5shiv and Respond.js用来让IE8支持HTML5元素和媒体查询-->
    <!--[if lt IE 9]>
    <script src="js/html5shiv.min.js"></script>
    <script src="js/respond.min.js"></script>
    <![endif]-->
</head>
<h1>你好，Bootstrap!</h1>

<!--jQuery是Bootstrap脚本插件必需的-->
<script src="static/js/jquery.min.js"></script>
<!--包含所有编译的插件-->
<script src="static/bootstrap/js/bootstrap.min.js"></script>
</body>
</html>
```
3.CSS支持
  Bootstrap的CSS样式为基础的HTML元素提供了美观的样式，此外还提供了一个高级的网格系统用来做页面布局。
（1）布局网格
  在Bootstrap里，行使用的样式为row,列使用col-md-数字，此数字范围为1~12，所有列加起来的和也是12，代码如下：
```html
<div class="row >
    <div class="col-md-1 table-bordered">.col-md-1</div>
    <div class="col-md-1">.col-md-1</div>
    <div class="col-md-1">.col-md-1</div>
    <div class="col-md-1">.col-md-1</div>
    <div class="col-md-1">.col-md-1</div>
    <div class="col-md-1">.col-md-1</div>
    <div class="col-md-1">.col-md-1</div>
    <div class="col-md-1">.col-md-1</div>
    <div class="col-md-1">.col-md-1</div>
    <div class="col-md-1">.col-md-1</div>
    <div class="col-md-1">.col-md-1</div>
    <div class="col-md-1">.col-md-1</div>
</div>
<div class="row">
    <div class="col-md-8">.col-md-8</div>
    <div class="col-md-4">.col-md-4</div>
</div>
<div class="row">
    <div class="col-md-4">.col-md-4</div>
    <div class="col-md-4">.col-md-4</div>
    <div class="col-md-4">.col-md-4</div>
</div>

<div class="row">
    <div class="col-md-6">.col-md-6</div>
    <div class="col-md-6">.col-md-6</div>
</div>
```
（2）html元素
Bootstrap为html元素提供了大量的样式，如表单元素、按钮、图标等。
4.页面组件支持
Bootstrap为我们提供了大量的页面组件，包括字体图标、下拉框、导航条、进度条、缩略图等
5.Javascript支持
Bootstrap为我们提供了大量的JavaScript插件，包括模式对话框、标签页、提示、警告等
### 7.7.2 AngularJS
1.什么是AngularJS
   AngularJS官方定义：AngularJS是HTML开发本应该的样子，它是用来设计开发Web应用的。
   AngularJS使用声名式模板+数据绑定（类似于JSP、Thymeleaf）、MVW（Model-View-Whatever）、MVVW(Model-View-ViewModel)\
MVC(Model-View-Controller)\依赖注入和测试，但是这一切的实现却只借助纯客户端的JavaScript.
   HTML一般是用来声明静态页面的，但是通常情况下我们希望页面是基于数据动态生成的，这也是我们很多服务端模板引擎出现的原因；而AngularJS
可以只通过前端技术就实现动态的页面。
2.下载并引入AngularJS
   最简单的AngularJS页面
```html
<!doctype html>
<!--1 ng-app所作用的范围是AngularJS起效的范围，本例是整个页面有效 -->
<html ng-app>
  <head>
  <!--2载入AngularJS的脚本 -->
    <script src="js/angular.min.js"></script>
  </head>
  <body>
   <lable>名字：</lable>
   <!--3 ng-model定义整个AngularJS的前端数据模型，模型的名称为yourName，模型的值来自你输入的值若输入的值改变，
   则数据模型值也会改变。 -->
   <input type="text" ng-model="yourName" placeholder="输入你的名字">
   <hr>
   <!--4使用{{模型名}}来读取模型中的值 -->
   <h1>你好{{yourName}}</h1>
  </body>
 </html> 
```   
3.模块、控制器和数据绑定
我们对MVC的概念已经烂熟于心了，但是平时的MVC都是服务端的MVC,这里用AngularJS实现了纯页面端的MVC,即实现了视图模板、
数据模板、代码控制的分离。
再来看看数据绑定，数据绑定是将视图和数据模型绑定在一起。如果视图变了，则模型的值就变了，如果模型值变了，则视图也会跟着改变。
AngularJS为了分离代码达到复用的效果，提供了一个module(模块)。定义一个模块需使用下面的代码
无依赖模块:
angular.module('firstModule',[]);
有依赖模块:
angular.module('firstModule',['moduleA','moduleB']);
我们看到了V就是我们的页面元素，M就是我们的ng-model,那C呢？我们可以通过下面的代码来定义控制器，页面使用ng-controller来和其关联:
```html
angular.module('firstModule',[])
   .controller('firstController',function(){
   ...
};
);
<div ng-controller="firstController">
...
</div>
```
4.Scope和Event
（1）Scope
Scope是AngularJS的内置对象，用$Scope来获得。在Scope中定义的数据是数据模型，可以通过{{模型名}}在视图上获得。
Scope主要是在编码中需要对数据模型进行处理的时候使用，Scope的作用范围与在页面声明的范围一致(如在controller内
使用，scope的作用范围是页面声明ng-controller标签元素的作用范围)
定义：
$scope.greeting='Hello'
获取:
{{greeting}}
（2）Event
因为Scope的作用范围不同，所以不同的Scope之间若要交互的话需要通过事件(Event)来完成。
1）冒泡事件(Emit)冒泡事件负责从子Scope向上发送事件，示例如下：
子Scope发送
$scope.$emit('Event_NAME_EMIT','message');
父Scope接收：
$scope.$on('EVENT_NAME_EMIT',function(event,data)){
...
})
2)广播事件(Broadcast)。广播事件负责从父Scope向下发送事件，示例如下。
父Scope发送：
$scope.$broadcast('EVENT_NAME_BROAD','message');
子scope接收：
$scope.$on('EVENT_NAME_BROAD',function(event,data){
...
})
5.多视图和路由
多视图和路由是AngularJS实现单页面应用的技术关键，AngularJS内置了一个$routeProvider对象来负责页面加载和页面路由转向。
需要注意的是，1.2.0之后的AngularJS将路由功能移出，所以使用路由功能要另外引入angular-route.js
例如：
angular.module('firstModule').config(function($routeProvider){
$routeProvider.when('/view1',{//1此处定义的是某个页面的路由名称
    controller:'Controller1',//2此处定义的是当前页面使用的控制器
    templateUrl:'view1.html',//3此处定义的要加载的真实页面
   }).when('/view2',{
    controller:'Controller2',
    templateUrl:'view2.html',
   
   });
})
在页面上可以用下面代码来使用我们定义的路由：
<ul>
  <li><a href="#/view1">view1</a></li>
  <li><a href="#/view2">view2</a></li>
</ul>
<ng-view></ng-view><!--此处为加载进来的页面显示位置-->
6.依赖注入
依赖注入是AngularJS的一大酷炫功能。可以实现对代码的解耦，在代码里可以注入AngularJS的对象或者我们自定义的对象。
下面示例是在控制器中注入$scope,注意使用依赖注入的代码格式
angular.module('firstModule')
   .controller("diController",['$scope',
        function($scope){
          ...
   }]);
7.Service和Factory
AngularJS为我们内置了一些服务，如$location、$timeout、$rootScope。很多时候我们需要自己定制一些服务，AngularJS
为我们提供了Service和Factory
Service和Factory的区别是：使用Service的话，AngularJS会使用new来初始化对象；而使用Factory会直接获得对象
（1）Service
定义：
angular.module('firstModule').service('helloService',function(){
    this.sayHello=function(name){
        alert('Hello '+name);
    }
});
注入调用:
angular.module('firstModule')
    .controller("diController",['$scope','helloService',
        function($scope,helloService){
            helloService.sayHello('wyf');
     }]);
(2)Factory
定义：
angular.module('firstModule').service('helloFactory',function(){
     return{
        sayHello:function(name){
            alert('Hello '+name);
        }
     }
});
注入调用：
angular.module('firstModule')
    .controller("diController",['$scope'],'helloFactory',
        function ($scope,helloFactory) {
            helloFactory.sayHello('wyf');
     }]);
8.http操作
AngularJS内置了$http对象用来进行Ajax的操作：
$http.get(url)
$http.post(url,data)
$http.put(url,data)
$http.delete(url)
$http.head(url)

9.自定义指令
AngularJS内置了大量的指令(directive)，如ng-repeat、ng-show、ng-model等。即使用一个简短的指令可以实现一个前端组件
比方说，有一个日期的js/jQuery插件，使用AngularJS封装后，在页面上调用此插件可以通过指令来实现，例如：
元素指令:<date-picker></date-picker>
属性指令:<input type="text" date-picker/>
样式指令:<input type="text" class="date-picker">
注释指令：<!--directive:date-picker-->

定义指令：
angular.module('myApp',[]).directive('helloWorld',function(){
    return{
        restrict:'AE',//支持使用属性、元素
        replace:true,
        template:'<h3>Hello,World!</h3>'
    };
});
调用指令，元素标签：
<hello-world/>

<hello:world/>
或者属性方式
<div hello-world />
### 7.7.3实战
在例子中，我们使用Bootstrap制作导航，使用AngularJS实现导航切换页面的路由功能，并演示AngularJS通过$http服务
和Spring Boot提供的REST服务，最后演示用指令封装jQueryUI的日期选择器
1.新建Spring Boot项目
初始化一个Spring Boot项目，依赖只需要选择Web
准备Bootstrap、AngularJS、jQuery、jQueryUI相关的资源到src/main/resources/static下。
2.制作导航
页面位置:src/main/resources/static/action.html:
3.模块和路由定义
页面位置：src/main/resources/static/js-action/app.js
4.控制器定义
脚本位置：src/main/resources/static/js-action/controllers.js
5.View1的界面(演示与服务端交互)
页面位置:src/main/resource/static/views/view1.html
6.服务端代码 
传值对象Javabean:Person
控制器：BootstrapAngularDemoApplication
7.自定义指令
脚本位置:src/main/resources/static/js-action/directives.js
8.View2的页面(演示自定义指令)
页面地址：src/main/resources/static/views/view2.html
9.运行
菜单及路由切换

注意：此处的实战没有运行通过，估计和angular版本有关，后边在来排错


























