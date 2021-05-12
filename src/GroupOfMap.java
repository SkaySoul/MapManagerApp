import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;
import java.util.Vector;

//Map Application
//Author: Maksim Zakharau, 256629 
//Data: October 2020;

public class GroupOfMap implements Iterable<Map>, Serializable {

	private static final long serialVersionUID = 1L;

	private String name;
	private GroupType type;
	private Collection<Map> collection;
	
	
	public GroupOfMap(GroupType type, String name) throws MapException {
		setName(name);
		if (type==null){
			throw new MapException("Incorrect type of collection");
		}
		this.type = type;
		setCollection(this.type.createCollection());
	}
	
	public GroupOfMap(String type_name, String name) throws MapException {
		setName(name);
		GroupType type = GroupType.find(type_name);
		if (type==null){
			throw new MapException("Incorrect type of collection");
		}
		this.type = type;
		setCollection(this.type.createCollection());
	}

	
	public void setName(String name) throws MapException {
		if ((name == null) || name.equals(""))
			throw new MapException("Name of group need to exist.");
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public GroupType getType() {
		return type;
	}
	public Collection<Map> getCollection() {
		return collection;
	}
	

	public void setCollection(Collection<Map> collection) {
		this.collection = collection;
	}
	
	public void setType(GroupType type) throws MapException {
		if (type == null) {
			throw new MapException("Type of collection need to exist");
		}
		if (this.type == type)
			return;
		Collection<Map> oldCollection = collection;
		collection = type.createCollection();
		this.type = type;
		for (Map map : oldCollection)
			collection.add(map);
	}
	
	public void setType(String type_name) throws MapException {
		for(GroupType type : GroupType.values()){
			if (type.toString().equals(type_name)) {
				setType(type);
				return;
			}
		}
		throw new MapException("This thype of collection is not exist");
	}
	public boolean add(Map e) {
		return collection.add(e);
	}

	public Iterator<Map> iterator() {
		return collection.iterator();
	}

	public int size() {
		return collection.size();
	}
	
	
	public void sortName() throws MapException {
		if (type==GroupType.HASH_SET){
			throw new MapException("'Set' type collection cannot be sorted");
		}
		
	}
	
	public void sortScale() throws MapException {
		if (type == GroupType.HASH_SET) {
			throw new MapException("'Set' type collection cannot be sorted");
		}
		Collections.sort((List<Map>) collection, new Comparator<Map>() {

			@Override
			public int compare(Map o1, Map o2) {
				if (o1.getScale() < o2.getScale())
					return -1;
				if (o1.getScale() > o2.getScale())
					return 1;
				return 0;
			}

		});
	}
	
	
	
	@Override
	public String toString() {
		return name + "  [" + type + "]";
	}
	
	public static void printToFile(PrintWriter writer, GroupOfMap group) {
		writer.println(group.getName());
		writer.println(group.getType());
		for (Map map : group.collection)
			Map.printToFile(writer, map);
	}
	
	public static void printToFile(String file_name, GroupOfMap group) throws MapException {
		try (PrintWriter writer = new PrintWriter(file_name)) {
			printToFile(writer, group);
		} catch (FileNotFoundException e){
			throw new MapException("File not found " + file_name);
		}
	}
	public static GroupOfMap readFromFile(BufferedReader reader) throws MapException{
		try {
			String group_name = reader.readLine();
			String type_name = reader.readLine();
			GroupOfMap groupOfMap = new GroupOfMap(type_name, group_name);

			Map map;
			while((map = Map.readFromFile(reader)) != null)
				groupOfMap.collection.add(map);
			return groupOfMap;
		} catch(IOException e){
			throw new MapException("Error on the file reading process");
		}
	}
	public static GroupOfMap readFromFile(String file_name) throws MapException {
		try (BufferedReader reader = new BufferedReader(new FileReader(new File(file_name)))) {
			return GroupOfMap.readFromFile(reader);
		} catch (FileNotFoundException e){
			throw new MapException("File not found " + file_name);
		} catch(IOException e){
			throw new MapException("Error on the file reading process");
		}
	}
}
enum GroupType {
	
	ARRAY_LIST("Arraylist Class"),
	HASH_SET("Hashset Class");

	String typeName;

	private GroupType(String type_name) {
		typeName = type_name;
	}


	@Override
	public String toString() {
		return typeName;
	}


	public static GroupType find(String type_name){
		for(GroupType type : values()){
			if (type.typeName.equals(type_name)){
				return type;
			}
		}
		return null;
	}

	public Collection<Map> createCollection() throws MapException {
		switch (this) {
		
		case ARRAY_LIST:  return new ArrayList<Map>();
		case HASH_SET:    return new HashSet<Map>();
		
		default:          throw new MapException("Gived type of collection is not implemented");
		}
	}

} 