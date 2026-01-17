@echo off
setlocal enabledelayedexpansion

if exist .env (
    echo Loading environment variables from .env
    for /f "usebackq delims=" %%L in (".env") do (
		set "line=%%L"
		if not "!line!"=="" if "!line:~0,1!" neq "#" (
			set "!line!"
		)
	)
) else (
    echo .env file not found
    exit /b 1
)


echo DB_URL=%DB_USERNAME%
echo DB_USERNAME=%DB_USERNAME%
echo DB_PASSWORD=%DB_USERNAME%
echo JAVA_OPTS=%DB_USERNAME%

call mvnw clean package -DskipTests || exit /b 1

for %%J in (target\*.jar) do set JAR_FILE=%%J

if not defined JAR_FILE (
    echo No jar file found in target\
    exit /b 1
)

java -jar "%JAR_FILE%"