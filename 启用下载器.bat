@echo off

echo ����������...

java -jar %~dp0\DownLoader.jar

echo ���������˳�...

ping -n 2 127.1 >nul

exit