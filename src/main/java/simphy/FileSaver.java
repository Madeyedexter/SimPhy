/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simphy;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Madeyedexter
 */
public class FileSaver extends JFileChooser
{
    public FileSaver()
    {
        
        setFileFilter(new FileNameExtensionFilter("Saved Scene Files","scene"));
        setAcceptAllFileFilterUsed(false);
    }
    
}
