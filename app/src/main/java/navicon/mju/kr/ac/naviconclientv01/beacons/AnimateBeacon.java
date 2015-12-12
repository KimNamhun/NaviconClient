package navicon.mju.kr.ac.naviconclientv01.beacons;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import navicon.mju.kr.ac.naviconclientv01.constants.Constants;

/**
 * Created by KimNamhun on 15. 12. 8..
 * 비콘의 전진 애니메이션을 관리하는 클래스이다.
 */
public class AnimateBeacon {
    private double currentBeaconX;
    private double currentBeaconY;
    private Canvas offScreen;
    private Bitmap image;
    private Paint mPaint;

    public AnimateBeacon() {

    }

    public double getCurrentBeaconX() {
        return currentBeaconX;
    }

    public void setCurrentBeaconX(double currentBeaconX) {
        this.currentBeaconX = currentBeaconX;
    }

    public double getCurrentBeaconY() {
        return currentBeaconY;
    }

    public void setCurrentBeaconY(double currentBeaconY) {
            this.currentBeaconY = currentBeaconY;

    }



    public Canvas getOffScreen() {
        return offScreen;
    }

    public void setOffScreen(Canvas offScreen) {
        this.offScreen = offScreen;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public Paint getmPaint() {
        return mPaint;
    }

    public void setmPaint(Paint mPaint) {
        this.mPaint = mPaint;
    }

    public void drawCurrentBeacon() {

        offScreen.drawBitmap(image, (int)currentBeaconX,(int)currentBeaconY, mPaint);

    }
}
