package org.grlea.imageTiles.pipeline.imageSource;

// $Id: SingleImageSource.java,v 1.1 2004-08-27 01:07:02 grlea Exp $
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
SingleImageSource
implements ImageSource
{
   private final BufferedImage image;

   public
   SingleImageSource(File imageFile)
   throws IOException
   {
      this(ImageIO.read(imageFile));
   }

   public
   SingleImageSource(String imageResourceUrl)
   throws IOException
   {
      this(getResourceUrl(imageResourceUrl));
   }

   private static URL
   getResourceUrl(String imageResourceUrl)
   throws FileNotFoundException
   {
      URL imageUrl = SingleImageSource.class.getClassLoader().getResource(imageResourceUrl);
      if (imageUrl == null)
         throw new FileNotFoundException("Resource '" + imageUrl + "' not found.");
      return imageUrl;
   }

   public
   SingleImageSource(URL imageUrl)
   throws IOException
   {
      this(ImageIO.read(imageUrl));
   }

   public
   SingleImageSource(BufferedImage image)
   {
      this.image = image;
   }

   public BufferedImage
   getImage()
   {
      return image;
   }
}