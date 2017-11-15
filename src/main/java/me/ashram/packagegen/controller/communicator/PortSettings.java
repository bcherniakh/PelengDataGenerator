package me.ashram.packagegen.controller.communicator;

/**
 * Created by ashram on 20.12.2015.
 */
public class PortSettings {
    private String portName = "COM1";
    private BaudRate baudRate = BaudRate.BAUDRATE_115200;
    private DataBits dataBits = DataBits.DATABITS_8;
    private StopBits stopBits = StopBits.STOPBITS_1;
    private Parity parity = Parity.PARITY_NONE;

    public PortSettings() {

    }

    public PortSettings(String name, BaudRate baudRate, DataBits dataBits, StopBits stopBits, Parity parity) {
        if ((name == null) || (baudRate == null) || (dataBits == null)
                || (stopBits == null) || (parity == null)) {
            throw new IllegalArgumentException("Neither of parameter could not be null");
        }
        this.portName = name;
        this.baudRate = baudRate;
        this.dataBits = dataBits;
        this.stopBits = stopBits;
        this.parity = parity;
    }

    public String getPortName() {
        return portName;
    }

    public void setPortName(String portName) {
        this.portName = portName;
    }

    public BaudRate getBaudRate() {
        return baudRate;
    }

    public void setBaudRate(BaudRate baudRate) {
        this.baudRate = baudRate;
    }

    public DataBits getDataBits() {
        return dataBits;
    }

    public void setDataBits(DataBits dataBits) {
        this.dataBits = dataBits;
    }

    public StopBits getStopBits() {
        return stopBits;
    }

    public void setStopBits(StopBits stopBits) {
        this.stopBits = stopBits;
    }

    public Parity getParity() {
        return parity;
    }

    public void setParity(Parity parity) {
        this.parity = parity;
    }

    public enum Parity {
        PARITY_NONE, PARITY_ODD, PARITY_EVEN, PARITY_MARK, PARITY_SPACE;
        public static Parity fromSting(String value) {
            if (value == null) {
                throw new IllegalArgumentException("Could not be null");
            }
            Parity parity = null;
            switch (value) {
                case "none":
                    parity = PARITY_NONE;
                    break;
                case "even":
                    parity = PARITY_EVEN;
                    break;
                case "odd":
                    parity = PARITY_ODD;
                    break;
                case "mark":
                    parity = PARITY_MARK;
                    break;
                case "space":
                    parity = PARITY_SPACE;
                    break;
                default:
                    throw new IllegalArgumentException("Invalid parity value: ");
            }
            return parity;
        }
    }

    public enum BaudRate {
        BAUDRATE_110, BAUDRATE_300, BAUDRATE_600, BAUDRATE_1200, BAUDRATE_4800,
        BAUDRATE_9600, BAUDRATE_14400, BAUDRATE_19200, BAUDRATE_38400, BAUDRATE_57600,
        BAUDRATE_115200, BAUDRATE_128000, BAUDRATE_256000;

        public static BaudRate fromSting(String value) {
            if (value == null) {
                throw new IllegalArgumentException("Could not be null");
            }
            BaudRate baudRate = null;
            switch (value) {
                case "110":
                    baudRate = BAUDRATE_110;
                    break;
                case "300":
                    baudRate = BAUDRATE_300;
                    break;
                case "600":
                    baudRate = BAUDRATE_600;
                    break;
                case "1200":
                    baudRate = BAUDRATE_1200;
                    break;
                case "4800":
                    baudRate = BAUDRATE_4800;
                    break;
                case "9600":
                    baudRate = BAUDRATE_9600;
                    break;
                case "14400":
                    baudRate = BAUDRATE_14400;
                    break;
                case "19200":
                    baudRate = BAUDRATE_19200;
                    break;
                case "38400":
                    baudRate = BAUDRATE_38400;
                    break;
                case "57600":
                    baudRate = BAUDRATE_57600;
                    break;
                case "115200":
                    baudRate = BAUDRATE_115200;
                    break;
                case "128000":
                    baudRate = BAUDRATE_128000;
                    break;
                case "256000":
                    baudRate = BAUDRATE_256000;
                    break;
                default:
                    throw new IllegalArgumentException("Invalid day baud rate: ");
            }
            return baudRate;
        }
    }

    public enum DataBits {
        DATABITS_5, DATABITS_6, DATABITS_7, DATABITS_8;

        public static DataBits fromSting(String value) {
            if (value == null) {
                throw new IllegalArgumentException("Could not be null");
            }
            DataBits dataBits = null;
            switch (value) {
                case "5":
                    dataBits = DATABITS_5;
                    break;
                case "6":
                    dataBits = DATABITS_6;
                    break;
                case "7":
                    dataBits = DATABITS_7;
                    break;
                case "8":
                    dataBits = DATABITS_8;
                    break;
                default:
                    throw new IllegalArgumentException("Invalid data bits value: ");
            }
            return dataBits;
        }
    }

    public enum StopBits {
        STOPBITS_1, STOPBITS_1_5, STOPBITS_2;

        public static StopBits fromSting(String value) {
            if (value == null) {
                throw new IllegalArgumentException("Could not be null");
            }
            StopBits stopBit = null;
            switch (value) {
                case "1":
                    stopBit = STOPBITS_1;
                    break;
                case "1.5":
                    stopBit = STOPBITS_1_5;
                    break;
                case "2":
                    stopBit = STOPBITS_2;
                    break;
                default:
                    throw new IllegalArgumentException("Invalid stop bit value: ");
            }
            return stopBit;
        }
    }
}
