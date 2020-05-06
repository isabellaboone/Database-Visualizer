package Gui;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import database.RetrieveManipulateInformation;

public class AddCharacter extends JFrame{
  RetrieveManipulateInformation rmi = null;
  String name, user;
  int maxHP, curHP, str, stam, loc;
  public AddCharacter(RetrieveManipulateInformation rmi) {
    this.rmi = rmi; 
    // JFrame settings
    setLayout(new GridBagLayout());
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setLocationRelativeTo(null); 
    
    // Add Labels & Buttons
    JLabel[] prompt = {
        new JLabel("Enter Character Name:"),
        new JLabel("Enter MaxHP: "),
        new JLabel("Enter current HP: "),
        new JLabel("Enter strength: "),
        new JLabel("Enter stamina: "),
        new JLabel("Enter location: "),
        new JLabel("Enter username: ")
    };
    
    JTextField[] txt = {
        new JTextField("Name"),
        new JTextField("000"),
        new JTextField("000"),
        new JTextField("000"),
        new JTextField("000"),
        new JTextField("000000000"),
        new JTextField("Username")
    };
    
    
    GridBagConstraints c = new GridBagConstraints(); 
    c.gridx = 0;
    c.gridy = 0;
    c.weightx = 0.35;
    c.weighty = 0.10; 
    
    for(int i = 0; i < prompt.length; i++) {
      add(prompt[i]);
      c.gridx = 1;
      add(txt[i]);
      c.gridy++; 
      c.gridx = 0; 
    }
    
    JButton cont = new JButton("Continue");
    add(cont, c); 
    
    addButtons(txt, cont);
    
    pack(); 
    setVisible(true);
    
  }
  
  private void addButtons(JTextField[] txt, JButton b) {
    b.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        name = txt[0].getText();
        maxHP = Integer.parseInt(txt[1].getText());
        curHP = Integer.parseInt(txt[2].getText());
        str = Integer.parseInt(txt[3].getText());
        str = Integer.parseInt(txt[4].getText());
        loc = Integer.parseInt(txt[5].getText());
        user = txt[6].getText();
        
        // check make sure each number is valid
        if(maxHP < 0) {
          fail("Max HP value invalid");
          return;
        } else if (curHP < 0) {
          fail("Current HP value invalid");
          return;
        } else if (str < 0) {
          fail("Strength value invalid"); 
          return;
        } else if(stam < 0) {
          fail("Stamina value invalid");
          return;
        }
        
        // check to make sure name is valid and doesn't exist
        if(rmi.userNameExists(name)) {
          fail("Player name already exists");
          return;
        }
        
        // check if the location exists
        if(!(rmi.locationExists(String.valueOf(loc)))) {
          fail("Location does not exist");
          return;
        }
        
        // check to see if the user exists
        if(!(rmi.playerExists(user))) {
          fail("Username does not exist");
          return;
        }
        
        // Finally, we've check all our failure conditions
        // and can properly add what the user inputs
        
        
        
      }
    });
    
    
    
    // These action listeners just print whenever you
    // press enter on a text field
    
    // name
    txt[0].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        System.out.println(txt[0].getText());
      }
    });
    
    // max hp
    txt[1].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        System.out.println(txt[1].getText());
      }
    });
    
    // current hp
    txt[2].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        System.out.println(txt[2].getText());
      }
    });
    
    // strength
    txt[3].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        System.out.println(txt[3].getText());
      }
    });
    
    // stamina
    txt[4].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        System.out.println(txt[4].getText());
      }
    });
    
    // location
    txt[5].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        System.out.println(txt[5].getText());
      }
    });
    
    // username
    txt[6].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        System.out.println(txt[6].getText());
      }
    });
  }
  
  private void fail(String reason) {
    JFrame frame = new JFrame("Error");
    frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    frame.setSize(200,200);
    frame.setLocationRelativeTo(null);
    JLabel bad = new JLabel("Add failed - " + reason);
    JButton cont = new JButton("Ok");
    cont.addActionListener(e -> frame.dispose());
    
    frame.setLayout(new FlowLayout());
    frame.add(bad);
    frame.add(cont);
    frame.pack();
    frame.setVisible(true);
    
  }
}