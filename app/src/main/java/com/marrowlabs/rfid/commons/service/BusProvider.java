package com.marrowlabs.rfid.commons.service;

import com.squareup.otto.Bus;

/**
 * Created by Matija on 11-Sep-17.
 */

public class BusProvider {

    private static Bus bus = new Bus();

    public static Bus getInstance() {
        if (bus == null) {
            bus = new Bus();
        }
        return bus;
    }
}
