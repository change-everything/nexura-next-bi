// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

/** sendMessageToAllClient POST /api/sse/broadcast */
export async function sendMessageToAllClientUsingPost(
  body: string,
  options?: { [key: string]: any },
) {
  return request<any>('/api/sse/broadcast', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** closeConnect GET /api/sse/closeConnect */
export async function closeConnectUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.closeConnectUsingGETParams,
  options?: { [key: string]: any },
) {
  return request<any>('/api/sse/closeConnect', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** createConnect GET /api/sse/createConnect */
export async function createConnectUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.createConnectUsingGETParams,
  options?: { [key: string]: any },
) {
  return request<API.SseEmitter>('/api/sse/createConnect', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** sendMessageToOneClient POST /api/sse/sendMessage */
export async function sendMessageToOneClientUsingPost(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.sendMessageToOneClientUsingPOSTParams,
  options?: { [key: string]: any },
) {
  return request<any>('/api/sse/sendMessage', {
    method: 'POST',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}
