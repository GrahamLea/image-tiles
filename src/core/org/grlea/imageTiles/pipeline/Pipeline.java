package org.grlea.imageTiles.pipeline;

// $Id: Pipeline.java,v 1.4 2005-03-31 20:53:06 grlea Exp $
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

import org.grlea.imageTiles.ImageTilesDefaults;
import org.grlea.imageTiles.TileSet;
import org.grlea.imageTiles.TileSpace;
import org.grlea.imageTiles.TileHolder;
import org.grlea.imageTiles.ImageSource;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.Timer;

/**
 * <p>Pipeline contains the main animation loop of Image Tiles. Given a {@link PipelineComponents}
 * object, it takes care of all the stopping, starting and pausing of transitions.</p>
 *
 * <p>Note that a Pipeline does not take responsibility for rendering during the animation cycle.
 * Rather, a {@link PipelineFrameListener} should be added to the Pipeline that will, on each frame,
 * request rendering through the {@link #render} method.</p>
 *
 * @author grlea
 * @version $Revision: 1.4 $
 */
public class
Pipeline
{
   private final Point zeroOffset = new Point(0, 0);

   private final PipelineComponents components;

   private final int transitionInterval;

   private TileSet currentTileSet;

   private final TileHolder tileHolder;

   private boolean createNewTileSet = true;

   private final Object ANIMATION_LOCK = new Object();

   private final Timer timer;

   private final List transitionListeners;

   private final List frameListeners;

   private volatile boolean running = false;

   private long startTime = 0;

   private long lastFrameRateReportTime = 0;

   private long frames = 0;

   private long lastFrames = 0;

   private long lastFrameAdvance = 0;

   public
   Pipeline(BufferedImage image)
   {
      this(new PipelineComponents(image));
   }

   public
   Pipeline(TileSpace tileSpace, ImageSource imageSource)
   {
      this(new PipelineComponents(tileSpace, imageSource));
   }

   public
   Pipeline(PipelineComponents components)
   {
      this(components, ImageTilesDefaults.DEFAULT_FRAME_RATE);
   }

   public
   Pipeline(PipelineComponents components, int frameRate)
   {
      this(components, frameRate, ImageTilesDefaults.DEFAULT_TRANSITION_INTERVAL);
   }

   public
   Pipeline(PipelineComponents components, int frameRate, int transitionInterval)
   {
      this(components, frameRate, transitionInterval, null, null);
   }

   public
   Pipeline(PipelineComponents components, int frameRate, int transitionInterval,
            PipelineFrameListener[] frameListeners, PipelineTransitionListener[] transitionListeners)
   {
      this.components = components;
      this.transitionInterval = transitionInterval;
      this.tileHolder = new TileHolder(components.tileSpace);

      timer = new Timer(1000 / frameRate, new AdvanceAndNotifyTask());
      components.animationKit.addListener(new TransitionIntervalTask());
      components.animationKit.addListener(new ChangeImageTask());

      this.frameListeners = new ArrayList(2);
      this.transitionListeners = new ArrayList(2);

      if (frameListeners != null)
      {
         for (int i = 0; i < frameListeners.length; i++)
         {
            addFrameListener(frameListeners[i]);
         }
      }

      if (transitionListeners != null)
      {
         for (int i = 0; i < transitionListeners.length; i++)
         {
            addTransitionListener(transitionListeners[i]);
         }
      }
   }

   public void
   addFrameListener(PipelineFrameListener listener)
   {
      frameListeners.add(listener);
   }

   public void
   removeFrameListener(PipelineFrameListener listener)
   {
      frameListeners.remove(listener);
   }

   public void
   addTransitionListener(PipelineTransitionListener listener)
   {
      transitionListeners.add(listener);
   }

   public void
   removeTransitionFrameListener(PipelineTransitionListener listener)
   {
      transitionListeners.remove(listener);
   }

   public boolean
   isRunning()
   {
      return running;
   }

   public void
   start()
   {
      this.running = true;
      startTime = System.currentTimeMillis();
      lastFrameAdvance = System.currentTimeMillis();
      timer.start();
   }

   public void
   stop()
   {
      this.running = false;
      timer.stop();
   }

   public PipelineComponents
   getComponents()
   {
      return components;
   }

   private void
   advanceFrame()
   {
      // We lock here and in get() to ensure rendering isn't attempted while the animation state
      // is being updated.
      synchronized (ANIMATION_LOCK)
      {
         if (createNewTileSet)
         {
            createNewTileSet = false;

            // Hold onto the last tile set - make a black one if there wasn't one (first time).
            TileSet oldTileSet = currentTileSet;
            if (oldTileSet == null)
            {
               Dimension tileSpaceSize = components.tileSpace.getSize();
               BufferedImage blackImage = new BufferedImage(tileSpaceSize.width,
                                                            tileSpaceSize.height,
                                                            BufferedImage.TYPE_INT_ARGB);
               oldTileSet = new TileSet(components.tileSpace, blackImage, components.tileRenderer);
            }

            // Create a new tile set
            BufferedImage sourceImage = components.imageSource.getImage();
            BufferedImage targetImage = components.tileSpace.createImage();
            Graphics2D targetGraphics = targetImage.createGraphics();
            components.placer.place(components.tileSpace, sourceImage, targetGraphics);
            targetGraphics.dispose();
            currentTileSet = new TileSet(components.tileSpace, targetImage, components.tileRenderer);

            // Notify Listeners, then reset the AnimationKit
            notifyNewTransition(oldTileSet, currentTileSet);
            components.animationKit.newTransition(oldTileSet, currentTileSet, tileHolder);

            lastFrameAdvance = System.currentTimeMillis();
         }
         else
         {
            long currentTime = System.currentTimeMillis();
            long timeSinceLastFrame = currentTime - lastFrameAdvance;
            lastFrameAdvance = currentTime;

            // Advance the frame
//            long startTime = System.currentTimeMillis();
            components.animationKit.advanceFrame(timeSinceLastFrame);
//            long endTime = System.currentTimeMillis();
//            long calculateFrameTime = endTime - startTime;
//            System.out.println("calculateFrameTime = " + calculateFrameTime);
         }
      }

//      frames++;
//      long time = System.currentTimeMillis();
//      if (time - lastFrameRateReportTime > 1000)
//      {
//         long seconds = ((time - startTime) / 1000);
//         if (seconds != 0)
//         {
//            long frameRate = frames / seconds;
//            System.out.println("frameRate = " + frameRate);
//            long newFrames = frames - lastFrames;
//            System.out.println("newFrames = " + newFrames);
//            lastFrameRateReportTime = time;
//            lastFrames = frames;
//         }
//      }

      notifyAdvancedFrame();
   }

   public void
   startNewTransition()
   {
      createNewTileSet = true;
   }

   public void
   render(Graphics2D graphics)
   {
      render(graphics, tileHolder.getTileSpace().getSize(), zeroOffset);
   }

   public void
   render(Graphics2D graphics, Component component)
   {
      int componentWidth = component.getWidth();
      int componentHeight = component.getHeight();
      int marginX = (componentWidth - components.getTileSpace().getWidth()) / 2;
      int marginY = (componentHeight - components.getTileSpace().getHeight()) / 2;
      render(graphics, component.getSize(), new Point(marginX, marginY));
   }

   private void
   render(Graphics2D graphics, Dimension canvasSize, Point tilespaceOffset)
   {
      // We lock here and in advanceFrame() to ensure rendering isn't attempted while the animation
      // state is being updated.
      synchronized (ANIMATION_LOCK)
      {

//         long startTime = System.currentTimeMillis();
         components.renderKit.render(tileHolder, graphics, canvasSize, tilespaceOffset);
//         long endTime = System.currentTimeMillis();
//         long renderFrameTime = endTime - startTime;
//         System.out.println("renderFrameTime =    " + renderFrameTime);
      }
   }

   private void
   notifyAdvancedFrame()
   {
      for (Iterator iter = frameListeners.iterator(); iter.hasNext();)
      {
         ((PipelineFrameListener) iter.next()).advancedFrame(this);
      }
   }

   private void
   notifyNewTransition(TileSet oldTileSet, TileSet newTileSet)
   {
      for (Iterator iter = transitionListeners.iterator(); iter.hasNext();)
      {
         ((PipelineTransitionListener) iter.next()).newTransition(this, oldTileSet, newTileSet);
      }
   }

   private final class
   AdvanceAndNotifyTask
   implements ActionListener
   {
//      long lastExecutionTime = System.currentTimeMillis();

      public void
      actionPerformed(ActionEvent e)
      {
//         long timeNow = System.currentTimeMillis();
//         System.out.println(timeNow - lastExecutionTime);
//         lastExecutionTime = timeNow;
         advanceFrame();
      }
   }


   private final class
   TransitionIntervalTask
   implements AnimationKitListener
   {
      private Timer transitionRestartTimer;

      public void
      transitionComplete()
      {
         if (transitionInterval != 0)
         {
            timer.stop();
            transitionRestartTimer = new Timer(transitionInterval, startTimerAction);
            transitionRestartTimer.start();
         }
      }

      private final ActionListener startTimerAction = new ActionListener()
      {
         public void
         actionPerformed(ActionEvent e)
         {
            if (running)
            {
               timer.start();
            }
            transitionRestartTimer.stop();
            transitionRestartTimer = null;
         }
      };
   }

   public class
   ChangeImageTask
   implements AnimationKitListener
   {
      public void
      transitionComplete()
      {
         startNewTransition();
      }
   }}