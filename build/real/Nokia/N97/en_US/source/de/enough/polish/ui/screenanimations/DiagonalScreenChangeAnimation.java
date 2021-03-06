//#condition polish.usePolishGui

/*
 * Created on 27-May-2005 at 18:54:36.
 * 
 * Copyright (c) 2010 Robert Virkus / Enough Software
 *
 * This file is part of J2ME Polish.
 *
 * J2ME Polish is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * J2ME Polish is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with J2ME Polish; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * 
 * Commercial licenses are also available, please
 * refer to the accompanying LICENSE.txt or visit
 * http://www.j2mepolish.org for details.
 */
package de.enough.polish.ui.screenanimations;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import de.enough.polish.ui.Color;
import de.enough.polish.ui.ScreenChangeAnimation;
import de.enough.polish.ui.Style;

/**
 * <p>Moves the new screen diagonally over the old screen.</p>
 * <p>Activate this animation by specifying it in the corresponding screen's style:
 * <pre>
 * .myAlert {
 * 		screen-change-animation: diagonal;
 * 		diagonal-screen-change-animation-speed: 4; ( 2 is default )
 * 		diagonal-screen-change-animation-move-previous: true; ( false is default )
 * }
 * </pre>
 * </p>
 *
 * <p>Copyright (c) Enough Software 2005 - 2009</p>
 * <pre>
 * history
 *        27-May-2005 - rob creation
 * </pre>
 * @author Robert Virkus, j2mepolish@enough.de
 */
public class DiagonalScreenChangeAnimation extends ScreenChangeAnimation {
	private int currentX;
	private int currentY;
	//#if polish.css.diagonal-screen-change-animation-speed
		//# private int speed = -1;
	//#endif
	//#if polish.css.diagonal-screen-change-animation-move-previous
		//# private boolean movePrevious;
	//#endif
	//#if polish.css.diagonal-screen-change-animation-background-color
		//# private int backgroundColor = 0xffffff;
	//#endif

	/**
	 * Creates a new animation 
	 */
	public DiagonalScreenChangeAnimation() {
		super();
	}

	
	/* (non-Javadoc)
	 * @see de.enough.polish.ui.ScreenChangeAnimation#setStyle(de.enough.polish.ui.Style)
	 */
	protected void setStyle(Style style)
	{
		super.setStyle(style);
		if (this.isForwardAnimation) {
			this.currentX = 0;
			this.currentY = 0;
		} else {
			this.currentX = this.screenWidth;
			this.currentY = this.screenHeight;
		}
		//#if polish.css.diagonal-screen-change-animation-speed
			//# Integer speedInt = style.getIntProperty( 221 );
			//# if (speedInt != null ) {
				//# this.speed = speedInt.intValue();
			//# }
		//#endif
		//#if polish.css.diagonal-screen-change-animation-move-previous
			//# Boolean movePreviousBool = style.getBooleanProperty(222);
			//# if (movePreviousBool != null) {
				//# this.movePrevious = movePreviousBool.booleanValue();
			//# }
		//#endif
		//#if polish.css.diagonal-screen-change-animation-background-color
			//# Color color = style.getColorProperty( 223 );
			//# if (color != null) {
				//# this.backgroundColor = color.getColor();
			//# }
		//#endif	
	}


	/* (non-Javadoc)
	 * @see de.enough.polish.ui.ScreenChangeAnimation#animate()
	 */
	protected boolean animate() {
		if (this.isForwardAnimation) {
			if (this.currentY < this.screenHeight) {
				//#if polish.css.diagonal-screen-change-animation-speed
					//# if (this.speed != -1) {
						//# this.currentY += this.speed;
					//# } else {
				//#endif
						int adjust = (this.screenHeight - this.currentY) / 3;
						if (adjust < 3) {
							adjust = 3;
						}
						this.currentY += adjust;
				//#if polish.css.diagonal-screen-change-animation-speed
					//# }
				//#endif
				this.currentX = ( this.currentY * this.screenWidth ) / this.screenHeight;
				return true;
			} 
		} else {
			if (this.currentY > 0) {
				//#if polish.css.diagonal-screen-change-animation-speed
					//# if (this.speed != -1) {
						//# this.currentY -= this.speed;
					//# } else {
				//#endif
						int adjust = (this.screenHeight - this.currentY) / 3;
						if (adjust < 3) {
							adjust = 3;
						}
						this.currentY -= adjust;
				//#if polish.css.diagonal-screen-change-animation-speed
					//# }
				//#endif
				this.currentX = ( this.currentY * this.screenWidth ) / this.screenHeight;
				return true;
			} 

		}
		//#if polish.css.diagonal-screen-change-animation-speed
			//# this.speed = -1;
		//#endif
		return false;
	}

	/* (non-Javadoc)
	 * @see javax.microedition.lcdui.Canvas#paint(javax.microedition.lcdui.Graphics)
	 */
	public void paintAnimation(Graphics g) {
		int y = 0;
		int x = 0;
		//#if polish.css.diagonal-screen-change-animation-move-previous
			//# if (this.movePrevious) {
				//#if polish.css.diagonal-screen-change-animation-background-color
					//# g.setColor( this.backgroundColor );
				//#else
					//# g.setColor( 0xffffff );
				//#endif
				//# g.fillRect( 0, 0, this.screenWidth, this.screenHeight );
				//# x = -this.currentX;
				//# y = -this.currentY;
			//# }
		//#endif
		Image first;
		Image second;
		if (this.isForwardAnimation) {
			first = this.lastCanvasImage;
			second = this.nextCanvasImage;
		} else {
			first = this.nextCanvasImage;
			second = this.lastCanvasImage;
			
		}
		g.drawImage( first, x, y, Graphics.TOP | Graphics.LEFT );
		g.drawImage( second, this.screenWidth - this.currentX, this.screenHeight -  this.currentY, Graphics.TOP | Graphics.LEFT );
	}

}
