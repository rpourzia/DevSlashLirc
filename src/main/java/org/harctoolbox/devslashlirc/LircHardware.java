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

import java.io.Closeable;

/**
 * An abstract base class for Lirc drivers and -devices.
 */
public abstract class LircHardware implements Closeable {
    static {
        System.loadLibrary("devslashlirc");
    }

    protected long nativePointer;
    protected String deviceName;

    private LircHardware() {
        this.nativePointer = 0;
    }

    protected LircHardware(String deviceName, long nativePointer) {
        this.nativePointer = nativePointer;
        this.deviceName = deviceName;
    }

    /**
     * Opens the device using the device name used in the constructor.
     * @throws LircDeviceException Failure to open.
     */
    public void open() throws LircDeviceException {
        boolean status = openNative();
        if (!status)
            throw new LircDeviceException("open failed");
    }

    public final void setTransmitterMask(int mask) throws NotSupportedException, NonExistentTransmitterException, LircDeviceException {
        if (!canSetTransmitterMask())
            throw new NotSupportedException("Setting transmitter mask not supported");
        int noTransmitters = getNumberTransmitters();

        // If noTransmitters reports as 0, probably this is just wrong;
        // ignore this "information".
        if (noTransmitters > 0 && (mask >= (1 << noTransmitters)))
            throw new NonExistentTransmitterException("No such transmitter");
        boolean result = setTransmitterMaskNative(mask);
        if (!result)
            throw new LircDeviceException("Error setting transmitters");
    }

    public abstract int getNumberTransmitters();

    protected abstract boolean setTransmitterMaskNative(int mask);

    protected abstract boolean openNative();

    /**
     * Returns the version string of the underlying C++ code, not the version of the hardware or the
     * device driver.
     * @return String
     */
    public abstract String getVersion();

    public abstract boolean canSend();

    public abstract boolean canSetTransmitterMask();

    public abstract boolean canRec();

    /**
     * Returns true if the device is opened and working.
     * @return boolean
     */
    public abstract boolean isValid();

    @Override
    public abstract String toString();
}
