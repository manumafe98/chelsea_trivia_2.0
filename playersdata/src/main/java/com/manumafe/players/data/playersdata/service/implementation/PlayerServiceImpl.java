package com.manumafe.players.data.playersdata.service.implementation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
	public void savePlayer(
			String playerName,
			String playerShirtNumber,
			String playerImgUrl,
			String playerPosition,
			Integer playerAge,
			List<String> playerNationalities,
			Integer appareances,
			Integer goals,
			Integer assists,
			Integer yellowCards,
			Integer redCards,
			Integer minutesPlayed) {
		Player player = new Player();
		player.setFullName(playerName);

		Set<String> playerShirtNumbers = new HashSet<>();
		playerShirtNumbers.add(playerShirtNumber);
		player.setShirtNumbers(playerShirtNumbers);

		player.setProfileImageUrl(playerImgUrl);

		Set<String> playerPositions = new HashSet<>();
		playerPositions.add(playerPosition);
		player.setPositions(playerPositions);

		Set<Integer> activeAgesAtClub = new HashSet<>();
		activeAgesAtClub.add(playerAge);
		player.setActiveAgesAtClub(activeAgesAtClub);

		player.setNationalities(playerNationalities);
		player.setAppareances(appareances);
		player.setGoals(goals);
		player.setAssists(assists);
		player.setYellowCardsReceived(yellowCards);
		player.setRedCardsReceived(redCards);
		player.setMinutesPlayed(minutesPlayed);
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
		// TODO what if two players have exaclty the same of that attribute, define by some other criteria
		Query query = new Query().with(Sort.by(Sort.Order.desc(attribute)));
		Player topScorerPlayer = mongoTemplate.findOne(query, Player.class);
		List<Player> randomPlayers = playerRepository.findRandomPlayersExcludingId(topScorerPlayer.getId(), 2);
		List<Player> players = new ArrayList<>(randomPlayers);
		players.add(topScorerPlayer);
		return players;
	}

	@Override
	public void updatePlayer(
			Player playerToUpdate,
			String playerShirtNumber,
			String playerPosition,
			Integer playerAge,
			Integer appareances,
			Integer goals,
			Integer assists,
			Integer yellowCards,
			Integer redCards,
			Integer minutesPlayed) {

		Set<String> shirtNumbers = new HashSet<>(playerToUpdate.getShirtNumbers());
		shirtNumbers.add(playerShirtNumber);
		playerToUpdate.setShirtNumbers(shirtNumbers);

		Set<String> playerPositions = new HashSet<>(playerToUpdate.getPositions());
		playerPositions.add(playerPosition);
		playerToUpdate.setPositions(playerPositions);

		Set<Integer> activeAgesAtClub = new HashSet<>(playerToUpdate.getActiveAgesAtClub());
		activeAgesAtClub.add(playerAge);
		playerToUpdate.setActiveAgesAtClub(activeAgesAtClub);

		playerToUpdate.setAppareances(playerToUpdate.getAppareances() + appareances);
		playerToUpdate.setGoals(playerToUpdate.getGoals() + goals);
		playerToUpdate.setAssists(playerToUpdate.getAssists() + assists);
		playerToUpdate.setYellowCardsReceived(playerToUpdate.getYellowCardsReceived() + yellowCards);
		playerToUpdate.setRedCardsReceived(playerToUpdate.getRedCardsReceived() + redCards);
		playerToUpdate.setMinutesPlayed(playerToUpdate.getMinutesPlayed() + minutesPlayed);

		playerRepository.save(playerToUpdate);
	}

	@Override
	public Player findPlayerByName(String playerName) {
		return playerRepository.findByFullName(playerName).orElse(null);
	}
}
