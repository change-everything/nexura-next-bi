import Footer from '@/components/Footer';
import SseComponent from '@/components/SseComponent';
import { getLoginUserUsingGet } from '@/services/next-bi/userController';
import type { RunTimeLayoutConfig } from '@umijs/max';
import { history } from '@umijs/max';
import defaultSettings from '../config/defaultSettings';
import logo from '../public/logo.png';
import { AvatarDropdown, AvatarName } from './components/RightContent/AvatarDropdown';
import { errorConfig } from './requestErrorConfig';
const loginPath = '/user/login';

/**
 * @see  https://umijs.org/zh-CN/plugins/plugin-initial-state
 *
 */
export async function getInitialState(): Promise<{
  currentUser?: API.LoginUserVO;
}> {
  const fetchUserInfo = async () => {
    try {
      const res = await getLoginUserUsingGet();
      return res.data;
    } catch (error) {
      history.push(loginPath);
    }
    return undefined;
  };
  // 如果不是登录页面，执行
  const { location } = history;
  if (location.pathname !== loginPath) {
    const currentUser = await fetchUserInfo();
    return {
      currentUser,
    };
  }
  return {};
}

// ProLayout 支持的api https://procomponents.ant.design/components/layout
// @ts-ignore
export const layout: RunTimeLayoutConfig = ({ initialState, setInitialState }) => {
  return {
    logo: logo,
    avatarProps: {
      src: initialState?.currentUser?.userAvatar,
      title: <AvatarName />,
      render: (_, avatarChildren) => {
        return <AvatarDropdown>{avatarChildren}</AvatarDropdown>;
      },
    },
    waterMarkProps: {
      content: initialState?.currentUser?.userName,
    },
    footerRender: () => <Footer />,
    onPageChange: () => {
      const { location } = history;
      // 如果没有登录，重定向到 login
      if (!initialState?.currentUser && location.pathname !== loginPath) {
        history.push(loginPath);
      }
    },
    menuHeaderRender: undefined,
    // 自定义 403 页面
    // unAccessible: <div>unAccessible</div>,
    // 增加一个 loading 的状态
    childrenRender: (children) => {
      // if (initialState?.loading) return <PageLoading />;
      return (
        <>
          {children}
          <SseComponent />
        </>
      );
    },
    ...defaultSettings,
  };
};

/**
 * @name request 配置，可以配置错误处理
 * 它基于 axios 和 ahooks 的 useRequest 提供了一套统一的网络请求和错误处理方案。
 * @doc https://umijs.org/docs/max/request#配置
 */
export const request = {
  baseURL: 'http://localhost:8123',
  withCredentials: true,
  ...errorConfig,
};
