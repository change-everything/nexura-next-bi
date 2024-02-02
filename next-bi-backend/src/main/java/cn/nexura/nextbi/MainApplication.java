package cn.nexura.nextbi;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 主类（项目启动入口）
 *
 * @author peiYP
 */
// 2024/1/19 图表数据分表存储，提高查询灵活性和性能
// 2024/1/19 由于图表数据是静态的，很适合使用 Redis 缓存来提高加载速度。
// 2024/1/19 使用死信队列来处理异常情况，将图表生成任务置为失败
// 2024/1/19 限制用户同时生成图表的数量，防止单用户抢占系统资源
// 2024/1/19 任务执行成功或失败，给用户发送实时消息通知, server side event
// 2024/1/22 给任务的执行增加 guava Retrying 重试机制，保证系统可靠性。
// 2024/1/22 如果说任务根本没提交到队列中（或者队列满了），可以用定时任务把失败状态的图表放到队列中（补偿）
// 2024/1/22 统计用户生成图表的次数，甚至可以添加积分系统，消耗积分来智能分析
// TODO: 2024/2/1 接入chatGPT
// 2024/2/2 实现重新生成
// TODO: 2024/2/1 实现修改图表功能
@SpringBootApplication
@MapperScan("cn.nexura.nextbi.mapper")
@EnableScheduling
//@EnableCaching
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
public class MainApplication {

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

}
