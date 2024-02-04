FROM nginx:alpine

ENV TZ=Asia/Shanghai

COPY ./nginx.conf /etc/nginx/conf.d/default.conf
COPY /dist /usr/share/nginx/html

EXPOSE 28080
