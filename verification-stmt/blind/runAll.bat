@echo off
cd batchsize
call run.bat
cd ..

cd batchsize-compressed
call run.bat
cd ..

cd comparison
call run.bat
cd ..

cd h2
call run.bat
cd ..

cd jaxp
call run.bat
cd ..

cd jedit
call run.bat
cd ..

cd mockito
call run.bat
cd ..

cd nashorn
call run.bat
cd ..
