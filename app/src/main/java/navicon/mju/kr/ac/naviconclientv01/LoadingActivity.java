package navicon.mju.kr.ac.naviconclientv01;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import navicon.mju.kr.ac.naviconclientv01.constants.Constants;

/**
 * Created by KimNamhun on 15. 11. 29..
 * 초기 로딩 화면이다
 */
public class LoadingActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Thread.sleep(Constants.MAIN_LOADING_TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        startActivity(new Intent(this, MainActivity.class));
        finish();

    }
}
