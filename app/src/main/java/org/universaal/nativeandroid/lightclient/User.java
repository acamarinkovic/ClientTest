package org.universaal.nativeandroid.lightclient;

import com.pixplicity.easyprefs.library.Prefs;

/**
 * Created by Aleksandar Marinković on 16-Oct-17.
 */

public class User {
    String avatar;
    String name;
    String type;
    String data;
    String userId;
    String phone;
    String address;
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public User(String name, String type, String data) {
        this.name = name;
        this.type = type;
        this.data = data;
        userId = DeviceIdGenerator.readDeviceId(Application.getInstance());
    }

    public User(String type, String data) {
        this.type = type;
        this.data = data;
        userId = DeviceIdGenerator.readDeviceId(Application.getInstance());
    }

    public User(String avatar, String name, String type, String data) {
        this.avatar = avatar;
        this.name = name;
        this.type = type;
        this.data = data;
        phone= Prefs.getString(Constants.PHONE,"");
        address= Prefs.getString(Constants.ADDRESS,"");
        userId = DeviceIdGenerator.readDeviceId(Application.getInstance());
    }


    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
