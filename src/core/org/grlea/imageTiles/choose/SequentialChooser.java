package org.grlea.imageTiles.choose;

import org.grlea.imageTiles.Chooser;
import org.grlea.imageTiles.Tile;
import org.grlea.imageTiles.TileSpace;

// $Id: SequentialChooser.java,v 1.2 2004-08-27 01:15:33 grlea Exp $
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
 * <p></p>
 *
 * @author grlea
 * @version $Revision: 1.2 $
 */
public class 
SequentialChooser
implements Chooser
{
   private final TileSpace tileSpace;

   private int row = 0;
   private int column = 0;

   public
   SequentialChooser(TileSpace tileSpace)
   {
      this.tileSpace = tileSpace;
   }

   public boolean
   hasMoreTiles()
   {
      return !(row >= tileSpace.getRows());
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
         column++;
         if (column == tileSpace.getColumns())
         {
            column = 0;
            row++;
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