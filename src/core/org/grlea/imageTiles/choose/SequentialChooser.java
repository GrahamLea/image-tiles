package org.grlea.imageTiles.choose;

import org.grlea.imageTiles.Chooser;
import org.grlea.imageTiles.Tile;
import org.grlea.imageTiles.TileSpace;

// $Id: SequentialChooser.java,v 1.3 2004-09-04 07:59:23 grlea Exp $
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



/**
 * <p>Chooses Tiles sequentially from the TileSpace, either horizontally or vertically as
 * specified.</p>
 *
 * @author grlea
 * @version $Revision: 1.3 $
 */
public class 
SequentialChooser
implements Chooser
{
   private final TileSpace tileSpace;

   private final boolean horizontalFirst;

   private int row = 0;
   private int column = 0;

   /**
    * Creates a new <code>SequentialChooser</code>.
    *
    * @param tileSpace the tile space the chooser is choosing from.
    *
    * @param horizontalFirst <code>true</code> to traverse the tilespace horizontally first (i.e.
    * choose from one row at a time), <code>false</code> to traverse vertically first (i.e. choose
    * from one column at a time).
    */
   public
   SequentialChooser(TileSpace tileSpace, boolean horizontalFirst)
   {
      this.tileSpace = tileSpace;
      this.horizontalFirst = horizontalFirst;
   }

   public boolean
   hasMoreTiles()
   {
      return horizontalFirst ? !(row >= tileSpace.getRows()) : !(column >= tileSpace.getColumns());
   }

   public Tile
   getNextTile()
   {
      try
      {
         return tileSpace.getTile(row, column);
      }
      finally
      {
         if (horizontalFirst)
         {
            column++;
            if (column == tileSpace.getColumns())
            {
               column = 0;
               row++;
            }
         }
         else
         {
            row++;
            if (row == tileSpace.getRows())
            {
               row = 0;
               column++;
            }
         }
      }
   }

   public void
   reset()
   {
      column = 0;
      row = 0;
   }
}