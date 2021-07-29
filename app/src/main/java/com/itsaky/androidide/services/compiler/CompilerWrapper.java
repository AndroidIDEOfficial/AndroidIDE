package com.itsaky.androidide.services.compiler;

import java.io.PrintWriter;
import org.eclipse.jdt.core.compiler.CompilationProgress;
import org.eclipse.jdt.internal.compiler.CompilationResult;
import org.eclipse.jdt.internal.compiler.Compiler;
import org.eclipse.jdt.internal.compiler.ICompilerRequestor;
import org.eclipse.jdt.internal.compiler.IErrorHandlingPolicy;
import org.eclipse.jdt.internal.compiler.IProblemFactory;
import org.eclipse.jdt.internal.compiler.ReadManager;
import org.eclipse.jdt.internal.compiler.ast.CompilationUnitDeclaration;
import org.eclipse.jdt.internal.compiler.ast.ImportReference;
import org.eclipse.jdt.internal.compiler.env.ICompilationUnit;
import org.eclipse.jdt.internal.compiler.env.INameEnvironment;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.jdt.internal.compiler.lookup.SourceTypeCollisionException;
import org.eclipse.jdt.internal.compiler.problem.AbortCompilation;
import org.eclipse.jdt.internal.compiler.util.Messages;

public class CompilerWrapper extends Compiler {

    public CompilerWrapper(INameEnvironment env,
                           IErrorHandlingPolicy errorHandlingPolicy,
                           CompilerOptions options,
                           ICompilerRequestor compilerRequestor,
                           IProblemFactory problemFactory,
                           PrintWriter out,
                           CompilationProgress progress) {
        super(env, errorHandlingPolicy, options, compilerRequestor, problemFactory, out, progress);
        this.useSingleThread = true;
    }

    public CompilerWrapper setProgress(CompilationProgress progress) {
        this.progress = progress;
        return this;
    }

    @Override
    public void compile(ICompilationUnit[] sourceUnits) {
        compileInternal(sourceUnits, false);
    }
    
    private void compileInternal(ICompilationUnit[] sourceUnits, boolean lastRound) {
        this.stats.startTime = System.currentTimeMillis();
        try {
            reportProgress(Messages.compilation_beginningToCompile);
            if (this.annotationProcessorManager == null) {
                beginToCompile(sourceUnits);
            } else {
                ICompilationUnit[] originalUnits = sourceUnits.clone(); // remember source units in case a source type collision occurs
                try {
                    beginToCompile(sourceUnits);
                    if (!lastRound) processAnnotations();
                    if (!this.options.generateClassFiles) return;
                } catch (SourceTypeCollisionException e) {
                    backupAptProblems();
                    reset();
                    int originalLength = originalUnits.length;
                    int newProcessedLength = e.newAnnotationProcessorUnits.length;
                    ICompilationUnit[] combinedUnits = new ICompilationUnit[originalLength + newProcessedLength];
                    System.arraycopy(originalUnits, 0, combinedUnits, 0, originalLength);
                    System.arraycopy(e.newAnnotationProcessorUnits, 0, combinedUnits, originalLength, newProcessedLength);
                    this.annotationProcessorStartIndex  = originalLength;
                    compileInternal(combinedUnits, e.isLastRound);
                    return;
                }
            }
            restoreAptProblems();
            processCompiledUnits(0, lastRound);
        } catch (AbortCompilation e) {
            this.handleInternalException(e, null);
        } catch (Throwable th) {
            AbortCompilation a = new AbortCompilation();
            a.isSilent = true;
            handleInternalException(a, null);
        }
        if (this.options.verbose) {
            if (this.totalUnits > 1) {
                this.out.println(Messages.bind(Messages.compilation_units, String.valueOf(this.totalUnits)));
            } else {
                this.out.println(Messages.bind(Messages.compilation_unit, String.valueOf(this.totalUnits)));
            }
        }
	}
    
    @Override
    protected void internalBeginToCompile(ICompilationUnit[] sourceUnits, int maxUnits) {
        if (!this.useSingleThread && maxUnits >= ReadManager.THRESHOLD)
            this.parser.readManager = new ReadManager(sourceUnits, maxUnits);
        
        for (int i = 0; i < maxUnits; i++) {
            CompilationResult unitResult = null;
            try {
                if (this.options.verbose) {
                    this.out.println(
                        Messages.bind(Messages.compilation_request,
                                      new String[] {
                                          String.valueOf(i + 1),
                                          String.valueOf(maxUnits),
                                          new String(sourceUnits[i].getFileName())
                                      }));
                }
                CompilationUnitDeclaration parsedUnit;
                unitResult = new CompilationResult(sourceUnits[i], i, maxUnits, this.options.maxProblemsPerUnit);
                long parseStart = System.currentTimeMillis();
                if (this.totalUnits < this.parseThreshold) {
                    parsedUnit = this.parser.parse(sourceUnits[i], unitResult);
                } else {
                    parsedUnit = this.parser.dietParse(sourceUnits[i], unitResult);
                }
                long resolveStart = System.currentTimeMillis();
                this.stats.parseTime += resolveStart - parseStart;
                this.lookupEnvironment.buildTypeBindings(parsedUnit, null);
                this.stats.resolveTime += System.currentTimeMillis() - resolveStart;
                addCompilationUnit(sourceUnits[i], parsedUnit);
                ImportReference currentPackage = parsedUnit.currentPackage;
                if (currentPackage != null) {
                    unitResult.recordPackageName(currentPackage.tokens);
                }
                if (this.parser.readManager != null) {
                    this.parser.readManager.shutdown();
                    this.parser.readManager = null;
                }
                this.lookupEnvironment.completeTypeBindings();
            } catch (AbortCompilation a) {
                if (a.compilationResult == null)
                    a.compilationResult = unitResult;
                throw a;
            } catch (Throwable th) {
                AbortCompilation a = new AbortCompilation();
                a.isSilent = true;
                throw a;
            } finally {
                sourceUnits[i] = null;
            }
        }
	}
}
