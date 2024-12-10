package com.manumafe.players.data.playersdata.service.implementation;

import java.util.List;

import org.springframework.stereotype.Service;

import com.manumafe.players.data.playersdata.document.Player;
import com.manumafe.players.data.playersdata.repository.PlayerRepository;
import com.manumafe.players.data.playersdata.service.PlayerService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;
    

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
}
