@echo off
call ..\setEnv.bat

set NIL_JAR="\research\NIL\NIL-master\build\libs\NIL-all.jar"

set NIL3="jedit-3.csv"
set NIL3xml="jedit-3.xml"
set NIL3d="jedit-3-dir.xml"
set NIL8="jedit-8.csv"
set NIL8xml="jedit-8.xml"
set NIL8d="jedit-8-dir.xml"
set NIL_SRC_DIR="C:/research/jEdit5.3.0/src/"
set SRC_DIR="/research/jEdit5.3.0/src"

REM java -jar %NIL_JAR% -s C:\research\jEdit5.3.0\src -mil 3 -mit 10 -v 100 -o %NIL3%
REM java -jar %NIL_JAR% -s C:\research\jEdit5.3.0\src -mil 8 -mit 10 -v 100 -o %NIL8%

java -cp %TOOL_JAR% nil.NiCadConvertor %NIL3%
java -cp %TOOL_JAR% nil.NiCadConvertor %NIL8%

java -cp %TOOL_JAR% drdup.ChangeDir %NIL_SRC_DIR% %NIL3xml%
java -cp %TOOL_JAR% drdup.ChangeDir %NIL_SRC_DIR% %NIL8xml%

java %OPTIONS% -jar %DRDUP2_JAR% jedit-3.properties
java %OPTIONS% -cp %TOOL_JAR% drdup.Separator drdup-jedit-3.xml

java %OPTIONS% -jar %DRDUP2_JAR% jedit-8.properties
java %OPTIONS% -cp %TOOL_JAR% drdup.Separator drdup-jedit-8.xml

java -cp %TOOL_JAR% drdup.Diff %NIL8d% drdup-jedit-3-separated.xml nil-8-drdup-3.xml
java -cp %TOOL_JAR% drdup.Diff drdup-jedit-8-separated.xml %NIL3d% drdup-8-nil-3.xml

java -cp %TOOL_JAR% drdup.Sourcer %SRC_DIR% nil-8-drdup-3.xml
java -cp %TOOL_JAR% drdup.Sourcer %SRC_DIR% drdup-8-nil-3.xml

java -jar %CHECKER_JAR% checker.properties
java -jar %CHECKER_JAR% checker2.properties
