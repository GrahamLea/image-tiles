package org.grlea.imageTiles.swing.easy;

// $Id: BasicAnimatedTileCanvas.java,v 1.3 2004-08-24 07:13:24 grlea Exp $
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

import org.grlea.imageTiles.Animator;
import org.grlea.imageTiles.ImageSource;
import org.grlea.imageTiles.ImageTilePipeline;
import org.grlea.imageTiles.Painter;
import org.grlea.imageTiles.Placer;
import org.grlea.imageTiles.TileRenderer;
import org.grlea.imageTiles.TileSpace;
import org.grlea.imageTiles.animate.SlideAnimator;
import org.grlea.imageTiles.choose.RandomChooser;
import org.grlea.imageTiles.paint.StaticPainter;
import org.grlea.imageTiles.place.CentrePlacer;
import org.grlea.imageTiles.render.BevelEdgeDecorator;
import org.grlea.imageTiles.render.DecorativeTileRenderer;
import org.grlea.imageTiles.render.Decorator;
import org.grlea.imageTiles.render.GlareDecorator;
import org.grlea.imageTiles.render.RoundedCornerDecorator;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;

/**
 * <p></p>
 *
 * @author grlea
 * @version $Revision: 1.3 $
 */
public class
BasicAnimatedTileCanvas
extends JComponent
{
   private static final int DEFAULT_FRAME_RATE = 50;

   private final TileSpace tileSpace;

   // TODO (grahaml) Fix this!
   private ImageTilePipeline pipeline;

   public
   BasicAnimatedTileCanvas(TileSpace tileSpace, ImageSource imageSource, TileRenderer renderer)
   {
      this(tileSpace, imageSource, renderer, new SlideAnimator(tileSpace));
   }

   public
   BasicAnimatedTileCanvas(TileSpace tileSpace, ImageSource imageSource, TileRenderer renderer,
                           Animator animator)
   {
      this(tileSpace, imageSource, renderer, animator, DEFAULT_FRAME_RATE);
   }

   public
   BasicAnimatedTileCanvas(TileSpace tileSpace, ImageSource imageSource, TileRenderer renderer,
                           Animator animator, int frameRate)
   {
      this.tileSpace = tileSpace;
      Painter painter = new StaticPainter(tileSpace);
      Placer placer = new CentrePlacer();
      RandomChooser chooser = new RandomChooser(tileSpace);

      final ImageTilePipeline pipeline =
         new ImageTilePipeline(tileSpace, imageSource, placer, chooser, renderer, painter,
                               animator, frameRate);
      this.pipeline = pipeline;

      addComponentListener(new ComponentAdapter()
      {
         public void
         componentShown(ComponentEvent e)
         {
            Graphics2D graphics = (Graphics2D) getGraphics();
            pipeline.start(graphics);
         }

         public void
         componentHidden(ComponentEvent e)
         {
            pipeline.stop();
         }
      });

   }

   public Dimension
   getPreferredSize()
   {
      Insets borderInsets = getBorder().getBorderInsets(this);
      Dimension size = tileSpace.getSize();
      size.width += borderInsets.left + borderInsets.right + 2;
      size.height += borderInsets.top + borderInsets.bottom;
      return size;
   }

   public void
   start()
   {
      Insets borderInsets = getBorder().getBorderInsets(this);
      Graphics2D graphics = (Graphics2D) getGraphics();
      graphics.translate(borderInsets.left, borderInsets.top);
      pipeline.start(graphics);
//      graphics.translate(-borderInsets.left, -borderInsets.top);
   }
}