package com.itsaky.apiinfo;

import com.blankj.utilcode.util.FileIOUtils;
import com.itsaky.apiinfo.models.ClassInfo;
import com.itsaky.apiinfo.models.FieldInfo;
import com.itsaky.apiinfo.models.MethodInfo;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ApiInfo {
    
    private final File versionsXml;
    
    private boolean read = false;
    
    private final Map<String, ClassInfo> classInfos;
    
    public static final String NAME = "name";
    public static final String SINCE = "since";
    public static final String DEPRECATED = "deprecated";
    public static final String REMOVED = "removed";
    public static final String EXTENDS = "extends";
    public static final String IMPLEMENTS = "implements";
    public static final String FIELD = "field";
    public static final String METHOD = "method";
    
    public ApiInfo(File versionsXml) {
        this.versionsXml = versionsXml;
        this.classInfos = new HashMap<String, ClassInfo>();
    }
    
    public boolean hasRead() {
        return read;
    }
    
    public ClassInfo getClassByName(String qualifiedName) {
        return classInfos.get(qualifiedName);
    }
    
    public void readAsync(Runnable onFinish) throws Exception{
        new Thread(() -> {
            String contents = FileIOUtils.readFile2String(versionsXml);
            if(contents != null) {
                contents = contents.substring(contents.indexOf("<api"));
                Document doc = Jsoup.parse(contents);
                Elements classes = doc.getElementsByTag("api").first().getElementsByTag("class");
                if(classes == null || classes.size() <= 0) {
                    return;
                }

                for(Element clazz : classes) {
                    ClassInfo info = parseClassInfo(clazz);
                    if(info != null)
                        classInfos.put(info.name, info);
                }
                
                read = true;
                if(onFinish != null)
                    onFinish.run();
            }
        }).start();
    }

    private ClassInfo parseClassInfo(Element clazz) {
        if(!clazz.hasAttr(NAME)) return null;
        ClassInfo info = new ClassInfo();
        
        info.name = clazz.attr(NAME)
                    .replace("/", ".")
                    .replace("$", ".");
        
        if(clazz.hasAttr(SINCE)) {
            info.since = Integer.parseInt(clazz.attr(SINCE));
        }
        
        if(clazz.hasAttr(DEPRECATED)) {
            info.deprecated = Integer.parseInt(clazz.attr(DEPRECATED));
        }
        
        if(clazz.hasAttr(REMOVED)) {
            info.removed = Integer.parseInt(clazz.attr(REMOVED));
        }
        
        Elements superClass = clazz.getElementsByTag(EXTENDS);
        if(superClass != null && superClass.size() == 1) {
            info.superClass = superClass.first().attr(NAME);
        }
        
        Elements interfaces = clazz.getElementsByTag(IMPLEMENTS);
        if(interfaces != null && interfaces.size() > 0) {
            for(Element iface : interfaces) {
                info.interfaces.add(iface.attr(NAME));
            }
        }
        
        Elements fields = clazz.getElementsByTag(FIELD);
        if(fields != null && fields.size() > 0) {
            for(Element field : fields) {
                putField(field, info);
            }
        }
        
        Elements methods = clazz.getElementsByTag(METHOD);
        if(methods != null && methods.size() > 0) {
            for(Element method : methods) {
                putMethod(method, info);
            }
        }
        
        return info;
    }

    private void putField(Element field, ClassInfo clazz) {
        if(!field.hasAttr(NAME)) return;
        FieldInfo info = new FieldInfo();

        info.name = field.attr(NAME);

        if(field.hasAttr(SINCE)) {
            info.since = Integer.parseInt(field.attr(SINCE));
        }

        if(field.hasAttr(DEPRECATED)) {
            info.deprecated = Integer.parseInt(field.attr(DEPRECATED));
        }

        if(field.hasAttr(REMOVED)) {
            info.removed = Integer.parseInt(field.attr(REMOVED));
        }
        
        clazz.fields.put(info.name, info);
    }

    private void putMethod(Element method, ClassInfo clazz) {
        if(!method.hasAttr(NAME)) return;
        MethodInfo info = new MethodInfo();

        info.name = method.attr(NAME);
        info.simpleName = info.name.substring(0, info.name.indexOf("("));

        if(method.hasAttr(SINCE)) {
            info.since = Integer.parseInt(method.attr(SINCE));
        }

        if(method.hasAttr(DEPRECATED)) {
            info.deprecated = Integer.parseInt(method.attr(DEPRECATED));
        }

        if(method.hasAttr(REMOVED)) {
            info.removed = Integer.parseInt(method.attr(REMOVED));
        }

        clazz.methods.put(info.name, info);
    }
}
