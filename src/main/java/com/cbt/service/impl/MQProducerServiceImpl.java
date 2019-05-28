package com.cbt.service.impl;

import com.cbt.service.MQProducerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class MQProducerServiceImpl implements MQProducerService {
    @Resource
    private AmqpTemplate amqpTemplate;
    private final static Logger logger = LoggerFactory.getLogger(MQProducerServiceImpl.class);

    @Override
    public void sendDataToQueue(String queueKey, Object object) {
        try {
            amqpTemplate.convertAndSend(queueKey, object);
        } catch (Exception e) {
            logger.error(e.toString());
            e.printStackTrace();
        }
    }
}
