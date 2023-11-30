package com.example.usbhosttesting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.app.Activity;
import android.content.Context;

public class MainActivity extends Activity {

    Button btnCheck;
    TextView textInfo;
    TextView textInfoInterface;
    TextView textEndPoint;

    Spinner spDeviceName;
    ArrayList<String> listDeviceName;
    ArrayList<UsbDevice> listUsbDevice;
    ArrayAdapter<String> adapterDevice;

    Spinner spInterface;
    ArrayList<String> listInterface;
    ArrayList<UsbInterface> listUsbInterface;
    ArrayAdapter<String> adapterInterface;

    Spinner spEndPoint;
    ArrayList<String> listEndPoint;
    ArrayList<UsbEndpoint> listUsbEndpoint;
    ArrayAdapter<String> adapterEndpoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spDeviceName = (Spinner)findViewById(R.id.spinnerdevicename);
        spInterface = (Spinner)findViewById(R.id.spinnerinterface);
        spEndPoint = (Spinner)findViewById(R.id.spinnerendpoint);
        textInfo = (TextView) findViewById(R.id.info);
        textInfoInterface = (TextView)findViewById(R.id.infointerface);
        textEndPoint = (TextView)findViewById(R.id.infoendpoint);

        btnCheck = (Button) findViewById(R.id.check);
        btnCheck.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                checkDeviceInfo();
            }
        });
    }

    private void checkDeviceInfo() {

        listDeviceName = new ArrayList<String>();
        listUsbDevice = new ArrayList<UsbDevice>();

        UsbManager manager = (UsbManager) getSystemService(Context.USB_SERVICE);
        HashMap<String, UsbDevice> deviceList = manager.getDeviceList();
        Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();

        while (deviceIterator.hasNext()) {
            UsbDevice device = deviceIterator.next();
            listDeviceName.add(device.getDeviceName());
            listUsbDevice.add(device);

        }

        textInfo.setText("");
        textInfoInterface.setText("");
        textEndPoint.setText("");

        adapterDevice = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listDeviceName);
        adapterDevice.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDeviceName.setAdapter(adapterDevice);
        spDeviceName.setOnItemSelectedListener(deviceOnItemSelectedListener);
    }

    OnItemSelectedListener deviceOnItemSelectedListener =
            new OnItemSelectedListener(){

                @Override
                public void onItemSelected(AdapterView<?> parent,
                                           View view, int position, long id) {
                    UsbDevice device = listUsbDevice.get(position);

                    String i = device.toString() + "\n" +
                            "DeviceID: " + device.getDeviceId() + "\n" +
                            "DeviceName: " + device.getDeviceName() + "\n" +
                            "DeviceClass: " + device.getDeviceClass() + " - "
                            + translateDeviceClass(device.getDeviceClass()) + "\n" +
                            "DeviceSubClass: " + device.getDeviceSubclass() + "\n" +
                            "VendorID: " + device.getVendorId() + "\n" +
                            "ProductID: " + device.getProductId() + "\n" +
                            "InterfaceCount: " + device.getInterfaceCount();
                    textInfo.setText(i);

                    checkUsbDevicve(device);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {}

            };

    private void checkUsbDevicve(UsbDevice d) {
        listInterface = new ArrayList<String>();
        listUsbInterface = new ArrayList<UsbInterface>();

        for(int i=0; i<d.getInterfaceCount(); i++){
            UsbInterface usbif = d.getInterface(i);
            listInterface.add(usbif.toString());
            listUsbInterface.add(usbif);
        }

        adapterInterface = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listInterface);
        adapterDevice.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spInterface.setAdapter(adapterInterface);
        spInterface.setOnItemSelectedListener(interfaceOnItemSelectedListener);
    }

    OnItemSelectedListener interfaceOnItemSelectedListener =
            new OnItemSelectedListener(){

                @Override
                public void onItemSelected(AdapterView<?> parent,
                                           View view, int position, long id) {

                    UsbInterface selectedUsbIf = listUsbInterface.get(position);

                    String sUsbIf = "\n" + selectedUsbIf.toString() + "\n"
                            + "Id: " + selectedUsbIf.getId() + "\n"
                            + "InterfaceClass: " + selectedUsbIf.getInterfaceClass() + "\n"
                            + "InterfaceProtocol: " + selectedUsbIf.getInterfaceProtocol() + "\n"
                            + "InterfaceSubclass: " + selectedUsbIf.getInterfaceSubclass() + "\n"
                            + "EndpointCount: " + selectedUsbIf.getEndpointCount();

                    textInfoInterface.setText(sUsbIf);
                    checkUsbInterface(selectedUsbIf);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {}

            };

    private void checkUsbInterface(UsbInterface uif) {
        listEndPoint = new ArrayList<String>();
        listUsbEndpoint = new ArrayList<UsbEndpoint>();

        for(int i=0; i<uif.getEndpointCount(); i++){
            UsbEndpoint usbEndpoint = uif.getEndpoint(i);
            listEndPoint.add(usbEndpoint.toString());
            listUsbEndpoint.add(usbEndpoint);
        }

        adapterEndpoint = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listEndPoint);
        adapterEndpoint.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spEndPoint.setAdapter(adapterEndpoint);
        spEndPoint.setOnItemSelectedListener(endpointOnItemSelectedListener);
    }

    OnItemSelectedListener endpointOnItemSelectedListener =
            new OnItemSelectedListener(){

                @Override
                public void onItemSelected(AdapterView<?> parent,
                                           View view, int position, long id) {

                    UsbEndpoint selectedEndpoint = listUsbEndpoint.get(position);

                    String sEndpoint = "\n" + selectedEndpoint.toString() + "\n"
                            + translateEndpointType(selectedEndpoint.getType());

                    textEndPoint.setText(sEndpoint);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {}

            };

    private String translateEndpointType(int type){
        switch(type){
            case UsbConstants.USB_ENDPOINT_XFER_CONTROL:
                return "USB_ENDPOINT_XFER_CONTROL (endpoint zero)";
            case UsbConstants.USB_ENDPOINT_XFER_ISOC:
                return "USB_ENDPOINT_XFER_ISOC (isochronous endpoint)";
            case UsbConstants.USB_ENDPOINT_XFER_BULK :
                return "USB_ENDPOINT_XFER_BULK (bulk endpoint)";
            case UsbConstants.USB_ENDPOINT_XFER_INT:
                return "USB_ENDPOINT_XFER_INT (interrupt endpoint)";
            default:
                return "unknown";
        }
    }

    private String translateDeviceClass(int deviceClass){
        switch(deviceClass){
            case UsbConstants.USB_CLASS_APP_SPEC:
                return "Application specific USB class";
            case UsbConstants.USB_CLASS_AUDIO:
                return "USB class for audio devices";
            case UsbConstants.USB_CLASS_CDC_DATA:
                return "USB class for CDC devices (communications device class)";
            case UsbConstants.USB_CLASS_COMM:
                return "USB class for communication devices";
            case UsbConstants.USB_CLASS_CONTENT_SEC:
                return "USB class for content security devices";
            case UsbConstants.USB_CLASS_CSCID:
                return "USB class for content smart card devices";
            case UsbConstants.USB_CLASS_HID:
                return "USB class for human interface devices (for example, mice and keyboards)";
            case UsbConstants.USB_CLASS_HUB:
                return "USB class for USB hubs";
            case UsbConstants.USB_CLASS_MASS_STORAGE:
                return "USB class for mass storage devices";
            case UsbConstants.USB_CLASS_MISC:
                return "USB class for wireless miscellaneous devices";
            case UsbConstants.USB_CLASS_PER_INTERFACE:
                return "USB class indicating that the class is determined on a per-interface basis";
            case UsbConstants.USB_CLASS_PHYSICA:
                return "USB class for physical devices";
            case UsbConstants.USB_CLASS_PRINTER:
                return "USB class for printers";
            case UsbConstants.USB_CLASS_STILL_IMAGE:
                return "USB class for still image devices (digital cameras)";
            case UsbConstants.USB_CLASS_VENDOR_SPEC:
                return "Vendor specific USB class";
            case UsbConstants.USB_CLASS_VIDEO:
                return "USB class for video devices";
            case UsbConstants.USB_CLASS_WIRELESS_CONTROLLER:
                return "USB class for wireless controller devices";
            default: return "Unknown USB class!";

        }
    }

}