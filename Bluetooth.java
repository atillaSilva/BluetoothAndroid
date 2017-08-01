package com.example.atilla.peixealimentador;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;


/**
 * Created by Atilla on 01-Aug-17.
 */

public class Bluetooth{
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothDevice mmDevice;
    private BluetoothSocket mmSocket;
    private OutputStream outStream;
    private InputStream inputStream;
    private final int REQUEST_ENABLE_BT = 1;

    /**
     */
    public Bluetooth(){
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    /**
     * Verifica se o dispositivo possui bluetooth e também se ele está ativado, caso não esteja
     * ativado ele pede permissão ao usuário para ativar
     */

    public int checkBlueToothState() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            return 1;
        }
        if (!bluetoothAdapter.isEnabled())
            return 2;
        else
            return 0;
    }

    /**
     * Encontra os dispositivos PAREADOS com o seu dispositivo que possue o nome igual ao passado
     * no construtor
     */

    public void pairDevice(String deviceName) throws IOException {
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                if (device.getName().equals(deviceName)) {
                    mmDevice = device;
                    break;
                }
            }
        }
        openBT();
    }

    public void writeData(String data) {
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

    private void openBT() throws IOException {
        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"); //Standard //SerialPortService ID
        mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
        mmSocket.connect();
        Log.d("Status Conexao", "Bluetooth Conectado!!");
    }

}
