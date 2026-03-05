#!/bin/bash

# Script para configurar variáveis de ambiente e executar os testes

# Cores para output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}Carrefour API Test Automation${NC}"
echo -e "${GREEN}========================================${NC}"
echo ""

# Configurar variáveis de ambiente padrão
export BASE_URL="${BASE_URL:-https://jsonplaceholder.typicode.com}"
export EMAIL="${EMAIL:-test@example.com}"
export PASSWORD="${PASSWORD:-defaultpassword}"

echo -e "${YELLOW}Configurações:${NC}"
echo "BASE_URL: $BASE_URL"
echo "EMAIL: $EMAIL"
echo ""

# Executar testes
echo -e "${GREEN}Executando testes...${NC}"
mvn clean test

# Verificar resultado
if [ $? -eq 0 ]; then
    echo ""
    echo -e "${GREEN}✓ Testes executados com sucesso!${NC}"
    echo ""
    echo -e "${YELLOW}Para visualizar o relatório Allure, execute:${NC}"
    echo "allure serve target/allure-results"
else
    echo ""
    echo -e "${RED}✗ Alguns testes falharam. Verifique os logs acima.${NC}"
    exit 1
fi
