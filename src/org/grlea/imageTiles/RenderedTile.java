package org.grlea.imageTiles;

// $Id: RenderedTile.java,v 1.1 2004-08-20 05:25:36 grlea Exp $
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
 * <p></p>
 *
 * @author grlea
 * @version $Revision: 1.1 $
 */
public class 
RenderedTile
{
   private final Tile tile;

   private final BufferedImage image;

   public
   RenderedTile(Tile tile, BufferedImage image)
   {
      this.tile = tile;
      this.image = image;
   }

   public Tile
   getTile()
   {
      return tile;
   }

   public BufferedImage
   getImage()
   {
      return image;
   }

   public void
   paint(Graphics2D graphics)
   {
      graphics.drawImage(image, tile.getX(), tile.getY(), null);
   }

   public void
   paint(Graphics2D graphics, Point offset)
   {
      graphics.drawImage(image, tile.getX() + offset.x, tile.getY() + offset.y, null);
   }
}