package org.grlea.imageTiles.animate;

// $Id: SlideAnimator.java,v 1.2 2004-08-27 01:08:10 grlea Exp $
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
import org.grlea.imageTiles.Tile;
import org.grlea.imageTiles.TileSpace;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * <p></p>
 *
 * @author grlea
 * @version $Revision: 1.2 $
 */
public class
SlideAnimator
extends AbstractAnimator
{
   private static final int DEFAULT_ANIMATED_TILES = 5;

   private static final int DEFAULT_SPEED = 7;

   private final Heading NORTH = new Heading(false, true);
   private final Heading SOUTH = new Heading(false, false);
   private final Heading EAST = new Heading(true, false);
   private final Heading WEST = new Heading(true, true);

   private final TileSpace tileSpace;

   private final int animatedTilesCount;

   private final int speed;

   private final List tileSliders;

   private final Random random;

   public
   SlideAnimator(TileSpace tileSpace)
   {
      this(tileSpace, DEFAULT_ANIMATED_TILES, DEFAULT_SPEED);
   }

   public
   SlideAnimator(TileSpace tileSpace, int animatedTilesCount, int speed)
   {
      this.tileSpace = tileSpace;
      this.animatedTilesCount = animatedTilesCount;
      this.speed = speed;
      tileSliders = new ArrayList(animatedTilesCount);
      random = new Random(System.currentTimeMillis());
   }

   public void
   advanceFrame(RenderedTileSource tileSource)
   {
      for (Iterator iter = tileSliders.iterator(); iter.hasNext();)
      {
         TileSlider tileSlider = (TileSlider) iter.next();
         if (tileSlider.hasArrived())
         {
            iter.remove();
            notifyTileAnimationComplete(tileSlider.renderedTile);
         }
      }

      if (!tileSource.hasMoreTiles() && tileSliders.isEmpty())
      {
         notifyAnimationComplete();
         return;
      }

      while (tileSliders.size() < animatedTilesCount && tileSource.hasMoreTiles())
      {
         tileSliders.add(new TileSlider(tileSource.getNextTile(), getRandomHeading()));
      }

      for (Iterator iter = tileSliders.iterator(); iter.hasNext();)
      {
         ((TileSlider) iter.next()).tick();
      }
   }

   private Heading
   getRandomHeading()
   {
      int randomNumber = random.nextInt(4);
      switch (randomNumber)
      {
         case 0:  return NORTH;
         case 1:  return SOUTH;
         case 2:  return EAST;
         case 3:  return WEST;
         default: throw new IllegalStateException("random.nextInt(4) returned: " + randomNumber);
      }
   }

   public void
   render(Graphics2D graphics)
   {
      for (Iterator iter = tileSliders.iterator(); iter.hasNext();)
      {
         ((TileSlider) iter.next()).paint(graphics);
      }
   }

   public void
   reset()
   {
      tileSliders.clear();
   }

   private final class
   Heading
   {
      private final boolean horizontal;

      private final boolean negative;

      private
      Heading(boolean horizontal, boolean negative)
      {
         this.horizontal = horizontal;
         this.negative = negative;
      }

      public Point
      getInitialOffset(RenderedTile renderedTile)
      {
         Tile tile = renderedTile.getTile();
         final int xOffset;
         final int yOffset;

         Dimension tileSpaceSize = tileSpace.getSize();

         if (horizontal)
         {
            yOffset = 0;
            int tileX = tile.getX();

            if (!negative)
               xOffset = -(tileX + (speed - (tileX % speed)));
            else
            {
               int distanceToEdge = tileSpaceSize.width - tileX;
               xOffset = distanceToEdge + (speed - (distanceToEdge % speed));
            }
         }
         else
         {
            xOffset = 0;
            int tileY = tile.getY();

            if (!negative)
               yOffset = -(tileY + (speed - (tileY % speed)));
            else
            {
               int distanceToEdge = tileSpaceSize.height - tileY;
               yOffset = distanceToEdge + (speed - (distanceToEdge % speed));
            }
         }

         return new Point(xOffset, yOffset);
      }

      public void
      decrease(Point offset)
      {
         if (horizontal)
            offset.x += (negative ? -speed : speed);
         else
            offset.y += (negative ? -speed : speed);
      }
   }

   private final class
   TileSlider
   {
      private final RenderedTile renderedTile;

      private final Heading heading;

      private final Point offset;

      public
      TileSlider(RenderedTile tile, Heading heading)
      {
         this.renderedTile = tile;
         offset = heading.getInitialOffset(tile);
         this.heading = heading;
      }

      public boolean
      hasArrived()
      {
         return offset.x == 0 && offset.y == 0;
      }

      public void
      tick()
      {
         heading.decrease(offset);
      }

      public void
      paint(Graphics2D graphics)
      {
         renderedTile.paint(graphics, offset);
      }
   }
}