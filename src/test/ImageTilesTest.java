package test;

// $Id: ImageTilesTest.java,v 1.1 2004-08-20 05:25:43 grlea Exp $
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
import org.grlea.imageTiles.render.BorderDecorator;
import org.grlea.imageTiles.render.DecorativeTileRenderer;
import org.grlea.imageTiles.render.Decorator;
import org.grlea.imageTiles.render.GlareDecorator;
import org.grlea.imageTiles.render.RoundedCornerDecorator;
import org.grlea.imageTiles.scale.ReduceOnlyScaler;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.GraphicsConfiguration;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

/**
 * <p></p>
 *
 * @author grlea
 * @version $Revision: 1.1 $
 */
public class
ImageTilesTest
{
   private static final int TILESIZE = 24;
   private static final int GAP = 1;
   private static final int ANIMATED_TILES = 12;
   private static final int ANIMATION_SPEED = 10;

   private static final int FRAME_RATE_HZ = 64;
   private static final long FRAME_TIME_MILLIS = (long) (1000F / FRAME_RATE_HZ);

   public static void
   main(String[] argv)
   throws IOException
   {
      String imageFileName = argv[0];
      File imageFile = new File(imageFileName);
      System.out.println("imageFileName = " + imageFileName);
      System.out.println("imageFile = " + imageFile);
      final BufferedImage image = ImageIO.read(imageFile);

      JFrame frame = new JFrame("Image Tiles Test");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      Rectangle bounds = frame.getGraphicsConfiguration().getBounds();
      bounds.width *= 0.9;
      bounds.height *= 0.9;
      TileSpace tileSpace = TileSpace.createTileSpace(bounds, TILESIZE, GAP);
      StaticPainter tilePainter = new StaticPainter(tileSpace);

      Dimension tileSpaceSize = tileSpace.getSize();
      Canvas canvas = new Canvas();
      canvas.setSize(tileSpaceSize.width, tileSpaceSize.height);
      canvas.setBackground(Color.black);
      Container contentPane = frame.getContentPane();
      contentPane.setLayout(new BorderLayout());
      contentPane.add(canvas, BorderLayout.CENTER);

      final Object LOCK = new Object();
      frame.addWindowListener(new WindowAdapter()
      {
         public void
         windowOpened(WindowEvent e)
         {
            synchronized (LOCK)
            {
               LOCK.notify();
            }
         }
      });

      synchronized (LOCK)
      {
//         frame.setLocation(100, 100);
         frame.pack();
         frame.setVisible(true);
         try
         {
            LOCK.wait();
         }
         catch (InterruptedException e)
         {
            throw new IllegalStateException("UnexpectedException: " + e);
         }
      }


      Scaler scaler = new ReduceOnlyScaler();
//      Scaler scaler = new ThresholdScaler();
//      Scaler scaler = new FullScaler();
      Placer positionChooser = new CentrePlacer();

//      TileRenderer tileRenderer = new PlainTileRenderer();

      Decorator[] tileDecorators = {
         new BevelEdgeDecorator(tileSpace),
         new RoundedCornerDecorator(tileSpace),
         new GlareDecorator(tileSpace),
//         new BorderDecorator(tileSpace, new Color(128,0,0,128))
      };
      TileRenderer tileRenderer = new DecorativeTileRenderer(tileDecorators);

      // Scale the image if necessary.
      BufferedImage scaledImage = scaler.scale(image, tileSpace);
      Point position = positionChooser.choosePosition(scaledImage, tileSpace);

      TileSet tileSet = new TileSet(tileSpace, scaledImage, position, tileRenderer);

//      Chooser chooser = new SequentialChooser(tileSpace);
      Chooser chooser = new RandomChooser(tileSpace);

//      Animator animator = new BasicAnimator();
      Animator animator = new SlideAnimator(tileSpace, ANIMATED_TILES, ANIMATION_SPEED);
      Image canvasBuffer = canvas.createImage(tileSpaceSize.width, tileSpaceSize.height);
      Graphics2D canvasBufferGraphics = (Graphics2D) canvasBuffer.getGraphics();
      Graphics canvasGraphics = canvas.getGraphics();
      AnimationController controller =
         new AnimationController(tileSet, chooser, animator, tilePainter, canvasBufferGraphics);

      System.out.println("Starting AnimationController");
      int framesDrawn = 0;
      long startTime = System.currentTimeMillis();
//      long totalFrameDrawingTime = 0;



      while (!controller.isAnimationComplete())
      {
         // TODO (grahaml) Obviously animation needs to be fixed! FPS doesn't work.
//         long startFrameTime = System.currentTimeMillis();
         long lastFrameDrawTime = System.currentTimeMillis();
         controller.drawFrame();
         canvasGraphics.drawImage(canvasBuffer, 0, 0, null);
//         long endFrameTime = System.currentTimeMillis();
//         long frameDrawTime = endFrameTime - startFrameTime;
//         totalFrameDrawingTime += frameDrawTime;
//         System.out.println("frameDrawTime = " + frameDrawTime);

         long nextFrameDrawTime = lastFrameDrawTime + FRAME_TIME_MILLIS;
//         int count = 0;
         long currentTime = System.currentTimeMillis();
         do
         {
            long timeToSleep = (long) (0.85 * (nextFrameDrawTime - currentTime));
            if (timeToSleep > 0 )
            {
               try
               {
//                  count++;
                  Thread.sleep(timeToSleep);
               }
               catch (InterruptedException ie)
               {}
            }
            currentTime = System.currentTimeMillis();
         }
         while (currentTime < nextFrameDrawTime);
//         System.out.println("       Sleep count: " + count);
         framesDrawn++;
      }
      long endTime = System.currentTimeMillis();

      System.out.println("DONE!");
      float totalTimeSeconds = (endTime - startTime) / 1000F;
      float framesPerSecond = framesDrawn / totalTimeSeconds;
      System.out.println("framesPerSecond = " + framesPerSecond);

//      System.out.println("totalTimeSeconds = " + totalTimeSeconds);
//      System.out.println("totalFrameDrawingTime = " + (totalFrameDrawingTime / 1000F));
   }
}