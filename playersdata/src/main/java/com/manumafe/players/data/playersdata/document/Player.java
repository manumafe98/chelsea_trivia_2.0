package com.manumafe.players.data.playersdata.document;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Player {

    @Id
    private String id;
    private String fullName;
    private Integer shirtNumber;
    private String profileImageUrl;
    private String position;
    private Integer age;
    private List<String> nationalities;
    private Integer appareances;
    private Integer goals;
    private Integer assists;
    private Integer yellowCardsReceived;
    private Integer redCardsReceived;
    private Integer minutesPlayed;
}
