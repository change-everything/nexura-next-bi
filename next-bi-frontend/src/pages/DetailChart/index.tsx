import {
  getChartByIdUsingGet,
  reGenChartByAiAsyncUsingPost,
} from '@/services/next-bi/chartController';
import { UploadOutlined } from '@ant-design/icons';
import { HotTable } from '@handsontable/react';
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
import { useForm } from 'antd/es/form/Form';
import TextArea from 'antd/es/input/TextArea';
import ReactECharts from 'echarts-for-react';
import Excel from 'exceljs';
import 'handsontable/dist/handsontable.full.min.css';
import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';

const AddChart: React.FC = () => {
  const [chart] = useForm<API.Chart>();
  const [submitting, setSubmitting] = useState<boolean>(false);
  const [option, setOption] = useState<any>();
  const [excelData, setExcelData] = useState([]);

  const formItemLayout = {
    labelCol: { span: 6 },
    wrapperCol: { span: 14 },
  };

  const routeParams = useParams();
  const chartId = routeParams['id'];

  const loadData = async () => {
    if (submitting) {
      return;
    }
    setSubmitting(true);
    setOption(undefined);
    chart.resetFields();
    try {
      const res = await getChartByIdUsingGet({ id: chartId });
      console.log(res);
      if (!res?.data) {
        message.error('查询失败');
      } else {
        const chartOption = JSON.parse(res.data.genChart ?? '{}');

        // 遍历工作表中的所有行（包括空行）
        const chartData: string = res?.data?.chartData;
        const sheetData = JSON.parse(chartData);

        setExcelData(sheetData);
        if (!chartOption) {
          throw new Error('图表代码解析错误');
        } else {
          chart.setFieldsValue(res.data);
          setOption(chartOption);
        }
      }
    } catch (e: any) {
      message.error('查询失败, ' + e.message);
    }
    setSubmitting(false);
  };

  useEffect(() => {
    loadData();
  }, [chartId]);

  const onFinish = async (values: any) => {
    if (submitting) {
      return;
    }
    setSubmitting(true);
    const params = {
      ...values,
      chartData: excelData,
      id: chartId,
      genResult: chart.getFieldValue('genResult'),
    };
    try {
      const res = await reGenChartByAiAsyncUsingPost(params, {});
      console.log(res);
      if (!res?.data) {
        message.error('生成失败, ' + res.message);
      } else {
        message.success('提交图表成功，稍后请在我的图表中查看');
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
            <Form name="validate_other" {...formItemLayout} onFinish={onFinish} form={chart}>
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
                    { value: '散点图', label: '散点图' },
                    { value: '盒须图', label: '盒须图' },
                    { value: '关系图', label: '关系图' },
                    { value: '漏斗图', label: '漏斗图' },
                    { value: '树图', label: '树图' },
                    { value: 'K线图', label: 'K线图' },
                    { value: '旭日图', label: '旭日图' },
                  ]}
                ></Select>
              </Form.Item>

              <Form.Item name="file" label="原始数据">
                <Upload
                  name="file"
                  beforeUpload={async (file, FileList) => {
                    console.log(file);
                    // const res = await genChartByAiUsingPost(params, {}, file);
                    const workbook = new Excel.Workbook();
                    await workbook.xlsx.load(file);

                    // 第一个工作表
                    const worksheet = workbook.getWorksheet(1);

                    // 遍历工作表中的所有行（包括空行）
                    const sheetData = [];
                    worksheet.eachRow({ includeEmpty: true }, function (row, rowNumber) {
                      console.log('Row ' + rowNumber + ' = ' + JSON.stringify(row.values));
                      // 使用row.values获取每一行的值时总会多出一条空数据(第一条)，这里我把它删除
                      const row_values = row.values.slice(1);
                      sheetData.push(row_values);
                    });
                    setExcelData(sheetData);
                  }}
                >
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
                    重新生成
                  </Button>
                  <Button htmlType="reset" loading={submitting} disabled={submitting}>
                    重置
                  </Button>
                </Space>
              </Form.Item>
            </Form>
          </Card>
          <Divider />
          <Card title="原始数据">
            {
              <div id="table_view">
                <HotTable
                  data={excelData}
                  readOnly={true}
                  rowHeaders={true}
                  colHeaders={true}
                  height={500} //默认表格高度
                  stretchH={'all'} //自适应
                  contextMenu={false} //右键菜单
                  columnSorting={true} //允许排序
                  licenseKey="non-commercial-and-evaluation" // 一定得加这个，handsontable是收费的，加了这个才能免费用
                />
              </div>
            }
            <Spin spinning={submitting} />
          </Card>
        </Col>
        <Col span={12}>
          <Card title="分析结论">
            {chart.getFieldValue('genResult') ?? <div>您的图表暂未分析成功</div>}
            <Spin spinning={submitting} />
          </Card>
          <Divider />
          <Card title="可视化图表">
            {option ? <ReactECharts option={option} /> : <div>您的图表暂未分析成功</div>}
            <Spin spinning={submitting} />
          </Card>
        </Col>
      </Row>
    </div>
  );
};
export default AddChart;
