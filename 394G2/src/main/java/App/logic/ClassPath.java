package App.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import App.SQLcon;

public class ClassPath {
	//priority
	public int area;
	public int major;
	private int fall, winter, spring, summer;
	//ArrayList with all classes taken by student
	private ArrayList<String> classesTaken;
	//arraylist of all required classes not yet taken
	private ArrayList<String> requirementsNeeded;
	private ArrayList<String> fallclasses, winterclasses, springclasses, summerclasses;
	//hashmap with area as key, and another hashmap as value. Value hashap contains all classes within that area (with classname as key and termsoffered as value. 
	//First entry is 'Required' which contains all required classes
	private HashMap<String, HashMap<String, String>> theClasses;
	private ArrayList<String> concClassesAvailable;
	private ArrayList<String> electiveClasses;
	//ArrayList containing HashMaps with term name as key, number of classes as value. Is ordered according to starting term provided by user
	private ArrayList<Map<String, Integer>> order;
	//number of areas for the major
	private int areaAmount;
	
	
	public ClassPath(int userid, int major, int area, int fall, int winter, int spring, int summer, String start) {
		this.major = major;
		this.area = area;
		this.fall = fall;
		this.winter = winter;
		this.spring = spring;
		this.summer = summer;
		this.areaAmount = this.getAreas(major);
		order = this.getOrder(start);
		this.classesTaken = getClassesTaken(userid);
		this.requirementsNeeded = getRequirementsNeeded(userid);
		this.theClasses = new HashMap<String, HashMap<String, String>>();
		this.concClassesAvailable = new ArrayList<String>();
		this.electiveClasses = new ArrayList<String>();
		

		this.execute();
	}
	
	private int getAreas(int major) {
		String q = "SELECT AreasAmount FROM Major WHERE ID=" + major;
		String areas = SQLcon.singleResultQuery(q, "AreasAmount");
		return Integer.valueOf(areas);
		
	}
	
	private ArrayList<String> getRequirementsNeeded(int userid) {
		String q = "SELECT ClassID FROM Major_Classes WHERE AreaID=?";
		String[] params = {((Integer)1).toString()};
		String[] columns = {"ClassID"};
		HashMap<String, String> result = SQLcon.multiResultQuery(q, params, columns, 1, 1);
		int count = Integer.valueOf(result.get("Count"));
		ArrayList<String> classes = new ArrayList<String>();
		for (int i = 0; i<count; i++) classes.add(result.get(i + "ClassID"));
		ArrayList<String> leftoverClasses = new ArrayList<String>();
		for (int i = 0; i<count; i++) if (!classesTaken.contains(classes.get(i))) leftoverClasses.add(classes.get(i));
		return leftoverClasses;
	}
	
	private ArrayList<String> getClassesTaken(int user) {
		String query = "SELECT ClassID FROM Course_Taken WHERE StudentUserID=?";
		String[] params = {((Integer)user).toString()};
		String[] columns = {"ClassID"};
		HashMap<String, String> result = SQLcon.multiResultQuery(query,  params,  columns,  1,  1);
		int c = Integer.valueOf(result.get("Count"));
		ArrayList<String> a = new ArrayList<String>();
		for (int i = 0; i<c; i++) a.add(result.get(i + "ClassID"));
		return a;
	}
	
	private void execute() {
		//fill theClasses hashmap with all classes
		for (int i = 1; i <= this.areaAmount; i++) {
			this.populate(this.major, i);
		}
		
		//fill concClassesAvailable arraylist with concentration classes
		HashMap<String, String> conc = theClasses.get(((Integer)this.area).toString());
		Iterator itr = conc.entrySet().iterator();
		while (itr.hasNext()) {
			Map.Entry<String, String> e = (Map.Entry<String, String>)itr.next();
			this.concClassesAvailable.add(e.getKey());
		}
		
		//fill electiveClasses arraylist
		for (int i = 2; i<this.areaAmount; i++) { //start at 2 so it doesnt get requirement classes
			String area = ((Integer)i).toString();
			HashMap<String, String> elect = theClasses.get(area);
			Iterator itrs = elect.entrySet().iterator();
			while (itrs.hasNext()) {
				Map.Entry<String, String> e = (Map.Entry<String, String>)itrs.next();
				this.electiveClasses.add(e.getKey());
			}
		}
		
	
	}

	

	private boolean checkTaken(String n, ArrayList<String> buf) {
		if (this.classesTaken.contains(n) || buf.contains(n)) return false;
		return true;
	}
	
	private boolean checkPreq(String classname) {
		String q = "SELECT PreqClassID FROM Class_Preqs WHERE ClassID=?";
		String[] params = {classname};
		String[] columns = {"PreqClassID"};
		HashMap<String, String> result = SQLcon.multiResultQuery(q, params, columns, 1, 1);
		int count = Integer.valueOf(result.get("Count"));
		for (int i = 0; i<count; i++) {
			if (!this.classesTaken.contains(result.get(i + "PreqClassID"))) return false;
		}
		return true;
	}
	
	public ArrayList<String> getPreq(String classname) {
		String q = "SELECT PreqClassID FROM Class_Preqs WHERE ClassID=?";
		String[] params = {classname};
		String[] columns = {"PreqClassID"};
		HashMap<String, String> result = SQLcon.multiResultQuery(q, params, columns, 1, 1);
		ArrayList<String> thepreqs = new ArrayList<String>();
		int count = Integer.valueOf(result.get("Count"));
		for (int i = 0; i<count; i++) {
			thepreqs.add(result.get(i + "PreqClassID"));
		}
		return thepreqs;
	}


	
	private ArrayList<Map<String, Integer>> getOrder(String start) {
		if (start == "Fall") {
			Map<String, Integer> a = new HashMap<String, Integer>();
			a.put("Fall", this.fall);
			Map<String, Integer> b = new HashMap<String, Integer>();
			b.put("Winter", this.winter);
			Map<String, Integer> c = new HashMap<String, Integer>();
			c.put("Spring", this.spring);
			Map<String, Integer> d = new HashMap<String, Integer>();
			d.put("Summer", this.summer);
			ArrayList<Map<String, Integer>> ar = new ArrayList<Map<String, Integer>>(4);
			ar.add(a);
			ar.add(b);
			ar.add(c);
			ar.add(d);
			return ar;
		}
		else if (start == "Winter") {
			HashMap<String, Integer> a = new HashMap<String, Integer>();
			a.put("Winter", this.winter);
			HashMap<String, Integer> b = new HashMap<String, Integer>();
			b.put("Spring", this.spring);
			HashMap<String, Integer> c = new HashMap<String, Integer>();
			c.put("Summer", this.summer);
			HashMap<String, Integer> d = new HashMap<String, Integer>();
			d.put("Fall", this.fall);
			ArrayList<Map<String, Integer>> ar = new ArrayList<Map<String, Integer>>(4);
			ar.add(a);
			ar.add(b);
			ar.add(c);
			ar.add(d);
			return ar;
		}
		else if (start == "Spring") {
			HashMap<String, Integer> a = new HashMap<String, Integer>();
			a.put("Spring", this.spring);
			HashMap<String, Integer> b = new HashMap<String, Integer>();
			b.put("Summer", this.summer);
			HashMap<String, Integer> c = new HashMap<String, Integer>();
			c.put("Fall", this.fall);
			HashMap<String, Integer> d = new HashMap<String, Integer>();
			d.put("Winter", this.winter);
			ArrayList<Map<String, Integer>> ar = new ArrayList<Map<String, Integer>>(4);
			ar.add(a);
			ar.add(b);
			ar.add(c);
			ar.add(d);
			return ar;
		}
		else {
			HashMap<String, Integer> a = new HashMap<String, Integer>();
			a.put("Summer", this.summer);
			HashMap<String, Integer> b = new HashMap<String, Integer>();
			b.put("Fall", this.fall);
			HashMap<String, Integer> c = new HashMap<String, Integer>();
			c.put("Winter", this.winter);
			HashMap<String, Integer> d = new HashMap<String, Integer>();
			d.put("Spring", this.spring);
			ArrayList<Map<String, Integer>> ar = new ArrayList<Map<String, Integer>>(4);
			ar.add(a);
			ar.add(b);
			ar.add(c);
			ar.add(d);
			return ar;
		}
	}
	
	private void populate(int major, int area) {
		String majorid = ((Integer)major).toString();
		String areaid = ((Integer)area).toString();
		String name;
		if (area == 1) name = "Required";
		else name = areaid;
		//get required classes
		String q = "SELECT Major_Classes.ClassID, Class.TypicallyOffered FROM Major_Classes INNER JOIN Class ON Major_Classes.ClassID = Class.Name WHERE Major_Classes.MajorID=? AND Major_Classes.AreaID=?";
		String[] params = {majorid, areaid};
		String[] columns = {"ClassID", "TypicallyOffered"};
		HashMap<String, String> result = SQLcon.multiResultQuery(q, params, columns, 2, 2);
		int reqCount = Integer.valueOf(result.get("Count"));
		this.theClasses.put(name, new HashMap<String, String>());
		HashMap<String, String> required = theClasses.get(name);
		for (int i = 0; i < reqCount; i++) {
			required.put(result.get(i + "ClassID"), result.get(i + "TypicallyOffered"));
		}
		
	}

}
