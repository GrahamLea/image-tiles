package org.grlea.imageTiles.pipeline;

// $Id: AnimationKit.java,v 1.2 2005-03-19 00:11:37 grlea Exp $
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
import org.grlea.imageTiles.Chooser;
import org.grlea.imageTiles.Transition;
import org.grlea.imageTiles.TileHolderRenderer;
import org.grlea.imageTiles.TransitionListener;
import org.grlea.imageTiles.TileSet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * <p>Object for keeping track of the animation of a Transition. Used internally by a Pipeline.</p>
 *
 * @author grlea
 * @version $Revision: 1.2 $
 */
class
AnimationKit
{
   private final Chooser chooser;

   private final Transition transition;

   private final TileHolderRenderer tileHolderRenderer;

   private final List stateListeners;

   private boolean transitionComplete = false;

   private boolean transitionCompleteNotificationSent = false;

   private final TransitionListener transitionListener = new TransitionListener()
   {
      public void
      transitionComplete()
      {
         transitionComplete = true;
         transitionCompleteNotificationSent = false;
      }
   };

   public
   AnimationKit(Chooser chooser, Transition transition, TileHolderRenderer tileHolderRenderer)
   {
      this.chooser = chooser;
      this.transition = transition;
      this.tileHolderRenderer = tileHolderRenderer;
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
         ((AnimationKitListener) iter.next()).transitionComplete();
      }
   }

   // TODO (grahaml) Need an old TileHolder and a new TileHolder here?
   public void
   newTransition(TileSet oldTileSet, TileSet newTileSet, TileHolder tileHolder)
   {
      this.transitionComplete = false;
      chooser.reset();
      transition.newTransition(oldTileSet, newTileSet, chooser, tileHolder);
      transition.addListener(transitionListener);
      tileHolder.removeListener(tileHolderRenderer);
      tileHolder.addListener(tileHolderRenderer);
   }

   public void
   advanceFrame(long timeSinceLastFrame)
   {
      if (transitionComplete)
      {
         if (!transitionCompleteNotificationSent)
         {
            transition.removeListener(transitionListener);
            notifyAnimationComplete();
            transitionCompleteNotificationSent = true;
         }
      }
      else
      {
         tileHolderRenderer.advanceFrame(timeSinceLastFrame);
         transition.advanceFrame(timeSinceLastFrame);
      }
   }
}