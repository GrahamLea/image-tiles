package org.grlea.imageTiles.swing;

// $Id: AnimatedTileCanvas.java,v 1.4 2004-09-04 07:59:37 grlea Exp $
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
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.HierarchyListener;
import java.awt.event.HierarchyEvent;

/**
 * <p>A {@link Canvas} component that renders an Image Tiles Pipeline onto itself.</p>
 *
 * <p>The different between an {@link AnimatedTileCanvas} and an {@link AnimatedTileComponent} is
 * that the former renders itself using a {@link CanvasPipelineRenderer} while the latter uses a
 * {@link ComponentPipelineRenderer} in conjunction with a {@link ComponentRepaintPipelineListener}.
 * The chief difference between these two approaches is that the <code>Canvas</code>-based
 * implementation implements active redrawing (i.e. doesn't rely on paint() being called by the AWT
 * event thread), while the <code>JComponent</code>-based version only renders when requested
 * (but note that these requests are triggered by the component itself). The difference in rendering
 * techniques can be determined from the documentation of the two renderer classes.</p>
 *
 * <p>Note that neither the {@link AnimatedTileCanvas} nor the {@link AnimatedTileComponent} take
 * borders, margins or size into consideration when rendering themselves. They simply render the
 * pipeline at the origin of the component. If the desire is to embed an Image Tiles component
 * within a GUI, it would be best to use an {@link AnimatedTileIcon}.</p>
 *
 * <p>Usage:<pre>
 *    Pipeline pipeline = ...;
 *    AnimatedTileCanvas canvas = new AnimatedTileCanvas(pipeline, true);
 * 
 *    Container container = ...;
 *    container.add(canvas);
 *    container.setVisible(true);
 * </pre></p>
 *
 * @see AnimatedTileComponent
 * @see AnimatedTileIcon
 *
 * @author grlea
 * @version $Revision: 1.4 $
 */
public class
AnimatedTileCanvas
extends Canvas
{
   private final Pipeline pipeline;

   private final CanvasRenderingPipelineListener renderer;

   /**
    * Creates a new <code>AnimatedTileCanvas</code> that will render the given pipeline.
    *
    * @param pipeline the image tiles pipeline to render
    *
    * @param autoStart <code>true</code> if the canvas should automatically start the pipeline when
    * the component is first shown, <code>false</code> if starting of the pipline will be performed
    * manually (i.e. outside this class).
    */
   public
   AnimatedTileCanvas(final Pipeline pipeline, boolean autoStart)
   {
      if (pipeline == null)
         throw new IllegalArgumentException("pipeline cannot be null.");

      this.pipeline = pipeline;
      renderer = new CanvasRenderingPipelineListener(this);
      pipeline.addFrameListener(renderer);

      if (autoStart)
      {
         addHierarchyListener(new HierarchyListener()
         {
            public void
            hierarchyChanged(HierarchyEvent e)
            {
               if (isShowing())
               {
                  pipeline.start();
                  removeHierarchyListener(this);
               }
            }
         });
      }
   }

   public Pipeline
   getPipeline()
   {
      return pipeline;
   }

   public boolean
   isDoubleBuffered()
   {
      return true;
   }

   public Dimension
   getPreferredSize()
   {
      return pipeline.getComponents().getTileSpace().getSize();
   }

   public void
   paint(Graphics graphics)
   {
      renderer.render(pipeline);
   }
}