package eddy.frame;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class WindowSnapper extends ComponentAdapter {
	private boolean locked = false;
	private int snap_distance = 10;
	
	public void componentMoved(ComponentEvent evt) {
		if(locked) {
			return;
		}
		
		Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
		int nx = evt.getComponent().getX();
		int ny = evt.getComponent().getY();
		
		if(ny < 0+snap_distance) {
			ny = 0;
		}
		// left
		if(nx < 0+snap_distance) {
			nx = 0;
		}
		// right
		if(nx > size.getWidth( ) - evt.getComponent( ).getWidth( ) -
				snap_distance) {
			nx = (int)size.getWidth( )-evt.getComponent( ).getWidth( );
		}
		// bottom
		if(ny > size.getHeight( ) - evt.getComponent( ).getHeight( ) -
				snap_distance) {
			ny = (int)size.getHeight( )-evt.getComponent( ).getHeight( );
		}

		locked = true;
		evt.getComponent().setLocation(nx, ny);
		locked = false;
	}
}
