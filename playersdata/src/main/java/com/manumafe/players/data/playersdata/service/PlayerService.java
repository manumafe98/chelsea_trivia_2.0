package com.manumafe.players.data.playersdata.service;

import java.util.List;

import com.manumafe.players.data.playersdata.document.Player;

public interface PlayerService {
// TODO add services to get 3 random players for age without age 0 and for shirts without shirt unknown
    void savePlayer(
        String playerName,
        String playerShirtNumbers,
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
