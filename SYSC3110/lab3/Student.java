/** SYSC 2101 - Prof-Student-TA Example
 * 
 *
 */

import java.util.Date;

public class Student implements ProfListener {
	private String name;
	private Date midterm;


	public Student(String aName) {
		this.name = aName;
	}

	private String getProfName(ProfEvent event) {
		Prof prof = (Prof) event.getSource();
		return prof.getName();
	}

	@Override
	public void midtermAnnouced(ProfEvent event) {
		String prof = getProfName(event);

		study(event.getMidterm(), prof);

	}

	@Override
	public void midtermPostponed(ProfEvent event) {
		String prof = getProfName(event);
		party(event.getMidterm(), prof);
	}

	public String getName() {
		return this.name;
	}

	public void study(Date date, String prof){
		this.midterm = date;

		if(prof.equals("Safaa")) {
			System.out.println(name + " : Bruh! I have to study hard for my midterm on " + this.midterm);
		}
		else {
			System.out.println(name + " : Doh! I have to study hard for my midterm on " + this.midterm);
		}
	}
	
	public void party(Date date, String prof){
		this.midterm = date;

		if(prof.equals("Safaa")) {
			System.out.println(name + " : Noooooo! I get to party since my midterm isn't until " + this.midterm);
		}
		else {
			System.out.println(name + " : Alright! I get to party since my midterm isn't until " + this.midterm);
		}
	}
}
