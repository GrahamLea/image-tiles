package org.grlea.imageTiles;

// $Id: TileSpace.java,v 1.3 2004-09-04 07:59:20 grlea Exp $
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

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.Collection;

/**
 * <p>Defines an array of tiles called a tile space. The space is defined by a tile size, a gap
 * between each pair of tiles, a number of rows and a number of columns. The tiles in the space
 * can be retrieved through the {@link #getTile} method.</p>
 *
 * @author grlea
 * @version $Revision: 1.3 $
 */
public class
TileSpace
{
   private final int tileSize;

   private final int gapSize;

   private final int columns;
   private final int rows;

   private final int width;
   private final int height;

   private final Tile[][] tiles;

   public
   TileSpace(int tileSize, int gapSize, int columns, int rows)
   {
      this.tileSize = tileSize;
      this.gapSize = gapSize;
      this.columns = columns;
      this.rows = rows;

      this.tiles = new Tile[rows][columns];

      this.width = (columns * tileSize) + ((columns - 1) * gapSize);
      this.height = (rows * tileSize) + ((rows - 1) * gapSize);

      int tileAndGapSize = tileSize + gapSize;

      int xOffset = 0;
      int yOffset = 0;
      for (int row = 0; row < rows; row++)
      {
         for (int column = 0; column < columns; column++)
         {
            tiles[row][column] = new Tile(column, row, xOffset, yOffset);
            xOffset += tileAndGapSize;
         }
         xOffset = 0;
         yOffset += tileAndGapSize;
      }
   }

   public int
   getTileSize()
   {
      return tileSize;
   }

   public int
   getGapSize()
   {
      return gapSize;
   }

   public Tile
   getTile(int row, int column)
   {
      return tiles[row][column];
   }

   public Dimension
   getSize()
   {
      return new Dimension(width, height);
   }

   public int
   getWidth()
   {
      return width;
   }

   public int
   getHeight()
   {
      return height;
   }

   public int
   getColumns()
   {
      return columns;
   }

   public int
   getRows()
   {
      return rows;
   }

   public int
   getTileCount()
   {
      return rows * columns;
   }

   /**
    * Adds all the tiles in this <code>TileSpace</code> to the given Collection.
    *
    * @param collection a collection to place {@link Tile}s in.
    */
   public void
   getAllTiles(Collection collection)
   {
      for (int row = 0; row < rows; row++)
      {
         for (int column = 0; column < columns; column++)
         {
            collection.add(tiles[row][column]);
         }
      }
   }

   /**
    * Creates and returns a new {@link BufferedImage} image that is the same size as this
    * <code>TileSpace</code>, with type {@link BufferedImage#TYPE_INT_ARGB}.
    *
    * @return a new BufferedImage
    */
   public BufferedImage
   createImage()
   {
      return createImage(BufferedImage.TYPE_INT_ARGB);
   }

   /**
    * Creates and returns a new {@link BufferedImage} image that is the same size as this
    * <code>TileSpace</code>.
    *
    * @param type the type for the BufferedImage (see {@link BufferedImage}<code>.TYPE_*</code>
    *
    * @return a new BufferedImage
    */
   public BufferedImage
   createImage(int type)
   {
      return new BufferedImage(width, height, type);
   }
}