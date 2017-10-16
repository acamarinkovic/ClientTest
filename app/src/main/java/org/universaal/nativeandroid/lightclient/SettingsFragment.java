package org.universaal.nativeandroid.lightclient;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.gson.Gson;
import com.pixplicity.easyprefs.library.Prefs;

import org.universaal.nativeandroid.lightclient.organizer.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SettingsFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.address_text)
    EditText addressText;
    @BindView(R.id.phone_text)
    EditText phoneText;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.settings_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    public void postMessage(String type, String message) {
        User user = new User(type, message);
        Gson gson = new Gson();
        String json = gson.toJson(user, User.class);
        ((MainActivity) getActivity()).sendData(json);

    }

    @OnClick(R.id.save)
    public void onViewClicked() {
        Prefs.putString(Constants.ADDRESS, addressText.getText().toString());
        Prefs.putString(Constants.PHONE, phoneText.getText().toString());
    }
}
