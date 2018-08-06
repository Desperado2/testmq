package com.jack.consumer;

import com.jack.util.MqUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;

public class QueueConsumer {

    static Logger logger = LoggerFactory.getLogger(QueueConsumer.class);

    public static void main(String[] args) {
        try {
            Session session = MqUtils.getSession();
            Queue destination = session.createQueue("Queue B");
            MessageConsumer consumer = session.createConsumer(destination);

            consumer.setMessageListener(new MessageListener() {
                @Override
                public void onMessage(Message message) {
                    TextMessage textMessage = (TextMessage) message;
                    try {
                        logger.info(textMessage.getText());
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                    try {
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
