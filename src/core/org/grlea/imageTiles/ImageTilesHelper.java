package org.grlea.imageTiles;

// $Id: ImageTilesHelper.java,v 1.4 2004-09-04 07:59:17 grlea Exp $
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

import java.awt.Component;
import java.awt.Dimension;
import java.awt.image.BufferedImage;

/**
 * <p>Methods to help in the set up of an Image Tiles TileSpace and Pipeline.</p>
 *
 * @author grlea
 * @version $Revision: 1.4 $
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
      int tileSize = (int) (Math.sqrt(Math.sqrt(width * height)) * 1.5);
      return tileSize;
   }

   public static int
   chooseAppropriateGapSize(int tileSize)
   {
      if (tileSize < 32)
         return 0;

      if (tileSize < 96)
         return 1;

      return 2;
   }

   public static TileSpace
   createTileSpace(Component component, int tileSize, int gapSize)
   {
      return createTileSpace(component.getSize(), tileSize, gapSize);
   }

   public static TileSpace
   createTileSpace(Dimension dimensions, int tileSize, int gapSize)
   {
      int tileAndGapWidth = tileSize + gapSize;
      int columns = dimensions.width / (tileAndGapWidth);
      int rows = dimensions.height / (tileAndGapWidth);
      return new TileSpace(tileSize, gapSize, columns, rows);
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
}