package org.grlea.imageTiles.render;

// $Id: BevelEdgeDecorator.java,v 1.1 2004-08-23 22:47:41 grlea Exp $
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

import java.awt.BasicStroke;
import java.awt.Color;
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
BevelEdgeDecorator
implements Decorator
{
   private static final float DEFAULT_BORDER_ALPHA = 0.4F;

   private final Color highlightColour;
   private final Color shadowColour;
   private final Stroke lineType;

   private final Line2D[] whiteLines;
   private final Line2D[] blackLines;

   public
   BevelEdgeDecorator(TileSpace tileSpace)
   {
      this(tileSpace, DEFAULT_BORDER_ALPHA);
   }

   public
   BevelEdgeDecorator(TileSpace tileSpace, float alpha)
   {
      lineType = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER);

      int tileSize = tileSpace.getTileSize();
      int lastPixel = tileSize - 1;

      // TODO (grahaml) Implement variable thickness, which will change these line arrays.
      this.whiteLines =
         new Line2D[] {new Line2D.Double(0, 0, lastPixel, 0),
                       new Line2D.Double(0, 0, 0, lastPixel)};
      this.blackLines =
         new Line2D[] {new Line2D.Double(lastPixel, 0, lastPixel, lastPixel),
                       new Line2D.Double(0, lastPixel, lastPixel, lastPixel)};

      this.highlightColour = new Color(1, 1, 1, alpha);
      this.shadowColour = new Color(0, 0, 0, alpha);
   }

   public void
   decorate(BufferedImage tileImage)
   {
      Graphics2D graphics = (Graphics2D) tileImage.getGraphics();

      Paint originalPaint = graphics.getPaint();
      graphics.setStroke(lineType);

      graphics.setPaint(highlightColour);
      for (int i = 0; i < whiteLines.length; i++)
      {
         graphics.draw(whiteLines[i]);
      }

      graphics.setPaint(shadowColour);
      for (int i = 0; i < blackLines.length; i++)
      {
         graphics.draw(blackLines[i]);
      }

      graphics.setPaint(originalPaint);
   }
}