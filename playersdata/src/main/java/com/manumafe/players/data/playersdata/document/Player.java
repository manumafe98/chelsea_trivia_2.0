package com.manumafe.players.data.playersdata.document;

import java.util.List;
import java.util.Set;

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
// TODO change data type of shirtNumbers to Integer, and update Unknown shirts for 0 value
    @Id
    private String id;
    private String fullName;
    private Set<String> shirtNumbers;
    private String profileImageUrl;
    private Set<String> positions;
    private Set<Integer> activeAgesAtClub;
    private List<String> nationalities;
    private Integer appareances;
    private Integer goals;
    private Integer assists;
    private Integer yellowCardsReceived;
    private Integer redCardsReceived;
    private Integer minutesPlayed;
}
