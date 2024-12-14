package com.manumafe.players.data.playersdata.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.manumafe.players.data.playersdata.document.Player;

public interface PlayerRepository extends MongoRepository<Player, String> {
    @Aggregation(pipeline = {
        "{ $match: { '_id': { $ne: ?0 } } }",
        "{ $sample: { size: ?1 } }"
    })
    List<Player> findRandomPlayersExcludingId(String excludedId, int sampleSize);

    @Aggregation(pipeline = { "{ $sample: { size: ?0 } }" })
    List<Player> findRandomPlayers(int sampleSize);

    @Aggregation(pipeline = { "{ $match: { fullName: ?0 } }" })
    Optional<Player> findByFullName(String fullName);
}
