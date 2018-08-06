package com.jack.util;


import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Session;

public class MqUtils {

    static  Logger logger = LoggerFactory.getLogger(MqUtils.class);
     private static final String URL="tcp://118.24.115.208:61616";
     private static final String USER_NAME="admin";
     private static final String PASSWORD="admin";
    private static Connection connection =null;
    private static Session session=null;

    private MqUtils(){}

    /**
     * 获取session   一个发送或者接受消息的线程
     * @return
     * @throws JMSException
     */
     public static Session getSession() throws JMSException {
         //获取连接工厂
         ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(USER_NAME,PASSWORD,URL);
         if(null == connection){
             //获取连接
             connection = connectionFactory.createConnection();
             connection.start();
             logger.info("连接创建成功");
         }
         if(null == session){
             //获取session对象
             session = connection.createSession(Boolean.TRUE,Session.AUTO_ACKNOWLEDGE);
             logger.info("session创建成功");
         }
         return session;
     }


    /**
     * 关闭session和连接
     * @throws JMSException
     */
     public static void closeSession() {
        if(null != session){
            try {
                session.close();
                logger.info("session关闭");
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
        if(null != connection){
            try {
                connection.close();
                logger.info("连接关闭");
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
     }

}
