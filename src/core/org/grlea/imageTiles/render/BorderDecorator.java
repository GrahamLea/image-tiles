package org.grlea.imageTiles.render;

// $Id: BorderDecorator.java,v 1.1 2004-08-23 22:47:41 grlea Exp $
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

import org.grlea.imageTiles.TileSpace;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;

/**
 * <p></p>
 *
 * @author grlea
 * @version $Revision: 1.1 $
 */
public class
BorderDecorator
implements Decorator
{
   private static final Color DEFAULT_BORDER_COLOR = Color.white;

   private final Stroke lineType;

   private final Line2D[] lines;

   private final Color borderColor;

   public
   BorderDecorator(TileSpace tileSpace)
   {
      this(tileSpace, DEFAULT_BORDER_COLOR);
   }

   public
   BorderDecorator(TileSpace tileSpace, Color borderColor)
   {
      this.borderColor = borderColor;
      lineType = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER);

      int tileSize = tileSpace.getTileSize();
      int lastPixel = tileSize - 1;

      this.lines =
         new Line2D[] {new Line2D.Double(0, 0, lastPixel, 0),
                       new Line2D.Double(0, 0, 0, lastPixel),
                       new Line2D.Double(lastPixel, 0, lastPixel, lastPixel),
                       new Line2D.Double(0, lastPixel, lastPixel, lastPixel)};
   }

   public void
   decorate(BufferedImage tileImage)
   {
      Graphics2D graphics = (Graphics2D) tileImage.getGraphics();

      Paint originalPaint = graphics.getPaint();
      graphics.setStroke(lineType);

      graphics.setPaint(borderColor);
      for (int i = 0; i < lines.length; i++)
      {
         graphics.draw(lines[i]);
      }

      graphics.setPaint(originalPaint);
   }
}