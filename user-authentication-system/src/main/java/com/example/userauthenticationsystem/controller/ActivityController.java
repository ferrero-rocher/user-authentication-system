package com.example.userauthenticationsystem.controller;


import com.example.userauthenticationsystem.config.MQConfig;
import com.example.userauthenticationsystem.entity.ActivityEntity;
import com.example.userauthenticationsystem.model.ActivityModel;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1")
@Validated
public class ActivityController {

    @Autowired
    private RabbitTemplate template;

    @PostMapping("/activity")
    public void postToMQ(@RequestBody @Valid ActivityModel activityModel)
    {
        ActivityEntity activity = new ActivityEntity(activityModel.getActivityName(), activityModel.getActivityDesc());
        template.convertAndSend(MQConfig.EXCHANGE,MQConfig.ROUTING_KEY,activity);
    }
}
