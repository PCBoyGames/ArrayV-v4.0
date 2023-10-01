package panes;

import java.awt.HeadlessException;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

// Many thanks to Freek de Bruijn on StackOverflow for providing a custom JOptionPane.
// https://stackoverflow.com/questions/14407804/how-to-change-the-default-text-of-buttons-in-joptionpane-showinputdialog?noredirect=1&lq=1
public class JEnhancedOptionPane extends JOptionPane {
 /**
  *
  */

 public static String showInputDialog(String title, Object message, Object[] options) throws HeadlessException {
     JOptionPane pane = new JOptionPane(message, QUESTION_MESSAGE,
                                              OK_CANCEL_OPTION, null,
                                              options, null);
     pane.setWantsInput(true);
     pane.setComponentOrientation((getRootFrame()).getComponentOrientation());
     pane.setMessageType(QUESTION_MESSAGE);
     pane.selectInitialValue();
     JDialog dialog = pane.createDialog(null, title);
     dialog.setVisible(true);
     dialog.dispose();
     Object value = pane.getInputValue();
     return (value == UNINITIALIZED_VALUE) ? null : (String) value;
 }
}