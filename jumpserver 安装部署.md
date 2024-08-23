### jumpserver 安装部署

#### 1、项目架构

![image-20240819175616724](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240819175616724.png)

![image-20240822154901973](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240822154901973.png)

#### 2、jumpserver概述

Jumpserver是一款开源的堡垒机，可使系统的管理员和开发人员安全的连接到企业内部服务器上执行操作，并且支持大部分操作系统，是一款非常安全的远程连接工具

#### 3、常见支持的系统

CentOS, RedHat, Fedora, Amazon LinuxDebianSUSE, UbuntuFreeBSD其他ssh协议硬件设备

#### 4、配置清单

|        主机名称         |    IP地址     |  最低配置   |  角色  |
| :---------------------: | :-----------: | :---------: | :----: |
| jumpserver（ecs-proxy） | 192.168.1.252 | 2CPU/8G内存 | 堡垒机 |
|         harbor          | 192.168.1.30  | 2CPU/4G内存 |  资产  |
|         jenkins         | 192.168.1.101 | 2CPU/8G内存 |  资产  |
|          nacos          | 192.168.1.13  | 2CPU/8G内存 |  资产  |
|        rocketmq         | 192.168.1.14  | 2CPU/8G内存 |  资产  |

#### 5、安装部署

在跳板机1.252安装 jumpserver

```bash
[root@ecs-proxy ~]# echo jumpserver > /etc/hostname
[root@ecs-proxy ~]# hostname jumpserver     #退出重新登录

配置docker的yum源， 安装docker
[root@jumpserver ~]# cp -r s4/docker/ /var/localrepo/     #如果之前的环境有，则不需要操作
[root@jumpserver ~]# createrepo --update /var/localrepo/  #如果之前的环境有，则不需要操作
[root@jumpserver ~]# vim /etc/yum.repos.d/local.repo
[local]
name=local
baseurl=http://192.168.1.252/localrepo/
gpgcheck=0
[root@jumpserver ~]# yum -y install docker-ce
[root@jumpserver ~]# systemctl enable --now docker

安装jumpserver
[root@jumpserver ~]# tar -xf package.tar.gz 
[root@jumpserver ~]# cd package/
[root@jumpserver package]# tar -xf jumpserver-offline-installer-v3.10.5-amd64.tar.gz 
[root@jumpserver package]# mv jumpserver-offline-installer-v3.10.5-amd64 /usr/local/jumpserver
[root@jumpserver package]# cd /usr/local/jumpserver/
[root@jumpserver jumpserver]# ./jmsctl.sh install   #一路回车，只需要更改端口
1. 检查配置文件
配置文件位置: /opt/jumpserver/config
/opt/jumpserver/config/config.txt  [ √ ]
/opt/jumpserver/config/nginx/cert/server.crt   [ √ ]
/opt/jumpserver/config/nginx/cert/server.key   [ √ ]
完成

>>> 安装配置 Docker
1. 安装 Docker
完成

2. 配置 Docker
是否需要支持 IPv6? (y/n)  (默认为 n):                                       #直接回车
完成

3. 启动 Docker
完成

>>> 加载 Docker 镜像
...
完成

>>> 安装配置 JumpServer
1. 配置加密密钥
SECRETE_KEY:     YjYxYWMyZmUtMzI4NS00YmZhLTg1NWUtN2MwZjY1MWZhMTMw
BOOTSTRAP_TOKEN: YjYxYWMyZmUtMzI4NS00YmZh
完成

2. 配置持久化目录
是否需要自定义持久化存储, 默认将使用目录 /data/jumpserver (y/n)  (默认为 n):   #直接回车
完成

3. 配置 MySQL
是否使用外部 MySQL? (y/n)  (默认为 n):                                      #直接回车
完成

4. 配置 Redis
是否使用外部 Redis? (y/n)  (默认为 n):                                      #直接回车
完成

5. 配置对外端口
是否需要配置 JumpServer 对外访问端口? (y/n)  (默认为 n): y     #输入y，修改web访问端口
JumpServer web port (default 80): 81                      #端口为81
JumpServer ssh port (default 2222):                       #直接回车
完成

6. 初始化数据库
[+] Running 4/4
 ⠿ Network jms_net      Created                             0.1s
 ⠿ Container jms_redis  Healthy                            11.0s
 ⠿ Container jms_mysql  Healthy                            11.0s
 ⠿ Container jms_core   Started                            11.2s
2023-07-04 01:52:27 Collect static files
2023-07-04 01:52:27 Collect static files done
2023-07-04 01:52:27 Check database structure change ...
2023-07-04 01:52:27 Migrate model change to database ...
...
  Applying users.0040_alter_user_source... OK
After migration, update builtin role permissions
完成

>>> 安装完成了
1. 可以使用如下命令启动, 然后访问
cd /usr/local/jumpserver
./jmsctl.sh start

2. 其它一些管理命令
./jmsctl.sh stop
./jmsctl.sh restart
./jmsctl.sh backup
./jmsctl.sh upgrade
更多还有一些命令, 你可以 ./jmsctl.sh --help 来了解

3. Web 访问
http://192.168.1.252:81
默认用户: admin  默认密码: admin

4. SSH/SFTP 访问
ssh -p2222 admin@192.168.1.252
sftp -P2222 admin@192.168.1.252

5. 更多信息
我们的官网: https://www.jumpserver.org/
我们的文档: https://docs.jumpserver.org/

[root@jumpserver jumpserver]# ./jmsctl.sh start     #启动jumpserver
[+] Running 8/8
 ⠿ Container jms_mysql   Healthy                            0.6s
 ⠿ Container jms_redis   Healthy                            0.6s
 ⠿ Container jms_core    Healthy                           14.3s
 ⠿ Container jms_web     Started                           15.6s
 ⠿ Container jms_koko    Started                           15.5s
 ⠿ Container jms_lion    Started                           15.3s
 ⠿ Container jms_celery  Started                           15.1s
 ⠿ Container jms_magnus  Started                           17.8s

[root@jumpserver jumpserver]# ./jmsctl.sh status        #查看状态
NAME         IMAGE                                  COMMAND                  SERVICE   CREATED       STATUS                 PORTS
jms_celery   docker.io/jumpserver/core-ce:v3.10.5   "./entrypoint.sh sta…"   celery    3 hours ago   Up 3 hours (healthy)   8080/tcp
jms_chen     docker.io/jumpserver/chen:v3.10.5      "./entrypoint.sh"        chen      3 hours ago   Up 3 hours (healthy)   8082/tcp
jms_core     docker.io/jumpserver/core-ce:v3.10.5   "./entrypoint.sh sta…"   core      3 hours ago   Up 3 hours (healthy)   8080/tcp
jms_kael     docker.io/jumpserver/kael:v3.10.5      "./entrypoint.sh"        kael      3 hours ago   Up 3 hours (healthy)   8083/tcp
jms_koko     docker.io/jumpserver/koko:v3.10.5      "./entrypoint.sh"        koko      3 hours ago   Up 3 hours (healthy)   0.0.0.0:2222->2222/tcp, :::2222->2222/tcp, 5000/tcp
jms_lion     docker.io/jumpserver/lion:v3.10.5      "./entrypoint.sh"        lion      3 hours ago   Up 3 hours (healthy)   4822/tcp, 8081/tcp
jms_magnus   docker.io/jumpserver/magnus:v3.10.5    "./entrypoint.sh"        magnus    3 hours ago   Up 3 hours (healthy)   8088/tcp, 14330/tcp, 0.0.0.0:33061-33062->33061-33062/tcp, :::33061-33062->33061-33062/tcp, 54320/tcp, 0.0.0.0:63790->63790/tcp, :::63790->63790/tcp
jms_mysql    jumpserver/mariadb:10.6                "docker-entrypoint.s…"   mysql     3 hours ago   Up 3 hours (healthy)   3306/tcp
jms_redis    jumpserver/redis:6.2                   "docker-entrypoint.s…"   redis     3 hours ago   Up 3 hours (healthy)   6379/tcp
jms_web      docker.io/jumpserver/web:v3.10.5       "/docker-entrypoint.…"   web       3 hours ago   Up 3 hours (healthy)   80/tcp, 0.0.0.0:81->81/tcp, :::81->81/tcp
```

### jumpserver使用

#### 1、访问jumpserver

```bash
使用jumpserver的公网登录： 公网ip:81
访问页面如下：用户名admin，密码admin登录，此时提示配置文件由问题，需要修改之后才能登陆
[root@jumpserver jumpserver]# vim /opt/jumpserver/config/config.txt 
128 DOMAINS=114.116.223.254:81                              #定义可信任的访问 IP，IP为jumpserver的公网ip
[root@jumpserver jumpserver]# cd /usr/local/jumpserver/
[root@jumpserver jumpserver]# ./jmsctl.sh restart           #重启
```

![image-20240819175940233](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240819175940233.png)

#### 2、jumpserver配置用户

```bash
在jumpserver里面的用户：
1、用户管理中的用户：这个用户指堡垒机账号， 能用这个账号登录web页面， 登录跳板机服务器的用户
2、特权用户：        是资产上已存在的, 并且拥有 高级权限 的系统用户，如root用户可以登录资产
```

创建登录jumpserver的用户operation

![image-20240819180027230](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240819180027230.png)

![image-20240819180040510](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240819180040510.png)

![image-20240819180054602](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240819180054602.png)

![image-20240819180255219](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240819180255219.png)

```
用户创建好之后，需要使用这个用户进行资产的管理，但是我们还没有资产。资产已经创建好harbor，jenkins，nacos，rocketmq
```

![image-20240819180319881](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240819180319881.png)

![image-20240819180326854](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240819180326854.png)

![image-20240819180334191](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240819180334191.png)

![image-20240819180342184](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240819180342184.png)

![image-20240819180359902](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240819180359902.png)



![image-20240819180420273](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240819180420273.png)

![image-20240819180432823](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240819180432823.png)

![image-20240819180439917](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240819180439917.png)

![image-20240819180453557](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240819180453557.png)

同样的方式添加jenkins，nacos，rocketmq主机资产

#### 3、授权

资产创建完成之后，需要授权给哪个用户登录进行管理

![image-20240819180531235](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240819180531235.png)



![image-20240819180540730](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240819180540730.png)



![image-20240819180659930](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240819180659930.png)

![image-20240819180708201](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240819180708201.png)

![image-20240819180715340](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240819180715340.png)

#### 4、测试

此时退出admin管理用户，使用operation用户登录，测试登录资产

![image-20240819180743959](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240819180743959.png)

点击web终端，测试登陆资产

![image-20240819180757707](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240819180757707.png)

如果jumpserver有多个用户，且授权不同的用户管理不同的资产，此时每个用户只能看到自己的资产，别人的资产是看不到的

#### 5、文件上传下载

![image-20240819180823342](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240819180823342.png)

![image-20240819180831508](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240819180831508.png)

![image-20240819180839345](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240819180839345.png)

下载文件，选中双击即可

配置完成后，jumpserver使用命令行通过operation用户连接，可以看到资产，此时也可以在jumpserver主机通过命令行连接

```bash
[root@jumpserver ~]# ssh -p2222 operation@192.168.1.252
operation@192.168.1.252's password: 
                运维人员,  JumpServer 开源堡垒机

        1) 输入 部分IP，主机名，备注 进行搜索登录(如果唯一).
        2) 输入 / + IP，主机名，备注 进行搜索，如：/192.168.
        3) 输入 p 进行显示您有权限的资产.
        4) 输入 g 进行显示您有权限的节点.
        5) 输入 h 进行显示您有权限的主机.
        6) 输入 d 进行显示您有权限的数据库.
        7) 输入 k 进行显示您有权限的Kubernetes.
        8) 输入 r 进行刷新最新的机器和节点信息.
        9) 输入 s 进行中文-English-日本語语言切换.
        10) 输入 ? 进行显示帮助.
        11) 输入 q 进行退出.
Opt> p
  ID | 名称     | 地址          | 平台             | 组织              | 备注             
-----+----------+---------------+------------------+-------------------+------------------
  1  | harbor   | 192.168.1.30  | Linux            | Default           |                  
  2  | jenkins  | 192.168.1.101 | Linux            | Default           |                  
  3  | nacos    | 192.168.1.13  | Linux            | Default           |                  
  4  | rocketmq | 192.168.1.14  | Linux            | Default           |                  
页码：1，每页行数：33，总页数：1，总数量：4
提示：输入资产ID直接登录，二级搜索使用 // + 字段，如：//192 上一页：b 下一页：n
搜索：

[Host]> 1
复用SSH连接（root@192.168.1.30）[连接数量: 2]
Last login: Sun Jul  7 16:11:35 2024 from 192.168.1.252
[root@harbor ~]# 
[root@harbor ~]# exit
logout
[Host]> exit
```

### jumpserver 安装部署

#### 1、项目架构

![image-20240819175616724](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240819175616724.png)



#### 2、jumpserver概述

Jumpserver是一款开源的堡垒机，可使系统的管理员和开发人员安全的连接到企业内部服务器上执行操作，并且支持大部分操作系统，是一款非常安全的远程连接工具

#### 3、常见支持的系统

CentOS, RedHat, Fedora, Amazon LinuxDebianSUSE, UbuntuFreeBSD其他ssh协议硬件设备

#### 4、配置清单

|        主机名称         |    IP地址     |  最低配置   |  角色  |
| :---------------------: | :-----------: | :---------: | :----: |
| jumpserver（ecs-proxy） | 192.168.1.252 | 2CPU/8G内存 | 堡垒机 |
|         harbor          | 192.168.1.30  | 2CPU/4G内存 |  资产  |
|         jenkins         | 192.168.1.101 | 2CPU/8G内存 |  资产  |
|          nacos          | 192.168.1.13  | 2CPU/8G内存 |  资产  |
|        rocketmq         | 192.168.1.14  | 2CPU/8G内存 |  资产  |

#### 5、安装部署

在跳板机1.252安装 jumpserver

```bash
[root@ecs-proxy ~]# echo jumpserver > /etc/hostname
[root@ecs-proxy ~]# hostname jumpserver     #退出重新登录

配置docker的yum源， 安装docker
[root@jumpserver ~]# cp -r s4/docker/ /var/localrepo/     #如果之前的环境有，则不需要操作
[root@jumpserver ~]# createrepo --update /var/localrepo/  #如果之前的环境有，则不需要操作
[root@jumpserver ~]# vim /etc/yum.repos.d/local.repo
[local]
name=local
baseurl=http://192.168.1.252/localrepo/
gpgcheck=0
[root@jumpserver ~]# yum -y install docker-ce
[root@jumpserver ~]# systemctl enable --now docker

安装jumpserver
[root@jumpserver ~]# tar -xf package.tar.gz 
[root@jumpserver ~]# cd package/
[root@jumpserver package]# tar -xf jumpserver-offline-installer-v3.10.5-amd64.tar.gz 
[root@jumpserver package]# mv jumpserver-offline-installer-v3.10.5-amd64 /usr/local/jumpserver
[root@jumpserver package]# cd /usr/local/jumpserver/
[root@jumpserver jumpserver]# ./jmsctl.sh install   #一路回车，只需要更改端口
1. 检查配置文件
配置文件位置: /opt/jumpserver/config
/opt/jumpserver/config/config.txt  [ √ ]
/opt/jumpserver/config/nginx/cert/server.crt   [ √ ]
/opt/jumpserver/config/nginx/cert/server.key   [ √ ]
完成

>>> 安装配置 Docker
1. 安装 Docker
完成

2. 配置 Docker
是否需要支持 IPv6? (y/n)  (默认为 n):                                       #直接回车
完成

3. 启动 Docker
完成

>>> 加载 Docker 镜像
...
完成

>>> 安装配置 JumpServer
1. 配置加密密钥
SECRETE_KEY:     YjYxYWMyZmUtMzI4NS00YmZhLTg1NWUtN2MwZjY1MWZhMTMw
BOOTSTRAP_TOKEN: YjYxYWMyZmUtMzI4NS00YmZh
完成

2. 配置持久化目录
是否需要自定义持久化存储, 默认将使用目录 /data/jumpserver (y/n)  (默认为 n):   #直接回车
完成

3. 配置 MySQL
是否使用外部 MySQL? (y/n)  (默认为 n):                                      #直接回车
完成

4. 配置 Redis
是否使用外部 Redis? (y/n)  (默认为 n):                                      #直接回车
完成

5. 配置对外端口
是否需要配置 JumpServer 对外访问端口? (y/n)  (默认为 n): y     #输入y，修改web访问端口
JumpServer web port (default 80): 81                      #端口为81
JumpServer ssh port (default 2222):                       #直接回车
完成

6. 初始化数据库
[+] Running 4/4
 ⠿ Network jms_net      Created                             0.1s
 ⠿ Container jms_redis  Healthy                            11.0s
 ⠿ Container jms_mysql  Healthy                            11.0s
 ⠿ Container jms_core   Started                            11.2s
2023-07-04 01:52:27 Collect static files
2023-07-04 01:52:27 Collect static files done
2023-07-04 01:52:27 Check database structure change ...
2023-07-04 01:52:27 Migrate model change to database ...
...
  Applying users.0040_alter_user_source... OK
After migration, update builtin role permissions
完成

>>> 安装完成了
1. 可以使用如下命令启动, 然后访问
cd /usr/local/jumpserver
./jmsctl.sh start

2. 其它一些管理命令
./jmsctl.sh stop
./jmsctl.sh restart
./jmsctl.sh backup
./jmsctl.sh upgrade
更多还有一些命令, 你可以 ./jmsctl.sh --help 来了解

3. Web 访问
http://192.168.1.252:81
默认用户: admin  默认密码: admin

4. SSH/SFTP 访问
ssh -p2222 admin@192.168.1.252
sftp -P2222 admin@192.168.1.252

5. 更多信息
我们的官网: https://www.jumpserver.org/
我们的文档: https://docs.jumpserver.org/

[root@jumpserver jumpserver]# ./jmsctl.sh start     #启动jumpserver
[+] Running 8/8
 ⠿ Container jms_mysql   Healthy                            0.6s
 ⠿ Container jms_redis   Healthy                            0.6s
 ⠿ Container jms_core    Healthy                           14.3s
 ⠿ Container jms_web     Started                           15.6s
 ⠿ Container jms_koko    Started                           15.5s
 ⠿ Container jms_lion    Started                           15.3s
 ⠿ Container jms_celery  Started                           15.1s
 ⠿ Container jms_magnus  Started                           17.8s

[root@jumpserver jumpserver]# ./jmsctl.sh status        #查看状态
NAME         IMAGE                                  COMMAND                  SERVICE   CREATED       STATUS                 PORTS
jms_celery   docker.io/jumpserver/core-ce:v3.10.5   "./entrypoint.sh sta…"   celery    3 hours ago   Up 3 hours (healthy)   8080/tcp
jms_chen     docker.io/jumpserver/chen:v3.10.5      "./entrypoint.sh"        chen      3 hours ago   Up 3 hours (healthy)   8082/tcp
jms_core     docker.io/jumpserver/core-ce:v3.10.5   "./entrypoint.sh sta…"   core      3 hours ago   Up 3 hours (healthy)   8080/tcp
jms_kael     docker.io/jumpserver/kael:v3.10.5      "./entrypoint.sh"        kael      3 hours ago   Up 3 hours (healthy)   8083/tcp
jms_koko     docker.io/jumpserver/koko:v3.10.5      "./entrypoint.sh"        koko      3 hours ago   Up 3 hours (healthy)   0.0.0.0:2222->2222/tcp, :::2222->2222/tcp, 5000/tcp
jms_lion     docker.io/jumpserver/lion:v3.10.5      "./entrypoint.sh"        lion      3 hours ago   Up 3 hours (healthy)   4822/tcp, 8081/tcp
jms_magnus   docker.io/jumpserver/magnus:v3.10.5    "./entrypoint.sh"        magnus    3 hours ago   Up 3 hours (healthy)   8088/tcp, 14330/tcp, 0.0.0.0:33061-33062->33061-33062/tcp, :::33061-33062->33061-33062/tcp, 54320/tcp, 0.0.0.0:63790->63790/tcp, :::63790->63790/tcp
jms_mysql    jumpserver/mariadb:10.6                "docker-entrypoint.s…"   mysql     3 hours ago   Up 3 hours (healthy)   3306/tcp
jms_redis    jumpserver/redis:6.2                   "docker-entrypoint.s…"   redis     3 hours ago   Up 3 hours (healthy)   6379/tcp
jms_web      docker.io/jumpserver/web:v3.10.5       "/docker-entrypoint.…"   web       3 hours ago   Up 3 hours (healthy)   80/tcp, 0.0.0.0:81->81/tcp, :::81->81/tcp
```

### jumpserver使用

#### 1、访问jumpserver

```bash
使用jumpserver的公网登录： 公网ip:81
访问页面如下：用户名admin，密码admin登录，此时提示配置文件由问题，需要修改之后才能登陆
[root@jumpserver jumpserver]# vim /opt/jumpserver/config/config.txt 
128 DOMAINS=114.116.223.254:81                              #定义可信任的访问 IP，IP为jumpserver的公网ip
[root@jumpserver jumpserver]# cd /usr/local/jumpserver/
[root@jumpserver jumpserver]# ./jmsctl.sh restart           #重启
```

![image-20240819175940233](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240819175940233.png)

#### 2、jumpserver配置用户

```bash
在jumpserver里面的用户：
1、用户管理中的用户：这个用户指堡垒机账号， 能用这个账号登录web页面， 登录跳板机服务器的用户
2、特权用户：        是资产上已存在的, 并且拥有 高级权限 的系统用户，如root用户可以登录资产
```

创建登录jumpserver的用户operation

![image-20240819180027230](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240819180027230.png)

![image-20240819180040510](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240819180040510.png)

![image-20240819180054602](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240819180054602.png)

![image-20240819180255219](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240819180255219.png)

```
用户创建好之后，需要使用这个用户进行资产的管理，但是我们还没有资产。资产已经创建好harbor，jenkins，nacos，rocketmq
```

![image-20240819180319881](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240819180319881.png)

![image-20240819180326854](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240819180326854.png)

![image-20240819180334191](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240819180334191.png)

![image-20240819180342184](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240819180342184.png)

![image-20240819180359902](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240819180359902.png)



![image-20240819180420273](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240819180420273.png)

![image-20240819180432823](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240819180432823.png)

![image-20240819180439917](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240819180439917.png)

![image-20240819180453557](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240819180453557.png)

同样的方式添加jenkins，nacos，rocketmq主机资产

#### 3、授权

资产创建完成之后，需要授权给哪个用户登录进行管理

![image-20240819180531235](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240819180531235.png)



![image-20240819180540730](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240819180540730.png)



![image-20240819180659930](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240819180659930.png)

![image-20240819180708201](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240819180708201.png)

![image-20240819180715340](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240819180715340.png)

#### 4、测试

此时退出admin管理用户，使用operation用户登录，测试登录资产

![image-20240819180743959](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240819180743959.png)

点击web终端，测试登陆资产

![image-20240819180757707](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240819180757707.png)

如果jumpserver有多个用户，且授权不同的用户管理不同的资产，此时每个用户只能看到自己的资产，别人的资产是看不到的

#### 5、文件上传下载

![image-20240819180823342](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240819180823342.png)

![image-20240819180831508](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240819180831508.png)

![image-20240819180839345](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240819180839345.png)

下载文件，选中双击即可

配置完成后，jumpserver使用命令行通过operation用户连接，可以看到资产，此时也可以在jumpserver主机通过命令行连接

```bash
[root@jumpserver ~]# ssh -p2222 operation@192.168.1.252
operation@192.168.1.252's password: 
                运维人员,  JumpServer 开源堡垒机

        1) 输入 部分IP，主机名，备注 进行搜索登录(如果唯一).
        2) 输入 / + IP，主机名，备注 进行搜索，如：/192.168.
        3) 输入 p 进行显示您有权限的资产.
        4) 输入 g 进行显示您有权限的节点.
        5) 输入 h 进行显示您有权限的主机.
        6) 输入 d 进行显示您有权限的数据库.
        7) 输入 k 进行显示您有权限的Kubernetes.
        8) 输入 r 进行刷新最新的机器和节点信息.
        9) 输入 s 进行中文-English-日本語语言切换.
        10) 输入 ? 进行显示帮助.
        11) 输入 q 进行退出.
Opt> p
  ID | 名称     | 地址          | 平台             | 组织              | 备注             
-----+----------+---------------+------------------+-------------------+------------------
  1  | harbor   | 192.168.1.30  | Linux            | Default           |                  
  2  | jenkins  | 192.168.1.101 | Linux            | Default           |                  
  3  | nacos    | 192.168.1.13  | Linux            | Default           |                  
  4  | rocketmq | 192.168.1.14  | Linux            | Default           |                  
页码：1，每页行数：33，总页数：1，总数量：4
提示：输入资产ID直接登录，二级搜索使用 // + 字段，如：//192 上一页：b 下一页：n
搜索：

[Host]> 1
复用SSH连接（root@192.168.1.30）[连接数量: 2]
Last login: Sun Jul  7 16:11:35 2024 from 192.168.1.252
[root@harbor ~]# 
[root@harbor ~]# exit
logout
[Host]> exit
```

### jumpserver 安装部署

#### 1、项目架构

![image-20240819175616724](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240819175616724.png)



#### 2、jumpserver概述

Jumpserver是一款开源的堡垒机，可使系统的管理员和开发人员安全的连接到企业内部服务器上执行操作，并且支持大部分操作系统，是一款非常安全的远程连接工具

#### 3、常见支持的系统

CentOS, RedHat, Fedora, Amazon LinuxDebianSUSE, UbuntuFreeBSD其他ssh协议硬件设备

#### 4、配置清单

|        主机名称         |    IP地址     |  最低配置   |  角色  |
| :---------------------: | :-----------: | :---------: | :----: |
| jumpserver（ecs-proxy） | 192.168.1.252 | 2CPU/8G内存 | 堡垒机 |
|         harbor          | 192.168.1.30  | 2CPU/4G内存 |  资产  |
|         jenkins         | 192.168.1.101 | 2CPU/8G内存 |  资产  |
|          nacos          | 192.168.1.13  | 2CPU/8G内存 |  资产  |
|        rocketmq         | 192.168.1.14  | 2CPU/8G内存 |  资产  |

#### 5、安装部署

在跳板机1.252安装 jumpserver

```bash
[root@ecs-proxy ~]# echo jumpserver > /etc/hostname
[root@ecs-proxy ~]# hostname jumpserver     #退出重新登录

配置docker的yum源， 安装docker
[root@jumpserver ~]# cp -r s4/docker/ /var/localrepo/     #如果之前的环境有，则不需要操作
[root@jumpserver ~]# createrepo --update /var/localrepo/  #如果之前的环境有，则不需要操作
[root@jumpserver ~]# vim /etc/yum.repos.d/local.repo
[local]
name=local
baseurl=http://192.168.1.252/localrepo/
gpgcheck=0
[root@jumpserver ~]# yum -y install docker-ce
[root@jumpserver ~]# systemctl enable --now docker

安装jumpserver
[root@jumpserver ~]# tar -xf package.tar.gz 
[root@jumpserver ~]# cd package/
[root@jumpserver package]# tar -xf jumpserver-offline-installer-v3.10.5-amd64.tar.gz 
[root@jumpserver package]# mv jumpserver-offline-installer-v3.10.5-amd64 /usr/local/jumpserver
[root@jumpserver package]# cd /usr/local/jumpserver/
[root@jumpserver jumpserver]# ./jmsctl.sh install   #一路回车，只需要更改端口
1. 检查配置文件
配置文件位置: /opt/jumpserver/config
/opt/jumpserver/config/config.txt  [ √ ]
/opt/jumpserver/config/nginx/cert/server.crt   [ √ ]
/opt/jumpserver/config/nginx/cert/server.key   [ √ ]
完成

>>> 安装配置 Docker
1. 安装 Docker
完成

2. 配置 Docker
是否需要支持 IPv6? (y/n)  (默认为 n):                                       #直接回车
完成

3. 启动 Docker
完成

>>> 加载 Docker 镜像
...
完成

>>> 安装配置 JumpServer
1. 配置加密密钥
SECRETE_KEY:     YjYxYWMyZmUtMzI4NS00YmZhLTg1NWUtN2MwZjY1MWZhMTMw
BOOTSTRAP_TOKEN: YjYxYWMyZmUtMzI4NS00YmZh
完成

2. 配置持久化目录
是否需要自定义持久化存储, 默认将使用目录 /data/jumpserver (y/n)  (默认为 n):   #直接回车
完成

3. 配置 MySQL
是否使用外部 MySQL? (y/n)  (默认为 n):                                      #直接回车
完成

4. 配置 Redis
是否使用外部 Redis? (y/n)  (默认为 n):                                      #直接回车
完成

5. 配置对外端口
是否需要配置 JumpServer 对外访问端口? (y/n)  (默认为 n): y     #输入y，修改web访问端口
JumpServer web port (default 80): 81                      #端口为81
JumpServer ssh port (default 2222):                       #直接回车
完成

6. 初始化数据库
[+] Running 4/4
 ⠿ Network jms_net      Created                             0.1s
 ⠿ Container jms_redis  Healthy                            11.0s
 ⠿ Container jms_mysql  Healthy                            11.0s
 ⠿ Container jms_core   Started                            11.2s
2023-07-04 01:52:27 Collect static files
2023-07-04 01:52:27 Collect static files done
2023-07-04 01:52:27 Check database structure change ...
2023-07-04 01:52:27 Migrate model change to database ...
...
  Applying users.0040_alter_user_source... OK
After migration, update builtin role permissions
完成

>>> 安装完成了
1. 可以使用如下命令启动, 然后访问
cd /usr/local/jumpserver
./jmsctl.sh start

2. 其它一些管理命令
./jmsctl.sh stop
./jmsctl.sh restart
./jmsctl.sh backup
./jmsctl.sh upgrade
更多还有一些命令, 你可以 ./jmsctl.sh --help 来了解

3. Web 访问
http://192.168.1.252:81
默认用户: admin  默认密码: admin

4. SSH/SFTP 访问
ssh -p2222 admin@192.168.1.252
sftp -P2222 admin@192.168.1.252

5. 更多信息
我们的官网: https://www.jumpserver.org/
我们的文档: https://docs.jumpserver.org/

[root@jumpserver jumpserver]# ./jmsctl.sh start     #启动jumpserver
[+] Running 8/8
 ⠿ Container jms_mysql   Healthy                            0.6s
 ⠿ Container jms_redis   Healthy                            0.6s
 ⠿ Container jms_core    Healthy                           14.3s
 ⠿ Container jms_web     Started                           15.6s
 ⠿ Container jms_koko    Started                           15.5s
 ⠿ Container jms_lion    Started                           15.3s
 ⠿ Container jms_celery  Started                           15.1s
 ⠿ Container jms_magnus  Started                           17.8s

[root@jumpserver jumpserver]# ./jmsctl.sh status        #查看状态
NAME         IMAGE                                  COMMAND                  SERVICE   CREATED       STATUS                 PORTS
jms_celery   docker.io/jumpserver/core-ce:v3.10.5   "./entrypoint.sh sta…"   celery    3 hours ago   Up 3 hours (healthy)   8080/tcp
jms_chen     docker.io/jumpserver/chen:v3.10.5      "./entrypoint.sh"        chen      3 hours ago   Up 3 hours (healthy)   8082/tcp
jms_core     docker.io/jumpserver/core-ce:v3.10.5   "./entrypoint.sh sta…"   core      3 hours ago   Up 3 hours (healthy)   8080/tcp
jms_kael     docker.io/jumpserver/kael:v3.10.5      "./entrypoint.sh"        kael      3 hours ago   Up 3 hours (healthy)   8083/tcp
jms_koko     docker.io/jumpserver/koko:v3.10.5      "./entrypoint.sh"        koko      3 hours ago   Up 3 hours (healthy)   0.0.0.0:2222->2222/tcp, :::2222->2222/tcp, 5000/tcp
jms_lion     docker.io/jumpserver/lion:v3.10.5      "./entrypoint.sh"        lion      3 hours ago   Up 3 hours (healthy)   4822/tcp, 8081/tcp
jms_magnus   docker.io/jumpserver/magnus:v3.10.5    "./entrypoint.sh"        magnus    3 hours ago   Up 3 hours (healthy)   8088/tcp, 14330/tcp, 0.0.0.0:33061-33062->33061-33062/tcp, :::33061-33062->33061-33062/tcp, 54320/tcp, 0.0.0.0:63790->63790/tcp, :::63790->63790/tcp
jms_mysql    jumpserver/mariadb:10.6                "docker-entrypoint.s…"   mysql     3 hours ago   Up 3 hours (healthy)   3306/tcp
jms_redis    jumpserver/redis:6.2                   "docker-entrypoint.s…"   redis     3 hours ago   Up 3 hours (healthy)   6379/tcp
jms_web      docker.io/jumpserver/web:v3.10.5       "/docker-entrypoint.…"   web       3 hours ago   Up 3 hours (healthy)   80/tcp, 0.0.0.0:81->81/tcp, :::81->81/tcp
```

### jumpserver使用

#### 1、访问jumpserver

```bash
使用jumpserver的公网登录： 公网ip:81
访问页面如下：用户名admin，密码admin登录，此时提示配置文件由问题，需要修改之后才能登陆
[root@jumpserver jumpserver]# vim /opt/jumpserver/config/config.txt 
128 DOMAINS=114.116.223.254:81                              #定义可信任的访问 IP，IP为jumpserver的公网ip
[root@jumpserver jumpserver]# cd /usr/local/jumpserver/
[root@jumpserver jumpserver]# ./jmsctl.sh restart           #重启
```

![image-20240819175940233](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240819175940233.png)

#### 2、jumpserver配置用户

```bash
在jumpserver里面的用户：
1、用户管理中的用户：这个用户指堡垒机账号， 能用这个账号登录web页面， 登录跳板机服务器的用户
2、特权用户：        是资产上已存在的, 并且拥有 高级权限 的系统用户，如root用户可以登录资产
```

创建登录jumpserver的用户operation

![image-20240819180027230](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240819180027230.png)

![image-20240819180040510](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240819180040510.png)

![image-20240819180054602](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240819180054602.png)

![image-20240819180255219](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240819180255219.png)

```
用户创建好之后，需要使用这个用户进行资产的管理，但是我们还没有资产。资产已经创建好harbor，jenkins，nacos，rocketmq
```

![image-20240819180319881](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240819180319881.png)

![image-20240819180326854](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240819180326854.png)

![image-20240819180334191](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240819180334191.png)

![image-20240819180342184](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240819180342184.png)

![image-20240819180359902](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240819180359902.png)



![image-20240819180420273](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240819180420273.png)

![image-20240819180432823](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240819180432823.png)

![image-20240819180439917](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240819180439917.png)

![image-20240819180453557](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240819180453557.png)

同样的方式添加jenkins，nacos，rocketmq主机资产

#### 3、授权

资产创建完成之后，需要授权给哪个用户登录进行管理

![image-20240819180531235](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240819180531235.png)



![image-20240819180540730](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240819180540730.png)



![image-20240819180659930](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240819180659930.png)

![image-20240819180708201](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240819180708201.png)

![image-20240819180715340](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240819180715340.png)

#### 4、测试

此时退出admin管理用户，使用operation用户登录，测试登录资产

![image-20240819180743959](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240819180743959.png)

点击web终端，测试登陆资产

![image-20240819180757707](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240819180757707.png)

如果jumpserver有多个用户，且授权不同的用户管理不同的资产，此时每个用户只能看到自己的资产，别人的资产是看不到的

#### 5、文件上传下载

![image-20240819180823342](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240819180823342.png)

![image-20240819180831508](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240819180831508.png)

![image-20240819180839345](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240819180839345.png)

下载文件，选中双击即可

配置完成后，jumpserver使用命令行通过operation用户连接，可以看到资产，此时也可以在jumpserver主机通过命令行连接

```bash
[root@jumpserver ~]# ssh -p2222 operation@192.168.1.252
operation@192.168.1.252's password: 
                运维人员,  JumpServer 开源堡垒机

        1) 输入 部分IP，主机名，备注 进行搜索登录(如果唯一).
        2) 输入 / + IP，主机名，备注 进行搜索，如：/192.168.
        3) 输入 p 进行显示您有权限的资产.
        4) 输入 g 进行显示您有权限的节点.
        5) 输入 h 进行显示您有权限的主机.
        6) 输入 d 进行显示您有权限的数据库.
        7) 输入 k 进行显示您有权限的Kubernetes.
        8) 输入 r 进行刷新最新的机器和节点信息.
        9) 输入 s 进行中文-English-日本語语言切换.
        10) 输入 ? 进行显示帮助.
        11) 输入 q 进行退出.
Opt> p
  ID | 名称     | 地址          | 平台             | 组织              | 备注             
-----+----------+---------------+------------------+-------------------+------------------
  1  | harbor   | 192.168.1.30  | Linux            | Default           |                  
  2  | jenkins  | 192.168.1.101 | Linux            | Default           |                  
  3  | nacos    | 192.168.1.13  | Linux            | Default           |                  
  4  | rocketmq | 192.168.1.14  | Linux            | Default           |                  
页码：1，每页行数：33，总页数：1，总数量：4
提示：输入资产ID直接登录，二级搜索使用 // + 字段，如：//192 上一页：b 下一页：n
搜索：

[Host]> 1
复用SSH连接（root@192.168.1.30）[连接数量: 2]
Last login: Sun Jul  7 16:11:35 2024 from 192.168.1.252
[root@harbor ~]# 
[root@harbor ~]# exit
logout
[Host]> exit
```

