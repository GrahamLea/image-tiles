package org.grlea.imageTiles;

// $Id: TileSet.java,v 1.2 2004-09-04 07:59:20 grlea Exp $
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
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>A set of {@link TileImage}s, created <i>from</i> a source image, <i>for</i> a TileSpace and
 * <i>by</i> a TileRenderer.</p>
 *
 * @author grlea
 * @version $Revision: 1.2 $
 */
public class
TileSet
{
   private final TileSpace tileSpace;

   private final BufferedImage sourceImage;

   private final TileRenderer renderer;

   private final Map tileImages;

   // TODO (grahaml) Add "paintEmptyTiles" parameter ?
   public
   TileSet(TileSpace tileSpace, BufferedImage sourceImage, TileRenderer renderer)
   {
      this.tileSpace = tileSpace;
      this.sourceImage = sourceImage;
      this.renderer = renderer;
      float loadFactor = 0.9F;
      tileImages = new HashMap((int) (tileSpace.getTileCount() / loadFactor), loadFactor);
   }

   public TileSpace
   getTileSpace()
   {
      return tileSpace;
   }

   public Dimension
   getSize()
   {
      // TODO (grahaml) This will change if we don't paint empty tiles
      return tileSpace.getSize();
   }

   public Point
   getOffset()
   {
      // TODO (grahaml) This will change if we don't paint empty tiles
      return new Point(0, 0);
   }

   /**
    * Paints into the given graphics object the portion of the source image that belongs on the
    * given tile.
    *
    * @param tile
    * @param graphics
    */
   public void
   paintTileImage(Tile tile, Graphics2D graphics)
   {
      graphics.drawImage(sourceImage, -tile.x, -tile.y, null);
   }

   public TileImage
   get(Tile tile)
   {
      TileImage tileImage = (TileImage) tileImages.get(tile);
      if (tileImage == null)
      {
         tileImage = renderer.render(this, tile);
         tileImages.put(tile, tileImage);
      }
      return tileImage;
   }
}