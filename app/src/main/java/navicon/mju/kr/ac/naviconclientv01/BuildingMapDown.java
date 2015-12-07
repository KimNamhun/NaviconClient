package navicon.mju.kr.ac.naviconclientv01;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * Created by KimNamhun on 15. 11. 12..
 * 서버에서 Json객체를 얻어오고 화면에 지도를 갱신하는 백그라운드 AsyncTask이다.
 */
public class BuildingMapDown extends AsyncTask<Void, Void, Bitmap> {

    private String serverURL;
    public BuildingMapDown(String serverURL){
        this.serverURL = serverURL;
    }

    @Override
    protected Bitmap doInBackground(Void... urlString) {
        System.out.println(serverURL);
        URL myImageURL = null;
        try {
            myImageURL = new URL(serverURL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection connection = (HttpURLConnection)myImageURL .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return  BitmapFactory.decodeStream(input);
        } catch (IOException e) {

            e.printStackTrace();
        }
        return null;
    }






    // doInBackground() 메서드의 수행이 모두 완료되면,
    // doInBackground() 메서드의 리턴값이 여기의 파라미터로 반환된다
    @Override
    protected void onPostExecute(Bitmap map) {

    }


}
