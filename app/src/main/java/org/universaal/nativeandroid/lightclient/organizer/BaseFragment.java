package org.universaal.nativeandroid.lightclient.organizer;


import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.inputmethod.InputMethodManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;


public abstract class BaseFragment extends Fragment {

    public static final String PRIMARY_ARG_TAG = "PRIMARY_ARG_TAG";
    public static final String STRING_ARRAY_ARG_TAG = "STRING_ARRAY_ARG_TAG";
    public static final String INITIATOR = "INITIATOR_ARG_TAG";
    private static final String TAG = "Base Fragment";

    public BaseFragment() {
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    Object data;

    @Subscribe
    public void onEvent(BusEvent event) {
        // Log.debug(TAG, "Base Fragment - onEvent triggered with id : " + event.getId());
    }

    protected boolean hasPrimaryArgument() {
        return null != getArguments().getString(PRIMARY_ARG_TAG);
    }

    protected String getPrimaryArgument() {
        return getArguments().getString(PRIMARY_ARG_TAG);
    }

    protected List<String> getStringArrayArguement() {
        if (getArguments().containsKey(STRING_ARRAY_ARG_TAG)) {
            return getArguments().getStringArrayList(STRING_ARRAY_ARG_TAG);
        }
        return null;
    }


    protected Class getInitiator() {
        if (getArguments().containsKey(INITIATOR)) {
            String className = getArguments().getString(INITIATOR);
            try {
                return Class.forName(className);
            } catch (ClassNotFoundException e) {
            }
        }
        return null;
    }


    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        hideKeyboard(getActivity());
        super.onStop();

    }


    public static void hideKeyboard(Activity activity) {
        try {
            InputMethodManager inputMethodManager =
                    (InputMethodManager) activity.getSystemService(
                            Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }


}
