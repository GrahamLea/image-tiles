package org.grlea.imageTiles.demo;

// $Id: SimpleDemo.java,v 1.1 2004-08-29 22:22:00 grlea Exp $
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

import org.grlea.imageTiles.ImageTilesHelper;
import org.grlea.imageTiles.TileRenderer;
import org.grlea.imageTiles.TileSpace;
import org.grlea.imageTiles.animate.SlideAnimator;
import org.grlea.imageTiles.pipeline.ImageSource;
import org.grlea.imageTiles.pipeline.imageSource.SingleImageSource;
import org.grlea.imageTiles.render.BevelEdgeDecorator;
import org.grlea.imageTiles.render.DecorativeTileRenderer;
import org.grlea.imageTiles.render.Decorator;
import org.grlea.imageTiles.render.GlareDecorator;
import org.grlea.imageTiles.render.RoundedCornerDecorator;
import org.grlea.imageTiles.swing.AnimatedTileCanvas;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * <p></p>
 *
 * @author grlea
 * @version $Revision: 1.1 $
 */
public class 
SimpleDemo
{
   private static final String USAGE =
      "usage: <jvm> " + SimpleDemo.class.getName() +
      " <imageResourceName> [tileSize] [gapSize] [animatedTiles] [frameRate]";

   private static final int DEFAULT_FRAME_RATE = 50;

   public static void
   main(String[] argv)
   {
      // Parse arguments.
      if (argv.length < 1 || argv.length > 5)
         usage();

      // Argument 1: Image
      String imageResourceName = argv[0];
      URL imageUrl = SimpleDemo.class.getClassLoader().getResource(imageResourceName);
      if (imageUrl == null)
         usage("Image '" + imageResourceName + "' not found.");

      BufferedImage image = null;
      try
      {
         image = ImageIO.read(imageUrl);
      }
      catch (IOException e)
      {
         usage("Error reading image: " + e);
      }
      if (image == null)
         usage("Failed to read image.");

      // Arguments 2, 3, 4: tileSize, animatedFrames, frameRate
      int tileSize = ImageTilesHelper.chooseAppropriateTileSize(image);

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

      TileSpace tileSpace = ImageTilesHelper.createTileSpace(image, tileSize, gapSize);

      int animatedTiles = (int) (Math.sqrt(tileSpace.getRows() * tileSpace.getColumns()) / 2);

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

      int frameRate = DEFAULT_FRAME_RATE;

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

      ImageSource imageSource = new SingleImageSource(image);

      Decorator[] tileDecorators = {
         new BevelEdgeDecorator(tileSpace),
         new RoundedCornerDecorator(tileSpace),
         new GlareDecorator(tileSpace),
      };
      TileRenderer tileRenderer = new DecorativeTileRenderer(tileDecorators);

      int animationSpeed = tileSize / 4;
      SlideAnimator animator = new SlideAnimator(tileSpace, animatedTiles, animationSpeed);

      final AnimatedTileCanvas tileCanvas =
         new AnimatedTileCanvas(tileSpace, imageSource, tileRenderer, animator);

      final JFrame frame = new JFrame("Image Tiles : SimpleDemo");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setResizable(false);

      Container contentPane = frame.getContentPane();
      contentPane.setLayout(new BorderLayout());
      contentPane.add(tileCanvas, BorderLayout.CENTER);

      frame.addWindowListener(new WindowAdapter()
      {
         public void
         windowOpened(WindowEvent e)
         {
            tileCanvas.start();
         }
      });

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