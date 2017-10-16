package org.universaal.nativeandroid.lightclient;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.pixplicity.easyprefs.library.Prefs;

import org.universaal.nativeandroid.lightclient.organizer.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SimulatorFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.file_dtected)
    Button fileDtected;
    @BindView(R.id.send_temperature)
    Button sendTemperature;
    @BindView(R.id.send_pressure)
    Button sendPressure;
    @BindView(R.id.send_message)
    Button sendMessage;
    @BindView(R.id.tem_text)
    EditText temText;
    @BindView(R.id.pressure_text)
    EditText pressureText;
    @BindView(R.id.message_text)
    EditText messageText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.simulator_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.file_dtected)
    public void onFileDtectedClicked() {
        postMessage(Constants.FALL_DETECTED,"");
    }

    @OnClick(R.id.send_temperature)
    public void onSendTemperatureClicked() {
        postMessage(Constants.TEMPERATURE,temText.getText().toString());
    }

    @OnClick(R.id.send_pressure)
    public void onSendPressureClicked() {
        postMessage(Constants.PRESSURE,pressureText.getText().toString());
    }

    @OnClick(R.id.send_message)
    public void onSendMessageClicked() {
        postMessage(Constants.MESSAGE,messageText.getText().toString());
    }

    public void postMessage(String type, String message) {
        User user = new User(type, message);
        Gson gson = new Gson();
        String json = gson.toJson(user, User.class);
        ((MainActivity) getActivity()).sendData(json);

    }

}
