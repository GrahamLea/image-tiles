package org.grlea.imageTiles.render;

// $Id: GlareDecorator.java,v 1.2 2004-09-04 07:59:29 grlea Exp $
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
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

/**
 * <p>A decorator that places a gradient over each Tile to give a glare effect.</p>
 *
 * @author grlea
 * @version $Revision: 1.2 $
 */
public class
GlareDecorator
implements Decorator
{
   private static final float DEFAULT_GLARE_ALPHA = 0.1F;
   private static final float DEFAULT_GLARE_WIDTH = 0.75F;

   private static final double THETA = Math.atan(3);
   private static final double SIN_THETA = Math.sin(THETA);
   private static final double COS_THETA = Math.cos(THETA);

   private static final Color TRANSPARENT_WHITE = new Color(1, 1, 1, 0F);
   private static final Color OPAQUE_WHITE = new Color(1, 1, 1, 1F);

   private final AlphaComposite alphaComposite;

   private final Line2D gradientLine;
   private final Stroke lineStyle;
   private final GradientPaint gradientPaint;

   public
   GlareDecorator(TileSpace tileSpace)
   {
      this(tileSpace, DEFAULT_GLARE_ALPHA, DEFAULT_GLARE_WIDTH);
   }

   public
   GlareDecorator(TileSpace tileSpace, float glareAlpha, float glareWidth)
   {
      this.alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, glareAlpha);

      int tileSize = tileSpace.getTileSize();
      Point linePoint1 = new Point(0, tileSize / 3);
      Point linePoint2 = new Point(tileSize, 0);
      float lineWidth = tileSize * glareWidth;
      gradientLine = new Line2D.Float(linePoint1, linePoint2);
      lineStyle = new BasicStroke(lineWidth);

      double gradientXDiff = lineWidth / 2 * COS_THETA;
      double gradientYDiff = lineWidth / 2 * SIN_THETA;
      Point2D.Double gradientPoint =
         new Point2D.Double(linePoint1.x - gradientXDiff, linePoint1.y - gradientYDiff);
      gradientPaint =
         new GradientPaint(gradientPoint, TRANSPARENT_WHITE, linePoint1, OPAQUE_WHITE, true);
   }

   public void
   decorate(BufferedImage tileImage)
   {
      Graphics2D graphics = (Graphics2D) tileImage.getGraphics();

      Composite originalComposite = graphics.getComposite();
      Paint originalPaint = graphics.getPaint();

      graphics.setComposite(alphaComposite);
      graphics.setStroke(lineStyle);
      graphics.setPaint(gradientPaint);

      graphics.draw(gradientLine);

      graphics.setComposite(originalComposite);
      graphics.setPaint(originalPaint);
   }
}