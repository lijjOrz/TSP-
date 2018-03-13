package org.redrock.algorithm.ga;

import org.json.JSONArray;
import org.json.JSONObject;
import org.redrock.algorithm.servlet.TestSocket;

import java.io.*;
import java.util.concurrent.TimeUnit;

public class TSP_GA {

    public static void handle(TestSocket socket, String message) throws InterruptedException, IOException {
        TourManager.init();
        JSONArray jsonArray = new JSONArray(message);
        for (int i = 0; i < jsonArray.length(); i++) {
            String name = jsonArray.getJSONObject(i).getString("name");
            City city = City.getCity(name);
            TourManager.addCity(city);
        }
        Population population = new Population(50, true);
        population = GA.evolvePopulation(population);
        Tour fittest = population.getFittest();
        int before = getTimestamp();
        for (int count = 0; count < 7000; count++) {
            for (int i = 0; i < 100; i++) population = GA.evolvePopulation(population);
            int now = getTimestamp();
            if (now - before > 5 && GA.getMutationRate() < 0.5 ) GA.addMutationRate();
            if (population.getFittest().getDistance() < fittest.getDistance()) {
                fittest = population.getFittest();
                if (now - before < 2) TimeUnit.SECONDS.sleep(2 - now + before);
                socket.sendMessage(fittest.toString());
            }
            before = getTimestamp();
        }
        JSONObject result = new JSONObject(fittest.toString());
        result.put("status", "result");
        socket.sendMessage(result.toString());
    }

    static int getTimestamp() { return (int) (System.currentTimeMillis() / 1000); }
}