@echo off

echo 程序运行中...

java -jar %~dp0\网易云下载音乐(免vip).jar

echo 程序正在退出...

ping -n 2 127.1 >nul

exit