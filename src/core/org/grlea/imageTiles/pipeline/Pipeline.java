package org.grlea.imageTiles.pipeline;

// $Id: Pipeline.java,v 1.1 2004-08-27 01:07:01 grlea Exp $
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

import org.grlea.imageTiles.pipeline.PipelineListener;
import org.grlea.imageTiles.TileSpace;
import org.grlea.imageTiles.Placer;
import org.grlea.imageTiles.AnimationKit;
import org.grlea.imageTiles.RenderKit;
import org.grlea.imageTiles.TileRenderer;
import org.grlea.imageTiles.TileSet;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;

/**
 * <p></p>
 *
 * @author grlea
 * @version $Revision: 1.1 $
 */
public class
Pipeline
{
   private final TileSpace tileSpace;

   private final ImageSource imageSource;

   private final Placer placer;

   private final AnimationKit animationKit;

   private final RenderKit renderKit;

   private final TileRenderer tileRenderer;

   private final PipelineListener[] listeners;

   private boolean createNewTileSet = true;

   private final Object ANIMATION_LOCK = new Object();

   public
   Pipeline(TileSpace tileSpace, ImageSource imageSource, Placer placer, TileRenderer tileRenderer,
            AnimationKit animationKit, RenderKit renderKit, PipelineListener[] listeners)
   {
      this.tileSpace = tileSpace;
      this.imageSource = imageSource;
      this.placer = placer;
      this.tileRenderer = tileRenderer;
      this.animationKit = animationKit;
      this.renderKit = renderKit;
      this.listeners = listeners;
   }

   public void
   advanceFrame()
   {
      // We lock here and in render() to ensure rendering isn't attempted while the animation state
      // is being updated.
      synchronized (ANIMATION_LOCK)
      {
         if (createNewTileSet)
         {
            createNewTileSet = false;

            // Create a new tile set
            BufferedImage image = imageSource.getImage();
            Point position = placer.choosePosition(image, tileSpace);
            TileSet tileSet = new TileSet(tileSpace, image, position, tileRenderer);

            // Reset the animationKit
            animationKit.initialiseAnimation(tileSet);
         }
         else
         {
            // Advance the frame
            animationKit.advanceFrame();
         }
      }

      // Notify listeners
      for (int i = 0; i < listeners.length; i++)
      {
         listeners[i].frameAdvanced();
      }
   }

   public void
   nextImage()
   {
      createNewTileSet = true;
   }

   public void
   render(Graphics2D graphics)
   {
      // We lock here and in advanceFrame() to ensure rendering isn't attempted while the animation
      // state is being updated.
      synchronized (ANIMATION_LOCK)
      {
         renderKit.render(graphics);
      }
   }
}