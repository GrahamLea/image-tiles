package org.grlea.imageTiles.demo;

// $Id: SimpleDemo.java,v 1.2 2004-09-04 07:59:36 grlea Exp $
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

import org.grlea.graphics.ImageFileFilter;
import org.grlea.imageTiles.ImageTilesDefaults;
import org.grlea.imageTiles.ImageTilesHelper;
import org.grlea.imageTiles.TileRenderer;
import org.grlea.imageTiles.TileSpace;
import org.grlea.imageTiles.Transition;
import org.grlea.imageTiles.imageSource.DirectoryImageSource;
import org.grlea.imageTiles.imageSource.SingleImageSource;
import org.grlea.imageTiles.ImageSource;
import org.grlea.imageTiles.pipeline.Pipeline;
import org.grlea.imageTiles.pipeline.PipelineComponents;
import org.grlea.imageTiles.render.BevelEdgeDecorator;
import org.grlea.imageTiles.render.DecorativeTileRenderer;
import org.grlea.imageTiles.render.Decorator;
import org.grlea.imageTiles.render.GlareDecorator;
import org.grlea.imageTiles.render.RoundedCornerDecorator;
import org.grlea.imageTiles.swing.AnimatedTileCanvas;
import org.grlea.imageTiles.transition.SlideTransition;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * <p>A small application demonstrating the use of a few simple Image Tiles components to create an
 * animated slideshow. The application uses an {@link AnimatedTileCanvas} to display either one
 * image or a directory of images. The size of the tiles, the gap between them, the number of
 * animated tiles and the frame rate of the pipeline can all (optionally) be specified on the
 * command line. When they are not, the demo uses Image Tiles classes to choose appropriate
 * defaults.</p>
 *
 * <p>This class demonstrates the use of:
 * <ul>
 * <li>{@link ImageTilesHelper}</li>
 * <li>{@link ImageTilesDefaults}</li>
 * <li>{@link SingleImageSource}</li>
 * <li>{@link DirectoryImageSource}</li>
 * <li>{@link DecorativeTileRenderer}</li>
 * <li>{@link SlideTransition}</li>
 * <li>{@link PipelineComponents}</li>
 * <li>{@link Pipeline}</li>
 * <li>{@link AnimatedTileCanvas}</li>
 * </ul></p>
 *
 * <p>Some of the Image Tiles code in this class demonstrates:<br><br>
 * 
 * Creating an {@link ImageSource}:<pre>
      if (!imageFile.isDirectory())
         imageSource = new SingleImageSource(imageFile);
      else
         imageSource = new DirectoryImageSource(imageFile, new ImageFileFilter(), true);
 * </pre>
 * Choosing a Tile size:<pre>
      int tileSize = ImageTilesHelper.chooseAppropriateTileSize(image);
      int gapSize = ImageTilesHelper.chooseAppropriateGapSize(tileSize);
 * </pre>
 * Creating a TileSpace:<pre>
      if (!imageFile.isDirectory())
         tileSpace = ImageTilesHelper.createTileSpace(image, tileSize, gapSize);
      else
         tileSpace = ImageTilesHelper.createTileSpace(spaceDimension, tileSize, gapSize);
 * </pre>
 * Creating a DecorativeTileRenderer:<pre>
      Decorator[] tileDecorators = {
         new BevelEdgeDecorator(tileSpace),
         new RoundedCornerDecorator(tileSpace),
         new GlareDecorator(tileSpace),
      };
      TileRenderer tileRenderer = new DecorativeTileRenderer(tileDecorators);
 * </pre>
 * Creating a Pipeline and an AnimatedTileCanvas:<pre>
      Transition transition = new SlideTransition(animatedTiles, tileSize / 3);
      PipelineComponents components =
         new PipelineComponents(tileSpace, imageSource, tileRenderer, transition);
      Pipeline pipeline = new Pipeline(components);
      AnimatedTileCanvas tileCanvas = new AnimatedTileCanvas(pipeline, true);
 * </pre></p>
 *
 * @author grlea
 * @version $Revision: 1.2 $
 */
public class
SimpleDemo
{
   private static final String USAGE =
      "usage: <jvm> " + SimpleDemo.class.getName() +
      " <imageFile>|<imageDirectory> [tileSize] [gapSize] [animatedTiles] [frameRate]";

   public static void
   main(String[] argv)
   {
      final JFrame frame = new JFrame("Image Tiles : SimpleDemo");
      Rectangle screenBounds = frame.getGraphicsConfiguration().getBounds();

      // Parse arguments.
      if (argv.length < 1 || argv.length > 5)
         usage();

      // Argument 1: Image
      String imageFilename = argv[0];
      File imageFile = new File(imageFilename);
      if (!imageFile.exists())
         usage("File '" + imageFilename + "' not found.");

      boolean singleImage = !imageFile.isDirectory();

      ImageSource imageSource;
      BufferedImage image = null;
      if (singleImage)
      {
         try
         {
            image = ImageIO.read(imageFile);
            if (image == null)
               throw new IOException("Failed to load image.");

            if (image.getWidth() > screenBounds.width ||
                image.getHeight() > screenBounds.getHeight())
            {
               // Scale the image down if it's bigger than the screen
               float scaleDownRatioX = (float) screenBounds.width / image.getWidth();
               float scaleDownRatioY = (float) screenBounds.height / image.getHeight();
               float scaleDownRatio = Math.min(scaleDownRatioX, scaleDownRatioY) * 0.8F;
               int newWidth = (int) (image.getWidth() * scaleDownRatio);
               int newHeight = (int) (image.getHeight() * scaleDownRatio);
               BufferedImage newImage = new BufferedImage(newWidth, newHeight, image.getType());
               Graphics2D newGraphics = newImage.createGraphics();
               newGraphics.scale(scaleDownRatio, scaleDownRatio);
               newGraphics.drawImage(image, 0, 0, null);
               newGraphics.dispose();
               image = newImage;
            }

            imageSource = new SingleImageSource(image);
         }
         catch (IOException e)
         {
            usage("Error reading image: " + e);
            throw new IllegalStateException("Failed to exit.");
         }
      }
      else
      {
         try
         {
            imageSource = new DirectoryImageSource(imageFile, new ImageFileFilter(), true);
         }
         catch (IOException e)
         {
            usage("Error reading images: " + e);
            throw new IllegalStateException("Failed to exit.");
         }
      }

      // Arguments 2, 3, 4: tileSize, animatedFrames, frameRate
      int tileSize;
      if (singleImage)
      {
         tileSize = ImageTilesHelper.chooseAppropriateTileSize(image);
      }
      else
      {
         tileSize = screenBounds.width / 32;
      }

      if (argv.length > 1)
      {
         try
         {
            tileSize = Integer.parseInt(argv[1]);
            if (tileSize < 1)
               usage("Illegal tileSize value: " + tileSize);
         }
         catch (NumberFormatException e)
         {
            usage("Illegal tileSize value (" + argv[1] + "): " + e);
         }
      }

      int gapSize = ImageTilesHelper.chooseAppropriateGapSize(tileSize);

      if (argv.length > 2)
      {
         try
         {
            gapSize = Integer.parseInt(argv[2]);
            if (gapSize < 0)
               usage("Illegal gapSize value: " + gapSize);
         }
         catch (NumberFormatException e)
         {
            usage("Illegal gapSize value (" + argv[2] + "): " + e);
         }
      }

      TileSpace tileSpace;
      if (singleImage)
      {
         tileSpace = ImageTilesHelper.createTileSpace(image, tileSize, gapSize);
      }
      else
      {
         int spaceSize = (int) ((screenBounds.height + screenBounds.height) / 2 * 0.8F);
         Dimension spaceDimension = new Dimension(spaceSize, spaceSize);
         tileSpace = ImageTilesHelper.createTileSpace(spaceDimension, tileSize, gapSize);
      }

      int animatedTiles = (int) (Math.sqrt(tileSpace.getRows() * tileSpace.getColumns()));

      if (argv.length > 3)
      {
         try
         {
            animatedTiles = Integer.parseInt(argv[3]);
            if (animatedTiles < 1)
               usage("Illegal animatedTiles value: " + animatedTiles);
         }
         catch (NumberFormatException e)
         {
            usage("Illegal animatedTiles value (" + argv[3] + "): " + e);
         }
      }

      int frameRate = ImageTilesDefaults.DEFAULT_FRAME_RATE;

     if (argv.length > 4)
      {
         try
         {
            frameRate = Integer.parseInt(argv[4]);
            if (frameRate < 1)
               usage("Illegal frameRate value: " + frameRate);
         }
         catch (NumberFormatException e)
         {
            usage("Illegal frameRate value (" + argv[4] + "): " + e);
         }
      }

      Decorator[] tileDecorators = {
         new BevelEdgeDecorator(tileSpace),
         new RoundedCornerDecorator(tileSpace),
         new GlareDecorator(tileSpace),
      };
      TileRenderer tileRenderer = new DecorativeTileRenderer(tileDecorators);

      Transition transition = new SlideTransition(animatedTiles, tileSize / 3);

      PipelineComponents components =
         new PipelineComponents(tileSpace, imageSource, tileRenderer, transition);
      Pipeline pipeline = new Pipeline(components);
      AnimatedTileCanvas tileCanvas = new AnimatedTileCanvas(pipeline, true);

//      pipeline.addFrameListener(new FrameRateListener());

      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setResizable(false);

      Container contentPane = frame.getContentPane();
      contentPane.setLayout(new BorderLayout());
      contentPane.add(tileCanvas, BorderLayout.CENTER);

      SwingUtilities.invokeLater(new Runnable()
      {
         public void
         run()
         {
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
         }
      });
   }

   private static void
   usage(String message)
   {
      System.err.println(message);
      usage();
   }

   private static void
   usage()
   {
      System.err.println(USAGE);
      System.exit(-1);
   }
}