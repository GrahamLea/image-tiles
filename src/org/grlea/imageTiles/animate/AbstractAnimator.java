package org.grlea.imageTiles.animate;

// $Id: AbstractAnimator.java,v 1.1 2004-08-20 05:25:38 grlea Exp $
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
import org.grlea.imageTiles.AnimatorListener;
import org.grlea.imageTiles.RenderedTile;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * <p></p>
 *
 * @author grlea
 * @version $Revision: 1.1 $
 */
public abstract class
AbstractAnimator
implements Animator
{
   private final List listeners;

   public
   AbstractAnimator()
   {
      listeners = new ArrayList(2);
   }

   public void
   addListener(AnimatorListener listener)
   {
      listeners.add(listener);
   }

   public void
   removeListener(AnimatorListener listener)
   {
      listeners.remove(listener);
   }

   protected void
   notifyTileAnimationComplete(RenderedTile tile)
   {
      for (Iterator iter = listeners.iterator(); iter.hasNext();)
      {
         ((AnimatorListener) iter.next()).tileAnimationComplete(tile);
      }
   }

   protected void
   notifyAnimationComplete()
   {
      for (Iterator iter = listeners.iterator(); iter.hasNext();)
      {
         ((AnimatorListener) iter.next()).animationComplete();
      }
   }
}