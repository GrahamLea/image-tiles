package org.grlea.imageTiles.swing;

// $Id: ComponentPipelineRenderer.java,v 1.2 2005-04-01 02:29:40 grlea Exp $
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
import org.grlea.imageTiles.pipeline.Pipeline;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.image.VolatileImage;

/**
 * <p>Renders a Pipeline to any Component using a VolatileImage as a back buffer.</p>
 *
 * @see Pipeline
 * @see AnimatedTileComponent
 * @see VolatileImage
 *
 * @author grlea
 * @version $Revision: 1.2 $
 */
public class 
ComponentPipelineRenderer
{
   private VolatileImage bufferImage = null;

   public
   ComponentPipelineRenderer()
   {}

   public void
   render(Pipeline pipeline, Component component, Graphics2D targetGraphics)
   {
      if (bufferImage == null)
         createBuffer(component);

      do
      {
         // VolatileImage handling code courtesy of the legendary Chet Haase
         int validateCode = bufferImage.validate(component.getGraphicsConfiguration());
         if (validateCode == VolatileImage.IMAGE_INCOMPATIBLE)
            createBuffer(component);

         Graphics2D bufferGraphics = bufferImage.createGraphics();
         pipeline.render(bufferGraphics, component);
         bufferGraphics.dispose();
         bufferGraphics = null;

         targetGraphics.drawImage(bufferImage, 0, 0, null);
      }
      while (bufferImage.contentsLost());
   }

   private void
   createBuffer(Component component)
   {
      if (bufferImage != null)
      {
         bufferImage.flush();
         bufferImage = null;
      }
      bufferImage = component.createVolatileImage(component.getWidth(), component.getHeight());
   }
}