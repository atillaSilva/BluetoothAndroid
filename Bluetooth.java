package com.example.atilla.peixealimentador;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;


/**
 * Created by Atilla on 01-Aug-17.
 */

public class Bluetooth extends Activity{
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothDevice mmDevice;
    private BluetoothSocket mmSocket;
    private OutputStream outStream;
    private InputStream inputStream;
    private final int REQUEST_ENABLE_BT = 1;
    private String nomeDispositivo;

    private Context context;
    public Bluetooth(Context context, String nomeDispositivo){
        this.context = (Context) context;
        this.nomeDispositivo  = nomeDispositivo;
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter == null){
            Toast.makeText(context, "Seu dispositivo não possui suporte a bluetooth", Toast.LENGTH_LONG);
        }
        if(bluetoothAdapter.isEnabled()){
            Intent bluetoothEnableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);

        }
        CheckBlueToothState();
    }

    private void CheckBlueToothState() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Seu dispositivo não possui suporte a bluetooth", Toast.LENGTH_LONG);
        }
        if (!bluetoothAdapter.isEnabled()) {
            Intent bluetoothEnableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            ((Activity)context).startActivityForResult(bluetoothEnableIntent, REQUEST_ENABLE_BT);
        }
            findBT();
            try {
                openBT();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    void findBT() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
        }

        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetooth, 0);
        }
        allPairDevices();
    }

    public void allPairDevices(){
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                if (device.getName().equals(nomeDispositivo)) {
                    mmDevice = device;
                    break;
                }
            }
        }
    }

    private void writeData(String data) {
        try {
            outStream = mmSocket.getOutputStream();
        } catch (IOException e) {
            Log.d("", "Bug BEFORE Sending stuff", e);
        }

        String message = data;
        byte[] msgBuffer = message.getBytes();

        try {
            outStream.write(msgBuffer);
        } catch (IOException e) {
            Log.d("", "Bug while sending stuff", e);
        }
    }

    void openBT() throws IOException {
        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"); //Standard //SerialPortService ID

        mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
        mmSocket.connect();
        Log.d("Status Conexao", "Bluetooth Conectado!!");
    }

}
