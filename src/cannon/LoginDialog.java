package com.dralasoft.gui.taskmanager;

import com.dralasoft.gui.common.DralaDialog;
import com.dralasoft.workflow.interfaces.Engine;
import com.dralasoft.workflow.interfaces.Resource;
import com.dralasoft.workflow.interfaces.ResourceManager;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRootPane;
import javax.swing.JTextField;

public class LoginDialog
  extends DralaDialog
{
  private ResourceBundle resources = null;
  private String CMD_CANCEL = "cmd.cancel";
  private String CMD_HELP = "cmd.help";
  private String CMD_LOGIN = "cmd.login";
  private Engine eng;
  private JButton loginButton = null;
  private JTextField userNameTextField;
  private JLabel serverNameLabel;
  private JLabel serverPort;
  private JLabel serverEngineLabel;
  private JLabel serverEngineID;
  private JPasswordField password;
  private Frame parent;
  
  public LoginDialog(Frame parent, Engine eng)
  {
    super(parent);
    this.parent = parent;
    this.eng = eng;
    initResources();
    initComponents();
    pack();
  }
  
  private void initResources()
  {
    this.resources = ResourceBundle.getBundle("com.dralasoft.resource.properties.taskmanageri18n");
  }
  
  private void initComponents()
  {
    Container contents = getContentPane();
    
    contents.setLayout(new GridBagLayout());
    
    setTitle(this.resources.getString("dialog.title"));
    
    addWindowListener(new WindowAdapter()
    {
      public void windowClosing(WindowEvent event)
      {
        LoginDialog.this.doCancel();
      }
    });
    this.userNameTextField = new JTextField();
    this.userNameTextField.setText(this.resources.getString("userNameTextField.initial"));
    
    JLabel userNameLabel = new JLabel();
    userNameLabel.setDisplayedMnemonic(this.resources.getString("userNameTextField.label").charAt(0));
    
    userNameLabel.setLabelFor(this.userNameTextField);
    userNameLabel.setText(this.resources.getString("userNameTextField.label"));
    
    contents.add(userNameLabel, new GridBagConstraints(0, 0, 1, 1, 0.0D, 0.0D, 17, 0, new Insets(12, 12, 0, 0), 0, 0));
    
    this.userNameTextField.setToolTipText(this.resources.getString("userNameTextField.tooltip"));
    
    contents.add(this.userNameTextField, new GridBagConstraints(1, 0, 1, 1, 1.0D, 0.0D, 10, 2, new Insets(12, 7, 0, 11), 0, 0));
    
    this.password = new JPasswordField();
    this.password.setText(this.resources.getString("passwordField.initial"));
    
    JLabel passwordLabel = new JLabel();
    passwordLabel.setDisplayedMnemonic(this.resources.getString("passwordField.mnemonic").charAt(0));
    
    passwordLabel.setText(this.resources.getString("passwordField.label"));
    passwordLabel.setLabelFor(this.password);
    
    contents.add(passwordLabel, new GridBagConstraints(0, 1, 1, 1, 0.0D, 0.0D, 17, 0, new Insets(11, 12, 0, 0), 0, 0));
    
    this.password.setToolTipText(this.resources.getString("passwordField.tooltip"));
    
    Font echoCharFont = new Font("Lucida Sans", 0, 12);
    this.password.setFont(echoCharFont);
    this.password.setEchoChar('*');
    
    contents.add(this.password, new GridBagConstraints(1, 1, 1, 1, 1.0D, 0.0D, 13, 2, new Insets(11, 7, 0, 11), 0, 0));
    
    JPanel buttonPanel = createButtonPanel();
    
    contents.add(buttonPanel, new GridBagConstraints(0, 3, 2, 1, 0.0D, 0.0D, 13, 0, new Insets(17, 12, 11, 11), 0, 0));
    
    getRootPane().setDefaultButton(this.loginButton);
    addCancelByEscapeKey();
  }
  
  public void keyPressed(KeyEvent e) {}
  
  public void keyReleased(KeyEvent e) {}
  
  public void keyTyped(KeyEvent e) {}
  
  private JPanel createButtonPanel()
  {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, 0));
    
    this.loginButton = new JButton();
    this.loginButton.setText(this.resources.getString("loginButton.label"));
    this.loginButton.setToolTipText(this.resources.getString("loginButton.tooltip"));
    this.loginButton.setActionCommand(this.CMD_LOGIN);
    this.loginButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent event)
      {
        LoginDialog.this.doLogin();
      }
    });
    panel.add(this.loginButton);
    
    panel.add(Box.createRigidArea(new Dimension(5, 0)));
    
    JButton cancelButton = new JButton();
    cancelButton.setText(this.resources.getString("cancelButton.label"));
    cancelButton.setActionCommand(this.CMD_CANCEL);
    cancelButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent event)
      {
        LoginDialog.this.doCancel();
      }
    });
    panel.add(cancelButton);
    
    panel.add(Box.createRigidArea(new Dimension(5, 0)));
    
    JButton helpButton = new JButton();
    helpButton.setMnemonic(this.resources.getString("helpButton.mnemonic").charAt(0));
    
    helpButton.setText(this.resources.getString("helpButton.label"));
    helpButton.setActionCommand(this.CMD_HELP);
    helpButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent event)
      {
        LoginDialog.this.doHelp();
      }
    });
    panel.add(helpButton);
    
    Vector buttons = new Vector(3);
    buttons.add(cancelButton);
    buttons.add(helpButton);
    buttons.add(this.loginButton);
    equalizeComponentSizes(buttons);
    buttons.removeAllElements();
    
    return panel;
  }
  
  private void equalizeComponentSizes(List components)
  {
    int i = 0;
    Dimension maxPreferred = new Dimension(0, 0);
    JComponent oneComponent = null;
    Dimension thisPreferred = null;
    for (i = 0; i < components.size(); i++)
    {
      oneComponent = (JComponent)components.get(i);
      thisPreferred = oneComponent.getPreferredSize();
      maxPreferred.width = Math.max(maxPreferred.width, (int)thisPreferred.getWidth());
      
      maxPreferred.height = Math.max(maxPreferred.height, (int)thisPreferred.getHeight());
    }
    for (i = 0; i < components.size(); i++)
    {
      oneComponent = (JComponent)components.get(i);
      oneComponent.setPreferredSize((Dimension)maxPreferred.clone());
      oneComponent.setMaximumSize((Dimension)maxPreferred.clone());
    }
  }
  
  public Engine getEngine()
  {
    return this.eng;
  }
  
  private void doLogin()
  {
    try
    {
      ResourceManager rm = this.eng.getResourceManager();
      
      String uid = this.userNameTextField.getText();
      if (rm.authenticate(uid, new String(this.password.getPassword())))
      {
        this.loginSuccess = true;
        
        Resource res = rm.getResource(uid);
        String mainTitle = this.resources.getString("dialog.about_taskmanager.prompt");
        TaskManager.getTaskManager().setTitle(mainTitle + " - " + uid + " (" + res.getName() + ")");
        
        hide();
      }
      else
      {
        JOptionPane.showMessageDialog(this, this.resources.getString("LoginDialog.0"), this.resources.getString("LoginDialog.1"), 0);
      }
    }
    catch (Exception e)
    {
      JOptionPane.showMessageDialog(this, this.resources.getString("LoginDialog.2") + this.resources.getString("LoginDialog.3") + e.getMessage(), this.resources.getString("LoginDialog.4"), 0);
    }
  }
  
  private void doHelp()
  {
    JOptionPane.showMessageDialog(this, this.resources.getString("LoginDialog.5") + this.resources.getString("LoginDialog.6") + this.resources.getString("LoginDialog.7") + this.resources.getString("LoginDialog.8") + this.resources.getString("LoginDialog.9"), this.resources.getString("LoginDialog.10"), 1);
  }
  
  private void doCancel()
  {
    this.cancelled = true;
    setVisible(false);
  }
  
  public String getUser()
  {
    return this.userNameTextField.getText();
  }
  
  public boolean loginSuccess()
  {
    return this.loginSuccess;
  }
  
  public boolean cancelled()
  {
    return this.cancelled;
  }
  
  private boolean loginSuccess = false;
  private boolean cancelled = false;
  
  public void addCancelByEscapeKey()
  {
    InputMap inputMap = getRootPane().getInputMap(1);
    
    inputMap.put(escapeKey, CANCEL_ACTION_KEY);
    AbstractAction cancelAction = new AbstractAction()
    {
      public void actionPerformed(ActionEvent e)
      {
        LoginDialog.this.setVisible(false);
        LoginDialog.this.cancelled = true;
      }
    };
    getRootPane().getActionMap().put(CANCEL_ACTION_KEY, cancelAction);
  }
}
