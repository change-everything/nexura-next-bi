package cn.nexura.nextbi.mq;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * @author PeiYP
 * @since 2024年01月16日 15:02
 */
public class MqInit {

    public static void main(String[] args) {

        try {
            ConnectionFactory connectionFactory = new ConnectionFactory();
            connectionFactory.setHost("120.46.207.211");
            Connection connection = connectionFactory.newConnection();
            Channel channel = connection.createChannel();

            // 声明死信交换机
            channel.exchangeDeclare(BiMqConstant.DEAD_LETTER_EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

            // 声明死信队列
            channel.queueDeclare(BiMqConstant.DEAD_LETTER_QUEUE_NAME, true, false, false, null);
            channel.queueBind(BiMqConstant.DEAD_LETTER_QUEUE_NAME, BiMqConstant.DEAD_LETTER_EXCHANGE_NAME, BiMqConstant.DEAD_LETTER_ROUTING_KEY);

            // 设置正常队列的死信交换机和死信路由键
            Map<String, Object> arguments = new HashMap<>();
            arguments.put("x-dead-letter-exchange", BiMqConstant.DEAD_LETTER_EXCHANGE_NAME);
            arguments.put("x-dead-letter-routing-key", BiMqConstant.DEAD_LETTER_ROUTING_KEY);
            arguments.put("x-message-ttl", 5000);

            String exchangeName = BiMqConstant.BI_EXCHANGE_NAME;
            channel.exchangeDeclare(exchangeName, BuiltinExchangeType.DIRECT);

            String queueName = BiMqConstant.BI_QUEUE_NAME;
            channel.queueDeclare(queueName, true, false, false, arguments);
            channel.queueBind(queueName, exchangeName, BiMqConstant.BI_ROUTING_KEY);


        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }

    }

}
