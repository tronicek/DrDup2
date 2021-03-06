@echo off
call ..\setEnv.bat

java %OPTIONS% -jar %DRDUP2_JAR% h2-full-compressed-naive.properties
java %OPTIONS% -jar %DRDUP2_JAR% h2-full-compressed-persistent.properties
java %OPTIONS% -jar %DRDUP2_JAR% h2-full-plain-naive.properties
java %OPTIONS% -jar %DRDUP2_JAR% h2-full-plain-persistent.properties
java %OPTIONS% -jar %DRDUP2_JAR% h2-simplified-compressed-naive.properties
java %OPTIONS% -jar %DRDUP2_JAR% h2-simplified-compressed-persistent.properties
java %OPTIONS% -jar %DRDUP2_JAR% h2-simplified-plain-naive.properties
java %OPTIONS% -jar %DRDUP2_JAR% h2-simplified-plain-persistent.properties

java %OPTIONS% -jar %DRDUP2_JAR% jaxp-full-compressed-naive.properties
java %OPTIONS% -jar %DRDUP2_JAR% jaxp-full-compressed-persistent.properties
java %OPTIONS% -jar %DRDUP2_JAR% jaxp-full-plain-naive.properties
java %OPTIONS% -jar %DRDUP2_JAR% jaxp-full-plain-persistent.properties
java %OPTIONS% -jar %DRDUP2_JAR% jaxp-simplified-compressed-naive.properties
java %OPTIONS% -jar %DRDUP2_JAR% jaxp-simplified-compressed-persistent.properties
java %OPTIONS% -jar %DRDUP2_JAR% jaxp-simplified-plain-naive.properties
java %OPTIONS% -jar %DRDUP2_JAR% jaxp-simplified-plain-persistent.properties

java %OPTIONS% -jar %DRDUP2_JAR% jedit-full-compressed-naive.properties
java %OPTIONS% -jar %DRDUP2_JAR% jedit-full-compressed-persistent.properties
java %OPTIONS% -jar %DRDUP2_JAR% jedit-full-plain-naive.properties
java %OPTIONS% -jar %DRDUP2_JAR% jedit-full-plain-persistent.properties
java %OPTIONS% -jar %DRDUP2_JAR% jedit-simplified-compressed-naive.properties
java %OPTIONS% -jar %DRDUP2_JAR% jedit-simplified-compressed-persistent.properties
java %OPTIONS% -jar %DRDUP2_JAR% jedit-simplified-plain-naive.properties
java %OPTIONS% -jar %DRDUP2_JAR% jedit-simplified-plain-persistent.properties

java %OPTIONS% -jar %DRDUP2_JAR% mockito-full-compressed-naive.properties
java %OPTIONS% -jar %DRDUP2_JAR% mockito-full-compressed-persistent.properties
java %OPTIONS% -jar %DRDUP2_JAR% mockito-full-plain-naive.properties
java %OPTIONS% -jar %DRDUP2_JAR% mockito-full-plain-persistent.properties
java %OPTIONS% -jar %DRDUP2_JAR% mockito-simplified-compressed-naive.properties
java %OPTIONS% -jar %DRDUP2_JAR% mockito-simplified-compressed-persistent.properties
java %OPTIONS% -jar %DRDUP2_JAR% mockito-simplified-plain-naive.properties
java %OPTIONS% -jar %DRDUP2_JAR% mockito-simplified-plain-persistent.properties

java %OPTIONS% -jar %DRDUP2_JAR% nashorn-full-compressed-naive.properties
java %OPTIONS% -jar %DRDUP2_JAR% nashorn-full-compressed-persistent.properties
java %OPTIONS% -jar %DRDUP2_JAR% nashorn-full-plain-naive.properties
java %OPTIONS% -jar %DRDUP2_JAR% nashorn-full-plain-persistent.properties
java %OPTIONS% -jar %DRDUP2_JAR% nashorn-simplified-compressed-naive.properties
java %OPTIONS% -jar %DRDUP2_JAR% nashorn-simplified-compressed-persistent.properties
java %OPTIONS% -jar %DRDUP2_JAR% nashorn-simplified-plain-naive.properties
java %OPTIONS% -jar %DRDUP2_JAR% nashorn-simplified-plain-persistent.properties

java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-h2-full-compressed-naive.xml                 drdup-h2-full-compressed-persistent.xml             drdup-h2-diff1.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-h2-full-compressed-persistent.xml            drdup-h2-full-plain-naive.xml                       drdup-h2-diff2.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-h2-full-plain-naive.xml                      drdup-h2-full-plain-persistent.xml                  drdup-h2-diff3.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-h2-full-plain-persistent.xml                 drdup-h2-simplified-compressed-naive.xml            drdup-h2-diff4.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-h2-simplified-compressed-naive.xml           drdup-h2-simplified-compressed-persistent.xml       drdup-h2-diff5.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-h2-simplified-compressed-persistent.xml      drdup-h2-simplified-plain-naive.xml                 drdup-h2-diff6.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-h2-simplified-plain-naive.xml                drdup-h2-simplified-plain-persistent.xml            drdup-h2-diff7.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-h2-simplified-plain-persistent.xml           drdup-h2-full-compressed-naive.xml                  drdup-h2-diff8.xml

java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-jaxp-full-compressed-naive.xml               drdup-jaxp-full-compressed-persistent.xml           drdup-jaxp-diff1.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-jaxp-full-compressed-persistent.xml          drdup-jaxp-full-plain-naive.xml                     drdup-jaxp-diff2.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-jaxp-full-plain-naive.xml                    drdup-jaxp-full-plain-persistent.xml                drdup-jaxp-diff3.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-jaxp-full-plain-persistent.xml               drdup-jaxp-simplified-compressed-naive.xml          drdup-jaxp-diff4.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-jaxp-simplified-compressed-naive.xml         drdup-jaxp-simplified-compressed-persistent.xml     drdup-jaxp-diff5.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-jaxp-simplified-compressed-persistent.xml    drdup-jaxp-simplified-plain-naive.xml               drdup-jaxp-diff6.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-jaxp-simplified-plain-naive.xml              drdup-jaxp-simplified-plain-persistent.xml          drdup-jaxp-diff7.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-jaxp-simplified-plain-persistent.xml         drdup-jaxp-full-compressed-naive.xml                drdup-jaxp-diff8.xml

java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-jedit-full-compressed-naive.xml              drdup-jedit-full-compressed-persistent.xml          drdup-jedit-diff1.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-jedit-full-compressed-persistent.xml         drdup-jedit-full-plain-naive.xml                    drdup-jedit-diff2.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-jedit-full-plain-naive.xml                   drdup-jedit-full-plain-persistent.xml               drdup-jedit-diff3.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-jedit-full-plain-persistent.xml              drdup-jedit-simplified-compressed-naive.xml         drdup-jedit-diff4.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-jedit-simplified-compressed-naive.xml        drdup-jedit-simplified-compressed-persistent.xml    drdup-jedit-diff5.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-jedit-simplified-compressed-persistent.xml   drdup-jedit-simplified-plain-naive.xml              drdup-jedit-diff6.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-jedit-simplified-plain-naive.xml             drdup-jedit-simplified-plain-persistent.xml         drdup-jedit-diff7.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-jedit-simplified-plain-persistent.xml        drdup-jedit-full-compressed-naive.xml               drdup-jedit-diff8.xml

java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-mockito-full-compressed-naive.xml            drdup-mockito-full-compressed-persistent.xml        drdup-mockito-diff1.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-mockito-full-compressed-persistent.xml       drdup-mockito-full-plain-naive.xml                  drdup-mockito-diff2.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-mockito-full-plain-naive.xml                 drdup-mockito-full-plain-persistent.xml             drdup-mockito-diff3.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-mockito-full-plain-persistent.xml            drdup-mockito-simplified-compressed-naive.xml       drdup-mockito-diff4.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-mockito-simplified-compressed-naive.xml      drdup-mockito-simplified-compressed-persistent.xml  drdup-mockito-diff5.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-mockito-simplified-compressed-persistent.xml drdup-mockito-simplified-plain-naive.xml            drdup-mockito-diff6.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-mockito-simplified-plain-naive.xml           drdup-mockito-simplified-plain-persistent.xml       drdup-mockito-diff7.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-mockito-simplified-plain-persistent.xml      drdup-mockito-full-compressed-naive.xml             drdup-mockito-diff8.xml

java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-nashorn-full-compressed-naive.xml            drdup-nashorn-full-compressed-persistent.xml        drdup-nashorn-diff1.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-nashorn-full-compressed-persistent.xml       drdup-nashorn-full-plain-naive.xml                  drdup-nashorn-diff2.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-nashorn-full-plain-naive.xml                 drdup-nashorn-full-plain-persistent.xml             drdup-nashorn-diff3.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-nashorn-full-plain-persistent.xml            drdup-nashorn-simplified-compressed-naive.xml       drdup-nashorn-diff4.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-nashorn-simplified-compressed-naive.xml      drdup-nashorn-simplified-compressed-persistent.xml  drdup-nashorn-diff5.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-nashorn-simplified-compressed-persistent.xml drdup-nashorn-simplified-plain-naive.xml            drdup-nashorn-diff6.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-nashorn-simplified-plain-naive.xml           drdup-nashorn-simplified-plain-persistent.xml       drdup-nashorn-diff7.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-nashorn-simplified-plain-persistent.xml      drdup-nashorn-full-compressed-naive.xml             drdup-nashorn-diff8.xml

del data\*.bin