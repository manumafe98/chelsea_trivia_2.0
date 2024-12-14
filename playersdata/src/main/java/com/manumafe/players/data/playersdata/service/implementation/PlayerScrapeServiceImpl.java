package com.manumafe.players.data.playersdata.service.implementation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import com.manumafe.players.data.playersdata.service.PlayerScrapeService;
import com.manumafe.players.data.playersdata.service.PlayerService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlayerScrapeServiceImpl implements PlayerScrapeService {

    // TODO Create a class or enum with all the URLS from 93/94 until now, and loop over all those
    private String WEB_URL = "https://www.transfermarkt.com/chelsea-fc/leistungsdaten/verein/631/reldata/%262024/plus/1";
    private final PlayerService playerService;

    @Override
    public Document connect() throws IOException {
        return Jsoup.connect(WEB_URL).get();
    }

    @Override
    public void scrapePlayers() throws IOException {
        playerService.deleteAllPlayers();

        Document doc = connect();
        Elements tableRows = doc.select("table tbody > tr");

        for (Element row : tableRows) {
            // TODO check if player exists in db
            String playerName = row.select("td:nth-child(2) table tbody tr td span.hide-for-small > a").text();

            if (row.text().contains("Not used during this season") || playerName.isBlank()) {
                continue;
            }

            Set<String> playerShirtNumbers = new HashSet<>();
            String playerShirData= row.select("td:nth-child(1) div").text();
            playerShirtNumbers.add(playerShirData.equals("-") ? "Unknown" : playerShirData);

            Set<String> playerPositions = new HashSet<>();
            String playerPositionData = row.selectFirst("td:nth-child(2) table tbody tr:nth-child(2) > td").text();
            playerPositions.add(playerPositionData);

            int playerAge = Integer.parseInt(row.select("td:nth-child(3)").text());

            Element portraitImgElement = row.selectFirst("table.inline-table img.bilderrahmen-fixed");
            String playerImgUrl = portraitImgElement != null ? portraitImgElement.attr("src") : "No image available";

            Elements nationalityImgs = row.select("td:nth-child(4) img");
            List<String> playerNationalities = new ArrayList<>();

            for (Element img : nationalityImgs) {
                playerNationalities.add(img.attr("title"));
            }

            int appareances = Integer.parseInt(row.select("td:nth-child(6)").text());

            String goalsData = row.select("td:nth-child(7)").text();
            int goals = goalsData.equals("-") ? 0 : Integer.parseInt(goalsData);

            String assistsData = row.select("td:nth-child(8)").text();
            int assists = assistsData.equals("-") ? 0 : Integer.parseInt(assistsData);

            String yellowCardsData = row.select("td:nth-child(9)").text();
            int yellowCards = yellowCardsData.equals("-") ? 0 : Integer.parseInt(yellowCardsData);

            String redCardsData = row.select("td:nth-child(11)").text();
            int redCards = redCardsData.equals("-") ? 0 : Integer.parseInt(redCardsData);

            int minutesPlayed = Integer.parseInt(row.select("td:nth-child(15)").text().replace("'", "").replace(".", ""));

            playerService.savePlayer(playerName, playerShirtNumbers, playerImgUrl, playerPositions, playerAge,
                    playerNationalities, appareances, goals, assists, yellowCards, redCards, minutesPlayed);
        }
    }
}
