package org.grlea.imageTiles;

// $Id: ImageTilesHelper.java,v 1.1 2004-08-23 22:47:37 grlea Exp $
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

import org.grlea.imageTiles.swing.AnimatedTileCanvas;

import java.awt.Component;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 * <p></p>
 *
 * @author grlea
 * @version $Revision: 1.1 $
 */
public class 
ImageTilesHelper
{
   private
   ImageTilesHelper()
   {}

   public static int
   chooseAppropriateTileSize(BufferedImage image)
   {
      int width = image.getWidth();
      int height = image.getHeight();
      int tileSize = (int) (Math.sqrt(width * height) / 16);
      return tileSize;
   }

   public static int
   chooseAppropriateGapSize(int tileSize)
   {
      if (tileSize < 16)
         return 0;

      if (tileSize < 64)
         return 1;

      return 2;
   }

   public static TileSpace
   createTileSpace(Component component, int tileSize, int gap)
   {
      int tileAndGapWidth = tileSize + gap;
      Rectangle bounds = component.getBounds();
      int columns = bounds.width / (tileAndGapWidth);
      int rows = bounds.height / (tileAndGapWidth);
      return new TileSpace(tileSize, gap, columns, rows);
   }

   public static TileSpace
   createTileSpace(BufferedImage image)
   {
      int tileSize = chooseAppropriateTileSize(image);
      int gapSize = chooseAppropriateGapSize(tileSize);
      return createTileSpace(image, tileSize, gapSize);
   }

   public static TileSpace
   createTileSpace(BufferedImage image, int tileSize, int gapSize)
   {
      int tileAndGapSize = tileSize + gapSize;

      int columns = image.getWidth() / tileAndGapSize;
      int imageSpaceWidth;
      do
      {
         columns++;
         imageSpaceWidth = (columns * tileSize) + ((columns - 1) * gapSize);
      }
      while (imageSpaceWidth < image.getWidth());

      int rows = image.getHeight() / tileAndGapSize;
      int imageSpaceHeight;
      do
      {
         rows++;
         imageSpaceHeight = (rows * tileSize) + ((rows - 1) * gapSize);
      }
      while (imageSpaceHeight < image.getHeight());

      return new TileSpace(tileSize, gapSize, columns, rows);
   }

   public AnimatedTileCanvas
   createAnimatedTileCanvas(BufferedImage image)
   {
      // TODO (grahaml) Implement
      throw new UnsupportedOperationException("Not implemented."); 
   }
}