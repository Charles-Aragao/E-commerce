@echo off
echo Compilando E-Commerce Java...

if not exist "bin" mkdir bin

javac -d bin -cp src src\main\java\com\ecommerce\*.java src\main\java\com\ecommerce\model\*.java src\main\java\com\ecommerce\dao\*.java src\main\java\com\ecommerce\service\*.java src\main\java\com\ecommerce\util\*.java src\main\java\com\ecommerce\view\*.java

if %errorlevel% == 0 (
    echo Compilacao concluida com sucesso!
    echo Para executar: run.bat
) else (
    echo Erro na compilacao!
)

pause