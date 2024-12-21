package com.manumafe.players.data.playersdata.service.implementation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private static final String NAME = "name";
    private static final String POSITION = "position";
    private static final String PORTRAIT = "portrait";
    private static final String AGE = "age";
    private static final String APPEARANCES = "appearances";
    private static final String GOALS = "goals";
    private static final String ASSISTS = "assists";
    private static final String YELLOW_CARDS = "yellowCards";
    private static final String RED_CARDS = "redCards";
    private static final String MINUTES_PLAYED = "minutesPlayed";

// TODO Investigate why Terry has only 684 appareances
// 98/99 -> 7
// 99/00 -> 9
// 00/01 -> 26
// 01/02 -> 47
// 02/03 -> 29
// 03/04 -> 51
// 04/05 -> 53
// 05/06 -> 50
// 06/07 -> 45
// 07/08 -> 37
// 08/09 -> 51
// 09/10 -> 52
// 10/11 -> 46
// 11/12 -> 44
// 12/13 -> 27
// 13/14 -> 47
// 14/15 -> 49
// 15/16 -> 33
// 16/17 -> 14

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

    private Map<String, String> parsetableHeaderSelectorselectors() throws IOException {
        Document doc = connect(ScrapeUrls.getCurrentYearFullUrl());
        Elements headers = doc.select("thead tr th");

        Map<String, String> columnMap = new HashMap<>();

        for (int i = 0; i < headers.size(); i++) {
            Element th = headers.get(i);
            addColumnMapping(th, i, columnMap);
        }

        return columnMap;
    }

    private void addColumnMapping(Element th, int index, Map<String, String> columnMap) {
        String spanTitle = th.select("a > span").attr("title");
        String text = th.text().trim();
        int currentIndex = index + 1;

        if (text.contains("#")) {
            columnMap.put(SHIRT_NUMBER, "td:nth-child(" + currentIndex + ") div");
        } else if (text.contains("Nat.")) {
            columnMap.put(NATIONALITY, "td:nth-child(" + currentIndex + ") img");
        } else if (text.contains("Player")) {
            columnMap.put(NAME, "td:nth-child(" + currentIndex + ") table tbody tr td span.hide-for-small > a");
            columnMap.put(POSITION, "td:nth-child(" + currentIndex + ") table tbody tr:nth-child(2) > td");
        } else if (text.contains("Age")) {
            columnMap.put(AGE, "td:nth-child(" + currentIndex  + ")");
        }

        switch (spanTitle) {
            case "Appearances" -> columnMap.put(APPEARANCES, "td:nth-child(" + currentIndex + ")");
            case "Goals" -> columnMap.put(GOALS, "td:nth-child(" + currentIndex + ")");
            case "Assists" -> columnMap.put(ASSISTS, "td:nth-child(" + currentIndex + ")");
            case "Yellow cards" -> columnMap.put(YELLOW_CARDS, "td:nth-child(" + currentIndex + ")");
            case "Red cards" -> columnMap.put(RED_CARDS, "td:nth-child(" + currentIndex + ")");
            case "Minutes played" -> columnMap.put(MINUTES_PLAYED, "td:nth-child(" + currentIndex + ")");
        }

        columnMap.put(PORTRAIT, "table.inline-table img.bilderrahmen-fixed");
    }

    private void scrapePlayers(String url) throws IOException {
        Map<String, String> tableHeaderSelectors = parsetableHeaderSelectorselectors();

        Document doc = connect(url);
        Elements tableRows = doc.select("table tbody > tr");

        for (Element row : tableRows) {
            String playerName = row.select(tableHeaderSelectors.get(NAME)).text();

            if (row.text().contains("Not used during this season") || row.text().contains("Not in squad during this season") || playerName.isBlank()) {
                continue;
            }

            String playerShirtString = row.select(tableHeaderSelectors.get(SHIRT_NUMBER)).text();
            int playerShirtData = convertStatToInteger(playerShirtString);

            String playerPositionData = row.selectFirst(tableHeaderSelectors.get(POSITION)).text();

            String playerAgeData = row.select(tableHeaderSelectors.get(AGE)).text();
            int playerAge = convertStatToInteger(playerAgeData);

            Element portraitImgElement = row.selectFirst(tableHeaderSelectors.get(PORTRAIT));
            String playerImgUrl = portraitImgElement != null ? portraitImgElement.attr("src") : "No image available";

            Elements nationalities = row.select(tableHeaderSelectors.get(NATIONALITY));
            List<String> playerNationalities = new ArrayList<>();

            for (Element nationality : nationalities) {
                playerNationalities.add(nationality.attr("title"));
            }

            String appareacesData = row.select(tableHeaderSelectors.get(APPEARANCES)).text();
            int appareances = convertStatToInteger(appareacesData);

            String goalsData = row.select(tableHeaderSelectors.get(GOALS)).text();
            int goals = convertStatToInteger(goalsData);

            String assistsData = row.select(tableHeaderSelectors.get(ASSISTS)).text();
            int assists = convertStatToInteger(assistsData);

            String yellowCardsData = row.select(tableHeaderSelectors.get(YELLOW_CARDS)).text();
            int yellowCards = convertStatToInteger(yellowCardsData);

            String redCardsData = row.select(tableHeaderSelectors.get(RED_CARDS)).text();
            int redCards = convertStatToInteger(redCardsData);

            String minutesPlayedData = row.select(tableHeaderSelectors.get(MINUTES_PLAYED)).text().replace("'", "").replace(".", "");
            int minutesPlayed = convertStatToInteger(minutesPlayedData);

            Player player = playerService.findPlayerByName(playerName);

            if (player != null) {
                playerService.updatePlayer(player, playerShirtData, playerPositionData, playerAge, appareances, goals, assists, yellowCards, redCards, minutesPlayed);
            } else {
                playerService.savePlayer(playerName, playerShirtData, playerImgUrl, playerPositionData, playerAge, playerNationalities, appareances, goals, assists, yellowCards, redCards, minutesPlayed);
            }
        }
    }

    private int convertStatToInteger(String stat) {
        return stat.equals("-") ? 0 : Integer.parseInt(stat);
    }
}
