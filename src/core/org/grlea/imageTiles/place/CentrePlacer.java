package org.grlea.imageTiles.place;

// $Id: CentrePlacer.java,v 1.1 2004-08-23 22:47:41 grlea Exp $
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

import org.grlea.imageTiles.Placer;
import org.grlea.imageTiles.TileSpace;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.image.BufferedImage;

/**
 * <p></p>
 *
 * @author grlea
 * @version $Revision: 1.1 $
 */
public class 
CentrePlacer
implements Placer
{
   public
   CentrePlacer()
   {
   }

   public Point
   choosePosition(BufferedImage image, TileSpace tileSpace)
   {
      Dimension size = tileSpace.getSize();
      int x = (size.width - image.getWidth()) / 2;
      int y = (size.height - image.getHeight()) / 2;
      return new Point(x, y);
   }
}