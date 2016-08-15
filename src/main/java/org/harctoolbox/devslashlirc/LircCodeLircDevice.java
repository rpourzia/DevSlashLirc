/*
Copyright (C) 2016 Bengt Martensson.

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 3 of the License, or (at
your option) any later version.

This program is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
General Public License for more details.

You should have received a copy of the GNU General Public License along with
this program. If not, see http://www.gnu.org/licenses/.
*/

package org.harctoolbox.devslashlirc;

/**
 * This class models a "LircCode" Lirc hardware on /dev/lirc*.
 */
public class LircCodeLircDevice extends LircDevice implements ILircCode {

    private native static long newLircCodeLircDevice(String deviceName);

    public LircCodeLircDevice(String deviceName, int beginTimeout) {
        super(deviceName, beginTimeout, newLircCodeLircDevice(deviceName));
    }

    public LircCodeLircDevice() {
        this(defaultDeviceName, defaultBeginTimeout);
    }

    @Override
    public native void send(int code);

    @Override
    public native int read();

    @Override
    protected native boolean openNative();
}

