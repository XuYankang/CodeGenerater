package eddy.frame.navigator;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import resources.Resources;
import eddy.database.DatabaseConnection;
import eddy.generate.config.ProjectConstant;

/**
 * 新建项目对话框
 * @author Eddy
 *
 */
public class NewProjectDialog {
	
	private JFrame parent = null;
	private JDialog dialog = null;
	private boolean ok = false;
	private JButton finishBtn = null;
	private JButton cancelBtn = null;
	private JButton nextBtn = null;
	private JButton backBtn = null;
	private CardLayout card = null; // CardLayout布局管理器	
	private JPanel centerPane;
	private JPanel contentPane = new JPanel();
	
	private JTextField projectDirectionTF;
	private JTextField projectNameTF;
	private JComboBox visionBox;
	private JComboBox mvcTypeBox;
	private DBConnectionCombo dbCombo;
	
	public NewProjectDialog(JFrame parent) {
		dialog = new JDialog(parent);
		dialog.setTitle("New Project");
		this.parent = parent;
		
		contentPane.setLayout(new BorderLayout());
		contentPane.add(createCenterPane(), BorderLayout.CENTER);
		contentPane.add(createOperatePane(), BorderLayout.SOUTH);
		dialog.setContentPane(contentPane);
		dialog.setSize(630, 300);
	}
	
	private JPanel createTipPane(String tipHtml) {
		JPanel northPane = new JPanel();
		northPane.setBackground(Color.WHITE);
		JLabel label = new JLabel(tipHtml) {
			private static final long serialVersionUID = 8236085626747866787L;
			public Dimension getPreferredSize() {
                return new Dimension(500, 30);
            }
            public Dimension getMinimumSize() {
                return new Dimension(500, 30);
            }
            public Dimension getMaximumSize() {
                return new Dimension(500, 30);
            }
        };
        
        label.setVerticalAlignment(SwingConstants.CENTER);
        label.setHorizontalAlignment(SwingConstants.CENTER);
		
        northPane.add(label);
		return northPane;
	}
	
	private JPanel createCenterPane() {
		centerPane = new JPanel();
		card = new CardLayout();
		centerPane.setLayout(card);
		centerPane.add(createProjectInfoPane(), "info");
		centerPane.add(createIbatisSetPane(), "ibatis");
		centerPane.add(createMVCPane(), "MVC Setting");
		return centerPane;
	}
	
	private JPanel createProjectInfoPane() {
		JPanel projectJnfoPane = new JPanel();
		String topText = "<html>\n" +
        "<b><font size=+1 color=green>工程配置</font></b>\n" +
        "\n";
		
		projectJnfoPane.setLayout(new BorderLayout());
		projectJnfoPane.add(createTipPane(topText), BorderLayout.NORTH);
		
		JPanel cp = new JPanel();
		Box vbox = Box.createVerticalBox();
		
		Box hbox = Box.createHorizontalBox();
		JLabel lb = new JLabel("Project Name");
		lb.setFont(Resources.getResources().getSysFont_CN());
		projectNameTF = new JTextField(70);
		projectNameTF.setFont(Resources.getResources().getSysFont_CN());
		hbox.add(lb);
		hbox.add(Box.createHorizontalStrut(10));
		hbox.add(projectNameTF);
		vbox.add(hbox);
		
		lb = new JLabel("Direction");
		lb.setFont(Resources.getResources().getSysFont_CN());
		projectDirectionTF = new JTextField(40);
		projectDirectionTF.setFont(Resources.getResources().getSysFont_CN());
		projectDirectionTF.setText(new File("").getAbsolutePath() + "\\temp");
		JButton browsBtn = new JButton("Brows");
		browsBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser(new File("."));
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int r = fileChooser.showSaveDialog(null);
				
				if (r == JFileChooser.APPROVE_OPTION) {
					File f = fileChooser.getSelectedFile();
					projectDirectionTF.setText(f.getPath());
				}
			}
		});
		
		hbox = Box.createHorizontalBox();
		hbox.add(lb);
		hbox.add(Box.createHorizontalStrut(30));
		hbox.add(projectDirectionTF);
		hbox.add(Box.createHorizontalStrut(10));
		hbox.add(browsBtn);
		vbox.add(hbox);
		
		vbox.add(hbox);
		vbox.add(Box.createGlue());
		cp.add(vbox, BorderLayout.CENTER);
		
		projectJnfoPane.add(cp);
		
		return projectJnfoPane;
	}
	
	private JPanel createIbatisSetPane() {
		JPanel ibatisSetPane = new JPanel();
		String topText = "<html>\n" +
        "<b><font size=+1 color=green>Ibatis配置</font></b>\n" +
        "\n";
		
		ibatisSetPane.setLayout(new BorderLayout());
		ibatisSetPane.add(createTipPane(topText), BorderLayout.NORTH);
		
		JPanel cp = new JPanel();
		cp.setLayout(null);
		int startX = 10;
		int startY = 10;
		int xgap = 130;
		int ygap = 45;
		
		JLabel lb = new JLabel("Ibatis Type");
		lb.setBounds(startX, startY, 80, 30);
		cp.add(lb);
		
		lb.setFont(Resources.getResources().getSysFont_CN());
		String[] visions = {ProjectConstant.Ibatis_Type_mybatis};
		visionBox = new JComboBox(visions);
		visionBox.setFont(Resources.getResources().getSysFont_EN());
		visionBox.setBounds(startX + xgap, startY, 460, 30);
		cp.add(visionBox);
		
		lb = new JLabel("Default Connecton");
		lb.setFont(Resources.getResources().getSysFont_CN());
		
		lb.setBounds(startX, startY+=ygap, 130, 30);
		cp.add(lb);
		
		dbCombo = new DBConnectionCombo();
		dbCombo.setBounds(startX + xgap, startY, 460, 30);
		cp.add(dbCombo);
		ibatisSetPane.add(cp, BorderLayout.CENTER);
		return ibatisSetPane;
	}
	
	private JPanel createMVCPane() {
		JPanel mvcpane = new JPanel();
		
		String topText = "<html>\n" +
        "<b><font size=+1 color=green>MVC 类型配置</font></b>\n" +
        "\n";
		
		mvcpane.setLayout(new BorderLayout());
		mvcpane.add(createTipPane(topText), BorderLayout.NORTH);
		
		JPanel cp = new JPanel();
		cp.setLayout(null);
		int startX = 10;
		int startY = 10;
		
		JLabel lb = new JLabel("MVC Type");
		lb.setBounds(startX, startY, 70, 30);
		cp.add(lb);
		
		lb.setFont(Resources.getResources().getSysFont_CN());
		String[] visions = {ProjectConstant.MVCType_Spring};
		mvcTypeBox = new JComboBox(visions);
		mvcTypeBox.setFont(Resources.getResources().getSysFont_EN());
		mvcTypeBox.setBounds(startX + lb.getWidth(), startY, 460, 30);
		cp.add(mvcTypeBox);
		
		mvcpane.add(cp, BorderLayout.CENTER);
		
		return mvcpane;
	}
	
	private JPanel createOperatePane() {
		JPanel operatePane = new JPanel();
		backBtn = new JButton("< back");
		backBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				card.previous(centerPane);
			}
		});
		
		nextBtn = new JButton("> next");
		nextBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				card.next(centerPane);
			}
		});
		
		finishBtn = new JButton(" finish ");
		finishBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ok = true;
				dialog.setVisible(false);
			}
		});
		
		cancelBtn = new JButton(" cancel ");
		cancelBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dialog.setVisible(false);
			}
		});
		
		operatePane.add(backBtn);
		operatePane.add(nextBtn);
		operatePane.add(finishBtn);
		operatePane.add(cancelBtn);
		return operatePane;
	}
	
	/**
	 * 显示向导对话框
	 * @return
	 */
	public boolean showDialog() {
		dialog.setModal(true);
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		int x = d.width/2 - dialog.getWidth()/2;
		int y = d.height/2 - dialog.getHeight()/2;
		
		if(parent != null) {
			Point pl = parent.getLocation();
			x = (int) pl.getX() + (parent.getWidth() - dialog.getWidth())/2;
			y = (int) pl.getY() + (parent.getHeight() - dialog.getHeight())/2;
		}
		
		dialog.setLocation(x, y);
		dialog.setVisible(true);
		return ok;
	}
	
	public String getProjectName() {
		return projectNameTF.getText();
	}
	
	public String getProjectDirection() {
		return projectDirectionTF.getText();
	}
	
	public String getIbatisType() {
		return visionBox.getSelectedItem().toString();
	}
	
	public String getMVCType() {
		return mvcTypeBox.getSelectedItem().toString();
	}
	
	public DatabaseConnection getDefaultConnection() {
		return (DatabaseConnection) dbCombo.getSelectedItem();
	}
}
