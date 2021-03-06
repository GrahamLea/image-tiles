package org.grlea.imageTiles.render;

// $Id: Decorator.java,v 1.2 2004-09-04 07:59:29 grlea Exp $
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

import java.awt.image.BufferedImage;

/**
 * <p>An object that decorates the source image of a Tile.</p>
 *
 * @author grlea
 * @version $Revision: 1.2 $
 */
public interface
Decorator
{
   void
   decorate(BufferedImage tileImage);
}