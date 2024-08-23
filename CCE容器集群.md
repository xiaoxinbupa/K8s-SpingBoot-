### CCE容器集群

#### 1、华为云创建CCE

![image-20240823193715732](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240823193715732.png)

![image-20240823193753648](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240823193753648.png)

![image-20240823193803030](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240823193803030.png)

![image-20240823193813021](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240823193813021.png)

![image-20240823193823331](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240823193823331.png)

取消安装的插件

![image-20240823193849307](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240823193849307.png)

![image-20240823193902158](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240823193902158.png)

点击返回集群管理

![image-20240823193920886](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240823193920886.png)

此集群创建好之后，还不能用，还没有节点，需要创建节点

![image-20240823193935494](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240823193935494.png)

![image-20240823193949355](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240823193949355.png)

密码为Taren123

![image-20240823194002712](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240823194002712.png)

![image-20240823194042707](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240823194042707.png)

![image-20240823194055739](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240823194055739.png)

点击返回节点列表页面

此时k8s集群已经搭建完毕，有三个node节点，使用cce搭建的k8s集群master节点是无法连接的，可以使用kubectl管理集群，找一台云主机比如jumpserver主机管理

#### 2、使用kubectl访问k8s-cce

![image-20240823194124380](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240823194124380.png)

![image-20240823194142501](https://gitee.com/xiaoxinbupa/linux-note/raw/master/linux_base_picture/image-20240823194142501.png)

```bash
需要先下载kubectl以及配置文件，拷贝到jumpserver主机，完成配置后，即可使用kubectl访问k8s集群

1、下载kubectl：下载与集群版本对应的或者更新的kubectl（无须下载）

2、复制粘贴kubectl配置文件内容，此内容在上图 配置 中可以找到

3、安装和配置kubectl：解压kubernetes-client-linux-amd64.tar包
[root@jumpserver ~]# tar -xf /root/package/kubernetes-client-linux-amd64.tar.gz 
[root@jumpserver ~]# mv /root/kubernetes/client/bin/kubectl /usr/local/bin/

4、登录到jumpserver主机，配置kubectl，若是root用户登录,可以直接执行
[root@jumpserver ~]# mkdir $HOME/.kube
[root@jumpserver ~]# vim $HOME/.kube/config  
...按照要求复制粘贴内容...

5、配置kubectl的tab键，节省输入时间
[root@jumpserver ~]# kubectl completion bash >/etc/bash_completion.d/kubectl
[root@jumpserver ~]# exit                  #退出重新登录
[root@jumpserver ~]# kubectl get nodes      #可以看到结果
NAME          STATUS   ROLES    AGE     VERSION
192.168.1.5   Ready    <none>   9m41s   v1.28.6-r0-28.0.24.3
192.168.1.6   Ready    <none>   9m32s   v1.28.6-r0-28.0.24.3
192.168.1.7   Ready    <none>   9m42s   v1.28.6-r0-28.0.24.3
```

#### 3、配置集群访问harbor镜像仓库

```bash
通过jumpserver主机连接三台k8s计算节点，更改hosts文件，做主机名和IP地址解析，更改containerd配置文件，使其后期可以下载镜像，三台机器都需要配置（以其中一台为例）
[root@jumpserver ~]# kubectl get nodes      #获取节点IP地址
NAME          STATUS   ROLES    AGE   VERSION
192.168.1.5   Ready    <none>   10m   v1.28.6-r0-28.0.24.3
192.168.1.6   Ready    <none>   10m   v1.28.6-r0-28.0.24.3
192.168.1.7   Ready    <none>   10m   v1.28.6-r0-28.0.24.3

[root@jumpserver ~]# ssh 192.168.1.5
[root@node-pkkea ~]# vim /etc/hosts
192.168.1.30 harbor

[root@node-pkkea ~]# vim /etc/containerd/config.toml    
122           endpoint = ["https://08fd0a6fce0026040ffdc0158fe37d60.mirror.swr.myhuaweicloud.com"]
123         [plugins."io.containerd.grpc.v1.cri".registry.mirrors."harbor:443"]    #新添加
124           endpoint = ["harbor:443"]                                         #新添加
125       [plugins."io.containerd.grpc.v1.cri".registry.configs]                   #新添加
126         [plugins."io.containerd.grpc.v1.cri".registry.configs."harbor:443".tls] #新添加
127           insecure_skip_verify = true                                       #新添加
[root@node-pkkea ~]# systemctl restart containerd
```

###