package org.grlea.imageTiles.scale;

// $Id: ThresholdScaler.java,v 1.1 2004-08-23 22:47:43 grlea Exp $
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

import org.grlea.imageTiles.Scaler;
import org.grlea.imageTiles.TileSpace;

import java.awt.Dimension;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

/**
 * <p></p>
 *
 * @author grlea
 * @version $Revision: 1.1 $
 */
public class
ThresholdScaler
implements Scaler
{
   private static final int DEFAULT_INTERPOLATION = AffineTransformOp.TYPE_BILINEAR;

   private static final float DEFAULT_THRESHOLD = 2;

   private final float threshold;

   private final int interpolationType;

   public
   ThresholdScaler()
   {
      this(DEFAULT_THRESHOLD);
   }

   public
   ThresholdScaler(float threshold)
   {
      this(threshold, DEFAULT_INTERPOLATION);
   }

   public
   ThresholdScaler(float threshold, int interpolationType)
   {
      this.threshold = threshold;
      this.interpolationType = interpolationType;
   }

   public BufferedImage
   scale(BufferedImage image, TileSpace tileSpace)
   {
      Dimension tileSpaceSize = tileSpace.getSize();

      boolean needsScaleDown = image.getWidth() > tileSpaceSize.width || image.getHeight() > tileSpaceSize.height;
      // These factors represent the amount by which each dimension would need to be reduced (or
      // expanded) in order to perfectly fit the given dimensions.
      float xFactor = (float) tileSpaceSize.width / image.getWidth();
      float yFactor = (float) tileSpaceSize.height / image.getHeight();

      float scaleFactor = Math.min(xFactor, yFactor);
      if (!needsScaleDown && scaleFactor > threshold)
         scaleFactor = threshold;

      if (scaleFactor == 0)
         return image;

      // TODO (grahaml) Try this with RescaleOp, too.

      // Create a (uniform) scale transform
      AffineTransform transform = AffineTransform.getScaleInstance(scaleFactor, scaleFactor);
      AffineTransformOp transformOp = new AffineTransformOp(transform, interpolationType);
      BufferedImage filteredImage = transformOp.filter(image, null);

      return filteredImage;
   }
}