package org.grlea.imageTiles.demo;

// $Id: SimpleAnimationDemo.java,v 1.2 2004-08-27 01:21:20 grlea Exp $
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

import org.grlea.imageTiles.AnimationController;
import org.grlea.imageTiles.Animator;
import org.grlea.imageTiles.Chooser;
import org.grlea.imageTiles.ImageTilesHelper;
import org.grlea.imageTiles.Painter;
import org.grlea.imageTiles.Placer;
import org.grlea.imageTiles.Scaler;
import org.grlea.imageTiles.TileRenderer;
import org.grlea.imageTiles.TileSet;
import org.grlea.imageTiles.TileSpace;
import org.grlea.imageTiles.animate.SlideAnimator;
import org.grlea.imageTiles.choose.RandomChooser;
import org.grlea.imageTiles.paint.StaticPainter;
import org.grlea.imageTiles.place.CentrePlacer;
import org.grlea.imageTiles.render.BevelEdgeDecorator;
import org.grlea.imageTiles.render.DecorativeTileRenderer;
import org.grlea.imageTiles.render.Decorator;
import org.grlea.imageTiles.render.GlareDecorator;
import org.grlea.imageTiles.render.RoundedCornerDecorator;
import org.grlea.imageTiles.scale.FullScaler;
import org.grlea.imageTiles.swing.AnimatedTileCanvas;
//import org.grlea.imageTiles.swing.AnimatedTileCanvasRenderTask;
//import org.grlea.imageTiles.swing.TileCanvas;
import org.grlea.util.FixedRateScheduler;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Point;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

/**
 * <p></p>
 *
 * @author grlea
 * @version $Revision: 1.2 $
 */
public class 
SimpleAnimationDemo
{
//   private static final String USAGE =
//      "usage: <jvm> " + SimpleAnimationDemo.class.getName() +
//      " <imageResourceName> [tileSize] [gapSize] [animatedTiles] [frameRate]";
//
//   private static final int DEFAULT_TILESIZE = 32;
//   private static final int DEFAULT_GAP = 1;
//   private static final int DEFAULT_ANIMATED_TILES = 10;
//   private static final int DEFAULT_FRAME_RATE = 50;
//
//   private static final int ANIMATION_SPEED = 6;
//
//   public static void
//   main(String[] argv)
//   {
//      // Parse arguments.
//      if (argv.length < 1 || argv.length > 5)
//         usage();
//
//      // Argument 1: Image
//      String imageResourceName = argv[0];
//      System.err.println("imageResourceName = " + imageResourceName);
//      URL imageUrl = SimpleAnimationDemo.class.getClassLoader().getResource(imageResourceName);
//      if (imageUrl == null)
//         usage("Image '" + imageResourceName + "' not found.");
//
//      BufferedImage image = null;
//      try
//      {
//         image = ImageIO.read(imageUrl);
//      }
//      catch (IOException e)
//      {
//         usage("Error reading image: " + e);
//      }
//      if (image == null)
//         usage("Failed to read image.");
//
//      // Arguments 2, 3, 4: tileSize, animatedFrames, frameRate
//      int tileSize = DEFAULT_TILESIZE;
//      int gapSize = DEFAULT_GAP;
//      int animatedTiles = DEFAULT_ANIMATED_TILES;
//      int frameRate = DEFAULT_FRAME_RATE;
//
//      if (argv.length > 1)
//      {
//         try
//         {
//            tileSize = Integer.parseInt(argv[1]);
//            if (tileSize < 1)
//               usage("Illegal tileSize value: " + tileSize);
//         }
//         catch (NumberFormatException e)
//         {
//            usage("Illegal tileSize value (" + argv[1] + "): " + e);
//         }
//      }
//
//      if (argv.length > 2)
//      {
//         try
//         {
//            gapSize = Integer.parseInt(argv[2]);
//            if (gapSize < 0)
//               usage("Illegal gapSize value: " + gapSize);
//         }
//         catch (NumberFormatException e)
//         {
//            usage("Illegal gapSize value (" + argv[2] + "): " + e);
//         }
//      }
//
//      if (argv.length > 3)
//      {
//         try
//         {
//            animatedTiles = Integer.parseInt(argv[3]);
//            if (animatedTiles < 1)
//               usage("Illegal animatedTiles value: " + animatedTiles);
//         }
//         catch (NumberFormatException e)
//         {
//            usage("Illegal animatedTiles value (" + argv[3] + "): " + e);
//         }
//      }
//
//     if (argv.length > 4)
//      {
//         try
//         {
//            frameRate = Integer.parseInt(argv[4]);
//            if (frameRate < 1)
//               usage("Illegal frameRate value: " + frameRate);
//         }
//         catch (NumberFormatException e)
//         {
//            usage("Illegal frameRate value (" + argv[4] + "): " + e);
//         }
//      }
//
//      TileSpace tileSpace = ImageTilesHelper.createTileSpace(image, tileSize, DEFAULT_GAP);
//
//      Scaler scaler = new FullScaler();
//      Placer placer = new CentrePlacer();
//
//      Decorator[] tileDecorators = {
//         new BevelEdgeDecorator(tileSpace),
//         new RoundedCornerDecorator(tileSpace),
//         new GlareDecorator(tileSpace),
//      };
//      TileRenderer tileRenderer = new DecorativeTileRenderer(tileDecorators);
//
//      BufferedImage scaledImage = scaler.scale(image, tileSpace);
//      Point position = placer.choosePosition(scaledImage, tileSpace);
//
//      TileSet tileSet = new TileSet(tileSpace, scaledImage, position, tileRenderer);
//
//      Chooser chooser = new RandomChooser(tileSpace);
//      Painter painter = new StaticPainter(tileSpace);
//      Animator animator = new SlideAnimator(tileSpace, animatedTiles, ANIMATION_SPEED);
//      final AnimationController controller =
//         new AnimationController(tileSet, chooser, animator, painter);
//
//      final TileCanvas canvas = new AnimatedTileCanvas(tileSpace, controller);
//      canvas.setOpaque(true);
//
//      JFrame frame = new JFrame("Image Tiles : SimpleAnimationDemo");
//      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//      frame.setResizable(false);
//
//      Container contentPane = frame.getContentPane();
//      contentPane.setLayout(new BorderLayout());
//      contentPane.add(canvas, BorderLayout.CENTER);
//
//      final Object LOCK = new Object();
//      frame.addWindowListener(new WindowAdapter()
//      {
//         public void
//         windowOpened(WindowEvent e)
//         {
//            synchronized (LOCK)
//            {
//               LOCK.notify();
//            }
//         }
//      });
//
//      synchronized (LOCK)
//      {
//         frame.pack();
//         frame.setLocationRelativeTo(null);
//         frame.setVisible(true);
//         try
//         {
//            LOCK.wait();
//         }
//         catch (InterruptedException e)
//         {
//            throw new IllegalStateException("UnexpectedException: " + e);
//         }
//      }
//
//      AnimatedTileCanvasRenderTask renderTask = new AnimatedTileCanvasRenderTask(controller, canvas);
//      FixedRateScheduler scheduler = new FixedRateScheduler(renderTask, frameRate);
//      scheduler.start();
//   }
//
//   private static void
//   usage(String message)
//   {
//      System.err.println(message);
//      usage();
//   }
//
//   private static void
//   usage()
//   {
//      System.err.println(USAGE);
//      System.exit(-1);
//   }
}