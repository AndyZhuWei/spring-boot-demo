# JavaEE开发的颠覆者 Spring Boot实战学习示例

#第8章 Spring Boot的数据访问
Spring Data项目是Spring用来解决数据访问问题的一揽子解决方案，Spring Data是一个伞形项目，包含了大量关系型数据库及非关系型数据库的
数据访问解决方案。Spring Data是我们可以快速且简单地使用普通的数据访问技术及新的数据访问技术。
Spring Data为我们使用统一的API来对数据存储技术进行数据访问提供了支持。这是Spring通过提供Spring Data Common项目来实现的，它是上述
各种Spring Data项目的依赖。Spring Data Commons让我们在使用关系型或非关系型数据访问技术时都使用基于Spring的统一标准，该标准包含
CRUD(创建、获取、更新、删除)、查询、排序和分页的相关的操作。
此处介绍下Spring Data Commons的一个重要概念：Spring Data Repository抽象。使用Spring Data Repository可以极大地减少数据访问层的代码。
既然是数据访问操作的统一标准，那肯定是定义了各种各样和数据访问相关的接口，Spring Data Repository抽象的根接口是Repository接口：
```java
package org.springframework.data.repository;
import java.io.Serializable;
public interface Repository<T,ID extends Serializable>{
    
}
```
从源码中可以看出，它接受领域类(JPA为实体类)和领域类的id类型作为类型参数。
它的子接口CrudRepository定义了和CRUD操作相关的内容
CurdRepository的子接口PagingAndSortingRepository定义了与分页和排序操作相关的内容
不同的数据访问技术也提供了不同的Repository,如Spring Data JPA有JpaRepository、Spring Data MongoDB有MongoRepository
Spring Data项目还给我们提供了一个激动人心的功能，即可以根据属性名进行计数、删除、查询方法等操作。
```java
public interface PersonRepository extends Repository<Person,Long> {
    //按照年龄技术
    Long countByAge(Integer age);
    //按照名字删除
    Long deleteByName(String name);
    //按照名字查询
    List<Person> findByName(String name);
    //按照名字和地址查询
    List<Person> findByNameAndAddress(String name,String address);
}
```

## 8.1 引入Docker
Docker是一个轻量级容器技术，类似于虚拟机技术(xen、kvm、vmware、virtual)。Dorker是直接运行在当前操作系统（Linus）之上，而不是运行
在虚拟机中，但是也实现了虚拟机技术的资源隔离，性能远远高于虚拟机技术。
Docker支持将软件编译成一个镜像(image),在这个镜像里做好对软件的各种配置，然后发布这个镜像，使用者可以运行这个镜像，运行中的镜像称之为容器（container）,
容器的启动是非常快的，一般都是以秒为单位。这个有点像我们平时安装ghost操作系统？系统安装好后软件都有了虽然完全不是一种东西，但是思路是类似的。
目前主流的软件以及非主流的软件大部分都有人将其封装成Docker镜像，我们只需要下载Docker镜像，然后运行镜像就可以快速获得已配置可运行的软件。
### 8.1.1 Docker安装
因为Docker的运行原理是基于Linux的，所以Docker只能在Linux下运行。我们在开发测试的时候，Docker是可以在Windows以及Mac OS X系统下的，运行的原理
是启动一个VirtualBox虚拟机，在此虚拟机里运行Docker
1.Linux下安装
CentOS 安装命令:
sudo yum update
sudo yum install docker
Ubuntu:
sudo apt-get update
sudo apt-get install docker
2.Windows下安装
安装这个软件Boot2Docker
### 8.1.2 Docker常用命令及参数
1.Docker镜像命令
基于Docker镜像是可以自己编译的。
通常情况下,Docker的镜像都放置在Docker官网的Docker Hub上，地址是http://registry.hub.docker.com
(1)Docker镜像检索
除了可以在http://registry.hub.docker.com网站检索镜像以外，还可以用下面命令检索：
docker search 镜像名
检索Redis,输入：
docker search redis
(2)镜像下载
下载镜像通过下面命令实现：
docker pull 镜像名
下载Redis镜像，运行：
docker pull redis
(3)镜像列表
查看本地镜像列表
docker images
其中REPOSITORY是镜像名
TAG是软件版本
latest为最新版
image id是当前镜像的唯一标识
created 是当前镜像创建时间
virtual size是当前镜像的大小
（4）镜像删除
删除指定镜像通过下面命令：
docker rmi image-id
删除所有镜像通过下面命令
docker rmi $(docker image -q)
2.Docker容器命令
（1）容器基本操作
最简单的运行镜像为容器的命令如下：
docker run --name container-name -d image-name
运行一个容器只要通过Docker run命令即可实现，其中，--name参数是为容器取得名称；-d表示 detached,意味着执行完这句话命令后控制台将不会被阻碍，
可以继续输入命令操作；最后的image-name 是要使用哪个镜像来运行容器。
我们来运行一个Redis容器
docker run --name test-redis -d redis
Docker会为我们的容器生成唯一的标识
（2）
容器列表
通过下面命令，查询运行中的容器列表
docker ps
其中CONTAINER ID是在启动的时候生成的ID,IMAGE是该容器使用的镜像；COMMAND是容器启动是调用的命令；CREATED是容器创建时间
STATUS是当前容器的状态PORTS是容器系统所使用的端口号，Redis默认使用6379端口；NAME是刚才给容器定义的名称
docker ps -a
(3)停止和启动容器
1）停止容器
docker stop container-name/container-id
我们可以通过容器名称或容器id来停止容器，以停止上面的Redis容器为例：
docker stop test-redis
此时运行中的容器列表为空。查看所有容器列表命令，可看出此时的STATUS为退出
2）启动容器
启动容器通过下面命令
docker start container-name/container-id
再次启动我么刚才停止的容器：
docker start test-redis
3）端口映射
Docker容器中运行的软件所使用的端口，在本机和本机的局域网是不能访问的，所以我们需要将Docker容器中的端口映射到当前主机的端口号上，这样我们在本机
和本机所在的局域网就能够访问该软件了
Docker的端口映射是通过一个-p参数来实现的。我们以刚才的Redis为例，映射容器的6379端口到本机的6378端口，命令如下
docker run -d -p 6378：6379 --name port-redis redis
4）删除容器
删除单个容器，可通过下面的命令
docker rm container-id
删除所有容器，可通过下面的命令
docker rm $(docker ps -a -q)
5)容器日志
查看当前容器日志，可通过下面的命令
docker logs container-name/container-id
我们查看下上面一个容器的日志
docker logs port-redis
6）登录容器
运行中的容器其实是一个功能完备的linux操作系统，所以我们可以像常规的系统一样登录并访问容器
我们可以使用下面命令，登录访问当前容器，登录后我们可以在容器中进行常规的Linux系统操作命令，还可以使用exit命令退出登录
docker exec -it container-id/container-name bash
### 8.1.3 下载本书所需的Docker镜像
docker pull wnameless/oracle-xe-11g
docker pull mongo
docker pull redis:2.8.21
docker pull cloudesire/activemq
docker pull rabbitmq
docker pull rabbitmq:3-management




























