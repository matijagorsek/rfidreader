package com.marrowlabs.rfid.commons;

import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;


public class BluetoothService extends Service {

    private static final String UUID_SERIAL_PORT_PROFILE = "00001101-0000-1000-8000-00805F9B34FB";
    private static final int NOTIFICATION_ID = 1;
    private static final int INTERVAL = 2 * 60 * 1000;

    private final IBinder binder = new LocalBinder();
    private final boolean mAllowInsecureConnections = true;

    private ConnectThread connectThread;
    private ConnectedThread connectedThread;

    private BluetoothDevice bluetoothDevice;
    private BluetoothSocket bluetoothSocket;
    private String line;

    private boolean isFirst = true;

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null)
            bluetoothDevice = intent.getParcelableExtra("device");
        else
            stopSelf();
        if (bluetoothDevice != null) {
            connectThread = new ConnectThread(bluetoothDevice);
            connectThread.start();
        }
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (connectedThread != null)
            connectedThread.cancel();
        isFirst = true;
    }

    public class LocalBinder extends Binder {
        public BluetoothService getService() {
            return BluetoothService.this;
        }
    }

    public void connected(BluetoothSocket socket, BluetoothDevice device) {
        // Cancel the thread that completed the connection
        if (connectThread != null) {
            connectThread.cancel();
            connectThread = null;
        }
        // Cancel any thread currently running a connection
        if (connectedThread != null) {
            connectedThread.cancel();
            connectedThread = null;
        }
        // Start the thread to manage the connection and perform transmissions
        connectedThread = new ConnectedThread(socket);
        connectedThread.start();
        // Send the name of the connected device back to the UI Activity
//        Message msg = mHandler.obtainMessage(BlueTerm.MESSAGE_DEVICE_NAME);
//        Bundle bundle = new Bundle();
//        bundle.putString(BlueTerm.DEVICE_NAME, device.getName());
//        msg.setData(bundle);
//        mHandler.sendMessage(msg);
    }

    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            // Use a temporary object that is later assigned to mmSocket,
            // because mmSocket is final
            BluetoothSocket tmp = null;
            mmDevice = device;
            // Get a BluetoothSocket to connect with the given BluetoothDevice
            try {
                // MY_UUID is the app's UUID string, also used by the server code
                if (mAllowInsecureConnections) {
                    Method method;

                    method = device.getClass().getMethod("createRfcommSocket", new Class[]{int.class});
                    tmp = (BluetoothSocket) method.invoke(mmDevice, 1);
                } else {
                    UUID uuid = UUID.fromString(UUID_SERIAL_PORT_PROFILE);
                    tmp = device.createRfcommSocketToServiceRecord(uuid);
                }
            } catch (IOException e) {
                stopSelf();
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                stopSelf();
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                stopSelf();
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                stopSelf();
                e.printStackTrace();
            }
            mmSocket = tmp;
        }

        public void run() {
            try {
                // Connect the device through the socket. This will block
                // until it succeeds or throws an exception
                mmSocket.connect();
            } catch (IOException connectException) {
                // Unable to connect; close the socket and get out
                stopSelf();
                try {
                    mmSocket.close();
                } catch (IOException closeException) {
                }
                return;
            }
            // Do work to manage the connection (in a separate thread)
            connectThread = null;
            connected(mmSocket, mmDevice);
        }

        /**
         * Will cancel an in-progress connection, and close the socket
         */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
            }
        }
    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;


        public ConnectedThread(BluetoothSocket socket) {
            Log.d("blutut", "create ConnectedThread");
            mmSocket = socket;

            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the BluetoothSocket input and output streams
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Log.e("blutut", "temp sockets not created", e);
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            try {
                while (true) {
                    Log.d("SERVICE1", "write START");
                    mmOutStream.write(1);

//                    ReadBufferFromBluetooth readBuffer = new ReadBufferFromBluetooth();
//                    readBuffer.execute(mmInStream);
//                    new ReadBufferFromBluetooth().execute(mmInStream);

                    Log.d("SERVICE1", "write STOP");
                    BufferedReader r = new BufferedReader(new InputStreamReader(mmInStream));
                    StringBuilder total = new StringBuilder();
//
                    while ((line = r.readLine()) != null) {
                        Log.d("SERVICE1", "append");
                        total.append(line);
                        mHandler.obtainMessage(1, line).sendToTarget();     //ovdje dobijes podatke s bluetooth porta
                        Log.d("SERVICE1", "prije break");
                        Log.d("SERVICE1", line);
                        break;
                    }
                }

            } catch (IOException e) {
                Log.d("SERVICE1", "Exception");
                e.printStackTrace();
                stopSelf();
            }
        }

        public void write(byte[] buffer) {
            try {
                mmOutStream.write(buffer);
            } catch (IOException e) {
                Log.e("blutut", "Exception during write", e);
            }
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e("blutut", "close() of connect socket failed", e);
            }
        }
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String line = (String) msg.obj;
            switch (msg.what) {
                case 1:
                    Log.d("SERVICE", line);
                    Log.d("SERVICE", "READ STOP");
                    Toast.makeText(getApplicationContext(), line, Toast.LENGTH_LONG).show();
            }
        }
    };


    private void connectionFailed() {
//        // Send a failure message back to the Activity
//        Message msg = mHandler.obtainMessage(BlueTerm.MESSAGE_TOAST);
//        Bundle bundle = new Bundle();
//        bundle.putString(BlueTerm.TOAST, mContext.getString(R.string.toast_unable_to_connect) );
//        msg.setData(bundle);
//        mHandler.sendMessage(msg);
    }

    /**
     * Indicate that the connection was lost and notify the UI Activity.
     */
    private void connectionLost() {
//        // Send a failure message back to the Activity
//        Message msg = mHandler.obtainMessage(BlueTerm.MESSAGE_TOAST);
//        Bundle bundle = new Bundle();
//        bundle.putString(BlueTerm.TOAST, mContext.getString(R.string.toast_connection_lost) );
//        msg.setData(bundle);
//        mHandler.sendMessage(msg);
    }

//    private class ReadBufferFromBluetooth extends AsyncTask<InputStream, Integer, String> {
//        String lineFromBuffer = null;
//
//        @Override
//        protected String doInBackground(InputStream... params) {
//
//
//            InputStream inputStream = params[0];
//
//            BufferedReader r;
//            r = new BufferedReader(new InputStreamReader(inputStream));
//
//            try {
//                lineFromBuffer = r.readLine();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return lineFromBuffer;
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//            line = s;
//        }
//    }
}
