package NickMakingLifeExpectancy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.utils.MapUtils;
import processing.core.PApplet;

public class LifeExpectancyMap extends PApplet {
	//create map
	UnfoldingMap map;
	
	Map<String, Float> lifeExpMap;
	
	//Creates two variables of abstract data type Feature and Marker in Lists.
	List<Feature> countries;
	List<Marker> countryMarkers;
	//first important method setup (runs once)
	public void setup(){
		size(800, 600, OPENGL);
		map = new UnfoldingMap(this, 50,50,700,500, new Google.GoogleMapProvider());
		MapUtils.createDefaultEventDispatcher(this, map);
		
		//create Object of Type Map
		lifeExpMap = loadLifeExpectancyFromCSV("LifeExpectancyWorldBank.csv");
		countries = GeoJSONReader.loadData(this, "countries.geo.json");
		countryMarkers = MapUtils.createSimpleMarkers(countries);
		map.addMarkers(countryMarkers);
		shadeCountries();
		
	}
	//second important method draw (loops continuously)
	public void draw(){
		
		map.draw();
		
	}
	
	private Map<String,Float> loadLifeExpectancyFromCSV(String fileName){
		//this is a helper method to help read in the data without
		//Cluttering up our setup method...
		
		//Constructor
		Map<String, Float> lifeExpMap = new HashMap<String, Float>();
		//Reference Type                       Object Type
		
		//read file
		String[] rows = loadStrings(fileName);

		//for each...in...
		//read in row one at a time
		for (String row : rows) {
			String[] columns = row.split(",");
			System.out.print(columns[4] + " = ");
			System.out.println(columns[5]);
			
			//parseFloat changes the string that was read in to a float...
			//make sure ".." is not a value that you are trying to parse into a float...
			if (columns[5].equals("..")){
				float value = 0;
				lifeExpMap.put(columns[4], value);
			}else{
				float value = Float.parseFloat(columns[5]);
				lifeExpMap.put(columns[4], value);
			}
		}
		
		
		//returns an object with both the countryID and the life expectancy level
		return lifeExpMap;
		
		
	}
	
	private void shadeCountries() {
		
		for (Marker marker : countryMarkers) {
			String countryId = marker.getId();
			System.out.println(countryId);
			if (lifeExpMap.containsKey(countryId)){
				float lifeExp = lifeExpMap.get(countryId);
				int colorLevel = (int) map(lifeExp, 40, 90, 10, 255);
				marker.setColor(color(255-colorLevel, 100, colorLevel));
				
			}else{
				marker.setColor(color(150,150,150));//shade grey if no value inside Feature I guess...
				
			}
		}
		
	}
}
