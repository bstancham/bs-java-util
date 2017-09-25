package info.bschambers.bsutil;

import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Box;
//import javax.swing.border.TitledBorder;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

/**
 * <p>Dynamically generate GUI for running a method via reflection...</p>
 *
 * <p>... where possible, user controls are automatically created for method
 * parameters...</p>
 *
 * <p>Method is looked up by name using reflection. If it fails, it prints error
 * message to terminal.</p>
 *
 * <p>Inner classes define various different GUI elements...
 * <br/>... numeric
 * <br/>... String
 * <br/>... object-chooser - define object-chooser objects for different types
 * of objects e.g. generator objects etc</p>
 *
 * <p>TODO: ? Parameters are checked... start button only becomes active when
 * legal parameters are defined...</p>
 *
 * <p>TODO: ? Cancel button - cancels operation without doing anything...?</p>
 *
 * <p>TODO: ? Disposes of it's own GUI after process has run (optional?)</p>
 */
public class RunnerGUI {

    private MethodRunner runner;
    protected String name;
    protected InGet[] inputs;
    protected Component gui;

    public RunnerGUI(Object o, String methodName) {
	this(o, methodName, new InGet[] {});
    }
    // public RunnerGUI(Object o, String methodName, Object arg) {
    // 	this(o, methodName, new InGet[] { new Inget.Arg(arg) });
    // }
    
    public RunnerGUI(Object o, String methodName, InGet input) {
	this(o, methodName, new InGet[] { input });
    }
    
    public RunnerGUI(Object object, String methodName, InGet[] inputs) {

	// make args array for MethodRunner
	Class[] argsTypes = new Class[inputs.length];
	for (int i = 0; i < inputs.length; i++) argsTypes[i] = inputs[i].getInputClass();
	runner = new MethodRunner(object, methodName, argsTypes);

	// init other variables
	name = runner.getMethod().getName();
	this.inputs = inputs;

	makeGUI();
    }

    protected void makeGUI() {
	// no inputs method?
	if (noInputs()) {
	    gui = getActionButton(name);
	} else {
	    JPanel panel = new JPanel();
	    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
	    panel.setBorder(BorderFactory.createTitledBorder(name));
	    // add contents to panel
	    for (InGet ig : inputs) panel.add(ig.getGUI());
	    // start button
	    panel.add(getActionButton("RUN"));
	    panel.add(Box.createVerticalGlue());
	    gui = panel;
	}
    }

    private JButton getActionButton(String title) {
	JButton button = new JButton(title);
	button.addActionListener(new ActionListener() {
		@Override public void actionPerformed(ActionEvent e) {
		    runMethod();
		} });
	return button;
    }

    protected void runMethod() {
	runner.run(getMethodArgs());
    }

    protected boolean noInputs() {
	if (inputs.length == 0) return true;
	boolean b = true;
	for (InGet ig : inputs)
	    if (!(ig instanceof InGet.Arg))
		b = false;
	return b;
    }

    public Component getGUI() { return gui; }

    private Object[] getMethodArgs() {
    	if (inputs == null) return null;
    	Object[] args = new Object [inputs.length];
    	for (int i = 0; i < inputs.length; i++) {
    	    args[i] = inputs[i].getVal();
    	    // System.out.println("... getMethodArgs: arg=" + i + " val=" + args[i]);
    	}
    	return args;
    }

    @Override
    public String toString() {
	String output = "RunnerGUI: " + name;
	if (inputs == null) { // check for no-args method
	    output += "\n... NO INPUTTERS...";
	} else {
	    for (InGet ig : inputs)
		output += "\n... " + ig;
	}
	return output;
    }



    /**
     * Performs action every time any of the input-getters is updated.
     */
    public static class Updater extends RunnerGUI {

	public Updater(Object o, String methodName, InGet input) {
	    this(o, methodName, new InGet[] { input });
	}
	public Updater(Object o, String methodName, InGet[] inputs) {
	    super(o, methodName, inputs);
	    // add an update-action to each input...
	    ActionListener action = new AbstractAction() {
		    @Override public void actionPerformed(ActionEvent e) {
			runMethod();
		    }};
	    for (InGet ig : inputs) ig.addUpdateAction(action);
	}

	@Override protected void makeGUI() {
	    // no inputs method?
	    if (noInputs()) {
		gui = new JLabel("TFUpdater HAS NO INPUTS!");
	    } else {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		if (inputs.length > 1)
		    panel.setBorder(BorderFactory.createTitledBorder(name));
		// add contents to panel
		for (InGet ig : inputs) panel.add(ig.getGUI());
		//
		gui = panel;
	    }
	}
    }
    
}
