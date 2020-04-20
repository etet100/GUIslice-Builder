/**
 *
 * The MIT License
 *
 * Copyright 2018-2020 Paul Conti
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
package builder.codegen.flash;

import java.lang.StringBuilder;
import java.util.List;
import java.util.Map;

import builder.codegen.CodeGenerator;
import builder.codegen.TemplateManager;
import builder.codegen.blocks.CodeBlock;
import builder.models.NumberInputModel;
import builder.models.WidgetModel;

/**
 * The Class NumberInput_P_CodeBlock outputs the code block
 * for GUIslice API gslc_ElemCreateTxt() calls that bring
 * up the numeric keypad.
 * 
 * @author Paul Conti
 * 
 */
public final class NumberInput_P_CodeBlock implements CodeBlock {

  /** The Constants for TEMPLATES. */
  private final static String ELEMENTREF_TEMPLATE    = "<ELEMENT_REF_FIND_P>";
  private final static String TEXT_INPUT_NUM_TEMPLATE = "<TEXT_INPUT_NUM_P>";
  private final static String TEXTUTF8_TEMPLATE      = "<TEXT_UTF8>";

  /**
   * Instantiates a new check box code block.
   */
  public NumberInput_P_CodeBlock() {
  }

  /**
   * Process will create our new code block and append it to
   * our input string builder object.
   *
   * @param cg
   *          the cg points to our CodeGenerator object that 
   *          is the controller for code generation.
   * @param tm
   *          the tm is the TemplateManager
   * @param sBd
   *          the sBd is the processed code
   * @param pageEnum
   *          the page enum
   * @param wm
   *          the wm is the widget model to use for code generation
   * @return the <code>string builder</code> object
   */
  static public StringBuilder process(CodeGenerator cg, TemplateManager tm, StringBuilder sBd, String pageEnum, WidgetModel wm) {
    NumberInputModel m = (NumberInputModel)wm;
    List<String> template = null;
    List<String> outputLines = null;
    Map<String, String> map = m.getMappedProperties(pageEnum);

    // now output creation API
    template = tm.loadTemplate(TEXT_INPUT_NUM_TEMPLATE);
    outputLines = tm.expandMacros(template, map);
    tm.codeWriter(sBd, outputLines);
    
    if (!m.getElementRef().isEmpty()) {
      template = tm.loadTemplate(ELEMENTREF_TEMPLATE);
      outputLines = tm.expandMacros(template, map);
      tm.codeWriter(sBd, outputLines);
      if (m.isUTF8()) {
        template = tm.loadTemplate(TEXTUTF8_TEMPLATE);
        outputLines = tm.expandMacros(template, map);
        tm.codeWriter(sBd, outputLines);
      }
    }
    
    template.clear();
    outputLines.clear();
    map.clear();
    return sBd;   
  }
  
}