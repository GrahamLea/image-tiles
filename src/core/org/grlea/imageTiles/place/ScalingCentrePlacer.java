package org.grlea.imageTiles.place;

// $Id: ScalingCentrePlacer.java,v 1.2 2005-03-31 21:22:46 grlea Exp $
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
import org.grlea.imageTiles.Placer;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

/**
 * <p>Places images in the centre of the TileSpace, as well as scaling them up or down as necessary.
 * A threshold can be placed on up-scaling to prevent scaling teeny-weeny images so large that you
 * can see the gaps between the pixels.</p>
 *
 * @author grlea
 * @version $Revision: 1.2 $
 */
public class 
ScalingCentrePlacer
implements Placer
{
   private static final float DEFAULT_THRESHOLD = 2;

   private final float threshold;

   /**
    * Creates a new this with the default up-scaling threshold (2).
    */
   public
   ScalingCentrePlacer()
   {
      this(DEFAULT_THRESHOLD);
   }

   /**
    * Creates a new this with the specified up-scaling threshold.
    *
    * @param threshold a threshold on up-scaling, given as a maximum scale factor (i.e. 2.0 for a
    * maximum of 2x scaling, 1.5 for a maximum of 1.5x, etc.). Use {@link Float#POSITIVE_INFINITY}
    * to scale to the * full size of the TileSpace no matter the size of the image. Use 0 (zero) to
    * perform down-scaling only.
    */
   public
   ScalingCentrePlacer(float threshold)
   {
      this.threshold = threshold;
   }

   public void
   place(TileSpace tileSpace, BufferedImage sourceImage, Graphics2D targetGraphics)
   {
      Dimension tileSpaceSize = tileSpace.getSize();

      int sourceImageWidth = sourceImage.getWidth();
      boolean needsScaleDown =
         sourceImageWidth > tileSpaceSize.width || sourceImage.getHeight() > tileSpaceSize.height;
      // These factors represent the amount by which each dimension would need to be reduced (or
      // expanded) in order to perfectly fit the given dimensions.
      float xFactor = (float) tileSpaceSize.width / sourceImageWidth;
      int sourceImageHeight = sourceImage.getHeight();
      float yFactor = (float) tileSpaceSize.height / sourceImageHeight;

      float scaleFactor = Math.min(xFactor, yFactor);
      if (!needsScaleDown && scaleFactor > threshold)
         scaleFactor = threshold;

      int newImageWidth = (int) (sourceImageWidth * scaleFactor);
      int newImageHeight = (int) (sourceImageHeight * scaleFactor);

      int x = (tileSpaceSize.width - newImageWidth) / 2;
      int y = (tileSpaceSize.height - newImageHeight) / 2;

      if (scaleFactor != 0)
      {
         x /= scaleFactor;
         y /= scaleFactor;
         targetGraphics.scale(scaleFactor, scaleFactor);
      }

      targetGraphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                                      RenderingHints.VALUE_INTERPOLATION_BICUBIC);
      targetGraphics.setRenderingHint(RenderingHints.KEY_RENDERING,
                                      RenderingHints.VALUE_RENDER_QUALITY);
      targetGraphics.drawImage(sourceImage, x, y, null);
   }
}