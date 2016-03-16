package me.ashram.packagegen.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import jssc.SerialPortException;
import me.ashram.packagegen.model.BearingFinderData;
import me.ashram.packagegen.controller.communicator.PortSettings;
import me.ashram.packagegen.controller.communicator.SerialCommunicator;

public class PackageGeneratorController {

    @FXML private Slider levelSlider01;
    @FXML private TextField valueTextField01;
    @FXML private Slider levelSlider02;
    @FXML private TextField valueTextField02;
    @FXML private Slider levelSlider03;
    @FXML private TextField valueTextField03;
    @FXML private Slider levelSlider04;
    @FXML private TextField valueTextField04;
    @FXML private Slider levelSlider05;
    @FXML private TextField valueTextField05;
    @FXML private Slider levelSlider06;
    @FXML private TextField valueTextField06;
    @FXML private Slider levelSlider07;
    @FXML private TextField valueTextField07;
    @FXML private Slider levelSlider08;
    @FXML private TextField valueTextField08;
    @FXML private Slider levelSlider09;
    @FXML private TextField valueTextField09;
    @FXML private Slider levelSlider0A;
    @FXML private TextField valueTextField0A;
    @FXML private Slider levelSlider0B;
    @FXML private TextField valueTextField0B;
    @FXML private Slider levelSlider0C;
    @FXML private TextField valueTextField0C;
    @FXML private ComboBox<String> portValueComboBox;
    @FXML private ComboBox<String> baudValueComboBox;
    @FXML private ComboBox<String> dataValueComboBox;
    @FXML private ComboBox<String> stopValueComboBox;
    @FXML private ComboBox<String> parityValueComboBox;
    @FXML private ToolBar comPortSettingsToolBar;
    @FXML private TextArea logTextArea;
    @FXML private Label connectionStatusLabel;
    @FXML private ToggleButton connectToComPortButton;
    @FXML private Slider frequencySlider;
    @FXML private TextField frequencyValueTextField;
    @FXML private TextField delimiterTextField;
    @FXML private TextField  sizeTextField;
    @FXML private TextField bearingTextField;
    @FXML private ToggleButton startTransmissionButton;

    private SerialCommunicator communicator;
    private BearingFinderData bearingFinderData = new BearingFinderData();


    public void initialize() {

        levelSlider01.valueProperty().addListener(new RssiValueChangeListener(levelSlider01, valueTextField01));
        levelSlider02.valueProperty().addListener(new RssiValueChangeListener(levelSlider02, valueTextField02));
        levelSlider03.valueProperty().addListener(new RssiValueChangeListener(levelSlider03, valueTextField03));
        levelSlider04.valueProperty().addListener(new RssiValueChangeListener(levelSlider04, valueTextField04));
        levelSlider05.valueProperty().addListener(new RssiValueChangeListener(levelSlider05, valueTextField05));
        levelSlider06.valueProperty().addListener(new RssiValueChangeListener(levelSlider06, valueTextField06));
        levelSlider07.valueProperty().addListener(new RssiValueChangeListener(levelSlider07, valueTextField07));
        levelSlider08.valueProperty().addListener(new RssiValueChangeListener(levelSlider08, valueTextField08));
        levelSlider09.valueProperty().addListener(new RssiValueChangeListener(levelSlider09, valueTextField09));
        levelSlider0A.valueProperty().addListener(new RssiValueChangeListener(levelSlider0A, valueTextField0A));
        levelSlider0B.valueProperty().addListener(new RssiValueChangeListener(levelSlider0B, valueTextField0B));
        levelSlider0C.valueProperty().addListener(new RssiValueChangeListener(levelSlider0C, valueTextField0C));

        frequencySlider.valueChangingProperty().addListener((observable, oldValue, newValue) -> {
            String value =  String.valueOf((int) frequencySlider.getValue());
            frequencyValueTextField.textProperty().setValue(value);
            bearingFinderData.setPauseInMillis(getPauseInMillis());
        });

        ObservableList<String> baudValuesList = FXCollections.observableArrayList("300", "600", "1200", "2400", "4800",
                "9600", "1400", "19200", "28800", "38400", "56000", "57600", "115200");
        baudValueComboBox.setItems(baudValuesList);
        baudValueComboBox.getSelectionModel().select(5);

        ObservableList<String> dataBitsValuesList = FXCollections.observableArrayList("5", "6", "7", "8");
        dataValueComboBox.setItems(dataBitsValuesList);
        dataValueComboBox.getSelectionModel().select(3);

        ObservableList<String> stopBitsValueList = FXCollections.observableArrayList("1", "1.5", "2");
        stopValueComboBox.setItems(stopBitsValueList);
        stopValueComboBox.getSelectionModel().select(0);

        ObservableList<String> parityValueList = FXCollections.observableArrayList("none", "even", "odd", "mark", "space");
        parityValueComboBox.setItems(parityValueList);
        parityValueComboBox.getSelectionModel().select(0);

        initializeSlidersValues();

//        portValueComboBox.setOnMousePressed(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//
//                portNameComboBoxClicked();
//            }
//        });

        portValueComboBox.setOnMousePressed((MouseEvent event) -> {
            portValueComboBox.setItems(FXCollections.observableArrayList(communicator.getAvailablePorts()));
        });

        bearingFinderData.setDelimiter(Byte.parseByte(delimiterTextField.getText(), 16));
        bearingFinderData.setSize(Byte.parseByte(sizeTextField.getText(), 16));
        bearingFinderData.setPauseInMillis(getPauseInMillis());
        bearingFinderData.setDirectionFinderAddress(10);
        communicator = new SerialCommunicator(bearingFinderData);

        portValueComboBox.setItems(FXCollections.observableArrayList(communicator.getAvailablePorts()));
        portValueComboBox.getSelectionModel().selectLast();

        delimiterTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                int newDelimiter = Integer.parseInt(newValue, 16);
                bearingFinderData.setDelimiter(newDelimiter);
            } catch (NumberFormatException|ClassCastException exception) {
                System.out.println("Exception occurred with delimiter value");
                logString("Invalid delimiter: " + exception.getMessage());
            }
        });

        sizeTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                int newSize = Integer.parseInt(newValue, 16);
                bearingFinderData.setSize(newSize);
            } catch (NumberFormatException|ClassCastException exception) {
                System.out.println("Exception occurred with size value");
                logString("Invalid size: " + exception.getMessage());
            }
        });

        bearingTextField.textProperty().addListener(((observable, oldValue, newValue) -> {
            try {
                int directionFinderAddress = Integer.parseInt(newValue, 16);
                bearingFinderData.setDirectionFinderAddress(directionFinderAddress);
            } catch (NumberFormatException|ClassCastException exception){
                System.out.println("Exception occurred with device address value");
                logString("Invalid value: " + exception.getMessage());
            }
        }));

        System.out.println("Initialization completed");
    }

    private int getPauseInMillis() {
        int pause = 1000 / Integer.parseInt(frequencyValueTextField.getText());
        return pause;
    }

    public void startTransmissionClicked(ActionEvent event) {
        if (communicator.isTransmissionOnLine()) {
            startTransmissionButton.setText("Start transmission");
            communicator.stopTransmission();
            logString("Transmission stopped");
        } else {
            startTransmissionButton.setText("Stop transmission");
            communicator.startTransmission();
            logString("Transmission started");
        }
    }

    public void clearLogTextAreaClicked(ActionEvent event) {
        logTextArea.clear();
    }

    public void connectToComPort(ActionEvent event) {
        if (communicator.isConnectionEstablished()) {
            try {
                communicator.brokeConnection();
            } catch (SerialPortException exception) {
                logString("Something gone wrong: " + exception.getMessage());
            }

            startTransmissionButton.setDisable(true);
            startTransmissionButton.setSelected(false);
            startTransmissionButton.setText("Start transmission");
            comPortSettingsToolBar.setDisable(false);
            connectToComPortButton.setText("connect");
            connectionStatusLabel.setText("disconnected");
            logString("Connection broken with port: " + portValueComboBox.getValue());
        } else {
            String portName = portValueComboBox.getValue();
            PortSettings.BaudRate baudRate = PortSettings.BaudRate.fromSting(baudValueComboBox.getValue());
            PortSettings.DataBits dataBits = PortSettings.DataBits.fromSting(dataValueComboBox.getValue());
            PortSettings.StopBits stopBits = PortSettings.StopBits.fromSting(stopValueComboBox.getValue());
            PortSettings.Parity parity = PortSettings.Parity.fromSting(parityValueComboBox.getValue());

            PortSettings settings = new PortSettings(portName, baudRate, dataBits, stopBits, parity);
            try {
                communicator.establishConnection(settings);
            } catch (SerialPortException exception){
                logString("Connection failed: " + exception.getMessage());
                connectToComPortButton.setSelected(false);
                return;
            }
            startTransmissionButton.setDisable(false);
            comPortSettingsToolBar.setDisable(true);
            connectToComPortButton.setText("disconnect");
            connectionStatusLabel.setText("connected");
            logString("Connection established with port: " + portName);
        }
    }

    public void portNameComboBoxClicked() {
        portValueComboBox.setItems(FXCollections.observableArrayList(communicator.getAvailablePorts()));
    }

    private void logString(String data) {
        String lineSeparator = System.getProperty("line.separator");
        logTextArea.appendText(">" + data + lineSeparator);
    }

    private void initializeSlidersValues() {
        valueTextField01.textProperty().setValue("0x" + Integer.toHexString((int)
                levelSlider01.getValue()).toUpperCase());
        valueTextField02.textProperty().setValue("0x" + Integer.toHexString((int)
                levelSlider02.getValue()).toUpperCase());
        valueTextField03.textProperty().setValue("0x" + Integer.toHexString((int)
                levelSlider03.getValue()).toUpperCase());
        valueTextField04.textProperty().setValue("0x" + Integer.toHexString((int)
                levelSlider04.getValue()).toUpperCase());
        valueTextField05.textProperty().setValue("0x" + Integer.toHexString((int)
                levelSlider05.getValue()).toUpperCase());
        valueTextField06.textProperty().setValue("0x" + Integer.toHexString((int)
                levelSlider06.getValue()).toUpperCase());
        valueTextField07.textProperty().setValue("0x" + Integer.toHexString((int)
                levelSlider07.getValue()).toUpperCase());
        valueTextField08.textProperty().setValue("0x" + Integer.toHexString((int)
                levelSlider08.getValue()).toUpperCase());
        valueTextField09.textProperty().setValue("0x" + Integer.toHexString((int)
                levelSlider09.getValue()).toUpperCase());
        valueTextField0A.textProperty().setValue("0x" + Integer.toHexString((int)
                levelSlider0A.getValue()).toUpperCase());
        valueTextField0B.textProperty().setValue("0x" + Integer.toHexString((int)
                levelSlider0B.getValue()).toUpperCase());
        valueTextField0C.textProperty().setValue("0x" + Integer.toHexString((int)
                levelSlider0C.getValue()).toUpperCase());
        frequencyValueTextField.textProperty().setValue(String.valueOf((int) frequencySlider.getValue()));
    }

    private class RssiValueChangeListener implements ChangeListener {
        private Slider slider;
        private TextField textField;

        public RssiValueChangeListener(Slider slider, TextField textField) {
            this.slider = slider;
            this.textField = textField;
        }

        @Override
        public void changed(ObservableValue observable, Object oldValue, Object newValue) {
            String value =  Integer.toHexString((int) slider.getValue()).toUpperCase();
            textField.textProperty().setValue("0x" + value);
            String sliderId = slider.getId();
            bearingFinderData.setRssiByBeaconId(BearingFinderData.BeaconId.fromSting(sliderId.substring(sliderId.length()-2)),
                    (int) slider.getValue());
        }
    }

}
