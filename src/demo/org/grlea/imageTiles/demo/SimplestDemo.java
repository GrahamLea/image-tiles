package org.grlea.imageTiles.demo;

// $Id: SimplestDemo.java,v 1.1 2004-08-29 22:22:09 grlea Exp $
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
SimplestDemo
{
   private static final String USAGE =
      "usage: <jvm> " + SimplestDemo.class.getName() + " <imageResourceName>";

   public static void
   main(String[] argv)
   {
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

      final AnimatedTileCanvas tileCanvas = new AnimatedTileCanvas(image);

      final JFrame frame = new JFrame("Image Tiles : SimplestAnimationDemo");
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