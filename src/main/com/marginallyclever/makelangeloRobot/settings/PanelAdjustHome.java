package com.marginallyclever.makelangeloRobot.settings;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import com.marginallyclever.makelangelo.Translator;
import com.marginallyclever.makelangelo.select.SelectFloat;
import com.marginallyclever.makelangeloRobot.MakelangeloRobot;

public class PanelAdjustHome extends JPanel implements ActionListener, PropertyChangeListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = -84665452555208524L;

	protected MakelangeloRobot robot;

	protected SelectFloat homeX, homeY;

	private JComboBox<String> homePositions;
	private static final String[] homePositionsNames = new String[] { "Custom", "Top", "Center", "Bottom", "Left", "Right", "TopLeft", "BottomLeft", "TopRight", "BottomRight" };

	public PanelAdjustHome( MakelangeloRobot robot) {
		this.robot = robot;

		this.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
		// this.setLayout(new GridLayout(0,1,8,8));
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		GridBagConstraints c = new GridBagConstraints();
		GridBagConstraints d = new GridBagConstraints();
		c.ipadx = 5;
		c.ipady = 0;

		int y = 1;
		JPanel p;
		Dimension s;
		
		// adjust machine size
		{
			p = new JPanel(new GridBagLayout());
			this.add(p);
	
			float w = (float)(robot.getSettings().getLimitRight() - robot.getSettings().getLimitLeft());
			float h = (float)(robot.getSettings().getLimitTop() - robot.getSettings().getLimitBottom());
			
			homePositions = new JComboBox<>(homePositionsNames);
			homePositions.setSelectedIndex(0);

			d.gridx = 0;
			d.gridy = y;
			p.add(homePositions, d);
			y++;
			homePositions.addActionListener(this);
			
			homeX = new SelectFloat((float)robot.getSettings().getHomeX());
			homeY = new SelectFloat((float)robot.getSettings().getHomeY());
			s = homeY.getPreferredSize();
			s.width = 80;
	
			c.gridwidth = 1;
			c.anchor = GridBagConstraints.EAST;
			d.anchor = GridBagConstraints.WEST;
			c.gridx = 0;
			c.gridy = y;
			p.add(new JLabel(Translator.get("homeX")), c);
			d.gridx = 1;
			d.gridy = y;
			p.add(homeX, d);
			d.gridx = 2;
			d.gridy = y;
			p.add(new JLabel("mm"), d);
			y++;
			c.gridx = 0;
			c.gridy = y;
			p.add(new JLabel(Translator.get("homeY")), c);
			d.gridx = 1;
			d.gridy = y;
			p.add(homeY, d);
			d.gridx = 2;
			d.gridy = y;
			p.add(new JLabel("mm"), d);
			y++;
		
			homeX.setPreferredSize(s);
			homeY.setPreferredSize(s);
			homeX.addPropertyChangeListener(this);
			homeY.addPropertyChangeListener(this);
			
			this.add(new JSeparator(SwingConstants.HORIZONTAL));
		}
		
		updateHome();
	}

	public void save() {
		double hX = ((Number)homeX.getValue()).doubleValue();
		double hY = ((Number)homeY.getValue()).doubleValue();

        boolean isDataSane = (  hX > robot.getSettings().getLimitLeft() && hX < robot.getSettings().getLimitRight() && 
                                hY > robot.getSettings().getLimitBottom() && hY < robot.getSettings().getLimitTop());
		if (isDataSane) {
			robot.getSettings().setHome(hX, hY);
		}
	}

	public void updateHome() {
        String newChoice = homePositionsNames[homePositions.getSelectedIndex()];
        
		double pw = robot.getSettings().getPaperWidth();
        double ph = robot.getSettings().getPaperHeight();
                
		switch (newChoice) {
			case "Custom":
				break;
            case "Center":
				homeX.setValue(0.0);
				homeY.setValue(0.0);
                break;
			case "Top":
				homeX.setValue(0.0);
				homeY.setValue(ph/2.0);
				break;
			case "Bottom":
				homeX.setValue(0.0);
				homeY.setValue(-ph/2.0);
				break;
			case "Left":
				homeX.setValue(-pw/2.0);
				homeY.setValue(0.0);
				break;
			case "Right":
				homeX.setValue(pw/2.0);
				homeY.setValue(0.0);
				break;
			case "TopLeft":
				homeX.setValue(-pw/2.0);
				homeY.setValue(ph/2.0);
				break;
			case "TopRight":
				homeX.setValue(pw/2.0);
				homeY.setValue(ph/2.0);
				break;
			case "BottomLeft":
				homeX.setValue(-pw/2.0);
				homeY.setValue(-ph/2.0);
				break;
			case "BottomRight":
				homeX.setValue(pw/2.0);
				homeY.setValue(-ph/2.0);
				break;
			default:
				break;
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		Object subject = evt.getSource();
	}

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
	  
        if(src == homePositions) {
            updateHome();
        }
    }
}
