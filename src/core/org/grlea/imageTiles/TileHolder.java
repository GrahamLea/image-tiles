package org.grlea.imageTiles;

// $Id: TileHolder.java,v 1.1 2004-09-04 07:59:19 grlea Exp $
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

import org.grlea.imageTiles.TileImage;
import org.grlea.imageTiles.Tile;
import org.grlea.imageTiles.TileHolderListener;
import org.grlea.imageTiles.TileSpace;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * <p>Holds Tiles that have been moved into position by a Transition.</p>
 *
 * @see TileHolderRenderer
 *
 * @author grlea
 * @version $Revision: 1.1 $
 */
public class
TileHolder
{
   private final TileSpace tileSpace;

   private final TileImage[][] tileImages;

   private final List listeners;

   public
   TileHolder(TileSpace tileSpace)
   {
      listeners = new ArrayList(2);
      this.tileSpace = tileSpace;
      this.tileImages = new TileImage[tileSpace.getRows()][tileSpace.getColumns()];
   }

   public void
   addListener(TileHolderListener listener)
   {
      listeners.add(listener);
   }

   public void
   removeListener(TileHolderListener listener)
   {
      listeners.remove(listener);
   }

   public void
   addTile(TileImage tileImage)
   {
      Tile tile = tileImage.getTile();
      tileImages[tile.row][tile.column] = tileImage;
      notifyTileAdded(tileImage);
   }

   public void
   removeAllTiles()
   {
      for (int row = 0; row < tileImages.length; row++)
      {
         TileImage[] tileImagesRow = tileImages[row];
         for (int column = 0; column < tileImagesRow.length; column++)
         {
            tileImagesRow[column] = null;
         }
      }

      notifyAllTilesRemoved();
   }

   private void
   notifyTileAdded(TileImage tileImage)
   {
      for (Iterator iter = listeners.iterator(); iter.hasNext();)
      {
         ((TileHolderListener) iter.next()).tileAdded(this, tileImage);
      }
   }

   private void
   notifyTileRemoved(TileImage tileImage)
   {
      for (Iterator iter = listeners.iterator(); iter.hasNext();)
      {
         ((TileHolderListener) iter.next()).tileRemoved(this, tileImage);
      }
   }

   private void
   notifyAllTilesRemoved()
   {
      for (Iterator iter = listeners.iterator(); iter.hasNext();)
      {
         ((TileHolderListener) iter.next()).allTilesRemoved(this);
      }
   }

   public TileSpace
   getTileSpace()
   {
      return tileSpace;
   }

   public TileImage[][]
   getTiles()
   {
      // TODO (grahaml) This is crap!
      return tileImages;
   }
}