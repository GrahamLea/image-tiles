package org.grlea.imageTiles;

// $Id: TileImage.java,v 1.1 2004-09-04 07:59:20 grlea Exp $
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

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;

/**
 * <p>Contains the image for a particular {@link Tile} in a particular {@link TileSet}, as created
 * by that <code>TileSet</code>'s {@link TileRenderer}.</p>
 *
 * @author grlea
 * @version $Revision: 1.1 $
 */
public class 
TileImage
{
   private final Tile tile;

   private final BufferedImage image;

   public
   TileImage(Tile tile, BufferedImage image)
   {
      this.tile = tile;
      this.image = image;
   }

   public Tile
   getTile()
   {
      return tile;
   }

   public void
   paint(Graphics2D graphics)
   {
      graphics.drawImage(image, tile.x, tile.y, null);
   }

   public void
   paint(Graphics2D graphics, Point offset)
   {
      graphics.drawImage(image, tile.x + offset.x, tile.y + offset.y, null);
   }

   public void
   paintAtOrigin(Graphics2D graphics)
   {
      graphics.drawImage(image, 0, 0, null);
   }
}