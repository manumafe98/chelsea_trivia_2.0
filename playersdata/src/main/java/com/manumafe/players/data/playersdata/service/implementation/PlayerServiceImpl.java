package com.manumafe.players.data.playersdata.service.implementation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.manumafe.players.data.playersdata.document.Player;
import com.manumafe.players.data.playersdata.repository.PlayerRepository;
import com.manumafe.players.data.playersdata.service.PlayerService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlayerServiceImpl implements PlayerService {

	private final PlayerRepository playerRepository;
	private final MongoTemplate mongoTemplate;


	@Override
	public void savePlayer(Player player) {
		playerRepository.save(player);
	}

	@Override
	public List<Player> findAllPlayers() {
		return playerRepository.findAll();
	}

	@Override
	public void deleteAllPlayers() {
		playerRepository.deleteAll();
	}

	@Override
	public List<Player> findRandomPlayers() {
		return playerRepository.findRandomPlayers(3);
	}

	@Override
	public List<Player> findPlayerWithMostOfCertainAttribute(String attribute) {
		Query query = new Query().with(Sort.by(Sort.Order.desc(attribute)));
		Player topScorerPlayer = mongoTemplate.findOne(query, Player.class);
		List<Player> randomPlayers = playerRepository.findRandomPlayersExcludingId(topScorerPlayer.getId(), 2);
		List<Player> players = new ArrayList<>(randomPlayers);
		players.add(topScorerPlayer);
		return players;
	}
}
