package org.grlea.imageTiles.background;

// $Id: ColourBackgroundPainter.java,v 1.3 2005-03-31 20:46:09 grlea Exp $
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

import org.grlea.imageTiles.BackgroundPainter;
import org.grlea.imageTiles.TileSpace;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Dimension;
import java.awt.Point;

/**
 * <p>Fills the background with a specified colour.</p>
 *
 * @author grlea
 * @version $Revision: 1.3 $
 */
public class 
ColourBackgroundPainter
implements BackgroundPainter
{
   private static final Color DEFAULT_BACKGROUND_COLOUR = Color.black;

   private final Color backgroundColour;

   public
   ColourBackgroundPainter()
   {
      this(DEFAULT_BACKGROUND_COLOUR);
   }

   public
   ColourBackgroundPainter(Color backgroundColour)
   {
      this.backgroundColour = backgroundColour;
   }

   public void
   paintBackground(Graphics2D graphics, Dimension canvasSize, Point tileSpaceOffset)
   {
      graphics.setColor(backgroundColour);
      graphics.fillRect(0, 0, canvasSize.width, canvasSize.height);
   }
}