package org.grlea.imageTiles.background;

// $Id: TransparentBackgroundPainter.java,v 1.3 2005-03-31 20:46:09 grlea Exp $
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

import java.awt.Graphics2D;
import java.awt.Dimension;
import java.awt.Point;

/**
 * <p>Clears the background to the graphics object's background (usually 100% transparent).</p>
 *
 * @author grlea
 * @version $Revision: 1.3 $
 */
public class 
TransparentBackgroundPainter
implements BackgroundPainter
{
   public
   TransparentBackgroundPainter()
   {
   }

   public void
   paintBackground(Graphics2D graphics, Dimension canvasSize, Point tileSpaceOffset)
   {
      graphics.clearRect(0, 0, canvasSize.width, canvasSize.height);
   }
}