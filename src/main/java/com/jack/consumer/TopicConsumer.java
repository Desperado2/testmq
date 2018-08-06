package com.jack.consumer;

import com.jack.util.MqUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;

public class TopicConsumer {

    static Logger logger = LoggerFactory.getLogger(TopicConsumer.class);

    public static void main(String[] args) {
        try {
            //获取session
            Session session = MqUtils.getSession();
            //设置订阅地址
            Topic destination = session.createTopic("Topic A");
            //订阅消息
            MessageConsumer consumer = session.createConsumer(destination);
            //消息监听（有事务限制）
            consumer.setMessageListener(new MessageListener() {
                @Override
                public void onMessage(Message message) {
                    //接收消息
                    TextMessage textMessage = (TextMessage) message;
                    try {
                        logger.info(textMessage.getText());
                        //提交
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
