package org.grlea.imageTiles.transition;

// $Id: SlideTransition.java,v 1.2 2005-03-19 00:11:38 grlea Exp $
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
import org.grlea.imageTiles.TileImage;
import org.grlea.imageTiles.Tile;
import org.grlea.imageTiles.TileSet;
import org.grlea.imageTiles.TileHolder;
import org.grlea.imageTiles.Transition;
import org.grlea.imageTiles.transition.AbstractTransition;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * <p>A Transition that slides Tiles, from a random direction, toward their final position.</p>
 *
 * @author grlea
 * @version $Revision: 1.2 $
 */
public class
SlideTransition
extends AbstractTransition
implements Transition
{
   private static final int DEFAULT_ANIMATED_TILES = 5;

   private static final int DEFAULT_SPEED = 500;

   private final Heading NORTH = new Heading(false, true);
   private final Heading SOUTH = new Heading(false, false);
   private final Heading EAST = new Heading(true, false);
   private final Heading WEST = new Heading(true, true);

   private TileSet tileSet;

   private Chooser chooser;

   private final int animatedTilesCount;

   private final int speed;

   private final List tileSliders;

   private final Random random;

   private TileHolder tileHolder;

   private long unusedTimeFromPreviousFrames = 0;

   public
   SlideTransition()
   {
      this(DEFAULT_ANIMATED_TILES, DEFAULT_SPEED);
   }

   public
   SlideTransition(int animatedTilesCount)
   {
      this(animatedTilesCount, DEFAULT_SPEED);
   }

   public
   SlideTransition(int animatedTilesCount, int speed)
   {
      this.animatedTilesCount = animatedTilesCount;
      this.speed = speed;
      tileSliders = new ArrayList(animatedTilesCount);
      random = new Random(System.currentTimeMillis());
   }

   public void
   newTransition(TileSet oldTileSet, TileSet newTileSet, Chooser chooser, TileHolder tileHolder)
   {
      tileSliders.clear();
      this.tileSet = newTileSet;
      this.chooser = chooser;
      this.tileHolder = tileHolder;
      tileHolder.removeAllTiles();
      unusedTimeFromPreviousFrames = 0;
   }

   public void
   advanceFrame(long timeSinceLastFrame)
   {
      // Check for tiles that have arrived.
      for (Iterator iter = tileSliders.iterator(); iter.hasNext();)
      {
         TileSlider tileSlider = (TileSlider) iter.next();
         if (tileSlider.hasArrived())
         {
            iter.remove();
            tileHolder.addTile(tileSlider.tileImage);
         }
      }

      // Check if there are any tiles still in transition.
      if (!chooser.hasMoreTiles() && tileSliders.isEmpty())
      {
         notifyTransitionComplete();
         return;
      }

      // Get new tiles to transition if necessary & possible.
      int newTiles = 0;
      int maxNewTiles = (animatedTilesCount / 10) + 1;
      while (tileSliders.size() < animatedTilesCount && chooser.hasMoreTiles() &&
             newTiles < maxNewTiles)
      {
         Tile nextTile = chooser.getNextTile();
         TileImage tileImage = tileSet.get(nextTile);
         tileSliders.add(new TileSlider(tileImage, getRandomHeading()));
         newTiles++;
      }

      // Advance the TileSliders.
      long timeElapsed = timeSinceLastFrame + unusedTimeFromPreviousFrames;
      long timePerPixel = 1000 / speed;
      int pixelsToAdvance = (int) (timeElapsed / timePerPixel);
      unusedTimeFromPreviousFrames = timeElapsed % timePerPixel;
      for (Iterator iter = tileSliders.iterator(); iter.hasNext();)
      {
         ((TileSlider) iter.next()).advance(pixelsToAdvance);
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
      getInitialOffset(TileImage tileImage)
      {
         Tile tile = tileImage.getTile();
         final int xOffset;
         final int yOffset;

         Dimension tileSpaceSize = tileSet.getTileSpace().getSize();

         if (horizontal)
         {
            yOffset = 0;
            int tileX = tile.x;

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
            int tileY = tile.y;

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
      decrease(Point offset, int pixels)
      {
         if (horizontal)
         {
            if (Math.abs(offset.x) < pixels)
               pixels = Math.abs(offset.x);
            offset.x += (negative ? -pixels : pixels);
         }
         else
         {
            if (Math.abs(offset.y) < pixels)
               pixels = Math.abs(offset.y);
            offset.y += (negative ? -pixels : pixels);
         }
      }
   }

   private final class
   TileSlider
   {
      private final TileImage tileImage;

      private final Heading heading;

      private final Point offset;

      public
      TileSlider(TileImage tile, Heading heading)
      {
         this.tileImage = tile;
         offset = heading.getInitialOffset(tile);
         this.heading = heading;
      }

      public boolean
      hasArrived()
      {
         return offset.x == 0 && offset.y == 0;
      }

      public void
      advance(int pixels)
      {
         heading.decrease(offset, pixels);
      }

      public void
      paint(Graphics2D graphics)
      {
         tileImage.paint(graphics, offset);
      }
   }
}