package navicon.mju.kr.ac.naviconclientv01.beacons;

import navicon.mju.kr.ac.naviconclientv01.constants.Constants;

/**
 * Created by KimNamhun on 15. 11. 29..
 * 비콘 애니메이션 카운트 관리 클래스이다.
 */
public class BeaconAnimation {
    private int beaconAnmationCount;
    private int currentPinSize;
    private int beaconPlusMinusSwitch;


public BeaconAnimation() {
    beaconAnmationCount = 0;
    currentPinSize = Constants.CURRENT_PIN_SIZE;
    beaconPlusMinusSwitch = 0;
}

    public void plusBeaconAnmationCount() {
        if(this.beaconAnmationCount > Constants.PIN_CHANGE_COUNT) {
            beaconAnmationCount = 0;
            if(beaconPlusMinusSwitch == 0) {
                beaconPlusMinusSwitch = 1;
            } else {
                beaconPlusMinusSwitch = 0;
            }
        } else {
            beaconAnmationCount++;
        }
    }

    public int getCurrentPinSize() {
        return currentPinSize;
    }

    public void plusCurrentPinSize() {
        if(this.beaconAnmationCount < Constants.PIN_CHANGE_COUNT) {
            if(this.beaconPlusMinusSwitch == 1 && this.currentPinSize <= Constants.MAX_PIN_SIZE) {
                currentPinSize += Constants.VARIABLE_PIN_SIZE;
            } else if(this.beaconPlusMinusSwitch == 0 && this.currentPinSize >= Constants.MIN_PIN_SIZE) {
                currentPinSize -= Constants.VARIABLE_PIN_SIZE;
            }


        }
    }
}
