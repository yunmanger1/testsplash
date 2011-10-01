package kz.edu.sdu.jg.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class UrlUtils {

   public static String readURL(URL url) {
      InputStream is = null;
      StringBuilder sb = new StringBuilder();

      try {
         is = url.openStream(); // throws an IOException
         BufferedReader r = new BufferedReader(new InputStreamReader(is, "cp1251"));
         String s = null;
         while ((s = r.readLine()) != null) {
            if (sb.length() != 0) {
               sb.append('\n');
            }
            sb.append(s);
         }
      } catch (Exception ioe) {
         ioe.printStackTrace();
      } finally {

         try {
            is.close();
         } catch (IOException ioe) {
         }

      } // end o
      return sb.toString();
   }
}
