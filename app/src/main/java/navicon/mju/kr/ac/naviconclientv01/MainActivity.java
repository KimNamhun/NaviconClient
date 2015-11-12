package navicon.mju.kr.ac.naviconclientv01;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.wizturn.sdk.central.Central;
import com.wizturn.sdk.central.CentralManager;
import com.wizturn.sdk.peripheral.Peripheral;
import com.wizturn.sdk.peripheral.PeripheralScanListener;

import navicon.mju.kr.ac.naviconclientv01.constants.Constants;


public class MainActivity extends ActionBarActivity {

    private CentralManager centralManager; // 비콘 매니저

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(); // 비콘 탐지 구동을 위한 초기 셋팅
        start(); // 비콘 탐지 구동
        System.out.println("finish");
        setContentView(R.layout.activity_main);

    }

    private void init() { // 비콘 탐지 구동을 위한 초기 셋팅
        setCentralManager(); // 비콘 탐지를 위한 매니저 셋팅 메소드
        terminateIfNotBLE(); // BLE가 아니면 종료
        turnOnBluetooth(); // 블루투스를 켠다
    }

    private void setCentralManager() { // 비콘 탐지를 위한 매니저 셋팅 메소드
        centralManager = CentralManager.getInstance();
        centralManager.init(getApplicationContext());
        centralManager.setPeripheralScanListener(new PeripheralScanListener() {
            @Override
            public void onPeripheralScan(Central central, final Peripheral peripheral) {
                System.out.println("onPeripheralScan() : peripheral : " + peripheral);
                runOnUiThread(new Runnable() {
                    public void run() {
                        if(peripheral.getDistance()<Constants.SHORTEST_BEACON_DISTANCE) { // 지정한 거리내에 들어오는 비콘을 최단거리 비콘 major(위치번호)로 설정한다
                            Constants.CURRENT_SHORTEST_BEACON_MAJOR=peripheral.getMajor();
                            System.out.println("current map beacon major : " + Constants.CURRENT_SHORTEST_BEACON_MAJOR);
                        }
                    }
                });
            }
        });
    }

    private void terminateIfNotBLE() { // BLE가 아니면 종료
        if(!centralManager.isBLESupported()) {
            Toast.makeText(this, "비콘이 탐지 되지 않습니다.", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void turnOnBluetooth() { // 블루투스를 켠다
        if(!centralManager.isBluetoothEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, Constants.REQUEST_ENABLE_BT);
        }
    }

    private void start() {
        centralManager.startScanning(); // 비콘 탐지 구동
    }

}
