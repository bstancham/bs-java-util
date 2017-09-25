package info.bschambers.bsutil;

/**
 * <p>Designed to assist in building GUI interfaces for methods with multiple
 * parameters.</p>
 *
 * <p>The parameters class is usually just a container class maybe even just a
 * collection of public member fields.</p>
 *
 * <p>Child classes should define a no-args constructor which passes an array of
 * <code>SetupHelper.Item</code> objects to super. Then all that is required is
 * to write the getParams() method.</p>
 *
 * @param <P> A parameters class 
 */
public abstract class SetupHelper<P> {

    private String title;
    private Item[] items;

    public SetupHelper(String title, Item ... items) {
        this.title = title;
        this.items = items;
        // validate();
    }

    public String getTitle() { return title; }

    /**
     * @return A params object of the type P, based on the current
     * state of this SetupHelper.
     */
    public abstract P getParams();

    // /**
    //  * Sets all of the vairables of the containing Params class according to the
    //  * current values held in this SetupHelper instance.
    //  * <br/>Setup-helper is created as inner class and therefore has access to the variables
    //  * of it's containing class.
    //  */
    // public abstract void applySetup();
	
    public Item getItem(int index) { return items[index]; }

    public int getLength() { return items.length; }



    // /**
    //  * Calls <code>set()</code> with <code>validate</code> set to <code>true</code>.
    //  */
    // public boolean set(int index, Object value) {
    //     return set(index, value, true);
    // }

    /**
     * Attempts to set the specified parameter-item to the input value.
     * <br/>Automatically converts wrapper-classes into primitive data types if
     * the required type fits the type of wrapper-class.
     *
     * <p>TODO: UNIT TEST - every primitive type and object must set value
     * succesfully.</p>
     *
     * <p>TODO: UNIT TEST - In every case - must return true UNLESS it is the
     * wrong type.</p>
     *
     * <p>TODO: UNIT TEST - Validate should be called for every successful
     * set(), but not for unsuccessful attempts.</p>
     *
     * @param index The index of the parameter to set.
     * @param value The input value.
     * @return True, if the input value is of the correct class, otherwise false.
     */
    // * @param validate If <code>true</code>, validate() is called after a sucessful
    // * parameter-set attempt.
    // public boolean set(int index, Object value, boolean validate) {
    public boolean set(int index, Object value) {

        // convert wrapper classes to primitive data types...

        // byte
        if (value instanceof Byte &&
            items[index].type == byte.class) {
            items[index].value = (Byte) value;
            // short
        } else if (value instanceof Short &&
                   items[index].type == short.class) {
            items[index].value = (Short) value;
            // int
        } else if (value instanceof Integer &&
                   items[index].type == int.class) {
            items[index].value = (Integer) value;
            // long
        } else if (value instanceof Long &&
                   items[index].type == long.class) {
            items[index].value = (Long) value;
            // float
        } else if (value instanceof Float &&
                   items[index].type == float.class) {
            items[index].value = (Float) value;
            // double
        } else if (value instanceof Double &&
                   items[index].type == double.class) {
            items[index].value = (Double) value;
            // boolean
        } else if (value instanceof Boolean &&
                   items[index].type == boolean.class) {
            items[index].value = (Boolean) value;
            // char
        } else if (value instanceof Character &&
                   items[index].type == char.class) {
            items[index].value = (Character) value;
            // Object
	    // } else if (value.getClass() == items[index].type) {
        } else if (items[index].type.isInstance(value)) {
            items[index].value = items[index].type.cast(value);
            // else... (INVALID!)    
        } else {
            System.out.println(getClass() + ".set() --- OBJECT " + value
                               + " IS NOT AN INSTANCE OF " + items[index].type + "!");
            return false;
        }
        //
        // if (validate) validate();
        return true;
    }

    @Override
    public String toString() {
        String str = "<" + getClass().getSimpleName() + ": ";
        int count = 1;
        for (Item i : items) {
            str += "[" + count + ": type=" + i.type + " decription=" + i.description
                + " value=" + i.value + "]";
        }
        str += ">";
        return str;
    }



    /*----------------------- INNER CLASS: Item ------------------------*/

    /**
     * <p>Wrapper class for parameters.</p>
     */
    public static class Item {

        private Class type;
        private String description;
        private Object value;
        private Object[] permittedValues;
        private String[] permittedValueNames;

        public Item(Class type, String description, Object defaultValue) {
            this(type, description, defaultValue, null, null);
        }

        public Item(Class type, String description, Object defaultValue,
                    Object[] permittedValues, String[] permittedValueNames) {
            this.type = type;
            this.description = description;
            value = defaultValue;
            this.permittedValues = permittedValues;
            this.permittedValueNames = permittedValueNames;
        }

        public Class getType() { return type; }

        public String getDescription() { return description; }

        public Object getValue() { return value; }

        /**
         * @return An array containing all values permitted for this
         * item, or <code>null</code>, if no permitted values have
         * been set up.
         */
        public Object[] getPermitted() { return permittedValues; }

        public String[] getPermittedValueNames() { return permittedValueNames; }

        /**
         * Specify a list of possible permitted values for this item.
         */
        public void setPermitted(Object[] permittedValues, String[] names) {
            this.permittedValues = permittedValues;
            this.permittedValueNames = names;
        }

        @Override
        public String toString() {
            return "<" + getClass().getSimpleName() + ":"
                + " type=" + type.getSimpleName()
                + " description=\"" + description
                + "\" value=" + value + ">";
        }
    }
 
}
