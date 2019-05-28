package com.cbt.mq;

import com.cbt.controller.PushEmailController;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import java.io.IOException;

public class RabbitMqConsumerListener implements ChannelAwareMessageListener {
    private Logger logger= LoggerFactory.getLogger(RabbitMqConsumerListener.class);

    PushEmailController rabbitMaConsumerTaskAction = new PushEmailController();
    @Override
    public void onMessage(Message message, Channel channel) throws Exception {

        //业务处理，放到action层，并返回处理成功还是异常的flag
        boolean mqFlag=rabbitMaConsumerTaskAction.saveMq(message);
        //还有一个点就是如何获取mq消息的报文部分message？
        //String message=new String(arg0.getBody(),"UTF-8");
        if(mqFlag){
            basicACK(message,channel);//处理正常--ack
        }else{
            basicNACK(message,channel);//处理异常--nack
        }
    }
    //正常消费掉后通知mq服务器移除此条mq
    private void basicACK(Message message,Channel channel){
        try{
          channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        }catch(IOException e){
            logger.error("通知服务器移除mq时异常，异常信息："+e);
        }
    }
    //处理异常，mq重回队列
    private void basicNACK(Message message,Channel channel){
        try{
            channel.basicNack(message.getMessageProperties().getDeliveryTag(),false,true);
        }catch(IOException e){
            logger.error("mq重新进入服务器时出现异常，异常信息："+e);
        }
    }
}
