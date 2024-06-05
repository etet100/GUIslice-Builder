/**
 *
 * The MIT License
 *
 * Copyright 2018-2023 Paul Conti
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 */
package builder.views;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;

import builder.clipboard.ClipboardKeyAdapter;
import builder.models.WidgetModel;
import builder.tables.ColorCellEditor;
import builder.tables.ColorCellRenderer;
import builder.tables.FontCellEditor;
import builder.tables.MultiClassTable;
import builder.tables.NonEditableCellRenderer;
import builder.tables.SelectAllCellEditor;

/**
 * The Class PropEditor handles the properties for one widget instance.
 * 
 * @author Paul Conti
 * 
 */
public class PropEditor {
  
  private JScrollPane scrollPane;
  private MultiClassTable table;
  private String key;
  //private WidgetModel model;
  private boolean initialized = false;

  /**
   * Instantiates a new prop editor.
   *
   * @param model
   *          the model
   */
  public PropEditor(WidgetModel model) {
    table = new MultiClassTable(new WidgetModel() {
      @Override
      public int getRowCount() {
        return 1;
      }

      @Override
      public int getColumnCount() {
        return 1;
      }

      @Override
      public String getColumnName(int columnIndex) {
        return "Info";
      }

      @Override
      public Class<?> getColumnClass(int columnIndex) {
        return String.class;
      }

      @Override
      public Class<?> getClassAt(int rowIndex) {
        return String.class;
      }

      @Override
      public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
      }

      @Override
      public Object getValueAt(int rowIndex, int columnIndex) {
        return "Select something...";
      }

      @Override
      public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
      }

      @Override
      public void addTableModelListener(javax.swing.event.TableModelListener l) {
      }

      @Override
      public void removeTableModelListener(javax.swing.event.TableModelListener l) {
      }
    });

    if (model != null) {
      setModel(model);
    }

    // set keyboard listener for copy and paste
    table.addKeyListener(new ClipboardKeyAdapter(table));

    scrollPane = new JScrollPane(
      table,
      JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
      JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
    );
    scrollPane.setPreferredSize(new Dimension(1200, 650));
  }

  public void setModel(WidgetModel model) {
    //this.model = model;
    table.setModel(model);
    if (!initialized) {
      init(model);
      initialized = true;
    }
  }

  private void init(WidgetModel model) {
    table.setPreferredScrollableViewportSize(table.getPreferredSize());

    // Set up renderer and editor for our Color cells.
    table.setDefaultRenderer(Color.class, new ColorCellRenderer(true));
    table.setDefaultEditor(Color.class, new ColorCellEditor());

    // Set up our gray out non editable cells renderer.
    table.setDefaultRenderer(String.class, new NonEditableCellRenderer());
    DefaultTableCellRenderer integerNonEditableCellRenderer = new NonEditableCellRenderer();
    table.setDefaultRenderer(Integer.class, integerNonEditableCellRenderer);
    DefaultTableCellRenderer checkBoxNonEditableCellRenderer = new NonEditableCellRenderer();
    checkBoxNonEditableCellRenderer.setHorizontalAlignment( SwingConstants.LEFT );
    table.setDefaultRenderer(Boolean.class, checkBoxNonEditableCellRenderer);
    DefaultTableCellRenderer JTextFieldNonEditableCellRenderer = new NonEditableCellRenderer();
    table.setDefaultRenderer(JTextField.class, JTextFieldNonEditableCellRenderer);

    // Setup editor for our Font cells.
    table.setDefaultEditor(JTextField.class, new FontCellEditor());

    // Setup editor for Integer cells.
    table.setDefaultEditor(Integer.class, new SelectAllCellEditor());

    // set our preferred column widths
    table.getColumnModel().getColumn(0).setPreferredWidth(130);
    table.getColumnModel().getColumn(1).setPreferredWidth(175);
//    table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
//    table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

    // restrict selections to one cell of our table
//    table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    table.setRowSelectionAllowed(true);
    table.setColumnSelectionAllowed(true);

    // Force JTable to commit data to model while it is still in editing mode
    table.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
    
    // disable user column dragging
    table.getTableHeader().setReorderingAllowed(false);

    scrollPane.updateUI();
    table.updateUI();
  }
  
  /**
   * Gets the key.
   *
   * @return the key
   */
  public String getKey() {
    return key;
  }
  
  /**
   * Sets the key.
   *
   * @param key
   *          the new key
   */
  public void setKey(String key) {
    this.key = key;
  }
  
  /**
   * Gets the prop panel.
   *
   * @return the prop panel
   */
  public JScrollPane getPropPanel()
  {
    return scrollPane;
  }
  
}
