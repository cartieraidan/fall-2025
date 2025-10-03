import java.util.Date;
import java.util.EventObject;

public class ProfEvent extends EventObject {
    private Date midterm;


    public ProfEvent(Object source, Date midterm) {
        super(source);
        this.midterm = midterm;


    }

   public Date getMidterm() {
        return midterm;
   }


}
