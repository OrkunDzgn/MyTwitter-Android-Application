package com.orkunduzgun.mongodbandamazonconnection;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.Map;

public class YardimciMetodlar {
    public static void httpPost(HttpURLConnection baglanti, byte[] bayt) throws IOException {
        baglanti.setRequestMethod("POST"); //Default olarak GET yani dosya çekme. POST ile gönderme yapıyoruz bunu.
        baglanti.setRequestProperty("Content-Type", "application/json"); //hangi veri turu gonderdigimizi belirtiyoruz. http çalışmasından kaynaklı zorundayız. mesela image/jpg
        baglanti.setRequestProperty("Content-Length", Integer.toString(bayt.length));
        baglanti.setRequestProperty("Accept-Charset", "UTF-8");
        baglanti.setDoOutput(true);
        baglanti.getOutputStream().write(bayt);
    }

    public static String responseTumString(HttpURLConnection baglanti) throws IOException {
        InputStream in = baglanti.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(in)); //string okurken bayt okumak java'nın mallığı. Çoğu dil string'i tam olarak labiliyor. Burada bayt olarak okuyoruz.
        String satir = "!";
        String tumIcerik = "";
        while (satir != null) {
            satir = br.readLine();
            tumIcerik += satir;
        }
        return tumIcerik;
    }
}
