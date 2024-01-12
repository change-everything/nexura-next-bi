import {
  Avatar,
  Button,
  Card,
  Col,
  Divider,
  Form,
  Input,
  List,
  message,
  Row,
  Select,
  Space,
  Spin,
  Upload,
} from 'antd';
import TextArea from 'antd/es/input/TextArea';
import React, { useEffect, useState } from 'react';
import { listChartByPageUsingPost } from '@/services/next-bi/chartController';
import ReactECharts from 'echarts-for-react';
import { getInitialState } from '@/app';
import { useModel } from '@umijs/max';
import Search from 'antd/es/input/Search';

const MyChart: React.FC = () => {
  const initSearchParams = {
    current: 1,
    pageSize: 10,
  };

  const [searchParams, setSearchParams] = useState<API.ChartQueryRequest>({
    ...initSearchParams,
  });

  const [chartList, setChartList] = useState<API.Chart[]>();
  const [total, setTotal] = useState<number>(0);
  const [loading, setLoading] = useState<boolean>(false);
  const { initialState } = useModel('@@initialState');
  const { currentUser } = initialState ?? {};
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
          <Card>
            <List.Item key={item.id}>
              <List.Item.Meta
                avatar={<Avatar src={currentUser?.userAvatar} />}
                title={item.name}
                description={'分析目标：' + item.goal}
              />
              <Row gutter={24}>
                <Col span={12}>
                  <Card>{item.genResult}</Card>
                </Col>
                <Col span={12}>
                  <ReactECharts option={item.genChart && JSON.parse(item.genChart)} />
                </Col>
              </Row>
            </List.Item>
          </Card>
        )}
      />
    </div>
  );
};
export default MyChart;
