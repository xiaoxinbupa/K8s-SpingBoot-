### Maven部署

#### 1、安装maven

```bash
[root@jumpserver ~]# ssh -p2222 operation@192.168.1.252
operation@192.168.1.252's password: 
...
Opt> p          #输入p
  ID | 名称     | 地址          | 平台             | 组织             | 备注              
-----+----------+---------------+------------------+------------------+-------------------
  1  | harbor   | 192.168.1.30  | Linux            | Default          |                   
  2  | jenkins  | 192.168.1.101 | Linux            | Default          |                   
  3  | nacos    | 192.168.1.13  | Linux            | Default          |                   
  4  | rocketmq | 192.168.1.14  | Linux            | Default          |                   
页码：1，每页行数：33，总页数：1，总数量：4
提示：输入资产ID直接登录，二级搜索使用 // + 字段，如：//192 上一页：b 下一页：n
搜索：
[Host]> 2      #进入jenkins主机

拷贝软件到jenkins主机
[root@jumpserver ~]# scp /root/package/node-v16.13.0-linux-x64.tar.gz /root/package/apache-maven-3.6.3-bin.tar.gz  /root/package/jenkins*  /root/package/repository.tar.gz 192.168.1.101:/root/

#安装Maven
[root@jenkins ~]# tar -xf apache-maven-3.6.3-bin.tar.gz
[root@jenkins ~]# mv apache-maven-3.6.3 /usr/local/maven
[root@jenkins ~]# yum -y install java-11-openjdk-devel     #安装JDK11环境

#maven环境变量
[root@jenkins ~]# vim /etc/bashrc       #文件末尾添加
...
export MAVEN_HOME="/usr/local/maven"
export PATH=${MAVEN_HOME}/bin/:$PATH
[root@jenkins ~]# source /etc/bashrc
[root@jenkins ~]# mvn -v                #测试Maven指令
Apache Maven 3.6.3 (cecedd343002696d0abb50b32b541b8a6ba2883f)
Maven home: /usr/local/maven
Java version: 11.0.20, vendor: Red Hat, Inc., runtime: /usr/lib/jvm/java-11-openjdk-11.0.19.0.7-4.el8.x86_64
Default locale: en_US, platform encoding: ANSI_X3.4-1968
OS name: "linux", version: "4.18.0-372.26.1.el8_6.x86_64", arch: "amd64", family: "unix"


#配置Maven本地仓库
[root@jenkins ~]# vim /usr/local/maven/conf/settings.xml 
 55   <localRepository>/usr/local/maven/repository/</localRepository>

直接解压/root/repository.tar.gz，把里面的内容放到/usr/local/maven/即可
[root@jenkins ~]# tar -xf /root/repository.tar.gz -C /usr/local/maven/
```

#### 2、代码准备

给jenkins主机绑定公网IP，拉取代码（由于之前已经有购买公网ip经验，绑定多次，此次不再重复截图）

```bash
拉取代码
[root@jenkins ~]# yum -y install git
[root@jenkins ~]# git clone https://gitee.com/cc-0001/tea
[root@jenkins ~]# ls tea/code/page/
admin-page              #网站商品管理页面
front-page              #网站商品展示页面

[root@jenkins ~]# ls tea/code/backend/
tarena-passport         #验证码中心组件
tarena-tp-tea           #商品组件(管理组件，服务组件)
```

##### 2.1、商品服务组件

```bash
#编写配置文件
[root@jenkins ~]# cd /root/tea/code/backend/tarena-tp-tea/
[root@jenkins tarena-tp-tea]# vim tea-server/tea-server-main/src/main/resources/application-vm.yaml  
spring:
  # 数据源配置
  datasource:
    url: jdbc:mysql://192.168.1.12:3306/tarena_tp_tea?useSSL=false&useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai&allowMultiQueries=true&allowPublicKeyRetrieval=true
    username: teauser
    password: Taren123
    driver-class-name: com.mysql.cj.jdbc.Driver
    hikari:
      minimum-idle: 1
      auto-commit: true
      idle-timeout: 600000
      minimum: 5
      max-lifetime: 1800000
      #数据库连接超时时间,默认30秒，即30000
      connection-timeout: 120000
      connection-test-query: SELECT 1 FROM DUAL

  redis:
    host: 192.168.1.147    #此地址写自己redis的负载均衡地址
    port: 6379
    password: "Taren123"
    database: 8
    jedis:
      pool:
        max-active: 8
        min-idle: 5
        max-idle: 8
        max-wait: PT15M
        time-between-eviction-runs: PT15M
rocketmq:
  name-server: 192.168.1.14:9876
  producer:
    group: ${spring.application.name}


mq:
  auto-close-delay-level: 15
  group:
    auto-close: order-auto-close-group
    success-paid: success-paid-group
  topic:
    pay-success-paid-topic: pay-success-paid
    order-cancel-topic: order-cancel-topic
    auto-close-topic: order-auto-close-topic
    order-success-paid-topic: order-success-paid-topic

pay:
  publicFilePath: '/usr/local/project/tea/publicKey.txt'
  redirectPath: 'https://dev-cs-pay-center.tctm.life/payCenter/queryOrderInfo'


#url相关
image_path: /home/images/vm/tea_attach/
url: http://122.9.48.80:30080/tea_attach/   #写自己华为云负载均衡ELB的IP


jwt:
  rsa:
    enabled: true
    private-key: |
      -----BEGIN PRIVATE KEY-----
      MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBANAn5giVT7oy/drc
      o0evOFJUYSnHLbYtXG4MUuEUM2QdGX9d1q3y5k9orwX3wrt4WP4gIkQdB0EzQApp
      WZMUbbVsibVWkVSTQ5aInPgjLjfaDlcP5U4Z1hh3QiPZ01i3AWYY8HS+2SBIlRLH
      S5X0MjZkdbOVEPODU73JNSjFKz4XAgMBAAECgYBwXblt1LxPNYuYBBcYcVwkBWzW
      ErF4cJA+z+RFoJQFbTgAa3WiPUdagpZI0HCMpvDTbiFRR6JM7g+aDzjNju+RUCW9
      0iaDrB5vKyh3INMzcdCfm4btGectxb/ZCYFkXPBvqtqzK/H9ANk/Xlq1KFw0O2x2
      fgV7swYuiZ3kSYLbEQJBAPx2gXPkpraqEPmt//ce7AP5fHxC8Td2JFROsRfyweJP
      0dUdiLDgMxwpFZIhidU1sJBWX62Vqv6Jh/MvoMBhttsCQQDTEnqy9P+VAS1huYkl
      XwWjbZKxHEIaxW+Bg/ombXLhGudpy+chHFy4QEgA1Puda7kxgcTMhRj1i1EliytB
      ecR1AkEA9STJPNS4wURQKXG4y6f9+zoFk8+Y1IbmberfcWaqt2tUUEFWWpHJbz3B
      kVz5rstsgCsuyqo7GOBI323PBR+c4wJBAJxA3dyFSn+AM5xYwZKM2Zu2jhXXGYjA
      CGAU16aC97x1MkM704rLeEheLe+PvAU5rgtcSdgt3+BGlnf4orkB79ECQE2uNOTP
      enyfRVLgN0j3ZLSyOSqw+kqGsoU2jpcxFCGy0RGk3z1fHVccHfspqCA7AMj9jK5O
      3EULSLOmkXQHPLQ=
      -----END PRIVATE KEY-----
    expiration: 1000000
    public-key: |
      -----BEGIN PUBLIC KEY-----
      MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDQJ+YIlU+6Mv3a3KNHrzhSVGEp
      xy22LVxuDFLhFDNkHRl/Xdat8uZPaK8F98K7eFj+ICJEHQdBM0AKaVmTFG21bIm1
      VpFUk0OWiJz4Iy432g5XD+VOGdYYd0Ij2dNYtwFmGPB0vtkgSJUSx0uV9DI2ZHWz
      lRDzg1O9yTUoxSs+FwIDAQAB
      -----END PUBLIC KEY-----

gateway:
  whitelists:
      - /doc.html
      - /register/**
      - /**/*.html"
      - /*.html"
      - /favicon.ico
      - /**/*.html
      - /**/*.css
      - /**/*.js
      - /swagger-resources/**
      - /v2/api-docs/**
      - /sso/valid/code
      - /*.js
      - /**.html
      - /user/login
      - /menu/**/**
      - /content/**/**
      - /images/**
      
#打包测试；-D'maven.test.skip'=true：构建项目时跳过运行测试。通过设置为true，可以忽略测试环节，直接进行打包
[root@jenkins tarena-tp-tea]# mvn clean package -Dmaven.test.skip=true
...
[INFO] ------------------------------------------------------------------------
[INFO] Reactor Summary for tp-tea 1.0.0-SNAPSHOT:
[INFO] 
[INFO] tp-tea ............................................. SUCCESS [ 27.827 s]
[INFO] tea-common ......................................... SUCCESS [ 13.587 s]
[INFO] tea-po ............................................. SUCCESS [  0.943 s]
[INFO] tea-server ......................................... SUCCESS [  0.045 s]
[INFO] tea-server-protocol ................................ SUCCESS [ 23.567 s]
[INFO] tea-server-dao-api ................................. SUCCESS [ 24.172 s]
[INFO] tea-server-domain .................................. SUCCESS [ 17.005 s]
[INFO] tea-server-dao-impl ................................ SUCCESS [  0.038 s]
[INFO] tea-server-infrastructure .......................... SUCCESS [  0.448 s]
[INFO] tea-web-adapter .................................... SUCCESS [  5.663 s]
[INFO] tea-server-admin ................................... SUCCESS [ 43.449 s]
[INFO] tea-admin .......................................... SUCCESS [  0.015 s]
[INFO] tea-admin-protocol ................................. SUCCESS [  0.565 s]
[INFO] tea-admin-dao-api .................................. SUCCESS [  0.236 s]
[INFO] tea-admin-domain ................................... SUCCESS [  0.944 s]
[INFO] tea-admin-dao-impl ................................. SUCCESS [  0.042 s]
[INFO] tea-admin-infrastructure ........................... SUCCESS [  0.513 s]
[INFO] tea-admin-adapter .................................. SUCCESS [  0.626 s]
[INFO] tea-admin-main ..................................... SUCCESS [  3.421 s]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  02:44 min
[INFO] Finished at: 2023-04-16T02:04:22+08:00
[INFO] ------------------------------------------------------------------------

#查看结果并启动jar测试
#-Dfile.encoding=utf-8 是设置Java虚拟机的文件编码为UTF-8，
#-Xmx128M -Xms128M -Xmn64m -XX:MaxMetaspaceSize=128M -XX:MetaspaceSize=128M：配置Java虚拟机的内存分配和垃圾回收相关设置的
[root@jenkins tarena-tp-tea]# ls tea-server/tea-server-main/target/tea-server-admin-1.0.0-SNAPSHOT.jar
[root@jenkins tarena-tp-tea]# java -Dfile.encoding=utf-8 -jar tea-server/tea-server-main/target/tea-server-admin-1.0.0-SNAPSHOT.jar -Xmx128M -Xms128M -Xmn64m -XX:MaxMetaspaceSize=128M -XX:MetaspaceSize=128M --server.port=30091 --spring.profiles.active=vm


#创建存放图片的目录，解压图片tar包到此目录，同时也是为后期实验做准备
[root@jenkins tarena-tp-tea]# mkdir -p /home/images/vm/tea_attach/
[root@jenkins tarena-tp-tea]# tar -xf /root/tea/teaimg.tar.gz -C /home/images/vm/tea_attach/
```

##### 2.2、商品管理组件

```bash
#编写配置文件
[root@jenkins tarena-tp-tea]# vim tea-admin/tea-admin-main/src/main/resources/application-vm.yaml 
spring:
  # 数据源配置
  datasource:
    url: jdbc:mysql://192.168.1.12:3306/tarena_tp_tea?useSSL=false&useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai&allowMultiQueries=true&allowPublicKeyRetrieval=true
    username: teauser
    password: Taren123
    driver-class-name: com.mysql.cj.jdbc.Driver
    hikari:
      minimum-idle: 1
      auto-commit: true
      idle-timeout: 600000
      minimum: 5
      max-lifetime: 1800000
      #数据库连接超时时间,默认30秒，即30000
      connection-timeout: 120000
      connection-test-query: SELECT 1 FROM DUAL

  redis:
    host: 192.168.1.147    #写自已redis负载均衡的地址
    port: 6379
    password: "Taren123"
    database: 8
    jedis:
      pool:
        max-active: 8
        min-idle: 5
        max-idle: 8
        max-wait: PT15M
        time-between-eviction-runs: PT15M

#图片路径配置
image_path: /home/images/vm/tea_attach/
url: http://122.9.48.80:30080/tea_attach/   #写自己华为云负载均衡ELB的IP


rocketmq:
  name-server: 192.168.1.14:9876
  producer:
    group: ${spring.application.name}

mq:
  topic:
    order-cancel-topic: order-cancel-topic

business-mq:
  topic:
    stock-rollback: 'order-cancel-topic'
  group:
    rollback-listener-group: 'stock-rollback-listener-group'


jwt:
  rsa:
    enabled: true
    private-key: |
      -----BEGIN PRIVATE KEY-----
      MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBANAn5giVT7oy/drc
      o0evOFJUYSnHLbYtXG4MUuEUM2QdGX9d1q3y5k9orwX3wrt4WP4gIkQdB0EzQApp
      WZMUbbVsibVWkVSTQ5aInPgjLjfaDlcP5U4Z1hh3QiPZ01i3AWYY8HS+2SBIlRLH
      S5X0MjZkdbOVEPODU73JNSjFKz4XAgMBAAECgYBwXblt1LxPNYuYBBcYcVwkBWzW
      ErF4cJA+z+RFoJQFbTgAa3WiPUdagpZI0HCMpvDTbiFRR6JM7g+aDzjNju+RUCW9
      0iaDrB5vKyh3INMzcdCfm4btGectxb/ZCYFkXPBvqtqzK/H9ANk/Xlq1KFw0O2x2
      fgV7swYuiZ3kSYLbEQJBAPx2gXPkpraqEPmt//ce7AP5fHxC8Td2JFROsRfyweJP
      0dUdiLDgMxwpFZIhidU1sJBWX62Vqv6Jh/MvoMBhttsCQQDTEnqy9P+VAS1huYkl
      XwWjbZKxHEIaxW+Bg/ombXLhGudpy+chHFy4QEgA1Puda7kxgcTMhRj1i1EliytB
      ecR1AkEA9STJPNS4wURQKXG4y6f9+zoFk8+Y1IbmberfcWaqt2tUUEFWWpHJbz3B
      kVz5rstsgCsuyqo7GOBI323PBR+c4wJBAJxA3dyFSn+AM5xYwZKM2Zu2jhXXGYjA
      CGAU16aC97x1MkM704rLeEheLe+PvAU5rgtcSdgt3+BGlnf4orkB79ECQE2uNOTP
      enyfRVLgN0j3ZLSyOSqw+kqGsoU2jpcxFCGy0RGk3z1fHVccHfspqCA7AMj9jK5O
      3EULSLOmkXQHPLQ=
      -----END PRIVATE KEY-----
    expiration: 1000000
    public-key: |
      -----BEGIN PUBLIC KEY-----
      MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDQJ+YIlU+6Mv3a3KNHrzhSVGEp
      xy22LVxuDFLhFDNkHRl/Xdat8uZPaK8F98K7eFj+ICJEHQdBM0AKaVmTFG21bIm1
      VpFUk0OWiJz4Iy432g5XD+VOGdYYd0Ij2dNYtwFmGPB0vtkgSJUSx0uV9DI2ZHWz
      lRDzg1O9yTUoxSs+FwIDAQAB
      -----END PUBLIC KEY-----

gateway:
  whitelists:
    - /doc.html
    - /register/**
    - /**/*.html"
    - /*.html"
    - /favicon.ico
    - /**/*.html
    - /**/*.css
    - /**/*.js
    - /swagger-resources/**
    - /v2/api-docs/**
    - /sso/valid/code
    - /*.js
    - /**.html
    - /user/login
    - /menu/**/**
    - /content/**/**
    - /*.jpg
    - /*.jpeg
    - /*.png
    - /images/**
    
#打包测试
[root@jenkins tarena-tp-tea]# mvn clean     #清理测试文件
[root@jenkins tarena-tp-tea]# mvn clean package -D'maven.test.skip'=true
...
[INFO] ------------------------------------------------------------------------
[INFO] Reactor Summary for tp-tea 1.0.0-SNAPSHOT:
[INFO] 
[INFO] tp-tea ............................................. SUCCESS [  0.081 s]
[INFO] tea-common ......................................... SUCCESS [  0.004 s]
[INFO] tea-po ............................................. SUCCESS [  0.003 s]
[INFO] tea-server ......................................... SUCCESS [  0.003 s]
[INFO] tea-server-protocol ................................ SUCCESS [  0.003 s]
[INFO] tea-server-dao-api ................................. SUCCESS [  0.004 s]
[INFO] tea-server-domain .................................. SUCCESS [  0.005 s]
[INFO] tea-server-dao-impl ................................ SUCCESS [  0.004 s]
[INFO] tea-server-infrastructure .......................... SUCCESS [  0.003 s]
[INFO] tea-web-adapter .................................... SUCCESS [  0.007 s]
[INFO] tea-server-admin ................................... SUCCESS [  0.015 s]
[INFO] tea-admin .......................................... SUCCESS [  0.004 s]
[INFO] tea-admin-protocol ................................. SUCCESS [  0.002 s]
[INFO] tea-admin-dao-api .................................. SUCCESS [  0.006 s]
[INFO] tea-admin-domain ................................... SUCCESS [  0.006 s]
[INFO] tea-admin-dao-impl ................................. SUCCESS [  0.004 s]
[INFO] tea-admin-infrastructure ........................... SUCCESS [  0.003 s]
[INFO] tea-admin-adapter .................................. SUCCESS [  0.009 s]
[INFO] tea-admin-main ..................................... SUCCESS [  0.007 s]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  1.065 s
[INFO] Finished at: 2023-04-16T02:11:35+08:00
[INFO] ------------------------------------------------------------------------

查看结果并启动jar测试
[root@jenkins tarena-tp-tea]# ls tea-admin/tea-admin-main/target/tea-admin-main-1.0.0-SNAPSHOT.jar
[root@jenkins tarena-tp-tea]# java -Dfile.encoding=utf-8 -jar tea-admin/tea-admin-main/target/tea-admin-main-1.0.0-SNAPSHOT.jar -Xmx128M -Xms128M -Xmn64m -XX:MaxMetaspaceSize=128M -XX:MetaspaceSize=128M --server.port=30092 --spring.profiles.active=vm

#清理测试文件
[root@jenkins tarena-tp-tea]# mvn clean
```

##### 2.3、验证码中心组件

```bash
#编写配置文件
[root@jenkins tarena-tp-tea]#cd /root/tea/code/backend/tarena-passport/
[root@jenkins tarena-passport]# vim passport-provider/src/main/resources/application-vm.yml
jwt:
  rsa:
    enabled: true
    private-key: |
      -----BEGIN PRIVATE KEY-----
      MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBANAn5giVT7oy/drc
      o0evOFJUYSnHLbYtXG4MUuEUM2QdGX9d1q3y5k9orwX3wrt4WP4gIkQdB0EzQApp
      WZMUbbVsibVWkVSTQ5aInPgjLjfaDlcP5U4Z1hh3QiPZ01i3AWYY8HS+2SBIlRLH
      S5X0MjZkdbOVEPODU73JNSjFKz4XAgMBAAECgYBwXblt1LxPNYuYBBcYcVwkBWzW
      ErF4cJA+z+RFoJQFbTgAa3WiPUdagpZI0HCMpvDTbiFRR6JM7g+aDzjNju+RUCW9
      0iaDrB5vKyh3INMzcdCfm4btGectxb/ZCYFkXPBvqtqzK/H9ANk/Xlq1KFw0O2x2
      fgV7swYuiZ3kSYLbEQJBAPx2gXPkpraqEPmt//ce7AP5fHxC8Td2JFROsRfyweJP
      0dUdiLDgMxwpFZIhidU1sJBWX62Vqv6Jh/MvoMBhttsCQQDTEnqy9P+VAS1huYkl
      XwWjbZKxHEIaxW+Bg/ombXLhGudpy+chHFy4QEgA1Puda7kxgcTMhRj1i1EliytB
      ecR1AkEA9STJPNS4wURQKXG4y6f9+zoFk8+Y1IbmberfcWaqt2tUUEFWWpHJbz3B
      kVz5rstsgCsuyqo7GOBI323PBR+c4wJBAJxA3dyFSn+AM5xYwZKM2Zu2jhXXGYjA
      CGAU16aC97x1MkM704rLeEheLe+PvAU5rgtcSdgt3+BGlnf4orkB79ECQE2uNOTP
      enyfRVLgN0j3ZLSyOSqw+kqGsoU2jpcxFCGy0RGk3z1fHVccHfspqCA7AMj9jK5O
      3EULSLOmkXQHPLQ=
      -----END PRIVATE KEY-----
    expiration: 31536000
    public-key: |
      -----BEGIN PUBLIC KEY-----
      MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDQJ+YIlU+6Mv3a3KNHrzhSVGEp
      xy22LVxuDFLhFDNkHRl/Xdat8uZPaK8F98K7eFj+ICJEHQdBM0AKaVmTFG21bIm1
      VpFUk0OWiJz4Iy432g5XD+VOGdYYd0Ij2dNYtwFmGPB0vtkgSJUSx0uV9DI2ZHWz
      lRDzg1O9yTUoxSs+FwIDAQAB
      -----END PUBLIC KEY-----

spring:
  datasource:
    url: jdbc:mysql://192.168.1.12:3306/cs_mall_passport?useSSL=false&useUnicode=true&allowPublicKeyRetrieval=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai&allowMultiQueries=true
    username: cs_mall_user
    password: Taren123
  redis:
    host: 192.168.1.147    #写自已redis负载均衡的地址
    port: 6379
    password: "Taren123"
    database: 9
    jedis:
      pool:
        max-active: 8
        min-idle: 5
        max-idle: 8
        max-wait: PT15M
        time-between-eviction-runs: PT15M

  cloud:
    nacos:
      discovery:
        server-addr: 192.168.1.13:8848
        namespace: linux                #nacos命名空间（project）id
        enabled: true
        register-enabled: true
  application:
    name: passport-server
mybatis:
  mapper-locations: classpath:mapper/*.xml
logging:
  level:
    com.tarena.passport: trace
  file:
    name: logs/passport.log

#打包测试
[root@jenkins tarena-passport]# mvn clean package -D'maven.test.skip'=true
...
[INFO] passport-bom ....................................... SUCCESS [  0.228 s]
[INFO] passport-protocol .................................. SUCCESS [  2.154 s]
[INFO] passport-autoconfigure ............................. SUCCESS [03:57 min]
[INFO] passport-common .................................... SUCCESS [  0.766 s]
[INFO] passport-domain .................................... SUCCESS [  0.456 s]
[INFO] passport-adaptor ................................... SUCCESS [  0.520 s]
[INFO] passport-instruction ............................... SUCCESS [  0.320 s]
[INFO] passport-sdk ....................................... SUCCESS [  0.372 s]
[INFO] passport-main ...................................... SUCCESS [  0.262 s]
[INFO] passport-provider .................................. SUCCESS [  7.911 s]
[INFO] tarena-passport .................................... SUCCESS [  0.001 s]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  04:11 min
[INFO] Finished at: 2023-07-10T01:39:06+08:00
[INFO] ------------------------------------------------------------------------

查看结果并启动jar测试
[root@jenkins tarena-passport]# ls passport-provider/target/passport-provider-1.0-SNAPSHOT.jar
[root@jenkins tarena-passport]# java -Dfile.encoding=utf-8 -jar passport-provider/target/passport-provider-1.0-SNAPSHOT.jar -Xmx128M -Xms128M -Xmn64m -XX:MaxMetaspaceSize=128M -XX:MetaspaceSize=128M --server.port=30094 --spring.profiles.active=vm

#清理测试文件
[root@jenkins tarena-passport]# mvn clean
```
