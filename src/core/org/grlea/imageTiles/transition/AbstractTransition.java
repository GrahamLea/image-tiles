package org.grlea.imageTiles.transition;

// $Id: AbstractTransition.java,v 1.1 2004-09-04 07:59:31 grlea Exp $
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

import org.grlea.imageTiles.TileImage;
import org.grlea.imageTiles.Transition;
import org.grlea.imageTiles.TransitionListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * <p>An abstract Transition base class that implements the listener behaviour.</p>
 *
 * @author grlea
 * @version $Revision: 1.1 $
 */
public abstract class
AbstractTransition
implements Transition
{
   private final List listeners;

   public
   AbstractTransition()
   {
      listeners = new ArrayList(2);
   }

   public void
   addListener(TransitionListener listener)
   {
      listeners.add(listener);
   }

   public void
   removeListener(TransitionListener listener)
   {
      listeners.remove(listener);
   }

   protected void
   notifyTransitionComplete()
   {
      for (Iterator iter = listeners.iterator(); iter.hasNext();)
      {
         ((TransitionListener) iter.next()).transitionComplete();
      }
   }
}