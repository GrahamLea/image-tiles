package org.grlea.imageTiles.demo;

// $Id: InsideApplicationDemo.java,v 1.2 2004-08-24 04:56:19 grlea Exp $
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
import org.grlea.imageTiles.ImageSource;
import org.grlea.imageTiles.TileRenderer;
import org.grlea.imageTiles.TileSpace;
import org.grlea.imageTiles.Animator;
import org.grlea.imageTiles.animate.SlideAnimator;
import org.grlea.imageTiles.imageSource.SequentialImageSource;
import org.grlea.imageTiles.render.PlainTileRenderer;
import org.grlea.imageTiles.swing.easy.BasicAnimatedTileCanvas;
import org.pietschy.explicit.TableBuilder;
import org.pietschy.explicit.align.Align;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * <p></p>
 *
 * @author grlea
 * @version $Revision: 1.2 $
 */
public class
InsideApplicationDemo
extends JFrame
{
   private static final String INSTALLER_HEADER_IMAGE = "Installer Header.jpg";

   private static final String[] INSTALLER_IMAGES =
      {"Installer Image Blue.gif", "Installer Image Red.gif", "Installer Image Green.gif"};

   private static final int ANIMATED_TILES = 20;

   private static final int ANIMATION_SPEED = 8;

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
      BasicAnimatedTileCanvas tileCanvas =
         new BasicAnimatedTileCanvas(tileSpace, imageSource, renderer, animator);
      tileCanvas.setBorder(BorderFactory.createEtchedBorder());

      JLabel stageProgressLabel = new JLabel("Copying files...");
      JLabel totalProgressLabel = new JLabel("Total:");
      stageProgressLabel.setAlignmentX(0);

      JProgressBar stageProgressBar = new JProgressBar(0, 100);
      stageProgressBar.setValue(95);
      stageProgressBar.setStringPainted(true);

      JProgressBar totalProgressBar = new JProgressBar(0, 100);
      totalProgressBar.setValue(80);
      totalProgressBar.setStringPainted(true);

      JComponent horizontalRule = new JComponent() {};
      horizontalRule.setPreferredSize(new Dimension(100, 2));
      horizontalRule.setBorder(BorderFactory.createEtchedBorder());

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
      middleBuilder.add(tileCanvas, 0, 0).fillX().fillY();
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
      mainBuilder.add(horizontalRule, 2, 0).alignX(Align.FILL).alignCentre();
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

      tileCanvas.start();
   }

   public static void
   main(String[] args)
   throws IOException, UnsupportedLookAndFeelException
   {
      UIManager.setLookAndFeel(new PlasticLookAndFeel());
      new InsideApplicationDemo().setVisible(true);
   }
}