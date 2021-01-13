@echo off
call ..\..\setEnv.bat

set SRC_DIR="/research/jEdit5.3.0/src"

java %OPTIONS% -jar %DRDUP_JAR% drdup-jedit-3.properties
java %OPTIONS% -cp %TOOL_JAR% drdup.Separator drdup-jedit-3.xml

java %OPTIONS% -jar %DRDUP_JAR% drdup-jedit-5.properties
java %OPTIONS% -cp %TOOL_JAR% drdup.Separator drdup-jedit-5.xml

java %OPTIONS% -jar %DRDUP2_JAR% drdup2-jedit-3.properties
java %OPTIONS% -cp %TOOL_JAR% drdup.Separator drdup2-jedit-3.xml

java %OPTIONS% -jar %DRDUP2_JAR% drdup2-jedit-5.properties
java %OPTIONS% -cp %TOOL_JAR% drdup.Separator drdup2-jedit-5.xml

java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-jedit-5-separated.xml drdup2-jedit-3-separated.xml drdup-drdup2.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup2-jedit-5-separated.xml drdup-jedit-3-separated.xml drdup2-drdup.xml

java %OPTIONS% -cp %TOOL_JAR% drdup.Sourcer %SRC_DIR% drdup-drdup2.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Sourcer %SRC_DIR% drdup2-drdup.xml
