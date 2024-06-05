package builder.views;

import javax.swing.JPanel;

import builder.dictionary.PageTypeDictionary;
import builder.models.WidgetModel;
import builder.widgets.Widget;

abstract public class MainSectionPane extends JPanel {
  abstract public String getKey();

  abstract public String getEnum();

  abstract public PageTypeDictionary getPageType();

  //abstract public Widget getWidget();

  abstract public WidgetModel getModel();

  public void setActive(boolean state) {
  }

  public void refreshView() {
  }
}
