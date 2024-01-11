export default [
  { path: '/user', layout: false, routes: [{ path: '/user/login', component: './User/Login' }] },
  { path: '/', redirect: '/add_chart' },
  { path: '/add_chart', name: '智能分析', icon: 'areaChart', component: './AddChart' },
  {
    path: '/admin',
    icon: 'crown',
    access: 'canAdmin',
    routes: [
      { path: '/admin', name: '管理页', redirect: '/admin/sub-page' },
      { path: '/admin/sub-page', name: '子管理页', component: './Admin' },
    ],
  },
  { icon: 'table', path: '/list', component: './TableList' },
  { path: '/', redirect: '/welcome' },
  { path: '*', layout: false, component: './404' },
];
