package com.manumafe.players.data.playersdata.service.implementation;

import java.io.IOException;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.manumafe.players.data.playersdata.service.PlayerScrapeService;
import com.manumafe.players.data.playersdata.service.ScheduleScrapeService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScheduleScrapeServiceImpl implements ScheduleScrapeService {

    private final PlayerScrapeService playerScrapeService;

    @Override
    @Scheduled(cron = "0 0 0 */3 * ?")
    public void scheduleScrape() throws IOException {
        playerScrapeService.scrapeCurrentYearPlayers();
    }
}
