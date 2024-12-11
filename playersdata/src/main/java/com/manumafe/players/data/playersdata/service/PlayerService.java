package com.manumafe.players.data.playersdata.service;

import java.util.List;

import com.manumafe.players.data.playersdata.document.Player;

public interface PlayerService {

    void savePlayer(Player player);
    void deleteAllPlayers();
    List<Player> findAllPlayers();
    List<Player> findRandomPlayers();
    List<Player> findPlayerWithMostOfCertainAttribute(String attribute);
}
