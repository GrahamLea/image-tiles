package org.grlea.imageTiles.swing;

// $Id: CanvasPipelineRenderer.java,v 1.1 2004-09-04 07:59:37 grlea Exp $
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

import org.grlea.imageTiles.pipeline.Pipeline;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;

/**
 * <p>Renders a Pipeline to a Canvas using a BufferStrategy.</p>
 *
 * @see Pipeline
 * @see AnimatedTileCanvas
 * @see BufferStrategy
 *
 * @author grlea
 * @version $Revision: 1.1 $
 */
public class 
CanvasPipelineRenderer
{
   protected final Canvas canvas;

   private BufferStrategy bufferStrategy = null;

   public
   CanvasPipelineRenderer(Canvas canvas)
   {
      this.canvas = canvas;
   }

   public void
   render(Pipeline pipeline)
   {
      if (bufferStrategy == null)
      {
         canvas.createBufferStrategy(2);
         bufferStrategy = canvas.getBufferStrategy();
      }

      Graphics graphics = bufferStrategy.getDrawGraphics();
      pipeline.render((Graphics2D) graphics);
      graphics.dispose();
      graphics = null;
      bufferStrategy.show();
   }
}