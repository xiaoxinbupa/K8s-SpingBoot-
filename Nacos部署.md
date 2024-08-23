### Nacos部署

#### 1、nacos服务部署

```bash
#通过jumpserver连接nacos
[root@jumpserver ~]# ssh -p2222 operation@192.168.1.252
operation@192.168.1.252's password: 
...
Opt> p
  ID | 名称     | 地址          | 平台       | 组织        | 备注        
-----+----------+---------------+------------+-------------+-------------
  1  | harbor   | 192.168.1.30  | Linux      | Default     |             
  2  | jenkins  | 192.168.1.101 | Linux      | Default     |             
  3  | nacos    | 192.168.1.13  | Linux      | Default     |             
  4  | rocketmq | 192.168.1.14  | Linux      | Default     |             
页码：1，每页行数：10，总页数：1，总数量：4
提示：输入资产ID直接登录，二级搜索使用 // + 字段，如：//192 上一页：b 下 一页：n
搜索：
[Host]> 3       #进入nacos主机
#安装nacos，上传nacos到jumpserver主机的root目录下，拷贝到nacoa主机
[root@nacos ~]# scp 192.168.1.252:/root/package/nacos-server-2.0.2.tar.gz /root/
[root@nacos ~]# tar -xf /root/nacos-server-2.0.2.tar.gz -C /usr/local/

#安装JDK11环境
[root@nacos ~]# yum -y install java-11-openjdk-devel

#配置nacos
[root@nacos ~]# cd /usr/local/nacos/
[root@nacos nacos]# vim conf/application.properties
 33 spring.datasource.platform=mysql
 36 db.num=1
 39 db.url.0=jdbc:mysql://192.168.1.12:3306/nacos?characterEncoding=utf8&connectTim    eout=1000&socketTimeout=3000&autoReconnect=true&useUnicode=true&useSSL=false&se    rverTimezone=UTC
 40 db.user.0=nacos
 41 db.password.0=Taren123
```

#### 2、创建nacos库，存储注册中心数据

创建nacos库

![image-20240823175844677](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240823175844677.png)

创建nacos用户任意主机可以连接，密码Taren123，并授权

![image-20240823175903035](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240823175903035.png)

![image-20240823175916139](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240823175916139.png)

导入数据

![image-20240823175943164](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240823175943164.png)

![image-20240823175952605](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240823175952605.png)

![image-20240823180002787](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240823180002787.png)

```bash
#修改nacos启动脚本
[root@nacos nacos]# vim bin/startup.sh 
 55 export MODE="standalone"    #以单机模式运行

#启动nacos服务
[root@tea nacos]# ./bin/startup.sh 
...

#确认nacos服务启动
[root@nacos nacos]# tail -2 logs/start.out 
2023-07-05 02:37:20,239 INFO Nacos started successfully in stand alone mode. use external storage

[root@nacos nacos]# jps
3073 Jps
2827 nacos-server.jar

[root@nacos nacos]# ss -antlp | grep 8848
LISTEN 0      100                *:8848            *:*    users:(("java",pid=11885,fd=150))     

#设置nacos服务开机自启动,追加到rc.local文件最后
[root@nacos nacos]# vim /etc/rc.d/rc.local
...
/usr/local/nacos/bin/startup.sh
[root@nacos nacos]# chmod +x /etc/rc.d/rc.local
```

#### 3、通过负载均衡ELB发布nacos

![image-20240823180043884](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240823180043884.png)

![image-20240823180054842](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240823180054842.png)

![image-20240823180103762](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240823180103762.png)

![image-20240823180115114](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240823180115114.png)

![image-20240823180123854](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240823180123854.png)

 访问nacos，ELB的公网ip:8848/nacos访问，用户名nacos，密码nacos

![image-20240823180212465](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240823180212465.png)

创建新的命名空间project，id为linux，用于后期使用

![image-20240823180229662](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240823180229662.png)

###