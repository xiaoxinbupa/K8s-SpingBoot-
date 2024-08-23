### Node部署

**前后端页面编译打包**

#### 1、node安装

```bash
Java前端代码打包编译 可以使用Node.js来运行前端构建工具，并使用npm来管理前端依赖。通过配置前端构建工具，可以将前端代码打包成静态资源文件，并生成合并、压缩、优化后的文件，方便部署到服务器
#Node安装
[root@jenkins ~]# cd /root/
[root@jenkins ~]# tar -xf node-v16.13.0-linux-x64.tar.gz 
[root@jenkins ~]# mv node-v16.13.0-linux-x64 /usr/local/node
[root@jenkins ~]# ls /usr/local/node
bin  CHANGELOG.md  include  lib  LICENSE  README.md  share

#配置环境变量
[root@jenkins ~]# vim /etc/bashrc 
...
 export MAVEN_HOME="/usr/local/maven"
 export NODE_HOME="/usr/local/node"      #新添加
 export PATH=${MAVEN_HOME}/bin/:${NODE_HOME}/bin/:$PATH   #更改
[root@jenkins ~]# source /etc/bashrc

#测试npm命令
[root@jenkins ~]# npm -v
8.1.0
```

#### 2、部署网站页面

##### 2.1 、商品管理页面

```bash
#测试编译
[root@jenkins ~]# cd /var/lib/jenkins/workspace/tea/code/page/admin-page/
[root@jenkins admin-page]# npm install          #安装此项目的各依赖项，此时，项目才是完整的、可运行的状态
[root@jenkins admin-page]# npm run build:test   #对项目进行打包和编译，根据test的环境进行打包编译
...
  Images and other types of assets omitted.

 DONE  Build complete. The dist directory is ready to be deployed.
 INFO  Check out deployment instructions at https://cli.vuejs.org/guide/deployment.html

#创建存放前端页面的目录/project/page
[root@jenkins admin-page]# mkdir -p /project/page

#由于打包编译的页面（无论是商品管理页面还是商品展示页面）都在dist目录下面存放，所以在/project/page再次创建子目录，存放页面，便于区分

#拷贝商品管理页面资源到/project/page/admin-page/
[root@jenkins admin-page]# mkdir /project/page/admin-page
[root@jenkins admin-page]# cp -r dist/ /project/page/admin-page
```

##### 2.2、商品展示页面

```bash
#测试编译
[root@jenkins admin-page]# cd ../front-page/
[root@jenkins front-page]# npm install      #安装此项目的各依赖项，此时，项目才是完整的、可运行的状态
...
added 1902 packages in 1m
[root@jenkins front-page]# npm run build:linuxTech
...
  Images and other types of assets omitted.

 DONE  Build complete. The dist directory is ready to be deployed.
 INFO  Check out deployment instructions at https://cli.vuejs.org/guide/deployment.html

#拷贝商品展示页面资源到/project/page/front-page/
[root@jenkins front-page]# mkdir /project/page/front-page
[root@jenkins front-page]# cp -r dist/ /project/page/front-page
```

#### 3、共享图片，jar包程序以及前端页面

使用nfs 把目录/project/jar/；/project/page；/home/images/vm/共享出去

```bash
[root@jenkins ~]# yum -y install nfs-utils
[root@jenkins ~]# vim /etc/exports
/project/jar       *(rw)
/project/page      *(rw)
/home/images/vm    *(rw)
[root@jenkins ~]# systemctl enable --now nfs-server
[root@jenkins ~]# chmod -R 777 /home/images/vm/   #后期需要在此目录中存储新的图片，需要写入权限
```
