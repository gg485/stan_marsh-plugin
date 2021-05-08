Stan Marsh Plugin  
=====================
简介  
--------------
一个基于Spring的支持在运行时无需重启即可对方法进行横插切面的插件管理工具   
特点是对业务代码**零侵入**！

为什么叫这个名字
-----------
**Stan Marsh**是著名动画《南方公园》的主角，我爱他！  

如何使用
------------
1.只需在正常的启动入口加上一行：
```java
@SpringBootApplication
@ComponentScan({"com.example.demo","stan.marsh.plugin"}) //加这么一行！
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
```
其中com.example.demo是你自己的项目名  
   
2.把想要添加插件的bean打上@Plugged
```java
@Service
@Plugged
public class ConnService {
    public void hello(){
        
    }
    public void bye(){
        
    }
}
```
除了这个注解，对业务代码没有任何影响
   
3.编写插件
```java
public class MyPlugin implements Before, After {
    @Override
    public void after(Object returnValue, Object[] args) {

    }

    @Override
    public void before(Object[] args) {

    }
}
```
插件只需实现Before和After接口，也可以只实现其中一个，具体功能的话，打日志，发邮件，还是访问https://www.southparkstudios.com ，什么都可以   
其中，args表示被插入的方法的参数，returnValue表示返回值。  
   
4.发布
把这个类打jar包，通过web ui上传，选择要插进哪些方法   
上传完的插件也能直接下线，同样不需重启就能生效   
