package builder.models;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

abstract public class BaseModel extends AbstractTableModel {
  protected class Property {
    public final String name;
    public final String key;
    public final Class<?> type;
    public final Boolean editable;
    public final String description;

    public Property(String key, Class<?> type, String name, Boolean editable, String description) {
      this.key = key;
      this.type = type;
      this.name = name;
      this.editable = editable;
      this.description = description;
    }
  }

  final private ArrayList<Property> properties = new ArrayList<Property>();

  private static final int COLUMN_INDEX_NAME = 0;
  private static final int COLUMN_INDEX_VALUE = 1;

  @Override
  final public int getColumnCount() {
    return 2;
  }

  @Override
  final public int getRowCount() {
     return properties.size();
  }

  @Override
  final public Class<?> getColumnClass(int columnIndex) {
    return super.getColumnClass(columnIndex);
  }

  @Override
  final public String getColumnName(int column) {
    switch (column) {
      case COLUMN_INDEX_NAME: return "Name";
      case COLUMN_INDEX_VALUE: return "Value";
    }
    throw new IllegalArgumentException("Invalid column index: " + column);
  }

  @Override
  final public boolean isCellEditable(int rowIndex, int columnIndex) {
    if (columnIndex == COLUMN_INDEX_NAME) {
      return false;
    }

    return properties.get(rowIndex).editable;
  }

  @Override
  final public Object getValueAt(int rowIndex, int columnIndex) {
    switch (columnIndex) {
      case COLUMN_INDEX_NAME: return properties.get(rowIndex).name;
      case COLUMN_INDEX_VALUE: return getPropertyValue(properties.get(rowIndex).key);
    }
    throw new IllegalArgumentException("Invalid column index: " + columnIndex);
  }

  abstract protected Object getPropertyValue(String key);
}
