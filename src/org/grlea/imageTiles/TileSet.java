package org.grlea.imageTiles;

// $Id: TileSet.java,v 1.2 2004-08-23 22:32:45 grlea Exp $
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

/**
 * <p></p>
 *
 * @author grlea
 * @version $Revision: 1.2 $
 */
public class
TileSet
{
   private final TileSpace tileSpace;

   private final BufferedImage image;

   private final Point imagePosition;

   private final TileRenderer renderer;

   // TODO (grahaml) Add "paintEmptyTiles" parameter ?
   // TODO (grahaml) Remove imagePosition in place of a Placer.
   public
   TileSet(TileSpace tileSpace, BufferedImage image, Point imagePosition, TileRenderer renderer)
   {
      this.tileSpace = tileSpace;
      this.image = image;
      this.imagePosition = imagePosition;
      this.renderer = renderer;
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

   public void
   paintTileImage(Tile tile, Graphics2D graphics)
   {
      graphics.drawImage(image, imagePosition.x - tile.getX(), imagePosition.y - tile.getY(), null);
   }

   public RenderedTile
   render(Tile tile)
   {
      // TODO (grahaml) Cache rendered tiles in here?
      return renderer.render(this, tile);
   }
}