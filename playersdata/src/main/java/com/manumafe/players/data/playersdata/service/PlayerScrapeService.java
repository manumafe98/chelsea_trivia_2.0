package com.manumafe.players.data.playersdata.service;

import java.io.IOException;

import org.jsoup.nodes.Document;

public interface PlayerScrapeService {

    Document connect(String url) throws IOException;

    void scrapeAllPlayers() throws IOException;

    void scrapeCurrentYearPlayers() throws IOException;
}
