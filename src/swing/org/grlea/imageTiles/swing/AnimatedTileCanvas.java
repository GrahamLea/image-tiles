package org.grlea.imageTiles.swing;

// $Id: AnimatedTileCanvas.java,v 1.3 2004-08-29 22:23:53 grlea Exp $
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
import org.grlea.imageTiles.AnimationKit;
import org.grlea.imageTiles.AnimationKitListener;
import org.grlea.imageTiles.AnimationRenderKit;
import org.grlea.imageTiles.Animator;
import org.grlea.imageTiles.BackgroundPainter;
import org.grlea.imageTiles.ImageTilesHelper;
import org.grlea.imageTiles.Painter;
import org.grlea.imageTiles.Placer;
import org.grlea.imageTiles.RenderKit;
import org.grlea.imageTiles.SchedulerPausingAnimationKitListener;
import org.grlea.imageTiles.TileRenderer;
import org.grlea.imageTiles.TileSpace;
import org.grlea.imageTiles.animate.SlideAnimator;
import org.grlea.imageTiles.background.ColourBackgroundPainter;
import org.grlea.imageTiles.choose.RandomChooser;
import org.grlea.imageTiles.paint.StaticPainter;
import org.grlea.imageTiles.pipeline.ImageSource;
import org.grlea.imageTiles.pipeline.Pipeline;
import org.grlea.imageTiles.pipeline.PipelineImageChanger;
import org.grlea.imageTiles.pipeline.PipelineListener;
import org.grlea.imageTiles.pipeline.PipelineTicker;
import org.grlea.imageTiles.pipeline.imageSource.SingleImageSource;
import org.grlea.imageTiles.place.CentrePlacer;
import org.grlea.imageTiles.render.PlainTileRenderer;
import org.grlea.util.FixedRateScheduler;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;

/**
 * <p></p>
 *
 * @author grlea
 * @version $Revision: 1.3 $
 */
public class
AnimatedTileCanvas
extends Component
{
   private static final int DEFAULT_FRAME_RATE = 50;

   private final TileSpace tileSpace;

   private final FixedRateScheduler scheduler;

   private Pipeline pipeline;

   private VolatileImage buffer = null;

   public
   AnimatedTileCanvas(BufferedImage image)
   {
      this(ImageTilesHelper.createTileSpace(image), new SingleImageSource(image));
   }

   public
   AnimatedTileCanvas(TileSpace tileSpace, BufferedImage image)
   {
      this(tileSpace, new SingleImageSource(image));
   }

   public
   AnimatedTileCanvas(TileSpace tileSpace, ImageSource imageSource)
   {
      this(tileSpace, imageSource, new PlainTileRenderer());
   }

   public
   AnimatedTileCanvas(TileSpace tileSpace, ImageSource imageSource, TileRenderer renderer)
   {
      this(tileSpace, imageSource, renderer, new SlideAnimator(tileSpace));
   }

   public
   AnimatedTileCanvas(TileSpace tileSpace, ImageSource imageSource, TileRenderer renderer,
                           Animator animator)
   {
      this(tileSpace, imageSource, renderer, animator, DEFAULT_FRAME_RATE);
   }

   public
   AnimatedTileCanvas(TileSpace tileSpace, ImageSource imageSource, TileRenderer renderer,
                           Animator animator, int frameRate)
   {
      this.tileSpace = tileSpace;
      BackgroundPainter backgroundPainter = new ColourBackgroundPainter(tileSpace);
      Painter painter = new StaticPainter(tileSpace);
      Placer placer = new CentrePlacer();
      RandomChooser chooser = new RandomChooser(tileSpace);

      AnimationKit animationController = new AnimationController(chooser, animator, painter);
      RenderKit renderKit = new AnimationRenderKit(backgroundPainter, animator, painter);
      RepaintComponentPipelineListener pipelineListener = new RepaintComponentPipelineListener(this);
      Pipeline pipeline = new Pipeline(tileSpace, imageSource, placer, renderer, animationController,
                                       renderKit, new PipelineListener[] {pipelineListener});
      this.pipeline = pipeline;

      PipelineTicker pipelineTicker = new PipelineTicker(pipeline);
      scheduler = new FixedRateScheduler(pipelineTicker, frameRate);

      AnimationKitListener schedulerPauser = new SchedulerPausingAnimationKitListener(scheduler);
      animationController.addListener(schedulerPauser);

      AnimationKitListener imageAdvancer = new PipelineImageChanger(pipeline);
      animationController.addListener(imageAdvancer);
   }

   public void
   start()
   {
      scheduler.start();
   }

   public void
   stop()
   {
      scheduler.stop();
   }

   public Dimension
   getPreferredSize()
   {
      return tileSpace.getSize();
   }

   public void
   paint(Graphics graphics)
   {
      super.paint(graphics);

      if (buffer == null)
         createBuffer();

      do
      {
         // VolatileImage handling code courtesy of the legendary Chet Haase
         int validateCode = buffer.validate(getGraphicsConfiguration());
         if (validateCode == VolatileImage.IMAGE_INCOMPATIBLE)
         {
            createBuffer();
         }

         Graphics2D bufferGraphics = buffer.createGraphics();
         pipeline.render(bufferGraphics);
         bufferGraphics.dispose();

         graphics.drawImage(buffer, 0, 0, null);
      }
      while (buffer.contentsLost());
   }

   private void
   createBuffer()
   {
      if (buffer != null)
      {
         buffer.flush();
         buffer = null;
      }
      buffer = createVolatileImage(tileSpace.getWidth(), tileSpace.getHeight());
   }
}