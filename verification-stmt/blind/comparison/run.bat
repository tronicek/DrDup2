@echo off
call ..\setEnv.bat

java %OPTIONS% -jar %DRDUP2_JAR% h2-full-compressed-naive.properties
java %OPTIONS% -jar %DRDUP2_JAR% h2-full-compressed-persistent.properties
java %OPTIONS% -jar %DRDUP2_JAR% h2-full-compressed.properties
java %OPTIONS% -jar %DRDUP2_JAR% h2-full-plain-naive.properties
java %OPTIONS% -jar %DRDUP2_JAR% h2-full-plain-persistent.properties
java %OPTIONS% -jar %DRDUP2_JAR% h2-full-plain.properties
java %OPTIONS% -jar %DRDUP2_JAR% h2-simplified-compressed-naive.properties
java %OPTIONS% -jar %DRDUP2_JAR% h2-simplified-compressed-persistent.properties
java %OPTIONS% -jar %DRDUP2_JAR% h2-simplified-compressed.properties
java %OPTIONS% -jar %DRDUP2_JAR% h2-simplified-plain-naive.properties
java %OPTIONS% -jar %DRDUP2_JAR% h2-simplified-plain-persistent.properties
java %OPTIONS% -jar %DRDUP2_JAR% h2-simplified-plain.properties

java %OPTIONS% -jar %DRDUP2_JAR% jaxp-full-compressed-naive.properties
java %OPTIONS% -jar %DRDUP2_JAR% jaxp-full-compressed-persistent.properties
java %OPTIONS% -jar %DRDUP2_JAR% jaxp-full-compressed.properties
java %OPTIONS% -jar %DRDUP2_JAR% jaxp-full-plain-naive.properties
java %OPTIONS% -jar %DRDUP2_JAR% jaxp-full-plain-persistent.properties
java %OPTIONS% -jar %DRDUP2_JAR% jaxp-full-plain.properties
java %OPTIONS% -jar %DRDUP2_JAR% jaxp-simplified-compressed-naive.properties
java %OPTIONS% -jar %DRDUP2_JAR% jaxp-simplified-compressed-persistent.properties
java %OPTIONS% -jar %DRDUP2_JAR% jaxp-simplified-compressed.properties
java %OPTIONS% -jar %DRDUP2_JAR% jaxp-simplified-plain-naive.properties
java %OPTIONS% -jar %DRDUP2_JAR% jaxp-simplified-plain-persistent.properties
java %OPTIONS% -jar %DRDUP2_JAR% jaxp-simplified-plain.properties

java %OPTIONS% -jar %DRDUP2_JAR% jedit-full-compressed-naive.properties
java %OPTIONS% -jar %DRDUP2_JAR% jedit-full-compressed-persistent.properties
java %OPTIONS% -jar %DRDUP2_JAR% jedit-full-compressed.properties
java %OPTIONS% -jar %DRDUP2_JAR% jedit-full-plain-naive.properties
java %OPTIONS% -jar %DRDUP2_JAR% jedit-full-plain-persistent.properties
java %OPTIONS% -jar %DRDUP2_JAR% jedit-full-plain.properties
java %OPTIONS% -jar %DRDUP2_JAR% jedit-simplified-compressed-naive.properties
java %OPTIONS% -jar %DRDUP2_JAR% jedit-simplified-compressed-persistent.properties
java %OPTIONS% -jar %DRDUP2_JAR% jedit-simplified-compressed.properties
java %OPTIONS% -jar %DRDUP2_JAR% jedit-simplified-plain-naive.properties
java %OPTIONS% -jar %DRDUP2_JAR% jedit-simplified-plain-persistent.properties
java %OPTIONS% -jar %DRDUP2_JAR% jedit-simplified-plain.properties

java %OPTIONS% -jar %DRDUP2_JAR% mockito-full-compressed-naive.properties
java %OPTIONS% -jar %DRDUP2_JAR% mockito-full-compressed-persistent.properties
java %OPTIONS% -jar %DRDUP2_JAR% mockito-full-compressed.properties
java %OPTIONS% -jar %DRDUP2_JAR% mockito-full-plain-naive.properties
java %OPTIONS% -jar %DRDUP2_JAR% mockito-full-plain-persistent.properties
java %OPTIONS% -jar %DRDUP2_JAR% mockito-full-plain.properties
java %OPTIONS% -jar %DRDUP2_JAR% mockito-simplified-compressed-naive.properties
java %OPTIONS% -jar %DRDUP2_JAR% mockito-simplified-compressed-persistent.properties
java %OPTIONS% -jar %DRDUP2_JAR% mockito-simplified-compressed.properties
java %OPTIONS% -jar %DRDUP2_JAR% mockito-simplified-plain-naive.properties
java %OPTIONS% -jar %DRDUP2_JAR% mockito-simplified-plain-persistent.properties
java %OPTIONS% -jar %DRDUP2_JAR% mockito-simplified-plain.properties

java %OPTIONS% -jar %DRDUP2_JAR% nashorn-full-compressed-naive.properties
java %OPTIONS% -jar %DRDUP2_JAR% nashorn-full-compressed-persistent.properties
java %OPTIONS% -jar %DRDUP2_JAR% nashorn-full-compressed.properties
java %OPTIONS% -jar %DRDUP2_JAR% nashorn-full-plain-naive.properties
java %OPTIONS% -jar %DRDUP2_JAR% nashorn-full-plain-persistent.properties
java %OPTIONS% -jar %DRDUP2_JAR% nashorn-full-plain.properties
java %OPTIONS% -jar %DRDUP2_JAR% nashorn-simplified-compressed-naive.properties
java %OPTIONS% -jar %DRDUP2_JAR% nashorn-simplified-compressed-persistent.properties
java %OPTIONS% -jar %DRDUP2_JAR% nashorn-simplified-compressed.properties
java %OPTIONS% -jar %DRDUP2_JAR% nashorn-simplified-plain-naive.properties
java %OPTIONS% -jar %DRDUP2_JAR% nashorn-simplified-plain-persistent.properties
java %OPTIONS% -jar %DRDUP2_JAR% nashorn-simplified-plain.properties

java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-h2-full-compressed-naive.xml                 drdup-h2-full-compressed-persistent.xml             drdup-h2-diff1.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-h2-full-compressed-persistent.xml            drdup-h2-full-compressed.xml                        drdup-h2-diff2.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-h2-full-compressed.xml                       drdup-h2-full-plain-naive.xml                       drdup-h2-diff3.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-h2-full-plain-naive.xml                      drdup-h2-full-plain-persistent.xml                  drdup-h2-diff4.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-h2-full-plain-persistent.xml                 drdup-h2-full-plain.xml                             drdup-h2-diff5.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-h2-full-plain.xml                            drdup-h2-simplified-compressed-naive.xml            drdup-h2-diff6.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-h2-simplified-compressed-naive.xml           drdup-h2-simplified-compressed-persistent.xml       drdup-h2-diff7.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-h2-simplified-compressed-persistent.xml      drdup-h2-simplified-compressed.xml                  drdup-h2-diff8.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-h2-simplified-compressed.xml                 drdup-h2-simplified-plain-naive.xml                 drdup-h2-diff9.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-h2-simplified-plain-naive.xml                drdup-h2-simplified-plain-persistent.xml            drdup-h2-diff10.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-h2-simplified-plain-persistent.xml           drdup-h2-simplified-plain.xml                       drdup-h2-diff11.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-h2-simplified-plain.xml                      drdup-h2-full-compressed-naive.xml                  drdup-h2-diff12.xml

java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-jaxp-full-compressed-naive.xml               drdup-jaxp-full-compressed-persistent.xml           drdup-jaxp-diff1.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-jaxp-full-compressed-persistent.xml          drdup-jaxp-full-compressed.xml                      drdup-jaxp-diff2.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-jaxp-full-compressed.xml                     drdup-jaxp-full-plain-naive.xml                     drdup-jaxp-diff3.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-jaxp-full-plain-naive.xml                    drdup-jaxp-full-plain-persistent.xml                drdup-jaxp-diff4.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-jaxp-full-plain-persistent.xml               drdup-jaxp-full-plain.xml                           drdup-jaxp-diff5.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-jaxp-full-plain.xml                          drdup-jaxp-simplified-compressed-naive.xml          drdup-jaxp-diff6.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-jaxp-simplified-compressed-naive.xml         drdup-jaxp-simplified-compressed-persistent.xml     drdup-jaxp-diff7.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-jaxp-simplified-compressed-persistent.xml    drdup-jaxp-simplified-compressed.xml                drdup-jaxp-diff8.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-jaxp-simplified-compressed.xml               drdup-jaxp-simplified-plain-naive.xml               drdup-jaxp-diff9.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-jaxp-simplified-plain-naive.xml              drdup-jaxp-simplified-plain-persistent.xml          drdup-jaxp-diff10.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-jaxp-simplified-plain-persistent.xml         drdup-jaxp-simplified-plain.xml                     drdup-jaxp-diff11.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-jaxp-simplified-plain.xml                    drdup-jaxp-full-compressed-naive.xml                drdup-jaxp-diff12.xml

java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-jedit-full-compressed-naive.xml              drdup-jedit-full-compressed-persistent.xml          drdup-jedit-diff1.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-jedit-full-compressed-persistent.xml         drdup-jedit-full-compressed.xml                     drdup-jedit-diff2.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-jedit-full-compressed.xml                    drdup-jedit-full-plain-naive.xml                    drdup-jedit-diff3.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-jedit-full-plain-naive.xml                   drdup-jedit-full-plain-persistent.xml               drdup-jedit-diff4.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-jedit-full-plain-persistent.xml              drdup-jedit-full-plain.xml                          drdup-jedit-diff5.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-jedit-full-plain.xml                         drdup-jedit-simplified-compressed-naive.xml         drdup-jedit-diff6.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-jedit-simplified-compressed-naive.xml        drdup-jedit-simplified-compressed-persistent.xml    drdup-jedit-diff7.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-jedit-simplified-compressed-persistent.xml   drdup-jedit-simplified-compressed.xml               drdup-jedit-diff8.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-jedit-simplified-compressed.xml              drdup-jedit-simplified-plain-naive.xml              drdup-jedit-diff9.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-jedit-simplified-plain-naive.xml             drdup-jedit-simplified-plain-persistent.xml         drdup-jedit-diff10.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-jedit-simplified-plain-persistent.xml        drdup-jedit-simplified-plain.xml                    drdup-jedit-diff11.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-jedit-simplified-plain.xml                   drdup-jedit-full-compressed-naive.xml               drdup-jedit-diff12.xml

java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-mockito-full-compressed-naive.xml            drdup-mockito-full-compressed-persistent.xml        drdup-mockito-diff1.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-mockito-full-compressed-persistent.xml       drdup-mockito-full-compressed.xml                   drdup-mockito-diff2.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-mockito-full-compressed.xml                  drdup-mockito-full-plain-naive.xml                  drdup-mockito-diff3.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-mockito-full-plain-naive.xml                 drdup-mockito-full-plain-persistent.xml             drdup-mockito-diff4.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-mockito-full-plain-persistent.xml            drdup-mockito-full-plain.xml                        drdup-mockito-diff5.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-mockito-full-plain.xml                       drdup-mockito-simplified-compressed-naive.xml       drdup-mockito-diff6.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-mockito-simplified-compressed-naive.xml      drdup-mockito-simplified-compressed-persistent.xml  drdup-mockito-diff7.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-mockito-simplified-compressed-persistent.xml drdup-mockito-simplified-compressed.xml             drdup-mockito-diff8.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-mockito-simplified-compressed.xml            drdup-mockito-simplified-plain-naive.xml            drdup-mockito-diff9.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-mockito-simplified-plain-naive.xml           drdup-mockito-simplified-plain-persistent.xml       drdup-mockito-diff10.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-mockito-simplified-plain-persistent.xml      drdup-mockito-simplified-plain.xml                  drdup-mockito-diff11.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-mockito-simplified-plain.xml                 drdup-mockito-full-compressed-naive.xml             drdup-mockito-diff12.xml

java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-nashorn-full-compressed-naive.xml            drdup-nashorn-full-compressed-persistent.xml        drdup-nashorn-diff1.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-nashorn-full-compressed-persistent.xml       drdup-nashorn-full-compressed.xml                   drdup-nashorn-diff2.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-nashorn-full-compressed.xml                  drdup-nashorn-full-plain-naive.xml                  drdup-nashorn-diff3.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-nashorn-full-plain-naive.xml                 drdup-nashorn-full-plain-persistent.xml             drdup-nashorn-diff4.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-nashorn-full-plain-persistent.xml            drdup-nashorn-full-plain.xml                        drdup-nashorn-diff5.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-nashorn-full-plain.xml                       drdup-nashorn-simplified-compressed-naive.xml       drdup-nashorn-diff6.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-nashorn-simplified-compressed-naive.xml      drdup-nashorn-simplified-compressed-persistent.xml  drdup-nashorn-diff7.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-nashorn-simplified-compressed-persistent.xml drdup-nashorn-simplified-compressed.xml             drdup-nashorn-diff8.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-nashorn-simplified-compressed.xml            drdup-nashorn-simplified-plain-naive.xml            drdup-nashorn-diff9.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-nashorn-simplified-plain-naive.xml           drdup-nashorn-simplified-plain-persistent.xml       drdup-nashorn-diff10.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-nashorn-simplified-plain-persistent.xml      drdup-nashorn-simplified-plain.xml                  drdup-nashorn-diff11.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-nashorn-simplified-plain.xml                 drdup-nashorn-full-compressed-naive.xml             drdup-nashorn-diff12.xml

del data\*.bin
