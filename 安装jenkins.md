### 安装jenkins

#### 1、实现自动化部署

```bash
[root@jenkins tarena-passport]# cd /root/
[root@jenkins ~]# yum -y install jenkins-2.468-1.1.noarch.rpm
[root@jenkins ~]# yum -y install java-17-openjdk-devel   #安装java17版本，目前版本的jenkins需要java17才能支持
[root@jenkins ~]# update-alternatives --config java     #切换java环境为17版本
There are 2 programs which provide 'java'.
  Selection    Command
-----------------------------------------------
*+ 1           java-11-openjdk.x86_64 (/usr/lib/jvm/java-11-openjdk-11.0.24.0.8-3.el8.x86_64/bin/java)
   2           java-17-openjdk.x86_64 (/usr/lib/jvm/java-17-openjdk-17.0.12.0.7-2.el8.x86_64/bin/java)

Enter to keep the current selection[+], or type selection number: 2

[root@jenkins ~]# systemctl enable --now jenkins
[root@jenkins ~]# ss -antlp | grep 8080
LISTEN 0      50                 *:8080            *:*    users:(("java",pid=48056,fd=9))
```

#### 2、jenkins初始化

浏览器访问jenkins的公网地址：公网ip:8080

![image-20240823185541071](/home/student/Desktop/linux-note/linux_base_picture/image-20240823185541071.png)

```bash
#获取Jenkins初始密码
[root@jenkins ~]# cat /var/lib/jenkins/secrets/initialAdminPassword  
587027e9419f4943aae3add096deb4b0
```

![image-20240823185605042](/home/student/Desktop/linux-note/linux_base_picture/image-20240823185605042.png)

选择无，暂时不安装插件

![image-20240823185619920](/home/student/Desktop/linux-note/linux_base_picture/image-20240823185619920.png)

![image-20240823185629478](/home/student/Desktop/linux-note/linux_base_picture/image-20240823185629478.png)

![image-20240823185754013](/home/student/Desktop/linux-note/linux_base_picture/image-20240823185754013.png)

更改密码为Taren123，点击save保存

![image-20240823185823331](/home/student/Desktop/linux-note/linux_base_picture/image-20240823185823331.png)

![image-20240823185833003](/home/student/Desktop/linux-note/linux_base_picture/image-20240823185833003.png)

#### 3、登录测试

重新使用用户admin，密码Taren123登录jenkins录测试
重新使用用户admin，密码Taren123登录jenkins

```bash
#离线部署Jenkins插件
[root@jenkins ~]# ls /var/lib/jenkins/plugins/              #空目录，没有插件
[root@jenkins ~]# tar -xf jenkins_plugins.tar.gz -C /var/lib/jenkins/plugins/ #解压插件
[root@jenkins ~]# systemctl restart jenkins                             #重启，加载插件
[root@jenkins ~]# ss -antlp | grep 8080
LISTEN 0      50                 *:8080            *:*    users:(("java",pid=18962,fd=9))
浏览器刷新jenkins页面，登录jenkins
```

![image-20240823185941385](/home/student/Desktop/linux-note/linux_base_picture/image-20240823185941385.png)

#### 4、对接gitee

需要安装gitee的插件才能去gitee拉取代码

![image-20240823190003370](/home/student/Desktop/linux-note/linux_base_picture/image-20240823190003370.png)

![image-20240823190013590](/home/student/Desktop/linux-note/linux_base_picture/image-20240823190013590.png)

点击Advanced Settings，使用国内站点下载gitee插件：https://mirrors.tuna.tsinghua.edu.cn/jenkins/updates/update-center.json

![image-20240823190034467](/home/student/Desktop/linux-note/linux_base_picture/image-20240823190034467.png)

如果有插件需要更新，点击Updates，选中所有的插件，点击update更新（没有则不需要更新）

点击Available plugins，安装gitee插件

![image-20240823190058143](/home/student/Desktop/linux-note/linux_base_picture/image-20240823190058143.png)

安装成功后，如果提示失败，可以点击安装完成后重启jenkins，重启 一下

![image-20240823190112590](/home/student/Desktop/linux-note/linux_base_picture/image-20240823190112590.png)

配置jenkins连接gitee拉取代码

![image-20240823190127224](/home/student/Desktop/linux-note/linux_base_picture/image-20240823190127224.png)

链接名：tea

域名：https://gitee.com，gitee的地址

证书令牌id为：77a632cf7c7cc9624121bb0f04700aba

![image-20240823190151917](/home/student/Desktop/linux-note/linux_base_picture/image-20240823190151917.png)

![image-20240823190200251](/home/student/Desktop/linux-note/linux_base_picture/image-20240823190200251.png)

![image-20240823190208693](/home/student/Desktop/linux-note/linux_base_picture/image-20240823190208693.png)

![image-20240823190217018](/home/student/Desktop/linux-note/linux_base_picture/image-20240823190217018.png)

选中令牌，点击测试连接，成功即可

![image-20240823190242547](/home/student/Desktop/linux-note/linux_base_picture/image-20240823190242547.png)

注：扩展（实验中不需要操作，知晓即可）

获取证书令牌：https://gitee.com/profile/personal_access_tokens，浏览器输入以上地址

![image-20240823190300086](/home/student/Desktop/linux-note/linux_base_picture/image-20240823190300086.png)

![image-20240823190317573](/home/student/Desktop/linux-note/linux_base_picture/image-20240823190317573.png)

私人令牌：每次令牌都不一样

![image-20240823190331557](/home/student/Desktop/linux-note/linux_base_picture/image-20240823190331557.png)

#### 5、构建项目

构建一个自由风格的项目，拉取代码

![image-20240823190453050](/home/student/Desktop/linux-note/linux_base_picture/image-20240823190453050.png)

![image-20240823190502059](/home/student/Desktop/linux-note/linux_base_picture/image-20240823190502059.png)

设置参数和源码管理

选择【参数化构建过程】或英文【This project is parameterized】，点击【添加参数】的下拉列表，选择【Git 参数】

![image-20240823190520257](/home/student/Desktop/linux-note/linux_base_picture/image-20240823190520257.png)

定义变量【名称】可以任意，编写变量信息【描述】，根据提示填写【默认值】，选择【Git】，填写git仓库的url路径【Repository URL】

![image-20240823190536412](/home/student/Desktop/linux-note/linux_base_picture/image-20240823190536412.png)

git获取项目地址：https://gitee.com/cc-0001/tea；分支为$webver

![image-20240823190552430](/home/student/Desktop/linux-note/linux_base_picture/image-20240823190552430.png)

##### 1、测试结果

![image-20240823190616928](/home/student/Desktop/linux-note/linux_base_picture/image-20240823190616928.png)

```bash
[root@jenkins ~]# ls /var/lib/jenkins/workspace/tea/        #可以看到结果
README.txt  code  sql  teaimg.tar.gz
```

但是此时下载下来的项目代码，yaml文件的配置和现有的实验环境不符（如，redis地址，图片url地址等）需要解决此问题，才不会影响后续mvn打包封装

替换更改好的code目录到/var/lib/jenkins/workspace/tea/

![image-20240823190649652](/home/student/Desktop/linux-note/linux_base_picture/image-20240823190649652.png)

![image-20240823190700443](/home/student/Desktop/linux-note/linux_base_picture/image-20240823190700443.png)

```bash
rm -rf /var/lib/jenkins/workspace/tea/code/backend
cp -r /root/tea/code/backend /var/lib/jenkins/workspace/tea/
```

复制粘贴如上指令，保存

更改启动jenkins服务的用户，否则执行拷贝时没有权限

```bash
[root@jenkins ~]# vim /lib/systemd/system/jenkins.service
 34 User=root
[root@jenkins ~]# chown -R root /var/lib/jenkins/
[root@jenkins ~]# systemctl daemon-reload       #重新加载 systemd 的配置文件
[root@jenkins ~]# systemctl restart jenkins
```

登录jenkins，再次构建即可

#### 6、配置环境

##### 6.1、配置maven环境

![image-20240823190852448](/home/student/Desktop/linux-note/linux_base_picture/image-20240823190852448.png)

![image-20240823190859643](/home/student/Desktop/linux-note/linux_base_picture/image-20240823190859643.png)

使用maven的配置文件，定义maven配置文件：/usr/local/maven/conf/settings.xml

![image-20240823190914587](/home/student/Desktop/linux-note/linux_base_picture/image-20240823190914587.png)

##### 6.2、配置jdk环境

由于项目代码打包需要java11版本，需要配置使用java11的环境，17版本的不匹配此项目代码

![image-20240823190944615](/home/student/Desktop/linux-note/linux_base_picture/image-20240823190944615.png)

新增mvn

设置名字：mvn，点击取消自动安装

maven家目录：/usr/local/maven，最终结果显示

![image-20240823191003916](/home/student/Desktop/linux-note/linux_base_picture/image-20240823191003916.png)

![image-20240823191012376](/home/student/Desktop/linux-note/linux_base_picture/image-20240823191012376.png)

#### 7、配置mvn打包jar包

![image-20240823191030248](/home/student/Desktop/linux-note/linux_base_picture/image-20240823191030248.png)

目标：clean package -Dmaven.test.skip=true

POM：code/backend/tarena-passport/pom.xml

![image-20240823191050041](/home/student/Desktop/linux-note/linux_base_picture/image-20240823191050041.png)

继续增加1个构建步骤，按照以上步骤继续使用maven打包把除了passport之外的jar也要打包；步骤和打包passport一样，只是pom的路径换成以下：

code/backend/tarena-tp-tea/pom.xml

点击保存，测试

![image-20240823191106877](/home/student/Desktop/linux-note/linux_base_picture/image-20240823191106877.png)

此时虽然jenkins已经能够使用maven打包，但是这些jar包，是分散存储到不同的目录下面的，管理比较麻烦，可以把jar包集中放到一个目录下管理，在这里统一放到/project/jar下面管理

最后增加构建shell的步骤，拷贝jar到test主机的/project/jar

![image-20240823191126557](/home/student/Desktop/linux-note/linux_base_picture/image-20240823191126557.png)

![image-20240823191141364](/home/student/Desktop/linux-note/linux_base_picture/image-20240823191141364.png)

```bash
执行的shell命令如下：
mkdir -p /project/jar
cp /var/lib/jenkins/workspace/tea/code/backend/tarena-passport/passport-provider/target/passport-provider-1.0-SNAPSHOT.jar /project/jar/

cp /var/lib/jenkins/workspace/tea/code/backend/tarena-tp-tea/tea-admin/tea-admin-main/target/tea-admin-main-1.0.0-SNAPSHOT.jar  /project/jar/

cp /var/lib/jenkins/workspace/tea/code/backend/tarena-tp-tea/tea-server/tea-server-main/target/tea-server-admin-1.0.0-SNAPSHOT.jar /project/jar/
```

![image-20240823191223503](/home/student/Desktop/linux-note/linux_base_picture/image-20240823191223503.png)

test主机查看结果，已经有jar包

```bash
[root@jenkins tea]# ls /project/jar/
tea-admin-main-1.0.0-SNAPSHOT.jar
passport-provider-1.0-SNAPSHOT.jar
tea-server-admin-1.0.0-SNAPSHOT.jar
```

###