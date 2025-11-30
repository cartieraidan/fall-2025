import java.util.ArrayList;
import java.util.Collections;

public class Sequence {

    private ArrayList<Integer> sequence;

    public Sequence() {
        sequence = new ArrayList<>();

        for (int i = 1; i <= 16; i++) {
            sequence.add(i);
        }

        Collections.shuffle(sequence);

        /**
        for (Integer i : sequence) {
            System.out.print(i + " ");
        }
         */
    }

    public Integer getIndex(int i) {
        return sequence.get(i);
    }

}
