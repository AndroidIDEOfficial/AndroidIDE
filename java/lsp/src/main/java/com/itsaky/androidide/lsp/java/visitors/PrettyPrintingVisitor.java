/*
 *  This file is part of AndroidIDE.
 *
 *  AndroidIDE is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  AndroidIDE is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *   along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.itsaky.androidide.lsp.java.visitors;

import static com.github.javaparser.utils.PositionUtils.sortByBeginPosition;
import static com.itsaky.androidide.lsp.java.utils.JavaParserUtils.getSimpleName;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.printer.DefaultPrettyPrinterVisitor;
import com.github.javaparser.printer.SourcePrinter;
import com.github.javaparser.printer.configuration.ConfigurationOption;
import com.github.javaparser.printer.configuration.DefaultConfigurationOption;
import com.github.javaparser.printer.configuration.DefaultPrinterConfiguration;
import com.github.javaparser.printer.configuration.PrinterConfiguration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PrettyPrintingVisitor extends DefaultPrettyPrinterVisitor {

  public PrettyPrintingVisitor(PrinterConfiguration configuration) {
    super(configuration);
  }

  public PrettyPrintingVisitor(PrinterConfiguration configuration, SourcePrinter printer) {
    super(configuration, printer);
  }

  @Override
  public void visit(Name n, Void arg) {
    printOrphanCommentsBeforeThisChildNode(n);
    printComment(n.getComment(), arg);
    printer.print(n.getIdentifier());
    printOrphanCommentsEnding(n);
  }

  @Override
  public void visit(SimpleName n, Void arg) {
    printOrphanCommentsBeforeThisChildNode(n);
    printComment(n.getComment(), arg);

    String identifier = n.getIdentifier();
    printer.print(getSimpleName(identifier));
  }

  @Override
  public void visit(ClassOrInterfaceType n, Void arg) {
    printOrphanCommentsBeforeThisChildNode(n);
    printComment(n.getComment(), arg);

    printAnnotations(n.getAnnotations(), false, arg);

    n.getName().accept(this, arg);

    if (n.isUsingDiamondOperator()) {
      printer.print("<>");
    } else {
      printTypeArgs(n, arg);
    }
  }

  protected void printOrphanCommentsBeforeThisChildNode(final Node node) {
    if (!getOption(DefaultPrinterConfiguration.ConfigOption.PRINT_COMMENTS).isPresent()) return;
    if (node instanceof Comment) return;

    Node parent = node.getParentNode().orElse(null);
    if (parent == null) return;
    List<Node> everything = new ArrayList<>(parent.getChildNodes());
    sortByBeginPosition(everything);
    int positionOfTheChild = -1;
    for (int i = 0; i < everything.size(); ++i) { // indexOf is by equality, so this
      // is used to index by identity
      if (everything.get(i) == node) {
        positionOfTheChild = i;
        break;
      }
    }
    if (positionOfTheChild == -1) {
      throw new AssertionError("I am not a child of my parent.");
    }
    int positionOfPreviousChild = -1;
    for (int i = positionOfTheChild - 1; i >= 0 && positionOfPreviousChild == -1; i--) {
      if (!(everything.get(i) instanceof Comment)) positionOfPreviousChild = i;
    }
    for (int i = positionOfPreviousChild + 1; i < positionOfTheChild; i++) {
      Node nodeToPrint = everything.get(i);
      if (!(nodeToPrint instanceof Comment))
        throw new RuntimeException(
            "Expected comment, instead "
                + nodeToPrint.getClass()
                + ". Position of previous child: "
                + positionOfPreviousChild
                + ", position of child "
                + positionOfTheChild);
      nodeToPrint.accept(this, null);
    }
  }

  private Optional<ConfigurationOption> getOption(
      DefaultPrinterConfiguration.ConfigOption cOption) {
    return configuration.get(new DefaultConfigurationOption(cOption));
  }

  protected void printOrphanCommentsEnding(final Node node) {
    if (!getOption(DefaultPrinterConfiguration.ConfigOption.PRINT_COMMENTS).isPresent()) return;

    List<Node> everything = new ArrayList<>(node.getChildNodes());
    sortByBeginPosition(everything);
    if (everything.isEmpty()) {
      return;
    }

    int commentsAtEnd = 0;
    boolean findingComments = true;
    while (findingComments && commentsAtEnd < everything.size()) {
      Node last = everything.get(everything.size() - 1 - commentsAtEnd);
      findingComments = (last instanceof Comment);
      if (findingComments) {
        commentsAtEnd++;
      }
    }
    for (int i = 0; i < commentsAtEnd; i++) {
      everything.get(everything.size() - commentsAtEnd + i).accept(this, null);
    }
  }
}
