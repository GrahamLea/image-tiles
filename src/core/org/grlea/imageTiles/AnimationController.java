package org.grlea.imageTiles;

// $Id: AnimationController.java,v 1.2 2004-08-27 01:17:47 grlea Exp $
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * <p></p>
 *
 * @author grlea
 * @version $Revision: 1.2 $
 */
public class
AnimationController
implements AnimationKit
{
   private final RenderedTileSource renderedTileSource;

   private final Chooser chooser;

   private final Animator animator;

   private final Painter painter;

   private final List stateListeners;

   private TileSet tileSet;

   private boolean animationComplete = false;

   private boolean animationCompleteNotificationSent = false;

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
         animationCompleteNotificationSent = false;
      }
   };

   public
   AnimationController(Chooser chooser, Animator animator, Painter painter)
   {
      this.chooser = chooser;
      renderedTileSource = new RenderedTileSourceImpl();
      this.animator = animator;
      this.painter = painter;
      stateListeners = new ArrayList(2);
   }

   public void
   addListener(AnimationKitListener listener)
   {
      stateListeners.add(listener);
   }

   public void
   removeListener(AnimationKitListener listener)
   {
      stateListeners.remove(listener);
   }

   private void
   notifyAnimationComplete()
   {
      for (Iterator iter = stateListeners.iterator(); iter.hasNext();)
      {
         ((AnimationKitListener) iter.next()).animationComplete();
      }
   }

   public void
   initialiseAnimation(TileSet tileSet)
   {
      this.tileSet = tileSet;
      this.animationComplete = false;
      chooser.reset();
      animator.reset();
      animator.addListener(animatorListener);
      painter.removeAllTiles();
   }

   public void
   advanceFrame()
   {
      if (animationComplete)
      {
         if (!animationCompleteNotificationSent)
         {
            animator.removeListener(animatorListener);
            notifyAnimationComplete();
            animationCompleteNotificationSent = true;
         }
      }
      else
      {
         painter.advanceFrame();
         animator.advanceFrame(renderedTileSource);
      }
   }

   private class
   RenderedTileSourceImpl
   implements RenderedTileSource
   {
      public boolean
      hasMoreTiles()
      {
         return tileSet != null && chooser.hasMoreTiles();
      }

      public RenderedTile
      getNextTile()
      {
         return tileSet.render(chooser.getNextTile());
      }
   }
}