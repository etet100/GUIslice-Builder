package builder.common;

import java.awt.Rectangle;

import java.util.ArrayList;
import java.util.List;
import java.util.EnumSet;

import builder.models.GridModel;
import builder.models.ProjectModel;
import builder.prefs.GridEditor;
import builder.widgets.Widget;

/**
 * Snapper lets you find the nearest point to span dragged vertice. 
 * 
 * @author etet100
 */
public class Snapper {
  //private List<Widget> widgets = new ArrayList<Widget>();

  private boolean snapToGrid;
  private boolean snapToGuidelines;
  private boolean snapToWidgets;
  private boolean snapToMargins;
  private Type type;

  // HORIZONTAL snapper snaps to vertical lines during X-axis movement, and vice versa.
  public enum Type {
    HORIZONTAL,
    VERTICAL
  }

  private enum ItemType {
    GRID,
    GUIDELINE,
    MARGIN,
    WIDGET
  }

  public enum SourceEdge {
    MIN, // left or top edge
    MAX; // right or bottom edge
    public static final EnumSet<SourceEdge> ALL = EnumSet.allOf(SourceEdge.class);
  }

  public class SnappingMarker {
    public final int position, min, max;

    public SnappingMarker(int pos, int min, int max) {
      this.position = pos;
      this.min = min;
      this.max = max;
    }
  }

  private final List<SnappingMarker> snappingMarkers = new ArrayList<SnappingMarker>();

  private class Item {
    public final ItemType type;
    public final EnumSet<SourceEdge> sourceEdge;
    public final int pos;
    public final Widget widget;
    public final Guidelines.Guideline guideline;

    public Item(ItemType type, int pos) {
      this(type, SourceEdge.ALL, pos, null, null);
    }

    public Item(ItemType type, EnumSet<SourceEdge> sourceEdge, int pos, Widget widget, Guidelines.Guideline guideline) {
      this.type = type;
      this.sourceEdge = sourceEdge;
      this.pos = pos;
      this.widget = widget;
      this.guideline = guideline;
    }

    public Item(EnumSet<SourceEdge> sourceEdge, int pos, Widget widget) {
      this(ItemType.WIDGET, sourceEdge, pos, widget, null);
    }

    public Item(int pos, Guidelines.Guideline guideline) {
      this(ItemType.GUIDELINE, SourceEdge.ALL, pos, null, guideline);
    }
  }
  
  private List<Item> items = new ArrayList<Item>();

  private final int SNAP_DISTANCE = 5;

  public Snapper(Type type, boolean snapToGrid, boolean snapToMargins, boolean snapToGuidelines, boolean snapToWidgets) {
    this.type = type;
    this.snapToGrid = snapToGrid;
    this.snapToGuidelines = snapToGuidelines;
    this.snapToWidgets = snapToWidgets;
    this.snapToMargins = snapToMargins;
  }

  public class Result {
    public int pos;
    public boolean snapped;
    public SourceEdge edge;
    public int distance;

    public Result(int pos, boolean snapped, SourceEdge edge, int distance) {
      this.pos = pos;
      this.snapped = snapped;
      this.edge = edge;
      this.distance = distance;
    }
  }

  public List<SnappingMarker> getSnappingMarkers() {
    return snappingMarkers;
  }

  /**
   * Snap min or max edge, depending on which one is closer to the nearest snapping point.
   */
  public Result snapMinOrMax(int posMin, int posMax) {
    Result snappedMin = snap_(posMin, SourceEdge.MIN);
    Result snappedMax = snap_(posMax, SourceEdge.MAX);

    if (snappedMin.distance < snappedMax.distance) {
      return snappedMin;
    } else {
      return snappedMax;
    }
  }

  private Result snap_(int pos, SourceEdge edge) {
    Item bestItem = findBestMatchingItem(pos, edge);
    if (bestItem != null && Math.abs(bestItem.pos - pos) < SNAP_DISTANCE) {
      System.out.println(type + "; snapped to " + bestItem.type);
      return new Result(
        bestItem.pos,
        true,
        edge,
        Math.abs(bestItem.pos - pos)
      );
    }

    return new Result(pos, false, edge, Integer.MAX_VALUE);
  }

  public int snap(int pos, SourceEdge edge) {
    snappingMarkers.clear();
    Item bestItem = findBestMatchingItem(pos, edge);
    if (bestItem != null && Math.abs(bestItem.pos - pos) < SNAP_DISTANCE) {
      if (bestItem.type == ItemType.WIDGET) {
        snappingMarkers.add(new SnappingMarker(bestItem.pos, bestItem.pos - SNAP_DISTANCE, bestItem.pos + SNAP_DISTANCE));
      }
      System.out.println(type + "; snapped to " + bestItem.type);
      return bestItem.pos;
    }

    return pos;
  }

  private Item findBestMatchingItem(int pos, SourceEdge edge) {
    Item bestItem = null;
   //System.out.println(items);
    for (Item item : items) {
      if (!item.sourceEdge.contains(edge)) { continue; }
      if (item.type == ItemType.GRID && !snapToGrid) { continue; }
      if (item.type == ItemType.GUIDELINE && !snapToGuidelines) { continue; }
      if (item.type == ItemType.WIDGET && !snapToWidgets) { continue; }
      if (item.type == ItemType.MARGIN && !snapToMargins) { continue; }
      if (Math.abs(item.pos - pos) > SNAP_DISTANCE) { continue; }
      if (bestItem == null || Math.abs(item.pos - pos) < Math.abs(bestItem.pos - pos)) {
        bestItem = item;
      }
      if (item.pos > pos) {
        break;
      }
    }

    return bestItem;
  }

  private void sortItems() {
    items.sort((a, b) -> a.pos - b.pos);
  }

  // position relative to the left or top edge of the widget
  public void addMargin(int pos, SourceEdge snappingTo) {
    items.add(new Item(ItemType.MARGIN, pos));
    sortItems();
  }

  // position relative to the left or top edge of the widget
  public void addGuideline(Guidelines.Guideline guideline) {
    items.add(new Item(guideline.getPos(), guideline));
    sortItems();
  }

  public void addWidget(Widget widget, int margin) {
    if (type == Type.HORIZONTAL) {
      // edge
      items.add(new Item(EnumSet.of(SourceEdge.MIN), widget.getY(), widget));
      items.add(new Item(EnumSet.of(SourceEdge.MAX), widget.getY() + widget.getHeight(), widget));
      // edge with margin
      items.add(new Item(EnumSet.of(SourceEdge.MAX), widget.getY() - margin, widget));
      items.add(new Item(EnumSet.of(SourceEdge.MIN), widget.getY() + widget.getHeight() + margin, widget));
    } else {
      // edge
      items.add(new Item(EnumSet.of(SourceEdge.MIN), widget.getX(), widget));
      items.add(new Item(EnumSet.of(SourceEdge.MAX), widget.getX() + widget.getWidth(), widget));
      // edge with margin
      items.add(new Item(EnumSet.of(SourceEdge.MAX), widget.getX() - margin, widget));
      items.add(new Item(EnumSet.of(SourceEdge.MIN), widget.getX() + widget.getWidth() + margin, widget));
    }

    sortItems();
  }

  public void clearWidgets() {
    //widgets.clear();
  }

  public void addGrid(int majorDivs, int minorDivs, int size) {
    int gridSize = minorDivs > 0 ? minorDivs : majorDivs;
    int gridPos = gridSize;
    while (gridPos < size) {
      items.add(new Item(ItemType.GRID, gridPos));
      gridPos += gridSize;
    }
    sortItems();
  }

  public class Builder {
    private GridModel gridModel;
    private ProjectModel projectModel;

    private Snapper buildHorizontal(Guidelines guidelines, List<Widget> widgets) {
      Snapper snapper = new Snapper(Type.HORIZONTAL, GridEditor.getInstance().getGridSnapTo(), true, true, true);
      snapper.addGrid(gridModel.getGridMajorWidth(), gridModel.getGridMinorWidth(), projectModel.getWidth());
      snapper.addMargin(projectModel.getMargins(), Snapper.SourceEdge.MIN);
      snapper.addMargin(projectModel.getHeight() - projectModel.getMargins(), Snapper.SourceEdge.MAX);
      for (Guidelines.Guideline g : guidelines.getGuidelines(Guidelines.Type.HORIZONTAL)) {
        snapper.addGuideline(g);
      }
      for (Widget w : widgets) {
        snapper.addWidget(w, projectModel.getVSpacing());
      }
      return snapper;
    }

    private Snapper buildVertical(Guidelines guidelines, List<Widget> widgets) {
      Snapper snapper = new Snapper(Type.VERTICAL, GridEditor.getInstance().getGridSnapTo(), true, true, true);
      snapper.addGrid(gridModel.getGridMajorHeight(), gridModel.getGridMinorHeight(), projectModel.getHeight());
      snapper.addMargin(projectModel.getMargins(), Snapper.SourceEdge.MIN);
      snapper.addMargin(projectModel.getWidth() - projectModel.getMargins(), Snapper.SourceEdge.MAX);
      for (Guidelines.Guideline g : guidelines.getGuidelines(Guidelines.Type.VERTICAL)) {
        snapper.addGuideline(g);
      }
      for (Widget w : widgets) {
        snapper.addWidget(w, projectModel.getHSpacing());
      }
      return snapper;
    }
  }
}
