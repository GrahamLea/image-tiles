package org.grlea.imageTiles;

import java.awt.Graphics2D;
import java.awt.Dimension;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

// $Id: AnimationController.java,v 1.1 2004-08-20 05:25:35 grlea Exp $
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
AnimationController
{
   private final RenderedTileSource renderedTileSource;

   private final TileSet tileSet;

   private final Chooser chooser;

   private final Animator animator;

   private final Painter painter;

   private final Graphics2D targetGraphics;

   private final List listeners;

   private boolean animationComplete = false;

   private final AnimatorListener animatorListener = new AnimatorListener()
   {
      public void
      tileAnimationComplete(RenderedTile tile)
      {
         painter.addTile(tile);
      }

      public void
      animationComplete()
      {
         animationComplete = true;
      }
   };

   public
   AnimationController(TileSet tileSet, Chooser chooser, Animator animator, Painter painter,
                       Graphics2D graphics)
   {
      this.tileSet = tileSet;
      this.chooser = chooser;
      renderedTileSource = new RenderedTileSourceImpl();
      this.animator = animator;
      this.painter = painter;
      this.targetGraphics = graphics;
      listeners = new ArrayList(2);

      animator.addListener(animatorListener);
   }

   public boolean
   isAnimationComplete()
   {
      return animationComplete;
   }

   public void
   addListener(AnimationControllerListener listener)
   {
      listeners.add(listener);
   }

   public void
   removeListener(AnimationControllerListener listener)
   {
      listeners.remove(listener);
   }

   protected void
   notifyAnimationComplete()
   {
      for (Iterator iter = listeners.iterator(); iter.hasNext();)
      {
         ((AnimationControllerListener) iter.next()).animationComplete();
      }
   }

   public void
   drawFrame()
   {
      if (animationComplete)
      {
         animator.removeListener(animatorListener);
         notifyAnimationComplete();
      }
      else
      {
         painter.tick();
         animator.tick(renderedTileSource);

         Dimension tileSetSize = tileSet.getSize();
         targetGraphics.clearRect(0, 0, tileSetSize.width, tileSetSize.height);
         painter.paint(targetGraphics);
         animator.paint(targetGraphics);
      }
   }

   private class
   RenderedTileSourceImpl
   implements RenderedTileSource
   {
      public boolean
      hasMoreTiles()
      {
         return chooser.hasMoreTiles();
      }

      public RenderedTile
      getNextTile()
      {
         return tileSet.render(chooser.getNextTile());
      }
   }
}