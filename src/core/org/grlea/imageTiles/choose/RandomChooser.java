package org.grlea.imageTiles.choose;

// $Id: RandomChooser.java,v 1.2 2004-08-27 01:15:33 grlea Exp $
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

import org.grlea.imageTiles.Chooser;
import org.grlea.imageTiles.Tile;
import org.grlea.imageTiles.TileSpace;

import java.util.ArrayList;
import java.util.Random;

/**
 * <p></p>
 *
 * @author grlea
 * @version $Revision: 1.2 $
 */
public class 
RandomChooser
implements Chooser
{
   private final TileSpace tileSpace;

   private final ArrayList tiles;

   private final Random random = new Random(System.currentTimeMillis());

   public
   RandomChooser(TileSpace tileSpace)
   {
      this.tileSpace = tileSpace;
      tiles = new ArrayList(tileSpace.getColumns() * tileSpace.getRows());
      reset();
   }

   public boolean
   hasMoreTiles()
   {
      return !tiles.isEmpty();
   }

   public Tile
   getNextTile()
   {
      return (Tile) tiles.remove(random.nextInt(tiles.size()));
   }

   public void
   reset()
   {
      tiles.clear();
      tileSpace.getAllTiles(tiles);
   }
}