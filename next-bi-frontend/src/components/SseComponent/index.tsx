import { ExclamationCircleOutlined } from '@ant-design/icons';
import { useModel } from '@umijs/max';
import { notification } from 'antd';
import React, { useEffect, useRef } from 'react';

const openNotification = (msg: string) => {
  notification.open({
    message: '系统消息',
    description: msg,
    icon: <ExclamationCircleOutlined style={{ color: '#108ee9' }} />,
    duration: 5,
  });
};

const SseComponent: React.FC = () => {
  const { initialState } = useModel('@@initialState');
  const { currentUser } = initialState || {};
  useRef<EventSource | null>(null);
  useEffect(() => {
    const initSee = async () => {
      if (window.EventSource) {
        const source = new EventSource(
          `http://119.3.252.5:28080/api/sse/createConnect?clientId=${currentUser?.id}`,
        );

        // 监听打开事件
        source.addEventListener('open', (e) => {
          console.log('打开连接 onopen==>', e);
          // openNotification('建立连接成功')
        });

        // 监听消息事件
        source.addEventListener('message', (e) => {
          console.log(e.data);
          const data = JSON.parse(e.data);
          const code = data.code;
          const msg = data.data;

          if (code === 200) {
            openNotification(msg);
          } else if (code === 0) {
            // 然后状态码为000 把客户端id储存在本地
            localStorage.setItem('clientId', msg);
          }
        });

        // 监听错误事件
        source.addEventListener('error', () => {
          openNotification('已断开与后端连接');
        });

        // 关闭连接
        // @ts-ignore
        source.close = function (e: any) {
          console.log('断开 οnerrοr==>', e);
        };
      } else {
        alert('该浏览器不支持sse');
      }
    };
    initSee();
  }, []);
  return null;
};

export default SseComponent;
