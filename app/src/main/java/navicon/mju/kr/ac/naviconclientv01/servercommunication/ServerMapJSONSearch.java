package navicon.mju.kr.ac.naviconclientv01.servercommunication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import navicon.mju.kr.ac.naviconclientv01.constants.Constants;
import navicon.mju.kr.ac.naviconclientv01.functions.JSONParser;

/**
 * Created by KimNamhun on 15. 11. 12..
 */
public class ServerMapJSONSearch extends AsyncTask<String, String, Bitmap> {
    private ImageView mapImgView; // 지도 view 객체
    JSONParser jsonData; // json 관리

    public ServerMapJSONSearch(ImageView img){
        this.mapImgView = img;
    }

    @Override
    public Bitmap doInBackground(String... urlString) {
        String stream = null; // json stream 담기
        try{
            System.out.println("ServerMapJSONSearch() -- connection start");
            URL url = new URL(urlString[0]);
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
        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }finally {

        }
        String mapURL = processJSON(stream);
        Bitmap bitmap = loadingMapImage(mapURL);

        return bitmap;
    }

    private String processJSON(String stream) {
        jsonData = new JSONParser(stream); // JSON객체로 만든다
        return jsonData.findMapURL();
    }

    private Bitmap loadingMapImage(String mapURL) {
        InputStream is=null;
        Bitmap bitmap = null;
        try {
            String url = Constants.SERVER_URL + mapURL;
            is = new java.net.URL(url).openStream();
            // InputStream에서 Drawable 작성
            bitmap = BitmapFactory.decodeStream(is);
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    // doInBackground() 메서드의 수행이 모두 완료되면,
    // doInBackground() 메서드의 리턴값이 여기의 파라미터로 반환된다
    public void onPostExecute(Bitmap map) {
        this.mapImgView.setImageBitmap(map);
    }
}
