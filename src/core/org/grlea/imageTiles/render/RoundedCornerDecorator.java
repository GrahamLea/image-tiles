package org.grlea.imageTiles.render;

// $Id: RoundedCornerDecorator.java,v 1.1 2004-08-23 22:47:42 grlea Exp $
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

import org.grlea.imageTiles.TileSpace;

import java.awt.Point;
import java.awt.image.BufferedImage;

/**
 * <p></p>
 *
 * @author grlea
 * @version $Revision: 1.1 $
 */
public class 
RoundedCornerDecorator
implements Decorator
{
   private final int lastPixel;

   private final Point[] transparentPixelXYs;

   private final Point[] semiTransparentPixelXYs;

   public
   RoundedCornerDecorator(TileSpace tileSpace)
   {
      this.lastPixel = tileSpace.getTileSize() - 1;
      // TODO (grahaml) Make the rounded corner size configurable
      transparentPixelXYs =
         new Point[] {new Point(0, 0), new Point(0, lastPixel),
                      new Point(lastPixel, 0), new Point(lastPixel, lastPixel)};

      int lastPixelButOne = lastPixel - 1;
      semiTransparentPixelXYs =
         new Point[] {new Point(1, 0), new Point(0, 1),
                      new Point(lastPixelButOne, 0), new Point(lastPixel, 1),
                      new Point(0, lastPixelButOne), new Point(1, lastPixel),
                      new Point(lastPixelButOne, lastPixel), new Point(lastPixel, lastPixelButOne)};
   }

   public void
   decorate(BufferedImage tileImage)
   {
      // Corner pixels 100% transparent.
      for (int i = 0; i < transparentPixelXYs.length; i++)
      {
         Point point = transparentPixelXYs[i];
         tileImage.setRGB(point.x, point.y, 0);
      }

      // Next to corner pixels get their opacity halved.
      for (int i = 0; i < semiTransparentPixelXYs.length; i++)
      {
         Point point = semiTransparentPixelXYs[i];
         int argb = tileImage.getRGB(point.x, point.y);

         // int a = (argb >> 24) & 0xFF;
         // int newA = a >> 1;
         // int newArgb = (newA << 24) | (argb & 0x00FFFFFF);

         // This line is the inline equivalent of the three above.
         // It just halves the most significant byte of the argb, i.e. the alpha.
         int newArgb = (((argb >> 25) & 0x8F) << 24) | (argb & 0x00FFFFFF);

         tileImage.setRGB(point.x, point.y, newArgb);
      }
   }
}