@echo off

echo ����������...

java -jar %~dp0\NeteasyMusicDownLoad.jar

echo ���������˳�...

ping -n 2 127.1 >nul

exit