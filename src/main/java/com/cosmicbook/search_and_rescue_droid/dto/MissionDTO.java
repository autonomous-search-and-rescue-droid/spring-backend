package com.cosmicbook.search_and_rescue_droid.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MissionDTO {

    private String id;
    private String spotName;
    private int noOfPersons;
    private String contractTeamName;
    private String healthStatus;
    private LocalDateTime timeOfEvent;
    private String location;

}
