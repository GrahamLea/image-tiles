package org.grlea.imageTiles.pipeline;

// $Id: RenderKit.java,v 1.1 2004-09-04 07:59:28 grlea Exp $
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

import org.grlea.imageTiles.TileHolder;
import org.grlea.imageTiles.BackgroundPainter;
import org.grlea.imageTiles.Transition;
import org.grlea.imageTiles.TileHolderRenderer;

import java.awt.Graphics2D;

/**
 * <p>Object for rendering the animation of a Transition. Used by the Pipeline.</p>
 *
 * @author grlea
 * @version $Revision: 1.1 $
 */
class
RenderKit
{
   private final BackgroundPainter background;

   private final Transition transition;

   private final TileHolderRenderer tileHolderRenderer;

   public
   RenderKit(BackgroundPainter background, Transition transition, TileHolderRenderer tileHolderRenderer)
   {
      this.background = background;
      this.transition = transition;
      this.tileHolderRenderer = tileHolderRenderer;
   }

   // TODO (grahaml) Gotta get this TileHolder out of this signature!?
   public void
   render(TileHolder tileHolder, Graphics2D graphics)
   {
//      long startTime = System.currentTimeMillis();
      background.paintBackground(graphics);
//      long startPainterTime = System.currentTimeMillis();
      tileHolderRenderer.render(tileHolder, graphics);
//      long startTransitionTime = System.currentTimeMillis();
      transition.render(graphics);
//      long endTime = System.currentTimeMillis();

//      long backgroundTime = startPainterTime - startTime;
//      long painterTime = startTransitionTime - startPainterTime;
//      long transitionTime = endTime - startTransitionTime;

//      System.out.println(backgroundTime + "\t" + painterTime + "\t" + transitionTime);
   }
}