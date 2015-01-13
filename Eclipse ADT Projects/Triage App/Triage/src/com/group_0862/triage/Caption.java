package com.group_0862.triage;

import java.io.IOException;
import java.text.ParseException;

/**
 * Child class in PatientItem's children array. 
 * Has a predefined list of "left" items (which are represented as TextViews in the adapter)
 * and "right" items, made from the PatientItem's patient (which are represented as EditTexts in the adapter).
 * @author Tim
 *
 */
public class Caption {
	
	public String left;
	public String right;
	public boolean checked;
	public Patient p;
	private static final String noData = "no data";
	
	public enum Infos {
		HEALTHNUM ("Health Card Number:"),
		NAME ("Name:"), 
		BIRTH ("Birth Date (yyyy/mm/dd):"), 
		DOCTOR ("Attended:"), 
		TEMP ("Temperature:"), 
		BP ("Blood Pressure:"), 
		HR ("Heart Rate:"), 
		DISCHARGE ("Discharged:");
		
		private final String name;
		Infos(String name) {
			this.name = name;
		}
		
		public String getName(){
			return name;
		}
	}
	
	public Caption (Infos info, Patient p) {
		this.checked = false;
		this.left = info.name;
		switch(info) {
		case HEALTHNUM:
			this.right = (p.getHealthNum() == null ? noData : p.getHealthNum().toString());
			break;
		case BIRTH:
			this.right = (p.getBirthdate() == null ? noData : p.getBirthdate().toString());
			break;
		case BP:
			this.right = ((p.getBp() == null || p.getBp().size() == 0) ? noData : p.getBp().firstKey().toString());
			break;
		case DISCHARGE:
			this.right = (p.getDischarged() == null ? noData : p.getDischarged().toString());
			break;
		case DOCTOR:
			this.right = ((p.getAttended() == null || p.getAttended().size() == 0) ? noData : p.getAttended().firstKey().toString());
			break;
		case HR:
			this.right = ((p.getHr() == null || p.getHr().size() == 0) ? noData : p.getHr().firstKey().toString());
			break;
		case NAME:
			this.right = (p.getName() == null ? noData : p.getName().toString());
			break;
		case TEMP:
			this.right = ((p.getTemp() == null || p.getTemp().size() == 0) ? noData : p.getTemp().firstKey().toString());
			break;
		default:
			this.right = noData;
			break;
		}
	}
	
	public void updatePatient(Infos info, Patient p) throws ParseException {
		switch(info) {
		case HEALTHNUM:
			try {
				p.setHealthNum(Integer.parseInt(this.right));
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case BIRTH:
			Birthdate newBirthDate = new Birthdate();
			newBirthDate.setLenient(false);  // will get angry if your date is improperly configured
			String[] bDate = this.right.split("/");
			Integer year = Integer.parseInt(bDate[0]);
			Integer month = Integer.parseInt(bDate[1]);
			Integer day = Integer.parseInt(bDate[2]);
			try {
				newBirthDate.set(year, month, day, 0, 0, 0);  // 0's replace hour of day, minute and second values, which aren't important
				p.setBirthdate(newBirthDate);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case BP:
			BloodPressure newBp = p.getBp();
			newBp.update(this.right, p);
			p.setBp(newBp);
			break;
		case DISCHARGE:
			p.setDischarged(Boolean.valueOf(this.right));
			break;
		case DOCTOR:
			Attended newAtt = p.getAttended();
			newAtt.update(this.right, p);
			p.setAttended(newAtt);
			break;
		case HR:
			HeartRate newHr = p.getHr();
			newHr.update(this.right, p);
			p.setHr(newHr);
			break;
		case NAME:
			String[] newName = this.right.split(" ");
			p.setName(new Name(newName));
			break;
		case TEMP:
			Temperature newTemp = p.getTemp();
			newTemp.update(this.right, p);
			p.setTemp(newTemp);
			break;
		default:
			break;
		}
	}
}
