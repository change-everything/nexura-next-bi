import { genChartByAiUsingPost } from '@/services/next-bi/chartController';
import { UploadOutlined } from '@ant-design/icons';
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
import Excel from 'exceljs'
import { HotTable } from '@handsontable/react';
import { registerAllModules } from 'handsontable/registry';
import 'handsontable/dist/handsontable.full.min.css';
import { textRenderer, registerRenderer } from 'handsontable/renderers';

const AddChart: React.FC = () => {
  const [chart, setChart] = useState<API.BiResponse>();
  const [submitting, setSubmitting] = useState<boolean>(false);
  const [option, setOption] = useState<any>();
  const [excelData, setExcelData] = useState([]);

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
      const file = values.file.file.originFileObj
      // const res = await genChartByAiUsingPost(params, {}, file);
      const workbook = new Excel.Workbook();
      await workbook.xlsx.load(file)

      // 第一个工作表
      const worksheet = workbook.getWorksheet(1);

      // 遍历工作表中的所有行（包括空行）
      const sheetData = [];
      worksheet.eachRow({ includeEmpty: true }, function(row, rowNumber) {
        console.log('Row ' + rowNumber + ' = ' + JSON.stringify(row.values));
        // 使用row.values获取每一行的值时总会多出一条空数据(第一条)，这里我把它删除
        const row_values = row.values.slice(1);
        sheetData.push(row_values)
      });
      setExcelData(sheetData);
      // console.log(res);
      // if (!res?.data) {
      //   message.error('生成失败');
      // } else {
      //   message.success('生成成功');
      //   const chartOption = JSON.parse(res.data.genChart ?? '');
      //   if (!chartOption) {
      //     throw new Error('图表代码解析错误');
      //   } else {
      //     setChart(res.data);
      //     setOption(chartOption);
      //   }
      // }
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
                  <Button htmlType="reset" loading={submitting} disabled={submitting}>
                    重置
                  </Button>
                </Space>
              </Form.Item>
            </Form>
          </Card>
          <Divider />
          <Card title="原始数据">
            {<div id='table_view'>
              <HotTable
                data={excelData}
                readOnly={true}
                rowHeaders={true}
                colHeaders={true}
                width="100vw"
                height="auto"
                licenseKey='non-commercial-and-evaluation'// 一定得加这个，handsontable是收费的，加了这个才能免费用
              />

            </div>}
            <Spin spinning={submitting} />
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
