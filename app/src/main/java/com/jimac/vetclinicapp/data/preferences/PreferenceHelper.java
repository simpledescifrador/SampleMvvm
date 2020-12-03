package com.jimac.vetclinicapp.data.preferences;

import com.jimac.vetclinicapp.data.models.Client;

public abstract class PreferenceHelper {

    public abstract Client getClientData();

    public abstract String getLoginDateTime();

    public abstract void saveClientData(Client client);

    public abstract void saveLoginDateTime(String loginDateTime);
}
