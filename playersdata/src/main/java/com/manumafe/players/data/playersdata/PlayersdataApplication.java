package com.manumafe.players.data.playersdata;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class PlayersdataApplication {
	public static void main(String[] args) throws Exception {
		String url = "https://www.transfermarkt.com/chelsea-fc/leistungsdaten/verein/631/reldata/%262024/plus/1";
		Document doc = Jsoup.connect(url).get();

		Elements tableRows = doc.select("table tbody > tr");

		for (Element row : tableRows) {
			String playerShirtNumber = row.select("td:nth-child(1) div").text();

			if (playerShirtNumber.isBlank() || playerShirtNumber.equals("-")) {
				continue;
			}

			if (row.text().contains("Not used during this season")) {
				continue;
			}

			String playerName = row.selectFirst("td:nth-child(2) table tbody tr td span.hide-for-small > a").text();
			String playerPosition = row.selectFirst("td:nth-child(2) table tbody tr:nth-child(2) > td").text();
			String playerAge = row.select("td:nth-child(3)").text();

			Element portraitImgElement = row.selectFirst("table.inline-table img.bilderrahmen-fixed");
			String playerImg = portraitImgElement != null ? portraitImgElement.attr("src") : "No image available";

			Elements nationalityImgs = row.select("td:nth-child(4) img");
			StringBuilder playerNationalities = new StringBuilder();
			for (Element img : nationalityImgs) {
				if (playerNationalities.length() > 0) {
					playerNationalities.append(", ");
				}
				playerNationalities.append(img.attr("title"));
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

			System.out.println("shirt_number: " + playerShirtNumber);
			System.out.println("img_url: " + playerImg);
			System.out.println("name: " + playerName);
			System.out.println("position: " + playerPosition);
			System.out.println("age: " + playerAge);
			System.out.println("nationalities: " + playerNationalities);
			System.out.println("appareances: " + appareances);
			System.out.println("goals: " + goals);
			System.out.println("assists: " + assists);
			System.out.println("yellow_cards: " + yellowCards);
			System.out.println("red_cards: " + redCards);
			System.out.println("minutes_played: " + minutesPlayed);
			System.out.println("-------");
		}
	}
}
