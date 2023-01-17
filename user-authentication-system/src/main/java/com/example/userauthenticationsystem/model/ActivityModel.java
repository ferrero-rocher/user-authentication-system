package com.example.userauthenticationsystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityModel {

    @NotBlank(message = "activity Name field should be provided ")
    private String activityName;
    @NotBlank(message = "activity Description field should be provided ")
    private String activityDesc;
}
