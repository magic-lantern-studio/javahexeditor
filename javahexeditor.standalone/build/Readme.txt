To turn Java executable JAR files into operating system executable, wrappers are required.

For Windows, this wrapper is provide by launch4j (http://launch4j.sourceforge.net/).

For Mac OS X, this wrapper is provided by the JarBundler task (https://github.com/UltraMixer/JarBundler/releases)
together with the universal JavaApplicationStub (https://github.com/tofi86/universalJavaApplicationStub/releases).
The latter is the successor of the the native JavaApplicationStub (2.3.1).

JarBundler
==========

JarBundler is a feature-rich Ant task which will create a Mac OS X application bundle from a list of Jar files and a main class name.
You can add an Icon resource, set various Mac OS X native look-and-feel bells and whistles, and maintain your application bundles as part of your normal build and release cycle.

See "https://github.com/UltraMixer/JarBundler/releases" to download "jarbundler-core-3.3.0.jar".

Installation
------------
Add the file"jarbundler-core-3.3.0.jar" in the preferences under "Ant / Runtime / Classpath / Global Entries".
To use the Jar Bundler Ant Task, create a task definition in your ANT build.xml file like this:
<taskdef name="jarbundler" classname="net.sourceforge.jarbundler.JarBundler" />


Files
=====

* jarbundler-core-3.3.0.jar - The download of the JarBundler task
* jarbundler.html - The download of the JarBundler task documentation
* JavaApplicationStub - The old native stub previously used by the Mac OS build, only keep for archiving purposes
* JavaHexEditor-Makefile.ant - The ANT makefile to build the binaries for all platforms
* stub.sh - The executable /bin/sh shell stub script used by the Linux build
* universalJavaApplicationStub - The executable /bin/bash shell stub script used by the Mac OS X build. The file is encoded in UTF-8 without BOM.

