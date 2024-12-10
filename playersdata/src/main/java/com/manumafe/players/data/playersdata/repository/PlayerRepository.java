package com.manumafe.players.data.playersdata.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.manumafe.players.data.playersdata.document.Player;

public interface PlayerRepository extends MongoRepository<Player, String> {}
