FROM openjdk:8-jdk-alpine
#项目的 jar 文件作为 “app.jar” 添加到容器中；
ADD  next-bi-backend-0.0.1.jar  app.jar
# Image创建容器时的初始化内存，最大内存，及启动时使用的profile. -c为清除以前启动的数据
ENTRYPOINT ["java","-Xms1024m","-Xmx1024m","-jar","/app.jar","--spring.profiles.active=prod","-c"]