package org.grlea.imageTiles.transition;

// $Id: BasicTransition.java,v 1.1 2004-09-04 07:59:32 grlea Exp $
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
import org.grlea.imageTiles.TileHolder;
import org.grlea.imageTiles.TileImage;
import org.grlea.imageTiles.TileSet;
import org.grlea.imageTiles.Transition;

import java.awt.Graphics2D;

/**
 * <p>Very basic Transition that just moves one Tile into the TileHolder on each frame. Demonstrates
 * the required TileHolder and TransitionListener behaviour.</p>
 *
 * @author grlea
 * @version $Revision: 1.1 $
 */
public class 
BasicTransition
extends AbstractTransition
implements Transition
{
   private TileImage nextTile = null;

   private TileSet tileSet;

   private Chooser chooser;

   private TileHolder tileHolder;

   public
   BasicTransition()
   {}

   public void
   newTransition(TileSet oldTileSet, TileSet newTileSet, Chooser chooser, TileHolder tileHolder)
   {
      this.nextTile = null;
      this.tileSet = newTileSet;
      this.chooser = chooser;
      this.tileHolder = tileHolder;
      tileHolder.removeAllTiles();
   }

   public void
   advanceFrame()
   {
      if (chooser.hasMoreTiles())
      {
         Tile nextTile = chooser.getNextTile();
         TileImage tileImage = tileSet.get(nextTile);
         tileHolder.addTile(tileImage);
      }
      else
      {
         notifyTransitionComplete();
      }
   }

   public void
   render(Graphics2D graphics)
   {
      if (nextTile != null)
         nextTile.paint(graphics);
   }
}