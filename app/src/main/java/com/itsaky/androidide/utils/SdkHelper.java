package com.itsaky.androidide.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SdkHelper{
	    public static final String SDK="android-sdk";
		public static final String BUILD_TOOLS="build-tools";
		public static final String PLATFORM_TOOLS="platform-tools";
		public static final String NDK="ndk";
		public static final String CMAKE="cmake";
		public static final String CMDLINE_TOOLS="cmdline-tools";

     public static String setupJDK(){
	 StringBuilder sb = new StringBuilder();
	 sb.append("pkg install -y openjdk-17");
	 join(sb);
	 sb.append("echo 'JAVA_HOME=/data/data/com.itsaky.androidide/files/usr/opt/openjdk' >> $SYSROOT/etc/ide-environment.properties");
	 join(sb);
	 return sb.toString();
     }

    public static String setupZip(File f){
	StringBuilder sb = new StringBuilder();
	if (f.getName().endsWith(".tar.xz")) {
		      //Tar Files Found
              if (f.getName().startsWith("cmdline-tools")|| f.getName().startsWith("platform-tools")) {
                sb.append("mkdir -p $HOME/android-sdk");
                join(sb);
                sb.append("$BUSYBOX tar xvJf ").append("$HOME/").append(f.getName()).append(" -C $HOME/android-sdk");
              }
             //build tools content must be places in android-sdk/build-tools folder
	          else if(f.getName().startsWith("build-tools")){
		      sb.append("mkdir -p $HOME/android-sdk/build-tools");
		      join(sb);
		      sb.append("$BUSYBOX tar xvJf ").append("$HOME/").append(f.getName()).append(" -C $HOME/android-sdk/build-tools");
	          }
	          else {
                sb.append("$BUSYBOX tar xvJf ").append("$HOME/").append(f.getName());
                join(sb);
                sb.append("cd $HOME");
              }
              join(sb);
    }
	else if (f.getName().endsWith(".zip")) {
	 	//Regular Zips Found
	    if(f.getName().startsWith("android-ndk-r24"))
	    sb.append(setupNDK());
	    else if(f.getName().startsWith("cmake"))
	    sb.append(setupCMAKE());
	    else{
        sb.append("$BUSYBOX unzip ").append(f.getAbsolutePath());
        join(sb);
		}
    }
	return sb.toString();
 }

 public static String setupCMAKE(){
	 StringBuilder builder = new StringBuilder();
	 builder.append("unzip cmake.zip -d $HOME/android-sdk");
	 join(builder);
	 builder.append("chmod -R +x $HOME/android-sdk/cmake/3.23.1/bin");
	 join(builder);
	 builder.append("echo -e '\nPATH=$PATH:$HOME/android-sdk/cmake/3.23.1/bin' >> $SYSROOT/etc/ide-environment.properties");
	 join(builder);
	 builder.append("echo 'NDK Has been Installed Sucessfully'");
	 join(builder);
	 return builder.toString();
 }


 public static String setupNDK(){
	//Thanks to MrIkso for Ndk Script
	//https://github.com/MrIkso/AndroidIDE-NDK
	StringBuilder builder = new StringBuilder();
	builder.append("mkdir -p $HOME/android-sdk/ndk/24.0.8215888");
	join(builder);
	builder.append("$BUSYBOX unzip $HOME/android-ndk-r24-aarch64.zip").append(" -d $HOME/android-sdk/ndk/24.0.8215888");
	join(builder);
	builder.append("ln -s $HOME/android-sdk/ndk/24.0.8215888/toolchains/llvm/prebuilt/linux-aarch64 $HOME/android-sdk/ndk/24.0.8215888/toolchains/llvm/prebuilt/linux-x86_64");
	join(builder);
	builder.append("ln -s $HOME/android-sdk/ndk/24.0.8215888/prebuilt/linux-aarch64 $HOME/android-sdk/ndk/24.0.8215888/prebuilt/linux-x86_64");
	join(builder);
    //Cmake Config
    builder.append("sed -i 's/if(CMAKE_HOST_SYSTEM_NAME STREQUAL Linux)/if(CMAKE_HOST_SYSTEM_NAME STREQUAL Android)\nset(ANDROID_HOST_TAG linux-aarch64)\nelseif(CMAKE_HOST_SYSTEM_NAME STREQUAL Linux)/g' $HOME/android-sdk/ndk/24.0.8215888/build/cmake/android-legacy.toolchain.cmake");
    join(builder);
    builder.append("sed -i 's/if(CMAKE_HOST_SYSTEM_NAME STREQUAL Linux)/if(CMAKE_HOST_SYSTEM_NAME STREQUAL Android)\nset(ANDROID_HOST_TAG linux-aarch64)\nelseif(CMAKE_HOST_SYSTEM_NAME STREQUAL Linux)/g' $HOME/android-sdk/ndk/24.0.8215888/build/cmake/android.toolchain.cmake");
    join(builder);
    
    return builder.toString();
 }

 public static String postInstall(){
   //For cleaing all downloaded zips,tars
	 StringBuilder sb = new StringBuilder();
	 sb.append("echo 'Running Post Install Process'");
     join(sb);
     sb.append("rm -rf *.tar.xz && rm -rf *.zip");
     join(sb);
     sb.append("echo 'Done'");
     join(sb);

    return sb.toString();
 }
 
 public static void join(StringBuilder sBuilder){
	 sBuilder.append("\n");
 	}

 public static Map<String,String> getLinks(ArrayList<HashMap<String,Object>> al){
		Map<String,String> temp = new HashMap<>();
		if(System.getProperty("os.arch").equals("aarch64"))
		{
		temp.put(SDK,al.get(0).get(SDK).toString());
		temp.put(BUILD_TOOLS,al.get(0).get(BUILD_TOOLS).toString());
		temp.put(PLATFORM_TOOLS,al.get(0).get(PLATFORM_TOOLS).toString());
		temp.put(NDK,al.get(0).get(NDK).toString());
		temp.put(CMAKE,al.get(0).get(CMAKE).toString());
		temp.put(CMDLINE_TOOLS,al.get(0).get(CMDLINE_TOOLS).toString());
		}
		else {
		temp.put(SDK,al.get(1).get(SDK).toString());
		temp.put(BUILD_TOOLS,al.get(1).get(BUILD_TOOLS).toString());
		temp.put(PLATFORM_TOOLS,al.get(1).get(PLATFORM_TOOLS).toString());
		temp.put(CMDLINE_TOOLS,al.get(1).get(CMDLINE_TOOLS).toString());
		}
		
	return temp;
	}
}

