package caes.web;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WebClient {
  public String getContent(URL url) {
    try {
      StringBuffer content = new StringBuffer();
      HttpURLConnection con = (HttpURLConnection) url.openConnection();
      con.setDoInput(true);
      InputStream is = con.getInputStream();

      BufferedReader r = new BufferedReader(new InputStreamReader(is));
      StringBuilder result = new StringBuilder();
      String line;
      while ((line = r.readLine()) != null) {
        result.append(line);
      }
      return result.toString();
    } catch (Exception e) {
      return null;
    }
  }
}