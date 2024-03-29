export default [
  { path: '/user', layout: false, routes: [{ path: '/user/login', component: './User/Login' }] },
  { path: '/', redirect: '/add_chart_async' },
  // { path: '/add_chart', name: '智能分析', icon: 'areaChart', component: './AddChart' },
  { path: '/detail_chart/:id', icon: 'areaChart', component: './DetailChart' },
  // { path: '/testSse', name: '123', icon: 'areaChart', component: '@/components/SseComponent' },
  {
    path: '/add_chart_async',
    name: '智能分析',
    icon: 'radarChart',
    component: './AddChartAsync',
  },
  { path: '/my_chart', name: '我的图表', icon: 'pieChart', component: './MyChart' },
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
