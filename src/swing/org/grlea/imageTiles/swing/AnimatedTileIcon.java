package org.grlea.imageTiles.swing;

// $Id: AnimatedTileIcon.java,v 1.2 2004-09-04 07:59:37 grlea Exp $
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

import org.grlea.imageTiles.ImageTilesHelper;
import org.grlea.imageTiles.TileSpace;
import org.grlea.imageTiles.ImageSource;
import org.grlea.imageTiles.pipeline.Pipeline;
import org.grlea.imageTiles.pipeline.PipelineComponents;
import org.grlea.imageTiles.imageSource.SingleImageSource;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.HierarchyListener;
import java.awt.event.HierarchyEvent;
import java.awt.image.BufferedImage;

import javax.swing.Icon;

/**
 * <p>An {@link Icon} that renders the contents of an Image Tiles Pipeline.
 * <code>AnimatedTileIcon</code> uses a {@link ComponentPipelineRenderer} in conjunction with a
 * {@link ComponentRepaintPipelineListener}.</p>
 *
 * <p><code>AnimatedTileIcon</code> differs from {@link AnimatedTileCanvas} and
 * {@link AnimatedTileComponent} in that it is only the icon for a component, not the component
 * itself. This has the advantage that the component will handle any translations required as a
 * result of layout or borders, and the icon will always be rendered in the correct position,
 * whereas with the other two this is not the case. This makes <code>AnimatedTileIcon</code> ideal
 * for embedding Image Tile Pipelines within full-blown GUIs.</p>
 *
 * <p>Usage:<pre>
 *    Pipeline pipeline = ...;
 *    JLabel label = new JLabel();
 *    AnimatedTileIcon icon = new AnimatedTileIcon(label, pipeline, true);
 *    label.setIcon(icon);
 *
 *    Container container = ...;
 *    container.add(label);
 *    container.setVisible(true);
 * </pre></p>
 *
 * <p>Note that a single <code>AnimatedTileIcon</code> MAY be rendered to more than one component,
 * however to do so a hook needs to be created to force each subsequent component to repaint itself
 * once each frame. This is done using the following code:<pre>
 *    JLabel label2 = new JLabel(icon);
 *    pipeline.addFrameListener(new {@link ComponentRepaintPipelineListener}(label2));
 * </pre></p>
 *
 * @author grlea
 * @version $Revision: 1.2 $
 */
public class
AnimatedTileIcon
implements Icon
{
   private final Pipeline pipeline;

   private final ComponentPipelineRenderer renderer;

   /**
    * Creates a new <code>AnimatedTileIcon</code> that will render the given pipeline to the given
    * component.
    *
    * @param pipeline the image tiles pipeline to render
    *
    * @param autoStart <code>true</code> if the pipeline should be automatically started when the
    * component is first shown, <code>false</code> if starting of the pipline will be performed
    * manually (i.e. outside this class).
    */
   public
   AnimatedTileIcon(final Component component, final Pipeline pipeline, boolean autoStart)
   {
      if (pipeline == null)
         throw new IllegalArgumentException("pipeline cannot be null.");
      this.pipeline = pipeline;
      pipeline.addFrameListener(new ComponentRepaintPipelineListener(component));
      this.renderer = new ComponentPipelineRenderer();

      if (autoStart)
      {
         component.addHierarchyListener(new HierarchyListener()
         {
            public void
            hierarchyChanged(HierarchyEvent e)
            {
               if (component.isShowing())
               {
                  pipeline.start();
                  component.removeHierarchyListener(this);
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

   public int
   getIconWidth()
   {
      return pipeline.getComponents().getTileSpace().getWidth();
   }

   public int
   getIconHeight()
   {
      return pipeline.getComponents().getTileSpace().getHeight();
   }

   public void
   paintIcon(Component component, Graphics graphics, int x, int y)
   {
      graphics.translate(x, y);
      renderer.render(pipeline, component, (Graphics2D) graphics);
      graphics.translate(-x, -y);
   }
}