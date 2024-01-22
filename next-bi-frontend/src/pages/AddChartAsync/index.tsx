import {
  genChartAsyncByAiUsingPost,
  genChartByAiUsingPost,
} from '@/services/next-bi/chartController';
import { ThunderboltFilled, UploadOutlined } from '@ant-design/icons';
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
import { useForm } from 'antd/es/form/Form';
import { HotTable } from '@handsontable/react';
import Excel from 'exceljs';
import { log } from 'handsontable/helpers';

const AddChart: React.FC = () => {
  const [form, setForm] = useForm();
  const [submitting, setSubmitting] = useState<boolean>(false);
  const [excelData, setExcelData] = useState([]);
  const formItemLayout = {
    labelCol: { span: 6 },
    wrapperCol: { span: 14 },
  };

  console.log(excelData);
  const onFinish = async (values: any) => {
    if (submitting) {
      return;
    }
    setSubmitting(true);
    const params = {
      ...values,
      file: undefined,
    };
    try {
      const res = await genChartAsyncByAiUsingPost(params, {}, values.file.file.originFileObj);
      console.log(res);
      if (!res?.data) {
        message.error('生成失败，' + res.message);
      } else {
        message.success('提交图表成功，稍后请在我的图表中查看');
        form.resetFields();
        setExcelData([]);
      }
    } catch (e: any) {
      message.error('生成失败, ' + e.message);
    }
    setSubmitting(false);
  };

  return (
    <div className="addChartAsync">
      <Row gutter={24}>
        <Col span={12}>
          <Card title="智能分析" style={{ width: '100%', height: '100%' }}>
            <Form
              form={form}
              name="validate_other"
              {...formItemLayout}
              onFinish={onFinish}
              initialValues={{}}
            >
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
                    提交 (<ThunderboltFilled /> 5积分)
                  </Button>
                  <Button htmlType="reset" loading={submitting} disabled={submitting}>
                    重置
                  </Button>
                </Space>
              </Form.Item>
            </Form>
          </Card>
        </Col>
        <Col span={12}>
          <Card title="原始数据">
            <div id="table_view" style={{ width: '100%', height: '100%' }}>
              {excelData.length > 0 ? (
                <HotTable
                  data={excelData}
                  readOnly={true}
                  rowHeaders={true}
                  colHeaders={true}
                  width="100%"
                  height="80vh"
                  licenseKey="non-commercial-and-evaluation" // 一定得加这个，handsontable是收费的，加了这个才能免费用
                />
              ) : (
                <div>请上传excel文件</div>
              )}
            </div>
          </Card>
        </Col>
      </Row>
    </div>
  );
};
export default AddChart;
