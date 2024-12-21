package com.manumafe.players.data.playersdata.service;

import java.util.List;

import com.manumafe.players.data.playersdata.document.Player;

public interface PlayerService {
    void savePlayer(
        String playerName,
        Integer playerShirtNumbers,
        String playerImgUrl,
        String playerPositions,
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
        Integer playerShirtNumber,
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

    List<Player> findRandomPlayers(String attribute);

    List<Player> findPlayerWithMostOfCertainAttribute(String attribute);
}
