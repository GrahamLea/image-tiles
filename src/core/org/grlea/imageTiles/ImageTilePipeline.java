package org.grlea.imageTiles;

import org.grlea.imageTiles.swing.RenderImageTilesFrameTask;
import org.grlea.util.FixedRateScheduler;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;

// $Id: ImageTilePipeline.java,v 1.1 2004-08-23 22:47:37 grlea Exp $
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



/**
 * <p></p>
 *
 * @author grlea
 * @version $Revision: 1.1 $
 */
public class
ImageTilePipeline
{
   private final TileSpace tileSpace;

   private final ImageSource imageSource;

   private final Placer placer;

   private final Chooser chooser;

   private final TileRenderer tileRenderer;

   private final Painter painter;

   private final Animator animator;

   private final int frameRate;

   private PipelineRunnable pipelineRunnable = null;

   private Thread pipelineThread = null;

   private final Object pipelineStateLock = new Object();

   public
   ImageTilePipeline(TileSpace tileSpace, ImageSource imageSource, Placer placer, Chooser chooser,
                     TileRenderer tileRenderer, Painter painter, Animator animator, int frameRate)
   {
      this.tileSpace = tileSpace;
      this.imageSource = imageSource;
      this.placer = placer;
      this.chooser = chooser;
      this.tileRenderer = tileRenderer;
      this.painter = painter;
      this.animator = animator;
      this.frameRate = frameRate;
   }

   public void
   start(final Graphics2D graphics)
   {
      synchronized (pipelineStateLock)
      {
         if (pipelineRunnable != null)
            throw new IllegalStateException("Pipeline already started.");

         pipelineRunnable = new PipelineRunnable(graphics);
         pipelineThread = new Thread(pipelineRunnable);
         pipelineThread.setDaemon(true);
         pipelineThread.start();
      }
   }

   public void
   stop()
   {
      synchronized (pipelineStateLock)
      {
         if (pipelineRunnable == null)
            return;

         pipelineRunnable.running = false;
         try
         {
            pipelineThread.join();
         }
         catch (InterruptedException e)
         {
            throw new IllegalStateException("Unexpected InterruptedException");
         }

         pipelineThread = null;
         pipelineRunnable = null;
      }
   }

   private final class
   PipelineRunnable
   implements Runnable
   {
      public boolean running = true;

      private final Graphics2D graphics;

      public PipelineRunnable(Graphics2D graphics)
      {
         this.graphics = graphics;
      }

      public void
      run()
      {
         while (running)
         {
            BufferedImage image = imageSource.getImage();
            Point position = placer.choosePosition(image, tileSpace);
            TileSet tileSet = new TileSet(tileSpace, image, position, tileRenderer);

            // TODO (grahaml) Need a ChooserFactory ?
            AnimationController animationController =
               new AnimationController(tileSet, chooser, animator, painter);

            Runnable renderTask =
               new RenderImageTilesFrameTask(tileSpace, animationController, graphics);
            final FixedRateScheduler scheduler = new FixedRateScheduler(renderTask, frameRate);

            final Object animationCompleteGate = new Object();
            animationController.addStateListener(new AnimationStateListener()
            {
               public void
               animationComplete()
               {
                  synchronized (animationCompleteGate)
                  {
                     scheduler.stop();
                     animationCompleteGate.notify();
                  }
               }
            });

            synchronized (animationCompleteGate)
            {
               scheduler.start();
               try
               {
                  animationCompleteGate.wait();
               }
               catch (InterruptedException e)
               {
                  throw new IllegalStateException("Unexpected InterruptedException");
               }
            }
         }
      }
   }
}