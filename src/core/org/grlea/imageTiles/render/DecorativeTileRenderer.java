package org.grlea.imageTiles.render;

// $Id: DecorativeTileRenderer.java,v 1.3 2005-03-31 21:23:11 grlea Exp $
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

import org.grlea.imageTiles.TileImage;
import org.grlea.imageTiles.Tile;
import org.grlea.imageTiles.TileRenderer;
import org.grlea.imageTiles.TileSet;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * <p>A Renderer that allows multiple {@link Decorator}s to decorate the source image of the
 * Tile.</p>
 *
 * @author grlea
 * @version $Revision: 1.3 $
 */
public class
DecorativeTileRenderer
implements TileRenderer
{
   /** Almost black. */
   private static final Color DEFAULT_BACKGROUND_COLOUR = new Color(16, 16, 16);

   private final Color tileBackgroundColour;

   private final Decorator[] tileDecorators;

   public
   DecorativeTileRenderer(Decorator[] tileDecorators)
   {
      this(DEFAULT_BACKGROUND_COLOUR, tileDecorators);
   }

   public
   DecorativeTileRenderer(Color tileBackgroundColour, Decorator[] tileDecorators)
   {
      this.tileBackgroundColour = tileBackgroundColour;
      this.tileDecorators = tileDecorators;
   }

   public TileImage
   render(TileSet tileSet, Tile tile)
   {
      int tileSize = tileSet.getTileSpace().getTileSize();
      BufferedImage image = new BufferedImage(tileSize, tileSize, BufferedImage.TYPE_INT_ARGB);
      Graphics2D graphics = image.createGraphics();

      graphics.setColor(tileBackgroundColour);
      graphics.fillRect(0, 0, tileSize, tileSize);

      tileSet.paintTileImage(tile, graphics);
      graphics.dispose();

      for (int i = 0; i < tileDecorators.length; i++)
      {
         tileDecorators[i].decorate(image);
      }

      return new TileImage(tile, image);
   }
}