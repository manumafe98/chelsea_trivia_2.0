package com.manumafe.players.data.playersdata.service.implementation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.SampleOperation;
import org.springframework.data.mongodb.core.query.Criteria;
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
	private static final String ACTIVE_AGES_AT_CLUB = "activeAgesAtClub";
	private static final String SHIRT_NUMBERS = "shirtNumbers";
	private static final String PROFILE_IMAGE_URL = "profileImageUrl";
	private static final String EXCLUDE_IMAGE_REGEX = ".*default.*";

	@Override
	public void savePlayer(
			String playerName,
			Integer playerShirtNumber,
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

		Set<Integer> playerShirtNumbers = new HashSet<>();
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
	public List<Player> findRandomPlayers(String attribute) {
		String collectionName = mongoTemplate.getCollectionName(Player.class);
		Player firstPlayer;

		if (attribute.equals(PROFILE_IMAGE_URL) || attribute.equals(ACTIVE_AGES_AT_CLUB) || attribute.equals(SHIRT_NUMBERS)) {
			firstPlayer = getRandomPlayerExcluding(collectionName, attribute, List.of());
		} else {
			firstPlayer = playerRepository.findRandomPlayer();
		}

		Player secondPlayer = getRandomPlayerExcluding(collectionName, attribute, List.of(firstPlayer));
		Player thirdPlayer = getRandomPlayerExcluding(collectionName, attribute, List.of(firstPlayer, secondPlayer));

		return Arrays.asList(firstPlayer, secondPlayer, thirdPlayer);
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
			Integer playerShirtNumber,
			String playerPosition,
			Integer playerAge,
			Integer appareances,
			Integer goals,
			Integer assists,
			Integer yellowCards,
			Integer redCards,
			Integer minutesPlayed) {

		Set<Integer> shirtNumbers = new HashSet<>(playerToUpdate.getShirtNumbers());
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

	private Object getAttributeValueUsingGetter(Player player, String attributeName) {
		try {
			String methodName = "get" + attributeName.substring(0, 1).toUpperCase() + attributeName.substring(1);
			Method method = Player.class.getMethod(methodName);

			return method.invoke(player);
		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException("Error retrieving attribute value using getter: " + attributeName, e);
		}
	}

	private Player getRandomPlayerExcluding(String collectionName, String attribute, List<Player> excludedPlayers) {
		Criteria criteria = buildExclusionCriteria(attribute, excludedPlayers);
		MatchOperation matchOperation = Aggregation.match(criteria);
		SampleOperation sampleOperation = Aggregation.sample(1);
		Aggregation aggregation = Aggregation.newAggregation(matchOperation, sampleOperation);
		AggregationResults<Player> results = mongoTemplate.aggregate(aggregation, collectionName, Player.class);

		return results.getUniqueMappedResult();
	}

	private Criteria buildExclusionCriteria(String attribute, List<Player> excludedPlayers) {
		List<Criteria> exclusionCriteria;

		if (excludedPlayers.size() > 0) {
			exclusionCriteria = new ArrayList<>(
				excludedPlayers.stream()
					.map(player -> Criteria.where(attribute).ne(getAttributeValueUsingGetter(player, attribute)))
					.collect(Collectors.toList())
			);
		} else {
			exclusionCriteria = new ArrayList<>();
		}

		if (attribute.equals(SHIRT_NUMBERS) || attribute.equals(ACTIVE_AGES_AT_CLUB)) {
			exclusionCriteria.add(Criteria.where(attribute).ne(0));
		} else if (attribute.equals(PROFILE_IMAGE_URL)) {
			exclusionCriteria.add(Criteria.where(attribute).not().regex(EXCLUDE_IMAGE_REGEX));
		}

		return new Criteria().andOperator(exclusionCriteria.toArray(new Criteria[0]));
	}
}
