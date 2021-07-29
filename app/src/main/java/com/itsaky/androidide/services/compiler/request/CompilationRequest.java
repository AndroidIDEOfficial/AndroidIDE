package com.itsaky.androidide.services.compiler.request;

import com.itsaky.androidide.interfaces.CompileListener;
import com.itsaky.androidide.services.compiler.model.CompilerDiagnostic;
import com.itsaky.androidide.tools.SourceJavaFileObject;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface CompilationRequest {
    
    List<File> files();
    CompileListener callback();
    
    public static class Builder {
        
        private List<File> files;
        private CompileListener callback;
        
        private Builder(){
            files = new ArrayList<>();
        }
        
        public static Builder newInstance() {
            return new Builder();
        }
        
        public Builder withFiles(File... files) {
            if(files == null) {
                this.files = new ArrayList<>();
                return this;
            }
            this.files = new ArrayList<>();
            for(File file : files) {
                this.files.add(file);
            }
            return this;
        }
        
        public Builder withFiles(List<File> files) {
            if(files == null) {
                this.files = new ArrayList<>();
                return this;
            }
            this.files = files;
            return this;
        }
        
        public Builder withCallback(CompileListener callback) {
            this.callback = callback;
            return this;
        }
        
        public CompilationRequest build() {
            return new CompilationRequest(){

                @Override
                public List<File> files() {
                    return files;
                }

                @Override
                public CompileListener callback() {
                    return callback;
                }
            };
        }
    }
}
