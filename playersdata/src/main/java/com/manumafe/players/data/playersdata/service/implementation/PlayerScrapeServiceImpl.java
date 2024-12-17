package com.manumafe.players.data.playersdata.service.implementation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
    private static final String SHIRT_NUMBER = "shirtNumber";
    private static final String NATIONALITY = "nationality";
    private static final String PLAYER_DATA = "playerData";
    private static final String AGE = "age";
    private static final String APPEARANCES = "appearances";
    private static final String GOALS = "goals";
    private static final String ASSISTS = "assists";
    private static final String YELLOW_CARDS = "yellowCards";
    private static final String RED_CARDS = "redCards";
    private static final String MINUTES_PLAYED = "minutesPlayed";

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
        scrapePlayers(ScrapeUrls.getCurrentYearFullUrl());
    }

    // TODO update it to directly store the selector instead only the number of th
    private Map<String, Integer> parseTableHeaders() throws IOException {
        Document doc = connect(ScrapeUrls.getCurrentYearFullUrl());
        Elements headers = doc.select("thead tr th");

        Map<String, Integer> columnMap = new HashMap<>();

        for (int i = 0; i < headers.size(); i++) {
            Element th = headers.get(i);
            addColumnMapping(th, i, columnMap);
        }

        return columnMap;
    }

    private void addColumnMapping(Element th, int index, Map<String, Integer> columnMap) {
        String spanTitle = th.select("a > span").attr("title");
        String text = th.text().trim();

        if (text.contains("#")) {
            columnMap.put(SHIRT_NUMBER, index + 1);
        } else if (text.contains("Nat.")) {
            columnMap.put(NATIONALITY, index + 1);
        } else if (text.contains("Player")) {
            columnMap.put(PLAYER_DATA, index + 1);
        } else if (text.contains("Age")) {
            columnMap.put(AGE, index + 1);
        }

        switch (spanTitle) {
            case "Appearances" -> columnMap.put(APPEARANCES, index + 1);
            case "Goals" -> columnMap.put(GOALS, index + 1);
            case "Assists" -> columnMap.put(ASSISTS, index + 1);
            case "Yellow cards" -> columnMap.put(YELLOW_CARDS, index + 1);
            case "Red cards" -> columnMap.put(RED_CARDS, index + 1);
            case "Minutes played" -> columnMap.put(MINUTES_PLAYED, index + 1);
        }
    }

    private void scrapePlayers(String url) throws IOException {
        Map<String, Integer> tableHeaders = parseTableHeaders();

        Document doc = connect(url);
        Elements tableRows = doc.select("table tbody > tr");

        for (Element row : tableRows) {
            String playerNameSelector = "td:nth-child(" + tableHeaders.get(PLAYER_DATA) + ") table tbody tr td span.hide-for-small > a";
            String playerName = row.select(playerNameSelector).text();

            if (row.text().contains("Not used during this season") || row.text().contains("Not in squad during this season") || playerName.isBlank()) {
                continue;
            }

            Set<String> playerShirtNumbers = new HashSet<>();
            String playerShirtSelector = "td:nth-child(" + tableHeaders.get(SHIRT_NUMBER) + ") div";
            String playerShirtString = row.select(playerShirtSelector).text();
            String playerShirtData = playerShirtString.equals("-") ? "Unknown" : playerShirtString;
            playerShirtNumbers.add(playerShirtData);

            Set<String> playerPositions = new HashSet<>();
            String playerPositionSelector = "td:nth-child(" + tableHeaders.get(PLAYER_DATA) + ") table tbody tr:nth-child(2) > td";
            String playerPositionData = row.selectFirst(playerPositionSelector).text();
            playerPositions.add(playerPositionData);

            String playerAgeSelector = "td:nth-child(" + tableHeaders.get(AGE)  + ")";
            String playerAgeData = row.select(playerAgeSelector).text();
            int playerAge = convertStatToInteger(playerAgeData);

            Element portraitImgElement = row.selectFirst("table.inline-table img.bilderrahmen-fixed");
            String playerImgUrl = portraitImgElement != null ? portraitImgElement.attr("src") : "No image available";

            String playerNationalitySelector = "td:nth-child(" + tableHeaders.get(NATIONALITY) + ") img";
            Elements nationalities = row.select(playerNationalitySelector);
            List<String> playerNationalities = new ArrayList<>();

            for (Element nationality : nationalities) {
                playerNationalities.add(nationality.attr("title"));
            }

            String appareacesSelector = "td:nth-child(" + tableHeaders.get(APPEARANCES) + ")";
            String appareacesData = row.select(appareacesSelector).text();
            int appareances = convertStatToInteger(appareacesData);

            String goalsSelector = "td:nth-child(" + tableHeaders.get(GOALS) + ")";
            String goalsData = row.select(goalsSelector).text();
            int goals = convertStatToInteger(goalsData);

            String assistsSelector = "td:nth-child(" + tableHeaders.get(ASSISTS) + ")";
            String assistsData = row.select(assistsSelector).text();
            int assists = convertStatToInteger(assistsData);

            String yellowCardsSelector = "td:nth-child(" + tableHeaders.get(YELLOW_CARDS) + ")";
            String yellowCardsData = row.select(yellowCardsSelector).text();
            int yellowCards = convertStatToInteger(yellowCardsData);

            String redCardsSelector = "td:nth-child(" + tableHeaders.get(RED_CARDS) + ")";
            String redCardsData = row.select(redCardsSelector).text();
            int redCards = convertStatToInteger(redCardsData);

            String minutesPlayedSelector = "td:nth-child(" + tableHeaders.get(MINUTES_PLAYED) + ")";
            String minutesPlayedData = row.select(minutesPlayedSelector).text().replace("'", "").replace(".", "");
            int minutesPlayed = convertStatToInteger(minutesPlayedData);

            Player player = playerService.findPlayerByName(playerName);

            if (player != null) {
                playerService.updatePlayer(player, playerShirtData, playerPositionData, playerAge, appareances, goals, assists, yellowCards, redCards, minutesPlayed);
            } else {
                playerService.savePlayer(playerName, playerShirtNumbers, playerImgUrl, playerPositions, playerAge, playerNationalities, appareances, goals, assists, yellowCards, redCards, minutesPlayed);
            }
        }
    }

    private int convertStatToInteger(String stat) {
        return stat.equals("-") ? 0 : Integer.parseInt(stat);
    }
}
