1.导入sql文件，在docs目录中vjshop.sql
2.执行 mvn eclipse:eclipse 并导入项目文件
3.修改vjshop.properties 将数据库链接用户名和密码改掉
4.执行mvn jetty:run 或者mvn tomcat7:run 可以运行项目
5.进入后台管理界面 http://localhost:8080/admin 用户名 admin 密码 123456
6.在后台管理生成 首页，及产品静态文件 内容--》 静态化管理--》生成类型，选择 首页、文章、商品 