package org.grlea.imageTiles;

// $Id: TileSpace.java,v 1.1 2004-08-20 05:25:37 grlea Exp $
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
import java.awt.Rectangle;
import java.util.Collection;

/**
 * <p></p>
 *
 * @author grlea
 * @version $Revision: 1.1 $
 */
public class 
TileSpace
{
   private final int tileSize;

   private final int gap;

   private final int columns;
   private final int rows;

   private final int width;
   private final int height;

   private final Tile[][] tiles;

   public
   TileSpace(int tileSize, int gap, int columns, int rows)
   {
      this.tileSize = tileSize;
      this.gap = gap;
      this.columns = columns;
      this.rows = rows;

      this.tiles = new Tile[rows][columns];

      this.width = (columns * tileSize) + ((columns - 1) * gap);
      this.height = (rows * tileSize) + ((rows - 1) * gap);

      int tileAndGapSize = tileSize + gap;

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

   public static TileSpace
   createTileSpace(Rectangle bounds, int tileSize, int gap)
   {
      int columns = bounds.width / (tileSize + gap);
      int rows = bounds.height / (tileSize + gap);
      return new TileSpace(tileSize, gap, columns, rows);
   }

   public int
   getTileSize()
   {
      return tileSize;
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
   getColumns()
   {
      return columns;
   }

   public int
   getRows()
   {
      return rows;
   }

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
}