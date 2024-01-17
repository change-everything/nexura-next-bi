import { Avatar, Card, List, message, Result, Space, Typography } from 'antd';
import React, { useEffect, useState } from 'react';
import { listChartByPageUsingPost } from '@/services/next-bi/chartController';
import ReactECharts from 'echarts-for-react';
import { useModel } from '@umijs/max';
import Search from 'antd/es/input/Search';

import { useNavigate } from 'react-router-dom';

const MyChart: React.FC = () => {
  const initSearchParams = {
    current: 1,
    pageSize: 10,
    sortField: 'createTime',
    sortOrder: 'desc',
  };

  const [searchParams, setSearchParams] = useState<API.ChartQueryRequest>({
    ...initSearchParams,
  });

  const { Paragraph, Text } = Typography;
  const [chartList, setChartList] = useState<API.Chart[]>();
  const [total, setTotal] = useState<number>(0);
  const [loading, setLoading] = useState<boolean>(false);
  const { initialState } = useModel('@@initialState');
  const { currentUser } = initialState ?? {};
  const route = useNavigate();
  const loadData = async () => {
    setLoading(true);
    try {
      const res = await listChartByPageUsingPost(searchParams);
      if (res.data) {
        setChartList(res.data.records ?? []);
        setTotal(res.data.total ?? 0);
      } else {
        message.error('获取图表失败');
      }
    } catch (e) {
      message.error('获取图表失败, ' + e.message);
    }
    setLoading(false);
  };

  useEffect(() => {
    loadData();
  }, [searchParams]);

  return (
    <div className="myChart">
      <div style={{ marginBottom: 16 }}>
        <Search
          loading={loading}
          placeholder="请输入图表名称"
          allowClear
          enterButton="搜索"
          size="large"
          onSearch={(value) => {
            setSearchParams({
              ...initSearchParams,
              name: value,
            });
          }}
        />
      </div>
      <List
        grid={{
          gutter: 16,
          xs: 1,
          sm: 1,
          md: 1,
          lg: 2,
          xl: 2,
          xxl: 2,
        }}
        itemLayout="vertical"
        loading={loading}
        size="large"
        pagination={{
          onChange: (page, pageSize) => {
            setSearchParams({
              ...searchParams,
              current: page,
              pageSize,
            });
          },
          current: searchParams.current,
          pageSize: searchParams.pageSize,
          total: total,
        }}
        dataSource={chartList}
        renderItem={(item) => (
          <div
            style={{ cursor: 'pointer' }}
            onClick={() => {
              route({ pathname: `/detail_chart/${item.id}` });
            }}
          >
            <Card style={{ height: '65vh' }}>
              <List.Item key={item.id}>
                <List.Item.Meta
                  avatar={<Avatar src={currentUser?.userAvatar} />}
                  title={item.name}
                  description={'分析目标：' + item.goal}
                />
                <>
                  {item.status === 'succeed' && (
                    <>
                      <Space direction="vertical" size="middle" style={{ display: 'flex' }}>
                        <Text
                          style={item.genResult ? { width: '100%' } : undefined}
                          ellipsis={item.genResult ? { tooltip: item.genResult } : false}
                        >
                          {item.genResult}
                        </Text>

                        <ReactECharts option={item.genChart && JSON.parse(item.genChart)} />
                      </Space>
                    </>
                  )}
                  {item.status === 'failed' && (
                    <>
                      <Result status="error" title="图表生成失败" subTitle={item.execMessage} />
                    </>
                  )}
                  {item.status === 'wait' && (
                    <>
                      <Result
                        status="warning"
                        title="待生成"
                        subTitle={item.execMessage ?? '当前任务繁忙，请耐心等待'}
                      />
                    </>
                  )}
                  {item.status === 'running' && (
                    <>
                      <Result status="info" title="图表生成中" subTitle={item.execMessage} />
                    </>
                  )}
                </>
              </List.Item>
            </Card>
          </div>
        )}
      />
    </div>
  );
};
export default MyChart;
