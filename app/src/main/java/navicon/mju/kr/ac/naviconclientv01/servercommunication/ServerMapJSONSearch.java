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
public class ServerMapJSONSearch extends AsyncTask<String, String, String> {
    static String stream = null;
    private JSONParser jsonData; // json 관리
    private ImageView mapImgView;

    public ServerMapJSONSearch(ImageView img){
        this.mapImgView = img;
    }

    @Override
    protected String doInBackground(String... urlString) {
        System.out.println("BackgroundService URL : " + urlString[0]);
        try{
            System.out.println("connection start");
            URL url = new URL(urlString[0]);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            System.out.println("connection response" + urlConnection.getResponseCode());
            // Check the connection status
            if(urlConnection.getResponseCode() == 200)
            {
                System.out.println("200OK");
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
                // Do something
            }
        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }finally {

        }

        System.out.println("JSON Object String : " + stream);
        // Return the data from specified url

        Constants.JSON_CURRENT_DATA = stream;
        Constants.MAP_URL = processJSON();
        loadingMapImage();




        return stream;
    }
    private String processJSON() {
        jsonData = new JSONParser(Constants.JSON_CURRENT_DATA); // JSON객체로 만든다
        return jsonData.findMapURL();
    }

    private void loadingMapImage() {
        InputStream is=null;
        Bitmap bitmap;
        try {
            String url = Constants.SERVER_URL + Constants.MAP_URL;
            System.out.println("MAP URL : " + url);
            is = new java.net.URL(url).openStream();
            // InputStream에서 Drawable 작성
            bitmap = BitmapFactory.decodeStream(is);
            System.out.println("BitmapObject : " + bitmap);
            this.mapImgView.setImageBitmap(bitmap);

        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
