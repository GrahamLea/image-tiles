package org.grlea.imageTiles.tileHolder;

// $Id: StaticTileHolderRenderer.java,v 1.2 2005-03-19 00:11:37 grlea Exp $
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

import org.grlea.imageTiles.TileHolderRenderer;
import org.grlea.imageTiles.TileImage;
import org.grlea.imageTiles.TileSpace;
import org.grlea.imageTiles.TileHolder;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * <p>Simply draws Tiles in their natural position.</p>
 *
 * @author grlea
 * @version $Revision: 1.2 $
 */
public class 
StaticTileHolderRenderer
implements TileHolderRenderer
{
   private BufferedImage bufferImage = null;

//   private VolatileImage bufferImage = null;

   private Graphics2D bufferGraphics = null;

   private final TileSpace tileSpace;

   public
   StaticTileHolderRenderer(TileSpace tileSpace)
   {
      this.tileSpace = tileSpace;
   }

   public void
   tileAdded(TileHolder tileHolder, TileImage tileImage)
   {
      if (bufferGraphics != null)
         tileImage.paint(bufferGraphics);
   }

   public void
   tileRemoved(TileHolder tileHolder, TileImage tileImage)
   {
      if (bufferGraphics != null)
      {
         internalRender(tileHolder);
      }
   }

   public void
   allTilesRemoved(TileHolder tileHolder)
   {
      if (bufferGraphics != null)
      {
         bufferGraphics.setBackground(new Color(0, 0, 0, 0));
         bufferGraphics.clearRect(0, 0, tileSpace.getWidth(), tileSpace.getHeight());
      }
   }

   public void
   advanceFrame(long timeSinceLastFrame)
   {}

   public void
   render(TileHolder tileHolder, Graphics2D graphics)
   {
      // For VolatileImage or BufferedImage:
      if (bufferImage == null)
      {
         createBuffer(graphics);
         internalRender(tileHolder);
      }

      // BufferedImage version:
      graphics.drawImage(bufferImage, 0, 0, null);

      // VolatileImage version:
//      do
//      {
//         int validateCode = bufferImage.validate(graphics.getDeviceConfiguration());
//         switch (validateCode)
//         {
//            case VolatileImage.IMAGE_INCOMPATIBLE:
//               createBuffer(graphics);
//               // Falls through
//            case VolatileImage.IMAGE_RESTORED:
//               internalRender();
//               // Falls through
//            case VolatileImage.IMAGE_OK:
//               graphics.drawImage(bufferImage, 0, 0, null);
//         }
//      }
//      while (bufferImage.contentsLost());
   }

   private void
   createBuffer(Graphics2D graphics)
   {
      // BufferedImage version:
      bufferImage =
         new BufferedImage(tileSpace.getWidth(), tileSpace.getHeight(), BufferedImage.TYPE_INT_ARGB);
      bufferGraphics = bufferImage.createGraphics();

      // VolatileImage version:
//      if (bufferImage != null)
//      {
//         bufferGraphics.dispose();
//         bufferGraphics = null;
//         bufferImage.flush();
//         bufferImage = null;
//      }
//      GraphicsConfiguration device = graphics.getDeviceConfiguration();
//      bufferImage = device.createCompatibleVolatileImage(tileSpace.getWidth(), tileSpace.getHeight());
//      bufferGraphics = bufferImage.createGraphics();
   }

   // Used when a new buffer is created or a Volatile buffer gets restored:
   private void
   internalRender(TileHolder tileHolder)
   {
      bufferGraphics.setBackground(new Color(0, 0, 0, 0));
      bufferGraphics.clearRect(0, 0, tileSpace.getWidth(), tileSpace.getHeight());

      TileImage[][] tileImages = tileHolder.getTiles();

      for (int row = 0; row < tileImages.length; row++)
      {
         TileImage[] tileImagesRow = tileImages[row];
         for (int column = 0; column < tileImagesRow.length; column++)
         {
            TileImage tileImage = tileImagesRow[column];
            if (tileImage != null)
               tileImage.paint(bufferGraphics);
         }
      }
   }

   public TileSpace
   getTileSpace()
   {
      return tileSpace;
   }
}