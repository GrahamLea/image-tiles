package org.grlea.imageTiles.imageSource;

// $Id: GoogleImageSource.java,v 1.1 2004-09-04 07:59:24 grlea Exp $
// Copyright (c) 2003 Forge Research Pty Ltd. All rights reserved.
// www.forge.com.au

import org.grlea.imageTiles.ImageSource;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

/**
 * <p>Sources images using a Google Image Search. The search terms are provided and images are
 * downloaded on the fly. Obviously, this ImageSource requires an internet connection to function
 * correctly.</p>
 *
 * @author grahaml
 * @version $Revision: 1.1 $
 */
public class 
GoogleImageSource
implements ImageSource
{
   private static final String URL = "http://images.google.com/images?";
   private static final String QUERY = "q=";
   private static final String IMAGE_SIZE = "&imgsz=large|xlarge|xxlarge";
   private static final String ALL_IMAGES = "&filter=0";
   private static final String PAGE = "&start=20";

   private static final String IMAGE_URL_PATTERN = "imgurl=(.*)&";

   private final String searchString;

   private final Pattern pattern;

   public
   GoogleImageSource(String searchString)
   {
      this.searchString = searchString;
      pattern = Pattern.compile(IMAGE_URL_PATTERN);

//      System.setProperty("http.proxyHost", "proxy");
//      System.setProperty("http.proxyPort", "8080");
   }

   public BufferedImage
   getImage()
   {
      BufferedImage image = null;

      do
      {
         URL url = getImageUrl();
         try
         {
            image = ImageIO.read(url);
         }
         catch (IOException e)
         {
            // TODO (grahaml) Debug this?
         }
      }
      while (image == null);

      return image;
   }

   private URL
   getImageUrl()
   {
      InputStream connectionIn = null;
      try
      {
         String searchUrlStr =
            URL + QUERY + URLEncoder.encode(searchString, "UTF-8") + IMAGE_SIZE + ALL_IMAGES + PAGE;
         System.out.println("Creating URL");
         URL searchUrl = new URL(searchUrlStr);
         System.out.println("Opening connection");
         HttpURLConnection connection = (HttpURLConnection) searchUrl.openConnection();
         System.out.println("connection.usingProxy() = " + connection.usingProxy());
         System.out.println("Creating InputStream");
         connectionIn = connection.getInputStream();
         System.out.println("Reading");

         InputStreamReader connectionReader = new InputStreamReader(connectionIn);
         BufferedReader in = new BufferedReader(connectionReader);
         String line;

         while ((line = in.readLine()) != null)
         {
            System.out.println("line = " + line);
            Matcher matcher = pattern.matcher(line);
            System.out.println("matcher.find() = " + matcher.find());
            if (matcher.find())
            {
               String imageUrl = matcher.group(1);
               System.out.println("imageUrl = " + imageUrl);
               return new URL(imageUrl);
            }
         }

         in.close();
         System.out.println("connection.usingProxy() = " + connection.usingProxy());
         connection.disconnect();
         System.out.println("connection.usingProxy() = " + connection.usingProxy());

         // TODO (grahaml) Do something else here.
         throw new IOException("No images found.");
      }
      catch (IOException e)
      {
         // TODO (grahaml) Do something else here.
         throw new UnsupportedOperationException("Failed image search: " + e);
      }
   }
}