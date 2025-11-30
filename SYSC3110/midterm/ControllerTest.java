import org.junit.Before;
import org.junit.Test;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.*;

public class ControllerTest {

    private Controller controller;
    private JButton[][] buttons;
    private ArrayList<JButton> buttonList;

    @Before
    public void setUp() {
        controller = new Controller();
        buttons = controller.getButtons();

        buttonList = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            buttonList.addAll(Arrays.asList(buttons[i]).subList(0, 4));
        }



       // for (JButton button : buttonList) {
        //    System.out.println(button.getText());
        //}

    }

    @Test
    public void testSequenceNotSame() {
        Sequence sequence1 = new Sequence();
        Sequence sequence2 = new Sequence();

        assertNotEquals(sequence1, sequence2);

    }

    @Test
    public void testFrameCreated() {
        Controller controllerTest = new Controller();
        assertNotNull(controllerTest);
    }

    @Test
    public void testSuccessfulGame() {
    //programmed to go almost to finish because when game is finished it System.exit which unit test not like
        int count = 1;
        while (count < 15) {
            for (JButton button : buttonList) {
                if (button.getText().equals(Integer.toString(count))) {
                    count++;

                    ActionEvent test = new ActionEvent(button, 1, "");
                    controller.actionPerformed(test);

                    assertFalse(controller.doneGame());

                }
            }



        }

        assertEquals(16, count);




    }

    /** don't knwo how to handle system exit with junit
    @Test
    public void testFailedGame() {

        int count = 1;
        while (controller != null) {
            for (JButton button : buttonList) {
                count++;

                ActionEvent test = new ActionEvent(button, 1, "");
                controller.actionPerformed(test);

            }

            assertNotEquals(16, count);

        }



    }

     */

}
