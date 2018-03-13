package org.redrock.algorithm.ga;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

public class Tour{

    private ArrayList<City> tour = new ArrayList<>();
    private double fitness = 0;
    private int distance = 0;

    public Tour(){
        for (int i = 0; i < TourManager.numberOfCities(); i++) {
            tour.add(null);
        }
    }

    public Tour(ArrayList tour){
        this.tour = tour;
    }

    public void generateIndividual() {
        for (int cityIndex = 0; cityIndex < TourManager.numberOfCities(); cityIndex++) {
            setCity(cityIndex, TourManager.getCity(cityIndex));
        }
        Collections.shuffle(tour);
    }

    public City getCity(int tourPosition) {
        return (City)tour.get(tourPosition);
    }

    public void setCity(int tourPosition, City city) {
        tour.set(tourPosition, city);
        fitness = 0;
        distance = 0;
    }

    public double getFitness() {
        if (fitness == 0) {
            fitness = 1/(double)getDistance();
        }
        return fitness;
    }

    public int getDistance(){
        if (distance == 0) {
            int tourDistance = 0;
            for (int cityIndex=0; cityIndex < tourSize(); cityIndex++) {
                City fromCity = getCity(cityIndex);
                City destinationCity;
                if(cityIndex+1 < tourSize()){
                    destinationCity = getCity(cityIndex+1);
                }
                else{
                    destinationCity = getCity(0);
                }
                tourDistance += fromCity.distanceTo(destinationCity);
            }
            distance = tourDistance;
        }
        return distance;
    }

    public int tourSize() {
        return tour.size();
    }

    public boolean containsCity(City city){
        return tour.contains(city);
    }

    @Override
    public String toString() {
        List<Map<String, String>> citys = new ArrayList<>();
        for (int i = 0; i < tourSize(); i++) {
            Map<String, String> name = new HashMap<>();
            name.put("name", tour.get(i).getName());
            citys.add(name);
        }
        JSONArray jsonArray = new JSONArray(citys);
        JSONObject object = new JSONObject();
        object.put("order", jsonArray);
        object.put("distance", getDistance() / 10000);
        return object.toString();
    }
}