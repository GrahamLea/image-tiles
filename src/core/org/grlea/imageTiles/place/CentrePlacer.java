package org.grlea.imageTiles.place;

// $Id: CentrePlacer.java,v 1.2 2004-09-04 07:59:28 grlea Exp $
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
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * <p>Places images in the centre of a TileSpace. Note that no scaling is performed, so if the image
 * is larger than the TileSpace, the edges of the Image will be lost.</p>
 *
 * @author grlea
 * @version $Revision: 1.2 $
 */
public class 
CentrePlacer
implements Placer
{
   public
   CentrePlacer()
   {}

   public void
   place(TileSpace tileSpace, BufferedImage sourceImage, Graphics2D targetGraphics)
   {
      Dimension size = tileSpace.getSize();
      int x = (size.width - sourceImage.getWidth()) / 2;
      int y = (size.height - sourceImage.getHeight()) / 2;
      targetGraphics.drawImage(sourceImage, x, y, null);
   }
}