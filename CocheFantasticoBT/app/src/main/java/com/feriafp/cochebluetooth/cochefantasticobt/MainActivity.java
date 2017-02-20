package com.feriafp.cochebluetooth.cochefantasticobt;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;


public class MainActivity extends Activity {


    int giro = 0, velocidad = 0;
    final int IZQ_MIN = -155, IZQ_MAX = -255, DRCH_MIN = 155, DRCH_MAX = 255, RECTO = 205;
    final int AV_MIN = 155, AV_MAX = 255, RET_MIN = AV_MIN * -1, RET_MAX = AV_MAX * -1;
    final int APAGADO = 0, ROJO = 1, VERDE = 2, NARANJA = 3, AZUL = 4, MORADO = 5, BLANCO = 6, FIESTA = 7, EMERGENCIA = 8;
    int currentD = APAGADO, currentI = APAGADO, currentT = APAGADO;
    Button btnOn, btnOff;
    TextView txtArduino, txtString, txtStringLength, sensorView0, sensorView1, sensorView2, sensorView3;
    TextView txtSendorLDR;
    Handler bluetoothIn;
    Spinner spinnerDelantero, spinnerTrasero, spinnerInterior;
    SeekBar barraPotencia, barraGiro;
    Switch swichtDelantero, swichtTrasero, swichtInterior;
    ImageButton btn_emergency, btn_party;
    final int handlerState = 0;             //used to identify handler message
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private StringBuilder recDataString = new StringBuilder();

    private ConnectedThread mConnectedThread;

    // SPP UUID service - this should work for most devices
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // String for MAC address
    private static String address = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        spinnerDelantero = (Spinner) findViewById(R.id.spinner_delantero);
        spinnerTrasero = (Spinner) findViewById(R.id.spinner_trasero);
        spinnerInterior = (Spinner) findViewById(R.id.spinner_interior);
        barraPotencia = (SeekBar) findViewById(R.id.seekbar_potencia);
        barraGiro = (SeekBar) findViewById(R.id.seekbar_giro);
        swichtDelantero = (Switch) findViewById(R.id.switch_delanteras);
        swichtInterior = (Switch) findViewById(R.id.switch_interiores);
        swichtTrasero = (Switch) findViewById(R.id.switch_traseras);
        btn_emergency = (ImageButton) findViewById(R.id.btn_emergency);
        btn_party = (ImageButton) findViewById(R.id.btn_partyJard);

        //Poner los valores de las dos barras en el centro.


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.Spinner_color_1, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinnerDelantero.setAdapter(adapter);


        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this,
                R.array.Spinner_color_1, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinnerInterior.setAdapter(adapter3);


        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.Spinner_color_2, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinnerTrasero.setAdapter(adapter2);


        barraGiro.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                /*if (i >= 40 && i <= 60) {
                    giro = 0;
                } else {
                    if (i > 60) {
                        giro = i+155-40;
                    } else if (i < 40){
                        giro = (i *100 / 40) - 255;
                    }
                    enviarDatos();

                }*/
                if (i >= 40 && i <= 60) {
                    giro = 0;
                } else if (i > 60) {
                    giro = (i * 100 / 40);
                } else if (i < 40){
                    giro = (i *100 / 40) - 255;
                }
                enviarDatos();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        barraPotencia.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                /*if (i >= 40 && i <= 60) {
                    velocidad = 0;
                } else {
                    if (i > 60) {
                        velocidad = i+155-40;
                    } else if (i < 40){
                        velocidad = (i *100 / 40) - 255;
                    }
                    enviarDatos();

                }*/
                if (i >= 40 && i <= 60) {
                    velocidad = 0;
                } else if (i > 60) {
                    velocidad = (i * 100 / 40) ;
                } else if (i < 40){
                    velocidad = (i *100 / 40) - 255;
                }
                enviarDatos();
            }


            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        swichtDelantero.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {

                    spinnerDelantero.setVisibility(View.VISIBLE);
                    currentD = BLANCO;


                } else {
                    spinnerDelantero.setVisibility(View.GONE);
                    currentD = APAGADO;

                }
                enviarDatos();
            }
        });


        swichtTrasero.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {

                    swichtTrasero.setVisibility(View.VISIBLE);
                    currentT = ROJO;


                } else {
                    swichtTrasero.setVisibility(View.GONE);
                    currentT = ROJO;

                } enviarDatos();
            }


        });


        swichtInterior.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {

                    spinnerInterior.setVisibility(View.VISIBLE);
                    currentI = NARANJA;


                } else {
                    spinnerInterior.setVisibility(View.GONE);
                    currentI = APAGADO;

                }enviarDatos();
            }


        });

        spinnerDelantero.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case ROJO:
                        currentD = ROJO;
                        break;
                    case VERDE:
                        currentD = VERDE;
                        break;
                    case NARANJA:
                        currentD = NARANJA;
                        break;

                    case AZUL:
                        currentD = AZUL;
                        break;
                    case MORADO:
                        currentD = MORADO;
                        break;

                    case BLANCO:
                        currentD = BLANCO;
                        break;

                }
                enviarDatos();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        spinnerInterior.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case ROJO:
                        currentI = ROJO;
                        break;
                    case VERDE:
                        currentI = VERDE;
                        break;
                    case NARANJA:
                        currentI = NARANJA;
                        break;

                    case AZUL:
                        currentI = AZUL;
                        break;
                    case MORADO:
                        currentI = MORADO;
                        break;

                    case BLANCO:
                        currentI = BLANCO;
                        break;

                }
                enviarDatos();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerTrasero.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case ROJO:
                        currentT = ROJO;
                        break;
                    case VERDE:
                        currentT = VERDE;
                        break;
                    case BLANCO:
                        currentT = BLANCO;
                        break;

                }
                enviarDatos();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btn_emergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentD = EMERGENCIA;
                currentI = EMERGENCIA;
                currentT = EMERGENCIA;
                enviarDatos();
            }

        });
        btn_party.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentD = FIESTA;
                currentI = FIESTA;
                currentT = FIESTA;
                enviarDatos();
            }

        });


        //Link the buttons and textViews to respective views
        //btnOn = (Button) findViewById(R.id.buttonOn);
        //btnOff = (Button) findViewById(R.id.buttonOff);
        //txtString = (TextView) findViewById(R.id.txtString);
        //txtStringLength = (TextView) findViewById(R.id.testView1);
        //sensorView0 = (TextView) findViewById(R.id.sensorView0);
        //sensorView1 = (TextView) findViewById(R.id.sensorView1);
        //sensorView2 = (TextView) findViewById(R.id.sensorView2);
        //sensorView3 = (TextView) findViewById(R.id.sensorView3);

        //txtSendorLDR = (TextView) findViewById(R.id.tv_sendorldr);


              /*  if (strength == 0){
                    mConnectedThread.write(AV_MIN+","+giro+";");    // Send "0" via Bluetooth
                    //Toast.makeText(getBaseContext(), "Quietor!", Toast.LENGTH_SHORT).show();
                }else if(0 <= angle && angle <= 90){
                    giro = (angle * RECTO)/90 ;
                    velocidad = (strength * AV_MAX)/100;
                }else if(90 < angle && angle <= 180){
                    giro = (angle * RECTO)/180;
                    velocidad = (strength * AV_MAX)/100;
                }else if( 180< angle && angle <= 270){
                    giro = (angle * RECTO)/270;
                    velocidad = (strength * RET_MAX)/100;
                }else if(270 < angle && angle <= 360){
                    giro = (angle * RECTO)/360;
                    velocidad = (strength * RET_MAX)/100;
                }

                if (strength != 0){
                          mConnectedThread.write(velocidad+","+giro+";");    // Send "0" via Bluetooth
                    //Toast.makeText(getBaseContext(), "Moviendo"+velocidad+";"+giro, Toast.LENGTH_SHORT).show();
                }
                Log.i("velocidad: ", velocidad+"");
                Log.i("giro: ", giro+"");
            }
        },1);*/

        bluetoothIn = new Handler() {
            public void handleMessage(android.os.Message msg) {
                if (msg.what == handlerState) {          //if message is what we want
                    String readMessage = (String) msg.obj;                                                                // msg.arg1 = bytes from connect thread
                    recDataString.append(readMessage);              //keep appending to string until ~
                    int endOfLineIndex = recDataString.indexOf("~");                    // determine the end-of-line
                    if (endOfLineIndex > 0) {                                           // make sure there data before ~
                        String dataInPrint = recDataString.substring(0, endOfLineIndex);    // extract string
                        //txtString.setText("Datos recibidos = " + dataInPrint);
                        int dataLength = dataInPrint.length();       //get length of data received
                        //txtStringLength.setText("Tamaño del String = " + String.valueOf(dataLength));

                        if (recDataString.charAt(0) == '#')        //if it starts with # we know it is what we are looking for
                        {
                            String sensor0 = recDataString.substring(1, 5);             //get sensor value from string between indices 1-5
                            String sensor1 = recDataString.substring(6, 10);            //same again...
                            String sensor2 = recDataString.substring(11, 15);
                            String sensor3 = recDataString.substring(16, 20);

                            if (sensor0.equals("1.00"))
                                sensorView0.setText("Encendido"); //update the textviews with sensor values
                            else
                                sensorView0.setText("Apagado"); //update the textviews with sensor values
                            sensorView1.setText(sensor1);
                            sensorView2.setText(sensor2);
                            sensorView3.setText(sensor3);
                            //sensorView3.setText(" Sensor 3 Voltage = " + sensor3 + "V");
                        }
                        recDataString.delete(0, recDataString.length());      //clear all string data
                        // strIncom =" ";
                        dataInPrint = " ";
                    }
                }
            }
        };

        btAdapter = BluetoothAdapter.getDefaultAdapter();       // get Bluetooth adapter
        checkBTState();


        // Set up onClick listeners for buttons to send 1 or 0 to turn on/off LED
        /*btnOff.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                mConnectedThread.write("2");    // Send "0" via Bluetooth
                Toast.makeText(getBaseContext(), "Apagar el LED", Toast.LENGTH_SHORT).show();
            }
        });

        btnOn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                mConnectedThread.write("1");    // Send "1" via Bluetooth
                Toast.makeText(getBaseContext(), "Encender el LED", Toast.LENGTH_SHORT).show();
            }
        });*/
    }


    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {

        return device.createRfcommSocketToServiceRecord(BTMODULEUUID);
        //creates secure outgoing connecetion with BT device using UUID
    }

    @Override
    public void onResume() {
        super.onResume();

        //Get MAC address from DeviceListActivity via intent
        Intent intent = getIntent();

        //Get the MAC address from the DeviceListActivty via EXTRA
        address = intent.getStringExtra(DeviceListActivity.EXTRA_DEVICE_ADDRESS);

        //create device and set the MAC address
        //Log.i("ramiro", "adress : " + address);
        BluetoothDevice device = btAdapter.getRemoteDevice(address);

        try {
            btSocket = createBluetoothSocket(device);
        } catch (IOException e) {
            Toast.makeText(getBaseContext(), "La creacción del Socket fallo", Toast.LENGTH_LONG).show();
        }
        // Establish the Bluetooth socket connection.
        try {
            btSocket.connect();
        } catch (IOException e) {
            try {
                btSocket.close();
            } catch (IOException e2) {
                //insert code to deal with this
            }
        }
        mConnectedThread = new ConnectedThread(btSocket);
        mConnectedThread.start();

        //I send a character when resuming.beginning transmission to check device is connected
        //If it is not an exception will be thrown in the write method and finish() will be called
        mConnectedThread.write("x");
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            //Don't leave Bluetooth sockets open when leaving activity
            btSocket.close();
        } catch (IOException e2) {
            //insert code to deal with this
        }
    }

    //Checks that the Android device Bluetooth is available and prompts to be turned on if off
    private void checkBTState() {

        if (btAdapter == null) {
            Toast.makeText(getBaseContext(), "El dispositivo no soporta bluetooth", Toast.LENGTH_LONG).show();
        } else {
            if (btAdapter.isEnabled()) {
            } else {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
        }
    }

    //create new class for connect thread
    private class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        //creation of the connect thread
        public ConnectedThread(BluetoothSocket socket) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                //Create I/O streams for connection
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }


        public void run() {
            byte[] buffer = new byte[256];
            int bytes;

            // Keep looping to listen for received messages
            while (true) {
                try {
                    bytes = mmInStream.read(buffer);         //read bytes from input buffer
                    String readMessage = new String(buffer, 0, bytes);
                    // Send the obtained bytes to the UI Activity via handler
                    bluetoothIn.obtainMessage(handlerState, bytes, -1, readMessage).sendToTarget();
                } catch (IOException e) {
                    break;
                }
            }
        }

        //write method
        public void write(String input) {
            byte[] msgBuffer = input.getBytes();           //converts entered String into bytes
            try {
                mmOutStream.write(msgBuffer);                //write bytes over BT connection via outstream
            } catch (IOException e) {
                //if you cannot write, close the application
                Toast.makeText(getBaseContext(), "La Conexión fallo", Toast.LENGTH_SHORT).show();
                finish();

            }
        }
    }

    //método para enviar datos
    public void enviarDatos() {
        Log.i("Datos para enviar","*"+velocidad + "," + giro + ";A" + currentD + "B" + currentI + "C" + currentT + "#" );
        mConnectedThread.write("*"+velocidad + "," + giro + ";A" + currentD + "B" + currentI + "C" + currentT + "#");


    }

}
