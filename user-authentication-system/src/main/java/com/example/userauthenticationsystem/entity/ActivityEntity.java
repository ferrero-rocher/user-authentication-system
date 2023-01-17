package com.example.userauthenticationsystem.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityEntity {

    private String activityName;
    private String activityDesc;
    private String timestamp;

    public ActivityEntity(String activityName, String activityDesc) {
        this.activityName = activityName;
        this.activityDesc = activityDesc;
        this.timestamp=calculateTimeStamp();
    }


    private String calculateTimeStamp() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now).toString();

    }


}
