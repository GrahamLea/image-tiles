package org.grlea.imageTiles.screensaver;

// $Id: ImageTilesScreensaver.java,v 1.2 2005-04-03 08:29:16 grlea Exp $
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

import org.grlea.imageTiles.pipeline.Pipeline;
import org.grlea.imageTiles.pipeline.PipelineFrameListener;
import org.grlea.imageTiles.pipeline.PipelineComponents;
import org.grlea.imageTiles.pipeline.PipelineTransitionListener;
import org.grlea.imageTiles.swing.ComponentPipelineRenderer;
import org.grlea.imageTiles.ImageSource;
import org.grlea.imageTiles.TileSpace;
import org.grlea.imageTiles.ImageTilesHelper;
import org.grlea.imageTiles.Chooser;
import org.grlea.imageTiles.TileRenderer;
import org.grlea.imageTiles.BackgroundPainter;
import org.grlea.imageTiles.Placer;
import org.grlea.imageTiles.TileHolderRenderer;
import org.grlea.imageTiles.Transition;
import org.grlea.imageTiles.transition.SlideTransition;
import org.grlea.imageTiles.tileHolder.StaticTileHolderRenderer;
import org.grlea.imageTiles.place.ScalingCentrePlacer;
import org.grlea.imageTiles.place.CentrePlacer;
import org.grlea.imageTiles.background.LastTileSetBackgroundPainter;
import org.grlea.imageTiles.background.TransparentBackgroundPainter;
import org.grlea.imageTiles.background.ColourBackgroundPainter;
import org.grlea.imageTiles.render.DecorativeTileRenderer;
import org.grlea.imageTiles.render.Decorator;
import org.grlea.imageTiles.render.BevelEdgeDecorator;
import org.grlea.imageTiles.render.RoundedCornerDecorator;
import org.grlea.imageTiles.render.GlareDecorator;
import org.grlea.imageTiles.render.PlainTileRenderer;
import org.grlea.imageTiles.choose.RandomChooser;
import org.grlea.imageTiles.choose.SequentialChooser;
import org.grlea.imageTiles.imageSource.DirectoryImageSource;
import org.grlea.imageTiles.imageSource.SingleImageSource;

import org.jdesktop.jdic.screensaver.ScreensaverFrame;
import org.jdesktop.jdic.screensaver.ScreensaverSettings;
import org.jdesktop.jdic.screensaver.SimpleScreensaver;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.RenderingHints;
import java.awt.BasicStroke;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

/**
 * <p></p>
 *
 * @author Graham Lea
 * @version $Revision: 1.2 $
 */
public class 
ImageTilesScreensaver
extends SimpleScreensaver
implements PipelineFrameListener
{
   private Component screen;
   private ComponentPipelineRenderer renderer;

   private Pipeline pipeline;

   public
   ImageTilesScreensaver()
   {
   }

   protected void
   init()
   {
      ScreensaverSettings settings = getContext().getSettings();
      this.screen = getContext().getComponent();

      // Tile size
      int tileSize = ImageTilesHelper.chooseAppropriateTileSize(screen);
      String tileSizeString = settings.getProperty("tileSize");
      if (tileSizeString != null)
      {
         try
         {
            tileSize = Integer.parseInt(tileSizeString);
         }
         catch (NumberFormatException e)
         {}
      }

      // Gap size
      int gapSize = ImageTilesHelper.chooseAppropriateGapSize(tileSize);
      String gapSizeString = settings.getProperty("gapSize");
      if (gapSizeString != null)
      {
         try
         {
            gapSize = Integer.parseInt(gapSizeString);
         }
         catch (NumberFormatException e)
         {}
      }

      TileSpace tileSpace = ImageTilesHelper.createTileSpace(screen, tileSize, gapSize);

      // Image Source
      String imageDirectory = settings.getProperty("imageDirectory");
      String searchSubdirectoriesString = settings.getProperty("searchSubdirectories");
      String randomImageOrderString = settings.getProperty("randomImageOrder");
      String startAtRandomImageString = settings.getProperty("startAtRandomImage");

      boolean searchSubdirectories = "true".equals(searchSubdirectoriesString);
      boolean randomImageOrder = "true".equals(randomImageOrderString);
      boolean startAtRandomImage = "true".equals(startAtRandomImageString);

      // TODO (grahaml) Print errors to the image tiles default screen.
      ImageSource imageSource = null;
      try
      {
         if (imageDirectory != null)
         {
            File directory = new File(imageDirectory);
            if (directory.exists() && directory.isDirectory() && directory.canRead())
               imageSource = new DirectoryImageSource(directory, randomImageOrder,
                                                      startAtRandomImage, searchSubdirectories);
         }
      }
      catch (IOException e)
      {
         System.out.println("Error loading images from directory:");
         e.printStackTrace();
      }

      boolean usingDefaultImage = false;
      if (imageSource == null)
      {
         usingDefaultImage = true;
         BufferedImage image = createDefaultImage(tileSpace);
         imageSource = new SingleImageSource(image);
      }

      // Tile Decorations
      ArrayList tileDecoratorsList = new ArrayList(3);

      String bevelString = settings.getProperty("bevel");
      boolean bevel = "true".equalsIgnoreCase(bevelString);
      if (bevel)
         tileDecoratorsList.add(new BevelEdgeDecorator(tileSpace));

      String rounderCornersString = settings.getProperty("roundedCorners");
      boolean roundedCorners = "true".equalsIgnoreCase(rounderCornersString);
      if (roundedCorners)
         tileDecoratorsList.add(new RoundedCornerDecorator(tileSpace));

      String glareString = settings.getProperty("glare");
      boolean glare = "true".equalsIgnoreCase(glareString);
      if (glare)
         tileDecoratorsList.add(new GlareDecorator(tileSpace));

      // Tile Renderer
      Decorator[] decorators =
         (Decorator[]) tileDecoratorsList.toArray(new Decorator[tileDecoratorsList.size()]);
      TileRenderer tileRenderer;
      if (decorators.length == 0)
         tileRenderer = new PlainTileRenderer();
      else
         tileRenderer = new DecorativeTileRenderer(decorators);

      // Scaling Threshold + Placer
      float scalingThreshold = 2;
      String scalingThresholdString = settings.getProperty("scalingThreshold");
      if ("noLimit".equalsIgnoreCase(scalingThresholdString))
         scalingThreshold = Float.POSITIVE_INFINITY;
      if ("noScale".equalsIgnoreCase(scalingThresholdString))
         scalingThreshold = 1;

      Placer placer = new ScalingCentrePlacer(scalingThreshold);
      if (scalingThreshold == 1)
         placer = new CentrePlacer();
      else
         placer = new ScalingCentrePlacer(scalingThreshold);

      // Tile Chooser
      String tileChoiceString = settings.getProperty("tileChoice");
      Chooser chooser;
      if ("sequential".equalsIgnoreCase(tileChoiceString))
         chooser = new SequentialChooser(tileSpace, true);
      else
         chooser = new RandomChooser(tileSpace);

      // Background Painer
      String clearImagesString = settings.getProperty("clearImages");
      boolean clearImages = "true".equals(clearImagesString);
      BackgroundPainter painter;
      if (clearImages || usingDefaultImage)
      {
         painter = new ColourBackgroundPainter();
      }
      else
      {
         painter = new LastTileSetBackgroundPainter();
      }

      // Tile Holder Renderer
      TileHolderRenderer holderRenderer = new StaticTileHolderRenderer(tileSpace);

      // Animated Tiles, Speed and Transition
      String animatedTilesCountString = settings.getProperty("animatedTiles");
      int animatedTilesCount = 400;
      try
      {
         animatedTilesCount = Integer.parseInt(animatedTilesCountString);
      }
      catch (NumberFormatException e)
      {}

      String slideSpeedString = settings.getProperty("slideSpeed");
      int slideSpeed = 600;
      try
      {
         slideSpeed = Integer.parseInt(slideSpeedString);
      }
      catch (NumberFormatException e)
      {}

      Transition transition = new SlideTransition(animatedTilesCount, slideSpeed);

      // Transition Interval
      String transitionIntervalString = settings.getProperty("transitionInterval");
      int transitionInterval = 1000;
      try
      {
         transitionInterval = Integer.parseInt(transitionIntervalString) * 1000;
      }
      catch (NumberFormatException e)
      {}

      // Target Frame Rate
      String targetFrameRateString = settings.getProperty("targetFrameRate");
      int targetFrameRate = 75;
      try
      {
         targetFrameRate = Integer.parseInt(targetFrameRateString);
      }
      catch (NumberFormatException e)
      {}

      // Put it all together
      PipelineComponents components =
         new PipelineComponents(tileSpace, imageSource, tileRenderer, placer, chooser, painter,
                                holderRenderer, transition);
      this.pipeline = new Pipeline(components, targetFrameRate, transitionInterval);
      if (painter instanceof PipelineTransitionListener)
         pipeline.addTransitionListener((PipelineTransitionListener) painter);
      this.renderer = new ComponentPipelineRenderer();
      pipeline.addFrameListener(this);
   }

   private BufferedImage
   createDefaultImage(TileSpace tileSpace)
   {
      int width = tileSpace.getWidth();
      int height = tileSpace.getHeight();
      BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
      Graphics2D graphics = image.createGraphics();
      graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

      GradientPaint paint = new GradientPaint(width * 3 / 7, height / 7, Color.white,
                                              width * 4 / 7, height,
                                              new Color(0, 0, 128));
      graphics.setPaint(paint);
      graphics.fillRect(0, 0, width, height);

      graphics.setColor(Color.black);

      String headingText = "Image Tiles";
      Font headingFont = new Font("SansSerif", Font.BOLD, 48);
      graphics.setFont(headingFont);
      FontMetrics headingFontMetrics = graphics.getFontMetrics();
      int xOffset = width / 20;
      int yOffset = xOffset + headingFontMetrics.getAscent() - headingFontMetrics.getLeading();
      graphics.drawString(headingText, xOffset, yOffset);

      int strokeWidth = 3;
      yOffset += headingFontMetrics.getDescent() + strokeWidth;
      graphics.setStroke(new BasicStroke(strokeWidth));
      graphics.drawLine(xOffset, yOffset, width - xOffset, yOffset);
      yOffset += strokeWidth;
//      yOffset += headingFontMetrics.getDescent();

      String urlText = "http://image-tiles.dev.java.net/";
      Font urlFont = headingFont.deriveFont(headingFont.getSize2D() * 2 / 5);
      graphics.setFont(urlFont);
      FontMetrics urlFontMetrics = graphics.getFontMetrics();
      yOffset += urlFontMetrics.getLeading() + urlFontMetrics.getAscent();
      graphics.drawString(urlText, xOffset, yOffset);

      String errorText = "Please select a directory containing images.";
      Font errorFont = urlFont;
      graphics.setFont(errorFont);
      FontMetrics errorFontMetrics = graphics.getFontMetrics();
      yOffset += (errorFontMetrics.getLeading() + errorFontMetrics.getAscent()) * 2;
      graphics.drawString(errorText, xOffset, yOffset);

      graphics.dispose();

      return image;
   }

   public void
   advancedFrame(Pipeline pipeline)
   {
      renderer.render(pipeline, screen, (Graphics2D) screen.getGraphics());
   }

   public void
   paint(Graphics graphics)
   {
      if (!pipeline.isRunning())
         pipeline.start();
   }

   public static void
   main(String[] args)
   {
      ScreensaverFrame testFrame = new ScreensaverFrame(new ImageTilesScreensaver(), "");
      testFrame.setVisible(true);
   }
}