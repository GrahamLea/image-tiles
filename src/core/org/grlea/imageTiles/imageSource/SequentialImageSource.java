package org.grlea.imageTiles.imageSource;

// $Id: SequentialImageSource.java,v 1.3 2004-09-04 07:59:24 grlea Exp $
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

import org.grlea.imageTiles.ImageSource;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

/**
 * <p>Iterates sequentially through a given array of images. Images can be provided as {@link File}s,
 * Resource URLs (either as {@link String}s or {@link URL}s), or as {@link BufferedImage}s. Where
 * files or URLs are provided, all images are loaded in the constructor. As such, beware of using a
 * <code>SequentialImageSource</code> for many or large images, as the images are stored permanently
 * in memory.</p>
 *
 * @author grlea
 * @version $Revision: 1.3 $
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