package cn.nexura.nextbi;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 主类（项目启动入口）
 *
 * @author peiYP
 */
// 2024/1/19 图表数据分表存储，提高查询灵活性和性能
// 2024/1/19 由于图表数据是静态的，很适合使用 Redis 缓存来提高加载速度。
// TODO: 2024/1/19 使用死信队列来处理异常情况，将图表生成任务置为失败
// TODO: 2024/1/19 可以使用 AI 来整理和压缩原始数据，进一步提高输入的数据数量。
// TODO: 2024/1/19 限制用户同时生成图表的数量，防止单用户抢占系统资源
// TODO: 2024/1/19 统计用户生成图表的次数，甚至可以添加积分系统，消耗积分来智能分析
// TODO: 2024/1/19 给任务的执行增加 guava Retrying 重试机制，保证系统可靠性。
// TODO: 2024/1/19 提前考虑到 AI 生成错误的情况，在后端进行异常处理（比如 AI 说了多余的话，提取正确的字符串）
// TODO: 2024/1/19 如果说任务根本没提交到队列中（或者队列满了），可以用定时任务把失败状态的图表放到队列中（补偿）
// TODO: 2024/1/19 建议给任务的执行增加一个超时时间，超时自动标记为失败（超时控制）
// TODO: 2024/1/19 反向压力，通过调用的服务状态来选择当前系统的策略，从而最大化利用系统资源。
// TODO: 2024/1/19 任务执行成功或失败，给用户发送实时消息通知（可以使用 websocket、server side event 等技术）
@SpringBootApplication
@MapperScan("cn.nexura.nextbi.mapper")
@EnableScheduling
@EnableCaching
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
public class MainApplication {

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

}
