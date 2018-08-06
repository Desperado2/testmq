package com.jack.provider;

import com.jack.util.MqUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;


public class QueryProvider {

    static Logger logger = LoggerFactory.getLogger(QueryProvider.class);
    public static void main(String[] args) {

        try {
            //获取session  一个发送消息的线程
            Session session = MqUtils.getSession();
            //设置消息发送的目的地
            Queue destination  = session.createQueue("Queue B");
            //获取消息发送者
            MessageProducer producer = session.createProducer(destination);
            //设置不持久化
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            //发送消息
            logger.info("开始发送消息");
            sendMessage(session,producer);
            //提交，只有提交了消息才会正真发送
            session.commit();
            logger.info("消息发送完毕");
        } catch (JMSException e) {
            e.printStackTrace();
        }finally {
            //关闭连接
            MqUtils.closeSession();
        }
    }


    /**
     * 发送文本消息
     * @param session  session对象
     * @param producer  消息发送者
     * @throws JMSException
     */
    public static void sendMessage(Session session,MessageProducer producer) throws JMSException {

            //构造文本消息
            TextMessage textMessage = session.createTextMessage("{\"name\":\"zhangsan\",\"age\":35,\"sex\":\"male\",\"marry\":false}");
            logger.info("发送消息");
            //发送消息
            producer.send(textMessage);

    }
}
