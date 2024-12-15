package com.manumafe.players.data.playersdata.service.implementation;

import java.io.IOException;
import java.time.Year;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import com.manumafe.players.data.playersdata.document.Player;
import com.manumafe.players.data.playersdata.enums.ScrapeUrls;
import com.manumafe.players.data.playersdata.service.PlayerScrapeService;
import com.manumafe.players.data.playersdata.service.PlayerService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlayerScrapeServiceImpl implements PlayerScrapeService {

    private final PlayerService playerService;

    @Override
    public Document connect(String url) throws IOException {
        return Jsoup.connect(url).timeout(0).get();
    }

    @Override
    @EventListener(ContextRefreshedEvent.class)
    public void scrapeAllPlayers() throws IOException {
        playerService.deleteAllPlayers();

        for (ScrapeUrls url : ScrapeUrls.values()) {
            scrapePlayers(url.getFullUrl());
        }
    }

    @Override
    public void scrapeCurrentYearPlayers() throws IOException {
        String yearInString = String.valueOf(Year.now().getValue());
        for (ScrapeUrls url : ScrapeUrls.values()) {
            if (url.getYear().equals(yearInString)) {
                scrapePlayers(url.getFullUrl());
            }
        }
    }

    // TODO add helper function to reduce de duplication of .equals("-") and Integer.parseInt
    private void scrapePlayers(String url) throws IOException {
        Document doc = connect(url);
        Elements tableRows = doc.select("table tbody > tr");

        for (Element row : tableRows) {
            String playerName = row.select("td:nth-child(2) table tbody tr td span.hide-for-small > a").text();

            if (row.text().contains("Not used during this season") || row.text().contains("Not in squad during this season") || playerName.isBlank()) {
                continue;
            }

            // TODO Fix bug on shirtNumbers
            // "fullName": "Mickey Thomas",
            // "shirtNumbers": [
            //     "Unknown",
            //     "-"
            // ],
            Set<String> playerShirtNumbers = new HashSet<>();
            String playerShirData = row.select("td:nth-child(1) div").text();
            playerShirtNumbers.add(playerShirData.equals("-") ? "Unknown" : playerShirData);

            Set<String> playerPositions = new HashSet<>();
            String playerPositionData = row.selectFirst("td:nth-child(2) table tbody tr:nth-child(2) > td").text();
            playerPositions.add(playerPositionData);

            String playerAgeData = row.select("td:nth-child(3)").text();
            int playerAge = playerAgeData.equals("-") ? 0 : Integer.parseInt(playerAgeData);

            Element portraitImgElement = row.selectFirst("table.inline-table img.bilderrahmen-fixed");
            String playerImgUrl = portraitImgElement != null ? portraitImgElement.attr("src") : "No image available";

            Elements nationalityImgs = row.select("td:nth-child(4) img");
            List<String> playerNationalities = new ArrayList<>();

            for (Element img : nationalityImgs) {
                playerNationalities.add(img.attr("title"));
            }

            String appareacesData = row.select("td:nth-child(6)").text();
            int appareances = appareacesData.equals("-") ? 0 : Integer.parseInt(appareacesData);

            String goalsData = row.select("td:nth-child(7)").text();
            int goals = goalsData.equals("-") ? 0 : Integer.parseInt(goalsData);

            String assistsData = row.select("td:nth-child(8)").text();
            int assists = assistsData.equals("-") ? 0 : Integer.parseInt(assistsData);

            String yellowCardsData = row.select("td:nth-child(9)").text();
            int yellowCards = yellowCardsData.equals("-") ? 0 : Integer.parseInt(yellowCardsData);

            String redCardsData = row.select("td:nth-child(11)").text();
            int redCards = redCardsData.equals("-") ? 0 : Integer.parseInt(redCardsData);

            String minutesPlayedData = row.select("td:nth-child(15)").text();
            int minutesPlayed = minutesPlayedData.equals("-") ? 0 : Integer.parseInt(minutesPlayedData.replace("'", "").replace(".", ""));

            Player player = playerService.findPlayerByName(playerName);

            if (player != null) {
                playerService.updatePlayer(player, playerShirData, playerPositionData, playerAge, appareances, goals, assists, yellowCards, redCards, minutesPlayed);
            } else {
                playerService.savePlayer(playerName, playerShirtNumbers, playerImgUrl, playerPositions, playerAge, playerNationalities, appareances, goals, assists, yellowCards, redCards, minutesPlayed);
            }
        }
    }
}
