/*
 *   Copyright 2020-2021 Rosemoe
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package io.github.rosemoe.editor.interfaces;

import androidx.annotation.Nullable;

import com.itsaky.lsp.api.ILanguageServer;
import com.itsaky.lsp.models.DiagnosticItem;
import com.itsaky.lsp.models.SemanticHighlight;

import io.github.rosemoe.editor.text.Content;
import io.github.rosemoe.editor.text.TextAnalyzeResult;
import io.github.rosemoe.editor.text.TextAnalyzer;
import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Interface for analyzing highlight
 *
 * @author Rose
 */
public interface CodeAnalyzer {
    
    /**
     * Tells the analyzer to store the semantic highlight ranges provided by LanguageServer
     */
    void setSemanticHighlights(SemanticHighlight highlights);
    
    /**
     * Tells the analyzer to update the diagnostics locally.
     * They will be then used to provide spans
     *
     * @param diagnostics The diagnostics to update to.
                 They are first mapped by their start line and then by their start column.
     */
    void updateDiagnostics(Map<Integer, Map<Integer, DiagnosticItem>> diagnostics);
    
    /**
     * Find the diagnostic containing the specific position
     *
     * @param line The line to find
     * @param column The column to find
     * @return The found {@link DiagnosticItem} or {@code null}
     */
    DiagnosticItem findDiagnosticContaining(int line, int column);
    
    /**
     * Find all diagnostics at the specified line
     *
     * @param line The line to find
     * @return A list containing all diagnostics at the line. Should never be null.
     */
    List<DiagnosticItem> findDiagnosticsContainingLine(int line);
    
    /**
     * Get diagnostics at the specified line.
     *
     * @return A map containing the diagnostics mapped by their columns
     */
    Map<Integer, DiagnosticItem> getDiagnosticsAtLine(int line);
    
    /**
     * Get any hex color string is present in provided line
     */
    Map<Integer, Integer> getHexColorsInLine (int line);
    
    /**
     * Analyze spans for the given input
     *
     * @param languageServer The language server provided by this language. Maybe null.
     * @param file The file opened in the editor
     * @param content  The input text
     * @param colors   Result dest
     * @param delegate Delegate between thread and analyzer
     * @throws Exception
     *
     * @see TextAnalyzer#analyze(Content)
     * @see TextAnalyzer.AnalyzeThread.Delegate#shouldAnalyze()
     */
    void analyze(@Nullable ILanguageServer languageServer, @Nullable File file, Content content, TextAnalyzeResult colors, TextAnalyzer.AnalyzeThread.Delegate delegate) throws Exception;

}
