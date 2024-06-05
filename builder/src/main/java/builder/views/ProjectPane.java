/**
 *
 * The MIT License
 *
 * Copyright 2018-2022 Paul Conti
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

import javax.swing.BorderFactory;

import builder.dictionary.PageTypeDictionary;
import builder.models.ProjectModel;
import builder.widgets.Widget;

public class ProjectPane extends MainSectionPane {
  private ProjectModel model;

  public ProjectPane(ProjectModel model) {
    this.setLocation(0, 0);
    this.setOpaque(true);
    this.setFocusable( true ); 
    this.setBorder(BorderFactory.createLineBorder(Color.black));
    this.setVisible(true);
    this.model = model;
  }

  @Override
  public String getKey() {
    return "project";
  }

  @Override
  public String getEnum() {
    return "project";
  }

  @Override
  public PageTypeDictionary getPageType() {
    return null;
  }

  // @Override
  // public Widget getWidget() {
  //   // TODO Auto-generated method stub
  //   return null;
  // }

  @Override
  public ProjectModel getModel() {
    return model;
  }
}