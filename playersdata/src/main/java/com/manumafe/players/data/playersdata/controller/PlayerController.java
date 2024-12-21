package com.manumafe.players.data.playersdata.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.manumafe.players.data.playersdata.document.Player;
import com.manumafe.players.data.playersdata.service.PlayerService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/v1/player")
@RequiredArgsConstructor
public class PlayerController {

    private final PlayerService playerService;

    @GetMapping
    public ResponseEntity<List<Player>> getPlayers() {
        List<Player> players = playerService.findAllPlayers();
        return ResponseEntity.status(HttpStatus.OK).body(players);
    }

    @GetMapping("/most")
    public ResponseEntity<List<Player>> getPlayerWithMostOfThatAttributeAndTwoRandom(@RequestParam String attribute) {
        List<Player> players = playerService.findPlayerWithMostOfCertainAttribute(attribute);
        return ResponseEntity.status(HttpStatus.OK).body(players);
    }

    @GetMapping("/random")
    public ResponseEntity<List<Player>> getRandomPlayers(@RequestParam String attribute) {
        List<Player> players = playerService.findRandomPlayers(attribute);
        return ResponseEntity.status(HttpStatus.OK).body(players);
    }
}
