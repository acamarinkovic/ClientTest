package org.universaal.nativeandroid.lightclient.organizer;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.SparseArray;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;


public class FragmentOrganizer extends AbstractFragmentOrganizer {

    private static final String TAG = "FRAGMENT ORGANIZER";
    private SparseArray<List<Class>> containersMap;

    protected Class initialFragment;

    public FragmentOrganizer(FragmentManager fragmentManager, Class fragment) {
        super(fragmentManager);
        firstTime=true;
        containersMap = new SparseArray<>();
        this.initialFragment = fragment;
    }


    public FragmentOrganizer(FragmentManager fragmentManager, FragmentManager fragmentManager2, Class fragment) {
        super(fragmentManager);
        containersMap = new SparseArray<>();
        this.initialFragment = fragment;
    }

    /**
     * Fragment factory method
     */
    @Subscribe
    @Override
    public void onEvent(FragmentEvent event) {
        Bundle arguments = new Bundle();
        arguments.putString(BaseFragment.PRIMARY_ARG_TAG, event.getId());

        if (event.getInitiatorFragment() != null) {
            arguments.putString(BaseFragment.INITIATOR, event.getInitiatorFragment().getName());
        }
        if (event.getStringArrayList() != null) {
            arguments.putStringArrayList(BaseFragment.STRING_ARRAY_ARG_TAG, event.getStringArrayList());
        }
        openFragment(createFragment(event.getType()), arguments, getFragmentContainer(event.getType()));
    }

    @Override
    public boolean handleBackNavigation() {
        try {
        Fragment fragment = getOpenFragment();
            if (fragment.getClass().isAnnotationPresent(IgnoreBackHandling.class)) {
                return true;
            }
            if (fragment.getClass().equals(initialFragment)) {
                return false;
            } else {
                fragmentManager.popBackStack();
                return true;
            }
        } catch (Exception e) {
            return false;
        }

    }

    public Fragment getCurrentFragment() {
        String tag = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName();
        return fragmentManager.findFragmentByTag(tag);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Fragment currentFragment = getCurrentFragment();
        if (currentFragment != null) {
            currentFragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    protected int getFragmentContainer(Class fragment) {
        for (int i = 0; i < containersMap.size(); i++) {
            int key = containersMap.keyAt(i);
            List<Class> fragments = containersMap.get(key);
            for (Class f : fragments) {
                if (f.equals(fragment)) {
                    return key;
                }
            }
        }
        return -1;
    }

    public void setUpContainer(int containerResourceId, ArrayList<Class> containerFragments) {
        setUpContainer(containerResourceId, containerFragments, false);
    }

    public void setUpContainerWitchAnimation(int containerResourceId, ArrayList<Class> containerFragments) {

        setUpContainer(containerResourceId, containerFragments, false);
        containersWithAnimation.add(containerResourceId);

    }

    public void setUpContainer(int containerResourceId, ArrayList<Class> containerFragments, boolean withoutBackStack) {
        containersMap.put(containerResourceId, containerFragments);
        if (withoutBackStack) {
            containersWithoutBackStack.add(containerResourceId);
        }
    }
}
