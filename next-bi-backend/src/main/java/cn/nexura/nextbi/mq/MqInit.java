package cn.nexura.nextbi.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
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

            String exchangeName = BiMqConstant.BI_EXCHANGE_NAME;
            channel.exchangeDeclare(exchangeName, "direct");

            String queueName = BiMqConstant.BI_QUEUE_NAME;
            channel.queueDeclare(queueName, true, false, false, null);
            channel.queueBind(queueName, exchangeName, BiMqConstant.BI_ROUTING_KEY);
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }

    }

}
