package me.ashram.packagegen.controller.communicator;

import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;
import me.ashram.packagegen.model.BearingFinderData;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Created by ashram on 20.12.2015.
 */
public class SerialCommunicator {
    private List<String> availablePortNames = Arrays.asList(SerialPortList.getPortNames());;
    private boolean connectionEstablished = false;
    private boolean transmissionOnLine = false;
    private SerialPort port;
    private BearingFinderData dataSource;
    private TransmissionRunnable currentTaskRunnable;
    private Thread currentTransmissionThread;


    public SerialCommunicator(BearingFinderData holder) {
        this.dataSource = holder;
    }

    public boolean establishConnection(PortSettings settings) throws SerialPortException {
        boolean result = false;
        String portName = settings.getPortName();
        int baudRateDriverCode = getBaudRateDriverCode(settings.getBaudRate());
        int dataBitsDriverCode = getDataBitsDriverCode(settings.getDataBits());
        int stopBitsDriverCode = getStopBitsDriverCode(settings.getStopBits());
        int parityDriverCode = getParityDriverCode(settings.getParity());

        port = new SerialPort(portName);
        port.openPort();
        port.setParams(baudRateDriverCode, dataBitsDriverCode, stopBitsDriverCode, parityDriverCode);
        connectionEstablished = true;
        return result;
    }

    public void startTransmission() {
        transmissionOnLine = true;
        currentTaskRunnable = new TransmissionRunnable();
        currentTransmissionThread = new Thread(currentTaskRunnable);
        currentTransmissionThread.start();
    }

    public void stopTransmission() {
        transmissionOnLine = false;
        if (currentTransmissionThread != null) {
            currentTransmissionThread.interrupt();
            currentTransmissionThread = null;
        }
    }

    private int getParityDriverCode(PortSettings.Parity parity) {
        int parityDriverCode = 0;
        switch (parity) {
            case PARITY_EVEN:
                parityDriverCode = SerialPort.PARITY_EVEN;
                break;
            case PARITY_MARK:
                parityDriverCode = SerialPort.PARITY_MARK;
                break;
            case PARITY_NONE:
                parityDriverCode = SerialPort.PARITY_NONE;
                break;
            case PARITY_ODD:
                parityDriverCode = SerialPort.PARITY_ODD;
                break;
            case PARITY_SPACE:
                parityDriverCode = SerialPort.PARITY_SPACE;
                break;
        }
        return parityDriverCode;
    }

    private int getStopBitsDriverCode(PortSettings.StopBits stopBits) {
        int stopBitsDriverCode = 0;
        switch (stopBits) {
            case STOPBITS_1:
                stopBitsDriverCode = SerialPort.STOPBITS_1;
                break;
            case STOPBITS_1_5:
                stopBitsDriverCode = SerialPort.STOPBITS_1_5;
                break;
            case STOPBITS_2:
                stopBitsDriverCode = SerialPort.STOPBITS_2;
                break;
        }

        return stopBitsDriverCode;
    }

    private int getDataBitsDriverCode(PortSettings.DataBits dataBits) {
        int dataBitsDriverCode = 0;
        switch (dataBits) {
            case DATABITS_5:
                dataBitsDriverCode = SerialPort.DATABITS_5;
                break;
            case DATABITS_6:
                dataBitsDriverCode = SerialPort.DATABITS_6;
                break;
            case DATABITS_7:
                dataBitsDriverCode = SerialPort.DATABITS_7;
                break;
            case DATABITS_8:
                dataBitsDriverCode = SerialPort.DATABITS_8;
                break;
        }
        return dataBitsDriverCode;
    }

    private int getBaudRateDriverCode(PortSettings.BaudRate baudRate) {
        int baudRateDriverCode = 0;
        switch (baudRate) {
            case BAUDRATE_110:
                baudRateDriverCode = SerialPort.BAUDRATE_110;
                break;
            case BAUDRATE_300:
                baudRateDriverCode = SerialPort.BAUDRATE_300;
                break;
            case BAUDRATE_600:
                baudRateDriverCode = SerialPort.BAUDRATE_600;
                break;
            case BAUDRATE_1200:
                baudRateDriverCode = SerialPort.BAUDRATE_600;
                break;
            case BAUDRATE_4800:
                baudRateDriverCode = SerialPort.BAUDRATE_4800;
                break;
            case BAUDRATE_9600:
                baudRateDriverCode = SerialPort.BAUDRATE_9600;
                break;
            case BAUDRATE_14400:
                baudRateDriverCode = SerialPort.BAUDRATE_14400;
                break;
            case BAUDRATE_19200:
                baudRateDriverCode = SerialPort.BAUDRATE_19200;
                break;
            case BAUDRATE_38400:
                baudRateDriverCode = SerialPort.BAUDRATE_38400;
                break;
            case BAUDRATE_57600:
                baudRateDriverCode = SerialPort.BAUDRATE_57600;
                break;
            case BAUDRATE_115200:
                baudRateDriverCode = SerialPort.BAUDRATE_115200;
                break;
            case BAUDRATE_128000:
                baudRateDriverCode = SerialPort.BAUDRATE_128000;
                break;
            case BAUDRATE_256000:
                baudRateDriverCode = SerialPort.BAUDRATE_256000;
                break;

        }
        return baudRateDriverCode;
    }

    public boolean brokeConnection() throws SerialPortException {
        boolean result = false;
        stopTransmission();
        connectionEstablished = false;
        port.closePort();
        return result;
    }

    public List<String> getAvailablePorts() {
        availablePortNames = Arrays.asList(SerialPortList.getPortNames());
        return Collections.unmodifiableList(availablePortNames);
    }

    public boolean isConnectionEstablished() {
        return connectionEstablished;
    }

    public boolean isTransmissionOnLine() {
        return transmissionOnLine;
    }

    private class TransmissionRunnable implements Runnable {
        boolean stopTask = false;

        @Override
        public void run() {
            Set<BearingFinderData.BeaconId> beaconIds = dataSource.getBeaconIds();
            outerLoop:
            while (!stopTask) {
                if(Thread.currentThread().isInterrupted()) {
                    stopTask();
                }
                for (BearingFinderData.BeaconId id : beaconIds) {
                    try {
                        if (stopTask) break outerLoop;
                        if (Thread.currentThread().isInterrupted()) {
                            stopTask();
                            break outerLoop;
                        }
                        port.writeBytes(new byte[]{(byte) dataSource.getDelimiter(), (byte) dataSource.getSize(),
                                (byte) dataSource.getDirectionFinderAddress(), (byte) dataSource.getBeaconAddressById(id),
                                (byte) dataSource.getRssiValueById(id)});
                        sleep();
                    } catch (SerialPortException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        private void sleep() {
            try {
                Thread.sleep(dataSource.getPauseInMillis());
            } catch (InterruptedException e) {
                stopTask = true;
                System.out.println("Stopping task");
            }
        }

        private void stopTask() {
            stopTask = true;
        }
    }
}
