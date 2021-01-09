# Open-his医疗管理云平台

> 基于Springboot+Mybatis-Plus的分布式依赖管理平台

该项目包含系统管理模块、生产进销管理模块、排班挂号模块、数据统计模块，共计4个模块，每个模块负责各自的功能，除系统管理模块外，其他三个模块的功能都通过dubbo进行远程调用来完成

## Installation

后台文件：

1. 导入数据库sql文件
2. 修改Open-his文件夹中，每个模块的配置文件
   - 修改mysql的URL
   - 修改redis的URL
   - 修改zookeeper的URL
3. 运行`mvn package`，将四个模块打包，之后运行四个jar，即可启动后台

前台文件：


1. `npm install`安装相关依赖
2. `npm run build`完成打包，将打包好的文件上传到nginx，并配置反向代理
3. 或`npm run dev`在线运行

## 使用到的框架

- Springboot
- SpringMVC
- Myabatis-Plus
- Shiro
- Redis
- Dubbo
- VUE
- Element-UI

> 前端模板为vue-element-admin
