package org.grlea.imageTiles.animate;

// $Id: BasicAnimator.java,v 1.2 2004-08-27 01:08:09 grlea Exp $
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

import org.grlea.imageTiles.RenderedTile;
import org.grlea.imageTiles.RenderedTileSource;

import java.awt.Graphics2D;

/**
 * <p></p>
 *
 * @author grlea
 * @version $Revision: 1.2 $
 */
public class 
BasicAnimator
extends AbstractAnimator
{
   private RenderedTile nextTile = null;

   public
   BasicAnimator()
   {}

   public void
   advanceFrame(RenderedTileSource tileSource)
   {
      if (tileSource.hasMoreTiles())
      {
         nextTile = tileSource.getNextTile();
         notifyTileAnimationComplete(nextTile);
      }
      else
      {
         notifyAnimationComplete();
      }
   }

   public void
   render(Graphics2D graphics)
   {
      if (nextTile != null)
         nextTile.paint(graphics);
   }

   public void
   reset()
   {
      nextTile = null;
   }
}