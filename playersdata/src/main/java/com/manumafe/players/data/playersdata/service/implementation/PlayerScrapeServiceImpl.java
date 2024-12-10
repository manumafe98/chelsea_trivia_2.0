package com.manumafe.players.data.playersdata.service.implementation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import com.manumafe.players.data.playersdata.document.Player;
import com.manumafe.players.data.playersdata.service.PlayerScrapeService;
import com.manumafe.players.data.playersdata.service.PlayerService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlayerScrapeServiceImpl implements PlayerScrapeService {

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
            String playerShirtString = row.select("td:nth-child(1) div").text();

            if (playerShirtString.isBlank() || playerShirtString.equals("-")) {
                continue;
            }

            int playerShirtNumber = Integer.parseInt(playerShirtString);

            if (row.text().contains("Not used during this season")) {
                continue;
            }

            String playerName = row.selectFirst("td:nth-child(2) table tbody tr td span.hide-for-small > a").text();
            String playerPosition = row.selectFirst("td:nth-child(2) table tbody tr:nth-child(2) > td").text();
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

            Player player = new Player();
            player.setFullName(playerName);
            player.setShirtNumber(playerShirtNumber);
            player.setProfileImageUrl(playerImgUrl);
            player.setPosition(playerPosition);
            player.setAge(playerAge);
            player.setNationalities(playerNationalities);
            player.setAppareances(appareances);
            player.setGoals(goals);
            player.setAssists(assists);
            player.setYellowCardsReceived(yellowCards);
            player.setRedCardsReceived(redCards);
            player.setMinutesPlayed(minutesPlayed);

            playerService.savePlayer(player);
        }
    }
}
