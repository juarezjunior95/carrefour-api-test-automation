@echo off
REM Script para configurar variáveis de ambiente e executar os testes no Windows

echo ========================================
echo Carrefour API Test Automation
echo ========================================
echo.

REM Configurar variáveis de ambiente padrão
if not defined BASE_URL set BASE_URL=https://jsonplaceholder.typicode.com
if not defined EMAIL set EMAIL=test@example.com
if not defined PASSWORD set PASSWORD=defaultpassword

echo Configurações:
echo BASE_URL: %BASE_URL%
echo EMAIL: %EMAIL%
echo.

REM Executar testes
echo Executando testes...
call mvn clean test

if %ERRORLEVEL% EQU 0 (
    echo.
    echo [SUCCESS] Testes executados com sucesso!
    echo.
    echo Para visualizar o relatório Allure, execute:
    echo allure serve target/allure-results
) else (
    echo.
    echo [ERROR] Alguns testes falharam. Verifique os logs acima.
    exit /b 1
)
