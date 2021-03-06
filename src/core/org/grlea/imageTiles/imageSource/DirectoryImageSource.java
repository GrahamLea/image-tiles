package org.grlea.imageTiles.imageSource;

// $Id: DirectoryImageSource.java,v 1.2 2005-03-31 20:49:13 grlea Exp $
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

import org.grlea.imageTiles.ImageSource;
import org.grlea.graphics.ImageFileFilter;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import javax.imageio.ImageIO;

/**
 * <p>Sources images from a specified directory. A <code>DirectoryImageSource</code> searches the
 * directory using a given {@link FileFilter}, and can search subdirectories as well. Images are
 * returned randomly. Searching of the directory occurs as part of the constructor. Each image is
 * loaded only when it is to be returned.</p>
 *
 * @author grlea
 * @version $Revision: 1.2 $
 */
public class
DirectoryImageSource
implements ImageSource
{
   private final File[] imageFiles;

   private final boolean randomOrder;

   private final Random random;

   private int nextImageFileIndex = 0;

   public
   DirectoryImageSource(File directory)
   throws IOException
   {
      this(directory, true, true, true);
   }

   public
   DirectoryImageSource(File directory, boolean randomOrder, boolean randomFirstImage)
   throws IOException
   {
      this(directory, randomOrder, randomFirstImage, true);
   }

   public
   DirectoryImageSource(File directory, FileFilter fileFilter, boolean randomOrder,
                        boolean randomFirstImage)
   throws IOException
   {
      this(directory, fileFilter, randomOrder, randomFirstImage, true);
   }

   public
   DirectoryImageSource(File directory, boolean randomOrder, boolean randomFirstImage,
                        boolean searchSubdirectories)
   throws IOException
   {
      this(directory, new ImageFileFilter(), randomOrder, randomFirstImage, searchSubdirectories);
   }

   public
   DirectoryImageSource(File directory, FileFilter fileFilter, boolean randomOrder,
                        boolean randomFirstImage, boolean searchSubdirectories)
   throws IOException
   {
      if (!searchSubdirectories)
      {
         imageFiles = directory.listFiles(fileFilter);
      }
      else
      {
         ArrayList imageFilesList = new ArrayList();
         HashSet visitedDirectories = new HashSet();
         FileFilter directoryFilter = new DirectoryFilter(visitedDirectories);
         visitDirectory(directory, fileFilter, imageFilesList, visitedDirectories, directoryFilter);
         imageFiles = (File[]) imageFilesList.toArray(new File[imageFilesList.size()]);
      }

      if (imageFiles == null)
         throw new IOException("Error reading contents of " + directory.getAbsolutePath());
      
      if (imageFiles.length == 0)
         throw new IOException("No image files found in " + directory.getAbsolutePath());

      this.randomOrder = randomOrder;
      if (randomOrder || randomFirstImage)
         random = new Random();
      else
         random = null;

      if (randomFirstImage)
         nextImageFileIndex = random.nextInt(imageFiles.length);
   }

   private void
   visitDirectory(File directory, FileFilter fileFilter, ArrayList imageFilesList,
                  HashSet visitedDirectories, FileFilter directoryFilter)
   {
      File[] files = directory.listFiles(fileFilter);
      for (int i = 0; i < files.length; i++)
      {
         File file = files[i];
         imageFilesList.add(file);
      }
      visitedDirectories.add(directory);

      File[] directoriesToVisit = directory.listFiles(directoryFilter);
      for (int i = 0; i < directoriesToVisit.length; i++)
      {
         File subdirectory = directoriesToVisit[i];
         visitDirectory(subdirectory, fileFilter, imageFilesList, visitedDirectories,
                        directoryFilter);
      }
   }

   public BufferedImage
   getImage()
   {
      BufferedImage image = null;

      do
      {
         int imageFileIndex;
         if (randomOrder)
         {
            imageFileIndex = nextImageFileIndex;
            if (imageFiles.length != 1)
            {
               // Choose a *different* random image for next time
               while (nextImageFileIndex == imageFileIndex)
                  nextImageFileIndex = random.nextInt(imageFiles.length);
            }
         }
         else
         {
            if (nextImageFileIndex >= imageFiles.length)
               nextImageFileIndex = 0;
            imageFileIndex = nextImageFileIndex;
            nextImageFileIndex++;
         }
         File imageFile = imageFiles[imageFileIndex];
         try
         {
            image = ImageIO.read(imageFile);
         }
         catch (IOException e)
         {}
      }
      while (image == null);

      return image;
   }

   private static final class
   DirectoryFilter
   implements FileFilter
   {
      private HashSet visitedDirectories;

      public
      DirectoryFilter(HashSet visitedDirectories)
      {
         this.visitedDirectories = visitedDirectories;
      }

      public boolean
      accept(File file)
      {
         return file.isDirectory() && !visitedDirectories.contains(file);
      }
   }
}