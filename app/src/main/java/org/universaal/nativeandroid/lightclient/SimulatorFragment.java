package org.universaal.nativeandroid.lightclient;

import android.content.Intent;
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
    Button sendHearthRate;
    @BindView(R.id.send_pressure)
    Button sendPressure;
    @BindView(R.id.send_message)
    Button sendMessage;
    @BindView(R.id.hearth_rate_text)
    EditText hearthRateText;
    @BindView(R.id.pressure_text)
    EditText pressureText;
    @BindView(R.id.diastolic_text)
    EditText diastolicText;
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
        Intent intent = new Intent(Constants.ACTION_FALL_DETECTED);
        intent.putExtra("type", Constants.FALL_DETECTED);
        postMessage(intent);
    }

    @OnClick(R.id.send_temperature)
    public void onSendTemperatureClicked() {
        Intent intent = new Intent(Constants.ACTION_HEARTH_RATE);
        intent.putExtra("type", Constants.HEARTH_RATE);
        intent.putExtra("value", hearthRateText.getText());
        postMessage(intent);
        hearthRateText.setText("");
    }

    @OnClick(R.id.send_pressure)
    public void onSendPressureClicked() {
        Intent intent = new Intent(Constants.ACTION_BLOOD_PRESSURE);
        intent.putExtra("type", Constants.PRESSURE);
        intent.putExtra("value", pressureText.getText());
        intent.putExtra("value2", diastolicText.getText());
        postMessage(intent);
        pressureText.setText("");
        diastolicText.setText("");
    }

    @OnClick(R.id.send_message)
    public void onSendMessageClicked() {
        Intent intent = new Intent(Constants.ACTION_SEND_MESSAGE);
        intent.putExtra("type", Constants.MESSAGE);
        intent.putExtra("value", messageText.getText());
        postMessage(intent);
        messageText.setText("");
    }

    public void postMessage(Intent intent) {
        ((MainActivity) getActivity()).sendData(intent);

    }

}
