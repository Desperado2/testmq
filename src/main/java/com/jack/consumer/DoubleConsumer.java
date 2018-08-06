package com.jack.consumer;

import com.jack.util.MqUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;

public class DoubleConsumer {

    static Logger logger = LoggerFactory.getLogger(DoubleConsumer.class);

    public static void main(String[] args) {
        try {
            //获取session
            Session session = MqUtils.getSession();
            //设置消息地址
            Destination destination = session.createQueue("DoubleA");
            //创建消息发送者
            MessageProducer messageProducer = session.createProducer(null);
            //设置不持久化
            messageProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            //创建消息接受者
            MessageConsumer messageConsumer = session.createConsumer(destination);
            //监听消息
            messageConsumer.setMessageListener(new MessageListener() {
                @Override
                public void onMessage(Message message) {
                    //接收消息
                    TextMessage textMessage = (TextMessage) message;
                    try {
                        //接收后构造回复消息
                        TextMessage txtMessage = session.createTextMessage();
                        String messageText = textMessage.getText();
                        logger.info(messageText);
                        txtMessage.setText(messageText);
                        //设置消息的id，保证每个消息的回复相对应
                        txtMessage.setJMSCorrelationID(message.getJMSCorrelationID());
                        //回复消息
                        messageProducer.send(message.getJMSReplyTo(),txtMessage);
                        session.commit();
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }

                }
            });
        } catch (JMSException e) {
            e.printStackTrace();
        }

    }
}
