package org.grlea.imageTiles.demo;

// $Id: InsideApplicationDemo.java,v 1.4 2004-08-27 01:20:12 grlea Exp $
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

import com.jgoodies.plaf.plastic.PlasticLookAndFeel;
import org.grlea.imageTiles.Animator;
import org.grlea.imageTiles.pipeline.ImageSource;
import org.grlea.imageTiles.TileRenderer;
import org.grlea.imageTiles.TileSpace;
import org.grlea.imageTiles.animate.SlideAnimator;
import org.grlea.imageTiles.pipeline.imageSource.SequentialImageSource;
import org.grlea.imageTiles.render.PlainTileRenderer;
import org.grlea.imageTiles.swing.AnimatedTileIcon;
import org.pietschy.explicit.TableBuilder;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSeparator;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * <p></p>
 *
 * @author grlea
 * @version $Revision: 1.4 $
 */
public class
InsideApplicationDemo
extends JFrame
{
   private static final String INSTALLER_HEADER_IMAGE = "Installer Header.jpg";

   private static final String[] INSTALLER_IMAGES =
      {"Installer Image Blue.gif", "Installer Image Red.gif", "Installer Image Green.gif"};

   private static final int ANIMATED_TILES = 10;

   private static final int ANIMATION_SPEED = 5;

   private final AnimatedTileIcon animatedTileIcon;

   public
   InsideApplicationDemo()
   throws IOException
   {
      super("Make Believe Installer");

      // Create components
      URL headerImageUrl = getClass().getClassLoader().getResource(INSTALLER_HEADER_IMAGE);
      BufferedImage image = ImageIO.read(headerImageUrl);
      ImageIcon imageIcon = new ImageIcon(image);
      JLabel headerLabel = new JLabel(imageIcon);

      TileSpace tileSpace = new TileSpace(16, 0, 10, 15);
      ImageSource imageSource = new SequentialImageSource(INSTALLER_IMAGES);
      TileRenderer renderer = new PlainTileRenderer();
      Animator animator = new SlideAnimator(tileSpace, ANIMATED_TILES, ANIMATION_SPEED);
      JLabel animatedTileLabel = new JLabel();
      animatedTileIcon =
         new AnimatedTileIcon(animatedTileLabel, tileSpace, imageSource, renderer, animator);
      animatedTileLabel.setIcon(animatedTileIcon);
      animatedTileLabel.setBorder(BorderFactory.createLineBorder(Color.black));

      JLabel stageProgressLabel = new JLabel("Copying files...");
      JLabel totalProgressLabel = new JLabel("Total:");
      stageProgressLabel.setAlignmentX(0);

      JProgressBar stageProgressBar = new JProgressBar(0, 100);
      stageProgressBar.setValue(95);
      stageProgressBar.setStringPainted(true);

      JProgressBar totalProgressBar = new JProgressBar(0, 100);
      totalProgressBar.setValue(80);
      totalProgressBar.setStringPainted(true);

      JSeparator separator = new JSeparator();
//      separator.setPreferredSize(new Dimension(400, 10));

      JButton backButton = new JButton("< Back");
      JButton nextButton = new JButton("Next >");
      JButton cancelButton = new JButton("Cancel");
      backButton.setEnabled(false);
      nextButton.setEnabled(false);
      cancelButton.addActionListener(new ActionListener()
      {
         public void
         actionPerformed(ActionEvent e)
         {
            System.exit(0);
         }
      });

      // Layout: Progress Bars
      TableBuilder progressBuilder = new TableBuilder();
//      progressBuilder.setBorder(BorderFactory.createEtchedBorder());
      int row = 0;
      progressBuilder.add(Box.createVerticalStrut(10), row++, 0).fillX();
      progressBuilder.add(stageProgressLabel, row++, 0).alignLeft();
      progressBuilder.add(stageProgressBar, row++, 0).fillX();
      progressBuilder.add(Box.createVerticalStrut(20), row++, 0).fillX();
      progressBuilder.add(totalProgressLabel, row++, 0).alignLeft();
      progressBuilder.add(totalProgressBar, row++, 0).fillX();
      progressBuilder.column(0).grow(1);
      progressBuilder.buildLayout();
      JPanel progressPanel = progressBuilder.getPanel();

      // Layout: Middle Panel
      TableBuilder middleBuilder = new TableBuilder();
      middleBuilder.add(animatedTileLabel, 0, 0).fillX().fillY();
      middleBuilder.add(progressPanel, 0, 1).fillX().fillY();
      middleBuilder.row(0).grow(1);
      middleBuilder.column(1).grow(1);
      middleBuilder.buildLayout();
      JPanel middlePanel = middleBuilder.getPanel();

      // Layout: Navigation Buttons
      TableBuilder navigationBuilder = new TableBuilder();
      navigationBuilder.add(backButton, 0, 0);
      navigationBuilder.add(nextButton, 0, 1);
      navigationBuilder.column(1).paddingLeft(0);
      navigationBuilder.add(Box.createHorizontalStrut(10), 0, 3);
      navigationBuilder.add(cancelButton, 0, 4);
      navigationBuilder.buildLayout();
      JPanel navigationPanel = navigationBuilder.getPanel();

      // Layout: Main Panel
      TableBuilder mainBuilder = new TableBuilder();
      mainBuilder.add(headerLabel, 0, 0).fillX().alignTop();
      mainBuilder.add(middlePanel, 1, 0).fillX().fillY();
      mainBuilder.row(1).paddingTop(2);
      mainBuilder.add(separator, 2, 0).fillX().alignMiddle();
      mainBuilder.add(navigationPanel, 3, 0).alignRight().alignBottom();
      mainBuilder.row(1).grow(1);
      mainBuilder.column(0).grow(1);
      mainBuilder.buildLayout();
      JPanel mainPanel = mainBuilder.getPanel();

      getContentPane().add(mainPanel);

      pack();
      setResizable(false);
      setLocationRelativeTo(null);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      addWindowListener(new WindowAdapter()
      {
         public void
         windowClosing(WindowEvent e)
         {
            animatedTileIcon.stop();
         }
      });
   }

   public static void
   main(String[] args)
   throws IOException, UnsupportedLookAndFeelException, InterruptedException,
      InvocationTargetException
   {
      UIManager.setLookAndFeel(new PlasticLookAndFeel());
      final InsideApplicationDemo demo = new InsideApplicationDemo();
      SwingUtilities.invokeAndWait(new Runnable()
      {
         public void run()
         {
            demo.setVisible(true);
         }
      });

      try
      {
         Thread.sleep(2000);
      }
      catch (InterruptedException ie)
      {}
      demo.animatedTileIcon.start();
   }
}