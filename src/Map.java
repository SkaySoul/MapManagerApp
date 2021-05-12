import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.*;


//Map Application
//Author: Maksim Zakharau, 256629 
//Data: October 2020;

public class Map implements Serializable, Comparable<Map> {
	
	private static final long serialVersionUID = 1L;
	
	private String countryname;
	private int scale;
	private Country country;
	private ArrayList<String> citylist;
	
	
	public Map(String countryname, int scale) throws MapException {
		this.setCountryname(countryname);
		this.setScale(scale);
		this.setCountry(countryname);
		this.setCitylist(createcityarray(getCountry()));
	}
	
	public void setCountry(Country country) {
		this.country = country;	
	}
	
	public Country getCountry() {
		return country;
	}

	public ArrayList<String> createcityarray(Country country) {
		ArrayList<String> cityarray = new ArrayList<>();
		Collections.addAll(cityarray, country.getCities());
			return cityarray;
	}
	
	public void printCityArray(ArrayList<String> list) {
		for (int i = 0; i < list.size(); i++) {
			if (i == list.size() - 1) {
				System.out.println(list.get(i) + ".");
			} else
				System.out.println(list.get(i) + ", ");

		}
	}


	public ArrayList<String> getCitylist() {
		return citylist;
	}



	public void setCitylist(ArrayList<String> citylist) {
		this.citylist = citylist;
	}



	public int getScale() {
		return scale;
	}



	public void setScale(int scale) throws MapException {
		if (scale < 1)
			throw new MapException("Scale cant be less than 1");
		this.scale = scale;
	}

	public void setScale(String scale) throws MapException {
		if (scale == null || scale.equals("")) {
			setScale(1);
			return;
		}
		try {
			setScale(Integer.parseInt(scale));
		} catch (NumberFormatException e) {
			throw new MapException("Scale needs to be an integer");

		}
	}

	public String getCountryname() {
		return countryname;
	}

	public void setCountry(String country) throws MapException {
		for (Country countries: Country.values()) {
			if (countries.name().equals(country.toUpperCase())) {
				this.country = countries;
				return;
			}
			
	}
		if (this.country == null || country.equals("")) {
			this.country = Country.UNDEFINED;
			throw new MapException("Cannot find the country");
		}
	}

	public void setCountryname(String countryname) throws MapException {
		if ((countryname == null) || countryname.equals(""))
			throw new MapException("You need to write a name of country");
		this.countryname = countryname;
	}

	public static void printToFile(PrintWriter writer, Map map) {
		writer.println(map.countryname + "#" + map.scale);
	}

	public static void printToFile(String file_name, Map map) throws MapException {
		try (PrintWriter writer = new PrintWriter(file_name)) {
			printToFile(writer, map);
		} catch (FileNotFoundException e) {
			throw new MapException("File not found " + file_name);
		}
	}

	public static Map readFromFile(BufferedReader reader) throws MapException {
		try {
			String line = reader.readLine();
			String[] txt = line.split("#");
			Map map = new Map(txt[0], Integer.parseInt(txt[1]));
			map.setCountry(txt[0]);
			return map;
		} catch (IOException e) {
			throw new MapException("Error on the file reading process");
		}
	}

	public static Map readFromFile(String file_name) throws MapException {
		try (BufferedReader reader = new BufferedReader(new FileReader(new File(file_name)))) {
			return Map.readFromFile(reader);
		} catch (FileNotFoundException e) {
			throw new MapException("File not found " + file_name);
		} catch (IOException e) {
			throw new MapException("Error on the file reading process");
		}
	}
	
	@Override
	public String toString() {
		return countryname + " " + scale;
	}

	
	public boolean equals(Object obj) {
	    if (this == obj)
	      return true; 
	    if (obj == null)
	      return false; 
	    if (!(obj instanceof Map))
	      return false; 
	    Map other = (Map)obj;
	    if (this.countryname == null) {
	      if (other.countryname != null)
	        return false; 
	    } else if (!this.countryname.equals(other.countryname)) {
	      return false;
	    } 
	    if (Integer.toString(this.scale) == null) {
	      if (Integer.toString(other.scale) != null)
	        return false; 
	    } else if (!Integer.toString(other.scale).equals(Integer.toString(other.scale))) {
	      return false;
	    } 
	    return true;
	  }
	

	  public int compareTo(Map o) {
	    int result = this.countryname.compareTo(o.countryname);
	    if (result == 0)
	      result = Integer.toString(this.scale).compareTo(Integer.toString(o.scale)); 
	    return result;
	  }
	  
	  public int hashCode() {
		    int prime = 31;
		    int result = 1;
		    result = 31 * result + ((this.countryname == null) ? 0 : this.countryname.hashCode());
		    result = 31 * result + ((Integer.toString(this.scale) == null) ? 0 : Integer.toString(this.scale).hashCode());
		    return result;
		  }
}

enum Country{
	UNDEFINED(new String[] {"NO INFO"}),
	BELARUS(new String[] {"Minsk","Brest" ,"Vitebsk" ,"Gomel","Grodno" ,"Mogilev"}),
	POLAND(new String[] {"Warszawa" ,"Krakow" , "Wroclaw","Gdansk" ,"Lodz" ,"Bialystok" ,"Lublin", "Poznan"}),
	GERMANY(new String[] {"Berlin" ,"Munchen" , "Drezden","Frankfurt" ,"Leipzig" ,"Koln" ,"Stuttgart"});
	
	private String[] cities;
	Country(String[] cities) {
		this.setCities(cities);
	}
	public String[] getCities() {
		return cities;
	}
	public void setCities(String[] cities) {
		this.cities = cities;
	}
	
	
}
class MapException extends Exception {

	private static final long serialVersionUID = 1L;

	public MapException(String message) {
		super(message);
	}
}
