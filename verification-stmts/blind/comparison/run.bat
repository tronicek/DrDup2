@echo off
call ..\setEnv.bat

java %OPTIONS% -jar %DRDUP2_JAR% h2-simplified-compressed-persistent.properties
java %OPTIONS% -jar %DRDUP2_JAR% h2-simplified-compressed.properties
java %OPTIONS% -jar %DRDUP2_JAR% h2-simplified-plain-persistent.properties
java %OPTIONS% -jar %DRDUP2_JAR% h2-simplified-plain.properties

java %OPTIONS% -jar %DRDUP2_JAR% jaxp-simplified-compressed-persistent.properties
java %OPTIONS% -jar %DRDUP2_JAR% jaxp-simplified-compressed.properties
java %OPTIONS% -jar %DRDUP2_JAR% jaxp-simplified-plain-persistent.properties
java %OPTIONS% -jar %DRDUP2_JAR% jaxp-simplified-plain.properties

java %OPTIONS% -jar %DRDUP2_JAR% jedit-simplified-compressed-persistent.properties
java %OPTIONS% -jar %DRDUP2_JAR% jedit-simplified-compressed.properties
java %OPTIONS% -jar %DRDUP2_JAR% jedit-simplified-plain-persistent.properties
java %OPTIONS% -jar %DRDUP2_JAR% jedit-simplified-plain.properties

java %OPTIONS% -jar %DRDUP2_JAR% mockito-simplified-compressed-persistent.properties
java %OPTIONS% -jar %DRDUP2_JAR% mockito-simplified-compressed.properties
java %OPTIONS% -jar %DRDUP2_JAR% mockito-simplified-plain-persistent.properties
java %OPTIONS% -jar %DRDUP2_JAR% mockito-simplified-plain.properties

java %OPTIONS% -jar %DRDUP2_JAR% nashorn-simplified-compressed-persistent.properties
java %OPTIONS% -jar %DRDUP2_JAR% nashorn-simplified-compressed.properties
java %OPTIONS% -jar %DRDUP2_JAR% nashorn-simplified-plain-persistent.properties
java %OPTIONS% -jar %DRDUP2_JAR% nashorn-simplified-plain.properties

java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-h2-simplified-compressed-persistent.xml drdup-h2-simplified-compressed.xml drdup-h2-diff1.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-h2-simplified-compressed.xml drdup-h2-simplified-plain-persistent.xml drdup-h2-diff2.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-h2-simplified-plain-persistent.xml drdup-h2-simplified-plain.xml drdup-h2-diff3.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-h2-simplified-plain.xml drdup-h2-simplified-compressed-persistent.xml drdup-h2-diff4.xml

java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-jaxp-simplified-compressed-persistent.xml drdup-jaxp-simplified-compressed.xml drdup-jaxp-diff1.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-jaxp-simplified-compressed.xml drdup-jaxp-simplified-plain-persistent.xml drdup-jaxp-diff2.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-jaxp-simplified-plain-persistent.xml drdup-jaxp-simplified-plain.xml drdup-jaxp-diff3.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-jaxp-simplified-plain.xml drdup-jaxp-simplified-compressed-persistent.xml drdup-jaxp-diff4.xml

java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-jedit-simplified-compressed-persistent.xml drdup-jedit-simplified-compressed.xml drdup-jedit-diff1.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-jedit-simplified-compressed.xml drdup-jedit-simplified-plain-persistent.xml drdup-jedit-diff2.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-jedit-simplified-plain-persistent.xml drdup-jedit-simplified-plain.xml drdup-jedit-diff3.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-jedit-simplified-plain.xml drdup-jedit-simplified-compressed-persistent.xml drdup-jedit-diff4.xml

java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-mockito-simplified-compressed-persistent.xml drdup-mockito-simplified-compressed.xml drdup-mockito-diff1.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-mockito-simplified-compressed.xml drdup-mockito-simplified-plain-persistent.xml drdup-mockito-diff2.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-mockito-simplified-plain-persistent.xml drdup-mockito-simplified-plain.xml drdup-mockito-diff3.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-mockito-simplified-plain.xml drdup-mockito-simplified-compressed-persistent.xml drdup-mockito-diff4.xml

java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-nashorn-simplified-compressed-persistent.xml drdup-nashorn-simplified-compressed.xml drdup-nashorn-diff1.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-nashorn-simplified-compressed.xml drdup-nashorn-simplified-plain-persistent.xml drdup-nashorn-diff2.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-nashorn-simplified-plain-persistent.xml drdup-nashorn-simplified-plain.xml drdup-nashorn-diff3.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-nashorn-simplified-plain.xml drdup-nashorn-simplified-compressed-persistent.xml drdup-nashorn-diff4.xml

del data\*.bin
