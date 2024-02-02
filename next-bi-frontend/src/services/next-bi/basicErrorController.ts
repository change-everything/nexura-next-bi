// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

/** errorHtml GET /api/error */
export async function errorHtmlUsingGet(options?: { [key: string]: any }) {
  return request<API.ModelAndView>('/api/error', {
    method: 'GET',
    ...(options || {}),
  });
}

/** errorHtml PUT /api/error */
export async function errorHtmlUsingPut(options?: { [key: string]: any }) {
  return request<API.ModelAndView>('/api/error', {
    method: 'PUT',
    ...(options || {}),
  });
}

/** errorHtml POST /api/error */
export async function errorHtmlUsingPost(options?: { [key: string]: any }) {
  return request<API.ModelAndView>('/api/error', {
    method: 'POST',
    ...(options || {}),
  });
}

/** errorHtml DELETE /api/error */
export async function errorHtmlUsingDelete(options?: { [key: string]: any }) {
  return request<API.ModelAndView>('/api/error', {
    method: 'DELETE',
    ...(options || {}),
  });
}

/** errorHtml PATCH /api/error */
export async function errorHtmlUsingPatch(options?: { [key: string]: any }) {
  return request<API.ModelAndView>('/api/error', {
    method: 'PATCH',
    ...(options || {}),
  });
}
