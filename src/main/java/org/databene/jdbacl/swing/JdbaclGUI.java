/*
 * (c) Copyright 2011 by Volker Bergmann. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, is permitted under the terms of the
 * GNU General Public License (GPL).
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * WITHOUT A WARRANTY OF ANY KIND. ALL EXPRESS OR IMPLIED CONDITIONS,
 * REPRESENTATIONS AND WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE
 * HEREBY EXCLUDED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package org.databene.jdbacl.swing;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;

import org.databene.commons.ConnectFailedException;
import org.databene.commons.FileUtil;
import org.databene.commons.IOUtil;
import org.databene.commons.ImportFailedException;
import org.databene.commons.OrderedMap;
import org.databene.commons.SystemInfo;
import org.databene.commons.ui.ApplicationUtil;
import org.databene.commons.ui.JavaApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple GUI for database browsing.<br/><br/>
 * Created: 07.11.2011 16:28:47
 * @since 0.7.0
 * @author Volker Bergmann
 */
@SuppressWarnings("serial")
public class JdbaclGUI extends JFrame implements JavaApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(JdbaclGUI.class);
	
	private static final String DATABENE_DIRECTORY_NAME = SystemInfo.getUserHome() + File.separator + "databene";
	private static final String GUI_PROPERTIES_FILE_NAME = 
		DATABENE_DIRECTORY_NAME + File.separator + "JdbaclGUI.properties";

	private EnvironmentSelector environmentSelector;
	private DatabasePane databasePane;

	private JTextField exclusionField;

	public static void main(String[] args) throws IOException {
		ApplicationUtil.prepareNativeLAF("jdbacl");
		JdbaclGUI appAndFrame = new JdbaclGUI();
		ApplicationUtil.configureApplication(appAndFrame);
		appAndFrame.setVisible(true);
	}

	public JdbaclGUI() {
		this.exclusionField = new JTextField();
		this.environmentSelector = new EnvironmentSelector();
		this.databasePane = new DatabasePane(new TextFieldValueProvider(exclusionField));
		this.environmentSelector.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				String environment = environmentSelector.getSelectedItem();
				try {
					databasePane.setEnvironment(environment);
				} catch (ConnectFailedException ex) {
					LOGGER.error("Error importing environment " + environment, ex);
				} catch (ImportFailedException ex) {
					LOGGER.error("Error importing environment " + environment, ex);
				}
			}
		});
		createMenuBar();
		createToolBar();
		getContentPane().add(databasePane, BorderLayout.CENTER);
		setSize(800, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		restoreState();
	}
	
	private void createToolBar() {
		JToolBar toolBar = new JToolBar();
		add(toolBar, BorderLayout.NORTH);
		toolBar.add(new JLabel("Exclusion:"));
		toolBar.add(exclusionField);
		toolBar.add(Box.createHorizontalStrut(8));
		toolBar.add(new JLabel("Environment:"));
		toolBar.add(environmentSelector);
		toolBar.add(Box.createHorizontalGlue());
		toolBar.setFloatable(false);
	}

	private void createMenuBar() {
	    JMenuBar menubar = new JMenuBar();
	    
	    // create file menu
	    JMenu fileMenu = new JMenu("File");
	    fileMenu.setMnemonic('F');
	    menubar.add(fileMenu);
	    if (!SystemInfo.isMacOsx())
	    	fileMenu.add(new AbstractAction("Exit") {
				public void actionPerformed(ActionEvent evt) {
					exit();
				}
	    	});
	    
	    // create edit menu
	    JMenu editMenu = new JMenu("Edit");
	    editMenu.setMnemonic('E');
	    menubar.add(editMenu);

	    // create help menu
	    JMenu helpMenu = new JMenu("Help");
	    editMenu.setMnemonic('H');
	    menubar.add(helpMenu);
	    if (!SystemInfo.isMacOsx())
	    	helpMenu.add(new AbstractAction("About") {
				public void actionPerformed(ActionEvent evt) {
					about();
				}
	    	});
	    
		setJMenuBar(menubar);
    }

	public void about() {
		JOptionPane.showMessageDialog(this, 
				"DB Sanity GUI " + SystemInfo.getLineSeparator() + // include version info
				"(c) 2011 by Volker Bergmann");
    }

	public void exit() {
		saveState();
		System.exit(0);
	}

	private void saveState() {
		try {
			FileUtil.ensureDirectoryExists(new File(DATABENE_DIRECTORY_NAME));
			Map<String, String> props = new OrderedMap<String, String>();
			props.put("exclusionPattern", exclusionField.getText());
			IOUtil.writeProperties(props, GUI_PROPERTIES_FILE_NAME);
		} catch (IOException e) {
			// writing the file failed but isn't tragic
		}
	}

	private void restoreState() {
		try {
			Map<String, String> props = IOUtil.readProperties(GUI_PROPERTIES_FILE_NAME);
			exclusionField.setText(props.get("exclusionPattern"));
		} catch (Exception e) {
			// no file defined yet, use default settings
		}
	}

}
