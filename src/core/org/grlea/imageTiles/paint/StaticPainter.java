package org.grlea.imageTiles.paint;

// $Id: StaticPainter.java,v 1.1 2004-08-23 22:47:41 grlea Exp $
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
import org.grlea.imageTiles.Tile;
import org.grlea.imageTiles.TileSpace;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * <p></p>
 *
 * @author grlea
 * @version $Revision: 1.1 $
 */
public class 
StaticPainter
extends AbstractPainter
{
   private final TileSpace tileSpace;

   private final RenderedTile[][] renderedTiles;

   private final BufferedImage image;

   private final Graphics2D graphics;

   public
   StaticPainter(TileSpace tileSpace)
   {
      this.tileSpace = tileSpace;
      this.renderedTiles = new RenderedTile[tileSpace.getRows()][tileSpace.getColumns()];
      Dimension size = tileSpace.getSize();
      this.image = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
      this.graphics = image.createGraphics();
   }

   public void
   addTile(RenderedTile renderedTile)
   {
      Tile tile = renderedTile.getTile();
      renderedTiles[tile.getRow()][tile.getColumn()] = renderedTile;
      renderedTile.paint(graphics);
   }

   public void
   tick()
   {}

   public void
   paint(Graphics2D graphics)
   {
      graphics.drawImage(image, 0, 0, null);
   }

   public TileSpace
   getTileSpace()
   {
      return tileSpace;
   }
}