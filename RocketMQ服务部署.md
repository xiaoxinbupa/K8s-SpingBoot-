### RocketMQ服务部署

#### 1、安装RocketMQ

```bash
依然使用jumpserver管理机器
[root@jumpserver ~]# ssh -p2222 operation@192.168.1.252
operation@192.168.1.252's password: 
...
Opt> p          #输入p
  ID | 名称     | 地址          | 平台            | 组织              | 备注              
-----+----------+---------------+-----------------+-------------------+-------------------
  1  | harbor   | 192.168.1.30  | Linux           | Default           |                   
  2  | jenkins  | 192.168.1.101 | Linux           | Default           |                   
  3  | nacos    | 192.168.1.13  | Linux           | Default           |                   
  4  | rocketmq | 192.168.1.14  | Linux           | Default           |                   
页码：1，每页行数：33，总页数：1，总数量：4
提示：输入资产ID直接登录，二级搜索使用 // + 字段，如：//192 上一页：b 下一页：n
搜索：
[Host]> 4       #进入rocketmq主机
#安装RocketMQ
[root@rocketmq ~]# scp 192.168.1.252:/root/package/rocketmq-all-4.9.4-bin-release.zip /root/
[root@rocketmq ~]# unzip /root/rocketmq-all-4.9.4-bin-release.zip 
[root@rocketmq ~]# mv rocketmq-all-4.9.4-bin-release/ /usr/local/rocketmq
[root@rocketmq ~]# ls /usr/local/rocketmq
benchmark  bin  conf  lib  LICENSE  NOTICE  README.md

#安装JDK11环境
[root@rocketmq ~]# yum -y install java-11-openjdk-devel

#配置RocketMQ服务
[root@rocketmq ~]# cd /usr/local/rocketmq/
[root@rocketmq rocketmq]# vim conf/broker.conf  #文件末尾追加
...
namesrvAddr=0.0.0.0:9876     #指定RocketMQ Broker要连接的NameServer的地址和端口，0.0.0.0表示监听所有网络接口
brokerIP1=192.168.1.14      #指定Broker所在主机的IP地址，写自己本机eth0的ip
brokerIP2=127.0.0.1         #写自己本机127.0.0.1

#启动RocketMQ服务
[root@rocketmq rocketmq]# nohup /usr/local/rocketmq/bin/mqnamesrv -n 0.0.0.0:9876 &> /usr/local/rocketmq/bin/namesrv.log &


#配置JVM虚拟机参数
[root@rocketmq rocketmq]# vim bin/runbroker.sh 
 85 JAVA_OPT="${JAVA_OPT} -server -Xms512m -Xmx512m"
 
[root@rocketmq rocketmq]# nohup /usr/local/rocketmq/bin/mqbroker -n 192.168.1.14:9876 autoCreateTopicEnable=true -c /usr/local/rocketmq/conf/broker.conf &> /usr/local/rocketmq/bin/mqbroker.log &  

#autoCreateTopicEnable=true 是否启用自动创建Topic的功能，地址改成自己本机eth0的ip

#验证RocketMQ启动结果
[root@rocketmq rocketmq]# jps
2787 Jps
2697 BrokerStartup
2378 NamesrvStartup
[root@rocketmq rocketmq]# ss -antplu | grep 9876
tcp   LISTEN 0      1024               *:9876             *:*    users:(("java",pid=11631,fd=94)) 

#设置RocketMQ服务开机自启动
[root@rocketmq rocketmq]# vim /etc/rc.d/rc.local    #文件末尾追加
nohup /usr/local/rocketmq/bin/mqnamesrv -n 0.0.0.0:9876 &> /usr/local/rocketmq/bin/namesrv.log &
nohup /usr/local/rocketmq/bin/mqbroker -n 192.168.1.14:9876 autoCreateTopicEnable=true -c /usr/local/rocketmq/conf/broker.conf &> /usr/local/rocketmq/bin/mqbroker.log &
[root@rocketmq rocketmq]# chmod +x /etc/rc.d/rc.local
```
