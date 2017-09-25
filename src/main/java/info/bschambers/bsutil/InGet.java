package info.bschambers.bsutil;

import java.awt.Component;
import javax.swing.*;
//import javax.swing.BoxLayout;
//import java.text.NumberFormat;
import java.text.DecimalFormat;
import javax.swing.text.DefaultFormatter;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JSpinner;
import javax.swing.SwingConstants;
import javax.swing.SpinnerListModel;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import javax.swing.BorderFactory;

/**
 * <p>Static classes for making GUI components for getting user input.</p>
 *
 * <p>TODO: Proper value checking for all text-field inputters... </p>
 */
public abstract class InGet {

    protected Class inputClass;
    protected JPanel pan;

    public InGet() {
	pan = new JPanel();
	// pan.setLayout(new BoxLayout(pan, BoxLayout.X_AXIS));
	pan.setAlignmentX(Component.LEFT_ALIGNMENT);
	pan.setBorder(BorderFactory.createEtchedBorder());
    }

    //public abstract boolean isValid();

    public abstract Object getVal();

    /**
     * <p>This is for use with RunnerGUI.Updater...</p>
     *
     * <p>... RunnerGUI.Updater uses this in it's constructor to pass in the
     * method-runner function call. Therefore every class should facilitate this
     * by keeping track of input-action, and when an update occurs call
     * actionPerformed.</p>
     *
     * @param al The input-action.
     */
    public abstract void addUpdateAction(final ActionListener al);

    public Class getInputClass() { return inputClass; }

    public Component getGUI() { return pan; }

    @Override
    public String toString() {
	return "<INPUT-GETTER: type=" + getClass().getSimpleName() + " val=" + getVal() + ">";
    }



    /**
     * <p>Returns and input-getter that gets the type of the input-type.</p>
     *
     * <p>TODO: Add case for all primitives</p>
     * <p>TODO: Title</p>
     * <p>TODO: Support for chooser-lists...</p>
     *
     * @param c The input type.
     */
    public static InGet getInputGetter(String description, Class c, Object defaultValue) {
	if ((c == int.class || c == Integer.class) && defaultValue instanceof Integer)
	    return new Int(description, (Integer) defaultValue);
	else if (c == double.class && defaultValue instanceof Double)
	    return new Dbl(description, (Double) defaultValue);
	else if (c == boolean.class && defaultValue instanceof Boolean)
	    return new CheckBox(description, (Boolean) defaultValue);
	// else...
	return new Arg(defaultValue, c);
    }






    /*--------------------------------------------------------------------------------*/
    /*-------------------------------- FIXED ARGUMENT --------------------------------*/
    /*--------------------------------------------------------------------------------*/

    /**
     * <p>Simply returns the input object as a method argument...
     * ... GUI component is just a JLabel showing the type of object.</p>
     */
    public static class Arg extends InGet {
	private Object val;
	public Arg(Object obj, Class inputClass) {
	    val = obj;
	    this.inputClass = inputClass;
	    pan.add(new JLabel("ARG: " + inputClass.getSimpleName()));
	}
	@Override
        public Object getVal() { return inputClass.cast(val); }
	@Override
        public void addUpdateAction(final ActionListener al) {}
    }


    


    /*------------------------------- TEXT-FIELD TYPES -------------------------------*/

    public static abstract class TextFieldGet extends InGet {
        
	/**
	 * Child classes should instantiate this in constructor!
	 */
	protected JFormattedTextField textField =
            new JFormattedTextField(new DefaultFormatter());
        
	@Override
        public void addUpdateAction(final ActionListener al) {
	    PropertyChangeListener cl = new PropertyChangeListener() {
	    	    @Override
                    public void propertyChange(PropertyChangeEvent e) {
			al.actionPerformed(new ActionEvent(this, 0, "propertyChange"));
	    	    }
	    	};
	    textField.addPropertyChangeListener(cl);
	}
    }



    public static class Int extends TextFieldGet {
	private int val;
	public Int(String label, int defaultVal) {
	    // textField = new JFormattedTextField(new DecimalFormat());
	    val = defaultVal;
	    inputClass = int.class;
	    textField.setColumns(5);
	    textField.setText("" + val);
	    //textField.setPreferredSize(new Dimension(50, 10));
	    // make panel
	    pan.add(new JLabel("(INT)"));
	    pan.add(textField);
	    pan.add(new JLabel(label));
	    //
	    pan.setMaximumSize(pan.getPreferredSize());
	}
	@Override
        public Object getVal() {
	    val = Integer.parseInt(textField.getText());
	    return val;
	}
    }



    public static class Dbl extends TextFieldGet {
	private double val;
	public Dbl(String label, double defaultVal) {
	    // textField = new JFormattedTextField(new DecimalFormat());
	    val = defaultVal;
	    inputClass = double.class;
	    textField.setColumns(10);
	    textField.setText("" + val);
	    // make panel
	    pan.add(new JLabel("(DOUBLE)"));
	    pan.add(textField);
	    pan.add(new JLabel(label));
	    //
	    pan.setMaximumSize(pan.getPreferredSize());
	}
	@Override
        public Object getVal() {
	    val = Double.parseDouble(textField.getText());
	    return val;
	}
    }



    public static class Flt extends TextFieldGet {
	private float val;
	public Flt(String label, float defaultVal) {
	    // textField = new JFormattedTextField(new DecimalFormat());
	    val = defaultVal;
	    inputClass = float.class;
	    textField.setColumns(5);
	    textField.setText("" + val);
	    // make panel
	    pan.add(new JLabel("(FLOAT)"));
	    pan.add(textField);
	    pan.add(new JLabel(label));
	    //
	    pan.setMaximumSize(pan.getPreferredSize());
	}
	@Override
        public Object getVal() {
	    val = Float.parseFloat(textField.getText());
	    return val;
	}
    }



    public static class Str extends TextFieldGet {
	private String val;
	public Str(String label, String defaultVal) {
	    // textField = new JFormattedTextField(new DefaultFormatter());
	    val = defaultVal;
	    inputClass = String.class;
	    textField.setColumns(20);
	    textField.setText(val);
	    // make panel
	    pan.add(new JLabel("(STRING)"));
	    pan.add(textField);
	    pan.add(new JLabel(label));
	    //
	    pan.setMaximumSize(pan.getPreferredSize());
	}
	@Override
        public Object getVal() {
	    val = textField.getText();
	    return val;
	}
    }





    /*------------------------------------ OTHERS ------------------------------------*/

    public static class CheckBox extends InGet {
        
	private JCheckBox cbox = new JCheckBox();
	private boolean val;
	private ActionListener updateAction = null;
        
	public CheckBox(String label, boolean defaultVal) {
	    val = defaultVal;
	    inputClass = boolean.class;
	    cbox.setSelected(val);
	    // make panel
	    pan.add(cbox);
	    pan.add(new JLabel(label));
	    //
	    pan.setMaximumSize(pan.getPreferredSize());
	    //
	    cbox.addActionListener(new ActionListener() {
		    @Override
                    public void actionPerformed(ActionEvent e) {
			val = !val;
			System.out.println("... InGet.CheckBox: state=" + val);
			if (updateAction != null)
			    updateAction.actionPerformed(
                                new ActionEvent(this, 0, "CheckBox.updateAction.actionPerformed"));
		    }
		});
	}
        
	@Override
        public Object getVal() { return val; }
        
	@Override
        public void addUpdateAction(final ActionListener al) {
            updateAction = al;
        }
    }




    public static class Clr extends InGet {
        
	private IntTextField red;
	private IntTextField green;
	private IntTextField blue;
	private Color val;
        
	public Clr() {
	    this(new Color(150, 150, 150));
	}
        
	public Clr(Color c) {
	    inputClass = Color.class;
	    //
	    val = c;
	    red = new IntTextField(0, 255, c.getRed());	    
	    green = new IntTextField(0, 255, c.getGreen());	    
	    blue = new IntTextField(0, 255, c.getBlue());	    
	    //
	    pan.add(new JLabel("(COLOR)"));
	    pan.add(new JLabel("red"));
	    pan.add(red);
	    pan.add(new JLabel("green"));
	    pan.add(green);
	    pan.add(new JLabel("blue"));
	    pan.add(blue);
	    //
	    pan.setMaximumSize(pan.getPreferredSize());
	}
        
	@Override
        public Object getVal() {
	    val = new Color(red.getVal(),
			    green.getVal(),
			    blue.getVal());
	    return val;
	}
        
	@Override
        public void addUpdateAction(final ActionListener al) {
	    System.out.println(getClass().getSimpleName() + ".addUpdateAction() NOT YET IMPLEMENTED!");
	}
    }



    /**
     * <p>TODO: Get selection list to drop down when clicked on...</p>
     * <p>TODO: set column number automatically based on size of min and max values</p>
     */
    public static class Spinner extends InGet implements ChangeListener {
        
	private Object[] objects;
	private Object val;
	private JSpinner spinner;
	private ActionListener updateAction = null;

	public Spinner(Object[] objects, Class c) {
	    this.objects = objects;
	    inputClass = c;
	    //
	    spinner = new JSpinner(new SpinnerListModel(objects));
	    spinner.addChangeListener(this);
	    pan.add(spinner);
	    //
	    pan.setMaximumSize(pan.getPreferredSize());
	    //
	    val = objects[0];
	}

	@Override
        public void stateChanged(ChangeEvent e) {
	    val = spinner.getModel().getValue();
	    //
	    if (updateAction != null)
		updateAction.actionPerformed(
                    new ActionEvent(this, 0, "Spinner.updateAction.actionPerformed"));
	}

	@Override public Object getVal() {
	    return val;
	}

	@Override
        public void addUpdateAction(final ActionListener al) {
	    updateAction = al;
	    //System.out.println(getClass().getSimpleName() + ".addUpdateAction() NOT YET IMPLEMENTED!");
	}
    }

    /**
     * <p>TODO: Get selection list to drop down when clicked on...</p>
     * <p>TODO: set column number automatically based on size of min and max values</p>
     */
    public static class ComboBox extends InGet implements ActionListener {
        
	private String title;
	private Object[] objects;
        private String[] names;
	private Object val;
	private JComboBox<String> combo;
	private ActionListener updateAction = null;

	public ComboBox(Object[] objects, Class c) {
	    this(null, objects, null, c);
	}

	public ComboBox(String title, Object[] objects, Class c) {
	    this(title, objects, null, c);
	}

	public ComboBox(String title, Object[] objects, String[] objectNames, Class c) {
	    this.title = title;
	    this.objects = objects;
            names = objectNames;
	    inputClass = c;

            // if names array is null, build new array from class and hashcodes
            if (names == null) {
                names = new String[objects.length];
                for (int i = 0; i < objects.length; i++)
                    names[i] = objects[i].getClass().getSimpleName() + "#" + objects[i].hashCode();
            }

            // build GUI component
	    combo = new JComboBox<String>(names);
	    combo.setSelectedIndex(0);
	    combo.addActionListener(this);
	    pan.add(combo);
	    if (title != null)
		pan.add(new JLabel(title));
	    pan.setMaximumSize(pan.getPreferredSize());
            
	    // default value is first item in objects
	    val = objects[0];
	}

	@Override
        public void actionPerformed(ActionEvent e) {
	    val = objects[((JComboBox) e.getSource()).getSelectedIndex()];
	    if (updateAction != null)
		updateAction.actionPerformed(
                    new ActionEvent(this, 0, "ComboBox.updateAction.actionPerformed"));
	}

	@Override
        public Object getVal() { return val; }

	@Override
        public void addUpdateAction(final ActionListener al) {
	    updateAction = al;
	    //System.out.println(getClass().getSimpleName() +
            // ".addUpdateAction() NOT YET IMPLEMENTED!");
	}
    }





    /*------------------------------- UTILITY CLASSES --------------------------------*/

    /**
     * <p>TODO: set column number automatically based on size of min and max values</p>
     */
    private class IntTextField extends JFormattedTextField {
        
	private int val;
	private int min;
	private int max;
        
	public IntTextField(int min, int max, int defaultVal) {
	    this.min = min;
	    this.max = max;
	    val = defaultVal;
	    setText("" + val);
	    setColumns(5);
	}
        
	public int getVal() {
	    val = Integer.parseInt(getText());
	    if (val < min) {
		System.out.println(this.getClass().getName()
				   + ": value is less that min - "
                                   + "substituting min value (" + min + ")");
		val = min;
	    } else if (val > max) {
		System.out.println(this.getClass().getName()
				   + ": value is greater that max - "
                                   + "substituting max value (" + max + ")");
		val = max;
	    }
	    return val;
	}
    }

}
