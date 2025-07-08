package com.cosmicbook.search_and_rescue_droid.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "missions")
public class Missions {

    @Id
    private String id;
    private String spotName;
    private int noOfPersons;
    private String contractTeamName;
    private String healthStatus;
    private LocalDateTime timeOfEvent;
    private String location;
}
