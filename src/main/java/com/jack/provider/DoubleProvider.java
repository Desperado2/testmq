package com.jack.provider;

import com.jack.util.MqUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import java.util.UUID;

public class DoubleProvider {

    static Logger logger = LoggerFactory.getLogger(DoubleProvider.class);

    public static void main(String[] args) {
        try {
            //获取连接
            Session session = MqUtils.getSession();
            //设置地址
            Destination destination1 = session.createQueue("DoubleA");
            //创建消息发送者
            MessageProducer messageProducer = session.createProducer(destination1);
            //设置消息不持久化
            messageProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            //创建接收消息的地址
            Destination destination = session.createTemporaryQueue();
            //创建消息接受者
            MessageConsumer messageConsumer = session.createConsumer(destination);
            //监听消息借口
            messageConsumer.setMessageListener(new MessageListener() {
                @Override
                public void onMessage(Message message) {
                    //接收消息
                    TextMessage textMessage = (TextMessage) message;
                    try {
                        logger.info(textMessage.getText());
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            });
            //创建消息体
            TextMessage textMessage = session.createTextMessage();
            textMessage.setText("HYYYY");
            //设置消息发送目的地
            textMessage.setJMSReplyTo(destination);
            String correlationId  = UUID.randomUUID().toString();
            //设置消息的id
            textMessage.setJMSCorrelationID(correlationId);
            //发送消息
            messageProducer.send(textMessage);
            session.commit();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
