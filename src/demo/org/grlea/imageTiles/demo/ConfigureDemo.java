package org.grlea.imageTiles.demo;

// $Id: ConfigureDemo.java,v 1.1 2004-09-04 07:59:36 grlea Exp $
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

import org.grlea.imageTiles.swing.AnimatedTileIcon;
import org.grlea.imageTiles.swing.configure.ConfigurationModel;
import org.grlea.imageTiles.swing.configure.ConfigurationPanel;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

/**
 * <p></p>
 *
 * @author grlea
 * @version $Revision: 1.1 $
 */
public class 
ConfigureDemo
extends JFrame
{
   public
   ConfigureDemo()
   {
      super("Image Tiles : ConfigureDemo");

      // Components
      final ConfigurationModel configurationModel = new ConfigurationModel();
      ConfigurationPanel configurationPanel = new ConfigurationPanel(configurationModel);

//      final JPanel animationContainer = new JPanel(new BorderLayout());
      final JLabel animationLabel = new JLabel();

      // Test code to initialise ConfigurationModel
      String userHome = System.getProperty("user.home");
      File myPicturesDirectory = new File(userHome + "/My Documents/My Pictures");
      configurationModel.setDirectory(myPicturesDirectory);
      configurationModel.setUseSingleImage(false);
      JButton goButton = new JButton("Go!");
      goButton.addActionListener(new ActionListener()
      {
         public void
         actionPerformed(ActionEvent e)
         {
            AnimatedTileIcon animatedTileIcon =
               configurationModel.getAnimatedTileIcon(animationLabel);
            animationLabel.setIcon(animatedTileIcon);
            animatedTileIcon.getPipeline().start();
         }
      });

      // Layout
      Container contentPane = getContentPane();
      contentPane.setLayout(new BorderLayout());
      contentPane.add(configurationPanel, BorderLayout.LINE_START);
      contentPane.add(animationLabel, BorderLayout.CENTER);
      contentPane.add(goButton, BorderLayout.PAGE_END);

      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
   }

   public static void
   main(String[] args)
   {
      final ConfigureDemo configureDemo = new ConfigureDemo();
      SwingUtilities.invokeLater(new Runnable()
      {
         public void
         run()
         {
            configureDemo.pack();
            configureDemo.setLocationRelativeTo(null);
            configureDemo.setVisible(true);
         }
      });
   }
}