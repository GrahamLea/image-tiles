package org.grlea.imageTiles.background;

// $Id: LastTileSetBackgroundPainter.java,v 1.1 2004-09-04 07:59:22 grlea Exp $
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

import org.grlea.imageTiles.BackgroundPainter;
import org.grlea.imageTiles.TileSet;
import org.grlea.imageTiles.TileSpace;
import org.grlea.imageTiles.pipeline.Pipeline;
import org.grlea.imageTiles.pipeline.PipelineTransitionListener;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * <p>Paints a background that is the final image (post-transition) of the previous TileSet. In
 * order to function correctly, this <code>LastTileSetBackgroundPainter</code> needs to be added to
 * the {@link Pipeline} as a {@link PipelineTransitionListener}.</p>
 *
 * @author grlea
 * @version $Revision: 1.1 $
 */
public class
LastTileSetBackgroundPainter
implements BackgroundPainter, PipelineTransitionListener
{
   private BufferedImage bufferImage;

   public
   LastTileSetBackgroundPainter()
   {}

   public void
   newTransition(Pipeline pipeline, TileSet oldTileSet, TileSet newTileSet)
   {
      TileSpace tileSpace = newTileSet.getTileSpace();
      if (bufferImage == null)
         bufferImage = new BufferedImage(tileSpace.getWidth(), tileSpace.getHeight(),
                                         BufferedImage.TYPE_INT_ARGB);

      Graphics2D bufferGraphics = bufferImage.createGraphics();
      bufferGraphics.clearRect(0, 0, tileSpace.getWidth(), tileSpace.getHeight());
      pipeline.render(bufferGraphics);
      bufferGraphics.dispose();
      bufferGraphics = null;
   }
   public void
   paintBackground(Graphics2D graphics)
   {
      if (bufferImage != null)
      {
         graphics.drawImage(bufferImage, 0, 0, null);
      }
   }
}