package org.grlea.imageTiles.pipeline.imageSource;

// $Id: SequentialImageSource.java,v 1.1 2004-08-27 01:07:02 grlea Exp $
// Copyright (c) 2004 Graham Lea. All rights reserved.

// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

import org.grlea.imageTiles.pipeline.ImageSource;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.net.URL;

import javax.imageio.ImageIO;

/**
 * <p></p>
 *
 * @author grlea
 * @version $Revision: 1.1 $
 */
public class 
SequentialImageSource
implements ImageSource
{
   private final BufferedImage[] images;

   private int nextImage = 0;

   public
   SequentialImageSource(File[] imageFiles)
   throws IOException
   {
      this(readImageFiles(imageFiles));
   }

   public
   SequentialImageSource(String[] imageResourceUrls)
   throws IOException
   {
      this(readImageUrls(getResourceUrls(imageResourceUrls)));
   }

   public
   SequentialImageSource(URL[] imageUrls)
   throws IOException
   {
      this(readImageUrls(imageUrls));
   }

   public
   SequentialImageSource(BufferedImage[] images)
   {
      this.images = images;
   }

   public BufferedImage
   getImage()
   {
      try
      {
         return images[nextImage++];
      }
      finally
      {
         if (nextImage == images.length)
            nextImage = 0;
      }
   }

   private static URL[]
   getResourceUrls(String[] resourceUrls)
   throws FileNotFoundException
   {
      ClassLoader classLoader = SequentialImageSource.class.getClassLoader();
      URL[] result = new URL[resourceUrls.length];
      for (int i = 0; i < resourceUrls.length; i++)
      {
         result[i] = classLoader.getResource(resourceUrls[i]);
         if (result[i] == null)
            throw new FileNotFoundException("Resource '" + resourceUrls[i] + "' not found.");
      }
      return result;
   }

   private static BufferedImage[]
   readImageFiles(File[] imageFiles)
   throws IOException
   {
      BufferedImage[] result = new BufferedImage[imageFiles.length];
      for (int i = 0; i < imageFiles.length; i++)
      {
         result[i] = ImageIO.read(imageFiles[i]);
      }
      return result;
   }

   private static BufferedImage[]
   readImageUrls(URL[] imageUrls)
   throws IOException
   {
      BufferedImage[] result = new BufferedImage[imageUrls.length];
      for (int i = 0; i < imageUrls.length; i++)
      {
         result[i] = ImageIO.read(imageUrls[i]);
      }
      return result;
   }
}