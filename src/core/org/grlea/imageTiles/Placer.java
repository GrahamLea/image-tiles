package org.grlea.imageTiles;

// $Id: Placer.java,v 1.2 2004-09-04 07:59:18 grlea Exp $
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

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * <p>Places an image within a tile space. A placer may perform many operations on the image as it
 * "places" it, including scaling, rotating and colour manipulation.</p>
 *
 * @author grlea
 * @version $Revision: 1.2 $
 */
public interface
Placer
{
   void
   place(TileSpace tileSpace, BufferedImage sourceImage, Graphics2D targetGraphics);
}