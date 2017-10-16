package org.universaal.nativeandroid.lightclient.organizer;



import java.util.ArrayList;


public class FragmentEvent extends BusEvent {

    private ArrayList<String> stringArrayList;

    public Class getType() {
        return classType;
    }

    public void setType(Class type) {
        this.classType = type;
    }

    private Class classType;
    private boolean isReturning;

    public FragmentEvent(Class type) {
        super(null);
        this.classType = type;
        isReturning = false;
    }

    public FragmentEvent(Class type, boolean isReturning) {
        super(null);
        this.classType = type;
        this.isReturning = isReturning;
    }

    public boolean isReturning() {
        return isReturning;
    }

    public FragmentEvent setReturning(boolean returning) {
        isReturning = returning;
        return this;
    }

    private Class initiatorFragment;
    public Class getInitiatorFragment() {
        return initiatorFragment;
    }

    public void setInitiatorFragment(Class initiatorFragment) {
        this.initiatorFragment = initiatorFragment;
    }

    public ArrayList<String> getStringArrayList() {
        return stringArrayList;
    }

    public void setStringArrayList(ArrayList<String> stringArrayList) {
        this.stringArrayList = stringArrayList;
    }
}
