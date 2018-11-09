package com.marrowlabs.rfid.home;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.marrowlabs.rfid.base.BaseFragment;
import com.marrowlabs.rfid.commons.UsbService;
import com.marrowlabs.rfid.commons.core.PreferenceHelper;
import com.marrowlabs.rfid.commons.core.Utils;
import com.marrowlabs.rfid.commons.model.AuthAdminResponse;
import com.marrowlabs.rfid.commons.model.CheckInOutResponse;
import com.marrowlabs.rfid.commons.model.UserModel;
import com.marrowlabs.rfid.commons.service.ApiService;
import com.marrowlabs.rfid.commons.service.RfidService;
import com.marrowlabs.rfid.home.absence_reasons.fragment.AbsenceReasonsFragment;
import com.marrowlabs.rfid.home.absence_reasons.model.AbsenceModel;
import com.marrowlabs.rfid.rfidandroid.R;
import com.squareup.otto.Subscribe;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.aflak.bluetooth.Bluetooth;
import retrofit.RetrofitError;

/**
 * Created by Matija on 06-Sep-17.
 */

public class HomeFragment extends BaseFragment {

    public static final String TAG = HomeFragment.class.getName();

    @Bind(R.id.time_number)
    TextView checkedTime;
    @Bind(R.id.date_number)
    TextView dateNumber;
    @Bind(R.id.status_success)
    TextView userStatus;
    @Bind(R.id.add_card_button)
    Button addCardButton;
    @Bind(R.id.choose_absence_reason)
    Button chooseAbsenceReason;
    @Bind(R.id.rfid)
    TextView rfidText;
    @Bind(R.id.data_layout)
    RelativeLayout dataLayout;
    @Bind(R.id.view_add_user)
    RelativeLayout viewAddUser;
    @Bind(R.id.name_input)
    EditText inputName;
    @Bind(R.id.surname_input)
    EditText surnameInput;
    @Bind(R.id.card_input)
    EditText cardInput;
    @Bind(R.id.confirm_button)
    Button confirmButton;
    @Bind(R.id.check_admin_layout)
    RelativeLayout checkAdminLayout;
    @Bind(R.id.status_admin)
    TextView statusAdmin;

    final int minimalTime = 5; // define minimal time between reading same RFID
    private String id, sub;
    private UsbService usbService;
    private ApiService apiService;
    private ArrayList<AbsenceModel> absenceModels;
    private ToneGenerator toneGenerator;
    private static boolean canRun = true;
    private static CountDownTimer mCountDownTimer;
    private String deviceName = "HC-06";
    private Bluetooth bt;
    private BluetoothAdapter bluetoothAdapter;
    private final String DEVICE_ADDRESS = "00:21:13:00:E8:12";
    private final UUID PORT_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");//Serial Port Service ID
    private BluetoothDevice device;
    private BluetoothSocket socket;
    private OutputStream outputStream;
    private BufferedInputStream inputBufferedStream;
    private InputStream inputStream;
    private String rfidString, rfidCode;
    Date lastTime;
    boolean deviceConnected = false;
    Thread thread;
    byte buffer[] = new byte[0];
    private long lastCheckedTime = 0;
    private boolean addNewUserView = false;
    private boolean checkAdmin = false;
    int bufferPosition;
    boolean stopThread, timerRunning;
    String croppedString, lastRfid = "";
    private String name, surname, cardNumber;

    private String completeString = "";
    private static final int COMPLETE_STRING_LENGTH = 12;

    public static HomeFragment newInstance() {
        HomeFragment amenitiesFragment = new HomeFragment();
        return amenitiesFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewAddUser.setVisibility(View.GONE);
        checkAdminLayout.setVisibility(View.GONE);
        getAbsenceReasons();
        chooseAbsenceReason.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (btInit()) {

            while (!btConnect()) {
            }
            deviceConnected = true;
            beginListenForData();

        } else {
            System.out.println("Bluetooth device not found!");
        }

        final Handler h = new Handler();
        h.post(new Runnable() {
            @Override
            public void run() {
                updateTime();
                h.postDelayed(this, 1000);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        deviceConnected = false;
    }

    @Override
    public void onStop() {
        super.onStop();
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        deviceConnected = false;
    }

    public void updateTime() {
        Date d = new Date();
        CharSequence date = Utils.simpleDateFormatFormatted.format(d);
        CharSequence time = DateFormat.format("HH:mm:ss", d.getTime());
        dateNumber.setText(date);
        checkedTime.setText(time);
    }

    public boolean btInit() {
        boolean found = false;
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(getActivity(), "Device doesnt Support Bluetooth", Toast.LENGTH_SHORT).show();
        }
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableAdapter = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableAdapter, 0);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();
        if (bondedDevices.isEmpty()) {
            Toast.makeText(getActivity(), "Please Pair the Device first", Toast.LENGTH_SHORT).show();
        } else {
            for (BluetoothDevice iterator : bondedDevices) {
                if (iterator.getAddress().equals(DEVICE_ADDRESS)) {
                    device = iterator;
                    found = true;
                    break;
                }
            }
        }
        return found;
    }

    public boolean btConnect() {
        try {
            socket = device.createRfcommSocketToServiceRecord(PORT_UUID);
            socket.connect();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Socket connection failed");
            //btConnect();
        }
        if (socket.isConnected()) {
            try {
                outputStream = socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Output stream not connected");
            }
            try {
                inputStream = socket.getInputStream();

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Input stream not connected");
            }
        }
        return socket.isConnected();
    }


    public void beginListenForData() {
        final Handler handler = new Handler();
        stopThread = false;
        timerRunning = false;
        thread = new Thread(new Runnable() {
            public void run() {
                while (!Thread.currentThread().isInterrupted() && !stopThread) {
                    rfidString = "";
                    final String rfid = readBuffer();
                    if (rfid.length() == COMPLETE_STRING_LENGTH) {
                        System.out.println(rfid);
                        if (!lastRfid.equals(rfid)) {
                            handler.post(new Runnable() {
                                public void run() {
                                    lastRfid = rfid;
                                    System.out.println(rfid);
                                    if (!addNewUserView && !checkAdmin) {
                                        RfidService.checkInOut(rfid);
                                    } else if (addNewUserView == true) {
                                        cardNumber = lastRfid;
                                        cardInput.setText(cardNumber);
                                    } else if (checkAdmin == true) {
                                        RfidService.authAdmin(rfid);
                                    }
                                }
                            });
                        }
                    }
                }
            }

        });
        thread.start();
    }

    private String readBuffer() {
        try {
            int i;
            while ((i = inputStream.read()) != -1) {
                char c = (char) i;
                if (i == 2) rfidString = "";
                else rfidString = rfidString + c;
                if (rfidString.length() == COMPLETE_STRING_LENGTH) return rfidString;
            }
        } catch (IOException e) {
        }
        return "";
    }

    public static String slurp(final InputStream is, final int bufferSize) {
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        try (Reader in = new InputStreamReader(is, "UTF-8")) {
            for (; ; ) {
                int rsz = in.read(buffer, 0, buffer.length);
                if (rsz < 0)
                    break;
                out.append(buffer, 0, rsz);
            }
        } catch (UnsupportedEncodingException ex) {
            /* ... */
        } catch (IOException ex) {
            /* ... */
        }
        return out.toString();
    }

    public String test(String string) {
        char[] charArray = string.toCharArray();
        String subString = "";
        for (int i = 0; i < charArray.length; i++) {
            if (Character.isLetterOrDigit(charArray[i])) {
                subString = subString + String.valueOf(charArray[i]);
            }
        }
        return subString.length() > 12 ? subString.substring(0, 12) : subString;
    }


    public void getAbsenceReasons() {
        RfidService.getAbsenceReasons();
    }

    @OnClick(R.id.add_card_button)
    public void addCardButton() {
        checkAdminLayout.setVisibility(View.VISIBLE);
        dataLayout.setVisibility(View.GONE);
        checkAdmin = true;
    }


    public void resetRfid() {
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                lastRfid = "";
            }
        }, 0, 5000);
    }

    public void resetRfidAfter() {
        userStatus.postDelayed(new Runnable() {
            public void run() {
                userStatus.setText("");
            }
        }, 10000);
    }


    @Subscribe
    public void onAuthAdminSuccess(AuthAdminResponse authAdminResponse) {
        Log.d("Test", authAdminResponse.toString());
        if (authAdminResponse.getRoleModel().getTitle().equals("admin")) {
            PreferenceHelper.setUserToken(getActivity(), authAdminResponse.getAccessToken());
            addNewUserView = true;
            checkAdmin = false;
            statusAdmin.setText("DA");
            userStatus.postDelayed(new Runnable() {
                public void run() {
                    checkAdminLayout.setVisibility(View.GONE);
                    viewAddUser.setVisibility(View.VISIBLE);
                }
            }, 2000);

        }
    }

    @Subscribe
    public void onAuthAdminFailure(RetrofitError error) {
        Toast.makeText(getActivity(), getString(R.string.auth_admin_failure), Toast.LENGTH_LONG).show();
    }

    @Subscribe
    public void onCheckInOutFailure(RetrofitError error) {
        //Log.d("Test", error.toString());
    }

    @Subscribe
    public void onCheckInOutSuccess(CheckInOutResponse checkInOutResponse) {
        toneGenerator = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 500);
        resetRfid();
        if (checkInOutResponse.getStatus() == 0) {
            toneGenerator.startTone(ToneGenerator.TONE_PROP_BEEP);
            id = String.valueOf(checkInOutResponse.getId());
            userStatus.setText(getString(R.string.status_failure));
            rfidText.setText("");
            AbsenceReasonsFragment absenceReasonsFragment = AbsenceReasonsFragment.newInstance(id, absenceModels);
            FragmentManager fmAbsenceReasonFragment = getFragmentManager();
            FragmentTransaction ftAbsenceReasonFragment = fmAbsenceReasonFragment.beginTransaction();
            ftAbsenceReasonFragment.add(R.id.fragment_container, absenceReasonsFragment, AbsenceReasonsFragment.TAG);
            ftAbsenceReasonFragment.addToBackStack(HomeFragment.TAG);
            ftAbsenceReasonFragment.commit();
            resetRfidAfter();
        } else {
            rfidCode = checkInOutResponse.getUserModel().getCardModel().getRfId();
            toneGenerator.startTone(ToneGenerator.TONE_PROP_BEEP);
            id = String.valueOf(checkInOutResponse.getId());
            userStatus.setText(getString(R.string.status_success));
            resetStatus();
        }
    }

    public void resetStatus() {
        userStatus.postDelayed(new Runnable() {
            public void run() {
                userStatus.setText("");
            }
        }, 5000);
    }

    @Subscribe
    public void onGetAbsenceReasons(ArrayList<AbsenceModel> absenceResponse) {
        absenceModels = absenceResponse;
    }


    @OnClick(R.id.confirm_button)
    public void onConfirmButtonClicked() {
        boolean errorFound = false;
        name = inputName.getText().toString();
        surname = surnameInput.getText().toString();
        if (TextUtils.isEmpty(name)) {
            inputName.setError(getString(R.string.required));
            errorFound = true;
        }
        if (TextUtils.isEmpty(surname)) {
            inputName.setError(getString(R.string.required));
            errorFound = true;
        }
        if (TextUtils.isEmpty(cardNumber)) {
            cardInput.setError(getString(R.string.required));
            errorFound = true;
        }
        if (errorFound) {
            return;
        } else {
            RfidService.createUser(cardNumber, name, surname);
        }
    }

    @Subscribe
    public void onCreateUserSuccess(UserModel userModel) {
        Log.d("Test", userModel.toString());
        Toast.makeText(getActivity(), getString(R.string.user_succesfully_added), Toast.LENGTH_LONG).show();
        addNewUserView = false;
        checkAdmin = false;
        cardInput.setText("");
        lastRfid = "";
        dataLayout.setVisibility(View.VISIBLE);
        viewAddUser.setVisibility(View.GONE);
    }


}
