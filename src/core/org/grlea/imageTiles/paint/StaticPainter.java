package org.grlea.imageTiles.paint;

// $Id: StaticPainter.java,v 1.3 2004-08-29 22:25:02 grlea Exp $
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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.image.VolatileImage;

/**
 * <p></p>
 *
 * @author grlea
 * @version $Revision: 1.3 $
 */
public class 
StaticPainter
extends AbstractPainter
{
   private final TileSpace tileSpace;

   private final RenderedTile[][] renderedTiles;

   private VolatileImage bufferImage = null;

   private Graphics2D bufferGraphics = null;

   public
   StaticPainter(TileSpace tileSpace)
   {
      this.tileSpace = tileSpace;
      this.renderedTiles = new RenderedTile[tileSpace.getRows()][tileSpace.getColumns()];
   }

   public void
   addTile(RenderedTile renderedTile)
   {
      Tile tile = renderedTile.getTile();
      renderedTiles[tile.getRow()][tile.getColumn()] = renderedTile;
      if (bufferGraphics != null)
         renderedTile.paint(bufferGraphics);
   }

   public void
   removeAllTiles()
   {
      for (int row = 0; row < renderedTiles.length; row++)
      {
         RenderedTile[] renderedTileRow = renderedTiles[row];
         for (int column = 0; column < renderedTileRow.length; column++)
         {
            renderedTileRow[column] = null;
         }
      }
      if (bufferGraphics != null)
      {
         bufferGraphics.setBackground(new Color(0, 0, 0, 0));
         bufferGraphics.clearRect(0, 0, tileSpace.getWidth(), tileSpace.getHeight());
      }
   }

   public void
   advanceFrame()
   {}

   public void
   render(Graphics2D graphics)
   {
      if (bufferImage == null)
         createBuffer(graphics);

      do
      {
         int validateCode = bufferImage.validate(graphics.getDeviceConfiguration());
         switch (validateCode)
         {
            case VolatileImage.IMAGE_INCOMPATIBLE:
               createBuffer(graphics);
               // Falls through
            case VolatileImage.IMAGE_RESTORED:
               internalRender();
               // Falls through
            case VolatileImage.IMAGE_OK:
               graphics.drawImage(bufferImage, 0, 0, null);
         }
      }
      while (bufferImage.contentsLost());
   }

   private void
   createBuffer(Graphics2D graphics)
   {
      if (bufferImage != null)
      {
         bufferGraphics.dispose();
         bufferGraphics = null;
         bufferImage.flush();
         bufferImage = null;
      }
      GraphicsConfiguration device = graphics.getDeviceConfiguration();
      bufferImage = device.createCompatibleVolatileImage(tileSpace.getWidth(), tileSpace.getHeight());
      bufferGraphics = bufferImage.createGraphics();
   }

   private void
   internalRender()
   {
      bufferGraphics.setBackground(new Color(0, 0, 0, 0));
      bufferGraphics.clearRect(0, 0, tileSpace.getWidth(), tileSpace.getHeight());

      for (int row = 0; row < renderedTiles.length; row++)
      {
         RenderedTile[] renderedTilesRow = renderedTiles[row];
         for (int column = 0; column < renderedTilesRow.length; column++)
         {
            RenderedTile renderedTile = renderedTilesRow[column];
            if (renderedTile != null)
               renderedTile.paint(bufferGraphics);
         }
      }
   }

   public TileSpace
   getTileSpace()
   {
      return tileSpace;
   }
}