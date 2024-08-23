windows或者linux的真机环境下载tea项目：https://gitee.com/cc-0001/tea，（必须下载）之后按照README.txt文件说明下载软件

在ecs-proxy执行以下命令，使用MD5校验软件下载是否正确  
[root@ecs-proxy ~]# yum -y install wget
[root@ecs-proxy ~]# wget -c 114.116.223.254/localrepo/package.tar.gz
[root@ecs-proxy ~]# md5sum package.tar.gz 
75f1c440a9b03fc2ad66fb066d498871  package.tar.gz
