package com.group_0862.triage;

import java.io.Serializable;

/**
 * This class represents Patient's name.
 */
public class Name implements Serializable {
	
	
	private static final long serialVersionUID = 1L;
	public String firstName;
	public String[] middleNames;
	public String lastName;
	
	/**
	 * Initializes Name with firstName and lastName, and optional middleName
	 * @param nameList
	 * 
	 * Pre-condition: nameList has either 2 or 3 items
	 */
	public Name (String[] nameList){
		if (nameList.length > 2){
			String[] middleNames = nameList[1].split(" ");
			this.middleNames = middleNames;
		}
		else {
			String[] middleNames = {};
			this.middleNames = middleNames;
		}
		this.firstName = nameList[0];
		this.lastName = nameList[nameList.length - 1];
	}
	
	/**
	 * @return String representation of this
	 */
	public String toString(){
		String middleName = "";
		for (String name : middleNames) {
			middleName += " " + name;
		}
		return this.firstName + middleName + " " + this.lastName;
	}
	
	public boolean hasCorrectFormat(){
		return (this.firstName != null && this.lastName != null);
	}

}
