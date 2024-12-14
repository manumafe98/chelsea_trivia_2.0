package com.manumafe.players.data.playersdata.service;

import java.util.List;
import java.util.Set;

import com.manumafe.players.data.playersdata.document.Player;

public interface PlayerService {

    void savePlayer(
            String playerName,
            Set<String> playerShirtNumbers,
            String playerImgUrl,
            Set<String> playerPositions,
            Integer playerAge,
            List<String> playerNationalities,
            Integer appareances,
            Integer goals,
            Integer assists,
            Integer yellowCards,
            Integer redCards,
            Integer minutesPlayed);

    void updatePlayer(
            Player playerToUpdate,
            String playerShirtNumber,
            String playerPosition,
            Integer playerAge,
            Integer appareances,
            Integer goals,
            Integer assists,
            Integer yellowCards,
            Integer redCards,
            Integer minutesPlayed);

    void deleteAllPlayers();

    Player findPlayerByName(String playerName);

    List<Player> findAllPlayers();

    List<Player> findRandomPlayers();

    List<Player> findPlayerWithMostOfCertainAttribute(String attribute);
}
