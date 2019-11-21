actionApp.directive('datePicker',function(){//1定义一个指令名为datePicker
    return {
        restrict:'AC',//2限制为属性指令和样式指令
        link:function(scope,elem,attrs) {//3使用link方法来定义指令，在link方法内可使用当前scope、当前元素及元素属性
            elem.datepicker();//4初始化jqueryui的datePicker（jquery的写法是$('#id').datePicker()）.
        }
    };
});
//通过上面的代码我们就定制了一个封装juqeryui的datePciker的指令，本例只是为了演示的目的，主流的脚本框架已经被很多人封装过了，
//有兴趣的读者可以访问http://ngmodules.org/网站，这个网站包含了大量AngularJS的第三方模块、插件和指令
