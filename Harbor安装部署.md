### Harbor安装部署

----

#### 1、安装harbor

```bash
#安装部署
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
# 安装部署 docker 及 compose 组件
[root@harbor ~]# yum -y install docker-ce docker-compose-plugin
[root@harbor ~]# systemctl enable --now docker

拷贝harbor-v2.7.0.tgz 到 harbor 主机 
[root@jumpserver ~]# scp /root/s4/public/harbor-v2.9.2.tgz  192.168.1.30:/root/

#创建 https 证书，后期harbor使用https的方式访问，使用openssl工具生成一个RSA密钥对，放到/root/tls/，名称为cert.key，秘钥位数2048位；使用openssl工具生成一个有效期为10年的证书，私钥使用之前生成的/root/tls/cert.key文件，将生成的证书保存到/root/tls/cert.crt文件中，并设置证书的主题信息
[root@harbor ~]# tar -xf harbor-v2.9.2.tgz  -C /usr/local/
[root@harbor ~]# cd /usr/local/harbor
[root@harbor harbor]# mkdir /root/tls
[root@harbor harbor]# openssl genrsa -out /root/tls/cert.key 2048
[root@harbor harbor]# openssl req -new -x509 -days 3650 -key /root/tls/cert.key -out /root/tls/cert.crt -subj "/C=CN/ST=BJ/L=BJ/O=Tedu/OU=NSD/CN=harbor"

# 修改配置文件
[root@harbor harbor]# cp harbor.yml.tmpl harbor.yml
[root@harbor harbor]# vim harbor.yml
  5 hostname: harbor               #harbor地址
  8 #http:                         #加上注释
 10   #port: 80                    #加上注释
 17   certificate: /root/tls/cert.crt    #nginx的证书文件位置
 18   private_key: /root/tls/cert.key    #nginx的秘钥文件位置

# 安装harbor
[root@harbor harbor]# ./install.sh 
[root@harbor harbor]# docker compose ps
NAME                IMAGE                                COMMAND                  SERVICE       CREATED         STATUS                   PORTS
harbor-core         goharbor/harbor-core:v2.9.2          "/harbor/entrypoint.…"   core          2 minutes ago   Up 2 minutes (healthy)   
harbor-db           goharbor/harbor-db:v2.9.2            "/docker-entrypoint.…"   postgresql    2 minutes ago   Up 2 minutes (healthy)   
harbor-jobservice   goharbor/harbor-jobservice:v2.9.2    "/harbor/entrypoint.…"   jobservice    2 minutes ago   Up 2 minutes (healthy)   
harbor-log          goharbor/harbor-log:v2.9.2           "/bin/sh -c /usr/loc…"   log           2 minutes ago   Up 2 minutes (healthy)   127.0.0.1:1514->10514/tcp
harbor-portal       goharbor/harbor-portal:v2.9.2        "nginx -g 'daemon of…"   portal        2 minutes ago   Up 2 minutes (healthy)   
nginx               goharbor/nginx-photon:v2.9.2         "nginx -g 'daemon of…"   proxy         2 minutes ago   Up 2 minutes (healthy)   0.0.0.0:80->8080/tcp, :::80->8080/tcp, 0.0.0.0:443->8443/tcp, :::443->8443/tcp
redis               goharbor/redis-photon:v2.9.2         "redis-server /etc/r…"   redis         2 minutes ago   Up 2 minutes (healthy)   
registry            goharbor/registry-photon:v2.9.2      "/home/harbor/entryp…"   registry      2 minutes ago   Up 2 minutes (healthy)   
registryctl         goharbor/harbor-registryctl:v2.9.2   "/home/harbor/start.…"   registryctl   2 minutes ago   Up 2 minutes (healthy) 
```

#### ￼2、发布 harbor 服务

通过ELB发布，ELB发布时监听443端口即可（如果没有负载均衡，自行购买一个带公网IP的负载均衡，之后发布harbor即可）

![image-20240823172920261](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240823172920261.png)

![image-20240823172929525](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240823172929525.png)

![image-20240823173011111](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240823173011111.png)

![image-20240823173024825](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240823173024825.png)

![image-20240823173037349](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240823173037349.png)

点击提交，点击返回列表

#### 3、访问测试

通过浏览器访问：[https://ELB](https://elb/)地址

登录harbor，点击新建项目

![image-20240823173117154](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240823173117154.png)

#### 4、制作镜像，测试上传下载

制作启动jar的基础镜像

```bash
通过test主机制作镜像，测试上传下载
[root@harbor harbor]# exit
[Host]> 2
开始连接到 root(root)@192.168.1.101  0.2
...
[root@jenkins ~]# vim /etc/hosts            #配置主机名解析
192.168.1.30   harbor
[root@jenkins ~]# yum -y install docker-ce
[root@jenkins ~]# mkdir /etc/docker         #没有此目录创建即可，有可以直接使用
[root@jenkins ~]# vim /etc/docker/daemon.json
{
   "registry-mirrors": ["https://harbor:443"],
   "insecure-registries": ["harbor:443"]
}
[root@jenkins ~]# systemctl enable --now docker


# 登录 harbor 仓库，用户名密码可能和自己的环境不一样，使用直接设置的用户名和密码
[root@jenkins ~]# docker login harbor:443
Username: admin     #用户名admin
Password:           #密码(实际以自己harbor服务密码为准)

# 认证信息记录文件在/root/.docker/config.json文件中

制作启动jar的基础镜像
[root@jenkins ~]# scp 192.168.1.252:/root/s4/public/myos.tar.xz /root/
[root@jenkins ~]# docker load -i /root/myos.tar.xz 
[root@jenkins ~]# mkdir /root/jar-package
[root@jenkins ~]# cd /root/jar-package/
[rootjenkins jar-package]# vim Dockerfile
FROM myos:8.5
RUN yum -y install java-11-openjdk java-11-openjdk-devel && yum clean all
CMD ["/bin/bash"]
[root@jenkins jar-package]# docker build -t  jar:base .

打标签上传jar:base镜像
[root@jenkins jar-package]# docker tag jar:base harbor:443/tea/jar:base
[root@jenkins jar-package]# docker push harbor:443/tea/jar:base

打标签上传tea:nginx镜像
[root@jenkins jar-package]# docker tag myos:nginx harbor:443/tea/tea:nginx
[root@jenkins jar-package]# docker push harbor:443/tea/tea:nginx
```

在harbor的web页面可以看到tea项目中有两个镜像

![image-20240823173213120](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240823173213120.png)

测试下载

```bash
[root@jenkins jar-package]# docker rmi jar:base 
[root@jenkins jar-package]# docker rmi harbor:443/tea/jar:base 
[root@jenkins jar-package]# docker pull harbor:443/tea/jar:base     #下载成功
```

###