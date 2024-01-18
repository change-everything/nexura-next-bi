INSERT INTO next_bi_db.chart (goal, chart_data, chart_type, gen_chart, gen_result, user_id, create_time, update_time, is_delete, name, status, exec_message) VALUES ('请帮我分析一下我的网站还能存活多久,请使用堆叠图', '日期,人数
1,10
2,20
3,30
', '堆叠图', '{
    "tooltip": {},
    "xAxis": {
        "type": "category",
        "data": ["1", "2", "3"]
    },
    "yAxis": {
        "type": "value"
    },
    "series": [{
        "name": "人数",
        "type": "bar",
        "data": [10, 20, 30],
        "stack": "总量"
    }]
}', '根据提供的数据，我们可以使用堆叠图来可视化网站的生存情况。通过图表可以直观地得出以下结论：

1. 网站在1号当天有10人访问，2号有20人访问，3号有30人访问。
2. 从图表可以看出，网站的访问量呈逐日增加的趋势。
3. 可以预测未来的访问量会继续增加。

根据目前的数据和趋势分析，可以初步判断网站还能存活相当长的一段时间。然而，具体的存活时间还需要根据您网站业务的实际情况以及其他相关指标的分析来综合判断。', 1745016049576775681, '2024-01-17 17:40:47', '2024-01-17 17:41:07', 0, '网站', 'succeed', '生成成功');


INSERT INTO next_bi_db.chart_1747554550780358657 (日期, 人数) VALUES ('1', '10');
INSERT INTO next_bi_db.chart_1747554550780358657 (日期, 人数) VALUES ('2', '20');
INSERT INTO next_bi_db.chart_1747554550780358657 (日期, 人数) VALUES ('3', '30');
