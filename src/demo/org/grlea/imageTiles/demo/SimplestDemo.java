package org.grlea.imageTiles.demo;

// $Id: SimplestDemo.java,v 1.3 2004-09-05 23:10:55 grlea Exp $
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
import org.grlea.imageTiles.swing.AnimatedTileCanvas;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * <p>An application demonstrating the simplest possible use of Image Tiles. The application
 * displays one image in an {@link AnimatedTileCanvas}.</p>
 *
 * <p>The only line of Image Tiles code in this class is:<pre>
 * AnimatedTileCanvas tileCanvas = new AnimatedTileCanvas(new Pipeline(image), true);
 * </pre></p>
 *
 * @author grlea
 * @version $Revision: 1.3 $
 */
public class 
SimplestDemo
{
   private static final String USAGE =
      "usage: <jvm> " + SimplestDemo.class.getName() + " [imageResourceName]";

   private static final String DEFAULT_IMAGE = "images/George.jpg";

   public static void
   main(String[] argv)
   {
      // Use default image if none specified.
      if (argv.length == 0)
         argv = new String[] {DEFAULT_IMAGE};

      // Parse arguments.
      if (argv.length != 1)
         usage();

      // Argument 1: Image
      String imageResourceName = argv[0];
      System.err.println("imageResourceName = " + imageResourceName);
      URL imageUrl = SimplestDemo.class.getClassLoader().getResource(imageResourceName);
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

      AnimatedTileCanvas tileCanvas = new AnimatedTileCanvas(new Pipeline(image), true);

      final JFrame frame = new JFrame("Image Tiles : SimplestDemo");
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

//      while (true)
//      {
//         try
//         {
//            int availableVideoMemory =
//               frame.getGraphicsConfiguration().getDevice().getAvailableAcceleratedMemory() / 1024;
//            System.out.println("availableVideoMemory = " + availableVideoMemory);
//            Thread.sleep(5000);
//         }
//         catch (InterruptedException ie)
//         {}
//      }
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