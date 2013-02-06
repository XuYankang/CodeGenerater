package eddy.frame;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.plaf.basic.BasicButtonUI;

import resources.Resources;
import eddy.frame.docpane.DocController;
import eddy.frame.docpane.EditTextPane;

/**
 * Tab±Í«©∞¥≈•
 * @author Eddy
 *
 */
public class ButtonTabComponent extends JPanel {
	private static final long serialVersionUID = 1L;
	private final JTabbedPane tabPane;
	private int index = 0;
	
	public int getIndex() {
		return index;
	}

	public ButtonTabComponent(final JTabbedPane pane, int index) {
		super(new FlowLayout(FlowLayout.LEFT, 0, 0));
		if (pane == null) {
			throw new NullPointerException("TabbedPane is null");
		}
		this.tabPane = pane;
		this.index = index;
		setOpaque(false);

		JLabel label = new JLabel() {
			private static final long serialVersionUID = 1L;

			public String getText() {
				int i = pane.indexOfTabComponent(ButtonTabComponent.this);
				if (i != -1) {
					return pane.getTitleAt(i);
				}
				return null;
			}
		};
		label.setFont(Resources.getResources().getSysFont_CN());
		label.setForeground(Color.BLACK);
		add(label);
		label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
		
		JButton button = new TabButton();
		add(button);
		setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
	}

	private class TabButton extends JButton implements ActionListener {
		private static final long serialVersionUID = 1L;

		public TabButton() {
			int size = 17;
			setPreferredSize(new Dimension(size, size));
			setToolTipText("close this tab");
			setUI(new BasicButtonUI());
			setContentAreaFilled(false);
			setFocusable(false);
			setBorder(BorderFactory.createEtchedBorder());
			setBorderPainted(false);
			addMouseListener(buttonMouseListener);
			setRolloverEnabled(true);
			addActionListener(this);
		}

		public void actionPerformed(ActionEvent e) {
			int i = tabPane.indexOfTabComponent(ButtonTabComponent.this);
			
			if (i != -1) {
				EditTextPane etp = DocController.getDocController().getEditTextPane(index);
				etp = DocController.getDocController().getEditTextPane(index);
				
				int n = etp.canClose();
				if(n == 0) {
					etp.saveDoc();
					tabPane.remove(tabPane.indexOfTabComponent(ButtonTabComponent.this));
					DocController.getDocController().removeEditTextPane(etp);
				}
				else if(n == 1) {
					tabPane.remove(tabPane.indexOfTabComponent(ButtonTabComponent.this));
					DocController.getDocController().removeEditTextPane(etp);
				} 
				else if(n == 2) {					
				}
			}
		}

		public void updateUI() {
		}

		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g.create();
			if (getModel().isPressed()) {
				g2.translate(1, 1);
			}
			g2.setStroke(new BasicStroke(2));
			g2.setColor(Color.BLACK);
			if (getModel().isRollover()) {
				g2.setColor(Color.MAGENTA);
			}
			int delta = 6;
			g2.drawLine(delta, delta, getWidth() - delta - 1, getHeight()
					- delta - 1);
			g2.drawLine(getWidth() - delta - 1, delta, delta, getHeight()
					- delta - 1);
			g2.dispose();
		}
	}

	private final static MouseListener buttonMouseListener = new MouseAdapter() {
		public void mouseEntered(MouseEvent e) {
			Component component = e.getComponent();
			if (component instanceof AbstractButton) {
				AbstractButton button = (AbstractButton) component;
				button.setBorderPainted(true);
			}
		}

		public void mouseExited(MouseEvent e) {
			Component component = e.getComponent();
			if (component instanceof AbstractButton) {
				AbstractButton button = (AbstractButton) component;
				button.setBorderPainted(false);
			}
		}
	};
}
