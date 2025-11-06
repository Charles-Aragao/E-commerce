@echo off
echo Executando E-Commerce Java...

if not exist "bin\com\ecommerce\ECommerceApp.class" (
    echo Projeto nao compilado! Execute compile.bat primeiro.
    pause
    exit
)

java -cp bin com.ecommerce.ECommerceApp

pause