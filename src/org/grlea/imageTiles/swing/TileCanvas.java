package org.grlea.imageTiles.swing;

// $Id: TileCanvas.java,v 1.1 2004-08-23 05:01:47 grlea Exp $
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

import org.grlea.imageTiles.TileSpace;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Color;

import javax.swing.JComponent;

/**
 * <p></p>
 *
 * @author grlea
 * @version $Revision: 1.1 $
 */
public abstract class
TileCanvas
extends JComponent
{
   private static final Color DEFAULT_BACKGROUND_COLOR = Color.black;

   private final TileSpace tileSpace;

   private boolean buffersInitialised = false;

   private Image bufferImage;

   private Graphics2D bufferGraphics;

   private Graphics2D graphics;

   public
   TileCanvas(TileSpace tileSpace)
   {
      this.tileSpace = tileSpace;
      setBackground(DEFAULT_BACKGROUND_COLOR);
   }

   public Dimension
   getPreferredSize()
   {
      return tileSpace.getSize();
   }

   public final void
   render()
   {
      render(graphics);
   }

   public final void
   render(Graphics2D targetGraphics)
   {
      Dimension tileSetSize = tileSpace.getSize();

      if (!buffersInitialised)
      {
         buffersInitialised = true;
         this.bufferImage = createImage(tileSetSize.width, tileSetSize.height);
         this.bufferGraphics = (Graphics2D) bufferImage.getGraphics();
         this.graphics = (Graphics2D) getGraphics();
      }

      // Handles the case where render() is called before the buffers are initialised.
      if (targetGraphics == null)
         targetGraphics = graphics;

      if (isOpaque())
      {
         bufferGraphics.setColor(getBackground());
         bufferGraphics.fillRect(0, 0, getWidth(), getHeight());
      }
      else
      {
         bufferGraphics.clearRect(0, 0, getWidth(), getHeight());
      }
      paintTiles(bufferGraphics);

      targetGraphics.drawImage(bufferImage, 0, 0, null);
   }

   protected abstract void
   paintTiles(Graphics2D graphics2D);
}