/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.netbeans.lib.nbjavac.services;

import com.sun.tools.javac.parser.JavacParser;
import com.sun.tools.javac.parser.Lexer;
import com.sun.tools.javac.parser.ParserFactory;
import com.sun.tools.javac.parser.ScannerFactory;
import com.sun.tools.javac.parser.Tokens.Comment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCEnhancedForLoop;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCModifiers;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.JCTree.JCTypeParameter;
import com.sun.tools.javac.tree.TreeInfo;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Names;
import com.sun.tools.javac.util.Position;

/**
 *
 * @author lahvac
 */
public class NBParserFactory extends ParserFactory {

    public static void preRegister(Context context) {
        context.put(parserFactoryKey, new Context.Factory<ParserFactory>() {
            @Override
            public ParserFactory make(Context c) {
                return new NBParserFactory(c);
            }
        });
    }

    private final ScannerFactory scannerFactory;
    private final Names names;
    private final CancelService cancelService;

    protected NBParserFactory(Context context) {
        super(context);
        this.scannerFactory = ScannerFactory.instance(context);
        this.names = Names.instance(context);
        this.cancelService = CancelService.instance(context);
    }

    @Override
    public JavacParser newParser(CharSequence input, boolean keepDocComments, boolean keepEndPos, boolean keepLineMap, boolean parseModuleInfo) {
        Lexer lexer = scannerFactory.newScanner(input, keepDocComments);
        return new NBJavacParser(this, lexer, keepDocComments, keepLineMap, keepEndPos, parseModuleInfo, cancelService);
    }

    public static class NBJavacParser extends JavacParser {

        private final Names names;
        private final CancelService cancelService;

        public NBJavacParser(NBParserFactory fac, Lexer S, boolean keepDocComments, boolean keepLineMap, boolean keepEndPos, boolean parseModuleInfo, CancelService cancelService) {
            super(fac, S, keepDocComments, keepLineMap, keepEndPos, parseModuleInfo);
            this.names = fac.names;
            this.cancelService = cancelService;
        }

        @Override
        protected AbstractEndPosTable newEndPosTable(boolean keepEndPositions) {
            AbstractEndPosTable res = super.newEndPosTable(keepEndPositions);
            
            if (keepEndPositions) {
                return new EndPosTableImpl(S, this, (SimpleEndPosTable) res);
            }
            
            return res;
        }

        @Override
        protected JCClassDecl classDeclaration(JCModifiers mods, Comment dc) {
            if (cancelService != null) {
                cancelService.abortIfCanceled();
            }
            return super.classDeclaration(mods, dc);
        }

        @Override
        protected JCClassDecl interfaceDeclaration(JCModifiers mods, Comment dc) {
            if (cancelService != null) {
                cancelService.abortIfCanceled();
            }
            return super.interfaceDeclaration(mods, dc);
        }

        @Override
        protected JCClassDecl enumDeclaration(JCModifiers mods, Comment dc) {
            if (cancelService != null) {
                cancelService.abortIfCanceled();
            }
            return super.enumDeclaration(mods, dc);
        }

        @Override
        protected JCTree methodDeclaratorRest(int pos, JCModifiers mods, JCExpression type, Name name, List<JCTypeParameter> typarams, boolean isInterface, boolean isVoid, boolean isRecord, Comment dc) {
            if (cancelService != null) {
                cancelService.abortIfCanceled();
            }
            return super.methodDeclaratorRest(pos, mods, type, name, typarams, isInterface, isVoid, isRecord, dc);
        }


        @Override
        public int getEndPos(JCTree jctree) {
            return TreeInfo.getEndPos(jctree, endPosTable);
        }

        @Override
        public JCStatement parseSimpleStatement() {
            JCStatement result = super.parseSimpleStatement();
            //workaround: if the code looks like:
            //for (name : <collection>) {...}
            //the "name" will be made a type of a variable with name "<error>", with
            //no end position. Inject the end position for the variable:
            if (result instanceof JCEnhancedForLoop) {
                JCEnhancedForLoop tree = (JCEnhancedForLoop) result;
                if (getEndPos(tree.var) == Position.NOPOS) {
                    endPosTable.storeEnd(tree.var, getEndPos(tree.var.vartype));
                }
            }
            return result;
        }

        public final class EndPosTableImpl extends AbstractEndPosTable {
            
            private final Lexer lexer;
            private final SimpleEndPosTable delegate;

            private EndPosTableImpl(Lexer lexer, JavacParser parser, SimpleEndPosTable delegate) {
                super(parser);
                this.lexer = lexer;
                this.delegate = delegate;
            }
            
            public void resetErrorEndPos() {
                delegate.errorEndPos = Position.NOPOS;
                errorEndPos = delegate.errorEndPos;
            }
            
            @Override public void storeEnd(JCTree tree, int endpos) {
                if (endpos >= 0)
                    delegate.storeEnd(tree, endpos);
            }

            @Override
            public void setErrorEndPos(int errPos) {
                delegate.setErrorEndPos(errPos);
                errorEndPos = delegate.errorEndPos;
            }

            @Override
            protected <T extends JCTree> T to(T t) {
                storeEnd(t, parser.token().endPos);
                return t;
            }

            @Override
            protected <T extends JCTree> T toP(T t) {
                storeEnd(t, lexer.prevToken().endPos);
                return t;
            }

            @Override
            public int getEndPos(JCTree jctree) {
                return delegate.getEndPos(jctree);
            }

            @Override
            public int replaceTree(JCTree jctree, JCTree jctree1) {
                return delegate.replaceTree(jctree, jctree1);
            }
        }
    }

}
