package org.grlea.imageTiles;

// $Id: TileHolderRenderer.java,v 1.2 2005-03-19 00:11:36 grlea Exp $
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

/**
 * <p>Renders the contents of a {@link org.grlea.imageTiles.TileHolder}. As it is assumed that all
 * <code>TileHolderRenderer</code>s will want to listen to the TileHolder they are supposed to be
 * rendering, <code>TileHolderRenderer</code> is an extension of {@link TileHolderListener}. The
 * <code>TileHolderRenderer</code> is automatically registered to listen to the current TileHolder
 * within the Pipeline.</p>
 *
 * @author grlea
 * @version $Revision: 1.2 $
 */
public interface
TileHolderRenderer
extends TileHolderListener
{
   void
   advanceFrame(long timeSinceLastFrame);

   void
   render(TileHolder tileHolder, Graphics2D graphics);
}