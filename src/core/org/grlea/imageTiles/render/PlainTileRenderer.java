package org.grlea.imageTiles.render;

// $Id: PlainTileRenderer.java,v 1.2 2004-09-04 07:59:29 grlea Exp $
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

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * <p>Renders Tiles as just their section of the source image.</p>
 *
 * @author grlea
 * @version $Revision: 1.2 $
 */
public class
PlainTileRenderer
implements TileRenderer
{
   public
   PlainTileRenderer()
   {}

   public TileImage
   render(TileSet tileSet, Tile tile)
   {
      int tileSize = tileSet.getTileSpace().getTileSize();
      BufferedImage image = new BufferedImage(tileSize, tileSize, BufferedImage.TYPE_INT_ARGB);
      Graphics2D graphics = image.createGraphics();
      tileSet.paintTileImage(tile, graphics);
      graphics.dispose();
      return new TileImage(tile, image);
   }
}