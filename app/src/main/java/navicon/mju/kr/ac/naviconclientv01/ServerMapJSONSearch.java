package navicon.mju.kr.ac.naviconclientv01;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import navicon.mju.kr.ac.naviconclientv01.constants.Constants;
import navicon.mju.kr.ac.naviconclientv01.functions.JSONParser;

/**
 * Created by KimNamhun on 15. 11. 12..
 * 서버에서 Json객체를 얻어오고 화면에 지도를 갱신하는 백그라운드 AsyncTask이다.
 */
public class ServerMapJSONSearch extends AsyncTask<Void, Void, Bitmap> {
    private String serverURL;
    private ImageView mapView;


    public ServerMapJSONSearch(ImageView mapView, String serverURL){
        this.mapView = mapView;
        this.serverURL = serverURL;
    }

    @Override
    protected Bitmap doInBackground(Void... urlString) {
        String stream = null; // json stream 담기
        try{
            System.out.println("ServerMapJSONSearch() -- connection start");
            URL url = new URL(this.serverURL);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            // Check the connection status
            if(urlConnection.getResponseCode() == 200)
            {
                System.out.println("ServerMapJSONSearch() -- connection ok");
                // if response code = 200 ok
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                // Read the BufferedInputStream
                BufferedReader r = new BufferedReader(new InputStreamReader(in));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    sb.append(line);
                }
                stream = sb.toString();
                // End reading...............

                // Disconnect the HttpURLConnection
                urlConnection.disconnect();
            }
            else
            {
                System.out.println("ServerMapJSONSearch() -- connection fail ::::: ERROR");
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        String mapURL = processJSON(stream);
        return loadingMapImage(mapURL);

    }



    private String processJSON(String stream) {
        JSONParser jsonData = new JSONParser(stream); // JSON객체로 만든다
        return jsonData.findMapURL();
    }

    private Bitmap loadingMapImage(String mapURL) {
        Bitmap bitmap = null;
        try {
            String url = Constants.SERVER_URL + mapURL;
            System.out.println("ServerMapJSONSearch() -- map url : " + url);
            InputStream is = new java.net.URL(url).openStream();
            // InputStream에서 Drawable 작성
            bitmap = BitmapFactory.decodeStream(is);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    // doInBackground() 메서드의 수행이 모두 완료되면,
    // doInBackground() 메서드의 리턴값이 여기의 파라미터로 반환된다
    @Override
    protected void onPostExecute(Bitmap map) {
        mapView.setImageBitmap(map);
        System.out.println("ServerMapJSONSearch() -- onPostExecute ::::: SUCCESS");
    }
}
