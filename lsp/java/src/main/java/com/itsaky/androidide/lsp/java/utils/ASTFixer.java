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

package com.itsaky.androidide.lsp.java.utils;

import androidx.annotation.NonNull;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Ordering;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import openjdk.source.tree.LineMap;
import openjdk.tools.javac.parser.Scanner;
import openjdk.tools.javac.parser.ScannerFactory;
import openjdk.tools.javac.parser.Tokens;
import openjdk.tools.javac.parser.Tokens.TokenKind;
import openjdk.tools.javac.util.Context;
import org.jetbrains.annotations.Contract;

/**
 * @author Akash Yadav
 */
public class ASTFixer {
  public static final String IDENT = "I_N_J_E_C_T_E_D";

  private static final Set<TokenKind> MEMBER_SELECTION_TOKENS =
      ImmutableSet.of(
          Tokens.TokenKind.IDENTIFIER,
          Tokens.TokenKind.LT,
          TokenKind.NEW,
          TokenKind.THIS,
          TokenKind.SUPER,
          TokenKind.CLASS,
          TokenKind.STAR);
  private static final Set<TokenKind> INVALID_SELECTION_SUFFIXES =
      ImmutableSet.of(TokenKind.RBRACE);

  private final Context context;

  public ASTFixer(Context context) {
    this.context = context;
  }

  public StringBuilder fix(CharSequence content) {
    Scanner scanner = ScannerFactory.instance(context).newScanner(content, true);
    List<Edit> edits = new ArrayList<>();
    for (; ; scanner.nextToken()) {
      Tokens.Token token = scanner.token();
      if (token.kind == TokenKind.EOF) {
        break;
      } else if (token.kind == TokenKind.DOT || token.kind == TokenKind.COLCOL) {
        fixMemberSelection(scanner, edits);
      } else if (token.kind == TokenKind.ERROR) {
        int errPos = scanner.errPos();
        if (errPos >= 0 && errPos < content.length()) {
          fixError(scanner, content, edits);
        }
      }
    }
    return Edit.applyInsertions(content, edits);
  }

  private void fixMemberSelection(@NonNull Scanner scanner, List<Edit> edits) {
    Tokens.Token token = scanner.token();
    Tokens.Token nextToken = scanner.token(1);

    LineMap lineMap = scanner.getLineMap();
    int tokenLine = (int) lineMap.getLineNumber(token.pos);
    int nextLine = (int) lineMap.getLineNumber(nextToken.pos);

    if (nextLine > tokenLine) {
      edits.add(Edit.create(token.endPos, IDENT + ";"));
    } else if (!MEMBER_SELECTION_TOKENS.contains(nextToken.kind)) {
      String toInsert = IDENT;
      if (INVALID_SELECTION_SUFFIXES.contains(nextToken.kind)) {
        toInsert = IDENT + ";";
      }
      edits.add(Edit.create(token.endPos, toInsert));
    }
  }

  private void fixError(@NonNull Scanner scanner, @NonNull CharSequence content, List<Edit> edits) {
    int errPos = scanner.errPos();
    if (content.charAt(errPos) == '.' && errPos > 0 && content.charAt(errPos) == '.') {
      if (errPos < content.length() - 1
          && Character.isJavaIdentifierStart(content.charAt(errPos + 1))) {
        edits.add(Edit.create(errPos, IDENT));
      }
    }
  }

  public static class Edit {
    private static final Ordering<Edit> REVERSE_INSERTION =
        Ordering.natural().onResultOf(Edit::getPos).reverse();

    private final int pos;
    private final String text;

    public Edit(int pos, String text) {
      this.pos = pos;
      this.text = text;
    }

    public int getPos() {
      return pos;
    }

    public String getText() {
      return text;
    }

    @NonNull
    @Contract(value = "_, _ -> new", pure = true)
    public static Edit create(int pos, String text) {
      return new Edit(pos, text);
    }

    @NonNull
    public static StringBuilder applyInsertions(CharSequence content, List<Edit> edits) {
      ImmutableList<Edit> reverseEdits = REVERSE_INSERTION.immutableSortedCopy(edits);

      StringBuilder sb = new StringBuilder(content);

      for (Edit edit : reverseEdits) {
        sb.insert(edit.getPos(), edit.getText());
      }
      return sb;
    }
  }
}
