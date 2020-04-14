package Gui;

import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.*;

import database.retrieveInformation;

public class ItemsLayout extends JPanel{
  

  JScrollPane item = new JScrollPane();
  JLabel selected = null;
  retrieveInformation ri = new retrieveInformation(retrieveInformation.getConncetion());
  int[] itemsList = new int[ri.getNumItems()];
  
  public ItemsLayout(){
    itemsList = ri.getAllItems();
    setLayout(new BorderLayout());
    setBackground(Color.GRAY);
    add(Label(),BorderLayout.NORTH);
    //item.add(buildItems(10));
    item.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    item.add(item.createVerticalScrollBar());
    add(item, BorderLayout.CENTER);
    
    item.setViewportView(buildItems(ri.getNumItems()));
    revalidate();
    repaint();
    
  }
  
  private JLabel Label(){
    JLabel label = new JLabel("<html><p style = \"color:black; font-size:15px\">Items</p></html>", SwingConstants.CENTER);
    label.setBackground(Color.WHITE);
    label.setOpaque(true);
    label.setPreferredSize(new Dimension(100,20));
    return label;
  }
  
  private JPanel buildItems(int rows){
    JPanel data = new JPanel();
    data.setLayout(new GridLayout(rows, 1));
    for(int i = 0; i < rows; i++) {
      JLabel label = new JLabel(buildHtml(itemsList[i]));
      label.setOpaque(true);
      label.setBackground(new Color(30, 30, 30));
      label.addMouseListener( new MouseAdapter() {
          @Override
          public void mouseClicked(MouseEvent e) {
              removeSelectedBackground();
              label.setBackground(new Color(234, 201, 55));
              selected = label;
          }
      });
      label.setPreferredSize(new Dimension(20,50));
      data.add(label);
    }
    return data;
  }

  private String buildHtml(int primaryKey){
    String labelText = "";
    ResultSet rs = null;
    try {
      rs = ri.getConncetion().createStatement().executeQuery("SELECT * FROM ITEM WHERE ITEM.ID = " + primaryKey);
      rs.next();
      labelText = "<html><p style = \"color:white;\">Id = " + rs.getInt("Id") + "   Weight = " + rs.getInt("Weight") + "   Volume = " + rs.getInt("Volume") +
          "   LocationId = " + (rs.getInt("LocationId") == 0 ? "null" : "" + rs.getInt("LOCATIONID")) + /*"   CharacterName = " + rs.getString("cName") + */"</p></html>";
      rs.close();
    } catch(SQLException e){
      e.printStackTrace();
    }
    
    
    return labelText;
  }
  
  private void removeSelectedBackground() {
    if(selected != null)
      selected.setBackground(new Color(30, 30, 30));
  }
  
}
