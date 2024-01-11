import { genChartByAiUsingPost } from '@/services/next-bi/chartController';
import { UploadOutlined } from '@ant-design/icons';
import { useEmotionCss } from '@ant-design/use-emotion-css';
import {
  Button,
  Card,
  Col,
  Divider,
  Form,
  Input,
  message,
  Row,
  Select,
  Space,
  Spin,
  Upload,
} from 'antd';
import TextArea from 'antd/es/input/TextArea';
import React, { useState } from 'react';
import ReactECharts from 'echarts-for-react';

const AddChart: React.FC = () => {
  const [chart, setChart] = useState<API.BiResponse>();
  const [submitting, setSubmitting] = useState<boolean>(false);
  const [option, setOption] = useState<any>();

  const formItemLayout = {
    labelCol: { span: 6 },
    wrapperCol: { span: 14 },
  };

  const onFinish = async (values: any) => {
    if (submitting) {
      return;
    }
    setSubmitting(true);
    setOption(undefined);
    setChart(undefined);
    const params = {
      ...values,
      file: undefined,
    };
    try {
      const res = await genChartByAiUsingPost(params, {}, values.file.file.originFileObj);
      console.log(res);
      if (!res?.data) {
        message.error('生成失败');
      } else {
        message.success('生成成功');
        const chartOption = JSON.parse(res.data.genChart ?? '');
        if (!chartOption) {
          throw new Error('图表代码解析错误');
        } else {
          setChart(res.data);
          setOption(chartOption);
        }
      }
    } catch (e: any) {
      message.error('生成失败, ' + e.message);
    }
    setSubmitting(false);
  };

  return (
    <div className="addChart">
      <Row gutter={24}>
        <Col span={12}>
          <Card title="智能分析">
            <Form name="validate_other" {...formItemLayout} onFinish={onFinish} initialValues={{}}>
              <Form.Item
                name="goal"
                label="分析目标"
                rules={[{ required: true, message: '请输入分析目标!' }]}
              >
                <TextArea placeholder="请输入你的分析需求，比如：请帮我分析人口增长速率" />
              </Form.Item>

              <Form.Item
                name="name"
                label="图表名称"
                rules={[{ required: true, message: '请输入图表名称!' }]}
              >
                <Input placeholder="请输入你的图表名称" />
              </Form.Item>

              <Form.Item name="chartType" label="图表类型" hasFeedback>
                <Select
                  options={[
                    { value: '柱状图', label: '柱状图' },
                    { value: '折线图', label: '折线图' },
                    { value: '堆叠图', label: '堆叠图' },
                    { value: '饼图', label: '饼图' },
                    { value: '雷达图', label: '雷达图' },
                  ]}
                ></Select>
              </Form.Item>

              <Form.Item name="file" label="原始数据">
                <Upload name="file">
                  <Button icon={<UploadOutlined />}>上传CSV文件</Button>
                </Upload>
              </Form.Item>

              <Form.Item wrapperCol={{ span: 12, offset: 6 }}>
                <Space>
                  <Button
                    type="primary"
                    htmlType="submit"
                    loading={submitting}
                    disabled={submitting}
                  >
                    提交
                  </Button>
                  <Button htmlType="reset">重置</Button>
                </Space>
              </Form.Item>
            </Form>
          </Card>
        </Col>
        <Col span={12}>
          <Card title="分析结论">
            {chart?.genResult ?? <div>请先在左侧提交数据</div>}
            <Spin spinning={submitting} />
          </Card>
          <Divider />
          <Card title="可视化图表">
            {option ? <ReactECharts option={option} /> : <div>请先在左侧提交数据</div>}
            <Spin spinning={submitting} />
          </Card>
        </Col>
      </Row>
    </div>
  );
};
export default AddChart;
