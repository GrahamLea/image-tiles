package org.grlea.imageTiles.swing;

// $Id: RenderImageTilesFrameTask.java,v 1.1 2004-08-23 22:47:57 grlea Exp $
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

import org.grlea.imageTiles.AnimationController;
import org.grlea.imageTiles.TileSpace;

import java.awt.Graphics2D;
import java.awt.Dimension;
import java.awt.image.BufferedImage;

/**
 * <p></p>
 *
 * @author grlea
 * @version $Revision: 1.1 $
 */
public class 
RenderImageTilesFrameTask
implements Runnable
{
   private final Dimension tileSetSize;

   private final AnimationController controller;

   private final Graphics2D graphics;

   private final BufferedImage bufferImage;

   private final Graphics2D bufferGraphics;

   public
   RenderImageTilesFrameTask(TileSpace tileSpace, AnimationController controller, Graphics2D graphics)
   {
      this.controller = controller;
      this.graphics = graphics;

      tileSetSize = tileSpace.getSize();
      this.bufferImage =
         new BufferedImage(tileSetSize.width, tileSetSize.height, BufferedImage.TYPE_INT_ARGB);
      this.bufferGraphics = (Graphics2D) bufferImage.getGraphics();
   }

   public void
   run()
   {
      controller.advanceFrame();
      bufferGraphics.clearRect(0, 0, tileSetSize.width, tileSetSize.height);
      controller.paint(bufferGraphics);
      graphics.drawImage(bufferImage, 0, 0, null);
   }
}