package com.jack.provider;

import com.jack.util.MqUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;

public class TopicProvider {

    static Logger logger = LoggerFactory.getLogger(TopicProvider.class);

    /**
     * 测试消息发布
     * @param args
     */
    public static void main(String[] args) {
        try {
            //获取session
            Session session = MqUtils.getSession();
            //制定发布地址
            Topic topic = session.createTopic("Topic A");
            //消息发布者
            MessageProducer producer = session.createProducer(topic);
            //设置不持久化
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            logger.info("开始发送消息");
            //发送消息
            sendMessage(session,producer);
            //提交
            session.commit();
            logger.info("消息发送结束");
        } catch (JMSException e) {
            e.printStackTrace();
        }finally {
            //关闭session和连接
            MqUtils.closeSession();
        }
    }

    /**
     * 发送消息
     * @param session   一个发送消息的线程
     * @param messageProducer  消息发送者
     * @throws JMSException
     */
    public static void sendMessage(Session session,MessageProducer messageProducer) throws JMSException {
        //构造文本消息
        TextMessage message = session.createTextMessage("{\"name\":\"zhangsan\"}");
        //发送消息
        messageProducer.send(message);
    }
}
