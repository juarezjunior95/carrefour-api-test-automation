# 🚀 Carrefour API Test Automation

![API Tests](https://github.com/SEU_USUARIO/carrefour-api-test-automation/workflows/API%20Tests/badge.svg)
![Java](https://img.shields.io/badge/Java-17-orange.svg)
![Maven](https://img.shields.io/badge/Maven-3.8+-blue.svg)
![RestAssured](https://img.shields.io/badge/RestAssured-5.4.0-green.svg)
![Allure](https://img.shields.io/badge/Allure-2.25.0-yellow.svg)

Framework de automação de testes de API desenvolvido com Java, RestAssured, JUnit 5 e Allure Reports.

---

## 📋 Pré-requisitos

- **Java 17** (JDK)
- **Maven 3.8+**
- **Allure Command Line** (opcional, para visualizar relatórios)

### Instalação do Allure (opcional)

**macOS:**
```bash
brew install allure
```

**Windows:**
```bash
scoop install allure
```

**Linux:**
```bash
# Download e extração manual
wget https://github.com/allure-framework/allure2/releases/download/2.25.0/allure-2.25.0.tgz
tar -zxvf allure-2.25.0.tgz -C /opt/
sudo ln -s /opt/allure-2.25.0/bin/allure /usr/bin/allure
```

---

## 🏗️ Estrutura do Projeto

```
carrefour-api-test-automation/
├── src/test/java/
│   ├── clients/           # ApiClient e Routes
│   ├── models/            # POJOs (UserRequest, LoginRequest)
│   ├── tests/             # Testes de API
│   └── utils/             # BaseTest, TestConfig, DataFactory, etc.
├── src/test/resources/
│   └── config/            # test.properties
├── .github/workflows/     # GitHub Actions CI/CD
├── pom.xml                # Dependências Maven
└── README.md
```

---

## 🔧 Configuração

### Variáveis de Ambiente

Configure as variáveis antes de executar os testes:

**Linux/macOS:**
```bash
export BASE_URL="https://serverest.dev"
export EMAIL="admin@qa.com"
export PASSWORD="senha123"
```

**Windows (PowerShell):**
```powershell
$env:BASE_URL="https://serverest.dev"
$env:EMAIL="admin@qa.com"
$env:PASSWORD="senha123"
```

**Valores padrão:**
- `BASE_URL`: https://serverest.dev
- `EMAIL`: admin@qa.com (criado automaticamente se não existir)
- `PASSWORD`: senha123

---

## 🚀 Como Executar

### Executar todos os testes

```bash
mvn clean test
```

### Executar testes específicos

```bash
# Por classe
mvn clean test -Dtest=UserCrudHappyPathTest

# Por método
mvn clean test -Dtest=UserCrudHappyPathTest#shouldCreateUserSuccessfully

# Por tag
mvn clean test -Dgroups="crud"
```

### Executar em ambientes diferentes

```bash
mvn clean test -DBASE_URL=https://api.staging.exemplo.com
```

---

## 📊 Relatórios Allure

### Gerar e visualizar relatório

Após executar os testes:

```bash
# 1. Executar testes (gera allure-results)
mvn clean test

# 2. Gerar e abrir relatório HTML
allure serve target/allure-results
```

O relatório será gerado e aberto automaticamente no navegador.

### Gerar relatório sem abrir

```bash
allure generate target/allure-results -o target/allure-report --clean
```

### Abrir relatório existente

```bash
allure open target/allure-report
```

### Via Maven Plugin

```bash
# Gerar relatório
mvn allure:report

# Servir relatório
mvn allure:serve
```

**Recursos do relatório:**
- Dashboard com overview dos testes
- Steps detalhados de cada requisição
- Anexos de request/response (apenas em falhas)
- Gráficos de execução
- Histórico de execuções

---

## 🔄 CI/CD - GitHub Actions

O projeto possui pipeline automatizado que executa em:
- Push para branches: `main`, `master`, `develop`
- Pull Requests para essas branches

### O que o pipeline faz

1. Setup Java 17
2. Cache de dependências Maven
3. Executa `mvn clean test`
4. Upload de `allure-results` como artifact (30 dias)
5. Gera resumo da execução

### Visualizar resultados

1. Acesse **Actions** no GitHub
2. Selecione o workflow **API Tests**
3. Baixe os artifacts para gerar relatório localmente:

```bash
# Após baixar allure-results.zip
unzip allure-results.zip -d allure-results
allure serve allure-results
```

---

## 🛠️ Stack Tecnológica

| Tecnologia | Versão | Descrição |
|-----------|---------|-----------|
| Java | 17 | Linguagem de programação |
| JUnit 5 | 5.10.1 | Framework de testes |
| RestAssured | 5.4.0 | Biblioteca para testes de API REST |
| Jackson | 2.16.1 | Serialização/Deserialização JSON |
| Allure | 2.25.0 | Framework de relatórios |
| Maven | 3.8+ | Gerenciamento de dependências |

---

## 📝 Testes Implementados

### CRUD Completo (Happy Path)
- ✅ Criar usuário
- ✅ Buscar usuário por ID
- ✅ Atualizar usuário
- ✅ Deletar usuário
- ✅ Validar usuário deletado

### Testes Negativos
- ✅ Email inválido
- ✅ Email obrigatório
- ✅ Email duplicado
- ✅ Usuário inexistente

### Autenticação
- ✅ Login com sucesso
- ✅ Login com credenciais inválidas
- ✅ Acesso sem token

---

## 📄 Licença

Este projeto é parte de um desafio técnico.

---

**Desenvolvido com ❤️ para o Carrefour**
