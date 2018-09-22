/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simphy;

import javax.swing.JTable;

/**
 *
 * @author Madeyedexter
 */
public class JTableCircle extends JTable
{
    private final Class[] types; 
    public JTableCircle()
    {
        this.types = new Class [] 
        {
            java.lang.String.class, String.class
        };
        this.setModel(new javax.swing.table.DefaultTableModel
        (
        new Object [][] {
        {"Item ID", ""},
        {"Radius", "0.0"},
        {"Center", "0,0"},
        {"Mass", "0"},
        {"Color", "0,0,0"},
        {"Velocity(X,Y)", "0,0"},
        {"Accelaration", "0,0"}
        },
        new String [] 
        {
            "Property", "Value"
        }
        )
        );
        
    }

        @Override
        public Class getColumnClass(int columnIndex) 
        {
            return types [columnIndex];
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) 
        {
            if(columnIndex==0)
                return false;
            if(columnIndex==1 && rowIndex==8)
                return false;
            return columnIndex==1 && rowIndex>=1;
        }
    
    
}