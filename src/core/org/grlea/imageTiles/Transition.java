package org.grlea.imageTiles;

// $Id: Transition.java,v 1.1 2004-09-04 07:59:20 grlea Exp $
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

import java.awt.Graphics2D;

/**
 * <p>Performs a transition from one TileSet to another. A Transition initially retrieves Tiles (as
 * many as it wants to animate at one time) from a Chooser. As the tiles "arrive" at their
 * destination, the Transition hands control of the tile over to the TileHolder. Theoretically, a
 * Transition could perform a reverse Transition by removing Tiles from the TileHolder and reversing
 * their animation before animating the Tile from the new TileSet into place.</p>
 *
 * @author grlea
 * @version $Revision: 1.1 $
 */
public interface
Transition
{
   void
   addListener(TransitionListener listener);

   void
   removeListener(TransitionListener listener);

   public void
   newTransition(TileSet oldTileSet, TileSet newTileSet, Chooser chooser, TileHolder tileHolder);

   public void
   advanceFrame();

   public void
   render(Graphics2D graphics);
}