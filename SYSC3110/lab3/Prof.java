/** SYSC 2101 - Prof-Student-TA Example
 * 
 *
 */

import java.util.ArrayList;
import java.util.Date;

public class Prof {
	private String name;

	//holds both TA and students
	private ArrayList<ProfListener> listeners;

	public Prof(String aName) {
		this.name = aName;

		this.listeners = new ArrayList<>();
	}

	//adds TA/student to list
	public void addProfListener(ProfListener listener) {
		this.listeners.add(listener);
	}

	//removes TA/student to list
	public void removeProfListener(ProfListener listener) {
		this.listeners.remove(listener);
	}


	public void setMidterm(Date date) {
		ProfEvent event = new ProfEvent(this, date);
		for (ProfListener listener : listeners) {
			listener.midtermAnnouced(event);
		}
	}
	
	public void postponeMidterm(Date date){
		ProfEvent event = new ProfEvent(this, date);
		for (ProfListener listener : listeners) {
			listener.midtermPostponed(event);
		}
	}

	public String getName() {
		return this.name;
	}

	public static void main(String[] args) {

		Prof p = new Prof("Safaa"); //change name to anything else but Safaal
		Student s = new Student("Mike");
		Student s2 = new Student("Anna");
		TeachingAssistant ta = new TeachingAssistant("Michael");
	
		p.addProfListener(s);
		p.addProfListener(s2);
		p.addProfListener(ta);

	
		Date midterm = new Date();
		p.setMidterm(midterm);
	
		p.postponeMidterm(new Date(midterm.getTime() + 1000000000));
	}

}
