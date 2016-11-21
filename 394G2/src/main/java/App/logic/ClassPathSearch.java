package App.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import App.SQLcon;

public final class ClassPathSearch {
	//priority
	public int area;
	public int major;
	private int fall, winter, spring, summer;
	//ArrayList with all classes taken by student
	private ArrayList<String> classesTaken;
	//arraylist of all required classes not yet taken
	private ArrayList<String> requirementsNeeded;
	public ArrayList<String> fallclasses, winterclasses, springclasses, summerclasses;
	//hashmap with area as key, and another hashmap as value. Value hashap contains all classes within that area (with classname as key and termsoffered as value. 
	//First entry is 'Required' which contains all required classes
	private HashMap<String, HashMap<String, String>> theClasses;
	private ArrayList<String> concClassesAvailable;
	private ArrayList<String> electiveClasses;
	//ArrayList containing HashMaps with term name as key, number of classes as value. Is ordered according to starting term provided by user
	public ArrayList<Map<String, Integer>> order;
	private boolean finished;
	private int areaAmount;
	public int flag = 0;
	public String start;
	
	//map of each terms classes taken, for result display
	public Map<String, ArrayList<String>> resultTerms;
	
	
	public ClassPathSearch(int userid, int major, int area, int fall, int winter, int spring, int summer, String start) {
		this.major = major;
		this.area = area;
		this.fall = fall;
		this.winter = winter;
		this.spring = spring;
		this.summer = summer;
		this.finished = false;
		this.start = start;
		this.areaAmount = this.getAreas(major);
		order = this.getOrder(start);
		this.classesTaken = getClassesTaken(userid);
		this.requirementsNeeded = getRequirementsNeeded(userid);
		this.theClasses = new HashMap<String, HashMap<String, String>>();
		this.concClassesAvailable = new ArrayList<String>();
		this.electiveClasses = new ArrayList<String>();
		
		//final result arraylists
		this.fallclasses = new ArrayList<String>();
		this.winterclasses = new ArrayList<String>();
		this.springclasses = new ArrayList<String>();
		this.summerclasses = new ArrayList<String>();
		//the map for the final result terms
		this.resultTerms = new HashMap<String, ArrayList<String>>();
		resultTerms.put("Fall", this.fallclasses);
		resultTerms.put("Spring", this.springclasses);
		resultTerms.put("Winter", this.winterclasses);
		resultTerms.put("Summer", this.summerclasses);
		this.execute();
	}
	
	private int getAreas(int major) {
		String q = "SELECT AreasAmount FROM Major WHERE ID=" + major;
		String areas = SQLcon.singleResultQuery(q, "AreasAmount");
		return Integer.valueOf(areas);
		
	}
	
	public void clearclasses() {
		Iterator it = theClasses.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, HashMap<String, String>> e = (Map.Entry<String, HashMap<String, String>>)it.next();
			e.getValue().clear();
		}
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
	
	private boolean execute() {
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
		
		//execute fill according to order provided
		int cycles = 0;
		while (!this.finished) {
			if (this.finished) return true;
			for (Map<String, Integer> e : this.order) {
				Iterator i = e.entrySet().iterator();
				Map.Entry<String, Integer> en = (Map.Entry<String, Integer>)i.next();
				this.fill(en.getKey(), en.getValue());
				//System.out.println(en.getKey());
				if (this.finished) return true;
			}
			cycles++;
			if (cycles > 10) {
				this.flag = 1;
				return true;
			}
		}
		return this.finished;
	}
	
	private int fill(String term, int cycles) {
		//find requirement class (loop until failure)
		//find concentration class (loop until failure)
		//find elective (loop until failure)
		System.out.println(term + "-------------------------------");
		ArrayList<String> requirementClasses = new ArrayList<String>();
		ArrayList<String> concClasses = new ArrayList<String>();
		ArrayList<String> electClasses = new ArrayList<String>();
		
		//assign an arraylist to add classes to
		ArrayList<String> termlist;
		if (term == "Fall") termlist = this.fallclasses;
		else if (term == "Winter") termlist = this.winterclasses;
		else if (term == "Spring") termlist = this.springclasses;
		else termlist = this.summerclasses;
		
		//buffer for checking continuity
		ArrayList<String> buffer = new ArrayList<String>();
		int classesfound = 0;
		int electsfound = 0;
		for (int i = 0; i < cycles; i++) {
			if (classesfound >= cycles) break;
			//search to find requirement classes
			boolean goRequirement = true;
			int startIndex = 0;
			while (goRequirement) {
				if (classesfound >= cycles) {
					goRequirement = false;
					break;
				}
				if (this.requirementsNeeded.size() == 0) {
					goRequirement = false;
					break;
				}
				int classFound = findRequirement(startIndex, term);
				if (classFound > -1) {
					// add to collection  
					String theClass = this.requirementsNeeded.get(classFound);
					requirementClasses.add(theClass);
					buffer.add(theClass);
					termlist.add(theClass);
					//System.out.println("req: " + theClass);
					//set start index to where it previously left search
					startIndex = classFound+1;
					classesfound++;
				}
				//if no more classes found, stop looking through requirements
				else goRequirement = false;
			}
			//search to find concentration classes
			//reset start index
			startIndex = 0;
			boolean goConc = true;
			while (goConc) {
				//update requirements needed
				this.updateReqs(requirementClasses);
				if (classesfound >= cycles) {
					goConc = false;
					break;
				}
				if (this.checkConc()) {
					goConc = false;
					break;
				}
				int classFound = findConc(startIndex, term, buffer);
				if (classFound > -1) {
					/** add to collection */ 
					String theClass = this.concClassesAvailable.get(classFound);
					//System.out.println("conc: " + theClass);
					concClasses.add(theClass);
					buffer.add(theClass);
					termlist.add(theClass);
					startIndex = classFound+1;
					classesfound++;
				}
				//if no more classes found, stop looking through concentration classes
				else goConc = false;
			}
			startIndex = 0;
			boolean goElect = true;
			while (goElect) {
				if (classesfound >= cycles) {
					goElect = false;
					break;
				}
				if ((this.checkElectivesNum() + electsfound) >= 8) {
					goElect = false;
					break;
				}
				else {
					int classFound = findElect(startIndex, term, buffer);
					if (classFound > -1) {
						/** add to collection */ 
						String theClass = this.electiveClasses.get(classFound);
						//System.out.println("elect " + term + " " + theClass);
						electClasses.add(theClass);
						buffer.add(theClass);
						termlist.add(theClass);
						startIndex = classFound+1;
						classesfound++;
						electsfound++;
					}
					//if no more classes found, stop looking through elective classes
					else goElect = false;
				}
				
			}
		}
		//add separator to distinguish between terms. ex: Fall 2016 or Fall 2017
		termlist.add("---");
		//add to classestaken
		if (buffer.size() > 0) this.classesTaken.addAll(buffer);
		buffer = null;
		termlist = null;
		//update requirements needed
		//this.updateReqs(requirementClasses);
		if (this.requirementsNeeded.size() == 0 && this.checkConc() && this.checkElectives()) this.finished = true;
		System.out.println("req size: " + this.requirementsNeeded.size());
		if (this.checkConc()) System.out.println("checkconc true");
		if (this.checkElectives()) System.out.println("checkelectiv true");
		return 1;
	}
	
	private void updateReqs(ArrayList<String> c) {
		ArrayList<String> tmp = new ArrayList<String>();
		for (String n : this.requirementsNeeded) {
			if (!c.contains(n)) tmp.add(n);
		}
		this.requirementsNeeded = null;
		this.requirementsNeeded = tmp;
	}
	
	private int findRequirement(int index, String term) {
		int size = this.requirementsNeeded.size();
		if (index >= size) return -1;
		for (int i = index; i < size; i++) {
			if (checkPreq(this.requirementsNeeded.get(i))) {
				String terms = new String(this.theClasses.get("Required").get(this.requirementsNeeded.get(i)));
				if (terms.contains(term)) return i;
			}
		}
		return -1;
	}
	
	private int findConc(int index, String term, ArrayList<String> buf) {
		int size = this.concClassesAvailable.size();
		if (index >= size) return -1;
		for (int i = index; i < size; i++) {
			if (checkPreq(this.concClassesAvailable.get(i))) {
				String terms = new String(this.theClasses.get(((Integer)area).toString()).get(this.concClassesAvailable.get(i)));
				if (checkTaken(this.concClassesAvailable.get(i), buf)) {
					if (terms.contains(term)) return i;
				}
				
			}
		}
		return -1;
	}
	
	private int findElect(int index, String term, ArrayList<String> buf) {
		int size = this.electiveClasses.size();
		if (index >= size) return -1;
		for (int i = index; i < size; i++) {
			if (checkPreq(this.electiveClasses.get(i))) {
				String terms = new String(SQLcon.singleResultQuery("SELECT TypicallyOffered FROM Class WHERE Name='" + this.electiveClasses.get(i) + "'", "TypicallyOffered"));
				if (checkTaken(this.electiveClasses.get(i), buf)) {
					if (terms.contains(term)) return i;
				}
				
			}
		}
		return -1;
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
	
	private boolean checkConc() {
		int count = 0;
		for (String n : this.classesTaken) {
			if (this.theClasses.get(((Integer)this.area).toString()).containsKey(n)) count++;
		}
		if (count >= 4) return true;
		else return false;
	}
	
	private boolean checkElectives() {
		int count = 0;
		for (String n : this.classesTaken) {
			if (!this.theClasses.get("Required").containsKey(n)) count++;
		}
		if (count >= 8) return true;
		else return false;
	}

	
	private int checkElectivesNum() {
		int count = 0;
		for (String n : this.classesTaken) {
			if (!this.theClasses.get("Required").containsKey(n)) count++;
		}
		return count;
	}
	

	
	private ArrayList<Map<String, Integer>> getOrder(String start) {
		if (start.equals("Fall")) {
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
		else if (start.equals("Winter")) {
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
		else if (start.equals("Spring")) {
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
		if (area == 1) name = new String("Required");
		else name = new String(areaid);
		//get required classes
		String q = "SELECT Major_Classes.ClassID, Class.TypicallyOffered FROM Major_Classes INNER JOIN Class ON Major_Classes.ClassID = Class.Name WHERE Major_Classes.MajorID=? AND Major_Classes.AreaID=?";
		String[] params = {majorid, areaid};
		String[] columns = {new String("ClassID"), new String("TypicallyOffered")};
		HashMap<String, String> result = SQLcon.multiResultQuery(q, params, columns, 2, 2);
		int reqCount = Integer.valueOf(result.get("Count"));
		this.theClasses.put(name, new HashMap<String, String>());
		HashMap<String, String> required = theClasses.get(name);
		for (int i = 0; i < reqCount; i++) {
			required.put(result.get(new String(i + new String("ClassID"))), result.get(new String(i + new String("TypicallyOffered"))));
		}
		
	}
	
	public String display() {
		String result = "";
		Iterator it = this.theClasses.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, HashMap<String, String>> e = (Map.Entry<String, HashMap<String, String>>)it.next();
			Iterator ite = e.getValue().entrySet().iterator();
			while (ite.hasNext()) {
				Map.Entry<String, String> en = (Map.Entry<String, String>) ite.next();
				result += e.getKey() + " -- " + en.getKey() + " - " + en.getValue() + "<br>";
			}
		}
		
		result += "<b>CLASSESTAKEN</b><br>";
		for (String c : this.classesTaken) {
			result += c + "<br>";
		}
		result += "<b>Order</b><br>";
		for (Map<String, Integer> e : this.order) {
			Iterator itt = e.entrySet().iterator();
			Map.Entry d = ((Map.Entry<String, Integer>)itt.next());
			result += d.getKey() + " - " + d.getValue() + "<br>";
		}
		result += this.requirementsNeeded.size();
		for (Map<String, Integer> e : this.order) {
			Iterator itt = e.entrySet().iterator();
			Map.Entry d = ((Map.Entry<String, Integer>)itt.next());
			result +=this.show((String)d.getKey());
		}
		return result;
	}
	
	public String show(String term) {
		String result = "";
		int j = 1;
		ArrayList<String> t = this.resultTerms.get(term);
		for (String cls : t) {
			if (cls != "---") result += cls + "<br>";
			else result += "End term " + term + (j++) + "<br>";
		}
		return result;
	}
	
}
